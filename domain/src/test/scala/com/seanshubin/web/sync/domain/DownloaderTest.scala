package com.seanshubin.web.sync.domain

import java.nio.charset.StandardCharsets
import java.nio.file.{Path, Paths}

import com.seanshubin.http.values.domain.{RequestValue, ResponseValue, Sender}
import org.scalatest.FunSuite
import org.scalatest.easymock.EasyMockSugar

class DownloaderTest extends FunSuite with EasyMockSugar {
  test("already downloaded") {
    new Helper {
      lazy val expectedDownloadResult = DownloadResult(
        remoteUrl,
        localPath,
        DownloadStatus.SameInLocalAndRemote,
        localHash = Some(hash),
        remoteHash = Some(hash))

      override def expecting = () => {
        notifications.beginDownloading(downloads)
        notifications.beginSingleDownload(downloads(0))
        sender.send(RequestValue(remoteUrl, "get", Seq(), Seq())).andReturn(ResponseValue(200, body, Seq()))
        fileSystem.fileExists(localPath).andReturn(true)
        fileSystem.readFileIntoBytes(localPath).andReturn(body)
        notifications.downloadResult(expectedDownloadResult)
      }

      override def whenExecuting = () => {
        val results = downloader.download(downloads)
        assert(results.size === 1)
        assert(results(0) === expectedDownloadResult)
      }
    }
  }

  test("missing from both local and remote") {
    new Helper {
      lazy val expectedDownloadResult = DownloadResult(
        remoteUrl,
        localPath,
        DownloadStatus.MissingFromLocalAndRemote,
        localHash = None,
        remoteHash = None)

      override def expecting = () => {
        notifications.beginDownloading(downloads)
        notifications.beginSingleDownload(downloads(0))
        sender.send(RequestValue(remoteUrl, "get", Seq(), Seq())).andReturn(ResponseValue(404, Seq(), Seq()))
        fileSystem.fileExists(localPath).andReturn(false)
        notifications.downloadResult(expectedDownloadResult)
      }

      override def whenExecuting = () => {
        val results = downloader.download(downloads)
        assert(results.size === 1)
        assert(results(0) === expectedDownloadResult)
      }
    }
  }

  test("download if missing locally and exists remotely") {
    new Helper {
      lazy val expectedDownloadResult = DownloadResult(
        remoteUrl,
        localPath,
        DownloadStatus.MissingFromLocalAndPresentInRemote,
        localHash = None,
        remoteHash = Some(hash)
      )

      override def expecting = () => {
        notifications.beginDownloading(downloads)
        notifications.beginSingleDownload(downloads(0))
        sender.send(RequestValue(remoteUrl, "get", Seq(), Seq())).andReturn(ResponseValue(200, body, Seq()))
        fileSystem.fileExists(localPath).andReturn(false)
        fileSystem.createMissingDirectories(localDir)
        fileSystem.writeBytesToFile(body, localPath)
        notifications.downloadResult(expectedDownloadResult)
      }

      override def whenExecuting = () => {
        val results = downloader.download(downloads)
        assert(results.size === 1)
        assert(results(0) === expectedDownloadResult)
      }
    }
  }

  test("exists locally and missing remotely") {
    new Helper {
      lazy val expectedDownloadResult = DownloadResult(
        remoteUrl,
        localPath,
        DownloadStatus.PresentLocallyAndMissingFromRemote,
        localHash = Some(hash),
        remoteHash = None)

      override def expecting = () => {
        notifications.beginDownloading(downloads)
        notifications.beginSingleDownload(downloads(0))
        sender.send(RequestValue(remoteUrl, "get", Seq(), Seq())).andReturn(ResponseValue(404, body, Seq()))
        fileSystem.fileExists(localPath).andReturn(true)
        fileSystem.readFileIntoBytes(localPath).andReturn(body)
        notifications.downloadResult(expectedDownloadResult)
      }

      override def whenExecuting = () => {
        val results = downloader.download(downloads)
        assert(results.size === 1)
        assert(results(0) === expectedDownloadResult)
      }
    }
  }

  test("files different") {
    new Helper {
      lazy val expectedDownloadResult = DownloadResult(
        remoteUrl,
        localPath,
        DownloadStatus.DifferentInLocalAndRemote,
        localHash = Some(hash),
        remoteHash = Some(differentHash))

      override def expecting = () => {
        notifications.beginDownloading(downloads)
        notifications.beginSingleDownload(downloads(0))
        sender.send(RequestValue(remoteUrl, "get", Seq(), Seq())).andReturn(ResponseValue(200, differentBody, Seq()))
        fileSystem.fileExists(localPath).andReturn(true)
        fileSystem.readFileIntoBytes(localPath).andReturn(body)
        fileSystem.createMissingDirectories(localDir)
        fileSystem.writeBytesToFile(differentBody, localPath)
        notifications.downloadResult(expectedDownloadResult)
      }

      override def whenExecuting = () => {
        val results = downloader.download(downloads)
        assert(results.size === 1)
        assert(results(0) === expectedDownloadResult)
      }
    }
  }

  trait Helper {
    lazy val sender: Sender = mock[Sender]
    lazy val oneWayHash: OneWayHash = new Sha256
    lazy val fileSystem: FileSystem = mock[FileSystem]
    lazy val notifications: Notifications = mock[Notifications]
    lazy val downloader = new DownloaderImpl(sender, oneWayHash, fileSystem, notifications)
    lazy val remoteUrl: String = "remote-url"
    lazy val localPath: Path = Paths.get("local", "path")
    lazy val localDir: Path = localPath.getParent
    lazy val downloads: Seq[Download] = Seq(Download(remoteUrl, localPath))
    lazy val body = "hello".getBytes(StandardCharsets.UTF_8)
    lazy val differentBody = "goodbye".getBytes(StandardCharsets.UTF_8)
    lazy val hash = oneWayHash.toHexString(body)
    lazy val differentHash = oneWayHash.toHexString(differentBody)

    def expecting: () => Unit

    def whenExecuting: () => Unit

    expecting()
    EasyMockSugar.whenExecuting(sender, fileSystem, notifications) {
      whenExecuting()
    }
  }

}
