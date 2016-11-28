package com.seanshubin.web.sync.domain

import java.nio.file.Path

case class Configuration(reportPath: Path, logPath: Path, downloads: Seq[Download])
