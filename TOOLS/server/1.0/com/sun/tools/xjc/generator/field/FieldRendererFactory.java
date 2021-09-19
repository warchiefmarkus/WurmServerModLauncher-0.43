package 1.0.com.sun.tools.xjc.generator.field;

import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.generator.field.FieldRenderer;
import com.sun.tools.xjc.grammar.FieldUse;

public interface FieldRendererFactory {
  FieldRenderer create(ClassContext paramClassContext, FieldUse paramFieldUse);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\FieldRendererFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */