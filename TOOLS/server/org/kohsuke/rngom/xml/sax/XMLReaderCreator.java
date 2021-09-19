package org.kohsuke.rngom.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public interface XMLReaderCreator {
  XMLReader createXMLReader() throws SAXException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\xml\sax\XMLReaderCreator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */