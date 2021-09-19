/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.behaviours.Seat;
/*     */ import com.wurmonline.server.behaviours.Vehicle;
/*     */ import com.wurmonline.server.behaviours.Vehicles;
/*     */ import com.wurmonline.server.bodys.BodyFactory;
/*     */ import com.wurmonline.server.intra.PlayerTransfer;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.SkillsFactory;
/*     */ import com.wurmonline.server.structures.NoSuchStructureException;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
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
/*     */ public class CreatureDataStream
/*     */ {
/*  56 */   private static Logger logger = Logger.getLogger(CreatureDataStream.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validateCreature(Creature animal) {
/*  66 */     return (animal.getStatus() != null && animal.getTemplate() != null && animal.getBody() != null);
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
/*     */   public static void toStream(Creature animal, DataOutputStream outputStream) throws IOException {
/*  81 */     Offspring baby = animal.getOffspring();
/*  82 */     if (baby != null) {
/*     */       
/*  84 */       outputStream.writeBoolean(true);
/*  85 */       outputStream.writeLong(baby.getMother());
/*  86 */       outputStream.writeLong(baby.getFather());
/*  87 */       outputStream.writeLong(baby.getTraits());
/*  88 */       outputStream.writeByte((byte)baby.getDaysLeft());
/*     */     }
/*     */     else {
/*     */       
/*  92 */       outputStream.writeBoolean(false);
/*     */     } 
/*     */ 
/*     */     
/*  96 */     outputStream.writeLong(animal.getWurmId());
/*  97 */     outputStream.writeUTF(animal.name);
/*  98 */     outputStream.writeUTF(animal.getTemplate().getName());
/*  99 */     outputStream.writeByte(animal.getSex());
/* 100 */     outputStream.writeShort(animal.getCentimetersHigh());
/* 101 */     outputStream.writeShort(animal.getCentimetersLong());
/* 102 */     outputStream.writeShort(animal.getCentimetersWide());
/* 103 */     outputStream.writeLong((animal.getStatus()).inventoryId);
/* 104 */     outputStream.writeLong(animal.getBody().getId());
/* 105 */     outputStream.writeLong(animal.getBuildingId());
/* 106 */     outputStream.writeShort(animal.getStatus().getStamina() & 0xFFFF);
/* 107 */     outputStream.writeShort(animal.getStatus().getHunger() & 0xFFFF);
/* 108 */     outputStream.writeFloat(animal.getStatus().getNutritionlevel());
/* 109 */     outputStream.writeShort(animal.getStatus().getThirst() & 0xFFFF);
/* 110 */     outputStream.writeBoolean(animal.isDead());
/* 111 */     outputStream.writeBoolean(animal.isStealth());
/* 112 */     outputStream.writeByte(0);
/* 113 */     outputStream.writeInt((animal.getStatus()).age);
/* 114 */     outputStream.writeLong((animal.getStatus()).lastPolledAge);
/* 115 */     outputStream.writeByte((animal.getStatus()).fat);
/* 116 */     outputStream.writeLong((animal.getStatus()).traits);
/* 117 */     outputStream.writeLong(-10L);
/* 118 */     outputStream.writeLong(animal.getMother());
/* 119 */     outputStream.writeLong(animal.getFather());
/* 120 */     outputStream.writeBoolean(animal.isReborn());
/* 121 */     outputStream.writeFloat(0.0F);
/* 122 */     outputStream.writeLong((animal.getStatus()).lastPolledLoyalty);
/* 123 */     outputStream.writeBoolean(animal.isOffline());
/* 124 */     outputStream.writeBoolean(animal.isStayonline());
/* 125 */     outputStream.writeShort((animal.getStatus()).detectInvisCounter);
/* 126 */     outputStream.writeByte(animal.getDisease());
/* 127 */     outputStream.writeLong(animal.getLastGroomed());
/* 128 */     outputStream.writeLong(animal.getVehicle());
/* 129 */     outputStream.writeByte((animal.getStatus()).modtype);
/* 130 */     outputStream.writeUTF(animal.petName);
/*     */ 
/*     */     
/* 133 */     if (animal.getSkills() == null || animal.getSkills().getSkillsNoTemp() == null) {
/* 134 */       outputStream.writeInt(0);
/*     */     } else {
/*     */       
/* 137 */       Skill[] animalSkills = animal.getSkills().getSkillsNoTemp();
/* 138 */       int numSkills = animalSkills.length;
/*     */       
/* 140 */       outputStream.writeInt(numSkills);
/* 141 */       for (Skill curSkill : animalSkills) {
/*     */         
/* 143 */         if (!curSkill.isTemporary()) {
/*     */           
/* 145 */           outputStream.writeInt(curSkill.getNumber());
/* 146 */           outputStream.writeDouble(curSkill.getKnowledge());
/* 147 */           outputStream.writeDouble(curSkill.getMinimumValue());
/* 148 */           outputStream.writeLong(curSkill.lastUsed);
/* 149 */           outputStream.writeLong(curSkill.id);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 155 */     Item[] animalItems = animal.getAllItems();
/* 156 */     int numItems = 0;
/* 157 */     for (Item curItem : animalItems) {
/*     */       
/* 159 */       if (!curItem.isBodyPart() && !curItem.isInventory() && !curItem.isTemporary())
/*     */       {
/* 161 */         numItems++;
/*     */       }
/*     */     } 
/*     */     
/* 165 */     outputStream.writeInt(numItems);
/* 166 */     for (Item curItem : animalItems) {
/*     */       
/* 168 */       if (!curItem.isBodyPart() && !curItem.isInventory() && !curItem.isTemporary())
/*     */       {
/* 170 */         PlayerTransfer.sendItem(curItem, outputStream, false);
/*     */       }
/*     */     } 
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
/*     */   public static void fromStream(DataInputStream inputStream) throws IOException {
/* 187 */     Connection dbcon = null;
/* 188 */     PreparedStatement ps = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     boolean hasBaby = inputStream.readBoolean();
/* 195 */     if (hasBaby) {
/*     */       
/* 197 */       long mother = inputStream.readLong();
/* 198 */       long father = inputStream.readLong();
/* 199 */       long traits = inputStream.readLong();
/* 200 */       byte daysLeft = inputStream.readByte();
/* 201 */       new Offspring(mother, father, traits, daysLeft, false);
/*     */     } 
/*     */     
/* 204 */     long creatureId = inputStream.readLong();
/* 205 */     Creature animal = null;
/*     */     
/*     */     try {
/* 208 */       animal = new Creature(creatureId);
/* 209 */       animal.setName(inputStream.readUTF());
/* 210 */       (animal.getStatus()).template = CreatureTemplateFactory.getInstance().getTemplate(inputStream.readUTF());
/* 211 */       animal.template = (animal.getStatus()).template;
/* 212 */       animal.getStatus().setSex(inputStream.readByte());
/* 213 */       short centimetersHigh = inputStream.readShort();
/* 214 */       short centimetersLong = inputStream.readShort();
/* 215 */       short centimetersWide = inputStream.readShort();
/* 216 */       (animal.getStatus()).inventoryId = inputStream.readLong();
/* 217 */       (animal.getStatus()).bodyId = inputStream.readLong();
/* 218 */       (animal.getStatus()).body = BodyFactory.getBody(animal, (animal.getStatus()).template.getBodyType(), centimetersHigh, centimetersLong, centimetersWide);
/* 219 */       (animal.getStatus()).buildingId = inputStream.readLong();
/* 220 */       if ((animal.getStatus()).buildingId != -10L) {
/*     */         
/*     */         try {
/*     */           
/* 224 */           Structure struct = Structures.getStructure((animal.getStatus()).buildingId);
/* 225 */           if (!struct.isFinalFinished())
/*     */           {
/* 227 */             animal.setStructure(struct);
/*     */           }
/*     */           else
/*     */           {
/* 231 */             (animal.getStatus()).buildingId = -10L;
/*     */           }
/*     */         
/* 234 */         } catch (NoSuchStructureException nss) {
/*     */           
/* 236 */           (animal.getStatus()).buildingId = -10L;
/* 237 */           logger.log(Level.INFO, "Could not find structure for " + animal.name);
/* 238 */           animal.setStructure(null);
/*     */         } 
/*     */       }
/* 241 */       (animal.getStatus()).stamina = inputStream.readShort();
/* 242 */       (animal.getStatus()).hunger = inputStream.readShort();
/* 243 */       (animal.getStatus()).nutrition = inputStream.readFloat();
/* 244 */       (animal.getStatus()).thirst = inputStream.readShort();
/* 245 */       (animal.getStatus()).dead = inputStream.readBoolean();
/* 246 */       (animal.getStatus()).stealth = inputStream.readBoolean();
/* 247 */       (animal.getStatus()).kingdom = inputStream.readByte();
/* 248 */       (animal.getStatus()).age = inputStream.readInt();
/* 249 */       (animal.getStatus()).lastPolledAge = inputStream.readLong();
/* 250 */       (animal.getStatus()).fat = inputStream.readByte();
/* 251 */       (animal.getStatus()).traits = inputStream.readLong();
/* 252 */       if ((animal.getStatus()).traits != 0L)
/*     */       {
/* 254 */         animal.getStatus().setTraitBits((animal.getStatus()).traits);
/*     */       }
/* 256 */       animal.dominator = inputStream.readLong();
/* 257 */       (animal.getStatus()).mother = inputStream.readLong();
/* 258 */       (animal.getStatus()).father = inputStream.readLong();
/* 259 */       (animal.getStatus()).reborn = inputStream.readBoolean();
/* 260 */       (animal.getStatus()).loyalty = inputStream.readFloat();
/* 261 */       (animal.getStatus()).lastPolledLoyalty = inputStream.readLong();
/* 262 */       (animal.getStatus()).offline = inputStream.readBoolean();
/* 263 */       (animal.getStatus()).stayOnline = inputStream.readBoolean();
/* 264 */       (animal.getStatus()).detectInvisCounter = inputStream.readShort();
/* 265 */       (animal.getStatus()).disease = inputStream.readByte();
/* 266 */       (animal.getStatus()).lastGroomed = inputStream.readLong();
/* 267 */       long hitchedTo = inputStream.readLong();
/* 268 */       if (hitchedTo > 0L) {
/*     */         
/*     */         try {
/*     */           
/* 272 */           Item vehicle = Items.getItem(hitchedTo);
/* 273 */           Vehicle vehic = Vehicles.getVehicle(vehicle);
/* 274 */           if (vehic != null && vehic.addDragger(animal)) {
/*     */             
/* 276 */             animal.setHitched(vehic, true);
/* 277 */             Seat driverseat = vehic.getPilotSeat();
/* 278 */             if (driverseat != null)
/*     */             {
/* 280 */               float _r = (-vehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 281 */               float _s = (float)Math.sin(_r);
/* 282 */               float _c = (float)Math.cos(_r);
/* 283 */               float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/* 284 */               float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/* 285 */               float nPosX = animal.getStatus().getPositionX() - xo;
/* 286 */               float nPosY = animal.getStatus().getPositionY() - yo;
/* 287 */               float nPosZ = animal.getStatus().getPositionZ() - driverseat.offz;
/* 288 */               animal.getStatus().setPositionX(nPosX);
/* 289 */               animal.getStatus().setPositionY(nPosY);
/* 290 */               animal.getStatus().setRotation(-vehicle.getRotation() + 180.0F);
/* 291 */               animal.getMovementScheme().setPosition(animal.getStatus().getPositionX(), animal.getStatus().getPositionY(), nPosZ, animal.getStatus().getRotation(), animal.getLayer());
/*     */             }
/*     */           
/*     */           } 
/* 295 */         } catch (NoSuchItemException e) {
/*     */           
/* 297 */           logger.log(Level.WARNING, "Exception", (Throwable)e);
/*     */         } 
/*     */       }
/* 300 */       (animal.getStatus()).modtype = inputStream.readByte();
/* 301 */       animal.setPetName(inputStream.readUTF());
/* 302 */       animal.loadTemplate();
/* 303 */       Creatures.getInstance().addCreature(animal, false, false);
/*     */     }
/* 305 */     catch (Exception e) {
/*     */       
/* 307 */       logger.log(Level.WARNING, "Exception", e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 313 */       assert animal != null;
/* 314 */       animal.skills = SkillsFactory.createSkills(animal.getWurmId());
/* 315 */       animal.skills.clone(animal.template.getSkills().getSkills());
/*     */     }
/* 317 */     catch (Exception e) {
/*     */       
/* 319 */       logger.log(Level.WARNING, "Exception", e);
/*     */     } 
/*     */     
/* 322 */     int numSkills = inputStream.readInt();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 329 */       for (int skillNo = 0; skillNo < numSkills; skillNo++)
/*     */       {
/* 331 */         int curSkillNum = inputStream.readInt();
/* 332 */         double curSkillValue = inputStream.readDouble();
/* 333 */         double curSkillMinValue = inputStream.readDouble();
/* 334 */         long curSkillLastUsed = inputStream.readLong();
/* 335 */         inputStream.readLong();
/*     */         
/* 337 */         animal.skills.learn(curSkillNum, (float)curSkillMinValue, false);
/* 338 */         (animal.skills.getSkill(curSkillNum)).lastUsed = curSkillLastUsed;
/* 339 */         animal.skills.getSkill(curSkillNum).setKnowledge(curSkillValue, false);
/*     */       }
/*     */     
/* 342 */     } catch (Exception e) {
/*     */       
/* 344 */       logger.log(Level.WARNING, "Exception", e);
/*     */     } 
/*     */     
/*     */     try {
/* 348 */       animal.getBody().createBodyParts();
/*     */     }
/* 350 */     catch (FailedException|com.wurmonline.server.items.NoSuchTemplateException e) {
/*     */       
/* 352 */       logger.log(Level.WARNING, "Exception", (Throwable)e);
/*     */     } 
/*     */     
/*     */     try {
/* 356 */       animal.loadPossessions((animal.getStatus()).inventoryId);
/*     */     }
/* 358 */     catch (Exception e) {
/*     */       
/* 360 */       logger.log(Level.WARNING, "Exception", e);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 365 */       dbcon = DbConnector.getCreatureDbCon();
/* 366 */       ps = dbcon.prepareStatement("insert into CREATURES (WURMID, NAME, TEMPLATENAME, SEX, CENTIMETERSHIGH, CENTIMETERSLONG, CENTIMETERSWIDE, INVENTORYID, BODYID, BUILDINGID, STAMINA, HUNGER, NUTRITION, THIRST, DEAD, STEALTH, KINGDOM, AGE, LASTPOLLEDAGE, FAT, TRAITS, DOMINATOR, MOTHER, FATHER, REBORN, LOYALTY, LASTPOLLEDLOYALTY, OFFLINE, STAYONLINE, DETECTIONSECS, DISEASE, LASTGROOMED, VEHICLE, TYPE, PETNAME) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 372 */       ps.setLong(1, animal.getWurmId());
/* 373 */       ps.setString(2, animal.name);
/* 374 */       ps.setString(3, animal.getTemplate().getName());
/* 375 */       ps.setByte(4, animal.getSex());
/* 376 */       ps.setShort(5, animal.getCentimetersHigh());
/* 377 */       ps.setShort(6, animal.getCentimetersLong());
/* 378 */       ps.setShort(7, animal.getCentimetersWide());
/* 379 */       ps.setLong(8, (animal.getStatus()).inventoryId);
/* 380 */       ps.setLong(9, animal.getBody().getId());
/* 381 */       ps.setLong(10, animal.getBuildingId());
/* 382 */       ps.setShort(11, (short)(animal.getStatus().getStamina() & 0xFFFF));
/* 383 */       ps.setShort(12, (short)(animal.getStatus().getHunger() & 0xFFFF));
/* 384 */       ps.setFloat(13, animal.getStatus().getNutritionlevel());
/* 385 */       ps.setShort(14, (short)(animal.getStatus().getThirst() & 0xFFFF));
/* 386 */       ps.setBoolean(15, animal.isDead());
/* 387 */       ps.setBoolean(16, animal.isStealth());
/* 388 */       ps.setByte(17, animal.getCurrentKingdom());
/* 389 */       ps.setInt(18, (animal.getStatus()).age);
/* 390 */       ps.setLong(19, (animal.getStatus()).lastPolledAge);
/* 391 */       ps.setByte(20, (animal.getStatus()).fat);
/* 392 */       ps.setLong(21, (animal.getStatus()).traits);
/* 393 */       ps.setLong(22, -10L);
/* 394 */       ps.setLong(23, animal.getMother());
/* 395 */       ps.setLong(24, animal.getFather());
/* 396 */       ps.setBoolean(25, animal.isReborn());
/* 397 */       ps.setFloat(26, animal.getLoyalty());
/* 398 */       ps.setLong(27, (animal.getStatus()).lastPolledLoyalty);
/* 399 */       ps.setBoolean(28, animal.isOffline());
/* 400 */       ps.setBoolean(29, animal.isStayonline());
/* 401 */       ps.setShort(30, (short)(animal.getStatus()).detectInvisCounter);
/* 402 */       ps.setByte(31, animal.getDisease());
/* 403 */       ps.setLong(32, animal.getLastGroomed());
/* 404 */       ps.setLong(33, animal.getVehicle());
/* 405 */       ps.setByte(34, (animal.getStatus()).modtype);
/* 406 */       ps.setString(35, animal.petName);
/* 407 */       ps.execute();
/*     */     }
/* 409 */     catch (SQLException e) {
/*     */       
/* 411 */       logger.log(Level.WARNING, "Exception", e);
/*     */     }
/*     */     finally {
/*     */       
/* 415 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 416 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 418 */     animal.getStatus().setStatusExists(true);
/* 419 */     inputStream.readInt();
/* 420 */     Items.loadAllItemsForCreature(animal, animal.getStatus().getInventoryId());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureDataStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */