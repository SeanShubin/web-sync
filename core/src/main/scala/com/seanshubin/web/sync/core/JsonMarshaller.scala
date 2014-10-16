package com.seanshubin.web.sync.core

trait JsonMarshaller {
  def fromJson[T](json: String, theClass: Class[T]): T
}
