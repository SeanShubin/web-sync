package com.seanshubin.web.sync.core

import java.nio.file.Path

trait Reporter {
  def generateReport(reportPath: Path, downloadResults: Seq[DownloadResult])
}
