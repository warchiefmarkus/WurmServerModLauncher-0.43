/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.Methods;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.PvPAlliance;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
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
/*     */ public class KingdomFoundationQuestion
/*     */   extends Question
/*     */ {
/*     */   public static final int playersNeeded = 1;
/*  51 */   private int playersFound = 0;
/*  52 */   private final Set<Creature> creaturesToConvert = new HashSet<>();
/*     */   
/*  54 */   private static Logger logger = Logger.getLogger(KingdomFoundationQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KingdomFoundationQuestion(Creature aResponder, long aTarget) {
/*  65 */     super(aResponder, "Declaring independence", "Do you wish to found a new kingdom?", 89, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkMovingPlayers(Creature resp, int x, int y, boolean surfaced) {
/*  71 */     VolaTile t = Zones.getTileOrNull(x, y, surfaced);
/*  72 */     if (t != null) {
/*     */       
/*  74 */       Creature[] crets = t.getCreatures();
/*  75 */       if (crets.length > 0)
/*  76 */         for (int c = 0; c < crets.length; c++) {
/*     */           
/*  78 */           if (crets[c].isPlayer()) {
/*     */             
/*  80 */             if (crets[c].getPower() == 0)
/*     */             {
/*  82 */               if (crets[c].isFighting()) {
/*     */                 
/*  84 */                 resp.getCommunicator().sendNormalServerMessage(crets[c]
/*  85 */                     .getName() + " was moving or fighting. Everyone has to stand still and honor the moment.");
/*     */                 
/*  87 */                 return false;
/*     */               } 
/*  89 */               if (crets[c].getCitizenVillage() != null && 
/*  90 */                 (crets[c].getCitizenVillage().getMayor()).wurmId == crets[c].getWurmId())
/*     */               {
/*  92 */                 if (crets[c].getWurmId() != resp.getWurmId()) {
/*     */                   
/*  94 */                   resp.getCommunicator()
/*  95 */                     .sendNormalServerMessage(crets[c]
/*  96 */                       .getName() + " is the mayor of another settlement. You have to ask " + crets[c]
/*     */                       
/*  98 */                       .getHimHerItString() + " to leave the area and be converted later.");
/*     */                   
/* 100 */                   return false;
/*     */                 } 
/*     */               }
/* 103 */               if (crets[c].isChampion()) {
/*     */                 
/* 105 */                 resp.getCommunicator()
/* 106 */                   .sendNormalServerMessage(crets[c]
/* 107 */                     .getName() + " is champion of a deity. You have to ask " + crets[c]
/*     */                     
/* 109 */                     .getHimHerItString() + " to leave the area and be converted later.");
/*     */                 
/* 111 */                 return false;
/*     */               } 
/* 113 */               if (crets[c].isPaying())
/* 114 */                 this.playersFound++; 
/* 115 */               this.creaturesToConvert.add(crets[c]);
/*     */             }
/*     */           
/* 118 */           } else if (crets[c].isSpiritGuard() || crets[c].isKingdomGuard() || crets[c].getLoyalty() > 0.0F) {
/* 119 */             this.creaturesToConvert.add(crets[c]);
/*     */           } 
/*     */         }  
/* 122 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/* 133 */     setAnswer(aAnswers);
/* 134 */     Creature resp = getResponder();
/* 135 */     if (resp.isChampion()) {
/* 136 */       resp.getCommunicator().sendAlertServerMessage("Champions are not able to rule kingdoms.");
/*     */       return;
/*     */     } 
/* 139 */     if (!Kingdoms.mayCreateKingdom()) {
/*     */       
/* 141 */       resp.getCommunicator().sendAlertServerMessage("There are too many kingdoms already.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 146 */       Item declaration = Items.getItem(this.target);
/* 147 */       if (declaration.deleted) {
/*     */         
/* 149 */         resp.getCommunicator().sendAlertServerMessage("The declaration is gone!");
/*     */         return;
/*     */       } 
/* 152 */       if (declaration.isTraded() || declaration.getOwnerId() != resp.getWurmId()) {
/*     */         
/* 154 */         resp.getCommunicator().sendAlertServerMessage("The declaration is not under your control any longer!");
/*     */         
/*     */         return;
/*     */       } 
/* 158 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 160 */       resp.getCommunicator().sendAlertServerMessage("The declaration is gone!");
/*     */       return;
/*     */     } 
/* 163 */     if (resp.getCitizenVillage() == null) {
/*     */       
/* 165 */       resp.getCommunicator().sendNormalServerMessage("You need to be mayor of a settlement.");
/*     */       return;
/*     */     } 
/* 168 */     if (resp.getCitizenVillage() != resp.getCurrentVillage()) {
/*     */       
/* 170 */       resp.getCommunicator().sendNormalServerMessage("You need to be standing in your settlement.");
/*     */       return;
/*     */     } 
/* 173 */     int sx = Zones.safeTileX(resp.getCitizenVillage().getStartX() - resp.getCitizenVillage().getPerimeterSize() - 5);
/*     */     
/* 175 */     int ex = Zones.safeTileX(resp.getCitizenVillage().getEndX() + resp.getCitizenVillage().getPerimeterSize() + 5);
/*     */     
/* 177 */     int sy = Zones.safeTileY(resp.getCitizenVillage().getStartY() - resp.getCitizenVillage().getPerimeterSize() - 5);
/*     */     
/* 179 */     int ey = Zones.safeTileY(resp.getCitizenVillage().getEndY() + resp.getCitizenVillage().getPerimeterSize() + 5);
/*     */     
/* 181 */     for (int x = sx; x <= ex; x++) {
/* 182 */       for (int y = sy; y <= ey; y++) {
/*     */         
/* 184 */         if (!checkMovingPlayers(resp, x, y, true))
/*     */           return; 
/* 186 */         if (!checkMovingPlayers(resp, x, y, false))
/*     */           return; 
/*     */       } 
/*     */     } 
/* 190 */     if (resp.getCitizenVillage() != null) {
/*     */       
/* 192 */       Player[] players = Players.getInstance().getPlayers();
/* 193 */       for (Player p : players) {
/*     */         
/* 195 */         if (p.getCitizenVillage() == resp.getCitizenVillage()) {
/*     */           
/* 197 */           if (!this.creaturesToConvert.contains(p))
/*     */           {
/* 199 */             if (p.isPaying())
/*     */             {
/* 201 */               this.playersFound++;
/*     */             }
/*     */           }
/* 204 */           this.creaturesToConvert.add(p);
/*     */         } 
/*     */       } 
/*     */     } 
/* 208 */     if (this.playersFound < 1 && resp.getPower() < 3) {
/*     */       
/* 210 */       getResponder().getCommunicator().sendNormalServerMessage("Only " + this.playersFound + " premium players were found in the village, on deed and in perimeter. You need " + '\001' + ".");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 216 */     boolean created = false;
/* 217 */     String kingdomName = "";
/* 218 */     String password = "";
/* 219 */     byte templateId = 0;
/* 220 */     String key = "kingdomName";
/* 221 */     String val = aAnswers.getProperty(key);
/* 222 */     if (val != null && val.length() > 0) {
/*     */       
/* 224 */       val = val.trim();
/* 225 */       if (val.length() < 2) {
/*     */         
/* 227 */         getResponder().getCommunicator().sendNormalServerMessage("The name is too short.");
/*     */         return;
/*     */       } 
/* 230 */       if (val.length() > 20) {
/*     */         
/* 232 */         getResponder().getCommunicator().sendNormalServerMessage("The name is too long.");
/*     */         return;
/*     */       } 
/* 235 */       if (QuestionParser.containsIllegalVillageCharacters(val)) {
/*     */         
/* 237 */         getResponder().getCommunicator().sendNormalServerMessage("The name contains illegal characters.");
/*     */         
/*     */         return;
/*     */       } 
/* 241 */       kingdomName = val;
/*     */       
/* 243 */       key = "passw";
/* 244 */       val = aAnswers.getProperty(key);
/* 245 */       if (val != null && val.length() > 0) {
/*     */         
/* 247 */         if (val.length() < 5) {
/*     */           
/* 249 */           getResponder().getCommunicator().sendNormalServerMessage("The password is too short.");
/*     */           return;
/*     */         } 
/* 252 */         password = val;
/*     */       } 
/*     */       
/* 255 */       key = "templateid";
/* 256 */       val = aAnswers.getProperty(key);
/* 257 */       if (val != null && val.length() > 0)
/*     */       {
/* 259 */         if (val.equals("0")) {
/* 260 */           templateId = 1;
/* 261 */         } else if (val.equals("1")) {
/* 262 */           templateId = 2;
/* 263 */         } else if (val.equals("2")) {
/* 264 */           templateId = 3;
/*     */         } else {
/*     */           
/* 267 */           getResponder().getCommunicator().sendNormalServerMessage("Illegal template: " + val);
/*     */           return;
/*     */         } 
/*     */       }
/* 271 */       Kingdom k = Kingdoms.getKingdomWithName(kingdomName);
/* 272 */       if (k != null) {
/*     */         
/* 274 */         if (k.existsHere()) {
/*     */           
/* 276 */           King existingRuler = King.getKing(k.getId());
/* 277 */           if (existingRuler != null) {
/*     */             
/* 279 */             getResponder().getCommunicator().sendNormalServerMessage("A kingdom with that name already exists in these lands ruled by " + existingRuler.kingName + ".");
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 284 */           boolean crownExists = false;
/* 285 */           Item[] _items = Items.getAllItems();
/* 286 */           for (Item lItem : _items) {
/*     */             
/* 288 */             if (lItem.isRoyal())
/*     */             {
/* 290 */               if (lItem.getKingdom() == k.getId())
/*     */               {
/* 292 */                 if (lItem.getTemplateId() == 536 || lItem
/* 293 */                   .getTemplateId() == 530 || lItem
/* 294 */                   .getTemplateId() == 533)
/* 295 */                   crownExists = true; 
/*     */               }
/*     */             }
/*     */           } 
/* 299 */           if (crownExists) {
/*     */             
/* 301 */             getResponder().getCommunicator().sendNormalServerMessage("A kingdom with that name already exists in these lands. You need to find the crown.");
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 306 */         if (!k.getPassword().equals(password)) {
/*     */           
/* 308 */           getResponder().getCommunicator().sendNormalServerMessage("The password you provided was wrong.");
/*     */           return;
/*     */         } 
/* 311 */         if (templateId != k.getTemplate()) {
/*     */           
/* 313 */           getResponder().getCommunicator().sendNormalServerMessage("You can not use that template for this kingdom since it already exists. Change template");
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 318 */       String mottoOne = "Friendly";
/* 319 */       String mottoTwo = "Peasants";
/*     */       
/* 321 */       val = aAnswers.getProperty("mottoone");
/* 322 */       if (val != null && val.length() > 0) {
/*     */         
/* 324 */         val = val.trim();
/* 325 */         if (LoginHandler.containsIllegalCharacters(val)) {
/*     */           
/* 327 */           getResponder().getCommunicator().sendNormalServerMessage("The first motto contains illegal characters.");
/*     */           return;
/*     */         } 
/* 330 */         mottoOne = val;
/*     */       } 
/* 332 */       val = aAnswers.getProperty("mottotwo");
/* 333 */       if (val != null && val.length() > 0) {
/*     */         
/* 335 */         val = val.trim();
/* 336 */         if (LoginHandler.containsIllegalCharacters(val)) {
/*     */           
/* 338 */           getResponder().getCommunicator().sendNormalServerMessage("The second motto contains illegal characters.");
/*     */           return;
/*     */         } 
/* 341 */         mottoTwo = val;
/*     */       } 
/* 343 */       boolean allowPortal = true;
/* 344 */       val = aAnswers.getProperty("allowPortal");
/*     */       
/* 346 */       if (val != null && val.equals("true")) {
/* 347 */         allowPortal = true;
/*     */       } else {
/* 349 */         allowPortal = false;
/* 350 */       }  String chatName = kingdomName.substring(0, Math.min(11, kingdomName.length()));
/*     */       
/* 352 */       if (Kingdoms.getKingdomWithChatTitle(chatName) != null)
/*     */       {
/* 354 */         if (chatName.contains(" ")) {
/*     */           
/* 356 */           chatName = kingdomName.replace(" ", "").substring(0, Math.min(11, kingdomName.length()));
/* 357 */           Kingdom kc = Kingdoms.getKingdomWithChatTitle(chatName);
/* 358 */           if (kc != null) {
/*     */             
/* 360 */             getResponder().getCommunicator().sendNormalServerMessage("That name is too similar to the kingdom " + kc
/* 361 */                 .getName() + ".");
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       }
/* 366 */       String suffix = kingdomName.replace(" ", "").substring(0, Math.min(4, kingdomName.length())) + ".";
/* 367 */       suffix = suffix.toLowerCase();
/* 368 */       if (Kingdoms.getKingdomWithSuffix(suffix) != null) {
/*     */         
/* 370 */         suffix = (Server.rand.nextBoolean() ? "z" : "y") + kingdomName.substring(0, Math.min(3, kingdomName.length())) + ".";
/*     */         
/* 372 */         suffix = suffix.toLowerCase();
/* 373 */         Kingdom kc = Kingdoms.getKingdomWithSuffix(suffix);
/* 374 */         if (kc != null) {
/*     */           
/* 376 */           getResponder().getCommunicator().sendNormalServerMessage("That name is too similar to the kingdom " + kc
/* 377 */               .getName() + ".");
/*     */           return;
/*     */         } 
/*     */       } 
/* 381 */       int allnum = resp.getCitizenVillage().getAllianceNumber();
/* 382 */       String aname = resp.getCitizenVillage().getAllianceName();
/* 383 */       if (allnum > 0)
/*     */       {
/* 385 */         if (allnum == resp.getCitizenVillage().getAllianceNumber()) {
/*     */           
/* 387 */           PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(allnum);
/* 388 */           for (Village v : pvpAll.getVillages()) {
/*     */             
/* 390 */             v.setAllianceNumber(0);
/* 391 */             v.broadCastAlert(aname + " alliance has been disbanded.");
/*     */           } 
/*     */ 
/*     */           
/* 395 */           if (pvpAll.exists())
/*     */           {
/* 397 */             pvpAll.delete();
/* 398 */             pvpAll.sendClearAllianceAnnotations();
/* 399 */             pvpAll.deleteAllianceMapAnnotations();
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 404 */           PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(allnum);
/* 405 */           resp.getCitizenVillage().broadCastAlert(resp
/* 406 */               .getCitizenVillage().getName() + " leaves the " + aname + " alliance.");
/* 407 */           resp.getCitizenVillage().setAllianceNumber(0);
/* 408 */           if (!pvpAll.exists())
/*     */           {
/* 410 */             pvpAll.delete();
/*     */           }
/*     */         } 
/*     */       }
/* 414 */       Kingdom newkingdom = new Kingdom(Kingdoms.getNextAvailableKingdomId(), templateId, kingdomName, password, chatName, suffix, mottoOne, mottoTwo, allowPortal);
/*     */       
/* 416 */       Kingdoms.addKingdom(newkingdom);
/*     */       
/* 418 */       LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 419 */       lsw.setKingdomInfo(newkingdom.getId(), templateId, kingdomName, password, chatName, suffix, mottoOne, mottoTwo, allowPortal);
/*     */       
/* 421 */       lsw.kingdomExists(Servers.localServer.id, newkingdom.getId(), true);
/*     */       
/*     */       try {
/* 424 */         getResponder().setKingdomId(newkingdom.getId(), true);
/*     */       }
/* 426 */       catch (IOException iox) {
/*     */         
/* 428 */         logger.log(Level.WARNING, getResponder().getName() + ": " + iox.getMessage(), iox);
/*     */       } 
/*     */       
/*     */       try {
/* 432 */         resp.getCitizenVillage().setKingdom(newkingdom.getId());
/* 433 */         resp.getCitizenVillage().setKingdomInfluence();
/* 434 */         for (Creature c : this.creaturesToConvert) {
/*     */ 
/*     */           
/*     */           try {
/* 438 */             c.setKingdomId(newkingdom.getId(), true);
/*     */           }
/* 440 */           catch (IOException iox) {
/*     */             
/* 442 */             logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */           } 
/*     */         } 
/* 445 */         this.creaturesToConvert.clear();
/*     */       }
/* 447 */       catch (IOException iox) {
/*     */         
/* 449 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */       } 
/* 451 */       Kingdoms.convertTowersWithin(sx, sy, ex, ey, newkingdom.getId());
/* 452 */       created = true;
/* 453 */       King king = King.createKing(newkingdom.getId(), resp.getName(), resp.getWurmId(), resp.getSex());
/* 454 */       king.setCapital(resp.getCitizenVillage().getName(), true);
/* 455 */       Methods.rewardRegalia(resp);
/* 456 */       NewKingQuestion nk = new NewKingQuestion(resp, "New ruler!", "Congratulations!", resp.getWurmId());
/*     */       
/* 458 */       nk.sendQuestion();
/* 459 */       Items.destroyItem(this.target);
/*     */       
/*     */       try {
/* 462 */         Item contract = ItemFactory.createItem(299, 50.0F + Server.rand.nextFloat() * 50.0F, 
/* 463 */             getResponder().getName());
/* 464 */         getResponder().getInventory().insertItem(contract);
/*     */       }
/* 466 */       catch (Exception ex2) {
/*     */         
/* 468 */         logger.log(Level.INFO, ex2.getMessage(), ex2);
/*     */       } 
/*     */     } 
/* 471 */     if (!created) {
/* 472 */       getResponder().getCommunicator().sendNormalServerMessage("You decide to do nothing.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 483 */     StringBuilder buf = new StringBuilder();
/* 484 */     buf.append(getBmlHeader());
/* 485 */     if (!Servers.localServer.PVPSERVER) {
/*     */       
/* 487 */       buf.append("text{text=\"You may not use this here.\"}");
/*     */     }
/* 489 */     else if (getResponder().isKing()) {
/*     */       
/* 491 */       buf.append("text{text=\"What a foolish idea. You are already the king! Imagine the laughter if your loyal subjects knew!\"}");
/*     */     }
/*     */     else {
/*     */       
/* 495 */       if (getResponder().getCitizenVillage() != null) {
/*     */         
/* 497 */         Player[] players = Players.getInstance().getPlayers();
/* 498 */         for (Player p : players) {
/*     */           
/* 500 */           if (p.getCitizenVillage() == getResponder().getCitizenVillage())
/*     */           {
/* 502 */             p.getCommunicator()
/* 503 */               .sendAlertServerMessage(
/* 504 */                 getResponder().getName() + " may be forming a new kingdom with your village in it! Make sure you are outside of enemy areas.");
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 509 */       buf.append("text{text=\"You are ready to declare your independence from " + 
/* 510 */           Kingdoms.getNameFor(getResponder().getKingdomId()) + "!\"}");
/* 511 */       buf.append("text{text=\"\"}");
/* 512 */       buf.append("text{text=\"In order to succeed with this, you need to be mayor of and stand in your future capital.\"}");
/*     */       
/* 514 */       buf.append("text{text=\"Any alliances you have with other village will be disbanded.\"}");
/*     */ 
/*     */       
/* 517 */       buf.append("text{text=\"Everyone has to stand still to honor this event.\"}");
/* 518 */       buf.append("text{text=\"\"}");
/* 519 */       buf.append("text{text=\"In case the name of the kingdom exists on other servers, you have to provide the password for that kingdom.\"}");
/* 520 */       buf.append("harray{label{text='Name your new kingdom: '};input{id='kingdomName'; text='';maxchars='20'}}");
/* 521 */       buf.append("text{text=\"\"}");
/* 522 */       buf.append("harray{label{text='Provide a password for multiple servers (min 6 letters): '};input{id='passw'; text='';maxchars='10'}}");
/* 523 */       buf.append("text{text=\"\"}");
/* 524 */       buf.append("text{text=\"You have to select the kingdom that will serve as your example when it comes to special titles, combat moves, creatures and deities.\"}");
/* 525 */       buf.append("text{text=\"If your new kingdom is dissolved for any reason, any remaining people will revert to this template kingdom.\"}");
/* 526 */       buf.append("text{text=\"Note that the king must stay premium at all time.\"}");
/* 527 */       buf.append("harray{label{text='Template kingdom: '};dropdown{id='templateid';options=\"Jenn-Kellon,Mol Rehan,Horde of the Summoned\"}}");
/*     */ 
/*     */       
/* 530 */       buf.append("text{text=\"\"}");
/* 531 */       buf.append("checkbox{id='allowPortal';text='Allow people to join the kingdom via portals?';selected=\"true\"}");
/* 532 */       buf.append("text{text=\"Finally, provide two words that you think will describe your kingdom. You can change these later.\"}");
/* 533 */       buf.append("harray{input{id='mottoone'; maxchars='10'; text=\"Friendly\"}label{text=\" Description one\"}}");
/* 534 */       buf.append("harray{input{id='mottotwo'; maxchars='10'; text=\"Peasants\"}label{text=\" Description two\"}}");
/* 535 */       buf.append("text{text=\"\"}");
/*     */     } 
/* 537 */     buf.append(createAnswerButton2());
/*     */     
/* 539 */     getResponder().getCommunicator().sendBml(700, 530, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\KingdomFoundationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */