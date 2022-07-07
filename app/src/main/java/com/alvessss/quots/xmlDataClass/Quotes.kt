package com.alvessss.quots.xmlDataClass

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "QUOTES")
class Quotes {
    @Element(name = "FRAME", required = false)
    lateinit var frame: List<Frame>
}