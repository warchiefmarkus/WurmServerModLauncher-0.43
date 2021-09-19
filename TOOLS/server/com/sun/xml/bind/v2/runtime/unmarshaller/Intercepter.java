package com.sun.xml.bind.v2.runtime.unmarshaller;

import org.xml.sax.SAXException;

public interface Intercepter {
  Object intercept(UnmarshallingContext.State paramState, Object paramObject) throws SAXException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\Intercepter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */