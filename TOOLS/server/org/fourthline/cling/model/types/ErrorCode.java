/*    */ package org.fourthline.cling.model.types;
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
/*    */ public enum ErrorCode
/*    */ {
/* 25 */   INVALID_ACTION(401, "No action by that name at this service"),
/* 26 */   INVALID_ARGS(402, "Not enough IN args, too many IN args, no IN arg by that name, one or more IN args are of the wrong data type"),
/* 27 */   ACTION_FAILED(501, "Current state of service prevents invoking that action"),
/* 28 */   ARGUMENT_VALUE_INVALID(600, "The argument value is invalid"),
/* 29 */   ARGUMENT_VALUE_OUT_OF_RANGE(601, "An argument value is less than the minimum or more than the maximum value of the allowedValueRange, or is not in the allowedValueList"),
/* 30 */   OPTIONAL_ACTION(602, "The requested action is optional and is not implemented by the device"),
/* 31 */   OUT_OF_MEMORY(603, "The device does not have sufficient memory available to complete the action"),
/* 32 */   HUMAN_INTERVENTION_REQUIRED(604, "The device has encountered an error condition which it cannot resolve itself"),
/* 33 */   ARGUMENT_TOO_LONG(605, "A string argument is too long for the device to handle properly"),
/* 34 */   ACTION_NOT_AUTHORIZED(606, "The action requested requires authorization and the sender was not authorized"),
/* 35 */   SIGNATURE_FAILURE(607, "The sender's signature failed to verify"),
/* 36 */   SIGNATURE_MISSING(608, "The action requested requires a digital signature and there was none provided"),
/* 37 */   NOT_ENCRYPTED(609, "This action requires confidentiality but the action was not delivered encrypted"),
/* 38 */   INVALID_SEQUENCE(610, "The sequence provided was not valid"),
/* 39 */   INVALID_CONTROL_URL(611, "The controlURL within the freshness element does not match the controlURL of the action actually invoked"),
/* 40 */   NO_SUCH_SESSION(612, "The session key reference is to a non-existent session"),
/* 41 */   TRANSPORT_LOCKED(705, "Transport locked"),
/* 42 */   ILLEGAL_MIME_TYPE(714, "Illegal mime-type");
/*    */   
/*    */   private int code;
/*    */   private String description;
/*    */   
/*    */   ErrorCode(int code, String description) {
/* 48 */     this.code = code;
/* 49 */     this.description = description;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 53 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 57 */     return this.description;
/*    */   }
/*    */   
/*    */   public static ErrorCode getByCode(int code) {
/* 61 */     for (ErrorCode errorCode : values()) {
/* 62 */       if (errorCode.getCode() == code)
/* 63 */         return errorCode; 
/*    */     } 
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\ErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */