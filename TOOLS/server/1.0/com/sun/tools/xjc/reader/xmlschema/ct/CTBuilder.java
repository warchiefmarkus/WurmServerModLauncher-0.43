package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;

import com.sun.msv.grammar.Expression;
import com.sun.xml.xsom.XSComplexType;

interface CTBuilder {
  boolean isApplicable(XSComplexType paramXSComplexType);
  
  Expression build(XSComplexType paramXSComplexType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\CTBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */