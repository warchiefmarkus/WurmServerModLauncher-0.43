/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.fourthline.cling.model.types.ServiceType;
/*    */ import org.fourthline.cling.model.types.UDAServiceType;
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
/*    */ public class UDAServiceTypeHeader
/*    */   extends ServiceTypeHeader
/*    */ {
/*    */   public UDAServiceTypeHeader() {}
/*    */   
/*    */   public UDAServiceTypeHeader(URI uri) {
/* 31 */     super(uri);
/*    */   }
/*    */   
/*    */   public UDAServiceTypeHeader(UDAServiceType value) {
/* 35 */     super((ServiceType)value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 41 */       setValue((ServiceType)UDAServiceType.valueOf(s));
/* 42 */     } catch (Exception ex) {
/* 43 */       throw new InvalidHeaderException("Invalid UDA service type header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\UDAServiceTypeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */