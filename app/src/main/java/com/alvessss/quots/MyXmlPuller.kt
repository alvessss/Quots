package com.alvessss.quots

import com.alvessss.quots.xmlDataClass.DataFetch
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

class MyXmlPuller
{
    companion object
    {
        fun getFrom(xml: String): DataFetch {
            val serializer: Serializer = Persister()
            return serializer.read(DataFetch::class.java, xml.trimIndent());
        }
    }
}