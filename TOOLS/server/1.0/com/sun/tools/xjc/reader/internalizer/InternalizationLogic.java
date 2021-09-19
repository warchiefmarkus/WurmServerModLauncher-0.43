package 1.0.com.sun.tools.xjc.reader.internalizer;

import com.sun.tools.xjc.reader.internalizer.DOMForest;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;

public interface InternalizationLogic {
  XMLFilter createExternalReferenceFinder(DOMForest paramDOMForest);
  
  boolean checkIfValidTargetNode(DOMForest paramDOMForest, Element paramElement1, Element paramElement2) throws SAXException;
  
  Element refineTarget(Element paramElement);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\InternalizationLogic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */