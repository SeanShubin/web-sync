package com.seanshubin.web.sync.core

import scala.collection.mutable.ArrayBuffer

sealed abstract case class DownloadStatus(isError: Boolean, shouldLog:Boolean, name: String, description: String) {
  DownloadStatus.valuesBuffer += this
  override def toString = name
}

object DownloadStatus {
  private val valuesBuffer = new ArrayBuffer[DownloadStatus]
  lazy val values = valuesBuffer.toSeq
  val MissingFromLocalAndRemote = new DownloadStatus(isError = true, shouldLog = true, "gone", "missing from local and remote") {}
  val PresentLocallyAndMissingFromRemote = new DownloadStatus(isError = true, shouldLog = true, "missing", "present locally, but missing from remote") {}
  val MissingFromLocalAndPresentInRemote = new DownloadStatus(isError = false, shouldLog = true, "download", "was missing locally, downloaded") {}
  val SameInLocalAndRemote = new DownloadStatus(isError = false, shouldLog = false, "same", "up to date, no action taken") {}
  val DifferentInLocalAndRemote = new DownloadStatus(isError = false, shouldLog = true, "different", "different, downloaded") {}

  def fromString(name: String): Option[DownloadStatus] = {
    def isMatch(status: DownloadStatus) = status.name == name
    values.find(isMatch)
  }

  def validValuesString = values.map(_.name).mkString(", ")
}

