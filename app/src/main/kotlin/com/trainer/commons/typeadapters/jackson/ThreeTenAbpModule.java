package com.trainer.commons.typeadapters.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Source: https://github.com/FasterXML/jackson-datatype-jsr310
 */
@SuppressWarnings("javadoc")
public final class ThreeTenAbpModule extends SimpleModule {
  private static final long serialVersionUID = 1L;

  public ThreeTenAbpModule() {
    addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
    addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);

    addDeserializer(LocalDateTime.class, new TimezonedLocalDateTimeDeserializer(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_ZONED_DATE_TIME));
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    context.addValueInstantiators(new ValueInstantiators.Base() {
      @Override
      public ValueInstantiator findValueInstantiator(DeserializationConfig config,
                                                     BeanDescription beanDesc, ValueInstantiator defaultInstantiator) {
        JavaType type = beanDesc.getType();
        Class<?> raw = type.getRawClass();

        // 15-May-2015, tatu: In theory not safe, but in practice we do need to do "fuzzy" matching
        // because we will (for now) be getting a subtype, but in future may want to downgrade
        // to the common base type. Even more, serializer may purposefully force use of base type.
        // So... in practice it really should always work, in the end. :)
        if (ZoneId.class.isAssignableFrom(raw)) {
          // let's assume we should be getting "empty" StdValueInstantiator here:
          if (defaultInstantiator instanceof StdValueInstantiator) {
            StdValueInstantiator inst = (StdValueInstantiator) defaultInstantiator;
            // one further complication: we need ZoneId info, not sub-class
            AnnotatedClass ac;
            if (raw == ZoneId.class) {
              ac = beanDesc.getClassInfo();
            } else {
              // we don't need Annotations, so constructing directly is fine here
              // even if it's not generally recommended
              ac = AnnotatedClass.construct(config.constructType(ZoneId.class), config);
            }
            if (!inst.canCreateFromString()) {
              AnnotatedMethod factory = _findFactory(ac, "of", String.class);
              if (factory != null) {
                inst.configureFromStringCreator(factory);
              }
              // otherwise... should we indicate an error?
            }
            // return ZoneIdInstantiator.construct(config, beanDesc, defaultInstantiator);
          }
        }
        return defaultInstantiator;
      }
    });
  }

  // For
  protected AnnotatedMethod _findFactory(AnnotatedClass cls, String name, Class<?>... argTypes) {
    final int argCount = argTypes.length;
    for (AnnotatedMethod method : cls.getStaticMethods()) {
      if (!name.equals(method.getName())
        || (method.getParameterCount() != argCount)) {
        continue;
      }
      for (int i = 0; i < argCount; ++i) {
        Class<?> argType = method.getParameter(i).getRawType();
        if (!argType.isAssignableFrom(argTypes[i])) {
          continue;
        }
      }
      return method;
    }
    return null;
  }
}
