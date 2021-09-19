/*    */ package org.fourthline.cling.support.contentdirectory;
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
/*    */ public enum ContentDirectoryErrorCode
/*    */ {
/* 23 */   NO_SUCH_OBJECT(701, "The specified ObjectID is invalid"),
/* 24 */   UNSUPPORTED_SORT_CRITERIA(709, "Unsupported or invalid sort criteria"),
/* 25 */   CANNOT_PROCESS(720, "Cannot process the request");
/*    */   
/*    */   private int code;
/*    */   private String description;
/*    */   
/*    */   ContentDirectoryErrorCode(int code, String description) {
/* 31 */     this.code = code;
/* 32 */     this.description = description;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 36 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 40 */     return this.description;
/*    */   }
/*    */   
/*    */   public static ContentDirectoryErrorCode getByCode(int code) {
/* 44 */     for (ContentDirectoryErrorCode errorCode : values()) {
/* 45 */       if (errorCode.getCode() == code)
/* 46 */         return errorCode; 
/*    */     } 
/* 48 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\ContentDirectoryErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */