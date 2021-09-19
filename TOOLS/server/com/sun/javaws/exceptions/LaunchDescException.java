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
/*    */ 
/*    */ 
/*    */ public class LaunchDescException
/*    */   extends JNLPException
/*    */ {
/*    */   private String _message;
/*    */   private boolean _isSignedLaunchDesc;
/*    */   
/*    */   public LaunchDescException() {
/* 24 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public LaunchDescException(Exception paramException) {
/* 29 */     this(null, paramException);
/*    */   }
/*    */   
/*    */   public void setIsSignedLaunchDesc() {
/* 33 */     this._isSignedLaunchDesc = true; } public boolean isSignedLaunchDesc() {
/* 34 */     return this._isSignedLaunchDesc;
/*    */   }
/*    */   
/*    */   public LaunchDescException(LaunchDesc paramLaunchDesc, Exception paramException) {
/* 38 */     super(ResourceManager.getString("launch.error.category.launchdesc"), paramLaunchDesc, paramException);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LaunchDescException(LaunchDesc paramLaunchDesc, String paramString, Exception paramException) {
/* 45 */     this(paramLaunchDesc, paramException);
/* 46 */     this._message = paramString;
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 50 */     return this._message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\LaunchDescException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */