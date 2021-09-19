/*    */ package com.wurmonline.server.statistics;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScoreNamePair
/*    */   implements Comparable<ScoreNamePair>
/*    */ {
/*    */   public final String name;
/*    */   public final ChallengeScore score;
/*    */   
/*    */   public ScoreNamePair(String owner, ChallengeScore score) {
/* 34 */     this.name = owner;
/* 35 */     this.score = score;
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
/*    */   public int compareTo(ScoreNamePair namePair) {
/* 48 */     if (this.score.getPoints() > namePair.score.getPoints())
/* 49 */       return -1; 
/* 50 */     if (this.name.toLowerCase().equals(namePair.name.toLowerCase()) && this.score.getPoints() == namePair.score.getPoints()) {
/* 51 */       return 0;
/*    */     }
/* 53 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\statistics\ScoreNamePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */