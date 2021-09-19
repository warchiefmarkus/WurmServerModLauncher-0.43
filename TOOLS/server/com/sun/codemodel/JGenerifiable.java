package com.sun.codemodel;

public interface JGenerifiable {
  JTypeVar generify(String paramString);
  
  JTypeVar generify(String paramString, Class paramClass);
  
  JTypeVar generify(String paramString, JClass paramJClass);
  
  JTypeVar[] typeParams();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JGenerifiable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */