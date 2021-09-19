/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public class LandOfTheDead
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 50;
/*     */   
/*     */   public LandOfTheDead() {
/*  40 */     super("Land of the Dead", 435, 60, 300, 70, 70, 259200000L);
/*  41 */     this.targetItem = true;
/*  42 */     this.targetTile = true;
/*  43 */     this.description = "summons the souls of the deceased";
/*  44 */     this.type = 0;
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
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  56 */     if (!Servers.isThisAPvpServer()) {
/*     */       
/*  58 */       performer.getCommunicator().sendNormalServerMessage("Libila cannot grant that power right now.");
/*  59 */       return false;
/*     */     } 
/*  61 */     return true;
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
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  73 */     if (!Servers.isThisAPvpServer()) {
/*     */       
/*  75 */       performer.getCommunicator().sendNormalServerMessage("Libila cannot grant that power right now.");
/*  76 */       return false;
/*     */     } 
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  85 */     castLandOfTheDead(performer, performer.getTileX(), performer.getTileY(), Math.max(10.0D, power));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*  91 */     castLandOfTheDead(performer, performer.getTileX(), performer.getTileY(), Math.max(10.0D, power));
/*     */   }
/*     */ 
/*     */   
/*     */   private void castLandOfTheDead(Creature performer, int startx, int starty, double power) {
/*  96 */     performer.getCommunicator().sendNormalServerMessage("You call back souls of the dead! Possess the shells! RISE! RISE!", (byte)2);
/*     */     
/*  98 */     Server.getInstance().broadCastAction(performer.getName() + " commands the souls of the dead to return and possess the shells of the deceased!", performer, 10);
/*  99 */     Village v = Villages.getVillage(startx, starty, true);
/* 100 */     if (v == null)
/*     */     {
/* 102 */       for (int x = -50; x < 50; x += 5) {
/* 103 */         for (int y = -50; y < 50; y += 5) {
/*     */           
/* 105 */           v = Villages.getVillage(startx + x, starty + y, true);
/* 106 */           if (v != null)
/*     */             break; 
/*     */         } 
/*     */       }  } 
/* 110 */     if (v != null) {
/* 111 */       HistoryManager.addHistory(performer.getName(), "Casts land of the dead near " + v.getName());
/*     */     } else {
/* 113 */       HistoryManager.addHistory(performer.getName(), "Casts land of the dead");
/*     */     } 
/* 115 */     int minx = Zones.safeTileX(startx - (int)(200.0D * power / 100.0D));
/* 116 */     int miny = Zones.safeTileY(starty - (int)(200.0D * power / 100.0D));
/* 117 */     int endx = Zones.safeTileX(startx + (int)(200.0D * power / 100.0D));
/* 118 */     int endy = Zones.safeTileY(starty + (int)(200.0D * power / 100.0D));
/* 119 */     Item[] its = Items.getAllItems();
/* 120 */     int maxCorpses = (int)power;
/* 121 */     for (int itx = 0; itx < its.length; itx++) {
/*     */       
/* 123 */       if (its[itx].getZoneId() > -1)
/*     */       {
/* 125 */         if (its[itx].getTemplateId() == 272) {
/*     */           
/* 127 */           int centerx = its[itx].getTileX();
/* 128 */           int centery = its[itx].getTileY();
/*     */           
/* 130 */           if (centerx < endx && centerx > minx && centery < endy && centery > miny)
/*     */           {
/* 132 */             if (Rebirth.mayRaise(performer, its[itx], false) && maxCorpses > 0) {
/*     */               
/* 134 */               Rebirth.raise(power, performer, its[itx], true);
/* 135 */               maxCorpses--;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LandOfTheDead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */