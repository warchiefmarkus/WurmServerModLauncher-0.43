/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.FaithZone;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
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
/*     */ 
/*     */ public class HolyCrop
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   HolyCrop() {
/*  45 */     super("Holy Crop", 400, 100, 300, 60, 50, 7200000L);
/*  46 */     this.isRitual = true;
/*  47 */     this.targetItem = true;
/*  48 */     this.description = "crop and animal blessings";
/*  49 */     this.type = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  55 */     if (performer.getDeity() != null) {
/*     */       
/*  57 */       Deity deity = performer.getDeity();
/*  58 */       Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*  59 */       if (templateDeity.getFavor() < 100000 && !Servers.isThisATestServer()) {
/*     */         
/*  61 */         performer.getCommunicator().sendNormalServerMessage(deity
/*  62 */             .getName() + " can not grant that power right now.", (byte)3);
/*     */         
/*  64 */         return false;
/*     */       } 
/*  66 */       if (target.getBless() == deity)
/*     */       {
/*  68 */         if (target.isDomainItem())
/*     */         {
/*  70 */           return true;
/*     */         }
/*     */       }
/*  73 */       performer.getCommunicator().sendNormalServerMessage(
/*  74 */           String.format("You need to cast this spell at an altar of %s.", new Object[] { deity.getName() }), (byte)3);
/*     */     } 
/*     */     
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*  83 */     Deity deity = performer.getDeity();
/*  84 */     Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*     */     
/*  86 */     performer.getCommunicator().sendNormalServerMessage(performer.getDeity().getName() + " graces the lands with abundant crop yield and happy animals!", (byte)2);
/*     */     
/*  88 */     Server.getInstance().broadCastSafe("As the Holy Crop ritual is completed, followers of " + deity.getName() + " may now receive a blessing!");
/*  89 */     HistoryManager.addHistory(performer.getName(), "casts " + this.name + ". " + performer.getDeity().getName() + " graces the lands with abundant crop yield and happy animals.");
/*  90 */     templateDeity.setFavor(templateDeity.getFavor() - 100000);
/*     */     
/*  92 */     performer.achievement(635);
/*  93 */     for (Creature c : performer.getLinks()) {
/*  94 */       c.achievement(635);
/*     */     }
/*     */     
/*  97 */     new RiteEvent.RiteOfCropEvent(-10, performer.getWurmId(), getNumber(), deity.getNumber(), System.currentTimeMillis(), 86400000L);
/*     */     
/*  99 */     int pow = 100 + Math.max(20, (int)power * 3);
/* 100 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*     */       
/* 102 */       for (FaithZone f : Zones.getFaithZones()) {
/*     */         
/* 104 */         if (f != null && f.getCurrentRuler().getTemplateDeity() == deity.getTemplateDeity())
/*     */           
/*     */           try {
/*     */             
/* 108 */             if (Zones.getFaithZone(f.getCenterX(), f.getCenterY(), true) == f)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 114 */               for (int tx = f.getStartX(); tx < f.getEndX(); tx++) {
/* 115 */                 for (int ty = f.getStartY(); ty < f.getEndY(); ty++)
/* 116 */                   effectTile(tx, ty, pow); 
/*     */               }  } 
/*     */           } catch (NoSuchZoneException e) {} 
/*     */       } 
/*     */     } else {
/* 121 */       FaithZone[][] surfaceZones = Zones.getFaithZones(true);
/*     */       
/* 123 */       for (int x = 0; x < Zones.faithSizeX; x++) {
/* 124 */         for (int y = 0; y < Zones.faithSizeY; y++) {
/* 125 */           if (surfaceZones[x][y].getCurrentRuler().getTemplateDeity() == deity.getTemplateDeity())
/* 126 */             for (int tx = surfaceZones[x][y].getStartX(); tx < surfaceZones[x][y].getEndX(); tx++) {
/* 127 */               for (int ty = surfaceZones[x][y].getStartY(); ty < surfaceZones[x][y].getEndY(); ty++)
/* 128 */                 effectTile(tx, ty, pow); 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private void effectTile(int tx, int ty, int pow) {
/* 134 */     int tile = Server.surfaceMesh.getTile(tx, ty);
/* 135 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_FIELD.id) {
/*     */       
/* 137 */       int worldResource = Server.getWorldResource(tx, ty);
/* 138 */       int farmedCount = worldResource >>> 11;
/* 139 */       int farmedChance = worldResource & 0x7FF;
/* 140 */       if (farmedCount < 5)
/* 141 */         farmedCount++; 
/* 142 */       farmedChance = Math.min(farmedChance + pow, 2047);
/* 143 */       Server.setWorldResource(tx, ty, (farmedCount << 11) + farmedChance);
/*     */     } 
/* 145 */     VolaTile t = Zones.getTileOrNull(tx, ty, true);
/* 146 */     if (t != null) {
/*     */       
/* 148 */       Creature[] crets = t.getCreatures();
/* 149 */       for (Creature lCret : crets) {
/*     */         
/* 151 */         if (lCret.getLoyalty() > 0.0F) {
/*     */           
/* 153 */           lCret.setLoyalty(99.0F);
/* 154 */           lCret.getStatus().modifyHunger(0, 80.0F);
/*     */         }
/* 156 */         else if (lCret.isDomestic()) {
/*     */           
/* 158 */           lCret.getStatus().modifyHunger(0, 80.0F);
/*     */         } 
/* 160 */         lCret.removeRandomNegativeTrait();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\HolyCrop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */