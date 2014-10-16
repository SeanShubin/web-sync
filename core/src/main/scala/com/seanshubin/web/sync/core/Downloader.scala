package com.seanshubin.web.sync.core

trait Downloader {
  def download(downloads: Seq[Download]): String
}
