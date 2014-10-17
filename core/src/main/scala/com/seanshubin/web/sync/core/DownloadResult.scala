package com.seanshubin.web.sync.core

case class DownloadResult(url: String, path: String, hash: Option[String], time: Long, status: DownloadStatus) {
  def errorStrings: Seq[String] =
    if (status.isError) Seq(
      s"url=$url",
      s"path=$path",
      s"hash=$hash",
      s"time=$time",
      s"status=${status.description}"
    )
    else Seq()
}
