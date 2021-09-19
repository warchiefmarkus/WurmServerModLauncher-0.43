/*    */ package com.sun.activation.registries;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MimeTypeEntry
/*    */ {
/*    */   private String type;
/*    */   private String extension;
/*    */   
/*    */   public MimeTypeEntry(String mime_type, String file_ext) {
/* 50 */     this.type = mime_type;
/* 51 */     this.extension = file_ext;
/*    */   }
/*    */   
/*    */   public String getMIMEType() {
/* 55 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getFileExtension() {
/* 59 */     return this.extension;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 63 */     return "MIMETypeEntry: " + this.type + ", " + this.extension;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\registries\MimeTypeEntry.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */