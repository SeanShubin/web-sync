package com.seanshubin.web.sync.domain

import java.nio.file.Path

trait Logger {
  def summary(logPath: Path, downloadResults: Seq[DownloadResult])
}
