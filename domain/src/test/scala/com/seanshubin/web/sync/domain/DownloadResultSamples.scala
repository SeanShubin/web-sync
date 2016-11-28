package com.seanshubin.web.sync.domain

import java.nio.file.Paths

object DownloadResultSamples {
  val url = "http://some.url"
  val downloadPath = Paths.get("download", "path")
  val hash = Some("aaa")
  val differentHash = Some("bbb")
  val downloadResultSame = DownloadResult(
    url, downloadPath, DownloadStatus.SameInLocalAndRemote, hash, hash)
  val downloadResultDifferent = DownloadResult(
    url, downloadPath, DownloadStatus.DifferentInLocalAndRemote, hash, differentHash)
  val downloadResultMissingBoth = DownloadResult(
    url, downloadPath, DownloadStatus.MissingFromLocalAndRemote, None, None)
  val bunchOfDownloadResults = Seq(downloadResultSame, downloadResultDifferent)
}
