package com.seanshubin.web.sync.core

import org.scalatest.FunSuite

class FileSystemTest extends FunSuite {
  test("read and write file") {
    val helloPath = IntegrationTestUtil.pathFor("hello.txt")
    val helloText = "Hello, world!"
    val fileSystem: FileSystem = new FileSystemImpl()
    fileSystem.ensureDirectoryExists(IntegrationTestUtil.sampleDataPath)
    fileSystem.writeStringToFile(helloText, helloPath)
    val actual = fileSystem.readFileIntoString(helloPath)
    assert(actual === helloText)
  }
}
