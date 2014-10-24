package com.seanshubin.web.sync.core

trait ErrorHandler {
  def shutdown(downloadResults: Seq[DownloadResult])
}
