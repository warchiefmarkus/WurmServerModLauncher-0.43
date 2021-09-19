/*     */ package com.wurmonline.server.creatures.ai.scripts;
/*     */ 
/*     */ import com.wurmonline.math.Vector2f;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.awt.Point;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.HashSet;
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
/*     */ public class UtilitiesAOE
/*     */ {
/*     */   public static HashSet<Creature> getLineAreaCreatures(Creature c, float distance, float width) {
/*  29 */     Rectangle2D.Float r = new Rectangle2D.Float(c.getPosX() - width / 2.0F, c.getPosY(), width, distance);
/*  30 */     AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(Creature.normalizeAngle(c.getStatus().getRotation() - 180.0F)), c.getPosX(), c.getPosY());
/*  31 */     Shape rotatedArea = at.createTransformedShape(r);
/*     */     
/*  33 */     HashSet<Creature> creatureList = new HashSet<>();
/*     */ 
/*     */     
/*  36 */     for (int i = c.getTileX() - (int)(distance / 4.0F) + 1; i < c.getTileX() + (int)(distance / 4.0F) + 1; i++) {
/*     */       
/*  38 */       for (int j = c.getTileY() - (int)(distance / 4.0F) + 1; j < c.getTileY() + (int)(distance / 4.0F) + 1; j++) {
/*     */         
/*  40 */         VolaTile v = Zones.getTileOrNull(i, j, c.isOnSurface());
/*  41 */         if (v != null)
/*     */         {
/*     */           
/*  44 */           for (Creature target : v.getCreatures()) {
/*     */             
/*  46 */             if (rotatedArea.contains(target.getPosX(), target.getPosY())) {
/*  47 */               creatureList.add(target);
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*  53 */     return creatureList;
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
/*     */   public static HashSet<Point> getLineArea(Creature c, float distance, float width) {
/*  67 */     Rectangle2D.Float r = new Rectangle2D.Float(c.getPosX() - width / 2.0F, c.getPosY(), width, distance);
/*  68 */     AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(Creature.normalizeAngle(c.getStatus().getRotation() - 180.0F)), c.getPosX(), c.getPosY());
/*  69 */     Shape rotatedArea = at.createTransformedShape(r);
/*     */     
/*  71 */     HashSet<Point> tileList = new HashSet<>();
/*     */ 
/*     */     
/*  74 */     for (int i = c.getTileX() - (int)(distance / 4.0F) + 1; i < c.getTileX() + (int)(distance / 4.0F) + 1; i++) {
/*     */       
/*  76 */       for (int j = c.getTileY() - (int)(distance / 4.0F) + 1; j < c.getTileY() + (int)(distance / 4.0F) + 1; j++) {
/*     */         
/*  78 */         if (rotatedArea.contains((i * 4 + 2), (j * 4 + 2)))
/*     */         {
/*  80 */           tileList.add(new Point(i, j));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     return tileList;
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
/*     */   public static HashSet<Creature> getRadialAreaCreatures(Creature c, float radius) {
/*  98 */     HashSet<Creature> creatureList = new HashSet<>();
/*     */     
/* 100 */     int tileRadius = (int)(radius / 4.0F);
/*     */     
/* 102 */     for (int i = c.getTileX() - tileRadius + 1; i < c.getTileX() + tileRadius + 1; i++) {
/*     */       
/* 104 */       for (int j = c.getTileY() - tileRadius + 1; j < c.getTileY() + tileRadius + 1; j++) {
/*     */         
/* 106 */         VolaTile v = Zones.getTileOrNull(i, j, c.isOnSurface());
/* 107 */         if (v != null)
/*     */         {
/*     */           
/* 110 */           for (Creature target : v.getCreatures()) {
/*     */             
/* 112 */             if ((target.getPosX() - c.getPosX()) * (target.getPosX() - c.getPosX()) + (target.getPosY() - c.getPosY()) * (target
/* 113 */               .getPosY() - c.getPosY()) < radius * radius) {
/* 114 */               creatureList.add(target);
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 120 */     return creatureList;
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
/*     */   public static HashSet<Point> getRadialArea(Creature c, int radius) {
/* 133 */     HashSet<Point> tileList = new HashSet<>();
/*     */ 
/*     */     
/* 136 */     for (int i = c.getTileX() - radius + 1; i < c.getTileX() + radius + 1; i++) {
/*     */       
/* 138 */       for (int j = c.getTileY() - radius + 1; j < c.getTileY() + radius + 1; j++) {
/*     */ 
/*     */         
/* 141 */         if ((i - c.getTileX()) * (i - c.getTileX()) + (j - c.getTileY()) * (j - c.getTileY()) < radius * radius)
/*     */         {
/*     */           
/* 144 */           tileList.add(new Point(i, j));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     return tileList;
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
/*     */   
/*     */   public static HashSet<Creature> getConeAreaCreatures(Creature c, float coneDistance, int coneAngle) {
/* 164 */     float attAngle = Creature.normalizeAngle(c.getStatus().getRotation() - 90.0F);
/* 165 */     Vector2f creaturePoint = new Vector2f(c.getPosX(), c.getPosY());
/* 166 */     Vector2f testPoint = new Vector2f();
/*     */     
/* 168 */     HashSet<Creature> creatureList = new HashSet<>();
/*     */     
/* 170 */     int coneDistTiles = (int)(coneDistance / 4.0F);
/*     */     
/* 172 */     for (int i = c.getTileX() - coneDistTiles + 1; i < c.getTileX() + coneDistTiles + 1; i++) {
/*     */       
/* 174 */       for (int j = c.getTileY() - coneDistTiles + 1; j < c.getTileY() + coneDistTiles + 1; j++) {
/*     */         
/* 176 */         VolaTile v = Zones.getTileOrNull(i, j, c.isOnSurface());
/* 177 */         if (v != null)
/*     */         {
/*     */           
/* 180 */           for (Creature target : v.getCreatures()) {
/*     */             
/* 182 */             if ((target.getPosX() - c.getPosX()) * (target.getPosX() - c.getPosX()) + (target.getPosY() - c.getPosY()) * (target
/* 183 */               .getPosY() - c.getPosY()) < coneDistance * coneDistance) {
/*     */ 
/*     */               
/* 186 */               testPoint.set(target.getPosX(), target.getPosY());
/* 187 */               if (Math.abs(getAngleDiff(creaturePoint, testPoint) - attAngle) < (coneAngle / 2))
/*     */               {
/*     */                 
/* 190 */                 creatureList.add(target);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     return creatureList;
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
/*     */   
/*     */   public static HashSet<Point> getConeArea(Creature c, int coneDistance, int coneAngle) {
/* 213 */     float attAngle = Creature.normalizeAngle(c.getStatus().getRotation() - 90.0F);
/* 214 */     Vector2f creaturePoint = new Vector2f(c.getPosX(), c.getPosY());
/* 215 */     Vector2f testPoint = new Vector2f();
/*     */     
/* 217 */     HashSet<Point> tileList = new HashSet<>();
/*     */ 
/*     */     
/* 220 */     for (int i = c.getTileX() - coneDistance + 1; i < c.getTileX() + coneDistance + 1; i++) {
/*     */       
/* 222 */       for (int j = c.getTileY() - coneDistance + 1; j < c.getTileY() + coneDistance + 1; j++) {
/*     */ 
/*     */         
/* 225 */         if ((i - c.getTileX()) * (i - c.getTileX()) + (j - c.getTileY()) * (j - c.getTileY()) < coneDistance * coneDistance) {
/*     */ 
/*     */           
/* 228 */           testPoint.set((i * 4 + 2), (j * 4 + 2));
/* 229 */           if (Math.abs(getAngleDiff(creaturePoint, testPoint) - attAngle) < (coneAngle / 2))
/*     */           {
/*     */             
/* 232 */             tileList.add(new Point(i, j));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     return tileList;
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
/*     */   public static Vector2f getPointInFrontOf(Creature c, float distance) {
/* 251 */     float attAngle = (float)Math.toRadians(Creature.normalizeAngle(c.getStatus().getRotation() - 90.0F));
/* 252 */     Vector2f toReturn = new Vector2f((float)Math.cos(attAngle) * distance, (float)Math.sin(attAngle) * distance);
/* 253 */     toReturn = toReturn.add(new Vector2f(c.getPosX(), c.getPosY()));
/*     */     
/* 255 */     return toReturn;
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
/*     */   private static float getAngleDiff(Vector2f from, Vector2f to) {
/* 268 */     float angle = (float)Math.toDegrees(Math.atan2((to.y - from.y), (to.x - from.x)));
/* 269 */     if (angle < 0.0F) {
/* 270 */       angle += 360.0F;
/*     */     }
/* 272 */     return angle;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\scripts\UtilitiesAOE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */