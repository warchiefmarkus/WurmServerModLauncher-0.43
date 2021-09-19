/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.structures.Wall;
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
/*     */ 
/*     */ 
/*     */ public class RitualSun
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public RitualSun() {
/*  50 */     super("Ritual of the Sun", 401, 100, 300, 60, 50, 43200000L);
/*  51 */     this.isRitual = true;
/*  52 */     this.targetItem = true;
/*  53 */     this.description = "damage in your gods domains is removed";
/*  54 */     this.type = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  65 */     if (performer.getDeity() != null) {
/*     */       
/*  67 */       Deity deity = performer.getDeity();
/*  68 */       Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*  69 */       if (templateDeity.getFavor() < 100000 && !Servers.isThisATestServer()) {
/*     */         
/*  71 */         performer.getCommunicator().sendNormalServerMessage(deity
/*  72 */             .getName() + " can not grant that power right now.", (byte)3);
/*     */         
/*  74 */         return false;
/*     */       } 
/*  76 */       if (target.getBless() == deity)
/*     */       {
/*  78 */         if (target.isDomainItem())
/*     */         {
/*  80 */           return true;
/*     */         }
/*     */       }
/*  83 */       performer.getCommunicator().sendNormalServerMessage(
/*  84 */           String.format("You need to cast this spell at an altar of %s.", new Object[] { deity.getName() }), (byte)3);
/*     */     } 
/*     */     
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*  98 */     Deity deity = performer.getDeity();
/*  99 */     Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*     */     
/* 101 */     performer.getCommunicator().sendNormalServerMessage(performer.getDeity().getName() + " increases protection in the lands by mending the broken!");
/* 102 */     Server.getInstance().broadCastSafe("As the Ritual of the Sun is completed, followers of " + deity.getName() + " may now receive a blessing!");
/* 103 */     HistoryManager.addHistory(performer.getName(), "casts " + this.name + ". " + performer.getDeity().getName() + " mends protections in the lands.");
/* 104 */     templateDeity.setFavor(templateDeity.getFavor() - 100000);
/*     */     
/* 106 */     performer.achievement(635);
/* 107 */     for (Creature c : performer.getLinks()) {
/* 108 */       c.achievement(635);
/*     */     }
/*     */     
/* 111 */     new RiteEvent.RiteOfTheSunEvent(-10, performer.getWurmId(), getNumber(), deity.getNumber(), System.currentTimeMillis(), 86400000L);
/*     */     
/* 113 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*     */       
/* 115 */       for (FaithZone f : Zones.getFaithZones()) {
/*     */         
/* 117 */         if (f != null && f.getCurrentRuler() != null && f.getCurrentRuler().getTemplateDeity() == deity.getTemplateDeity())
/*     */           
/*     */           try {
/*     */ 
/*     */             
/* 122 */             if (Zones.getFaithZone(f.getCenterX(), f.getCenterY(), true) == f)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 132 */               for (int tx = f.getStartX(); tx < f.getEndX(); tx++) {
/* 133 */                 for (int ty = f.getStartY(); ty < f.getEndY(); ty++)
/* 134 */                   effectTile(tx, ty); 
/*     */               }  } 
/*     */           } catch (NoSuchZoneException e) {} 
/*     */       } 
/*     */     } else {
/* 139 */       FaithZone[][] surfaceZones = Zones.getFaithZones(true);
/* 140 */       for (int x = 0; x < Zones.faithSizeX; x++) {
/* 141 */         for (int y = 0; y < Zones.faithSizeY; y++) {
/* 142 */           if (surfaceZones[x][y].getCurrentRuler().getTemplateDeity() == deity.getTemplateDeity())
/* 143 */             for (int tx = surfaceZones[x][y].getStartX(); tx <= surfaceZones[x][y].getEndX(); tx++) {
/* 144 */               for (int ty = surfaceZones[x][y].getStartY(); ty <= surfaceZones[x][y].getEndY(); ty++)
/* 145 */                 effectTile(tx, ty); 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private void effectTile(int tx, int ty) {
/* 151 */     VolaTile t = Zones.getTileOrNull(tx, ty, true);
/* 152 */     if (t != null) {
/*     */       
/* 154 */       Wall[] walls = t.getWalls();
/* 155 */       for (Wall lWall : walls)
/*     */       {
/* 157 */         lWall.setDamage(0.0F);
/*     */       }
/* 159 */       for (Floor floor : t.getFloors())
/* 160 */         floor.setDamage(0.0F); 
/* 161 */       for (Fence fence : t.getFences())
/* 162 */         fence.setDamage(0.0F); 
/* 163 */       for (BridgePart bp : t.getBridgeParts())
/* 164 */         bp.setDamage(0.0F); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RitualSun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */