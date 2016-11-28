package com.seanshubin.web.sync.domain

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}

class DownloadStatusDeserializer extends JsonDeserializer[DownloadStatus] {
  def deserialize(parser: JsonParser, context: DeserializationContext) = {
    DownloadStatus.fromString(parser.getValueAsString) match {
      case Some(downloadStatus) => downloadStatus
      case None => throw new RuntimeException(
        s"Cannot convert '${parser.getValueAsString}' to a DownloadStatus, expected one of: ${DownloadStatus.validValuesString}")
    }
  }
}
