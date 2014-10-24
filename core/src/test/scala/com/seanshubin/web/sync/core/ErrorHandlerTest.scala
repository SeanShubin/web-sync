package com.seanshubin.web.sync.core

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ErrorHandlerTest extends FunSuite with EasyMockSugar {
  test("do nothing if no errors") {
    val errorHandler: ErrorHandler = new ErrorHandlerImpl
    val downloadResults: Seq[DownloadResult] = Seq(DownloadResultSamples.downloadResultSame)
    errorHandler.shutdown(downloadResults)
  }

  test("propagate exception if errors") {
    val errorHandler: ErrorHandler = new ErrorHandlerImpl
    val downloadResults: Seq[DownloadResult] = Seq(DownloadResultSamples.downloadResultDifferent)
    val thrown = intercept[RuntimeException] {
      errorHandler.shutdown(downloadResults)
    }
    assert(thrown.getMessage === "Forwarding exception due to web sync errors")
  }
}
