package com.seanshubin.web.sync.core

import java.nio.file.Paths

import org.scalatest.FunSuite

class ConfigurationParserTest extends FunSuite {
  test("parse configuration") {
    val configurationText =
      """{
        |    "reportPathParts": ["foo", "reports", "report.json"],
        |    "logPathParts": ["foo", "reports", "report.txt"],
        |    "downloadsByDestination": [
        |        {
        |            "destinationPathParts": ["foo", "bar"],
        |            "sourceUrls": [
        |                "https://raw.githubusercontent.com/requirejs/text/latest/text.js",
        |                "https://raw.githubusercontent.com/requirejs/domReady/latest/domReady.js"
        |            ]
        |        },
        |        {
        |            "destinationPathParts": ["foo", "baz"],
        |            "sourceUrls": [
        |                "http://code.jquery.com/qunit/qunit-1.15.0.js",
        |                "http://code.jquery.com/qunit/qunit-1.15.0.css"
        |            ]
        |        }
        |    ]
        |}
        | """.stripMargin

    val textPath = Paths.get("foo", "bar", "text.js")
    val domReadyPath = Paths.get("foo", "bar", "domReady.js")
    val qunitJsPath = Paths.get("foo", "baz", "qunit-1.15.0.js")
    val qunitCssPath = Paths.get("foo", "baz", "qunit-1.15.0.css")
    val expectedReportPath = Paths.get("foo", "reports", "report.json")
    val expectedDownloads = Seq(
      Download("https://raw.githubusercontent.com/requirejs/text/latest/text.js", textPath),
      Download("https://raw.githubusercontent.com/requirejs/domReady/latest/domReady.js", domReadyPath),
      Download("http://code.jquery.com/qunit/qunit-1.15.0.js", qunitJsPath),
      Download("http://code.jquery.com/qunit/qunit-1.15.0.css", qunitCssPath)
    )
    val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl()
    val configurationParser: ConfigurationParser = new ConfigurationParserImpl(jsonMarshaller)
    val actual = configurationParser.parse(configurationText)
    assert(actual.reportPath === expectedReportPath)
    assertSequencesEqual(actual.downloads, expectedDownloads)
  }

  def assertSequencesEqual[T](actual: Seq[T], expected: Seq[T], index: Int = 0): Unit = {
    if (actual.isEmpty) {
      if (expected.isEmpty) {
        //done
      } else {
        fail(s"missing at index $index, expected:\n${expected.head}")
      }
    } else {
      if (expected.isEmpty) {
        fail(s"extra at index $index, did not expect:\n${expected.head}")
      } else {
        assert(actual.head === expected.head, s"comparison at index $index")
        assertSequencesEqual(actual.tail, expected.tail, index + 1)
      }
    }
  }
}
