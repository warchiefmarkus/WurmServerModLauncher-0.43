/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class Questions
/*     */   implements TimeConstants
/*     */ {
/*  34 */   private static Map<Integer, Question> questions = new HashMap<>();
/*     */   
/*  36 */   private static Logger logger = Logger.getLogger(Questions.class.getName());
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
/*     */   static void addQuestion(Question question) {
/*  50 */     questions.put(Integer.valueOf(question.getId()), question);
/*  51 */     Question lastQuestion = ((Player)question.getResponder()).getCurrentQuestion();
/*  52 */     if (lastQuestion != null)
/*     */     {
/*     */       
/*  55 */       lastQuestion.timedOut();
/*     */     }
/*  57 */     ((Player)question.getResponder()).setQuestion(question);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Question getQuestion(int id) throws NoSuchQuestionException {
/*  69 */     Integer iid = Integer.valueOf(id);
/*  70 */     Question question = questions.get(iid);
/*  71 */     if (question == null)
/*  72 */       throw new NoSuchQuestionException(String.valueOf(id)); 
/*  73 */     return question;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getNumUnanswered() {
/*  78 */     return questions.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeQuestion(Question question) {
/*  87 */     if (question != null) {
/*     */       
/*  89 */       Integer iid = Integer.valueOf(question.getId());
/*  90 */       questions.remove(iid);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeQuestions(Player player) {
/* 100 */     Question[] quests = (Question[])questions.values().toArray((Object[])new Question[questions.values().size()]);
/* 101 */     for (int x = 0; x < quests.length; x++) {
/*     */       
/* 103 */       if (quests[x].getResponder() == player) {
/*     */         
/* 105 */         quests[x].clearResponder();
/* 106 */         questions.remove(Integer.valueOf(quests[x].getId()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void trimQuestions() {
/* 116 */     long now = System.currentTimeMillis();
/* 117 */     Set<Question> toRemove = new HashSet<>();
/* 118 */     for (Question lQuestion : questions.values()) {
/*     */       
/* 120 */       long maxTime = 900000L;
/*     */       
/* 122 */       if (lQuestion instanceof CultQuestion)
/* 123 */         maxTime = 1800000L; 
/* 124 */       if (lQuestion instanceof SpawnQuestion) {
/* 125 */         maxTime = 7200000L;
/*     */       }
/* 127 */       if (!(lQuestion instanceof SelectSpawnQuestion))
/*     */       {
/* 129 */         if (now - lQuestion.getSendTime() > maxTime || !lQuestion.getResponder().hasLink())
/* 130 */           toRemove.add(lQuestion); 
/*     */       }
/*     */     } 
/* 133 */     for (Question lQuestion : toRemove) {
/*     */       
/* 135 */       lQuestion.timedOut();
/* 136 */       removeQuestion(lQuestion);
/* 137 */       if (lQuestion.getResponder().isPlayer())
/*     */       {
/* 139 */         if (((Player)lQuestion.getResponder()).question == lQuestion)
/* 140 */           ((Player)lQuestion.getResponder()).question = null; 
/*     */       }
/*     */     } 
/* 143 */     if (logger.isLoggable(Level.FINER) && questions.size() > 0)
/*     */     {
/* 145 */       logger.finer("Size of question list=" + questions.size());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\Questions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */