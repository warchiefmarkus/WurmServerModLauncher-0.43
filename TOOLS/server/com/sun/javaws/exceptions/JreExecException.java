/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JreExecException
/*    */   extends JNLPException
/*    */ {
/*    */   private String _version;
/*    */   
/*    */   public JreExecException(String paramString, Exception paramException) {
/* 19 */     super(ResourceManager.getString("launch.error.category.unexpected"), paramException);
/* 20 */     this._version = paramString;
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 24 */     return ResourceManager.getString("launch.error.failedexec", this._version);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     return "JreExecException[ " + getMessage() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\JreExecException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */