package com.seanshubin.web.sync.core

import java.nio.file.Paths

import org.scalatest.FunSuite

class ConfigurationParserTest extends FunSuite {
  test("parse configuration") {
    val configurationText =
      """{
        |    "downloadsByDestination": [
        |        {
        |            "destinationParts": ["foo", "bar"],
        |            "sourceUrls": [
        |                "https://raw.githubusercontent.com/requirejs/text/latest/text.js",
        |                "https://raw.githubusercontent.com/requirejs/domReady/latest/domReady.js"
        |            ]
        |        },
        |        {
        |            "destinationParts": ["foo", "baz"],
        |            "sourceUrls": [
        |                "http://code.jquery.com/qunit/qunit-1.15.0.js",
        |                "http://code.jquery.com/qunit/qunit-1.15.0.css"
        |            ]
        |        }
        |    ]
        |}
        | """.stripMargin

    val barPath = Paths.get("foo", "bar").toString
    val bazPath = Paths.get("foo", "baz").toString
    val expected = Seq(
      Download("https://raw.githubusercontent.com/requirejs/text/latest/text.js", barPath),
      Download("https://raw.githubusercontent.com/requirejs/domReady/latest/domReady.js", barPath),
      Download("http://code.jquery.com/qunit/qunit-1.15.0.js", bazPath),
      Download("http://code.jquery.com/qunit/qunit-1.15.0.css", bazPath)
    )
    val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl()
    val configurationParser: ConfigurationParser = new ConfigurationParserImpl(jsonMarshaller)
    val actual = configurationParser.parse(configurationText)
    assert(actual === expected)
  }
}
