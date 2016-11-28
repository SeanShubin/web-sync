package com.seanshubin.web.sync.domain

import java.nio.file.Paths
import java.time.ZonedDateTime

import org.scalatest.FunSuite
import org.scalatest.easymock.EasyMockSugar

class LoggerTest extends FunSuite with EasyMockSugar {
  test("all good") {
    val fileSystem = mock[FileSystem]
    val clock = mock[Clock]
    val logger = new LoggerImpl(fileSystem, clock)
    val downloadResult1 = DownloadResult(
      "url1", Paths.get("path1"), DownloadStatus.SameInLocalAndRemote, Some("hash1"), Some("hash1"))
    val downloadResult2 = DownloadResult(
      "url2", Paths.get("path2"), DownloadStatus.SameInLocalAndRemote, Some("hash2"), Some("hash2"))
    val downloadResults = Seq(downloadResult1, downloadResult2)
    val logPath = Paths.get("log")

    val now = ZonedDateTime.parse("2014-12-14T01:40:31.479Z")

    expecting {
      clock.zonedDateTimeNow().andReturn(now)
      fileSystem.appendLine(logPath, "2014-12-14T01:40:31.479Z - all files in sync with web")
    }

    whenExecuting(fileSystem, clock) {
      logger.summary(logPath, downloadResults)
    }
  }

  test("missing from local and remote") {
    val fileSystem = mock[FileSystem]
    val clock = mock[Clock]
    val logger = new LoggerImpl(fileSystem, clock)
    val downloadResult1 = DownloadResult(
      "url1", Paths.get("path1"), DownloadStatus.SameInLocalAndRemote, Some("hash1"), Some("hash1"))
    val downloadResult2 = DownloadResult(
      "url2", Paths.get("path2"), DownloadStatus.MissingFromLocalAndRemote, None, None)
    val downloadResults = Seq(downloadResult1, downloadResult2)
    val logPath = Paths.get("log")

    val now = ZonedDateTime.parse("2014-12-14T01:40:31.479Z")

    expecting {
      clock.zonedDateTimeNow().andReturn(now)
      fileSystem.appendLine(logPath, "2014-12-14T01:40:31.479Z - sync failed")
      fileSystem.appendLine(logPath, "  missing from local and remote: url2 -> path2")
    }

    whenExecuting(fileSystem, clock) {
      logger.summary(logPath, downloadResults)
    }
  }
}
