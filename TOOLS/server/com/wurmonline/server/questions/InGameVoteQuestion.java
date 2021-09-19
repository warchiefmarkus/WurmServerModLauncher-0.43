/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.VoteServer;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerVote;
/*     */ import com.wurmonline.server.players.PlayerVotes;
/*     */ import com.wurmonline.server.support.Tickets;
/*     */ import com.wurmonline.server.support.VoteQuestion;
/*     */ import com.wurmonline.server.support.VoteQuestions;
/*     */ import com.wurmonline.server.webinterface.WcVoting;
/*     */ import java.util.Properties;
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
/*     */ public class InGameVoteQuestion
/*     */   extends Question
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(InGameVoteQuestion.class.getName());
/*     */   
/*     */   private static final byte SHOWLIST = 0;
/*     */   
/*     */   private static final byte EDITQUESTION = 2;
/*     */   private static final byte VIEWQUESTION = 3;
/*     */   private static final byte SHOWRESULTS = 4;
/*  51 */   private byte part = 0;
/*  52 */   private int questionId = 0;
/*     */ 
/*     */   
/*     */   public InGameVoteQuestion(Creature aResponder) {
/*  56 */     this(aResponder, "In game voting", "", (byte)0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InGameVoteQuestion(Creature aResponder, String aTitle, String aQuestion, byte aPart) {
/*  62 */     this(aResponder, aTitle, aQuestion, aPart, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InGameVoteQuestion(Creature aResponder, String aTitle, String aQuestion, byte aPart, int aQuestionId) {
/*  68 */     super(aResponder, aTitle, aQuestion, 107, aResponder.getWurmId());
/*  69 */     this.part = aPart;
/*  70 */     this.questionId = aQuestionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  81 */     setAnswer(aAnswer);
/*  82 */     if (this.type == 0) {
/*     */       
/*  84 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  87 */     if (this.type == 107) {
/*     */       String strId, windowTitle; boolean opt1; String header; boolean opt2; byte nextPart; boolean opt3; VoteQuestion vq; boolean opt4; Player p; VoteQuestion voteQuestion1; InGameVoteQuestion igvsq;
/*  89 */       boolean hasPower = (getResponder().getPower() >= 2);
/*     */       
/*  91 */       switch (this.part) {
/*     */ 
/*     */         
/*     */         case 0:
/*  95 */           strId = aAnswer.getProperty("qid");
/*  96 */           if (strId == null)
/*     */             return; 
/*  98 */           this.questionId = Integer.parseInt(strId);
/*  99 */           if (this.questionId == -1) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 107 */           nextPart = 0;
/*     */           
/* 109 */           vq = VoteQuestions.getVoteQuestion(this.questionId);
/* 110 */           p = (Player)getResponder();
/* 111 */           if ((hasPower && vq.isActive()) || vq.canVote(p)) {
/*     */             
/* 113 */             if (hasPower || p.hasVoted(this.questionId))
/*     */             {
/*     */               
/* 116 */               nextPart = 3;
/* 117 */               windowTitle = "View Question";
/* 118 */               header = vq.getQuestionTitle();
/*     */             
/*     */             }
/*     */             else
/*     */             {
/* 123 */               nextPart = 2;
/* 124 */               windowTitle = "Vote";
/* 125 */               header = vq.getQuestionTitle();
/*     */             }
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 131 */             nextPart = 4;
/* 132 */             windowTitle = "Vote Results";
/* 133 */             header = vq.getQuestionTitle();
/*     */           } 
/* 135 */           igvsq = new InGameVoteQuestion(getResponder(), windowTitle, header, nextPart, this.questionId);
/*     */           
/* 137 */           igvsq.sendQuestion();
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 143 */           opt1 = false;
/* 144 */           opt2 = false;
/* 145 */           opt3 = false;
/* 146 */           opt4 = false;
/*     */           
/* 148 */           voteQuestion1 = VoteQuestions.getVoteQuestion(this.questionId);
/* 149 */           if (voteQuestion1.isAllowMultiple()) {
/*     */ 
/*     */             
/* 152 */             String sopt1 = aAnswer.getProperty("opt1");
/* 153 */             String sopt2 = aAnswer.getProperty("opt2");
/* 154 */             String sopt3 = aAnswer.getProperty("opt3");
/* 155 */             String sopt4 = aAnswer.getProperty("opt4");
/* 156 */             opt1 = Boolean.parseBoolean(sopt1);
/* 157 */             opt2 = Boolean.parseBoolean(sopt2);
/* 158 */             opt3 = Boolean.parseBoolean(sopt3);
/* 159 */             opt4 = Boolean.parseBoolean(sopt4);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 164 */             String sopts = aAnswer.getProperty("opts");
/* 165 */             int ans = Integer.parseInt(sopts);
/* 166 */             switch (ans) {
/*     */               
/*     */               case 1:
/* 169 */                 opt1 = true;
/*     */                 break;
/*     */               case 2:
/* 172 */                 opt2 = true;
/*     */                 break;
/*     */               case 3:
/* 175 */                 opt3 = true;
/*     */                 break;
/*     */               case 4:
/* 178 */                 opt4 = true;
/*     */                 break;
/*     */               default:
/*     */                 return;
/*     */             } 
/*     */           } 
/* 184 */           if (opt1 || opt2 || opt3 || opt4) {
/*     */             
/* 186 */             if (!Servers.isThisLoginServer() && !isLoginAvailable()) {
/* 187 */               getResponder()
/* 188 */                 .getCommunicator()
/* 189 */                 .sendNormalServerMessage("Login server is currently down, so your vote cannot be registered, please try again later.");
/*     */               
/*     */               break;
/*     */             } 
/*     */             
/* 194 */             PlayerVote pv = PlayerVotes.addPlayerVote(new PlayerVote(getResponder().getWurmId(), this.questionId, opt1, opt2, opt3, opt4), true);
/*     */             
/* 196 */             ((Player)getResponder()).addPlayerVote(pv);
/* 197 */             if (!Servers.isThisLoginServer()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 204 */               WcVoting wv = new WcVoting(pv);
/* 205 */               wv.sendToLoginServer();
/*     */             } 
/* 207 */             getResponder().getCommunicator().sendNormalServerMessage("Your vote has been registered.");
/*     */             
/*     */             break;
/*     */           } 
/* 211 */           getResponder().getCommunicator().sendNormalServerMessage("You did not select anything.");
/*     */           break;
/*     */       } 
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
/*     */   public void sendQuestion() {
/* 231 */     int width = 350;
/* 232 */     int height = 500;
/* 233 */     StringBuilder buf = new StringBuilder();
/* 234 */     buf.append(getBmlHeader());
/* 235 */     boolean hasPower = (getResponder().getPower() >= 2); try {
/*     */       VoteQuestion[] vqs; int cols; VoteQuestion vq; PlayerVote pv; String optType, selected; VoteQuestion vq1;
/*     */       int rows;
/* 238 */       String disabled = "";
/* 239 */       switch (this.part) {
/*     */ 
/*     */ 
/*     */         
/*     */         case 0:
/* 244 */           cols = 5;
/* 245 */           if (hasPower) {
/*     */ 
/*     */             
/* 248 */             vqs = VoteQuestions.getVoteQuestions();
/* 249 */             cols = 4;
/*     */           } else {
/*     */             
/* 252 */             vqs = VoteQuestions.getVoteQuestions((Player)getResponder());
/*     */           } 
/* 254 */           if (vqs.length > 0) {
/*     */             
/* 256 */             buf.append("text{text=\"Select the questions to view.\"};");
/*     */             
/* 258 */             buf.append("table{rows=\"" + (vqs.length + 2) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"Question\"};label{type=\"bold\";text=\"Vote Start\"};label{type=\"bold\";text=\"Vote End\"};label{type=\"bold\";text=\"" + ((cols == 5) ? "Voted?" : "") + "\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 265 */             for (VoteQuestion voteQuestion : vqs) {
/*     */               
/* 267 */               boolean voted = ((Player)getResponder()).hasVoted(voteQuestion.getQuestionId());
/*     */               
/* 269 */               String colour = "";
/* 270 */               if (voteQuestion.getSent() == 2) {
/* 271 */                 if (voted)
/* 272 */                 { colour = "color=\"255,255,100\";"; }
/*     */                 else
/* 274 */                 { colour = "color=\"100,255,100\";"; } 
/* 275 */               } else if (voteQuestion.hasSummary()) {
/* 276 */                 colour = "color=\"100,100,255\";";
/*     */               } 
/* 278 */               String strVoted = voted ? "Yes" : "No";
/* 279 */               buf.append(((colour.length() == 0) ? "label{text=\"\"}" : ("radio{group=\"qid\";id=\"" + voteQuestion
/* 280 */                   .getQuestionId() + "\"};")) + "label{" + colour + "text=\"" + voteQuestion
/* 281 */                   .getQuestionTitle() + "\"};label{" + colour + "text=\"" + 
/* 282 */                   Tickets.convertTime(voteQuestion.getVoteStart()) + "\"};label{" + colour + "text=\"" + 
/* 283 */                   Tickets.convertTime(voteQuestion.getVoteEnd() - 1000L) + "\"};label{" + colour + "text=\"" + ((cols == 5) ? strVoted : "") + "\"};");
/*     */             } 
/*     */ 
/*     */             
/* 287 */             buf.append("radio{group=\"qid\";id=\"-1\";selected=\"true\";hidden=\"true\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 293 */             buf.append("}");
/*     */           } else {
/*     */             
/* 296 */             buf.append("text{type=\"bold\";text=\"No polls available at the moment.\"};");
/* 297 */           }  width = 350;
/* 298 */           height = 140 + vqs.length * 16;
/*     */           break;
/*     */         
/*     */         case 3:
/* 302 */           disabled = ";enabled=\"false\"";
/*     */ 
/*     */         
/*     */         case 2:
/* 306 */           width = 300;
/* 307 */           height = 200;
/* 308 */           vq = VoteQuestions.getVoteQuestion(this.questionId);
/* 309 */           pv = PlayerVotes.getPlayerVoteByQuestion(getResponder().getWurmId(), this.questionId);
/*     */ 
/*     */           
/* 312 */           if (this.part == 3 && !hasPower)
/*     */           {
/* 314 */             if (vq.hasEnded()) {
/* 315 */               buf.append("label{type=\"bolditalic\";color=\"255,100,100\";text=\"Voting has ended so cannot amend\"}");
/*     */             } else {
/* 317 */               buf.append("label{type=\"bolditalic\";color=\"255,255,100\";text=\"Already voted and therefore cannot amend\"}");
/*     */             }  } 
/* 319 */           optType = "";
/* 320 */           buf.append("text{color=\"255,150,0\";type=\"bold\";text=\"" + vq.getQuestionText() + "\"};");
/* 321 */           if (vq.isAllowMultiple()) {
/*     */ 
/*     */             
/* 324 */             optType = "checkbox{id=\"opt";
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 329 */             optType = "radio{group=\"opts\";id=\"";
/* 330 */             buf.append(optType + "0\";text=\"" + vq.getOption1Text() + "\";selected=\"true\";hidden=\"true\"}");
/*     */           } 
/*     */ 
/*     */           
/* 334 */           selected = "";
/* 335 */           selected = (pv != null && pv.getOption1()) ? ";selected=\"true\"" : "";
/* 336 */           buf.append(optType + "1\";text=\"" + vq.getOption1Text() + "\"" + selected + disabled + "}");
/* 337 */           selected = (pv != null && pv.getOption2()) ? ";selected=\"true\"" : "";
/* 338 */           buf.append(optType + "2\";text=\"" + vq.getOption2Text() + "\"" + selected + disabled + "}");
/* 339 */           if (vq.getOption3Text().length() > 0) {
/*     */             
/* 341 */             selected = (pv != null && pv.getOption3()) ? ";selected=\"true\"" : "";
/* 342 */             buf.append(optType + "3\";text=\"" + vq.getOption3Text() + "\"" + selected + disabled + "}");
/* 343 */             height += 16;
/*     */           } 
/* 345 */           if (vq.getOption4Text().length() > 0) {
/*     */             
/* 347 */             selected = (pv != null && pv.getOption4()) ? ";selected=\"true\"" : "";
/* 348 */             buf.append(optType + "4\";text=\"" + vq.getOption4Text() + "\"" + selected + disabled + "}");
/* 349 */             height += 16;
/*     */           } 
/*     */           
/* 352 */           if (hasPower) {
/*     */             
/* 354 */             if (vq.isPremOnly()) {
/* 355 */               buf.append("label{text=\"Prem-Only can vote.\"}");
/*     */             } else {
/* 357 */               buf.append("label{text=\"Everyone can vote.\"}");
/*     */             } 
/* 359 */             if (vq.isAllowMultiple()) {
/* 360 */               buf.append("label{text=\"Multiple selections allowed.\"}");
/*     */             } else {
/* 362 */               buf.append("label{text=\"Single selection only.\"}");
/*     */             } 
/* 364 */             buf.append("label{text=\"Kingdoms allowed:");
/* 365 */             int len = 0;
/* 366 */             int cnt = 1;
/* 367 */             if (vq.isJK())
/* 368 */               len++; 
/* 369 */             if (vq.isMR())
/* 370 */               len++; 
/* 371 */             if (vq.isHots())
/* 372 */               len++; 
/* 373 */             if (vq.isFreedom())
/* 374 */               len++; 
/* 375 */             String comma = "";
/* 376 */             if (vq.isJK()) {
/*     */               
/* 378 */               buf.append(comma + "JK");
/* 379 */               if (++cnt < len) {
/* 380 */                 comma = ", ";
/*     */               } else {
/* 382 */                 comma = " and ";
/*     */               } 
/* 384 */             }  if (vq.isMR()) {
/*     */               
/* 386 */               buf.append(comma + "MR");
/* 387 */               if (++cnt < len) {
/* 388 */                 comma = ", ";
/*     */               } else {
/* 390 */                 comma = " and ";
/*     */               } 
/* 392 */             }  if (vq.isHots()) {
/*     */               
/* 394 */               buf.append(comma + "Hots");
/* 395 */               if (++cnt < len) {
/* 396 */                 comma = ", ";
/*     */               } else {
/* 398 */                 comma = " and ";
/*     */               } 
/* 400 */             }  if (vq.isFreedom())
/* 401 */               buf.append(comma + "Freedom"); 
/* 402 */             buf.append("\"}");
/*     */             
/* 404 */             if (vq.getServers().isEmpty()) {
/* 405 */               buf.append("label{text=\"List of Servers is only available on Login Server.\"}");
/*     */             } else {
/*     */               
/* 408 */               buf.append("label{text=\"Servers:");
/* 409 */               len = vq.getServers().size();
/* 410 */               cnt = 1;
/* 411 */               comma = "";
/* 412 */               for (VoteServer vs : vq.getServers()) {
/*     */                 
/* 414 */                 ServerEntry se = Servers.getServerWithId(vs.getServerId());
/* 415 */                 buf.append(comma + se.getAbbreviation());
/* 416 */                 if (++cnt < len) {
/* 417 */                   comma = ", "; continue;
/*     */                 } 
/* 419 */                 comma = " and ";
/*     */               } 
/* 421 */               buf.append("\"}");
/*     */             } 
/* 423 */             height += 32;
/*     */           }
/* 425 */           else if (vq.isAllowMultiple()) {
/* 426 */             buf.append("label{color=\"0,255,255\";type=\"italic\";text=\"Multiple selections are allowed.\"}");
/*     */           } else {
/* 428 */             buf.append("label{color=\"0,255,255\";type=\"italic\";text=\"Only one selection is allowed.\"}");
/* 429 */           }  buf.append("text{type=\"bold\";text=\"Note: Votes will be used as guidelines to assess player sentiment. There is no guarantee that any majority vote will result in the implementation or removal of anything.\"}");
/*     */ 
/*     */           
/* 432 */           height += 32;
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 438 */           vq1 = VoteQuestions.getVoteQuestion(this.questionId);
/* 439 */           rows = 3;
/* 440 */           if (vq1.getOption3Text().length() > 0)
/* 441 */             rows++; 
/* 442 */           if (vq1.getOption4Text().length() > 0) {
/* 443 */             rows++;
/*     */           }
/* 445 */           buf.append("table{rows=\"" + rows + "\";cols=\"2\";");
/* 446 */           buf.append("label{text=\"" + vq1.getOption1Text() + "\"}label{text=\"" + vq1
/* 447 */               .getOption1Count() + "\"}");
/* 448 */           buf.append("label{text=\"" + vq1.getOption2Text() + "\"}label{text=\"" + vq1
/* 449 */               .getOption2Count() + "\"}");
/* 450 */           if (vq1.getOption3Text().length() > 0)
/* 451 */             buf.append("label{text=\"" + vq1.getOption3Text() + "\"}label{text=\"" + vq1
/* 452 */                 .getOption3Count() + "\"}"); 
/* 453 */           if (vq1.getOption4Text().length() > 0)
/* 454 */             buf.append("label{text=\"" + vq1.getOption4Text() + "\"}label{text=\"" + vq1
/* 455 */                 .getOption4Count() + "\"}"); 
/* 456 */           buf.append("label{text=\"Total Players who voted\"}label{text=\"" + vq1
/* 457 */               .getVoteCount() + "\"}");
/* 458 */           buf.append("}");
/* 459 */           buf.append("text{text=\"\"}");
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 494 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 497 */       logger.log(Level.WARNING, e.getMessage(), e);
/* 498 */       getResponder().getCommunicator().sendNormalServerMessage("Exception:" + e.getMessage());
/*     */     } 
/*     */     
/* 501 */     buf.append(createAnswerButton2());
/*     */     
/* 503 */     getResponder().getCommunicator().sendBml(width, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isLoginAvailable() {
/* 508 */     if (Servers.isThisLoginServer())
/* 509 */       return true; 
/* 510 */     return Servers.loginServer.isAvailable(5, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\InGameVoteQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */