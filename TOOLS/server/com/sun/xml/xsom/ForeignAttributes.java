package com.sun.xml.xsom;

import org.relaxng.datatype.ValidationContext;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public interface ForeignAttributes extends Attributes {
  ValidationContext getContext();
  
  Locator getLocator();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\ForeignAttributes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */