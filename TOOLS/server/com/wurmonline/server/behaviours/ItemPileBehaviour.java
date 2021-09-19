/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ final class ItemPileBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  54 */   private static final Logger logger = Logger.getLogger(ItemPileBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ItemPileBehaviour() {
/*  61 */     super((short)2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ItemPileBehaviour(short type) {
/*  72 */     super(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  83 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/*  85 */     if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 6.0F)) {
/*     */ 
/*     */       
/*  88 */       BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  89 */       if (performer.getPower() > 1 || result == null) {
/*     */ 
/*     */         
/*  92 */         toReturn.add(Actions.actionEntrys[6]);
/*  93 */         if (target.isHollow()) {
/*     */           
/*     */           try {
/*     */             
/*  97 */             Creature[] watchers = target.getWatchers();
/*  98 */             boolean watching = false;
/*  99 */             for (int x = 0; x < watchers.length; x++) {
/*     */               
/* 101 */               if (watchers[x] == performer)
/* 102 */                 watching = true; 
/*     */             } 
/* 104 */             if (watching) {
/* 105 */               toReturn.add(Actions.actionEntrys[4]);
/*     */             } else {
/* 107 */               toReturn.add(Actions.actionEntrys[3]);
/*     */             } 
/* 109 */           } catch (NoSuchCreatureException nsc) {
/*     */             
/* 111 */             toReturn.add(Actions.actionEntrys[3]);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 116 */     if (performer.getPower() > 2) {
/*     */       
/* 118 */       int cnt = -2;
/* 119 */       if (Servers.isThisATestServer())
/* 120 */         cnt--; 
/* 121 */       toReturn.add(new ActionEntry((short)cnt, "Specials", "Specials"));
/* 122 */       toReturn.add(Actions.actionEntrys[179]);
/* 123 */       toReturn.add(Actions.actionEntrys[185]);
/* 124 */       if (Servers.isThisATestServer()) {
/* 125 */         toReturn.add(Actions.actionEntrys[180]);
/*     */       }
/* 127 */     } else if (Players.isArtist(performer.getWurmId(), false, false)) {
/*     */       
/* 129 */       toReturn.add(Actions.actionEntrys[185]);
/*     */     } 
/* 131 */     addEmotes(toReturn);
/* 132 */     return toReturn;
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
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 144 */     return getBehavioursFor(performer, target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 155 */     boolean toReturn = true;
/* 156 */     if (action == 6) {
/*     */       
/* 158 */       toReturn = MethodsItems.takePile(act, performer, target);
/*     */     }
/* 160 */     else if (action == 7) {
/*     */       
/* 162 */       String[] msg = MethodsItems.drop(performer, target, true);
/* 163 */       if (msg.length > 0)
/*     */       {
/* 165 */         performer.getCommunicator().sendNormalServerMessage(msg[0] + msg[1] + msg[2]);
/* 166 */         Server.getInstance().broadCastAction(performer.getName() + " drops " + msg[1] + msg[3], performer, 5);
/*     */       }
/*     */     
/*     */     }
/* 170 */     else if (action == 3) {
/*     */       
/* 172 */       toReturn = true;
/* 173 */       if (!performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 6.0F)) {
/*     */         
/* 175 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/* 176 */         return toReturn;
/*     */       } 
/* 178 */       if (performer.addItemWatched(target))
/*     */       {
/* 180 */         performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName());
/* 181 */         target.addWatcher(target.getWurmId(), performer);
/* 182 */         target.sendContainedItems(target.getWurmId(), performer);
/*     */       }
/*     */     
/* 185 */     } else if (action == 4) {
/*     */       
/* 187 */       toReturn = true;
/* 188 */       performer.removeItemWatched(target);
/* 189 */       performer.getCommunicator().sendCloseInventoryWindow(target.getWurmId());
/* 190 */       target.removeWatcher(performer, true);
/*     */     }
/* 192 */     else if (action == 1) {
/*     */       
/* 194 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/* 195 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*     */     }
/* 197 */     else if (action == 179) {
/*     */       
/* 199 */       toReturn = true;
/* 200 */       if (performer.getPower() > 2) {
/*     */         
/* 202 */         Item[] items = target.getItemsAsArray();
/* 203 */         logger.log(Level.INFO, performer.getName() + " summoning pile at " + target.getTileX() + ", " + target
/* 204 */             .getTileY() + ". Number of items=" + items.length + ".");
/*     */         
/* 206 */         for (int x = 0; x < items.length; x++) {
/*     */           
/*     */           try
/*     */           {
/* 210 */             Zone currZone = Zones.getZone((int)items[x].getPosX() >> 2, (int)items[x].getPosY() >> 2, items[x]
/* 211 */                 .isOnSurface());
/* 212 */             currZone.removeItem(items[x]);
/* 213 */             items[x].putItemInfrontof(performer);
/*     */           }
/* 215 */           catch (NoSuchZoneException nsz)
/*     */           {
/* 217 */             performer.getCommunicator().sendNormalServerMessage("Failed to locate the zone for that item. Failed to summon.");
/*     */             
/* 219 */             logger.log(Level.WARNING, target.getWurmId() + ": " + nsz.getMessage(), (Throwable)nsz);
/*     */           }
/* 221 */           catch (NoSuchCreatureException nsc)
/*     */           {
/* 223 */             performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon.");
/*     */             
/* 225 */             logger.log(Level.WARNING, target.getWurmId() + ": " + nsc.getMessage(), (Throwable)nsc);
/*     */           }
/* 227 */           catch (NoSuchItemException nsi)
/*     */           {
/* 229 */             performer.getCommunicator().sendNormalServerMessage("Failed to locate the item for that request! Failed to summon.");
/*     */             
/* 231 */             logger.log(Level.WARNING, target.getWurmId() + ": " + nsi.getMessage(), (Throwable)nsi);
/*     */           }
/* 233 */           catch (NoSuchPlayerException nsp)
/*     */           {
/* 235 */             performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon.");
/*     */             
/* 237 */             logger.log(Level.WARNING, target.getWurmId() + ": " + nsp.getMessage(), (Throwable)nsp);
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 242 */     } else if (action == 180 && Servers.isThisATestServer()) {
/*     */       
/* 244 */       toReturn = true;
/* 245 */       if (performer.getPower() > 2) {
/*     */         
/* 247 */         Item[] items = target.getItemsAsArray();
/* 248 */         logger.log(Level.INFO, performer.getName() + " destroying items in pile at " + target.getTileX() + ", " + target
/* 249 */             .getTileY() + ". Number of items=" + items.length + ".");
/*     */         
/* 251 */         for (int x = 0; x < items.length; x++) {
/*     */           
/* 253 */           performer.getLogger().log(Level.INFO, performer
/*     */               
/* 255 */               .getName() + " destroyed item " + items[x].getWurmId() + ", " + items[x]
/* 256 */               .getNameWithGenus() + ", ql=" + items[x]
/* 257 */               .getQualityLevel());
/* 258 */           Items.destroyItem(items[x].getWurmId());
/*     */         } 
/*     */       } 
/*     */     } 
/* 262 */     if (action == 185) {
/*     */       
/* 264 */       toReturn = true;
/* 265 */       if (performer.getPower() >= 0 || Players.isArtist(performer.getWurmId(), false, false)) {
/*     */         
/* 267 */         performer.getCommunicator().sendNormalServerMessage("WurmId:" + target
/* 268 */             .getWurmId() + ", posx=" + target.getPosX() + "(" + ((int)target.getPosX() >> 2) + "), posy=" + target
/* 269 */             .getPosY() + "(" + ((int)target.getPosY() >> 2) + "), posz=" + target
/* 270 */             .getPosZ() + ", rot" + target.getRotation() + " layer=" + (
/* 271 */             target.isOnSurface() ? 0 : -1) + " fl=" + target.getFloorLevel() + " bridge=" + target
/* 272 */             .getBridgeId());
/* 273 */         performer.getCommunicator().sendNormalServerMessage("Ql:" + target
/* 274 */             .getQualityLevel() + ", damage=" + target.getDamage() + ", weight=" + target
/* 275 */             .getWeightGrams() + ", temp=" + target.getTemperature());
/* 276 */         performer.getCommunicator().sendNormalServerMessage("parentid=" + target
/* 277 */             .getParentId() + " ownerid=" + target.getOwnerId() + " zoneid=" + target
/* 278 */             .getZoneId() + " sizex=" + target.getSizeX() + ", sizey=" + target.getSizeY() + " sizez=" + target
/* 279 */             .getSizeZ() + ".");
/* 280 */         long timeSince = WurmCalendar.currentTime - target.getLastMaintained();
/*     */         
/* 282 */         String timeString = Server.getTimeFor(timeSince * 1000L);
/* 283 */         performer.getCommunicator().sendNormalServerMessage("Last maintained " + timeString + " ago.");
/* 284 */         String lastOwnerS = Long.toString(target.lastOwner);
/*     */         
/* 286 */         PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getLastOwnerId());
/* 287 */         if (p != null) {
/* 288 */           lastOwnerS = p.getName();
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 294 */             Creature c = Creatures.getInstance().getCreature(target.lastOwner);
/* 295 */             lastOwnerS = c.getName();
/*     */           }
/* 297 */           catch (NoSuchCreatureException nsc) {
/*     */             
/* 299 */             lastOwnerS = "dead " + lastOwnerS;
/*     */           } 
/*     */         } 
/*     */         
/* 303 */         performer.getCommunicator().sendNormalServerMessage("lastownerid=" + lastOwnerS + ", Model=" + target
/* 304 */             .getModelName());
/* 305 */         if (performer.getPower() >= 5) {
/*     */           
/* 307 */           performer.getCommunicator().sendNormalServerMessage("Zoneid=" + target
/* 308 */               .getZoneId() + " real zid=" + target.zoneId + " Counter=" + 
/* 309 */               WurmId.getNumber(target.getWurmId()) + " origin=" + WurmId.getOrigin(target.getWurmId()));
/* 310 */           if (target.isVehicle()) {
/*     */             
/* 312 */             float diffposx = target.getPosX() - performer.getPosX();
/* 313 */             float diffposy = target.getPosY() - performer.getPosY();
/* 314 */             performer.getCommunicator().sendNormalServerMessage("Relative: offx=" + diffposx + ", offy=" + diffposy + ", offz=" + performer
/* 315 */                 .getPositionZ() + " altOffZ=" + performer
/* 316 */                 .getAltOffZ());
/*     */           } 
/*     */         } 
/* 319 */         if (target.hasData())
/* 320 */           performer.getCommunicator().sendNormalServerMessage("data=" + target
/* 321 */               .getData() + ", data1=" + target.getData1() + " data2=" + target.getData2()); 
/* 322 */         String creator = ", creator=" + target.creator;
/* 323 */         if (target.creator == null || target.creator.length() == 0)
/* 324 */           creator = ""; 
/* 325 */         performer.getCommunicator().sendNormalServerMessage("auxdata=" + target.getAuxData() + creator);
/* 326 */         if (target.isKey())
/* 327 */           performer.getCommunicator().sendNormalServerMessage("lock id=" + target.getLockId()); 
/* 328 */         if (target.isLock()) {
/*     */           
/* 330 */           long[] keys = target.getKeyIds();
/* 331 */           performer.getCommunicator().sendNormalServerMessage("Keys:");
/* 332 */           for (long lKey : keys)
/* 333 */             performer.getCommunicator().sendNormalServerMessage(String.valueOf(lKey)); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 337 */     return toReturn;
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
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 350 */     boolean done = action(act, performer, target, action, counter);
/* 351 */     return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ItemPileBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */