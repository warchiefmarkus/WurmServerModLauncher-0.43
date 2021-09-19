package com.sun.xml.bind.api;

public abstract class RawAccessor<B, V> {
  public abstract V get(B paramB) throws AccessorException;
  
  public abstract void set(B paramB, V paramV) throws AccessorException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\RawAccessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */