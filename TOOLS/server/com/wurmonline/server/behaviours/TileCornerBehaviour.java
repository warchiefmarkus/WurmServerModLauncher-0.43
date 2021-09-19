/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.math.TilePos;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.highways.HighwayPos;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ final class TileCornerBehaviour
/*     */   extends Behaviour
/*     */   implements MiscConstants
/*     */ {
/*     */   TileCornerBehaviour() {
/*  49 */     super((short)54);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset) {
/*  56 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  57 */     byte type = Tiles.decodeType(tile);
/*  58 */     toReturn.addAll(super.getBehavioursFor(performer, source, tilex, tiley, onSurface, corner, tile, heightOffset));
/*     */     
/*  60 */     if (performer.getPower() >= 4 && source.getTemplateId() == 176 && heightOffset == 0) {
/*  61 */       toReturn.add(Actions.actionEntrys[518]);
/*     */     }
/*  63 */     if (onSurface && source.isDiggingtool() && heightOffset == 0 && type != Tiles.Tile.TILE_ROCK.id)
/*     */     {
/*  65 */       toReturn.add(Actions.actionEntrys[144]);
/*     */     }
/*  67 */     if (source.isMiningtool() && heightOffset == 0 && ((onSurface && type == Tiles.Tile.TILE_ROCK.id) || !onSurface)) {
/*     */       
/*  69 */       toReturn.add(Actions.actionEntrys[145]);
/*     */     }
/*  71 */     else if (source.getTemplateId() == 782 && heightOffset == 0) {
/*     */       
/*  73 */       toReturn.add(Actions.actionEntrys[518]);
/*     */     }
/*  75 */     else if (source.isSign() || source.isStreetLamp()) {
/*     */       
/*  77 */       if (performer.isWithinDistanceTo((tilex << 2), (tiley << 2), 0.0F, 4.0F))
/*     */       {
/*  79 */         toReturn.add(Actions.actionEntrys[176]);
/*     */       }
/*     */     }
/*  82 */     else if (Features.Feature.HIGHWAYS.isEnabled() && source.isRoadMarker()) {
/*     */ 
/*     */       
/*  85 */       if (!Zones.getOrCreateTile(tilex, tiley, onSurface).hasFenceOnCorner(performer.getFloorLevel()))
/*     */       {
/*  87 */         if (performer.isWithinDistanceTo((tilex << 2), (tiley << 2), 0.0F, 4.0F))
/*     */         {
/*  89 */           if (passFloorCheck(tilex, tiley, onSurface, heightOffset)) {
/*     */             
/*  91 */             HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface, heightOffset);
/*  92 */             if (highwayPos != null)
/*     */             {
/*  94 */               if ((MethodsHighways.middleOfHighway(highwayPos) && 
/*  95 */                 !MethodsHighways.containsMarker(highwayPos, (byte)0) && 
/*  96 */                 !MethodsHighways.isNextToACamp(highwayPos)) || performer.getPower() > 1) {
/*     */                 
/*  98 */                 byte pLinks = MethodsHighways.getPossibleLinksFrom(highwayPos, source);
/*  99 */                 if (MethodsHighways.canPlantMarker(null, highwayPos, source, pLinks))
/* 100 */                   toReturn.add(new ActionEntry((short)176, "Plant", "planting")); 
/* 101 */                 toReturn.add(new ActionEntry((short)759, "View possible protected tiles", "viewing"));
/* 102 */                 if (pLinks != 0)
/* 103 */                   toReturn.add(new ActionEntry((short)748, "View possible links", "viewing")); 
/*     */               } 
/*     */             }
/*     */           } 
/*     */         }
/*     */       }
/*     */     } 
/* 110 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset) {
/* 117 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 118 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, corner, tile, heightOffset));
/* 119 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset, short action, float counter) {
/* 126 */     boolean done = true;
/*     */     
/* 128 */     if (action == 1) {
/*     */       
/* 130 */       done = action(act, performer, tilex, tiley, onSurface, corner, tile, heightOffset, action, counter);
/*     */     }
/* 132 */     else if (onSurface && action == 144 && source.isDiggingtool() && heightOffset == 0) {
/*     */       
/* 134 */       done = Terraforming.dig(performer, source, tilex, tiley, tile, counter, true, 
/* 135 */           performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh);
/*     */     }
/* 137 */     else if (action == 145 && source.isMiningtool() && heightOffset == 0) {
/*     */ 
/*     */       
/* 140 */       if (onSurface)
/*     */       {
/* 142 */         done = TileRockBehaviour.mine(act, performer, source, tilex, tiley, action, counter, tilex, tiley);
/*     */       }
/*     */       else
/*     */       {
/* 146 */         TilePos digTilePos = TilePos.fromXY(tilex, tiley);
/* 147 */         done = CaveTileBehaviour.mine(act, performer, source, tilex, tiley, action, counter, 0, digTilePos);
/*     */       }
/*     */     
/* 150 */     } else if (action == 518 && source.getTemplateId() == 782 && heightOffset == 0) {
/*     */ 
/*     */       
/* 153 */       done = CaveTileBehaviour.raiseRockLevel(performer, source, tilex, tiley, counter, act);
/*     */     }
/* 155 */     else if (action == 518 && heightOffset == 0 && performer.getPower() >= 4 && source.getTemplateId() == 176) {
/*     */       
/* 157 */       done = CaveTileBehaviour.raiseRockLevel(performer, source, tilex, tiley, counter, act);
/*     */     }
/* 159 */     else if (action == 176 && (source.isSign() || source.isStreetLamp())) {
/*     */       
/* 161 */       if (performer.getPower() > 0) {
/* 162 */         done = MethodsItems.plantSignFinish(performer, source, true, tilex, tiley, onSurface, -10L, false, -1L);
/*     */       } else {
/* 164 */         done = MethodsItems.plantSign(performer, source, counter, true, tilex, tiley, onSurface, -10L, false, -1L);
/*     */       } 
/* 166 */     } else if (action == 176 && source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled()) {
/*     */       
/* 168 */       if (!Zones.getOrCreateTile(tilex, tiley, onSurface).hasFenceOnCorner(performer.getFloorLevel())) {
/*     */         
/* 170 */         if (passFloorCheck(tilex, tiley, onSurface, heightOffset)) {
/*     */           
/* 172 */           HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface, heightOffset);
/* 173 */           if (highwayPos != null) {
/*     */             
/* 175 */             if (MethodsHighways.middleOfHighway(highwayPos) && 
/* 176 */               !MethodsHighways.containsMarker(highwayPos, (byte)0))
/*     */             {
/* 178 */               if (MethodsHighways.isNextToACamp(highwayPos) && performer.getPower() <= 1) {
/*     */                 
/* 180 */                 performer.getCommunicator().sendNormalServerMessage("That tile corner borders onto a wagoners camp, who does not allow such actions.");
/*     */                 
/* 182 */                 return true;
/*     */               } 
/*     */ 
/*     */               
/* 186 */               byte pLinks = MethodsHighways.getPossibleLinksFrom(highwayPos, source);
/* 187 */               if (!MethodsHighways.canPlantMarker(performer, highwayPos, source, pLinks)) {
/* 188 */                 done = true;
/* 189 */               } else if (performer.getPower() > 0) {
/* 190 */                 done = MethodsItems.plantSignFinish(performer, source, true, highwayPos.getTilex(), highwayPos.getTiley(), highwayPos
/* 191 */                     .isOnSurface(), highwayPos.getBridgeId(), false, -1L);
/*     */               } else {
/* 193 */                 done = MethodsItems.plantSign(performer, source, counter, true, highwayPos.getTilex(), highwayPos.getTiley(), highwayPos
/* 194 */                     .isOnSurface(), highwayPos.getBridgeId(), false, -1L);
/* 195 */               }  if (done && source.isPlanted())
/*     */               {
/*     */                 
/* 198 */                 MethodsHighways.autoLink(source, pLinks);
/*     */               
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 204 */               performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 205 */               return true;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 210 */             performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 211 */             return true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 216 */           performer.getCommunicator().sendNormalServerMessage("There is a floor in the way.");
/* 217 */           return true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 222 */         performer.getCommunicator().sendNormalServerMessage("There is a fence in the way.");
/* 223 */         return true;
/*     */       }
/*     */     
/* 226 */     } else if (action == 748 && source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled()) {
/*     */       
/* 228 */       if (!Zones.getOrCreateTile(tilex, tiley, onSurface).hasFenceOnCorner(performer.getFloorLevel())) {
/*     */         
/* 230 */         HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface, heightOffset);
/* 231 */         if (highwayPos != null) {
/*     */           
/* 233 */           if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0))
/*     */           {
/* 235 */             done = MarkerBehaviour.showLinks(performer, source, act, counter, highwayPos);
/*     */           }
/*     */           else
/*     */           {
/* 239 */             performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 240 */             return true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 245 */           performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 246 */           return true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 251 */         performer.getCommunicator().sendNormalServerMessage("There is a fence in the way.");
/* 252 */         return true;
/*     */       }
/*     */     
/* 255 */     } else if (action == 759 && source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled()) {
/*     */       
/* 257 */       HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface, heightOffset);
/* 258 */       if (highwayPos != null) {
/*     */         
/* 260 */         if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0))
/*     */         {
/* 262 */           done = MarkerBehaviour.showProtection(performer, source, act, counter, highwayPos);
/*     */         }
/*     */         else
/*     */         {
/* 266 */           performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 267 */           return true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 272 */         performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 273 */         return true;
/*     */       } 
/*     */     } else {
/*     */       
/* 277 */       return action(act, performer, tilex, tiley, onSurface, corner, tile, heightOffset, action, counter);
/* 278 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset, short action, float counter) {
/* 285 */     if (action == 1) {
/*     */       
/* 287 */       HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/* 288 */       if (highwayPos != null && MethodsHighways.middleOfHighway(highwayPos)) {
/* 289 */         performer.getCommunicator().sendNormalServerMessage("This outlines where signs and road markers could be planted.");
/*     */       } else {
/* 291 */         performer.getCommunicator().sendNormalServerMessage("This outlines where signs can be planted.");
/*     */       } 
/* 293 */       if (performer.getPower() > 2) {
/*     */         
/* 295 */         int meshtile = Server.surfaceMesh.getTile(tilex, tiley);
/*     */         
/* 297 */         short tileHeight = Tiles.decodeHeight(meshtile);
/* 298 */         int rockTile = Server.rockMesh.getTile(tilex, tiley);
/* 299 */         short rockHeight = Tiles.decodeHeight(rockTile);
/* 300 */         performer.getCommunicator().sendNormalServerMessage("Height Surface:" + tileHeight + " Rock:" + rockHeight + ".");
/*     */         
/* 302 */         int currtile = Server.caveMesh.getTile(tilex, tiley);
/* 303 */         short currHeight = Tiles.decodeHeight(currtile);
/* 304 */         short cceil = (short)(Tiles.decodeData(currtile) & 0xFF);
/* 305 */         if (currHeight == -100) {
/* 306 */           performer.getCommunicator().sendNormalServerMessage("No cave.");
/*     */         } else {
/* 308 */           performer.getCommunicator().sendNormalServerMessage("Height Cave Floor:" + currHeight + " Ceiling:" + (currHeight + cceil) + ".");
/*     */         } 
/* 310 */       }  if (tilex - 2 < 0 || tilex + 2 > 1 << Constants.meshSize || tiley - 2 < 0 || tiley + 2 > 1 << Constants.meshSize) {
/*     */ 
/*     */         
/* 313 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to measure.");
/* 314 */         return true;
/*     */       } 
/*     */     } 
/* 317 */     return true;
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
/*     */   private static boolean passFloorCheck(int tilex, int tiley, boolean onSurface, int heightOffset) {
/* 332 */     if (heightOffset != 0)
/* 333 */       return true; 
/* 334 */     Floor[] floors = null;
/* 335 */     Structure structure = Structures.getStructureForTile(tilex, tiley, onSurface);
/* 336 */     if (structure != null && structure.isTypeHouse())
/* 337 */       floors = Zones.getFloorsAtTile(tilex, tiley, 0, 0, onSurface); 
/* 338 */     if (structure == null || !structure.isTypeHouse() || floors == null)
/* 339 */       structure = Structures.getStructureForTile(tilex - 1, tiley, onSurface); 
/* 340 */     if (structure != null && structure.isTypeHouse() && floors == null)
/* 341 */       floors = Zones.getFloorsAtTile(tilex - 1, tiley, 0, 0, onSurface); 
/* 342 */     if (structure == null || !structure.isTypeHouse() || floors == null)
/* 343 */       structure = Structures.getStructureForTile(tilex - 1, tiley - 1, onSurface); 
/* 344 */     if (structure != null && structure.isTypeHouse() && floors == null)
/* 345 */       floors = Zones.getFloorsAtTile(tilex - 1, tiley - 1, 0, 0, onSurface); 
/* 346 */     if (structure == null || !structure.isTypeHouse() || floors == null)
/* 347 */       structure = Structures.getStructureForTile(tilex, tiley - 1, onSurface); 
/* 348 */     if (structure != null && structure.isTypeHouse() && floors == null) {
/* 349 */       floors = Zones.getFloorsAtTile(tilex, tiley - 1, 0, 0, onSurface);
/*     */     }
/* 351 */     return (structure == null || !structure.isTypeHouse() || floors == null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileCornerBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */