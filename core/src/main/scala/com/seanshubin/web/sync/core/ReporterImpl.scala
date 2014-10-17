package com.seanshubin.web.sync.core

import java.nio.file.Path

class ReporterImpl(reportPath: Path, jsonMarshaller: JsonMarshaller, fileSystem: FileSystem) extends Reporter {
  override def generateReport(downloadResults: Seq[DownloadResult]): Unit = {
    val json = jsonMarshaller.toJson(downloadResults)
    fileSystem.createMissingDirectories(reportPath.getParent)
    fileSystem.writeStringToFile(json, reportPath)
  }
}
