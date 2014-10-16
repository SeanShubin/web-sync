package com.seanshubin.web.sync.console

import java.nio.file.Path

import com.seanshubin.web.sync.core._

trait ProductionRunnerWiring {
  def configurationFilePath: Path

  lazy val fileSystem: FileSystem = new FileSystemImpl()
  lazy val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl
  lazy val configurationParser: ConfigurationParser = new ConfigurationParserImpl(jsonMarshaller)
  lazy val downloader: Downloader = new DownloaderImpl()
  lazy val reporter: Reporter = new ReporterImpl()
  lazy val shutdownHandler: ShutdownHandler = new ShutdownHandlerImpl()
  lazy val runner: Runner = new RunnerImpl(
    configurationFilePath,
    fileSystem,
    configurationParser,
    downloader,
    reporter,
    shutdownHandler)
}
