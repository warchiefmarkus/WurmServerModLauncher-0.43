package 1.0.com.sun.tools.xjc.grammar;

import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.ExternalItem;
import com.sun.tools.xjc.grammar.FieldItem;
import com.sun.tools.xjc.grammar.IgnoreItem;
import com.sun.tools.xjc.grammar.InterfaceItem;
import com.sun.tools.xjc.grammar.PrimitiveItem;
import com.sun.tools.xjc.grammar.SuperClassItem;

public interface JavaItemVisitor {
  Object onClass(ClassItem paramClassItem);
  
  Object onField(FieldItem paramFieldItem);
  
  Object onIgnore(IgnoreItem paramIgnoreItem);
  
  Object onInterface(InterfaceItem paramInterfaceItem);
  
  Object onPrimitive(PrimitiveItem paramPrimitiveItem);
  
  Object onExternal(ExternalItem paramExternalItem);
  
  Object onSuper(SuperClassItem paramSuperClassItem);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\JavaItemVisitor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */