package com.sun.xml.xsom;

public interface XSParticle extends XSContentType {
  public static final int UNBOUNDED = -1;
  
  int getMinOccurs();
  
  int getMaxOccurs();
  
  boolean isRepeated();
  
  XSTerm getTerm();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSParticle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */