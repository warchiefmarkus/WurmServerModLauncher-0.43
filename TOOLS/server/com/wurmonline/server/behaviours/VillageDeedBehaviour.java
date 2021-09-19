/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.questions.KingdomHistory;
/*     */ import com.wurmonline.server.questions.KingdomStatusQuestion;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ final class VillageDeedBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(VillageDeedBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   VillageDeedBehaviour() {
/*  52 */     super((short)24);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  58 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  59 */     toReturn.addAll(getBehavioursForPapers(performer, target));
/*  60 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  66 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  67 */     toReturn.addAll(getBehavioursForPapers(performer, target));
/*  68 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   List<ActionEntry> getBehavioursForPapers(Creature performer, Item target) {
/*  73 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  74 */     long ownerId = target.getOwnerId();
/*  75 */     if (ownerId == performer.getWurmId()) {
/*     */       
/*  77 */       int villageId = target.getData2();
/*  78 */       if (villageId <= 0) {
/*     */         
/*  80 */         if (target.getTemplateId() != 663 && target.getTemplateId() != 862) {
/*     */           
/*  82 */           toReturn.add(new ActionEntry((short)(Servers.localServer.testServer ? -2 : -1), "Settlement", "Settlement options"));
/*     */           
/*  84 */           toReturn.add(Actions.actionEntrys[466]);
/*  85 */           if (Servers.localServer.testServer)
/*  86 */             toReturn.add(Actions.actionEntrys[65]); 
/*     */         } 
/*  88 */         toReturn.add(new ActionEntry((short)-1, "Settlement", "Settlement options"));
/*  89 */         toReturn.add(Actions.actionEntrys[65]);
/*     */ 
/*     */       
/*     */       }
/*  93 */       else if (target.getTemplateId() != 663) {
/*     */         
/*  95 */         int nums = -2;
/*  96 */         toReturn.add(new ActionEntry((short)-2, "Settlement", "Settlement options"));
/*  97 */         toReturn.add(Actions.actionEntrys[78]);
/*  98 */         toReturn.add(Actions.actionEntrys[466]);
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 104 */           Village curVill = Villages.getVillage(villageId);
/* 105 */           toReturn.addAll(VillageTokenBehaviour.getSettlementMenu(performer, false, curVill, curVill));
/*     */         }
/* 107 */         catch (NoSuchVillageException e) {
/*     */ 
/*     */           
/* 110 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 121 */     boolean done = true;
/* 122 */     if (action == 1) {
/*     */       
/* 124 */       if (!target.isNewDeed()) {
/*     */         
/* 126 */         if (target.isVillageDeed()) {
/*     */ 
/*     */           
/*     */           try {
/* 130 */             Village village = Villages.getVillage(target.getData2());
/* 131 */             performer.getCommunicator().sendNormalServerMessage("This is the village deed for " + village
/* 132 */                 .getName() + ". You should replace it with the new version.");
/*     */           
/*     */           }
/* 135 */           catch (NoSuchVillageException nss) {
/*     */ 
/*     */             
/* 138 */             int templateId = target.getTemplateId();
/* 139 */             int size = Villages.getSizeForDeed(templateId);
/* 140 */             performer.getCommunicator().sendNormalServerMessage("A paper giving the possessor the right to found a village of the size " + size + ". You should refund it and use the new version instead.");
/*     */ 
/*     */             
/* 143 */             if (target.getData2() >= 1) {
/* 144 */               logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/*     */             }
/*     */           } 
/* 147 */         } else if (target.isHomesteadDeed()) {
/*     */ 
/*     */           
/*     */           try {
/* 151 */             Village stead = Villages.getVillage(target.getData2());
/* 152 */             performer.getCommunicator().sendNormalServerMessage("This is the homestead deed for " + stead
/* 153 */                 .getName() + ". You should replace it with the new version.");
/*     */           
/*     */           }
/* 156 */           catch (NoSuchVillageException nss) {
/*     */             
/* 158 */             int size = Villages.getSizeForDeed(target.getTemplateId());
/* 159 */             performer.getCommunicator().sendNormalServerMessage("A paper giving the possessor the right to found a homestead of the size " + size + ". You should refund it and use the new version instead.");
/*     */ 
/*     */             
/* 162 */             if (target.getData2() >= 1) {
/* 163 */               logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 171 */           Village village = Villages.getVillage(target.getData2());
/* 172 */           performer.getCommunicator().sendNormalServerMessage("This is the settlement deed for " + village
/* 173 */               .getName() + ".");
/*     */         }
/* 175 */         catch (NoSuchVillageException nss) {
/*     */           
/* 177 */           if (target.getData2() >= 1) {
/* 178 */             logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/*     */           }
/*     */         } 
/*     */       } 
/* 182 */     } else if (action == 77) {
/*     */       
/* 184 */       done = true;
/* 185 */       if (checkPapersOk(performer, target)) {
/* 186 */         Methods.sendVillageInfo(performer, target);
/*     */       }
/* 188 */     } else if (action == 670) {
/*     */       
/* 190 */       done = true;
/* 191 */       if (checkPapersOk(performer, target)) {
/* 192 */         Methods.sendManageUpkeep(performer, target);
/*     */       }
/* 194 */     } else if (action == 71) {
/*     */       
/* 196 */       done = true;
/* 197 */       if (checkPapersOk(performer, target)) {
/* 198 */         Methods.sendVillageHistory(performer, target);
/*     */       }
/* 200 */     } else if (action == 72) {
/*     */       
/* 202 */       done = true;
/* 203 */       if (checkPapersOk(performer, target)) {
/* 204 */         Methods.sendAreaHistory(performer, target);
/*     */       }
/* 206 */     } else if (action == 68) {
/*     */       
/* 208 */       done = true;
/* 209 */       if (checkPapersOk(performer, target)) {
/* 210 */         Methods.sendManageVillageSettingsQuestion(performer, target);
/*     */       }
/* 212 */     } else if (action == 540) {
/*     */       
/* 214 */       done = true;
/* 215 */       if (checkPapersOk(performer, target)) {
/* 216 */         Methods.sendManageVillageRolesQuestion(performer, target);
/*     */       }
/* 218 */     } else if (action == 69) {
/*     */       
/* 220 */       done = true;
/* 221 */       if (checkPapersOk(performer, target)) {
/* 222 */         Methods.sendReputationManageQuestion(performer, target);
/*     */       }
/* 224 */     } else if (action == 67) {
/*     */       
/* 226 */       done = true;
/* 227 */       if (checkPapersOk(performer, target)) {
/* 228 */         Methods.sendManageVillageGuardsQuestion(performer, target);
/*     */       }
/* 230 */     } else if (action == 66) {
/*     */       
/* 232 */       done = true;
/* 233 */       if (checkPapersOk(performer, target)) {
/* 234 */         Methods.sendManageVillageCitizensQuestion(performer, target);
/*     */       }
/* 236 */     } else if (action == 70) {
/*     */       
/* 238 */       done = true;
/* 239 */       if (checkPapersOk(performer, target)) {
/* 240 */         Methods.sendManageVillageGatesQuestion(performer, target);
/*     */       }
/* 242 */     } else if (action == 355) {
/*     */       
/* 244 */       done = true;
/*     */       
/* 246 */       KingdomStatusQuestion kq = new KingdomStatusQuestion(performer, "Kingdom status", "Kingdoms", performer.getWurmId());
/* 247 */       kq.sendQuestion();
/*     */     }
/* 249 */     else if (action == 356) {
/*     */       
/* 251 */       done = true;
/*     */       
/* 253 */       KingdomHistory kq = new KingdomHistory(performer, "Kingdom history", "History of the kingdoms", performer.getWurmId());
/* 254 */       kq.sendQuestion();
/*     */     }
/* 256 */     else if (action == 65) {
/*     */       
/* 258 */       if (!target.isNewDeed() && !Servers.localServer.testServer && target
/* 259 */         .getTemplateId() != 862) {
/*     */         
/* 261 */         performer.getCommunicator().sendSafeServerMessage("You need to refund this deed and purchase a new one instead.");
/*     */         
/* 263 */         return true;
/*     */       } 
/*     */       
/* 266 */       long ownerId = target.getOwnerId();
/* 267 */       if (ownerId == performer.getWurmId()) {
/*     */         
/* 269 */         int villageId = target.getData2();
/* 270 */         if (villageId > 0) {
/*     */           
/* 272 */           performer.getCommunicator().sendSafeServerMessage("This settlement is already founded!");
/*     */         }
/*     */         else {
/*     */           
/* 276 */           Methods.sendFoundVillageQuestion(performer, target);
/*     */         } 
/*     */       } else {
/*     */         
/* 280 */         logger.log(Level.WARNING, performer.getName() + " trying to manage deed which isn't his.");
/*     */       } 
/* 282 */     }  if (action == 76) {
/*     */       
/* 284 */       if (target.isNewDeed()) {
/* 285 */         Methods.sendExpandVillageQuestion(performer, target);
/*     */       } else {
/* 287 */         logger.log(Level.WARNING, performer.getName() + " shouldn't be able to do this with a " + target.getName() + ".");
/*     */       }
/*     */     
/* 290 */     } else if (action == 466) {
/*     */       
/* 292 */       done = true;
/* 293 */       if (!target.isNewDeed())
/*     */       {
/* 295 */         if (target.isOldDeed()) {
/*     */           
/* 297 */           int villageId = target.getData2();
/* 298 */           if (villageId > 0) {
/*     */             
/* 300 */             performer.getCommunicator().sendSafeServerMessage("This village/homestead is already founded. Disband first, then refund.");
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 305 */           else if (performer.getWurmId() == target.getOwnerId()) {
/*     */             
/* 307 */             long left = target.getValue();
/* 308 */             if (left > 0L)
/*     */             {
/* 310 */               LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 311 */               if (!lsw.addMoney(performer.getWurmId(), performer.getName(), left, "Refund " + target
/* 312 */                   .getWurmId()))
/*     */               {
/* 314 */                 performer.getCommunicator().sendSafeServerMessage("Failed to contact your bank. Please try later.");
/*     */               
/*     */               }
/*     */               else
/*     */               {
/* 319 */                 Items.destroyItem(target.getWurmId());
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/*     */       }
/* 327 */     } else if (action == 78) {
/*     */       
/* 329 */       done = true;
/* 330 */       int villageId = target.getData2();
/* 331 */       if (villageId > 0)
/*     */       {
/* 333 */         if (target.getOwnerId() == performer.getWurmId()) {
/*     */           try
/*     */           {
/*     */             
/* 337 */             Village village = Villages.getVillage(villageId);
/* 338 */             village.replaceDeed(performer, target);
/*     */           }
/* 340 */           catch (NoSuchVillageException nsv)
/*     */           {
/* 342 */             performer.getCommunicator()
/* 343 */               .sendSafeServerMessage("Failed to locate the village that this deed is for!");
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 348 */     } else if (action == 80) {
/*     */       
/* 350 */       done = true;
/* 351 */       if (checkPapersOk(performer, target)) {
/* 352 */         Methods.sendManageAllianceQuestion(performer, target);
/*     */       }
/* 354 */     } else if (action == 481) {
/*     */       
/* 356 */       done = true;
/* 357 */       int villageId = target.getData2();
/* 358 */       if (villageId <= 0) {
/*     */         
/* 360 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/* 361 */         logger.log(Level.WARNING, performer.getName() + " managing deed with id " + target.getWurmId() + " but villageid=" + villageId);
/*     */       } else {
/*     */         
/*     */         try
/*     */         {
/*     */ 
/*     */           
/* 368 */           Village currVill = Villages.getVillage(target.getData2());
/* 369 */           if (currVill.isActionAllowed((short)68, performer) || performer
/* 370 */             .getPower() >= 2)
/*     */           {
/* 372 */             Methods.sendConfigureTwitter(performer, target.getData2(), true, currVill.getName());
/*     */           }
/*     */           else
/*     */           {
/* 376 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/* 377 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*     */           }
/*     */         
/*     */         }
/* 381 */         catch (NoSuchVillageException nsv)
/*     */         {
/* 383 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 389 */       done = super.action(act, performer, target, action, counter);
/* 390 */     }  return done;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkPapersOk(Creature performer, Item target) {
/* 395 */     if (!target.isNewDeed()) {
/* 396 */       performer.getCommunicator().sendSafeServerMessage("You need to replace the deed first.");
/*     */     } else {
/*     */       
/* 399 */       long ownerId = target.getOwnerId();
/* 400 */       if (ownerId == performer.getWurmId()) {
/*     */         
/* 402 */         int villageId = target.getData2();
/* 403 */         if (villageId <= 0) {
/*     */           
/* 405 */           performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/* 406 */           logger.log(Level.WARNING, performer.getName() + " managing deed with id " + target.getWurmId() + " but tried to do illegal action since villageid=" + villageId);
/*     */         }
/*     */         else {
/*     */           
/* 410 */           return true;
/*     */         } 
/*     */       } else {
/* 413 */         logger.log(Level.WARNING, performer.getName() + " trying to manage deed which isn't theirs.");
/*     */       } 
/* 415 */     }  return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\VillageDeedBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */