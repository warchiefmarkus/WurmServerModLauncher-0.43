/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.CropTilePoller;
/*     */ import com.wurmonline.server.zones.VolaTile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TileDirtBehaviour
/*     */   extends TileBehaviour
/*     */ {
/*     */   private static final short MAX_WATER_CROP_DEPTH = -4;
/*     */   
/*     */   TileDirtBehaviour() {
/*  54 */     super((short)15);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile) {
/*  61 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  62 */     List<ActionEntry> nature = new LinkedList<>();
/*  63 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, tile));
/*  64 */     byte type = Tiles.decodeType(tile);
/*  65 */     byte data = Tiles.decodeData(tile);
/*     */     
/*  67 */     if (subject.isSeed()) {
/*  68 */       toReturn.add(Actions.actionEntrys[153]);
/*  69 */     } else if (subject.getTemplateId() == 495) {
/*     */ 
/*     */       
/*  72 */       toReturn.add(new ActionEntry((short)-2, "Lay boards", "paving", emptyIntArr));
/*  73 */       toReturn.add(new ActionEntry((short)155, "Over dirt", "laying", new int[] { 43 }));
/*     */ 
/*     */       
/*  76 */       toReturn.add(new ActionEntry((short)576, "In nearest corner", "laying", new int[] { 43 }));
/*     */ 
/*     */     
/*     */     }
/*  80 */     else if (subject.getTemplateId() == 526) {
/*  81 */       toReturn.add(Actions.actionEntrys[118]);
/*     */     } 
/*  83 */     if (subject.getTemplateId() == 266) {
/*     */       
/*  85 */       nature.add(Actions.actionEntrys[186]);
/*  86 */       nature.add(Actions.actionEntrys[660]);
/*     */     }
/*  88 */     else if (subject.isFlower()) {
/*  89 */       toReturn.add(new ActionEntry((short)186, "Plant Flowers", "planting"));
/*  90 */     } else if (subject.isNaturePlantable()) {
/*  91 */       toReturn.add(new ActionEntry((short)186, "Plant", "planting"));
/*  92 */     }  if (Server.hasGrubs(tilex, tiley))
/*     */     {
/*  94 */       nature.add(new ActionEntry((short)935, "Search for wurms", "searching"));
/*     */     }
/*  96 */     toReturn.addAll(getNatureMenu(performer, tilex, tiley, type, data, nature));
/*     */     
/*  98 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/* 105 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 106 */     List<ActionEntry> nature = new LinkedList<>();
/* 107 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/* 108 */     byte type = Tiles.decodeType(tile);
/* 109 */     byte data = Tiles.decodeData(tile);
/* 110 */     if (Server.hasGrubs(tilex, tiley))
/*     */     {
/* 112 */       nature.add(new ActionEntry((short)935, "Search for wurms", "searching"));
/*     */     }
/* 114 */     toReturn.addAll(getNatureMenu(performer, tilex, tiley, type, data, nature));
/*     */     
/* 116 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) {
/* 123 */     boolean done = true;
/* 124 */     if (action == 1) {
/*     */       
/* 126 */       performer.getCommunicator().sendNormalServerMessage("You see a patch of brown dirt, good for growing crops on.");
/* 127 */       sendVillageString(performer, tilex, tiley, true);
/*     */     }
/* 129 */     else if (action == 935) {
/*     */       
/* 131 */       done = Terraforming.pickWurms(act, performer, tilex, tiley, tile, counter);
/*     */     } else {
/*     */       
/* 134 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/* 135 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/* 143 */     boolean done = true;
/* 144 */     if (action == 153 && source.isSeed()) {
/*     */       
/* 146 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_DIRT.id) {
/*     */         
/* 148 */         performer.getCommunicator().sendNormalServerMessage("The ground must be loosened dirt for any seeds to grow.", (byte)3);
/* 149 */         return true;
/*     */       } 
/* 151 */       if (source.isProcessedFood()) {
/*     */         
/* 153 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/* 154 */             .getName() + " is processed and cannot be planted.");
/* 155 */         return true;
/*     */       } 
/* 157 */       VolaTile vtile = Zones.getTileOrNull(tilex, tiley, onSurface);
/* 158 */       if (vtile != null)
/*     */       {
/* 160 */         if (vtile.getStructure() != null) {
/*     */           
/* 162 */           performer.getCommunicator()
/* 163 */             .sendNormalServerMessage("The " + source.getName() + " would never grow inside.", (byte)3);
/* 164 */           return true;
/*     */         } 
/*     */       }
/*     */       
/*     */       try {
/* 169 */         if (!Terraforming.isFlat(tilex, tiley, onSurface, 4)) {
/*     */           
/* 171 */           performer.getCommunicator().sendNormalServerMessage("The ground is not flat enough for crops to grow. You need to flatten it first.", (byte)3);
/*     */           
/* 173 */           return true;
/*     */         } 
/* 175 */         boolean isUnderWater = Terraforming.isCornerUnderWater(tilex, tiley, onSurface);
/*     */         
/* 177 */         boolean isWaterPlant = (source.getTemplateId() == 744 || source.getTemplateId() == 746);
/* 178 */         if (isUnderWater && !isWaterPlant) {
/*     */           
/* 180 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep. You cannot sow that crop there.", (byte)3);
/*     */           
/* 182 */           return true;
/*     */         } 
/*     */         
/* 185 */         if (isWaterPlant)
/*     */         {
/* 187 */           if (!Terraforming.isAllCornersInsideHeightRange(tilex, tiley, onSurface, (short)-1, (short)-4))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 194 */             performer.getCommunicator().sendNormalServerMessage("This type of crop can only grow in water of suitable depth.", (byte)3);
/*     */             
/* 196 */             return true;
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 205 */       catch (IllegalArgumentException iae) {
/*     */         
/* 207 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep. You cannot sow that crop there.", (byte)3);
/* 208 */         return true;
/*     */       } 
/* 210 */       done = false;
/* 211 */       if (counter == 1.0F)
/*     */       {
/* 213 */         performer.getCommunicator().sendNormalServerMessage("You start sowing the seeds.");
/* 214 */         Server.getInstance().broadCastAction(performer.getName() + " starts sowing some seeds.", performer, 5);
/* 215 */         Skill farming = performer.getSkills().getSkillOrLearn(10049);
/* 216 */         short time = (short)(int)(130.0D - farming.getKnowledge(source, 0.0D) - act.getRarity() - source.getRarity());
/*     */         
/* 218 */         performer.sendActionControl(Actions.actionEntrys[153].getVerbString(), true, time);
/* 219 */         act.setTimeLeft(time);
/*     */       }
/* 221 */       else if (counter > (act.getTimeLeft() / 10))
/*     */       {
/* 223 */         if (act.getRarity() != 0)
/* 224 */           performer.playPersonalSound("sound.fx.drumroll"); 
/* 225 */         performer.getStatus().modifyStamina(-2000.0F);
/* 226 */         done = true;
/* 227 */         int crop = Crops.getNumber(source.getTemplateId());
/* 228 */         byte type = Tiles.decodeType(tile);
/* 229 */         if (type == Tiles.Tile.TILE_DIRT.id) {
/*     */           
/* 231 */           if (onSurface) {
/*     */             
/* 233 */             Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Crops.getTileType(crop), 
/* 234 */                 Crops.encodeFieldData(true, 0, crop));
/*     */             
/* 236 */             double diff = Crops.getDifficultyFor(crop);
/* 237 */             Skill farming = performer.getSkills().getSkillOrLearn(10049);
/* 238 */             farming.skillCheck(diff, 0.0D, false, 1.0F);
/* 239 */             Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/*     */ 
/*     */ 
/*     */             
/* 243 */             int resource = (int)(100.0D - farming.getKnowledge(0.0D) + source.getQualityLevel() + (source.getRarity() * 20) + (act.getRarity() * 50));
/* 244 */             Server.setWorldResource(tilex, tiley, resource);
/* 245 */             CropTilePoller.addCropTile(tile, tilex, tiley, crop, onSurface);
/*     */ 
/*     */             
/* 248 */             performer.achievement(523);
/*     */           }
/*     */           else {
/*     */             
/* 252 */             Server.caveMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Crops.getTileType(crop), 
/* 253 */                   Crops.encodeFieldData(true, 0, crop)));
/*     */             
/* 255 */             Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/* 256 */             CropTilePoller.addCropTile(tile, tilex, tiley, crop, onSurface);
/*     */           } 
/* 258 */           performer.getCommunicator().sendNormalServerMessage("You sow the " + Crops.getCropName(crop) + ".");
/* 259 */           Server.getInstance().broadCastAction(performer.getName() + " sows some seeds.", performer, 5);
/*     */         }
/*     */         else {
/*     */           
/* 263 */           performer.getCommunicator().sendNormalServerMessage("You sow the seeds, but the field is already sown and it will have little effect.");
/*     */           
/* 265 */           Server.getInstance().broadCastAction(performer.getName() + " sows some seeds.", performer, 5);
/*     */         } 
/* 267 */         Items.destroyItem(source.getWurmId());
/*     */       }
/*     */     
/* 270 */     } else if (action == 186) {
/*     */       
/* 272 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_DIRT.id) {
/*     */         
/* 274 */         performer.getCommunicator().sendNormalServerMessage("The ground must be dirt for any plants to grow.", (byte)3);
/* 275 */         return true;
/*     */       } 
/* 277 */       if (source.isNaturePlantable()) {
/* 278 */         done = Terraforming.plantFlower(performer, source, tilex, tiley, onSurface, tile, counter);
/*     */       } else {
/* 280 */         done = Terraforming.plantSprout(performer, source, tilex, tiley, onSurface, tile, counter, false);
/*     */       } 
/* 282 */     } else if (action == 660) {
/*     */       
/* 284 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_DIRT.id) {
/*     */         
/* 286 */         performer.getCommunicator().sendNormalServerMessage("The ground must be dirt for any plants to grow.", (byte)3);
/* 287 */         return true;
/*     */       } 
/* 289 */       done = Terraforming.plantSprout(performer, source, tilex, tiley, onSurface, tile, counter, true);
/*     */     }
/* 291 */     else if (action == 1 || action == 152) {
/* 292 */       done = action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/* 293 */     } else if (action == 155 || action == 576) {
/* 294 */       done = Terraforming.makeFloor(performer, source, tilex, tiley, onSurface, tile, counter);
/* 295 */     } else if (action == 118 && source.getTemplateId() == 526) {
/*     */       
/* 297 */       performer.getCommunicator().sendNormalServerMessage("You draw a circle in the air in front of you with " + source
/* 298 */           .getNameWithGenus() + ".");
/* 299 */       Server.getInstance().broadCastAction(performer
/* 300 */           .getName() + " draws a circle in the air in front of " + performer.getHimHerItString() + " with " + source
/* 301 */           .getNameWithGenus() + ".", performer, 5);
/* 302 */       done = true;
/* 303 */       if (source.getAuxData() > 0) {
/*     */         
/* 305 */         Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, 
/* 306 */             (byte)(Server.rand.nextInt(7) + 1));
/* 307 */         Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/* 308 */         source.setAuxData((byte)(source.getAuxData() - 1));
/*     */       } else {
/*     */         
/* 311 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*     */       } 
/*     */     } else {
/* 314 */       done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/*     */     } 
/* 316 */     return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileDirtBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */