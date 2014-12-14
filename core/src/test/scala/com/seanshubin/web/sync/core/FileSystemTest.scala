package com.seanshubin.web.sync.core

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class FileSystemTest extends FunSuite {
  test("read and write text file") {
    val helloPath = IntegrationTestUtil.pathFor("hello-1.txt")
    val helloText = "Hello, world!"
    val fileSystem: FileSystem = new FileSystemImpl()
    fileSystem.createMissingDirectories(IntegrationTestUtil.sampleDataPath)
    fileSystem.writeStringToFile(helloText, helloPath)
    val actual = fileSystem.readFileIntoString(helloPath)
    assert(actual === helloText)
  }

  test("read and write binary file") {
    val charset = StandardCharsets.UTF_8
    val helloPath = IntegrationTestUtil.pathFor("hello-2.txt")
    val helloText = "Hello, world!"
    val helloBytes = helloText.getBytes(charset)
    val fileSystem: FileSystem = new FileSystemImpl()
    fileSystem.createMissingDirectories(IntegrationTestUtil.sampleDataPath)
    fileSystem.deleteIfExists(helloPath)
    assert(fileSystem.fileExists(helloPath) === false)
    fileSystem.writeBytesToFile(helloBytes, helloPath)
    assert(fileSystem.fileExists(helloPath) === true)
    val actual = fileSystem.readFileIntoBytes(helloPath)
    assert(actual === helloBytes)
  }

  test("append to text file") {
    val helloPath = IntegrationTestUtil.pathFor("hello-3.txt")
    val fileSystem: FileSystem = new FileSystemImpl()
    fileSystem.deleteIfExists(helloPath)
    fileSystem.createMissingDirectories(IntegrationTestUtil.sampleDataPath)
    fileSystem.appendLine(helloPath, "hello")
    fileSystem.appendLine(helloPath, "world")
    val expected = Seq("hello", "world")
    val actual = fileSystem.readFileIntoLines(helloPath)
    assert(actual === expected)
  }
}
