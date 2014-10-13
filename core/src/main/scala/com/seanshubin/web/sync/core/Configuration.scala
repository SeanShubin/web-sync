package com.seanshubin.web.sync.core

case class Configuration(downloadsByDestination: Seq[DownloadsByDestination], behaviourOnConflict: String)
