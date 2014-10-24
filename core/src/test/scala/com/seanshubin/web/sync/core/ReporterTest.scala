package com.seanshubin.web.sync.core

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ReporterTest extends FunSuite with EasyMockSugar {
  test("generate report") {
    val jsonMarshaller: JsonMarshaller = mock[JsonMarshaller]
    val fileSystem: FileSystem = mock[FileSystem]
    val reporter: Reporter = new ReporterImpl(jsonMarshaller, fileSystem)

    val reportPath = Paths.get("report", "path")
    val downloadPath = Paths.get("download", "path")
    val url = "some url"
    val time = 12345
    val status = DownloadStatus.DifferentInLocalAndRemote
    val localHash = Some("aaa")
    val remoteHash = Some("bbb")
    val downloadResult = DownloadResult(url, downloadPath, time, status, localHash, remoteHash)
    val downloadResults = Seq(downloadResult)
    val jsonResult = "json result"

    expecting {
      jsonMarshaller.toJson(downloadResults).andReturn(jsonResult)
      fileSystem.createMissingDirectories(reportPath.getParent)
      fileSystem.writeStringToFile(jsonResult, reportPath)
    }

    whenExecuting(jsonMarshaller, fileSystem) {
      reporter.generateReport(reportPath, downloadResults)
    }
  }
}
