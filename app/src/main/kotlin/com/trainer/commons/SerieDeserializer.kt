package com.trainer.commons

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import com.trainer.modules.training.workout.Serie
import com.trainer.modules.training.workout.SerieType
import com.trainer.modules.training.workout.types.cyclic.Cycle
import com.trainer.modules.training.workout.types.standard.Set
import com.trainer.modules.training.workout.types.standard.SuperSet


/**
 * Created by dariusz on 31.08.17.
 */
class SerieDeserializer : JsonDeserializer<Serie>() {

  override fun deserialize(parser: JsonParser, context: DeserializationContext): Serie {
    val mapper = parser.codec as ObjectMapper
    val obj = mapper.readTree<TreeNode>(parser)
    val objType = (obj.get("type") as TextNode).asText().run { SerieType.valueOf(this) }

    val response = when (objType) {
      SerieType.SET -> mapper.treeToValue(obj, Set::class.java)
      SerieType.SUPER_SET -> mapper.treeToValue(obj, SuperSet::class.java)
      SerieType.CYCLE -> mapper.treeToValue(obj, Cycle::class.java)
    }
    return response
  }
}