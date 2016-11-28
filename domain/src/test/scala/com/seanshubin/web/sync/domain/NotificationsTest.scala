package com.seanshubin.web.sync.domain

import org.scalatest.FunSuite

class NotificationsTest extends FunSuite {
  test("begin downloading") {
    val emit = new FakeLineEmitter
    val notifications = new NotificationsImpl(emit)
    notifications.beginDownloading(DownloadSamples.bunchOfDownloads)
    assert(emit.lines.size === 1)
    assert(emit.lines(0) === "syncing 9 resources from web to disk")
  }

  test("download result") {
    val emit = new FakeLineEmitter
    val notifications = new NotificationsImpl(emit)
    notifications.downloadResult(DownloadResultSamples.downloadResultDifferent)
    val pathString = DownloadResultSamples.downloadResultDifferent.path.toString
    assert(emit.lines.size === 1)
    assert(emit.lines(0) === s"  different (different, downloaded                   ): http://some.url -> $pathString")
  }

  test("summary") {
    val emit = new FakeLineEmitter
    val notifications = new NotificationsImpl(emit)
    notifications.summary(DownloadResultSamples.bunchOfDownloadResults)
    assert(emit.lines.size === 3)
    assert(emit.lines(0) === "summary")
    assert(emit.lines(1) === "  1 same")
    assert(emit.lines(2) === "  1 different")
  }
}
