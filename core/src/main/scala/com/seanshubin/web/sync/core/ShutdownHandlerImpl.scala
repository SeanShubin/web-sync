package com.seanshubin.web.sync.core

class ShutdownHandlerImpl(systemShutdown: SystemShutdown) extends ShutdownHandler {
  override def shutdown(downloadResults: Seq[DownloadResult]): Unit = {
    val errorCount = downloadResults.count(hasError)
    if (errorCount == 0) {
      systemShutdown.shutdown(0)
    } else {
      systemShutdown.shutdown(1)
    }
  }

  private def hasError(downloadResult: DownloadResult) = downloadResult.status.isError
}
