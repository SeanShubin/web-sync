package com.seanshubin.web.sync.console

import com.seanshubin.web.sync.core.{Runner, RunnerImpl}

trait ProductionRunnerWiring {
  def configuration: String

  lazy val emitLine: String => Unit = println
  lazy val runner: Runner = new RunnerImpl(configuration, emitLine)
}

object ProductionRunnerWiring {
  def apply(theConfiguration: String) = new ProductionRunnerWiring {
    override def configuration: String = theConfiguration
  }
}
