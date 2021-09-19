/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.math.Vector2f;
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.PlonkData;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.bodys.Body;
/*      */ import com.wurmonline.server.bodys.BodyFactory;
/*      */ import com.wurmonline.server.combat.CombatConstants;
/*      */ import com.wurmonline.server.creatures.ai.Order;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.Trade;
/*      */ import com.wurmonline.server.modifiers.DoubleValueModifier;
/*      */ import com.wurmonline.server.modifiers.ModifierTypes;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.SpellResistance;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import java.io.IOException;
/*      */ import java.util.BitSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CreatureStatus
/*      */   implements MiscConstants, CombatConstants, TimeConstants, ModifierTypes, ProtoConstants, CreatureTypes
/*      */ {
/*      */   Creature statusHolder;
/*   88 */   private static Logger logger = Logger.getLogger(CreatureStatus.class.getName());
/*      */   private static final int FAT_INCREASE_LEVEL = 120;
/*      */   private static final int FAT_DECREASE_LEVEL = 1;
/*   91 */   private CreaturePos position = null;
/*      */   
/*   93 */   private byte diseaseCounter = 0;
/*      */ 
/*      */   
/*      */   boolean moving = false;
/*      */ 
/*      */   
/*      */   private boolean unconscious = false;
/*      */ 
/*      */   
/*      */   public boolean visible = true;
/*      */   
/*  104 */   private Trade trade = null;
/*      */ 
/*      */   
/*      */   CreatureTemplate template;
/*      */   
/*  109 */   byte sex = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  117 */   long inventoryId = -10L;
/*      */ 
/*      */ 
/*      */   
/*  121 */   long bodyId = -10L;
/*      */ 
/*      */ 
/*      */   
/*      */   Body body;
/*      */ 
/*      */   
/*      */   int thirst;
/*      */ 
/*      */   
/*      */   int hunger;
/*      */ 
/*      */   
/*  134 */   int stamina = 65535;
/*      */   
/*  136 */   float nutrition = 0.0F;
/*  137 */   private static int DAILY_CALORIES = 2000;
/*  138 */   private static int DAILY_CARBS = 300;
/*  139 */   private static int DAILY_FATS = 80;
/*  140 */   private static int DAILY_PROTEINS = 50;
/*  141 */   private static float CCFP_MAX_PERCENTAGE = 1.0F;
/*  142 */   private static float CCFP_REDUCE_AMOUNT = 3.3333334E-5F;
/*      */   
/*  144 */   float calories = 0.25F;
/*  145 */   float carbs = 0.25F;
/*  146 */   float fats = 0.25F;
/*  147 */   float proteins = 0.25F;
/*      */ 
/*      */   
/*      */   public int damage;
/*      */   
/*  152 */   long buildingId = -10L;
/*  153 */   private int lastSentThirst = 0;
/*  154 */   private int lastSentHunger = 0;
/*  155 */   public int lastSentStamina = 0;
/*  156 */   private int lastSentDamage = 0;
/*      */ 
/*      */   
/*      */   private boolean normalRegen = true;
/*      */   
/*  161 */   private float stunned = 0.0F;
/*  162 */   private Set<DoubleValueModifier> modifiers = null;
/*  163 */   private Path path = null;
/*      */   public static final int MOVE_MOD_LIMIT = 2000;
/*  165 */   private final DoubleValueModifier moveMod = new DoubleValueModifier(-0.5D);
/*      */   
/*  167 */   public byte kingdom = 0;
/*      */   boolean dead = false;
/*  169 */   public long lastPolledAge = 0L;
/*  170 */   public int age = 0;
/*  171 */   public byte fat = 50;
/*  172 */   private int fatCounter = Server.rand.nextInt(1000);
/*  173 */   public SpellEffects spellEffects = null;
/*      */   boolean reborn = false;
/*  175 */   public float loyalty = 0.0F;
/*  176 */   long lastPolledLoyalty = 0L;
/*      */   
/*      */   boolean stealth = false;
/*      */   
/*      */   boolean offline = false;
/*      */   
/*      */   boolean stayOnline = false;
/*  183 */   protected int detectInvisCounter = 0;
/*  184 */   byte modtype = 0;
/*  185 */   private float lastDamPercSent = -1.0F;
/*  186 */   protected long traits = 0L;
/*  187 */   protected BitSet traitbits = new BitSet(64);
/*      */   
/*  189 */   protected long mother = -10L;
/*  190 */   protected long father = -10L;
/*      */   
/*  192 */   public byte disease = 0;
/*  193 */   protected long lastGroomed = System.currentTimeMillis();
/*      */   
/*      */   private boolean statusExists = false;
/*      */   
/*      */   private boolean changed = false;
/*      */   
/*      */   CreatureStatus(Creature creature, float posX, float posY, float aRot, int aLayer) throws Exception {
/*  200 */     this.statusHolder = creature;
/*  201 */     this.template = creature.template;
/*  202 */     if (this.template != null) {
/*  203 */       this.body = BodyFactory.getBody(creature, this.template.getBodyType(), this.template.getCentimetersHigh(), this.template
/*  204 */           .getCentimetersLong(), this.template.getCentimetersWide());
/*      */     }
/*  206 */     setPosition(CreaturePos.getPosition(creature.getWurmId()));
/*  207 */     if (getPosition() == null) {
/*      */       
/*  209 */       int zid = 0;
/*      */       
/*      */       try {
/*  212 */         zid = Zones.getZoneIdFor((int)posX >> 2, (int)posY >> 2, (aLayer >= 0));
/*      */       }
/*  214 */       catch (NoSuchZoneException ex) {
/*      */         
/*  216 */         logger.log(Level.INFO, this.statusHolder.getWurmId() + "," + this.statusHolder.getName() + ": " + ex.getMessage(), (Throwable)ex);
/*      */       } 
/*  218 */       setPosition(new CreaturePos(creature.getWurmId(), posX, posY, 0.0F, aRot, zid, aLayer, -10L, true));
/*      */     } 
/*      */     
/*  221 */     if (this.template != null) {
/*      */       
/*  223 */       getPosition()
/*  224 */         .setPosZ(Zones.calculateHeight(getPosition().getPosX(), getPosition().getPosY(), isOnSurface()), false);
/*  225 */       if (isOnSurface())
/*      */       {
/*  227 */         if (!creature.isSubmerged()) {
/*  228 */           getPosition().setPosZ(Math.max(-1.25F, getPosition().getPosZ()), false);
/*  229 */         } else if (getPosition().getPosZ() < 0.0F) {
/*      */           
/*  231 */           if (creature.isFloating()) {
/*  232 */             getPosition().setPosZ((creature.getTemplate()).offZ, false);
/*      */           } else {
/*  234 */             getPosition().setPosZ(getPosition().getPosZ() / 2.0F, false);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   CreatureStatus() {}
/*      */ 
/*      */   
/*      */   public final void addModifier(DoubleValueModifier modifier) {
/*  247 */     if (this.modifiers == null)
/*  248 */       this.modifiers = new HashSet<>(); 
/*  249 */     this.modifiers.add(modifier);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void createNewBody() throws Exception {
/*  254 */     this.body = BodyFactory.getBody(this.statusHolder, this.template.getBodyType(), this.template.getCentimetersHigh(), this.template
/*  255 */         .getCentimetersLong(), this.template.getCentimetersWide());
/*  256 */     this.body.createBodyParts();
/*  257 */     Item bodypart = this.body.getBodyItem();
/*  258 */     this.bodyId = bodypart.getWurmId();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void createNewPossessions() throws Exception {
/*  263 */     this.inventoryId = this.statusHolder.createPossessions();
/*  264 */     setChanged(true);
/*  265 */     logger.log(Level.INFO, "New inventory id for " + this.statusHolder.getName() + " is " + this.inventoryId);
/*  266 */     setInventoryId(this.inventoryId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Path getPath() {
/*  275 */     return this.path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPath(@Nullable Path newPath) {
/*  284 */     if (newPath == null) {
/*      */       
/*  286 */       if (this.statusHolder.isDominated())
/*      */       {
/*  288 */         if (this.statusHolder.hasOrders()) {
/*      */           
/*  290 */           Order order = this.statusHolder.getFirstOrder();
/*  291 */           if (order.isTile())
/*      */           {
/*  293 */             if (order.isResolved((this.statusHolder.getCurrentTile()).tilex, (this.statusHolder.getCurrentTile()).tiley, this.statusHolder
/*  294 */                 .getLayer()))
/*  295 */               this.statusHolder.removeOrder(order); 
/*      */           }
/*      */         } 
/*      */       }
/*  299 */       if (this.path != null)
/*  300 */         this.path.clear(); 
/*  301 */       this.statusHolder.pathRecalcLength = 0;
/*      */     } 
/*  303 */     this.path = newPath;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeModifier(DoubleValueModifier modifier) {
/*  308 */     if (this.modifiers != null) {
/*  309 */       this.modifiers.remove(modifier);
/*      */     }
/*      */   }
/*      */   
/*      */   final double getModifierValuesFor(int type) {
/*  314 */     double toReturn = 0.0D;
/*  315 */     if (this.modifiers != null)
/*      */     {
/*  317 */       for (DoubleValueModifier val : this.modifiers) {
/*      */         
/*  319 */         if (val.getType() == type)
/*  320 */           toReturn += val.getModifier(); 
/*      */       } 
/*      */     }
/*  323 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private void increaseFat() {
/*  328 */     if (this.fat < 125) {
/*      */       
/*  330 */       setChanged(true);
/*  331 */       this.fat = (byte)Math.min(125, this.fat + 1);
/*  332 */       if (this.statusHolder.isPlayer())
/*      */       {
/*  334 */         if (this.fat == 125)
/*  335 */           this.statusHolder.achievement(149); 
/*      */       }
/*  337 */       if ((this.fat == 120 || this.fat == 1) && !this.statusHolder.isPlayer())
/*      */       {
/*  339 */         this.statusHolder.refreshVisible();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int checkDisease() {
/*  350 */     int nums = (this.statusHolder.getCurrentTile().getCreatures()).length;
/*  351 */     if (this.diseaseCounter == 8) {
/*      */       
/*  353 */       float villRatio = 10.0F;
/*  354 */       if (this.statusHolder.getCurrentVillage() != null)
/*      */       {
/*  356 */         villRatio = this.statusHolder.getCurrentVillage().getCreatureRatio();
/*      */       }
/*  358 */       if (nums < 3 && (villRatio >= Village.OPTIMUMCRETRATIO || Server.rand
/*  359 */         .nextInt((int)Math.max(1.0F, Village.OPTIMUMCRETRATIO - villRatio)) == 0)) {
/*      */ 
/*      */         
/*  362 */         if (this.disease > 0)
/*      */         {
/*  364 */           byte mod = 2;
/*  365 */           if (System.currentTimeMillis() - this.lastGroomed < 172800000L)
/*  366 */             mod = 5; 
/*  367 */           if (this.hunger < 10000)
/*  368 */             mod = (byte)(mod + 5); 
/*  369 */           if (this.fat > 100)
/*  370 */             mod = (byte)(mod + 5); 
/*  371 */           if (this.fat < 2)
/*  372 */             mod = (byte)(int)(mod - 1.0F); 
/*  373 */           this.statusHolder.setDisease((byte)Math.max(0, this.disease - mod));
/*      */         }
/*      */       
/*      */       }
/*  377 */       else if (this.modtype != 11) {
/*      */         
/*  379 */         if (!this.statusHolder.isKingdomGuard() && !this.statusHolder.isSpiritGuard() && 
/*  380 */           !this.statusHolder.isUnique() && (
/*  381 */           !this.statusHolder.isPlayer() || Server.rand.nextInt(4) == 0)) {
/*      */           
/*  383 */           float healthMod = 1.0F;
/*  384 */           if (isTraitBitSet(19))
/*  385 */             healthMod++; 
/*  386 */           if (isTraitBitSet(20))
/*  387 */             healthMod -= 0.5F; 
/*  388 */           if (System.currentTimeMillis() - this.lastGroomed < 172800000L)
/*  389 */             healthMod -= 0.3F; 
/*  390 */           if (this.hunger < 10000) {
/*  391 */             healthMod -= 0.1F;
/*  392 */           } else if (this.hunger > 60000) {
/*  393 */             healthMod += 0.1F;
/*  394 */           }  if (this.fat > 100)
/*  395 */             healthMod -= 0.1F; 
/*  396 */           if (this.fat < 2)
/*  397 */             healthMod += 0.1F; 
/*  398 */           if (this.disease > 0) {
/*  399 */             this.statusHolder.setDisease((byte)(int)Math.min(120.0F, this.disease + (5 * nums) * healthMod));
/*  400 */           } else if (nums > 2 && Server.rand.nextInt(100) < nums * healthMod) {
/*  401 */             this.statusHolder.setDisease((byte)1);
/*      */           } 
/*      */         } 
/*      */       } 
/*  405 */     }  return nums;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void decreaseCCFPValues() {
/*  414 */     this.calories = Math.max(this.calories - CCFP_REDUCE_AMOUNT, 0.0F);
/*  415 */     this.carbs = Math.max(this.carbs - CCFP_REDUCE_AMOUNT, 0.0F);
/*  416 */     this.fats = Math.max(this.fats - CCFP_REDUCE_AMOUNT, 0.0F);
/*  417 */     this.proteins = Math.max(this.proteins - CCFP_REDUCE_AMOUNT, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void clearCCFPValues() {
/*  422 */     this.calories = 0.0F;
/*  423 */     this.carbs = 0.0F;
/*  424 */     this.fats = 0.0F;
/*  425 */     this.proteins = 0.0F;
/*  426 */     sendHunger();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void clearHunger() {
/*  431 */     this.hunger = 45000;
/*  432 */     sendHunger();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void clearThirst() {
/*  437 */     this.thirst = 65535;
/*  438 */     sendThirst();
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean decreaseFat() {
/*  443 */     int originalfat = this.fat;
/*  444 */     int nums = checkDisease();
/*      */ 
/*      */     
/*  447 */     if (this.fat > 3 && this.fat < 121) {
/*      */       
/*  449 */       byte newfatMod = -2;
/*      */       
/*  451 */       if (nums > 2)
/*  452 */         newfatMod = (byte)(newfatMod - 2); 
/*  453 */       if (System.currentTimeMillis() - this.lastGroomed > 172800000L)
/*  454 */         newfatMod = (byte)(newfatMod - 3); 
/*  455 */       if (this.modtype != 11 && this.disease > 0) {
/*  456 */         newfatMod = (byte)(newfatMod - 2);
/*      */       }
/*      */       
/*  459 */       this.fat = (byte)Math.max(0, this.fat + newfatMod);
/*  460 */       if (this.statusHolder.isPlayer())
/*      */       {
/*  462 */         if (this.fat == 0) {
/*  463 */           this.statusHolder.achievement(148);
/*      */         }
/*      */       }
/*  466 */       this.statusHolder.achievement(147);
/*      */     }
/*      */     else {
/*      */       
/*  470 */       this.fat = (byte)Math.max(0, this.fat - 1);
/*  471 */       if (this.statusHolder.isPlayer())
/*      */       {
/*  473 */         if (this.fat == 0) {
/*  474 */           this.statusHolder.achievement(148);
/*      */         }
/*      */       }
/*      */     } 
/*  478 */     if ((this.fat == 120 || this.fat == 1) && !this.statusHolder.isPlayer())
/*      */     {
/*  480 */       this.statusHolder.refreshVisible();
/*      */     }
/*  482 */     if (originalfat != this.fat)
/*  483 */       setChanged(true); 
/*  484 */     if (this.fat == 0)
/*  485 */       return false; 
/*  486 */     return true;
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
/*      */   public final boolean pollFat() {
/*  500 */     if (Server.rand.nextBoolean()) {
/*  501 */       this.fatCounter++;
/*      */     }
/*  503 */     if (this.fatCounter > (this.statusHolder.isPlayer() ? 50 : 1500)) {
/*      */       
/*  505 */       this.diseaseCounter = (byte)(this.diseaseCounter + 1);
/*      */       
/*  507 */       this.fatCounter = 0;
/*  508 */       if (this.hunger < 10000 && this.nutrition > (this.statusHolder.isPlayer() ? 0.4F : 0.1F)) {
/*      */         
/*  510 */         increaseFat();
/*  511 */         checkDisease();
/*      */       }
/*  513 */       else if (this.hunger >= 60000) {
/*      */ 
/*      */         
/*  516 */         decreaseFat();
/*      */         
/*  518 */         if (!this.statusHolder.isPlayer()) {
/*      */           
/*  520 */           if ((this.statusHolder.isHerbivore() || this.statusHolder.isOmnivore()) && !this.statusHolder.isUnique()) {
/*      */             
/*  522 */             if (this.fat < 1 && !this.statusHolder.isInvulnerable()) {
/*  523 */               return true;
/*      */             }
/*  525 */             this.hunger = 10000;
/*      */           }
/*  527 */           else if (Server.rand.nextInt(10) == 0) {
/*      */             
/*  529 */             this.hunger = 10000;
/*      */           } 
/*  531 */         } else if (this.fat > 0) {
/*      */           
/*  533 */           this.statusHolder.getCommunicator().sendNormalServerMessage("Your hunger goes away as you fast.");
/*  534 */           this.nutrition = Math.max(0.1F, this.nutrition - 0.2F);
/*  535 */           this.hunger = 10000;
/*      */         } 
/*      */       } else {
/*      */         
/*  539 */         checkDisease();
/*  540 */       }  if (this.diseaseCounter == 8 && this.disease > 50) {
/*      */         
/*  542 */         boolean canSpread = (this.statusHolder.getHitched() == null || !this.statusHolder.isMoving());
/*  543 */         if (canSpread) {
/*  544 */           this.statusHolder.getCurrentTile().checkDiseaseSpread();
/*      */         }
/*      */         
/*  547 */         if (this.disease > 100 && Server.rand.nextBoolean() && !this.statusHolder.isInvulnerable())
/*  548 */           return true; 
/*      */       } 
/*  550 */       if (this.diseaseCounter == 8)
/*  551 */         this.diseaseCounter = 0; 
/*      */     } 
/*  553 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final String getFatString() {
/*  559 */     if (this.disease > 0 && this.modtype != 11)
/*  560 */       return "diseased "; 
/*  561 */     if (this.statusHolder.isNeedFood()) {
/*      */       
/*  563 */       if (this.fat <= 1)
/*  564 */         return "starving "; 
/*  565 */       if (this.fat > 120)
/*  566 */         return "fat "; 
/*      */     } 
/*  568 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getBodyType() {
/*  573 */     double strength = this.statusHolder.getStrengthSkill();
/*  574 */     if (strength < 21.0D) {
/*      */       
/*  576 */       if (this.fat < 10)
/*  577 */         return this.statusHolder.getHeSheItString() + " is only skin and bones."; 
/*  578 */       if (this.fat < 30)
/*  579 */         return this.statusHolder.getHeSheItString() + " is very thin."; 
/*  580 */       if (this.fat > 100)
/*  581 */         return this.statusHolder.getHeSheItString() + " is extremely well nourished."; 
/*  582 */       if (this.fat > 70) {
/*  583 */         return this.statusHolder.getHeSheItString() + " is a bit round.";
/*      */       }
/*  585 */       return this.statusHolder.getHeSheItString() + " has a normal build.";
/*      */     } 
/*  587 */     if (strength < 25.0D) {
/*      */       
/*  589 */       if (this.fat < 10)
/*  590 */         return this.statusHolder.getHeSheItString() + " has muscles, but looks a bit undernourished."; 
/*  591 */       if (this.fat < 30)
/*  592 */         return this.statusHolder.getHeSheItString() + " is strong but lacks body fat."; 
/*  593 */       if (this.fat > 100)
/*  594 */         return this.statusHolder.getHeSheItString() + " is strong and has a good reserve of fat."; 
/*  595 */       if (this.fat > 70) {
/*  596 */         return this.statusHolder.getHeSheItString() + " is strong and well nourished.";
/*      */       }
/*  598 */       return this.statusHolder.getHeSheItString() + " is well defined.";
/*      */     } 
/*      */ 
/*      */     
/*  602 */     if (this.fat < 10)
/*  603 */       return this.statusHolder.getHeSheItString() + " has large muscles, but looks a bit undernourished."; 
/*  604 */     if (this.fat < 30)
/*  605 */       return this.statusHolder.getHeSheItString() + " looks very strong, but lacks body fat."; 
/*  606 */     if (this.fat > 100)
/*  607 */       return this.statusHolder.getHeSheItString() + " is very strong and has a good reserve of fat."; 
/*  608 */     if (this.fat > 70) {
/*  609 */       return this.statusHolder.getHeSheItString() + " is very strong and well nourished.";
/*      */     }
/*  611 */     return this.statusHolder.getHeSheItString() + " is very well defined.";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLayer(int aLayer) {
/*  621 */     getPosition().setLayer(aLayer);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOnSurface() {
/*  626 */     return (getPosition().getLayer() >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CreatureTemplate getTemplate() {
/*  635 */     return this.template;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getSex() {
/*  644 */     return this.sex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBuildingId(long id) {
/*  654 */     this.buildingId = id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getBuildingId() {
/*  663 */     return this.buildingId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Body getBody() {
/*  672 */     return this.body;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSex(byte aSex) {
/*  681 */     this.sex = aSex;
/*  682 */     setChanged(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPositionX(float pos) {
/*  691 */     getPosition().setPosX(pos);
/*  692 */     this.statusHolder.updateEffects();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPositionY(float pos) {
/*  701 */     getPosition().setPosY(pos);
/*  702 */     this.statusHolder.updateEffects();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setPositionZ(float pos) {
/*  707 */     setPositionZ(pos, false);
/*  708 */     this.statusHolder.updateEffects();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPositionZ(float pos, boolean force) {
/*  717 */     getPosition().setPosZ(pos, force);
/*  718 */     this.statusHolder.updateEffects();
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
/*      */   public final void setPositionXYZ(float posX, float posY, float posZ) {
/*  733 */     getPosition().setPosX(posX);
/*  734 */     getPosition().setPosY(posY);
/*  735 */     getPosition().setPosZ(posZ, false);
/*  736 */     this.statusHolder.updateEffects();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setRotation(float r) {
/*  741 */     getPosition().setRotation(Creature.normalizeAngle(r));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMoving() {
/*  750 */     return this.moving;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMoving(boolean aMoving) {
/*  759 */     this.moving = aMoving;
/*  760 */     if (aMoving) {
/*  761 */       PlayerTutorial.firePlayerTrigger(this.statusHolder.getWurmId(), PlayerTutorial.PlayerTrigger.MOVED_PLAYER);
/*      */     }
/*      */   }
/*      */   
/*      */   public final boolean hasNormalRegen() {
/*  766 */     return this.normalRegen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNormalRegen(boolean nregen) {
/*  775 */     this.normalRegen = nregen;
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
/*      */   public final void setUnconscious(boolean aUnconscious) {
/*  787 */     this.unconscious = aUnconscious;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isUnconscious() {
/*  796 */     return this.unconscious;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Vector2f getPosition2f() {
/*  801 */     return getPosition().getPos2f();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Vector3f getPosition3f() {
/*  806 */     return getPosition().getPos3f();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionX() {
/*  815 */     return getPosition().getPosX();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionY() {
/*  824 */     return getPosition().getPosY();
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
/*      */   public final long getInventoryId() {
/*  836 */     return this.inventoryId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionZ() {
/*  845 */     return getPosition().getPosZ();
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getRotation() {
/*  850 */     return getPosition().getRotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getBridgeId() {
/*  855 */     return getPosition().getBridgeId();
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getDir() {
/*  860 */     return (byte)(((int)getRotation() + 45) / 90 * 2 % 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getBodyId() {
/*  869 */     return this.bodyId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getStunned() {
/*  878 */     return this.stunned;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setStunned(float stunTime) {
/*  883 */     setStunned(stunTime, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setStunned(float stunTime, boolean applyResistance) {
/*  892 */     if (this.stunned > 0.0F && stunTime <= 0.0F) {
/*      */       
/*  894 */       this.statusHolder.getCombatHandler().setCurrentStance(-1, (byte)0);
/*  895 */       this.statusHolder.getMovementScheme().setFightMoveMod(false);
/*  896 */       if (!this.statusHolder.isDead()) {
/*  897 */         Server.getInstance().broadCastAction(this.statusHolder
/*  898 */             .getNameWithGenus() + " regains " + this.statusHolder.getHisHerItsString() + " bearings.", this.statusHolder, 2, true);
/*      */       }
/*      */       
/*  901 */       this.statusHolder.getCommunicator().sendStunned(false);
/*  902 */       this.statusHolder.getCommunicator().sendRemoveSpellEffect(SpellEffectsEnum.STUNNED);
/*  903 */       this.stunned = Math.max(0.0F, stunTime);
/*      */ 
/*      */     
/*      */     }
/*  907 */     else if (this.stunned <= 0.0F && stunTime > 0.0F) {
/*      */       
/*  909 */       SpellResistance currSpellRes = null;
/*  910 */       if (applyResistance)
/*  911 */         currSpellRes = this.statusHolder.getSpellResistance((short)SpellEffectsEnum.STUNNED.getTypeId()); 
/*  912 */       float successChance = 1.0F;
/*  913 */       if (applyResistance && this.statusHolder.isPlayer())
/*      */       {
/*  915 */         if (currSpellRes == null) {
/*      */           
/*  917 */           this.statusHolder.addSpellResistance((short)SpellEffectsEnum.STUNNED.getTypeId());
/*  918 */           currSpellRes = this.statusHolder.getSpellResistance((short)SpellEffectsEnum.STUNNED.getTypeId());
/*  919 */           if (currSpellRes != null) {
/*  920 */             currSpellRes.setResistance(0.5F, 0.023F);
/*      */           }
/*      */         } else {
/*  923 */           successChance = 1.0F - currSpellRes.getResistance();
/*      */         } 
/*      */       }
/*  926 */       if (Server.rand.nextFloat() < successChance || !applyResistance)
/*      */       {
/*  928 */         this.statusHolder.getCombatHandler().setCurrentStance(-1, (byte)8);
/*  929 */         this.statusHolder.maybeInterruptAction(200000);
/*  930 */         this.statusHolder.getMovementScheme().setFightMoveMod(true);
/*  931 */         this.statusHolder.getCommunicator().sendStunned(true);
/*  932 */         this.statusHolder.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.STUNNED, (int)stunTime, stunTime);
/*  933 */         this.stunned = Math.max(0.0F, stunTime);
/*  934 */         this.statusHolder.playAnimation("stun", false);
/*      */       
/*      */       }
/*      */       else
/*      */       {
/*  939 */         Server.getInstance().broadCastAction(this.statusHolder.getNameWithGenus() + " brushes away the stunned feeling.", this.statusHolder, 2, true);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  945 */       this.stunned = Math.max(0.0F, stunTime);
/*      */     } 
/*      */     
/*  948 */     sendStateString();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean modifyWounds(int dam) {
/*  953 */     boolean _dead = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  962 */     if (this.statusHolder.getTemplate().getCreatureAI() != null)
/*      */     {
/*  964 */       dam = this.statusHolder.getTemplate().getCreatureAI().woundDamageChanged(this.statusHolder, dam);
/*      */     }
/*      */     
/*  967 */     setChanged(true);
/*  968 */     this.damage += dam;
/*      */     
/*  970 */     if (this.damage >= 65535) {
/*      */       
/*  972 */       if (this.statusHolder.getPower() >= 3)
/*      */       {
/*  974 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  976 */           logger.fine("Deity with id=" + this.statusHolder.getWurmId() + ", damage has reached or exceeded the maximum so it is time heal. Damage was: " + this.damage + ", creature/player: " + this.statusHolder);
/*      */         }
/*      */ 
/*      */         
/*  980 */         this.damage = 0;
/*  981 */         this.statusHolder.getBody().healFully();
/*  982 */         this.statusHolder.getCommunicator().sendCombatAlertMessage("You died but were instantly healed.");
/*      */       }
/*      */       else
/*      */       {
/*  986 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  988 */           logger.fine("Creature/Player with id=" + this.statusHolder.getWurmId() + ", damage has reached or exceeded the maximum so it is time to die. Damage: " + this.damage + ", creature/player: " + this.statusHolder);
/*      */         }
/*      */ 
/*      */         
/*  992 */         this.damage = 65535;
/*  993 */         this.statusHolder.die(false, "Damage");
/*  994 */         _dead = true;
/*      */       }
/*      */     
/*  997 */     } else if (this.damage < 0) {
/*  998 */       this.damage = 0;
/*  999 */     }  if (!this.statusHolder.isUnique())
/* 1000 */       this.stamina = Math.min(this.stamina, 65535 - this.damage); 
/* 1001 */     sendStamina();
/*      */     
/* 1003 */     return _dead;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeWounds() {
/* 1008 */     this.damage = 0;
/* 1009 */     sendStamina();
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void setTraitBits(long bits) {
/* 1014 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 1016 */       if (x != 28 || this.statusHolder.getName().contains("traitor")) {
/*      */         
/* 1018 */         if (x == 28 && this.statusHolder.getName().contains("traitor")) {
/*      */           
/* 1020 */           Effect traitorEffect = EffectFactory.getInstance().createGenericEffect(this.statusHolder.getWurmId(), "traitor", this.statusHolder
/* 1021 */               .getPosX(), this.statusHolder.getPosY(), this.statusHolder.getPositionZ() + this.statusHolder.getHalfHeightDecimeters() / 10.0F, this.statusHolder
/* 1022 */               .isOnSurface(), -1.0F, this.statusHolder.getStatus().getRotation());
/* 1023 */           this.statusHolder.addEffect(traitorEffect);
/*      */         } 
/*      */         
/* 1026 */         if (x == 0) {
/*      */           
/* 1028 */           if ((bits & 0x1L) == 1L) {
/* 1029 */             this.traitbits.set(x, true);
/*      */           } else {
/* 1031 */             this.traitbits.set(x, false);
/*      */           } 
/* 1033 */         } else if ((bits >> x & 0x1L) == 1L) {
/* 1034 */           this.traitbits.set(x, true);
/*      */         } else {
/* 1036 */           this.traitbits.set(x, false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected final long getTraitBits() {
/* 1042 */     long ret = 0L;
/* 1043 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 1045 */       if (this.traitbits.get(x))
/*      */       {
/* 1047 */         ret += 1L << x;
/*      */       }
/*      */     } 
/* 1050 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int traitsCount() {
/* 1055 */     int cnt = 0;
/* 1056 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 1058 */       if (this.traitbits.get(x) && Traits.getTraitString(x).length() > 0)
/* 1059 */         cnt++; 
/*      */     } 
/* 1061 */     return cnt;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTraitBitSet(int setting) {
/* 1066 */     return this.traitbits.get(setting);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final boolean removeRandomNegativeTrait() {
/* 1071 */     if (this.traits == 0L)
/* 1072 */       return false; 
/* 1073 */     for (int x = 0; x < 64; x++) {
/* 1074 */       if (Traits.isTraitNegative(x) && isTraitBitSet(x)) {
/*      */         
/* 1076 */         setTraitBit(x, false);
/* 1077 */         return true;
/*      */       } 
/* 1079 */     }  return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setTraitBit(int setting, boolean value) {
/* 1084 */     this.traitbits.set(setting, value);
/* 1085 */     this.traits = getTraitBits();
/*      */     
/*      */     try {
/* 1088 */       setInheritance(this.traits, this.mother, this.father);
/*      */     }
/* 1090 */     catch (IOException iox) {
/*      */       
/* 1092 */       logger.log(Level.WARNING, iox.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getStaminaSkill() {
/*      */     try {
/* 1104 */       return (float)this.statusHolder.getSkills().getSkill(103).getKnowledge(0.0D);
/*      */     }
/* 1106 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1108 */       logger.log(Level.WARNING, "Creature has no stamina :" + this.statusHolder.getName() + "," + nss.getMessage(), (Throwable)nss);
/* 1109 */       this.statusHolder.getSkills().learn(103, 20.0F);
/* 1110 */       return 20.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void resetCreatureStamina() {
/* 1116 */     int currMax = 65535 - this.damage;
/* 1117 */     int oldStamina = this.stamina;
/* 1118 */     this.stamina = currMax;
/* 1119 */     if (this.stamina != oldStamina)
/* 1120 */       setChanged(true); 
/* 1121 */     checkStaminaEffects(oldStamina);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void modifyStamina(float staminaPoints) {
/* 1126 */     if (!this.statusHolder.isUnique() || this.statusHolder.getPower() >= 4) {
/*      */       
/* 1128 */       if ((staminaPoints > 0.0F && this.stamina < 65535) || (staminaPoints < 0.0F && this.stamina > 1))
/*      */       {
/* 1130 */         float staminaMod = getStaminaSkill() / 100.0F;
/* 1131 */         int currMax = 65535 - this.damage;
/* 1132 */         int oldStamina = this.stamina;
/* 1133 */         if (staminaPoints > 0.0F) {
/* 1134 */           this.stamina = (int)(this.stamina + staminaPoints * Math.max(0.0D, (1.0F + staminaMod) + getModifierValuesFor(1)));
/* 1135 */         } else if (staminaPoints < 0.0F) {
/*      */ 
/*      */           
/* 1138 */           if (this.hunger < 10000)
/* 1139 */             staminaMod = (float)(staminaMod + 0.05D); 
/* 1140 */           if (this.statusHolder.getCultist() != null && this.statusHolder.getCultist().usesNoStamina())
/*      */           {
/* 1142 */             staminaMod += 0.3F;
/*      */           }
/*      */           
/* 1145 */           float caloriesModifier = 1.0F / (1.0F + Math.min(this.calories, 1.0F) / 3.0F);
/* 1146 */           staminaMod = (1.0F - staminaMod) * caloriesModifier;
/* 1147 */           this.stamina = (int)(this.stamina + Math.min(staminaPoints * staminaMod * ItemBonus.getStaminaReductionBonus(this.statusHolder), staminaPoints * 0.01F));
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1152 */         this.stamina = Math.max(1, this.stamina);
/* 1153 */         this.stamina = Math.min(this.stamina, currMax);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1159 */         if (crossedStatusBorder(oldStamina, this.stamina))
/* 1160 */           sendStateString(); 
/* 1161 */         if (oldStamina != this.stamina)
/* 1162 */           setChanged(true); 
/* 1163 */         sendStamina();
/* 1164 */         checkStaminaEffects(oldStamina);
/* 1165 */         if (staminaPoints < 0.0F) {
/* 1166 */           modifyThirst(Math.max(1.0F, -staminaPoints / 1000.0F));
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1172 */       if (this.stamina != 65535)
/* 1173 */         setChanged(true); 
/* 1174 */       this.stamina = 65535;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void modifyStamina2(float staminaPercent) {
/* 1180 */     if (!this.statusHolder.isUnique()) {
/*      */       
/* 1182 */       int currMax = 65535 - this.damage;
/* 1183 */       int oldStamina = this.stamina;
/* 1184 */       this.stamina = (int)(this.stamina + staminaPercent * currMax);
/* 1185 */       this.stamina = Math.max(1, this.stamina);
/* 1186 */       this.stamina = Math.min(this.stamina, currMax);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1192 */       if (crossedStatusBorder(oldStamina, this.stamina))
/* 1193 */         sendStateString(); 
/* 1194 */       if (oldStamina != this.stamina)
/* 1195 */         setChanged(true); 
/* 1196 */       sendStamina();
/* 1197 */       checkStaminaEffects(oldStamina);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1202 */       if (this.stamina != 65535)
/* 1203 */         setChanged(true); 
/* 1204 */       this.stamina = 65535;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void checkStaminaEffects(int oldStamina) {
/* 1210 */     if (this.stamina < 2000 && oldStamina >= 2000) {
/*      */       
/* 1212 */       this.statusHolder.getMovementScheme().addModifier(this.moveMod);
/*      */     }
/* 1214 */     else if (this.stamina >= 2000 && oldStamina < 2000) {
/*      */       
/* 1216 */       this.statusHolder.getMovementScheme().removeModifier(this.moveMod);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendStamina() {
/* 1222 */     if (this.stamina > this.lastSentStamina + 100 || this.stamina < this.lastSentStamina - 100 || this.damage > this.lastSentDamage + 100 || this.damage < this.lastSentDamage - 100) {
/*      */ 
/*      */       
/* 1225 */       this.lastSentStamina = this.stamina;
/* 1226 */       this.lastSentDamage = this.damage;
/*      */       
/* 1228 */       this.statusHolder.getCommunicator().sendStamina(this.stamina, this.damage);
/*      */       
/* 1230 */       float damp = calcDamPercent();
/* 1231 */       if (damp != this.lastDamPercSent) {
/*      */         
/* 1233 */         this.statusHolder.sendDamage(damp);
/* 1234 */         this.lastDamPercSent = damp;
/*      */       } 
/*      */       
/* 1237 */       if (this.statusHolder.isPlayer() && damp < 90.0F)
/*      */       {
/*      */         
/* 1240 */         PlonkData.FIRST_DAMAGE.trigger(this.statusHolder);
/*      */       }
/*      */ 
/*      */       
/* 1244 */       if (this.statusHolder.isPlayer() && calcStaminaPercent() < 30.0F)
/*      */       {
/*      */         
/* 1247 */         PlonkData.LOW_STAMINA.trigger(this.statusHolder);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final float calcDamPercent() {
/* 1254 */     if (this.damage == 0) {
/* 1255 */       return 100.0F;
/*      */     }
/*      */     
/* 1258 */     return Math.max(1.0F, (65535 - this.damage) / 65535.0F * 100.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float calcStaminaPercent() {
/* 1264 */     if (this.stamina == 65535) {
/* 1265 */       return 100.0F;
/*      */     }
/*      */     
/* 1268 */     return this.stamina / 65535.0F * 100.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int modifyHunger(int hungerModification, float newNutritionLevel) {
/* 1274 */     return modifyHunger(hungerModification, newNutritionLevel, -1.0F, -1.0F, -1.0F, -1.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int modifyHunger(int hungerModification, float newNutritionLevel, float addCalories, float addCarbs, float addFats, float addProteins) {
/* 1280 */     int oldHunger = this.hunger;
/*      */     
/* 1282 */     int localHungerModification = hungerModification;
/* 1283 */     if (localHungerModification > 0) {
/* 1284 */       localHungerModification = (int)(localHungerModification * (1.0F - Math.min(this.proteins, 1.0F) / 3.0F));
/*      */     }
/* 1286 */     this.hunger = Math.min(65535, Math.max(1, this.hunger + localHungerModification));
/*      */     
/* 1288 */     if (this.hunger < 65535) {
/*      */ 
/*      */ 
/*      */       
/* 1292 */       int realHungerModification = this.hunger - oldHunger;
/*      */       
/* 1294 */       if (realHungerModification < 0) {
/*      */         
/* 1296 */         newNutritionLevel = Math.min(newNutritionLevel * 1.1F, 0.99F);
/*      */ 
/*      */         
/* 1299 */         if (this.nutrition > 0.0F) {
/*      */           
/* 1301 */           float oldNutPercent = Math.max(1.0F, (65535 - oldHunger)) / Math.max(1.0F, (65535 - this.hunger));
/* 1302 */           float newNutPercent = -realHungerModification / Math.max(1.0F, (65535 - this.hunger));
/* 1303 */           this.nutrition = oldNutPercent * this.nutrition + newNutPercent * newNutritionLevel;
/*      */         } else {
/*      */           
/* 1306 */           this.nutrition = newNutritionLevel;
/*      */         } 
/*      */       } 
/* 1309 */       if (addCalories >= 0.0F)
/*      */       {
/* 1311 */         addToCCFPValues(addCalories, addCarbs, addFats, addProteins);
/*      */       }
/*      */     } else {
/*      */       
/* 1315 */       this.nutrition = 0.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1321 */     if (crossedStatusBorder(oldHunger, this.hunger)) {
/* 1322 */       sendStateString();
/*      */     }
/* 1324 */     if (this.hunger < this.lastSentHunger - 100 || this.hunger > this.lastSentHunger + 100) {
/*      */       
/* 1326 */       this.lastSentHunger = this.hunger;
/* 1327 */       sendHunger();
/*      */       
/* 1329 */       if (this.statusHolder.isPlayer())
/*      */       {
/*      */         
/* 1332 */         if (PlonkData.HUNGRY.hasSeenThis(this.statusHolder)) {
/*      */           
/* 1334 */           float hungerPercent = 100.0F - 100.0F * this.hunger / 65535.0F;
/* 1335 */           if (hungerPercent <= 50.0F)
/*      */           {
/*      */             
/* 1338 */             PlonkData.HUNGRY.trigger(this.statusHolder);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 1343 */     return this.hunger;
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
/*      */   void addToCCFPValues(float addCalories, float addCarbs, float addFats, float addProteins) {
/* 1356 */     this.calories = Math.min(this.calories + addCalories / DAILY_CALORIES, CCFP_MAX_PERCENTAGE);
/* 1357 */     this.carbs = Math.min(this.carbs + addCarbs / DAILY_CARBS, CCFP_MAX_PERCENTAGE);
/* 1358 */     this.fats = Math.min(this.fats + addFats / DAILY_FATS, CCFP_MAX_PERCENTAGE);
/* 1359 */     this.proteins = Math.min(this.proteins + addProteins / DAILY_PROTEINS, CCFP_MAX_PERCENTAGE);
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
/*      */   public final boolean refresh(float newNutrition, boolean fullStamina) {
/* 1373 */     this.hunger = 0;
/* 1374 */     this.thirst = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1380 */     if (fullStamina) {
/*      */       
/* 1382 */       int oldStam = this.stamina;
/*      */       
/* 1384 */       this.nutrition = 0.99F;
/* 1385 */       this.stamina = 65535;
/* 1386 */       setChanged(true);
/* 1387 */       sendStamina();
/* 1388 */       sendStateString();
/* 1389 */       checkStaminaEffects(oldStam);
/* 1390 */       return true;
/*      */     } 
/*      */     
/* 1393 */     if (this.nutrition < 0.5F) {
/*      */       
/* 1395 */       setChanged(true);
/*      */       
/* 1397 */       this.nutrition = 0.5F;
/* 1398 */       sendStateString();
/* 1399 */       return true;
/*      */     } 
/* 1401 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxCCFP() {
/* 1409 */     this.calories = CCFP_MAX_PERCENTAGE;
/* 1410 */     this.carbs = CCFP_MAX_PERCENTAGE;
/* 1411 */     this.fats = CCFP_MAX_PERCENTAGE;
/* 1412 */     this.proteins = CCFP_MAX_PERCENTAGE;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean crossedStatusBorder(int oldnumber, int newnumber) {
/* 1417 */     if (oldnumber >= 65535 && newnumber < 65535)
/* 1418 */       return true; 
/* 1419 */     if (oldnumber > 60000 && newnumber <= 60000)
/* 1420 */       return true; 
/* 1421 */     if (oldnumber > 45000 && newnumber <= 45000)
/* 1422 */       return true; 
/* 1423 */     if (oldnumber > 20000 && newnumber <= 20000)
/* 1424 */       return true; 
/* 1425 */     if (oldnumber > 10000 && newnumber <= 10000)
/* 1426 */       return true; 
/* 1427 */     if (oldnumber > 1000 && newnumber <= 1000)
/* 1428 */       return true; 
/* 1429 */     if (oldnumber > 1 && newnumber <= 1)
/* 1430 */       return true; 
/* 1431 */     if (oldnumber < 1 && newnumber >= 1)
/* 1432 */       return true; 
/* 1433 */     if (oldnumber < 1000 && newnumber >= 1000)
/* 1434 */       return true; 
/* 1435 */     if (oldnumber < 10000 && newnumber >= 10000)
/* 1436 */       return true; 
/* 1437 */     if (oldnumber < 20000 && newnumber >= 20000)
/* 1438 */       return true; 
/* 1439 */     if (oldnumber < 45000 && newnumber >= 45000)
/* 1440 */       return true; 
/* 1441 */     if (oldnumber < 60000 && newnumber >= 60000)
/* 1442 */       return true; 
/* 1443 */     if (oldnumber < 65535 && newnumber >= 65535)
/* 1444 */       return true; 
/* 1445 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCaloriesAsPercent() {
/* 1450 */     return Math.min(this.calories * 100.0F, 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCarbsAsPercent() {
/* 1455 */     return Math.min(this.carbs * 100.0F, 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFatsAsPercent() {
/* 1460 */     return Math.min(this.fats * 100.0F, 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getProteinsAsPercent() {
/* 1465 */     return Math.min(this.proteins * 100.0F, 100.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void sendHunger() {
/* 1471 */     this.statusHolder.getCommunicator().sendHunger(this.hunger, this.nutrition, 
/* 1472 */         getCaloriesAsPercent(), getCarbsAsPercent(), getFatsAsPercent(), getProteinsAsPercent());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int modifyThirst(float thirstModification) {
/* 1483 */     return modifyThirst(thirstModification, -1.0F, -1.0F, -1.0F, 1.0F);
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
/*      */   public final int modifyThirst(float thirstModification, float addCalories, float addCarbs, float addFats, float addProteins) {
/* 1500 */     int oldThirst = this.thirst;
/* 1501 */     float realThirstModification = thirstModification;
/* 1502 */     if (realThirstModification > 0.0F)
/*      */     {
/*      */       
/* 1505 */       realThirstModification *= 1.0F - Math.min(this.carbs, 1.0F) / 3.0F;
/*      */     }
/* 1507 */     this.thirst = (int)(this.thirst + realThirstModification);
/* 1508 */     this.thirst = Math.max(1, this.thirst);
/* 1509 */     this.thirst = Math.min(65535, this.thirst);
/* 1510 */     if (crossedStatusBorder(oldThirst, this.thirst))
/* 1511 */       sendStateString(); 
/* 1512 */     if (this.thirst < this.lastSentThirst - 100 || this.thirst > this.lastSentThirst + 100) {
/*      */       
/* 1514 */       this.lastSentThirst = this.thirst;
/* 1515 */       sendThirst();
/*      */       
/* 1517 */       if (!PlonkData.THIRSTY.hasSeenThis(this.statusHolder)) {
/*      */         
/* 1519 */         float thirstPercent = 100.0F - 100.0F * this.thirst / 65535.0F;
/* 1520 */         if (thirstPercent <= 50.0F)
/*      */         {
/*      */           
/* 1523 */           PlonkData.THIRSTY.trigger(this.statusHolder);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1528 */     if (addCalories >= 0.0F)
/*      */     {
/* 1530 */       addToCCFPValues(addCalories, addCarbs, addFats, addProteins);
/*      */     }
/* 1532 */     return this.thirst;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void sendThirst() {
/* 1538 */     this.statusHolder.getCommunicator().sendThirst(this.thirst);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getHunger() {
/* 1547 */     return this.hunger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getNutritionlevel() {
/* 1556 */     return this.nutrition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getCalories() {
/* 1565 */     return this.calories;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getCarbs() {
/* 1574 */     return this.carbs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFats() {
/* 1583 */     return this.fats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getProteins() {
/* 1592 */     return this.proteins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getThirst() {
/* 1601 */     return this.thirst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStamina() {
/* 1610 */     return this.stamina;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setTrade(@Nullable Trade _trade) {
/* 1620 */     this.trade = _trade;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Trade getTrade() {
/* 1629 */     return this.trade;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTrading() {
/* 1634 */     return (this.trade != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void sendStateString() {
/* 1643 */     if (this.statusHolder.isPlayer()) {
/*      */       
/* 1645 */       List<String> strings = new LinkedList<>();
/* 1646 */       if (this.stunned > 0.0F)
/* 1647 */         strings.add("Stunned"); 
/* 1648 */       String agg = getAggressiveness();
/* 1649 */       if (agg.length() > 0) {
/* 1650 */         strings.add(agg);
/*      */       }
/* 1652 */       if (!this.visible)
/*      */       {
/* 1654 */         if (this.statusHolder.getPower() > 0)
/* 1655 */           strings.add("Invisible"); 
/*      */       }
/* 1657 */       if (this.disease > 0)
/* 1658 */         strings.add("Diseased"); 
/* 1659 */       if (this.statusHolder.opponent != null)
/*      */       {
/* 1661 */         strings.add("Opponent:" + this.statusHolder.opponent.getName());
/*      */       }
/* 1663 */       if (this.statusHolder.getTarget() != null)
/*      */       {
/* 1665 */         strings.add("Target:" + this.statusHolder.getTarget().getName());
/*      */       }
/*      */       
/* 1668 */       if (this.detectInvisCounter > 0)
/* 1669 */         strings.add("Alerted"); 
/* 1670 */       if (this.stealth)
/* 1671 */         strings.add("Stealthmode"); 
/* 1672 */       if (this.statusHolder.getCRCounterBonus() > 0)
/* 1673 */         strings.add("Sharp"); 
/* 1674 */       if (this.statusHolder.isDead())
/* 1675 */         strings.add("Dead"); 
/* 1676 */       if (this.statusHolder.damageCounter > 0)
/* 1677 */         strings.add("Hurting"); 
/* 1678 */       if (this.statusHolder.getFarwalkerSeconds() > 0) {
/* 1679 */         strings.add("Unstoppable");
/*      */       }
/* 1681 */       if (this.statusHolder.linkedTo != -10L) {
/*      */         
/*      */         try {
/*      */           
/* 1685 */           Creature c = Server.getInstance().getCreature(this.statusHolder.linkedTo);
/* 1686 */           strings.add("Link: " + c.getName());
/*      */         }
/* 1688 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */         
/* 1691 */         } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */ 
/*      */       
/* 1695 */       StringBuilder stbuf = new StringBuilder();
/* 1696 */       for (ListIterator<String> it = strings.listIterator(); it.hasNext(); ) {
/*      */         
/* 1698 */         String next = it.next();
/* 1699 */         it.remove();
/* 1700 */         stbuf.append(next);
/* 1701 */         if (strings.size() > 0)
/* 1702 */           stbuf.append(", "); 
/*      */       } 
/* 1704 */       this.statusHolder.getCommunicator().sendStatus(stbuf.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canEat() {
/* 1713 */     return (this.hunger >= 10000);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isHungry() {
/* 1721 */     return (this.hunger >= 45000);
/*      */   }
/*      */ 
/*      */   
/*      */   private String getAggressiveness() {
/* 1726 */     byte fightStyle = this.statusHolder.getFightStyle();
/* 1727 */     if (fightStyle == 1)
/* 1728 */       return "Aggressive"; 
/* 1729 */     if (fightStyle == 2) {
/* 1730 */       return "Defensive";
/*      */     }
/* 1732 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean pollAge(int maxAge) {
/* 1737 */     boolean rebornPoll = false;
/* 1738 */     if (this.reborn && this.mother == -10L)
/*      */     {
/* 1740 */       if (WurmCalendar.currentTime - this.lastPolledAge > 604800L)
/*      */       {
/* 1742 */         rebornPoll = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1747 */     boolean fasterGrowth = (this.statusHolder.getTemplate().getTemplateId() == 65 || this.statusHolder.getTemplate().getTemplateId() == 117 || this.statusHolder.getTemplate().getTemplateId() == 118);
/* 1748 */     if (WurmCalendar.currentTime - this.lastPolledAge > ((Servers.localServer.PVPSERVER && this.age < 8 && fasterGrowth) ? 259200L : 2419200L) || (
/* 1749 */       isTraitBitSet(29) && WurmCalendar.currentTime - this.lastPolledAge > 345600L) || rebornPoll) {
/*      */       
/* 1751 */       if ((this.statusHolder.isGhost() || this.statusHolder.isKingdomGuard() || this.statusHolder.isUnique()) && (!this.reborn || this.mother == -10L))
/*      */       {
/* 1753 */         this.age = Math.max(this.age, 11); } 
/* 1754 */       int newAge = this.age + 1;
/* 1755 */       boolean updated = false;
/* 1756 */       if (!this.statusHolder.isCaredFor() && !isTraitBitSet(29) && (newAge >= maxAge || (
/* 1757 */         isTraitBitSet(13) && newAge >= Math.max(1, maxAge - Server.rand
/* 1758 */           .nextInt(maxAge / 2))))) {
/* 1759 */         return true;
/*      */       }
/*      */       
/* 1762 */       if (!rebornPoll) {
/*      */         
/* 1764 */         if (newAge > (isTraitBitSet(21) ? 75 : (isTraitBitSet(29) ? 36 : 50)) && 
/* 1765 */           !this.statusHolder.isGhost() && !this.statusHolder.isHuman() && !this.statusHolder.isUnique() && 
/* 1766 */           !this.statusHolder.isCaredFor())
/* 1767 */           return true; 
/* 1768 */         if (newAge - 1 >= 5 && !this.reborn)
/*      */         {
/* 1770 */           if (getTemplate().getAdultMaleTemplateId() > -1 || 
/* 1771 */             getTemplate().getAdultFemaleTemplateId() > -1) {
/*      */             
/* 1773 */             int newtemplateId = getTemplate().getAdultMaleTemplateId();
/* 1774 */             if (this.sex == 1 && getTemplate().getAdultFemaleTemplateId() > -1)
/*      */             {
/* 1776 */               newtemplateId = getTemplate().getAdultFemaleTemplateId();
/*      */             }
/* 1778 */             if (newtemplateId != getTemplate().getTemplateId()) {
/*      */               
/* 1780 */               newAge = 1;
/*      */               
/*      */               try {
/* 1783 */                 updateAge(newAge);
/* 1784 */                 updated = true;
/*      */               }
/* 1786 */               catch (IOException iox) {
/*      */                 
/* 1788 */                 logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */               } 
/*      */               
/*      */               try {
/* 1792 */                 setChanged(true);
/* 1793 */                 CreatureTemplate newTemplate = CreatureTemplateFactory.getInstance().getTemplate(newtemplateId);
/*      */                 
/* 1795 */                 this.template = newTemplate;
/* 1796 */                 this.statusHolder.template = this.template;
/* 1797 */                 if (!this.statusHolder.isNpc()) {
/*      */ 
/*      */ 
/*      */                   
/* 1801 */                   if (this.statusHolder.getMother() == -10L || (!this.statusHolder.isHorse() && !this.statusHolder.isUnicorn() && !this.reborn))
/*      */                     
/*      */                     try {
/* 1804 */                       if (this.statusHolder.getName().endsWith("traitor")) {
/* 1805 */                         this.statusHolder.setName(this.template.getName() + " traitor");
/*      */                       } else {
/* 1807 */                         this.statusHolder.setName(this.template.getName());
/*      */                       } 
/* 1809 */                     } catch (Exception ex) {
/*      */                       
/* 1811 */                       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */                     }  
/* 1813 */                   if (!this.reborn || this.mother == -10L)
/*      */                     
/*      */                     try {
/* 1816 */                       this.statusHolder.skills.delete();
/* 1817 */                       this.statusHolder.skills.clone(newTemplate.getSkills().getSkills());
/* 1818 */                       this.statusHolder.skills.save();
/*      */                     }
/* 1820 */                     catch (Exception ex) {
/*      */                       
/* 1822 */                       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */                     }  
/*      */                 } 
/* 1825 */                 save();
/* 1826 */                 this.statusHolder.refreshVisible();
/*      */               }
/* 1828 */               catch (NoSuchCreatureTemplateException nsc) {
/*      */                 
/* 1830 */                 logger.log(Level.WARNING, this.statusHolder
/* 1831 */                     .getName() + ", " + this.statusHolder.getWurmId() + ": " + nsc.getMessage(), (Throwable)nsc);
/*      */               }
/* 1833 */               catch (IOException iox) {
/*      */                 
/* 1835 */                 logger.log(Level.WARNING, this.statusHolder
/* 1836 */                     .getName() + ", " + this.statusHolder.getWurmId() + ": " + iox.getMessage(), iox);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/* 1842 */       if (!updated) {
/*      */         
/*      */         try {
/*      */           
/* 1846 */           updateAge(newAge);
/*      */           
/* 1848 */           if (this.statusHolder.getHitched() != null && !this.statusHolder.getHitched().isAnySeatOccupied(false))
/*      */           {
/* 1850 */             if (!this.statusHolder.isDomestic() && getBattleRatingTypeModifier() > 1.2F) {
/*      */               
/* 1852 */               Server.getInstance().broadCastMessage(this.statusHolder.getName() + " stops dragging a " + 
/* 1853 */                   Vehicle.getVehicleName(this.statusHolder.getHitched()) + ".", this.statusHolder
/* 1854 */                   .getTileX(), this.statusHolder.getTileY(), this.statusHolder.isOnSurface(), 5);
/* 1855 */               if (this.statusHolder.getHitched().removeDragger(this.statusHolder)) {
/* 1856 */                 this.statusHolder.setHitched(null, false);
/*      */               }
/*      */             } 
/*      */           }
/* 1860 */         } catch (IOException iox) {
/*      */           
/* 1862 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1867 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isChampion() {
/* 1875 */     return (this.modtype == 99);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getAgeString() {
/* 1884 */     if (this.age < 3)
/* 1885 */       return "young"; 
/* 1886 */     if (this.age < 8)
/* 1887 */       return "adolescent"; 
/* 1888 */     if (this.age < 12)
/* 1889 */       return "mature"; 
/* 1890 */     if (this.age < 30)
/* 1891 */       return "aged"; 
/* 1892 */     if (this.age < 40) {
/* 1893 */       return "old";
/*      */     }
/* 1895 */     return "venerable";
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean hasCustomColor() {
/* 1900 */     if (this.modtype > 0) {
/*      */       
/* 1902 */       switch (this.modtype) {
/*      */         
/*      */         case 1:
/* 1905 */           return true;
/*      */         case 2:
/* 1907 */           return true;
/*      */         case 3:
/* 1909 */           return true;
/*      */         case 4:
/* 1911 */           return true;
/*      */         case 5:
/* 1913 */           return true;
/*      */         case 6:
/* 1915 */           return true;
/*      */         case 7:
/* 1917 */           return true;
/*      */         case 8:
/* 1919 */           return true;
/*      */         case 9:
/* 1921 */           return true;
/*      */         case 10:
/* 1923 */           return true;
/*      */         case 11:
/* 1925 */           return true;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1932 */       return false;
/*      */     } 
/*      */     
/* 1935 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final byte getColorRed() {
/* 1940 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   final byte getColorGreen() {
/* 1945 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   final byte getColorBlue() {
/* 1950 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract void setVehicle(long paramLong, byte paramByte);
/*      */   
/*      */   final float getSizeMod() {
/* 1957 */     float aiDataModifier = 1.0F;
/* 1958 */     if (this.statusHolder.getCreatureAIData() != null) {
/* 1959 */       aiDataModifier = this.statusHolder.getCreatureAIData().getSizeModifier();
/*      */     }
/* 1961 */     float floatToRet = 1.0F;
/* 1962 */     if (!this.statusHolder.isVehicle() && this.modtype != 0)
/*      */     {
/* 1964 */       switch (this.modtype) {
/*      */         
/*      */         case 3:
/* 1967 */           floatToRet = 1.4F;
/*      */           break;
/*      */         case 4:
/* 1970 */           floatToRet = 2.0F;
/*      */           break;
/*      */         case 6:
/* 1973 */           floatToRet = 2.0F;
/*      */           break;
/*      */         case 7:
/* 1976 */           floatToRet = 0.8F;
/*      */           break;
/*      */         case 8:
/* 1979 */           floatToRet = 0.9F;
/*      */           break;
/*      */         case 9:
/* 1982 */           floatToRet = 1.5F;
/*      */           break;
/*      */         case 10:
/* 1985 */           floatToRet = 1.3F;
/*      */           break;
/*      */         case 99:
/* 1988 */           floatToRet = 3.0F;
/*      */           break;
/*      */         case -1:
/* 1991 */           floatToRet = 0.5F;
/*      */           break;
/*      */         case -2:
/* 1994 */           floatToRet = 0.25F;
/*      */           break;
/*      */         case -3:
/* 1997 */           floatToRet = 0.125F;
/*      */           break;
/*      */       } 
/*      */     
/*      */     }
/* 2002 */     if (this.statusHolder.getHitched() == null && this.statusHolder.getTemplate().getTemplateId() == 82 && 
/* 2003 */       !this.statusHolder.getNameWithoutPrefixes().equalsIgnoreCase(this.statusHolder.getTemplate().getName())) {
/* 2004 */       floatToRet = 2.0F;
/*      */     }
/* 2006 */     if (!this.statusHolder.isVehicle() && this.statusHolder.hasTrait(28)) {
/* 2007 */       floatToRet *= 1.5F;
/*      */     }
/* 2009 */     return floatToRet * getAgeSizeModifier() * aiDataModifier;
/*      */   }
/*      */ 
/*      */   
/*      */   private float getAgeSizeModifier() {
/* 2014 */     if (this.statusHolder.isHuman() || this.statusHolder.isGhost() || this.template.getAdultFemaleTemplateId() > -1 || this.template
/* 2015 */       .getAdultMaleTemplateId() > -1)
/* 2016 */       return 1.0F; 
/* 2017 */     if (this.age < 3)
/* 2018 */       return 0.7F; 
/* 2019 */     if (this.age < 8)
/* 2020 */       return 0.8F; 
/* 2021 */     if (this.age < 12)
/* 2022 */       return 0.9F; 
/* 2023 */     if (this.age < 40) {
/* 2024 */       return 1.0F;
/*      */     }
/* 2026 */     return 0.95F;
/*      */   }
/*      */ 
/*      */   
/*      */   final String getTypeString() {
/* 2031 */     if (this.modtype > 0) {
/*      */       
/* 2033 */       switch (this.modtype) {
/*      */         
/*      */         case 1:
/* 2036 */           return "fierce ";
/*      */         case 2:
/* 2038 */           return "angry ";
/*      */         case 3:
/* 2040 */           return "raging ";
/*      */         case 4:
/* 2042 */           return "slow ";
/*      */         case 5:
/* 2044 */           return "alert ";
/*      */         case 6:
/* 2046 */           return "greenish ";
/*      */         case 7:
/* 2048 */           return "lurking ";
/*      */         case 8:
/* 2050 */           return "sly ";
/*      */         case 9:
/* 2052 */           return "hardened ";
/*      */         case 10:
/* 2054 */           return "scared ";
/*      */         case 11:
/* 2056 */           return "diseased ";
/*      */         case 99:
/* 2058 */           return "champion ";
/*      */       } 
/* 2060 */       return "";
/*      */     } 
/*      */     
/* 2063 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte getModtypeForString(String corpseString) {
/* 2073 */     if (corpseString.contains(" fierce "))
/* 2074 */       return 1; 
/* 2075 */     if (corpseString.contains(" angry "))
/* 2076 */       return 2; 
/* 2077 */     if (corpseString.contains(" raging "))
/* 2078 */       return 3; 
/* 2079 */     if (corpseString.contains(" slow "))
/* 2080 */       return 4; 
/* 2081 */     if (corpseString.contains(" alert "))
/* 2082 */       return 5; 
/* 2083 */     if (corpseString.contains(" greenish "))
/* 2084 */       return 6; 
/* 2085 */     if (corpseString.contains(" lurking "))
/* 2086 */       return 7; 
/* 2087 */     if (corpseString.contains(" sly "))
/* 2088 */       return 8; 
/* 2089 */     if (corpseString.contains(" hardened "))
/* 2090 */       return 9; 
/* 2091 */     if (corpseString.contains(" scared "))
/* 2092 */       return 10; 
/* 2093 */     if (corpseString.contains(" diseased "))
/* 2094 */       return 11; 
/* 2095 */     if (corpseString.contains(" champion "))
/* 2096 */       return 99; 
/* 2097 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean setStealth(boolean st) {
/* 2102 */     if (this.stealth != st) {
/*      */       
/* 2104 */       this.stealth = st;
/* 2105 */       sendStateString();
/* 2106 */       this.statusHolder.getCommunicator().sendToggle(3, this.stealth);
/* 2107 */       return true;
/*      */     } 
/* 2109 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean modifyLoyalty(float mod) {
/* 2114 */     setLoyalty(this.loyalty + mod);
/* 2115 */     return (this.loyalty <= 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean pollLoyalty() {
/* 2120 */     if (this.loyalty > 0.0F) {
/*      */       
/* 2122 */       long timeBetweenPolls = 86400000L;
/* 2123 */       if (this.statusHolder.isAggHuman() && (this.statusHolder
/* 2124 */         .getBaseCombatRating() > 20.0F || this.statusHolder.getSoulStrengthVal() > 40.0D))
/* 2125 */         timeBetweenPolls = 3600000L; 
/* 2126 */       if (System.currentTimeMillis() - this.lastPolledLoyalty > timeBetweenPolls) {
/*      */         
/* 2128 */         setLastPolledLoyalty();
/*      */         
/* 2130 */         if (!this.statusHolder.isReborn() || this.statusHolder.getMother() != -10L) {
/*      */           
/* 2132 */           int sstrngth = (int)this.statusHolder.getSoulStrengthVal();
/* 2133 */           sstrngth += isTraitBitSet(11) ? 20 : 0;
/*      */           
/* 2135 */           if (Server.rand.nextInt(50) < sstrngth) {
/*      */             
/* 2137 */             if (logger.isLoggable(Level.FINEST))
/*      */             {
/* 2139 */               logger.finest(this.statusHolder.getName() + " decreasing loyalty (" + this.loyalty + ") by " + 
/* 2140 */                   Math.min(-5, sstrngth / -5));
/*      */             }
/* 2142 */             if (modifyLoyalty(Math.min(-5, sstrngth / -5))) {
/*      */               
/* 2144 */               if (logger.isLoggable(Level.FINER))
/*      */               {
/* 2146 */                 logger.finer(this.statusHolder.getName() + " loyalty became " + this.loyalty + ". Turned!");
/*      */               }
/* 2148 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2153 */       return false;
/*      */     } 
/*      */     
/* 2156 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void pollDetectInvis() {
/* 2161 */     if (this.detectInvisCounter > 0) {
/*      */       
/* 2163 */       this.detectInvisCounter--;
/* 2164 */       if (this.detectInvisCounter == 0 || this.detectInvisCounter % 60 == 0)
/* 2165 */         setDetectionSecs(); 
/* 2166 */       if (this.detectInvisCounter == 0) {
/* 2167 */         this.statusHolder.getCommunicator().sendRemoveSpellEffect(SpellEffectsEnum.DETECT_INVIS);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void setDetectInvisCounter(int detectInvisSecs) {
/* 2173 */     this.detectInvisCounter = detectInvisSecs;
/* 2174 */     if (this.statusHolder.isPlayer())
/*      */     {
/* 2176 */       if (this.detectInvisCounter > 0) {
/* 2177 */         this.statusHolder.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.DETECT_INVIS, this.detectInvisCounter, 100.0F);
/*      */       } else {
/* 2179 */         this.statusHolder.getCommunicator().sendRemoveSpellEffect(SpellEffectsEnum.DETECT_INVIS);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public final int getDetectInvisCounter() {
/* 2185 */     return this.detectInvisCounter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBattleRatingTypeModifier() {
/* 2194 */     float floatToRet = 1.0F;
/*      */     
/* 2196 */     switch (this.modtype) {
/*      */       
/*      */       case 1:
/* 2199 */         floatToRet = 1.6F;
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
/* 2221 */         return floatToRet * getAgeBRModifier();case 3: floatToRet = 2.0F; return floatToRet * getAgeBRModifier();case 2: case 9: floatToRet = 1.3F; return floatToRet * getAgeBRModifier();case 4: case 11: floatToRet = 0.5F; return floatToRet * getAgeBRModifier();case 6: floatToRet = 3.0F; return floatToRet * getAgeBRModifier();case 99: floatToRet = 6.0F; return floatToRet * getAgeBRModifier();
/*      */     } 
/*      */     return floatToRet * getAgeBRModifier();
/*      */   }
/*      */   final float getAgeBRModifier() {
/* 2226 */     if (this.age < 3)
/* 2227 */       return 0.9F; 
/* 2228 */     if (this.age < 8)
/* 2229 */       return 1.0F; 
/* 2230 */     if (this.age < 12)
/* 2231 */       return 1.1F; 
/* 2232 */     if (this.age < 30)
/* 2233 */       return 1.2F; 
/* 2234 */     if (this.age < 40) {
/* 2235 */       return 1.3F;
/*      */     }
/* 2237 */     return 1.4F;
/*      */   }
/*      */ 
/*      */   
/*      */   final float getMovementTypeModifier() {
/* 2242 */     float floatToRet = 1.0F;
/* 2243 */     switch (this.modtype) {
/*      */       
/*      */       case 11:
/* 2246 */         floatToRet = 0.5F;
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
/* 2267 */         return floatToRet * getAgeMoveModifier();case 3: floatToRet = 1.2F; return floatToRet * getAgeMoveModifier();case 2: case 10: floatToRet = 1.1F; return floatToRet * getAgeMoveModifier();case 4: floatToRet = 0.7F; return floatToRet * getAgeMoveModifier();case 5: floatToRet = 1.4F; return floatToRet * getAgeMoveModifier();case 99: floatToRet = 1.4F; return floatToRet * getAgeMoveModifier();
/*      */     } 
/*      */     return Math.min(1.6F, floatToRet * getAgeMoveModifier());
/*      */   }
/*      */   private float getAgeMoveModifier() {
/* 2272 */     if (this.age < 3)
/* 2273 */       return 0.9F; 
/* 2274 */     if (this.age < 8)
/* 2275 */       return 1.0F; 
/* 2276 */     if (this.age < 12)
/* 2277 */       return 1.1F; 
/* 2278 */     if (this.age < 30)
/* 2279 */       return 1.3F; 
/* 2280 */     if (this.age < 40) {
/* 2281 */       return 1.0F;
/*      */     }
/* 2283 */     return 0.8F;
/*      */   }
/*      */ 
/*      */   
/*      */   final float getDamageTypeModifier() {
/* 2288 */     float floatToRet = 1.0F;
/* 2289 */     switch (this.modtype) {
/*      */       
/*      */       case 6:
/* 2292 */         floatToRet = 2.0F;
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
/* 2317 */         return floatToRet * getAgeDamageModifier();case 1: floatToRet = 1.1F; return floatToRet * getAgeDamageModifier();case 3: floatToRet = 1.3F; return floatToRet * getAgeDamageModifier();case 2: case 9: floatToRet = 1.2F; return floatToRet * getAgeDamageModifier();case 10: case 11: floatToRet = 0.7F; return floatToRet * getAgeDamageModifier();case 8: floatToRet = 0.5F; return floatToRet * getAgeDamageModifier();case 99: floatToRet = 2.0F; return floatToRet * getAgeDamageModifier();
/*      */     } 
/*      */     return floatToRet * getAgeDamageModifier();
/*      */   }
/*      */   private float getAgeDamageModifier() {
/* 2322 */     if (this.age < 3)
/* 2323 */       return 0.8F; 
/* 2324 */     if (this.age < 8)
/* 2325 */       return 0.9F; 
/* 2326 */     if (this.age < 12)
/* 2327 */       return 1.0F; 
/* 2328 */     if (this.age < 30)
/* 2329 */       return 1.1F; 
/* 2330 */     if (this.age < 40) {
/* 2331 */       return 1.2F;
/*      */     }
/* 2333 */     return 1.5F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final float getParryTypeModifier() {
/* 2341 */     float floatToRet = 1.0F;
/* 2342 */     switch (this.modtype) {
/*      */       
/*      */       case 1:
/* 2345 */         floatToRet = 1.2F;
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
/* 2370 */         return floatToRet * getAgeParryModifier();case 3: case 7: floatToRet = 1.5F; return floatToRet * getAgeParryModifier();case 2: floatToRet = 1.1F; return floatToRet * getAgeParryModifier();case 10: floatToRet = 0.3F; return floatToRet * getAgeParryModifier();case 8: floatToRet = 0.1F; return floatToRet * getAgeParryModifier();case 6: case 9: floatToRet = 0.7F; return floatToRet * getAgeParryModifier();case 99: floatToRet = 1.2F; return floatToRet * getAgeParryModifier();
/*      */     } 
/*      */     return floatToRet * getAgeParryModifier();
/*      */   }
/*      */   private float getAgeParryModifier() {
/* 2375 */     if (this.age < 3)
/* 2376 */       return 1.2F; 
/* 2377 */     if (this.age < 8)
/* 2378 */       return 1.1F; 
/* 2379 */     if (this.age < 12)
/* 2380 */       return 0.8F; 
/* 2381 */     if (this.age < 30)
/* 2382 */       return 1.0F; 
/* 2383 */     if (this.age < 40) {
/* 2384 */       return 1.2F;
/*      */     }
/* 2386 */     return 1.3F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final float getDodgeTypeModifier() {
/* 2396 */     float floatToRet = 1.0F;
/* 2397 */     switch (this.modtype) {
/*      */       
/*      */       case 1:
/* 2400 */         floatToRet = 0.94F;
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
/* 2429 */         return floatToRet * getAgeDodgeModifier();case 2: floatToRet = 0.97F; return floatToRet * getAgeDodgeModifier();case 10: floatToRet = 0.9F; return floatToRet * getAgeDodgeModifier();case 6: case 9: floatToRet = 0.95F; return floatToRet * getAgeDodgeModifier();case 7: floatToRet = 1.5F; return floatToRet * getAgeDodgeModifier();case 5: case 8: floatToRet = 0.85F; return floatToRet * getAgeDodgeModifier();case 4: case 11: floatToRet = 1.7F; return floatToRet * getAgeDodgeModifier();case 99: floatToRet = 0.9F; return floatToRet * getAgeDodgeModifier();
/*      */     } 
/*      */     return floatToRet * getAgeDodgeModifier();
/*      */   }
/*      */   private float getAgeDodgeModifier() {
/* 2434 */     if (this.age < 3)
/* 2435 */       return 1.0F; 
/* 2436 */     if (this.age < 8)
/* 2437 */       return 1.1F; 
/* 2438 */     if (this.age < 12)
/* 2439 */       return 1.2F; 
/* 2440 */     if (this.age < 30)
/* 2441 */       return 1.1F; 
/* 2442 */     if (this.age < 40) {
/* 2443 */       return 1.4F;
/*      */     }
/* 2445 */     return 1.5F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getAggTypeModifier() {
/* 2455 */     float floatToRet = 1.0F;
/* 2456 */     switch (this.modtype) {
/*      */       
/*      */       case 1:
/*      */       case 6:
/* 2460 */         floatToRet = 1.3F;
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
/* 2476 */         return floatToRet * getAgeAggModifier();case 3: floatToRet = 1.5F; return floatToRet * getAgeAggModifier();case 2: case 7: floatToRet = 1.1F; return floatToRet * getAgeAggModifier();case 10: case 11: floatToRet = 0.5F; return floatToRet * getAgeAggModifier();
/*      */     } 
/*      */     return floatToRet * getAgeAggModifier();
/*      */   }
/*      */   private float getAgeAggModifier() {
/* 2481 */     if (this.age < 3)
/* 2482 */       return 0.8F; 
/* 2483 */     if (this.age < 8)
/* 2484 */       return 1.3F; 
/* 2485 */     if (this.age < 12)
/* 2486 */       return 1.2F; 
/* 2487 */     if (this.age < 30)
/* 2488 */       return 1.0F; 
/* 2489 */     if (this.age < 40) {
/* 2490 */       return 0.8F;
/*      */     }
/* 2492 */     return 0.6F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getLayer() {
/* 2500 */     return getPosition().getLayer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getZoneId() {
/* 2508 */     return getPosition().getZoneId();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setLoyalty(float paramFloat);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void load() throws Exception;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void savePosition(long paramLong, boolean paramBoolean1, int paramInt, boolean paramBoolean2) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean save() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setKingdom(byte paramByte) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setDead(boolean paramBoolean) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void updateAge(int paramInt) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setDominator(long paramLong);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setReborn(boolean paramBoolean);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setLastPolledLoyalty();
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setOffline(boolean paramBoolean);
/*      */ 
/*      */ 
/*      */   
/*      */   abstract boolean setStayOnline(boolean paramBoolean);
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setDetectionSecs();
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setType(byte paramByte);
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void updateFat() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setInheritance(long paramLong1, long paramLong2, long paramLong3) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setInventoryId(long paramLong) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void saveCreatureName(String paramString) throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setLastGroomed(long paramLong);
/*      */ 
/*      */   
/*      */   abstract void setDisease(byte paramByte);
/*      */ 
/*      */   
/*      */   public boolean isStatusExists() {
/* 2592 */     return this.statusExists;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatusExists(boolean aStatusExists) {
/* 2602 */     this.statusExists = aStatusExists;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CreaturePos getPosition() {
/* 2612 */     return this.position;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosition(CreaturePos aPosition) {
/* 2623 */     this.position = aPosition;
/* 2624 */     if (aPosition != null && 
/* 2625 */       aPosition.getRotation() > 1000.0F) {
/* 2626 */       aPosition.setRotation(aPosition.getRotation() % 360.0F);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isChanged() {
/* 2635 */     return this.changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChanged(boolean aChanged) {
/* 2646 */     this.changed = aChanged;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getModType() {
/* 2651 */     return this.modtype;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */