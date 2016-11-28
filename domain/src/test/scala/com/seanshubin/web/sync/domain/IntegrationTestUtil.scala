package com.seanshubin.web.sync.domain

import java.nio.file.{Path, Paths}

object IntegrationTestUtil {
  val sampleDataPath = Paths.get("target", "test", "integration", "generated")

  def pathFor(target: String): Path = {
    sampleDataPath.resolve(target)
  }
}
