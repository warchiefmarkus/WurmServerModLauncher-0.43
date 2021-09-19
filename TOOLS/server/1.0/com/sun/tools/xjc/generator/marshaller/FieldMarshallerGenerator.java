package 1.0.com.sun.tools.xjc.generator.marshaller;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;
import com.sun.tools.xjc.generator.field.FieldRenderer;
import com.sun.tools.xjc.generator.util.BlockReference;

public interface FieldMarshallerGenerator {
  FieldRenderer owner();
  
  JExpression peek(boolean paramBoolean);
  
  void increment(BlockReference paramBlockReference);
  
  JExpression hasMore();
  
  com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator clone(JBlock paramJBlock, String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\FieldMarshallerGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */