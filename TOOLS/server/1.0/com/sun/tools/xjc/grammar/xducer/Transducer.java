package 1.0.com.sun.tools.xjc.grammar.xducer;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import com.sun.msv.grammar.ValueExp;
import com.sun.tools.xjc.generator.GeneratorContext;
import com.sun.tools.xjc.generator.util.BlockReference;
import com.sun.tools.xjc.grammar.AnnotatedGrammar;
import com.sun.tools.xjc.grammar.id.SymbolSpace;
import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
import com.sun.tools.xjc.grammar.xducer.SerializerContext;

public interface Transducer {
  JType getReturnType();
  
  void populate(AnnotatedGrammar paramAnnotatedGrammar, GeneratorContext paramGeneratorContext);
  
  JExpression generateSerializer(JExpression paramJExpression, SerializerContext paramSerializerContext);
  
  void declareNamespace(BlockReference paramBlockReference, JExpression paramJExpression, SerializerContext paramSerializerContext);
  
  JExpression generateDeserializer(JExpression paramJExpression, DeserializerContext paramDeserializerContext);
  
  boolean needsDelayedDeserialization();
  
  boolean isID();
  
  SymbolSpace getIDSymbolSpace();
  
  boolean isBuiltin();
  
  JExpression generateConstant(ValueExp paramValueExp);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\Transducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */