package com.seanshubin.web.sync.core

import java.nio.file.Path

trait FileSystem {
  def ensureDirectoryExists(path: Path)

  def readFileIntoString(path: Path): String

  def writeStringToFile(s: String, path: Path)
}
