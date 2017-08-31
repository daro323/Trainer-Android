package com.trainer.commons

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import com.trainer.modules.training.plan.TrainingPlanApi
import com.trainer.modules.training.workout.SerieType


/**
 * Created by dariusz on 31.08.17.
 */
class SerieResponseDeserializer : JsonDeserializer<TrainingPlanApi.SerieResponse>() {

  override fun deserialize(parser: JsonParser, context: DeserializationContext): TrainingPlanApi.SerieResponse {
    val mapper = parser.codec as ObjectMapper
    val obj = mapper.readTree<TreeNode>(parser)
    val objType = (obj.get("type") as TextNode).asText().run { SerieType.valueOf(this) }

    val response = when (objType) {
      SerieType.SET -> mapper.treeToValue(obj, TrainingPlanApi.SetResponse::class.java)
      SerieType.SUPER_SET -> mapper.treeToValue(obj, TrainingPlanApi.SuperSetResponse::class.java)
      SerieType.CYCLE -> mapper.treeToValue(obj, TrainingPlanApi.CycleResponse::class.java)
    }
    return response
  }
}