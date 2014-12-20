package com.seanshubin.web.sync.core

import java.nio.file.Path

trait Logger {
  def summary(logPath: Path, downloadResults: Seq[DownloadResult])
}
