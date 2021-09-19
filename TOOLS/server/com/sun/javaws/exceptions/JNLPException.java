/*    */ package com.sun.javaws.exceptions;
/*    */ 
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
/*    */ public abstract class JNLPException
/*    */   extends Exception
/*    */ {
/* 18 */   private static LaunchDesc _defaultLaunchDesc = null;
/*    */ 
/*    */   
/* 21 */   private LaunchDesc _exceptionLaunchDesc = null;
/*    */ 
/*    */   
/* 24 */   private String _categoryMsg = null;
/*    */ 
/*    */   
/* 27 */   private Throwable _wrappedException = null;
/*    */ 
/*    */   
/*    */   public JNLPException(String paramString) {
/* 31 */     this(paramString, null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public JNLPException(String paramString, LaunchDesc paramLaunchDesc) {
/* 36 */     this(paramString, paramLaunchDesc, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public JNLPException(String paramString, Throwable paramThrowable) {
/* 41 */     this(paramString, null, paramThrowable);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JNLPException(String paramString, LaunchDesc paramLaunchDesc, Throwable paramThrowable) {
/* 47 */     this._categoryMsg = paramString;
/* 48 */     this._exceptionLaunchDesc = paramLaunchDesc;
/* 49 */     this._wrappedException = paramThrowable;
/*    */   }
/*    */   
/*    */   public static void setDefaultLaunchDesc(LaunchDesc paramLaunchDesc) {
/* 53 */     _defaultLaunchDesc = paramLaunchDesc;
/*    */   }
/*    */   
/*    */   public static LaunchDesc getDefaultLaunchDesc() {
/* 57 */     return _defaultLaunchDesc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 64 */     return getRealMessage();
/*    */   } public String getBriefMessage() {
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract String getRealMessage();
/*    */ 
/*    */   
/*    */   public LaunchDesc getLaunchDesc() {
/* 75 */     return (this._exceptionLaunchDesc != null) ? this._exceptionLaunchDesc : _defaultLaunchDesc;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLaunchDescSource() {
/* 80 */     LaunchDesc launchDesc = getLaunchDesc();
/* 81 */     if (launchDesc == null) return null; 
/* 82 */     return launchDesc.getSource();
/*    */   }
/*    */   
/*    */   public String getCategory() {
/* 86 */     return this._categoryMsg;
/*    */   }
/*    */   public Throwable getWrappedException() {
/* 89 */     return this._wrappedException;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 93 */     return "JNLPException[category: " + this._categoryMsg + " : Exception: " + this._wrappedException + " : LaunchDesc: " + this._exceptionLaunchDesc + " ]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\JNLPException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */