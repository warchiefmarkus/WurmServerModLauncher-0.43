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
/*    */ public class Affinity
/*    */ {
/*    */   public int skillNumber;
/*    */   public int number;
/*    */   
/*    */   public Affinity(int skillnum, int _number) {
/* 27 */     this.number = _number;
/* 28 */     this.skillNumber = skillnum;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSkillNumber() {
/* 38 */     return this.skillNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSkillNumber(int aSkillNumber) {
/* 49 */     this.skillNumber = aSkillNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumber() {
/* 59 */     return this.number;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNumber(int aNumber) {
/* 70 */     this.number = aNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\Affinity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */