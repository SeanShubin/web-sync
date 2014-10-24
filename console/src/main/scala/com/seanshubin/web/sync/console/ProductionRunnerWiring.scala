package com.seanshubin.web.sync.console

import java.nio.file.Path

import com.seanshubin.http.values.client.apache.HttpSender
import com.seanshubin.http.values.core.Sender
import com.seanshubin.web.sync.core._

trait ProductionRunnerWiring {
  def configurationFilePath: Path

  def reportPath: Path

  lazy val emit: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl()
  lazy val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl
  lazy val configurationParser: ConfigurationParser = new ConfigurationParserImpl(jsonMarshaller)
  lazy val oneWayHash: OneWayHash = new Sha256()
  lazy val sender: Sender = new HttpSender()
  lazy val notifications: Notifications = new NotificationsImpl(emit)
  lazy val downloader: Downloader = new DownloaderImpl(sender, oneWayHash, fileSystem, notifications)
  lazy val reporter: Reporter = new ReporterImpl(jsonMarshaller, fileSystem)
  lazy val errorHandler: ErrorHandler = new ErrorHandlerImpl
  lazy val runner: Runner = new RunnerImpl(
    configurationFilePath,
    fileSystem,
    configurationParser,
    downloader,
    reporter,
    errorHandler,
    notifications)
}
