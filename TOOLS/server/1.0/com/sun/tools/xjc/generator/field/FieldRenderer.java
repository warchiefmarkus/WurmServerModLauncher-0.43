package 1.0.com.sun.tools.xjc.generator.field;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpression;
import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
import com.sun.tools.xjc.grammar.FieldUse;

public interface FieldRenderer {
  void generate();
  
  JBlock getOnSetEventHandler();
  
  FieldUse getFieldUse();
  
  void setter(JBlock paramJBlock, JExpression paramJExpression);
  
  void toArray(JBlock paramJBlock, JExpression paramJExpression);
  
  void unsetValues(JBlock paramJBlock);
  
  JExpression hasSetValue();
  
  JExpression getValue();
  
  JClass getValueType();
  
  JExpression ifCountEqual(int paramInt);
  
  JExpression ifCountGte(int paramInt);
  
  JExpression ifCountLte(int paramInt);
  
  JExpression count();
  
  FieldMarshallerGenerator createMarshaller(JBlock paramJBlock, String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\FieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */