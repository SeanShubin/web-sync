package com.seanshubin.web.sync.core

trait SystemShutdown {
  def shutdown(exitCode: Int)
}
