package com.seanshubin.web.sync.domain

import java.security.MessageDigest

class Sha256 extends OneWayHash {
  private val messageDigest = MessageDigest.getInstance("sha-256")

  override def toHexString(bytes: Seq[Byte]): String = {
    val digest = messageDigest.digest(bytes.toArray)
    val hexCharArray = digest.flatMap(OneWayHash.byteToHexString)
    val hexString = new String(hexCharArray)
    hexString
  }
}
