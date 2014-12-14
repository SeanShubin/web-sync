package com.seanshubin.web.sync.core

import java.nio.file.Path

class RunnerImpl(configurationFilePath: Path,
                 fileSystem: FileSystem,
                 configurationParser: ConfigurationParser,
                 downloader: Downloader,
                 reporter: Reporter,
                 errorHandler: ErrorHandler,
                 notifications: Notifications,
                 logger: Logger) extends Runner {
  override def run(): Unit = {
    val configurationText = fileSystem.readFileIntoString(configurationFilePath)
    val Configuration(reportPath, logPath, downloads) = configurationParser.parse(configurationText)
    val downloadResults = downloader.download(downloads)
    reporter.generateReport(reportPath, downloadResults)
    notifications.summary(downloadResults)
    logger.summary(logPath, downloadResults)
    errorHandler.shutdown(downloadResults)
  }
}
