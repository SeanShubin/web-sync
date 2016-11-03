package com.seanshubin.web.sync.core

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.StandardOpenOption.{APPEND, CREATE}
import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._

class FileSystemImpl extends FileSystem {
  override def readFileIntoString(path: Path): String = bytesToString(Files.readAllBytes(path))

  override def createMissingDirectories(path: Path): Unit = Files.createDirectories(path)

  override def writeStringToFile(s: String, path: Path): Unit = Files.write(path, stringToBytes(s))

  override def writeBytesToFile(bytes: Seq[Byte], path: Path): Unit = Files.write(path, bytes.toArray)

  override def readFileIntoBytes(path: Path): Seq[Byte] = Files.readAllBytes(path)

  override def readFileIntoLines(path: Path): Seq[String] = Files.readAllLines(path).asScala

  override def fileExists(path: Path): Boolean = Files.exists(path)

  override def deleteIfExists(path: Path): Unit = Files.deleteIfExists(path)

  override def appendLine(path: Path, line: String): Unit = Files.write(path, Seq(line).asJava, APPEND, CREATE)

  private val charset: Charset = StandardCharsets.UTF_8

  private def stringToBytes(s: String): Array[Byte] = s.getBytes(charset)

  private def bytesToString(bytes: Array[Byte]): String = new String(bytes, charset)
}
