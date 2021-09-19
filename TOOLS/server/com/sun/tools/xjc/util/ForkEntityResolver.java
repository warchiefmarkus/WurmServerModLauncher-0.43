/*    */ package com.sun.tools.xjc.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ForkEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final EntityResolver lhs;
/*    */   private final EntityResolver rhs;
/*    */   
/*    */   public ForkEntityResolver(EntityResolver lhs, EntityResolver rhs) {
/* 55 */     this.lhs = lhs;
/* 56 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 60 */     InputSource is = this.lhs.resolveEntity(publicId, systemId);
/* 61 */     if (is != null)
/* 62 */       return is; 
/* 63 */     return this.rhs.resolveEntity(publicId, systemId);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\ForkEntityResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */