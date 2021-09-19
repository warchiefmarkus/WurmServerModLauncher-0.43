/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public abstract class JResourceFile
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   protected JResourceFile(String name) {
/* 34 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String name() {
/* 41 */     return this.name;
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
/*    */   protected boolean isResource() {
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract void build(OutputStream paramOutputStream) throws IOException;
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JResourceFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */