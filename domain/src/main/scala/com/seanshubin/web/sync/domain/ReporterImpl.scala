package com.seanshubin.web.sync.domain

import java.nio.file.Path

class ReporterImpl(jsonMarshaller: JsonMarshaller, fileSystem: FileSystem) extends Reporter {
  override def generateReport(reportPath: Path, downloadResults: Seq[DownloadResult]): Unit = {
    val json = jsonMarshaller.toJson(downloadResults)
    fileSystem.createMissingDirectories(reportPath.getParent)
    fileSystem.writeStringToFile(json, reportPath)
  }
}
