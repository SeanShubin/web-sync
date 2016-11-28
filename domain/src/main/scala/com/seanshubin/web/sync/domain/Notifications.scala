package com.seanshubin.web.sync.domain

trait Notifications {
  def beginDownloading(downloads: Seq[Download])

  def beginSingleDownload(download: Download)

  def downloadResult(downloadResult: DownloadResult)

  def summary(downloadResults: Seq[DownloadResult])
}
