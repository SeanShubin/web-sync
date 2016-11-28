package com.seanshubin.web.sync.domain

trait ErrorHandler {
  def shutdown(downloadResults: Seq[DownloadResult])
}
