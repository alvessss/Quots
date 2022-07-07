package com.alvessss.quots

import com.alvessss.quots.xmlDataClass.DataFetch
import com.alvessss.quots.xmlDataClass.Quotes
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

class TestMyXmlPuller {
    val xmlToParse = """
    <?xml version="1.0"?>
    <DATA_FETCH>
    <QUOTES>
    <FRAME PHRASE="hello world" image="https://github.com/alvessss/QuotesSource/blob/master/backgroundImages/road.jpg"/>
    </QUOTES>
    </DATA_FETCH>
    """.trimIndent()

    @Test
    fun testXMLParse() {
        val data = MyXmlPuller.getFrom(xmlToParse)
        assertEquals(data.quotes.size, 1)
        assertEquals(data.quotes.first().phrase, "hello world")
    }
}