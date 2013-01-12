package org.pinky.util

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec


class PropertySpec extends FlatSpec with ShouldMatchers with Property {

    "property reading" should "work" in {
        Property.key("test").get should be ("beepbeep")
        key("test").get should be ("beepbeep")
    }
}
