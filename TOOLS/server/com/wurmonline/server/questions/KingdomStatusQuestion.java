/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.Appointment;
/*     */ import com.wurmonline.server.kingdom.Appointments;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.webinterface.WcKingdomInfo;
/*     */ import com.wurmonline.server.webinterface.WebCommand;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.LinkedList;
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
/*     */ public final class KingdomStatusQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  51 */   private static String inOffice = "";
/*     */   
/*  53 */   private final LinkedList<Kingdom> askedAllies = new LinkedList<>();
/*     */   
/*  55 */   private static final DecimalFormat twoDecimals = new DecimalFormat("##0.00");
/*     */   
/*  57 */   private static Logger logger = Logger.getLogger(KingdomStatusQuestion.class.getName());
/*     */   
/*  59 */   private final LinkedList<Village> villages = new LinkedList<>();
/*     */ 
/*     */   
/*     */   public KingdomStatusQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  63 */     super(aResponder, aTitle, aQuestion, 65, aTarget);
/*     */   }
/*     */ 
/*     */   
/*     */   public final LinkedList<Village> getVillages() {
/*  68 */     return this.villages;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  74 */     if (getResponder().isKing()) {
/*     */       
/*  76 */       String cap = answers.getProperty("capital");
/*  77 */       if (cap != null) {
/*     */         
/*  79 */         Village[] vills = Villages.getVillages();
/*  80 */         for (int x = 0; x < vills.length; x++) {
/*     */           
/*  82 */           if (vills[x].getName().equalsIgnoreCase(cap))
/*     */           {
/*  84 */             if ((vills[x]).kingdom == getResponder().getKingdomId()) {
/*     */               
/*  86 */               King k = King.getKing(getResponder().getKingdomId());
/*  87 */               if (!cap.equalsIgnoreCase(k.capital) && 
/*  88 */                 !k.setCapital(cap, false)) {
/*  89 */                 getResponder()
/*  90 */                   .getCommunicator()
/*  91 */                   .sendNormalServerMessage("You must wait 6 hours between capital changes. Also the server must have been up that long.");
/*     */               }
/*     */             } else {
/*     */               
/*  95 */               getResponder().getCommunicator().sendNormalServerMessage("Please set your capital to a settlement in your own kingdom.");
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 100 */       boolean changed = false;
/* 101 */       Kingdom kingd = Kingdoms.getKingdom(getResponder().getKingdomId());
/* 102 */       String val = answers.getProperty("mottoone");
/* 103 */       if (val != null && val.length() > 0)
/*     */       {
/* 105 */         if (!val.equals(kingd.getFirstMotto()))
/*     */         {
/* 107 */           if (!QuestionParser.containsIllegalVillageCharacters(val)) {
/*     */             
/* 109 */             kingd.setFirstMotto(val);
/* 110 */             changed = true;
/*     */           } else {
/*     */             
/* 113 */             getResponder().getCommunicator().sendNormalServerMessage("Invalid characters in the motto.");
/*     */           }  } 
/*     */       }
/* 116 */       val = answers.getProperty("mottotwo");
/* 117 */       if (val != null && val.length() > 0)
/*     */       {
/* 119 */         if (!val.equals(kingd.getSecondMotto()))
/*     */         {
/* 121 */           if (!QuestionParser.containsIllegalVillageCharacters(val)) {
/*     */             
/* 123 */             kingd.setSecondMotto(val);
/* 124 */             changed = true;
/*     */           } else {
/*     */             
/* 127 */             getResponder().getCommunicator().sendNormalServerMessage("Invalid characters in the motto.");
/*     */           } 
/*     */         }
/*     */       }
/* 131 */       boolean allowPortal = true;
/* 132 */       val = answers.getProperty("allowPortal");
/*     */       
/* 134 */       if (val != null && val.equals("true")) {
/* 135 */         allowPortal = true;
/*     */       } else {
/* 137 */         allowPortal = false;
/* 138 */       }  if (allowPortal != kingd.acceptsTransfers()) {
/*     */         
/* 140 */         kingd.setAcceptsTransfers(allowPortal);
/* 141 */         changed = true;
/*     */       } 
/* 143 */       if (kingd.isCustomKingdom() && getResponder().isKing()) {
/*     */         
/* 145 */         String ostra = answers.getProperty("expel");
/* 146 */         if (ostra != null && ostra.length() > 0)
/*     */         {
/* 148 */           kingd.expelMember(getResponder(), ostra);
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
/* 166 */         for (Village v : this.villages) {
/*     */           
/* 168 */           val = answers.getProperty("revRej" + v.getId());
/* 169 */           if (val != null && val.equals("true")) {
/*     */             
/* 171 */             v.pmkKickDate = 0L;
/*     */             
/* 173 */             for (Village v2 : Villages.getVillages()) {
/*     */               
/* 175 */               if (v2.kingdom == getResponder().getKingdomId()) {
/* 176 */                 v2.broadCastSafe(v.getName() + " is no longer being ousted from " + kingd.getName() + ".");
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 182 */       if (mayAlly(getResponder().getKingdomId()))
/* 183 */         checkAlliances(answers, kingd); 
/* 184 */       if (changed) {
/*     */         
/* 186 */         kingd.update();
/* 187 */         WcKingdomInfo wck = new WcKingdomInfo(WurmId.getNextWCCommandId(), true, getResponder().getKingdomId());
/* 188 */         wck.encode();
/* 189 */         Servers.sendWebCommandToAllServers((short)7, (WebCommand)wck, wck.isEpicOnly());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkAlliances(Properties answers, Kingdom kingd) {
/* 197 */     Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 198 */     for (Kingdom kingdz : kingdoms) {
/*     */       
/* 200 */       if (kingdz.existsHere()) {
/*     */         
/* 202 */         String str = answers.getProperty("rev" + kingdz.getId());
/* 203 */         if (str != null && str.equals("true"))
/*     */         {
/* 205 */           if (kingdz.isAllied(kingd.getId())) {
/*     */             
/* 207 */             kingdz.setAlliance(kingd.getId(), (byte)0);
/* 208 */             kingd.setAlliance(kingdz.getId(), (byte)0);
/* 209 */             getResponder().getCommunicator().sendNormalServerMessage("You break the alliance with " + kingdz
/* 210 */                 .getName() + ".");
/* 211 */             King other = King.getKing(kingdz.getId());
/*     */             
/*     */             try {
/* 214 */               Player otherKing = Players.getInstance().getPlayer(other.kingid);
/* 215 */               otherKing.getCommunicator().sendNormalServerMessage("Your alliance with " + kingd
/* 216 */                   .getName() + " has ended.");
/*     */             }
/* 218 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */ 
/*     */             
/* 222 */             if (Server.rand.nextBoolean()) {
/* 223 */               HistoryManager.addHistory(kingd.getName(), "is no longer allied with " + kingdz.getName());
/*     */             } else {
/* 225 */               HistoryManager.addHistory(kingdz.getName(), "is no longer allied with " + kingd.getName());
/*     */             } 
/*     */           }  } 
/* 228 */         str = answers.getProperty("acc" + kingdz.getId());
/* 229 */         if (str != null && str.equals("true"))
/*     */         {
/* 231 */           if (kingdz.hasSentRequestingAlliance(kingd.getId())) {
/*     */             
/* 233 */             kingdz.setAlliance(kingd.getId(), (byte)1);
/* 234 */             kingd.setAlliance(kingdz.getId(), (byte)1);
/* 235 */             getResponder().getCommunicator().sendNormalServerMessage("You accept the request for alliance with " + kingdz
/* 236 */                 .getName() + ".");
/* 237 */             King other = King.getKing(kingdz.getId());
/*     */             
/*     */             try {
/* 240 */               Player otherKing = Players.getInstance().getPlayer(other.kingid);
/* 241 */               otherKing.getCommunicator().sendNormalServerMessage("You are now allied to " + kingd
/* 242 */                   .getName() + "!");
/*     */             }
/* 244 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */ 
/*     */             
/* 248 */             if (Server.rand.nextBoolean()) {
/* 249 */               HistoryManager.addHistory(kingd.getName(), "forms alliance with " + kingdz.getName());
/*     */             } else {
/* 251 */               HistoryManager.addHistory(kingdz.getName(), "forms alliance with " + kingd.getName());
/*     */             } 
/*     */           }  } 
/* 254 */         str = answers.getProperty("rem" + kingdz.getId());
/* 255 */         if (str != null && str.equals("true"))
/*     */         {
/* 257 */           if (kingd.hasSentRequestingAlliance(kingdz.getId())) {
/*     */             
/* 259 */             kingdz.setAlliance(kingd.getId(), (byte)0);
/* 260 */             kingd.setAlliance(kingdz.getId(), (byte)1);
/* 261 */             getResponder().getCommunicator().sendNormalServerMessage("You revoke your request for alliance with " + kingdz
/* 262 */                 .getName() + ".");
/* 263 */             King other = King.getKing(kingdz.getId());
/*     */             
/*     */             try {
/* 266 */               Player otherKing = Players.getInstance().getPlayer(other.kingid);
/* 267 */               otherKing.getCommunicator().sendNormalServerMessage(kingd
/* 268 */                   .getName() + " has withdrawn their request for an alliance.");
/*     */             }
/* 270 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 278 */     String val = answers.getProperty("askAlliance");
/* 279 */     if (val != null && val.length() > 0) {
/*     */       
/*     */       try {
/*     */         
/* 283 */         int index = Integer.parseInt(val);
/* 284 */         if (index > 0) {
/*     */           
/* 286 */           Kingdom kingdz = this.askedAllies.get(index - 1);
/* 287 */           if (!kingd.hasSentRequestingAlliance(kingdz.getId()) && 
/* 288 */             !kingdz.hasSentRequestingAlliance(kingd.getId()))
/*     */           {
/* 290 */             getResponder().getCommunicator().sendNormalServerMessage("You invite " + kingdz
/* 291 */                 .getName() + " to join you in an alliance.");
/* 292 */             King other = King.getKing(kingdz.getId());
/* 293 */             if (other != null) {
/*     */               
/*     */               try {
/* 296 */                 Player otherKing = Players.getInstance().getPlayer(other.kingid);
/* 297 */                 otherKing.getCommunicator().sendNormalServerMessage(kingd
/* 298 */                     .getName() + " invites you to enter a mutual alliance.");
/*     */               }
/* 300 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */             
/*     */             }
/*     */             else {
/*     */               
/* 305 */               getResponder().getCommunicator().sendNormalServerMessage("Seems " + kingdz
/* 306 */                   .getName() + " has no ruler to ally with.");
/* 307 */             }  kingd.setAlliance(kingdz.getId(), (byte)2);
/*     */           }
/*     */         
/*     */         } 
/* 311 */       } catch (NumberFormatException nnf) {
/*     */         
/* 313 */         logger.log(Level.WARNING, val + ": " + nnf.getMessage(), nnf);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean mayAlly(byte kingdom) {
/* 320 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(byte kingdom, StringBuilder buf) {
/* 331 */     King k = King.getKing(kingdom);
/*     */     
/* 333 */     Kingdom kingd = Kingdoms.getKingdom(kingdom);
/* 334 */     String motone = "The " + ((k == null) ? "ruler" : k.getRulerTitle());
/* 335 */     String mottwo = "rules";
/* 336 */     if (kingd != null) {
/*     */       
/* 338 */       motone = kingd.getFirstMotto();
/* 339 */       mottwo = kingd.getSecondMotto();
/*     */     } 
/* 341 */     buf.append("text{text=\"" + Kingdoms.getNameFor(kingdom) + " '" + motone + " " + mottwo + "' (" + twoDecimals
/* 342 */         .format(Zones.getPercentLandForKingdom(kingdom)) + "% land)\"}");
/* 343 */     if (k != null) {
/*     */       
/* 345 */       buf.append("text{text=\" under the rule of " + k.getRulerTitle() + " " + k.kingName + ":\"}");
/* 346 */       float sperc = k.getLandSuccessPercent();
/* 347 */       if (kingd != null && k.kingid == getResponder().getWurmId()) {
/*     */         
/* 349 */         buf.append("harray{input{id='capital'; maxchars='40'; text=\"" + k.capital + "\"}label{text=\"capital\"}}");
/* 350 */         if (kingd.isCustomKingdom()) {
/*     */           
/* 352 */           buf.append("text{text=\"Password used to become ruler on other servers: " + kingd.getPassword() + "\"}");
/* 353 */           buf.append("text{text=\"\"}");
/* 354 */           buf.append("checkbox{id='allowPortal';text='Allow people to join the kingdom via portals?';selected=\"" + kingd
/* 355 */               .acceptsTransfers() + "\"}");
/*     */           
/* 357 */           for (Village v : Villages.getVillages()) {
/*     */             
/* 359 */             if (v.kingdom == k.kingdom && 
/* 360 */               !v.isCapital())
/* 361 */               this.villages.add(v); 
/*     */           } 
/* 363 */           if (Players.getInstance().getPlayersFromKingdom(kingdom) > 10 || Servers.localServer.testServer) {
/*     */             
/* 365 */             buf.append("harray{input{id='expel'; maxchars='30'; text=\"\"}label{text=\" Expel this person from the kingdom. Must be online.\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 381 */             buf.append("text{text=\"Your kingdom is too small to expel people (more than 10 members required).\"}");
/* 382 */           }  if (this.villages.size() > 0)
/*     */           {
/* 384 */             for (Village v : this.villages) {
/*     */               
/* 386 */               if (v.pmkKickDate > 0L) {
/*     */                 
/* 388 */                 buf.append("text{text=\"\"}");
/* 389 */                 buf.append("checkbox{id=\"revRej" + v.getId() + "\";text=\"" + v.getName() + " is being kicked out in " + 
/*     */                     
/* 391 */                     Server.getTimeFor(v.pmkKickDate - System.currentTimeMillis()) + ". Remove kicking? \";selected=\"false\"};");
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 397 */         buf.append("harray{input{id='mottoone'; maxchars='10'; text=\"" + kingd.getFirstMotto() + "\"}label{text=\" Description one\"}}");
/*     */         
/* 399 */         buf.append("harray{input{id='mottotwo'; maxchars='10'; text=\"" + kingd.getSecondMotto() + "\"}label{text=\" Description two\"}}");
/*     */         
/* 401 */         if (sperc == 100.0F) {
/* 402 */           buf.append("text{text=\"Your kingdom share lies steady at " + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + " of the available land.\"}");
/*     */         }
/* 404 */         else if (sperc > 100.0F) {
/* 405 */           buf.append("text{text=\"You have increased your kingdom share from " + 
/* 406 */               String.format("%.2f%%", new Object[] { Float.valueOf(k.startLand) }) + " to " + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + " of the available land, which is an increase by " + 
/* 407 */               String.format("%.2f%%", new Object[] { Float.valueOf(sperc - 100.0F) }) + ".\"}");
/*     */         } else {
/* 409 */           buf.append("text{text=\"Your share of the available lands has decreased from " + 
/* 410 */               String.format("%.2f%%", new Object[] { Float.valueOf(k.startLand) }) + " to " + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + " of the available land, so you have lost " + 
/* 411 */               String.format("%.2f%%", new Object[] { Float.valueOf(100.0F - sperc) }) + ".\"}");
/* 412 */         }  if (Servers.localServer.isChallengeOrEpicServer())
/*     */         {
/* 414 */           if (getResponder().hasCustomKingdom())
/*     */           {
/* 416 */             if (getResponder().isEligibleForKingdomBonus()) {
/* 417 */               buf.append("text{text=\"You are eligible for royal combat rating bonuses since you have more than 2.0% land.\"}");
/*     */             } else {
/*     */               
/* 420 */               buf.append("text{text=\"You are not eligible for royal combat rating bonuses since you have less than 2.0% land.\"}");
/*     */             } 
/*     */           }
/*     */         }
/* 424 */         buf.append("text{text=\"You have appointed " + k.appointed + " levels of titles, orders and offices.\"}");
/* 425 */         buf.append("text{text=\"Your subjects have slain " + k.levelskilled + " appointment levels of enemy nobles, and your enemies have slain " + k.levelslost + " levels of your nobles, which equals " + 
/*     */             
/* 427 */             String.format("%d%%", new Object[] { Integer.valueOf((int)k.getAppointedSuccessPercent()) }) + " success.\"}");
/* 428 */         buf.append("text{text=\"This ranks you as " + k.getFullTitle() + ".\"}");
/* 429 */         buf.append("text{text=\"\"}");
/*     */         
/* 431 */         if (k.getChallengeDate() > 0L) {
/*     */           
/* 433 */           long nca = k.getChallengeDate();
/* 434 */           String sa = Server.getTimeFor(System.currentTimeMillis() - nca);
/* 435 */           buf.append("text{text=\"You were challenged " + sa + " ago.\"}");
/*     */         } 
/* 437 */         if (k.getChallengeAcceptedDate() > 0L) {
/*     */           
/* 439 */           long nca = k.getChallengeAcceptedDate();
/* 440 */           String sa = Server.getTimeFor(nca - System.currentTimeMillis());
/* 441 */           buf.append("text{text=\"You must show up in the duelling ring in " + sa + ".\"}");
/*     */         } 
/* 443 */         long nc = k.getNextChallenge();
/* 444 */         if (nc > System.currentTimeMillis()) {
/*     */           
/* 446 */           String s = Server.getTimeFor(nc - System.currentTimeMillis());
/* 447 */           buf.append("text{text=\"Next challenge avail in " + s + ".\"}");
/*     */         } 
/* 449 */         if (k.hasFailedAllChallenges())
/*     */         {
/* 451 */           buf.append("text{text=\"You have failed all challenges. Voting is in progress.\"}");
/*     */         }
/* 453 */         if (getResponder().getPower() >= 3)
/*     */         {
/* 455 */           buf.append("text{text=\" Challenges: " + k.getChallengeSize() + " Declined: " + k
/* 456 */               .getDeclinedChallengesNumber() + " Votes: " + k.getVotes() + ".\"}");
/*     */         }
/* 458 */         buf.append("text{text=\"\"}");
/* 459 */         if (mayAlly(kingdom))
/*     */         {
/* 461 */           boolean allAllies = true;
/* 462 */           Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 463 */           for (Kingdom kingdz : kingdoms) {
/*     */             
/* 465 */             if (kingdz.existsHere() && mayAlly(kingdz.getId()))
/*     */             {
/* 467 */               if (kingdz.isAllied(kingdom)) {
/*     */                 
/* 469 */                 buf.append("checkbox{id=\"rev" + kingdz.getId() + "\";text=\"" + kingdz.getName() + " is an ally. Revoke? \";selected=\"false\"};");
/*     */               
/*     */               }
/* 472 */               else if (kingdz.hasSentRequestingAlliance(kingdom)) {
/*     */                 
/* 474 */                 buf.append("checkbox{id=\"acc" + kingdz.getId() + "\";text=\"" + kingdz.getName() + " is asking to become an ally. Accept? \";selected=\"false\"};");
/*     */               
/*     */               }
/* 477 */               else if (kingd.hasSentRequestingAlliance(kingdz.getId())) {
/*     */                 
/* 479 */                 buf.append("checkbox{id=\"rem" + kingdz.getId() + "\";text=\" You are asking " + kingdz
/* 480 */                     .getName() + " to become an ally. Revoke? \";selected=\"false\"};");
/*     */               } else {
/*     */                 
/* 483 */                 allAllies = false;
/*     */               }  } 
/*     */           } 
/* 486 */           buf.append("text{text=\"\"}");
/* 487 */           buf.append("text{text=\"Potential allies. Select one to invite to an alliance:\"}");
/* 488 */           buf.append("dropdown{id='askAlliance';options=\"");
/* 489 */           buf.append("No thanks");
/* 490 */           if (!allAllies)
/*     */           {
/* 492 */             for (Kingdom kingdz : kingdoms) {
/*     */               
/* 494 */               if (kingdz.existsHere() && kingdz.getId() != 0)
/*     */               {
/* 496 */                 if (!kingdz.isAllied(kingdom) && kingdz.getId() != kingdom && mayAlly(kingdz.getId())) {
/*     */                   
/* 498 */                   buf.append("," + kingdz.getName());
/* 499 */                   this.askedAllies.add(kingdz);
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           }
/* 504 */           buf.append("\"}");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 509 */         if (kingdom == getResponder().getKingdomId()) {
/*     */           
/* 511 */           if (k.getChallengeDate() > 0L) {
/*     */             
/* 513 */             long nca = k.getChallengeDate();
/* 514 */             String sa = Server.getTimeFor(System.currentTimeMillis() - nca);
/* 515 */             buf.append("text{text=\"The ruler was challenged " + sa + " ago.\"}");
/*     */           } 
/* 517 */           if (k.getChallengeAcceptedDate() > 0L) {
/*     */             
/* 519 */             long nca = k.getChallengeAcceptedDate();
/* 520 */             String sa = Server.getTimeFor(nca - System.currentTimeMillis());
/* 521 */             buf.append("text{text=\"The ruler must show up in " + sa + ".\"}");
/*     */           } 
/* 523 */           long nc = k.getNextChallenge();
/* 524 */           if (nc > System.currentTimeMillis()) {
/*     */             
/* 526 */             String s = Server.getTimeFor(nc - System.currentTimeMillis());
/* 527 */             buf.append("text{text=\"Next challenge avail in " + s + ".\"}");
/*     */           } 
/* 529 */           if (k.hasFailedAllChallenges()) {
/*     */             
/* 531 */             buf.append("text{text=\"The " + k.getRulerTitle() + " has failed all challenges. Voting is in progress.\"}");
/*     */             
/* 533 */             if ((((Player)getResponder()).getSaveFile()).votedKing) {
/* 534 */               buf.append("text{text=\"You have already voted.\"}");
/*     */             } else {
/* 536 */               buf.append("text{text=\"You may head to the duelling ring and vote for removal of the current ruler.\"}");
/*     */             } 
/* 538 */           }  if (getResponder().getPower() >= 3)
/*     */           {
/* 540 */             buf.append("text{text=\" Challenges: " + k.getChallengeSize() + " Declined: " + k
/* 541 */                 .getDeclinedChallengesNumber() + " Votes: " + k.getVotes() + ".\"}");
/*     */           }
/* 543 */           buf.append("text{text=\"\"}");
/* 544 */           if (Servers.localServer.isChallengeOrEpicServer())
/*     */           {
/* 546 */             if (getResponder().hasCustomKingdom())
/*     */             {
/* 548 */               if (getResponder().isEligibleForKingdomBonus()) {
/* 549 */                 buf.append("text{text=\"The " + kingd.getName() + " people are eligible for royal combat rating bonuses since you have more than " + 2.0F + "% land.\"}");
/*     */               }
/*     */               else {
/*     */                 
/* 553 */                 buf.append("text{text=\"The " + kingd.getName() + " people are not eligible for royal combat rating bonuses since you have less than " + 2.0F + "% land.\"}");
/*     */               } 
/*     */             }
/*     */           }
/*     */         } 
/*     */         
/* 559 */         if (sperc == 100.0F) {
/* 560 */           buf.append("text{text=\"Kingdom share lies steady at " + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + " of the available land.\"}");
/*     */         }
/* 562 */         else if (sperc > 100.0F) {
/* 563 */           buf.append("text{text=\"" + k.kingName + " has increased the kingdom share from " + 
/* 564 */               String.format("%.2f%%", new Object[] { Float.valueOf(k.startLand) }) + " to " + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + " of the available land, which is an increase by " + 
/* 565 */               String.format("%.2f%%", new Object[] { Float.valueOf(sperc - 100.0F) }) + ".\"}");
/*     */         } else {
/* 567 */           buf.append("text{text=\"During the reign of " + k.kingName + ", the share of the available lands has decreased from " + 
/* 568 */               String.format("%.2f%%", new Object[] { Float.valueOf(k.startLand) }) + " to " + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + " of the available land, so the kingdom has lost " + 
/* 569 */               String.format("%.2f%%", new Object[] { Float.valueOf(100.0F - sperc) }) + ".\"}");
/*     */         } 
/* 571 */         buf.append("text{text=\"" + k.kingName + " has appointed " + k.appointed + " levels of titles, orders and offices.\"}");
/*     */         
/* 573 */         buf.append("text{text=\"The subjects of " + Kingdoms.getNameFor(kingdom) + " have slain " + k.levelskilled + " appointment levels of enemy nobles. Their enemies have slain " + k.levelslost + " levels of " + 
/*     */             
/* 575 */             Kingdoms.getNameFor(kingdom) + " nobles, which equals " + String.format("%d%%", new Object[] { Integer.valueOf((int)k.getAppointedSuccessPercent()) }) + " success.\"}");
/* 576 */         buf.append("text{text=\"This ranks " + k.kingName + " as " + k.getFullTitle() + ".\"}");
/*     */       } 
/* 578 */       buf.append("text{text=\"\"}");
/*     */       
/* 580 */       addApps(buf, kingdom, false);
/*     */     }
/* 582 */     else if (kingdom != 0) {
/* 583 */       buf.append("text{text=\"Ruler unknown.\"}");
/* 584 */     }  float crbon = Players.getInstance().getCRBonus(kingdom);
/* 585 */     if (crbon > 0.0F)
/*     */     {
/* 587 */       if (crbon < 2.0F) {
/* 588 */         buf.append("text{text=\"Because of the low active population, the subjects are known to fight a bit more fierce than their enemies.\"}");
/*     */       } else {
/* 590 */         buf.append("text{text=\"Because of the low active population, the subjects are known to fight quite a bit more fierce than their enemies.\"}");
/*     */       }  } 
/* 592 */     buf.append("text{text=\"\"}");
/* 593 */     buf.append("text{text=\"\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private void addApps(StringBuilder buf, byte kingdom, boolean isResponder) {
/* 598 */     Appointments apps = King.getCurrentAppointments(kingdom);
/* 599 */     if (apps != null) {
/*     */       
/* 601 */       if (isResponder) {
/*     */         
/* 603 */         String titles = apps.getOffices(getResponder().getWurmId(), (getResponder().getSex() == 0));
/* 604 */         if (titles.length() > 0) {
/*     */           
/* 606 */           buf.append("text{text=\"");
/* 607 */           buf.append("You are the ");
/* 608 */           buf.append(titles);
/* 609 */           buf.append(" of ");
/* 610 */           buf.append(Kingdoms.getNameFor(getResponder().getKingdomId()));
/* 611 */           buf.append(".");
/* 612 */           buf.append("\"}");
/* 613 */           buf.append("text{text=\"\"}");
/*     */         } 
/* 615 */         titles = apps.getTitles(getResponder().getAppointments(), (getResponder().getSex() == 0));
/* 616 */         if (titles.length() > 0) {
/*     */           
/* 618 */           buf.append("text{text=\"");
/* 619 */           buf.append(" You are ");
/* 620 */           buf.append(titles);
/* 621 */           buf.append(" of ");
/* 622 */           buf.append(Kingdoms.getNameFor(getResponder().getKingdomId()));
/* 623 */           buf.append(".");
/* 624 */           buf.append("\"}");
/* 625 */           buf.append("text{text=\"\"}");
/*     */         } 
/* 627 */         titles = apps.getOrders(getResponder().getAppointments(), (getResponder().getSex() == 0));
/* 628 */         if (titles.length() > 0)
/*     */         {
/* 630 */           buf.append("text{text=\"");
/* 631 */           buf.append("You have received the ");
/* 632 */           buf.append(titles);
/* 633 */           buf.append(".");
/* 634 */           buf.append("\"}");
/* 635 */           buf.append("text{text=\"\"}");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 640 */         boolean isSecretPolice = false;
/* 641 */         Appointments rap = King.getCurrentAppointments(getResponder().getKingdomId());
/* 642 */         if (rap != null) {
/*     */           
/* 644 */           long secretP = rap.getOfficialForId(1500);
/* 645 */           if (secretP == getResponder().getWurmId()) {
/* 646 */             isSecretPolice = true;
/*     */           }
/* 648 */           long[] offices = apps.officials;
/* 649 */           for (int x = 0; x < offices.length; x++) {
/*     */             
/* 651 */             if (offices[x] != 0L) {
/*     */               
/* 653 */               inOffice = "";
/* 654 */               Appointment app = apps.getAppointment(1500 + x);
/* 655 */               if (app != null) {
/*     */                 
/* 657 */                 String name = "Unknown";
/* 658 */                 PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(offices[x]);
/* 659 */                 if (pinf != null)
/* 660 */                   name = pinf.getName(); 
/* 661 */                 if (isSecretPolice) {
/*     */                   
/*     */                   try {
/*     */ 
/*     */                     
/* 666 */                     Player p = Players.getInstance().getPlayer(offices[x]);
/* 667 */                     inOffice = "(in office)";
/*     */                   }
/* 669 */                   catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */                 }
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 675 */                 buf.append("label{text=\"" + app.getNameForGender((byte)0) + "\"};label{text=\"" + name + " " + inOffice + "\"};");
/*     */                 
/* 677 */                 buf.append("text{text=\"\"}");
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 683 */       buf.append("text{text=\"\"}");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 690 */     StringBuilder buf = new StringBuilder();
/* 691 */     buf.append(getBmlHeader());
/* 692 */     buf.append("center{image{src='img.gui.kingdoms';size='512,128'}}");
/* 693 */     buf.append("text{text=\"\"}");
/* 694 */     addApps(buf, getResponder().getKingdomId(), true);
/*     */     
/* 696 */     Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 697 */     for (Kingdom k : kingdoms) {
/*     */       
/* 699 */       if (k.existsHere()) {
/* 700 */         append(k.getId(), buf);
/*     */       }
/*     */     } 
/* 703 */     buf.append(createAnswerButton2());
/* 704 */     getResponder().getCommunicator().sendBml(700, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\KingdomStatusQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */