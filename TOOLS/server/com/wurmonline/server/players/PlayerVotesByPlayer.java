/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.logging.Logger;
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
/*    */ public class PlayerVotesByPlayer
/*    */ {
/* 34 */   private static Logger logger = Logger.getLogger(PlayerVotesByPlayer.class.getName());
/*    */   
/* 36 */   private final Map<Integer, PlayerVote> playerQuestionVotes = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public PlayerVotesByPlayer() {}
/*    */ 
/*    */   
/*    */   public PlayerVotesByPlayer(PlayerVote pv) {
/* 44 */     add(pv);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(PlayerVote pv) {
/* 49 */     this.playerQuestionVotes.put(Integer.valueOf(pv.getQuestionId()), pv);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(int questionId) {
/* 54 */     if (this.playerQuestionVotes.containsKey(Integer.valueOf(questionId))) {
/* 55 */       this.playerQuestionVotes.remove(Integer.valueOf(questionId));
/*    */     }
/*    */   }
/*    */   
/*    */   public PlayerVote get(int qId) {
/* 60 */     return this.playerQuestionVotes.get(Integer.valueOf(qId));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsKey(int qId) {
/* 65 */     return this.playerQuestionVotes.containsKey(Integer.valueOf(qId));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerVote[] getVotes() {
/* 70 */     return (PlayerVote[])this.playerQuestionVotes.values().toArray((Object[])new PlayerVote[this.playerQuestionVotes.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerVotesByPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */