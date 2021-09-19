/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.CaveTile;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.bodys.Wounds;
/*      */ import com.wurmonline.server.combat.CombatConstants;
/*      */ import com.wurmonline.server.creatures.Brand;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.NotOwnedException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.GmInterface;
/*      */ import com.wurmonline.server.questions.GmTool;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.spells.Spell;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.spells.Spells;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchWallException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.support.Tickets;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchTileException;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import java.io.IOException;
/*      */ import java.util.Optional;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
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
/*      */ public final class Action
/*      */   implements MiscConstants, CounterTypes, CombatConstants, TimeConstants
/*      */ {
/*      */   private static final String YOU_ARE_NOW_TOO_FAR_AWAY_TO = "You are now too far away to ";
/*      */   private static final String YOU_ARE_TOO_FAR_AWAY_TO_DO_THAT = "You are too far away to do that.";
/*      */   public static final String MESSAGEPLACEHOLDER = "This is a placeholder message.";
/*      */   static final String NOT_ALLOWED_DEED_ACTION_BY_LEGAL_PLAYER_MESSAGE = "That would be illegal here. You can check the settlement token for the local laws.";
/*      */   static final String NOT_ALLOWED_ACTION_BY_LEGAL_PLAYER_MESSAGE = "That would be illegal. ";
/*      */   static final String NOT_SAME_BRIDGE = "You need to be on the same bridge in order to do that. ";
/*      */   public static final String NOT_ALLOWED_ACTION_ON_FREEDOM_MESSAGE = "That would be very bad for your karma and is disallowed on this server.";
/*      */   private static final String NOT_ALLOWED_ACTION_TREE_ON_FREEDOM_MESSAGE = "This action is not allowed here, because the tree is on a player owned deed that has disallowed it.";
/*      */   static final String NOT_ALLOWED_ACTION_TILE_ON_FREEDOM_MESSAGE = "This action is not allowed here, because the tile is on a player owned deed that has disallowed it.";
/*      */   static final String GUARD_WARNS_A_PLAYER_MESSAGE = "A guard has noted you and stops you with a warning.";
/*      */   static final String NO_SENSE = "That action makes no sense here.";
/*      */   static final short BUILD_ACTIONS = 20000;
/*      */   public static final short CREATE_ACTIONS = 10000;
/*      */   public static final short RECIPE_ACTIONS = 8000;
/*  123 */   private static final Logger logger = Logger.getLogger(Action.class.getName());
/*      */   
/*      */   private Item destroyedItem;
/*  126 */   private Creature tempCreature = null;
/*      */   
/*      */   private final Creature performer;
/*      */   
/*      */   private long subject;
/*      */   
/*  132 */   private long lastPolledAction = 0L;
/*      */ 
/*      */ 
/*      */   
/*  136 */   private long target = -10L;
/*      */   
/*      */   private long[] targets;
/*      */   
/*  140 */   private int numbTargets = 0;
/*      */   
/*      */   private final Behaviour behaviour;
/*      */   
/*      */   private short action;
/*      */   
/*      */   private boolean done = false;
/*      */   
/*  148 */   private byte rarity = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private float posX;
/*      */ 
/*      */ 
/*      */   
/*      */   private float posY;
/*      */ 
/*      */ 
/*      */   
/*      */   private final float posZ;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean personalAction = false;
/*      */ 
/*      */ 
/*      */   
/*  168 */   private int tilex = -1;
/*      */   
/*  170 */   private int tiley = -1;
/*      */   
/*  172 */   private int tilez = -1;
/*      */   
/*      */   private boolean onSurface = true;
/*      */   
/*  176 */   private int tile = -1;
/*      */   
/*  178 */   private int heightOffset = -1;
/*      */ 
/*      */   
/*      */   private final float rot;
/*      */ 
/*      */   
/*  184 */   private float counter = 0.0F;
/*      */   
/*      */   private int targetType;
/*      */   
/*  188 */   private int tenthOfSecondsLeftOnAction = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  193 */   private float failSecond = 2.14748365E9F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  198 */   private float power = 0.1F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  203 */   private String actionString = "";
/*      */   
/*  205 */   private Wall wall = null;
/*      */   
/*  207 */   private Fence fence = null;
/*      */   
/*      */   private boolean isSpell = false;
/*      */   
/*      */   private boolean isOffensive = false;
/*      */   
/*  213 */   private int triggerCounter = 0;
/*      */   
/*  215 */   private int currentSecond = -1;
/*  216 */   private int lastSecond = 0;
/*      */ 
/*      */   
/*      */   private boolean justTickedSecond = true;
/*      */   
/*  221 */   private float nextTick = 10.0F;
/*  222 */   private int tickCount = 0;
/*      */ 
/*      */   
/*  225 */   private long data = 0L;
/*  226 */   private byte auxByte = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean manualInvulnerable = false;
/*      */ 
/*      */   
/*      */   private Spell spell;
/*      */ 
/*      */ 
/*      */   
/*      */   public float getNextTick() {
/*  238 */     return this.nextTick;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNextTick(float aNextTick) {
/*  249 */     this.nextTick = aNextTick;
/*      */   }
/*      */ 
/*      */   
/*      */   public void incNextTick(float aIncTick) {
/*  254 */     this.nextTick += aIncTick;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTickCount() {
/*  259 */     return this.tickCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTickCount(int aTickCount) {
/*  264 */     this.tickCount = aTickCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void incTickCount() {
/*  269 */     this.tickCount++;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getData() {
/*  274 */     return this.data;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setData(long newData) {
/*  279 */     this.data = newData;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getAuxByte() {
/*  284 */     return this.auxByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAuxByte(byte newByte) {
/*  289 */     this.auxByte = newByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Action(Creature aPerformer, long aSubj, long _target, short act, float aPosX, float aPosY, float aPosZ, float aRot) throws NoSuchPlayerException, NoSuchCreatureException, NoSuchItemException, NoSuchBehaviourException, FailedException, NoSuchWallException {
/*  296 */     this.performer = aPerformer;
/*  297 */     this.subject = aSubj;
/*      */     
/*  299 */     this.target = _target;
/*  300 */     this.numbTargets = 1;
/*      */     
/*  302 */     this.action = act;
/*  303 */     this.posX = aPosX;
/*  304 */     this.posY = aPosY;
/*  305 */     this.posZ = aPosZ;
/*  306 */     this.rot = aRot;
/*  307 */     this.targetType = WurmId.getType(this.target);
/*      */     
/*  309 */     if (this.action >= 0) {
/*      */       
/*  311 */       if (needsFood() && aPerformer.getPower() <= 1)
/*      */       {
/*  313 */         if (aPerformer.getStatus().getHunger() > 60000)
/*  314 */           throw new FailedException("You are too hungry."); 
/*      */       }
/*  316 */       boolean isEmote = (this.action >= 2000 && this.action < 8000);
/*  317 */       if (!isEmote) {
/*      */         
/*  319 */         this.isSpell = Actions.actionEntrys[getNumber()].isSpell();
/*  320 */         this.isOffensive = Actions.actionEntrys[getNumber()].isOffensive();
/*      */         
/*  322 */         if (this.isSpell) {
/*      */           
/*  324 */           this.spell = Spells.getSpell(getNumber());
/*  325 */           if (this.spell != null && this.spell.isReligiousSpell()) {
/*      */             
/*  327 */             if (!this.performer.isSpellCaster() && !this.performer.isSummoner()) {
/*      */               
/*  329 */               if (aPerformer.getDeity() == null)
/*  330 */                 throw new FailedException("You do not follow a deity."); 
/*  331 */               if (!aPerformer.isPriest())
/*  332 */                 throw new FailedException("You are not a priest."); 
/*  333 */               if (!aPerformer.getDeity().hasSpell(this.spell))
/*  334 */                 throw new FailedException(aPerformer.getDeity().getName() + " does not bestow the power of '" + this.spell.getName() + "'."); 
/*  335 */               if (this.spell.level > (int)aPerformer.getFaith()) {
/*  336 */                 throw new FailedException("You must reach a higher level of faith to cast this spell.");
/*      */               }
/*      */             } 
/*  339 */           } else if (!aPerformer.knowsKarmaSpell(getNumber())) {
/*  340 */             throw new FailedException("You do not know how to cast '" + this.spell.getName() + "'.");
/*      */           } 
/*      */         } 
/*      */         
/*  344 */         if (aPerformer.isPlayer() && !isQuick()) {
/*  345 */           ((Player)aPerformer).resetInactivity(true);
/*      */         }
/*  347 */         if (aPerformer.isGuest())
/*      */         {
/*  349 */           if (!isActionGuest(this.action)) {
/*  350 */             throw new FailedException("Guests may mostly go around and look at things. You need to register to do other things.");
/*      */           }
/*      */         }
/*  353 */         if (getActionEntry().isStanceChange() || getActionEntry().isDefend() || getNumber() == 105) {
/*      */           
/*  355 */           if (aPerformer.getTarget() != null) {
/*      */             
/*  357 */             this.subject = -1L;
/*  358 */             this.target = aPerformer.target;
/*      */             
/*      */             try {
/*  361 */               Creature tc = Server.getInstance().getCreature(this.target);
/*  362 */               if (Creature.rangeTo(aPerformer, tc) > Actions.actionEntrys[getNumber()].getRange())
/*      */               {
/*  364 */                 if (aPerformer.opponent != null)
/*      */                 {
/*  366 */                   this.target = aPerformer.opponent.getWurmId();
/*      */                 }
/*      */               }
/*      */             }
/*  370 */             catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */             
/*  373 */             } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */           
/*      */           }
/*  377 */           else if (this.action != 340) {
/*      */             
/*  379 */             this.subject = -1L;
/*  380 */             this.target = aPerformer.getWurmId();
/*      */           } else {
/*      */             
/*  383 */             throw new FailedException("You need to be in combat in order to focus.");
/*      */           } 
/*  385 */         } else if (getNumber() == 342) {
/*      */           
/*  387 */           if (this.target == -10L) {
/*  388 */             this.target = aPerformer.target;
/*      */           }
/*  390 */         } else if (getNumber() >= 197 && getNumber() <= 208) {
/*      */           
/*  392 */           if ((this.targetType != 1 && this.targetType != 0) || this.target == aPerformer
/*  393 */             .getWurmId())
/*      */           {
/*  395 */             if (aPerformer.getTarget() != null) {
/*  396 */               this.target = aPerformer.target;
/*      */             } else {
/*      */               
/*  399 */               this.subject = -1L;
/*  400 */               this.target = aPerformer.getWurmId();
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*  405 */       if (isFatigue()) {
/*      */         
/*  407 */         if (this.performer.getVehicle() != -10L && !Actions.isActionAllowedOnVehicle(this.action))
/*      */         {
/*  409 */           if (Actions.isActionAllowedOnBoat(this.action)) {
/*      */ 
/*      */             
/*      */             try {
/*  413 */               Item vehicle = Items.getItem(this.performer.getVehicle());
/*  414 */               if (!vehicle.isBoat())
/*  415 */                 throw new FailedException("You need to be on solid ground to do that."); 
/*  416 */               if (vehicle.getPosZ() > 0.0F) {
/*  417 */                 throw new FailedException("The boat must be on water to do that from the boat.");
/*      */               }
/*  419 */             } catch (NoSuchItemException e) {
/*      */               
/*  421 */               throw new FailedException("You need to be on solid ground to do that.");
/*      */             } 
/*      */           } else {
/*      */             
/*  425 */             throw new FailedException("You need to be on solid ground to do that.");
/*      */           }  } 
/*  427 */         if (aPerformer.getFatigueLeft() <= 0)
/*  428 */           throw new FailedException("You are too mentally exhausted to do that now."); 
/*  429 */         if (this.performer.getBridgeId() != -10L && this.action == 109)
/*  430 */           throw new FailedException("You can't track on a bridge."); 
/*      */       } 
/*      */     } 
/*  433 */     this.behaviour = getBehaviour(this.target, aPerformer.isOnSurface());
/*  434 */     this.onSurface = getIsOnSurface(this.target, aPerformer.isOnSurface());
/*  435 */     this.targetType = WurmId.getType(this.target);
/*      */     
/*  437 */     if (!aPerformer.isActionFaithful(this) && !aPerformer.isChampion())
/*  438 */       throw new FailedException((aPerformer.getDeity()).name + " does not accept that action."); 
/*  439 */     if (WurmId.getType(this.subject) == 8 || WurmId.getType(this.subject) == 32)
/*  440 */       this.subject = -1L; 
/*  441 */     if (this.action >= 8000 || (this.subject != -1L && (
/*      */       
/*  443 */       WurmId.getType(this.subject) == 2 || WurmId.getType(this.subject) == 6 || 
/*  444 */       WurmId.getType(this.subject) == 19 || WurmId.getType(this.subject) == 20))) {
/*      */       
/*      */       try {
/*      */         
/*  448 */         Item item = Items.getItem(this.subject);
/*  449 */         if (!item.deleted) {
/*      */           
/*  451 */           long owner = -10L;
/*      */ 
/*      */           
/*      */           try {
/*  455 */             if (getNumber() < 0 || 
/*  456 */               getNumber() >= Actions.actionEntrys.length || 
/*  457 */               !getActionEntry().isUseItemOnGroundAction()) {
/*  458 */               owner = item.getOwner();
/*      */             }
/*  460 */             if (this.isSpell && this.spell != null)
/*      */             {
/*  462 */               if (!this.performer.isSpellCaster() && !this.performer.isSummoner())
/*      */               {
/*  464 */                 if (this.spell.isReligiousSpell() && aPerformer.getDeity() != null) {
/*      */                   
/*  466 */                   if (!item.isHolyItem(aPerformer.getDeity())) {
/*  467 */                     throw new FailedException("You must activate your deity's channeling item.");
/*      */                   }
/*  469 */                 } else if (this.spell.isSorcerySpell()) {
/*      */                   
/*  471 */                   if (!item.isMagicStaff() && !((item.getTemplateId() == 176 && aPerformer.getPower() >= 2 && Servers.isThisATestServer()) ? 1 : 0))
/*  472 */                     throw new FailedException("You must activate a magical staff to use this spell."); 
/*      */                 } 
/*      */               }
/*      */             }
/*  476 */             if (getNumber() == -1)
/*      */             {
/*  478 */               if (item.isWeaponBow() && this.performer.isArcheryMode())
/*  479 */                 this.action = 124; 
/*      */             }
/*  481 */             if (item.mailed)
/*      */             {
/*  483 */               throw new FailedException("You cannot use " + item.getName() + " right now.");
/*      */             }
/*  485 */             if (owner != aPerformer.getWurmId() && !item.isTraded() && 
/*  486 */               getNumber() >= 0 && 
/*  487 */               getNumber() < Actions.actionEntrys.length && 
/*  488 */               !getActionEntry().isUseItemOnGroundAction()) {
/*      */               
/*  490 */               aPerformer.getCommunicator().sendSafeServerMessage("You are using an item that belongs to someone else! Please report this bug.");
/*      */               
/*  492 */               logger.warning(aPerformer.getName() + " tries to use an item, " + item.getWurmId() + " which is owned by someone else, " + owner + "!");
/*      */               
/*  494 */               throw new NoSuchItemException(aPerformer.getName() + " tries to use an item which is owned by someone else!");
/*      */             } 
/*      */             
/*  497 */             if (item.isUseOnGroundOnly() && this.target > 0L && 
/*  498 */               !isEmote() && getActionEntry().isBlockedByUseOnGroundOnly()) {
/*      */               
/*  500 */               if (isQuick()) {
/*      */ 
/*      */                 
/*      */                 try {
/*  504 */                   if (this.targetType == 2 || this.targetType == 19 || this.targetType == 20 || this.targetType == 6)
/*      */                   {
/*      */                     
/*  507 */                     Item targ = Items.getItem(this.target);
/*  508 */                     throw new FailedException("You need to use the " + targ.getName() + " on the " + item
/*  509 */                         .getName() + " while it is on the ground.");
/*      */                   }
/*      */                 
/*  512 */                 } catch (NoSuchItemException noSuchItemException) {}
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/*  517 */                 throw new FailedException("The " + item.getName() + " needs to be on the ground when used.");
/*      */               } 
/*  519 */             } else if (item.isTraded()) {
/*      */               
/*  521 */               if (this.action != 1 && this.action != 87 && !isActionEmote(this.action)) {
/*  522 */                 throw new FailedException("You cannot use " + item.getName() + " while trading it.");
/*      */               }
/*  524 */             } else if (item.isBanked()) {
/*      */               
/*  526 */               if (this.action != 1 && this.action != 87 && !isActionEmote(this.action))
/*  527 */                 throw new FailedException("You cannot use " + item.getName() + " while it is banked."); 
/*      */             } 
/*  529 */             if (item.getTemplateId() == 176 || item.getTemplateId() == 315)
/*      */             {
/*  531 */               if (aPerformer.getPower() < 1 && 
/*  532 */                 !Players.isArtist(this.performer.getWurmId(), false, false))
/*      */               {
/*  534 */                 logger.warning(aPerformer + " tried to use a wand, " + item + ", but their power is " + aPerformer
/*  535 */                     .getPower() + ", action: " + this.action);
/*  536 */                 throw new FailedException("You cannot use the " + item.getName() + ".");
/*      */               }
/*      */             
/*      */             }
/*  540 */           } catch (NotOwnedException nex) {
/*      */             
/*  542 */             if (logger.isLoggable(Level.FINER))
/*      */             {
/*  544 */               logger.log(Level.WARNING, aPerformer.getName() + " tries to use " + item.getName() + " on the ground.", (Throwable)nex);
/*      */             }
/*      */             
/*  547 */             aPerformer.getCommunicator().sendSafeServerMessage("You must carry the " + item
/*  548 */                 .getName() + " to use it.");
/*  549 */             throw new NoSuchItemException(aPerformer.getName() + " tries to use an item on the ground.");
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  554 */           if (logger.isLoggable(Level.FINER))
/*      */           {
/*  556 */             logger.finer(item + " deleted");
/*      */           }
/*  558 */           throw new NoSuchItemException("Item deleted");
/*      */         }
/*      */       
/*  561 */       } catch (NoSuchItemException nsex) {
/*      */         
/*  563 */         this.subject = -1L;
/*      */       } 
/*      */     }
/*      */     
/*  567 */     if (this.targetType == 5) {
/*      */       
/*  569 */       if (this.action == -1)
/*  570 */         this.action = 1; 
/*  571 */       if (this.action == 162)
/*  572 */         this.action = 193; 
/*  573 */       this.tilex = Tiles.decodeTileX(this.target);
/*  574 */       this.tiley = Tiles.decodeTileY(this.target);
/*  575 */       this.heightOffset = Tiles.decodeHeightOffset(this.target);
/*  576 */       this.onSurface = (Tiles.decodeLayer(this.target) == 0);
/*      */       
/*  578 */       for (int xx = 1; xx >= -1; xx--) {
/*      */         
/*  580 */         for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */           
/*      */           try {
/*  584 */             Zone zone = Zones.getZone(this.tilex + xx, this.tiley + yy, this.onSurface);
/*  585 */             VolaTile lTile = zone.getTileOrNull(this.tilex + xx, this.tiley + yy);
/*  586 */             if (lTile != null) {
/*      */               
/*  588 */               Wall[] walls = lTile.getWalls();
/*  589 */               for (int s = 0; s < walls.length; s++) {
/*      */                 
/*  591 */                 if (walls[s].getId() == this.target) {
/*      */                   
/*  593 */                   this.wall = walls[s];
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */             } 
/*  599 */           } catch (NoSuchZoneException noSuchZoneException) {}
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  605 */       if (this.wall == null) {
/*  606 */         throw new NoSuchWallException("No wall with id " + this.target);
/*      */       }
/*      */       try {
/*  609 */         VolaTile t = this.wall.getOrCreateInnerTile(this.onSurface);
/*  610 */         this.tilex = t.tilex;
/*  611 */         this.tiley = t.tiley;
/*      */       }
/*  613 */       catch (NoSuchTileException nst) {
/*      */         
/*  615 */         logger.log(Level.WARNING, "tile at " + this.tilex + ", " + this.tiley + " " + nst.getMessage(), (Throwable)nst);
/*      */       }
/*  617 */       catch (NoSuchZoneException nsz) {
/*      */         
/*  619 */         logger.log(Level.WARNING, "tile at " + this.tilex + ", " + this.tiley + " " + nsz.getMessage(), (Throwable)nsz);
/*      */       } 
/*  621 */       if (this.action >= 8000 || this.action < 2000) {
/*      */         
/*  623 */         if (!aPerformer.isWithinTileDistanceTo((int)(this.target >> 32L) & 0xFFFF, (int)(this.target >> 16L) & 0xFFFF, this.heightOffset, 1))
/*      */         {
/*      */           
/*  626 */           if (this.action > 2000 || !getActionEntry().isIgnoresRange())
/*  627 */             throw new FailedException("You are too far away to do that."); 
/*      */         }
/*  629 */         if (!Actions.isActionManage(this.action) && 
/*  630 */           !Methods.isActionAllowed(aPerformer, this.action, this.wall.getTileX(), this.wall.getTileY())) {
/*  631 */           throw new FailedException("This is a placeholder message.");
/*      */         }
/*      */       } 
/*  634 */     } else if (this.targetType == 2 || this.targetType == 6 || this.targetType == 19 || this.targetType == 20) {
/*      */ 
/*      */       
/*  637 */       if (this.action == -1)
/*  638 */         this.action = 1; 
/*  639 */       Item targ = Items.getItem(this.target);
/*  640 */       this.tilex = (int)targ.getPosX() >> 2;
/*  641 */       this.tiley = (int)targ.getPosY() >> 2;
/*  642 */       if (targ.isTraded()) {
/*      */         
/*  644 */         if (this.action != 1 && this.action != 87 && !isActionEmote(this.action)) {
/*  645 */           throw new FailedException("You cannot use " + targ.getName() + " while trading it.");
/*      */         }
/*  647 */       } else if (targ.isBanked()) {
/*      */         
/*  649 */         if (this.action != 1 && this.action != 87 && !isActionEmote(this.action))
/*  650 */           throw new FailedException("You cannot use " + targ.getName() + " while it is banked."); 
/*      */       } 
/*  652 */       if (targ.mailed)
/*      */       {
/*  654 */         throw new FailedException("You cannot use " + targ.getName() + " right now.");
/*      */       }
/*  656 */       if (this.action == 851 && targ.isBusy() && targ
/*  657 */         .getTemplateId() == 1125 && !targ.isUnfinished())
/*      */       {
/*  659 */         throw new FailedException("You cannot use the " + targ.getName() + " right now as it is already being used.");
/*      */       }
/*      */ 
/*      */       
/*  663 */       if (aPerformer.getCurrentTile().getStructure() != null && 
/*  664 */         !aPerformer.isWithinDistanceToZ(targ.getPosZ(), 4.0F, true) && 
/*  665 */         !isActionEmote(this.action) && 
/*  666 */         !getActionEntry().isIgnoresRange() && 
/*  667 */         !Actions.isActionAllowedThroughFloors(this.action))
/*      */       {
/*  669 */         throw new FailedException("You are too far away to do that.");
/*      */       }
/*      */       
/*  672 */       if (isSameBridge(this.action)) {
/*      */         
/*  674 */         Item parent = Items.getItem(targ.getTopParent());
/*      */         
/*  676 */         if (parent.getBridgeId() != -10L) {
/*      */           
/*  678 */           BridgePart bridgePart = Zones.getBridgePartFor(parent.getTileX(), parent.getTileY(), parent.isOnSurface());
/*  679 */           if (bridgePart == null || bridgePart.getStructureId() != parent.getBridgeId())
/*      */           {
/*      */             
/*  682 */             parent.setOnBridge(-10L);
/*      */           }
/*      */         } 
/*  685 */         if (this.performer.getBridgeId() != parent.getBridgeId() && this.performer.getWurmId() != targ.getOwnerId())
/*      */         {
/*      */           
/*  688 */           if (this.performer.getBridgeId() == -10L && parent.getBridgeId() != -10L) {
/*      */ 
/*      */             
/*  691 */             int ix = parent.getTileX();
/*  692 */             int iy = parent.getTileY();
/*  693 */             int px = this.performer.getTileX();
/*  694 */             int py = this.performer.getTileY();
/*      */             
/*  696 */             if (ix != px && iy != py) {
/*  697 */               throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */             }
/*      */             
/*  700 */             BridgePart bridgePart = Zones.getBridgePartFor(ix, iy, parent.isOnSurface());
/*  701 */             if (bridgePart != null && bridgePart.hasAnExit()) {
/*      */               
/*  703 */               if (iy < py) {
/*      */ 
/*      */                 
/*  706 */                 if (!bridgePart.hasSouthExit()) {
/*  707 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*  709 */               } else if (ix > px) {
/*      */ 
/*      */                 
/*  712 */                 if (!bridgePart.hasWestExit()) {
/*  713 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*  715 */               } else if (iy > py) {
/*      */ 
/*      */                 
/*  718 */                 if (!bridgePart.hasNorthExit()) {
/*  719 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*  721 */               } else if (ix < px) {
/*      */ 
/*      */                 
/*  724 */                 if (!bridgePart.hasEastExit()) {
/*  725 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*      */               } else {
/*  728 */                 throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */               } 
/*      */             } else {
/*  731 */               throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */             } 
/*  733 */           } else if (this.performer.getBridgeId() != -10L && parent.getBridgeId() == -10L) {
/*      */ 
/*      */             
/*  736 */             int ix = parent.getTileX();
/*  737 */             int iy = parent.getTileY();
/*  738 */             int px = this.performer.getTileX();
/*  739 */             int py = this.performer.getTileY();
/*      */ 
/*      */             
/*  742 */             BridgePart bridgePart = Zones.getBridgePartFor(px, py, parent.isOnSurface());
/*  743 */             if (bridgePart != null && bridgePart.hasAnExit()) {
/*      */               
/*  745 */               if (iy < py) {
/*      */ 
/*      */                 
/*  748 */                 if (!bridgePart.hasNorthExit()) {
/*  749 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*  751 */               } else if (ix > px) {
/*      */ 
/*      */                 
/*  754 */                 if (!bridgePart.hasEastExit()) {
/*  755 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*  757 */               } else if (iy > py) {
/*      */ 
/*      */                 
/*  760 */                 if (!bridgePart.hasSouthExit()) {
/*  761 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*  763 */               } else if (ix < px) {
/*      */ 
/*      */                 
/*  766 */                 if (!bridgePart.hasWestExit()) {
/*  767 */                   throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */                 }
/*      */               } else {
/*  770 */                 throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */               } 
/*      */             } else {
/*  773 */               throw new FailedException("You need to be on the same bridge in order to do that. ");
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */       try {
/*  779 */         long towner = targ.getOwner();
/*  780 */         if (this.action == 7 && targ.isBusy()) {
/*  781 */           throw new FailedException("You are using that item.");
/*      */         }
/*      */         
/*  784 */         if (towner != aPerformer.getWurmId()) {
/*      */           
/*  786 */           if (this.action != 1 && this.action != 87 && !isActionEmote(this.action) && this.action != 185)
/*      */           {
/*  788 */             throw new FailedException("You need to carry that item to use it.");
/*      */           }
/*  790 */         } else if (this.action == 7 && (targ
/*  791 */           .getTemplateId() == 26 || targ.getTemplateId() == 298)) {
/*      */           
/*  793 */           if (!MethodsItems.mayDropDirt(aPerformer))
/*  794 */             throw new FailedException("You are not allowed to drop dirt there."); 
/*      */         } else {
/*  796 */           if (targ.isTraded() && this.action != 1 && this.action != 87 && 
/*  797 */             !isActionEmote(this.action))
/*      */           {
/*  799 */             throw new FailedException("You may not tamper with items you are trading.");
/*      */           }
/*  801 */           if (targ.isUseOnGroundOnly() && !isQuick() && this.action != 100 && this.action != 176 && this.action != 180 && this.action != 633 && this.action != 925 && this.action != 926)
/*      */           {
/*      */ 
/*      */ 
/*      */             
/*  806 */             throw new FailedException("You may only use that item while it is on the ground.");
/*      */           }
/*      */         } 
/*  809 */         if (towner == this.performer.getWurmId())
/*  810 */           this.personalAction = true; 
/*  811 */         if (this.action == 162) {
/*  812 */           this.personalAction = true;
/*      */         }
/*  814 */       } catch (NotOwnedException nso) {
/*      */         
/*  816 */         if (targ.getZoneId() < 0)
/*      */         {
/*  818 */           logger.log(Level.WARNING, aPerformer
/*  819 */               .getName() + " interacting with a " + targ.getName() + "(id=" + targ.getWurmId() + ") not in the world action=" + this.action);
/*      */         }
/*      */         
/*  822 */         float iposX = targ.getPosX();
/*  823 */         float iposY = targ.getPosY();
/*  824 */         float iposZ = targ.getPosZ();
/*  825 */         boolean ok = (aPerformer.isOnSurface() == targ.isOnSurface());
/*  826 */         if (!ok) {
/*      */           
/*  828 */           VolaTile lTile = Zones.getOrCreateTile((int)iposX >> 2, (int)iposY >> 2, targ.isOnSurface());
/*  829 */           if (lTile.isTransition && this.action != 74 && this.action != 3 && this.action != 6 && this.action != 100)
/*      */           {
/*      */             
/*  832 */             if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile((int)iposX >> 2, (int)iposY >> 2)))) {
/*  833 */               ok = false;
/*      */             } else {
/*  835 */               ok = true;
/*      */             }  } 
/*      */         } 
/*  838 */         if (!ok) {
/*      */           
/*  840 */           VolaTile lTile = Zones.getOrCreateTile(aPerformer.getTileX(), aPerformer.getTileY(), aPerformer
/*  841 */               .isOnSurface());
/*  842 */           if (lTile.isTransition && this.action != 74 && this.action != 3 && this.action != 6 && this.action != 100)
/*      */           {
/*      */             
/*  845 */             if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(aPerformer.getTileX(), aPerformer
/*  846 */                     .getTileY())))) {
/*  847 */               ok = false;
/*      */             } else {
/*  849 */               ok = true;
/*      */             }  } 
/*      */         } 
/*  852 */         if (!ok)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  858 */           if (aPerformer.getPower() >= 2)
/*      */           {
/*  860 */             if (this.action == 185 || this.action == 179)
/*      */             {
/*  862 */               ok = true;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  870 */         if (ok) {
/*      */           
/*  872 */           if (targ.isUseOnGroundOnly() && !isActionEmote(this.action) && getActionEntry().isBlockedByUseOnGroundOnly() && (
/*  873 */             !isActionRecipe(this.action) || targ.getTemplateId() != 768) && targ
/*  874 */             .getParentId() != -10L && !isQuick() && this.action != 192 && this.action != 162 && this.action != 100 && this.action != 176 && this.action != 925 && this.action != 926 && aPerformer
/*      */ 
/*      */             
/*  877 */             .getPower() > 0 && this.action != 180)
/*      */           {
/*  879 */             throw new FailedException("You may only use that item while it is on the ground.");
/*      */           }
/*  881 */           if (isActionEmote(this.action)) {
/*      */             
/*  883 */             if (!aPerformer.isWithinDistanceTo(iposX, iposY, iposZ, Emotes.emoteEntrys[this.action - 2000]
/*  884 */                 .getRange())) {
/*  885 */               throw new FailedException("You are too far away to do that.");
/*      */             }
/*  887 */           } else if (this.action < 2000) {
/*      */ 
/*      */             
/*  890 */             float maxDist = Actions.actionEntrys[this.action].getRange();
/*  891 */             if (targ.isVehicle()) {
/*      */               
/*  893 */               Vehicle vehicle = Vehicles.getVehicle(targ);
/*  894 */               if (vehicle != null)
/*  895 */                 maxDist = Math.max(maxDist, vehicle.getMaxAllowedLoadDistance()); 
/*      */             } 
/*  897 */             if (aPerformer.getPower() < 5 && targ
/*  898 */               .getTopParent() != aPerformer.getVehicle() && 
/*  899 */               !getActionEntry().isIgnoresRange() && 
/*  900 */               !aPerformer.isWithinDistanceTo(iposX, iposY, iposZ, maxDist))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  910 */               throw new FailedException("You are too far away to do that."); } 
/*  911 */             if (Actions.actionEntrys[getNumber()].isPoliced()) {
/*      */               
/*  913 */               if (MethodsItems.checkIfStealing(targ, aPerformer, this) && 
/*  914 */                 checkLegalMode(aPerformer))
/*  915 */                 throw new NoSuchItemException("This is a placeholder message."); 
/*  916 */               if (targ.isHollow()) {
/*      */                 
/*  918 */                 Item[] items = targ.getAllItems(false);
/*  919 */                 for (int x = 0; x < items.length; x++) {
/*  920 */                   if (MethodsItems.checkIfStealing(items[x], aPerformer, this) && 
/*  921 */                     checkLegalMode(aPerformer))
/*  922 */                     throw new NoSuchItemException("This is a placeholder message."); 
/*      */                 } 
/*      */               } 
/*  925 */             } else if (this.action == 362) {
/*      */               
/*  927 */               Village village = Zones.getVillage(this.tilex, this.tiley, aPerformer.isOnSurface());
/*  928 */               if (village != null)
/*      */               {
/*  930 */                 if (!village.isActionAllowed(this.action, aPerformer))
/*      */                 {
/*  932 */                   if (!Zones.isOnPvPServer(this.tilex, this.tiley))
/*      */                   {
/*  934 */                     throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it.");
/*      */                   }
/*  936 */                   failCheckEnemy(aPerformer, village);
/*      */                 }
/*      */               
/*      */               }
/*      */             } 
/*  941 */           } else if (this.action >= 8000) {
/*      */             
/*  943 */             if (!targ.isNoTake() && !targ.isUseOnGroundOnly())
/*      */             {
/*  945 */               throw new FailedException("You need to carry that item to work with it.");
/*      */             }
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  951 */           throw new NoSuchItemException("You are too far away from " + targ.getNameWithGenus() + ".");
/*      */         } 
/*      */       } 
/*  954 */       if (targ.getBless() != null && aPerformer.getDeity() != null && aPerformer
/*  955 */         .getDeity().accepts((targ.getBless()).alignment))
/*      */       {
/*  957 */         if (this.isOffensive)
/*      */         {
/*  959 */           if (aPerformer.faithful)
/*      */           {
/*  961 */             throw new FailedException(aPerformer.getDeity() + " would not approve of that since the " + targ
/*  962 */                 .getName() + " has " + aPerformer.getDeity().getHisHerItsString() + " blessings.");
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  968 */             if (targ.isDomainItem()) {
/*      */               
/*  970 */               aPerformer.getCommunicator().sendAlertServerMessage(
/*  971 */                   (aPerformer.getDeity()).name + " noticed you and is outraged that you desecrate " + aPerformer
/*  972 */                   .getDeity().getHisHerItsString() + " altars.");
/*  973 */               aPerformer.setFavor(aPerformer.getFavor() - 10.0F);
/*  974 */               aPerformer.modifyFaith(-1.0F);
/*  975 */               VolaTile t = aPerformer.getCurrentTile();
/*  976 */               if (t != null) {
/*  977 */                 aPerformer.calculateZoneBonus(t.tilex, t.tiley, aPerformer.isOnSurface());
/*      */               }
/*  979 */             } else if (Server.rand.nextInt(100) > aPerformer.getFaith() - 10.0F) {
/*      */               
/*  981 */               aPerformer.getCommunicator().sendAlertServerMessage(
/*  982 */                   (aPerformer.getDeity()).name + " noticed you and is upset since the " + targ.getName() + " has " + aPerformer
/*  983 */                   .getDeity().getHisHerItsString() + " blessings.");
/*  984 */               aPerformer.setFavor(aPerformer.getFavor() - 10.0F);
/*  985 */               aPerformer.modifyFaith(-0.25F);
/*  986 */               VolaTile t = aPerformer.getCurrentTile();
/*  987 */               if (t != null) {
/*  988 */                 aPerformer.calculateZoneBonus(t.tilex, t.tiley, aPerformer.isOnSurface());
/*      */               }
/*      */             } 
/*  991 */           } catch (IOException iox) {
/*      */             
/*  993 */             logger.log(Level.WARNING, aPerformer.getName(), iox);
/*  994 */             throw new FailedException("An error occured while trying to attack. Please contact the administrators.");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     }
/* 1001 */     else if (this.targetType == 1 || WurmId.getType(this.target) == 0) {
/*      */       
/* 1003 */       this.personalAction = true;
/* 1004 */       if (this.action == -1) {
/*      */         
/* 1006 */         this.action = 1;
/* 1007 */         if (this.target != aPerformer.getWurmId()) {
/*      */           
/*      */           try {
/*      */             
/* 1011 */             Creature cret = Server.getInstance().getCreature(this.target);
/* 1012 */             if (this.performer.isArcheryMode() && (this.target == aPerformer.target || cret
/* 1013 */               .getAttitude(aPerformer) == 2)) {
/*      */               
/* 1015 */               this.action = 342;
/*      */               
/*      */               try {
/* 1018 */                 Item bow = this.performer.getEquippedWeapon((byte)14, true);
/* 1019 */                 if (bow != null && bow.isWeaponBow()) {
/* 1020 */                   this.action = 124;
/*      */                 } else {
/*      */                   
/* 1023 */                   bow = this.performer.getEquippedWeapon((byte)13, true);
/* 1024 */                   if (bow != null && bow.isWeaponBow()) {
/* 1025 */                     this.action = 124;
/*      */                   }
/*      */                 } 
/* 1028 */               } catch (NoSpaceException spc) {
/*      */                 
/* 1030 */                 logger.log(Level.WARNING, aPerformer.getName() + ": " + spc.getMessage(), (Throwable)spc);
/*      */               } 
/*      */             } 
/*      */             
/* 1034 */             if (this.action == 1)
/*      */             {
/* 1036 */               if (cret.getAttitude(aPerformer) == 2)
/*      */               {
/* 1038 */                 this.action = 326;
/*      */               }
/*      */             }
/*      */           }
/* 1042 */           catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */           
/* 1045 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1053 */       if (this.action == 11)
/* 1054 */         this.action = 331; 
/* 1055 */       Creature tcret = Server.getInstance().getCreature(this.target);
/* 1056 */       boolean ok = (aPerformer.isOnSurface() == tcret.isOnSurface());
/*      */       
/* 1058 */       if (isSameBridge(this.action))
/*      */       {
/* 1060 */         if (this.performer.getBridgeId() != tcret.getBridgeId())
/* 1061 */           throw new FailedException("You need to be on the same bridge in order to do that. "); 
/*      */       }
/* 1063 */       if (!ok) {
/*      */         
/* 1065 */         ok = (this.action == 185 || this.action == 1);
/* 1066 */         if (!ok) {
/*      */ 
/*      */           
/* 1069 */           ok = true;
/* 1070 */           boolean transition = false;
/* 1071 */           if ((tcret.getCurrentTile()).isTransition) {
/*      */             
/* 1073 */             transition = true;
/* 1074 */             if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tcret.getTileX(), tcret.getTileY()))))
/*      */             {
/* 1076 */               ok = false;
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1082 */           if (ok && (aPerformer.getCurrentTile()).isTransition) {
/*      */             
/* 1084 */             transition = true;
/* 1085 */             if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(aPerformer.getTileX(), aPerformer
/* 1086 */                     .getTileY()))))
/*      */             {
/* 1088 */               ok = false;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1093 */           if (!transition)
/* 1094 */             ok = false; 
/*      */         } 
/*      */       } 
/* 1097 */       if (ok) {
/*      */         
/* 1099 */         float iposX = tcret.getStatus().getPositionX();
/* 1100 */         float iposY = tcret.getStatus().getPositionY();
/* 1101 */         float iposZ = tcret.getStatus().getPositionZ() + tcret.getAltOffZ();
/* 1102 */         if (this.action >= 2000 && this.action < 8000) {
/*      */           
/* 1104 */           if (!aPerformer.isWithinDistanceTo(iposX, iposY, iposZ, Emotes.emoteEntrys[this.action - 2000]
/* 1105 */               .getRange())) {
/* 1106 */             throw new FailedException("You are too far away to do that.");
/*      */           }
/*      */         } else {
/*      */           
/* 1110 */           if (!getActionEntry().isIgnoresRange() && 
/* 1111 */             Creature.rangeTo(aPerformer, tcret) > Actions.actionEntrys[getNumber()].getRange() && (!getActionEntry().isSpell() || !this.performer.isSpellCaster())) {
/* 1112 */             throw new FailedException("You are too far away to do that.");
/*      */           }
/* 1114 */           if (aPerformer instanceof Player || aPerformer.isDominated()) {
/*      */             
/* 1116 */             if (this.isSpell && tcret.getPower() > aPerformer.getPower())
/*      */             {
/* 1118 */               throw new FailedException("Your spell dissolves in mid-air.");
/*      */             }
/* 1120 */             if (!aPerformer.equals(tcret) && (this.isOffensive || isActionAttack(this.action) || isActionShoot(this.action))) {
/*      */               
/* 1122 */               Creature realPerformer = aPerformer;
/* 1123 */               if (aPerformer.isDominated())
/*      */               {
/* 1125 */                 if (aPerformer.getDominator() != null)
/*      */                 {
/* 1127 */                   if (tcret != aPerformer.getDominator())
/*      */                   {
/* 1129 */                     realPerformer = aPerformer.getDominator();
/*      */                   }
/*      */                 }
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1137 */               if (realPerformer.isPlayer())
/*      */               {
/* 1139 */                 if (!tcret.isDuelOrSpar(realPerformer)) {
/*      */                   
/* 1141 */                   if (!realPerformer.isOnPvPServer() || !tcret.isOnPvPServer()) {
/*      */                     
/* 1143 */                     if (tcret.isPlayer())
/* 1144 */                       throw new FailedException("That would be very bad for your karma and is disallowed on this server."); 
/* 1145 */                     if (tcret.isDominated() || tcret.getHitched() != null || (tcret
/* 1146 */                       .getLeader() != null && tcret.getLeader().isPlayer())) {
/* 1147 */                       throw new FailedException("That would be very bad for your karma and is disallowed on this server.");
/*      */                     }
/*      */                   } 
/* 1150 */                   if (realPerformer.getCitizenVillage() == null || tcret
/* 1151 */                     .getCurrentVillage() != realPerformer.getCitizenVillage())
/*      */                   {
/* 1153 */                     if (realPerformer.getKingdomTemplateId() != 3)
/*      */                     {
/* 1155 */                       if (tcret.isRidden()) {
/*      */                         
/* 1157 */                         if (realPerformer.isLegal() || !tcret.isOnPvPServer())
/*      */                         {
/* 1159 */                           for (Long riderLong : tcret.getRiders())
/*      */                           {
/*      */                             
/*      */                             try {
/* 1163 */                               Creature rider = Server.getInstance().getCreature(riderLong
/* 1164 */                                   .longValue());
/*      */ 
/*      */                               
/* 1167 */                               boolean rok = !realPerformer.isFriendlyKingdom(rider.getKingdomId());
/* 1168 */                               if (rider.isOkToKillBy(realPerformer))
/* 1169 */                                 rok = true; 
/* 1170 */                               if (!rok)
/*      */                               {
/* 1172 */                                 this.performer.setTarget(-10L, true);
/* 1173 */                                 this.performer.getCommunicator().sendNormalServerMessage("That would be illegal. ");
/*      */                                 
/* 1175 */                                 throw new FailedException("That would be illegal. ");
/*      */                               }
/*      */                             
/*      */                             }
/* 1179 */                             catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */                             
/*      */                             }
/* 1183 */                             catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */                           
/*      */                           }
/*      */ 
/*      */                         
/*      */                         }
/*      */                       }
/* 1190 */                       else if (tcret.getHitched() != null) {
/*      */                         
/* 1192 */                         if (!tcret.getHitched().isCreature()) {
/*      */                           try
/*      */                           {
/*      */                             
/* 1196 */                             Item i = Items.getItem((tcret.getHitched()).wurmid);
/* 1197 */                             long ownid = i.getLastOwnerId();
/*      */                             
/*      */                             try {
/* 1200 */                               byte kingd = Players.getInstance().getKingdomForPlayer(ownid);
/* 1201 */                               if (realPerformer.isFriendlyKingdom(kingd) && 
/* 1202 */                                 !realPerformer.hasBeenAttackedBy(ownid))
/*      */                               {
/* 1204 */                                 if (realPerformer.isLegal() || 
/* 1205 */                                   !tcret.isOnPvPServer())
/*      */                                 {
/*      */                                   
/* 1208 */                                   boolean rok = false;
/*      */ 
/*      */                                   
/*      */                                   try {
/* 1212 */                                     Creature c = Server.getInstance().getCreature(ownid);
/* 1213 */                                     if (c.isOkToKillBy(realPerformer)) {
/* 1214 */                                       rok = true;
/*      */                                     }
/* 1216 */                                   } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */                                   
/* 1220 */                                   if (!rok)
/*      */                                   {
/* 1222 */                                     this.performer.setTarget(-10L, true);
/* 1223 */                                     this.performer
/* 1224 */                                       .getCommunicator()
/* 1225 */                                       .sendNormalServerMessage("That would be illegal. ");
/*      */                                     
/* 1227 */                                     throw new FailedException("That would be illegal. ");
/*      */                                   }
/*      */                                 
/*      */                                 }
/*      */                               
/*      */                               }
/*      */                             }
/* 1234 */                             catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */                           
/*      */                           }
/* 1239 */                           catch (NoSuchItemException nsi)
/*      */                           {
/* 1241 */                             logger.log(Level.INFO, (tcret.getHitched()).wurmid + " no such item:", (Throwable)nsi);
/*      */                           
/*      */                           }
/*      */                         
/*      */                         }
/*      */                       }
/* 1247 */                       else if (tcret.isDominated()) {
/*      */                         
/* 1249 */                         if (realPerformer.isFriendlyKingdom(tcret.getKingdomId()))
/*      */                         {
/* 1251 */                           if (realPerformer.isLegal() || !tcret.isOnPvPServer())
/*      */                           {
/* 1253 */                             boolean iok = false;
/*      */                             
/*      */                             try {
/* 1256 */                               Creature owner = Server.getInstance().getCreature(tcret.dominator);
/*      */                               
/* 1258 */                               if (owner.isOkToKillBy(realPerformer)) {
/* 1259 */                                 iok = true;
/*      */                               }
/* 1261 */                             } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */                             
/* 1265 */                             if (!iok)
/*      */                             {
/* 1267 */                               this.performer.setTarget(-10L, true);
/* 1268 */                               this.performer.getCommunicator().sendNormalServerMessage("That would be illegal. ");
/*      */                               
/* 1270 */                               throw new FailedException("That would be illegal. ");
/*      */                             }
/*      */                           
/*      */                           }
/*      */                         
/*      */                         }
/* 1276 */                       } else if (tcret.getCurrentVillage() != null) {
/*      */                         
/* 1278 */                         Brand brand = Creatures.getInstance().getBrand(tcret.getWurmId());
/* 1279 */                         if (brand != null) {
/*      */                           try
/*      */                           {
/*      */                             
/* 1283 */                             Village villageBrand = Villages.getVillage(
/* 1284 */                                 (int)brand.getBrandId());
/*      */ 
/*      */                             
/* 1287 */                             if (tcret.getCurrentVillage() == villageBrand)
/*      */                             {
/* 1289 */                               if (realPerformer.getCitizenVillage() != tcret.getCurrentVillage() && (
/* 1290 */                                 realPerformer.isLegal() || !tcret.isOnPvPServer()))
/*      */                               {
/* 1292 */                                 this.performer.setTarget(-10L, true);
/* 1293 */                                 this.performer.getCommunicator().sendNormalServerMessage("That would be illegal. ");
/*      */                                 
/* 1295 */                                 throw new FailedException("That would be illegal. ");
/*      */                               }
/*      */                             
/*      */                             }
/*      */                           }
/* 1300 */                           catch (NoSuchVillageException nsv)
/*      */                           {
/* 1302 */                             brand.deleteBrand();
/*      */                           }
/*      */                         
/*      */                         }
/*      */                       } 
/*      */                     }
/*      */                   }
/*      */                 } 
/*      */               }
/* 1311 */             } else if ((this.action != 106 || !tcret.mayCommand(aPerformer)) && (this.action != 331 || 
/* 1312 */               !tcret.mayCommand(aPerformer)) && (this.action != 332 || 
/* 1313 */               !tcret.mayPassenger(aPerformer))) {
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
/* 1337 */               this.tilex = (int)iposX >> 2;
/* 1338 */               this.tiley = (int)iposY >> 2;
/* 1339 */               Village village = Zones.getVillage(this.tilex, this.tiley, tcret.isOnSurface());
/* 1340 */               if (village != null && !Actions.isActionManage(this.action))
/*      */               {
/* 1342 */                 if (this.action == 106 || this.action == 331 || this.action == 332) {
/*      */ 
/*      */                   
/* 1345 */                   if (tcret.getDominator() != aPerformer) {
/*      */                     
/* 1347 */                     boolean owner = Creatures.getInstance().wasLastLed(this.performer.getWurmId(), tcret
/* 1348 */                         .getWurmId());
/* 1349 */                     Brand brand = Creatures.getInstance().getBrand(tcret.getWurmId());
/* 1350 */                     if (brand != null) {
/*      */                       
/*      */                       try {
/*      */                         
/* 1354 */                         Village v = Villages.getVillage((int)brand.getBrandId());
/* 1355 */                         if (v != null && v == this.performer.getCitizenVillage()) {
/* 1356 */                           owner = true;
/*      */                         }
/* 1358 */                       } catch (NoSuchVillageException nsv) {
/*      */                         
/* 1360 */                         brand.deleteBrand();
/*      */                       } 
/*      */                     }
/* 1363 */                     if (!owner)
/*      */                     {
/* 1365 */                       if (!village.isActionAllowed(this.action, aPerformer, false, 0, 0))
/*      */                       {
/* 1367 */                         if (!this.performer.isOnPvPServer() || !tcret.isOnPvPServer())
/* 1368 */                           throw new FailedException("That would be very bad for your karma and is disallowed on this server."); 
/* 1369 */                         failCheckEnemy(aPerformer, village);
/*      */                       }
/*      */                     
/*      */                     }
/*      */                   } 
/* 1374 */                 } else if (!tcret.isDuelOrSpar(aPerformer)) {
/*      */                   
/* 1376 */                   if (!village.isActionAllowed(this.action, aPerformer, false, 0, 0))
/*      */                   {
/* 1378 */                     failCheckEnemy(aPerformer, village);
/*      */                   }
/*      */                 }
/*      */               
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/* 1388 */         throw new FailedException("You are too far away from " + tcret.getNameWithGenus() + ".");
/*      */       }
/*      */     
/* 1391 */     } else if (this.targetType == 7) {
/*      */       
/* 1393 */       if (this.action == -1)
/* 1394 */         this.action = 1; 
/* 1395 */       if (this.action == 162)
/* 1396 */         this.action = 193; 
/* 1397 */       this.tilex = Tiles.decodeTileX(this.target);
/* 1398 */       this.tiley = Tiles.decodeTileY(this.target);
/* 1399 */       boolean onSurface = (Tiles.decodeLayer(this.target) == 0);
/* 1400 */       VolaTile _tile = null;
/* 1401 */       _tile = Zones.getTileOrNull(this.tilex, this.tiley, onSurface);
/* 1402 */       if (_tile != null)
/*      */       {
/* 1404 */         this.fence = _tile.getFence(this.target);
/*      */       }
/* 1406 */       if (this.fence == null)
/* 1407 */         throw new NoSuchWallException("No fence with id " + this.target); 
/* 1408 */       if (this.action >= 8000 || this.action < 2000) {
/*      */         
/* 1410 */         if (!aPerformer.isWithinTileDistanceTo(this.tilex, this.tiley, this.heightOffset, 1) && 
/* 1411 */           !getActionEntry().isIgnoresRange())
/* 1412 */           throw new FailedException("You are too far away to do that."); 
/* 1413 */         Village village = this.fence.getVillage();
/* 1414 */         if (village != null && !Actions.isActionManage(this.action))
/*      */         {
/* 1416 */           if (!village.isActionAllowed(this.action, aPerformer))
/*      */           {
/* 1418 */             if (!this.fence.isOnPvPServer() || Servers.isThisAChaosServer())
/* 1419 */               throw new FailedException("That would be very bad for your karma and is disallowed on this server."); 
/* 1420 */             failCheckEnemy(aPerformer, village);
/*      */           }
/*      */         
/*      */         }
/*      */       } 
/* 1425 */     } else if (this.targetType == 23) {
/*      */       
/* 1427 */       if (this.action == -1)
/* 1428 */         this.action = 1; 
/* 1429 */       if (this.action == 162)
/* 1430 */         this.action = 193; 
/* 1431 */       if (this.action >= 8000 || this.action < 2000 || this.action == 508 || this.action == 509 || this.action == 507)
/*      */       {
/*      */         
/* 1434 */         if (!aPerformer.isWithinTileDistanceTo((int)(this.target >> 32L) & 0xFFFF, (int)(this.target >> 16L) & 0xFFFF, this.heightOffset, 1))
/*      */         {
/*      */           
/* 1437 */           if ((this.action > 2000 && this.action < 8000) || !getActionEntry().isIgnoresRange())
/* 1438 */             throw new FailedException("You are too far away to do that."); 
/*      */         }
/*      */       }
/* 1441 */       this.tilex = Tiles.decodeTileX(this.target);
/* 1442 */       this.tiley = Tiles.decodeTileY(this.target);
/*      */     }
/* 1444 */     else if (this.targetType == 28) {
/*      */       
/* 1446 */       if (this.action == -1)
/* 1447 */         this.action = 1; 
/* 1448 */       if (this.action == 162)
/* 1449 */         this.action = 193; 
/* 1450 */       if (this.action >= 8000 || this.action < 2000)
/*      */       {
/* 1452 */         if (!aPerformer.isWithinTileDistanceTo((int)(this.target >> 32L) & 0xFFFF, (int)(this.target >> 16L) & 0xFFFF, this.heightOffset, 1))
/*      */         {
/*      */           
/* 1455 */           throw new FailedException("You are too far away to do that.");
/*      */         }
/*      */       }
/* 1458 */       this.tilex = Tiles.decodeTileX(this.target);
/* 1459 */       this.tiley = Tiles.decodeTileY(this.target);
/*      */     }
/* 1461 */     else if (!checkValidTileTarget(aPerformer)) {
/*      */ 
/*      */ 
/*      */       
/* 1465 */       if (this.targetType == 14) {
/*      */         
/* 1467 */         if (this.action == -1) {
/* 1468 */           this.action = 1;
/*      */         }
/*      */       }
/* 1471 */       else if (this.targetType == 25) {
/*      */         
/* 1473 */         if (this.action == -1) {
/* 1474 */           this.action = 587;
/*      */         }
/* 1476 */       } else if (this.action >= 2000 && this.action < 8000) {
/*      */ 
/*      */         
/* 1479 */         if (!aPerformer.isWithinDistanceTo(aPosX, aPosY, aPosZ, Emotes.emoteEntrys[this.action - 2000].getRange())) {
/* 1480 */           throw new FailedException("You are too far away to do that.");
/*      */         }
/* 1482 */       } else if (this.action < 2000) {
/*      */         
/* 1484 */         if (this.action == -1)
/* 1485 */           this.action = 1; 
/* 1486 */         if (!getActionEntry().isIgnoresRange() && 
/* 1487 */           !aPerformer.isWithinDistanceTo(aPosX, aPosY, aPosZ, Actions.actionEntrys[this.action].getRange())) {
/* 1488 */           throw new FailedException("You are too far away to do that.");
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
/*      */   public Action(Creature aPerformer, long aSubj, long[] _targets, short act, float aPosX, float aPosY, float aPosZ, float aRot) throws NoSuchItemException, NoSuchCreatureException, NoSuchPlayerException, NoSuchBehaviourException, FailedException {
/* 1516 */     this.performer = aPerformer;
/* 1517 */     this.subject = aSubj;
/*      */     
/* 1519 */     this.targets = _targets;
/* 1520 */     this.numbTargets = this.targets.length;
/* 1521 */     this.target = _targets[0];
/*      */     
/* 1523 */     this.action = act;
/* 1524 */     this.posX = aPosX;
/* 1525 */     this.posY = aPosY;
/* 1526 */     this.posZ = aPosZ;
/* 1527 */     this.rot = aRot;
/* 1528 */     this.targetType = WurmId.getType(this.target);
/*      */     
/* 1530 */     this.behaviour = getBehaviour(this.target, aPerformer.isOnSurface());
/* 1531 */     this.onSurface = getIsOnSurface(this.target, aPerformer.isOnSurface());
/* 1532 */     this.targetType = WurmId.getType(this.target);
/*      */     
/* 1534 */     if (this.action == -1)
/* 1535 */       this.action = 1; 
/* 1536 */     if (isFatigue()) {
/*      */       
/* 1538 */       if (this.performer.getVehicle() != -10L && !Actions.isActionAllowedOnVehicle(this.action))
/*      */       {
/* 1540 */         if (Actions.isActionAllowedOnBoat(this.action)) {
/*      */ 
/*      */           
/*      */           try {
/* 1544 */             Item vehicle = Items.getItem(this.performer.getVehicle());
/* 1545 */             if (!vehicle.isBoat())
/* 1546 */               throw new FailedException("You need to be on solid ground to do that."); 
/* 1547 */             if (vehicle.getPosZ() > 0.0F) {
/* 1548 */               throw new FailedException("The boat must be on water to do that from the boat.");
/*      */             }
/* 1550 */           } catch (NoSuchItemException e) {
/*      */             
/* 1552 */             throw new FailedException("You need to be on solid ground to do that.");
/*      */           } 
/*      */         } else {
/*      */           
/* 1556 */           throw new FailedException("You need to be on solid ground to do that.");
/*      */         }  } 
/* 1558 */       if (aPerformer.getFatigueLeft() <= 0)
/* 1559 */         throw new FailedException("You are too mentally exhausted to do that now."); 
/*      */     } 
/* 1561 */     for (int x = 0; x < this.targets.length; x++) {
/*      */       
/* 1563 */       Item targ = Items.getItem(this.targets[x]);
/* 1564 */       this.tilex = (int)targ.getPosX() >> 2;
/* 1565 */       this.tiley = (int)targ.getPosY() >> 2;
/* 1566 */       if (targ.isTraded() || targ.isBanked())
/*      */       {
/* 1568 */         throw new FailedException("You cannot use " + targ.getName() + " while trading it.");
/*      */       }
/* 1570 */       if (targ.mailed)
/*      */       {
/* 1572 */         throw new FailedException("You cannot use " + targ.getName() + " right now.");
/*      */       }
/*      */       
/*      */       try {
/* 1576 */         long towner = targ.getOwner();
/* 1577 */         if (this.action == 7 && targ.isBusy()) {
/* 1578 */           throw new FailedException("You are using one of those items.");
/*      */         }
/*      */         
/* 1581 */         if (towner != aPerformer.getWurmId())
/*      */         {
/* 1583 */           throw new FailedException("You need to carry that item to use it.");
/*      */         }
/* 1585 */         if (this.action == 7 && (targ
/* 1586 */           .getTemplateId() == 26 || targ.getTemplateId() == 298)) {
/*      */           
/* 1588 */           if (!MethodsItems.mayDropDirt(aPerformer)) {
/* 1589 */             throw new FailedException("You are not allowed to drop dirt there.");
/*      */           }
/* 1591 */         } else if (targ.isTraded()) {
/*      */           
/* 1593 */           throw new FailedException("You may not tamper with items you are trading.");
/*      */         } 
/*      */         
/* 1596 */         if (towner == this.performer.getWurmId())
/* 1597 */           this.personalAction = true; 
/* 1598 */         if (this.action == 162) {
/* 1599 */           this.personalAction = true;
/*      */         }
/* 1601 */       } catch (NotOwnedException nso) {
/*      */         
/* 1603 */         if (targ.getZoneId() < 0)
/*      */         {
/* 1605 */           logger.log(Level.WARNING, aPerformer
/* 1606 */               .getName() + " interacting with a " + targ.getName() + "(id=" + targ.getWurmId() + ") not in the world action=" + this.action);
/*      */         }
/*      */         
/* 1609 */         float iposX = targ.getPosX();
/* 1610 */         float iposY = targ.getPosY();
/* 1611 */         boolean ok = (aPerformer.isOnSurface() == targ.isOnSurface());
/* 1612 */         if (!ok) {
/*      */           
/* 1614 */           VolaTile lTile = Zones.getOrCreateTile((int)iposX >> 2, (int)iposY >> 2, targ.isOnSurface());
/* 1615 */           if (lTile.isTransition)
/*      */           {
/* 1617 */             if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile((int)iposX >> 2, (int)iposY >> 2)))) {
/* 1618 */               ok = false;
/*      */             } else {
/* 1620 */               ok = true;
/*      */             }  } 
/*      */         } 
/* 1623 */         if (!ok) {
/*      */           
/* 1625 */           VolaTile lTile = Zones.getOrCreateTile(aPerformer.getTileX(), aPerformer.getTileY(), aPerformer
/* 1626 */               .isOnSurface());
/* 1627 */           if (lTile.isTransition)
/*      */           {
/* 1629 */             if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(aPerformer.getTileX(), aPerformer
/* 1630 */                     .getTileY())))) {
/* 1631 */               ok = false;
/*      */             } else {
/* 1633 */               ok = true;
/*      */             }  } 
/*      */         } 
/* 1636 */         if (!ok)
/*      */         {
/* 1638 */           throw new NoSuchItemException("You are too far away from " + targ.getNameWithGenus() + ".");
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void failCheckEnemy(Creature aPerformer, Village village) throws FailedException {
/* 1646 */     if (!village.isEnemy(aPerformer)) {
/*      */       
/* 1648 */       if (aPerformer.isLegal())
/* 1649 */         throw new FailedException("That would be illegal here. You can check the settlement token for the local laws."); 
/* 1650 */       if (Actions.actionEntrys[this.action].isEnemyAllowedWhenNoGuards() && (village.getGuards()).length > 0)
/* 1651 */         throw new FailedException("A guard has noted you and stops you with a warning."); 
/* 1652 */       if (Actions.actionEntrys[this.action].isEnemyNeverAllowed()) {
/* 1653 */         throw new FailedException("That action makes no sense here.");
/*      */       }
/*      */     } else {
/*      */       
/* 1657 */       if (Actions.actionEntrys[this.action].isEnemyAllowedWhenNoGuards() && (village.getGuards()).length > 0)
/* 1658 */         throw new FailedException("A guard has noted you and stops you with a warning."); 
/* 1659 */       if (Actions.actionEntrys[this.action].isEnemyNeverAllowed()) {
/* 1660 */         throw new FailedException("That action makes no sense here.");
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
/*      */   public final boolean checkValidTileTarget(Creature aPerformer) throws FailedException {
/* 1673 */     if (this.targetType == 3) {
/*      */       
/* 1675 */       if (this.action == -1)
/* 1676 */         this.action = 1; 
/* 1677 */       MeshIO mesh = Server.surfaceMesh;
/* 1678 */       this.tilex = Tiles.decodeTileX(this.target);
/* 1679 */       this.tiley = Tiles.decodeTileY(this.target);
/* 1680 */       this.tile = mesh.getTile(this.tilex, this.tiley);
/* 1681 */       this.tilez = (int)(Tiles.decodeHeight(this.tile) / 10.0F);
/* 1682 */       if (this.action >= 8000 || this.action < 2000) {
/*      */         
/* 1684 */         if (aPerformer.getPower() < 5 && 
/* 1685 */           !aPerformer.isWithinTileDistanceTo(this.tilex, this.tiley, this.tilez, Actions.actionEntrys[this.action].getRange() / 4))
/*      */         {
/* 1687 */           if (this.action > 8000 || !getActionEntry().isIgnoresRange())
/*      */           {
/* 1689 */             throw new FailedException("You are too far away to do that.");
/*      */           }
/*      */         }
/* 1692 */         Village village = null;
/* 1693 */         int encodedTile = Server.surfaceMesh.getTile(this.tilex, this.tiley);
/*      */ 
/*      */ 
/*      */         
/* 1697 */         if (getActionEntry().isCornerAction()) {
/*      */           
/* 1699 */           int digTilex = (int)this.performer.getStatus().getPositionX() + 2 >> 2;
/* 1700 */           int digTiley = (int)this.performer.getStatus().getPositionY() + 2 >> 2;
/* 1701 */           encodedTile = Server.surfaceMesh.getTile(digTilex, digTiley);
/* 1702 */           village = Zones.getVillage(digTilex, digTiley, aPerformer.isOnSurface());
/* 1703 */           if (village == null) {
/*      */ 
/*      */             
/* 1706 */             digTilex = (int)this.performer.getStatus().getPositionX() - 2 >> 2;
/* 1707 */             village = Zones.getVillage(digTilex, digTiley, aPerformer.isOnSurface());
/*      */           } 
/* 1709 */           if (village == null) {
/*      */ 
/*      */             
/* 1712 */             digTiley = (int)this.performer.getStatus().getPositionY() - 2 >> 2;
/* 1713 */             village = Zones.getVillage(digTilex, digTiley, aPerformer.isOnSurface());
/*      */           } 
/* 1715 */           if (village == null)
/*      */           {
/*      */             
/* 1718 */             digTilex = (int)this.performer.getStatus().getPositionX() + 2 >> 2;
/* 1719 */             village = Zones.getVillage(digTilex, digTiley, aPerformer.isOnSurface());
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1724 */           village = Zones.getVillage(this.tilex, this.tiley, aPerformer.isOnSurface());
/*      */         } 
/*      */         
/* 1727 */         short checkAction = this.action;
/* 1728 */         if (checkAction == 186) {
/*      */           
/*      */           try {
/*      */             
/* 1732 */             Item item = Items.getItem(this.subject);
/* 1733 */             if (item.getTemplateId() == 266)
/*      */             {
/* 1735 */               checkAction = 660;
/*      */             }
/*      */           }
/* 1738 */           catch (NoSuchItemException noSuchItemException) {}
/*      */         }
/*      */ 
/*      */         
/* 1742 */         if (village != null)
/*      */         {
/* 1744 */           if (!Actions.isActionManage(this.action) && 
/* 1745 */             !village.isActionAllowed(checkAction, aPerformer, true, encodedTile, 0)) {
/*      */             
/* 1747 */             if (!Zones.isOnPvPServer(this.tilex, this.tiley)) {
/*      */               
/* 1749 */               if (this.behaviour.getType() == 7)
/* 1750 */                 throw new FailedException("This action is not allowed here, because the tree is on a player owned deed that has disallowed it."); 
/* 1751 */               throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it.");
/*      */             } 
/* 1753 */             failCheckEnemy(aPerformer, village);
/*      */           } 
/*      */         }
/*      */       } 
/* 1757 */       return true;
/*      */     } 
/* 1759 */     if (this.targetType == 12) {
/*      */       
/* 1761 */       if (this.action == -1) {
/* 1762 */         this.action = 1;
/*      */       }
/* 1764 */       if (this.action == 532)
/* 1765 */         this.action = 865; 
/* 1766 */       if (this.action == 150)
/* 1767 */         this.action = 533; 
/* 1768 */       this.tilex = Tiles.decodeTileX(this.target);
/* 1769 */       this.tiley = Tiles.decodeTileY(this.target);
/* 1770 */       Tiles.TileBorderDirection dir = Tiles.decodeDirection(this.target);
/* 1771 */       this.heightOffset = Tiles.decodeHeightOffset(this.target);
/* 1772 */       if (MethodsStructure.doesTileBorderContainWallOrFence(this.tilex, this.tiley, this.heightOffset, dir, aPerformer.isOnSurface(), true))
/*      */       {
/* 1774 */         throw new FailedException("There is a fence or wall there. Action not allowed.");
/*      */       }
/* 1776 */       if (this.action >= 8000 || this.action < 2000) {
/*      */         
/* 1778 */         if (!aPerformer.isWithinTileDistanceTo(this.tilex, this.tiley, this.heightOffset, 1) && 
/* 1779 */           !getActionEntry().isIgnoresRange()) {
/* 1780 */           throw new FailedException("You are too far away to do that.");
/*      */         }
/* 1782 */         VolaTile vtile = null;
/* 1783 */         Structure structure = MethodsStructure.getStructureOrNullAtTileBorder(this.tilex, this.tiley, dir, this.performer.isOnSurface(), vtile);
/* 1784 */         if (structure == null || !structure.isTypeHouse()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1790 */           Village village = Zones.getVillage(this.tilex, this.tiley, true);
/* 1791 */           if (village != null && !Actions.isActionManage(this.action))
/*      */           {
/* 1793 */             if (!village.isActionAllowed(this.action, aPerformer)) {
/*      */               
/* 1795 */               if (!Zones.isOnPvPServer(this.tilex, this.tiley))
/* 1796 */                 throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it."); 
/* 1797 */               failCheckEnemy(aPerformer, village);
/*      */             } 
/*      */           }
/* 1800 */           if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*      */             
/* 1802 */             village = Zones.getVillage(this.tilex - 1, this.tiley, true);
/* 1803 */             if (village != null && !Actions.isActionManage(this.action))
/*      */             {
/* 1805 */               if (!village.isActionAllowed(this.action, aPerformer))
/*      */               {
/* 1807 */                 if (!Zones.isOnPvPServer(this.tilex - 1, this.tiley))
/* 1808 */                   throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it."); 
/* 1809 */                 failCheckEnemy(aPerformer, village);
/*      */               }
/*      */             
/*      */             }
/* 1813 */           } else if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*      */             
/* 1815 */             village = Zones.getVillage(this.tilex, this.tiley - 1, true);
/* 1816 */             if (village != null && !Actions.isActionManage(this.action))
/*      */             {
/* 1818 */               if (!village.isActionAllowed(this.action, aPerformer)) {
/*      */                 
/* 1820 */                 if (!Zones.isOnPvPServer(this.tilex, this.tiley - 1))
/* 1821 */                   throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it."); 
/* 1822 */                 failCheckEnemy(aPerformer, village);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1828 */       return true;
/*      */     } 
/* 1830 */     if (this.targetType == 17) {
/*      */       
/* 1832 */       if (this.action == -1)
/* 1833 */         this.action = 1; 
/* 1834 */       this.tilex = Tiles.decodeTileX(this.target);
/* 1835 */       this.tiley = Tiles.decodeTileY(this.target);
/* 1836 */       int dir = CaveTile.decodeCaveTileDir(getTarget());
/* 1837 */       MeshIO mesh = Server.caveMesh;
/* 1838 */       this.tile = mesh.getTile(this.tilex, this.tiley);
/* 1839 */       this.heightOffset = (int)(Tiles.decodeHeight(this.tile) / 10.0F);
/* 1840 */       if (this.action >= 8000 || this.action < 2000)
/*      */       {
/* 1842 */         if (!getActionEntry().isIgnoresRange() && 
/* 1843 */           !aPerformer.isWithinTileDistanceTo(this.tilex, this.tiley, this.heightOffset, Actions.actionEntrys[this.action]
/* 1844 */             .getRange() / 4))
/* 1845 */           throw new FailedException("You are too far away to do that."); 
/*      */       }
/* 1847 */       Village village = Zones.getVillage(this.tilex, this.tiley, false);
/* 1848 */       if (village != null && !Actions.isActionManage(this.action)) {
/*      */         
/* 1850 */         if (!village.isActionAllowed(this.action, aPerformer, false, this.tile, dir)) {
/*      */           
/* 1852 */           if (!Zones.isOnPvPServer(this.tilex, this.tiley))
/* 1853 */             throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it."); 
/* 1854 */           failCheckEnemy(aPerformer, village);
/*      */         } 
/*      */         
/* 1857 */         byte type = Tiles.decodeType(this.tile);
/* 1858 */         if (this.action == 145 && dir != 1 && (this.tile == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || 
/* 1859 */           Tiles.isReinforcedFloor(type) || Tiles.isRoadType(type)))
/*      */         {
/* 1861 */           if (!village.isActionAllowed((short)229, aPerformer, true, this.tile, dir)) {
/*      */             
/* 1863 */             if (!Zones.isOnPvPServer(this.tilex, this.tiley))
/* 1864 */               throw new FailedException("This action is not allowed here, because the tile is on a player owned deed that has disallowed it."); 
/* 1865 */             failCheckEnemy(aPerformer, village);
/*      */           } 
/*      */         }
/*      */       } 
/* 1869 */       return true;
/*      */     } 
/* 1871 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isActionGuest(int actnum) {
/* 1876 */     return (isActionEmote(actnum) || actnum == 1 || actnum == 17 || actnum == 77 || actnum == 71);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isActionEmote(int actnum) {
/* 1882 */     return (actnum >= 2000 && actnum < 8000);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isActionRecipe(int actnum) {
/* 1887 */     return (actnum >= 8000 && actnum < 10000);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isActionAttack(int actnum) {
/* 1892 */     return (actnum == 114);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isStanceChange(short actnum) {
/* 1897 */     return (actnum < 8000 && Actions.actionEntrys[actnum].isStanceChange());
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getTargetType() {
/* 1902 */     return this.targetType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOffensive() {
/* 1912 */     return this.isOffensive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpell() {
/* 1922 */     return this.isSpell;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOpportunity() {
/* 1927 */     return getActionEntry().isOpportunity();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isVulnerable() {
/* 1932 */     if (isManualInvulnerable())
/* 1933 */       return false; 
/* 1934 */     return getActionEntry().isVulnerable();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isActionShoot(int actnum) {
/* 1939 */     return ((actnum >= 124 && actnum <= 131) || actnum == 342);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildHouseWallAction() {
/* 1944 */     return (isBuildWallAction() || isBuildDoorAction() || 
/* 1945 */       isBuildDoubleDoorAction() || isBuildWindowAction() || 
/* 1946 */       isBuildArchedWallAction() || isBuildPortcullisAction() || 
/* 1947 */       isBuildBarredWall() || isBuildBalcony() || 
/* 1948 */       isBuildJetty() || isBuildOriel() || 
/* 1949 */       isBuildCanopyDoor() || isBuildScaffolding());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildWallAction() {
/* 1954 */     return (this.action == 612 || this.action == 617 || this.action == 622 || this.action == 648 || this.action == 772 || this.action == 784 || this.action == 796 || this.action == 808 || this.action == 820);
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
/*      */   public final boolean isBuildDoorAction() {
/* 1967 */     return (this.action == 614 || this.action == 619 || this.action == 624 || this.action == 651 || this.action == 775 || this.action == 787 || this.action == 799 || this.action == 811 || this.action == 823);
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
/*      */   public final boolean isBuildDoubleDoorAction() {
/* 1980 */     return (this.action == 615 || this.action == 620 || this.action == 625 || this.action == 652 || this.action == 776 || this.action == 788 || this.action == 800 || this.action == 812 || this.action == 824);
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
/*      */   public final boolean isBuildPortcullisAction() {
/* 1993 */     return (this.action == 655 || this.action == 657 || this.action == 658 || this.action == 778 || this.action == 790 || this.action == 802 || this.action == 814 || this.action == 826);
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
/*      */   public final boolean isBuildBarredWall() {
/* 2005 */     return (this.action == 656 || this.action == 779 || this.action == 791 || this.action == 803 || this.action == 815 || this.action == 827);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBuildBalcony() {
/* 2015 */     return (this.action == 676);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildJetty() {
/* 2020 */     return (this.action == 677);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildOriel() {
/* 2025 */     return (this.action == 678 || this.action == 681 || this.action == 780 || this.action == 792 || this.action == 804 || this.action == 816 || this.action == 828);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBuildCanopyDoor() {
/* 2036 */     return (this.action == 679);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildScaffolding() {
/* 2041 */     return (this.action == 869);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildNormalArchAction() {
/* 2046 */     return (this.action == 616 || this.action == 621 || this.action == 626 || this.action == 653 || this.action == 777 || this.action == 789 || this.action == 801 || this.action == 813 || this.action == 825);
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
/*      */   public final boolean isBuildLeftArchAction() {
/* 2059 */     return (this.action == 760 || this.action == 763 || this.action == 766 || this.action == 769 || this.action == 781 || this.action == 793 || this.action == 805 || this.action == 817 || this.action == 829);
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
/*      */   public final boolean isBuildRightArchAction() {
/* 2072 */     return (this.action == 761 || this.action == 764 || this.action == 767 || this.action == 770 || this.action == 782 || this.action == 794 || this.action == 806 || this.action == 818 || this.action == 830);
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
/*      */   public final boolean isBuildTArchAction() {
/* 2085 */     return (this.action == 762 || this.action == 765 || this.action == 768 || this.action == 771 || this.action == 783 || this.action == 795 || this.action == 807 || this.action == 819 || this.action == 831);
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
/*      */   public final boolean isBuildArchedWallAction() {
/* 2098 */     return (isBuildNormalArchAction() || isBuildLeftArchAction() || isBuildRightArchAction() || isBuildTArchAction());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBuildWindowAction() {
/* 2103 */     return (this.action == 613 || this.action == 618 || this.action == 623 || this.action == 650 || this.action == 649 || this.action == 680 || this.action == 773 || this.action == 785 || this.action == 797 || this.action == 809 || this.action == 821 || this.action == 774 || this.action == 786 || this.action == 798 || this.action == 810 || this.action == 822);
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
/*      */   
/*      */   public final ActionEntry getActionEntry() {
/* 2123 */     return Actions.actionEntrys[getNumber()];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Creature getPerformer() {
/* 2132 */     return this.performer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestroyedItem(@Nullable Item item) {
/* 2141 */     this.destroyedItem = item;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getDestroyedItem() {
/* 2150 */     return this.destroyedItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreature(@Nullable Creature creature) {
/* 2159 */     this.tempCreature = creature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Creature getCreature() {
/* 2169 */     return this.tempCreature;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPriority() {
/* 2174 */     if (this.action >= 2000 && this.action < 8000)
/* 2175 */       return Emotes.emoteEntrys[getNumber() - 2000].getPriority(); 
/* 2176 */     return Actions.actionEntrys[getNumber()].getPriority();
/*      */   }
/*      */ 
/*      */   
/*      */   public int currentSecond() {
/* 2181 */     return this.currentSecond;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSecond() {
/* 2186 */     if (this.currentSecond == -1)
/* 2187 */       return this.lastSecond; 
/* 2188 */     return this.currentSecond;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayPlaySound() {
/* 2194 */     return (currentSecond() % 5 == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void maybeUpdateSecond() {
/* 2200 */     int current = (int)this.counter;
/*      */ 
/*      */     
/* 2203 */     this.justTickedSecond = false;
/* 2204 */     if (current == this.currentSecond) {
/*      */       
/* 2206 */       this.lastSecond = this.currentSecond;
/* 2207 */       this.currentSecond = -1;
/*      */     }
/* 2209 */     else if (this.lastSecond != current) {
/*      */ 
/*      */       
/* 2212 */       this.currentSecond = current;
/* 2213 */       this.justTickedSecond = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInterruptedAtMove() {
/* 2221 */     if (this.action >= 2000 && this.action < 8000)
/* 2222 */       return true; 
/* 2223 */     if (this.isSpell || this.action >= 8000 || Actions.actionEntrys[getNumber()].isNoMove())
/*      */     {
/* 2225 */       return true;
/*      */     }
/* 2227 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean getIsOnSurface(long id, boolean surfaced) {
/* 2232 */     boolean isOnSurface = surfaced;
/* 2233 */     int targetType = WurmId.getType(id);
/* 2234 */     if (targetType == 3) {
/* 2235 */       isOnSurface = true;
/* 2236 */     } else if (targetType == 17) {
/* 2237 */       isOnSurface = false;
/* 2238 */     }  return isOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Behaviour getBehaviour(long id, boolean surfaced) throws NoSuchBehaviourException, NoSuchPlayerException, NoSuchCreatureException, NoSuchItemException {
/* 2244 */     Behaviour behaviour = null;
/* 2245 */     int targetType = WurmId.getType(id);
/* 2246 */     if (targetType == 30) {
/*      */       
/* 2248 */       behaviour = Behaviours.getInstance().getBehaviour((short)53);
/*      */     }
/* 2250 */     else if (targetType == 27) {
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
/* 2265 */       behaviour = Behaviours.getInstance().getBehaviour((short)54);
/*      */     
/*      */     }
/* 2268 */     else if (targetType == 3) {
/*      */       
/* 2270 */       int x = Tiles.decodeTileX(id);
/* 2271 */       int y = Tiles.decodeTileY(id);
/*      */       
/* 2273 */       if (x >= 0 && x < 1 << Constants.meshSize && y >= 0 && y < 1 << Constants.meshSize) {
/*      */         
/* 2275 */         MeshIO mesh = Server.surfaceMesh;
/* 2276 */         int tile = mesh.getTile(x, y);
/* 2277 */         byte type = Tiles.decodeType(tile);
/* 2278 */         Tiles.Tile theTile = Tiles.getTile(type);
/*      */         
/* 2280 */         if (theTile.isTree()) {
/* 2281 */           behaviour = Behaviours.getInstance().getBehaviour((short)7);
/* 2282 */         } else if (theTile.isBush()) {
/* 2283 */           behaviour = Behaviours.getInstance().getBehaviour((short)7);
/* 2284 */         } else if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/*      */           
/* 2286 */           behaviour = Behaviours.getInstance().getBehaviour((short)8);
/* 2287 */         } else if (type == Tiles.Tile.TILE_KELP.id) {
/* 2288 */           behaviour = Behaviours.getInstance().getBehaviour((short)8);
/* 2289 */         } else if (type == Tiles.Tile.TILE_REED.id) {
/* 2290 */           behaviour = Behaviours.getInstance().getBehaviour((short)8);
/* 2291 */         } else if (type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_CLIFF.id) {
/* 2292 */           behaviour = Behaviours.getInstance().getBehaviour((short)9);
/* 2293 */         } else if (type == Tiles.Tile.TILE_DIRT.id) {
/* 2294 */           behaviour = Behaviours.getInstance().getBehaviour((short)15);
/* 2295 */         } else if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) {
/* 2296 */           behaviour = Behaviours.getInstance().getBehaviour((short)17);
/*      */         } else {
/* 2298 */           behaviour = Behaviours.getInstance().getBehaviour((short)5);
/*      */         } 
/*      */       } else {
/* 2301 */         throw new NoSuchBehaviourException("Out of the map [" + x + "," + y + "]. Allowing no actions.");
/*      */       } 
/* 2303 */     } else if (targetType == 1 || targetType == 0) {
/*      */       
/* 2305 */       Creature targetCreature = Server.getInstance().getCreature(id);
/* 2306 */       behaviour = targetCreature.getBehaviour();
/*      */     }
/* 2308 */     else if (targetType == 2 || targetType == 6 || targetType == 19 || targetType == 20) {
/*      */ 
/*      */       
/* 2311 */       Item targetItem = Items.getItem(id);
/* 2312 */       behaviour = targetItem.getBehaviour();
/*      */     }
/* 2314 */     else if (targetType == 5) {
/*      */       
/* 2316 */       behaviour = Behaviours.getInstance().getBehaviour((short)20);
/*      */     }
/* 2318 */     else if (targetType == 7) {
/*      */       
/* 2320 */       behaviour = Behaviours.getInstance().getBehaviour((short)22);
/*      */     }
/* 2322 */     else if (targetType == 8 || targetType == 32) {
/*      */       
/* 2324 */       behaviour = Behaviours.getInstance().getBehaviour((short)27);
/*      */     }
/* 2326 */     else if (targetType == 17) {
/*      */       
/* 2328 */       int x = (int)(id >> 32L) & 0xFFFF;
/* 2329 */       int y = (int)(id >> 16L) & 0xFFFF;
/*      */       
/* 2331 */       if (x >= 0 && x < 1 << Constants.meshSize && y >= 0 && y < 1 << Constants.meshSize) {
/*      */         
/* 2333 */         MeshIO mesh = Server.caveMesh;
/* 2334 */         int tile = mesh.getTile(x, y);
/*      */         
/* 2336 */         byte type = Tiles.decodeType(tile);
/* 2337 */         if (Tiles.isSolidCave(type)) {
/* 2338 */           behaviour = Behaviours.getInstance().getBehaviour((short)38);
/* 2339 */         } else if (type == Tiles.Tile.TILE_CAVE.id || Tiles.isRoadType(type) || Tiles.isReinforcedFloor(type) || type == Tiles.Tile.TILE_CAVE_EXIT.id || type == Tiles.Tile.TILE_ROCK.id) {
/*      */           
/* 2341 */           behaviour = Behaviours.getInstance().getBehaviour((short)39);
/*      */         } else {
/* 2343 */           behaviour = Behaviours.getInstance().getBehaviour((short)5);
/*      */         } 
/*      */       } else {
/* 2346 */         throw new NoSuchBehaviourException("Out of the map. Allowing no actions.");
/*      */       } 
/* 2348 */     } else if (targetType == 12) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2353 */       int x = Tiles.decodeTileX(id);
/* 2354 */       int y = Tiles.decodeTileY(id);
/*      */       
/* 2356 */       Tiles.TileBorderDirection dir = Tiles.decodeDirection(id);
/*      */       
/* 2358 */       boolean onSurface = (Tiles.decodeLayer(id) == 0);
/* 2359 */       if (MethodsStructure.getStructureOrNullAtTileBorder(x, y, dir, onSurface) != null)
/*      */       {
/* 2361 */         behaviour = Behaviours.getInstance().getBehaviour((short)6);
/*      */       }
/*      */       else
/*      */       {
/* 2365 */         behaviour = Behaviours.getInstance().getBehaviour((short)32);
/*      */       }
/*      */     
/* 2368 */     } else if (targetType == 14) {
/*      */       
/* 2370 */       behaviour = Behaviours.getInstance().getBehaviour((short)36);
/*      */     }
/* 2372 */     else if (targetType == 18) {
/*      */       
/* 2374 */       behaviour = Behaviours.getInstance().getBehaviour((short)42);
/*      */     }
/* 2376 */     else if (targetType == 22) {
/*      */       
/* 2378 */       behaviour = Behaviours.getInstance().getBehaviour((short)43);
/*      */     }
/* 2380 */     else if (targetType == 23) {
/*      */       
/* 2382 */       behaviour = Behaviours.getInstance().getBehaviour((short)45);
/*      */     }
/* 2384 */     else if (targetType == 25) {
/*      */       
/* 2386 */       behaviour = Behaviours.getInstance().getBehaviour((short)50);
/*      */     }
/* 2388 */     else if (targetType == 24) {
/*      */       
/* 2390 */       behaviour = Behaviours.getInstance().getBehaviour((short)0);
/*      */     }
/* 2392 */     else if (targetType == 28) {
/*      */       
/* 2394 */       Tiles.TileBorderDirection dir = Tiles.decodeDirection(id);
/* 2395 */       if (dir == Tiles.TileBorderDirection.CORNER) {
/* 2396 */         behaviour = Behaviours.getInstance().getBehaviour((short)60);
/*      */       } else {
/* 2398 */         behaviour = Behaviours.getInstance().getBehaviour((short)51);
/*      */       } 
/* 2400 */     }  return behaviour;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPosX() {
/* 2409 */     return this.posX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPosY() {
/* 2418 */     return this.posY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPosZ() {
/* 2427 */     return this.posZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosX(float newPosX) {
/* 2436 */     this.posX = newPosX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosY(float newPosY) {
/* 2445 */     this.posY = newPosY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRot() {
/* 2454 */     return this.rot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTileX() {
/* 2464 */     return this.tilex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTileY() {
/* 2474 */     return this.tiley;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/* 2479 */     return this.onSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setActionString(String aActionString) {
/* 2488 */     this.actionString = aActionString;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTimeLeft() {
/* 2493 */     return this.tenthOfSecondsLeftOnAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFailSecond() {
/* 2502 */     return this.failSecond;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailSecond(float second) {
/* 2511 */     this.failSecond = second;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPower() {
/* 2520 */     return this.power;
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
/*      */   public void setPower(float pow) {
/* 2534 */     this.power = pow;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTimeLeft(int _tenthOfSecondsLeftOnAction) {
/* 2539 */     this.tenthOfSecondsLeftOnAction = _tenthOfSecondsLeftOnAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getActionString() {
/* 2548 */     if (this.actionString.equals(""))
/*      */       
/*      */       try {
/* 2551 */         if (this.action < 2000) {
/* 2552 */           return Actions.getVerbForAction(this.action);
/*      */         }
/* 2554 */       } catch (Exception ex) {
/*      */         
/* 2556 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       }  
/* 2558 */     return this.actionString;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean checkLegalMode(Creature performer) {
/* 2563 */     if (!performer.isOnPvPServer()) {
/*      */       
/* 2565 */       performer.getCommunicator().sendNormalServerMessage("That would be very bad for your karma and is disallowed on this server.", (byte)3);
/* 2566 */       return true;
/*      */     } 
/* 2568 */     Village v = performer.getCurrentVillage();
/* 2569 */     boolean hasGuards = false;
/* 2570 */     if (v != null)
/* 2571 */       hasGuards = (v.guards.size() > 0); 
/* 2572 */     if (performer.isLegal() && hasGuards) {
/*      */       
/* 2574 */       performer.getCommunicator().sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.", (byte)3);
/* 2575 */       return true;
/*      */     } 
/* 2577 */     if (performer.getDeity() != null && performer.faithful && !performer.getDeity().isLibila()) {
/*      */       
/* 2579 */       performer.getCommunicator().sendNormalServerMessage("Your deity would never allow stealing.", (byte)3);
/* 2580 */       return true;
/*      */     } 
/* 2582 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public short getNumber() {
/* 2587 */     return getNumber(this.action);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final short getNumber(short _action) {
/* 2592 */     if (_action >= 20000)
/* 2593 */       return 116; 
/* 2594 */     if (_action >= 10000)
/* 2595 */       return 148; 
/* 2596 */     if (_action >= 8000) {
/* 2597 */       return 148;
/*      */     }
/* 2599 */     return _action;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTarget() {
/* 2608 */     return this.target;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean justTickedSecond() {
/* 2613 */     return this.justTickedSecond;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setTarget(long aTarget) {
/* 2624 */     this.target = aTarget;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Behaviour getBehaviour() {
/* 2633 */     return this.behaviour;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean isQuick() {
/* 2638 */     return isQuick(this.action);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isQuick(int actNum) {
/* 2643 */     if (actNum < 8000) {
/*      */       
/* 2645 */       if (actNum >= 2000) {
/* 2646 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2651 */         return Actions.actionEntrys[actNum].isQuickSkillLess();
/*      */       }
/* 2653 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2655 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + actNum, ai);
/* 2656 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2661 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isEquipAction() {
/* 2666 */     switch (getNumber()) {
/*      */       
/*      */       case 582:
/*      */       case 583:
/*      */       case 584:
/* 2671 */         return true;
/*      */     } 
/* 2673 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isStackable(int actNum) {
/* 2679 */     if (actNum < 8000) {
/*      */       
/* 2681 */       if (actNum >= 2000) {
/* 2682 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2687 */         return Actions.actionEntrys[actNum].isStackable();
/*      */       }
/* 2689 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2691 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + actNum, ai);
/* 2692 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2697 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isStackableFight(int actNum) {
/* 2702 */     if (actNum < 8000) {
/*      */       
/* 2704 */       if (actNum >= 2000) {
/* 2705 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2710 */         return Actions.actionEntrys[actNum].isStackableFight();
/*      */       }
/* 2712 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2714 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + actNum, ai);
/* 2715 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2720 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBlockingNumber(int actNum) {
/* 2725 */     if (actNum < 8000) {
/*      */       
/* 2727 */       if (actNum >= 2000) {
/* 2728 */         return 5;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2733 */         return Actions.actionEntrys[actNum].getBlockType();
/*      */       }
/* 2735 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2737 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + actNum, ai);
/* 2738 */         return 4;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2743 */     return 4;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean needsFood() {
/* 2748 */     if (this.action < 8000) {
/*      */       
/* 2750 */       if (this.action >= 2000) {
/* 2751 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2756 */         return Actions.actionEntrys[this.action].needsFood();
/*      */       }
/* 2758 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2760 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + this.action, ai);
/* 2761 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2766 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isFatigue() {
/* 2771 */     if (this.action < 8000) {
/*      */       
/* 2773 */       if (this.action >= 2000) {
/* 2774 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2779 */         return Actions.actionEntrys[this.action].isFatigue();
/*      */       }
/* 2781 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2783 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + this.action, ai);
/* 2784 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2789 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isStanceChange() {
/* 2794 */     return Actions.actionEntrys[getNumber(this.action)].isStanceChange();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDefend() {
/* 2799 */     return Actions.actionEntrys[getNumber()].isDefend();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSameBridge(int actNum) {
/* 2804 */     if (actNum < 8000) {
/*      */       
/* 2806 */       if (actNum >= 2000) {
/* 2807 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 2812 */         return Actions.actionEntrys[actNum].isSameBridgeOnly();
/*      */       }
/* 2814 */       catch (ArrayIndexOutOfBoundsException ai) {
/*      */         
/* 2816 */         logger.log(Level.WARNING, "Arrayindexexception for action: " + actNum, ai);
/* 2817 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2822 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmote() {
/* 2827 */     return isActionEmote(this.action);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCounterAsFloat() {
/* 2832 */     return this.counter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetCounter() {
/* 2840 */     this.counter = 2.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean poll() {
/*      */     try {
/* 2848 */       if (isFatigue())
/*      */       {
/* 2850 */         if (this.performer.getFatigueLeft() <= 0) {
/*      */           
/* 2852 */           this.performer.getCommunicator().sendNormalServerMessage("You are too mentally exhausted to do that now.");
/* 2853 */           return true;
/*      */         } 
/*      */       }
/* 2856 */       if (this.isSpell) {
/*      */         
/* 2858 */         if (this.spell == null || (this.spell.religious && !this.performer.isPriest() && this.performer.getPower() == 0)) {
/*      */           
/* 2860 */           this.performer.getCommunicator().sendNormalServerMessage("You fail to find any power to channel.");
/* 2861 */           return true;
/*      */         } 
/* 2863 */         if (this.spell.offensive) {
/* 2864 */           this.performer.removeIllusion();
/*      */         }
/*      */       } 
/* 2867 */       this.performer.getStatus().setNormalRegen(false);
/* 2868 */       Item item = null;
/* 2869 */       if (this.subject != -1L && !isEmote() && 
/* 2870 */         getActionEntry().getUseActiveItem() != 2) {
/*      */         
/*      */         try {
/*      */           
/* 2874 */           item = Items.getItem(this.subject);
/* 2875 */           if (item.deleted)
/*      */           {
/* 2877 */             if (!isQuick())
/* 2878 */               this.performer.getCurrentTile().sendStopUseItem(this.performer); 
/* 2879 */             if (getNumber() == 329)
/* 2880 */               this.performer.getCommunicator().sendStopUseBinoculars(); 
/* 2881 */             if (getNumber() == 748 || getNumber() == 759)
/* 2882 */               this.performer.getCommunicator().sendHideLinks(); 
/* 2883 */             return true;
/*      */           }
/*      */         
/* 2886 */         } catch (NoSuchItemException nex) {
/*      */           
/* 2888 */           if (!isQuick())
/* 2889 */             this.performer.getCurrentTile().sendStopUseItem(this.performer); 
/* 2890 */           if (getNumber() == 329)
/* 2891 */             this.performer.getCommunicator().sendStopUseBinoculars(); 
/* 2892 */           if (getNumber() == 748 || getNumber() == 759) {
/* 2893 */             this.performer.getCommunicator().sendHideLinks();
/*      */           }
/*      */           
/* 2896 */           return true;
/*      */         } 
/*      */       }
/*      */       
/* 2900 */       if (item == null && !isEmote() && getActionEntry().getUseActiveItem() == 1)
/*      */       {
/* 2902 */         if (this.performer.isPlayer() == true) {
/*      */ 
/*      */           
/* 2905 */           this.performer.getCommunicator().sendNormalServerMessage("'" + getActionEntry().getActionString() + "' requires an active item.");
/* 2906 */           return true;
/*      */         } 
/*      */       }
/*      */       
/* 2910 */       if (this.counter == 0.0F || this.lastPolledAction == 0L) {
/*      */         
/* 2912 */         this.counter = 1.0F;
/* 2913 */         this.lastPolledAction = System.currentTimeMillis();
/*      */       }
/*      */       else {
/*      */         
/* 2917 */         float change = (float)(System.currentTimeMillis() - this.lastPolledAction) / 1000.0F;
/* 2918 */         if (change == 0.0F)
/* 2919 */           return false; 
/* 2920 */         this.counter += Math.max(change, 1.0E-4F);
/* 2921 */         this.lastPolledAction = System.currentTimeMillis();
/*      */       } 
/*      */       
/* 2924 */       maybeUpdateSecond();
/*      */       
/* 2926 */       if (this.done) {
/*      */ 
/*      */ 
/*      */         
/* 2930 */         boolean toReturn = true;
/* 2931 */         if (this.action != 142 && this.action != 148 && this.action != 192 && this.action != 350 && (this.action < 496 || this.action > 502))
/*      */         {
/*      */           
/* 2934 */           if ((this.action >= 2000 && this.action < 8000) || !getActionEntry().isFatigue() || this.counter > 1.0F) {
/*      */             
/* 2936 */             toReturn = MissionTriggers.activateTriggers(this.performer, item, this.action, this.target, this.triggerCounter);
/* 2937 */             if (!toReturn && this.targetType == 5) {
/*      */               
/* 2939 */               VolaTile t = Zones.getTileOrNull(this.tilex, this.tiley, this.performer.isOnSurface());
/* 2940 */               if (t != null)
/*      */               {
/* 2942 */                 if (t.getStructure() != null) {
/* 2943 */                   toReturn = MissionTriggers.activateTriggers(this.performer, item, this.action, t.getStructure()
/* 2944 */                       .getWurmId(), this.triggerCounter);
/*      */                 }
/*      */               }
/*      */             } 
/* 2948 */             if (this.justTickedSecond || this.triggerCounter == 0)
/* 2949 */               this.triggerCounter++; 
/*      */           } 
/*      */         }
/* 2952 */         return toReturn;
/*      */       } 
/* 2954 */       if (!this.personalAction && !this.performer.isWithinDistanceTo(this.posX, this.posY, this.posZ, 12.0F, 2.0F)) {
/*      */         
/* 2956 */         this.performer.getCommunicator().sendNormalServerMessage(stop(true));
/* 2957 */         this.done = true;
/* 2958 */         if (this.action == 925 || this.action == 926) {
/* 2959 */           this.performer.getCommunicator().sendCancelPlacingItem();
/* 2960 */         } else if (this.action == 160) {
/*      */ 
/*      */           
/* 2963 */           MethodsFishing.playerOutOfRange(getPerformer(), this);
/*      */         }
/*      */       
/* 2966 */       } else if (this.action == 157) {
/*      */         
/* 2968 */         this.performer.setFightingStyle((byte)1);
/* 2969 */         this.done = true;
/*      */       }
/* 2971 */       else if (this.action == 159) {
/*      */         
/* 2973 */         this.performer.setFightingStyle((byte)2);
/* 2974 */         this.done = true;
/*      */       }
/* 2976 */       else if (this.action == 158) {
/*      */         
/* 2978 */         this.performer.setFightingStyle((byte)0);
/* 2979 */         this.done = true;
/*      */       }
/* 2981 */       else if (this.action == 84) {
/*      */         
/* 2983 */         this.performer.setSpam(!this.performer.spamMode());
/* 2984 */         this.done = true;
/*      */       }
/* 2986 */       else if (this.action == 35) {
/*      */         
/* 2988 */         this.performer.setLegal(!this.performer.isLegal());
/* 2989 */         this.done = true;
/*      */       }
/* 2991 */       else if (this.action == 36) {
/*      */         
/* 2993 */         this.performer.setFaithMode(!this.performer.faithful);
/* 2994 */         this.done = true;
/*      */       }
/* 2996 */       else if (this.action == 341) {
/*      */         
/* 2998 */         this.performer.setTarget(-10L, true);
/* 2999 */         this.done = true;
/*      */       }
/* 3001 */       else if (this.action == 38) {
/*      */         
/* 3003 */         this.done = true;
/*      */         
/*      */         try {
/* 3006 */           this.performer.setClimbing(true);
/* 3007 */           this.performer.getStatus().sendStateString();
/*      */         }
/* 3009 */         catch (IOException iox) {
/*      */           
/* 3011 */           logger.log(Level.WARNING, "Failed to set climbing for " + this.performer.getName() + ": ", iox);
/*      */         }
/*      */       
/* 3014 */       } else if (this.action == 39) {
/*      */         
/* 3016 */         this.done = true;
/*      */         
/*      */         try {
/* 3019 */           this.performer.setClimbing(false);
/* 3020 */           this.performer.getStatus().sendStateString();
/*      */         }
/* 3022 */         catch (IOException iox) {
/*      */           
/* 3024 */           logger.log(Level.WARNING, "Failed to stop climbing for " + this.performer.getName() + ": ", iox);
/*      */         }
/*      */       
/* 3027 */       } else if (this.action == 467) {
/*      */         
/* 3029 */         this.done = true;
/* 3030 */         if (this.performer.getPower() >= 2 || this.performer.mayMute())
/*      */         {
/* 3032 */           GmInterface gmi = new GmInterface(this.performer, this.performer.getWurmId());
/* 3033 */           gmi.sendQuestion();
/*      */         }
/*      */       
/* 3036 */       } else if (this.action == 534) {
/*      */         
/* 3038 */         this.done = true;
/* 3039 */         if (this.performer.getPower() >= 2)
/*      */         {
/* 3041 */           GmTool gmt = new GmTool(this.performer, this.target);
/* 3042 */           gmt.sendQuestion();
/*      */         }
/*      */       
/* 3045 */       } else if (this.action == 582 || this.action == 583 || this.action == 584) {
/*      */         
/* 3047 */         AutoEquipMethods.autoEquip(this.target, this.performer, this.action, this);
/* 3048 */         this.done = true;
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
/*      */       }
/* 3072 */       else if (this.action == 585) {
/*      */         
/* 3074 */         this.done = true;
/*      */         try {
/* 3076 */           Item equip = Items.getItem(this.target);
/* 3077 */           if (equip.isBodyPartAttached()) {
/* 3078 */             for (Item equipment : this.performer.getBody().getContainersAndWornItems()) {
/*      */               
/*      */               try
/*      */               {
/* 3082 */                 if (equipment.isArmour() && equipment.getParent().getWurmId() != this.performer.getBody().getId())
/*      */                 {
/* 3084 */                   AutoEquipMethods.unequip(equipment, this.performer);
/*      */                 }
/*      */               }
/* 3087 */               catch (NoSuchItemException e)
/*      */               {
/* 3089 */                 logger.warning(String.format("Creature %s somehow had armour %s equipped without a parent. [Wurm ID %s]", new Object[] { this.performer
/* 3090 */                         .getName(), equipment.getName(), Long.valueOf(equipment.getWurmId()) }));
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 3096 */             AutoEquipMethods.unequip(this.target, this.performer);
/*      */           } 
/* 3098 */         } catch (NoSuchItemException e) {
/* 3099 */           logger.warning(String.format("Creature %s tried unequipping an invalid item. [Wurm ID %s]", new Object[] { this.performer
/* 3100 */                   .getName(), Long.valueOf(this.target) }));
/*      */         }
/*      */       
/*      */       }
/* 3104 */       else if (this.action == 693) {
/*      */         
/* 3106 */         this.done = true;
/* 3107 */         this.performer.setFlag(42, true);
/* 3108 */         this.performer.achievement(141);
/* 3109 */         this.performer.getCommunicator().sendNormalServerMessage("You decide to skip the tutorial.");
/*      */       }
/* 3111 */       else if (this.action == 723) {
/*      */         
/* 3113 */         this.done = AutoEquipMethods.timedDragEquip(this.performer, this.subject, this.target, this, this.counter);
/*      */       }
/* 3115 */       else if (this.action == 724) {
/*      */         
/* 3117 */         this.done = AutoEquipMethods.timedAutoEquip(this.performer, this.target, (short)724, this, this.counter);
/*      */       }
/*      */       else {
/*      */         
/* 3121 */         if (this.counter == 1.0F && this.performer.isPlayer())
/*      */         {
/* 3123 */           if (this.performer.getPower() == 0 && this.performer.isPriest() && this.performer.getDeity() != null && 
/* 3124 */             !this.performer.isChampion())
/*      */           {
/* 3126 */             if (getNumber() < 2000 || getNumber() > 8000) {
/*      */               
/* 3128 */               ActionEntry entry = Actions.actionEntrys[getNumber()];
/* 3129 */               if (entry.isNonReligion() || (
/* 3130 */                 (this.performer.getDeity()).number == 4 && entry.isNonLibila()) || (
/* 3131 */                 (this.performer.getDeity()).number != 4 && entry.isNonWhiteReligion()))
/*      */               {
/* 3133 */                 if (!entry.isAllowed(this.performer))
/*      */                 {
/* 3135 */                   if (this.performer.faithful) {
/*      */                     
/* 3137 */                     this.performer.getCommunicator().sendNormalServerMessage(
/* 3138 */                         (this.performer.getDeity()).name + " would not approve of that.");
/* 3139 */                     this.done = true;
/*      */                   } else {
/*      */                     
/* 3142 */                     this.performer.modifyFaith(-0.1F);
/*      */                   }  } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         }
/* 3148 */         if (currentSecond() == 2 && this.performer.isPlayer())
/*      */         {
/* 3150 */           if (isOpportunity()) {
/*      */             
/* 3152 */             this.performer.getCurrentTile().checkOpportunityAttacks(this.performer);
/* 3153 */             if (this.performer.isDead())
/* 3154 */               this.done = true; 
/*      */           } 
/*      */         }
/* 3157 */         if (!this.done) {
/*      */           
/* 3159 */           if (isFatigue())
/*      */           {
/* 3161 */             if (justTickedSecond()) {
/*      */               
/* 3163 */               this.performer.decreaseFatigue();
/* 3164 */               if (!this.isOffensive)
/* 3165 */                 this.performer.checkWorkMusic(); 
/*      */             } 
/*      */           }
/* 3168 */           if (currentSecond() == 1 && this.performer.isStealth())
/*      */           {
/* 3170 */             if (!isQuick() || isEmote()) {
/* 3171 */               this.performer.setStealth(false);
/*      */             }
/*      */           }
/* 3174 */           if (item != null)
/*      */           {
/* 3176 */             if (currentSecond() == 1) {
/*      */               
/* 3178 */               if (!isQuick())
/*      */               {
/* 3180 */                 if (item.getOwnerId() == this.performer.getWurmId() || (
/* 3181 */                   getNumber() >= 0 && 
/* 3182 */                   getNumber() < Actions.actionEntrys.length && 
/* 3183 */                   !getActionEntry().isUseItemOnGroundAction()))
/*      */                 {
/* 3185 */                   if (this.action == 182)
/*      */                   {
/* 3187 */                     if (this.target != -10L && this.targetType == 2) {
/*      */                       
/*      */                       try {
/* 3190 */                         Item targ = Items.getItem(this.target);
/* 3191 */                         this.performer.getCurrentTile().sendUseItem(this.performer, targ.getModelName(), targ
/* 3192 */                             .getRarity(), WurmColor.getColorRed(targ.getColor()), WurmColor.getColorGreen(targ.getColor()), 
/* 3193 */                             WurmColor.getColorBlue(targ.getColor()), WurmColor.getColorRed(targ.getColor2()), WurmColor.getColorGreen(targ.getColor2()), 
/* 3194 */                             WurmColor.getColorBlue(targ.getColor2()));
/*      */                       }
/* 3196 */                       catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */                     
/*      */                     }
/*      */ 
/*      */                   
/*      */                   }
/* 3203 */                   else if (getActionEntry().getUseActiveItem() != 2)
/*      */                   {
/*      */                     
/*      */                     try {
/* 3207 */                       Item targ = Items.getItem(this.target);
/* 3208 */                       boolean sendTarget = false;
/* 3209 */                       if (!targ.isUseOnGroundOnly())
/*      */                       {
/* 3211 */                         if (targ.isTool() && !item.isTool()) {
/* 3212 */                           sendTarget = true;
/*      */                         }
/*      */                       }
/* 3215 */                       if (!sendTarget)
/*      */                       {
/* 3217 */                         this.performer.getCurrentTile().sendUseItem(this.performer, item.getModelName(), item
/* 3218 */                             .getRarity(), WurmColor.getColorRed(item.getColor()), WurmColor.getColorGreen(item.getColor()), 
/* 3219 */                             WurmColor.getColorBlue(item.getColor()), WurmColor.getColorRed(item.getColor2()), WurmColor.getColorGreen(item.getColor2()), 
/* 3220 */                             WurmColor.getColorBlue(item.getColor2()));
/*      */                       }
/*      */                       else
/*      */                       {
/* 3224 */                         this.performer.getCurrentTile().sendUseItem(this.performer, targ.getModelName(), targ
/* 3225 */                             .getRarity(), WurmColor.getColorRed(targ.getColor()), WurmColor.getColorGreen(targ.getColor()), 
/* 3226 */                             WurmColor.getColorBlue(targ.getColor()), WurmColor.getColorRed(targ.getColor2()), WurmColor.getColorGreen(targ.getColor2()), 
/* 3227 */                             WurmColor.getColorBlue(targ.getColor2()));
/*      */                       }
/*      */                     
/* 3230 */                     } catch (NoSuchItemException nsi) {
/*      */                       
/* 3232 */                       this.performer.getCurrentTile().sendUseItem(this.performer, item.getModelName(), item
/* 3233 */                           .getRarity(), WurmColor.getColorRed(item.getColor()), WurmColor.getColorGreen(item.getColor()), 
/* 3234 */                           WurmColor.getColorBlue(item.getColor()), WurmColor.getColorRed(item.getColor2()), WurmColor.getColorGreen(item.getColor2()), 
/* 3235 */                           WurmColor.getColorBlue(item.getColor2()));
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */               }
/* 3242 */             } else if (currentSecond() == 2) {
/*      */               
/* 3244 */               if (getNumber() == 329)
/* 3245 */                 this.performer.getCommunicator().sendUseBinoculars(); 
/*      */             } 
/*      */           }
/* 3248 */           if (this.targetType == 3) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3255 */             if (this.counter == 1.0F || justTickedSecond()) {
/*      */               
/* 3257 */               BlockingResult result = Blocking.getBlockerBetween(this.performer, this.target, true, 
/* 3258 */                   getBlockingNumber(this.action), this.performer.getBridgeId(), -10L);
/* 3259 */               if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                 
/* 3261 */                 this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3262 */                     .getFirstBlocker().getName() + " is in the way.");
/* 3263 */                 this.done = true;
/*      */               }
/* 3265 */               else if (!this.performer.isOnSurface()) {
/*      */ 
/*      */ 
/*      */                 
/* 3269 */                 this.performer.getCommunicator().sendNormalServerMessage("The cave entrance is in the way.");
/*      */                 
/* 3271 */                 this.done = true;
/*      */               } 
/*      */             } 
/* 3274 */             if (!this.done) {
/*      */               
/* 3276 */               this.tile = Server.surfaceMesh.getTile(this.tilex, this.tiley);
/*      */               
/* 3278 */               if (getNumber() >= 2000) {
/*      */                 
/* 3280 */                 Emotes.emoteAt(this.action, this.performer, this.tilex, this.tiley, this.heightOffset, this.tile);
/* 3281 */                 this.done = true;
/*      */               }
/* 3283 */               else if (Actions.actionEntrys[getNumber()].isQuickSkillLess() && this.action != 189) {
/*      */                 
/* 3285 */                 this.behaviour.action(this, this.performer, this.tilex, this.tiley, this.onSurface, this.tile, this.action, this.counter);
/* 3286 */                 this.done = true;
/*      */               }
/* 3288 */               else if (item != null) {
/*      */                 
/* 3290 */                 item.setBusy(true);
/* 3291 */                 this.done = this.behaviour.action(this, this.performer, item, this.tilex, this.tiley, this.onSurface, this.heightOffset, this.tile, this.action, this.counter);
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/* 3296 */                 this.done = this.behaviour.action(this, this.performer, this.tilex, this.tiley, this.onSurface, this.tile, this.action, this.counter);
/*      */               } 
/* 3298 */               if (this.action >= 8000 || this.action < 2000)
/*      */               {
/* 3300 */                 if (this.counter == 1.0F || currentSecond() % 5 == 0) {
/*      */                   
/* 3302 */                   int encodedTile = this.tile;
/*      */                   
/* 3304 */                   if (getActionEntry().isCornerAction()) {
/*      */                     
/* 3306 */                     int digTilex = (int)this.performer.getStatus().getPositionX() + 2 >> 2;
/* 3307 */                     int digTiley = (int)this.performer.getStatus().getPositionY() + 2 >> 2;
/* 3308 */                     encodedTile = Server.surfaceMesh.getTile(digTilex, digTiley);
/*      */                   } 
/* 3310 */                   Village village = Zones.getVillage(this.tilex, this.tiley, true);
/* 3311 */                   if (village != null && !Actions.isActionManage(this.action))
/*      */                   {
/*      */ 
/*      */                     
/* 3315 */                     if (this.action != 174 || !village.isEnemy(this.performer)) {
/*      */ 
/*      */                       
/* 3318 */                       short checkAction = this.action;
/* 3319 */                       if (checkAction == 186)
/*      */                       {
/* 3321 */                         if (item != null && item.getTemplateId() == 266)
/* 3322 */                           checkAction = 660; 
/*      */                       }
/* 3324 */                       if (!Actions.isActionManage(this.action) && 
/* 3325 */                         !Methods.isActionAllowed(this.performer, checkAction, true, this.tilex, this.tiley, encodedTile, 0) && 
/* 3326 */                         warnedPlayer(village)) {
/* 3327 */                         this.done = true;
/*      */                       }
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */               }
/*      */             } 
/* 3334 */           } else if (this.targetType == 1 || this.targetType == 0) {
/*      */ 
/*      */             
/*      */             try {
/* 3338 */               Creature tcret = Server.getInstance().getCreature(this.target);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3345 */               if (tcret != this.performer && (this.counter == 1.0F || justTickedSecond())) {
/*      */ 
/*      */                 
/* 3348 */                 BlockingResult result = isActionShoot(this.action) ? Blocking.getRangedBlockerBetween(this.performer, tcret) : Blocking.getBlockerBetween(this.performer, tcret, 
/* 3349 */                     getBlockingNumber(this.action));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3355 */                 if (result != null)
/*      */                 {
/* 3357 */                   if (isActionShoot(this.action)) {
/*      */                     
/* 3359 */                     for (Blocker b : result.getBlockerArray()) {
/*      */                       
/* 3361 */                       if (b.getBlockPercent(this.performer) >= 100.0F)
/*      */                       {
/* 3363 */                         if (this.performer.getPower() > 0) {
/* 3364 */                           this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3365 */                               .getFirstBlocker().getName() + " at fl " + result
/* 3366 */                               .getFirstBlocker().getFloorLevel() + "," + result
/* 3367 */                               .getFirstBlocker().getId() + " is in the way.");
/*      */                         } else {
/*      */                           
/* 3370 */                           this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3371 */                               .getFirstBlocker().getName() + " is in the way.");
/* 3372 */                         }  this.done = true;
/*      */                       }
/*      */                     
/*      */                     } 
/* 3376 */                   } else if (result.getTotalCover() >= 100.0F) {
/*      */                     
/* 3378 */                     this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3379 */                         .getFirstBlocker().getName() + " is in the way.");
/* 3380 */                     this.done = true;
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */               
/* 3385 */               if (!this.done && tcret.getTemplateId() != 119)
/*      */               {
/*      */ 
/*      */                 
/* 3389 */                 if (getNumber() >= 2000) {
/*      */                   
/* 3391 */                   Emotes.emoteAt(this.action, this.performer, tcret);
/* 3392 */                   this.done = true;
/*      */                 }
/*      */                 else {
/*      */                   
/* 3396 */                   boolean ok = (this.performer.isOnSurface() == tcret.isOnSurface());
/* 3397 */                   if (!ok) {
/*      */                     
/* 3399 */                     ok = (this.action == 185 || this.action == 1);
/* 3400 */                     if (!ok) {
/*      */ 
/*      */ 
/*      */                       
/* 3404 */                       ok = true;
/* 3405 */                       boolean transition = false;
/* 3406 */                       if ((tcret.getCurrentTile()).isTransition) {
/*      */                         
/* 3408 */                         transition = true;
/* 3409 */                         if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tcret.getTileX(), tcret
/* 3410 */                                 .getTileY()))))
/*      */                         {
/* 3412 */                           ok = false;
/*      */                         }
/*      */                       } 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 3419 */                       if (ok && (this.performer.getCurrentTile()).isTransition) {
/*      */                         
/* 3421 */                         transition = true;
/* 3422 */                         if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(this.performer
/* 3423 */                                 .getTileX(), this.performer.getTileY()))))
/*      */                         {
/* 3425 */                           ok = false;
/*      */                         }
/*      */                       } 
/*      */ 
/*      */ 
/*      */                       
/* 3431 */                       if (!transition)
/* 3432 */                         ok = false; 
/*      */                     } 
/*      */                   } 
/* 3435 */                   if (ok) {
/*      */                     
/* 3437 */                     if (!getActionEntry().isIgnoresRange() && 
/* 3438 */                       Creature.rangeTo(this.performer, tcret) > Actions.actionEntrys[getNumber()]
/* 3439 */                       .getRange()) {
/*      */                       
/* 3441 */                       this.performer.getCommunicator().sendNormalServerMessage("You are now too far away to " + Actions.actionEntrys[this.action]
/* 3442 */                           .getActionString().toLowerCase() + " " + tcret
/* 3443 */                           .getNameWithGenus() + ".");
/* 3444 */                       this.performer.setOpponent(null);
/* 3445 */                       this.done = true;
/*      */                     }
/* 3447 */                     else if (this.isOffensive) {
/*      */                       
/* 3449 */                       if (!tcret.isDuelOrSpar(this.performer))
/*      */                       {
/* 3451 */                         if ((!tcret.isOnPvPServer() || !this.performer.isOnPvPServer()) && tcret
/* 3452 */                           .isPlayer() && this.performer.isPlayer()) {
/*      */                           
/* 3454 */                           if (tcret.getCitizenVillage() == null || 
/* 3455 */                             !tcret.getCitizenVillage().isEnemy(this.performer))
/*      */                           {
/* 3457 */                             this.performer.getCommunicator().sendNormalServerMessage("That would be very bad for your karma and is disallowed on this server.");
/*      */                             
/* 3459 */                             this.done = true;
/*      */                           }
/*      */                         
/*      */                         } else {
/*      */                           
/* 3464 */                           this.tilex = tcret.getTileX();
/* 3465 */                           this.tiley = tcret.getTileY();
/* 3466 */                           Village village = Zones.getVillage(this.tilex, this.tiley, tcret.isOnSurface());
/* 3467 */                           if (village != null && !Actions.isActionManage(this.action))
/*      */                           {
/* 3469 */                             if (((isActionAttack(this.action) || isActionShoot(this.action)) && this.counter <= 1.0F) || (this.isSpell && 
/* 3470 */                               currentSecond() <= 1))
/*      */                             {
/* 3472 */                               if (this.performer.isFriendlyKingdom(tcret.getKingdomId()) && 
/* 3473 */                                 !this.performer.isFighting() && 
/* 3474 */                                 !village.mayAttack(this.performer, tcret))
/*      */                               {
/* 3476 */                                 if (!this.performer.isOnPvPServer() || !tcret.isOnPvPServer())
/*      */                                 {
/* 3478 */                                   this.performer.getCommunicator().sendNormalServerMessage("That would be very bad for your karma and is disallowed on this server.");
/*      */                                   
/* 3480 */                                   this.done = true;
/*      */                                 }
/* 3482 */                                 else if (!tcret.isInvulnerable())
/*      */                                 {
/* 3484 */                                   if (this.performer.mayAttack(tcret))
/*      */                                   {
/* 3486 */                                     if ((!isActionShoot(this.action) || !this.performer.isLegal()) && 
/* 3487 */                                       village.getReputation(tcret) > -30 && 
/* 3488 */                                       village.checkGuards(this, this.performer)) {
/* 3489 */                                       village.modifyReputations(this, this.performer);
/*      */                                     }
/*      */                                   }
/*      */                                 }
/*      */                               
/*      */                               }
/*      */                             }
/* 3496 */                             else if (currentSecond() % 5 == 0)
/*      */                             {
/* 3498 */                               if (village.checkGuards(this, this.performer)) {
/* 3499 */                                 village.resolveDispute(this.performer, tcret);
/*      */                               }
/*      */                             }
/*      */                           
/*      */                           }
/*      */                         }
/*      */                       
/*      */                       }
/* 3507 */                     } else if (currentSecond() % 5 == 0) {
/*      */                       
/* 3509 */                       if (!tcret.isDuelOrSpar(this.performer))
/*      */                       {
/* 3511 */                         if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley, 0, 0) && 
/* 3512 */                           warnedPlayer()) {
/* 3513 */                           this.done = true;
/*      */                         }
/*      */                       }
/*      */                     } 
/* 3517 */                     if (!this.done)
/*      */                     {
/* 3519 */                       if (Actions.actionEntrys[this.action].isQuickSkillLess()) {
/*      */                         
/* 3521 */                         this.behaviour.action(this, this.performer, tcret, this.action, this.counter);
/* 3522 */                         this.done = true;
/*      */                       }
/* 3524 */                       else if (this.action == 114) {
/*      */                         
/* 3526 */                         this.done = this.behaviour.action(this, this.performer, tcret, this.action, this.counter);
/*      */                       }
/* 3528 */                       else if (item != null) {
/*      */                         
/* 3530 */                         if (!Actions.actionEntrys[this.action].isIgnoresRange() && 
/* 3531 */                           Creature.rangeTo(this.performer, tcret) > Actions.actionEntrys[this.action]
/* 3532 */                           .getRange())
/*      */                         {
/* 3534 */                           this.performer.getCommunicator().sendNormalServerMessage("You are now too far away to " + Actions.actionEntrys[this.action]
/*      */                               
/* 3536 */                               .getActionString() + " " + tcret
/* 3537 */                               .getNameWithGenus() + ".");
/* 3538 */                           this.done = true;
/*      */                         }
/*      */                         else
/*      */                         {
/* 3542 */                           item.setBusy(true);
/* 3543 */                           this.done = this.behaviour.action(this, this.performer, item, tcret, this.action, this.counter);
/*      */                         }
/*      */                       
/*      */                       } else {
/*      */                         
/* 3548 */                         this.done = this.behaviour.action(this, this.performer, tcret, this.action, this.counter);
/*      */                       } 
/* 3550 */                       if (tcret.isDead())
/*      */                       {
/* 3552 */                         this.done = true;
/*      */                       }
/*      */                     }
/*      */                   
/* 3556 */                   } else if (!getActionEntry().isIgnoresRange()) {
/*      */                     
/* 3558 */                     this.performer.getCommunicator().sendSafeServerMessage("You are too far away from " + tcret
/* 3559 */                         .getNameWithGenus() + ".", (byte)3);
/* 3560 */                     this.done = true;
/*      */                   } 
/*      */                 } 
/*      */               }
/* 3564 */             } catch (NoSuchCreatureException nex) {
/*      */               
/* 3566 */               this.done = true;
/*      */             }
/* 3568 */             catch (NoSuchPlayerException nsp) {
/*      */               
/* 3570 */               this.done = true;
/*      */             }
/*      */           
/* 3573 */           } else if (this.numbTargets > 1) {
/*      */             
/*      */             try
/*      */             {
/* 3577 */               Item[] targetItems = new Item[this.numbTargets];
/* 3578 */               for (int x = 0; x < this.numbTargets; x++) {
/* 3579 */                 targetItems[x] = Items.getItem(this.targets[x]);
/*      */               }
/* 3581 */               this.done = this.behaviour.action(this, this.performer, targetItems, this.action, this.counter);
/*      */             }
/* 3583 */             catch (NoSuchItemException nsi)
/*      */             {
/*      */               
/* 3586 */               this.done = true;
/*      */             }
/*      */           
/* 3589 */           } else if (this.targetType == 2 || this.targetType == 6 || this.targetType == 19 || this.targetType == 20) {
/*      */ 
/*      */             
/*      */             try {
/*      */               
/* 3594 */               Item targetItem = Items.getItem(this.target);
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3599 */               if (targetItem.getOwnerId() != this.performer.getWurmId() && (this.counter == 1.0F || justTickedSecond())) {
/*      */ 
/*      */ 
/*      */                 
/* 3603 */                 BlockingResult result = isActionShoot(this.action) ? Blocking.getRangedBlockerBetween(this.performer, targetItem) : Blocking.getBlockerBetween(this.performer, targetItem, 
/* 3604 */                     getBlockingNumber(this.action));
/* 3605 */                 boolean blocked = false;
/* 3606 */                 if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                   
/* 3608 */                   blocked = true;
/* 3609 */                   if (result.getFirstBlocker() == result.getLastBlocker() && result.getFirstBlocker() != null)
/*      */                   {
/* 3611 */                     if (result.getFirstBlocker().isFence()) {
/*      */                       
/* 3613 */                       Fence f = (Fence)result.getFirstBlocker();
/* 3614 */                       if (f != null && f.getType() == StructureConstantsEnum.FENCE_SIEGEWALL)
/*      */                       {
/* 3616 */                         blocked = false;
/*      */                       }
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */                 
/* 3622 */                 if (blocked) {
/*      */                   
/* 3624 */                   this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3625 */                       .getFirstBlocker().getName() + " is in the way.");
/* 3626 */                   throw new NoSuchItemException("The " + result.getFirstBlocker().getName() + " is in the way.");
/*      */                 } 
/*      */               } 
/*      */               
/*      */               try {
/*      */                 long towner;
/*      */                 
/* 3633 */                 if (getNumber() < 0 || 
/* 3634 */                   getNumber() >= Actions.actionEntrys.length || 
/* 3635 */                   !getActionEntry().isUseItemOnGroundAction()) {
/* 3636 */                   towner = targetItem.getOwner();
/*      */                 } else {
/* 3638 */                   towner = -10L;
/* 3639 */                 }  if (towner != this.performer.getWurmId() && getNumber() >= 0 && 
/* 3640 */                   getNumber() < Actions.actionEntrys.length && 
/* 3641 */                   !getActionEntry().isUseItemOnGroundAction()) {
/*      */                   
/* 3643 */                   if (this.action != 1 && this.action != 87 && !isActionEmote(this.action) && this.action != 185)
/*      */                   {
/* 3645 */                     throw new NoSuchItemException("You are not the owner of that any longer.");
/*      */                   }
/* 3647 */                 } else if (this.action == 7 && (targetItem
/* 3648 */                   .getTemplateId() == 26 || targetItem.getTemplateId() == 298)) {
/*      */                   
/* 3650 */                   if (!MethodsItems.mayDropDirt(this.performer))
/* 3651 */                     throw new NoSuchItemException("You are not allowed to drop dirt there."); 
/*      */                 } else {
/* 3653 */                   if (targetItem.isTraded() && this.action != 1 && this.action != 87 && 
/* 3654 */                     !isActionEmote(this.action))
/*      */                   {
/* 3656 */                     throw new NoSuchItemException("You may not tamper with items you are trading.");
/*      */                   }
/* 3658 */                   if (targetItem.isUseOnGroundOnly() && !isQuick() && this.action != 100 && this.action != 176 && this.action != 925 && this.action != 926 && this.performer
/*      */                     
/* 3660 */                     .getPower() > 0 && this.action != 180 && this.action != 633)
/*      */                   {
/* 3662 */                     throw new NoSuchItemException("You may only use that item while it is on the ground.");
/*      */                   }
/*      */                 } 
/* 3665 */               } catch (NotOwnedException notOwnedException) {}
/*      */ 
/*      */ 
/*      */               
/* 3669 */               if (getNumber() >= 2000) {
/*      */                 
/* 3671 */                 Emotes.emoteAt(this.action, this.performer, targetItem);
/* 3672 */                 this.done = true;
/*      */               }
/* 3674 */               else if (Actions.actionEntrys[getNumber()].isQuickSkillLess() && this.action < 8000) {
/*      */                 
/* 3676 */                 if (item != null && (this.action == 117 || this.action == 28 || this.action == 102 || this.action == 3 || this.action == 76 || this.action == 93 || this.action == 54)) {
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 3681 */                   this.behaviour.action(this, this.performer, item, targetItem, this.action, this.counter);
/*      */                 }
/*      */                 else {
/*      */                   
/* 3685 */                   this.behaviour.action(this, this.performer, targetItem, this.action, this.counter);
/*      */                 } 
/* 3687 */                 this.done = true;
/*      */               }
/* 3689 */               else if (item == null) {
/*      */                 
/* 3691 */                 targetItem.setBusy(true);
/* 3692 */                 this.done = this.behaviour.action(this, this.performer, targetItem, this.action, this.counter);
/*      */               }
/*      */               else {
/*      */                 
/* 3696 */                 item.setBusy(true);
/* 3697 */                 targetItem.setBusy(true);
/* 3698 */                 this.done = this.behaviour.action(this, this.performer, item, targetItem, this.action, this.counter);
/*      */               } 
/* 3700 */               if (this.done)
/*      */               {
/* 3702 */                 if (targetItem != null) {
/* 3703 */                   targetItem.setBusy(false);
/*      */                 }
/*      */               }
/* 3706 */             } catch (NoSuchItemException nsi) {
/*      */ 
/*      */               
/* 3709 */               this.done = true;
/*      */             }
/*      */           
/* 3712 */           } else if (this.targetType == 5) {
/*      */             
/* 3714 */             if (this.counter == 1.0F || justTickedSecond()) {
/*      */               
/* 3716 */               BlockingResult result = Blocking.getBlockerBetween(this.performer, this.wall, 
/* 3717 */                   getBlockingNumber(this.action));
/* 3718 */               if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                 
/* 3720 */                 this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3721 */                     .getFirstBlocker().getName() + " is in the way.");
/* 3722 */                 this.done = true;
/*      */               } 
/*      */             } 
/* 3725 */             if (!this.done)
/*      */             {
/*      */ 
/*      */               
/* 3729 */               if (getNumber() >= 2000) {
/*      */                 
/* 3731 */                 Emotes.emoteAt(this.action, this.performer, this.wall);
/* 3732 */                 this.done = true;
/*      */               }
/* 3734 */               else if (item == null) {
/* 3735 */                 this.done = this.behaviour.action(this, this.performer, this.wall, this.action, this.counter);
/*      */               } else {
/*      */                 
/* 3738 */                 item.setBusy(true);
/* 3739 */                 this.done = this.behaviour.action(this, this.performer, item, this.wall, this.action, this.counter);
/*      */               } 
/*      */             }
/* 3742 */             if (this.action >= 8000 || this.action < 2000)
/*      */             {
/* 3744 */               if (currentSecond() % 5 == 0)
/*      */               {
/* 3746 */                 if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley, 0, 0) && 
/* 3747 */                   warnedPlayer()) {
/* 3748 */                   this.done = true;
/*      */                 }
/*      */               }
/*      */             }
/* 3752 */           } else if (this.targetType == 7) {
/*      */             
/* 3754 */             this.fence = Fence.getFence(this.target);
/* 3755 */             if (this.fence != null) {
/*      */               
/* 3757 */               if (this.counter == 1.0F || justTickedSecond()) {
/*      */                 
/* 3759 */                 BlockingResult result = Blocking.getBlockerBetween(this.performer, this.fence, 
/* 3760 */                     getBlockingNumber(this.action));
/* 3761 */                 if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                   
/* 3763 */                   this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3764 */                       .getFirstBlocker().getName() + " is in the way.");
/* 3765 */                   this.done = true;
/*      */                 } 
/*      */               } 
/* 3768 */               if (!this.done)
/*      */               {
/*      */ 
/*      */                 
/* 3772 */                 if (getNumber() >= 2000) {
/*      */                   
/* 3774 */                   Emotes.emoteAt(this.action, this.performer, this.fence);
/* 3775 */                   this.done = true;
/*      */                 }
/* 3777 */                 else if (item == null) {
/* 3778 */                   this.done = this.behaviour.action(this, this.performer, this.onSurface, this.fence, this.action, this.counter);
/*      */                 } else {
/*      */                   
/* 3781 */                   item.setBusy(true);
/* 3782 */                   this.done = this.behaviour.action(this, this.performer, item, this.onSurface, this.fence, this.action, this.counter);
/*      */                 } 
/*      */               }
/* 3785 */               if (this.action < 2000 || this.action >= 8000)
/*      */               {
/* 3787 */                 if (currentSecond() % 5 == 0)
/*      */                 {
/* 3789 */                   if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley, 0, 0) && 
/* 3790 */                     warnedPlayer()) {
/* 3791 */                     this.done = true;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             } else {
/* 3796 */               this.done = true;
/*      */             } 
/* 3798 */           }  if (this.targetType == 17) {
/*      */ 
/*      */ 
/*      */             
/* 3802 */             this.tile = Server.caveMesh.getTile(this.tilex, this.tiley);
/* 3803 */             int dir = CaveTile.decodeCaveTileDir(this.target);
/* 3804 */             if (this.counter == 1.0F || justTickedSecond()) {
/*      */               
/* 3806 */               int ceilingHeight = 0;
/* 3807 */               if (dir == 1) {
/*      */                 
/* 3809 */                 int meshtile = Server.caveMesh.getTile(this.tilex, this.tiley);
/* 3810 */                 ceilingHeight = Tiles.decodeData(meshtile) & 0xFF;
/*      */               } 
/*      */               
/* 3813 */               BlockingResult result = Blocking.getBlockerBetween(this.performer, this.target, false, 
/* 3814 */                   getBlockingNumber(this.action), this.performer.getBridgeId(), -10L, ceilingHeight);
/* 3815 */               if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                 
/* 3817 */                 this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3818 */                     .getFirstBlocker().getName() + " is in the way.");
/* 3819 */                 this.done = true;
/*      */               } 
/*      */             } 
/* 3822 */             if (!this.done)
/*      */             {
/*      */ 
/*      */               
/* 3826 */               if (getNumber() >= 2000) {
/*      */                 
/* 3828 */                 Emotes.emoteAt(this.action, this.performer, this.tilex, this.tiley, this.heightOffset, this.tile);
/* 3829 */                 this.done = true;
/*      */               }
/* 3831 */               else if (Actions.actionEntrys[getNumber()].isQuickSkillLess() && this.action != 189) {
/*      */                 
/* 3833 */                 this.behaviour.action(this, this.performer, this.tilex, this.tiley, this.onSurface, this.tile, dir, this.action, this.counter);
/* 3834 */                 this.done = true;
/*      */               }
/* 3836 */               else if (item != null) {
/*      */                 
/* 3838 */                 item.setBusy(true);
/* 3839 */                 if (this.performer == null) {
/* 3840 */                   logger.log(Level.WARNING, "performer is null");
/* 3841 */                 } else if (this.behaviour == null) {
/* 3842 */                   logger.log(Level.WARNING, "behaviour is null");
/* 3843 */                 }  this.done = this.behaviour.action(this, this.performer, item, this.tilex, this.tiley, this.onSurface, this.heightOffset, this.tile, dir, this.action, this.counter);
/*      */               }
/*      */               else {
/*      */                 
/* 3847 */                 this.done = this.behaviour.action(this, this.performer, this.tilex, this.tiley, this.onSurface, this.tile, dir, this.action, this.counter);
/* 3848 */               }  }  if (this.action >= 8000 || this.action < 2000)
/*      */             {
/* 3850 */               if (currentSecond() % 5 == 0)
/*      */               {
/* 3852 */                 if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley, this.tile, dir) && 
/* 3853 */                   warnedPlayer()) {
/* 3854 */                   this.done = true;
/*      */                 }
/*      */               }
/*      */             }
/* 3858 */           } else if (this.targetType == 12) {
/*      */             
/* 3860 */             this.onSurface = (Tiles.decodeLayer(this.target) == 0);
/* 3861 */             if (this.counter == 1.0F || justTickedSecond()) {
/*      */               
/* 3863 */               BlockingResult result = Blocking.getBlockerBetween(this.performer, this.target, this.onSurface, 
/* 3864 */                   getBlockingNumber(this.action), this.performer.getBridgeId(), -10L);
/* 3865 */               if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                 
/* 3867 */                 Blocker[] blockers = result.getBlockerArray();
/*      */ 
/*      */                 
/* 3870 */                 if (blockers.length != 1 || !blockers[0].isRoof()) {
/*      */                   
/* 3872 */                   this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3873 */                       .getFirstBlocker().getName() + " is in the way.");
/* 3874 */                   this.done = true;
/*      */                 } 
/*      */               } 
/*      */             } 
/* 3878 */             if (!this.done) {
/*      */               
/* 3880 */               Tiles.TileBorderDirection dir = Tiles.decodeDirection(this.target);
/* 3881 */               if (item != null) {
/*      */                 
/* 3883 */                 item.setBusy(true);
/* 3884 */                 if (this.performer == null) {
/* 3885 */                   logger.log(Level.WARNING, "performer is null");
/* 3886 */                 } else if (this.behaviour == null) {
/* 3887 */                   logger.log(Level.WARNING, "behaviour is null");
/* 3888 */                 }  this
/* 3889 */                   .done = this.behaviour.action(this, this.performer, item, this.tilex, this.tiley, this.onSurface, this.heightOffset, dir, this.target, this.action, this.counter);
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/* 3894 */                 this.done = this.behaviour.action(this, this.performer, this.tilex, this.tiley, this.onSurface, dir, this.target, this.action, this.counter);
/*      */               } 
/* 3896 */               if (this.action < 2000 || this.action >= 8000)
/*      */               {
/* 3898 */                 if (currentSecond() % 5 == 0) {
/*      */                   
/* 3900 */                   if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley, 0, 0) && 
/* 3901 */                     warnedPlayer())
/* 3902 */                     this.done = true; 
/* 3903 */                   if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*      */                     
/* 3905 */                     if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex - 1, this.tiley, 0, 0) && 
/* 3906 */                       warnedPlayer()) {
/* 3907 */                       this.done = true;
/*      */                     }
/* 3909 */                   } else if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*      */                     
/* 3911 */                     if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley - 1, 0, 0) && 
/* 3912 */                       warnedPlayer()) {
/* 3913 */                       this.done = true;
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             } 
/* 3919 */           } else if (this.targetType == 27) {
/*      */             
/* 3921 */             this.onSurface = (Tiles.decodeLayer(this.target) == 0);
/* 3922 */             if (this.counter == 1.0F || justTickedSecond()) {
/*      */               
/* 3924 */               BlockingResult result = Blocking.getBlockerBetween(this.performer, this.target, this.onSurface, 
/* 3925 */                   getBlockingNumber(this.action), this.performer.getBridgeId(), -10L);
/* 3926 */               if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                 
/* 3928 */                 this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3929 */                     .getFirstBlocker().getName() + " is in the way.");
/* 3930 */                 this.done = true;
/*      */               } 
/*      */             } 
/* 3933 */             if (!this.done) {
/*      */               
/* 3935 */               this.tilex = Tiles.decodeTileX(this.target);
/* 3936 */               this.tiley = Tiles.decodeTileY(this.target);
/* 3937 */               if (!this.personalAction && !this.performer.isWithinDistanceTo((this.tilex << 2), (this.tiley << 2), this.posZ, 4.0F, 0.0F)) {
/*      */                 
/* 3939 */                 this.performer.getCommunicator().sendNormalServerMessage("You are now too far away for " + 
/* 3940 */                     getActionString().toLowerCase() + ".");
/* 3941 */                 this.done = true;
/*      */               } 
/*      */             } 
/* 3944 */             if (!this.done) {
/*      */               
/* 3946 */               this.heightOffset = Tiles.decodeHeightOffset(this.target);
/* 3947 */               if (this.onSurface) {
/* 3948 */                 this.tile = Server.surfaceMesh.getTile(this.tilex, this.tiley);
/*      */               } else {
/* 3950 */                 this.tile = Server.caveMesh.getTile(this.tilex, this.tiley);
/* 3951 */               }  if (item != null) {
/*      */                 
/* 3953 */                 item.setBusy(true);
/* 3954 */                 if (this.performer == null) {
/* 3955 */                   logger.log(Level.WARNING, "performer is null");
/* 3956 */                 } else if (this.behaviour == null) {
/* 3957 */                   logger.log(Level.WARNING, "behaviour is null");
/* 3958 */                 }  this
/* 3959 */                   .done = this.behaviour.action(this, this.performer, item, this.tilex, this.tiley, this.onSurface, true, this.tile, this.heightOffset, this.action, this.counter);
/*      */               }
/*      */               else {
/*      */                 
/* 3963 */                 this.done = this.behaviour.action(this, this.performer, this.tilex, this.tiley, this.onSurface, true, this.tile, this.heightOffset, this.action, this.counter);
/*      */               } 
/* 3965 */               if (this.action < 2000 || this.action >= 8000)
/*      */               {
/* 3967 */                 if (currentSecond() % 5 == 0)
/*      */                 {
/* 3969 */                   if (!Methods.isActionAllowed(this.performer, this.action, true, this.tilex, this.tiley, 0, 0) && 
/* 3970 */                     warnedPlayer()) {
/* 3971 */                     this.done = true;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             } 
/* 3976 */           } else if (this.targetType == 8 || this.targetType == 32) {
/*      */ 
/*      */             
/*      */             try {
/* 3980 */               Wounds wounds = this.performer.getBody().getWounds();
/* 3981 */               boolean found = false;
/* 3982 */               if (wounds != null) {
/*      */                 
/* 3984 */                 Wound wound = wounds.getWound(this.target);
/* 3985 */                 if (wound != null) {
/*      */                   
/* 3987 */                   Creature tcret = wound.getCreature();
/* 3988 */                   if (tcret != null) {
/*      */                     
/* 3990 */                     if (tcret != this.performer && (this.counter == 1.0F || justTickedSecond())) {
/*      */                       
/* 3992 */                       BlockingResult result = Blocking.getBlockerBetween(this.performer, tcret, 
/* 3993 */                           getBlockingNumber(this.action));
/* 3994 */                       if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                         
/* 3996 */                         this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3997 */                             .getFirstBlocker().getName() + " is in the way.");
/* 3998 */                         this.done = true;
/*      */                       } 
/*      */                       
/* 4001 */                       if (isSameBridge(this.action))
/*      */                       {
/* 4003 */                         if (this.performer.getBridgeId() != tcret.getBridgeId()) {
/*      */                           
/* 4005 */                           this.performer.getCommunicator().sendNormalServerMessage("You need to be on the same bridge in order to do that. ");
/* 4006 */                           this.done = true;
/*      */                         } 
/*      */                       }
/*      */                     } 
/* 4010 */                     if (!this.done) {
/*      */                       
/* 4012 */                       found = true;
/* 4013 */                       if (getNumber() >= 2000) {
/*      */                         
/* 4015 */                         Emotes.emoteAt(this.action, this.performer, wound);
/* 4016 */                         this.done = true;
/*      */                       }
/* 4018 */                       else if (item == null) {
/* 4019 */                         this.done = this.behaviour.action(this, this.performer, wound, this.action, this.counter);
/*      */                       } else {
/*      */                         
/* 4022 */                         item.setBusy(true);
/* 4023 */                         this.done = this.behaviour.action(this, this.performer, item, wound, this.action, this.counter);
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/* 4029 */               if (!found) {
/*      */                 
/* 4031 */                 Wound wound = Wounds.getAnyWound(this.target);
/* 4032 */                 if (wound != null) {
/*      */                   
/* 4034 */                   Creature tcret = wound.getCreature();
/* 4035 */                   if (tcret != null) {
/*      */                     
/* 4037 */                     if (tcret != this.performer && (this.counter == 1.0F || justTickedSecond())) {
/*      */                       
/* 4039 */                       BlockingResult result = Blocking.getBlockerBetween(this.performer, tcret, 
/* 4040 */                           getBlockingNumber(this.action));
/* 4041 */                       if (result != null && result.getTotalCover() >= 100.0F) {
/*      */                         
/* 4043 */                         this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 4044 */                             .getFirstBlocker().getName() + " is in the way.");
/* 4045 */                         this.done = true;
/*      */                       } 
/* 4047 */                       if (isSameBridge(this.action))
/*      */                       {
/* 4049 */                         if (this.performer.getBridgeId() != tcret.getBridgeId()) {
/*      */                           
/* 4051 */                           this.performer.getCommunicator().sendNormalServerMessage("You need to be on the same bridge in order to do that. ");
/* 4052 */                           this.done = true;
/*      */                         } 
/*      */                       }
/*      */                     } 
/* 4056 */                     if (!this.done)
/*      */                     {
/* 4058 */                       if (getNumber() >= 2000) {
/*      */                         
/* 4060 */                         Emotes.emoteAt(this.action, this.performer, wound);
/* 4061 */                         this.done = true;
/*      */                       }
/* 4063 */                       else if (!getActionEntry().isIgnoresRange() && 
/* 4064 */                         Creature.rangeTo(this.performer, tcret) > Actions.actionEntrys[this.action]
/* 4065 */                         .getRange()) {
/*      */                         
/* 4067 */                         this.performer.getCommunicator().sendNormalServerMessage("You are now too far away to " + Actions.actionEntrys[this.action]
/*      */                             
/* 4069 */                             .getActionString().toLowerCase() + " " + tcret
/* 4070 */                             .getNameWithGenus() + ".");
/* 4071 */                         this.done = true;
/*      */                       }
/* 4073 */                       else if (item == null) {
/* 4074 */                         this.done = this.behaviour.action(this, this.performer, wound, this.action, this.counter);
/*      */                       } else {
/*      */                         
/* 4077 */                         item.setBusy(true);
/* 4078 */                         this.done = this.behaviour.action(this, this.performer, item, wound, this.action, this.counter);
/*      */                       } 
/*      */                     }
/*      */                   } else {
/*      */                     
/* 4083 */                     this.done = true;
/*      */                   } 
/*      */                 } else {
/* 4086 */                   this.done = true;
/*      */                 } 
/*      */               } 
/* 4089 */             } catch (Exception ex) {
/*      */               
/* 4091 */               this.done = true;
/*      */             }
/*      */           
/* 4094 */           } else if (this.targetType == 14) {
/*      */             
/* 4096 */             int planetId = (int)(this.target >> 16L) & 0xFFFF;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 4102 */             if (item == null) {
/* 4103 */               this.done = this.behaviour.action(this, this.performer, planetId, this.action, this.counter);
/*      */             } else {
/*      */               
/* 4106 */               this.done = this.behaviour.action(this, this.performer, item, planetId, this.action, this.counter);
/*      */             }
/*      */           
/* 4109 */           } else if (this.targetType == 18) {
/*      */             
/* 4111 */             int skillid = (int)(this.target >> 32L) & 0xFFFFFFFF;
/*      */             
/* 4113 */             if (skillid == 2147483644 || skillid == 2147483645 || skillid == 2147483642 || skillid == 2147483643 || skillid == Integer.MAX_VALUE || skillid == 2147483646) {
/*      */ 
/*      */               
/* 4116 */               this.done = true;
/*      */             } else {
/*      */ 
/*      */               
/*      */               try {
/* 4121 */                 Skill skill = this.performer.getSkills().getSkill(skillid);
/* 4122 */                 if (item == null) {
/* 4123 */                   this.done = this.behaviour.action(this, this.performer, skill, this.action, this.counter);
/*      */                 } else {
/*      */                   
/* 4126 */                   this.done = this.behaviour.action(this, this.performer, item, skill, this.action, this.counter);
/*      */                 }
/*      */               
/* 4129 */               } catch (NoSuchSkillException noSuchSkillException) {}
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 4135 */           else if (this.targetType == 22) {
/*      */             
/* 4137 */             int missionId = (int)(this.target >> 8L) & 0xFFFFFFFF;
/* 4138 */             if (item == null) {
/* 4139 */               this.done = this.behaviour.action(this, this.performer, missionId, this.action, this.counter);
/*      */             } else {
/*      */               
/* 4142 */               this.done = this.behaviour.action(this, this.performer, item, missionId, this.action, this.counter);
/*      */             }
/*      */           
/* 4145 */           } else if (this.targetType == 25) {
/*      */             
/* 4147 */             int ticketId = Tickets.decodeTicketId(this.target);
/* 4148 */             this.done = this.behaviour.action(this, this.performer, ticketId, this.action, this.counter);
/*      */           }
/* 4150 */           else if (this.targetType == 23) {
/*      */             MeshIO mesh;
/*      */             
/* 4153 */             this.tilex = Tiles.decodeTileX(this.target);
/* 4154 */             this.tiley = Tiles.decodeTileY(this.target);
/* 4155 */             byte layer = Tiles.decodeLayer(this.target);
/*      */             
/* 4157 */             if (layer == 0) {
/* 4158 */               mesh = Server.surfaceMesh;
/*      */             } else {
/* 4160 */               mesh = Server.caveMesh;
/* 4161 */             }  this.tile = mesh.getTile(this.tilex, this.tiley);
/* 4162 */             int htOffset = Floor.getHeightOffsetFromWurmId(this.target);
/* 4163 */             this.tilez = (int)(Tiles.decodeHeight(this.tile) / 10.0F);
/* 4164 */             Floor[] floors = Zones.getFloorsAtTile(this.tilex, this.tiley, htOffset, htOffset, layer);
/* 4165 */             if (floors == null) {
/*      */               
/* 4167 */               this.performer.getCommunicator().sendNormalServerMessage("Could not find that floor.");
/* 4168 */               this.done = true;
/*      */ 
/*      */             
/*      */             }
/* 4172 */             else if (floors.length == 1) {
/*      */               
/* 4174 */               if (this.counter == 1.0F || justTickedSecond()) {
/*      */                 
/* 4176 */                 BlockingResult result = Blocking.getBlockerBetween(this.performer, floors[0], 
/* 4177 */                     getBlockingNumber(this.action));
/* 4178 */                 if (result != null) {
/*      */                   
/* 4180 */                   this.performer.getCommunicator().sendNormalServerMessage("The " + result
/* 4181 */                       .getFirstBlocker().getName() + " is in the way.");
/* 4182 */                   this.done = true;
/*      */                 } 
/*      */               } 
/* 4185 */               if (!this.done)
/*      */               {
/*      */ 
/*      */                 
/* 4189 */                 if (getNumber() >= 2000) {
/*      */                   
/* 4191 */                   Emotes.emoteAt(this.action, this.performer, floors[0]);
/* 4192 */                   this.done = true;
/*      */                 } else {
/*      */                   
/* 4195 */                   this.done = this.behaviour.action(this, this.performer, item, this.onSurface, floors[0], this.tile, this.action, this.counter);
/*      */                 } 
/*      */               }
/*      */             } else {
/* 4199 */               this.performer.getCommunicator().sendNormalServerMessage("Your sensitive mind notice a wrongness in the fabric of space.");
/*      */               
/* 4201 */               this.done = true;
/*      */             }
/*      */           
/*      */           }
/* 4205 */           else if (this.targetType == 28) {
/*      */             
/* 4207 */             MeshIO mesh = Server.surfaceMesh;
/* 4208 */             this.tile = mesh.getTile(this.tilex, this.tiley);
/* 4209 */             this.tilex = Tiles.decodeTileX(this.target);
/* 4210 */             this.tiley = Tiles.decodeTileY(this.target);
/* 4211 */             this.tile = mesh.getTile(this.tilex, this.tiley);
/* 4212 */             int ht = Tiles.decodeHeight(this.tile);
/* 4213 */             int layer = Tiles.decodeLayer(this.target);
/*      */             
/* 4215 */             this.tilez = (int)(ht / 10.0F);
/* 4216 */             BridgePart[] bridgeParts = Zones.getBridgePartsAtTile(this.tilex, this.tiley, (layer == 0));
/* 4217 */             if (bridgeParts == null) {
/*      */               
/* 4219 */               this.performer.getCommunicator().sendNormalServerMessage("Could not find that bridge part.");
/* 4220 */               this.done = true;
/*      */             }
/*      */             else {
/*      */               
/* 4224 */               BridgePart foundPart = null;
/* 4225 */               if (bridgeParts.length > 1) {
/*      */                 
/* 4227 */                 this.performer.getCommunicator().sendNormalServerMessage("Found more than 1 bridge part on tile.");
/* 4228 */                 this.done = true;
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/* 4233 */                 foundPart = bridgeParts[0];
/*      */               } 
/* 4235 */               if (!this.done)
/*      */               {
/*      */ 
/*      */                 
/* 4239 */                 if (getNumber() >= 2000) {
/*      */                   
/* 4241 */                   Emotes.emoteAt(this.action, this.performer, foundPart);
/* 4242 */                   this.done = true;
/*      */                 
/*      */                 }
/* 4245 */                 else if (item == null) {
/* 4246 */                   this.done = this.behaviour.action(this, this.performer, this.onSurface, foundPart, this.tile, this.action, this.counter);
/*      */                 } else {
/* 4248 */                   this.done = this.behaviour.action(this, this.performer, item, this.onSurface, foundPart, this.tile, this.action, this.counter);
/*      */                 }  } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 4253 */       }  if (this.done) {
/*      */         
/* 4255 */         if (getNumber() == 329)
/* 4256 */           this.performer.getCommunicator().sendStopUseBinoculars(); 
/* 4257 */         if (getNumber() == 748 || getNumber() == 759)
/* 4258 */           this.performer.getCommunicator().sendHideLinks(); 
/* 4259 */         if (item != null) {
/*      */           
/* 4261 */           item.setBusy(false);
/* 4262 */           if (!isQuick())
/* 4263 */             this.performer.getCurrentTile().sendStopUseItem(this.performer); 
/* 4264 */           if (item.isRefreshedOnUse())
/* 4265 */             item.setLastMaintained(WurmCalendar.currentTime); 
/*      */         } 
/* 4267 */         if (this.targetType == 2 || this.targetType == 6 || this.targetType == 19 || this.targetType == 20) {
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 4272 */             Item targetItem = Items.getItem(this.target);
/* 4273 */             targetItem.setBusy(false);
/* 4274 */             if (targetItem.isRefreshedOnUse()) {
/* 4275 */               targetItem.setLastMaintained(WurmCalendar.currentTime);
/*      */             }
/* 4277 */           } catch (NoSuchItemException noSuchItemException) {}
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 4283 */       if (this.done) {
/*      */ 
/*      */ 
/*      */         
/* 4287 */         boolean toReturn = true;
/* 4288 */         if (this.action != 142 && this.action != 148 && this.action != 192 && this.action != 350 && (this.action < 496 || this.action > 502))
/*      */         {
/*      */           
/* 4291 */           if ((this.action >= 2000 && this.action < 8000) || !getActionEntry().isFatigue() || this.counter > 1.0F) {
/*      */             
/* 4293 */             toReturn = MissionTriggers.activateTriggers(this.performer, item, this.action, this.target, this.triggerCounter);
/* 4294 */             if (!toReturn && this.targetType == 5) {
/*      */               
/* 4296 */               VolaTile t = Zones.getTileOrNull(this.tilex, this.tiley, this.performer.isOnSurface());
/* 4297 */               if (t != null)
/*      */               {
/* 4299 */                 if (t.getStructure() != null) {
/* 4300 */                   toReturn = MissionTriggers.activateTriggers(this.performer, item, this.action, t.getStructure()
/* 4301 */                       .getWurmId(), this.triggerCounter);
/*      */                 }
/*      */               }
/*      */             } 
/* 4305 */             if (this.action == 236 || this.action == 235 || this.action == 237 || this.action == 7 || this.action == 6 || this.action == 342 || this.action == 745 || this.action == 4 || this.action == 3 || this.action == 1 || this.action == 343 || this.action == 344) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 4313 */               StringBuffer acts = new StringBuffer(Actions.actionEntrys[getNumber()].getAnimationString().toLowerCase().replace(" ", ""));
/* 4314 */               if (Actions.actionEntrys[getNumber()].isSpell())
/* 4315 */                 acts.insert(0, "spell."); 
/* 4316 */               this.performer.playAnimation(acts.toString(), false, this.target);
/*      */             } 
/*      */             
/* 4319 */             if (this.justTickedSecond || this.triggerCounter == 0) {
/* 4320 */               this.triggerCounter++;
/*      */             }
/*      */           } 
/*      */         }
/* 4324 */         if (toReturn && (justTickedSecond() ? (currentSecond() > 2) : (this.lastSecond > 2))) {
/*      */           
/* 4326 */           this.performer.achievement(106);
/* 4327 */           decayEnchants();
/*      */         } 
/*      */         
/* 4330 */         return toReturn;
/*      */       } 
/*      */ 
/*      */       
/* 4334 */       if (currentSecond() == 1)
/*      */       {
/* 4336 */         if ((getNumber() < 2000 || getNumber() > 8000) && !isActionAttack(getNumber()))
/*      */         {
/* 4338 */           if (this.targetType == 2 || this.targetType == 6 || this.targetType == 19 || this.targetType == 20) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 4346 */             if (this.action == 239 || this.action == 238)
/*      */             {
/* 4348 */               this.performer.playAnimation(Actions.actionEntrys[237].getActionString().toLowerCase()
/* 4349 */                   .replace(" ", ""), false, this.target);
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*      */               
/* 4355 */               StringBuffer acts = new StringBuffer(Actions.actionEntrys[getNumber()].getActionString().toLowerCase().replace(" ", ""));
/* 4356 */               if (Actions.actionEntrys[getNumber()].isSpell())
/* 4357 */                 acts.insert(0, "spell."); 
/* 4358 */               this.performer.playAnimation(acts.toString(), false, this.target);
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
/*      */             }
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
/*      */           }
/*      */           else {
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
/* 4392 */             StringBuffer acts = new StringBuffer(Actions.actionEntrys[getNumber()].getAnimationString());
/* 4393 */             if (Actions.actionEntrys[getNumber()].isSpell())
/* 4394 */               acts.insert(0, "spell."); 
/* 4395 */             if (this.performer.getLayer() < 0) {
/*      */               
/* 4397 */               if (getNumber() == 532 || getNumber() == 150)
/* 4398 */                 acts.append(".cave"); 
/* 4399 */               if (this.targetType == 17)
/*      */               {
/* 4401 */                 if (getNumber() == 532 || getNumber() == 150 || getNumber() == 145) {
/*      */                   
/* 4403 */                   int dir = CaveTile.decodeCaveTileDir(this.target);
/* 4404 */                   if (dir == 1)
/* 4405 */                     acts.append(".ceiling"); 
/*      */                 } 
/*      */               }
/*      */             } 
/* 4409 */             if (this.targetType == 23 && FloorBehaviour.isConstructionAction(getNumber())) {
/*      */ 
/*      */ 
/*      */               
/* 4413 */               acts.setLength(0);
/* 4414 */               acts.append("buildfloor");
/*      */               
/* 4416 */               int tfl = Tiles.decodeFloorLevel(this.target);
/* 4417 */               if (tfl > this.performer.getFloorLevel())
/* 4418 */                 acts.append(".above"); 
/*      */             } 
/* 4420 */             if (this.targetType == 28) {
/*      */               
/* 4422 */               acts.setLength(0);
/* 4423 */               acts.append("buildbridge");
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 4430 */             if (getNumber() == 183 && (this.targetType == 3 || this.targetType == 17))
/* 4431 */               acts.append(".ground"); 
/* 4432 */             if (getNumber() == 160) {
/*      */               
/* 4434 */               Optional<Item> i = Items.getItemOptional(this.subject);
/* 4435 */               if (i.isPresent() && ((Item)i.get()).getTemplateId() == 1343)
/*      */               {
/* 4437 */                 acts.append(".net");
/* 4438 */                 this.performer.playAnimation(acts.toString(), false, this.target);
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 4443 */               this.performer.playAnimation(acts.toString(), false, this.target);
/*      */             }
/*      */           
/*      */           } 
/*      */         }
/*      */       }
/* 4449 */     } catch (NullPointerException ex) {
/*      */       
/* 4451 */       logger.log(Level.WARNING, "NullPointer while polling an action " + this.performer
/* 4452 */           .getName() + " due to " + ex.getMessage(), ex);
/* 4453 */       this.done = true;
/*      */     }
/* 4455 */     catch (ArrayIndexOutOfBoundsException aio) {
/*      */       
/* 4457 */       logger.log(Level.WARNING, "Array index out of bounds while polling an action " + this.performer.getName() + " due to " + aio
/* 4458 */           .getMessage(), aio);
/* 4459 */       this.done = true;
/*      */     } 
/* 4461 */     if (this.done && (justTickedSecond() ? (currentSecond() > 2) : (this.lastSecond > 2)))
/* 4462 */       this.performer.achievement(106); 
/* 4463 */     return this.done;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean warnedPlayer() {
/* 4468 */     Village village = Zones.getVillage(this.tilex, this.tiley, true);
/* 4469 */     return warnedPlayer(village);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean warnedPlayer(Village village) {
/* 4474 */     if (village != null)
/*      */     {
/* 4476 */       if (village.checkGuards(this, this.performer)) {
/*      */         
/* 4478 */         this.performer.getCommunicator().sendNormalServerMessage("A guard has noted you and stops you with a warning.", (byte)4);
/*      */         
/* 4480 */         village.modifyReputations(this, this.performer);
/* 4481 */         if (village.getReputation(this.performer) <= -30)
/* 4482 */           village.addTarget(this.performer); 
/* 4483 */         return true;
/*      */       } 
/*      */     }
/* 4486 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getSubjectId() {
/* 4491 */     return this.subject;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Item getSubject() {
/* 4497 */     Item item = null;
/* 4498 */     if (this.subject != -1L && !isEmote() && 
/* 4499 */       getActionEntry().getUseActiveItem() != 2) {
/*      */       
/*      */       try {
/*      */         
/* 4503 */         item = Items.getItem(this.subject);
/*      */       }
/* 4505 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */     
/* 4509 */     return item;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setRarity(byte newRarity) {
/* 4514 */     this.rarity = newRarity;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getRarity() {
/* 4519 */     return this.rarity;
/*      */   }
/*      */ 
/*      */   
/*      */   public String stop(boolean farAway) {
/* 4524 */     decayEnchants();
/*      */ 
/*      */     
/* 4527 */     String toReturn = "You stop.";
/* 4528 */     int num = getNumber();
/* 4529 */     if (num < 2000) {
/*      */       
/* 4531 */       toReturn = "You stop " + Actions.actionEntrys[getNumber()].getVerbString().toLowerCase() + ".";
/* 4532 */       if (this.isOffensive)
/* 4533 */         this.performer.setOpponent(null); 
/*      */     } 
/* 4535 */     if (num == 329)
/* 4536 */       this.performer.getCommunicator().sendStopUseBinoculars(); 
/* 4537 */     if (getNumber() == 748 || getNumber() == 759) {
/* 4538 */       this.performer.getCommunicator().sendHideLinks();
/* 4539 */     } else if (num == 136) {
/* 4540 */       this.performer.getCommunicator().sendToggle(3, false);
/* 4541 */     } else if (num == 742) {
/*      */       
/* 4543 */       if (this.performer.isPlayer())
/*      */       {
/* 4545 */         ((Player)this.performer).setIsWritingRecipe(false);
/*      */       }
/*      */     } 
/*      */     
/* 4549 */     if (num == 118 && 
/* 4550 */       this.performer.isFrozen()) {
/* 4551 */       this.performer.toggleFrozen(this.performer);
/*      */     }
/* 4553 */     if (num == 160) {
/* 4554 */       MethodsFishing.destroyFishCreature(this);
/*      */     }
/* 4556 */     if (num == 353) {
/*      */ 
/*      */ 
/*      */       
/* 4560 */       if (this.performer.getKingdomId() == 1) {
/*      */         
/* 4562 */         if (Methods.getJennElector() != null)
/*      */         {
/* 4564 */           Methods.getJennElector().submerge();
/*      */         }
/* 4566 */         Methods.resetJennElector();
/*      */       } 
/* 4568 */       if (this.performer.getKingdomId() == 3)
/* 4569 */         Methods.resetHotsElector(); 
/* 4570 */       if (this.performer.getKingdomId() == 2)
/* 4571 */         Methods.resetMolrStone(); 
/*      */     } 
/* 4573 */     if (this.destroyedItem != null)
/* 4574 */       Items.decay(this.destroyedItem.getWurmId(), this.destroyedItem.getDbStrings()); 
/* 4575 */     if (this.tempCreature != null)
/*      */     {
/* 4577 */       this.tempCreature.destroy();
/*      */     }
/* 4579 */     if (this.subject != -1L) {
/*      */       
/*      */       try {
/*      */         
/* 4583 */         if (!isQuick())
/* 4584 */           this.performer.getCurrentTile().sendStopUseItem(this.performer); 
/* 4585 */         Item item = Items.getItem(this.subject);
/*      */         
/* 4587 */         item.setBusy(false);
/* 4588 */         if (item.isRefreshedOnUse()) {
/* 4589 */           item.setLastMaintained(WurmCalendar.currentTime);
/*      */         }
/* 4591 */         if (item.getTemplate().isRune() && getNumber() == 118) {
/* 4592 */           toReturn = "You stop attaching.";
/*      */         }
/* 4594 */       } catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4599 */     if ((this.targetType == 2 || this.targetType == 6 || this.targetType == 19 || this.targetType == 20) && this.destroyedItem == null) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 4604 */         Item targetItem = Items.getItem(this.target);
/* 4605 */         targetItem.setBusy(false);
/* 4606 */         if (targetItem.isRefreshedOnUse()) {
/* 4607 */           targetItem.setLastMaintained(WurmCalendar.currentTime);
/*      */         }
/* 4609 */       } catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4618 */     if (farAway)
/*      */     {
/* 4620 */       if (num < 2000)
/* 4621 */         toReturn = "You are now too far away to " + Actions.actionEntrys[num].getActionString().toLowerCase() + "."; 
/*      */     }
/* 4623 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private void decayEnchants() {
/*      */     Item item;
/*      */     try {
/* 4630 */       item = Items.getItem(this.subject);
/* 4631 */     } catch (NoSuchItemException e) {
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 4636 */     int rarityModifier = Math.max(1, item.getRarity() * 5);
/* 4637 */     ItemSpellEffects eff = item.getSpellEffects();
/* 4638 */     if (eff != null) {
/*      */       
/* 4640 */       float runeModifier = eff.getRuneEffect(RuneUtilities.ModifierEffect.ENCH_ENCHRETENTION);
/* 4641 */       double actionSeconds = this.counter;
/*      */       
/* 4643 */       SpellEffect[] effs = eff.getEffects();
/* 4644 */       for (int x = 0; x < effs.length; x++) {
/*      */         
/* 4646 */         if ((effs[x]).type >= 0) {
/*      */ 
/*      */           
/* 4649 */           double enchantResistance = (rarityModifier * (effs[x]).power * 30.0F * runeModifier);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4654 */           double enchantLossChance = 1.0D - Math.pow(1.0D - 1.0D / enchantResistance, Math.max(1.5D, actionSeconds / 5.0D - 0.5D));
/* 4655 */           enchantLossChance *= Math.min(actionSeconds / 10.0D, 1.0D) * Math.min(actionSeconds / 10.0D, 1.0D);
/*      */           
/* 4657 */           if (Server.rand.nextDouble() < enchantLossChance) {
/*      */             
/* 4659 */             effs[x].setPower((effs[x]).power - 1.0F);
/* 4660 */             if ((effs[x]).power <= 0.0F)
/*      */             {
/* 4662 */               eff.removeSpellEffect((effs[x]).type);
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
/*      */   public boolean isManualInvulnerable() {
/* 4676 */     return this.manualInvulnerable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setManualInvulnerable(boolean aManualInvulnerable) {
/* 4687 */     this.manualInvulnerable = aManualInvulnerable;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Action.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */