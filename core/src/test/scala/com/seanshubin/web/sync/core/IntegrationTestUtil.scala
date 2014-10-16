package com.seanshubin.web.sync.core

import java.nio.file.{Path, Paths}

object IntegrationTestUtil {
  val sampleDataPath = Paths.get("target", "test", "integration", "generated")

  def pathFor(target: String): Path = {
    sampleDataPath.resolve(target)
  }
}
