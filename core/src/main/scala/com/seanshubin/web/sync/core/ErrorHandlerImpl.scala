package com.seanshubin.web.sync.core

class ErrorHandlerImpl extends ErrorHandler {
  override def shutdown(downloadResults: Seq[DownloadResult]): Unit = {
    val errorCount = downloadResults.count(hasError)
    if (errorCount != 0) {
      throw new RuntimeException("Forwarding exception due to web sync errors")
    }
  }

  private def hasError(downloadResult: DownloadResult) = downloadResult.status.isError
}
