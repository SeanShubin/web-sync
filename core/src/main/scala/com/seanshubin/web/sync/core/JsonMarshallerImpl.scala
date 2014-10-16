package com.seanshubin.web.sync.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class JsonMarshallerImpl extends JsonMarshaller {
  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.configure(SerializationFeature.INDENT_OUTPUT, true)

  override def fromJson[T](json: String, theClass: Class[T]): T = {
    try {
      val parsed = mapper.readValue(json, theClass)
      parsed
    } catch {
      case ex: JsonParseException =>
        val escapedJson = StringUtil.doubleQuote(json)
        throw new RuntimeException(s"Error while attempting to parse $escapedJson: ${ex.getMessage}", ex)
    }
  }
}
