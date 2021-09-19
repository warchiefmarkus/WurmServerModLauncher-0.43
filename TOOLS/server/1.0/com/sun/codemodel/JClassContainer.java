package 1.0.com.sun.codemodel;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import java.util.Iterator;

public interface JClassContainer {
  JDefinedClass _class(int paramInt, String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _class(String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _interface(int paramInt, String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _interface(String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _class(int paramInt, String paramString, boolean paramBoolean) throws JClassAlreadyExistsException;
  
  Iterator classes();
  
  com.sun.codemodel.JClassContainer parentContainer();
  
  JCodeModel owner();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JClassContainer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */