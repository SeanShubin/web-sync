package com.seanshubin.web.sync.core

class NotificationsImpl(emit: String => Unit) extends Notifications {
  override def beginDownloading(downloads: Seq[Download]): Unit = {
    emit(s"syncing ${downloads.size} resources from web to disk")
  }

  override def downloadResult(downloadResult: DownloadResult): Unit = {
    val DownloadResult(url, path, status, _, _) = downloadResult
    val message = f"  ${status.name}%-9s (${status.description}%-40s): $url%s -> $path%s"
    emit(message)
  }

  override def summary(downloadResults: Seq[DownloadResult]): Unit = {
    emit("summary")
    def statusToString(status: DownloadStatus) = if (status.isError) s"${status.name} (error)" else status.name
    val lines = downloadResults.groupBy(_.status).map { case (key, value) => s"  ${value.size} ${statusToString(key)}"}
    lines.foreach(emit)
  }
}
