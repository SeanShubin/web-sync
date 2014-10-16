package com.seanshubin.web.sync.core

import java.nio.file.Path

class RunnerImpl(configurationFilePath: Path,
                 fileSystem: FileSystem,
                 configurationParser: ConfigurationParser,
                 downloader: Downloader,
                 reporter: Reporter,
                 shutdownHandler: ShutdownHandler) extends Runner {
  override def run(): Unit = {
    val configurationText = fileSystem.readFileIntoString(configurationFilePath)
    val downloads = configurationParser.parse(configurationText)
    val downloadResults = downloader.download(downloads)
    reporter.generateReport(downloadResults)
    shutdownHandler.shutdown(downloadResults)
  }
}
