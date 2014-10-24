package com.seanshubin.web.sync.core

import java.nio.file.Path

case class DownloadResult(url: String,
                          path: Path,
                          status: DownloadStatus,
                          localHash: Option[String],
                          remoteHash: Option[String])
