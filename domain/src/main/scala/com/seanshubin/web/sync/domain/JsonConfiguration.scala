package com.seanshubin.web.sync.domain

case class JsonConfiguration(reportPathParts: Seq[String],
                             logPathParts: Seq[String],
                             downloadsByDestination: Seq[DownloadsByDestination])
