package javax.xml.bind;

import org.xml.sax.ContentHandler;

public interface UnmarshallerHandler extends ContentHandler {
  Object getResult() throws JAXBException, IllegalStateException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\UnmarshallerHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */