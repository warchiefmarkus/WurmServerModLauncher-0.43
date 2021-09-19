/*    */ package com.sun.mail.iap;
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
/*    */ public class CommandFailedException
/*    */   extends ProtocolException
/*    */ {
/*    */   private static final long serialVersionUID = 793932807880443631L;
/*    */   
/*    */   public CommandFailedException() {}
/*    */   
/*    */   public CommandFailedException(String s) {
/* 63 */     super(s);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CommandFailedException(Response r) {
/* 71 */     super(r);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\mail\iap\CommandFailedException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */