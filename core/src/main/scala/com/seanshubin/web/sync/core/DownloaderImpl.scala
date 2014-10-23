package com.seanshubin.web.sync.core

import java.nio.file.Path

import com.seanshubin.http.values.core.{RequestValue, ResponseValue, Sender}

class DownloaderImpl(sender: Sender,
                     oneWayHash: OneWayHash,
                     systemClock: SystemClock,
                     fileSystem: FileSystem,
                     emit: String => Unit) extends Downloader {
  override def download(downloads: Seq[Download]): Seq[DownloadResult] = {
    downloads.map(singleDownload)
  }

  private def singleDownload(download: Download): DownloadResult = {
    val request = RequestValue(download.url, "get", Seq(), Map())
    val response = sender.send(request)
    val remoteExists = ResponseValue.isSuccess(response.statusCode)
    val localExists = fileSystem.fileExists(download.path)
    val time = systemClock.currentTimeMillis
    (remoteExists, localExists) match {
      case (true, true) => handleBothExist(download.url, download.path, response.body, time)
      case (true, false) => doDownload(download.url, response.body, download.path, time)
      case (false, true) => errorRemoteHasGoneMissing(download.url, download.path, time)
      case (false, false) => errorNotFoundAnywhere(download.url, download.path, time)
    }
  }

  private def handleBothExist(url: String, localPath: Path, remoteBytes: Seq[Byte], time: Long): DownloadResult = {
    val localBytes = fileSystem.readFileIntoBytes(localPath)
    val localHash = oneWayHash.toHexString(localBytes)
    val remoteHash = oneWayHash.toHexString(remoteBytes)
    if (localHash == remoteHash) doNothing(url, localPath, localHash, remoteHash, time)
    else errorFilesDifferent(url, localPath, localHash, remoteHash, time)
  }

  private def doDownload(url: String, remoteBytes: Seq[Byte], localPath: Path, time: Long): DownloadResult = {
    fileSystem.createMissingDirectories(localPath.getParent)
    fileSystem.writeBytesToFile(remoteBytes, localPath)
    val hash = oneWayHash.toHexString(remoteBytes)
    emit(s"Downloading $url -> $localPath")
    DownloadResult(
      url,
      localPath,
      time,
      DownloadStatus.MissingFromLocalAndPresentInRemote,
      localHash = None,
      remoteHash = Some(hash))
  }

  private def errorRemoteHasGoneMissing(url: String, path: Path, time: Long): DownloadResult = {
    val localBytes = fileSystem.readFileIntoBytes(path)
    val localHash = oneWayHash.toHexString(localBytes)
    DownloadResult(
      url,
      path,
      time,
      DownloadStatus.PresentLocallyAndMissingFromRemote,
      localHash = Some(localHash),
      remoteHash = None)
  }

  private def errorNotFoundAnywhere(url: String, path: Path, time: Long): DownloadResult = {
    DownloadResult(
      url,
      path,
      time,
      DownloadStatus.MissingFromLocalAndRemote,
      localHash = None,
      remoteHash = None)
  }

  private def doNothing(url: String, path: Path, localHash: String, remoteHash: String, time: Long): DownloadResult = {
    DownloadResult(url, path, time, DownloadStatus.SameInLocalAndRemote, Some(localHash), Some(remoteHash))
  }

  private def errorFilesDifferent(url: String, path: Path, localHash: String, remoteHash: String, time: Long): DownloadResult = {
    DownloadResult(url, path, time, DownloadStatus.DifferentInLocalAndRemote, Some(localHash), Some(remoteHash))
  }
}
