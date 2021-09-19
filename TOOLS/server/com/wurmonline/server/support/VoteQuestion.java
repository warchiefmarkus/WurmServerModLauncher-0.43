/*     */ package com.wurmonline.server.support;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.VoteServer;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerVotes;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.ProtoConstants;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class VoteQuestion
/*     */   implements MiscConstants, ProtoConstants, TimeConstants
/*     */ {
/*     */   private static final String ADDVOTINGQUESTION = "INSERT INTO VOTINGQUESTIONS (QUESTIONTITLE,QUESTIONTEXT,OPTION1_TEXT,OPTION2_TEXT,OPTION3_TEXT,OPTION4_TEXT,ALLOW_MULTIPLE,PREMIUM_ONLY,JK,MR,HOTS,FREEDOM,VOTE_START,VOTE_END,SENT,QUESTIONID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   private static final String UPDATEVOTINGQUESTION = "UPDATE VOTINGQUESTIONS SET QUESTIONTITLE=?,QUESTIONTEXT=?,OPTION1_TEXT=?,OPTION2_TEXT=?,OPTION3_TEXT=?,OPTION4_TEXT=?,ALLOW_MULTIPLE=?,PREMIUM_ONLY=?,JK=?,MR=?,HOTS=?,FREEDOM=?,VOTE_START=?,VOTE_END=?,SENT=?,QUESTIONID=?)";
/*     */   private static final String UPDATEVOTINGCOUNTS = "UPDATE VOTINGQUESTIONS SET VOTES_TOTAL=?,HAS_SUMMARY=1,OPTION1_COUNT=?,OPTION2_COUNT=?,OPTION3_COUNT=?,OPTION4_COUNT=? WHERE QUESTIONID=?";
/*     */   private static final String UPDATESENT = "UPDATE VOTINGQUESTIONS SET SENT=? WHERE QUESTIONID=?";
/*     */   private static final String UPDATEVOTEEND = "UPDATE VOTINGQUESTIONS SET VOTE_END=? WHERE QUESTIONID=?";
/*     */   private static final String UPDATETRELLOCARDID = "UPDATE VOTINGQUESTIONS SET TRELLOCARDID=? WHERE QUESTIONID=?";
/*     */   private static final String UPDATEARCHIVESTATE = "UPDATE VOTINGQUESTIONS SET ARCHIVESTATECODE=? WHERE QUESTIONID=?";
/*     */   private static final String DELETEVOTINGQUESTION = "DELETE FROM VOTINGQUESTIONS WHERE QUESTIONID=?";
/*     */   private static final String ADDVOTINGSERVERS = "INSERT INTO VOTINGSERVERS (QUESTIONID,SERVERID) VALUES(?,?)";
/*     */   private static final String DELETEVOTINGSERVERS = "DELETE FROM VOTINGSERVERS WHERE QUESTIONID=?";
/*  82 */   private static final Logger logger = Logger.getLogger(VoteQuestion.class.getName());
/*     */   
/*     */   public static final byte SENT_NOTHING = 0;
/*     */   
/*     */   public static final byte SENT_QUESTION = 1;
/*     */   
/*     */   public static final byte VOTING_IN_PROGRESS = 2;
/*     */   public static final byte VOTING_ENDED = 3;
/*     */   public static final byte SENT_SUMMARY = 4;
/*     */   private final int questionId;
/*     */   private String questionTitle;
/*     */   private String questionText;
/*     */   private String option1Text;
/*     */   private String option2Text;
/*     */   private String option3Text;
/*     */   private String option4Text;
/*     */   private boolean allowMultiple;
/*     */   private boolean premOnly;
/*     */   private boolean jk;
/*     */   private boolean mr;
/*     */   private boolean hots;
/*     */   private boolean freedom;
/*     */   private long start;
/*     */   private long end;
/* 106 */   private byte sent = 0;
/* 107 */   private short voteCount = 0;
/*     */   private boolean hasSummary = false;
/* 109 */   private short option1Count = 0;
/* 110 */   private short option2Count = 0;
/* 111 */   private short option3Count = 0;
/* 112 */   private short option4Count = 0;
/* 113 */   private String trelloCardId = "";
/* 114 */   private byte archiveState = 0;
/*     */   
/* 116 */   private List<VoteServer> servers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOnDb = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean serversOnLine = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoteQuestion(int aQuestionId, String aQuestionTitle, String aQuestionText, String aOption1Text, String aOption2Text, String aOption3Text, String aOption4Text, boolean aAllowMultiple, boolean aPremOnly, boolean aJK, boolean aMR, boolean aHoTs, boolean aFreedom, long voteStart, long voteEnd, List<VoteServer> theServers) {
/* 145 */     this(aQuestionId, aQuestionTitle, aQuestionText, aOption1Text, aOption2Text, aOption3Text, aOption4Text, aAllowMultiple, aPremOnly, aJK, aMR, aHoTs, aFreedom, voteStart, voteEnd, (byte)0, (short)0, false, (short)0, (short)0, (short)0, (short)0, "", (byte)0, theServers, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoteQuestion(int aQuestionId, String aQuestionTitle, String aQuestionText, String aOption1Text, String aOption2Text, String aOption3Text, String aOption4Text, boolean aAllowMultiple, boolean aPremOnly, boolean aJK, boolean aMR, boolean aHoTs, boolean aFreedom, long voteStart, long voteEnd) {
/* 177 */     this(aQuestionId, aQuestionTitle, aQuestionText, aOption1Text, aOption2Text, aOption3Text, aOption4Text, aAllowMultiple, aPremOnly, aJK, aMR, aHoTs, aFreedom, voteStart, voteEnd, (byte)1, (short)0, false, (short)0, (short)0, (short)0, (short)0, "", (byte)0, new ArrayList<>(), false);
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
/*     */   public VoteQuestion(int aQuestionId, String aQuestionTitle, String aQuestionText, String aOption1Text, String aOption2Text, String aOption3Text, String aOption4Text, boolean aAllowMultiple, boolean aPremOnly, boolean aJK, boolean aMR, boolean aHoTs, boolean aFreedom, long voteStart, long voteEnd, byte aSent, short aVoteCount, boolean aHasSummary, short aOption1Count, short aOption2Count, short aOption3Count, short aOption4Count, String aTrelloCardId, byte theArchiveState) {
/* 221 */     this(aQuestionId, aQuestionTitle, aQuestionText, aOption1Text, aOption2Text, aOption3Text, aOption4Text, aAllowMultiple, aPremOnly, aJK, aMR, aHoTs, aFreedom, voteStart, voteEnd, aSent, aVoteCount, aHasSummary, aOption1Count, aOption2Count, aOption3Count, aOption4Count, aTrelloCardId, theArchiveState, new ArrayList<>(), true);
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
/*     */   public VoteQuestion(int aQuestionId, String aQuestionTitle, String aQuestionText, String aOption1Text, String aOption2Text, String aOption3Text, String aOption4Text, boolean aAllowMultiple, boolean aPremOnly, boolean aJK, boolean aMR, boolean aHoTs, boolean aFreedom, long voteStart, long voteEnd, byte aSent, short aVoteCount, boolean aHasSummary, short aOption1Count, short aOption2Count, short aOption3Count, short aOption4Count, String aTrelloCardId, byte theArchiveState, List<VoteServer> theServers, boolean isFromDb) {
/* 266 */     this.questionId = aQuestionId;
/* 267 */     this.questionTitle = aQuestionTitle;
/* 268 */     this.questionText = aQuestionText;
/* 269 */     this.option1Text = aOption1Text;
/* 270 */     this.option2Text = aOption2Text;
/* 271 */     this.option3Text = aOption3Text;
/* 272 */     this.option4Text = aOption4Text;
/* 273 */     this.allowMultiple = aAllowMultiple;
/* 274 */     this.premOnly = aPremOnly;
/* 275 */     this.jk = aJK;
/* 276 */     this.mr = aMR;
/* 277 */     this.hots = aHoTs;
/* 278 */     this.freedom = aFreedom;
/* 279 */     this.start = voteStart;
/* 280 */     this.end = voteEnd;
/* 281 */     this.sent = aSent;
/* 282 */     this.voteCount = aVoteCount;
/* 283 */     this.hasSummary = aHasSummary;
/* 284 */     this.option1Count = aOption1Count;
/* 285 */     this.option2Count = aOption2Count;
/* 286 */     this.option3Count = aOption3Count;
/* 287 */     this.option4Count = aOption4Count;
/* 288 */     this.trelloCardId = aTrelloCardId;
/* 289 */     this.archiveState = theArchiveState;
/* 290 */     this.servers = theServers;
/* 291 */     this.isOnDb = isFromDb;
/*     */     
/* 293 */     if (!isFromDb) {
/* 294 */       dbAddOrUpdateVotingQuestion();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getQuestionId() {
/* 299 */     return this.questionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getQuestionTitle() {
/* 304 */     return this.questionTitle;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getQuestionText() {
/* 309 */     return this.questionText;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOption1Text() {
/* 314 */     return this.option1Text;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOption2Text() {
/* 319 */     return this.option2Text;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOption3Text() {
/* 324 */     return this.option3Text;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOption4Text() {
/* 329 */     return this.option4Text;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowMultiple() {
/* 334 */     return this.allowMultiple;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPremOnly() {
/* 339 */     return this.premOnly;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isJK() {
/* 344 */     return this.jk;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMR() {
/* 349 */     return this.mr;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHots() {
/* 354 */     return this.hots;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFreedom() {
/* 359 */     return this.freedom;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getVoteStart() {
/* 364 */     return this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getVoteEnd() {
/* 369 */     return this.end;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getSent() {
/* 374 */     return this.sent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSent(byte newSent) {
/* 379 */     this.sent = newSent;
/* 380 */     dbUpdateSent();
/*     */   }
/*     */ 
/*     */   
/*     */   public short getVoteCount() {
/* 385 */     return this.voteCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSummary() {
/* 390 */     return this.hasSummary;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getOption1Count() {
/* 395 */     return this.option1Count;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getOption2Count() {
/* 400 */     return this.option2Count;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getOption3Count() {
/* 405 */     return this.option3Count;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getOption4Count() {
/* 410 */     return this.option4Count;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTrelloCardId() {
/* 415 */     return this.trelloCardId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTrelloCardId(String newTrelloCardId) {
/* 420 */     this.trelloCardId = newTrelloCardId;
/* 421 */     dbUpdateTrelloCardId();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getArchiveState() {
/* 426 */     return this.archiveState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setArchiveState(byte newArchiveState) {
/* 431 */     this.archiveState = newArchiveState;
/* 432 */     dbUpdateArchiveState();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VoteServer> getServers() {
/* 437 */     return this.servers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addServer(int aServerId, short aTotal, short count1, short count2, short count3, short count4) {
/* 443 */     this.servers.add(new VoteServer(this.questionId, aServerId, aTotal, count1, count2, count3, count4));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addServer(VoteServer aServer) {
/* 448 */     this.servers.add(aServer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addServers(List<VoteServer> theServers) {
/* 453 */     this.servers = theServers;
/* 454 */     dbAddOrUpdateVotingServers();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canVote(Player player) {
/* 460 */     if (player.getPower() != 0)
/*     */     {
/* 462 */       return false;
/*     */     }
/* 464 */     if (!isActive()) {
/* 465 */       return false;
/*     */     }
/*     */     
/* 468 */     if (this.premOnly && !player.isPaying()) {
/* 469 */       return false;
/*     */     }
/* 471 */     if (this.jk && player.getKingdomTemplateId() == 1) {
/* 472 */       return true;
/*     */     }
/* 474 */     if (this.mr && player.getKingdomTemplateId() == 2) {
/* 475 */       return true;
/*     */     }
/* 477 */     if (this.hots && player.getKingdomTemplateId() == 3) {
/* 478 */       return true;
/*     */     }
/* 480 */     if (this.freedom && player.getKingdomTemplateId() == 4) {
/* 481 */       return true;
/*     */     }
/* 483 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean justStarted() {
/* 488 */     return (this.start < System.currentTimeMillis() && this.sent == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 495 */     if (Servers.isThisLoginServer()) {
/*     */       
/* 497 */       boolean found = false;
/* 498 */       for (VoteServer vs : this.servers) {
/*     */         
/* 500 */         if (vs.getServerId() == Servers.getLocalServerId()) {
/*     */           
/* 502 */           found = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 506 */       if (!found)
/* 507 */         return false; 
/*     */     } 
/* 509 */     return (this.start < System.currentTimeMillis() && this.end > 
/* 510 */       System.currentTimeMillis() && this.sent == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endVoting() {
/* 516 */     if (justEnded()) {
/* 517 */       this.sent = 3;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean justEnded() {
/* 522 */     return (hasEnded() && this.sent == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnded() {
/* 527 */     return (this.end < System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMakeSummary() {
/* 532 */     return (this.sent == 3 && !this.hasSummary && areServerOnline());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean aboutToStart() {
/* 538 */     if (this.sent == 0)
/*     */     {
/*     */       
/* 541 */       if (System.currentTimeMillis() > this.start - 300000L)
/*     */       {
/*     */         
/* 544 */         return areServerOnline();
/*     */       }
/*     */     }
/* 547 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean areServerOnline() {
/* 552 */     for (VoteServer vs : this.servers) {
/*     */       
/* 554 */       if (!Servers.getServerWithId(vs.getServerId()).isAvailable(5, true)) {
/*     */         
/* 556 */         if (this.serversOnLine)
/*     */         {
/* 558 */           logger.log(Level.WARNING, "Not all required servers online, so not sending question (yet) to other servers."); } 
/* 559 */         this.serversOnLine = false;
/* 560 */         return false;
/*     */       } 
/*     */     } 
/* 563 */     if (!this.serversOnLine) {
/* 564 */       logger.log(Level.INFO, "Questions servers are back online, so sending question to them.");
/*     */     }
/* 566 */     this.serversOnLine = true;
/* 567 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setArchive() {
/* 572 */     if (System.currentTimeMillis() > this.end + 604800000L && this.archiveState == 0)
/*     */     {
/*     */       
/* 575 */       if (Servers.isThisLoginServer()) {
/* 576 */         this.archiveState = 2;
/*     */       } else {
/* 578 */         this.archiveState = 3;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeVoting(long aVoteEnd) {
/* 589 */     this.end = aVoteEnd;
/* 590 */     dbUpdateVoteEnd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(String aQuestionTitle, String aQuestionText, String aOption1Text, String aOption2Text, String aOption3Text, String aOption4Text, boolean aAllowMultiple, boolean aPremOnly, boolean aJK, boolean aMR, boolean aHoTs, boolean aFreedom, long voteStart, long voteEnd, List<VoteServer> aServers) {
/* 599 */     this.questionTitle = aQuestionTitle;
/* 600 */     this.questionText = aQuestionText;
/* 601 */     this.option1Text = aOption1Text;
/* 602 */     this.option2Text = aOption2Text;
/* 603 */     this.option3Text = aOption3Text;
/* 604 */     this.option4Text = aOption4Text;
/* 605 */     this.allowMultiple = aAllowMultiple;
/* 606 */     this.premOnly = aPremOnly;
/* 607 */     this.jk = aJK;
/* 608 */     this.mr = aMR;
/* 609 */     this.hots = aHoTs;
/* 610 */     this.freedom = aFreedom;
/* 611 */     this.start = voteStart;
/* 612 */     this.end = voteEnd;
/* 613 */     this.servers = aServers;
/*     */     
/* 615 */     dbAddOrUpdateVotingQuestion();
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() {
/* 620 */     dbAddOrUpdateVotingQuestion();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 626 */     PlayerVotes.deletePlayerVotes(this.questionId);
/*     */ 
/*     */     
/* 629 */     dbDelete();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbAddOrUpdateVotingQuestion() {
/* 637 */     Connection dbcon = null;
/* 638 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 641 */       dbcon = DbConnector.getLoginDbCon();
/* 642 */       if (this.isOnDb) {
/* 643 */         ps = dbcon.prepareStatement("UPDATE VOTINGQUESTIONS SET QUESTIONTITLE=?,QUESTIONTEXT=?,OPTION1_TEXT=?,OPTION2_TEXT=?,OPTION3_TEXT=?,OPTION4_TEXT=?,ALLOW_MULTIPLE=?,PREMIUM_ONLY=?,JK=?,MR=?,HOTS=?,FREEDOM=?,VOTE_START=?,VOTE_END=?,SENT=?,QUESTIONID=?)");
/*     */       } else {
/* 645 */         ps = dbcon.prepareStatement("INSERT INTO VOTINGQUESTIONS (QUESTIONTITLE,QUESTIONTEXT,OPTION1_TEXT,OPTION2_TEXT,OPTION3_TEXT,OPTION4_TEXT,ALLOW_MULTIPLE,PREMIUM_ONLY,JK,MR,HOTS,FREEDOM,VOTE_START,VOTE_END,SENT,QUESTIONID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/* 646 */       }  ps.setString(1, this.questionTitle);
/* 647 */       ps.setString(2, this.questionText);
/* 648 */       ps.setString(3, this.option1Text);
/* 649 */       ps.setString(4, this.option2Text);
/* 650 */       ps.setString(5, this.option3Text);
/* 651 */       ps.setString(6, this.option4Text);
/* 652 */       ps.setBoolean(7, this.allowMultiple);
/* 653 */       ps.setBoolean(8, this.premOnly);
/* 654 */       ps.setBoolean(9, this.jk);
/* 655 */       ps.setBoolean(10, this.mr);
/* 656 */       ps.setBoolean(11, this.hots);
/* 657 */       ps.setBoolean(12, this.freedom);
/* 658 */       ps.setLong(13, this.start);
/* 659 */       ps.setLong(14, this.end);
/* 660 */       ps.setByte(15, this.sent);
/* 661 */       ps.setInt(16, this.questionId);
/*     */       
/* 663 */       ps.executeUpdate();
/* 664 */       dbAddOrUpdateVotingServers();
/* 665 */       this.isOnDb = true;
/*     */     }
/* 667 */     catch (SQLException sqx) {
/*     */       
/* 669 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 673 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 674 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dbAddOrUpdateVotingServers() {
/* 680 */     Connection dbcon = null;
/* 681 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 684 */       dbcon = DbConnector.getLoginDbCon();
/* 685 */       if (this.servers.size() > 0) {
/*     */         
/* 687 */         if (this.isOnDb) {
/*     */ 
/*     */           
/* 690 */           ps = dbcon.prepareStatement("DELETE FROM VOTINGSERVERS WHERE QUESTIONID=?");
/* 691 */           ps.setInt(1, this.questionId);
/* 692 */           ps.executeUpdate();
/* 693 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         } 
/*     */         
/* 696 */         ps = dbcon.prepareStatement("INSERT INTO VOTINGSERVERS (QUESTIONID,SERVERID) VALUES(?,?)");
/* 697 */         for (VoteServer serv : this.servers)
/*     */         {
/* 699 */           ps.setInt(1, serv.getQuestionId());
/* 700 */           ps.setInt(2, serv.getServerId());
/* 701 */           ps.executeUpdate();
/*     */         }
/*     */       
/*     */       } 
/* 705 */     } catch (SQLException sqx) {
/*     */       
/* 707 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 711 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 712 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveSummary(short aVoteCount, short count1, short count2, short count3, short count4) {
/* 719 */     this.voteCount = aVoteCount;
/* 720 */     this.hasSummary = true;
/* 721 */     this.option1Count = count1;
/* 722 */     this.option2Count = count2;
/* 723 */     this.option3Count = count3;
/* 724 */     this.option4Count = count4;
/* 725 */     dbUpdateVotingCounts();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbUpdateVotingCounts() {
/* 733 */     Connection dbcon = null;
/* 734 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 737 */       dbcon = DbConnector.getLoginDbCon();
/* 738 */       ps = dbcon.prepareStatement("UPDATE VOTINGQUESTIONS SET VOTES_TOTAL=?,HAS_SUMMARY=1,OPTION1_COUNT=?,OPTION2_COUNT=?,OPTION3_COUNT=?,OPTION4_COUNT=? WHERE QUESTIONID=?");
/* 739 */       ps.setShort(1, this.voteCount);
/*     */       
/* 741 */       ps.setShort(2, this.option1Count);
/* 742 */       ps.setShort(3, this.option2Count);
/* 743 */       ps.setShort(4, this.option3Count);
/* 744 */       ps.setShort(5, this.option4Count);
/* 745 */       ps.setInt(6, this.questionId);
/*     */       
/* 747 */       ps.executeUpdate();
/*     */     }
/* 749 */     catch (SQLException sqx) {
/*     */       
/* 751 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 755 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 756 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbUpdateSent() {
/* 765 */     Connection dbcon = null;
/* 766 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 769 */       dbcon = DbConnector.getLoginDbCon();
/* 770 */       ps = dbcon.prepareStatement("UPDATE VOTINGQUESTIONS SET SENT=? WHERE QUESTIONID=?");
/* 771 */       ps.setByte(1, this.sent);
/* 772 */       ps.setInt(2, this.questionId);
/*     */       
/* 774 */       ps.executeUpdate();
/*     */     }
/* 776 */     catch (SQLException sqx) {
/*     */       
/* 778 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 782 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 783 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbUpdateVoteEnd() {
/* 792 */     Connection dbcon = null;
/* 793 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 796 */       dbcon = DbConnector.getLoginDbCon();
/* 797 */       ps = dbcon.prepareStatement("UPDATE VOTINGQUESTIONS SET VOTE_END=? WHERE QUESTIONID=?");
/* 798 */       ps.setLong(1, this.end);
/* 799 */       ps.setInt(2, this.questionId);
/*     */       
/* 801 */       ps.executeUpdate();
/*     */     }
/* 803 */     catch (SQLException sqx) {
/*     */       
/* 805 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 809 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 810 */       DbConnector.returnConnection(dbcon);
/*     */     } 
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
/*     */   private void dbUpdateTrelloCardId() {
/* 854 */     Connection dbcon = null;
/* 855 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 858 */       dbcon = DbConnector.getLoginDbCon();
/* 859 */       ps = dbcon.prepareStatement("UPDATE VOTINGQUESTIONS SET TRELLOCARDID=? WHERE QUESTIONID=?");
/* 860 */       ps.setString(1, this.trelloCardId);
/* 861 */       ps.setInt(2, this.questionId);
/*     */       
/* 863 */       ps.executeUpdate();
/*     */     }
/* 865 */     catch (SQLException sqx) {
/*     */       
/* 867 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 871 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 872 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dbUpdateArchiveState() {
/* 878 */     Connection dbcon = null;
/* 879 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 882 */       dbcon = DbConnector.getLoginDbCon();
/* 883 */       ps = dbcon.prepareStatement("UPDATE VOTINGQUESTIONS SET ARCHIVESTATECODE=? WHERE QUESTIONID=?");
/* 884 */       ps.setByte(1, this.archiveState);
/* 885 */       ps.setInt(2, this.questionId);
/*     */       
/* 887 */       ps.executeUpdate();
/*     */     }
/* 889 */     catch (SQLException sqx) {
/*     */       
/* 891 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 895 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 896 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dbDelete() {
/* 902 */     Connection dbcon = null;
/* 903 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 906 */       dbcon = DbConnector.getLoginDbCon();
/* 907 */       ps = dbcon.prepareStatement("DELETE FROM VOTINGSERVERS WHERE QUESTIONID=?");
/* 908 */       ps.setInt(1, this.questionId);
/* 909 */       ps.executeUpdate();
/*     */       
/* 911 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 913 */       ps = dbcon.prepareStatement("DELETE FROM VOTINGQUESTIONS WHERE QUESTIONID=?");
/* 914 */       ps.setInt(1, this.questionId);
/* 915 */       ps.executeUpdate();
/*     */     }
/* 917 */     catch (SQLException sqx) {
/*     */       
/* 919 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 923 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 924 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 936 */     return "VoteQuestion [id=" + this.questionId + ", title=\"" + this.questionTitle + ", text=\"" + this.questionText + "\", opt1=\"" + this.option1Text + "\", opt2=\"" + this.option2Text + "\"" + (
/*     */ 
/*     */       
/* 939 */       (this.option3Text.length() > 0) ? (", opt3=\"" + this.option3Text + "\"") : "") + (
/* 940 */       (this.option4Text.length() > 0) ? (", opt4=\"" + this.option4Text + "\"") : "") + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\VoteQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */