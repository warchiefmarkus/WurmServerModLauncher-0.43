/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
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
/*    */ public class URIDatatype
/*    */   extends AbstractDatatype<URI>
/*    */ {
/*    */   public URI valueOf(String s) throws InvalidValueException {
/* 30 */     if (s.equals("")) return null; 
/*    */     try {
/* 32 */       return new URI(s);
/* 33 */     } catch (URISyntaxException ex) {
/* 34 */       throw new InvalidValueException(ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\URIDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */