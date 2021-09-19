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
/*    */ public class DLNAConversionIndicatorAttribute
/*    */   extends DLNAAttribute<DLNAConversionIndicator>
/*    */ {
/*    */   public DLNAConversionIndicatorAttribute() {
/* 23 */     setValue(DLNAConversionIndicator.NONE);
/*    */   }
/*    */   
/*    */   public DLNAConversionIndicatorAttribute(DLNAConversionIndicator indicator) {
/* 27 */     setValue(indicator);
/*    */   }
/*    */   
/*    */   public void setString(String s, String cf) throws InvalidDLNAProtocolAttributeException {
/* 31 */     DLNAConversionIndicator value = null;
/*    */     try {
/* 33 */       value = DLNAConversionIndicator.valueOf(Integer.parseInt(s));
/* 34 */     } catch (NumberFormatException numberFormatException) {}
/*    */     
/* 36 */     if (value == null) {
/* 37 */       throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA play speed integer from: " + s);
/*    */     }
/* 39 */     setValue(value);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 43 */     return Integer.toString(getValue().getCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAConversionIndicatorAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */