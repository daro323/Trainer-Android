# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/dariusz/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# OkHttp
-dontwarn okhttp3.**

# Picasso
-dontwarn com.squareup.okhttp.**

# Jackson
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-dontnote com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }
-dontnote org.codehaus.**

-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *;
}

# RxJava
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

-ignorewarnings

# Kotlin
-keep class kotlin.** { *;}
-dontnote kotlin.**

# Android support libraries
-dontwarn android.support.**
-dontnote android.support.**

# Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn okio.**

# OkHttp 3
-dontwarn okhttp3.**

# Other
-keep @android.support.annotation.Keep class * {*;}