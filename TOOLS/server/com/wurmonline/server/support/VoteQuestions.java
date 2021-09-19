/*     */ package com.wurmonline.server.support;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.VoteServer;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerVote;
/*     */ import com.wurmonline.server.players.PlayerVotes;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.webinterface.WcVoting;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class VoteQuestions
/*     */   implements CounterTypes, TimeConstants
/*     */ {
/*  52 */   private static Logger logger = Logger.getLogger(VoteQuestions.class.getName());
/*  53 */   private static final Map<Integer, VoteQuestion> voteQuestions = new ConcurrentHashMap<>();
/*  54 */   private static final ConcurrentLinkedDeque<VoteQuestionsQueue> questionsQueue = new ConcurrentLinkedDeque<>();
/*     */   
/*  56 */   private static int lastQuestionId = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOADALLQUESTIONS = "SELECT * FROM VOTINGQUESTIONS";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOADALLVOTINGSERVERS = "SELECT * FROM VOTINGSERVERS";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadVoteQuestions() {
/*  71 */     long start = System.nanoTime();
/*     */     
/*     */     try {
/*  74 */       dbLoadAllVoteQuestions();
/*     */     }
/*  76 */     catch (Exception ex) {
/*     */       
/*  78 */       logger.log(Level.WARNING, "Problems loading Vote Questions", ex);
/*     */     } 
/*  80 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  81 */     logger.log(Level.INFO, "Loaded " + voteQuestions.size() + " Vote Questions. It took " + lElapsedTime + " millis.");
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
/*     */   
/*     */   public static VoteQuestion addVoteQuestion(VoteQuestion newVoteQuestion, boolean saveit) {
/*  94 */     if (voteQuestions.containsKey(Integer.valueOf(newVoteQuestion.getQuestionId()))) {
/*     */       
/*  96 */       VoteQuestion oldVoteQuestion = voteQuestions.get(Integer.valueOf(newVoteQuestion.getQuestionId()));
/*     */       
/*  98 */       oldVoteQuestion.update(newVoteQuestion.getQuestionTitle(), newVoteQuestion.getQuestionText(), newVoteQuestion
/*  99 */           .getOption1Text(), newVoteQuestion.getOption2Text(), newVoteQuestion
/* 100 */           .getOption3Text(), newVoteQuestion.getOption4Text(), newVoteQuestion
/* 101 */           .isAllowMultiple(), newVoteQuestion.isPremOnly(), newVoteQuestion
/* 102 */           .isJK(), newVoteQuestion.isMR(), newVoteQuestion
/* 103 */           .isHots(), newVoteQuestion.isFreedom(), newVoteQuestion
/* 104 */           .getVoteStart(), newVoteQuestion.getVoteEnd(), newVoteQuestion
/* 105 */           .getServers());
/* 106 */       return oldVoteQuestion;
/*     */     } 
/*     */ 
/*     */     
/* 110 */     voteQuestions.put(Integer.valueOf(newVoteQuestion.getQuestionId()), newVoteQuestion);
/*     */     
/* 112 */     if (saveit) {
/* 113 */       newVoteQuestion.save();
/*     */     }
/* 115 */     return newVoteQuestion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion[] getArchiveVoteQuestions() {
/* 126 */     Map<Integer, VoteQuestion> archiveVoteQuestions = new HashMap<>();
/* 127 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/*     */       
/* 129 */       VoteQuestion voteQuestion = entry.getValue();
/* 130 */       if (voteQuestion.getArchiveState() == 2)
/*     */       {
/* 132 */         archiveVoteQuestions.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 135 */     return (VoteQuestion[])archiveVoteQuestions.values().toArray((Object[])new VoteQuestion[archiveVoteQuestions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion[] getFinishedQuestions() {
/* 145 */     Map<Integer, VoteQuestion> finishedVoteQuestions = new HashMap<>();
/* 146 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/*     */       
/* 148 */       VoteQuestion voteQuestion = entry.getValue();
/* 149 */       if (voteQuestion.hasSummary() && voteQuestion
/* 150 */         .getSent() == 4)
/*     */       {
/* 152 */         finishedVoteQuestions.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 155 */     return (VoteQuestion[])finishedVoteQuestions.values().toArray((Object[])new VoteQuestion[finishedVoteQuestions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteVoteQuestion(int aId) {
/* 164 */     VoteQuestion vq = getVoteQuestion(aId);
/* 165 */     if (Servers.isThisLoginServer() && vq != null && vq.getSent() == 1) {
/*     */       
/* 167 */       WcVoting wv = new WcVoting((byte)5, aId);
/* 168 */       for (VoteServer vs : vq.getServers()) {
/*     */ 
/*     */         
/* 171 */         if (vs.getServerId() != Servers.getLocalServerId())
/* 172 */           wv.sendToServer(vs.getServerId()); 
/*     */       } 
/*     */     } 
/* 175 */     voteQuestions.remove(Integer.valueOf(aId));
/* 176 */     if (vq != null) {
/* 177 */       vq.delete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeVoteing(int aId, long aVoteEnd) {
/* 185 */     VoteQuestion vq = getVoteQuestion(aId);
/* 186 */     if (Servers.isThisLoginServer() && vq != null && vq.getSent() == 1) {
/*     */       
/* 188 */       WcVoting wv = new WcVoting((byte)6, aId, aVoteEnd);
/* 189 */       for (VoteServer vs : vq.getServers()) {
/*     */ 
/*     */         
/* 192 */         if (vs.getServerId() != Servers.getLocalServerId()) {
/* 193 */           wv.sendToServer(vs.getServerId());
/*     */         }
/*     */       } 
/*     */     } 
/* 197 */     vq.closeVoting(aVoteEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion getVoteQuestion(int aId) {
/* 208 */     return voteQuestions.get(Integer.valueOf(aId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion[] getVoteQuestions(Player player) {
/* 218 */     Map<Integer, VoteQuestion> playerVoteQuestions = new HashMap<>();
/* 219 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/*     */       
/* 221 */       VoteQuestion voteQuestion = entry.getValue();
/* 222 */       if (voteQuestion.canVote(player))
/*     */       {
/* 224 */         playerVoteQuestions.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 227 */     return (VoteQuestion[])playerVoteQuestions.values().toArray((Object[])new VoteQuestion[playerVoteQuestions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getVoteQuestionIds(Player player) {
/* 238 */     VoteQuestion[] vqs = getVoteQuestions(player);
/* 239 */     int[] ids = new int[vqs.length];
/* 240 */     for (int i = 0; i < vqs.length; i++)
/* 241 */       ids[i] = vqs[i].getQuestionId(); 
/* 242 */     return ids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion[] getVoteQuestionsAboutToStart() {
/* 252 */     Map<Integer, VoteQuestion> playerVoteQuestions = new HashMap<>();
/* 253 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/*     */       
/* 255 */       VoteQuestion voteQuestion = entry.getValue();
/* 256 */       if (voteQuestion.aboutToStart())
/*     */       {
/* 258 */         playerVoteQuestions.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 261 */     return (VoteQuestion[])playerVoteQuestions.values().toArray((Object[])new VoteQuestion[playerVoteQuestions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion[] getVoteQuestionsNeedingSummary() {
/* 271 */     Map<Integer, VoteQuestion> playerVoteQuestions = new HashMap<>();
/* 272 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/*     */       
/* 274 */       VoteQuestion voteQuestion = entry.getValue();
/* 275 */       if (voteQuestion.canMakeSummary())
/*     */       {
/* 277 */         playerVoteQuestions.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 280 */     return (VoteQuestion[])playerVoteQuestions.values().toArray((Object[])new VoteQuestion[playerVoteQuestions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoteQuestion[] getVoteQuestions() {
/* 290 */     return (VoteQuestion[])voteQuestions.values().toArray((Object[])new VoteQuestion[voteQuestions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbLoadAllVoteQuestions() {
/* 298 */     Connection dbcon = null;
/* 299 */     PreparedStatement ps = null;
/* 300 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 303 */       dbcon = DbConnector.getLoginDbCon();
/* 304 */       ps = dbcon.prepareStatement("SELECT * FROM VOTINGQUESTIONS");
/* 305 */       rs = ps.executeQuery();
/* 306 */       while (rs.next()) {
/*     */         
/* 308 */         int aQuestionId = rs.getInt("QUESTIONID");
/* 309 */         String aQuestionTitle = rs.getString("QUESTIONTITLE");
/* 310 */         String aQuestionText = rs.getString("QUESTIONTEXT");
/* 311 */         String aOption1Text = rs.getString("OPTION1_TEXT");
/* 312 */         String aOption2Text = rs.getString("OPTION2_TEXT");
/* 313 */         String aOption3Text = rs.getString("OPTION3_TEXT");
/* 314 */         String aOption4Text = rs.getString("OPTION4_TEXT");
/* 315 */         boolean aAllowMultiple = rs.getBoolean("ALLOW_MULTIPLE");
/* 316 */         boolean aPremOnly = rs.getBoolean("PREMIUM_ONLY");
/* 317 */         boolean aJK = rs.getBoolean("JK");
/* 318 */         boolean aMR = rs.getBoolean("MR");
/* 319 */         boolean aHots = rs.getBoolean("HOTS");
/* 320 */         boolean aFreedom = rs.getBoolean("FREEDOM");
/* 321 */         long aStart = rs.getLong("VOTE_START");
/* 322 */         long aEnd = rs.getLong("VOTE_END");
/* 323 */         byte aSent = rs.getByte("SENT");
/* 324 */         short aVotesTotal = rs.getShort("VOTES_TOTAL");
/* 325 */         boolean aHasSummary = rs.getBoolean("HAS_SUMMARY");
/* 326 */         short aOption1Count = rs.getShort("OPTION1_COUNT");
/* 327 */         short aOption2Count = rs.getShort("OPTION2_COUNT");
/* 328 */         short aOption3Count = rs.getShort("OPTION3_COUNT");
/* 329 */         short aOption4Count = rs.getShort("OPTION4_COUNT");
/* 330 */         String aTrelloCardId = rs.getString("TRELLOCARDID");
/* 331 */         byte aArchiveState = rs.getByte("ARCHIVESTATECODE");
/*     */         
/* 333 */         if (aQuestionId > lastQuestionId) lastQuestionId = aQuestionId;
/*     */         
/* 335 */         addVoteQuestion(new VoteQuestion(aQuestionId, aQuestionTitle, aQuestionText, aOption1Text, aOption2Text, aOption3Text, aOption4Text, aAllowMultiple, aPremOnly, aJK, aMR, aHots, aFreedom, aStart, aEnd, aSent, aVotesTotal, aHasSummary, aOption1Count, aOption2Count, aOption3Count, aOption4Count, aTrelloCardId, aArchiveState), false);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 343 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 344 */       ps = dbcon.prepareStatement("SELECT * FROM VOTINGSERVERS");
/* 345 */       rs = ps.executeQuery();
/* 346 */       while (rs.next())
/*     */       {
/* 348 */         int aQuestionId = rs.getInt("QUESTIONID");
/* 349 */         int aServerId = rs.getInt("SERVERID");
/* 350 */         short aVotesTotal = rs.getShort("VOTES_TOTAL");
/* 351 */         short aOption1Count = rs.getShort("OPTION1_COUNT");
/* 352 */         short aOption2Count = rs.getShort("OPTION2_COUNT");
/* 353 */         short aOption3Count = rs.getShort("OPTION3_COUNT");
/* 354 */         short aOption4Count = rs.getShort("OPTION4_COUNT");
/*     */         
/* 356 */         getVoteQuestion(aQuestionId).addServer(aServerId, aVotesTotal, aOption1Count, aOption2Count, aOption3Count, aOption4Count);
/*     */       }
/*     */     
/*     */     }
/* 360 */     catch (SQLException sqx) {
/*     */       
/* 362 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 366 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 367 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getNextQuestionId() {
/* 373 */     return ++lastQuestionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void queueAddVoteQuestion(int aQuestionId, String aQuestionTitle, String aQuestionText, String aOption1Text, String aOption2Text, String aOption3Text, String aOption4Text, boolean aAllowMultiple, boolean aPremOnly, boolean aJK, boolean aMR, boolean aHoTs, boolean aFreedom, long voteStart, long voteEnd) {
/* 383 */     VoteQuestion newVoteQuestion = new VoteQuestion(aQuestionId, aQuestionTitle, aQuestionText, aOption1Text, aOption2Text, aOption3Text, aOption4Text, aAllowMultiple, aPremOnly, aJK, aMR, aHoTs, aFreedom, voteStart, voteEnd);
/*     */ 
/*     */     
/* 386 */     questionsQueue.add(new VoteQuestionsQueue((byte)0, newVoteQuestion));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void queueRemoveVoteQuestion(int aQuestionId) {
/* 391 */     questionsQueue.add(new VoteQuestionsQueue((byte)1, aQuestionId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void queueCloseVoteQuestion(int aQuestionId, long newEnd) {
/* 396 */     questionsQueue.add(new VoteQuestionsQueue((byte)2, aQuestionId, newEnd));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void queueSetTrelloCardId(int aQuestionId, String aTrelloCardId) {
/* 401 */     questionsQueue.add(new VoteQuestionsQueue((byte)3, aQuestionId, aTrelloCardId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void queueSetArchiveState(int aQuestionId, byte newArchiveState) {
/* 406 */     questionsQueue.add(new VoteQuestionsQueue((byte)4, aQuestionId, newArchiveState));
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
/*     */   public static final void handleVoting() {
/* 430 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/* 431 */       ((VoteQuestion)entry.getValue()).endVoting();
/*     */     }
/*     */     
/* 434 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/* 435 */       ((VoteQuestion)entry.getValue()).setArchive();
/*     */     }
/* 437 */     if (Servers.isThisLoginServer()) {
/*     */       
/* 439 */       VoteQuestion[] vqStarting = getVoteQuestionsAboutToStart();
/* 440 */       if (vqStarting.length > 0) {
/*     */         
/* 442 */         for (VoteQuestion vq : vqStarting) {
/*     */ 
/*     */ 
/*     */           
/* 446 */           WcVoting wv = new WcVoting(vq);
/* 447 */           for (VoteServer vs : vq.getServers()) {
/*     */ 
/*     */             
/* 450 */             if (vs.getServerId() != Servers.getLocalServerId())
/* 451 */               wv.sendToServer(vs.getServerId()); 
/*     */           } 
/* 453 */           vq.setSent((byte)1);
/*     */         } 
/*     */         return;
/*     */       } 
/* 457 */       VoteQuestion[] vqEnding = getVoteQuestionsNeedingSummary();
/* 458 */       if (vqEnding.length > 0) {
/*     */         
/* 460 */         for (VoteQuestion vq : vqEnding) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 465 */           short total = 0;
/* 466 */           short count1 = 0;
/* 467 */           short count2 = 0;
/* 468 */           short count3 = 0;
/* 469 */           short count4 = 0;
/* 470 */           for (PlayerVote pv : PlayerVotes.getPlayerVotesByQuestion(vq.getQuestionId())) {
/*     */             
/* 472 */             total = (short)(total + 1);
/* 473 */             if (pv.getOption1()) count1 = (short)(count1 + 1); 
/* 474 */             if (pv.getOption2()) count2 = (short)(count2 + 1); 
/* 475 */             if (pv.getOption3()) count3 = (short)(count3 + 1); 
/* 476 */             if (pv.getOption4()) count4 = (short)(count4 + 1); 
/*     */           } 
/* 478 */           vq.saveSummary(total, count1, count2, count3, count4);
/*     */ 
/*     */           
/* 481 */           WcVoting wv = new WcVoting(vq.getQuestionId(), vq.getVoteCount(), vq.getOption1Count(), vq.getOption2Count(), vq.getOption3Count(), vq.getOption4Count());
/* 482 */           for (VoteServer vs : vq.getServers()) {
/*     */ 
/*     */             
/* 485 */             if (vs.getServerId() != Servers.getLocalServerId())
/* 486 */               wv.sendToServer(vs.getServerId()); 
/*     */           } 
/* 488 */           vq.setSent((byte)4);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 495 */     VoteQuestionsQueue vqq = questionsQueue.pollFirst();
/* 496 */     while (vqq != null) {
/*     */       
/* 498 */       vqq.action();
/* 499 */       vqq = questionsQueue.pollFirst();
/*     */     } 
/*     */ 
/*     */     
/* 503 */     for (Map.Entry<Integer, VoteQuestion> entry : voteQuestions.entrySet()) {
/*     */       
/* 505 */       VoteQuestion voteQuestion = entry.getValue();
/* 506 */       if (voteQuestion.justStarted()) {
/*     */ 
/*     */         
/* 509 */         voteQuestion.setSent((byte)2);
/*     */         
/* 511 */         Players.sendVotingOpen(voteQuestion);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void handleArchiveTickets() {
/* 521 */     for (VoteQuestion vq : voteQuestions.values()) {
/*     */       
/* 523 */       if (vq.getArchiveState() == 3)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 529 */         deleteVoteQuestion(vq.getQuestionId());
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\VoteQuestions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */