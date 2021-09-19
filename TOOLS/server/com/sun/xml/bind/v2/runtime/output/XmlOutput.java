package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

public interface XmlOutput {
  void startDocument(XMLSerializer paramXMLSerializer, boolean paramBoolean, int[] paramArrayOfint, NamespaceContextImpl paramNamespaceContextImpl) throws IOException, SAXException, XMLStreamException;
  
  void endDocument(boolean paramBoolean) throws IOException, SAXException, XMLStreamException;
  
  void beginStartTag(Name paramName) throws IOException, XMLStreamException;
  
  void beginStartTag(int paramInt, String paramString) throws IOException, XMLStreamException;
  
  void attribute(Name paramName, String paramString) throws IOException, XMLStreamException;
  
  void attribute(int paramInt, String paramString1, String paramString2) throws IOException, XMLStreamException;
  
  void endStartTag() throws IOException, SAXException;
  
  void endTag(Name paramName) throws IOException, SAXException, XMLStreamException;
  
  void endTag(int paramInt, String paramString) throws IOException, SAXException, XMLStreamException;
  
  void text(String paramString, boolean paramBoolean) throws IOException, SAXException, XMLStreamException;
  
  void text(Pcdata paramPcdata, boolean paramBoolean) throws IOException, SAXException, XMLStreamException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\XmlOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */