/*    */ package com.wurmonline.server.skills;
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
/*    */ public class NoSuchSkillException
/*    */   extends WurmServerException
/*    */ {
/*    */   private static final long serialVersionUID = 534621721301818809L;
/*    */   
/*    */   public NoSuchSkillException(String message) {
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
/*    */   public NoSuchSkillException(Throwable cause) {
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
/*    */   public NoSuchSkillException(String message, Throwable cause) {
/* 81 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\NoSuchSkillException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */