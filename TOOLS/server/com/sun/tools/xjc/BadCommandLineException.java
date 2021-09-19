/*    */ package com.sun.tools.xjc;
/*    */ 
/*    */ import com.sun.istack.Nullable;
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
/*    */ public class BadCommandLineException
/*    */   extends Exception
/*    */ {
/*    */   private Options options;
/*    */   
/*    */   public BadCommandLineException(String msg) {
/* 47 */     super(msg);
/*    */   }
/*    */   
/*    */   public BadCommandLineException(String message, Throwable cause) {
/* 51 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public BadCommandLineException() {
/* 55 */     this(null);
/*    */   }
/*    */   
/*    */   public void initOptions(Options opt) {
/* 59 */     assert this.options == null;
/* 60 */     this.options = opt;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Options getOptions() {
/* 67 */     return this.options;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\BadCommandLineException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */