/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.shared.exceptions.WurmServerException;
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
/*    */ public final class NoSuchActionException
/*    */   extends WurmServerException
/*    */ {
/*    */   private static final long serialVersionUID = 7185872169527936353L;
/*    */   
/*    */   public NoSuchActionException(String message) {
/* 45 */     super(message);
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSuchActionException(Throwable cause) {
/* 63 */     super(cause);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSuchActionException(String message, Throwable cause) {
/* 85 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\NoSuchActionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */