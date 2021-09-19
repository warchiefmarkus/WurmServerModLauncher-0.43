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
/*    */ public final class NoSuchBehaviourException
/*    */   extends WurmServerException
/*    */ {
/*    */   private static final long serialVersionUID = -7889104664023078651L;
/*    */   
/*    */   public NoSuchBehaviourException(String message) {
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
/*    */   public NoSuchBehaviourException(Throwable cause) {
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
/*    */   public NoSuchBehaviourException(String message, Throwable cause) {
/* 85 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\NoSuchBehaviourException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */