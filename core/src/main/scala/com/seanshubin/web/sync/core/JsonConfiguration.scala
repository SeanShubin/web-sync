package com.seanshubin.web.sync.core

case class JsonConfiguration(reportPathParts: Seq[String],
                             logPathParts: Seq[String],
                             downloadsByDestination: Seq[DownloadsByDestination])
