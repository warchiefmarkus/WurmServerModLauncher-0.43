/*    */ package org.fourthline.cling.support.connectionmanager;
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
/*    */ public enum ConnectionManagerErrorCode
/*    */ {
/* 23 */   INCOMPATIBLE_PROTOCOL_INFO(701, "The connection cannot be established because the protocol info parameter is incompatible"),
/* 24 */   INCOMPATIBLE_DIRECTIONS(702, "The connection cannot be established because the directions of the involved ConnectionManagers (source/sink) are incompatible"),
/* 25 */   INSUFFICIENT_NETWORK_RESOURCES(703, "The connection cannot be established because there are insufficient network resources"),
/* 26 */   LOCAL_RESTRICTIONS(704, "The connection cannot be established because of local restrictions in the device"),
/* 27 */   ACCESS_DENIED(705, "The connection cannot be established because the client is not permitted."),
/* 28 */   INVALID_CONNECTION_REFERENCE(706, "Not a valid connection established by this service"),
/* 29 */   NOT_IN_NETWORK(707, "The connection cannot be established because the ConnectionManagers are not part of the same physical network.");
/*    */   
/*    */   private int code;
/*    */   private String description;
/*    */   
/*    */   ConnectionManagerErrorCode(int code, String description) {
/* 35 */     this.code = code;
/* 36 */     this.description = description;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 40 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 44 */     return this.description;
/*    */   }
/*    */   
/*    */   public static ConnectionManagerErrorCode getByCode(int code) {
/* 48 */     for (ConnectionManagerErrorCode errorCode : values()) {
/* 49 */       if (errorCode.getCode() == code)
/* 50 */         return errorCode; 
/*    */     } 
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\ConnectionManagerErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */