/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CouldNotLoadArgumentException
/*    */   extends JNLPException
/*    */ {
/*    */   private String _argument;
/*    */   
/*    */   public CouldNotLoadArgumentException(String paramString, Exception paramException) {
/* 14 */     super(ResourceManager.getString("launch.error.category.arguments"), paramException);
/* 15 */     this._argument = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 20 */     return ResourceManager.getString("launch.error.couldnotloadarg", this._argument);
/*    */   }
/*    */   
/*    */   public String getField() {
/* 24 */     return getMessage();
/*    */   }
/*    */   public String toString() {
/* 27 */     return "CouldNotLoadArgumentException[ " + getRealMessage() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\CouldNotLoadArgumentException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */