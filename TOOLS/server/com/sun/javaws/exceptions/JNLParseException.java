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
/*    */ 
/*    */ public class JNLParseException
/*    */   extends LaunchDescException
/*    */ {
/*    */   private String _msg;
/*    */   private int _line;
/*    */   private String _launchDescSource;
/*    */   
/*    */   public JNLParseException(String paramString1, Exception paramException, String paramString2, int paramInt) {
/* 22 */     super(paramException);
/* 23 */     this._msg = paramString2;
/* 24 */     this._line = paramInt;
/* 25 */     this._launchDescSource = paramString1;
/*    */   }
/*    */   public int getLine() {
/* 28 */     return this._line;
/*    */   }
/*    */   public String getRealMessage() {
/* 31 */     if (!isSignedLaunchDesc()) {
/* 32 */       return ResourceManager.getString("launch.error.parse", this._line);
/*    */     }
/* 34 */     return ResourceManager.getString("launch.error.parse-signedjnlp", this._line);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLaunchDescSource() {
/* 40 */     return this._launchDescSource;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return "JNLParseException[ " + getMessage() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\JNLParseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */