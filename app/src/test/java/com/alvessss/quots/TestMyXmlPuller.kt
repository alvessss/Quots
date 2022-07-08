package com.alvessss.quots

import com.alvessss.quots.xmlDataClass.DataFetch
import com.alvessss.quots.xmlDataClass.Quotes
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.File

class TestMyXmlPuller {
    @Test
    fun testXMLParse() {
        WebFileDownloader.getFromUrl("https://raw.githubusercontent.com/alvessss/QuotesSource/master/quotesSource.xml", "xml")
        val xmlToParse: String = File("xml").readText()
        val data = MyXmlPuller.getFrom(xmlToParse.trimIndent())
        assertEquals(data.quotes.size, 1)
        assertEquals(data.quotes.first().phrase, "hello world")
    }
}