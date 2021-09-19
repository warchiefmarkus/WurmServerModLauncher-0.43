/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
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
/*    */ enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 46 */   ILLEGAL_PARAMETER,
/* 47 */   UNABLE_TO_FIND_CONVERSION_METHOD,
/* 48 */   MISSING_ID,
/* 49 */   NOT_IMPLEMENTED_IN_2_0,
/* 50 */   UNRECOGNIZED_ELEMENT_NAME,
/* 51 */   TYPE_MISMATCH,
/* 52 */   MISSING_OBJECT,
/* 53 */   NOT_IDENTIFIABLE,
/* 54 */   DANGLING_IDREF,
/* 55 */   NULL_OUTPUT_RESOLVER,
/* 56 */   UNABLE_TO_MARSHAL_NON_ELEMENT,
/* 57 */   UNSUPPORTED_PROPERTY,
/* 58 */   NULL_PROPERTY_NAME,
/* 59 */   MUST_BE_X,
/* 60 */   NOT_MARSHALLABLE,
/* 61 */   UNSUPPORTED_RESULT,
/* 62 */   UNSUPPORTED_ENCODING,
/* 63 */   SUBSTITUTED_BY_ANONYMOUS_TYPE,
/* 64 */   CYCLE_IN_MARSHALLER,
/* 65 */   UNABLE_TO_DISCOVER_EVENTHANDLER,
/* 66 */   ELEMENT_NEEDED_BUT_FOUND_DOCUMENT,
/* 67 */   UNKNOWN_CLASS,
/* 68 */   FAILED_TO_GENERATE_SCHEMA;
/*    */   
/*    */   static {
/* 71 */     rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */   }
/*    */   public String toString() {
/* 74 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 78 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */