package com.seanshubin.web.sync.core

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
    assert(emit.lines.size === 1)
    assert(emit.lines(0) === "  different (difference between local and remote     ): http://some.url -> download/path")
  }

  test("summary") {
    val emit = new FakeLineEmitter
    val notifications = new NotificationsImpl(emit)
    notifications.summary(DownloadResultSamples.bunchOfDownloadResults)
    assert(emit.lines.size === 3)
    assert(emit.lines(0) === "summary")
    assert(emit.lines(1) === "  1 different (error)")
    assert(emit.lines(2) === "  1 same")
  }
}
