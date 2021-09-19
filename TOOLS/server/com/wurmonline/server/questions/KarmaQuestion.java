/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.endgames.EndGameItem;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public class KarmaQuestion
/*     */   extends Question
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(Question.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KarmaQuestion(Creature aResponder) {
/*  55 */     super(aResponder, "Using your Karma", "Decide how you wish to use your Karma", 100, aResponder.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  66 */     String key = "karma";
/*  67 */     String val = answers.getProperty("karma");
/*  68 */     if (val != null)
/*     */     {
/*  70 */       if (val.equals("light50")) {
/*     */         
/*  72 */         if (getResponder().getKarma() >= 500) {
/*     */           
/*  74 */           int sx = Zones.safeTileX(getResponder().getTileX() - 50);
/*  75 */           int ex = Zones.safeTileX(getResponder().getTileX() + 50);
/*  76 */           int sy = Zones.safeTileY(getResponder().getTileY() - 50);
/*  77 */           int ey = Zones.safeTileY(getResponder().getTileY() + 50);
/*  78 */           Item[] items = Items.getAllItems();
/*  79 */           for (Item item : items) {
/*     */             
/*  81 */             if (item.getZoneId() > 0)
/*     */             {
/*  83 */               if (item.isStreetLamp())
/*     */               {
/*  85 */                 if (item.isWithin(sx, ex, sy, ey) && 
/*  86 */                   item.isPlanted()) {
/*     */                   
/*  88 */                   item.setAuxData((byte)120);
/*  89 */                   item.setTemperature((short)10000);
/*     */                 } 
/*     */               }
/*     */             }
/*     */           } 
/*  94 */           Server.getInstance().broadCastAction(
/*  95 */               getResponder().getName() + " convinces the fire spirits to light up the area!", getResponder(), 50);
/*  96 */           getResponder().getCommunicator().sendSafeServerMessage("The fire spirits light up the area!");
/*  97 */           getResponder().modifyKarma(-500);
/*     */         } else {
/*     */           
/* 100 */           getResponder().getCommunicator().sendNormalServerMessage("You need 500 karma for this.");
/*     */         } 
/* 102 */       } else if (val.equals("light100")) {
/*     */         
/* 104 */         if (getResponder().getKarma() >= 1500) {
/*     */           
/* 106 */           int sx = Zones.safeTileX(getResponder().getTileX() - 100);
/* 107 */           int ex = Zones.safeTileX(getResponder().getTileX() + 100);
/* 108 */           int sy = Zones.safeTileY(getResponder().getTileY() - 100);
/* 109 */           int ey = Zones.safeTileY(getResponder().getTileY() + 100);
/* 110 */           Item[] items = Items.getAllItems();
/* 111 */           for (Item item : items) {
/*     */             
/* 113 */             if (item.getZoneId() > 0)
/*     */             {
/* 115 */               if (item.isStreetLamp())
/*     */               {
/* 117 */                 if (item.isWithin(sx, ex, sy, ey) && 
/* 118 */                   item.isPlanted()) {
/*     */                   
/* 120 */                   item.setAuxData((byte)120);
/* 121 */                   item.setTemperature((short)10000);
/*     */                 } 
/*     */               }
/*     */             }
/*     */           } 
/* 126 */           Server.getInstance()
/* 127 */             .broadCastAction(getResponder().getName() + " convinces the fire spirits to light up the area!", 
/* 128 */               getResponder(), 100);
/* 129 */           getResponder().getCommunicator().sendSafeServerMessage("The fire spirits light up the area!");
/* 130 */           getResponder().modifyKarma(-1500);
/*     */         } else {
/*     */           
/* 133 */           getResponder().getCommunicator().sendNormalServerMessage("You need 1500 karma for this.");
/*     */         } 
/* 135 */       } else if (val.equals("light200")) {
/*     */         
/* 137 */         if (getResponder().getKarma() >= 3000) {
/*     */           
/* 139 */           int sx = Zones.safeTileX(getResponder().getTileX() - 200);
/* 140 */           int ex = Zones.safeTileX(getResponder().getTileX() + 200);
/* 141 */           int sy = Zones.safeTileY(getResponder().getTileY() - 200);
/* 142 */           int ey = Zones.safeTileY(getResponder().getTileY() + 200);
/* 143 */           Item[] items = Items.getAllItems();
/* 144 */           for (Item item : items) {
/*     */             
/* 146 */             if (item.getZoneId() > 0)
/*     */             {
/* 148 */               if (item.isStreetLamp())
/*     */               {
/* 150 */                 if (item.isWithin(sx, ex, sy, ey) && 
/* 151 */                   item.isPlanted()) {
/*     */                   
/* 153 */                   item.setAuxData((byte)120);
/* 154 */                   item.setTemperature((short)10000);
/*     */                 } 
/*     */               }
/*     */             }
/*     */           } 
/* 159 */           Server.getInstance()
/* 160 */             .broadCastAction(getResponder().getName() + " convinces the fire spirits to light up the area!", 
/* 161 */               getResponder(), 200);
/* 162 */           getResponder().getCommunicator().sendSafeServerMessage("The fire spirits light up the area!");
/* 163 */           getResponder().modifyKarma(-3000);
/*     */         } else {
/*     */           
/* 166 */           getResponder().getCommunicator().sendNormalServerMessage("You need 3000 karma for this.");
/*     */         } 
/* 168 */       } else if (val.equals("corpse")) {
/*     */         
/* 170 */         if (getResponder().getKarma() > 3000) {
/*     */           
/* 172 */           if (getResponder().maySummonCorpse()) {
/*     */             
/* 174 */             Item toSummon = null;
/* 175 */             int maxContained = 0;
/* 176 */             for (Item i : Items.getAllItems()) {
/*     */               
/* 178 */               if (i.getOwnerId() <= -10L)
/*     */               {
/* 180 */                 if (i.getName().equals("corpse of " + getResponder().getName())) {
/*     */                   
/* 182 */                   int nums = i.getItems().size();
/* 183 */                   if (nums >= maxContained) {
/*     */                     
/* 185 */                     toSummon = i;
/* 186 */                     maxContained = nums;
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             } 
/* 191 */             if (toSummon != null) {
/*     */               
/* 193 */               if (toSummon.getZoneId() >= 0) {
/*     */                 
/*     */                 try {
/*     */                   
/* 197 */                   Zone z = Zones.getZone((int)toSummon.getPosX() >> 2, 
/* 198 */                       (int)toSummon.getPosY() >> 2, toSummon.isOnSurface());
/* 199 */                   z.removeItem(toSummon);
/* 200 */                   logger.log(Level.INFO, toSummon.getName() + " was removed from " + (
/* 201 */                       (int)toSummon.getPosX() >> 2) + ',' + (
/* 202 */                       (int)toSummon.getPosY() >> 2) + ", surf=" + toSummon.isOnSurface());
/*     */                 }
/* 204 */                 catch (NoSuchZoneException nsz) {
/*     */                   
/* 206 */                   logger.log(Level.INFO, toSummon.getName() + " was not on " + (
/* 207 */                       (int)toSummon.getPosX() >> 2) + ',' + (
/* 208 */                       (int)toSummon.getPosY() >> 2) + ", surf=" + toSummon.isOnSurface());
/*     */                 } 
/*     */               }
/*     */               
/*     */               try {
/* 213 */                 Item parent = toSummon.getParent();
/* 214 */                 parent.dropItem(toSummon.getWurmId(), true);
/* 215 */                 logger.log(Level.INFO, toSummon.getName() + " was removed from " + parent.getName() + '.');
/*     */               
/*     */               }
/* 218 */               catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */               
/* 221 */               getResponder().getInventory().insertItem(toSummon);
/* 222 */               getResponder().getCommunicator().sendSafeServerMessage("The spirits summon your corpse!");
/* 223 */               getResponder().modifyKarma(-3000);
/*     */             } else {
/*     */               
/* 226 */               getResponder().getCommunicator().sendSafeServerMessage("The spirits fail to locate your corpse!");
/*     */             } 
/*     */           } else {
/*     */             
/* 230 */             long timeToNext = getResponder().getTimeToSummonCorpse();
/* 231 */             getResponder().getCommunicator().sendNormalServerMessage("You have to wait " + 
/* 232 */                 Server.getTimeFor(timeToNext) + " until you can summon your corpse.");
/*     */           } 
/*     */         } else {
/*     */           
/* 236 */           getResponder().getCommunicator().sendNormalServerMessage("You need 3000 karma for this.");
/*     */         } 
/* 238 */       } else if (val.equals("townportal")) {
/*     */         
/* 240 */         Item[] inventoryItems = getResponder().getInventory().getAllItems(true);
/* 241 */         for (Item lInventoryItem : inventoryItems) {
/*     */           
/* 243 */           if (lInventoryItem.isArtifact()) {
/*     */             
/* 245 */             getResponder().getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 246 */                 .getName() + " hums and disturbs the weave. You can not teleport right now.");
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 251 */         Item[] bodyItems = getResponder().getBody().getBodyItem().getAllItems(true);
/* 252 */         for (Item lInventoryItem : bodyItems) {
/*     */           
/* 254 */           if (lInventoryItem.isArtifact()) {
/*     */             
/* 256 */             getResponder().getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 257 */                 .getName() + " hums and disturbs the weave. You can not teleport right now.");
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 262 */         if (getResponder().getEnemyPresense() > 0 || getResponder().isFighting()) {
/*     */           
/* 264 */           getResponder().getCommunicator().sendNormalServerMessage("There are enemies in the vicinity. You fail to focus.");
/*     */           
/*     */           return;
/*     */         } 
/* 268 */         if (getResponder().getCitizenVillage() == null) {
/*     */           
/* 270 */           getResponder().getCommunicator().sendNormalServerMessage("You need to be citizen in a village to teleport home.");
/*     */           
/*     */           return;
/*     */         } 
/* 274 */         if (getResponder().mayChangeVillageInMillis() > 0L) {
/*     */           
/* 276 */           getResponder().getCommunicator().sendNormalServerMessage("You are still too new to this village to teleport home.");
/*     */           
/*     */           return;
/*     */         } 
/* 280 */         if (getResponder().getKarma() < 1000) {
/*     */           
/* 282 */           getResponder().getCommunicator().sendNormalServerMessage("You need 1000 karma to perform this feat.");
/*     */           
/*     */           return;
/*     */         } 
/* 286 */         if (getResponder().isOnPvPServer() && 
/* 287 */           Zones.isWithinDuelRing(getResponder().getTileX(), getResponder().getTileY(), true) != null) {
/*     */           
/* 289 */           getResponder().getCommunicator().sendNormalServerMessage("The magic of the duelling ring interferes. You can not teleport here.");
/*     */           
/*     */           return;
/*     */         } 
/* 293 */         if (getResponder().isInPvPZone()) {
/*     */           
/* 295 */           getResponder().getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not teleport here.");
/*     */           
/*     */           return;
/*     */         } 
/* 299 */         if (Servers.localServer.PVPSERVER && EndGameItems.getEvilAltar() != null) {
/*     */           
/* 301 */           EndGameItem egi = EndGameItems.getEvilAltar();
/*     */           
/* 303 */           if (getResponder().isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi
/* 304 */               .getItem().getPosZ(), 50.0F)) {
/*     */ 
/*     */             
/* 307 */             getResponder().getCommunicator().sendNormalServerMessage("The magic of this place interferes. You can not teleport here.");
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/* 312 */         } else if (Servers.localServer.PVPSERVER && EndGameItems.getGoodAltar() != null) {
/*     */           
/* 314 */           EndGameItem egi = EndGameItems.getGoodAltar();
/*     */           
/* 316 */           if (getResponder().isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi
/* 317 */               .getItem().getPosZ(), 50.0F)) {
/*     */ 
/*     */             
/* 320 */             getResponder().getCommunicator().sendNormalServerMessage("The magic of this place interferes. You can not teleport here.");
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/*     */         try {
/*     */           short[] tokenCoords;
/*     */           
/*     */           try {
/* 330 */             tokenCoords = getResponder().getCitizenVillage().getTokenCoords();
/*     */           }
/* 332 */           catch (NoSuchItemException nsi) {
/*     */             
/* 334 */             tokenCoords = getResponder().getCitizenVillage().getSpawnPoint();
/*     */           } 
/* 336 */           getResponder().setTeleportPoints(tokenCoords[0], tokenCoords[1], 0, 0);
/* 337 */           if (getResponder().startTeleporting())
/*     */           {
/* 339 */             getResponder().modifyKarma(-1000);
/* 340 */             getResponder().getCommunicator().sendNormalServerMessage("You feel a slight tingle in your spine.");
/* 341 */             getResponder().getCommunicator().sendTeleport(false);
/*     */           }
/*     */         
/* 344 */         } catch (Exception ex) {
/*     */           
/* 346 */           getResponder().getCommunicator().sendNormalServerMessage("The weave does not contain a proper teleport spot.");
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } else {
/* 352 */         getResponder().getCommunicator().sendNormalServerMessage("You decide to bide your time.");
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
/*     */   public void sendQuestion() {
/* 364 */     StringBuilder buf = new StringBuilder();
/* 365 */     buf.append(getBmlHeader());
/*     */     
/* 367 */     buf.append("text{text=\"You have " + getResponder().getKarma() + " karma, how would you like to spend it?\"}");
/* 368 */     buf.append("text{text=''}");
/* 369 */     buf.append("radio{ group='karma'; id='light50';text='Light up 50 tiles radius (500 karma)'}");
/* 370 */     buf.append("radio{ group='karma'; id='light100';text='Light up 100 tiles radius (1500 karma)'}");
/* 371 */     buf.append("radio{ group='karma'; id='light200';text='Light up 200 tiles radius (3000 karma)'}");
/* 372 */     buf.append("radio{ group='karma'; id='corpse';text='Summon corpse (3000 karma, 5 minutes delay)'}");
/* 373 */     buf.append("radio{ group='karma'; id='townportal';text='Town Portal (1000 karma, enemies block)'}");
/* 374 */     buf.append("radio{ group='karma'; id='false';text='Do nothing';selected='true'}");
/* 375 */     buf.append(createAnswerButton2());
/*     */     
/* 377 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\KarmaQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */