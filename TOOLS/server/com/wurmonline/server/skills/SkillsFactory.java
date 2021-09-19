/*    */ package com.wurmonline.server.skills;
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
/*    */ public final class SkillsFactory
/*    */ {
/*    */   public static Skills createSkills(long id) {
/* 34 */     return new DbSkills(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Skills createSkills(String templateName) {
/* 39 */     return new DbSkills(templateName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillsFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */