/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.behaviours.CargoTransportationMethods;
/*     */ import com.wurmonline.server.behaviours.CreatureBehaviour;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ class ChickenCoops
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*  23 */   private static final Logger logger = Logger.getLogger(ChickenCoops.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int cretCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void poll(Item theItem) {
/*  34 */     getCreatureCountAndContinue(theItem);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getCreatureCountAndContinue(Item theItem) {
/*  45 */     if (theItem.getTemplateId() == 1436) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/*  50 */         long delay = System.currentTimeMillis() - 3600000L;
/*  51 */         if (delay > theItem.getParent().getData())
/*     */         {
/*     */           
/*  54 */           if (theItem.getParent().getDamage() >= 80.0F)
/*     */           {
/*  56 */             for (Item item : theItem.getParent().getAllItems(true)) {
/*     */               
/*  58 */               if (item.getTemplateId() == 1436)
/*     */               {
/*  60 */                 for (Item chickens : item.getAllItems(true))
/*     */                 {
/*  62 */                   unload(chickens);
/*     */                 }
/*     */               }
/*     */             } 
/*     */           }
/*  67 */           cretCount = (theItem.getAllItems(true)).length;
/*  68 */           if (cretCount > 0)
/*     */           {
/*     */             
/*  71 */             for (Item item : theItem.getParent().getAllItems(true)) {
/*     */               
/*  73 */               pollFeeder(item);
/*  74 */               pollDrinker(item);
/*  75 */               eggPoller(item);
/*     */             } 
/*     */           }
/*     */           
/*  79 */           theItem.getParent().setData(System.currentTimeMillis());
/*     */         }
/*     */       
/*  82 */       } catch (NoSuchItemException ex) {
/*     */         
/*  84 */         logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
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
/*     */   private static void pollFeeder(Item theItem) {
/*  96 */     if (theItem.getTemplateId() == 1434) {
/*     */       
/*  98 */       long delay = System.currentTimeMillis() - 14400000L;
/*  99 */       if (delay > theItem.getData()) {
/*     */ 
/*     */         
/* 102 */         if (theItem.isEmpty(true) || (theItem.getAllItems(true)).length < cretCount) {
/*     */ 
/*     */           
/*     */           try {
/* 106 */             for (Item item : theItem.getParent().getAllItems(true))
/*     */             {
/* 108 */               if (item.getTemplateId() == 1436)
/*     */               {
/* 110 */                 for (Item chickens : item.getAllItems(true))
/*     */                 {
/* 112 */                   unload(chickens);
/*     */                 }
/*     */               }
/*     */             }
/*     */           
/* 117 */           } catch (NoSuchItemException ex) {
/*     */             
/* 119 */             logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 124 */           Item[] foodEaten = theItem.getAllItems(true);
/* 125 */           for (int x = 0; x < cretCount; x++)
/*     */           {
/* 127 */             Items.destroyItem(foodEaten[x].getWurmId());
/*     */           }
/*     */         } 
/* 130 */         theItem.setData(System.currentTimeMillis());
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
/*     */   private static void pollDrinker(Item theItem) {
/* 142 */     if (theItem.getTemplateId() == 1435) {
/*     */       
/* 144 */       long delay = System.currentTimeMillis() - 14400000L;
/* 145 */       if (delay > theItem.getData()) {
/*     */         
/* 147 */         int cretKG = cretCount * 250;
/*     */         
/* 149 */         for (Item water : theItem.getAllItems(true)) {
/*     */           
/* 151 */           if (theItem.isEmpty(true) || water.getWeightGrams() < cretKG) {
/*     */ 
/*     */             
/*     */             try {
/* 155 */               for (Item item : theItem.getParent().getAllItems(true))
/*     */               {
/* 157 */                 if (item.getTemplateId() == 1436)
/*     */                 {
/* 159 */                   for (Item chickens : item.getAllItems(true))
/*     */                   {
/* 161 */                     unload(chickens);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             
/* 166 */             } catch (NoSuchItemException ex) {
/*     */               
/* 168 */               logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*     */             }
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 174 */             water.setWeight(water.getWeightGrams() - cretKG, true);
/*     */           } 
/*     */         } 
/* 177 */         theItem.setData(System.currentTimeMillis());
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
/*     */   private static void eggPoller(Item theItem) {
/* 189 */     if (theItem.getTemplateId() == 1433) {
/*     */       
/* 191 */       long delay = System.currentTimeMillis() - 43200000L;
/* 192 */       if (delay > theItem.getData()) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 197 */           for (int x = 1; x <= cretCount; x++) {
/*     */             
/* 199 */             if ((theItem.getAllItems(true)).length < 100)
/*     */             {
/* 201 */               Item egg = ItemFactory.createItem(464, theItem.getQualityLevel(), null);
/* 202 */               theItem.insertItem(egg);
/*     */               
/* 204 */               if (Server.rand.nextInt(20) == 0)
/*     */               {
/* 206 */                 egg.setData1(48);
/*     */                 
/* 208 */                 egg.setName("fertile egg");
/*     */               }
/*     */             
/*     */             }
/*     */             else
/*     */             {
/* 214 */               Item[] overflow = theItem.getAllItems(true);
/* 215 */               for (int y = 1; y <= overflow.length - 100; y++)
/*     */               {
/* 217 */                 Items.destroyItem(overflow[y].getWurmId());
/*     */               }
/*     */             }
/*     */           
/*     */           } 
/* 222 */         } catch (FailedException|NoSuchTemplateException ex) {
/*     */           
/* 224 */           logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*     */         } 
/* 226 */         theItem.setData(System.currentTimeMillis());
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
/*     */   private static void unload(Item theItem) {
/*     */     try {
/*     */       int layer;
/* 240 */       Item parent = theItem.getParent();
/* 241 */       Creature creature = Creatures.getInstance().getCreature(theItem.getData());
/*     */       
/* 243 */       if (parent.isOnSurface()) {
/*     */         
/* 245 */         layer = 0;
/*     */       }
/*     */       else {
/*     */         
/* 249 */         layer = -1;
/*     */       } 
/* 251 */       Creatures cstat = Creatures.getInstance();
/* 252 */       creature.getStatus().setDead(false);
/* 253 */       cstat.removeCreature(creature);
/* 254 */       cstat.addCreature(creature, false);
/* 255 */       creature.putInWorld();
/*     */       
/* 257 */       float px = (((int)parent.getParent().getPosX() >> 2) * 4 + 2);
/* 258 */       float py = (((int)parent.getParent().getPosY() >> 2) * 4 + 2);
/* 259 */       CreatureBehaviour.blinkTo(creature, px, py, layer, parent
/* 260 */           .getPosZ(), parent.getBridgeId(), parent.getFloorLevel());
/*     */ 
/*     */       
/* 263 */       Item coop = parent.getParent();
/*     */       
/* 265 */       DbCreatureStatus.setLoaded(0, creature.getWurmId());
/* 266 */       Items.destroyItem(theItem.getWurmId());
/* 267 */       CargoTransportationMethods.updateItemModel(coop);
/*     */     }
/* 269 */     catch (IOException|NoSuchItemException|com.wurmonline.server.creatures.NoSuchCreatureException ex) {
/*     */       
/* 271 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ChickenCoops.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */