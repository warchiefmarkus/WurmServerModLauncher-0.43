package 1.0.com.sun.tools.xjc.grammar.xducer;

import com.sun.codemodel.JExpression;

public interface DeserializerContext {
  JExpression addToIdTable(JExpression paramJExpression);
  
  JExpression getObjectFromId(JExpression paramJExpression);
  
  JExpression getNamespaceContext();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\DeserializerContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */