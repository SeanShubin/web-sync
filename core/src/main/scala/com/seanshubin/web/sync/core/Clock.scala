package com.seanshubin.web.sync.core

import java.time.ZonedDateTime

trait Clock {
  def zonedDateTimeNow(): ZonedDateTime
}

