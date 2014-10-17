package com.seanshubin.web.sync.core

trait Reporter {
  def generateReport(downloadResults: Seq[DownloadResult])
}
