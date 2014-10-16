package com.seanshubin.web.sync.core

import java.nio.file.Paths

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
    val runner: Runner = new RunnerImpl(configurationLocation, fileSystem, configurationParser, downloader, reporter, shutdownHandler)
    val downloads: Seq[Download] = Seq(Download("foo1", "bar1"), Download("foo2", "bar2"))
    val downloadResults: String = "download results"
    expecting {
      fileSystem.readFileIntoString(configurationLocation).andReturn(configurationText)
      configurationParser.parse(configurationText).andReturn(downloads)
      downloader.download(downloads).andReturn(downloadResults)
      reporter.generateReport(downloadResults)
      shutdownHandler.shutdown(downloadResults)
    }
    whenExecuting(fileSystem, configurationParser, downloader, reporter, shutdownHandler) {
      runner.run()
    }
  }
}
