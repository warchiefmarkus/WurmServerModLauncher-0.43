package com.sun.xml.bind.api;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

public abstract class ClassResolver {
  @Nullable
  public abstract Class<?> resolveElementName(@NotNull String paramString1, @NotNull String paramString2) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\ClassResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */