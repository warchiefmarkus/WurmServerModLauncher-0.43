/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.math.Vector;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MessageServer;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Set;
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
/*      */ public class Arrows
/*      */   implements MiscConstants, ItemMaterials, SoundNames, TimeConstants
/*      */ {
/*   74 */   private static Logger logger = Logger.getLogger(Arrows.class.getName());
/*      */   
/*      */   public enum ArrowHitting
/*      */   {
/*   78 */     NOT, SHIELD, EVASION, HIT, HIT_NO_DAMAGE, TREE, FENCE_STONE, FENCE_TREE, GROUND;
/*      */   }
/*      */   
/*      */   public enum ArrowDestroy
/*      */   {
/*   83 */     NOT, NORMAL, WATER, BREAKS, DO_NOTHING;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   private static final Set<Arrows> arrows = new HashSet<>();
/*      */   
/*      */   private final Item arrow;
/*      */   
/*      */   private final Creature performer;
/*      */   
/*      */   private Creature defender;
/*      */   
/*      */   private boolean hitTarget;
/*      */   
/*      */   private final int tileArrowDownX;
/*      */   
/*      */   private final int tileArrowDownY;
/*      */   
/*      */   private final ArrowHitting arrowHitting;
/*      */   
/*      */   private final ArrowDestroy arrowDestroy;
/*      */   
/*      */   private final Item bow;
/*      */   
/*      */   private double damage;
/*      */   
/*      */   private float damMod;
/*      */   
/*      */   private float armourMod;
/*      */   
/*      */   private byte pos;
/*      */   
/*      */   private final float speed;
/*      */   
/*      */   private final float totalTime;
/*      */   
/*      */   private float time;
/*      */   
/*      */   private final boolean hittingCreature;
/*      */   
/*      */   private Item item;
/*      */   
/*      */   private String scoreString;
/*      */   
/*      */   private boolean dryRun;
/*      */   
/*      */   private double difficulty;
/*      */   
/*      */   private double bonus;
/*      */ 
/*      */   
/*      */   public Arrows(Item aArrow, Creature aDefender, ArrowHitting aArrowHitting, ArrowDestroy aArrowDestroy, Item aBow, double aDamage, byte aPos, byte proj) {
/*  137 */     this.hittingCreature = true;
/*  138 */     this.arrow = aArrow;
/*  139 */     this.defender = aDefender;
/*  140 */     this.arrowHitting = aArrowHitting;
/*  141 */     this.arrowDestroy = aArrowDestroy;
/*  142 */     this.bow = aBow;
/*  143 */     this.damage = aDamage;
/*  144 */     this.damMod = 0.0F;
/*  145 */     this.armourMod = 0.0F;
/*  146 */     this.tileArrowDownX = -1;
/*  147 */     this.tileArrowDownY = -1;
/*  148 */     this.performer = null;
/*  149 */     this.pos = aPos;
/*  150 */     this.dryRun = true;
/*  151 */     this.difficulty = 0.0D;
/*  152 */     this.bonus = 0.0D;
/*  153 */     this.time = 0.0F;
/*  154 */     aArrow.setPosXYZ(aBow.getPosX(), aBow.getPosY(), aBow.getPosZ() + 2.0F);
/*      */     
/*  156 */     if (aArrowHitting == ArrowHitting.HIT || aArrowHitting == ArrowHitting.HIT_NO_DAMAGE || aArrowHitting == ArrowHitting.SHIELD || aArrowHitting == ArrowHitting.EVASION) {
/*      */ 
/*      */       
/*  159 */       this.hitTarget = true;
/*      */     }
/*      */     else {
/*      */       
/*  163 */       this.hitTarget = false;
/*      */     } 
/*      */     
/*  166 */     float length = 1.0F;
/*  167 */     if (this.hitTarget) {
/*      */       
/*  169 */       float x = aDefender.getPosX() - aBow.getPosX();
/*  170 */       float y = aDefender.getPosY() - aBow.getPosY();
/*  171 */       float h = aDefender.getPositionZ() + aDefender.getAltOffZ() - aBow.getPosZ() + 2.0F;
/*      */       
/*  173 */       Vector vector = new Vector(x, y, h);
/*  174 */       length = vector.length();
/*      */     } 
/*  176 */     this.speed = (this.arrow.getMaterial() == 21) ? 13.0F : 45.0F;
/*      */     
/*  178 */     this.totalTime = length / this.speed * 1000.0F;
/*      */     
/*  180 */     if (this.hitTarget) {
/*      */       
/*  182 */       double newrot = Math.atan2((aDefender.getPosY() - (int)aArrow.getPosY()), (aDefender
/*  183 */           .getPosX() - (int)aArrow.getPosX()));
/*      */       
/*  185 */       float attAngle = (float)(newrot * 57.29577951308232D) - 90.0F;
/*  186 */       attAngle = Creature.normalizeAngle(attAngle);
/*  187 */       VolaTile tile = Zones.getOrCreateTile(aBow.getTileX(), aBow.getTileY(), aBow.isOnSurface());
/*  188 */       if (tile != null)
/*      */       {
/*  190 */         tile.sendProjectile(aArrow.getWurmId(), proj, aArrow.getModelName(), aArrow
/*  191 */             .getName(), aArrow
/*  192 */             .getMaterial(), aBow.getPosX(), aBow.getPosY(), aBow.getPosZ() + 2.0F, attAngle, 
/*  193 */             (byte)(aBow.isOnSurface() ? 0 : -1), (int)aDefender.getPosX(), 
/*  194 */             (int)aDefender.getPosY(), aDefender.getPositionZ() + aDefender.getAltOffZ(), aBow.getWurmId(), aDefender
/*  195 */             .getWurmId(), this.totalTime, this.totalTime);
/*      */       }
/*      */ 
/*      */       
/*  199 */       tile = aDefender.getCurrentTile();
/*      */       
/*  201 */       if (tile != null)
/*      */       {
/*  203 */         tile.sendProjectile(aArrow.getWurmId(), proj, aArrow.getModelName(), aArrow
/*  204 */             .getName(), aArrow
/*  205 */             .getMaterial(), aBow.getPosX(), aBow.getPosY(), aBow.getPosZ() + 2.0F, attAngle, 
/*  206 */             (byte)(aBow.isOnSurface() ? 0 : -1), (int)aDefender.getPosX(), 
/*  207 */             (int)aDefender.getPosY(), aDefender.getPositionZ() + aDefender.getAltOffZ(), aBow.getWurmId(), aDefender
/*  208 */             .getWurmId(), this.totalTime, this.totalTime);
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
/*      */   public Arrows(Item aArrow, Creature aPerformer, Creature aDefender, int aTileArrowDownX, int aTileArrowDownY, ArrowHitting aArrowHitting, ArrowDestroy aArrowDestroy, Item aBow1, double aDamage, float aDamMod, float aArmourMod, byte aPos, boolean dry, double diff, double bon) {
/*  220 */     this.hittingCreature = true;
/*  221 */     this.arrow = aArrow;
/*  222 */     this.performer = aPerformer;
/*  223 */     this.defender = aDefender;
/*  224 */     this.tileArrowDownX = aTileArrowDownX;
/*  225 */     this.tileArrowDownY = aTileArrowDownY;
/*  226 */     this.arrowHitting = aArrowHitting;
/*  227 */     this.arrowDestroy = aArrowDestroy;
/*  228 */     this.bow = aBow1;
/*  229 */     this.damage = aDamage;
/*  230 */     this.damMod = aDamMod;
/*  231 */     this.armourMod = aArmourMod;
/*  232 */     this.pos = aPos;
/*  233 */     this.dryRun = dry;
/*  234 */     this.difficulty = diff;
/*  235 */     this.bonus = bon;
/*  236 */     this.time = 0.0F;
/*  237 */     aArrow.setPosXYZ(aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ());
/*      */     
/*  239 */     if (aArrowHitting == ArrowHitting.HIT || aArrowHitting == ArrowHitting.HIT_NO_DAMAGE || aArrowHitting == ArrowHitting.SHIELD || aArrowHitting == ArrowHitting.EVASION) {
/*      */ 
/*      */       
/*  242 */       this.hitTarget = true;
/*      */     }
/*      */     else {
/*      */       
/*  246 */       this.hitTarget = false;
/*      */     } 
/*      */     
/*  249 */     float length = 1.0F;
/*  250 */     if (this.hitTarget) {
/*      */       
/*  252 */       float x = aDefender.getPosX() - aPerformer.getPosX();
/*  253 */       float y = aDefender.getPosY() - aPerformer.getPosY();
/*      */       
/*  255 */       float h = aDefender.getPositionZ() + aDefender.getAltOffZ() - aPerformer.getPositionZ() + aPerformer.getAltOffZ();
/*      */       
/*  257 */       Vector vector = new Vector(x, y, h);
/*  258 */       length = vector.length();
/*      */     }
/*      */     else {
/*      */       
/*  262 */       float x = ((aTileArrowDownX << 2) + 2) - aPerformer.getPosX();
/*  263 */       float y = ((aTileArrowDownY << 2) + 2) - aPerformer.getPosY();
/*      */       
/*  265 */       float h = aDefender.getPositionZ() + aDefender.getAltOffZ() - aPerformer.getPositionZ() + aPerformer.getAltOffZ();
/*      */       
/*  267 */       Vector vector = new Vector(x, y, h);
/*  268 */       length = vector.length();
/*      */     } 
/*      */     
/*  271 */     this.speed = 45.0F;
/*  272 */     this.totalTime = length / this.speed * 1000.0F;
/*      */     
/*  274 */     if (this.hitTarget) {
/*      */       
/*  276 */       VolaTile tile = aPerformer.getCurrentTile();
/*  277 */       if (tile != null)
/*      */       {
/*  279 */         tile.sendProjectile(aArrow.getWurmId(), (byte)1, aArrow.getModelName(), aArrow
/*  280 */             .getName(), aArrow
/*  281 */             .getMaterial(), aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer
/*  282 */             .getAltOffZ(), aPerformer
/*  283 */             .getStatus().getRotation(), (byte)aPerformer.getLayer(), (int)aDefender.getPosX(), 
/*  284 */             (int)aDefender.getPosY(), aDefender.getPositionZ() + aDefender.getAltOffZ(), aPerformer.getWurmId(), aDefender
/*  285 */             .getWurmId(), this.totalTime, this.totalTime);
/*      */       }
/*      */ 
/*      */       
/*  289 */       tile = aDefender.getCurrentTile();
/*      */       
/*  291 */       if (tile != null)
/*      */       {
/*  293 */         tile.sendProjectile(aArrow.getWurmId(), (byte)1, aArrow.getModelName(), aArrow
/*  294 */             .getName(), aArrow
/*  295 */             .getMaterial(), aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer
/*  296 */             .getAltOffZ(), aPerformer
/*  297 */             .getStatus().getRotation(), (byte)aPerformer.getLayer(), (int)aDefender.getPosX(), 
/*  298 */             (int)aDefender.getPosY(), aDefender.getPositionZ() + aDefender.getAltOffZ(), aPerformer.getWurmId(), aDefender
/*  299 */             .getWurmId(), this.totalTime, this.totalTime);
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  305 */       VolaTile tile = aPerformer.getCurrentTile();
/*  306 */       if (tile != null)
/*      */       {
/*  308 */         tile.sendProjectile(aArrow.getWurmId(), (byte)1, aArrow.getModelName(), aArrow
/*  309 */             .getName(), aArrow
/*  310 */             .getMaterial(), aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer
/*  311 */             .getAltOffZ(), aPerformer
/*  312 */             .getStatus().getRotation(), (byte)aPerformer.getLayer(), ((aTileArrowDownX << 2) + 2), ((aTileArrowDownY << 2) + 2), aDefender
/*  313 */             .getPositionZ() + aDefender.getAltOffZ(), aPerformer.getWurmId(), -2L, this.totalTime, this.totalTime);
/*      */       }
/*      */ 
/*      */       
/*  317 */       tile = aDefender.getCurrentTile();
/*      */       
/*  319 */       if (tile != null)
/*      */       {
/*  321 */         tile.sendProjectile(aArrow.getWurmId(), (byte)1, aArrow.getModelName(), aArrow
/*  322 */             .getName(), aArrow
/*  323 */             .getMaterial(), aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer
/*  324 */             .getAltOffZ(), aPerformer
/*  325 */             .getStatus().getRotation(), (byte)aPerformer.getLayer(), ((aTileArrowDownX << 2) + 2), ((aTileArrowDownY << 2) + 2), aDefender
/*  326 */             .getPositionZ() + aDefender.getAltOffZ(), aPerformer.getWurmId(), -2L, this.totalTime, this.totalTime);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Arrows(Item aArrow, Creature aPerformer, Item aItem, int aTileArrowDownX, int aTileArrowDownY, Item aBow, ArrowHitting aArrowHitting, ArrowDestroy aArrowDestroy, String aScoreString) {
/*  337 */     this.hittingCreature = false;
/*  338 */     this.arrow = aArrow;
/*  339 */     this.performer = aPerformer;
/*  340 */     this.item = aItem;
/*  341 */     this.tileArrowDownX = aTileArrowDownX;
/*  342 */     this.tileArrowDownY = aTileArrowDownY;
/*  343 */     this.arrowHitting = aArrowHitting;
/*  344 */     this.arrowDestroy = aArrowDestroy;
/*  345 */     this.bow = aBow;
/*  346 */     this.scoreString = aScoreString;
/*  347 */     this.time = 0.0F;
/*      */     
/*  349 */     aArrow.setPosXYZ(aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer.getAltOffZ());
/*      */     
/*  351 */     if (aArrowHitting == ArrowHitting.HIT || aArrowHitting == ArrowHitting.HIT_NO_DAMAGE || aArrowHitting == ArrowHitting.SHIELD || aArrowHitting == ArrowHitting.EVASION) {
/*      */ 
/*      */       
/*  354 */       this.hitTarget = true;
/*      */     }
/*      */     else {
/*      */       
/*  358 */       this.hitTarget = false;
/*      */     } 
/*      */     
/*  361 */     float length = 1.0F;
/*      */     
/*  363 */     if (this.hitTarget) {
/*      */       
/*  365 */       float x = aItem.getPosX() - aPerformer.getPosX();
/*  366 */       float y = aItem.getPosY() - aPerformer.getPosY();
/*  367 */       float h = aItem.getPosZ() + 1.0F - aPerformer.getPositionZ() + aPerformer.getAltOffZ();
/*      */       
/*  369 */       Vector vector = new Vector(x, y, h);
/*  370 */       length = vector.length();
/*      */     }
/*      */     else {
/*      */       
/*  374 */       float x = ((aTileArrowDownX << 2) + 2) - aPerformer.getPosX();
/*  375 */       float y = ((aTileArrowDownY << 2) + 2) - aPerformer.getPosY();
/*  376 */       float h = aItem.getPosZ() - aPerformer.getPositionZ() + aPerformer.getAltOffZ();
/*      */       
/*  378 */       Vector vector = new Vector(x, y, h);
/*  379 */       length = vector.length();
/*      */     } 
/*      */     
/*  382 */     this.speed = 45.0F;
/*  383 */     this.totalTime = length / this.speed * 1000.0F;
/*      */     
/*  385 */     if (this.hitTarget) {
/*      */       
/*  387 */       VolaTile tile = aPerformer.getCurrentTile();
/*  388 */       if (tile != null)
/*      */       {
/*  390 */         tile.sendProjectile(aArrow.getWurmId(), (byte)1, aArrow.getModelName(), aArrow
/*  391 */             .getName(), aArrow
/*  392 */             .getMaterial(), aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer
/*  393 */             .getAltOffZ(), aPerformer
/*  394 */             .getStatus().getRotation(), (byte)aPerformer.getLayer(), (int)aItem.getPosX(), 
/*  395 */             (int)aItem.getPosY(), aItem.getPosZ(), aPerformer.getWurmId(), aItem.getWurmId(), this.totalTime, this.totalTime);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  400 */       VolaTile tile = aPerformer.getCurrentTile();
/*  401 */       if (tile != null)
/*      */       {
/*  403 */         tile.sendProjectile(aArrow.getWurmId(), (byte)1, aArrow.getModelName(), aArrow
/*  404 */             .getName(), aArrow
/*  405 */             .getMaterial(), aPerformer.getPosX(), aPerformer.getPosY(), aPerformer.getPositionZ() + aPerformer
/*  406 */             .getAltOffZ(), aPerformer
/*  407 */             .getStatus().getRotation(), (byte)aPerformer.getLayer(), ((aTileArrowDownX << 2) + 2), ((aTileArrowDownY << 2) + 2), aItem
/*  408 */             .getPosZ(), aPerformer.getWurmId(), -2L, this.totalTime, this.totalTime);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pollAll(float elapsedTime) {
/*  415 */     for (Iterator<Arrows> i = arrows.iterator(); i.hasNext(); ) {
/*      */       
/*  417 */       Arrows arrow = i.next();
/*      */       
/*  419 */       if (arrow.hittingCreature) {
/*      */         
/*  421 */         if (!arrow.pollHitCreature(elapsedTime))
/*      */         {
/*  423 */           i.remove();
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*  428 */       if (!arrow.pollHitItem(elapsedTime))
/*      */       {
/*  430 */         i.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean pollHitCreature(float elapsedTime) {
/*  438 */     this.time += elapsedTime;
/*      */     
/*  440 */     if (this.defender.isDead())
/*  441 */       return false; 
/*  442 */     if (this.time > this.totalTime) {
/*      */ 
/*      */ 
/*      */       
/*  446 */       boolean tooLate = (this.arrow.getMaterial() == 21 && !this.defender.isWithinDistanceTo(this.bow.getPosX(), this.bow.getPosY(), this.bow.getPosZ(), 12.0F));
/*  447 */       if (tooLate) {
/*      */         
/*  449 */         Items.destroyItem(this.arrow.getWurmId());
/*  450 */         return false;
/*      */       } 
/*  452 */       if (this.arrowHitting == ArrowHitting.HIT) {
/*      */         
/*  454 */         if (this.performer == null)
/*      */         {
/*  456 */           byte type = 2;
/*  457 */           if (this.bow.isEnchantedTurret())
/*      */           {
/*  459 */             switch (this.bow.getTemplateId()) {
/*      */               
/*      */               case 940:
/*  462 */                 type = 10;
/*      */                 break;
/*      */               case 941:
/*  465 */                 type = 4;
/*      */                 break;
/*      */               case 968:
/*  468 */                 type = 8;
/*      */                 break;
/*      */               case 942:
/*  471 */                 type = 4;
/*      */                 break;
/*      */             } 
/*      */           
/*      */           }
/*  476 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  477 */           segments.add(new MulticolorLineSegment(this.arrow.getNameWithGenus() + " from the ", (byte)7));
/*  478 */           segments.add(new MulticolorLineSegment(this.bow.getName(), (byte)2));
/*  479 */           segments.add(new MulticolorLineSegment(" hits you in the " + this.defender.getBody().getWoundLocationString(this.pos) + ".", (byte)7));
/*      */           
/*  481 */           this.defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */           
/*  485 */           this.defender.addWoundOfType(this.performer, type, this.pos, false, 1.0F, true, this.damage, 0.0F, 0.0F, false, false);
/*      */         }
/*      */         else
/*      */         {
/*  489 */           Archery.hit(this.defender, this.performer, this.arrow, this.bow, this.damage, this.damMod, this.armourMod, this.pos, true, this.dryRun, this.difficulty, this.bonus);
/*      */         }
/*      */       
/*  492 */       } else if (this.arrowHitting == ArrowHitting.HIT_NO_DAMAGE) {
/*      */         
/*  494 */         if (this.performer != null) {
/*      */           
/*  496 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  497 */           segments.add(new MulticolorLineSegment("Your arrow glances off ", (byte)0));
/*  498 */           segments.add(new CreatureLineSegment(this.defender));
/*  499 */           segments.add(new MulticolorLineSegment(" and does no damage.", (byte)0));
/*      */           
/*  501 */           this.performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         } 
/*      */ 
/*      */         
/*  505 */         this.defender.getCommunicator().sendCombatSafeMessage("An arrow hits you on the " + this.defender
/*  506 */             .getBody().getWoundLocationString(this.pos) + " but does no damage.");
/*      */       }
/*  508 */       else if (this.arrowHitting == ArrowHitting.SHIELD) {
/*      */         
/*  510 */         if (this.performer != null) {
/*      */           
/*  512 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  513 */           segments.add(new MulticolorLineSegment("Your arrow glances off ", (byte)0));
/*  514 */           segments.add(new CreatureLineSegment(this.defender));
/*  515 */           segments.add(new MulticolorLineSegment("'s shield.", (byte)0));
/*      */           
/*  517 */           this.performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         } 
/*      */ 
/*      */         
/*  521 */         this.defender.getCommunicator().sendCombatSafeMessage("You instinctively block an arrow with your shield.");
/*      */       }
/*  523 */       else if (this.arrowHitting == ArrowHitting.EVASION) {
/*      */         
/*  525 */         if (this.performer != null) {
/*      */           
/*  527 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  528 */           segments.add(new MulticolorLineSegment("Your arrow glances off ", (byte)0));
/*  529 */           segments.add(new CreatureLineSegment(this.defender));
/*  530 */           segments.add(new MulticolorLineSegment("'s armour.", (byte)0));
/*      */           
/*  532 */           this.performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         } 
/*      */ 
/*      */         
/*  536 */         this.defender.getCommunicator().sendCombatSafeMessage("An arrow hits you on the " + this.defender
/*  537 */             .getBody().getWoundLocationString(this.pos) + " but glances off your armour.");
/*      */       
/*      */       }
/*  540 */       else if (this.arrowHitting == ArrowHitting.TREE) {
/*      */         
/*  542 */         SoundPlayer.playSound("sound.arrow.stuck.wood", this.tileArrowDownX, this.tileArrowDownY, this.defender.isOnSurface(), 0.0F);
/*      */       }
/*  544 */       else if (this.arrowHitting == ArrowHitting.FENCE_STONE) {
/*      */         
/*  546 */         SoundPlayer.playSound("sound.work.masonry", this.tileArrowDownX, this.tileArrowDownY, this.defender.isOnSurface(), 0.0F);
/*      */       }
/*  548 */       else if (this.arrowHitting == ArrowHitting.FENCE_TREE) {
/*      */         
/*  550 */         SoundPlayer.playSound("sound.arrow.stuck.wood", this.tileArrowDownX, this.tileArrowDownY, this.defender.isOnSurface(), 0.0F);
/*      */       }
/*  552 */       else if (this.arrowHitting == ArrowHitting.GROUND) {
/*      */         
/*  554 */         SoundPlayer.playSound("sound.arrow.stuck.ground", this.tileArrowDownX, this.tileArrowDownY, this.defender.isOnSurface(), 0.0F);
/*      */       } 
/*      */       
/*  557 */       if (this.arrowDestroy == ArrowDestroy.BREAKS) {
/*      */         
/*  559 */         Items.destroyItem(this.arrow.getWurmId());
/*  560 */         if (this.performer != null)
/*      */         {
/*  562 */           this.performer.getCommunicator().sendCombatNormalMessage("The arrow breaks.");
/*      */         }
/*      */       }
/*  565 */       else if (this.arrowDestroy == ArrowDestroy.NORMAL) {
/*      */         
/*  567 */         Items.destroyItem(this.arrow.getWurmId());
/*      */       }
/*  569 */       else if (this.arrowDestroy == ArrowDestroy.WATER) {
/*      */         
/*  571 */         Items.destroyItem(this.arrow.getWurmId());
/*  572 */         if (this.performer != null)
/*      */         {
/*  574 */           this.performer.getCommunicator().sendCombatNormalMessage("The arrow disappears from your view.");
/*      */         }
/*      */       }
/*  577 */       else if (this.arrowDestroy == ArrowDestroy.NOT && this.arrowHitting != ArrowHitting.HIT) {
/*      */ 
/*      */         
/*      */         try {
/*  581 */           Zone z = Zones.getZone(this.tileArrowDownX, this.tileArrowDownY, this.defender.isOnSurface());
/*  582 */           VolaTile t = z.getOrCreateTile(this.tileArrowDownX, this.tileArrowDownY);
/*  583 */           t.addItem(this.arrow, false, false);
/*      */         }
/*  585 */         catch (NoSuchZoneException e) {
/*      */           
/*  587 */           logger.log(Level.WARNING, e.getMessage());
/*      */         } 
/*      */       } 
/*      */       
/*  591 */       return false;
/*      */     } 
/*      */     
/*  594 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean pollHitItem(float elapsedTime) {
/*  599 */     this.time += elapsedTime;
/*      */     
/*  601 */     if (this.time > this.totalTime) {
/*      */       
/*  603 */       if (this.arrowHitting == ArrowHitting.HIT) {
/*      */         
/*  605 */         SoundPlayer.playSound("sound.arrow.hit.wood", this.item, 1.6F);
/*      */         
/*  607 */         this.item.setDamage(this.item.getDamage() + 
/*  608 */             Math.max(Server.rand.nextFloat() * 0.4F, (float)(this.damage / 5000.0D) * this.item.getDamageModifier()));
/*      */         
/*  610 */         if (this.item.getTemplateId() == 458)
/*      */         {
/*  612 */           this.performer.getCommunicator().sendSafeServerMessage("You score " + this.scoreString);
/*  613 */           Server.getInstance().broadCastAction(this.performer.getName() + " scores " + this.scoreString, this.performer, 5);
/*      */         }
/*      */       
/*  616 */       } else if (this.arrowHitting == ArrowHitting.NOT) {
/*      */         
/*  618 */         SoundPlayer.playSound("sound.arrow.miss", this.item, 1.6F);
/*      */       } 
/*      */       
/*  621 */       if (this.arrowDestroy == ArrowDestroy.BREAKS) {
/*      */         
/*  623 */         Items.destroyItem(this.arrow.getWurmId());
/*  624 */         this.performer.getCommunicator().sendCombatNormalMessage("The arrow breaks.");
/*      */       }
/*  626 */       else if (this.arrowDestroy == ArrowDestroy.NORMAL) {
/*      */         
/*  628 */         Items.destroyItem(this.arrow.getWurmId());
/*      */       }
/*  630 */       else if (this.arrowDestroy == ArrowDestroy.WATER) {
/*      */         
/*  632 */         Items.destroyItem(this.arrow.getWurmId());
/*  633 */         this.performer.getCommunicator().sendCombatNormalMessage("The arrow disappears from your view.");
/*      */       }
/*  635 */       else if (this.arrowDestroy == ArrowDestroy.NOT) {
/*      */ 
/*      */         
/*      */         try {
/*  639 */           Zone z = Zones.getZone(this.tileArrowDownX, this.tileArrowDownY, this.performer.isOnSurface());
/*  640 */           VolaTile t = z.getOrCreateTile(this.tileArrowDownX, this.tileArrowDownY);
/*  641 */           t.addItem(this.arrow, false, false);
/*      */           
/*  643 */           if (this.arrowHitting == ArrowHitting.NOT)
/*      */           {
/*  645 */             SoundPlayer.playSound("sound.arrow.stuck.ground", this.tileArrowDownX, this.tileArrowDownY, this.item.isOnSurface(), 0.0F);
/*      */           }
/*      */         }
/*  648 */         catch (NoSuchZoneException e) {
/*      */           
/*  650 */           logger.log(Level.WARNING, e.getMessage());
/*      */         } 
/*      */       } 
/*      */       
/*  654 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  658 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void hitCreature(Item projectile, Item source, Creature performer, Creature defender, int damage, ArrowDestroy arrowDestroy) {
/*      */     try {
/*  665 */       arrows.add(new Arrows(projectile, performer, defender, -1, -1, ArrowHitting.HIT, arrowDestroy, source, damage, 1.0F, 0.0F, defender
/*  666 */             .getBody().getRandomWoundPos(), false, 0.0D, 0.0D));
/*      */     }
/*  668 */     catch (Exception e) {
/*      */ 
/*      */       
/*  671 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void shootCreature(Item shooter, Creature defender, int damage) {
/*  677 */     byte proj = 1;
/*  678 */     int template = 830;
/*  679 */     if (!shooter.isEnchantedTurret()) {
/*      */       
/*  681 */       SoundPlayer.playSound("sound.arrow.shot", shooter, shooter.getSizeZ() / 2.0F);
/*      */     }
/*      */     else {
/*      */       
/*  685 */       switch (shooter.getTemplateId()) {
/*      */         
/*      */         case 940:
/*  688 */           proj = 6;
/*      */           break;
/*      */         case 941:
/*  691 */           proj = 7;
/*      */           break;
/*      */         case 968:
/*  694 */           proj = 5;
/*      */           break;
/*      */         case 942:
/*  697 */           proj = 8;
/*      */           break;
/*      */         default:
/*  700 */           proj = 1;
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*      */     try {
/*  706 */       Item arrow = ItemFactory.createItem(830, 50.0F + shooter.getQualityLevel() / 2.0F, null);
/*  707 */       if (shooter.isEnchantedTurret())
/*  708 */         arrow.setName("ball of energy"); 
/*  709 */       boolean parriedShield = false;
/*  710 */       Item defShield = defender.getShield();
/*  711 */       if (defShield != null)
/*      */       {
/*  713 */         if (defender.getStatus().getStamina() >= 300) {
/*      */           
/*  715 */           Skill defShieldSkill = null;
/*  716 */           Skills defenderSkills = defender.getSkills();
/*  717 */           double defCheck = 0.0D;
/*  718 */           int skillnum = -10;
/*      */ 
/*      */           
/*      */           try {
/*  722 */             skillnum = defShield.getPrimarySkill();
/*  723 */             defShieldSkill = defenderSkills.getSkill(skillnum);
/*      */           }
/*  725 */           catch (NoSuchSkillException nss) {
/*      */             
/*  727 */             if (skillnum != -10)
/*  728 */               defShieldSkill = defenderSkills.learn(skillnum, 1.0F); 
/*      */           } 
/*  730 */           if (defShieldSkill != null) {
/*      */ 
/*      */ 
/*      */             
/*  734 */             double sdiff = Math.max(defShieldSkill.getKnowledge() - 10.0D, Math.max(20.0F, 
/*  735 */                   defender.isMoving() ? (shooter.getQualityLevel() / 2.0F + 20.0F) : (shooter.getQualityLevel() / 2.0F)));
/*  736 */             sdiff -= ((defShield.getSizeY() + defShield.getSizeZ()) / 3.0F);
/*  737 */             defCheck = defShieldSkill.skillCheck(sdiff, defShield, 0.0D, false, 1.0F);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  742 */           if (defCheck > 0.0D)
/*  743 */             parriedShield = true; 
/*  744 */           defender.getStatus().modifyStamina(-300.0F);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/*  750 */         if (parriedShield) {
/*  751 */           arrows.add(new Arrows(arrow, defender, ArrowHitting.SHIELD, ArrowDestroy.NORMAL, shooter, 0.0D, defender
/*  752 */                 .getBody().getRandomWoundPos(), proj));
/*      */         } else {
/*  754 */           arrows.add(new Arrows(arrow, defender, ArrowHitting.HIT, ArrowDestroy.NORMAL, shooter, damage, defender
/*  755 */                 .getBody().getRandomWoundPos(), proj));
/*      */         } 
/*  757 */       } catch (Exception e) {
/*      */         
/*  759 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       }
/*      */     
/*  762 */     } catch (FailedException e) {
/*      */       
/*  764 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     }
/*  766 */     catch (NoSuchTemplateException e) {
/*      */       
/*  768 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void addToHitCreature(Item arrow, Creature performer, Creature defender, float counter, Action act, int trees, Item bow, Skill bowskill, Skill archery, boolean isAttackingPenned, int tileArrowDownX, int tileArrowDownY, int treetilex, int treetiley, @Nullable Fence fence, boolean limitFail) {
/*  778 */     ArrowHitting arrowHitting = ArrowHitting.NOT;
/*  779 */     ArrowDestroy arrowDestroy = ArrowDestroy.NOT;
/*  780 */     double damage = 0.0D;
/*  781 */     float armourMod = 0.0F;
/*  782 */     byte pos = 0;
/*      */     
/*  784 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  785 */     segments.add(new CreatureLineSegment(performer));
/*  786 */     segments.add(new MulticolorLineSegment(" lets an arrow fly.", (byte)0));
/*      */     
/*  788 */     MessageServer.broadcastColoredAction(segments, performer, 5, true);
/*  789 */     ((MulticolorLineSegment)segments.get(1)).setText(" let the arrow fly.");
/*  790 */     performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */     
/*  794 */     SoundPlayer.playSound("sound.arrow.shot", performer, 1.6F);
/*      */     
/*  796 */     double diff = Archery.getBaseDifficulty(act.getNumber());
/*  797 */     if (WurmCalendar.getHour() > 19) {
/*  798 */       diff += (WurmCalendar.getHour() - 19);
/*  799 */     } else if (WurmCalendar.getHour() < 6) {
/*  800 */       diff += (6 - WurmCalendar.getHour());
/*  801 */     }  diff += trees;
/*  802 */     diff += Zones.getCoverHolder();
/*  803 */     double deviation = Archery.getRangeDifficulty(performer, bow.getTemplateId(), defender.getPosX(), defender
/*  804 */         .getPosY());
/*      */     
/*  806 */     diff += deviation;
/*  807 */     diff -= arrow.getMaterialArrowDifficulty();
/*  808 */     float damMod = arrow.getMaterialArrowDamageModifier();
/*  809 */     diff -= (bow.getRarity() * 5);
/*  810 */     diff -= (arrow.getRarity() * 5);
/*  811 */     float bon = bow.getSpellNimbleness();
/*  812 */     if (bon > 0.0F)
/*  813 */       diff -= (bon / 10.0F); 
/*  814 */     if (deviation > 5.0D)
/*  815 */       damMod *= 0.9F; 
/*  816 */     if (deviation > 10.0D)
/*  817 */       damMod *= Servers.localServer.isChallengeServer() ? 0.6F : 0.9F; 
/*  818 */     if (deviation > 20.0D) {
/*  819 */       damMod *= Servers.localServer.isChallengeServer() ? 0.4F : 0.9F;
/*      */     }
/*  821 */     int nums = 1;
/*  822 */     diff -= bow.getMaterialBowDifficulty();
/*      */ 
/*      */     
/*  825 */     diff = Math.max(1.0D, diff);
/*  826 */     if (arrow.getTemplateId() == 456) {
/*  827 */       diff += 3.0D;
/*  828 */     } else if (arrow.getTemplateId() == 454) {
/*      */       
/*  830 */       diff += 5.0D;
/*      */     } 
/*      */     
/*      */     try {
/*  834 */       Skill bcontrol = defender.getSkills().getSkill(104);
/*  835 */       diff += bcontrol.getKnowledge(0.0D) / 5.0D;
/*      */     }
/*  837 */     catch (NoSuchSkillException nss) {
/*      */       
/*  839 */       logger.log(Level.WARNING, defender.getWurmId() + ", " + defender.getName() + " no body control.");
/*      */     } 
/*      */     
/*  842 */     damage = Archery.getDamage(performer, defender, bow, arrow, archery);
/*  843 */     if (damage < 3000.0D || arrow.getTemplateId() == 454)
/*      */     {
/*  845 */       nums = 0;
/*      */     }
/*  847 */     if (defender.getPositionZ() + defender.getAltOffZ() + defender.getCentimetersHigh() / 100.0F < -1.0F) {
/*  848 */       diff += 40.0D;
/*      */     }
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
/*  860 */     diff *= 1.0D - (defender.getMovementScheme()).armourMod.getModifier();
/*      */     
/*  862 */     diff *= 1.0D + (performer.getMovementScheme()).armourMod.getModifier();
/*      */ 
/*      */     
/*  865 */     boolean dryRun = (defender.isNoSkillgain() || defender.isNoSkillFor(performer) || defender.isSentinel() || isAttackingPenned || (defender.getCitizenVillage() != null && defender.getCitizenVillage() == performer.getCitizenVillage()));
/*      */     
/*  867 */     if (defender.isPlayer() && (!defender.isPaying() || defender.isNewbie()))
/*  868 */       dryRun = true; 
/*  869 */     if (!defender.isPlayer() && (
/*  870 */       defender.getHitched() != null || defender.isRidden() || defender.getDominator() != null))
/*  871 */       dryRun = (Server.rand.nextInt(3) == 0); 
/*  872 */     float armourBonusMultiplier = 1.0F + ((performer.getArmourLimitingFactor() > 0.0F) ? performer.getArmourLimitingFactor() : 0.0F);
/*  873 */     double bonus = bowskill.skillCheck(diff, bow, arrow.getCurrentQualityLevel(), dryRun, nums);
/*      */     
/*  875 */     if (defender.isMoving())
/*      */     {
/*  877 */       if (defender.isPlayer()) {
/*  878 */         diff += 6.0D;
/*      */       } else {
/*  880 */         diff += 4.0D;
/*      */       } 
/*      */     }
/*      */     
/*  884 */     if (performer.getArmourLimitingFactor() < 0.0F)
/*      */     {
/*  886 */       diff += (Math.abs(performer.getArmourLimitingFactor()) * 20.0F);
/*      */     }
/*      */     
/*  889 */     if (act.getNumber() == 126 || act.getNumber() == 127 || act
/*  890 */       .getNumber() == 131)
/*      */     {
/*  892 */       bonus -= 30.0D;
/*      */     }
/*  894 */     if (Servers.localServer.HOMESERVER && performer.getKingdomId() != Servers.localServer.KINGDOM) {
/*  895 */       bonus -= 30.0D;
/*      */     }
/*      */ 
/*      */     
/*  899 */     double power = archery.skillCheck(diff, bow, bonus / 5.0D * armourBonusMultiplier, dryRun, nums);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  905 */     double defCheck = 0.0D;
/*  906 */     boolean parriedShield = false;
/*  907 */     if (power > 0.0D) {
/*      */       
/*  909 */       Item defShield = defender.getShield();
/*  910 */       if (defShield != null)
/*      */       {
/*  912 */         if (defender.getStatus().getStamina() >= 300)
/*      */         {
/*  914 */           if (Archery.willParryWithShield(performer, defender)) {
/*      */             
/*  916 */             Skill defShieldSkill = null;
/*  917 */             Skills defenderSkills = defender.getSkills();
/*      */             
/*  919 */             int skillnum = -10;
/*      */ 
/*      */             
/*      */             try {
/*  923 */               skillnum = defShield.getPrimarySkill();
/*  924 */               defShieldSkill = defenderSkills.getSkill(skillnum);
/*      */             }
/*  926 */             catch (NoSuchSkillException nss) {
/*      */               
/*  928 */               if (skillnum != -10)
/*  929 */                 defShieldSkill = defenderSkills.learn(skillnum, 1.0F); 
/*      */             } 
/*  931 */             if (defShieldSkill != null) {
/*      */               
/*  933 */               double sdiff = Math.max(20.0D, defender.isMoving() ? (power + 20.0D) : power);
/*  934 */               sdiff -= ((defShield.getSizeY() + defShield.getSizeZ()) * defShield.getCurrentQualityLevel() / 100.0F / 10.0F);
/*  935 */               defCheck = defShieldSkill.skillCheck(sdiff, defShield, 0.0D, dryRun, 1.0F);
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  940 */             if (defCheck > 0.0D)
/*  941 */               parriedShield = true; 
/*  942 */             defender.getStatus().modifyStamina(-300.0F);
/*      */           } 
/*      */         }
/*      */       }
/*  946 */       defender.addAttacker(performer);
/*      */       
/*  948 */       power = Math.max(power, 5.0D);
/*  949 */       pos = Archery.getWoundPos(defender, act.getNumber());
/*  950 */       pos = (byte)CombatEngine.getRealPosition(pos);
/*  951 */       armourMod = defender.getArmourMod();
/*  952 */       float evasionChance = 0.0F;
/*      */       
/*  954 */       if (defCheck > 0.0D) {
/*      */         
/*  956 */         arrowHitting = ArrowHitting.SHIELD;
/*      */       }
/*  958 */       else if (armourMod == 1.0F || defender.isVehicle()) {
/*      */ 
/*      */         
/*      */         try {
/*  962 */           byte bodyPosition = ArmourTemplate.getArmourPosition(pos);
/*  963 */           Item armour = defender.getArmour(bodyPosition);
/*  964 */           armourMod = ArmourTemplate.calculateDR(armour, (byte)2);
/*  965 */           armour.setDamage(armour.getDamage() + (float)(damage * armourMod / 300000.0D) * armour.getDamageModifier() * 
/*  966 */               ArmourTemplate.getArmourDamageModFor(armour, (byte)2));
/*  967 */           CombatEngine.checkEnchantDestruction(arrow, armour, defender);
/*  968 */           evasionChance = ArmourTemplate.calculateArrowGlance(armour, arrow);
/*      */         }
/*  970 */         catch (NoArmourException nsi) {
/*      */ 
/*      */ 
/*      */           
/*  974 */           if (!CombatEngine.isEye(pos) || defender.isUnique()) {
/*  975 */             evasionChance = 1.0F - defender.getArmourMod();
/*      */           }
/*  977 */         } catch (NoSpaceException nsp) {
/*      */           
/*  979 */           logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + pos, (Throwable)nsp);
/*      */         } 
/*  981 */         if (defender.getBonusForSpellEffect((byte)22) > 0.0F)
/*      */         {
/*  983 */           if (armourMod >= 1.0F) {
/*  984 */             armourMod = 0.2F + (1.0F - defender.getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F;
/*      */           } else {
/*  986 */             armourMod = Math.min(armourMod, 0.2F + (1.0F - defender
/*  987 */                 .getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F);
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  996 */       if (!defender.isPlayer())
/*      */       {
/*  998 */         if (!performer.isInvulnerable())
/*  999 */           defender.setTarget(performer.getWurmId(), false); 
/*      */       }
/* 1001 */       if (defender.isUnique()) {
/*      */         
/* 1003 */         evasionChance = 0.5F;
/* 1004 */         damage *= armourMod;
/*      */       } 
/* 1006 */       boolean dropattile = false;
/* 1007 */       if (defCheck > 0.0D) {
/*      */         
/* 1009 */         dropattile = true;
/*      */       }
/* 1011 */       else if (Server.rand.nextFloat() < evasionChance) {
/*      */         
/* 1013 */         dropattile = true;
/*      */         
/* 1015 */         arrowHitting = ArrowHitting.EVASION;
/*      */       }
/* 1017 */       else if (damage > 500.0D) {
/*      */         
/* 1019 */         arrowHitting = ArrowHitting.HIT;
/*      */       }
/*      */       else {
/*      */         
/* 1023 */         dropattile = true;
/* 1024 */         arrowHitting = ArrowHitting.HIT_NO_DAMAGE;
/*      */       } 
/*      */       
/* 1027 */       if (dropattile)
/*      */       {
/* 1029 */         if (defCheck > 0.0D && parriedShield) {
/*      */           
/* 1031 */           tileArrowDownX = (defender.getCurrentTile()).tilex;
/* 1032 */           tileArrowDownY = (defender.getCurrentTile()).tiley;
/* 1033 */           float damageMod = 1.0E-5F;
/* 1034 */           if (defender.isPlayer())
/* 1035 */             defShield.setDamage(defShield.getDamage() + 
/* 1036 */                 Math.max(0.01F, 1.0E-5F * (float)damage * defShield
/* 1037 */                   .getDamageModifier())); 
/* 1038 */           if (defShield.isWood()) {
/* 1039 */             SoundPlayer.playSound("sound.arrow.hit.wood", defender, 1.6F);
/* 1040 */           } else if (defShield.isMetal()) {
/* 1041 */             SoundPlayer.playSound("sound.arrow.hit.metal", defender, 1.6F);
/*      */           } 
/* 1043 */         }  if (Server.rand.nextInt(Math.max(1, (int)arrow.getCurrentQualityLevel())) < 2)
/*      */         {
/* 1045 */           arrowDestroy = ArrowDestroy.NORMAL;
/*      */         }
/* 1047 */         else if (!arrow.setDamage(arrow.getDamage() + 5.0F * damMod))
/*      */         {
/* 1049 */           tileArrowDownX = (defender.getCurrentTile()).tilex;
/* 1050 */           tileArrowDownY = (defender.getCurrentTile()).tiley;
/*      */ 
/*      */           
/*      */           try {
/* 1054 */             Zones.getZone(tileArrowDownX, tileArrowDownY, performer.isOnSurface());
/*      */           }
/* 1056 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1058 */             arrowDestroy = ArrowDestroy.WATER;
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 1065 */     } else if (Server.rand.nextInt(Math.max(1, (int)arrow.getCurrentQualityLevel())) < 2) {
/*      */       
/* 1067 */       arrowDestroy = ArrowDestroy.BREAKS;
/* 1068 */       arrowHitting = ArrowHitting.HIT_NO_DAMAGE;
/*      */     }
/* 1070 */     else if (arrow.setDamage(arrow.getDamage() + 5.0F * damMod)) {
/*      */       
/* 1072 */       arrowDestroy = ArrowDestroy.BREAKS;
/* 1073 */       arrowHitting = ArrowHitting.HIT_NO_DAMAGE;
/*      */     }
/*      */     else {
/*      */       
/* 1077 */       if (trees > 0)
/*      */       {
/* 1079 */         if (treetilex > 0) {
/*      */           
/* 1081 */           tileArrowDownX = treetilex;
/* 1082 */           tileArrowDownY = treetiley;
/*      */         } 
/*      */       }
/* 1085 */       boolean hitdef = false;
/* 1086 */       if (tileArrowDownX == -1) {
/*      */         
/* 1088 */         if (defender.opponent != null && performer.getKingdomId() == defender.opponent.getKingdomId())
/*      */         {
/* 1090 */           if (power < -20.0D && Server.rand.nextInt(100) < Math.abs(power))
/*      */           {
/* 1092 */             if (defender.opponent.isPlayer() && defender.opponent != performer) {
/*      */               
/* 1094 */               pos = Archery.getWoundPos(defender.opponent, act.getNumber());
/* 1095 */               pos = (byte)CombatEngine.getRealPosition(pos);
/* 1096 */               armourMod = defender.opponent.getArmourMod();
/* 1097 */               damage = Archery.getDamage(performer, defender, bow, arrow, archery);
/* 1098 */               if (armourMod == 1.0F || defender.isVehicle()) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1102 */                   byte bodyPosition = ArmourTemplate.getArmourPosition(pos);
/* 1103 */                   Item armour = defender.opponent.getArmour(bodyPosition);
/* 1104 */                   armourMod = ArmourTemplate.calculateDR(armour, (byte)2);
/*      */ 
/*      */ 
/*      */                   
/* 1108 */                   armour.setDamage(armour.getDamage() + (float)(damage * armourMod / 300000.0D) * armour
/* 1109 */                       .getDamageModifier() * 
/* 1110 */                       ArmourTemplate.getArmourDamageModFor(armour, (byte)2));
/* 1111 */                   CombatEngine.checkEnchantDestruction(arrow, armour, defender.opponent);
/*      */                 }
/* 1113 */                 catch (NoArmourException noArmourException) {
/*      */ 
/*      */                 
/*      */                 }
/* 1117 */                 catch (NoSpaceException nsp) {
/*      */                   
/* 1119 */                   logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + pos);
/*      */                 } 
/*      */               }
/* 1122 */               arrowHitting = ArrowHitting.HIT;
/* 1123 */               hitdef = true;
/*      */             } 
/*      */           }
/*      */         }
/* 1127 */         if (!hitdef) {
/*      */           
/* 1129 */           tileArrowDownX = (defender.getCurrentTile()).tilex;
/* 1130 */           tileArrowDownY = (defender.getCurrentTile()).tiley;
/*      */         } 
/*      */       } 
/* 1133 */       if (!hitdef) {
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/* 1139 */           Zones.getZone(tileArrowDownX, tileArrowDownY, performer.isOnSurface());
/* 1140 */           if (treetilex > 0) {
/*      */             
/* 1142 */             arrowHitting = ArrowHitting.TREE;
/*      */           }
/* 1144 */           else if (fence != null) {
/*      */             
/* 1146 */             if (fence.isStone())
/*      */             {
/* 1148 */               arrowHitting = ArrowHitting.FENCE_STONE;
/*      */             }
/*      */             else
/*      */             {
/* 1152 */               arrowHitting = ArrowHitting.FENCE_TREE;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1157 */             arrowHitting = ArrowHitting.GROUND;
/*      */           }
/*      */         
/* 1160 */         } catch (NoSuchZoneException nsz) {
/*      */           
/* 1162 */           arrowDestroy = ArrowDestroy.WATER;
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1168 */     arrows.add(new Arrows(arrow, performer, defender, tileArrowDownX, tileArrowDownY, arrowHitting, arrowDestroy, bow, damage, damMod, armourMod, pos, dryRun, diff, bonus));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addToHitItem(Item arrow, Creature performer, Item target, float counter, Skill bowskill, Item bow, int tileArrowDownX, int tileArrowDownY, double deviation, double diff, int trees, int treetilex, int treetiley) {
/* 1176 */     ArrowHitting arrowHitting = ArrowHitting.NOT;
/* 1177 */     ArrowDestroy arrowDestroy = ArrowDestroy.NOT;
/* 1178 */     String scoreString = "outside the rings.";
/*      */     
/* 1180 */     Server.getInstance().broadCastAction(performer.getName() + " lets an arrow fly.", performer, 5);
/* 1181 */     performer.getCommunicator().sendNormalServerMessage("You let the arrow fly.");
/*      */     
/* 1183 */     SoundPlayer.playSound("sound.arrow.aim", performer, 1.0F);
/* 1184 */     diff -= arrow.getMaterialArrowDifficulty();
/* 1185 */     float damMod = arrow.getMaterialArrowDamageModifier();
/* 1186 */     if (deviation > 5.0D)
/* 1187 */       damMod *= 0.9F; 
/* 1188 */     if (deviation > 10.0D)
/* 1189 */       damMod *= 0.9F; 
/* 1190 */     if (deviation > 20.0D) {
/* 1191 */       damMod *= 0.9F;
/*      */     }
/* 1193 */     diff -= bow.getMaterialBowDifficulty();
/* 1194 */     diff = Math.max(1.0D, diff);
/* 1195 */     if (arrow.getTemplateId() == 456) {
/* 1196 */       diff += 3.0D;
/* 1197 */     } else if (arrow.getTemplateId() == 454) {
/* 1198 */       diff += 5.0D;
/* 1199 */     }  boolean dryrun = false;
/* 1200 */     if (bowskill.getRealKnowledge() >= 30.0D) {
/*      */       
/* 1202 */       if (Server.rand.nextInt(10) == 0)
/* 1203 */         performer.getCommunicator().sendNormalServerMessage("You don't learn anything from this type of shooting any more."); 
/* 1204 */       dryrun = true;
/*      */     } 
/* 1206 */     if (target.getTemplateId() != 458 && !target.isBoat())
/*      */     {
/* 1208 */       dryrun = true;
/*      */     }
/* 1210 */     Skill archery = null;
/*      */     
/*      */     try {
/* 1213 */       archery = performer.getSkills().getSkill(1030);
/*      */     }
/* 1215 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1217 */       archery = performer.getSkills().learn(1030, 1.0F);
/*      */     } 
/* 1219 */     if (archery.getKnowledge() >= 40.0D) {
/*      */       
/* 1221 */       if (!dryrun && Server.rand.nextInt(10) == 0)
/* 1222 */         performer.getCommunicator().sendNormalServerMessage("You don't learn anything from this type of shooting any more."); 
/* 1223 */       dryrun = true;
/*      */     } 
/* 1225 */     double bonus = bowskill.skillCheck(diff, bow, arrow.getCurrentQualityLevel(), dryrun, counter);
/*      */     
/* 1227 */     double power = archery.skillCheck(diff, bow, bonus, dryrun, counter);
/*      */     
/* 1229 */     if (target.isBoat() && target.getDamage() > 50.0F) {
/*      */       
/* 1231 */       power = 0.0D;
/* 1232 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is too hard to hit because of existing damage.");
/*      */     } 
/* 1234 */     if (power > 0.0D) {
/*      */       
/* 1236 */       power = Math.max(power, 5.0D);
/* 1237 */       double damage = (bow.getDamagePercent() * bow.getCurrentQualityLevel() / 100.0F);
/* 1238 */       damage *= 1.0D + (performer.getStrengthSkill() - 20.0D) / 100.0D;
/* 1239 */       damage *= (1.0F + arrow.getCurrentQualityLevel() / 100.0F);
/* 1240 */       if (arrow.getTemplateId() == 456) {
/* 1241 */         damage *= 1.2000000476837158D;
/* 1242 */       } else if (arrow.getTemplateId() == 454) {
/* 1243 */         damage *= 0.30000001192092896D;
/*      */       } 
/* 1245 */       target.setDamage((float)(target.getDamage() + damage / 1000000.0D));
/* 1246 */       boolean dropattile = false;
/* 1247 */       if (target.getTemplateId() != 458) {
/*      */         
/* 1249 */         performer.getCommunicator().sendSafeServerMessage("Your arrow hits the " + target.getName() + ".");
/* 1250 */         Server.getInstance().broadCastAction("An arrow hits " + target.getNameWithGenus() + ".", performer, 5);
/*      */       }
/*      */       else {
/*      */         
/* 1254 */         scoreString = "outside the rings.";
/* 1255 */         int points = (int)(power / 10.0D);
/* 1256 */         if (points == 10) {
/*      */           
/* 1258 */           scoreString = "a perfect ten!";
/*      */         }
/* 1260 */         else if (points == 9) {
/*      */           
/* 1262 */           scoreString = "a fine 9!";
/*      */         }
/* 1264 */         else if (points == 8) {
/*      */           
/* 1266 */           scoreString = "a skilled 8.";
/*      */         }
/* 1268 */         else if (points > 2) {
/*      */           
/* 1270 */           scoreString = "a " + points + ".";
/*      */         } else {
/*      */           
/* 1273 */           scoreString = "a measly " + points + ".";
/*      */         } 
/*      */       } 
/* 1276 */       arrowHitting = ArrowHitting.HIT;
/*      */       
/* 1278 */       if (Server.rand.nextInt(Math.max(1, (int)arrow.getCurrentQualityLevel())) < 2) {
/*      */         
/* 1280 */         arrowDestroy = ArrowDestroy.BREAKS;
/*      */       }
/* 1282 */       else if (!arrow.setDamage(arrow.getDamage() + 5.0F * damMod)) {
/*      */         
/* 1284 */         if (target.isHollow()) {
/* 1285 */           target.insertItem(arrow, true);
/*      */         } else {
/* 1287 */           dropattile = true;
/*      */         } 
/* 1289 */       }  if (dropattile) {
/*      */         try
/*      */         {
/*      */           
/* 1293 */           tileArrowDownX = target.getTileX();
/* 1294 */           tileArrowDownY = target.getTileY();
/* 1295 */           Zones.getZone(tileArrowDownX, tileArrowDownY, performer.isOnSurface());
/*      */         }
/* 1297 */         catch (NoSuchZoneException nsz)
/*      */         {
/* 1299 */           arrowDestroy = ArrowDestroy.WATER;
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/* 1305 */       arrowHitting = ArrowHitting.NOT;
/*      */       
/* 1307 */       if (Server.rand.nextInt(Math.max(1, (int)arrow.getCurrentQualityLevel())) < 2) {
/*      */         
/* 1309 */         arrowDestroy = ArrowDestroy.BREAKS;
/*      */       }
/* 1311 */       else if (arrow.setDamage(arrow.getDamage() + 5.0F * damMod)) {
/*      */         
/* 1313 */         arrowDestroy = ArrowDestroy.BREAKS;
/*      */       }
/*      */       else {
/*      */         
/* 1317 */         if (trees > 0) {
/*      */           
/* 1319 */           if (treetilex > 0)
/*      */           {
/* 1321 */             tileArrowDownX = treetilex;
/* 1322 */             tileArrowDownY = treetiley;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1327 */           tileArrowDownX = target.getTileX();
/* 1328 */           tileArrowDownY = target.getTileY();
/*      */         } 
/*      */         
/*      */         try {
/* 1332 */           Zones.getZone(tileArrowDownX, tileArrowDownY, performer.isOnSurface());
/*      */         }
/* 1334 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 1336 */           arrowDestroy = ArrowDestroy.WATER;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1341 */     arrows.add(new Arrows(arrow, performer, target, tileArrowDownX, tileArrowDownY, bow, arrowHitting, arrowDestroy, scoreString));
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\Arrows.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */