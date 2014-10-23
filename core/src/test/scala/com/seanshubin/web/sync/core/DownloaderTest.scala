package com.seanshubin.web.sync.core

import java.nio.charset.StandardCharsets
import java.nio.file.{Path, Paths}

import com.seanshubin.http.values.core.{ResponseValue, RequestValue, Sender}
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class DownloaderTest extends FunSuite with EasyMockSugar {
  test("do nothing if already downloaded") {
    val sender: Sender = mock[Sender]
    val oneWayHash: OneWayHash = new Sha256
    val systemClock: SystemClock = mock[SystemClock]
    val fileSystem: FileSystem = mock[FileSystem]
    val emit = new FakeLineEmitter()
    val downloader = new DownloaderImpl(sender, oneWayHash, systemClock, fileSystem, emit)
    val remoteUrl: String = "remote url"
    val localPath: Path = Paths.get("local", "path")
    val downloads:Seq[Download] = Seq(Download(remoteUrl, localPath))
    val now = 12345
    val body = "hello".getBytes(StandardCharsets.UTF_8)
    val hash = oneWayHash.toHexString(body)
    val expectedDownloadResult = DownloadResult(
      remoteUrl, localPath, Some(hash), now, DownloadStatus.SameInLocalAndRemote)

    expecting {
      sender.send(RequestValue(remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(200, body, Map()))
      fileSystem.fileExists(localPath).andReturn(true)
      systemClock.currentTimeMillis.andReturn(now)
      fileSystem.readFileIntoBytes(localPath).andReturn(body)
    }

    whenExecuting(sender,systemClock,fileSystem) {
      val results = downloader.download(downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }

    assert(emit.lines.size === 0)
  }

  test("error if missing from both local and remote") {
    val sender: Sender = mock[Sender]
    val oneWayHash: OneWayHash = mock[OneWayHash]
    val systemClock: SystemClock = mock[SystemClock]
    val fileSystem: FileSystem = mock[FileSystem]
    val emit = new FakeLineEmitter()
    val downloader = new DownloaderImpl(sender, oneWayHash, systemClock, fileSystem, emit)
    val remoteUrl: String = "remote url"
    val localPath: Path = Paths.get("local", "path")
    val downloads:Seq[Download] = Seq(Download(remoteUrl, localPath))
    val now = 12345
    val expectedDownloadResult = DownloadResult(
      remoteUrl, localPath, hash = None, now, DownloadStatus.MissingFromLocalAndRemote)

    expecting {
      sender.send(RequestValue(remoteUrl, "get", Seq(), Map())).andReturn(ResponseValue(404, Seq(), Map()))
      fileSystem.fileExists(localPath).andReturn(false)
      systemClock.currentTimeMillis.andReturn(now)
    }

    whenExecuting(sender,oneWayHash,systemClock,fileSystem) {
      val results = downloader.download(downloads)
      assert(results.size === 1)
      assert(results(0) === expectedDownloadResult)
    }

    assert(emit.lines.size === 0)
  }
}
