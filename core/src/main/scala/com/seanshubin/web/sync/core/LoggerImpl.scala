package com.seanshubin.web.sync.core

import java.nio.file.Path

class LoggerImpl(fileSystem: FileSystem, clock: Clock) extends Logger {
  override def summary(logPath: Path, downloadResults: Seq[DownloadResult]): Unit = {
    val now = clock.zonedDateTimeNow()
    val isError = downloadResults.exists(_.status.isError)
    def emitLine(line: String): Unit = {
      fileSystem.appendLine(logPath, line)
    }
    val message = if (isError) "sync failed" else "all files in sync with web"
    emitLine(s"$now - $message")
    val lines = downloadResults.flatMap(downloadResultToLines)
    lines.foreach(emitLine)
  }

  private def downloadResultToLines(downloadResult: DownloadResult): Seq[String] = {
    if (downloadResult.status.shouldLog) Seq(s"  ${downloadResult.status.description}: ${downloadResult.url} -> ${downloadResult.path}")
    else Seq()
  }
}
