package com.sun.tools.xjc.reader.internalizer;

import org.w3c.dom.Element;
import org.xml.sax.helpers.XMLFilterImpl;

public interface InternalizationLogic {
  XMLFilterImpl createExternalReferenceFinder(DOMForest paramDOMForest);
  
  boolean checkIfValidTargetNode(DOMForest paramDOMForest, Element paramElement1, Element paramElement2);
  
  Element refineTarget(Element paramElement);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\InternalizationLogic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */