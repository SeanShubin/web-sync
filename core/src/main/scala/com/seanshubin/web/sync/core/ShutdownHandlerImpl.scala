package com.seanshubin.web.sync.core

class ShutdownHandlerImpl(systemShutdown: SystemShutdown, emit: String => Unit) extends ShutdownHandler {
  override def shutdown(downloadResults: Seq[DownloadResult]): Unit = {
    val errorCount = downloadResults.count(hasError)
    if (errorCount == 0) {
      systemShutdown.shutdown(0)
    } else {
      emit("There were errors during web synchronization")
      val errorLines = downloadResults.flatMap(_.errorStrings)
      errorLines.foreach(emit)
      systemShutdown.shutdown(1)
    }
  }

  private def hasError(downloadResult: DownloadResult) = downloadResult.status.isError
}
