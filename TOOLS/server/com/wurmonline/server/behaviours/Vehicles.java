/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemSettings;
/*     */ import com.wurmonline.shared.constants.ProtoConstants;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class Vehicles
/*     */   implements ProtoConstants, CreatureTemplateIds
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(Vehicles.class.getName());
/*     */ 
/*     */   
/*  43 */   private static Map<Long, Vehicle> vehicles = new ConcurrentHashMap<>();
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
/*     */   public static Vehicle getVehicle(Item item) {
/*  62 */     Vehicle toReturn = null;
/*  63 */     if (item.isVehicle() || item.isHitchTarget()) {
/*     */       
/*  65 */       toReturn = vehicles.get(Long.valueOf(item.getWurmId()));
/*  66 */       if (toReturn == null)
/*     */       {
/*  68 */         toReturn = createVehicle(item);
/*     */       }
/*     */     } 
/*  71 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vehicle getVehicle(Creature creature) {
/*  82 */     Vehicle toReturn = null;
/*  83 */     if (creature.isVehicle()) {
/*     */       
/*  85 */       toReturn = vehicles.get(Long.valueOf(creature.getWurmId()));
/*  86 */       if (toReturn == null)
/*     */       {
/*  88 */         toReturn = createVehicle(creature);
/*     */       }
/*     */     } 
/*  91 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vehicle getVehicleForId(long id) {
/* 101 */     return vehicles.get(Long.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vehicle createVehicle(Item item) {
/* 112 */     Vehicle vehic = new Vehicle(item.getWurmId());
/* 113 */     setSettingsForVehicle(item, vehic);
/* 114 */     vehicles.put(Long.valueOf(item.getWurmId()), vehic);
/* 115 */     return vehic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vehicle createVehicle(Creature creature) {
/* 126 */     Vehicle vehic = new Vehicle(creature.getWurmId());
/* 127 */     setSettingsForVehicle(creature, vehic);
/* 128 */     vehicles.put(Long.valueOf(creature.getWurmId()), vehic);
/* 129 */     return vehic;
/*     */   }
/*     */ 
/*     */   
/*     */   static void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
/* 134 */     int cid = creature.getTemplate().getTemplateId();
/* 135 */     vehicle.embarkString = "mount";
/* 136 */     vehicle.embarksString = "mounts";
/* 137 */     if (cid == 49 || cid == 3) {
/*     */       
/* 139 */       vehicle.createPassengerSeats(0);
/*     */       
/* 141 */       vehicle.setSeatFightMod(0, 0.7F, 0.9F);
/* 142 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 143 */       vehicle.creature = true;
/* 144 */       vehicle.skillNeeded = 19.0F;
/* 145 */       if (cid == 49)
/* 146 */         vehicle.skillNeeded = 23.0F; 
/* 147 */       vehicle.name = creature.getName();
/* 148 */       vehicle.maxDepth = -0.7F;
/* 149 */       vehicle.maxHeightDiff = 0.04F;
/* 150 */       vehicle.setMaxSpeed(17.0F);
/* 151 */       vehicle.commandType = 3;
/*     */     }
/* 153 */     else if (cid == 64) {
/*     */       
/* 155 */       vehicle.createPassengerSeats(0);
/*     */       
/* 157 */       vehicle.setSeatFightMod(0, 0.7F, 0.9F);
/* 158 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 159 */       vehicle.creature = true;
/* 160 */       vehicle.skillNeeded = 21.0F;
/* 161 */       vehicle.name = creature.getName();
/* 162 */       vehicle.maxDepth = -0.7F;
/* 163 */       vehicle.maxHeightDiff = 0.04F;
/* 164 */       vehicle.setMaxSpeed(30.0F);
/* 165 */       vehicle.commandType = 3;
/* 166 */       vehicle.canHaveEquipment = true;
/*     */     }
/* 168 */     else if (cid == 83) {
/*     */       
/* 170 */       vehicle.createPassengerSeats(0);
/*     */       
/* 172 */       vehicle.setSeatFightMod(0, 0.7F, 0.9F);
/* 173 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 174 */       vehicle.creature = true;
/* 175 */       vehicle.skillNeeded = 31.0F;
/* 176 */       vehicle.name = creature.getName();
/* 177 */       vehicle.maxDepth = -0.7F;
/* 178 */       vehicle.maxHeightDiff = 0.04F;
/* 179 */       vehicle.setMaxSpeed(32.0F);
/* 180 */       vehicle.commandType = 3;
/* 181 */       vehicle.canHaveEquipment = true;
/*     */     }
/* 183 */     else if (CreatureTemplate.isFullyGrownDragon(cid)) {
/*     */       
/* 185 */       vehicle.createPassengerSeats(1);
/* 186 */       vehicle.setSeatFightMod(0, 0.3F, 1.5F);
/* 187 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 3.0F);
/* 188 */       vehicle.setSeatFightMod(1, 0.3F, 1.3F);
/* 189 */       vehicle.setSeatOffset(1, 1.0F, 0.0F, 3.0F);
/* 190 */       vehicle.creature = true;
/* 191 */       vehicle.skillNeeded = 30.0F;
/* 192 */       vehicle.name = creature.getName();
/* 193 */       vehicle.setMaxSpeed(35.0F);
/* 194 */       vehicle.maxDepth = -0.7F;
/* 195 */       vehicle.commandType = 3;
/* 196 */       vehicle.canHaveEquipment = true;
/*     */     }
/* 198 */     else if (CreatureTemplate.isDragonHatchling(cid)) {
/*     */       
/* 200 */       vehicle.createPassengerSeats(0);
/* 201 */       vehicle.setSeatFightMod(0, 0.6F, 1.3F);
/* 202 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.5F);
/* 203 */       vehicle.creature = true;
/* 204 */       vehicle.skillNeeded = 29.0F;
/* 205 */       vehicle.name = creature.getName();
/* 206 */       vehicle.setMaxSpeed(33.0F);
/* 207 */       vehicle.maxDepth = -0.7F;
/* 208 */       vehicle.commandType = 3;
/* 209 */       vehicle.canHaveEquipment = true;
/*     */     }
/* 211 */     else if (cid == 12) {
/*     */       
/* 213 */       vehicle.createPassengerSeats(0);
/* 214 */       vehicle.setSeatFightMod(0, 0.8F, 1.1F);
/* 215 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 216 */       vehicle.creature = true;
/* 217 */       vehicle.skillNeeded = 23.0F;
/* 218 */       vehicle.name = creature.getName();
/* 219 */       vehicle.maxHeightDiff = 0.04F;
/* 220 */       vehicle.maxDepth = -0.7F;
/* 221 */       vehicle.setMaxSpeed(20.0F);
/* 222 */       vehicle.commandType = 3;
/*     */     }
/* 224 */     else if (cid == 40 || cid == 37 || cid == 86) {
/*     */       
/* 226 */       vehicle.createPassengerSeats(0);
/* 227 */       vehicle.setSeatFightMod(0, 0.8F, 1.1F);
/* 228 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.5F);
/* 229 */       vehicle.creature = true;
/* 230 */       vehicle.skillNeeded = 24.0F;
/* 231 */       vehicle.name = creature.getName();
/* 232 */       vehicle.maxDepth = -0.7F;
/* 233 */       vehicle.maxHeightDiff = 0.04F;
/* 234 */       vehicle.setMaxSpeed(20.0F);
/* 235 */       vehicle.commandType = 3;
/*     */     }
/* 237 */     else if (cid == 59) {
/*     */       
/* 239 */       vehicle.createPassengerSeats(0);
/* 240 */       vehicle.setSeatFightMod(0, 0.5F, 1.1F);
/* 241 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 242 */       vehicle.creature = true;
/* 243 */       vehicle.skillNeeded = 25.0F;
/* 244 */       vehicle.name = creature.getName();
/* 245 */       vehicle.maxDepth = -0.7F;
/* 246 */       vehicle.maxHeightDiff = 0.04F;
/* 247 */       vehicle.setMaxSpeed(17.0F);
/* 248 */       vehicle.commandType = 3;
/*     */     }
/* 250 */     else if (cid == 21) {
/*     */       
/* 252 */       vehicle.createPassengerSeats(1);
/* 253 */       vehicle.setSeatFightMod(0, 0.6F, 1.0F);
/* 254 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 255 */       vehicle.setSeatFightMod(1, 0.9F, 0.5F);
/* 256 */       vehicle.setSeatOffset(1, 0.5F, 0.0F, 1.54F);
/* 257 */       vehicle.creature = true;
/* 258 */       vehicle.skillNeeded = 26.0F;
/* 259 */       vehicle.name = creature.getName();
/* 260 */       vehicle.maxDepth = -0.7F;
/* 261 */       vehicle.maxHeightDiff = 0.04F;
/* 262 */       vehicle.setMaxSpeed(33.0F);
/* 263 */       vehicle.commandType = 3;
/* 264 */       vehicle.canHaveEquipment = true;
/*     */     }
/* 266 */     else if (cid == 24) {
/*     */       
/* 268 */       vehicle.createPassengerSeats(1);
/* 269 */       vehicle.setSeatFightMod(0, 0.6F, 1.0F);
/* 270 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.5F);
/* 271 */       vehicle.setSeatFightMod(1, 0.3F, 1.3F);
/* 272 */       vehicle.setSeatOffset(1, 0.5F, -0.3F, 0.5F);
/* 273 */       vehicle.setSeatFightMod(2, 0.3F, 1.3F);
/* 274 */       vehicle.setSeatOffset(2, 0.5F, 0.3F, 0.5F);
/* 275 */       vehicle.creature = true;
/* 276 */       vehicle.skillNeeded = 26.0F;
/* 277 */       vehicle.name = creature.getName();
/* 278 */       vehicle.maxDepth = -0.3F;
/* 279 */       vehicle.maxHeightDiff = 0.04F;
/* 280 */       vehicle.setMaxSpeed(20.0F);
/* 281 */       vehicle.commandType = 3;
/*     */     }
/* 283 */     else if (cid == 58) {
/*     */       
/* 285 */       vehicle.createPassengerSeats(1);
/* 286 */       vehicle.setSeatFightMod(0, 1.3F, 1.0F);
/* 287 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F);
/* 288 */       vehicle.setSeatOffset(1, 0.5F, 0.0F, 0.0F);
/* 289 */       vehicle.creature = true;
/* 290 */       vehicle.skillNeeded = 25.0F;
/* 291 */       vehicle.maxHeight = 2499.0F;
/* 292 */       vehicle.name = creature.getName();
/* 293 */       vehicle.maxHeightDiff = 0.04F;
/* 294 */       vehicle.setMaxSpeed(11.0F);
/* 295 */       vehicle.commandType = 3;
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
/*     */   static void setSettingsForVehicle(Item item, Vehicle vehicle) {
/* 308 */     if (item.getTemplateId() == 861) {
/*     */       
/* 310 */       vehicle.setUnmountable(true);
/* 311 */       vehicle.createOnlyPassengerSeats(1);
/*     */       
/* 313 */       vehicle.setSeatFightMod(0, 0.7F, 0.4F);
/* 314 */       vehicle.creature = false;
/* 315 */       vehicle.embarkString = "enter";
/* 316 */       vehicle.embarksString = "enters";
/* 317 */       vehicle.name = item.getName();
/* 318 */       vehicle.maxDepth = -0.7F;
/* 319 */       vehicle.maxHeightDiff = 0.04F;
/* 320 */       vehicle.commandType = 2;
/* 321 */       Seat[] hitches = new Seat[1];
/* 322 */       hitches[0] = new Seat((byte)2);
/* 323 */       (hitches[0]).offx = -3.0F;
/* 324 */       (hitches[0]).offy = -1.0F;
/* 325 */       vehicle.addHitchSeats(hitches);
/*     */     } 
/* 327 */     if (item.getTemplateId() == 863) {
/*     */       
/* 329 */       vehicle.setUnmountable(true);
/* 330 */       vehicle.createOnlyPassengerSeats(1);
/*     */       
/* 332 */       vehicle.setSeatFightMod(0, 0.7F, 0.4F);
/* 333 */       vehicle.creature = false;
/* 334 */       vehicle.embarkString = "enter";
/* 335 */       vehicle.embarksString = "enters";
/* 336 */       vehicle.name = item.getName();
/* 337 */       vehicle.maxDepth = -0.7F;
/* 338 */       vehicle.maxHeightDiff = 0.04F;
/* 339 */       vehicle.commandType = 2;
/* 340 */       Seat[] hitches = new Seat[1];
/* 341 */       hitches[0] = new Seat((byte)2);
/* 342 */       (hitches[0]).offx = -3.0F;
/* 343 */       (hitches[0]).offy = -1.0F;
/* 344 */       vehicle.addHitchSeats(hitches);
/*     */     } 
/* 346 */     if (item.getTemplateId() == 864) {
/*     */       
/* 348 */       vehicle.setUnmountable(true);
/* 349 */       vehicle.createOnlyPassengerSeats(1);
/*     */       
/* 351 */       vehicle.setSeatFightMod(0, 0.7F, 0.4F);
/* 352 */       vehicle.creature = false;
/* 353 */       vehicle.embarkString = "enter";
/* 354 */       vehicle.embarksString = "enters";
/* 355 */       vehicle.name = item.getName();
/* 356 */       vehicle.maxDepth = -0.7F;
/* 357 */       vehicle.maxHeightDiff = 0.04F;
/* 358 */       vehicle.commandType = 2;
/* 359 */       Seat[] hitches = new Seat[1];
/* 360 */       hitches[0] = new Seat((byte)2);
/* 361 */       (hitches[0]).offx = -3.0F;
/* 362 */       (hitches[0]).offy = -1.0F;
/* 363 */       vehicle.addHitchSeats(hitches);
/*     */     } 
/* 365 */     if (item.getTemplateId() == 186) {
/*     */       
/* 367 */       vehicle.setUnmountable(true);
/* 368 */       vehicle.createOnlyPassengerSeats(1);
/*     */       
/* 370 */       vehicle.setSeatFightMod(0, 0.7F, 0.4F);
/* 371 */       vehicle.creature = false;
/* 372 */       vehicle.embarkString = "board";
/* 373 */       vehicle.embarksString = "boards";
/* 374 */       vehicle.name = item.getName();
/* 375 */       vehicle.maxDepth = -0.7F;
/* 376 */       vehicle.maxHeightDiff = 0.04F;
/* 377 */       vehicle.commandType = 2;
/*     */     }
/* 379 */     else if (item.getTemplateId() == 490) {
/*     */       
/* 381 */       vehicle.createPassengerSeats(2);
/* 382 */       vehicle.pilotName = "captain";
/* 383 */       vehicle.creature = false;
/* 384 */       vehicle.name = item.getName();
/* 385 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 0.246F);
/* 386 */       vehicle.setSeatOffset(1, -0.997F, 0.0F, 0.246F);
/* 387 */       vehicle.setSeatOffset(2, -2.018F, 0.0F, 0.246F);
/*     */       
/* 389 */       vehicle.setSeatFightMod(0, 0.7F, 0.4F);
/*     */       
/* 391 */       vehicle.setSeatFightMod(1, 1.5F, 0.4F);
/* 392 */       vehicle.setSeatFightMod(2, 1.5F, 0.4F);
/* 393 */       vehicle.setWindImpact((byte)10);
/* 394 */       vehicle.maxHeight = -0.5F;
/* 395 */       vehicle.skillNeeded = 19.0F;
/* 396 */       vehicle.setMaxSpeed(5.0F);
/* 397 */       vehicle.commandType = 1;
/* 398 */       vehicle.setMaxAllowedLoadDistance(6);
/*     */     }
/* 400 */     else if (item.getTemplateId() == 491) {
/*     */       
/* 402 */       vehicle.createPassengerSeats(4);
/* 403 */       vehicle.pilotName = "captain";
/* 404 */       vehicle.creature = false;
/* 405 */       vehicle.embarkString = "board";
/* 406 */       vehicle.embarksString = "boards";
/* 407 */       vehicle.name = item.getName();
/*     */       
/* 409 */       vehicle.setSeatFightMod(0, 0.9F, 0.4F);
/*     */       
/* 411 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 0.351F);
/* 412 */       vehicle.setSeatOffset(1, -1.392F, 0.378F, 0.351F);
/* 413 */       vehicle.setSeatOffset(2, -2.15F, -0.349F, 0.341F);
/* 414 */       vehicle.setSeatOffset(3, -3.7F, -0.281F, 0.34F);
/* 415 */       vehicle.setSeatOffset(4, -4.39F, 0.14F, 0.352F);
/* 416 */       vehicle.setWindImpact((byte)30);
/* 417 */       vehicle.maxHeight = -0.5F;
/* 418 */       vehicle.skillNeeded = 20.1F;
/* 419 */       vehicle.setMaxSpeed(5.0F);
/* 420 */       vehicle.commandType = 1;
/* 421 */       vehicle.setMaxAllowedLoadDistance(6);
/*     */     }
/* 423 */     else if (item.getTemplateId() == 539) {
/*     */       
/* 425 */       vehicle.createPassengerSeats(3);
/* 426 */       vehicle.pilotName = "driver";
/* 427 */       vehicle.creature = false;
/* 428 */       vehicle.embarkString = "ride";
/* 429 */       vehicle.embarksString = "rides";
/* 430 */       vehicle.name = item.getName();
/*     */ 
/*     */       
/* 433 */       vehicle.setSeatFightMod(0, 0.9F, 0.3F);
/* 434 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 1.458F);
/*     */ 
/*     */       
/* 437 */       vehicle.setSeatFightMod(1, 1.0F, 0.4F);
/* 438 */       vehicle.setSeatOffset(1, 0.448F, 0.729F, 1.529F);
/*     */       
/* 440 */       vehicle.setSeatFightMod(2, 1.0F, 0.4F);
/* 441 */       vehicle.setSeatOffset(2, 0.65F, -0.697F, 1.568F);
/*     */       
/* 443 */       vehicle.setSeatFightMod(3, 1.0F, 0.0F);
/* 444 */       vehicle.setSeatOffset(3, 1.122F, 0.738F, 1.621F);
/*     */       
/* 446 */       vehicle.maxHeightDiff = 0.04F;
/* 447 */       vehicle.maxDepth = -0.7F;
/* 448 */       vehicle.skillNeeded = 20.1F;
/* 449 */       vehicle.setMaxSpeed(0.75F);
/* 450 */       vehicle.commandType = 2;
/* 451 */       Seat[] hitches = new Seat[2];
/* 452 */       hitches[0] = new Seat((byte)2);
/* 453 */       hitches[1] = new Seat((byte)2);
/* 454 */       (hitches[0]).offx = -2.0F;
/* 455 */       (hitches[0]).offy = -1.0F;
/* 456 */       (hitches[1]).offx = -2.0F;
/* 457 */       (hitches[1]).offy = 1.0F;
/* 458 */       vehicle.addHitchSeats(hitches);
/* 459 */       vehicle.setMaxAllowedLoadDistance(4);
/*     */     }
/* 461 */     else if (item.getTemplateId() == 853) {
/*     */       
/* 463 */       vehicle.createPassengerSeats(0);
/* 464 */       vehicle.pilotName = "driver";
/* 465 */       vehicle.creature = false;
/* 466 */       vehicle.embarkString = "ride";
/* 467 */       vehicle.embarksString = "rides";
/* 468 */       vehicle.name = item.getName();
/*     */ 
/*     */       
/* 471 */       vehicle.setSeatFightMod(0, 0.9F, 0.3F);
/* 472 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 1.563F);
/*     */       
/* 474 */       vehicle.maxHeightDiff = 0.04F;
/* 475 */       vehicle.maxDepth = -1.5F;
/* 476 */       vehicle.skillNeeded = 20.1F;
/* 477 */       vehicle.setMaxSpeed(0.5F);
/* 478 */       vehicle.commandType = 2;
/* 479 */       Seat[] hitches = new Seat[2];
/* 480 */       hitches[0] = new Seat((byte)2);
/* 481 */       hitches[1] = new Seat((byte)2);
/* 482 */       (hitches[0]).offx = -2.0F;
/* 483 */       (hitches[0]).offy = -1.0F;
/* 484 */       (hitches[1]).offx = -2.0F;
/* 485 */       (hitches[1]).offy = 1.0F;
/* 486 */       vehicle.addHitchSeats(hitches);
/*     */     }
/* 488 */     else if (item.getTemplateId() == 1410) {
/*     */       
/* 490 */       vehicle.createPassengerSeats(1);
/* 491 */       vehicle.pilotName = "driver";
/* 492 */       vehicle.creature = false;
/* 493 */       vehicle.embarkString = "ride";
/* 494 */       vehicle.embarksString = "rides";
/* 495 */       vehicle.name = item.getName();
/*     */ 
/*     */       
/* 498 */       vehicle.setSeatFightMod(0, 0.9F, 0.3F);
/* 499 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 1.563F);
/*     */       
/* 501 */       vehicle.setSeatFightMod(1, 1.0F, 0.4F);
/* 502 */       vehicle.setSeatOffset(1, 4.05F, 0.0F, 0.84F);
/*     */       
/* 504 */       vehicle.maxHeightDiff = 0.04F;
/* 505 */       vehicle.maxDepth = -1.5F;
/* 506 */       vehicle.skillNeeded = 20.1F;
/* 507 */       vehicle.setMaxSpeed(0.5F);
/* 508 */       vehicle.commandType = 2;
/* 509 */       Seat[] hitches = new Seat[2];
/* 510 */       hitches[0] = new Seat((byte)2);
/* 511 */       hitches[1] = new Seat((byte)2);
/* 512 */       (hitches[0]).offx = -2.0F;
/* 513 */       (hitches[0]).offy = -1.0F;
/* 514 */       (hitches[1]).offx = -2.0F;
/* 515 */       (hitches[1]).offy = 1.0F;
/* 516 */       vehicle.addHitchSeats(hitches);
/*     */     }
/* 518 */     else if (item.getTemplateId() == 850) {
/*     */       
/* 520 */       if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
/* 521 */         vehicle.createPassengerSeats(1);
/*     */       } else {
/* 523 */         vehicle.createPassengerSeats(0);
/* 524 */       }  vehicle.pilotName = "driver";
/* 525 */       vehicle.creature = false;
/* 526 */       vehicle.embarkString = "ride";
/* 527 */       vehicle.embarksString = "rides";
/* 528 */       vehicle.name = item.getName();
/*     */ 
/*     */       
/* 531 */       vehicle.setSeatFightMod(0, 0.9F, 0.3F);
/* 532 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 1.453F);
/*     */       
/* 534 */       if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
/*     */         
/* 536 */         vehicle.setSeatFightMod(1, 1.0F, 0.4F);
/* 537 */         vehicle.setSeatOffset(1, 4.05F, 0.0F, 0.84F);
/*     */       } 
/*     */       
/* 540 */       vehicle.maxHeightDiff = 0.04F;
/* 541 */       vehicle.maxDepth = -0.7F;
/* 542 */       vehicle.skillNeeded = 21.0F;
/* 543 */       vehicle.setMaxSpeed(0.7F);
/* 544 */       vehicle.commandType = 2;
/* 545 */       Seat[] hitches = new Seat[4];
/* 546 */       hitches[0] = new Seat((byte)2);
/* 547 */       hitches[1] = new Seat((byte)2);
/* 548 */       hitches[2] = new Seat((byte)2);
/* 549 */       hitches[3] = new Seat((byte)2);
/* 550 */       (hitches[0]).offx = -2.0F;
/* 551 */       (hitches[0]).offy = -1.0F;
/* 552 */       (hitches[1]).offx = -2.0F;
/* 553 */       (hitches[1]).offy = 1.0F;
/* 554 */       (hitches[2]).offx = -5.0F;
/* 555 */       (hitches[2]).offy = -1.0F;
/* 556 */       (hitches[3]).offx = -5.0F;
/* 557 */       (hitches[3]).offy = 1.0F;
/* 558 */       vehicle.addHitchSeats(hitches);
/* 559 */       vehicle.setMaxAllowedLoadDistance(4);
/*     */     }
/* 561 */     else if (item.getTemplateId() == 541) {
/*     */       
/* 563 */       vehicle.createPassengerSeats(6);
/* 564 */       vehicle.pilotName = "captain";
/* 565 */       vehicle.creature = false;
/* 566 */       vehicle.embarkString = "board";
/* 567 */       vehicle.embarksString = "boards";
/* 568 */       vehicle.name = item.getName();
/*     */       
/* 570 */       vehicle.setSeatFightMod(0, 0.9F, 0.9F);
/* 571 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 3.02F);
/* 572 */       vehicle.setSeatOffset(1, -7.192F, -1.036F, 2.16F);
/* 573 */       vehicle.setSeatOffset(2, 3.0F, 1.287F, 2.47F);
/* 574 */       vehicle.setSeatOffset(3, -3.657F, 1.397F, 1.93F);
/* 575 */       vehicle.setSeatOffset(4, 2.858F, -1.076F, 2.473F);
/* 576 */       vehicle.setSeatOffset(5, -5.625F, 0.679F, 1.926F);
/* 577 */       vehicle.setSeatOffset(6, -2.3F, -1.838F, 1.93F);
/* 578 */       vehicle.setWindImpact((byte)60);
/* 579 */       vehicle.maxHeight = -2.0F;
/* 580 */       vehicle.skillNeeded = 21.0F;
/* 581 */       vehicle.setMaxSpeed(3.8F);
/* 582 */       vehicle.commandType = 1;
/* 583 */       vehicle.setMaxAllowedLoadDistance(12);
/*     */     }
/* 585 */     else if (item.getTemplateId() == 540) {
/*     */       
/* 587 */       vehicle.createPassengerSeats(8);
/* 588 */       vehicle.pilotName = "captain";
/* 589 */       vehicle.creature = false;
/* 590 */       vehicle.embarkString = "board";
/* 591 */       vehicle.embarksString = "boards";
/* 592 */       vehicle.name = item.getName();
/*     */       
/* 594 */       vehicle.setSeatFightMod(0, 0.9F, 0.9F);
/* 595 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 4.011F);
/* 596 */       vehicle.setSeatOffset(1, -16.042F, -0.901F, 3.96F);
/* 597 */       vehicle.setSeatOffset(2, -7.629F, 0.0F, 14.591F);
/* 598 */       vehicle.setSeatOffset(3, -4.411F, -2.097F, 3.51F);
/* 599 */       vehicle.setSeatOffset(4, -16.01F, 0.838F, 3.96F);
/* 600 */       vehicle.setSeatOffset(5, -9.588F, -1.855F, 1.802F);
/* 601 */       vehicle.setSeatOffset(6, -11.08F, 2.451F, 1.805F);
/* 602 */       vehicle.setSeatOffset(7, -4.411F, 1.774F, 3.52F);
/* 603 */       vehicle.setSeatOffset(8, -1.813F, -1.872F, 3.789F);
/* 604 */       vehicle.setWindImpact((byte)80);
/* 605 */       vehicle.maxHeight = -2.0F;
/* 606 */       vehicle.skillNeeded = 22.0F;
/* 607 */       vehicle.setMaxSpeed(3.5F);
/* 608 */       vehicle.commandType = 1;
/* 609 */       vehicle.setMaxAllowedLoadDistance(12);
/*     */     }
/* 611 */     else if (item.getTemplateId() == 542) {
/*     */       
/* 613 */       vehicle.createPassengerSeats(12);
/* 614 */       vehicle.pilotName = "captain";
/* 615 */       vehicle.creature = false;
/* 616 */       vehicle.embarkString = "board";
/* 617 */       vehicle.embarksString = "boards";
/* 618 */       vehicle.name = item.getName();
/*     */       
/* 620 */       vehicle.setSeatFightMod(0, 0.9F, 0.9F);
/*     */       
/* 622 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 0.787F);
/*     */       
/* 624 */       vehicle.setSeatOffset(1, -7.713F, -0.41F, 0.485F);
/* 625 */       vehicle.setSeatOffset(2, -9.722F, 0.455F, 0.417F);
/* 626 */       vehicle.setSeatOffset(3, -3.85F, -0.412F, 0.598F);
/* 627 */       vehicle.setSeatOffset(4, -11.647F, 0.0F, 0.351F);
/* 628 */       vehicle.setSeatOffset(5, -1.916F, -0.211F, 0.651F);
/* 629 */       vehicle.setSeatOffset(6, -12.627F, 0.018F, 0.469F);
/* 630 */       vehicle.setSeatOffset(7, -5.773F, 0.429F, 0.547F);
/* 631 */       vehicle.setSeatOffset(8, -2.882F, 0.388F, 0.626F);
/* 632 */       vehicle.setSeatOffset(9, -8.726F, 0.013F, 0.445F);
/* 633 */       vehicle.setSeatOffset(10, -10.66F, -0.162F, 0.387F);
/* 634 */       vehicle.setSeatOffset(11, -7.708F, 0.454F, 0.479F);
/* 635 */       vehicle.setSeatOffset(12, -5.773F, -0.429F, 0.547F);
/* 636 */       vehicle.setWindImpact((byte)50);
/* 637 */       vehicle.maxHeight = -0.5F;
/* 638 */       vehicle.skillNeeded = 23.0F;
/* 639 */       vehicle.setMaxSpeed(4.1F);
/* 640 */       vehicle.commandType = 1;
/* 641 */       vehicle.setMaxAllowedLoadDistance(8);
/*     */     }
/* 643 */     else if (item.getTemplateId() == 543) {
/*     */       
/* 645 */       vehicle.createPassengerSeats(13);
/* 646 */       vehicle.pilotName = "captain";
/* 647 */       vehicle.creature = false;
/* 648 */       vehicle.embarkString = "board";
/* 649 */       vehicle.embarksString = "boards";
/* 650 */       vehicle.name = item.getName();
/*     */ 
/*     */       
/* 653 */       vehicle.setSeatFightMod(0, 0.9F, 0.9F);
/* 654 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 3.866F);
/* 655 */       vehicle.setSeatOffset(1, -6.98F, 0.0F, 12.189F);
/* 656 */       vehicle.setSeatOffset(2, -14.716F, -0.202F, 3.402F);
/* 657 */       vehicle.setSeatOffset(3, -4.417F, 1.024F, 2.013F);
/* 658 */       vehicle.setSeatOffset(4, 1.206F, -0.657F, 4.099F);
/* 659 */       vehicle.setSeatOffset(5, -7.953F, 0.028F, 0.731F);
/* 660 */       vehicle.setSeatOffset(6, -5.317F, -1.134F, 1.941F);
/* 661 */       vehicle.setSeatOffset(7, -7.518F, 1.455F, 0.766F);
/* 662 */       vehicle.setSeatOffset(8, -2.598F, -0.104F, 2.22F);
/* 663 */       vehicle.setSeatOffset(9, -12.46F, 0.796F, 2.861F);
/* 664 */       vehicle.setSeatOffset(10, -12.417F, -0.82F, 2.852F);
/* 665 */       vehicle.setSeatOffset(11, -4.046F, -0.536F, 2.056F);
/* 666 */       vehicle.setSeatOffset(12, -1.089F, 1.004F, 3.65F);
/* 667 */       vehicle.setSeatOffset(13, -0.942F, -0.845F, 3.678F);
/* 668 */       vehicle.setWindImpact((byte)70);
/* 669 */       vehicle.maxHeight = -2.0F;
/* 670 */       vehicle.skillNeeded = 24.0F;
/* 671 */       vehicle.setMaxSpeed(4.0F);
/* 672 */       vehicle.commandType = 1;
/* 673 */       vehicle.setMaxAllowedLoadDistance(12);
/*     */     }
/* 675 */     else if (item.getTemplateId() == 931) {
/*     */ 
/*     */       
/* 678 */       vehicle.createPassengerSeats(0);
/* 679 */       vehicle.pilotName = "pusher";
/* 680 */       vehicle.creature = false;
/* 681 */       vehicle.embarkString = "board";
/* 682 */       vehicle.embarksString = "boards";
/* 683 */       vehicle.name = item.getName();
/* 684 */       vehicle.setWindImpact((byte)0);
/* 685 */       vehicle.maxHeight = 6000.0F;
/* 686 */       vehicle.maxDepth = -1.0F;
/* 687 */       vehicle.skillNeeded = 200.0F;
/* 688 */       vehicle.setMaxSpeed(3.0F);
/*     */     }
/* 690 */     else if (item.getTemplateId() == 936 || item.getTemplateId() == 937) {
/*     */ 
/*     */       
/* 693 */       vehicle.createPassengerSeats(0);
/* 694 */       vehicle.pilotName = "mover";
/* 695 */       vehicle.creature = false;
/* 696 */       vehicle.embarkString = "board";
/* 697 */       vehicle.embarksString = "boards";
/* 698 */       vehicle.name = item.getName();
/* 699 */       vehicle.setWindImpact((byte)0);
/* 700 */       vehicle.maxHeight = 6000.0F;
/* 701 */       vehicle.maxDepth = -1.0F;
/* 702 */       vehicle.skillNeeded = 200.0F;
/* 703 */       vehicle.setMaxSpeed(0.0F);
/*     */     }
/* 705 */     else if (item.getTemplateId() == 263 || item
/* 706 */       .getTemplateId() == 265) {
/*     */       
/* 708 */       vehicle.createOnlyPassengerSeats(1);
/* 709 */       vehicle.setChair(true);
/*     */       
/* 711 */       vehicle.creature = false;
/* 712 */       vehicle.embarkString = "sit";
/* 713 */       vehicle.embarksString = "sits";
/* 714 */       vehicle.name = item.getName();
/*     */       
/* 716 */       vehicle.setSeatFightMod(0, 1.0F, 0.4F);
/* 717 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.59F);
/*     */       
/* 719 */       vehicle.setWindImpact((byte)0);
/* 720 */       vehicle.maxDepth = -0.7F;
/* 721 */       vehicle.maxHeightDiff = 0.04F;
/* 722 */       vehicle.commandType = 2;
/*     */     }
/* 724 */     else if (item.getTemplateId() == 404 || item
/* 725 */       .getTemplateId() == 891 || item
/* 726 */       .getTemplateId() == 924) {
/*     */       
/* 728 */       vehicle.createOnlyPassengerSeats(2);
/* 729 */       vehicle.setChair(true);
/*     */       
/* 731 */       vehicle.creature = false;
/* 732 */       vehicle.embarkString = "sit";
/* 733 */       vehicle.embarksString = "sits";
/* 734 */       vehicle.name = item.getName();
/*     */       
/* 736 */       vehicle.setSeatFightMod(0, 1.0F, 0.4F);
/* 737 */       vehicle.setSeatOffset(0, 0.0F, -0.4F, 0.59F);
/* 738 */       vehicle.setSeatFightMod(1, 1.0F, 0.4F);
/* 739 */       vehicle.setSeatOffset(1, 0.0F, 0.4F, 0.59F);
/*     */       
/* 741 */       vehicle.setWindImpact((byte)0);
/* 742 */       vehicle.maxDepth = -0.7F;
/* 743 */       vehicle.maxHeightDiff = 0.04F;
/* 744 */       vehicle.commandType = 2;
/*     */     }
/* 746 */     else if (item.getTemplateId() == 261 || item
/* 747 */       .getTemplateId() == 913 || item
/* 748 */       .getTemplateId() == 914 || item
/* 749 */       .getTemplateId() == 915) {
/*     */       
/* 751 */       vehicle.createOnlyPassengerSeats(1);
/* 752 */       vehicle.setChair(true);
/*     */       
/* 754 */       vehicle.creature = false;
/* 755 */       vehicle.embarkString = "sit";
/* 756 */       vehicle.embarksString = "sits";
/* 757 */       vehicle.name = item.getName();
/*     */       
/* 759 */       vehicle.setSeatFightMod(0, 1.0F, 0.4F);
/* 760 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.57F);
/*     */       
/* 762 */       vehicle.setWindImpact((byte)0);
/* 763 */       vehicle.maxDepth = -0.7F;
/* 764 */       vehicle.maxHeightDiff = 0.04F;
/* 765 */       vehicle.commandType = 2;
/*     */     }
/* 767 */     else if (item.getTemplateId() == 894) {
/*     */       
/* 769 */       vehicle.createOnlyPassengerSeats(1);
/* 770 */       vehicle.setChair(true);
/*     */       
/* 772 */       vehicle.creature = false;
/* 773 */       vehicle.embarkString = "sit";
/* 774 */       vehicle.embarksString = "sits";
/* 775 */       vehicle.name = item.getName();
/*     */       
/* 777 */       vehicle.setSeatFightMod(0, 1.0F, 0.4F);
/* 778 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.63F);
/*     */       
/* 780 */       vehicle.setWindImpact((byte)0);
/* 781 */       vehicle.maxDepth = -0.7F;
/* 782 */       vehicle.maxHeightDiff = 0.04F;
/* 783 */       vehicle.commandType = 2;
/*     */     }
/* 785 */     else if (item.getTemplateId() == 1313 || item
/* 786 */       .getTemplateId() == 484 || item
/* 787 */       .getTemplateId() == 890) {
/*     */       
/* 789 */       vehicle.createOnlyPassengerSeats(1);
/* 790 */       vehicle.setBed(true);
/*     */       
/* 792 */       vehicle.creature = false;
/* 793 */       vehicle.embarkString = "lie down";
/* 794 */       vehicle.embarksString = "lies down";
/* 795 */       vehicle.name = item.getName();
/*     */       
/* 797 */       vehicle.setSeatFightMod(0, 1.0F, 0.4F);
/* 798 */       vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.57F);
/*     */       
/* 800 */       vehicle.setWindImpact((byte)0);
/* 801 */       vehicle.maxDepth = -0.7F;
/* 802 */       vehicle.maxHeightDiff = 0.04F;
/* 803 */       vehicle.commandType = 2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroyVehicle(long id) {
/* 813 */     Vehicle vehicle = vehicles.get(Long.valueOf(id));
/* 814 */     if (vehicle != null) {
/*     */       
/* 816 */       vehicle.kickAll();
/* 817 */       vehicle.seats = Vehicle.EMPTYSEATS;
/* 818 */       if (vehicle.getDraggers() != null)
/*     */       {
/* 820 */         vehicle.purgeDraggers();
/*     */       }
/*     */     } 
/* 823 */     vehicles.remove(Long.valueOf(id));
/*     */     
/* 825 */     ItemSettings.remove(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getNumberOfVehicles() {
/* 835 */     if (vehicles != null)
/*     */     {
/* 837 */       return vehicles.size();
/*     */     }
/*     */ 
/*     */     
/* 841 */     logger.warning("vehicles Map is null");
/* 842 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void removeDragger(Creature dragger) {
/* 848 */     for (Vehicle draggedVehicle : (Vehicle[])vehicles.values().toArray((Object[])new Vehicle[vehicles.size()])) {
/*     */       
/* 850 */       if (draggedVehicle.removeDragger(dragger)) {
/*     */         
/* 852 */         dragger.setHitched(null, false);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Vehicles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */