package com.seanshubin.web.sync.core

trait OneWayHash {
  def toHexString(source: Seq[Byte]): String
}

object OneWayHash {
  def byteToHexString(b: Byte): String = f"$b%02x"

  def byteArrayToHexString(bytes: Array[Byte]): String = new String(bytes.flatMap(OneWayHash.byteToHexString))
}
