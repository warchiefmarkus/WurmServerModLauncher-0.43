/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
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
/*     */ public class BodyTemplate
/*     */ {
/*  35 */   private static Logger logger = Logger.getLogger(BodyTemplate.class.getName());
/*     */   public static final byte TYPE_HUMAN = 0;
/*     */   public static final byte TYPE_HORSE = 1;
/*     */   public static final byte TYPE_BEAR = 2;
/*     */   public static final byte TYPE_DOG = 3;
/*     */   public static final byte TYPE_ETTIN = 4;
/*     */   public static final byte TYPE_CYCLOPS = 5;
/*     */   public static final byte TYPE_DRAGON = 6;
/*     */   public static final byte TYPE_BIRD = 7;
/*     */   public static final byte TYPE_SPIDER = 8;
/*     */   public static final byte TYPE_SNAKE = 9;
/*  46 */   byte type = 0;
/*     */   public static final byte body = 0;
/*  48 */   String bodyS = "body";
/*     */   
/*     */   public static final byte head = 1;
/*  51 */   String headS = "head";
/*     */   public static final byte torso = 2;
/*  53 */   String torsoS = "torso";
/*     */   public static final byte leftArm = 3;
/*  55 */   String leftArmS = "left arm";
/*     */   public static final byte rightArm = 4;
/*  57 */   String rightArmS = "right arm";
/*     */   public static final byte leftOverArm = 5;
/*  59 */   String leftOverArmS = "left upper arm";
/*     */   public static final byte rightOverArm = 6;
/*  61 */   String rightOverArmS = "right upper arm";
/*     */   public static final byte leftThigh = 7;
/*  63 */   String leftThighS = "left thigh";
/*     */   public static final byte rightThigh = 8;
/*  65 */   String rightThighS = "right thigh";
/*     */   public static final byte leftUnderArm = 9;
/*  67 */   String leftUnderArmS = "left underarm";
/*     */   public static final byte rightUnderArm = 10;
/*  69 */   String rightUnderArmS = "right underarm";
/*     */   public static final byte leftCalf = 11;
/*  71 */   String leftCalfS = "left calf";
/*     */   public static final byte rightCalf = 12;
/*  73 */   String rightCalfS = "right calf";
/*     */   public static final byte leftHand = 13;
/*  75 */   String leftHandS = "left hand";
/*     */   public static final byte rightHand = 14;
/*  77 */   String rightHandS = "right hand";
/*     */   public static final byte leftFoot = 15;
/*  79 */   String leftFootS = "left foot";
/*     */   public static final byte rightFoot = 16;
/*  81 */   String rightFootS = "right foot";
/*     */   public static final byte neck = 17;
/*  83 */   String neckS = "neck";
/*     */   public static final byte leftEye = 18;
/*  85 */   String leftEyeS = "left eye";
/*     */   public static final byte rightEye = 19;
/*  87 */   String rightEyeS = "right eye";
/*     */   public static final byte centerEye = 20;
/*  89 */   String centerEyeS = "center eye";
/*     */   public static final byte chest = 21;
/*  91 */   String chestS = "chest";
/*     */   public static final byte topBack = 22;
/*  93 */   String topBackS = "top of the back";
/*     */   public static final byte stomach = 23;
/*  95 */   String stomachS = "stomach";
/*     */   public static final byte lowerBack = 24;
/*  97 */   String lowerBackS = "lower back";
/*     */   public static final byte crotch = 25;
/*  99 */   String crotchS = "crotch";
/*     */   public static final byte leftShoulder = 26;
/* 101 */   String leftShoulderS = "left shoulder";
/*     */   public static final byte rightShoulder = 27;
/* 103 */   String rightShoulderS = "right shoulder";
/*     */   public static final byte secondHead = 28;
/* 105 */   String secondHeadS = "second head";
/*     */   public static final byte face = 29;
/* 107 */   String faceS = "face";
/*     */   public static final byte leftLeg = 30;
/* 109 */   String leftLegS = "left leg";
/*     */   public static final byte rightLeg = 31;
/* 111 */   String rightLegS = "right leg";
/*     */   public static final byte hip = 32;
/* 113 */   String hipS = "hip";
/*     */   public static final byte baseOfNose = 33;
/* 115 */   String baseOfNoseS = "baseOfNose";
/*     */   public static final byte legs = 34;
/* 117 */   String legsS = "legs";
/*     */   
/*     */   public static final byte tabardSlot = 35;
/*     */   public static final byte neckSlot = 36;
/*     */   public static final byte lHeldSlot = 37;
/*     */   public static final byte rHeldSlot = 38;
/*     */   public static final byte lRingSlot = 39;
/*     */   public static final byte rRingSlot = 40;
/*     */   public static final byte quiverSlot = 41;
/*     */   public static final byte backSlot = 42;
/*     */   public static final byte beltSlot = 43;
/*     */   public static final byte shieldSlot = 44;
/*     */   public static final byte capeSlot = 45;
/*     */   public static final byte lShoulderSlot = 46;
/*     */   public static final byte rShoulderSlot = 47;
/*     */   public static final byte inventory = 48;
/* 133 */   public String[] typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BodyTemplate(byte aType) {
/* 141 */     this.type = aType;
/*     */   }
/*     */ 
/*     */   
/*     */   void buildBody(Item[] spaces, Creature owner) {
/* 146 */     spaces[0].setOwner(owner.getWurmId(), true);
/* 147 */     spaces[0].insertItem(spaces[1]);
/* 148 */     spaces[1].insertItem(spaces[29]);
/* 149 */     spaces[0].insertItem(spaces[2]);
/* 150 */     spaces[2].insertItem(spaces[3]);
/* 151 */     spaces[2].insertItem(spaces[4]);
/* 152 */     spaces[3].insertItem(spaces[13]);
/* 153 */     spaces[4].insertItem(spaces[14]);
/*     */     
/* 155 */     spaces[2].insertItem(spaces[34]);
/* 156 */     spaces[34].insertItem(spaces[15]);
/* 157 */     spaces[34].insertItem(spaces[16]);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos() throws Exception {
/* 162 */     int rand = Server.rand.nextInt(1000);
/* 163 */     if (rand < 30)
/* 164 */       return 1; 
/* 165 */     if (rand < 80)
/* 166 */       return 5; 
/* 167 */     if (rand < 130)
/* 168 */       return 6; 
/* 169 */     if (rand < 180)
/* 170 */       return 7; 
/* 171 */     if (rand < 230)
/* 172 */       return 8; 
/* 173 */     if (rand < 280)
/* 174 */       return 9; 
/* 175 */     if (rand < 320)
/* 176 */       return 10; 
/* 177 */     if (rand < 370)
/* 178 */       return 11; 
/* 179 */     if (rand < 420)
/* 180 */       return 12; 
/* 181 */     if (rand < 460)
/* 182 */       return 13; 
/* 183 */     if (rand < 500)
/* 184 */       return 14; 
/* 185 */     if (rand < 540)
/* 186 */       return 15; 
/* 187 */     if (rand < 580)
/* 188 */       return 16; 
/* 189 */     if (rand < 600)
/* 190 */       return 17; 
/* 191 */     if (rand < 601)
/* 192 */       return 18; 
/* 193 */     if (rand < 602)
/* 194 */       return 19; 
/* 195 */     if (rand < 730)
/* 196 */       return 21; 
/* 197 */     if (rand < 780)
/* 198 */       return 22; 
/* 199 */     if (rand < 830)
/* 200 */       return 23; 
/* 201 */     if (rand < 890)
/* 202 */       return 24; 
/* 203 */     if (rand < 900)
/* 204 */       return 25; 
/* 205 */     if (rand < 950)
/* 206 */       return 26; 
/* 207 */     if (rand < 1000) {
/* 208 */       return 27;
/*     */     }
/* 210 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getUpperLeftWoundPos() throws Exception {
/* 215 */     int rand = Server.rand.nextInt(100);
/* 216 */     if (rand < 3)
/* 217 */       return 1; 
/* 218 */     if (rand < 40)
/* 219 */       return 5; 
/* 220 */     if (rand < 50)
/* 221 */       return 17; 
/* 222 */     if (rand < 51)
/* 223 */       return 18; 
/* 224 */     if (rand < 60)
/* 225 */       return 21; 
/* 226 */     if (rand < 78)
/* 227 */       return 22; 
/* 228 */     if (rand < 100) {
/* 229 */       return 26;
/*     */     }
/* 231 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getUpperRightWoundPos() throws Exception {
/* 236 */     int rand = Server.rand.nextInt(100);
/* 237 */     if (rand < 3)
/* 238 */       return 1; 
/* 239 */     if (rand < 13)
/* 240 */       return 17; 
/* 241 */     if (rand < 50)
/* 242 */       return 6; 
/* 243 */     if (rand < 51)
/* 244 */       return 19; 
/* 245 */     if (rand < 63)
/* 246 */       return 21; 
/* 247 */     if (rand < 78)
/* 248 */       return 22; 
/* 249 */     if (rand < 100) {
/* 250 */       return 27;
/*     */     }
/* 252 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getHighWoundPos() throws Exception {
/* 257 */     int rand = Server.rand.nextInt(100);
/* 258 */     if (rand < 40)
/* 259 */       return 1; 
/* 260 */     if (rand < 60)
/* 261 */       return 17; 
/* 262 */     if (rand < 61)
/* 263 */       return 18; 
/* 264 */     if (rand < 62)
/* 265 */       return 19; 
/* 266 */     if (rand < 81)
/* 267 */       return 26; 
/* 268 */     if (rand < 100) {
/* 269 */       return 27;
/*     */     }
/* 271 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getMidLeftWoundPos() throws Exception {
/* 276 */     int rand = Server.rand.nextInt(100);
/* 277 */     if (rand < 18)
/* 278 */       return 5; 
/* 279 */     if (rand < 48)
/* 280 */       return 7; 
/* 281 */     if (rand < 58)
/* 282 */       return 9; 
/* 283 */     if (rand < 66)
/* 284 */       return 13; 
/* 285 */     if (rand < 73)
/* 286 */       return 21; 
/* 287 */     if (rand < 83)
/* 288 */       return 23; 
/* 289 */     if (rand < 100) {
/* 290 */       return 24;
/*     */     }
/* 292 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getCenterWoundPos() throws Exception {
/* 297 */     int rand = Server.rand.nextInt(100);
/*     */     
/* 299 */     if (rand < 11)
/* 300 */       return 7; 
/* 301 */     if (rand < 22)
/* 302 */       return 8; 
/* 303 */     if (rand < 32)
/* 304 */       return 9; 
/* 305 */     if (rand < 42) {
/* 306 */       return 10;
/*     */     }
/* 308 */     if (rand < 46)
/* 309 */       return 13; 
/* 310 */     if (rand < 50) {
/* 311 */       return 14;
/*     */     }
/* 313 */     if (rand < 73)
/* 314 */       return 21; 
/* 315 */     if (rand < 100) {
/* 316 */       return 23;
/*     */     }
/* 318 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getMidRightWoundPos() throws Exception {
/* 323 */     int rand = Server.rand.nextInt(100);
/*     */     
/* 325 */     if (rand < 18) {
/* 326 */       return 6;
/*     */     }
/* 328 */     if (rand < 48) {
/* 329 */       return 8;
/*     */     }
/* 331 */     if (rand < 58) {
/* 332 */       return 10;
/*     */     }
/* 334 */     if (rand < 66) {
/* 335 */       return 14;
/*     */     }
/* 337 */     if (rand < 73) {
/* 338 */       return 21;
/*     */     }
/* 340 */     if (rand < 83)
/* 341 */       return 23; 
/* 342 */     if (rand < 100) {
/* 343 */       return 24;
/*     */     }
/* 345 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getLowerLeftWoundPos() throws Exception {
/* 350 */     int rand = Server.rand.nextInt(100);
/*     */     
/* 352 */     if (rand < 48) {
/* 353 */       return 7;
/*     */     }
/* 355 */     if (rand < 58) {
/* 356 */       return 9;
/*     */     }
/* 358 */     if (rand < 78) {
/* 359 */       return 11;
/*     */     }
/* 361 */     if (rand < 98)
/* 362 */       return 15; 
/* 363 */     if (rand < 100) {
/* 364 */       return 25;
/*     */     }
/* 366 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getLowWoundPos() throws Exception {
/* 371 */     int rand = Server.rand.nextInt(100);
/*     */     
/* 373 */     if (rand < 10)
/* 374 */       return 7; 
/* 375 */     if (rand < 20) {
/* 376 */       return 8;
/*     */     }
/* 378 */     if (rand < 40)
/* 379 */       return 11; 
/* 380 */     if (rand < 60) {
/* 381 */       return 12;
/*     */     }
/* 383 */     if (rand < 75)
/* 384 */       return 15; 
/* 385 */     if (rand < 90) {
/* 386 */       return 16;
/*     */     }
/* 388 */     if (rand < 100) {
/* 389 */       return 25;
/*     */     }
/* 391 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */   
/*     */   byte getLowerRightWoundPos() throws Exception {
/* 396 */     int rand = Server.rand.nextInt(100);
/*     */     
/* 398 */     if (rand < 48) {
/* 399 */       return 8;
/*     */     }
/* 401 */     if (rand < 58) {
/* 402 */       return 10;
/*     */     }
/* 404 */     if (rand < 78) {
/* 405 */       return 12;
/*     */     }
/* 407 */     if (rand < 98)
/* 408 */       return 16; 
/* 409 */     if (rand < 100) {
/* 410 */       return 25;
/*     */     }
/* 412 */     throw new WurmServerException("Bad randomizer");
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
/*     */   public final String getBodyPositionDescription(byte position) {
/*     */     String lDescription;
/* 427 */     if (position >= 0 && position <= this.typeString.length) {
/*     */       
/* 429 */       lDescription = this.typeString[position];
/*     */     }
/*     */     else {
/*     */       
/* 433 */       lDescription = "Unknown position-" + position;
/*     */     } 
/*     */     
/* 436 */     return lDescription;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte convertToArmorEquipementSlot(byte bodyPart) {
/* 441 */     byte toReturn = -1;
/*     */     
/* 443 */     switch (bodyPart) {
/*     */       
/*     */       case 1:
/*     */       case 28:
/* 447 */         toReturn = 2;
/*     */         break;
/*     */       case 29:
/* 450 */         toReturn = 25;
/*     */         break;
/*     */       case 0:
/*     */       case 2:
/* 454 */         toReturn = 3;
/*     */         break;
/*     */       case 42:
/* 457 */         toReturn = 20;
/*     */         break;
/*     */       case 34:
/* 460 */         toReturn = 4;
/*     */         break;
/*     */       case 43:
/* 463 */         toReturn = 22;
/*     */         break;
/*     */       case 3:
/* 466 */         toReturn = 5;
/*     */         break;
/*     */       case 4:
/* 469 */         toReturn = 6;
/*     */         break;
/*     */       case 13:
/* 472 */         toReturn = 7;
/*     */         break;
/*     */       case 14:
/* 475 */         toReturn = 8;
/*     */         break;
/*     */       case 15:
/* 478 */         toReturn = 9;
/*     */         break;
/*     */       case 16:
/* 481 */         toReturn = 10;
/*     */         break;
/*     */       case 46:
/* 484 */         toReturn = 18;
/*     */         break;
/*     */       case 47:
/* 487 */         toReturn = 19;
/*     */         break;
/*     */       case 39:
/* 490 */         toReturn = 17;
/*     */         break;
/*     */       case 40:
/* 493 */         toReturn = 16;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 499 */     if (toReturn == -1)
/*     */     {
/* 501 */       logger.log(Level.FINEST, "Could not convert BodyTemplate bodypart to Equipementpart, Constant number: " + bodyPart);
/*     */     }
/*     */ 
/*     */     
/* 505 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte convertToItemEquipementSlot(byte bodyPart) {
/* 511 */     switch (bodyPart) {
/*     */       
/*     */       case 1:
/* 514 */         return 2;
/*     */       case 29:
/* 516 */         return 25;
/*     */       case 13:
/* 518 */         return 7;
/*     */       case 37:
/* 520 */         return 0;
/*     */       case 14:
/* 522 */         return 8;
/*     */       case 38:
/* 524 */         return 1;
/*     */       case 0:
/*     */       case 2:
/* 527 */         return 12;
/*     */       case 42:
/* 529 */         return 20;
/*     */       case 3:
/* 531 */         return 26;
/*     */       case 44:
/* 533 */         return 11;
/*     */       case 4:
/* 535 */         return 27;
/*     */       case 34:
/* 537 */         return 13;
/*     */       case 43:
/* 539 */         return 22;
/*     */       case 41:
/* 541 */         return 23;
/*     */       case 15:
/* 543 */         return 9;
/*     */       case 16:
/* 545 */         return 10;
/*     */       case 35:
/* 547 */         return 15;
/*     */       case 40:
/* 549 */         return 16;
/*     */       case 39:
/* 551 */         return 17;
/*     */       case 36:
/* 553 */         return 21;
/*     */       case 46:
/* 555 */         return 18;
/*     */       case 47:
/* 557 */         return 19;
/*     */     } 
/*     */     
/* 560 */     logger.log(Level.FINEST, "Could not convert BodyTemplate bodypart to Equipementpart, Constant number: " + bodyPart);
/*     */     
/* 562 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */