package com.seanshubin.web.sync.core

case class ReportDownload(oneWayHashLocal: String,
                          oneWayHashRemote: String,
                          downloadedAt: ReportTimestamp,
                          remoteCheckedAt: ReportTimestamp,
                          localLocation: String,
                          remoteLocation: String)
