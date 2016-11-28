package com.seanshubin.web.sync.domain

trait ConfigurationParser {
  def parse(text: String): Configuration
}
