/*    */ package com.sun.xml.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final Class base;
/*    */   
/*    */   public ResourceEntityResolver(Class _base) {
/* 27 */     this.base = _base;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) {
/* 33 */     return new InputSource(this.base.getResourceAsStream(systemId));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\imp\\util\ResourceEntityResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */