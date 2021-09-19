/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HackerIp
/*    */ {
/* 22 */   public int timesFailed = 0;
/* 23 */   public long mayTryAgain = 0L;
/*    */   
/*    */   public String name;
/*    */   
/*    */   public HackerIp(int _timesFailed, long _mayTryAgain, String _name) {
/* 28 */     this.timesFailed = _timesFailed;
/* 29 */     this.mayTryAgain = _mayTryAgain;
/* 30 */     this.name = _name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTimesFailed() {
/* 40 */     return this.timesFailed;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementTimesFailed() {
/* 48 */     this.timesFailed++;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getMayTryAgain() {
/* 58 */     return this.mayTryAgain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMayTryAgain(long aMayTryAgain) {
/* 68 */     this.mayTryAgain = aMayTryAgain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 78 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String aName) {
/* 88 */     this.name = aName;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\HackerIp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */