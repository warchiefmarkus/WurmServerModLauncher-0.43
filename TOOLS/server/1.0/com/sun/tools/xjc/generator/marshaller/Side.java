package 1.0.com.sun.tools.xjc.generator.marshaller;

import com.sun.msv.grammar.ChoiceExp;
import com.sun.msv.grammar.Expression;
import com.sun.tools.xjc.grammar.FieldItem;

interface Side {
  void onChoice(ChoiceExp paramChoiceExp);
  
  void onZeroOrMore(Expression paramExpression);
  
  void onMarshallableObject();
  
  void onField(FieldItem paramFieldItem);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\Side.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */