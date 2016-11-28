package com.seanshubin.web.sync.domain

import java.nio.file.Paths

object DownloadSamples {
  val requireJs = Download("http://requirejs.org/docs/release/2.1.15/comments/require.js", Paths.get("generated/javascript/lib/require.js"))
  val textJs = Download("https://raw.githubusercontent.com/requirejs/text/latest/text.js", Paths.get("generated/javascript/lib/text.js"))
  val domReadyJs = Download("https://raw.githubusercontent.com/requirejs/domReady/latest/domReady.js", Paths.get("generated/javascript/lib/domReady.js"))
  val jqueryJs = Download("http://code.jquery.com/jquery-2.1.1.js", Paths.get("generated/javascript/lib/jquery-2.1.1.js"))
  val underscoreJs = Download("http://underscorejs.org/underscore.js", Paths.get("generated/javascript/lib/underscore.js"))
  val underscoreStringJs = Download("http://epeli.github.io/underscore.string/lib/underscore.string.js", Paths.get("generated/javascript/lib/underscore.string.js"))
  val qunitJs = Download("http://code.jquery.com/qunit/qunit-1.15.0.js", Paths.get("generated/javascript/lib/qunit-1.15.0.js"))
  val qunitCss = Download("http://code.jquery.com/qunit/qunit-1.15.0.css", Paths.get("generated/javascript/lib/qunit-1.15.0.css"))
  val sinonJs = Download("http://sinonjs.org/releases/sinon-1.10.3.js", Paths.get("generated/javascript/lib/sinon-1.10.3.js"))
  val bunchOfDownloads = Seq(
    requireJs,
    textJs,
    domReadyJs,
    jqueryJs,
    underscoreJs,
    underscoreStringJs,
    qunitJs,
    qunitCss,
    sinonJs)
}
