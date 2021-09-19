package 1.0.com.sun.tools.xjc.grammar.xducer;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;

public interface SerializerContext {
  void declareNamespace(JBlock paramJBlock, JExpression paramJExpression1, JExpression paramJExpression2, JExpression paramJExpression3);
  
  JExpression getNamespaceContext();
  
  JExpression onID(JExpression paramJExpression1, JExpression paramJExpression2);
  
  JExpression onIDREF(JExpression paramJExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\SerializerContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */