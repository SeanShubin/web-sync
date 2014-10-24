package com.seanshubin.web.sync.core

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ShutdownHandlerTest extends FunSuite with EasyMockSugar {
  test("exit code is zero if no errors") {
    val emit: String => Unit = new FakeLineEmitter
    val systemShutdown: SystemShutdown = mock[SystemShutdown]
    val shutdownHandler: ShutdownHandler = new ShutdownHandlerImpl(systemShutdown, emit)
    val downloadResults: Seq[DownloadResult] = Seq(SampleDownloadResult.downloadResultSame)

    expecting {
      systemShutdown.shutdown(0)
    }

    whenExecuting(systemShutdown) {
      shutdownHandler.shutdown(downloadResults)
    }
  }

  test("exit code is not zero if errors") {
    val emit: String => Unit = new FakeLineEmitter
    val systemShutdown: SystemShutdown = mock[SystemShutdown]
    val shutdownHandler: ShutdownHandler = new ShutdownHandlerImpl(systemShutdown, emit)
    val downloadResults: Seq[DownloadResult] = Seq(SampleDownloadResult.downloadResultDifferent)

    expecting {
      systemShutdown.shutdown(1)
    }

    whenExecuting(systemShutdown) {
      shutdownHandler.shutdown(downloadResults)
    }
  }
}
