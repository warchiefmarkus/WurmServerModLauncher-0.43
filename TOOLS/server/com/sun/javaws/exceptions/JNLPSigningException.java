/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import com.sun.javaws.jnl.LaunchDesc;
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
/*    */ public class JNLPSigningException
/*    */   extends LaunchDescException
/*    */ {
/* 18 */   String _signedSource = null;
/*    */ 
/*    */   
/*    */   public JNLPSigningException(LaunchDesc paramLaunchDesc, String paramString) {
/* 22 */     super(paramLaunchDesc, (Exception)null);
/* 23 */     this._signedSource = paramString;
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 27 */     return ResourceManager.getString("launch.error.badsignedjnlp");
/*    */   } public String getSignedSource() {
/* 29 */     return this._signedSource;
/*    */   }
/*    */   public String toString() {
/* 32 */     return "JNLPSigningException[" + getMessage() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\JNLPSigningException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */