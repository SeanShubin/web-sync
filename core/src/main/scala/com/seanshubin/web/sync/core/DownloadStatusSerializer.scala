package com.seanshubin.web.sync.core

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}

class DownloadStatusSerializer extends JsonSerializer[DownloadStatus] {
  def serialize(value: DownloadStatus, generator: JsonGenerator, provider: SerializerProvider) {
    generator.writeString(value.name)
  }
}
