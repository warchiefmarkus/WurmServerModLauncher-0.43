/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerVote;
/*     */ import com.wurmonline.server.players.PlayerVotes;
/*     */ import com.wurmonline.server.support.VoteQuestion;
/*     */ import com.wurmonline.server.support.VoteQuestions;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class WcVoting
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(WcVoting.class.getName());
/*     */   
/*     */   public static final byte DO_NOTHING = 0;
/*     */   
/*     */   public static final byte VOTE_QUESTION = 1;
/*     */   
/*     */   public static final byte ASK_FOR_VOTES = 2;
/*     */   
/*     */   public static final byte PLAYER_VOTE = 3;
/*     */   
/*     */   public static final byte VOTE_SUMMARY = 4;
/*     */   
/*     */   public static final byte REMOVE_QUESTION = 5;
/*     */   
/*     */   public static final byte CLOSE_VOTING = 6;
/*     */   
/*  66 */   private byte type = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int questionId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String questionTitle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String questionText;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String option1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String option2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String option3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String option4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean allowMultiple;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean premOnly;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean jk;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hots;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean freedom;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long voteStart;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long voteEnd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long playerId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short voteCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short count1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short count2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short count3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short count4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] questionIds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PlayerVote[] playerVotes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcVoting(long aId, byte[] aData) {
/* 217 */     super(aId, (short)20, aData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/* 239 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 240 */     DataOutputStream dos = null;
/* 241 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 244 */       dos = new DataOutputStream(bos);
/* 245 */       dos.writeByte(this.type);
/* 246 */       switch (this.type) {
/*     */         
/*     */         case 1:
/* 249 */           dos.writeInt(this.questionId);
/* 250 */           dos.writeUTF(this.questionTitle);
/* 251 */           dos.writeUTF(this.questionText);
/* 252 */           dos.writeUTF(this.option1);
/* 253 */           dos.writeUTF(this.option2);
/* 254 */           dos.writeUTF(this.option3);
/* 255 */           dos.writeUTF(this.option4);
/* 256 */           dos.writeBoolean(this.allowMultiple);
/* 257 */           dos.writeBoolean(this.premOnly);
/* 258 */           dos.writeBoolean(this.jk);
/* 259 */           dos.writeBoolean(this.mr);
/* 260 */           dos.writeBoolean(this.hots);
/* 261 */           dos.writeBoolean(this.freedom);
/* 262 */           dos.writeLong(this.voteStart);
/* 263 */           dos.writeLong(this.voteEnd);
/*     */           break;
/*     */         
/*     */         case 2:
/* 267 */           dos.writeLong(this.playerId);
/* 268 */           dos.writeInt(this.questionIds.length);
/* 269 */           for (int qId : this.questionIds) {
/* 270 */             dos.writeInt(qId);
/*     */           }
/*     */           break;
/*     */         case 3:
/* 274 */           dos.writeLong(this.playerId);
/* 275 */           dos.writeInt(this.playerVotes.length);
/* 276 */           for (PlayerVote pv : this.playerVotes) {
/*     */             
/* 278 */             dos.writeInt(pv.getQuestionId());
/* 279 */             dos.writeBoolean(pv.getOption1());
/* 280 */             dos.writeBoolean(pv.getOption2());
/* 281 */             dos.writeBoolean(pv.getOption3());
/* 282 */             dos.writeBoolean(pv.getOption4());
/*     */           } 
/*     */           break;
/*     */         
/*     */         case 4:
/* 287 */           dos.writeInt(this.questionId);
/* 288 */           dos.writeShort(this.voteCount);
/* 289 */           dos.writeShort(this.count1);
/* 290 */           dos.writeShort(this.count2);
/* 291 */           dos.writeShort(this.count3);
/* 292 */           dos.writeShort(this.count4);
/*     */           break;
/*     */         
/*     */         case 5:
/* 296 */           dos.writeInt(this.questionId);
/*     */           break;
/*     */         
/*     */         case 6:
/* 300 */           dos.writeInt(this.questionId);
/* 301 */           dos.writeLong(this.voteEnd);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 307 */       dos.flush();
/* 308 */       dos.close();
/*     */     }
/* 310 */     catch (Exception ex) {
/*     */       
/* 312 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 316 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 317 */       barr = bos.toByteArray();
/* 318 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 319 */       setData(barr);
/*     */     } 
/* 321 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 332 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 337 */           DataInputStream dis = null;
/*     */           try {
/*     */             int i;
/* 340 */             dis = new DataInputStream(new ByteArrayInputStream(WcVoting.this.getData()));
/* 341 */             WcVoting.this.type = dis.readByte();
/* 342 */             switch (WcVoting.this.type) {
/*     */               
/*     */               case 1:
/* 345 */                 WcVoting.this.questionId = dis.readInt();
/* 346 */                 WcVoting.this.questionTitle = dis.readUTF();
/* 347 */                 WcVoting.this.questionText = dis.readUTF();
/* 348 */                 WcVoting.this.option1 = dis.readUTF();
/* 349 */                 WcVoting.this.option2 = dis.readUTF();
/* 350 */                 WcVoting.this.option3 = dis.readUTF();
/* 351 */                 WcVoting.this.option4 = dis.readUTF();
/* 352 */                 WcVoting.this.allowMultiple = dis.readBoolean();
/* 353 */                 WcVoting.this.premOnly = dis.readBoolean();
/* 354 */                 WcVoting.this.jk = dis.readBoolean();
/* 355 */                 WcVoting.this.mr = dis.readBoolean();
/* 356 */                 WcVoting.this.hots = dis.readBoolean();
/* 357 */                 WcVoting.this.freedom = dis.readBoolean();
/* 358 */                 WcVoting.this.voteStart = dis.readLong();
/* 359 */                 WcVoting.this.voteEnd = dis.readLong();
/*     */                 break;
/*     */               
/*     */               case 2:
/* 363 */                 WcVoting.this.playerId = dis.readLong();
/* 364 */                 WcVoting.this.questionIds = new int[dis.readInt()];
/* 365 */                 for (i = 0; i < WcVoting.this.questionIds.length; i++) {
/* 366 */                   WcVoting.this.questionIds[i] = dis.readInt();
/*     */                 }
/*     */                 break;
/*     */               case 3:
/* 370 */                 WcVoting.this.playerId = dis.readLong();
/* 371 */                 WcVoting.this.playerVotes = new PlayerVote[dis.readInt()];
/* 372 */                 for (i = 0; i < WcVoting.this.playerVotes.length; i++) {
/*     */ 
/*     */                   
/* 375 */                   PlayerVote pv = new PlayerVote(WcVoting.this.playerId, dis.readInt(), dis.readBoolean(), dis.readBoolean(), dis.readBoolean(), dis.readBoolean());
/* 376 */                   WcVoting.this.playerVotes[i] = pv;
/*     */                 } 
/*     */                 break;
/*     */               
/*     */               case 4:
/* 381 */                 WcVoting.this.questionId = dis.readInt();
/* 382 */                 WcVoting.this.voteCount = dis.readShort();
/* 383 */                 WcVoting.this.count1 = dis.readShort();
/* 384 */                 WcVoting.this.count2 = dis.readShort();
/* 385 */                 WcVoting.this.count3 = dis.readShort();
/* 386 */                 WcVoting.this.count4 = dis.readShort();
/*     */                 break;
/*     */               
/*     */               case 5:
/* 390 */                 WcVoting.this.questionId = dis.readInt();
/*     */                 break;
/*     */               
/*     */               case 6:
/* 394 */                 WcVoting.this.questionId = dis.readInt();
/* 395 */                 WcVoting.this.voteEnd = dis.readLong();
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 402 */           } catch (IOException ex) {
/*     */             
/* 404 */             WcVoting.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 409 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/* 411 */           switch (WcVoting.this.type) {
/*     */ 
/*     */ 
/*     */             
/*     */             case 1:
/* 416 */               VoteQuestions.queueAddVoteQuestion(WcVoting.this.questionId, WcVoting.this.questionTitle, WcVoting.this.questionText, WcVoting.this
/* 417 */                   .option1, WcVoting.this.option2, WcVoting.this.option3, WcVoting.this.option4, WcVoting.this.allowMultiple, WcVoting.this.premOnly, WcVoting.this
/* 418 */                   .jk, WcVoting.this.mr, WcVoting.this.hots, WcVoting.this.freedom, WcVoting.this.voteStart, WcVoting.this.voteEnd);
/*     */               break;
/*     */ 
/*     */             
/*     */             case 2:
/* 423 */               if (Servers.isThisLoginServer()) {
/*     */ 
/*     */                 
/* 426 */                 Map<Integer, PlayerVote> pVotes = new ConcurrentHashMap<>();
/* 427 */                 for (int qId : WcVoting.this.questionIds) {
/*     */                   
/* 429 */                   PlayerVote pv = PlayerVotes.getPlayerVoteByQuestion(WcVoting.this.playerId, qId);
/* 430 */                   if (pv != null)
/*     */                   {
/*     */ 
/*     */                     
/* 434 */                     if (pv.hasVoted())
/* 435 */                       pVotes.put(Integer.valueOf(qId), pv); 
/*     */                   }
/*     */                 } 
/* 438 */                 WcVoting wv = new WcVoting(WcVoting.this.playerId, (PlayerVote[])pVotes.values().toArray((Object[])new PlayerVote[pVotes.size()]));
/* 439 */                 wv.sendToServer(WurmId.getOrigin(WcVoting.this.getWurmId()));
/*     */               } 
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 3:
/* 449 */               if (Servers.isThisLoginServer())
/*     */               {
/* 451 */                 for (PlayerVote pv : WcVoting.this.playerVotes) {
/* 452 */                   PlayerVotes.addPlayerVote(pv, true);
/*     */                 }
/*     */               }
/*     */               
/*     */               try {
/* 457 */                 Player p = Players.getInstance().getPlayer(WcVoting.this.playerId);
/* 458 */                 p.setVotes(WcVoting.this.playerVotes);
/*     */               }
/* 460 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 4:
/* 468 */               if (Servers.isThisLoginServer()) {
/*     */                 
/* 470 */                 VoteQuestion vq = VoteQuestions.getVoteQuestion(WcVoting.this.questionId);
/*     */                 
/* 472 */                 WcVoting wv = new WcVoting(vq.getQuestionId(), vq.getVoteCount(), vq.getOption1Count(), vq.getOption2Count(), vq.getOption3Count(), vq.getOption4Count());
/* 473 */                 wv.sendToServer(WurmId.getOrigin(WcVoting.this.getWurmId()));
/*     */               } 
/*     */               break;
/*     */             
/*     */             case 5:
/* 478 */               VoteQuestions.queueRemoveVoteQuestion(WcVoting.this.questionId);
/*     */               break;
/*     */             
/*     */             case 6:
/* 482 */               VoteQuestions.queueCloseVoteQuestion(WcVoting.this.questionId, WcVoting.this.voteEnd);
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 489 */       }).start();
/*     */   }
/*     */   
/*     */   public WcVoting(VoteQuestion voteQuestion) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = 1;
/*     */     this.questionId = voteQuestion.getQuestionId();
/*     */     this.questionTitle = voteQuestion.getQuestionTitle();
/*     */     this.questionText = voteQuestion.getQuestionText();
/*     */     this.option1 = voteQuestion.getOption1Text();
/*     */     this.option2 = voteQuestion.getOption2Text();
/*     */     this.option3 = voteQuestion.getOption3Text();
/*     */     this.option4 = voteQuestion.getOption4Text();
/*     */     this.allowMultiple = voteQuestion.isAllowMultiple();
/*     */     this.premOnly = voteQuestion.isPremOnly();
/*     */     this.jk = voteQuestion.isJK();
/*     */     this.mr = voteQuestion.isMR();
/*     */     this.hots = voteQuestion.isHots();
/*     */     this.freedom = voteQuestion.isFreedom();
/*     */     this.voteStart = voteQuestion.getVoteStart();
/*     */     this.voteEnd = voteQuestion.getVoteEnd();
/*     */   }
/*     */   
/*     */   public WcVoting(long aPlayerId, int[] aQuestions) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = 2;
/*     */     this.questionIds = aQuestions;
/*     */     this.playerId = aPlayerId;
/*     */   }
/*     */   
/*     */   public WcVoting(PlayerVote pv) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = 3;
/*     */     this.playerId = pv.getPlayerId();
/*     */     this.playerVotes = new PlayerVote[] { pv };
/*     */   }
/*     */   
/*     */   public WcVoting(long aPlayerId, PlayerVote[] pvs) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = 3;
/*     */     this.playerId = aPlayerId;
/*     */     this.playerVotes = pvs;
/*     */   }
/*     */   
/*     */   public WcVoting(int aQuestionId, short aVoteCount, short aCount1, short aCount2, short aCount3, short aCount4) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = 4;
/*     */     this.questionId = aQuestionId;
/*     */     this.voteCount = aVoteCount;
/*     */     this.count1 = aCount1;
/*     */     this.count2 = aCount2;
/*     */     this.count3 = aCount3;
/*     */     this.count4 = aCount4;
/*     */   }
/*     */   
/*     */   public WcVoting(byte aAction, int aQuestionId) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = aAction;
/*     */     this.questionId = aQuestionId;
/*     */   }
/*     */   
/*     */   public WcVoting(byte aAction, int aQuestionId, long when) {
/*     */     super(WurmId.getNextWCCommandId(), (short)20);
/*     */     this.type = aAction;
/*     */     this.questionId = aQuestionId;
/*     */     this.voteEnd = when;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcVoting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */