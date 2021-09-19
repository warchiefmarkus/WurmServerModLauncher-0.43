/*    */ package com.wurmonline.server.villages;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageRecruitee
/*    */ {
/*    */   final int villageId;
/*    */   final String recruiteeName;
/*    */   final long recruiteeId;
/*    */   
/*    */   public VillageRecruitee(int v_id, long r_id, String r_name) {
/* 32 */     this.villageId = v_id;
/* 33 */     this.recruiteeId = r_id;
/* 34 */     this.recruiteeName = r_name;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getVillageId() {
/* 39 */     return this.villageId;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getRecruiteeName() {
/* 44 */     return this.recruiteeName;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getRecruiteeId() {
/* 49 */     return this.recruiteeId;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\VillageRecruitee.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */