/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BadFieldException
/*    */   extends LaunchDescException
/*    */ {
/*    */   private String _field;
/*    */   private String _value;
/*    */   private String _launchDescSource;
/*    */   
/*    */   public BadFieldException(String paramString1, String paramString2, String paramString3) {
/* 17 */     this._value = paramString3;
/* 18 */     this._field = paramString2;
/* 19 */     this._launchDescSource = paramString1;
/*    */   }
/*    */   
/*    */   public String getField() {
/* 23 */     return getMessage();
/*    */   }
/*    */   public String getValue() {
/* 26 */     return this._value;
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 30 */     if (getValue().equals("https"))
/* 31 */       return ResourceManager.getString("launch.error.badfield", this._field, this._value) + "\n" + ResourceManager.getString("launch.error.badfield.https"); 
/* 32 */     if (!isSignedLaunchDesc()) {
/* 33 */       return ResourceManager.getString("launch.error.badfield", this._field, this._value);
/*    */     }
/* 35 */     return ResourceManager.getString("launch.error.badfield-signedjnlp", this._field, this._value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLaunchDescSource() {
/* 41 */     return this._launchDescSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     if (getValue().equals("https")) {
/* 48 */       return "BadFieldException[ " + getRealMessage() + "]";
/*    */     }
/* 50 */     return "BadFieldException[ " + getField() + "," + getValue() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\BadFieldException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */