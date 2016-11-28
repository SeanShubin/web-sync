package com.seanshubin.web.sync.domain

trait Downloader {
  def download(downloads: Seq[Download]): Seq[DownloadResult]
}
