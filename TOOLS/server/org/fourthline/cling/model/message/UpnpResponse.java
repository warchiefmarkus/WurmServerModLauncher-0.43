/*    */ package org.fourthline.cling.model.message;
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
/*    */ public class UpnpResponse
/*    */   extends UpnpOperation
/*    */ {
/*    */   private int statusCode;
/*    */   private String statusMessage;
/*    */   
/*    */   public enum Status
/*    */   {
/* 27 */     OK(200, "OK"),
/* 28 */     BAD_REQUEST(400, "Bad Request"),
/* 29 */     NOT_FOUND(404, "Not Found"),
/* 30 */     METHOD_NOT_SUPPORTED(405, "Method Not Supported"),
/* 31 */     PRECONDITION_FAILED(412, "Precondition Failed"),
/* 32 */     UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
/* 33 */     INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
/* 34 */     NOT_IMPLEMENTED(501, "Not Implemented");
/*    */     
/*    */     private int statusCode;
/*    */     private String statusMsg;
/*    */     
/*    */     Status(int statusCode, String statusMsg) {
/* 40 */       this.statusCode = statusCode;
/* 41 */       this.statusMsg = statusMsg;
/*    */     }
/*    */     
/*    */     public int getStatusCode() {
/* 45 */       return this.statusCode;
/*    */     }
/*    */     
/*    */     public String getStatusMsg() {
/* 49 */       return this.statusMsg;
/*    */     }
/*    */     
/*    */     public static Status getByStatusCode(int statusCode) {
/* 53 */       for (Status status : values()) {
/* 54 */         if (status.getStatusCode() == statusCode)
/* 55 */           return status; 
/*    */       } 
/* 57 */       return null;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UpnpResponse(int statusCode, String statusMessage) {
/* 65 */     this.statusCode = statusCode;
/* 66 */     this.statusMessage = statusMessage;
/*    */   }
/*    */   
/*    */   public UpnpResponse(Status status) {
/* 70 */     this.statusCode = status.getStatusCode();
/* 71 */     this.statusMessage = status.getStatusMsg();
/*    */   }
/*    */   
/*    */   public int getStatusCode() {
/* 75 */     return this.statusCode;
/*    */   }
/*    */   
/*    */   public String getStatusMessage() {
/* 79 */     return this.statusMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isFailed() {
/* 86 */     return (this.statusCode >= 300);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResponseDetails() {
/* 93 */     return getStatusCode() + " " + getStatusMessage();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 98 */     return getResponseDetails();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\UpnpResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */