package com.seanshubin.web.sync.core

class SystemShutdownImpl extends SystemShutdown {
  override def shutdown(exitCode: Int): Unit = System.exit(exitCode)
}
