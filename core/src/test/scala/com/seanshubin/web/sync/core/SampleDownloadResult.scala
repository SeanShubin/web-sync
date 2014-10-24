package com.seanshubin.web.sync.core

import java.nio.file.Paths

object SampleDownloadResult {
  val url = "some url"
  val downloadPath = Paths.get("download", "path")
  val time = 12345
  val hash = Some("aaa")
  val differentHash = Some("bbb")
  val downloadResultSame = DownloadResult(
    url, downloadPath, time, DownloadStatus.SameInLocalAndRemote, hash, hash)
  val downloadResultDifferent = DownloadResult(
    url, downloadPath, time, DownloadStatus.DifferentInLocalAndRemote, hash, differentHash)
}
