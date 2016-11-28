package com.seanshubin.web.sync.domain

import java.time.ZonedDateTime

trait Clock {
  def zonedDateTimeNow(): ZonedDateTime
}

