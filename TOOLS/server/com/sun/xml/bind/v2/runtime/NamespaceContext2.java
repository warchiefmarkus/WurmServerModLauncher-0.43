package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import javax.xml.namespace.NamespaceContext;

public interface NamespaceContext2 extends NamespaceContext {
  String declareNamespace(String paramString1, String paramString2, boolean paramBoolean);
  
  int force(@NotNull String paramString1, @NotNull String paramString2);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\NamespaceContext2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */