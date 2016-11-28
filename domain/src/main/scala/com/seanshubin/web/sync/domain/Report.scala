package com.seanshubin.web.sync.domain

case class Report(lastChecked: ReportTimestamp, downloadReports: Seq[ReportDownload])
