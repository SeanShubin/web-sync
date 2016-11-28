package com.seanshubin.web.sync.domain

case class DownloadsByDestination(destinationPathParts: Seq[String], sourceUrls: Seq[String])
