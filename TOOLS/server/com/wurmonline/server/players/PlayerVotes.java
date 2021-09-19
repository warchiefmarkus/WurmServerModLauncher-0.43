/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class PlayerVotes
/*     */ {
/*  44 */   private static Logger logger = Logger.getLogger(PlayerVotes.class.getName());
/*  45 */   private static final Map<Long, PlayerVotesByPlayer> playerVotes = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOADALLPLAYERVOTES = "SELECT * FROM VOTES";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DELETEQUESTIONVOTES = "DELETE FROM VOTES WHERE QUESTIONID=?";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadAllPlayerVotes() {
/*  61 */     long start = System.nanoTime();
/*     */     
/*     */     try {
/*  64 */       dbLoadAllPlayerVotes();
/*     */     }
/*  66 */     catch (Exception ex) {
/*     */       
/*  68 */       logger.log(Level.WARNING, "Problems loading Player Votes.", ex);
/*     */     } 
/*  70 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  71 */     logger.log(Level.INFO, "Loaded " + playerVotes.size() + " Player Votes. It took " + lElapsedTime + " millis.");
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
/*     */   public static PlayerVote addPlayerVote(PlayerVote newPlayerVote, boolean saveit) {
/*  84 */     Long pId = Long.valueOf(newPlayerVote.getPlayerId());
/*     */     
/*  86 */     if (!playerVotes.containsKey(pId)) {
/*  87 */       playerVotes.put(pId, new PlayerVotesByPlayer());
/*     */     }
/*  89 */     PlayerVotesByPlayer pvbp = playerVotes.get(pId);
/*  90 */     PlayerVote oldPlayerVote = pvbp.get(newPlayerVote.getQuestionId());
/*  91 */     if (oldPlayerVote != null) {
/*     */ 
/*     */       
/*  94 */       oldPlayerVote.update(newPlayerVote.getOption1(), newPlayerVote.getOption2(), newPlayerVote
/*  95 */           .getOption3(), newPlayerVote.getOption4());
/*  96 */       return oldPlayerVote;
/*     */     } 
/*     */ 
/*     */     
/* 100 */     pvbp.add(newPlayerVote);
/*     */     
/* 102 */     if (saveit) {
/* 103 */       newPlayerVote.save();
/*     */     }
/* 105 */     return newPlayerVote;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PlayerVote[] getPlayerVotes(long aPlayerId) {
/* 111 */     PlayerVotesByPlayer pvbp = playerVotes.get(Long.valueOf(aPlayerId));
/* 112 */     if (pvbp == null)
/* 113 */       return new PlayerVote[0]; 
/* 114 */     return pvbp.getVotes();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasPlayerVotedByQuestion(long aPlayerId, int aQuestionId) {
/* 119 */     Long pId = Long.valueOf(aPlayerId);
/* 120 */     if (playerVotes.containsKey(pId)) {
/*     */       
/* 122 */       PlayerVotesByPlayer pvbp = playerVotes.get(pId);
/* 123 */       if (pvbp.containsKey(aQuestionId)) {
/*     */         
/* 125 */         PlayerVote pv = pvbp.get(aQuestionId);
/* 126 */         return pv.hasVoted();
/*     */       } 
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static PlayerVote getPlayerVotesByQuestions(long aPlayerId, int aQuestionId) {
/* 134 */     Long pId = Long.valueOf(aPlayerId);
/* 135 */     if (playerVotes.containsKey(pId)) {
/*     */ 
/*     */       
/* 138 */       PlayerVotesByPlayer pvbp = playerVotes.get(pId);
/* 139 */       if (pvbp.containsKey(aQuestionId)) {
/*     */         
/* 141 */         PlayerVote pv = pvbp.get(aQuestionId);
/* 142 */         return pv;
/*     */       } 
/*     */     } 
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static PlayerVote getPlayerVoteByQuestion(long aPlayerId, int aQuestionId) {
/* 150 */     Long pId = Long.valueOf(aPlayerId);
/* 151 */     if (playerVotes.containsKey(pId)) {
/*     */       
/* 153 */       PlayerVotesByPlayer pvbp = playerVotes.get(pId);
/* 154 */       if (pvbp.containsKey(aQuestionId)) {
/*     */         
/* 156 */         PlayerVote pv = pvbp.get(aQuestionId);
/* 157 */         return pv;
/*     */       } 
/*     */     } 
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PlayerVote[] getPlayerVotesByQuestion(int aQuestionId) {
/* 171 */     Map<Long, PlayerVote> pVotes = new HashMap<>();
/* 172 */     for (Map.Entry<Long, PlayerVotesByPlayer> entry : playerVotes.entrySet()) {
/*     */       
/* 174 */       if (((PlayerVotesByPlayer)entry.getValue()).containsKey(aQuestionId)) {
/*     */         
/* 176 */         PlayerVote pv = ((PlayerVotesByPlayer)entry.getValue()).get(aQuestionId);
/* 177 */         if (pv.hasVoted())
/* 178 */           pVotes.put(entry.getKey(), pv); 
/*     */       } 
/*     */     } 
/* 181 */     return (PlayerVote[])pVotes.values().toArray((Object[])new PlayerVote[pVotes.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deletePlayerVotes(int questionId) {
/* 187 */     for (Map.Entry<Long, PlayerVotesByPlayer> entry : playerVotes.entrySet())
/* 188 */       ((PlayerVotesByPlayer)entry.getValue()).remove(questionId); 
/* 189 */     for (Player p : Players.getInstance().getPlayers()) {
/* 190 */       p.removePlayerVote(questionId);
/*     */     }
/*     */     
/* 193 */     Connection dbcon = null;
/* 194 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 197 */       dbcon = DbConnector.getPlayerDbCon();
/* 198 */       ps = dbcon.prepareStatement("DELETE FROM VOTES WHERE QUESTIONID=?");
/* 199 */       ps.setInt(1, questionId);
/* 200 */       ps.executeUpdate();
/*     */     }
/* 202 */     catch (SQLException sqx) {
/*     */       
/* 204 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 208 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 209 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbLoadAllPlayerVotes() {
/* 218 */     Connection dbcon = null;
/* 219 */     PreparedStatement ps = null;
/* 220 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 223 */       dbcon = DbConnector.getPlayerDbCon();
/* 224 */       ps = dbcon.prepareStatement("SELECT * FROM VOTES");
/* 225 */       rs = ps.executeQuery();
/* 226 */       while (rs.next())
/*     */       {
/* 228 */         long aPlayerId = rs.getLong("PLAYERID");
/* 229 */         int aQuestionId = rs.getInt("QUESTIONID");
/* 230 */         boolean aOption1 = rs.getBoolean("OPTION1");
/* 231 */         boolean aOption2 = rs.getBoolean("OPTION2");
/* 232 */         boolean aOption3 = rs.getBoolean("OPTION3");
/* 233 */         boolean aOption4 = rs.getBoolean("OPTION4");
/*     */         
/* 235 */         addPlayerVote(new PlayerVote(aPlayerId, aQuestionId, aOption1, aOption2, aOption3, aOption4), false);
/*     */       }
/*     */     
/*     */     }
/* 239 */     catch (SQLException sqx) {
/*     */       
/* 241 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 245 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 246 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerVotes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */