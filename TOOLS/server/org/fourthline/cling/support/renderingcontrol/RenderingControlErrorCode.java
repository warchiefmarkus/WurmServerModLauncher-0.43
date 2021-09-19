/*    */ package org.fourthline.cling.support.renderingcontrol;
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
/*    */ public enum RenderingControlErrorCode
/*    */ {
/* 23 */   INVALID_PRESET_NAME(701, "The specified name is not a valid preset name"),
/* 24 */   INVALID_INSTANCE_ID(702, "The specified instanceID is invalid for this RenderingControl");
/*    */   
/*    */   private int code;
/*    */   private String description;
/*    */   
/*    */   RenderingControlErrorCode(int code, String description) {
/* 30 */     this.code = code;
/* 31 */     this.description = description;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 35 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 39 */     return this.description;
/*    */   }
/*    */   
/*    */   public static RenderingControlErrorCode getByCode(int code) {
/* 43 */     for (RenderingControlErrorCode errorCode : values()) {
/* 44 */       if (errorCode.getCode() == code)
/* 45 */         return errorCode; 
/*    */     } 
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\RenderingControlErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */