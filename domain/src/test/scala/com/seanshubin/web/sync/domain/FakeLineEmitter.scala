package com.seanshubin.web.sync.domain

import scala.collection.mutable.ArrayBuffer

class FakeLineEmitter extends Function[String, Unit] {
  val lines: ArrayBuffer[String] = new ArrayBuffer

  override def apply(line: String): Unit = lines.append(line)
}
