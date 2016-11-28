package com.seanshubin.web.sync.domain

import java.nio.file.Path

trait Reporter {
  def generateReport(reportPath: Path, downloadResults: Seq[DownloadResult])
}
