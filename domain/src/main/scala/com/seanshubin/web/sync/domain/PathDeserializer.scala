package com.seanshubin.web.sync.domain

import java.nio.file.{Path, Paths}

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}

class PathDeserializer extends JsonDeserializer[Path] {
  def deserialize(parser: JsonParser, context: DeserializationContext) = {
    Paths.get(parser.getValueAsString)
  }
}
