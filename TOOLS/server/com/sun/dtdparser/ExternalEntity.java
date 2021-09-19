/*    */ package com.sun.dtdparser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
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
/*    */ final class ExternalEntity
/*    */   extends EntityDecl
/*    */ {
/*    */   String systemId;
/*    */   String publicId;
/*    */   String notation;
/*    */   
/*    */   public ExternalEntity(InputEntity in) {}
/*    */   
/*    */   public InputSource getInputSource(EntityResolver r) throws IOException, SAXException {
/* 29 */     InputSource retval = r.resolveEntity(this.publicId, this.systemId);
/*    */     
/* 31 */     if (retval == null)
/* 32 */       retval = Resolver.createInputSource(new URL(this.systemId), false); 
/* 33 */     return retval;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\dtdparser\ExternalEntity.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */