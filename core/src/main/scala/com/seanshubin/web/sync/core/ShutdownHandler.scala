package com.seanshubin.web.sync.core

trait ShutdownHandler {
  def shutdown(downloadResults: String)
}
