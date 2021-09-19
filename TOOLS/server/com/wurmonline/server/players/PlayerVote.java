/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.support.VoteQuestion;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
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
/*     */ public class PlayerVote
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(VoteQuestion.class.getName());
/*     */ 
/*     */   
/*     */   private static final String ADDVOTE = "INSERT INTO VOTES (OPTION1,OPTION2,OPTION3,OPTION4,PLAYERID,QUESTIONID) VALUES(?,?,?,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String UPDATEVOTE = "UPDATE VOTES SET OPTION1=?,OPTION2=?,OPTION3=?,OPTION4=? WHERE PLAYERID=? AND QUESTIONID=?";
/*     */ 
/*     */   
/*     */   private final long playerId;
/*     */ 
/*     */   
/*     */   private final int questionId;
/*     */ 
/*     */   
/*     */   private boolean option1;
/*     */ 
/*     */   
/*     */   private boolean option2;
/*     */ 
/*     */   
/*     */   private boolean option3;
/*     */   
/*     */   private boolean option4;
/*     */ 
/*     */   
/*     */   public PlayerVote(long aPlayerId, int aQuestionId) {
/*  67 */     this(aPlayerId, aQuestionId, false, false, false, false);
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
/*     */   public PlayerVote(long aPlayerId, int aQuestionId, boolean aOption1, boolean aOption2, boolean aOption3, boolean aOption4) {
/*  83 */     this.playerId = aPlayerId;
/*  84 */     this.questionId = aQuestionId;
/*  85 */     this.option1 = aOption1;
/*  86 */     this.option2 = aOption2;
/*  87 */     this.option3 = aOption3;
/*  88 */     this.option4 = aOption4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(boolean aOption1, boolean aOption2, boolean aOption3, boolean aOption4) {
/*  93 */     this.option1 = aOption1;
/*  94 */     this.option2 = aOption2;
/*  95 */     this.option3 = aOption3;
/*  96 */     this.option4 = aOption4;
/*     */     
/*  98 */     if (!Servers.isThisLoginServer())
/*     */       return; 
/* 100 */     Connection dbcon = null;
/* 101 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 104 */       dbcon = DbConnector.getPlayerDbCon();
/* 105 */       ps = dbcon.prepareStatement("UPDATE VOTES SET OPTION1=?,OPTION2=?,OPTION3=?,OPTION4=? WHERE PLAYERID=? AND QUESTIONID=?");
/* 106 */       ps.setBoolean(1, this.option1);
/* 107 */       ps.setBoolean(2, this.option2);
/* 108 */       ps.setBoolean(3, this.option3);
/* 109 */       ps.setBoolean(4, this.option4);
/* 110 */       ps.setLong(5, this.playerId);
/* 111 */       ps.setInt(6, this.questionId);
/*     */       
/* 113 */       ps.executeUpdate();
/*     */     }
/* 115 */     catch (SQLException sqx) {
/*     */       
/* 117 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 121 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 122 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() {
/* 131 */     if (!Servers.isThisLoginServer())
/*     */       return; 
/* 133 */     Connection dbcon = null;
/* 134 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 137 */       dbcon = DbConnector.getPlayerDbCon();
/* 138 */       ps = dbcon.prepareStatement("INSERT INTO VOTES (OPTION1,OPTION2,OPTION3,OPTION4,PLAYERID,QUESTIONID) VALUES(?,?,?,?,?,?)");
/* 139 */       ps.setBoolean(1, this.option1);
/* 140 */       ps.setBoolean(2, this.option2);
/* 141 */       ps.setBoolean(3, this.option3);
/* 142 */       ps.setBoolean(4, this.option4);
/* 143 */       ps.setLong(5, this.playerId);
/* 144 */       ps.setInt(6, this.questionId);
/*     */       
/* 146 */       ps.executeUpdate();
/*     */     }
/* 148 */     catch (SQLException sqx) {
/*     */       
/* 150 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 154 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 155 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/* 161 */     return this.playerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQuestionId() {
/* 166 */     return this.questionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOption1() {
/* 171 */     return this.option1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOption2() {
/* 176 */     return this.option2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOption3() {
/* 181 */     return this.option3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOption4() {
/* 186 */     return this.option4;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasVoted() {
/* 191 */     return (this.option1 || this.option2 || this.option3 || this.option4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerVote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */