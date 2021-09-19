/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MissingFieldException
/*    */   extends LaunchDescException
/*    */ {
/*    */   private String _field;
/*    */   private String _launchDescSource;
/*    */   
/*    */   public MissingFieldException(String paramString1, String paramString2) {
/* 16 */     this._field = paramString2;
/* 17 */     this._launchDescSource = paramString1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 22 */     if (!isSignedLaunchDesc()) {
/* 23 */       return ResourceManager.getString("launch.error.missingfield", this._field);
/*    */     }
/* 25 */     return ResourceManager.getString("launch.error.missingfield-signedjnlp", this._field);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getField() {
/* 30 */     return getMessage();
/*    */   }
/*    */   
/*    */   public String getLaunchDescSource() {
/* 34 */     return this._launchDescSource;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return "MissingFieldException[ " + getField() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\MissingFieldException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */