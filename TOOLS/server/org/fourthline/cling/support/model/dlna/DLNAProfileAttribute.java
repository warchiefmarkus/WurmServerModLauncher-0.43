/*    */ package org.fourthline.cling.support.model.dlna;
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
/*    */ public class DLNAProfileAttribute
/*    */   extends DLNAAttribute<DLNAProfiles>
/*    */ {
/*    */   public DLNAProfileAttribute() {
/* 23 */     setValue(DLNAProfiles.NONE);
/*    */   }
/*    */   
/*    */   public DLNAProfileAttribute(DLNAProfiles profile) {
/* 27 */     setValue(profile);
/*    */   }
/*    */   
/*    */   public void setString(String s, String cf) throws InvalidDLNAProtocolAttributeException {
/* 31 */     DLNAProfiles value = DLNAProfiles.valueOf(s, cf);
/* 32 */     if (value == null) {
/* 33 */       throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA profile from: " + s);
/*    */     }
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 39 */     return getValue().getCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAProfileAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */