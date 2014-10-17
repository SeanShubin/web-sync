package com.seanshubin.web.sync.core

import java.nio.file.Paths

class ConfigurationParserImpl(jsonMarshaller: JsonMarshaller) extends ConfigurationParser {
  override def parse(text: String): Seq[Download] = {
    val jsonConfiguration = jsonMarshaller.fromJson(text, classOf[JsonConfiguration])
    val downloads = for {
      downloadByDestination <- jsonConfiguration.downloadsByDestination
      DownloadsByDestination(destinationParts, sourceUrls) = downloadByDestination
      sourceUrl <- sourceUrls
      download = composeDownload(destinationParts, sourceUrl)
    } yield {
      download
    }
    downloads
  }

  def composeDownload(destinationParts: Seq[String], sourceUrl: String): Download = {
    val destinationDir = Paths.get(destinationParts.head, destinationParts.tail: _*)
    val name = splitNameFromUrl(sourceUrl)
    val destinationPath = destinationDir.resolve(name)
    val destinationPathString = destinationPath.toString
    Download(sourceUrl, destinationPathString)
  }

  def splitNameFromUrl(url: String): String = {
    val lastForwardSlash = url.lastIndexOf('/')
    val name = url.substring(lastForwardSlash + 1)
    name
  }
}
