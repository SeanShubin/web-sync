package com.seanshubin.web.sync.domain

import java.time.ZonedDateTime

class ClockImpl extends Clock {
  override def zonedDateTimeNow(): ZonedDateTime = ZonedDateTime.now()
}
