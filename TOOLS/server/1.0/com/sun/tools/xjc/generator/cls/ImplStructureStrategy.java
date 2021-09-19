package 1.0.com.sun.tools.xjc.generator.cls;

import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.generator.MethodWriter;
import com.sun.tools.xjc.grammar.ClassItem;

public interface ImplStructureStrategy {
  JDefinedClass createImplClass(ClassItem paramClassItem);
  
  MethodWriter createMethodWriter(ClassContext paramClassContext);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\cls\ImplStructureStrategy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */