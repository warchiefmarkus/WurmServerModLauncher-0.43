package 1.0.com.sun.xml.xsom.impl.parser;

import com.sun.xml.xsom.impl.parser.Patch;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public interface PatcherManager {
  void addPatcher(Patch paramPatch);
  
  void reportError(String paramString, Locator paramLocator) throws SAXException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\PatcherManager.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */