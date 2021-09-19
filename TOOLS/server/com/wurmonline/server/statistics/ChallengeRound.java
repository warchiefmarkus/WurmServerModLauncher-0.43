/*    */ package com.wurmonline.server.statistics;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChallengeRound
/*    */ {
/*    */   private final int round;
/* 34 */   private final ConcurrentHashMap<Integer, ChallengeScore> privateScores = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   ChallengeRound(int roundval) {
/* 38 */     this.round = roundval;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void setScore(ChallengeScore score) {
/* 43 */     this.privateScores.put(Integer.valueOf(score.getType()), score);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final ChallengeScore getCurrentScoreForType(int type) {
/* 48 */     return this.privateScores.get(Integer.valueOf(type));
/*    */   }
/*    */ 
/*    */   
/*    */   protected final ChallengeScore[] getScores() {
/* 53 */     return (ChallengeScore[])this.privateScores.values().toArray((Object[])new ChallengeScore[this.privateScores.size()]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRound() {
/* 63 */     return this.round;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getRoundName() {
/* 68 */     return ChallengePointEnum.ChallengeScenario.fromInt(this.round).getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getRoundDescription() {
/* 73 */     return ChallengePointEnum.ChallengeScenario.fromInt(this.round).getDesc();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getRoundIcon() {
/* 78 */     return ChallengePointEnum.ChallengeScenario.fromInt(this.round).getUrl();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isCurrent() {
/* 83 */     return (this.round == ChallengePointEnum.ChallengeScenario.current.getNum());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\statistics\ChallengeRound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */