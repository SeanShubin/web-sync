package com.seanshubin.web.sync.core

import java.nio.file.Path

trait FileSystem {
  def deleteIfExists(path: Path)

  def createMissingDirectories(path: Path)

  def readFileIntoString(path: Path): String

  def readFileIntoLines(path: Path): Seq[String]

  def readFileIntoBytes(path: Path): Seq[Byte]

  def writeStringToFile(s: String, path: Path)

  def writeBytesToFile(bytes: Seq[Byte], path: Path)

  def fileExists(path: Path): Boolean

  def appendLine(path: Path, line:String)
}
