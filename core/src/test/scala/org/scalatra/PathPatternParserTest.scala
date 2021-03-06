package org.scalatra

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class PathPatternParserTest extends FunSuite with ShouldMatchers {
  test("should match exactly on a simple path") {
    val (pattern, names) = PathPatternParser.parseFrom("/simple/path")

    pattern toString() should equal ("^/simple/path$")
    names should equal (Nil)
  }

  test("should match with a trailing slash") {
    val (pattern, names) = PathPatternParser.parseFrom("/simple/path/")

    pattern toString() should equal ("^/simple/path/$")
    names should equal (Nil)
  }
  
  test("should replace a splat with a capturing group") {
    val (pattern, names) = PathPatternParser.parseFrom("/splat/path/*")

    pattern toString() should equal ("^/splat/path/(.*?)$")
    names should equal (List("splat"))
  }

  test("should capture named groups") {
    val (pattern, names) = PathPatternParser.parseFrom("/path/:group")

    pattern toString() should equal ("^/path/([^/?]+)$")
    names should equal (List("group"))
  }

  test("should escape special regex characters") {
    val (pattern, names) = PathPatternParser.parseFrom("/special/$.+()")

    pattern toString() should equal ("""^/special/\$\.\+\(\)$""")
    names should equal (Nil)
  }

  test("allow optional named groups") {
    val (pattern, names) = PathPatternParser.parseFrom("/optional/?:stuff?")

    pattern toString() should equal ("^/optional/?([^/?]+)?$")
    names should equal (List("stuff"))
  }

  test("should support seperate named params for filename and extension") {
    val (pattern, names) = PathPatternParser.parseFrom("/path-with/:file.:extension")

    pattern toString() should equal ("""^/path-with/([^/?]+)\.([^/?]+)$""")
    names should equal (List("file", "extension"))
  }
}