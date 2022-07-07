package com.alvessss.quots.xmlDataClass

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(strict = false, name = "FRAME")
class Frame {
    @field:Attribute(name = "PHRASE", required = false)
    var phrase: String? = null

    @field:Attribute(name = "IMAGE", required = false)
    var image: String? = null
}