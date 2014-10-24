package com.seanshubin.web.sync.core

import java.nio.charset.StandardCharsets
import java.nio.file.{Path, Paths}

import com.seanshubin.http.values.core.{RequestValue, ResponseValue, Sender}
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class DownloaderTest extends FunSuite with EasyMockSugar {
  test("already downloaded") {
    val helper = new Helper
    val expectedDownloadResult = DownloadResult(
      helper.remoteUrl,
      helper.localPath,
      DownloadStatus.SameInLocalAndRemote,
      localHash = Some(helper.hash),
      remoteHash = Some(helper.hash))

    expecting {
      helper.notifications.beginDownloading(helper.downloads)
      helper.sender.send(RequestValue(helper.remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(200, helper.body, Map()))
      helper.fileSystem.fileExists(helper.localPath).andReturn(true)
      helper.fileSystem.readFileIntoBytes(helper.localPath).andReturn(helper.body)
      helper.notifications.downloadResult(expectedDownloadResult)
    }

    whenExecuting(helper.mocks: _*) {
      val results = helper.downloader.download(helper.downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }
  }

  test("missing from both local and remote") {
    val helper = new Helper
    val expectedDownloadResult = DownloadResult(
      helper.remoteUrl,
      helper.localPath,
      DownloadStatus.MissingFromLocalAndRemote,
      localHash = None,
      remoteHash = None)

    expecting {
      helper.notifications.beginDownloading(helper.downloads)
      helper.sender.send(RequestValue(helper.remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(404, Seq(), Map()))
      helper.fileSystem.fileExists(helper.localPath).andReturn(false)
      helper.notifications.downloadResult(expectedDownloadResult)
    }

    whenExecuting(helper.mocks: _*) {
      val results = helper.downloader.download(helper.downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }
  }

  test("download if missing locally and exists remotely") {
    val helper = new Helper
    val expectedDownloadResult = DownloadResult(
      helper.remoteUrl,
      helper.localPath,
      DownloadStatus.MissingFromLocalAndPresentInRemote,
      localHash = None,
      remoteHash = Some(helper.hash)
    )

    expecting {
      helper.notifications.beginDownloading(helper.downloads)
      helper.sender.send(RequestValue(helper.remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(200, helper.body, Map()))
      helper.fileSystem.fileExists(helper.localPath).andReturn(false)
      helper.fileSystem.createMissingDirectories(helper.localDir)
      helper.fileSystem.writeBytesToFile(helper.body, helper.localPath)
      helper.notifications.downloadResult(expectedDownloadResult)
    }

    whenExecuting(helper.mocks: _*) {
      val results = helper.downloader.download(helper.downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }
  }

  test("exists locally and missing remotely") {
    val helper = new Helper
    val expectedDownloadResult = DownloadResult(
      helper.remoteUrl,
      helper.localPath,
      DownloadStatus.PresentLocallyAndMissingFromRemote,
      localHash = Some(helper.hash),
      remoteHash = None)

    expecting {
      helper.notifications.beginDownloading(helper.downloads)
      helper.sender.send(RequestValue(helper.remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(404, helper.body, Map()))
      helper.fileSystem.fileExists(helper.localPath).andReturn(true)
      helper.fileSystem.readFileIntoBytes(helper.localPath).andReturn(helper.body)
      helper.notifications.downloadResult(expectedDownloadResult)
    }

    whenExecuting(helper.mocks: _*) {
      val results = helper.downloader.download(helper.downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }
  }

  test("files different") {
    val helper = new Helper
    val expectedDownloadResult = DownloadResult(
      helper.remoteUrl,
      helper.localPath,
      DownloadStatus.DifferentInLocalAndRemote,
      localHash = Some(helper.hash),
      remoteHash = Some(helper.differentHash))

    expecting {
      helper.notifications.beginDownloading(helper.downloads)
      helper.sender.send(RequestValue(helper.remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(200, helper.differentBody, Map()))
      helper.fileSystem.fileExists(helper.localPath).andReturn(true)
      helper.fileSystem.readFileIntoBytes(helper.localPath).andReturn(helper.body)
      helper.notifications.downloadResult(expectedDownloadResult)
    }

    whenExecuting(helper.mocks: _*) {
      val results = helper.downloader.download(helper.downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }
  }

  class Helper {
    val sender: Sender = mock[Sender]
    val oneWayHash: OneWayHash = new Sha256
    val fileSystem: FileSystem = mock[FileSystem]
    val notifications: Notifications = mock[Notifications]
    val downloader = new DownloaderImpl(sender, oneWayHash, fileSystem, notifications)
    val remoteUrl: String = "remote url"
    val localPath: Path = Paths.get("local", "path")
    val localDir: Path = localPath.getParent
    val downloads: Seq[Download] = Seq(Download(remoteUrl, localPath))
    val body = "hello".getBytes(StandardCharsets.UTF_8)
    val differentBody = "goodbye".getBytes(StandardCharsets.UTF_8)
    val hash = oneWayHash.toHexString(body)
    val differentHash = oneWayHash.toHexString(differentBody)
    val mocks = Seq(sender, fileSystem, notifications)
  }

}
