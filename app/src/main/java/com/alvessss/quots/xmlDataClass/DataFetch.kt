package com.alvessss.quots.xmlDataClass

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "DATA_FETCH", strict = false)
class DataFetch {
    @field:ElementList(name = "QUOTES", required = false)
    lateinit var quotes: List<Frame>
}