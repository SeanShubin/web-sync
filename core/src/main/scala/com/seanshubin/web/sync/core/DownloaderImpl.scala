package com.seanshubin.web.sync.core

import java.nio.file.Path

import com.seanshubin.http.values.core.{RequestValue, ResponseValue, Sender}

class DownloaderImpl(sender: Sender,
                     oneWayHash: OneWayHash,
                     fileSystem: FileSystem,
                     notifications: Notifications) extends Downloader {
  override def download(downloads: Seq[Download]): Seq[DownloadResult] = {
    notifications.beginDownloading(downloads)
    downloads.map(singleDownload)
  }

  private def singleDownload(download: Download): DownloadResult = {
    val request = RequestValue(download.url, "get", Seq(), Map())
    val response = sender.send(request)
    val remoteExists = ResponseValue.isSuccess(response.statusCode)
    val localExists = fileSystem.fileExists(download.path)
    val downloadResult = (remoteExists, localExists) match {
      case (true, true) => handleBothExist(download.url, download.path, response.body)
      case (true, false) => handleDownloadMissing(download.url, response.body, download.path)
      case (false, true) => remoteHasGoneMissing(download.url, download.path)
      case (false, false) => notFoundAnywhere(download.url, download.path)
    }
    notifications.downloadResult(downloadResult)
    downloadResult
  }

  private def handleBothExist(url: String, localPath: Path, remoteBytes: Seq[Byte]): DownloadResult = {
    val localBytes = fileSystem.readFileIntoBytes(localPath)
    val localHash = oneWayHash.toHexString(localBytes)
    val remoteHash = oneWayHash.toHexString(remoteBytes)
    if (localHash == remoteHash) doNothing(url, localPath, localHash, remoteHash)
    else handleFilesDifferent(url, localPath, localHash, remoteHash, remoteBytes)
  }

  private def doDownload(url: String, remoteBytes: Seq[Byte], localPath: Path): Unit = {
    fileSystem.createMissingDirectories(localPath.getParent)
    fileSystem.writeBytesToFile(remoteBytes, localPath)
  }

  private def handleDownloadMissing(url: String, remoteBytes: Seq[Byte], localPath: Path): DownloadResult = {
    doDownload(url, remoteBytes, localPath)
    val hash = oneWayHash.toHexString(remoteBytes)
    DownloadResult(
      url,
      localPath,
      DownloadStatus.MissingFromLocalAndPresentInRemote,
      localHash = None,
      remoteHash = Some(hash))
  }

  private def remoteHasGoneMissing(url: String, path: Path): DownloadResult = {
    val localBytes = fileSystem.readFileIntoBytes(path)
    val localHash = oneWayHash.toHexString(localBytes)
    DownloadResult(
      url,
      path,
      DownloadStatus.PresentLocallyAndMissingFromRemote,
      localHash = Some(localHash),
      remoteHash = None)
  }

  private def notFoundAnywhere(url: String, localPath: Path): DownloadResult = {
    DownloadResult(
      url,
      localPath,
      DownloadStatus.MissingFromLocalAndRemote,
      localHash = None,
      remoteHash = None)
  }

  private def doNothing(url: String, localPath: Path, localHash: String, remoteHash: String): DownloadResult = {
    DownloadResult(url, localPath, DownloadStatus.SameInLocalAndRemote, Some(localHash), Some(remoteHash))
  }

  private def handleFilesDifferent(url: String, localPath: Path, localHash: String, remoteHash: String, remoteBytes:Seq[Byte]): DownloadResult = {
    doDownload(url, remoteBytes, localPath)
    DownloadResult(url, localPath, DownloadStatus.DifferentInLocalAndRemote, Some(localHash), Some(remoteHash))
  }
}
