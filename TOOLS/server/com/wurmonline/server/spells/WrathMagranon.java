/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.MethodsStructure;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.NoSuchStructureException;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ public class WrathMagranon
/*     */   extends DamageSpell
/*     */   implements AttitudeConstants
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   public static final double BASE_DAMAGE = 3000.0D;
/*     */   public static final double DAMAGE_PER_POWER = 60.0D;
/*     */   public static final float BASE_STRUCTURE_DAMAGE = 7.5F;
/*     */   public static final float STRUCTURE_DAMAGE_PER_POWER = 0.15F;
/*     */   public static final int RADIUS = 1;
/*     */   
/*     */   public WrathMagranon() {
/*  61 */     super("Wrath of Magranon", 441, 10, 50, 50, 50, 300000L);
/*  62 */     this.targetTile = true;
/*  63 */     this.offensive = true;
/*  64 */     this.description = "covers an area with exploding power, damaging enemies and walls";
/*  65 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  72 */     performer.getCommunicator().sendNormalServerMessage("You slam down the fist of Magranon, which crushes enemy structures in the area!");
/*     */ 
/*     */     
/*  75 */     int radiusBonus = (int)(power / 80.0D);
/*  76 */     int sx = Zones.safeTileX(tilex - 1 - radiusBonus - performer.getNumLinks());
/*  77 */     int sy = Zones.safeTileY(tiley - 1 - radiusBonus - performer.getNumLinks());
/*  78 */     int ex = Zones.safeTileX(tilex + 1 + radiusBonus + performer.getNumLinks());
/*  79 */     int ey = Zones.safeTileY(tiley + 1 + radiusBonus + performer.getNumLinks());
/*  80 */     float structureDamage = 7.5F + (float)power * 0.15F;
/*  81 */     ArrayList<Fence> damagedFences = new ArrayList<>();
/*  82 */     for (int x = sx; x <= ex; x++) {
/*     */       
/*  84 */       for (int y = sy; y <= ey; y++) {
/*     */         
/*  86 */         VolaTile volaTile = Zones.getTileOrNull(x, y, (layer == 0));
/*     */         
/*  88 */         if (volaTile != null) {
/*     */ 
/*     */ 
/*     */           
/*  92 */           Item ring = Zones.isWithinDuelRing(x, y, (layer >= 0));
/*  93 */           if (ring == null) {
/*     */ 
/*     */ 
/*     */             
/*  97 */             for (Creature lCret : volaTile.getCreatures()) {
/*     */               
/*  99 */               if (!lCret.isInvulnerable() && lCret.getAttitude(performer) == 2) {
/*     */                 
/* 101 */                 lCret.addAttacker(performer);
/*     */                 
/* 103 */                 double damage = calculateDamage(lCret, power, 3000.0D, 60.0D);
/*     */                 
/* 105 */                 lCret.addWoundOfType(performer, (byte)0, 1, true, 1.0F, false, damage, (float)power / 5.0F, 0.0F, false, true);
/*     */               } 
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 111 */             if (Servers.isThisAPvpServer())
/*     */             {
/*     */               
/* 114 */               for (Wall wall : volaTile.getWalls()) {
/*     */ 
/*     */                 
/* 117 */                 if (wall.getType() != StructureTypeEnum.PLAN) {
/*     */                   Structure structure;
/*     */                   
/* 120 */                   boolean dealDam = true;
/*     */ 
/*     */                   
/*     */                   try {
/* 124 */                     structure = Structures.getStructure(wall.getStructureId());
/*     */                   }
/* 126 */                   catch (NoSuchStructureException nss) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 132 */                   int tx = wall.getTileX();
/* 133 */                   int ty = wall.getTileY();
/* 134 */                   Village v = Zones.getVillage(tx, ty, performer.isOnSurface());
/*     */                   
/* 136 */                   if (v != null && 
/* 137 */                     !v.isEnemy(performer) && 
/* 138 */                     !MethodsStructure.mayModifyStructure(performer, structure, wall.getTile(), (short)82))
/*     */                   {
/* 140 */                     dealDam = false;
/*     */                   }
/* 142 */                   if (dealDam) {
/*     */                     
/* 144 */                     float wallql = wall.getCurrentQualityLevel();
/* 145 */                     float damageToDeal = structureDamage * (150.0F - wallql) / 100.0F;
/* 146 */                     wall.setDamage(wall.getDamage() + damageToDeal);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             }
/*     */             
/* 152 */             for (Fence fence : volaTile.getAllFences()) {
/*     */ 
/*     */               
/* 155 */               if (fence.isFinished())
/*     */               {
/*     */ 
/*     */                 
/* 159 */                 if (!damagedFences.contains(fence)) {
/*     */ 
/*     */                   
/* 162 */                   boolean dealDam = true;
/* 163 */                   Village vill = MethodsStructure.getVillageForFence(fence);
/* 164 */                   if (vill != null && !vill.isEnemy(performer))
/*     */                   {
/*     */                     
/* 167 */                     dealDam = false;
/*     */                   }
/*     */                   
/* 170 */                   float mult = 1.0F;
/*     */                   
/* 172 */                   if (performer.getCultist() != null && performer.getCultist().doubleStructDamage()) {
/* 173 */                     mult *= 2.0F;
/*     */                   }
/* 175 */                   if (dealDam)
/*     */                   
/* 177 */                   { float fenceql = fence.getCurrentQualityLevel();
/* 178 */                     float damageToDeal = structureDamage * (150.0F - fenceql) / 100.0F;
/* 179 */                     fence.setDamage(fence.getDamage() + damageToDeal * mult);
/* 180 */                     damagedFences.add(fence); } 
/*     */                 }  } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 186 */     }  VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 187 */     if (t != null)
/*     */     {
/*     */ 
/*     */       
/* 191 */       if (layer == 0)
/* 192 */         Zones.flash(tilex, tiley, false); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WrathMagranon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */