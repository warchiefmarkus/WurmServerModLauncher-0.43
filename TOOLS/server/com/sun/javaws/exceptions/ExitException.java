/*    */ package com.sun.javaws.exceptions;
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
/*    */ public class ExitException
/*    */   extends Exception
/*    */ {
/*    */   private int _reason;
/*    */   private Exception _exception;
/*    */   public static final int OK = 0;
/*    */   public static final int REBOOT = 1;
/*    */   public static final int LAUNCH_ERROR = 2;
/*    */   
/*    */   public ExitException(Exception paramException, int paramInt) {
/* 23 */     this._exception = paramException;
/* 24 */     this._reason = paramInt;
/*    */   }
/*    */   public Exception getException() {
/* 27 */     return this._exception;
/*    */   } public int getReason() {
/* 29 */     return this._reason;
/*    */   }
/*    */   public String toString() {
/* 32 */     String str = "ExitException[ " + getReason() + "]";
/* 33 */     if (this._exception != null) {
/* 34 */       str = str + this._exception.toString();
/*    */     }
/* 36 */     return str;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\ExitException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */