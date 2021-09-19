/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.Point;
/*     */ import com.wurmonline.shared.constants.ValreiConstants;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValreiFight
/*     */ {
/*     */   private static final int MAP_SIZE = 7;
/*     */   private static final short MODIFIER_NORMAL = 0;
/*     */   private static final short MODIFIER_BLANK = -1;
/*  19 */   private static final Point test1 = new Point(0, 0);
/*  20 */   private static final Point test2 = new Point(0, 0);
/*     */   
/*     */   private final MapHex mapHex;
/*     */   
/*     */   private final FightEntity fighter1;
/*     */   
/*     */   private final FightEntity fighter2;
/*     */   
/*     */   private ValreiFightHistory fightHistory;
/*     */   
/*     */   private ValreiFightHex[][] fightMap;
/*     */   
/*     */   private Random fightRand;
/*     */ 
/*     */   
/*     */   public ValreiFight(MapHex mapHex, EpicEntity fighter1, EpicEntity fighter2) {
/*  36 */     this.mapHex = mapHex;
/*  37 */     this.fighter1 = new FightEntity(fighter1);
/*  38 */     this.fighter2 = new FightEntity(fighter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValreiFightHistory completeFight(boolean test) {
/*  49 */     this.fightHistory = new ValreiFightHistory(this.mapHex.getId(), this.mapHex.getName());
/*  50 */     this.fightHistory.addFighter(this.fighter1.getEntityId(), this.fighter1.getEntityName());
/*  51 */     this.fightHistory.addFighter(this.fighter2.getEntityId(), this.fighter2.getEntityName());
/*     */     
/*  53 */     if (test) {
/*  54 */       this.fightRand = new Random(System.nanoTime());
/*     */     } else {
/*  56 */       this.fightRand = new Random(this.fightHistory.getFightTime());
/*  57 */     }  this.fightMap = createFightMap();
/*     */     
/*  59 */     moveEntity(this.fighter1, 1, 1);
/*  60 */     moveEntity(this.fighter2, 5, 5);
/*     */     
/*  62 */     this.fighter1.setMaxFavor(25.0F + 0.75F * this.fighter1.rollSkill(105, 106));
/*  63 */     this.fighter1.setMaxKarma(25.0F + 0.75F * this.fighter1.rollSkill(106, 100));
/*     */     
/*  65 */     this.fighter2.setMaxFavor(25.0F + 0.75F * this.fighter2.rollSkill(105, 106));
/*  66 */     this.fighter2.setMaxKarma(25.0F + 0.75F * this.fighter2.rollSkill(106, 100));
/*     */     
/*  68 */     FightEntity currentFighter = this.fighter2;
/*  69 */     if (this.fighter1.rollInitiative() > this.fighter2.rollInitiative()) {
/*  70 */       currentFighter = this.fighter1;
/*     */     }
/*  72 */     while (!this.fightHistory.isFightCompleted()) {
/*     */ 
/*     */       
/*  75 */       if (takeTurn(currentFighter)) {
/*     */         
/*  77 */         if (this.fighter1.getHealth() <= 0.0F && this.fighter2.getHealth() > 0.0F) {
/*     */           
/*  79 */           this.fightHistory.addAction((short)8, ValreiConstants.getEndFightData(this.fighter2.getEntityId()));
/*     */         }
/*  81 */         else if (this.fighter2.getHealth() <= 0.0F && this.fighter1.getHealth() > 0.0F) {
/*     */           
/*  83 */           this.fightHistory.addAction((short)8, ValreiConstants.getEndFightData(this.fighter1.getEntityId()));
/*     */         
/*     */         }
/*     */         else {
/*     */           
/*  88 */           this.fightHistory.addAction((short)8, ValreiConstants.getEndFightData(-1L));
/*     */         } 
/*     */         
/*  91 */         this.fightHistory.setFightCompleted(true);
/*     */       } 
/*     */       
/*  94 */       if (currentFighter == this.fighter2) {
/*  95 */         currentFighter = this.fighter1; continue;
/*     */       } 
/*  97 */       currentFighter = this.fighter2;
/*     */     } 
/*     */     
/* 100 */     if (!test) {
/* 101 */       this.fightHistory.saveActions();
/*     */     }
/* 103 */     return this.fightHistory;
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
/*     */   private boolean takeTurn(FightEntity e) {
/* 115 */     FightEntity opponent = (e == this.fighter1) ? this.fighter2 : this.fighter1;
/* 116 */     int actionCount = 2;
/* 117 */     boolean smartRound = (e.rollSkill(100) > 0.0F);
/* 118 */     float spellRegen = e.rollSkill(100, 101);
/* 119 */     if (spellRegen > 0.0F) {
/*     */       
/* 121 */       float favorGone = e.getMaxKarma() - e.getFavor();
/* 122 */       float karmaGone = e.getMaxFavor() - e.getKarma();
/* 123 */       if (favorGone + karmaGone > 0.0F) {
/*     */         
/* 125 */         float favorPercent = favorGone / (favorGone + karmaGone);
/* 126 */         float karmaPercent = karmaGone / (favorGone + karmaGone);
/*     */         
/* 128 */         e.setFavor(Math.min(e.getMaxFavor(), e.getFavor() + spellRegen * favorPercent));
/* 129 */         e.setKarma(Math.min(e.getMaxKarma(), e.getKarma() + spellRegen * karmaPercent));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 135 */     while (actionCount > 0 && e.getHealth() > 0.0F && opponent.getHealth() > 0.0F) {
/*     */       
/* 137 */       boolean moveTowards = true;
/* 138 */       short currentAction = (short)(4 + this.fightRand.nextInt(4));
/* 139 */       int distance = e.getDistanceTo(opponent);
/*     */       
/* 141 */       if (smartRound) {
/*     */         
/* 143 */         short preferredAction = e.getPreferredAction();
/* 144 */         switch (preferredAction) {
/*     */           
/*     */           case 4:
/* 147 */             if (distance > 1) {
/* 148 */               currentAction = 2; break;
/*     */             } 
/* 150 */             currentAction = preferredAction;
/*     */             break;
/*     */           case 5:
/* 153 */             if (distance <= 2) {
/*     */               
/* 155 */               currentAction = 2;
/* 156 */               moveTowards = false;
/*     */               break;
/*     */             } 
/* 159 */             currentAction = preferredAction;
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 166 */       if ((currentAction == 6 && e.getFavor() < 20.0F) || (currentAction == 7 && e
/* 167 */         .getKarma() < 20.0F))
/*     */       {
/* 169 */         if (distance > 2) {
/* 170 */           currentAction = 5;
/*     */         } else {
/* 172 */           currentAction = 4;
/*     */         } 
/*     */       }
/* 175 */       if (currentAction == 4 && distance > 1) {
/*     */         
/* 177 */         currentAction = 2;
/* 178 */         moveTowards = true;
/*     */       } 
/* 180 */       Point moveTarget = e.getTargetMove(moveTowards, opponent);
/* 181 */       if (currentAction == 2 && !isMoveValid(e, moveTarget.getX(), moveTarget.getY()))
/*     */       {
/* 183 */         if (distance > 1) {
/* 184 */           currentAction = 5;
/*     */         } else {
/* 186 */           currentAction = 4;
/*     */         }  } 
/* 188 */       switch (currentAction) {
/*     */         
/*     */         case 2:
/* 191 */           moveEntity(e, moveTarget.getX(), moveTarget.getY());
/* 192 */           actionCount--;
/*     */         
/*     */         case 4:
/*     */         case 5:
/* 196 */           attackEntity(e, opponent, currentAction);
/* 197 */           actionCount--;
/*     */         
/*     */         case 6:
/*     */         case 7:
/* 201 */           castSpell(e, opponent, currentAction);
/* 202 */           actionCount--;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 208 */     if (e.getHealth() <= 0.0F || opponent.getHealth() <= 0.0F) {
/* 209 */       return true;
/*     */     }
/* 211 */     return false;
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
/*     */   private void moveEntity(FightEntity e, int xPos, int yPos) {
/* 223 */     e.xPos = xPos;
/* 224 */     e.yPos = yPos;
/*     */ 
/*     */ 
/*     */     
/* 228 */     byte[] moveData = ValreiConstants.getMoveData(e.getEntityId(), xPos, yPos);
/* 229 */     this.fightHistory.addAction((short)2, moveData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void attackEntity(FightEntity attacker, FightEntity defender, short attackType) {
/* 236 */     float attackRoll = (attackType == 4) ? attacker.rollSkill(102, 104, attacker.getAttackBuffed()) : attacker.rollSkill(104, 103, attacker.getAttackBuffed());
/* 237 */     float defendRoll = defender.rollSkill(103, 102, defender.getPhysDefBuffed());
/*     */     
/* 239 */     float damage = Math.min(attackRoll, attackRoll - defendRoll);
/*     */     
/* 241 */     if (attackRoll < 0.0F) {
/* 242 */       damage = -1.0F;
/* 243 */     } else if (defendRoll > attackRoll) {
/* 244 */       damage = 0.0F;
/*     */     } 
/* 246 */     if (damage > 0.0F) {
/*     */ 
/*     */       
/* 249 */       damage /= 3.0F;
/* 250 */       defender.setHealth(defender.getHealth() - damage);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     byte[] attackData = ValreiConstants.getAttackData(attacker.getEntityId(), defender.getEntityId(), damage);
/* 258 */     this.fightHistory.addAction(attackType, attackData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void castSpell(FightEntity caster, FightEntity defender, short spellType) {
/* 265 */     float casterRoll = (spellType == 6) ? caster.rollSkill(105, 106) : caster.rollSkill(106, 100);
/* 266 */     float defendRoll = defender.rollSkill(101, 105, defender.getSpellDefBuffed());
/*     */     
/* 268 */     byte s = 1;
/* 269 */     if (spellType == 6) {
/* 270 */       s = caster.getDeitySpell(defender);
/* 271 */     } else if (spellType == 7) {
/* 272 */       s = caster.getSorcerySpell(defender);
/*     */     } 
/* 274 */     float damage = -100.0F;
/* 275 */     switch (s) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 280 */         casterRoll = (spellType == 6) ? caster.rollSkill(105, 106, caster.getAttackBuffed()) : caster.rollSkill(106, 100, caster.getAttackBuffed());
/* 281 */         damage = Math.min(casterRoll, casterRoll - defendRoll);
/* 282 */         if (casterRoll < 0.0F) {
/* 283 */           damage = -1.0F;
/* 284 */         } else if (defendRoll > casterRoll) {
/* 285 */           damage = 0.0F;
/*     */         } 
/* 287 */         if (damage > 0.0F) {
/*     */           
/* 289 */           damage /= 2.0F;
/* 290 */           defender.setHealth(defender.getHealth() - damage);
/*     */           
/* 292 */           if (spellType == 6) {
/* 293 */             caster.setFavor(caster.getFavor() - 20.0F); break;
/*     */           } 
/* 295 */           caster.setKarma(caster.getKarma() - 20.0F);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 303 */         damage = casterRoll;
/* 304 */         if (casterRoll < 0.0F) {
/* 305 */           damage = -1.0F;
/*     */         }
/* 307 */         if (damage > 0.0F) {
/*     */           
/* 309 */           damage /= 2.0F;
/* 310 */           caster.setHealth(Math.min(100.0F, caster.getHealth() + damage));
/*     */           
/* 312 */           caster.setFavor(caster.getFavor() - 30.0F);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 319 */         damage = casterRoll;
/* 320 */         if (casterRoll < 0.0F) {
/* 321 */           damage = -1.0F;
/*     */         }
/* 323 */         if (damage > 0.0F) {
/*     */           
/* 325 */           caster.setAttackBuffed(damage / 50.0F);
/* 326 */           caster.setFavor(caster.getFavor() - 50.0F);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 332 */         damage = casterRoll;
/* 333 */         if (casterRoll < 0.0F) {
/* 334 */           damage = -1.0F;
/*     */         }
/* 336 */         if (damage > 0.0F) {
/*     */           
/* 338 */           caster.setPhysDefBuffed(damage / 50.0F);
/* 339 */           caster.setKarma(caster.getKarma() - 60.0F);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 345 */         damage = casterRoll;
/* 346 */         if (casterRoll < 0.0F) {
/* 347 */           damage = -1.0F;
/*     */         }
/* 349 */         if (damage > 0.0F) {
/*     */           
/* 351 */           caster.setSpellDefBuffed(damage / 50.0F);
/* 352 */           caster.setKarma(caster.getKarma() - 60.0F);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 359 */     byte[] spellData = ValreiConstants.getSpellData(caster.getEntityId(), defender.getEntityId(), s, damage);
/* 360 */     this.fightHistory.addAction(spellType, spellData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ValreiFightHex[][] createFightMap() {
/* 371 */     ValreiFightHex[][] toReturn = new ValreiFightHex[7][7];
/*     */     
/* 373 */     for (int i = 0; i < 7; i++) {
/* 374 */       for (int j = 0; j < 7; j++) {
/*     */         
/* 376 */         toReturn[i][j] = new ValreiFightHex(i, j);
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
/* 392 */         if (j + 1 < 4) {
/*     */ 
/*     */           
/* 395 */           if (i >= 4 + j) {
/* 396 */             toReturn[i][j].setModifier((short)-1);
/*     */           }
/* 398 */         } else if (j + 1 > 4) {
/*     */ 
/*     */           
/* 401 */           if (i <= j - 7)
/* 402 */             toReturn[i][j].setModifier((short)-1); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 406 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean isMoveValid(FightEntity e, int mapX, int mapY) {
/* 411 */     if (this.fightMap == null) {
/* 412 */       return false;
/*     */     }
/* 414 */     if (mapX < 0 || mapY < 0 || mapX >= 7 || mapY >= 7) {
/* 415 */       return false;
/*     */     }
/* 417 */     if (this.fightMap[mapX][mapY].getModifier() == -1) {
/* 418 */       return false;
/*     */     }
/* 420 */     FightEntity opponent = (e == this.fighter1) ? this.fighter2 : this.fighter1;
/* 421 */     if (mapX == opponent.xPos && mapY == opponent.yPos) {
/* 422 */       return false;
/*     */     }
/* 424 */     if (mapX == e.xPos && mapY == e.yPos) {
/* 425 */       return false;
/*     */     }
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   class FightEntity
/*     */   {
/*     */     private int xPos;
/*     */     
/*     */     private int yPos;
/*     */     
/*     */     private float health;
/*     */     
/*     */     private float maxFavor;
/*     */     
/*     */     private float maxKarma;
/*     */     
/*     */     private float favor;
/*     */     
/*     */     private float karma;
/*     */     
/* 447 */     private float attackBuffed = 0.0F;
/*     */     
/* 449 */     private float physDefBuffed = 0.0F;
/*     */     
/* 451 */     private float spellDefBuffed = 0.0F;
/*     */     
/*     */     private EpicEntity entityBase;
/*     */ 
/*     */     
/*     */     FightEntity(EpicEntity entity) {
/* 457 */       this.entityBase = entity;
/* 458 */       this.health = 100.0F;
/* 459 */       this.favor = 100.0F;
/* 460 */       this.karma = 100.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getEntityId() {
/* 465 */       return this.entityBase.getId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getEntityName() {
/* 470 */       return this.entityBase.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public float rollInitiative() {
/* 475 */       float bodyCon = this.entityBase.getCurrentSkill(104);
/* 476 */       bodyCon += this.entityBase.getCurrentSkill(101) / 3.0F;
/*     */       
/* 478 */       return ValreiFight.this.fightRand.nextFloat() * 10.0F + bodyCon / 10.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     public float rollSkill(int skillId) {
/* 483 */       return rollSkill(skillId, -1);
/*     */     }
/*     */ 
/*     */     
/*     */     public float rollSkill(int skillId, int bonusSkillId, float skillBuffed) {
/* 488 */       return rollSkill(skillId, bonusSkillId, 3.0F, skillBuffed);
/*     */     }
/*     */ 
/*     */     
/*     */     public float rollSkill(int skillId, int bonusSkillId) {
/* 493 */       return rollSkill(skillId, bonusSkillId, 3.0F, 0.0F);
/*     */     }
/*     */ 
/*     */     
/*     */     public float rollSkill(int skillId, int bonusSkillId, float bonusModifier, float skillBuffed) {
/* 498 */       EpicEntity.SkillVal skillValue = this.entityBase.getSkill(skillId);
/* 499 */       if (skillValue != null) {
/*     */         
/* 501 */         float actualVal = skillValue.getCurrentVal();
/* 502 */         EpicEntity.SkillVal bonusVal = this.entityBase.getSkill(bonusSkillId);
/* 503 */         if (bonusVal != null) {
/* 504 */           actualVal += bonusVal.getCurrentVal() / bonusModifier;
/*     */         }
/* 506 */         actualVal -= ValreiFight.this.fightRand.nextFloat() * 100.0F;
/* 507 */         if (skillBuffed > 0.0F) {
/* 508 */           actualVal += (100.0F - actualVal) * skillBuffed;
/*     */         }
/* 510 */         return actualVal;
/*     */       } 
/*     */       
/* 513 */       return -100.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getAttackBuffed() {
/* 518 */       return this.attackBuffed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAttackBuffed(float isBuffed) {
/* 523 */       this.attackBuffed = isBuffed;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getPhysDefBuffed() {
/* 528 */       return this.physDefBuffed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPhysDefBuffed(float isBuffed) {
/* 533 */       this.physDefBuffed = isBuffed;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getSpellDefBuffed() {
/* 538 */       return this.spellDefBuffed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSpellDefBuffed(float isBuffed) {
/* 543 */       this.spellDefBuffed = isBuffed;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getHealth() {
/* 548 */       return this.health;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setHealth(float newHealth) {
/* 553 */       this.health = newHealth;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getMaxFavor() {
/* 558 */       return this.maxFavor;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMaxFavor(float newMax) {
/* 563 */       this.maxFavor = newMax;
/*     */       
/* 565 */       if (this.favor > this.maxFavor) {
/* 566 */         setFavor(this.maxFavor);
/*     */       }
/*     */     }
/*     */     
/*     */     public float getFavor() {
/* 571 */       return this.favor;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFavor(float newFavor) {
/* 576 */       this.favor = newFavor;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getMaxKarma() {
/* 581 */       return this.maxKarma;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMaxKarma(float newMax) {
/* 586 */       this.maxKarma = newMax;
/*     */       
/* 588 */       if (this.karma > this.maxKarma) {
/* 589 */         setKarma(this.maxKarma);
/*     */       }
/*     */     }
/*     */     
/*     */     public float getKarma() {
/* 594 */       return this.karma;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setKarma(float newKarma) {
/* 599 */       this.karma = newKarma;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDistanceTo(FightEntity other) {
/* 604 */       int totalDist = 0;
/* 605 */       ValreiFight.test1.setXY(this.xPos, this.yPos);
/* 606 */       ValreiFight.test2.setXY(other.xPos, other.yPos);
/* 607 */       while (ValreiFight.test1.getX() != ValreiFight.test2.getX() || ValreiFight.test1.getY() != ValreiFight.test2.getY()) {
/*     */         
/* 609 */         if (ValreiFight.test1.getY() != ValreiFight.test2.getY()) {
/*     */           
/* 611 */           int yDiff = 0;
/* 612 */           int xDiff = 0;
/* 613 */           if (ValreiFight.test1.getY() < ValreiFight.test2.getY()) {
/*     */             
/* 615 */             yDiff = 1;
/* 616 */             if (ValreiFight.test1.getX() < ValreiFight.test2.getX()) {
/* 617 */               xDiff = 1;
/*     */             }
/*     */           } else {
/*     */             
/* 621 */             yDiff = -1;
/* 622 */             if (ValreiFight.test1.getX() > ValreiFight.test2.getX()) {
/* 623 */               xDiff = -1;
/*     */             }
/*     */           } 
/* 626 */           ValreiFight.test1.setX(ValreiFight.test1.getX() + xDiff);
/* 627 */           ValreiFight.test1.setY(ValreiFight.test1.getY() + yDiff);
/*     */         }
/*     */         else {
/*     */           
/* 631 */           ValreiFight.test1.setX(ValreiFight.test1.getX() + ((ValreiFight.test1.getX() < ValreiFight.test2.getX()) ? 1 : -1));
/*     */         } 
/*     */         
/* 634 */         totalDist++;
/*     */       } 
/* 636 */       return totalDist;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Point getTargetMove(boolean towards, FightEntity other) {
/* 648 */       ValreiFight.test1.setXY(this.xPos, this.yPos);
/* 649 */       ValreiFight.test2.setXY(other.xPos, other.yPos);
/* 650 */       if (ValreiFight.test1.getY() != ValreiFight.test2.getY()) {
/*     */         
/* 652 */         if (towards) {
/*     */           
/* 654 */           int yDiff = 0;
/* 655 */           int xDiff = 0;
/* 656 */           if (ValreiFight.test1.getY() < ValreiFight.test2.getY()) {
/*     */             
/* 658 */             yDiff = 1;
/* 659 */             if (ValreiFight.test1.getX() < ValreiFight.test2.getX()) {
/* 660 */               xDiff = 1;
/*     */             }
/*     */           } else {
/*     */             
/* 664 */             yDiff = -1;
/* 665 */             if (ValreiFight.test1.getX() > ValreiFight.test2.getX()) {
/* 666 */               xDiff = -1;
/*     */             }
/*     */           } 
/* 669 */           return new Point(ValreiFight.test1.getX() + xDiff, ValreiFight.test1.getY() + yDiff);
/*     */         } 
/*     */ 
/*     */         
/* 673 */         int testDir = ValreiFight.this.fightRand.nextInt(3); int i;
/* 674 */         for (i = 0; i < 3; i++) {
/*     */           
/* 676 */           int newX = ValreiFight.test1.getX();
/* 677 */           int newY = ValreiFight.test1.getY();
/* 678 */           switch (testDir) {
/*     */             
/*     */             case 0:
/* 681 */               newY += (ValreiFight.test2.getY() > ValreiFight.test1.getY()) ? -1 : ((ValreiFight.test2.getY() == ValreiFight.test1.getY()) ? 0 : 1);
/* 682 */               if (newY != ValreiFight.test1.getY() && 
/* 683 */                 ValreiFight.this.isMoveValid(this, newX, newY))
/* 684 */                 return new Point(newX, newY); 
/*     */               break;
/*     */             case 1:
/* 687 */               newX += (ValreiFight.test2.getX() > ValreiFight.test1.getX()) ? -1 : ((ValreiFight.test2.getX() == ValreiFight.test1.getX()) ? 0 : 1);
/* 688 */               if (newX != ValreiFight.test1.getX() && 
/* 689 */                 ValreiFight.this.isMoveValid(this, newX, newY))
/* 690 */                 return new Point(newX, newY); 
/*     */               break;
/*     */             case 2:
/* 693 */               if (ValreiFight.test2.getX() > ValreiFight.test1.getX() || ValreiFight.test2.getY() > ValreiFight.test1.getY()) {
/*     */                 
/* 695 */                 newX--;
/* 696 */                 newY--;
/*     */               }
/* 698 */               else if (ValreiFight.test2.getX() < ValreiFight.test1.getX() || ValreiFight.test2.getY() < ValreiFight.test1.getY()) {
/*     */                 
/* 700 */                 newX++;
/* 701 */                 newY++;
/*     */               } 
/* 703 */               if ((newX != ValreiFight.test1.getX() || newY != ValreiFight.test1.getY()) && 
/* 704 */                 ValreiFight.this.isMoveValid(this, newX, newY)) {
/* 705 */                 return new Point(newX, newY);
/*     */               }
/*     */               break;
/*     */           } 
/* 709 */           testDir++;
/* 710 */           if (testDir == 3) {
/* 711 */             testDir = 0;
/*     */           }
/*     */         } 
/*     */         
/* 715 */         testDir = ValreiFight.this.fightRand.nextInt(3);
/* 716 */         for (i = 0; i < 3; i++)
/*     */         {
/* 718 */           int newX = ValreiFight.test1.getX();
/* 719 */           int newY = ValreiFight.test1.getY();
/* 720 */           switch (testDir) {
/*     */             
/*     */             case 0:
/* 723 */               newY += (ValreiFight.test2.getY() > ValreiFight.test1.getY()) ? 1 : ((ValreiFight.test2.getY() == ValreiFight.test1.getY()) ? 0 : -1);
/* 724 */               if (newY != ValreiFight.test1.getY() && 
/* 725 */                 ValreiFight.this.isMoveValid(this, newX, newY))
/* 726 */                 return new Point(newX, newY); 
/*     */               break;
/*     */             case 1:
/* 729 */               newX += (ValreiFight.test2.getX() > ValreiFight.test1.getX()) ? 1 : ((ValreiFight.test2.getX() == ValreiFight.test1.getX()) ? 0 : -1);
/* 730 */               if (newX != ValreiFight.test1.getX() && 
/* 731 */                 ValreiFight.this.isMoveValid(this, newX, newY))
/* 732 */                 return new Point(newX, newY); 
/*     */               break;
/*     */             case 2:
/* 735 */               if (ValreiFight.test2.getX() > ValreiFight.test1.getX() || ValreiFight.test2.getY() > ValreiFight.test1.getY()) {
/*     */                 
/* 737 */                 newX++;
/* 738 */                 newY++;
/*     */               }
/* 740 */               else if (ValreiFight.test2.getX() < ValreiFight.test1.getX() || ValreiFight.test2.getY() < ValreiFight.test1.getY()) {
/*     */                 
/* 742 */                 newX--;
/* 743 */                 newY--;
/*     */               } 
/* 745 */               if ((newX != ValreiFight.test1.getX() || newY != ValreiFight.test1.getY()) && 
/* 746 */                 ValreiFight.this.isMoveValid(this, newX, newY)) {
/* 747 */                 return new Point(newX, newY);
/*     */               }
/*     */               break;
/*     */           } 
/* 751 */           testDir++;
/* 752 */           if (testDir == 3) {
/* 753 */             testDir = 0;
/*     */           }
/*     */         }
/*     */       
/* 757 */       } else if (ValreiFight.test1.getX() != ValreiFight.test2.getX()) {
/*     */         
/* 759 */         return new Point(ValreiFight.test1.getX() + ((ValreiFight.test1.getX() < ValreiFight.test2.getX()) ? 1 : -1), ValreiFight.test1.getY());
/*     */       } 
/*     */       
/* 762 */       return new Point(ValreiFight.test1.getX(), ValreiFight.test1.getY());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public short getPreferredAction() {
/* 768 */       float meleeAtk = this.entityBase.getCurrentSkill(102) + this.entityBase.getCurrentSkill(104) / 3.0F;
/*     */       
/* 770 */       float rangedAtk = this.entityBase.getCurrentSkill(104) + this.entityBase.getCurrentSkill(103) / 3.0F;
/*     */       
/* 772 */       float deitySpell = this.entityBase.getCurrentSkill(105) + this.entityBase.getCurrentSkill(106) / 3.0F;
/*     */       
/* 774 */       float sorcSpell = this.entityBase.getCurrentSkill(106) + this.entityBase.getCurrentSkill(100) / 3.0F;
/*     */       
/* 776 */       if (meleeAtk > deitySpell && meleeAtk > sorcSpell && meleeAtk > rangedAtk)
/* 777 */         return 4; 
/* 778 */       if (rangedAtk > deitySpell && rangedAtk > sorcSpell)
/* 779 */         return 5; 
/* 780 */       if (deitySpell > sorcSpell) {
/* 781 */         return 6;
/*     */       }
/* 783 */       return 7;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getDeitySpell(FightEntity defender) {
/* 788 */       byte preferredType = 1;
/*     */       
/* 790 */       if (getFavor() >= 30.0F && getHealth() < 75.0F && defender.getHealth() > getHealth()) {
/* 791 */         preferredType = 0;
/* 792 */       } else if (getFavor() >= 50.0F && getAttackBuffed() == 0.0F) {
/*     */         
/* 794 */         if (defender.entityBase.getCurrentSkill(101) > defender.entityBase
/* 795 */           .getCurrentSkill(103)) {
/* 796 */           preferredType = 4;
/*     */         }
/*     */       } 
/* 799 */       return preferredType;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getSorcerySpell(FightEntity defender) {
/* 804 */       byte preferredType = 1;
/*     */       
/* 806 */       if (getKarma() >= 60.0F && getHealth() < 75.0F && defender.getHealth() > getHealth()) {
/*     */         
/* 808 */         float attackHigh = Math.max(defender.entityBase.getCurrentSkill(102), defender.entityBase
/* 809 */             .getCurrentSkill(104));
/* 810 */         float spellHigh = Math.max(defender.entityBase.getCurrentSkill(105), defender.entityBase
/* 811 */             .getCurrentSkill(106));
/* 812 */         if (attackHigh > spellHigh && getPhysDefBuffed() == 0.0F) {
/* 813 */           preferredType = 2;
/* 814 */         } else if (getSpellDefBuffed() == 0.0F) {
/* 815 */           preferredType = 3;
/*     */         } 
/*     */       } 
/* 818 */       return preferredType;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class ValreiFightHex
/*     */   {
/*     */     private int xPos;
/*     */     
/*     */     private int yPos;
/*     */     
/*     */     private short modifierType;
/*     */ 
/*     */     
/*     */     ValreiFightHex(int xPos, int yPos) {
/* 834 */       this.xPos = xPos;
/* 835 */       this.yPos = yPos;
/* 836 */       this.modifierType = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setModifier(short newModifier) {
/* 841 */       this.modifierType = newModifier;
/*     */     }
/*     */ 
/*     */     
/*     */     public short getModifier() {
/* 846 */       return this.modifierType;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getX() {
/* 851 */       return this.xPos;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getY() {
/* 856 */       return this.yPos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\ValreiFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */