/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.CaveTile;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.highways.HighwayPos;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class StructureBehaviour
/*     */   extends Behaviour
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(WallBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   StructureBehaviour() {
/*  54 */     super((short)6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, boolean border, int heightOffset) {
/*  61 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, tilex, tiley, onSurface, dir, border, heightOffset);
/*  62 */     toReturn.add(Actions.actionEntrys[607]);
/*  63 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, boolean border, int heightOffset) {
/*  70 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target, tilex, tiley, onSurface, dir, border, heightOffset);
/*     */     
/*  72 */     toReturn.add(Actions.actionEntrys[607]);
/*  73 */     boolean hasMarker = hasMarker(tilex, tiley, onSurface, dir, heightOffset);
/*  74 */     if (!MethodsStructure.isCorrectToolForBuilding(performer, target.getTemplateId()))
/*     */     {
/*  76 */       return toReturn;
/*     */     }
/*  78 */     Structure structure = MethodsStructure.getStructureOrNullAtTileBorder(tilex, tiley, dir, onSurface);
/*  79 */     if (structure != null && structure.isActionAllowed(performer, (short)116)) {
/*     */ 
/*     */       
/*  82 */       if (!onSurface) {
/*     */         
/*  84 */         int minHeight = (performer.getFloorLevel() + 1) * 30;
/*  85 */         int nwCorner = Server.caveMesh.getTile(tilex, tiley);
/*  86 */         short nwCeil = (short)CaveTile.decodeCeilingHeight(nwCorner);
/*  87 */         if (nwCeil < minHeight)
/*     */         {
/*     */           
/*  90 */           return toReturn;
/*     */         }
/*     */         
/*  93 */         if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*     */           
/*  95 */           int neCorner = Server.caveMesh.getTile(tilex + 1, tiley);
/*  96 */           short neCeil = (short)CaveTile.decodeCeilingHeight(neCorner);
/*  97 */           if (neCeil < minHeight)
/*     */           {
/*     */             
/* 100 */             return toReturn;
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 105 */           int swCorner = Server.caveMesh.getTile(tilex, tiley + 1);
/* 106 */           short swCeil = (short)CaveTile.decodeCeilingHeight(swCorner);
/* 107 */           if (swCeil < minHeight)
/*     */           {
/*     */             
/* 110 */             return toReturn;
/*     */           }
/*     */         } 
/*     */       } 
/* 114 */       boolean hasArch = false;
/*     */       
/* 116 */       if (!MethodsStructure.doesTileBorderContainWallOrFence(tilex, tiley, heightOffset, dir, onSurface, false)) {
/*     */         
/* 118 */         toReturn.add(new ActionEntry((short)-1, "Plan", "planning"));
/* 119 */         toReturn.add(new ActionEntry((short)(20000 + StructureTypeEnum.SOLID.ordinal()), "Wall", "planning wall", emptyIntArr));
/*     */       } else {
/*     */         
/* 122 */         hasArch = true;
/* 123 */       }  if (!hasMarker) {
/*     */         
/* 125 */         toReturn.add(new ActionEntry((short)-11, "Fence", "Fence options"));
/*     */ 
/*     */         
/* 128 */         List<ActionEntry> iron = new LinkedList<>();
/* 129 */         iron.add(Actions.actionEntrys[611]);
/* 130 */         iron.add(Actions.actionEntrys[477]);
/* 131 */         iron.add(Actions.actionEntrys[479]);
/* 132 */         iron.add(Actions.actionEntrys[521]);
/* 133 */         iron.add(Actions.actionEntrys[545]);
/* 134 */         if (!hasArch)
/*     */         {
/* 136 */           iron.add(Actions.actionEntrys[546]);
/*     */         }
/* 138 */         toReturn.add(new ActionEntry((short)-iron.size(), "Iron", "Fence options"));
/* 139 */         Collections.sort(iron);
/* 140 */         toReturn.addAll(iron);
/*     */ 
/*     */         
/* 143 */         List<ActionEntry> marble = new LinkedList<>();
/* 144 */         marble.add(Actions.actionEntrys[844]);
/* 145 */         marble.add(Actions.actionEntrys[845]);
/* 146 */         marble.add(Actions.actionEntrys[846]);
/* 147 */         marble.add(Actions.actionEntrys[904]);
/* 148 */         marble.add(Actions.actionEntrys[905]);
/* 149 */         marble.add(Actions.actionEntrys[902]);
/* 150 */         if (!hasArch) {
/*     */           
/* 152 */           marble.add(Actions.actionEntrys[900]);
/* 153 */           marble.add(Actions.actionEntrys[901]);
/* 154 */           marble.add(Actions.actionEntrys[903]);
/*     */         } 
/* 156 */         toReturn.add(new ActionEntry((short)-marble.size(), "Marble", "Fence options"));
/* 157 */         Collections.sort(marble);
/* 158 */         toReturn.addAll(marble);
/*     */ 
/*     */         
/* 161 */         List<ActionEntry> plank = new LinkedList<>();
/* 162 */         plank.add(Actions.actionEntrys[520]);
/* 163 */         plank.add(Actions.actionEntrys[528]);
/* 164 */         plank.add(Actions.actionEntrys[166]);
/* 165 */         plank.add(Actions.actionEntrys[168]);
/* 166 */         if (!hasArch)
/*     */         {
/* 168 */           plank.add(Actions.actionEntrys[516]);
/*     */         }
/* 170 */         toReturn.add(new ActionEntry((short)-plank.size(), "Plank", "Fence options"));
/* 171 */         Collections.sort(plank);
/* 172 */         toReturn.addAll(plank);
/*     */ 
/*     */         
/* 175 */         List<ActionEntry> pottery = new LinkedList<>();
/* 176 */         pottery.add(Actions.actionEntrys[838]);
/* 177 */         pottery.add(Actions.actionEntrys[839]);
/* 178 */         pottery.add(Actions.actionEntrys[840]);
/* 179 */         pottery.add(Actions.actionEntrys[898]);
/* 180 */         pottery.add(Actions.actionEntrys[899]);
/* 181 */         pottery.add(Actions.actionEntrys[896]);
/* 182 */         if (!hasArch) {
/*     */           
/* 184 */           pottery.add(Actions.actionEntrys[894]);
/* 185 */           pottery.add(Actions.actionEntrys[895]);
/* 186 */           pottery.add(Actions.actionEntrys[897]);
/*     */         } 
/* 188 */         toReturn.add(new ActionEntry((short)-pottery.size(), "Pottery", "Fence options"));
/* 189 */         Collections.sort(pottery);
/* 190 */         toReturn.addAll(pottery);
/*     */ 
/*     */         
/* 193 */         List<ActionEntry> rope = new LinkedList<>();
/* 194 */         rope.add(Actions.actionEntrys[544]);
/* 195 */         rope.add(Actions.actionEntrys[543]);
/* 196 */         toReturn.add(new ActionEntry((short)-rope.size(), "Rope", "Rope options"));
/* 197 */         Collections.sort(rope);
/* 198 */         toReturn.addAll(rope);
/*     */ 
/*     */         
/* 201 */         List<ActionEntry> round = new LinkedList<>();
/* 202 */         round.add(Actions.actionEntrys[835]);
/* 203 */         round.add(Actions.actionEntrys[836]);
/* 204 */         round.add(Actions.actionEntrys[837]);
/* 205 */         round.add(Actions.actionEntrys[880]);
/* 206 */         round.add(Actions.actionEntrys[881]);
/* 207 */         round.add(Actions.actionEntrys[878]);
/* 208 */         if (!hasArch) {
/*     */           
/* 210 */           round.add(Actions.actionEntrys[876]);
/* 211 */           round.add(Actions.actionEntrys[877]);
/* 212 */           round.add(Actions.actionEntrys[879]);
/*     */         } 
/* 214 */         toReturn.add(new ActionEntry((short)-round.size(), "Rounded stone", "Fence options"));
/* 215 */         Collections.sort(round);
/* 216 */         toReturn.addAll(round);
/*     */ 
/*     */         
/* 219 */         List<ActionEntry> sandstone = new LinkedList<>();
/* 220 */         sandstone.add(Actions.actionEntrys[841]);
/* 221 */         sandstone.add(Actions.actionEntrys[842]);
/* 222 */         sandstone.add(Actions.actionEntrys[843]);
/* 223 */         sandstone.add(Actions.actionEntrys[886]);
/* 224 */         sandstone.add(Actions.actionEntrys[887]);
/* 225 */         sandstone.add(Actions.actionEntrys[884]);
/* 226 */         if (!hasArch) {
/*     */           
/* 228 */           sandstone.add(Actions.actionEntrys[882]);
/* 229 */           sandstone.add(Actions.actionEntrys[883]);
/* 230 */           sandstone.add(Actions.actionEntrys[885]);
/*     */         } 
/* 232 */         toReturn.add(new ActionEntry((short)-sandstone.size(), "Sandstone", "Fence options"));
/* 233 */         Collections.sort(sandstone);
/* 234 */         toReturn.addAll(sandstone);
/*     */ 
/*     */         
/* 237 */         List<ActionEntry> shaft = new LinkedList<>();
/* 238 */         shaft.add(Actions.actionEntrys[527]);
/* 239 */         shaft.add(Actions.actionEntrys[526]);
/* 240 */         shaft.add(Actions.actionEntrys[529]);
/* 241 */         toReturn.add(new ActionEntry((short)-shaft.size(), "Shaft", "Fence options"));
/* 242 */         Collections.sort(shaft);
/* 243 */         toReturn.addAll(shaft);
/*     */ 
/*     */         
/* 246 */         List<ActionEntry> slate = new LinkedList<>();
/* 247 */         slate.add(Actions.actionEntrys[832]);
/* 248 */         slate.add(Actions.actionEntrys[833]);
/* 249 */         slate.add(Actions.actionEntrys[834]);
/* 250 */         slate.add(Actions.actionEntrys[874]);
/* 251 */         slate.add(Actions.actionEntrys[875]);
/* 252 */         slate.add(Actions.actionEntrys[872]);
/* 253 */         if (!hasArch) {
/*     */           
/* 255 */           slate.add(Actions.actionEntrys[870]);
/* 256 */           slate.add(Actions.actionEntrys[871]);
/* 257 */           slate.add(Actions.actionEntrys[873]);
/*     */         } 
/* 259 */         toReturn.add(new ActionEntry((short)-slate.size(), "Slate", "Fence options"));
/* 260 */         Collections.sort(slate);
/* 261 */         toReturn.addAll(slate);
/*     */ 
/*     */         
/* 264 */         List<ActionEntry> stone = new LinkedList<>();
/* 265 */         stone.add(Actions.actionEntrys[542]);
/* 266 */         stone.add(Actions.actionEntrys[541]);
/* 267 */         stone.add(Actions.actionEntrys[517]);
/* 268 */         if (!hasArch) {
/*     */           
/* 270 */           stone.add(Actions.actionEntrys[654]);
/* 271 */           stone.add(Actions.actionEntrys[164]);
/*     */         } 
/* 273 */         toReturn.add(new ActionEntry((short)-stone.size(), "Stone", "Fence options"));
/* 274 */         Collections.sort(stone);
/* 275 */         toReturn.addAll(stone);
/*     */ 
/*     */         
/* 278 */         List<ActionEntry> woven = new LinkedList<>();
/* 279 */         woven.add(Actions.actionEntrys[478]);
/* 280 */         toReturn.add(new ActionEntry((short)-woven.size(), "Woven", "Fence options"));
/* 281 */         Collections.sort(woven);
/* 282 */         toReturn.addAll(woven);
/*     */       } 
/*     */     } 
/* 285 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean canBuildFenceOnFloor(short action) {
/* 290 */     switch (action) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 164:
/*     */       case 166:
/*     */       case 168:
/*     */       case 477:
/*     */       case 478:
/*     */       case 479:
/*     */       case 516:
/*     */       case 517:
/*     */       case 520:
/*     */       case 521:
/*     */       case 526:
/*     */       case 527:
/*     */       case 528:
/*     */       case 529:
/*     */       case 541:
/*     */       case 542:
/*     */       case 543:
/*     */       case 544:
/*     */       case 545:
/*     */       case 546:
/*     */       case 611:
/*     */       case 654:
/*     */       case 832:
/*     */       case 833:
/*     */       case 834:
/*     */       case 835:
/*     */       case 836:
/*     */       case 837:
/*     */       case 838:
/*     */       case 839:
/*     */       case 840:
/*     */       case 841:
/*     */       case 842:
/*     */       case 843:
/*     */       case 844:
/*     */       case 845:
/*     */       case 846:
/*     */       case 870:
/*     */       case 871:
/*     */       case 872:
/*     */       case 873:
/*     */       case 874:
/*     */       case 875:
/*     */       case 876:
/*     */       case 877:
/*     */       case 878:
/*     */       case 879:
/*     */       case 880:
/*     */       case 881:
/*     */       case 882:
/*     */       case 883:
/*     */       case 884:
/*     */       case 885:
/*     */       case 886:
/*     */       case 887:
/*     */       case 888:
/*     */       case 889:
/*     */       case 890:
/*     */       case 891:
/*     */       case 892:
/*     */       case 893:
/*     */       case 894:
/*     */       case 895:
/*     */       case 896:
/*     */       case 897:
/*     */       case 898:
/*     */       case 899:
/*     */       case 900:
/*     */       case 901:
/*     */       case 902:
/*     */       case 903:
/*     */       case 904:
/*     */       case 905:
/* 372 */         return true;
/*     */     } 
/* 374 */     return false;
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
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, Tiles.TileBorderDirection dir, long borderId, short action, float counter) {
/* 390 */     boolean hasMarker = hasMarker(tilex, tiley, onSurface, dir, heightOffset);
/* 391 */     Structure structure = MethodsStructure.getStructureOrNullAtTileBorder(tilex, tiley, dir, onSurface);
/* 392 */     if (action == 20000 + StructureTypeEnum.SOLID.ordinal() && structure != null && structure.isActionAllowed(performer, (short)116))
/*     */     {
/* 394 */       return MethodsStructure.planWallAt(act, performer, source, tilex, tiley, onSurface, heightOffset, dir, action, counter);
/*     */     }
/* 396 */     if (!hasMarker && canBuildFenceOnFloor(action) && structure != null && structure.isActionAllowed(performer, (short)116))
/*     */     {
/*     */       
/* 399 */       return MethodsStructure.buildFence(act, performer, source, tilex, tiley, onSurface, heightOffset, dir, borderId, action, counter);
/*     */     }
/*     */     
/* 402 */     if (action == 607) {
/*     */       
/* 404 */       performer.getCommunicator().sendAddTileBorderToCreationWindow(borderId);
/* 405 */       return true;
/*     */     } 
/* 407 */     if (action == 1)
/*     */     {
/* 409 */       return action(act, performer, tilex, tiley, onSurface, dir, borderId, action, counter);
/*     */     }
/* 411 */     if (hasMarker)
/*     */     {
/* 413 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that on a highway.");
/*     */     }
/* 415 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, long borderId, short action, float counter) {
/* 423 */     if (action == 1) {
/*     */       
/* 425 */       performer.getCommunicator().sendNormalServerMessage("This outlines where walls may be built.");
/*     */     }
/* 427 */     else if (action == 607) {
/*     */       
/* 429 */       performer.getCommunicator().sendAddTileBorderToCreationWindow(borderId);
/*     */     } 
/* 431 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean hasMarker(int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, int heightOffset) {
/*     */     HighwayPos highwaypos;
/* 437 */     Floor[] floors = Zones.getFloorsAtTile(tilex, tiley, heightOffset, heightOffset, onSurface);
/*     */ 
/*     */     
/* 440 */     if (floors != null && floors.length == 1) {
/* 441 */       highwaypos = MethodsHighways.getHighwayPos(floors[0]);
/*     */ 
/*     */     
/*     */     }
/* 445 */     else if (heightOffset > 0) {
/*     */       
/* 447 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/* 448 */       if (bridgePart != null) {
/* 449 */         highwaypos = MethodsHighways.getHighwayPos(bridgePart);
/*     */       } else {
/* 451 */         return false;
/*     */       } 
/*     */     } else {
/* 454 */       highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*     */     } 
/*     */ 
/*     */     
/* 458 */     if (MethodsHighways.containsMarker(highwaypos, (byte)-1))
/* 459 */       return true; 
/* 460 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ && MethodsHighways.containsMarker(highwaypos, (byte)4))
/* 461 */       return true; 
/* 462 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN && MethodsHighways.containsMarker(highwaypos, (byte)16))
/* 463 */       return true; 
/* 464 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\StructureBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */