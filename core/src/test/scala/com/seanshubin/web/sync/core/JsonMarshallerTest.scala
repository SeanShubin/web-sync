package com.seanshubin.web.sync.core

import org.scalatest.FunSuite

case class SampleForMarshalling(stringSeq: Seq[String],
                                stringSeqSeq: Seq[Seq[String]],
                                optionString: Option[String])

case class UnknownPropertiesTestHelper(bar: Int)

case class NullPropertiesTestHelper(a: String, b: String, c: Option[String], d: Option[String])

class JsonMarshallerTest extends FunSuite {
  val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl

  test("sample from json") {
    val json = """{ "stringSeq" : [ "aaa" ], "stringSeqSeq" : [ [ "bbb" ] ], "optionString" : "ccc"}"""
    val expected = SampleForMarshalling(Seq("aaa"), Seq(Seq("bbb")), Some("ccc"))
    val actual = jsonMarshaller.fromJson(json, classOf[SampleForMarshalling])
    assert(actual === expected)
  }

  test("sensible error message on failure to parse") {
    val jsonMissingClosingBrace = """{ "a" : "b" """
    try {
      jsonMarshaller.fromJson(jsonMissingClosingBrace, classOf[Map[String, String]])
      fail("should have thrown exception")
    } catch {
      case ex: RuntimeException =>
        val expectedMessage =
          "Error while attempting to parse \"{ \\\"a\\\" : \\\"b\\\" \": " +
            "Unexpected end-of-input: expected close marker for OBJECT " +
            "(from [Source: { \"a\" : \"b\" ; line: 1, column: 0])\n" +
            " at [Source: { \"a\" : \"b\" ; line: 1, column: 25]"
        val actualMessage = ex.getMessage
        assert(actualMessage === expectedMessage)
    }
  }

  test("ignore unknown properties") {
    val json = """{ "bar": 123, "baz":456 }"""
    val expected = UnknownPropertiesTestHelper(123)
    val actual = jsonMarshaller.fromJson(json, classOf[UnknownPropertiesTestHelper])
    assert(actual === expected)
  }
}
