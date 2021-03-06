package com.seanshubin.web.sync.console

import java.nio.file.Path

import com.seanshubin.http.values.client.google.HttpSender
import com.seanshubin.http.values.domain.Sender
import com.seanshubin.web.sync.domain._

trait ProductionRunnerWiring {
  def configurationFilePath: Path

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
  lazy val clock: Clock = new ClockImpl
  lazy val logger: LoggerImpl = new LoggerImpl(fileSystem, clock)
  lazy val runner: Runner = new RunnerImpl(
    configurationFilePath,
    fileSystem,
    configurationParser,
    downloader,
    reporter,
    errorHandler,
    notifications,
    logger
  )
}
