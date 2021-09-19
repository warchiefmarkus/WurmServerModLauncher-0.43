/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.behaviours.MethodsItems;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.server.players.JournalTier;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerJournal;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TestQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  59 */   private static final Logger logger = Logger.getLogger(TestQuestion.class.getName());
/*  60 */   private static final ConcurrentHashMap<Long, Long> armourCreators = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestQuestion(Creature aResponder, long aTarget) {
/*  71 */     super(aResponder, "Testing", "What do you want to do?", 96, aTarget);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkIfMayCreateArmour() {
/*  76 */     if (getResponder().getPower() > 0)
/*  77 */       return true; 
/*  78 */     Long last = armourCreators.get(Long.valueOf(getResponder().getWurmId()));
/*  79 */     if (last != null)
/*     */     {
/*  81 */       if (System.currentTimeMillis() - last.longValue() < 300000L)
/*  82 */         return false; 
/*     */     }
/*  84 */     last = new Long(System.currentTimeMillis());
/*  85 */     armourCreators.put(Long.valueOf(getResponder().getWurmId()), last);
/*  86 */     return true;
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
/*  97 */     if (Servers.localServer.testServer) {
/*     */       
/*  99 */       getResponder().getBody().healFully();
/* 100 */       getResponder().getStatus().modifyStamina2(100.0F);
/*     */ 
/*     */       
/* 103 */       String priestTypeString = aAnswers.getProperty("priestType");
/* 104 */       String faithLevelString = aAnswers.getProperty("faithLevel");
/*     */       
/* 106 */       if (priestTypeString != null) {
/*     */         
/* 108 */         int count, priestType = Integer.parseInt(priestTypeString);
/* 109 */         switch (priestType) {
/*     */           case 0:
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/* 116 */             if (getResponder().getDeity() != null) {
/*     */               
/*     */               try {
/*     */                 
/* 120 */                 getResponder().setFaith(0.0F);
/* 121 */                 getResponder().setFavor(0.0F);
/* 122 */                 getResponder().setDeity(null);
/* 123 */                 getResponder().getCommunicator().sendNormalServerMessage("You follow no deity.");
/*     */               }
/* 125 */               catch (IOException e) {
/*     */                 
/* 127 */                 getResponder().getCommunicator().sendNormalServerMessage("Could not remove deity.");
/*     */               } 
/*     */             }
/*     */             break;
/*     */ 
/*     */           
/*     */           default:
/* 134 */             count = 2;
/* 135 */             for (Deity d : Deities.getDeities()) {
/*     */               
/* 137 */               if (count == priestType) {
/*     */                 
/*     */                 try {
/*     */                   
/* 141 */                   getResponder().setDeity(d);
/* 142 */                   getResponder().getCommunicator().sendNormalServerMessage("You are now a follower of " + d.getName() + ".");
/*     */                 }
/* 144 */                 catch (IOException e) {
/*     */                   
/* 146 */                   getResponder().getCommunicator().sendNormalServerMessage("Could not set deity.");
/*     */                 } 
/*     */               }
/* 149 */               count++;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */       } 
/* 154 */       if (faithLevelString != null) {
/*     */         
/* 156 */         int faithLevel = Integer.parseInt(faithLevelString);
/* 157 */         if (faithLevel > 0) {
/*     */           
/* 159 */           faithLevel = Math.min(100, faithLevel);
/* 160 */           if (getResponder().getDeity() != null) {
/*     */             
/*     */             try {
/*     */               
/* 164 */               getResponder().getCommunicator().sendNormalServerMessage("Faith set to " + faithLevel + ".");
/* 165 */               if (faithLevel >= 30 && !getResponder().isPriest()) {
/*     */                 
/* 167 */                 getResponder().setPriest(true);
/* 168 */                 getResponder().getCommunicator().sendNormalServerMessage("You are now a priest of " + getResponder().getDeity().getName() + ".");
/*     */                 
/* 170 */                 if (getResponder().isPlayer()) {
/* 171 */                   PlayerJournal.sendTierUnlock((Player)getResponder(), (JournalTier)PlayerJournal.getAllTiers().get(Byte.valueOf((byte)10)));
/*     */                 }
/* 173 */               } else if (faithLevel < 30 && getResponder().isPriest()) {
/*     */                 
/* 175 */                 getResponder().setPriest(false);
/* 176 */                 getResponder().getCommunicator().sendNormalServerMessage("You are no longer a priest of " + getResponder().getDeity().getName() + ".");
/*     */               } 
/* 178 */               getResponder().setFaith(faithLevel);
/*     */             }
/* 180 */             catch (IOException e) {
/*     */               
/* 182 */               getResponder().getCommunicator().sendNormalServerMessage("Could not set faith.");
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 188 */       String skillLevel = aAnswers.getProperty("skillLevel");
/* 189 */       if (skillLevel != null) {
/*     */         
/*     */         try {
/*     */           
/* 193 */           double slevel = Double.parseDouble(skillLevel);
/* 194 */           slevel = Math.min(slevel, 90.0D);
/* 195 */           if (slevel > 0.0D) {
/*     */             
/* 197 */             Skills s = getResponder().getSkills();
/* 198 */             if (s != null) {
/*     */               
/* 200 */               Skill[] skills = s.getSkills();
/* 201 */               for (Skill sk : skills) {
/*     */                 
/* 203 */                 if (sk.getType() != 0 && sk.getType() != 1) {
/* 204 */                   sk.setKnowledge(slevel, false);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/* 209 */         } catch (Exception e) {
/*     */           
/* 211 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 213 */             logger.fine("skill bug?");
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 218 */       String alignLevel = aAnswers.getProperty("alignmentLevel");
/* 219 */       if (alignLevel != null) {
/*     */         
/*     */         try {
/*     */           
/* 223 */           float alignment = Float.parseFloat(alignLevel);
/* 224 */           if (alignment != 0.0F)
/*     */           {
/* 226 */             if (alignment > 99.0F)
/* 227 */               alignment = 99.0F; 
/* 228 */             if (alignment < -99.0F) {
/* 229 */               alignment = -99.0F;
/*     */             }
/* 231 */             getResponder().setAlignment(alignment);
/*     */           }
/*     */         
/* 234 */         } catch (Exception e) {
/*     */           
/* 236 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 238 */             logger.fine("alignment update issue");
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 244 */       String charLevel = aAnswers.getProperty("characteristicsLevel");
/* 245 */       if (charLevel != null) {
/*     */         
/*     */         try {
/*     */           
/* 249 */           double slevel = Double.parseDouble(charLevel);
/* 250 */           slevel = Math.min(slevel, 90.0D);
/* 251 */           if (slevel > 0.0D) {
/*     */             
/* 253 */             Skills s = getResponder().getSkills();
/* 254 */             if (s != null) {
/*     */               
/* 256 */               Skill[] skills = s.getSkills();
/* 257 */               for (Skill sk : skills) {
/*     */                 
/* 259 */                 if (sk.getType() == 0 || sk.getType() == 1) {
/* 260 */                   sk.setKnowledge(slevel, false);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/* 265 */         } catch (Exception e) {
/*     */           
/* 267 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 269 */             logger.fine("skill bug?");
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 274 */       String itemtype = aAnswers.getProperty("itemtype");
/* 275 */       if (itemtype != null) {
/*     */         
/* 277 */         String quantity = aAnswers.getProperty("quantity");
/* 278 */         int qty = 0;
/*     */         
/*     */         try {
/* 281 */           qty = Integer.parseInt(quantity);
/*     */         }
/* 283 */         catch (NumberFormatException nfs) {
/*     */           
/* 285 */           qty = 0;
/*     */         } 
/* 287 */         if (qty < 0) {
/* 288 */           qty = 0;
/*     */         }
/* 290 */         String materialType = aAnswers.getProperty("materialtype");
/* 291 */         byte matType = -1;
/*     */         
/*     */         try {
/* 294 */           matType = Byte.parseByte(materialType);
/*     */         }
/* 296 */         catch (NumberFormatException nfs) {
/*     */           
/* 298 */           matType = -1;
/*     */         } 
/* 300 */         if (matType < 0) {
/* 301 */           matType = -1;
/*     */         }
/* 303 */         if (matType > 0) {
/*     */           
/* 305 */           matType = (byte)(matType - 1);
/*     */           
/* 307 */           if (matType < (MethodsItems.getAllNormalWoodTypes()).length) {
/*     */             
/* 309 */             matType = MethodsItems.getAllNormalWoodTypes()[matType];
/*     */           }
/*     */           else {
/*     */             
/* 313 */             matType = (byte)(matType - (MethodsItems.getAllNormalWoodTypes()).length);
/*     */             
/* 315 */             if (matType > (MethodsItems.getAllMetalTypes()).length) {
/*     */               
/* 317 */               matType = -1;
/*     */             }
/*     */             else {
/*     */               
/* 321 */               matType = MethodsItems.getAllMetalTypes()[matType];
/*     */             } 
/*     */           } 
/*     */         } else {
/*     */           
/* 326 */           matType = -1;
/*     */         } 
/* 328 */         String qualityLevel = aAnswers.getProperty("qualitylevel");
/*     */         
/* 330 */         if (qualityLevel != null) {
/*     */           
/*     */           try {
/* 333 */             int ql = Integer.parseInt(qualityLevel);
/* 334 */             if (ql > 0) {
/*     */               
/* 336 */               ql = Math.min(ql, 90); try {
/*     */                 int drakeColor, scaleColor;
/*     */                 ItemTemplate[] itemtemps;
/* 339 */                 int num = Integer.parseInt(itemtype);
/* 340 */                 if (num == 0) {
/*     */                   return;
/*     */                 }
/*     */                 
/* 344 */                 num--;
/*     */                 
/* 346 */                 if (num <= 6 && 
/* 347 */                   !checkIfMayCreateArmour())
/*     */                 {
/* 349 */                   getResponder().getCommunicator().sendNormalServerMessage("You may only create items every 5 minutes in order to save the database.");
/*     */                 }
/*     */ 
/*     */                 
/* 353 */                 switch (num) {
/*     */                   
/*     */                   case 0:
/* 356 */                     createAndInsertItems(getResponder(), 109, 114, ql, false, matType);
/*     */                     
/* 358 */                     createAndInsertItems(getResponder(), 109, 109, ql, false, matType);
/*     */                     
/* 360 */                     createAndInsertItems(getResponder(), 114, 114, ql, false, matType);
/*     */                     
/* 362 */                     createAndInsertItems(getResponder(), 111, 111, ql, false, matType);
/*     */                     
/* 364 */                     createAndInsertItems(getResponder(), 779, 779, ql, false, matType);
/*     */                     break;
/*     */                   case 1:
/* 367 */                     createAndInsertItems(getResponder(), 103, 108, ql, false, matType);
/*     */                     
/* 369 */                     createAndInsertItems(getResponder(), 103, 103, ql, false, matType);
/*     */                     
/* 371 */                     createAndInsertItems(getResponder(), 105, 105, ql, false, matType);
/*     */                     
/* 373 */                     createAndInsertItems(getResponder(), 106, 106, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 2:
/* 377 */                     createAndInsertItems(getResponder(), 115, 120, ql, false, matType);
/*     */                     
/* 379 */                     createAndInsertItems(getResponder(), 119, 119, ql, false, matType);
/*     */                     
/* 381 */                     createAndInsertItems(getResponder(), 116, 116, ql, false, matType);
/*     */                     
/* 383 */                     createAndInsertItems(getResponder(), 115, 115, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 3:
/* 387 */                     createAndInsertItems(getResponder(), 274, 279, ql, false, matType);
/* 388 */                     createAndInsertItems(getResponder(), 278, 278, ql, false, matType);
/*     */                     
/* 390 */                     createAndInsertItems(getResponder(), 274, 274, ql, false, matType);
/* 391 */                     createAndInsertItems(getResponder(), 277, 277, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 4:
/* 395 */                     createAndInsertItems(getResponder(), 280, 287, ql, false, matType);
/* 396 */                     createAndInsertItems(getResponder(), 284, 284, ql, false, matType);
/*     */                     
/* 398 */                     createAndInsertItems(getResponder(), 280, 280, ql, false, matType);
/* 399 */                     createAndInsertItems(getResponder(), 283, 283, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 5:
/* 403 */                     drakeColor = getRandomDragonColor();
/* 404 */                     createAndInsertItems(getResponder(), 468, 473, ql, drakeColor, false);
/*     */                     
/* 406 */                     createAndInsertItems(getResponder(), 472, 472, ql, drakeColor, false);
/*     */                     
/* 408 */                     createAndInsertItems(getResponder(), 469, 469, ql, drakeColor, false);
/*     */                     
/* 410 */                     createAndInsertItems(getResponder(), 468, 468, ql, drakeColor, false);
/*     */                     break;
/*     */                   
/*     */                   case 6:
/* 414 */                     scaleColor = getRandomDragonColor();
/* 415 */                     createAndInsertItems(getResponder(), 474, 478, ql, scaleColor, false);
/*     */                     
/* 417 */                     createAndInsertItems(getResponder(), 478, 478, ql, scaleColor, false);
/*     */                     
/* 419 */                     createAndInsertItems(getResponder(), 474, 474, ql, scaleColor, false);
/*     */                     
/* 421 */                     createAndInsertItems(getResponder(), 477, 477, ql, scaleColor, false);
/*     */                     break;
/*     */                   
/*     */                   case 7:
/* 425 */                     createAndInsertItems(getResponder(), 80, 80, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 8:
/* 429 */                     createAndInsertItems(getResponder(), 21, 21, ql, false, matType);
/*     */                     break;
/*     */                   case 9:
/* 432 */                     createAndInsertItems(getResponder(), 81, 81, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 10:
/* 436 */                     createAndInsertItems(getResponder(), 291, 291, ql, false, matType);
/*     */                     break;
/*     */                   case 11:
/* 439 */                     createAndInsertItems(getResponder(), 292, 292, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 12:
/* 443 */                     createAndInsertItems(getResponder(), 290, 290, ql, false, matType);
/*     */                     break;
/*     */                   case 13:
/* 446 */                     createAndInsertItems(getResponder(), 3, 3, ql, false, matType);
/*     */                     break;
/*     */                   case 14:
/* 449 */                     createAndInsertItems(getResponder(), 90, 90, ql, false, matType);
/*     */                     break;
/*     */                   case 15:
/* 452 */                     createAndInsertItems(getResponder(), 87, 87, ql, false, matType);
/*     */                     break;
/*     */                   case 16:
/* 455 */                     createAndInsertItems(getResponder(), 706, 706, ql, false, matType);
/*     */                     break;
/*     */                   case 17:
/* 458 */                     createAndInsertItems(getResponder(), 705, 705, ql, false, matType);
/*     */                     break;
/*     */                   case 18:
/* 461 */                     createAndInsertItems(getResponder(), 707, 707, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 19:
/* 465 */                     createAndInsertItems(getResponder(), 86, 86, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 20:
/* 469 */                     createAndInsertItems(getResponder(), 4, 4, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 21:
/* 473 */                     createAndInsertItems(getResponder(), 85, 85, ql, false, matType);
/*     */                     break;
/*     */                   
/*     */                   case 22:
/* 477 */                     createAndInsertItems(getResponder(), 82, 82, ql, false, matType);
/*     */                     break;
/*     */ 
/*     */                   
/*     */                   case 23:
/* 482 */                     createMultiple(getResponder(), 25, 1, ql, matType);
/* 483 */                     createMultiple(getResponder(), 20, 1, ql, matType);
/* 484 */                     createMultiple(getResponder(), 24, 1, ql, matType);
/* 485 */                     createMultiple(getResponder(), 480, 1, ql, matType);
/* 486 */                     createMultiple(getResponder(), 8, 1, ql, matType);
/* 487 */                     createMultiple(getResponder(), 143, 1, ql, matType);
/* 488 */                     createMultiple(getResponder(), 7, 1, ql, matType);
/* 489 */                     createMultiple(getResponder(), 62, 1, ql, matType);
/* 490 */                     createMultiple(getResponder(), 63, 1, ql, matType);
/* 491 */                     createMultiple(getResponder(), 493, 1, ql, matType);
/* 492 */                     createMultiple(getResponder(), 97, 1, ql, matType);
/* 493 */                     createMultiple(getResponder(), 313, 1, ql, matType);
/* 494 */                     createMultiple(getResponder(), 296, 1, ql, matType);
/* 495 */                     createMultiple(getResponder(), 388, 1, ql, matType);
/* 496 */                     createMultiple(getResponder(), 421, 1, ql, matType);
/*     */                     break;
/*     */                   case 24:
/* 499 */                     itemtemps = ItemTemplateFactory.getInstance().getTemplates();
/* 500 */                     for (ItemTemplate temp : itemtemps) {
/*     */                       
/* 502 */                       if (temp.isCombine() && 
/* 503 */                         !temp.isFood() && temp
/* 504 */                         .getTemplateId() != 683 && temp
/* 505 */                         .getTemplateId() != 737 && (temp
/* 506 */                         .getDecayTime() == 86401L || temp
/* 507 */                         .getDecayTime() == 28800L || temp.destroyOnDecay) && 
/* 508 */                         !temp.getModelName().startsWith("model.resource.scrap."))
/* 509 */                         for (int x = 0; x < 5; x++)
/* 510 */                           createAndInsertItems(getResponder(), temp.getTemplateId(), temp
/* 511 */                               .getTemplateId(), (1 + Server.rand
/* 512 */                               .nextInt(ql)), 0, true, false, (byte)-1);  
/*     */                     } 
/*     */                     break;
/*     */                   case 25:
/* 516 */                     createOnGround(getResponder(), 132, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 26:
/* 519 */                     createOnGround(getResponder(), 492, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 27:
/* 522 */                     createOnGround(getResponder(), 146, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 28:
/* 525 */                     createOnGround(getResponder(), 860, (qty == 0) ? 4 : qty, ql, matType);
/*     */                     break;
/*     */                   case 29:
/* 528 */                     createOnGround(getResponder(), 188, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 30:
/* 531 */                     createOnGround(getResponder(), 217, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 31:
/* 534 */                     createOnGround(getResponder(), 218, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 32:
/* 537 */                     createOnGround(getResponder(), 22, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 33:
/* 540 */                     createOnGround(getResponder(), 23, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 34:
/* 543 */                     createOnGround(getResponder(), 9, (qty == 0) ? 4 : qty, ql, matType);
/*     */                     break;
/*     */                   case 35:
/* 546 */                     createOnGround(getResponder(), 557, (qty == 0) ? 4 : qty, ql, matType);
/*     */                     break;
/*     */                   case 36:
/* 549 */                     createOnGround(getResponder(), 558, (qty == 0) ? 4 : qty, ql, matType);
/*     */                     break;
/*     */                   case 37:
/* 552 */                     createOnGround(getResponder(), 559, (qty == 0) ? 4 : qty, ql, matType);
/*     */                     break;
/*     */                   case 38:
/* 555 */                     createOnGround(getResponder(), 319, (qty == 0) ? 4 : qty, ql, matType);
/*     */                     break;
/*     */                   case 39:
/* 558 */                     createOnGround(getResponder(), 786, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 40:
/* 561 */                     createOnGround(getResponder(), 785, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 41:
/* 564 */                     createOnGround(getResponder(), 26, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 42:
/* 567 */                     createOnGround(getResponder(), 130, (qty == 0) ? 10 : qty, ql, matType);
/*     */                     break;
/*     */                   case 43:
/* 570 */                     createMultiple(getResponder(), 903, 1, ql, matType);
/* 571 */                     createMultiple(getResponder(), 901, 1, ql, matType);
/*     */                     break;
/*     */                   case 44:
/* 574 */                     createMultiple(getResponder(), 711, 1, ql, matType);
/* 575 */                     createMultiple(getResponder(), 213, 4, ql, matType);
/* 576 */                     createMultiple(getResponder(), 439, 8, ql, matType);
/*     */                     break;
/*     */                   case 45:
/* 579 */                     createMultiple(getResponder(), 221, 5, ql, matType);
/* 580 */                     createMultiple(getResponder(), 223, 5, ql, matType);
/* 581 */                     createMultiple(getResponder(), 480, 1, ql, matType);
/* 582 */                     createMultiple(getResponder(), 23, 3, ql, matType);
/* 583 */                     createMultiple(getResponder(), 64, 1, ql, matType);
/*     */                     break;
/*     */                   
/*     */                   case 46:
/* 587 */                     if (matType != 8 || matType != 7) {
/* 588 */                       matType = 8;
/*     */                     }
/* 590 */                     createMultiple(getResponder(), 505, 1, ql, matType);
/* 591 */                     createMultiple(getResponder(), 507, 1, ql, matType);
/* 592 */                     createMultiple(getResponder(), 508, 1, ql, matType);
/* 593 */                     createMultiple(getResponder(), 506, 1, ql, matType);
/*     */                     break;
/*     */                   case 47:
/* 596 */                     createMultiple(getResponder(), 376, 1, ql, matType);
/* 597 */                     createMultiple(getResponder(), 374, 1, ql, matType);
/* 598 */                     createMultiple(getResponder(), 380, 1, ql, matType);
/* 599 */                     createMultiple(getResponder(), 382, 1, ql, matType);
/* 600 */                     createMultiple(getResponder(), 378, 1, ql, matType);
/*     */                     break;
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 607 */               } catch (NumberFormatException nfs) {
/*     */                 
/* 609 */                 getResponder().getCommunicator().sendNormalServerMessage("Error: input was " + itemtype + " - failed to parse.");
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 614 */               getResponder().getCommunicator().sendNormalServerMessage("No quality level selected so not creating.");
/*     */             }
/*     */           
/* 617 */           } catch (NumberFormatException nfs) {
/*     */             
/* 619 */             getResponder().getCommunicator().sendNormalServerMessage("Error: input was " + itemtype + " - failed to parse.");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createOnGround(Creature receiver, int itemTemplate, int howMany, float qualityLevel, byte materialType) {
/* 631 */     for (int x = 0; x < howMany; x++) {
/* 632 */       createAndInsertItems(receiver, itemTemplate, itemTemplate, qualityLevel, false, materialType);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void createMultiple(Creature receiver, int itemTemplate, int howMany, float qualityLevel, byte materialType) {
/* 638 */     for (int x = 0; x < howMany; x++) {
/* 639 */       createAndInsertItems(receiver, itemTemplate, itemTemplate, qualityLevel, false, materialType);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void createAndInsertItems(Creature receiver, int itemStart, int itemEnd, float qualityLevel, boolean newbieItem, byte materialType) {
/* 645 */     createAndInsertItems(receiver, itemStart, itemEnd, qualityLevel, 0, false, newbieItem, materialType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void createAndInsertItems(Creature receiver, int itemStart, int itemEnd, float qualityLevel, int color, boolean newbieItem) {
/* 651 */     createAndInsertItems(receiver, itemStart, itemEnd, qualityLevel, color, false, newbieItem, (byte)-1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void createAndInsertItems(Creature receiver, int itemStart, int itemEnd, float qualityLevel, int color, boolean onGround, boolean newbieItem, byte material) {
/* 657 */     if (itemStart > itemEnd) {
/*     */       
/* 659 */       receiver.getCommunicator().sendNormalServerMessage("Error: Bugged test case.");
/*     */       return;
/*     */     } 
/* 662 */     for (int x = itemStart; x <= itemEnd; x++) {
/*     */       
/* 664 */       if (x != 110)
/*     */       {
/* 666 */         if (onGround) {
/*     */           
/*     */           try {
/* 669 */             ItemFactory.createItem(x, qualityLevel, receiver.getPosX(), receiver
/* 670 */                 .getPosY(), Server.rand
/* 671 */                 .nextFloat() * 180.0F, receiver.isOnSurface(), (byte)0, -10L, receiver.getName());
/*     */           }
/* 673 */           catch (Exception ex) {
/*     */             
/* 675 */             receiver.getCommunicator().sendAlertServerMessage(ex.getMessage());
/*     */           } 
/*     */         } else {
/*     */           
/*     */           try {
/* 680 */             Item i = ItemFactory.createItem(x, qualityLevel, receiver.getName());
/* 681 */             if (newbieItem) {
/* 682 */               i.setAuxData((byte)1);
/*     */             }
/*     */             
/* 685 */             if (i.isGem()) {
/*     */               
/* 687 */               i.setData1((qualityLevel <= 0.0F) ? 0 : (int)(qualityLevel * 2.0F));
/* 688 */               i.setDescription("v");
/*     */             } 
/*     */             
/* 691 */             if (i.isDragonArmour()) {
/*     */               
/* 693 */               i.setMaterial((byte)16);
/* 694 */               i.setColor(color);
/* 695 */               String dName = i.getDragonColorNameByColor(color);
/* 696 */               if (dName != "")
/* 697 */                 i.setName(dName + " " + i.getName()); 
/*     */             } 
/* 699 */             if (material != -1)
/*     */             {
/* 701 */               i.setMaterial(material);
/*     */             }
/* 703 */             receiver.getInventory().insertItem(i);
/*     */           }
/* 705 */           catch (Exception ex) {
/*     */             
/* 707 */             receiver.getCommunicator().sendAlertServerMessage(ex.getMessage());
/*     */           } 
/*     */         }  } 
/*     */     } 
/* 711 */     receiver.wearItems();
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
/* 722 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/* 724 */     buf.append("text{text='Create an armour set or a weapon and set skills:'}");
/* 725 */     buf.append("harray{label{text='Item'}dropdown{id='itemtype';options=\"");
/* 726 */     buf.append("Nothing,");
/* 727 */     buf.append("Cloth,");
/* 728 */     buf.append("Leather,");
/* 729 */     buf.append("Studded,");
/* 730 */     buf.append("Chain,");
/* 731 */     buf.append("Plate,");
/* 732 */     buf.append("Drake (random color),");
/* 733 */     buf.append("Dragon Scale (random color),");
/* 734 */     buf.append("Shortsword,");
/* 735 */     buf.append("Longsword,");
/* 736 */     buf.append("Twohanded sword,");
/* 737 */     buf.append("Small maul,");
/* 738 */     buf.append("Med maul,");
/* 739 */     buf.append("Large maul,");
/* 740 */     buf.append("Small axe,");
/* 741 */     buf.append("Large axe,");
/* 742 */     buf.append("Twohanded axe,");
/* 743 */     buf.append("Halberd,");
/* 744 */     buf.append("Long spear,");
/* 745 */     buf.append("Steel spear,");
/* 746 */     buf.append("Large Metal Shield,");
/* 747 */     buf.append("Medium Metal Shield,");
/* 748 */     buf.append("Large Wooden Shield,");
/* 749 */     buf.append("Small Wooden Shield,");
/* 750 */     buf.append("Basic Tools,");
/* 751 */     buf.append("Raw materials,");
/* 752 */     buf.append("#10 Stone Bricks,");
/* 753 */     buf.append("#10 Mortar,");
/* 754 */     buf.append("#10 Rock Shards,");
/* 755 */     buf.append("#4 Wood Beams,");
/* 756 */     buf.append("#10 Iron Ribbons,");
/* 757 */     buf.append("#10 Large Nails,");
/* 758 */     buf.append("#10 Small Nails,");
/* 759 */     buf.append("#10 Planks,");
/* 760 */     buf.append("#10 Shafts,");
/* 761 */     buf.append("#4 Logs,");
/* 762 */     buf.append("#4 Thick Ropes,");
/* 763 */     buf.append("#4 Mooring Ropes,");
/* 764 */     buf.append("#4 Cordage Ropes,");
/* 765 */     buf.append("#4 Normal Ropes,");
/* 766 */     buf.append("#10 Marble Bricks,");
/* 767 */     buf.append("#10 Marble Shards,");
/* 768 */     buf.append("#10 Dirt,");
/* 769 */     buf.append("#10 Clay,");
/* 770 */     buf.append("Bridge Tools,");
/* 771 */     buf.append("Make your own RangePole,");
/* 772 */     buf.append("Make your own Dioptra,");
/* 773 */     buf.append("Statuette Set,");
/* 774 */     buf.append("Vesseled Gems Set,");
/* 775 */     buf.append("\";default=\"0\"}}");
/*     */     
/* 777 */     buf.append("text{text='Select material:'}");
/* 778 */     buf.append("harray{label{text='Material'}dropdown{id='materialtype';options=\"");
/* 779 */     buf.append("Standard.,");
/* 780 */     for (byte material : MethodsItems.getAllNormalWoodTypes())
/*     */     {
/* 782 */       buf.append(StringUtilities.raiseFirstLetter(Item.getMaterialString(material)) + ",");
/*     */     }
/* 784 */     for (byte material : MethodsItems.getAllMetalTypes())
/*     */     {
/* 786 */       buf.append(StringUtilities.raiseFirstLetter(Item.getMaterialString(material)) + ",");
/*     */     }
/* 788 */     buf.append("\";default=\"0\"}}");
/*     */     
/* 790 */     buf.append("harray{label{text='Item qualitylevel (Max 90)'};input{maxchars='2'; id='qualitylevel'; text='50'}}");
/* 791 */     buf.append("harray{label{text='Set skills to (Max 90, 0=no change)'};input{maxchars='2'; id='skillLevel'; text='0'}}");
/* 792 */     buf.append("harray{label{text='Set characteristics to (Max 90, 0=no change)'};input{maxchars='2'; id='characteristicsLevel'; text='0'}}");
/* 793 */     buf.append("harray{label{text='Set Alignment to (Max 99, Min -99, 0=no change)'};input{maxchars='3'; id='alignmentLevel'; text='0'}}");
/* 794 */     buf.append("harray{label{text='Item quantity (0..99, 0 = use default)'};input{maxchars='2'; id='quantity'; text='0'}}");
/* 795 */     buf.append("text{text='Quantity is only used for items with a # before their name, if 0 then the default number after the # is used.'};");
/* 796 */     buf.append("text{text='Set Deity:'}");
/* 797 */     buf.append("harray{label{text='Deity'}dropdown{id='priestType';options=\"");
/* 798 */     buf.append("No Change,");
/* 799 */     buf.append("No Deity,");
/* 800 */     for (Deity d : Deities.getDeities())
/*     */     {
/* 802 */       buf.append(d.getName() + ",");
/*     */     }
/* 804 */     buf.append("\";default=\"0\"}}");
/*     */     
/* 806 */     buf.append("harray{label{text='Faith (Max 100, 0=no change)'};input{maxchars='3'; id='faithLevel'; text='0'}}");
/*     */     
/* 808 */     buf.append(createAnswerButton2());
/* 809 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   final int getRandomDragonColor() {
/* 814 */     int c = Server.rand.nextInt(5);
/* 815 */     switch (c) {
/*     */       
/*     */       case 0:
/* 818 */         return WurmColor.createColor(215, 40, 40);
/*     */       case 1:
/* 820 */         return WurmColor.createColor(10, 10, 10);
/*     */       case 2:
/* 822 */         return WurmColor.createColor(10, 210, 10);
/*     */       case 3:
/* 824 */         return WurmColor.createColor(255, 255, 255);
/*     */       case 4:
/* 826 */         return WurmColor.createColor(40, 40, 215);
/*     */     } 
/* 828 */     return WurmColor.createColor(100, 100, 100);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TestQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */