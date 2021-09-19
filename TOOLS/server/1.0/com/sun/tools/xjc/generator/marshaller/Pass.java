package 1.0.com.sun.tools.xjc.generator.marshaller;

import com.sun.msv.grammar.AttributeExp;
import com.sun.msv.grammar.ElementExp;
import com.sun.msv.grammar.Expression;
import com.sun.msv.grammar.ValueExp;
import com.sun.tools.xjc.grammar.ExternalItem;
import com.sun.tools.xjc.grammar.PrimitiveItem;

interface Pass {
  void build(Expression paramExpression);
  
  String getName();
  
  void onElement(ElementExp paramElementExp);
  
  void onExternal(ExternalItem paramExternalItem);
  
  void onAttribute(AttributeExp paramAttributeExp);
  
  void onPrimitive(PrimitiveItem paramPrimitiveItem);
  
  void onValue(ValueExp paramValueExp);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\Pass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */