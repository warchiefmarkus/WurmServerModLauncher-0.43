/*    */ package org.fourthline.cling.support.model;
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
/*    */ public class DIDLAttribute
/*    */ {
/*    */   private String namespaceURI;
/*    */   private String prefix;
/*    */   private String value;
/*    */   
/*    */   public DIDLAttribute(String namespaceURI, String prefix, String value) {
/* 30 */     this.namespaceURI = namespaceURI;
/* 31 */     this.prefix = prefix;
/* 32 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getNamespaceURI() {
/* 38 */     return this.namespaceURI;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPrefix() {
/* 45 */     return this.prefix;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 52 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\DIDLAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */