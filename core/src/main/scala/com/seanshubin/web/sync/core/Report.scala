package com.seanshubin.web.sync.core

case class Report(lastChecked: ReportTimestamp, downloadReports: Seq[ReportDownload])
