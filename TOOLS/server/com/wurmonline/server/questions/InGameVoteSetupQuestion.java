/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.VoteServer;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.PlayerVote;
/*     */ import com.wurmonline.server.players.PlayerVotes;
/*     */ import com.wurmonline.server.support.Tickets;
/*     */ import com.wurmonline.server.support.VoteQuestion;
/*     */ import com.wurmonline.server.support.VoteQuestions;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InGameVoteSetupQuestion
/*     */   extends Question
/*     */ {
/*  51 */   private static final Logger logger = Logger.getLogger(InGameVoteSetupQuestion.class.getName());
/*     */   
/*     */   private static final byte SHOWLIST = 0;
/*     */   
/*     */   private static final byte ADDNEW = 1;
/*     */   private static final byte EDITQUESTION = 2;
/*     */   private static final byte VIEWQUESTION = 3;
/*     */   private static final byte SHOWRESULTS = 4;
/*  59 */   private static final SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy HH:mm");
/*     */   
/*  61 */   private byte part = 0;
/*  62 */   private int questionId = 0;
/*  63 */   private String qtitle = "";
/*  64 */   private String qtext = "";
/*  65 */   private String opt1 = "";
/*  66 */   private String opt2 = "";
/*  67 */   private String opt3 = "";
/*  68 */   private String opt4 = "";
/*     */   private boolean ma = false;
/*     */   private boolean po = false;
/*     */   private boolean jk = true;
/*     */   private boolean mr = true;
/*     */   private boolean hots = true;
/*     */   private boolean freedom = true;
/*  75 */   private long voteStart = 0L;
/*  76 */   private long voteEnd = 0L;
/*     */   private int startDay;
/*     */   private int startMonth;
/*     */   private int startYear;
/*     */   private int startHour;
/*     */   private int startMins;
/*     */   private int lasts;
/*  83 */   private String failReason = "";
/*  84 */   private Set<Integer> serverIds = new HashSet<>();
/*     */   
/*     */   private int width;
/*     */   private int height;
/*     */   
/*     */   public InGameVoteSetupQuestion(Creature aResponder) {
/*  90 */     this(aResponder, "In game voting setup", "List of Questions", (byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   public InGameVoteSetupQuestion(Creature aResponder, String aTitle, String aQuestion, byte aPart) {
/*  95 */     super(aResponder, aTitle, aQuestion, 114, aResponder.getWurmId());
/*  96 */     this.part = aPart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InGameVoteSetupQuestion(Creature aResponder, String aTitle, String aQuestion, byte aPart, int aQuestionId, String aFailed) {
/* 102 */     super(aResponder, aTitle, aQuestion, 114, aResponder.getWurmId());
/* 103 */     this.part = aPart;
/* 104 */     this.questionId = aQuestionId;
/* 105 */     this.failReason = aFailed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InGameVoteSetupQuestion(Creature aResponder, String aTitle, String aQuestion, byte aPart, int aQuestionId) {
/* 111 */     this(aResponder, aTitle, aQuestion, aPart, aQuestionId, "");
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
/* 122 */     setAnswer(aAnswer);
/* 123 */     if (this.type == 0) {
/*     */       
/* 125 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/* 128 */     if (this.type == 114) {
/*     */       String strId; String windowTitle; String delete; String header; String strMa; byte nextPart; String strPo; VoteQuestion vq; String strJk; InGameVoteSetupQuestion igvsq; String strMr; String strHots; String strFreedom; String strVoteDay; String strVoteMonth; String strVoteYear; String strVoteHour; String strVoteMins; String strVoteLasts; int day; int month; int year; int hour; int mins; int votelasts; String fail; ServerEntry[] entries; int x; List<VoteServer> servers; String remove;
/*     */       VoteQuestion newVoteQuestion;
/* 131 */       switch (this.part) {
/*     */ 
/*     */         
/*     */         case 0:
/* 135 */           strId = aAnswer.getProperty("qid");
/* 136 */           this.questionId = Integer.parseInt(strId);
/* 137 */           if (this.questionId == 0) {
/*     */ 
/*     */             
/* 140 */             InGameVoteSetupQuestion inGameVoteSetupQuestion = new InGameVoteSetupQuestion(getResponder(), "In game voting setup", "Add New Question", (byte)1);
/*     */             
/* 142 */             inGameVoteSetupQuestion.sendQuestion();
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 148 */           nextPart = 0;
/*     */           
/* 150 */           vq = VoteQuestions.getVoteQuestion(this.questionId);
/* 151 */           if (vq.getSent() == 0) {
/*     */ 
/*     */             
/* 154 */             nextPart = 2;
/* 155 */             windowTitle = "Edit Question";
/* 156 */             header = "";
/*     */           }
/* 158 */           else if (vq.getVoteEnd() > System.currentTimeMillis()) {
/*     */ 
/*     */             
/* 161 */             nextPart = 3;
/* 162 */             windowTitle = "View Question";
/* 163 */             header = vq.getQuestionTitle();
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 168 */             nextPart = 4;
/* 169 */             windowTitle = "Poll Results";
/* 170 */             header = vq.getQuestionTitle();
/*     */           } 
/* 172 */           igvsq = new InGameVoteSetupQuestion(getResponder(), windowTitle, header, nextPart, this.questionId);
/*     */           
/* 174 */           igvsq.sendQuestion();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1:
/* 179 */           this.questionId = VoteQuestions.getNextQuestionId();
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 184 */           delete = aAnswer.getProperty("remove");
/* 185 */           if (delete != null && Boolean.parseBoolean(delete)) {
/*     */ 
/*     */             
/* 188 */             VoteQuestions.deleteVoteQuestion(this.questionId);
/*     */             return;
/*     */           } 
/* 191 */           this.qtitle = aAnswer.getProperty("qtitle").trim();
/* 192 */           this.qtext = aAnswer.getProperty("qtext").trim();
/* 193 */           this.opt1 = aAnswer.getProperty("opt1").trim();
/* 194 */           this.opt2 = aAnswer.getProperty("opt2").trim();
/* 195 */           this.opt3 = aAnswer.getProperty("opt3").trim();
/* 196 */           this.opt4 = aAnswer.getProperty("opt4").trim();
/* 197 */           strMa = aAnswer.getProperty("ma");
/* 198 */           strPo = aAnswer.getProperty("po");
/* 199 */           strJk = aAnswer.getProperty("jk");
/* 200 */           strMr = aAnswer.getProperty("mr");
/* 201 */           strHots = aAnswer.getProperty("hots");
/* 202 */           strFreedom = aAnswer.getProperty("freedom");
/*     */           
/* 204 */           strVoteDay = aAnswer.getProperty("day");
/* 205 */           strVoteMonth = aAnswer.getProperty("month");
/* 206 */           strVoteYear = aAnswer.getProperty("year");
/* 207 */           strVoteHour = aAnswer.getProperty("hour");
/* 208 */           strVoteMins = aAnswer.getProperty("mins");
/* 209 */           strVoteLasts = aAnswer.getProperty("lasts");
/*     */           
/* 211 */           day = 1;
/* 212 */           month = 12;
/* 213 */           year = 2020;
/* 214 */           hour = 0;
/* 215 */           mins = 0;
/* 216 */           votelasts = 7;
/* 217 */           fail = "";
/*     */           
/*     */           try {
/* 220 */             day = Integer.parseInt(strVoteDay);
/* 221 */             month = Integer.parseInt(strVoteMonth);
/* 222 */             year = Integer.parseInt(strVoteYear);
/* 223 */             hour = Integer.parseInt(strVoteHour);
/* 224 */             mins = Integer.parseInt(strVoteMins);
/* 225 */             votelasts = Integer.parseInt(strVoteLasts);
/*     */           }
/* 227 */           catch (NumberFormatException nfe) {
/*     */ 
/*     */             
/* 230 */             fail = "Invalid number.";
/*     */           } 
/* 232 */           this.startDay = 0;
/* 233 */           this.startMonth = 0;
/* 234 */           this.startYear = 0;
/*     */           
/*     */           try {
/* 237 */             Date t = ft.parse(strVoteDay + "-" + (month + 1) + "-" + strVoteYear + " " + strVoteHour + ":" + strVoteMins);
/* 238 */             Calendar calStart = Calendar.getInstance();
/* 239 */             calStart.setTime(t);
/*     */             
/* 241 */             this.startDay = calStart.get(5);
/* 242 */             this.startMonth = calStart.get(2);
/* 243 */             this.startYear = calStart.get(1);
/*     */             
/* 245 */             this.voteStart = t.getTime();
/* 246 */             calStart.add(5, votelasts);
/* 247 */             this.voteEnd = calStart.getTimeInMillis();
/*     */           }
/* 249 */           catch (ParseException parseException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 254 */           this.ma = Boolean.parseBoolean(strMa);
/* 255 */           this.po = Boolean.parseBoolean(strPo);
/* 256 */           this.jk = Boolean.parseBoolean(strJk);
/* 257 */           this.mr = Boolean.parseBoolean(strMr);
/* 258 */           this.hots = Boolean.parseBoolean(strHots);
/* 259 */           this.freedom = Boolean.parseBoolean(strFreedom);
/*     */           
/* 261 */           this.serverIds = new HashSet<>();
/*     */           
/* 263 */           entries = Servers.getAllServers();
/* 264 */           for (x = 0; x < entries.length; x++) {
/*     */             
/* 266 */             String strServer = aAnswer.getProperty("s" + entries[x].getId());
/* 267 */             if (strServer != null && Boolean.parseBoolean(strServer))
/*     */             {
/*     */               
/* 270 */               this.serverIds.add(Integer.valueOf(entries[x].getId()));
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 280 */           if (fail.length() == 0)
/*     */           {
/*     */ 
/*     */             
/* 284 */             if (this.qtitle.length() < 5) {
/* 285 */               fail = "Title not long enough";
/* 286 */             } else if (this.qtext.length() < 10) {
/* 287 */               fail = "Question is not long enough";
/* 288 */             } else if (this.opt1.length() == 0 || this.opt2.length() == 0) {
/* 289 */               fail = "Must have at least 2 options to vote for";
/* 290 */             } else if (!this.jk && !this.mr && !this.hots && !this.freedom) {
/* 291 */               fail = "Must have at least one kingdom to be allowed to vote";
/* 292 */             } else if (this.serverIds.isEmpty()) {
/* 293 */               fail = "Must have at least one server selected";
/* 294 */             } else if (this.voteStart == 0L) {
/* 295 */               fail = "Start date is not a date";
/* 296 */             } else if (day != this.startDay) {
/* 297 */               fail = "Start day is not correct for month";
/* 298 */             } else if (month != this.startMonth) {
/* 299 */               fail = "Start month is not correct for year!";
/* 300 */             } else if (year != this.startYear || this.startYear < 2014) {
/* 301 */               fail = "Start year is wrong! or too early";
/* 302 */             } else if (this.voteEnd <= this.voteStart) {
/* 303 */               fail = "Vote duration is not long enough OR not a number";
/* 304 */             } else if (this.voteEnd < System.currentTimeMillis()) {
/* 305 */               fail = "Can't modify as voting has already ended";
/* 306 */             } else if (this.voteStart < System.currentTimeMillis()) {
/* 307 */               fail = "Can't modify as voting has already started";
/*     */             }  } 
/* 309 */           if (fail.length() > 0) {
/*     */ 
/*     */             
/* 312 */             InGameVoteSetupQuestion inGameVoteSetupQuestion = new InGameVoteSetupQuestion(getResponder(), this.title, "Error: " + fail, this.part, this.questionId, fail);
/*     */             
/* 314 */             inGameVoteSetupQuestion.setData(this.questionId, this.qtitle, this.qtext, this.opt1, this.opt2, this.opt3, this.opt4, this.ma, this.po, this.jk, this.mr, this.hots, this.freedom, this.voteStart, this.voteEnd, day, month, year, hour, mins, this.lasts, this.serverIds);
/*     */             
/* 316 */             inGameVoteSetupQuestion.sendQuestion();
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 321 */           servers = new ArrayList<>();
/* 322 */           for (Integer serverId : this.serverIds)
/*     */           {
/* 324 */             servers.add(new VoteServer(this.questionId, serverId.intValue()));
/*     */           }
/* 326 */           newVoteQuestion = new VoteQuestion(this.questionId, this.qtitle, this.qtext, this.opt1, this.opt2, this.opt3, this.opt4, this.ma, this.po, this.jk, this.mr, this.hots, this.freedom, this.voteStart, this.voteEnd, servers);
/*     */ 
/*     */           
/* 329 */           VoteQuestions.addVoteQuestion(newVoteQuestion, false);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 3:
/* 334 */           remove = aAnswer.getProperty("remove");
/* 335 */           if (remove != null && Boolean.parseBoolean(remove)) {
/*     */ 
/*     */             
/* 338 */             Calendar calNow = Calendar.getInstance();
/* 339 */             VoteQuestions.closeVoteing(this.questionId, calNow.getTimeInMillis());
/*     */             return;
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 366 */     this.width = 350;
/* 367 */     this.height = 500;
/* 368 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 370 */     buf.append(getBmlHeader());
/* 371 */     if (getResponder().getPower() >= 2) {
/*     */       try {
/*     */         String colour; ServerEntry[] servers; VoteQuestion[] vqs; int x; VoteQuestion vq;
/*     */         int rows;
/* 375 */         switch (this.part) {
/*     */           
/*     */           case 0:
/* 378 */             colour = "";
/*     */             
/* 380 */             servers = Servers.getAllServers();
/*     */             
/* 382 */             vqs = VoteQuestions.getVoteQuestions();
/*     */             
/* 384 */             buf.append("table{rows=\"" + (vqs.length + 1) + "\";cols=\"" + (4 + servers.length) + "\";label{text=\"\"};label{type=\"bold\";text=\"Question\"};label{type=\"bold\";text=\"Vote Start\"};label{type=\"bold\";text=\"Vote End\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 390 */             for (ServerEntry se : servers) {
/* 391 */               buf.append("label{type=\"bold\";text=\"" + se.getAbbreviation() + "\"};");
/*     */             }
/* 393 */             for (x = 0; x < vqs.length; x++) {
/*     */               
/* 395 */               if (vqs[x].getSent() == 0) {
/* 396 */                 colour = ";color=\"100,100,255\"";
/* 397 */               } else if (vqs[x].getVoteEnd() > System.currentTimeMillis()) {
/* 398 */                 colour = ";color=\"100,255,100\"";
/*     */               } else {
/* 400 */                 colour = "";
/*     */               } 
/* 402 */               buf.append("radio{group=\"qid\";id=\"" + vqs[x].getQuestionId() + "\"};label{text=\"" + vqs[x]
/* 403 */                   .getQuestionTitle() + "\"" + colour + "};label{text=\"" + 
/* 404 */                   Tickets.convertTime(vqs[x].getVoteStart()) + "\"" + colour + "};label{text=\"" + 
/* 405 */                   Tickets.convertTime(vqs[x].getVoteEnd() - 1000L) + "\"" + colour + "};");
/*     */ 
/*     */               
/* 408 */               List<VoteServer> vsl = vqs[x].getServers();
/* 409 */               for (ServerEntry se : servers) {
/*     */                 
/* 411 */                 String sss = " o";
/* 412 */                 for (VoteServer vs : vsl) {
/*     */                   
/* 414 */                   if (vs.getServerId() == se.getId()) {
/*     */                     
/* 416 */                     sss = " X";
/*     */                     break;
/*     */                   } 
/*     */                 } 
/* 420 */                 buf.append("label{text=\"" + sss + "\"};");
/*     */               } 
/*     */             } 
/* 423 */             buf.append("radio{group=\"qid\";id=\"0\";selected=\"true\"};label{text=\"New\"};label{text=\"\"};label{text=\"\"};");
/*     */ 
/*     */ 
/*     */             
/* 427 */             for (ServerEntry se : servers) {
/* 428 */               buf.append("label{text=\"" + se.getAbbreviation() + "\"};");
/*     */             }
/* 430 */             buf.append("}");
/*     */             
/* 432 */             this.width = 300 + servers.length * 20;
/* 433 */             this.height = 140 + vqs.length * 16;
/*     */             break;
/*     */           
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/* 439 */             buf.append(showQuestion());
/*     */             break;
/*     */           
/*     */           case 4:
/* 443 */             this.width = 500;
/* 444 */             this.height = 300;
/*     */ 
/*     */             
/* 447 */             vq = VoteQuestions.getVoteQuestion(this.questionId);
/* 448 */             rows = 3;
/* 449 */             if (vq.getOption3Text().length() > 0) rows++; 
/* 450 */             if (vq.getOption4Text().length() > 0) rows++;
/*     */             
/* 452 */             buf.append("table{rows=\"" + rows + "\";cols=\"3\";");
/* 453 */             buf.append("label{type=\"bold\";text=\"Options\"}label{type=\"bold\";text=\"Count\"}label{type=\"bold\";text=\"Percentage\"}");
/*     */ 
/*     */             
/* 456 */             buf.append(showSummaryRow(vq.getOption1Text(), vq.getOption1Count(), vq.getVoteCount()));
/* 457 */             buf.append(showSummaryRow(vq.getOption2Text(), vq.getOption2Count(), vq.getVoteCount()));
/* 458 */             buf.append(showSummaryRow(vq.getOption3Text(), vq.getOption3Count(), vq.getVoteCount()));
/* 459 */             buf.append(showSummaryRow(vq.getOption4Text(), vq.getOption4Count(), vq.getVoteCount()));
/*     */             
/* 461 */             buf.append("label{type=\"bold\";text=\"Total Players who voted\"};label{type=\"bold\";text=\"" + vq
/* 462 */                 .getVoteCount() + "\"};label{text=\"\"}");
/*     */             
/* 464 */             buf.append("}");
/* 465 */             buf.append("text{text=\"\"}");
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 498 */         buf.append(createAnswerButton2());
/*     */         
/* 500 */         getResponder().getCommunicator().sendBml(this.width, this.height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/* 502 */       catch (Exception e) {
/*     */ 
/*     */         
/* 505 */         logger.log(Level.WARNING, e.getMessage(), e);
/* 506 */         getResponder().getCommunicator().sendNormalServerMessage("Exception:" + e.getMessage());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private String showSummaryRow(String text, int count, int total) {
/* 513 */     if (text.length() == 0)
/* 514 */       return ""; 
/* 515 */     int perc = -1;
/* 516 */     String percText = "NA";
/* 517 */     String colour = "";
/* 518 */     if (total > 0) {
/*     */       
/* 520 */       perc = count * 100 / total;
/* 521 */       percText = "(" + perc + "%)";
/* 522 */       if (perc >= 75) {
/* 523 */         colour = ";color=\"100,255,100\"";
/* 524 */       } else if (perc <= 25) {
/* 525 */         colour = ";color=\"255,100,100\"";
/*     */       } else {
/* 527 */         colour = ";color=\"100,100,255\"";
/*     */       } 
/*     */     } 
/* 530 */     StringBuilder buf = new StringBuilder();
/* 531 */     buf.append("label{text=\"" + text + "\"" + colour + "}label{text=\"" + count + "\"" + colour + "}label{text=\"" + percText + "\"" + colour + "};");
/*     */ 
/*     */     
/* 534 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showQuestion() {
/* 539 */     Calendar calStart = Calendar.getInstance();
/* 540 */     Calendar calEnd = Calendar.getInstance();
/* 541 */     VoteQuestion vq = null;
/* 542 */     String disabled = "";
/* 543 */     String noamend = "";
/* 544 */     String remove = "";
/* 545 */     String totalVoted = "";
/*     */     
/* 547 */     if (this.part == 1) {
/*     */       
/* 549 */       if (this.failReason.length() == 0)
/*     */       {
/* 551 */         this.qtitle = "";
/* 552 */         this.qtext = "";
/* 553 */         this.opt1 = "";
/* 554 */         this.opt2 = "";
/* 555 */         this.opt3 = "";
/* 556 */         this.opt4 = "";
/* 557 */         this.ma = false;
/* 558 */         this.po = false;
/* 559 */         this.jk = true;
/* 560 */         this.mr = true;
/* 561 */         this.hots = true;
/* 562 */         this.freedom = true;
/*     */         
/* 564 */         calStart.add(5, 1);
/* 565 */         calEnd.add(5, 8);
/* 566 */         this.startDay = calStart.get(5);
/* 567 */         this.startMonth = calStart.get(2);
/* 568 */         this.startYear = calStart.get(1);
/* 569 */         this.startHour = 0;
/* 570 */         this.startMins = 0;
/* 571 */         this.lasts = (int)daysBetween(calStart, calEnd);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 576 */     else if (this.failReason.length() == 0) {
/*     */       
/* 578 */       vq = VoteQuestions.getVoteQuestion(this.questionId);
/* 579 */       calStart.setTimeInMillis(vq.getVoteStart());
/* 580 */       calEnd.setTimeInMillis(vq.getVoteEnd());
/* 581 */       this.qtitle = vq.getQuestionTitle();
/* 582 */       this.qtext = vq.getQuestionText();
/* 583 */       this.opt1 = vq.getOption1Text();
/* 584 */       this.opt2 = vq.getOption2Text();
/* 585 */       this.opt3 = vq.getOption3Text();
/* 586 */       this.opt4 = vq.getOption4Text();
/* 587 */       this.ma = vq.isAllowMultiple();
/* 588 */       this.po = vq.isPremOnly();
/* 589 */       this.jk = vq.isJK();
/* 590 */       this.mr = vq.isMR();
/* 591 */       this.hots = vq.isHots();
/* 592 */       this.freedom = vq.isFreedom();
/*     */       
/* 594 */       this.startDay = calStart.get(5);
/* 595 */       this.startMonth = calStart.get(2);
/* 596 */       this.startYear = calStart.get(1);
/* 597 */       this.startHour = calStart.get(11);
/* 598 */       this.startMins = calStart.get(12);
/* 599 */       this.lasts = (int)daysBetween(calStart, calEnd);
/*     */       
/* 601 */       if (this.part == 3) {
/*     */         
/* 603 */         remove = "checkbox{id=\"remove\";text=\"Close Poll?\"};";
/*     */         
/* 605 */         short total = 0;
/* 606 */         short count1 = 0;
/* 607 */         short count2 = 0;
/* 608 */         short count3 = 0;
/* 609 */         short count4 = 0;
/* 610 */         for (PlayerVote pv : PlayerVotes.getPlayerVotesByQuestion(vq.getQuestionId())) {
/*     */           
/* 612 */           total = (short)(total + 1);
/* 613 */           if (pv.getOption1()) count1 = (short)(count1 + 1); 
/* 614 */           if (pv.getOption2()) count2 = (short)(count2 + 1); 
/* 615 */           if (pv.getOption3()) count3 = (short)(count3 + 1); 
/* 616 */           if (pv.getOption4()) count4 = (short)(count4 + 1); 
/*     */         } 
/* 618 */         totalVoted = " (" + total + " voted so far).";
/* 619 */         if (total > 0) {
/*     */           
/* 621 */           if (this.opt1.length() > 0)
/* 622 */             this.opt1 += " (" + count1 + ":" + (count1 * 100 / total) + "%)"; 
/* 623 */           if (this.opt2.length() > 0)
/* 624 */             this.opt2 += " (" + count2 + ":" + (count2 * 100 / total) + "%)"; 
/* 625 */           if (this.opt3.length() > 0)
/* 626 */             this.opt3 += " (" + count3 + ":" + (count3 * 100 / total) + "%)"; 
/* 627 */           if (this.opt4.length() > 0) {
/* 628 */             this.opt4 += " (" + count4 + ":" + (count4 * 100 / total) + "%)";
/*     */           }
/*     */         } 
/*     */       } else {
/* 632 */         remove = "checkbox{id=\"remove\";text=\"Delete Poll?\"};";
/*     */       } 
/*     */     } 
/* 635 */     if (this.part == 3) {
/*     */       
/* 637 */       disabled = ";enabled=\"false\"";
/* 638 */       noamend = ";label{type=\"bolditalic\";color=\"255,100,100\";text=\"  In progress so cant amend" + totalVoted + "\"}";
/*     */     } 
/*     */     
/* 641 */     StringBuilder buf = new StringBuilder();
/* 642 */     buf.append("harray{label{text=\"Question:  Title->\"};input{id=\"qtitle\";maxchars=\"40\";text=\"" + this.qtitle + "\"" + disabled + "}" + noamend + "}");
/* 643 */     buf.append("input{id=\"qtext\";maxlines=\"3\";maxchars=\"200\";text=\"" + this.qtext + "\"" + disabled + "}");
/* 644 */     buf.append("harray{label{text=\"Option 1 [\"};label{color=\"100,100,255\";text=\"Mandatory\"};label{text=\"]:\"};input{id=\"opt1\";maxchars=\"50\";text=\"" + this.opt1 + "\"" + disabled + "}}");
/*     */     
/* 646 */     buf.append("harray{label{text=\"Option 2 [\"};label{color=\"100,100,255\";text=\"Mandatory\"};label{text=\"]:\"};input{id=\"opt2\";maxchars=\"50\";text=\"" + this.opt2 + "\"" + disabled + "}}");
/*     */     
/* 648 */     buf.append("harray{label{text=\"Option 3 [ Optional ]  :\"};input{id=\"opt3\";maxchars=\"50\";text=\"" + this.opt3 + "\"" + disabled + "}}");
/*     */     
/* 650 */     buf.append("harray{label{text=\"Option 4 [ Optional ]  :\"};input{id=\"opt4\";maxchars=\"50\";text=\"" + this.opt4 + "\"" + disabled + "}}");
/*     */     
/* 652 */     buf.append("harray{label{text=\"Multiple answers allowed?\"};checkbox{id=\"ma\";selected=\"" + this.ma + "\"" + disabled + "};label{text=\"   Premium Only?\"};checkbox{id=\"po\";selected=\"" + this.po + "\"" + disabled + "}}");
/*     */     
/* 654 */     buf.append("harray{label{type=\"bold\";text=\"By Kingdoms: \"}label{text=\"JK?\"};checkbox{id=\"jk\";selected=\"" + this.jk + "\"" + disabled + "}label{text=\"MR?\"};checkbox{id=\"mr\";selected=\"" + this.mr + "\"" + disabled + "}label{text=\"HOTS?\"};checkbox{id=\"hots\";selected=\"" + this.hots + "\"" + disabled + "}label{text=\"Freedom?\"};checkbox{id=\"freedom\";selected=\"" + this.freedom + "\"" + disabled + "}}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 659 */     buf.append("label{type=\"bold\";text=\"On Server(s) - must have at least one\"}");
/*     */     
/* 661 */     ServerEntry[] entries = Servers.getAllServers();
/* 662 */     for (int x = 0; x < entries.length; x++) {
/*     */ 
/*     */       
/* 665 */       String selected = "";
/* 666 */       if (vq != null) {
/*     */         
/* 668 */         for (VoteServer vs : vq.getServers()) {
/*     */           
/* 670 */           if (vs.getServerId() == entries[x].getId()) {
/*     */             
/* 672 */             selected = ";selected=\"true\"";
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 679 */         for (Integer sid : this.serverIds) {
/*     */           
/* 681 */           if (sid.intValue() == entries[x].getId()) {
/*     */             
/* 683 */             selected = ";selected=\"true\"";
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 688 */       if (entries[x].isAvailable(getResponder().getPower(), getResponder().isPaying())) {
/*     */         
/* 690 */         buf.append("harray{label{color=\"100,255,100\";text=\"" + entries[x].getName() + "?\"}checkbox{id=\"s" + entries[x]
/* 691 */             .getId() + "\"" + selected + disabled + "}}");
/*     */       }
/*     */       else {
/*     */         
/* 695 */         buf.append("harray{label{color=\"255,100,100\";text=\"" + entries[x].getName() + "?\"}checkbox{id=\"s" + entries[x]
/* 696 */             .getId() + "\"" + selected + disabled + "}}");
/*     */       } 
/*     */     } 
/* 699 */     String textMonth = (new DateFormatSymbols()).getMonths()[this.startMonth];
/* 700 */     String txtMonth = textMonth.substring(0, 3);
/* 701 */     String strMonth = "";
/* 702 */     String strHour = "";
/* 703 */     String strMins = "";
/* 704 */     if (this.part == 3) {
/*     */       
/* 706 */       strMonth = "input{id=\"month\";maxchars=\"3\";text=\"" + txtMonth + "\";enabled=\"false\"}";
/* 707 */       strHour = "input{id=\"hour\";maxchars=\"2\";text=\"" + ((this.startHour < 10) ? "0" : "") + this.startHour + "\";enabled=\"false\"}";
/* 708 */       strMins = "input{id=\"mins\";maxchars=\"2\";text=\"" + ((this.startMins < 10) ? "0" : "") + this.startMins + "\";enabled=\"false\"}";
/*     */     }
/*     */     else {
/*     */       
/* 712 */       strMonth = "dropdown{id=\"month\";default=\"" + this.startMonth + "\";options=\"Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec\"}";
/* 713 */       strHour = "dropdown{id=\"hour\";default=\"" + this.startHour + "\";options=\"00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23\"}";
/* 714 */       strMins = "dropdown{id=\"mins\";default=\"" + this.startMins + "\";options=\"00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59\"}";
/*     */     } 
/*     */ 
/*     */     
/* 718 */     buf.append("harray{label{text=\"Start:\"};input{id=\"day\";maxchars=\"2\";text=\"" + this.startDay + "\"" + disabled + "};label{text=\"/\"};" + strMonth + ";label{text=\"/\"};input{id=\"year\";maxchars=\"4\";text=\"" + this.startYear + "\"" + disabled + "};label{text=\" \"};" + strHour + "label{text=\":\"};" + strMins + "}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 724 */     buf.append("harray{label{text=\"Duration (in days):\"};input{id=\"lasts\";maxchars=\"2\";text=\"" + this.lasts + "\"" + disabled + "}}");
/*     */     
/* 726 */     if (remove.length() > 0) {
/* 727 */       buf.append(remove);
/*     */     }
/* 729 */     this.width = 400;
/* 730 */     this.height = 380 + entries.length * 16;
/*     */     
/* 732 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(int aquestionId, String aqtitle, String aqtext, String aopt1, String aopt2, String aopt3, String aopt4, boolean ama, boolean apo, boolean ajk, boolean amr, boolean ahots, boolean afreedom, long avoteStart, long avoteEnd, int avoteDay, int avoteMonth, int avoteYear, int aVoteHour, int aVoteMins, int alasts, Set<Integer> aserverIds) {
/* 741 */     this.questionId = aquestionId;
/* 742 */     this.qtitle = aqtitle;
/* 743 */     this.qtext = aqtext;
/* 744 */     this.opt1 = aopt1;
/* 745 */     this.opt2 = aopt2;
/* 746 */     this.opt3 = aopt3;
/* 747 */     this.opt4 = aopt4;
/* 748 */     this.ma = ama;
/* 749 */     this.po = apo;
/* 750 */     this.jk = ajk;
/* 751 */     this.mr = amr;
/* 752 */     this.hots = ahots;
/* 753 */     this.freedom = afreedom;
/* 754 */     this.voteStart = avoteStart;
/* 755 */     this.voteEnd = avoteEnd;
/* 756 */     this.startDay = avoteDay;
/* 757 */     this.startMonth = avoteMonth;
/* 758 */     this.startYear = avoteYear;
/* 759 */     this.startHour = aVoteHour;
/* 760 */     this.startMins = aVoteMins;
/* 761 */     this.lasts = alasts;
/* 762 */     this.serverIds = aserverIds;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long daysBetween(Calendar startDate, Calendar endDate) {
/* 768 */     Calendar date = (Calendar)startDate.clone();
/* 769 */     long daysBetween = 0L;
/* 770 */     while (date.before(endDate)) {
/*     */       
/* 772 */       date.add(5, 1);
/* 773 */       daysBetween++;
/*     */     } 
/* 775 */     return daysBetween;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\InGameVoteSetupQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */