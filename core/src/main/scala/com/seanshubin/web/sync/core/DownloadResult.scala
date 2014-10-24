package com.seanshubin.web.sync.core

import java.nio.file.Path

case class DownloadResult(url: String,
                          path: Path,
                          status: DownloadStatus,
                          localHash: Option[String],
                          remoteHash: Option[String]) {
  def errorStrings: Seq[String] =
    if (status.isError) Seq(
      s"url=$url",
      s"path=$path",
      s"status=${status.description}",
      s"localHash=$localHash",
      s"remoteHash=$remoteHash"
    )
    else Seq()
}
