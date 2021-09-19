package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSComponent;

public interface XSDeclaration extends XSComponent {
  String getTargetNamespace();
  
  String getName();
  
  boolean isAnonymous();
  
  boolean isGlobal();
  
  boolean isLocal();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSDeclaration.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */