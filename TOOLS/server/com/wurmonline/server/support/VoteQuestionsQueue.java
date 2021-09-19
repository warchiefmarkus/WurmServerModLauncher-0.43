/*     */ package com.wurmonline.server.support;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerVote;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VoteQuestionsQueue
/*     */ {
/*     */   public static final byte ADD = 0;
/*     */   public static final byte DELETE = 1;
/*     */   public static final byte CLOSE = 2;
/*     */   public static final byte SETCARDID = 3;
/*     */   public static final byte SETARCHIVESTATE = 4;
/*     */   private final byte action;
/*     */   private final int questionId;
/*     */   private VoteQuestion voteQuestion;
/*     */   private long newVoteEnd;
/*     */   private String newTrelloCardId;
/*     */   private byte newArchiveState;
/*     */   
/*     */   public VoteQuestionsQueue(byte aAction, VoteQuestion aVoteQuestion) {
/*  49 */     this.questionId = aVoteQuestion.getQuestionId();
/*  50 */     this.voteQuestion = aVoteQuestion;
/*  51 */     this.action = aAction;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoteQuestionsQueue(byte aAction, int aQuestionId) {
/*  56 */     this.questionId = aQuestionId;
/*  57 */     this.action = aAction;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoteQuestionsQueue(byte aAction, int aQuestionId, long aNewEnd) {
/*  62 */     this.questionId = aQuestionId;
/*  63 */     this.action = aAction;
/*  64 */     this.newVoteEnd = aNewEnd;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoteQuestionsQueue(byte aAction, int aQuestionId, String aNewTrelloCardId) {
/*  69 */     this.questionId = aQuestionId;
/*  70 */     this.action = aAction;
/*  71 */     this.newTrelloCardId = aNewTrelloCardId;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoteQuestionsQueue(byte aAction, int aQuestionId, byte aNewArchiveState) {
/*  76 */     this.questionId = aQuestionId;
/*  77 */     this.action = aAction;
/*  78 */     this.newArchiveState = aNewArchiveState;
/*     */   }
/*     */   public void action() {
/*     */     VoteQuestion vq;
/*     */     VoteQuestion vq1;
/*     */     VoteQuestion vq2;
/*  84 */     switch (this.action) {
/*     */ 
/*     */       
/*     */       case 0:
/*  88 */         VoteQuestions.addVoteQuestion(this.voteQuestion, true);
/*  89 */         for (Player p : Players.getInstance().getPlayers()) {
/*     */           
/*  91 */           if (this.voteQuestion.canVote(p)) {
/*     */             
/*  93 */             p.addPlayerVote(new PlayerVote(p.getWurmId(), this.voteQuestion.getQuestionId(), false, false, false, false));
/*     */             
/*  95 */             p.gotVotes(true);
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 1:
/* 101 */         VoteQuestions.deleteVoteQuestion(this.questionId);
/*     */         break;
/*     */       
/*     */       case 2:
/* 105 */         vq = VoteQuestions.getVoteQuestion(this.questionId);
/* 106 */         if (vq != null) {
/* 107 */           vq.closeVoting(this.newVoteEnd);
/*     */         }
/*     */         break;
/*     */       case 3:
/* 111 */         vq1 = VoteQuestions.getVoteQuestion(this.questionId);
/* 112 */         if (vq1 != null) {
/* 113 */           vq1.setTrelloCardId(this.newTrelloCardId);
/*     */         }
/*     */         break;
/*     */       case 4:
/* 117 */         vq2 = VoteQuestions.getVoteQuestion(this.questionId);
/* 118 */         if (vq2 != null)
/* 119 */           vq2.setArchiveState(this.newArchiveState); 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\VoteQuestionsQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */