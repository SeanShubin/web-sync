package com.seanshubin.web.sync.domain

case class SampleForMarshalling(stringSeq: Seq[String],
                                stringSeqSeq: Seq[Seq[String]],
                                optionString: Option[String])
