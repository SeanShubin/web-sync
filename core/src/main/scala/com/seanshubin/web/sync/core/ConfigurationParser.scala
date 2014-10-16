package com.seanshubin.web.sync.core

trait ConfigurationParser {
  def parse(text: String): Seq[Download]
}
