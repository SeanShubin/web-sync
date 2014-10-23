package com.seanshubin.web.sync.core

import java.nio.file.Path

case class DownloadResult(url: String, path: Path, hash: Option[String], time: Long, status: DownloadStatus) {
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
