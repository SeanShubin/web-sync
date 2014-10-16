package com.seanshubin.web.sync.core

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path}

class FileSystemImpl extends FileSystem {
  override def readFileIntoString(path: Path): String = bytesToString(Files.readAllBytes(path))

  override def ensureDirectoryExists(path: Path): Unit = Files.createDirectories(path)

  override def writeStringToFile(s: String, path: Path): Unit = Files.write(path, stringToBytes(s))

  private val charset: Charset = StandardCharsets.UTF_8

  private def stringToBytes(s: String): Array[Byte] = s.getBytes(charset)

  private def bytesToString(bytes: Array[Byte]): String = new String(bytes, charset)
}
