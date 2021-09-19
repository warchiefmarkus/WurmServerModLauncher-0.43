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
/*    */ public class PragmaType
/*    */ {
/*    */   private String token;
/*    */   private boolean quote;
/*    */   private String value;
/*    */   
/*    */   public PragmaType(String token, String value, boolean quote) {
/* 28 */     this.token = token;
/* 29 */     this.value = value;
/* 30 */     this.quote = quote;
/*    */   }
/*    */   
/*    */   public PragmaType(String token, String value) {
/* 34 */     this.token = token;
/* 35 */     this.value = value;
/*    */   }
/*    */   
/*    */   public PragmaType(String value) {
/* 39 */     this.token = null;
/* 40 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getToken() {
/* 48 */     return this.token;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getString() {
/* 63 */     String s = "";
/* 64 */     if (this.token != null) {
/* 65 */       s = s + this.token + "=";
/*    */     }
/* 67 */     s = s + (this.quote ? ("\"" + this.value + "\"") : this.value);
/* 68 */     return s;
/*    */   }
/*    */   
/*    */   public static PragmaType valueOf(String s) throws InvalidValueException {
/* 72 */     if (s.length() != 0) {
/* 73 */       String token = null, value = null;
/* 74 */       boolean quote = false;
/* 75 */       String[] params = s.split("=");
/* 76 */       if (params.length > 1) {
/* 77 */         token = params[0];
/* 78 */         value = params[1];
/* 79 */         if (value.startsWith("\"") && value.endsWith("\"")) {
/* 80 */           quote = true;
/* 81 */           value = value.substring(1, value.length() - 1);
/*    */         } 
/*    */       } else {
/*    */         
/* 85 */         value = s;
/*    */       } 
/* 87 */       return new PragmaType(token, value, quote);
/*    */     } 
/* 89 */     throw new InvalidValueException("Can't parse Bytes Range: " + s);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\PragmaType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */