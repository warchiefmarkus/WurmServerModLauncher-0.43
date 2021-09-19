/*    */ package com.mysql.jdbc;
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
/*    */ public class AssertionFailedException
/*    */   extends RuntimeException
/*    */ {
/*    */   public static void shouldNotHappen(Exception ex) throws AssertionFailedException {
/* 49 */     throw new AssertionFailedException(ex);
/*    */   }
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
/*    */   public AssertionFailedException(Exception ex) {
/* 63 */     super(Messages.getString("AssertionFailedException.0") + ex.toString() + Messages.getString("AssertionFailedException.1"));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\AssertionFailedException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */