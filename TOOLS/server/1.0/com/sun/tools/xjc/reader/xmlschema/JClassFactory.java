package 1.0.com.sun.tools.xjc.reader.xmlschema;

import com.sun.codemodel.JDefinedClass;
import org.xml.sax.Locator;

public interface JClassFactory {
  JDefinedClass create(String paramString, Locator paramLocator);
  
  com.sun.tools.xjc.reader.xmlschema.JClassFactory getParentFactory();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\JClassFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */