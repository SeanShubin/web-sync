package com.seanshubin.web.sync.core

class SystemClockImpl extends SystemClock {
  override def currentTimeMillis: Long = System.currentTimeMillis()
}
