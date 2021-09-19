/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.Action;
/*     */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*     */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.spells.SpellResist;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocatePlayerQuestion
/*     */   extends Question
/*     */ {
/*     */   private boolean properlySent = false;
/*     */   private boolean override = false;
/*     */   private double power;
/*     */   
/*     */   public LocatePlayerQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, boolean eyeVyn, double power) {
/*  54 */     super(aResponder, aTitle, aQuestion, 79, aTarget);
/*  55 */     if (eyeVyn) {
/*  56 */       this.override = true;
/*     */     }
/*  58 */     this.power = power;
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
/*     */   public static String locatePlayerString(int targetDistance, String name, String direction, int maxDistance) {
/*  72 */     if (targetDistance > maxDistance)
/*     */     {
/*  74 */       return "No such soul found.";
/*     */     }
/*  76 */     String toReturn = "";
/*  77 */     if (targetDistance == 0) {
/*     */       
/*  79 */       toReturn = toReturn + "You are practically standing on the " + name + "! ";
/*     */     }
/*  81 */     else if (targetDistance < 1) {
/*     */       
/*  83 */       toReturn = toReturn + "The " + name + " is " + direction + " a few steps away! ";
/*     */     }
/*  85 */     else if (targetDistance < 4) {
/*     */       
/*  87 */       toReturn = toReturn + "The " + name + " is " + direction + " a stone's throw away! ";
/*     */     }
/*  89 */     else if (targetDistance < 6) {
/*     */       
/*  91 */       toReturn = toReturn + "The " + name + " is " + direction + " very close. ";
/*     */     }
/*  93 */     else if (targetDistance < 10) {
/*     */       
/*  95 */       toReturn = toReturn + "The " + name + " is " + direction + " pretty close by. ";
/*     */     }
/*  97 */     else if (targetDistance < 20) {
/*     */       
/*  99 */       toReturn = toReturn + "The " + name + " is " + direction + " fairly close by. ";
/*     */     }
/* 101 */     else if (targetDistance < 50) {
/*     */       
/* 103 */       toReturn = toReturn + "The " + name + " is some distance away " + direction + ". ";
/*     */     }
/* 105 */     else if (targetDistance < 200) {
/*     */       
/* 107 */       toReturn = toReturn + "The " + name + " is quite some distance away " + direction + ". ";
/*     */     }
/* 109 */     else if (targetDistance < 500) {
/*     */       
/* 111 */       toReturn = toReturn + "The " + name + " is rather a long distance away " + direction + ". ";
/*     */     }
/* 113 */     else if (targetDistance < 1000) {
/*     */       
/* 115 */       toReturn = toReturn + "The " + name + " is pretty far away " + direction + ". ";
/*     */     }
/* 117 */     else if (targetDistance < 2000) {
/*     */       
/* 119 */       toReturn = toReturn + "The " + name + " is far away " + direction + ". ";
/*     */     }
/*     */     else {
/*     */       
/* 123 */       toReturn = toReturn + "The " + name + " is very far away " + direction + ". ";
/*     */     } 
/* 125 */     return toReturn;
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
/* 136 */     if (!this.properlySent)
/*     */       return; 
/* 138 */     boolean found = false;
/* 139 */     String name = aAnswers.getProperty("name");
/* 140 */     if (name != null && name.length() > 1)
/*     */     {
/* 142 */       found = locateCorpse(name, getResponder(), this.power, this.override);
/*     */     }
/* 144 */     if (!found) {
/* 145 */       getResponder().getCommunicator().sendNormalServerMessage("No such soul found.");
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean locateCorpse(String name, Creature responder, double power, boolean overrideNolocate) {
/* 150 */     boolean found = false;
/* 151 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/*     */     
/* 153 */     if (pinf == null || !pinf.loaded) {
/* 154 */       return false;
/*     */     }
/*     */     
/* 157 */     if (pinf.getPower() > responder.getPower()) {
/* 158 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 162 */       Creature player = Server.getInstance().getCreature(pinf.wurmId);
/*     */       
/* 164 */       boolean nolocation = (player.getBonusForSpellEffect((byte)29) >= power);
/* 165 */       if (!nolocation)
/*     */       {
/*     */         
/* 168 */         nolocation = (SpellResist.getSpellResistance(player, 451) < 1.0D);
/*     */       }
/* 170 */       if (!nolocation || overrideNolocate) {
/*     */ 
/*     */         
/* 173 */         int maxDistance = Integer.MAX_VALUE;
/* 174 */         if (Servers.isThisAnEpicOrChallengeServer()) {
/* 175 */           maxDistance = 200;
/* 176 */         } else if (Servers.isThisAChaosServer()) {
/* 177 */           maxDistance = 500;
/*     */         } 
/* 179 */         found = true;
/* 180 */         int centerx = player.getTileX();
/* 181 */         int centery = player.getTileY();
/* 182 */         int dx = Math.abs(centerx - responder.getTileX());
/* 183 */         int dy = Math.abs(centery - responder.getTileY());
/* 184 */         int mindist = (int)Math.sqrt((dx * dx + dy * dy));
/* 185 */         int dir = MethodsCreatures.getDir(responder, centerx, centery);
/* 186 */         float bon = player.getNoLocateItemBonus((mindist <= maxDistance));
/* 187 */         if (bon > 0.0F)
/*     */         {
/*     */           
/* 190 */           if (1.0D + power < bon)
/* 191 */             found = false; 
/*     */         }
/* 193 */         if (found)
/*     */         {
/*     */           
/* 196 */           String direction = MethodsCreatures.getLocationStringFor(responder
/* 197 */               .getStatus().getRotation(), dir, "you");
/* 198 */           String toReturn = locatePlayerString(mindist, player.getName(), direction, maxDistance);
/* 199 */           responder.getCommunicator().sendNormalServerMessage(toReturn);
/*     */ 
/*     */ 
/*     */           
/* 203 */           if (bon > 0.0F && responder.getKingdomId() != player.getKingdomId())
/*     */           {
/* 205 */             SpellResist.addSpellResistance(player, 451, bon);
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 210 */     } catch (NoSuchCreatureException|com.wurmonline.server.NoSuchPlayerException noSuchCreatureException) {}
/*     */     
/* 212 */     Item[] its = Items.getAllItems();
/* 213 */     for (int itx = 0; itx < its.length; itx++) {
/*     */       
/* 215 */       if (its[itx].getZoneId() > -1)
/*     */       {
/* 217 */         if (its[itx].getTemplateId() == 272)
/*     */         {
/* 219 */           if (its[itx].getName().equals("corpse of " + pinf.getName())) {
/*     */             
/* 221 */             found = true;
/* 222 */             int centerx = its[itx].getTileX();
/* 223 */             int centery = its[itx].getTileY();
/* 224 */             int mindist = Math.max(Math.abs(centerx - responder.getTileX()), Math.abs(centery - responder
/* 225 */                   .getTileY()));
/* 226 */             if (responder.getPower() <= 0) {
/*     */               
/* 228 */               int dir = MethodsCreatures.getDir(responder, centerx, centery);
/* 229 */               String direction = MethodsCreatures.getLocationStringFor(responder.getStatus()
/* 230 */                   .getRotation(), dir, "you");
/* 231 */               String toReturn = EndGameItems.getDistanceString(mindist, its[itx].getName(), direction, false);
/*     */               
/* 233 */               if (!its[itx].isOnSurface()) {
/* 234 */                 responder.getCommunicator()
/* 235 */                   .sendNormalServerMessage(toReturn + " It lies below ground.");
/*     */               } else {
/* 237 */                 responder.getCommunicator().sendNormalServerMessage(toReturn);
/*     */               } 
/*     */             } else {
/* 240 */               responder.getCommunicator().sendNormalServerMessage(its[itx]
/* 241 */                   .getName() + " at " + centerx + ", " + centery + " surfaced=" + its[itx]
/* 242 */                   .isOnSurface());
/*     */             } 
/*     */           }  } 
/*     */       }
/*     */     } 
/* 247 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 258 */     boolean ok = true;
/* 259 */     if (getResponder().getPower() <= 0) {
/*     */       
/*     */       try {
/*     */         
/* 263 */         ok = false;
/* 264 */         Action act = getResponder().getCurrentAction();
/* 265 */         if (act.getNumber() == 419 || act
/* 266 */           .getNumber() == 118)
/*     */         {
/* 268 */           ok = true;
/*     */         }
/*     */       }
/* 271 */       catch (NoSuchActionException noSuchActionException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 276 */     if (!ok) {
/*     */       
/*     */       try {
/*     */         
/* 280 */         Item arti = Items.getItem(this.target);
/* 281 */         if (arti.getTemplateId() == 332)
/*     */         {
/* 283 */           if (arti.getOwnerId() == getResponder().getWurmId())
/*     */           {
/* 285 */             ok = true;
/*     */           }
/*     */         }
/*     */       }
/* 289 */       catch (NoSuchItemException noSuchItemException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 294 */     if (ok) {
/*     */       
/* 296 */       this.properlySent = true;
/* 297 */       StringBuilder sb = new StringBuilder();
/* 298 */       sb.append(getBmlHeader());
/* 299 */       sb.append("text{text='Which soul do you wish to locate?'};");
/*     */       
/* 301 */       sb.append("label{text='Name:'};input{id='name';maxchars='40';text=\"\"};");
/*     */       
/* 303 */       sb.append(createAnswerButton2());
/* 304 */       getResponder().getCommunicator().sendBml(300, 300, true, true, sb.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\LocatePlayerQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */