package com.seanshubin.web.sync.core

import java.nio.file.Path

case class Configuration(reportPath: Path, downloads: Seq[Download])
