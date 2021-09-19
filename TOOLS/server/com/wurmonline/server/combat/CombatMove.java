/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.behaviours.CreatureBehaviour;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CombatMove
/*      */   implements CombatConstants, MiscConstants, SoundNames
/*      */ {
/*   58 */   private static final Logger logger = Logger.getLogger(CombatMove.class.getName());
/*      */   
/*      */   private final String name;
/*      */   
/*      */   private final String actionString;
/*      */   
/*      */   private final int number;
/*      */   
/*      */   public static final int SWEEP = 1;
/*      */   
/*      */   public static final int EARTHSHAKE = 2;
/*      */   public static final int FIREBREATH = 3;
/*      */   public static final int DOUBLE_FIST = 4;
/*      */   public static final int STOMP = 5;
/*      */   public static final int THROW = 6;
/*      */   public static final int STUN = 7;
/*      */   public static final int BASH = 8;
/*      */   public static final int ACIDBREATH = 9;
/*      */   public static final int HELLHORSEFIRE = 10;
/*      */   public static final int PHASE = 11;
/*      */   private final float difficulty;
/*      */   private final float basedam;
/*      */   private final float rarity;
/*      */   private final byte woundType;
/*   82 */   private static final Map<Integer, CombatMove> moves = new HashMap<>();
/*      */ 
/*      */   
/*      */   static {
/*   86 */     new CombatMove(1, "sweep", 20.0F, " makes a circular powerful sweep!", 25000.0F, 0.01F, (byte)1);
/*   87 */     new CombatMove(2, "earthshake", 20.0F, " shakes the earth!", 23000.0F, 0.013F, (byte)0);
/*   88 */     new CombatMove(3, "firebreath", 20.0F, " breathes fire!", 27000.0F, 0.011F, (byte)4);
/*   89 */     new CombatMove(4, "double fist", 20.0F, " throws down @hisher powerful fists!", 30000.0F, 0.01F, (byte)0);
/*   90 */     new CombatMove(5, "stomp", 20.0F, " stomps!", 10000.0F, 0.02F, (byte)0);
/*   91 */     new CombatMove(6, "throws", 20.0F, " picks up and throws @defender!", 5000.0F, 0.05F, (byte)0);
/*   92 */     new CombatMove(7, "stuns", 30.0F, " stuns @defender!", 24000.0F, 0.1F, (byte)9);
/*   93 */     new CombatMove(8, "bashes", 10.0F, " bashes @defender!", 25000.0F, 0.1F, (byte)0);
/*   94 */     new CombatMove(9, "acidbreath", 20.0F, " breathes acid!", 20000.0F, 0.011F, (byte)10);
/*   95 */     new CombatMove(10, "firebreath", 20.0F, " breathes fire!", 7000.0F, 0.003F, (byte)4);
/*   96 */     new CombatMove(11, "phase", 20.0F, " phases!", 5000.0F, 0.011F, (byte)9);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CombatMove getCombatMove(int number) {
/*  109 */     return moves.get(Integer.valueOf(number));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CombatMove(int _number, String _name, float _difficulty, String aActionString, float aBaseDamage, float aRarity, byte aWoundType) {
/*  125 */     this.number = _number;
/*  126 */     this.name = _name;
/*  127 */     this.difficulty = _difficulty;
/*  128 */     this.actionString = aActionString;
/*  129 */     this.basedam = aBaseDamage;
/*  130 */     this.rarity = aRarity;
/*  131 */     this.woundType = aWoundType;
/*  132 */     moves.put(Integer.valueOf(this.number), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void perform(Creature creature) {
/*  141 */     if (!creature.isUnique() || creature.getHugeMoveCounter() >= 2)
/*  142 */       creature.playAnimation("fight_" + getName(), false); 
/*  143 */     switch (this.number) {
/*      */       
/*      */       case 1:
/*  146 */         sweep(creature);
/*      */         return;
/*      */       case 2:
/*  149 */         shakeEarth(creature);
/*      */         return;
/*      */       case 3:
/*      */       case 10:
/*  153 */         breatheFire(creature);
/*      */         return;
/*      */       case 4:
/*  156 */         doubleFist(creature);
/*      */         return;
/*      */       case 5:
/*  159 */         stomp(creature);
/*      */         return;
/*      */       case 6:
/*  162 */         throwOpponent(creature);
/*      */         return;
/*      */       case 7:
/*  165 */         stun(creature);
/*      */         return;
/*      */       case 8:
/*  168 */         bashOpponent(creature);
/*      */         return;
/*      */       case 9:
/*  171 */         breathAcid(creature);
/*      */         return;
/*      */       case 11:
/*  174 */         phaseOpponent(creature);
/*      */         return;
/*      */     } 
/*  177 */     logger.warning("Perform an unknown CombatMove: " + this.number);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void sweep(Creature creature) {
/*  188 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  189 */     int x = (creature.getCurrentTile()).tilex;
/*  190 */     int y = (creature.getCurrentTile()).tiley;
/*  191 */     Server.getInstance().broadCastAction(creature.getNameWithGenus() + this.actionString, creature, 5, true);
/*  192 */     for (int a = Math.max(0, x - 1); a < Math.min(Zones.worldTileSizeX - 1, x + 1); a++) {
/*      */       
/*  194 */       for (int b = Math.max(0, y - 1); b < Math.min(Zones.worldTileSizeY - 1, y + 1); b++) {
/*      */         
/*  196 */         VolaTile tile = Zones.getTileOrNull(a, b, creature.isOnSurface());
/*  197 */         if (tile != null) {
/*      */           
/*  199 */           Creature[] crets = tile.getCreatures();
/*  200 */           if (crets.length > 0)
/*      */           {
/*  202 */             for (int l = 0; l < crets.length; l++) {
/*      */               
/*  204 */               if (crets[l] != creature && !crets[l].isUnique())
/*      */               {
/*  206 */                 if ((crets[l].isPlayer() && crets[l].getPower() <= 0) || crets[l].isDominated())
/*      */                 {
/*  208 */                   crets[l].addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */                 }
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void stun(Creature creature) {
/*  221 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  222 */     int x = (creature.getCurrentTile()).tilex;
/*  223 */     int y = (creature.getCurrentTile()).tiley;
/*  224 */     for (int a = Math.max(0, x - 1); a < Math.min(Zones.worldTileSizeX - 1, x + 1); a++) {
/*      */       
/*  226 */       for (int b = Math.max(0, y - 1); b < Math.min(Zones.worldTileSizeY - 1, y + 1); b++) {
/*      */         
/*  228 */         VolaTile tile = Zones.getTileOrNull(a, b, creature.isOnSurface());
/*  229 */         if (tile != null) {
/*      */           
/*  231 */           Creature[] crets = tile.getCreatures();
/*  232 */           if (crets.length > 0)
/*      */           {
/*  234 */             for (int l = 0; l < crets.length; l++) {
/*      */               
/*  236 */               if (crets[l] != creature)
/*      */               {
/*  238 */                 if (!crets[l].isUnique() && ((crets[l]
/*  239 */                   .isPlayer() && crets[l].getPower() <= 0) || crets[l]
/*  240 */                   .isDominated())) {
/*      */                   
/*  242 */                   Server.getInstance().broadCastAction(creature
/*  243 */                       .getNameWithGenus() + replace(this.actionString, "@defender", crets[l].getNameWithGenus()), creature, 5, true);
/*      */ 
/*      */                   
/*  246 */                   crets[l].getStatus().setStunned((Server.rand.nextInt(5) + 4));
/*  247 */                   crets[l].addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void stomp(Creature creature) {
/*  264 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  265 */     int x = (creature.getCurrentTile()).tilex;
/*  266 */     int y = (creature.getCurrentTile()).tiley;
/*  267 */     Server.getInstance().broadCastAction(creature.getNameWithGenus() + this.actionString, creature, 5, true);
/*      */     
/*  269 */     for (int a = Math.max(0, x - 2); a <= Math.min(Zones.worldTileSizeX - 1, x + 2); a++) {
/*      */       
/*  271 */       for (int b = Math.max(0, y - 2); b <= Math.min(Zones.worldTileSizeY - 1, y + 2); b++) {
/*      */         
/*  273 */         VolaTile tile = Zones.getTileOrNull(a, b, creature.isOnSurface());
/*  274 */         if (tile != null) {
/*      */           
/*  276 */           Creature[] crets = tile.getCreatures();
/*  277 */           if (crets.length > 0)
/*      */           {
/*  279 */             for (int l = 0; l < crets.length; l++) {
/*      */               
/*  281 */               if (crets[l] != creature)
/*      */               {
/*  283 */                 if (!crets[l].isUnique() && ((crets[l]
/*  284 */                   .isPlayer() && crets[l].getPower() <= 0) || crets[l]
/*  285 */                   .isDominated()))
/*      */                 {
/*  287 */                   crets[l].addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */                 }
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void shakeEarth(Creature creature) {
/*  304 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  305 */     int x = (creature.getCurrentTile()).tilex;
/*  306 */     int y = (creature.getCurrentTile()).tiley;
/*  307 */     Server.getInstance().broadCastAction(creature.getNameWithGenus() + this.actionString, creature, 5, true);
/*  308 */     for (int a = Math.max(0, x - 2); a < Math.min(Zones.worldTileSizeX - 1, x + 2); a++) {
/*      */       
/*  310 */       for (int b = Math.max(0, y - 2); b < Math.min(Zones.worldTileSizeY - 1, y + 2); b++) {
/*      */         
/*  312 */         VolaTile tile = Zones.getTileOrNull(a, b, creature.isOnSurface());
/*  313 */         if (tile != null) {
/*      */           
/*  315 */           Creature[] crets = tile.getCreatures();
/*  316 */           if (crets.length > 0)
/*      */           {
/*      */             
/*  319 */             for (int l = 0; l < crets.length; l++) {
/*      */               
/*  321 */               if (crets[l] != creature)
/*      */               {
/*  323 */                 if (!crets[l].isUnique() && ((crets[l]
/*  324 */                   .isPlayer() && crets[l].getPower() <= 0) || crets[l]
/*  325 */                   .isDominated()))
/*      */                 {
/*  327 */                   crets[l].addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */                 }
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void doubleFist(Creature creature) {
/*  344 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  345 */     int x1 = (creature.getCurrentTile()).tilex;
/*  346 */     int y1 = (creature.getCurrentTile()).tiley;
/*  347 */     int x2 = (creature.getCurrentTile()).tilex;
/*  348 */     int y2 = (creature.getCurrentTile()).tiley;
/*      */     
/*  350 */     Server.getInstance().broadCastAction(creature
/*  351 */         .getNameWithGenus() + replace(this.actionString, "@hisher", creature.getHisHerItsString()), creature, 5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  356 */     float attAngle = Creature.normalizeAngle(creature.getStatus().getRotation());
/*  357 */     byte dir = 0;
/*  358 */     float degree = 22.5F;
/*  359 */     if (attAngle >= 337.5D || attAngle < 22.5F) {
/*  360 */       dir = 0;
/*      */     } else {
/*      */       
/*  363 */       for (int x = 0; x < 8; x++) {
/*      */         
/*  365 */         if (attAngle < 22.5F + (45 * x)) {
/*      */           
/*  367 */           dir = (byte)x;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  372 */     if (dir == 0) {
/*      */       
/*  374 */       x1--;
/*  375 */       y1--;
/*  376 */       x2++;
/*  377 */       y2--;
/*      */     }
/*  379 */     else if (dir == 7) {
/*      */       
/*  381 */       y1--;
/*  382 */       x2--;
/*      */     }
/*  384 */     else if (dir == 6) {
/*      */       
/*  386 */       x1--;
/*  387 */       y1--;
/*  388 */       x2--;
/*  389 */       y2++;
/*      */     }
/*  391 */     else if (dir == 5) {
/*      */       
/*  393 */       x1--;
/*  394 */       y2++;
/*      */     }
/*  396 */     else if (dir == 4) {
/*      */       
/*  398 */       x1--;
/*  399 */       y1++;
/*  400 */       x2++;
/*  401 */       y2++;
/*      */     }
/*  403 */     else if (dir == 3) {
/*      */       
/*  405 */       y1++;
/*  406 */       x2++;
/*      */     }
/*  408 */     else if (dir == 2) {
/*      */       
/*  410 */       x1++;
/*  411 */       y1--;
/*  412 */       x2++;
/*  413 */       y2++;
/*      */     }
/*  415 */     else if (dir == 1) {
/*      */       
/*  417 */       y1--;
/*  418 */       x2++;
/*      */     } 
/*  420 */     VolaTile tile = Zones.getTileOrNull(x1, y1, creature.isOnSurface());
/*      */     
/*      */     try {
/*  423 */       ItemFactory.createItem(344, 20.0F, ((x1 << 2) + 2), ((y1 << 2) + 2), attAngle, creature.isOnSurface(), (byte)0, creature
/*  424 */           .getBridgeId(), creature.getName());
/*      */     }
/*  426 */     catch (Exception ex) {
/*      */       
/*  428 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*  430 */     if (tile != null) {
/*      */       
/*  432 */       Creature[] crets = tile.getCreatures();
/*  433 */       if (crets.length > 0)
/*      */       {
/*  435 */         for (int l = 0; l < crets.length; l++) {
/*      */           
/*  437 */           if (crets[l] != creature)
/*      */           {
/*  439 */             if (!crets[l].isUnique() && ((crets[l]
/*  440 */               .isPlayer() && crets[l].getPower() <= 0) || crets[l].isDominated()))
/*      */             {
/*  442 */               crets[l].addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  449 */     tile = Zones.getTileOrNull(x2, y2, creature.isOnSurface());
/*      */     
/*      */     try {
/*  452 */       ItemFactory.createItem(344, 20.0F, ((x2 << 2) + 2), ((y2 << 2) + 2), attAngle, creature.isOnSurface(), (byte)0, creature
/*  453 */           .getBridgeId(), creature.getName());
/*      */     }
/*  455 */     catch (Exception ex) {
/*      */       
/*  457 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*  459 */     if (tile != null) {
/*      */       
/*  461 */       Creature[] crets = tile.getCreatures();
/*  462 */       if (crets.length > 0)
/*      */       {
/*  464 */         for (int l = 0; l < crets.length; l++) {
/*      */           
/*  466 */           if (crets[l] != creature)
/*      */           {
/*  468 */             if (!crets[l].isUnique() && ((crets[l]
/*  469 */               .isPlayer() && crets[l].getPower() <= 0) || crets[l].isDominated()))
/*      */             {
/*  471 */               crets[l].addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void playDamageSound(Creature defender, int defdamage) {
/*  488 */     int numsound = Server.rand.nextInt(3);
/*  489 */     if (defdamage > 10000) {
/*      */       
/*  491 */       if (numsound == 0)
/*  492 */         SoundPlayer.playSound("sound.combat.fleshbone1", defender, 1.6F); 
/*  493 */       if (numsound == 1)
/*  494 */         SoundPlayer.playSound("sound.combat.fleshbone2", defender, 1.6F); 
/*  495 */       if (numsound == 2) {
/*  496 */         SoundPlayer.playSound("sound.combat.fleshbone3", defender, 1.6F);
/*      */       }
/*      */     } else {
/*      */       
/*  500 */       if (numsound == 0)
/*  501 */         SoundPlayer.playSound("sound.combat.fleshhit1", defender, 1.6F); 
/*  502 */       if (numsound == 1)
/*  503 */         SoundPlayer.playSound("sound.combat.fleshhit2", defender, 1.6F); 
/*  504 */       if (numsound == 2)
/*  505 */         SoundPlayer.playSound("sound.combat.fleshhit3", defender, 1.6F); 
/*      */     } 
/*  507 */     SoundPlayer.playSound(defender.getHitSound(), defender, 1.6F);
/*      */   }
/*      */ 
/*      */   
/*      */   private final void breathAcid(Creature creature) {
/*  512 */     long[] ids = creature.getLatestAttackers();
/*  513 */     if (ids.length > 0) {
/*      */       
/*  515 */       long targetcret = ids[Server.rand.nextInt(ids.length)];
/*      */       
/*      */       try {
/*  518 */         Creature c = Server.getInstance().getCreature(targetcret);
/*  519 */         if (!c.isDead()) {
/*  520 */           creature.turnTowardsCreature(c);
/*      */         }
/*  522 */       } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/*      */       }
/*  526 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  531 */     int x1 = (creature.getCurrentTile()).tilex;
/*  532 */     int y1 = (creature.getCurrentTile()).tiley;
/*  533 */     byte[] tilearr = null;
/*  534 */     Server.getInstance().broadCastAction(creature
/*  535 */         .getNameWithGenus() + replace(this.actionString, "@hisher", creature.getHisHerItsString()), creature, 5, true);
/*      */     
/*  537 */     float attAngle = Creature.normalizeAngle(creature.getStatus().getRotation());
/*      */     
/*  539 */     byte dir = 0;
/*  540 */     float degree = 22.5F;
/*  541 */     if (attAngle >= 337.5D || attAngle < 22.5F) {
/*  542 */       dir = 0;
/*      */     } else {
/*      */       
/*  545 */       for (int x = 0; x < 8; x++) {
/*      */         
/*  547 */         if (attAngle < 22.5F + (45 * x)) {
/*      */           
/*  549 */           dir = (byte)x;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  556 */     if (dir == 0) {
/*      */       
/*  558 */       x1 -= 2;
/*  559 */       y1 -= 5;
/*  560 */       tilearr = BreathWeapon.fiveNorth;
/*      */     }
/*  562 */     else if (dir == 7) {
/*      */       
/*  564 */       x1 -= 5;
/*  565 */       y1 -= 5;
/*  566 */       tilearr = BreathWeapon.fiveNWest;
/*      */     }
/*  568 */     else if (dir == 6) {
/*      */       
/*  570 */       x1 -= 5;
/*  571 */       y1 -= 2;
/*  572 */       tilearr = BreathWeapon.fiveWest;
/*      */     }
/*  574 */     else if (dir == 5) {
/*      */       
/*  576 */       x1 -= 5;
/*  577 */       y1++;
/*  578 */       tilearr = BreathWeapon.fiveSWest;
/*      */     } 
/*  580 */     if (dir == 4) {
/*      */       
/*  582 */       x1 -= 2;
/*  583 */       y1++;
/*  584 */       tilearr = BreathWeapon.fiveSouth;
/*      */     }
/*  586 */     else if (dir == 3) {
/*      */       
/*  588 */       x1++;
/*  589 */       y1++;
/*  590 */       tilearr = BreathWeapon.fiveSEast;
/*      */     }
/*  592 */     else if (dir == 2) {
/*      */       
/*  594 */       x1++;
/*  595 */       y1 -= 2;
/*  596 */       tilearr = BreathWeapon.fiveEast;
/*      */     }
/*  598 */     else if (dir == 1) {
/*      */       
/*  600 */       x1++;
/*  601 */       y1 -= 5;
/*  602 */       tilearr = BreathWeapon.fiveNEast;
/*      */     } 
/*  604 */     if (tilearr != null) {
/*      */       
/*  606 */       int num = 0;
/*      */       
/*  608 */       for (int y = 0; y < 5; y++) {
/*      */         
/*  610 */         for (int x = 0; x < 5; x++) {
/*      */           
/*  612 */           if (tilearr[num] > 0)
/*      */           {
/*      */ 
/*      */             
/*  616 */             breathAcid(Zones.getOrCreateTile(x1 + x, y1 + y, creature.isOnSurface()), creature);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  621 */           num++;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  626 */       logger.log(Level.WARNING, "Facing " + creature.getStatus().getRotation() + " no tilarr");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void breathAcid(VolaTile t, Creature creature) {
/*  631 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*      */     
/*  633 */     if (t != null) {
/*      */       
/*  635 */       Creature[] crets = t.getCreatures();
/*  636 */       if (crets.length > 0)
/*      */       {
/*  638 */         for (int l = 0; l < crets.length; l++) {
/*      */           
/*  640 */           if (crets[l] != creature)
/*      */           {
/*  642 */             if (!crets[l].isUnique() && (crets[l]
/*  643 */               .isPlayer() || crets[l].isDominated()))
/*      */             {
/*  645 */               crets[l].addWoundOfType(creature, (byte)10, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void breatheFire(Creature creature) {
/*  660 */     long[] ids = creature.getLatestAttackers();
/*  661 */     if (ids.length > 0) {
/*      */       
/*  663 */       long targetcret = ids[Server.rand.nextInt(ids.length)];
/*      */       
/*      */       try {
/*  666 */         Creature c = Server.getInstance().getCreature(targetcret);
/*  667 */         if (!c.isDead()) {
/*  668 */           creature.turnTowardsCreature(c);
/*      */         }
/*  670 */       } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/*      */       }
/*  674 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  679 */     int x1 = (creature.getCurrentTile()).tilex;
/*  680 */     int y1 = (creature.getCurrentTile()).tiley;
/*  681 */     byte[] tilearr = null;
/*  682 */     Server.getInstance().broadCastAction(creature
/*  683 */         .getNameWithGenus() + replace(this.actionString, "@hisher", creature.getHisHerItsString()), creature, 5, true);
/*      */     
/*  685 */     float attAngle = Creature.normalizeAngle(creature.getStatus().getRotation());
/*      */     
/*  687 */     byte dir = 0;
/*  688 */     float degree = 22.5F;
/*  689 */     if (attAngle >= 337.5D || attAngle < 22.5F) {
/*  690 */       dir = 0;
/*      */     } else {
/*      */       
/*  693 */       for (int x = 0; x < 8; x++) {
/*      */         
/*  695 */         if (attAngle < 22.5F + (45 * x)) {
/*      */           
/*  697 */           dir = (byte)x;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  704 */     if (dir == 0) {
/*      */       
/*  706 */       x1 -= 2;
/*  707 */       y1 -= 5;
/*  708 */       tilearr = BreathWeapon.fiveNorth;
/*      */     }
/*  710 */     else if (dir == 7) {
/*      */       
/*  712 */       x1 -= 5;
/*  713 */       y1 -= 5;
/*  714 */       tilearr = BreathWeapon.fiveNWest;
/*      */     }
/*  716 */     else if (dir == 6) {
/*      */       
/*  718 */       x1 -= 5;
/*  719 */       y1 -= 2;
/*  720 */       tilearr = BreathWeapon.fiveWest;
/*      */     }
/*  722 */     else if (dir == 5) {
/*      */       
/*  724 */       x1 -= 5;
/*  725 */       y1++;
/*  726 */       tilearr = BreathWeapon.fiveSWest;
/*      */     } 
/*  728 */     if (dir == 4) {
/*      */       
/*  730 */       x1 -= 2;
/*  731 */       y1++;
/*  732 */       tilearr = BreathWeapon.fiveSouth;
/*      */     }
/*  734 */     else if (dir == 3) {
/*      */       
/*  736 */       x1++;
/*  737 */       y1++;
/*  738 */       tilearr = BreathWeapon.fiveSEast;
/*      */     }
/*  740 */     else if (dir == 2) {
/*      */       
/*  742 */       x1++;
/*  743 */       y1 -= 2;
/*  744 */       tilearr = BreathWeapon.fiveEast;
/*      */     }
/*  746 */     else if (dir == 1) {
/*      */       
/*  748 */       x1++;
/*  749 */       y1 -= 5;
/*  750 */       tilearr = BreathWeapon.fiveNEast;
/*      */     } 
/*  752 */     if (tilearr != null) {
/*      */       
/*  754 */       int num = 0;
/*      */       
/*  756 */       for (int y = 0; y < 5; y++) {
/*      */         
/*  758 */         for (int x = 0; x < 5; x++) {
/*      */           
/*  760 */           if (tilearr[num] > 0)
/*      */           {
/*      */ 
/*      */             
/*  764 */             breatheFire(Zones.getOrCreateTile(x1 + x, y1 + y, creature.isOnSurface()), creature);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  769 */           num++;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  774 */       logger.log(Level.WARNING, "Facing " + creature.getStatus().getRotation() + " no tilarr");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void breatheFire(VolaTile t, Creature creature) {
/*  784 */     float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*      */     
/*      */     try {
/*  787 */       ItemFactory.createItem(520, 20.0F, ((t.getTileX() << 2) + 2), ((t.getTileY() << 2) + 2), Server.rand
/*  788 */           .nextFloat() * 180.0F, t.isOnSurface(), (byte)0, creature.getBridgeId(), "");
/*      */     }
/*  790 */     catch (Exception ex) {
/*      */ 
/*      */       
/*  793 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*  795 */     if (t != null) {
/*      */       
/*  797 */       Creature[] crets = t.getCreatures();
/*  798 */       if (crets.length > 0)
/*      */       {
/*  800 */         for (int l = 0; l < crets.length; l++) {
/*      */           
/*  802 */           if (crets[l] != creature)
/*      */           {
/*  804 */             if (!crets[l].isUnique() && ((crets[l]
/*  805 */               .isPlayer() && crets[l].getPower() <= 0) || crets[l].isDominated())) {
/*      */ 
/*      */               
/*  808 */               byte woundType = getBreathDamageType(creature);
/*      */ 
/*      */               
/*  811 */               crets[l].addWoundOfType(creature, (byte)4, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte getBreathDamageType(Creature attacker) {
/*  822 */     byte type = 4;
/*  823 */     int cid = attacker.getTemplate().getTemplateId();
/*  824 */     if (CreatureTemplate.isDragon(cid))
/*      */     
/*  826 */     { switch (cid)
/*      */       
/*      */       { case 89:
/*  829 */           type = 10;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  847 */           return type;case 91: type = 6; return type;case 90: type = 5; return type;case 16: type = 4; return type;case 92: type = 8; return type; }  type = 4; }  return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void throwOpponent(Creature creature) {
/*  856 */     if (creature.opponent != null && (creature.opponent.getPower() <= 0 || Servers.isThisATestServer() || creature.opponent
/*  857 */       .isDominated())) {
/*      */       
/*  859 */       if (creature.opponent.isUnique())
/*      */         return; 
/*  861 */       if (!creature.isOnSurface()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  866 */       BlockingResult result = Blocking.getBlockerBetween(creature, creature.opponent, 4);
/*  867 */       if (result != null) {
/*      */         return;
/*      */       }
/*      */       
/*  871 */       int x = (creature.getCurrentTile()).tilex;
/*  872 */       int y = (creature.getCurrentTile()).tiley;
/*  873 */       int targetX = Zones.safeTileX(x + 10 - Server.rand.nextInt(20));
/*  874 */       int targetY = Zones.safeTileY(y + 10 - Server.rand
/*  875 */           .nextInt(20));
/*      */       
/*  877 */       VolaTile t = Zones.getTileOrNull(targetX, targetY, true);
/*  878 */       if (t != null) {
/*      */         
/*  880 */         if (t.getStructure() != null)
/*      */           return; 
/*  882 */         if (t.getVillage() != null)
/*      */           return; 
/*      */       } 
/*  885 */       if (creature.opponent.getVehicle() != -10L) {
/*      */         
/*      */         try {
/*      */           
/*  889 */           Item vehicle = Items.getItem(creature.opponent.getVehicle());
/*  890 */           if (vehicle.isBoat())
/*      */           {
/*  892 */             if (vehicle.getSizeY() > 130)
/*      */             {
/*  894 */               if (creature.getPositionZ() <= 0.0F) {
/*      */                 return;
/*      */               }
/*      */             }
/*      */           }
/*  899 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  904 */       Creature opp = creature.opponent;
/*  905 */       float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  906 */       Server.getInstance().broadCastAction(creature
/*  907 */           .getNameWithGenus() + replace(this.actionString, "@defender", creature.opponent.getNameWithGenus()), creature, 5, true);
/*      */       
/*  909 */       if (!opp.addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false)) {
/*      */ 
/*      */         
/*  912 */         Creatures.getInstance().setCreatureDead(opp);
/*  913 */         Players.getInstance().setCreatureDead(opp);
/*  914 */         opp.setTeleportPoints((short)targetX, (short)targetY, opp.getLayer(), 0);
/*  915 */         opp.startTeleporting();
/*  916 */         opp.getCommunicator().sendAlertServerMessage("OUCH! " + creature.getNameWithGenus() + " throws you!");
/*  917 */         opp.getCommunicator().sendTeleport(false);
/*  918 */         if (!opp.isPlayer())
/*      */         {
/*  920 */           opp.getMovementScheme().resumeSpeedModifier();
/*      */         }
/*  922 */         opp.achievement(50);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void bashOpponent(Creature creature) {
/*  933 */     if (creature.opponent != null && (creature.opponent.getPower() <= 0 || creature.opponent.isDominated())) {
/*      */       
/*  935 */       if (creature.opponent.isUnique())
/*      */         return; 
/*  937 */       Creature opp = creature.opponent;
/*      */       
/*  939 */       BlockingResult result = Blocking.getBlockerBetween(creature, opp, 4);
/*  940 */       if (result != null) {
/*      */         return;
/*      */       }
/*      */       
/*  944 */       float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  945 */       Server.getInstance().broadCastAction(creature
/*  946 */           .getNameWithGenus() + replace(this.actionString, "@defender", creature.opponent.getNameWithGenus()), creature, 5, true);
/*      */       
/*      */       try {
/*  949 */         opp.getCommunicator().sendAlertServerMessage(creature.getNameWithGenus() + " bashes you!");
/*      */         
/*  951 */         opp.getStatus().setStunned((Server.rand.nextInt(5) + 4));
/*  952 */         creature.opponent.addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */       
/*      */       }
/*  955 */       catch (Exception ex) {
/*      */         
/*  957 */         logger.log(Level.WARNING, opp.getName() + ": " + ex, ex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void phaseOpponent(Creature creature) {
/*  968 */     if (creature.opponent != null && (creature.opponent.getPower() <= 0 || creature.opponent.isDominated())) {
/*      */       
/*  970 */       if (creature.opponent.isUnique())
/*      */         return; 
/*  972 */       Creature opp = creature.opponent;
/*      */       
/*  974 */       BlockingResult result = Blocking.getBlockerBetween(creature, opp, 4);
/*  975 */       if (result != null) {
/*      */         return;
/*      */       }
/*      */       
/*  979 */       float damage = this.basedam + Server.rand.nextFloat() * this.basedam;
/*  980 */       Server.getInstance().broadCastAction(creature
/*  981 */           .getNameWithGenus() + replace(this.actionString, "@defender", creature.opponent.getNameWithGenus()), creature, 5, true);
/*      */       
/*      */       try {
/*  984 */         opp.getCommunicator().sendAlertServerMessage(creature.getNameWithGenus() + " confuses you!");
/*      */         
/*  986 */         opp.getStatus().setStunned(((byte)Server.rand.nextInt(5) + 4));
/*  987 */         creature.opponent.addWoundOfType(creature, this.woundType, 1, true, 1.0F, true, damage, 0.0F, 0.0F, false, false);
/*      */       
/*      */       }
/*  990 */       catch (Exception ex) {
/*      */         
/*  992 */         logger.log(Level.WARNING, opp.getName() + ": " + ex, ex);
/*      */       } 
/*  994 */       if (creature.getLayer() >= 0) {
/*      */         
/*  996 */         creature.setTarget(-10L, true);
/*  997 */         creature.setOpponent(null);
/*  998 */         opp.setOpponent(null);
/*  999 */         opp.setTarget(-10L, true);
/* 1000 */         float newPosX = creature.getPosX() - 20.0F + Server.rand.nextFloat() * 41.0F;
/* 1001 */         float newPosY = creature.getPosY() - 20.0F + Server.rand.nextFloat() * 41.0F;
/* 1002 */         float newPosZ = Zones.calculatePosZ(newPosX, newPosY, null, creature.isOnSurface(), false, creature.getPositionZ(), creature, creature.getBridgeId());
/* 1003 */         CreatureBehaviour.blinkTo(creature, newPosX, newPosY, creature.getLayer(), newPosZ, creature.getBridgeId(), creature.getFloorLevel());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String replace(String target, String from, String to) {
/* 1024 */     int start = target.indexOf(from);
/* 1025 */     if (start == -1)
/* 1026 */       return target; 
/* 1027 */     int lf = from.length();
/* 1028 */     char[] targetChars = target.toCharArray();
/* 1029 */     StringBuilder buffer = new StringBuilder();
/* 1030 */     int copyFrom = 0;
/* 1031 */     while (start != -1) {
/*      */       
/* 1033 */       buffer.append(targetChars, copyFrom, start - copyFrom);
/* 1034 */       buffer.append(to);
/* 1035 */       copyFrom = start + lf;
/* 1036 */       start = target.indexOf(from, copyFrom);
/*      */     } 
/* 1038 */     buffer.append(targetChars, copyFrom, targetChars.length - copyFrom);
/* 1039 */     return buffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRarity() {
/* 1049 */     return this.rarity;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getName() {
/* 1054 */     return this.name;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\CombatMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */