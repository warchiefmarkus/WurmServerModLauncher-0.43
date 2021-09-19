/*    */ package com.sun.xml.xsom;
/*    */ 
/*    */ import org.relaxng.datatype.ValidationContext;
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
/*    */ public final class XmlString
/*    */ {
/*    */   public final String value;
/*    */   public final ValidationContext context;
/*    */   
/*    */   public XmlString(String value, ValidationContext context) {
/* 32 */     this.value = value;
/* 33 */     this.context = context;
/* 34 */     if (context == null) {
/* 35 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlString(String value) {
/* 42 */     this(value, NULL_CONTEXT);
/*    */   }
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
/*    */   public final String resolvePrefix(String prefix) {
/* 66 */     return this.context.resolveNamespacePrefix(prefix);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 70 */     return this.value;
/*    */   }
/*    */   
/* 73 */   private static final ValidationContext NULL_CONTEXT = new ValidationContext() {
/*    */       public String resolveNamespacePrefix(String s) {
/* 75 */         if (s.length() == 0) return ""; 
/* 76 */         if (s.equals("xml")) return "http://www.w3.org/XML/1998/namespace"; 
/* 77 */         return null;
/*    */       }
/*    */       
/*    */       public String getBaseUri() {
/* 81 */         return null;
/*    */       }
/*    */       
/*    */       public boolean isUnparsedEntity(String s) {
/* 85 */         return false;
/*    */       }
/*    */       
/*    */       public boolean isNotation(String s) {
/* 89 */         return false;
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XmlString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */