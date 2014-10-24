package com.seanshubin.web.sync.core

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val configurationLocation = Paths.get("sample-config.json")
    val configurationText = "configuration text"
    val fileSystem: FileSystem = mock[FileSystem]
    val configurationParser: ConfigurationParser = mock[ConfigurationParser]
    val downloader: Downloader = mock[Downloader]
    val reporter: Reporter = mock[Reporter]
    val shutdownHandler: ShutdownHandler = mock[ShutdownHandler]
    val notifications: Notifications = mock[Notifications]
    val runner: Runner = new RunnerImpl(configurationLocation, fileSystem, configurationParser, downloader, reporter, shutdownHandler, notifications)
    val reportPath: Path = Paths.get("foo")
    val bar1: Path = Paths.get("bar1")
    val bar2: Path = Paths.get("bar2")
    val downloads: Seq[Download] = DownloadSamples.bunchOfDownloads
    val configuration: Configuration = Configuration(reportPath, downloads)
    val downloadResults: Seq[DownloadResult] = DownloadResultSamples.bunchOfDownloadResults
    expecting {
      fileSystem.readFileIntoString(configurationLocation).andReturn(configurationText)
      configurationParser.parse(configurationText).andReturn(configuration)
      downloader.download(downloads).andReturn(downloadResults)
      reporter.generateReport(reportPath, downloadResults)
      notifications.summary(downloadResults)
      shutdownHandler.shutdown(downloadResults)
    }
    whenExecuting(fileSystem, configurationParser, downloader, reporter, shutdownHandler, notifications) {
      runner.run()
    }
  }
}
