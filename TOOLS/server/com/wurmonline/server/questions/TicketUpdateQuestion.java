/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.support.Ticket;
/*     */ import com.wurmonline.server.support.TicketAction;
/*     */ import com.wurmonline.server.support.Tickets;
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
/*     */ 
/*     */ public class TicketUpdateQuestion
/*     */   extends Question
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(TicketUpdateQuestion.class.getName());
/*     */   
/*     */   private int ticketId;
/*     */   private short action;
/*     */   
/*     */   public TicketUpdateQuestion(Creature aResponder, int aTicketId, short aAction) {
/*  45 */     super(aResponder, "Ticket: #" + aTicketId, makeQuestion(aResponder, aAction), 108, aTicketId);
/*     */     
/*  47 */     this.ticketId = aTicketId;
/*  48 */     this.action = aAction;
/*     */   }
/*     */ 
/*     */   
/*     */   static String makeQuestion(Creature aResponder, short aAction) {
/*  53 */     switch (aAction) {
/*     */       
/*     */       case 588:
/*  56 */         return "Add reason why you are cancelling this ticket.";
/*     */       
/*     */       case 590:
/*  59 */         return "Add note for how the ticket was resolved.";
/*     */       
/*     */       case 596:
/*  62 */         return "Please add why you are passing it back to CMs.";
/*     */       
/*     */       case 591:
/*  65 */         return "Please add why you are passing this to GMs.";
/*     */       
/*     */       case 592:
/*  68 */         return "Please add why you are passing this to Arch.";
/*     */       
/*     */       case 593:
/*  71 */         return "Please add why you are passing this to Dev.";
/*     */       
/*     */       case 594:
/*  74 */         return "Please add why you are putting this ticket on hold.";
/*     */       
/*     */       case 587:
/*  77 */         if (((Player)aResponder).mayHearDevTalk() || ((Player)aResponder).mayHearMgmtTalk()) {
/*  78 */           return "Add note to ticket.";
/*     */         }
/*  80 */         return "Append to ticket description.";
/*     */       
/*     */       case 597:
/*  83 */         return "Ticket Feedback.";
/*     */     } 
/*     */     
/*  86 */     return "Question for unknown action " + aAction;
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
/*     */   public void answer(Properties aAnswer) {
/*  98 */     setAnswer(aAnswer);
/*  99 */     if (this.type == 0) {
/*     */       
/* 101 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/* 104 */     if (this.type == 108) {
/*     */       byte service, courteous, knowledgeable; boolean general1, general2, general3, general4, general5, general6, general7; byte general; boolean quality1, quality2, quality3, quality4, quality5, quality6; byte quality; boolean irked1, irked2, irked3, irked4, irked5, irked6; byte irked;
/* 106 */       this.ticketId = Integer.parseInt(aAnswer.getProperty("tid"));
/* 107 */       this.action = Short.parseShort(aAnswer.getProperty("action"));
/* 108 */       boolean append = Boolean.parseBoolean(aAnswer.getProperty("append"));
/* 109 */       byte level = Byte.parseByte(aAnswer.getProperty("level"));
/* 110 */       String note = aAnswer.getProperty("note");
/* 111 */       note = note.replace('"', '\'');
/*     */       
/* 113 */       if (note.length() == 0) {
/*     */         
/* 115 */         getResponder().getCommunicator().sendNormalServerMessage("Must supply a note for ticket action");
/*     */         return;
/*     */       } 
/* 118 */       Ticket ticket = Tickets.getTicket(this.ticketId);
/* 119 */       if (this.action == 587 && append)
/*     */       {
/* 121 */         if (note.length() > 0)
/*     */         {
/*     */           
/* 124 */           ticket.appendDescription(note);
/*     */         }
/*     */       }
/* 127 */       switch (this.action) {
/*     */         
/*     */         case 588:
/* 130 */           ticket.addNewTicketAction((byte)1, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 596:
/* 134 */           ticket.addNewTicketAction((byte)13, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 591:
/* 138 */           ticket.addNewTicketAction((byte)6, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 592:
/* 142 */           ticket.addNewTicketAction((byte)7, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 593:
/* 146 */           ticket.addNewTicketAction((byte)8, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 594:
/* 150 */           ticket.addNewTicketAction((byte)10, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 590:
/* 154 */           ticket.addNewTicketAction((byte)9, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 599:
/* 158 */           ticket.addNewTicketAction((byte)15, getResponder().getName(), note, level);
/*     */           break;
/*     */         
/*     */         case 587:
/* 162 */           if (note.length() > 0) {
/* 163 */             ticket.addNewTicketAction((byte)0, getResponder().getName(), note, level);
/*     */           }
/*     */           break;
/*     */         case 597:
/* 167 */           if (ticket.hasFeedback()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 174 */           service = Byte.parseByte(aAnswer.getProperty("service"));
/* 175 */           courteous = Byte.parseByte(aAnswer.getProperty("courteous"));
/* 176 */           knowledgeable = Byte.parseByte(aAnswer.getProperty("knowledgeable"));
/* 177 */           general1 = Boolean.parseBoolean(aAnswer.getProperty("general1"));
/* 178 */           general2 = Boolean.parseBoolean(aAnswer.getProperty("general2"));
/* 179 */           general3 = Boolean.parseBoolean(aAnswer.getProperty("general3"));
/* 180 */           general4 = Boolean.parseBoolean(aAnswer.getProperty("general4"));
/* 181 */           general5 = Boolean.parseBoolean(aAnswer.getProperty("general5"));
/* 182 */           general6 = Boolean.parseBoolean(aAnswer.getProperty("general6"));
/* 183 */           general7 = Boolean.parseBoolean(aAnswer.getProperty("general7"));
/* 184 */           general = (byte)((general1 ? 1 : 0) + (general2 ? 2 : 0) + (general3 ? 4 : 0) + (general4 ? 8 : 0) + (general5 ? 16 : 0) + (general6 ? 32 : 0) + (general7 ? 64 : 0));
/*     */ 
/*     */           
/* 187 */           quality1 = Boolean.parseBoolean(aAnswer.getProperty("quality1"));
/* 188 */           quality2 = Boolean.parseBoolean(aAnswer.getProperty("quality2"));
/* 189 */           quality3 = Boolean.parseBoolean(aAnswer.getProperty("quality3"));
/* 190 */           quality4 = Boolean.parseBoolean(aAnswer.getProperty("quality4"));
/* 191 */           quality5 = Boolean.parseBoolean(aAnswer.getProperty("quality5"));
/* 192 */           quality6 = Boolean.parseBoolean(aAnswer.getProperty("quality6"));
/* 193 */           quality = (byte)((quality1 ? 1 : 0) + (quality2 ? 2 : 0) + (quality3 ? 4 : 0) + (quality4 ? 8 : 0) + (quality5 ? 16 : 0) + (quality6 ? 32 : 0));
/*     */ 
/*     */           
/* 196 */           irked1 = Boolean.parseBoolean(aAnswer.getProperty("irked1"));
/* 197 */           irked2 = Boolean.parseBoolean(aAnswer.getProperty("irked2"));
/* 198 */           irked3 = Boolean.parseBoolean(aAnswer.getProperty("irked3"));
/* 199 */           irked4 = Boolean.parseBoolean(aAnswer.getProperty("irked4"));
/* 200 */           irked5 = Boolean.parseBoolean(aAnswer.getProperty("irked5"));
/* 201 */           irked6 = Boolean.parseBoolean(aAnswer.getProperty("irked6"));
/* 202 */           irked = (byte)((irked1 ? 1 : 0) + (irked2 ? 2 : 0) + (irked3 ? 4 : 0) + (irked4 ? 8 : 0) + (irked5 ? 16 : 0) + (irked6 ? 32 : 0));
/*     */ 
/*     */ 
/*     */           
/* 206 */           ticket.addNewTicketAction((byte)14, getResponder().getName(), note, level, service, courteous, knowledgeable, general, quality, irked);
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
/*     */   public void sendQuestion() {
/* 225 */     StringBuilder buf = new StringBuilder();
/* 226 */     Ticket ticket = Tickets.getTicket(this.ticketId);
/* 227 */     Player player = (Player)getResponder();
/*     */     
/* 229 */     int lines = 0;
/* 230 */     buf.append(getBmlHeader());
/* 231 */     buf.append("passthrough{id=\"tid\";text=\"" + this.ticketId + "\"}");
/* 232 */     buf.append("passthrough{id=\"action\";text=\"" + this.action + "\"}");
/*     */ 
/*     */ 
/*     */     
/* 236 */     buf.append("text{type=\"bolditalic\";text=\"Description: " + ticket.getDateAsString() + "\"};");
/* 237 */     buf.append("text{text=\"" + ticket.getDescription() + "\"};");
/*     */ 
/*     */     
/* 240 */     String[] descLines = ticket.getDescription().split("\\n");
/* 241 */     int descLineCount = descLines.length;
/*     */     
/* 243 */     buf.append("text{type=\"bolditalic\";text=\"Actions so far:\"};");
/* 244 */     buf.append("text{color=\"66,255,255\";type=\"italic\";text=\"Times are in server time.\"};");
/* 245 */     TicketAction[] ticketActions = ticket.getTicketActions(player);
/* 246 */     if (ticketActions.length == 0) {
/*     */       
/* 248 */       buf.append("text{text=\"none!\"};");
/*     */     }
/*     */     else {
/*     */       
/* 252 */       for (TicketAction ta : ticketActions) {
/*     */         
/* 254 */         buf.append("text{text=\"" + ta.getLine(player) + "\"};");
/* 255 */         String note = ta.getNotePlus(player);
/* 256 */         if (note.length() > 0) {
/*     */           
/* 258 */           buf.append("text{text=\"  " + note + "\"};");
/* 259 */           lines++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     if (player.getWurmId() == ticket.getPlayerId() && this.action != 591) {
/*     */       
/* 266 */       buf.append("passthrough{id=\"level\";text=\"0\"}");
/*     */       
/* 268 */       if (ticket.isOpen())
/*     */       {
/* 270 */         if (this.action == 588) {
/*     */ 
/*     */           
/* 273 */           buf.append("passthrough{id=\"append\";text=\"false\"}");
/*     */           
/* 275 */           buf.append("text{type=\"bold\";text=\"Please do let us know how you resolved this ticket.\"};");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 280 */           buf.append("checkbox{id=\"append\";text=\"Select to append to description.\";selected=\"false\"};");
/*     */           
/* 282 */           buf.append("text{type=\"bold\";text=\"You may add a note which can be optionally appended to the description.\"};");
/*     */         } 
/* 284 */         buf.append("input{id=\"note\";maxchars=\"200\";text=\"\"}");
/*     */       }
/* 286 */       else if (this.action == 597)
/*     */       {
/* 288 */         buf.append("passthrough{id=\"append\";text=\"false\"}");
/* 289 */         if (ticket.hasFeedback()) {
/* 290 */           showReadOnlyFeedback(ticket, buf);
/*     */         } else {
/*     */           
/* 293 */           buf.append("text{text=\"\"}");
/* 294 */           buf.append("text{type=\"bold\";color=\"66,255,255\";text=\"Feedback Survey\"}");
/* 295 */           buf.append("text{type=\"bold\";text=\"How would you rate the quality of service for this ticket?\"}");
/* 296 */           buf.append("radio{group=\"service\";id=\"0\";hidden=\"true\";text=\"hidden\"}");
/* 297 */           buf.append("radio{group=\"service\";id=\"1\";text=\"Superior\"}");
/* 298 */           buf.append("radio{group=\"service\";id=\"2\";text=\"Good\"}");
/* 299 */           buf.append("radio{group=\"service\";id=\"3\";text=\"Average\"}");
/* 300 */           buf.append("radio{group=\"service\";id=\"4\";text=\"Fair\"}");
/* 301 */           buf.append("radio{group=\"service\";id=\"5\";text=\"Poor\"}");
/* 302 */           buf.append("text{type=\"bold\";text=\"The Support Team was very courteous:\"}");
/* 303 */           buf.append("radio{group=\"courteous\";id=\"0\";hidden=\"true\";text=\"hidden\"}");
/* 304 */           buf.append("radio{group=\"courteous\";id=\"1\";text=\"Strongly Agree\"}");
/* 305 */           buf.append("radio{group=\"courteous\";id=\"2\";text=\"Somewhat Agree\"}");
/* 306 */           buf.append("radio{group=\"courteous\";id=\"3\";text=\"Neutral\"}");
/* 307 */           buf.append("radio{group=\"courteous\";id=\"4\";text=\"Somewhat Disagree\"}");
/* 308 */           buf.append("radio{group=\"courteous\";id=\"5\";text=\"Strongly Disagree\"}");
/* 309 */           buf.append("text{type=\"bold\";text=\"The Support Team was very knowledgeable:\"}");
/* 310 */           buf.append("radio{group=\"knowledgeable\";id=\"0\";hidden=\"true\";text=\"hidden\"}");
/* 311 */           buf.append("radio{group=\"knowledgeable\";id=\"1\";text=\"Strongly Agree\"}");
/* 312 */           buf.append("radio{group=\"knowledgeable\";id=\"2\";text=\"Somewhat Agree\"}");
/* 313 */           buf.append("radio{group=\"knowledgeable\";id=\"3\";text=\"Neutral\"}");
/* 314 */           buf.append("radio{group=\"knowledgeable\";id=\"4\";text=\"Somewhat Disagree\"}");
/* 315 */           buf.append("radio{group=\"knowledgeable\";id=\"5\";text=\"Strongly Disagree\"}");
/* 316 */           buf.append("text{type=\"bold\";text=\"The Support Team:\"}");
/* 317 */           buf.append("checkbox{id=\"general1\";text=\"Gave me the wrong information\"}");
/* 318 */           buf.append("checkbox{id=\"general2\";text=\"Didn't understand the question\"}");
/* 319 */           buf.append("checkbox{id=\"general3\";text=\"Gave unclear answers\"}");
/* 320 */           buf.append("checkbox{id=\"general4\";text=\"Couldn't solve the problem\"}");
/* 321 */           buf.append("checkbox{id=\"general5\";text=\"Was disorganized\"}");
/* 322 */           buf.append("checkbox{id=\"general6\";text=\"Other\"}");
/* 323 */           buf.append("checkbox{id=\"general7\";text=\"No improvement needed\"}");
/* 324 */           buf.append("text{type=\"bold\";text=\"Which of the following qualities of the Support Team stood out?\"}");
/* 325 */           buf.append("checkbox{id=\"quality1\";text=\"Patient\"}");
/* 326 */           buf.append("checkbox{id=\"quality2\";text=\"Enthusiastic\"}");
/* 327 */           buf.append("checkbox{id=\"quality3\";text=\"Listened Carefully\"}");
/* 328 */           buf.append("checkbox{id=\"quality4\";text=\"Friendly\"}");
/* 329 */           buf.append("checkbox{id=\"quality5\";text=\"Responsive\"}");
/* 330 */           buf.append("checkbox{id=\"quality6\";text=\"Nothing stood out.\"}");
/* 331 */           buf.append("text{type=\"bold\";text=\"What qualities of the Support Team irked you?\"}");
/* 332 */           buf.append("checkbox{id=\"irked1\";text=\"Not Patient\"}");
/* 333 */           buf.append("checkbox{id=\"irked2\";text=\"Not Enthusiastic\"}");
/* 334 */           buf.append("checkbox{id=\"irked3\";text=\"Didn't Listen Carefully\"}");
/* 335 */           buf.append("checkbox{id=\"irked4\";text=\"Unfriendly\"}");
/* 336 */           buf.append("checkbox{id=\"irked5\";text=\"Unresponsive\"}");
/* 337 */           buf.append("checkbox{id=\"irked6\";text=\"No qualities irked me\"}");
/* 338 */           buf.append("text{type=\"bold\";text=\"Briefly describe any aspect of the process and/or team member which you considered outstanding or could be improved.\"}");
/* 339 */           buf.append("input{id=\"note\";maxchars=\"200\";text=\"\"}");
/* 340 */           buf.append("text{text=\"Thank you for your feedback. We sincerely appreciate your honest opinion and will take your input into consideration while providing services in the future.\"};");
/*     */         }
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 346 */         buf.append("passthrough{id=\"append\";text=\"false\"}");
/* 347 */         buf.append("passthrough{id=\"note\";text=\"\"}");
/*     */       }
/*     */     
/* 350 */     } else if (player.mayHearDevTalk() && this.action == 597 && ticket.hasFeedback()) {
/*     */       
/* 352 */       buf.append("passthrough{id=\"append\";text=\"false\"}");
/* 353 */       showReadOnlyFeedback(ticket, buf);
/*     */     }
/* 355 */     else if (player.mayMute()) {
/*     */       
/* 357 */       buf.append("label{type=\"bold\";text=\"Who can see the associated note?\"};");
/* 358 */       buf.append("harray{");
/* 359 */       buf.append("radio{group=\"level\";id=\"0\";text=\"All?\"};");
/* 360 */       if (player.mayHearDevTalk()) {
/*     */         
/* 362 */         buf.append("radio{group=\"level\";id=\"1\";text=\"CM and above?\"};");
/* 363 */         buf.append("radio{group=\"level\";id=\"2\";text=\"GM and above?\";selected=\"true\"};");
/*     */       } else {
/*     */         
/* 366 */         buf.append("radio{group=\"level\";id=\"1\";text=\"CM and above?\";selected=\"true\"};");
/*     */       } 
/* 368 */       buf.append("}");
/* 369 */       buf.append("passthrough{id=\"append\";text=\"false\"}");
/*     */       
/* 371 */       if (this.action == 590) {
/* 372 */         buf.append("text{type=\"bold\";text=\"How was this ticket resolved?\"};");
/* 373 */       } else if (this.action == 594) {
/* 374 */         buf.append("text{type=\"bold\";text=\"Why was this ticket put on hold?\"};");
/* 375 */       } else if (this.action == 587) {
/* 376 */         buf.append("text{type=\"bold\";text=\"Please add your note here.\"};");
/* 377 */       } else if (this.action == 596) {
/* 378 */         buf.append("text{type=\"bold\";text=\"Why are you forwarding this to CM?\"};");
/* 379 */       } else if (this.action == 591) {
/* 380 */         buf.append("text{type=\"bold\";text=\"Why are you forwarding this to GM?\"};");
/* 381 */       } else if (this.action == 592) {
/* 382 */         buf.append("text{type=\"bold\";text=\"Why are you forwarding this to Arch?\"};");
/* 383 */       } else if (this.action == 593) {
/* 384 */         buf.append("text{type=\"bold\";text=\"Why are you forwarding this to Dev?\"};");
/* 385 */       } else if (this.action == 599) {
/* 386 */         buf.append("text{type=\"bold\";text=\"Why are you Re-opening this?\"};");
/* 387 */       }  buf.append("input{id=\"note\";maxchars=\"200\";text=\"\"}");
/*     */     } 
/*     */     
/* 390 */     buf.append(createAnswerButton2());
/* 391 */     int height = Math.min(220 + ticketActions.length * 23 + lines * 23 + descLineCount * 23, 500);
/* 392 */     getResponder().getCommunicator().sendBml(500, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private void showReadOnlyFeedback(Ticket ticket, StringBuilder buf) {
/* 397 */     TicketAction ta = ticket.getFeedback();
/* 398 */     buf.append("passthrough{id=\"note\";text=\"\"}");
/* 399 */     buf.append("text{text=\"\"}");
/*     */ 
/*     */     
/* 402 */     buf.append("text{type=\"bold\";color=\"66,255,255\";text=\"Feedback Survey - Read Only\"}");
/* 403 */     buf.append("text{type=\"bold\";text=\"How would you rate the quality of service for this ticket?\"}");
/* 404 */     buf.append("radio{group=\"service\";id=\"1\";text=\"Superior\";enabled=\"false\";selected=\"" + ta.wasServiceSuperior() + "\"}");
/* 405 */     buf.append("radio{group=\"service\";id=\"2\";text=\"Good\";enabled=\"false\";selected=\"" + ta.wasServiceGood() + "\"}");
/* 406 */     buf.append("radio{group=\"service\";id=\"3\";text=\"Average\";enabled=\"false\";selected=\"" + ta.wasServiceAverage() + "\"}");
/* 407 */     buf.append("radio{group=\"service\";id=\"4\";text=\"Fair\";enabled=\"false\";selected=\"" + ta.wasServiceFair() + "\"}");
/* 408 */     buf.append("radio{group=\"service\";id=\"5\";text=\"Poor\";enabled=\"false\";selected=\"" + ta.wasServicePoor() + "\"}");
/* 409 */     buf.append("text{type=\"bold\";text=\"The Support Team was very courteous:\"}");
/* 410 */     buf.append("radio{group=\"courteous\";id=\"1\";text=\"Strongly Agree\";enabled=\"false\";selected=\"" + ta.wasCourteousStronglyAgree() + "\"}");
/* 411 */     buf.append("radio{group=\"courteous\";id=\"2\";text=\"Somewhat Agree\";enabled=\"false\";selected=\"" + ta.wasCourteousSomewhatAgree() + "\"}");
/* 412 */     buf.append("radio{group=\"courteous\";id=\"3\";text=\"Neutral\";enabled=\"false\";selected=\"" + ta.wasCourteousNeutral() + "\"}");
/* 413 */     buf.append("radio{group=\"courteous\";id=\"4\";text=\"Somewhat Disagree\";enabled=\"false\";selected=\"" + ta.wasCourteousSomewhatDisagree() + "\"}");
/* 414 */     buf.append("radio{group=\"courteous\";id=\"5\";text=\"Strongly Disagree\";enabled=\"false\";selected=\"" + ta.wasCourteousStronglyDisagree() + "\"}");
/* 415 */     buf.append("text{type=\"bold\";text=\"The Support Team was very knowledgeable:\"}");
/* 416 */     buf.append("radio{group=\"knowledgeable\";id=\"1\";text=\"Strongly Agree\";enabled=\"false\";selected=\"" + ta.wasKnowledgeableStronglyAgree() + "\"}");
/* 417 */     buf.append("radio{group=\"knowledgeable\";id=\"2\";text=\"Somewhat Agree\";enabled=\"false\";selected=\"" + ta.wasKnowledgeableSomewhatAgree() + "\"}");
/* 418 */     buf.append("radio{group=\"knowledgeable\";id=\"3\";text=\"Neutral\";enabled=\"false\";selected=\"" + ta.wasKnowledgeableNeutral() + "\"}");
/* 419 */     buf.append("radio{group=\"knowledgeable\";id=\"4\";text=\"Somewhat Disagree\";enabled=\"false\";selected=\"" + ta.wasKnowledgeableSomewhatDisagree() + "\"}");
/* 420 */     buf.append("radio{group=\"knowledgeable\";id=\"5\";text=\"Strongly Disagree\";enabled=\"false\";selected=\"" + ta.wasKnowledgeableStronglyDisagree() + "\"}");
/* 421 */     buf.append("text{type=\"bold\";text=\"The Support Team:\"}");
/* 422 */     buf.append("checkbox{id=\"general1\";text=\"Gave me the wrong information\";enabled=\"false\";selected=\"" + ta.wasGeneralWrongInfo() + "\"}");
/* 423 */     buf.append("checkbox{id=\"general2\";text=\"Didn't understand the question\";enabled=\"false\";selected=\"" + ta.wasGeneralNoUnderstand() + "\"}");
/* 424 */     buf.append("checkbox{id=\"general3\";text=\"Gave unclear answers\";enabled=\"false\";selected=\"" + ta.wasGeneralUnclear() + "\"}");
/* 425 */     buf.append("checkbox{id=\"general4\";text=\"Couldn't solve the problem\";enabled=\"false\";selected=\"" + ta.wasGeneralNoSolve() + "\"}");
/* 426 */     buf.append("checkbox{id=\"general5\";text=\"Was disorganized\";enabled=\"false\";selected=\"" + ta.wasGeneralDisorganized() + "\"}");
/* 427 */     buf.append("checkbox{id=\"general6\";text=\"Other\";enabled=\"false\";selected=\"" + ta.wasGeneralOther() + "\"}");
/* 428 */     buf.append("checkbox{id=\"general7\";text=\"No improvement needed\";enabled=\"false\";selected=\"" + ta.wasGeneralFine() + "\"}");
/* 429 */     buf.append("text{type=\"bold\";text=\"Which of the following qualities of the Support Team stood out?\"}");
/* 430 */     buf.append("checkbox{id=\"quality1\";text=\"Patient\";enabled=\"false\";selected=\"" + ta.wasQualityPatient() + "\"}");
/* 431 */     buf.append("checkbox{id=\"quality2\";text=\"Enthusiastic\";enabled=\"false\";selected=\"" + ta.wasQualityEnthusiastic() + "\"}");
/* 432 */     buf.append("checkbox{id=\"quality3\";text=\"Listened Carefully\";enabled=\"false\";selected=\"" + ta.wasQualityListened() + "\"}");
/* 433 */     buf.append("checkbox{id=\"quality4\";text=\"Friendly\";enabled=\"false\";selected=\"" + ta.wasQualityFriendly() + "\"}");
/* 434 */     buf.append("checkbox{id=\"quality5\";text=\"Responsive\";enabled=\"false\";selected=\"" + ta.wasQualityResponsive() + "\"}");
/* 435 */     buf.append("checkbox{id=\"quality6\";text=\"Nothing stood out.\";enabled=\"false\";selected=\"" + ta.wasQualityNothing() + "\"}");
/* 436 */     buf.append("text{type=\"bold\";text=\"What qualities of the Support Team irked you?\"}");
/* 437 */     buf.append("checkbox{id=\"irked1\";text=\"Not Patient\";enabled=\"false\";selected=\"" + ta.wasIrkedPatient() + "\"}");
/* 438 */     buf.append("checkbox{id=\"irked2\";text=\"Not Enthusiastic\";enabled=\"false\";selected=\"" + ta.wasIrkedEnthusiastic() + "\"}");
/* 439 */     buf.append("checkbox{id=\"irked3\";text=\"Didn't Listen Carefully\";enabled=\"false\";selected=\"" + ta.wasIrkedListened() + "\"}");
/* 440 */     buf.append("checkbox{id=\"irked4\";text=\"Unfriendly\";enabled=\"false\";selected=\"" + ta.wasIrkedFriendly() + "\"}");
/* 441 */     buf.append("checkbox{id=\"irked5\";text=\"Unresponsive\";enabled=\"false\";selected=\"" + ta.wasIrkedResponsive() + "\"}");
/* 442 */     buf.append("checkbox{id=\"irked6\";text=\"No qualities irked me\";enabled=\"false\";selected=\"" + ta.wasIrkedNothing() + "\"}");
/* 443 */     buf.append("text{type=\"bold\";text=\"Briefly describe any aspect of the process and/or team member which you considered outstanding or could be improved.\"}");
/* 444 */     buf.append("text{text=\"" + ta.getNote() + "\"}");
/* 445 */     buf.append("text{text=\"Thank you for your feedback. We sincerely appreciate your honest opinion and will take your input into consideration while providing services in the future.\"};");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TicketUpdateQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */