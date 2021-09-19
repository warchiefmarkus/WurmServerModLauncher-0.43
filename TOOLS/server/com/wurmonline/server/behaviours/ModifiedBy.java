/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
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
/*     */ public enum ModifiedBy
/*     */ {
/*  35 */   NOTHING(0),
/*  36 */   NO_TREES(1),
/*  37 */   NEAR_TREE(2),
/*  38 */   NEAR_BUSH(3),
/*  39 */   NEAR_OAK(4),
/*  40 */   EASTER(5),
/*  41 */   HUNGER(6),
/*  42 */   WOUNDED(7),
/*  43 */   NEAR_WATER(8);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int code;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ModifiedBy(int aCode) {
/*  54 */     this.code = aCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public float chanceModifier(Creature performer, int modifier, int tilex, int tiley) {
/*  59 */     if (this == NOTHING) {
/*  60 */       return 0.0F;
/*     */     }
/*  62 */     if (this == EASTER) {
/*     */       
/*  64 */       if (!performer.isPlayer() || (((Player)performer).isReallyPaying() && WurmCalendar.isEaster() && 
/*  65 */         !((Player)performer).isReimbursed())) {
/*  66 */         return modifier;
/*     */       }
/*  68 */       return 0.0F;
/*     */     } 
/*  70 */     if (this == HUNGER) {
/*     */       
/*  72 */       if (performer.getStatus().getHunger() < 20) {
/*  73 */         return modifier;
/*     */       }
/*  75 */       return 0.0F;
/*     */     } 
/*  77 */     if (this == WOUNDED) {
/*     */       
/*  79 */       if ((performer.getStatus()).damage > 15) {
/*  80 */         return modifier;
/*     */       }
/*  82 */       return 0.0F;
/*     */     } 
/*     */     
/*  85 */     MeshIO mesh = Server.surfaceMesh;
/*     */     
/*  87 */     if (isAModifier(mesh.getTile(tilex, tiley))) {
/*     */       
/*  89 */       if (this == NO_TREES) {
/*  90 */         return 0.0F;
/*     */       }
/*  92 */       return modifier;
/*     */     } 
/*     */     int x;
/*  95 */     for (x = -1; x <= 1; x++) {
/*     */       
/*  97 */       for (int y = -1; y <= 1; y++) {
/*     */         
/*  99 */         if (x == -1 || x == 1 || y == -1 || y == 1)
/*     */         {
/* 101 */           if (isAModifier(mesh.getTile(tilex + x, tiley + y))) {
/*     */             
/* 103 */             if (this == NO_TREES) {
/* 104 */               return 0.0F;
/*     */             }
/* 106 */             return (modifier / 2);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     for (x = -2; x <= 2; x++) {
/*     */       
/* 114 */       for (int y = -2; y <= 2; y++) {
/*     */         
/* 116 */         if (x == -2 || x == 2 || y == -2 || y == 2)
/*     */         {
/* 118 */           if (isAModifier(mesh.getTile(tilex + x, tiley + y))) {
/*     */             
/* 120 */             if (this == NO_TREES) {
/* 121 */               return 0.0F;
/*     */             }
/* 123 */             return (modifier / 3);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     for (x = -5; x <= 5; x++) {
/*     */       
/* 131 */       for (int y = -5; y <= 5; y++) {
/*     */         
/* 133 */         if (x <= -3 || x >= 3 || y <= -3 || y >= 3)
/*     */         {
/* 135 */           if (isAModifier(mesh.getTile(tilex + x, tiley + y))) {
/*     */             
/* 137 */             if (this == NO_TREES) {
/* 138 */               return 0.0F;
/*     */             }
/* 140 */             return (modifier / 4);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (this == NO_TREES) {
/* 147 */       return modifier;
/*     */     }
/* 149 */     return 0.0F;
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
/*     */   private boolean isAModifier(int tile) {
/* 163 */     if (this == NEAR_WATER)
/*     */     {
/* 165 */       return (Tiles.decodeHeight(tile) < 5);
/*     */     }
/* 167 */     byte decodedType = Tiles.decodeType(tile);
/* 168 */     byte decodedData = Tiles.decodeData(tile);
/* 169 */     Tiles.Tile theTile = Tiles.getTile(decodedType);
/*     */     
/* 171 */     if (this == NEAR_OAK) {
/*     */       
/* 173 */       if (theTile.isNormalTree())
/* 174 */         return theTile.isOak(decodedData); 
/*     */     } else {
/* 176 */       if (this == NEAR_TREE || this == NO_TREES)
/*     */       {
/* 178 */         return theTile.isNormalTree();
/*     */       }
/* 180 */       if (this == NEAR_BUSH)
/*     */       {
/* 182 */         return theTile.isNormalBush();
/*     */       }
/*     */     } 
/* 185 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ModifiedBy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */