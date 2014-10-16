package com.seanshubin.web.sync.core

import java.nio.file.Paths

class ConfigurationParserImpl(jsonMarshaller: JsonMarshaller) extends ConfigurationParser {
  override def parse(text: String): Seq[Download] = {
    println(text)
    val jsonConfiguration = jsonMarshaller.fromJson(text, classOf[JsonConfiguration])
    val downloads = for {
      downloadByDestination <- jsonConfiguration.downloadsByDestination
      DownloadsByDestination(destinationParts, sourceUrls) = downloadByDestination
      sourceUrl <- sourceUrls
      destinationPath = Paths.get(destinationParts.head, destinationParts.tail: _*).toString
    } yield {
      Download(sourceUrl, destinationPath)
    }
    downloads
  }
}
