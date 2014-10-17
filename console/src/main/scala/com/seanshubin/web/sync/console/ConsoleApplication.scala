package com.seanshubin.web.sync.console

import java.nio.file.{Path, Paths}

object ConsoleApplication extends App {
  new ProductionRunnerWiring {
    override def configurationFilePath: Path = Paths.get(args(0))

    override def reportPath: Path = Paths.get(args(1))
  }.runner.run()
}
