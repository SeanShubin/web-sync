package com.seanshubin.web.sync.domain

import java.nio.file.Path

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}

class PathSerializer extends JsonSerializer[Path] {
  def serialize(value: Path, generator: JsonGenerator, provider: SerializerProvider) {
    generator.writeString(value.toString)
  }
}
