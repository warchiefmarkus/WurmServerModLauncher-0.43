/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.MethodsItems;
/*     */ import com.wurmonline.server.behaviours.Terraforming;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
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
/*     */ public class Dirt
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 8;
/*     */   
/*     */   public Dirt() {
/*  50 */     super("Dirt", 453, 10, 20, 50, 40, 0L);
/*  51 */     this.targetTile = true;
/*  52 */     this.targetItem = true;
/*  53 */     this.description = "creates and destroys dirt";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  64 */     if (layer >= 0) {
/*     */ 
/*     */       
/*  67 */       for (int x = -2; x <= 1; x++) {
/*     */         
/*  69 */         for (int y = -2; y <= 1; y++) {
/*     */           
/*  71 */           if (isBlocked(tilex + x, tiley + y, performer))
/*  72 */             return false; 
/*     */         } 
/*     */       } 
/*  75 */       return true;
/*     */     } 
/*  77 */     performer.getCommunicator().sendNormalServerMessage("This spell does not work on rock.", (byte)3);
/*  78 */     return false;
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
/*  89 */     if (!target.isHollow() || (target.getTemplate().hasViewableSubItems() && !target.getTemplate().isContainerWithSubItems())) {
/*     */       
/*  91 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is not a container.", (byte)3);
/*     */       
/*  93 */       return false;
/*     */     } 
/*  95 */     if (target.isBulkContainer()) {
/*     */       
/*  97 */       if (target.getTemplateId() == 661) {
/*     */         
/*  99 */         performer.getCommunicator().sendNormalServerMessage("The spell wont work on the " + target.getName() + ".", (byte)3);
/*     */         
/* 101 */         return false;
/*     */       } 
/* 103 */       long topParent = target.getTopParent();
/* 104 */       if (topParent != -10L && topParent == performer.getInventory().getWurmId()) {
/*     */         
/* 106 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " must not be in your inventory.", (byte)3);
/*     */         
/* 108 */         return false;
/*     */       } 
/*     */     } 
/* 111 */     if (target.isLockable() && target.getLockId() != -10L)
/*     */     {
/* 113 */       if (!MethodsItems.mayUseInventoryOfVehicle(performer, target) && 
/* 114 */         !MethodsItems.hasKeyForContainer(performer, target)) {
/*     */         
/* 116 */         performer.getCommunicator().sendNormalServerMessage("You must be able to open the " + target
/* 117 */             .getName() + " to create dirt inside of it.", (byte)3);
/*     */         
/* 119 */         return false;
/*     */       } 
/*     */     }
/* 122 */     if (target.getTemplateId() == 1028) {
/*     */       
/* 124 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " can only hold ore.", (byte)3);
/*     */       
/* 126 */       return false;
/*     */     } 
/* 128 */     if (target.isTent())
/*     */     {
/* 130 */       if (target.getParentOrNull() != null) {
/*     */         
/* 132 */         performer.getCommunicator().sendNormalServerMessage("You cannot create dirt inside of that.", (byte)3);
/*     */         
/* 134 */         return false;
/*     */       } 
/*     */     }
/* 137 */     if (target.isCrate()) {
/*     */       
/* 139 */       int nums = target.getRemainingCrateSpace();
/* 140 */       if (nums <= 0)
/*     */       {
/* 142 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 143 */             .getName() + " will not be able to contain all that dirt.", (byte)3);
/*     */         
/* 145 */         return false;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 150 */       int sizeLeft = target.getFreeVolume();
/*     */       
/*     */       try {
/* 153 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(26);
/* 154 */         int nums = sizeLeft / template.getVolume();
/* 155 */         if (nums <= 0) {
/*     */           
/* 157 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 158 */               .getName() + " will not be able to contain all that dirt.", (byte)3);
/*     */           
/* 160 */           return false;
/*     */         } 
/*     */         
/* 163 */         if (target.getOwnerId() == performer.getWurmId())
/*     */         {
/* 165 */           if (!performer.canCarry(template.getWeightGrams())) {
/*     */             
/* 167 */             performer.getCommunicator().sendNormalServerMessage("You would not be able to carry all that dirt.", (byte)3);
/*     */             
/* 169 */             return false;
/*     */           } 
/*     */         }
/*     */         
/* 173 */         if (target.isContainerLiquid())
/*     */         {
/* 175 */           for (Item i : target.getAllItems(false)) {
/*     */             
/* 177 */             if (i.isLiquid())
/*     */             {
/* 179 */               performer.getCommunicator().sendNormalServerMessage("That would destroy the liquid.", (byte)3);
/*     */               
/* 181 */               return false;
/*     */             }
/*     */           
/*     */           } 
/*     */         }
/* 186 */       } catch (NoSuchTemplateException nst) {
/*     */         
/* 188 */         return false;
/*     */       } 
/*     */     } 
/* 191 */     return true;
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
/* 202 */     int sizeLeft = target.getFreeVolume();
/*     */     
/*     */     try {
/* 205 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(26);
/*     */       
/* 207 */       boolean created = false;
/*     */       
/* 209 */       int nums = Math.min(6, sizeLeft / template.getVolume());
/* 210 */       if (target.isCrate()) {
/* 211 */         nums = Math.min(6, target.getRemainingCrateSpace());
/*     */       }
/* 213 */       if (nums > 0)
/*     */       {
/* 215 */         if (target.isBulkContainer()) {
/*     */ 
/*     */           
/* 218 */           Item dirt = ItemFactory.createItem(26, (float)power, performer.getName());
/*     */           
/* 220 */           dirt.setWeight(template.getWeightGrams() * nums, true);
/* 221 */           dirt.setMaterial(template.getMaterial());
/* 222 */           if (target.isCrate()) {
/* 223 */             dirt.AddBulkItemToCrate(performer, target);
/*     */           } else {
/* 225 */             dirt.AddBulkItem(performer, target);
/*     */           } 
/* 227 */           created = true;
/*     */         }
/*     */         else {
/*     */           
/* 231 */           for (int x = 0; x < nums; x++) {
/*     */             
/* 233 */             if (target.getOwnerId() == performer.getWurmId()) {
/*     */               
/* 235 */               if (!performer.canCarry(template.getWeightGrams())) {
/*     */                 
/* 237 */                 if (created) {
/* 238 */                   performer.getCommunicator().sendNormalServerMessage("You create some dirt.", (byte)2);
/*     */                 }
/*     */                 return;
/*     */               } 
/* 242 */             } else if (!target.mayCreatureInsertItem()) {
/*     */               
/* 244 */               if (created)
/* 245 */                 performer.getCommunicator().sendNormalServerMessage("You create some dirt.", (byte)2); 
/*     */               return;
/*     */             } 
/* 248 */             Item dirt = ItemFactory.createItem(26, (float)power, performer.getName());
/* 249 */             target.insertItem(dirt);
/* 250 */             created = true;
/*     */           } 
/*     */         } 
/*     */       }
/* 254 */       if (created) {
/* 255 */         performer.getCommunicator().sendNormalServerMessage("You create some dirt.", (byte)2);
/*     */       }
/* 257 */     } catch (NoSuchTemplateException noSuchTemplateException) {
/*     */ 
/*     */     
/*     */     }
/* 261 */     catch (FailedException failedException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean isBlocked(int tx, int ty, Creature performer) {
/* 269 */     int otile = Server.surfaceMesh.getTile(tx, ty);
/* 270 */     int diff = Math.abs(Terraforming.getMaxSurfaceDifference(otile, tx, ty));
/* 271 */     if (diff > 270) {
/*     */       
/* 273 */       performer.getCommunicator().sendNormalServerMessage("The slope would crumble.", (byte)3);
/* 274 */       return true;
/*     */     } 
/* 276 */     for (int x = 0; x >= -1; x--) {
/*     */       
/* 278 */       for (int y = 0; y >= -1; y--) {
/*     */ 
/*     */         
/*     */         try {
/* 282 */           int tile = Server.surfaceMesh.getTile(tx + x, ty + y);
/* 283 */           byte type = Tiles.decodeType(tile);
/* 284 */           if (Terraforming.isNonDiggableTile(type)) {
/*     */             
/* 286 */             performer.getCommunicator().sendNormalServerMessage("You need to clear the area first.", (byte)3);
/*     */             
/* 288 */             return true;
/*     */           } 
/* 290 */           if (Terraforming.isRoad(type)) {
/*     */             
/* 292 */             performer.getCommunicator().sendNormalServerMessage("The road is too hard.", (byte)3);
/*     */             
/* 294 */             return true;
/*     */           } 
/* 296 */           if (type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_PEAT.id)
/*     */           {
/* 298 */             return true;
/*     */           }
/* 300 */           if (Tiles.decodeHeight(tile) < -3000) {
/*     */             
/* 302 */             performer.getCommunicator().sendNormalServerMessage("Nothing happens at this depth.", (byte)3);
/*     */             
/* 304 */             return true;
/*     */           } 
/* 306 */           Zone zone = Zones.getZone(tx + x, ty + y, performer.isOnSurface());
/* 307 */           VolaTile vtile = zone.getTileOrNull(tx + x, ty + y);
/* 308 */           if (vtile != null) {
/*     */             
/* 310 */             if (vtile.getStructure() != null) {
/*     */               
/* 312 */               performer.getCommunicator().sendNormalServerMessage("The structure is in the way.", (byte)3);
/*     */               
/* 314 */               return true;
/*     */             } 
/* 316 */             Fence[] fences = vtile.getFencesForLevel(0);
/* 317 */             if (fences.length > 0) {
/*     */               
/* 319 */               if (x == 0 && y == 0) {
/*     */                 
/* 321 */                 performer.getCommunicator().sendNormalServerMessage("The " + fences[0]
/* 322 */                     .getName() + " is in the way.", (byte)3);
/*     */                 
/* 324 */                 return true;
/*     */               } 
/* 326 */               if (x == -1 && y == 0) {
/*     */                 
/* 328 */                 for (Fence f : fences) {
/*     */                   
/* 330 */                   if (f.isHorizontal())
/*     */                   {
/* 332 */                     String wname = f.getName();
/* 333 */                     performer.getCommunicator().sendNormalServerMessage("The " + wname + " is in the way.", (byte)3);
/*     */                     
/* 335 */                     return true;
/*     */                   }
/*     */                 
/*     */                 } 
/* 339 */               } else if (y == -1 && x == 0) {
/*     */                 
/* 341 */                 for (Fence f : fences) {
/*     */                   
/* 343 */                   if (!f.isHorizontal())
/*     */                   {
/* 345 */                     String wname = f.getName();
/* 346 */                     performer.getCommunicator().sendNormalServerMessage("The " + wname + " is in the way.", (byte)3);
/*     */                     
/* 348 */                     return true;
/*     */                   }
/*     */                 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 355 */         } catch (NoSuchZoneException nsz) {
/*     */           
/* 357 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep to dig in.", (byte)3);
/*     */           
/* 359 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 363 */     return false;
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 375 */     for (int x = -2; x <= 1; x++) {
/*     */       
/* 377 */       for (int y = -2; y <= 1; y++) {
/*     */         
/* 379 */         if (!isBlocked(tilex + x, tiley + y, performer)) {
/*     */           
/* 381 */           int tile = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/* 382 */           byte oldType = Tiles.decodeType(tile);
/* 383 */           int rocktile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 384 */           short rockheight = Tiles.decodeHeight(rocktile);
/* 385 */           short mod = 0;
/* 386 */           if (x > -2 && y > -2)
/* 387 */             mod = 3; 
/* 388 */           if (x == 0 && y == 0)
/* 389 */             mod = 5; 
/* 390 */           short newHeight = (short)Math.max(rockheight, Tiles.decodeHeight(tile) - mod);
/* 391 */           byte type = Tiles.Tile.TILE_DIRT.id;
/* 392 */           if (oldType == Tiles.Tile.TILE_SAND.id) {
/* 393 */             type = oldType;
/* 394 */           } else if (oldType == Tiles.Tile.TILE_CLAY.id || oldType == Tiles.Tile.TILE_TAR.id || oldType == Tiles.Tile.TILE_PEAT.id) {
/* 395 */             type = oldType;
/* 396 */           } else if (oldType == Tiles.Tile.TILE_MOSS.id) {
/* 397 */             type = oldType;
/* 398 */           } else if (oldType == Tiles.Tile.TILE_MARSH.id) {
/* 399 */             type = oldType;
/* 400 */           }  if (Terraforming.allCornersAtRockLevel(tilex + x, tiley + y, Server.surfaceMesh))
/* 401 */             type = Tiles.Tile.TILE_ROCK.id; 
/* 402 */           Server.setSurfaceTile(tilex + x, tiley + y, newHeight, type, (byte)0);
/* 403 */           Players.getInstance().sendChangedTile(tilex + x, tiley + y, true, true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Dirt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */