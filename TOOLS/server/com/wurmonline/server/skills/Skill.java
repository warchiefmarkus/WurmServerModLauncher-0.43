/*      */ package com.wurmonline.server.skills;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.modifiers.DoubleValueModifier;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.IOException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Skill
/*      */   implements MiscConstants, CounterTypes, TimeConstants, Comparable<Skill>
/*      */ {
/*      */   public long lastUsed;
/*   94 */   protected double knowledge = 1.0D;
/*      */   
/*      */   private static final double regainMultiplicator = 3.0D;
/*      */   
/*      */   public double minimum;
/*      */   
/*      */   boolean joat = false;
/*      */   
/*      */   int number;
/*      */   
/*      */   private static final double maxBonus = 70.0D;
/*  105 */   public static final Logger affinityDebug = Logger.getLogger("affinities");
/*  106 */   private static int totalAffinityChecks = 0;
/*  107 */   private static int totalAffinitiesGiven = 0;
/*      */   
/*      */   Skills parent;
/*      */   
/*  111 */   private static Logger logger = Logger.getLogger(Skill.class.getName());
/*  112 */   public int affinity = 0;
/*      */ 
/*      */   
/*      */   private static final float affinityMultiplier = 0.1F;
/*      */   
/*  117 */   public long id = -10L;
/*  118 */   private Set<DoubleValueModifier> modifiers = null;
/*  119 */   private byte saveCounter = 0;
/*  120 */   private static Random random = new Random();
/*  121 */   private static final byte[][] chances = calculateChances();
/*  122 */   private static final double skillMod = Servers.localServer.EPIC ? 3.0D : 1.5D;
/*      */   
/*      */   private static final double maxSkillGain = 1.0D;
/*      */   private boolean basicPersonal = false;
/*      */   private boolean noCurve = false;
/*  127 */   protected static final boolean isChallenge = Servers.localServer.isChallengeServer();
/*      */ 
/*      */   
/*      */   Skill(int aNumber, double startValue, Skills aParent) {
/*  131 */     this.number = aNumber;
/*  132 */     this.knowledge = Math.max(1.0D, startValue);
/*  133 */     this.minimum = startValue;
/*  134 */     this.parent = aParent;
/*  135 */     if (aParent.isPersonal()) {
/*      */       
/*  137 */       if (WurmId.getType(aParent.getId()) == 0) {
/*      */         
/*  139 */         this.id = isTemporary() ? WurmId.getNextTemporarySkillId() : WurmId.getNextPlayerSkillId();
/*  140 */         if (SkillSystem.getTypeFor(aNumber) == 0 || 
/*  141 */           SkillSystem.getTypeFor(this.number) == 1) {
/*      */           
/*  143 */           this.knowledge = Math.max(1.0D, startValue);
/*  144 */           this.minimum = this.knowledge;
/*  145 */           this.basicPersonal = true;
/*  146 */           this.noCurve = true;
/*      */         } 
/*      */       } else {
/*      */         
/*  150 */         this.id = isTemporary() ? WurmId.getNextTemporarySkillId() : WurmId.getNextCreatureSkillId();
/*  151 */       }  if (this.number == 10076) {
/*  152 */         this.noCurve = true;
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
/*      */   Skill(long _id, int _number, double _knowledge, double _minimum, long _lastused) {
/*  167 */     this.id = _id;
/*  168 */     this.number = _number;
/*  169 */     this.knowledge = _knowledge;
/*  170 */     this.minimum = _minimum;
/*  171 */     this.lastUsed = _lastused;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirty() {
/*  176 */     return (this.saveCounter > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Skill(long _id, Skills _parent, int _number, double _knowledge, double _minimum, long _lastused) {
/*  185 */     this.id = _id;
/*  186 */     this.parent = _parent;
/*  187 */     this.number = _number;
/*  188 */     this.knowledge = _knowledge;
/*  189 */     this.minimum = _minimum;
/*  190 */     this.lastUsed = _lastused;
/*  191 */     if (WurmId.getType(this.parent.getId()) == 0) {
/*      */       
/*  193 */       if (SkillSystem.getTypeFor(this.number) == 0 || 
/*  194 */         SkillSystem.getTypeFor(this.number) == 1) {
/*      */         
/*  196 */         this.basicPersonal = true;
/*  197 */         this.noCurve = true;
/*      */       } 
/*  199 */       if (this.number == 10076) {
/*  200 */         this.noCurve = true;
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
/*      */   public int compareTo(Skill otherSkill) {
/*  214 */     return getName().compareTo(otherSkill.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   private static final byte[][] calculateChances() {
/*  219 */     logger.log(Level.INFO, "Calculating skill chances...");
/*  220 */     long start = System.nanoTime();
/*  221 */     byte[][] toReturn = (byte[][])null;
/*      */     
/*      */     try {
/*  224 */       toReturn = DbSkill.loadSkillChances();
/*  225 */       if (toReturn == null)
/*  226 */         throw new WurmServerException("Load failed. Creating chances."); 
/*  227 */       logger.log(Level.INFO, "Loaded skill chances succeeded.");
/*      */     }
/*  229 */     catch (Exception ex) {
/*      */       
/*  231 */       toReturn = new byte[101][101];
/*      */       
/*  233 */       for (int x = 0; x < 101; x++) {
/*      */         
/*  235 */         for (int y = 0; y < 101; y++) {
/*      */           
/*  237 */           if (x == 0) {
/*      */             
/*  239 */             toReturn[x][y] = 0;
/*      */           }
/*  241 */           else if (y == 0) {
/*      */             
/*  243 */             toReturn[x][y] = 99;
/*      */           }
/*      */           else {
/*      */             
/*  247 */             float succeed = 0.0F;
/*  248 */             for (int i = 0; i < 1000; i++)
/*      */             {
/*      */               
/*  251 */               succeed++;
/*      */             }
/*  253 */             succeed /= 10.0F;
/*  254 */             toReturn[x][y] = (byte)(int)succeed;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  261 */       Thread t = new Thread()
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public void run()
/*      */           {
/*  271 */             Skill.logger.log(Level.INFO, "Starting to slowly build up statistics.");
/*  272 */             byte[][] toSave = new byte[101][101];
/*  273 */             for (int x = 0; x < 101; x++) {
/*      */               
/*  275 */               for (int y = 0; y < 101; y++) {
/*      */                 
/*  277 */                 if (x == 0) {
/*      */                   
/*  279 */                   toSave[x][y] = 0;
/*      */                 }
/*  281 */                 else if (y == 0) {
/*      */                   
/*  283 */                   toSave[x][y] = 99;
/*      */                 
/*      */                 }
/*      */                 else {
/*      */                   
/*  288 */                   float succeed = 0.0F;
/*  289 */                   for (int t2 = 0; t2 < 30000; t2++) {
/*      */                     
/*  291 */                     if (Skill.rollGaussian(x, y, 0L, "test") > 0.0F)
/*  292 */                       succeed++; 
/*      */                   } 
/*  294 */                   succeed /= 300.0F;
/*  295 */                   toSave[x][y] = (byte)(int)succeed;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/*      */             try {
/*  301 */               Skill.logger.log(Level.INFO, "Saving skill chances.");
/*  302 */               DbSkill.saveSkillChances(toSave);
/*      */             }
/*  304 */             catch (Exception ex2) {
/*      */               
/*  306 */               Skill.logger.log(Level.WARNING, "Saving failed.", ex2);
/*      */             } 
/*      */           }
/*      */         };
/*  310 */       t.setPriority(3);
/*  311 */       t.start();
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  316 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  317 */       logger.info("Done. Loading/Calculating skill chances from the database took " + lElapsedTime + " millis.");
/*      */     } 
/*      */     
/*  320 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   Skill(long aId, Skills aParent) throws IOException {
/*  325 */     this.id = aId;
/*  326 */     this.parent = aParent;
/*  327 */     load();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addModifier(DoubleValueModifier modifier) {
/*  332 */     if (this.modifiers == null)
/*  333 */       this.modifiers = new HashSet<>(); 
/*  334 */     this.modifiers.add(modifier);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeModifier(DoubleValueModifier modifier) {
/*  339 */     if (this.modifiers != null) {
/*  340 */       this.modifiers.remove(modifier);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean ignoresEnemy() {
/*  345 */     return SkillSystem.ignoresEnemies(this.number);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getModifierValues() {
/*  350 */     double toReturn = 0.0D;
/*  351 */     if (this.modifiers != null)
/*      */     {
/*  353 */       for (Iterator<DoubleValueModifier> it = this.modifiers.iterator(); it.hasNext();)
/*  354 */         toReturn += ((DoubleValueModifier)it.next()).getModifier(); 
/*      */     }
/*  356 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setParent(Skills skills) {
/*  365 */     this.parent = skills;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  374 */     return SkillSystem.getNameFor(this.number);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumber() {
/*  383 */     return this.number;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getId() {
/*  392 */     return this.id;
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
/*      */   public double getKnowledge() {
/*  408 */     return this.knowledge;
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
/*      */   public double getKnowledge(double bonus) {
/*  420 */     if (bonus > 70.0D)
/*  421 */       bonus = 70.0D; 
/*  422 */     double bonusKnowledge = this.knowledge;
/*  423 */     if (this.number == 102 || this.number == 105) {
/*      */       
/*  425 */       long parentId = this.parent.getId();
/*  426 */       if (parentId != -10L) {
/*      */         
/*      */         try {
/*      */           
/*  430 */           Creature holder = Server.getInstance().getCreature(parentId);
/*      */           
/*  432 */           float hellStrength = holder.getBonusForSpellEffect((byte)40);
/*  433 */           float forestGiantStrength = holder.getBonusForSpellEffect((byte)25);
/*  434 */           if (hellStrength > 0.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  440 */             double pow = 0.8D;
/*  441 */             double target = Math.pow(this.knowledge / 100.0D, 0.8D) * 100.0D;
/*  442 */             double diff = target - this.knowledge;
/*  443 */             bonusKnowledge += diff * hellStrength / 100.0D;
/*      */           }
/*  445 */           else if (forestGiantStrength > 0.0F && this.number == 102) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  451 */             double pow = 0.6D;
/*  452 */             double target = Math.pow(this.knowledge / 100.0D, 0.6D) * 100.0D;
/*  453 */             double diff = target - this.knowledge;
/*  454 */             bonusKnowledge += diff * forestGiantStrength / 100.0D;
/*      */           } 
/*      */           
/*  457 */           float ws = holder.getBonusForSpellEffect((byte)41);
/*  458 */           if (ws > 0.0F)
/*      */           {
/*  460 */             bonusKnowledge *= 0.800000011920929D;
/*      */           }
/*      */         }
/*  463 */         catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */         
/*      */         }
/*  467 */         catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  473 */     if (bonus != 0.0D) {
/*      */       
/*  475 */       double linearMax = (100.0D + bonusKnowledge) / 2.0D;
/*      */       
/*  477 */       double diffToMaxChange = Math.min(bonusKnowledge, linearMax - bonusKnowledge);
/*  478 */       double newBon = diffToMaxChange * bonus / 100.0D;
/*  479 */       bonusKnowledge += newBon;
/*      */     } 
/*      */ 
/*      */     
/*  483 */     bonusKnowledge = Math.max(1.0D, bonusKnowledge * (1.0D + getModifierValues()));
/*      */ 
/*      */ 
/*      */     
/*  487 */     if (!this.parent.paying) {
/*      */       
/*  489 */       if (!this.basicPersonal || Servers.localServer.PVPSERVER) {
/*  490 */         return Math.min(bonusKnowledge, 20.0D);
/*      */       }
/*  492 */       return Math.min(bonusKnowledge, 30.0D);
/*      */     } 
/*  494 */     if (this.noCurve)
/*  495 */       return bonusKnowledge; 
/*  496 */     return Server.getModifiedPercentageEffect(bonusKnowledge);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getKnowledge(Item item, double bonus) {
/*  502 */     if (item == null || item.isBodyPart())
/*  503 */       return getKnowledge(bonus); 
/*  504 */     if (this.number == 1023) {
/*      */       
/*      */       try {
/*      */         
/*  508 */         int primweaponskill = item.getPrimarySkill();
/*  509 */         Skill pw = null;
/*      */         
/*      */         try {
/*  512 */           pw = this.parent.getSkill(primweaponskill);
/*  513 */           bonus += pw.getKnowledge(item, 0.0D);
/*      */         }
/*  515 */         catch (NoSuchSkillException nss) {
/*      */           
/*  517 */           pw = this.parent.learn(primweaponskill, 1.0F);
/*  518 */           bonus += pw.getKnowledge(item, 0.0D);
/*      */         }
/*      */       
/*  521 */       } catch (NoSuchSkillException noSuchSkillException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  527 */     double bonusKnowledge = 0.0D;
/*  528 */     double ql = item.getCurrentQualityLevel();
/*      */     
/*  530 */     if (bonus > 70.0D) {
/*  531 */       bonus = 70.0D;
/*      */     }
/*  533 */     if (ql <= this.knowledge) {
/*      */       
/*  535 */       bonusKnowledge = (this.knowledge + ql) / 2.0D;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  540 */       double diff = ql - this.knowledge;
/*  541 */       bonusKnowledge = this.knowledge + this.knowledge * diff / 100.0D;
/*      */     } 
/*      */     
/*  544 */     if (this.number == 102) {
/*      */       
/*  546 */       long parentId = this.parent.getId();
/*  547 */       if (parentId != -10L) {
/*      */         
/*      */         try {
/*      */           
/*  551 */           Creature holder = Server.getInstance().getCreature(parentId);
/*      */           
/*  553 */           float hs = holder.getBonusForSpellEffect((byte)40);
/*  554 */           if (hs > 0.0F) {
/*      */             
/*  556 */             if (this.knowledge < 40.0D)
/*      */             {
/*  558 */               double diff = 40.0D - this.knowledge;
/*  559 */               bonusKnowledge += diff * hs / 100.0D;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  564 */             float x = holder.getBonusForSpellEffect((byte)25);
/*  565 */             if (x > 0.0F)
/*      */             {
/*  567 */               if (this.knowledge < 40.0D) {
/*      */                 
/*  569 */                 double diff = 40.0D - this.knowledge;
/*  570 */                 bonusKnowledge += diff * x / 100.0D;
/*      */               } 
/*      */             }
/*      */           } 
/*      */           
/*  575 */           float ws = holder.getBonusForSpellEffect((byte)41);
/*  576 */           if (ws > 0.0F)
/*      */           {
/*  578 */             bonusKnowledge *= 0.800000011920929D;
/*      */           }
/*      */         }
/*  581 */         catch (NoSuchPlayerException nsp) {
/*      */           
/*  583 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */         }
/*  585 */         catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  591 */     if (bonus != 0.0D) {
/*      */       
/*  593 */       double linearMax = (100.0D + bonusKnowledge) / 2.0D;
/*      */       
/*  595 */       double diffToMaxChange = Math.min(bonusKnowledge, linearMax - bonusKnowledge);
/*  596 */       double newBon = diffToMaxChange * bonus / 100.0D;
/*  597 */       bonusKnowledge += newBon;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  604 */     bonusKnowledge = Math.max(1.0D, bonusKnowledge * (1.0D + getModifierValues()));
/*      */     
/*  606 */     if (!this.parent.paying) {
/*      */       
/*  608 */       if (!this.basicPersonal || Servers.localServer.PVPSERVER) {
/*  609 */         return Math.min(bonusKnowledge, 20.0D);
/*      */       }
/*  611 */       return Math.min(bonusKnowledge, 30.0D);
/*      */     } 
/*  613 */     if (this.basicPersonal)
/*  614 */       return bonusKnowledge; 
/*  615 */     return Server.getModifiedPercentageEffect(bonusKnowledge);
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
/*      */   public final double getRealKnowledge() {
/*  627 */     if (this.parent.paying)
/*  628 */       return getKnowledge(); 
/*  629 */     if (!this.basicPersonal || Servers.localServer.PVPSERVER) {
/*  630 */       return Math.min(getKnowledge(), 20.0D);
/*      */     }
/*  632 */     return Math.min(getKnowledge(), 30.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKnowledge(double aKnowledge, boolean load) {
/*  640 */     setKnowledge(aKnowledge, load, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKnowledge(double aKnowledge, boolean load, boolean setMinimum) {
/*  648 */     if (aKnowledge < 100.0D) {
/*      */       
/*  650 */       double oldknowledge = this.knowledge;
/*  651 */       this.knowledge = Math.max(Math.min(aKnowledge, 100.0D), 1.0D);
/*      */       
/*  653 */       checkTitleChange(oldknowledge, this.knowledge);
/*  654 */       if (!load) {
/*      */         
/*  656 */         if (setMinimum) {
/*  657 */           this.minimum = this.knowledge;
/*      */         }
/*      */         try {
/*  660 */           save();
/*      */         }
/*  662 */         catch (IOException iox) {
/*      */           
/*  664 */           logger.log(Level.INFO, "Failed to save skill " + this.id, iox);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  669 */         long parentId = this.parent.getId();
/*  670 */         if (parentId != -10L)
/*      */         {
/*  672 */           if (WurmId.getType(parentId) == 0) {
/*      */             
/*      */             try {
/*      */               
/*  676 */               Player holder = Players.getInstance().getPlayer(parentId);
/*      */               
/*  678 */               double bonusKnowledge = this.knowledge;
/*  679 */               if (this.number == 102) {
/*      */                 
/*  681 */                 float hs = holder.getBonusForSpellEffect((byte)40);
/*  682 */                 if (hs > 0.0F) {
/*      */                   
/*  684 */                   if (this.knowledge < 40.0D)
/*      */                   {
/*  686 */                     double diff = 40.0D - this.knowledge;
/*  687 */                     bonusKnowledge = this.knowledge + diff * hs / 100.0D;
/*      */                   }
/*      */                 
/*      */                 } else {
/*      */                   
/*  692 */                   float x = holder.getBonusForSpellEffect((byte)25);
/*  693 */                   if (x > 0.0F)
/*      */                   {
/*  695 */                     if (this.knowledge < 40.0D) {
/*      */                       
/*  697 */                       double diff = 40.0D - this.knowledge;
/*  698 */                       bonusKnowledge = this.knowledge + diff * x / 100.0D;
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */                 
/*  703 */                 float ws = holder.getBonusForSpellEffect((byte)41);
/*  704 */                 if (ws > 0.0F)
/*      */                 {
/*  706 */                   bonusKnowledge *= 0.800000011920929D;
/*      */                 }
/*      */               } 
/*      */               
/*  710 */               if (!this.parent.paying && !this.basicPersonal) {
/*  711 */                 bonusKnowledge = Math.min(20.0D, bonusKnowledge);
/*  712 */               } else if (!this.parent.paying && bonusKnowledge > 20.0D) {
/*  713 */                 bonusKnowledge = Math.min(getKnowledge(0.0D), bonusKnowledge);
/*  714 */               }  holder.getCommunicator().sendUpdateSkill(this.number, (float)bonusKnowledge, 
/*  715 */                   isTemporary() ? 0 : this.affinity);
/*      */             }
/*  717 */             catch (NoSuchPlayerException nsp) {
/*      */               
/*  719 */               logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public double getMinimumValue() {
/*  729 */     return this.minimum;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public int[] getDependencies() {
/*  735 */     return SkillSystem.getDependenciesFor(this.number);
/*      */   }
/*      */ 
/*      */   
/*      */   public int[] getUniqueDependencies() {
/*  740 */     int[] fDeps = getDependencies();
/*  741 */     Set<Integer> lst = new HashSet<>();
/*  742 */     for (int i = 0; i < fDeps.length; i++) {
/*      */       
/*  744 */       Integer val = Integer.valueOf(fDeps[i]);
/*  745 */       if (!lst.contains(val)) {
/*  746 */         lst.add(val);
/*      */       }
/*      */     } 
/*  749 */     int[] deps = new int[lst.size()];
/*  750 */     int ind = 0;
/*  751 */     for (Integer integer : lst) {
/*      */       
/*  753 */       deps[ind] = integer.intValue();
/*  754 */       ind++;
/*      */     } 
/*      */     
/*  757 */     return deps;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDifficulty(boolean checkPriest) {
/*  762 */     return SkillSystem.getDifficultyFor(this.number, checkPriest);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getType() {
/*  767 */     return SkillSystem.getTypeFor(this.number);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider) {
/*  809 */     return skillCheck(check, bonus, test, times, useNewSystem, skillDivider, null, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, double bonus, boolean test, float times) {
/*  814 */     return skillCheck(check, bonus, test, 10.0F, true, 2.0D);
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
/*      */   public double skillCheck(double check, double bonus, boolean test, float times, @Nullable Creature skillowner, @Nullable Creature opponent) {
/*  833 */     return skillCheck(check, bonus, test, 10.0F, true, 2.0D, skillowner, opponent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider, @Nullable Creature skillowner, @Nullable Creature opponent) {
/*  842 */     if (skillowner != null && opponent != null)
/*      */     {
/*  844 */       if (this.number == 10055 || this.number == 10053 || this.number == 10054);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  870 */     touch();
/*  871 */     double power = checkAdvance(check, null, bonus, test, times, useNewSystem, skillDivider);
/*  872 */     if (WurmId.getType(this.parent.getId()) == 0) {
/*      */       
/*      */       try {
/*      */         
/*  876 */         save();
/*      */       
/*      */       }
/*  879 */       catch (IOException iOException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  886 */     return power;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, Item item, double bonus, boolean test, float times, @Nullable Creature skillowner, @Nullable Creature opponent) {
/*  894 */     return skillCheck(check, item, bonus, test, 10.0F, true, 2.0D, skillowner, opponent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, Item item, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider, @Nullable Creature skillowner, @Nullable Creature opponent) {
/*  903 */     if (skillowner == null || opponent != null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  922 */     touch();
/*  923 */     double power = checkAdvance(check, item, bonus, test, times, useNewSystem, skillDivider);
/*  924 */     if (WurmId.getType(this.parent.getId()) == 0) {
/*      */       
/*      */       try {
/*      */         
/*  928 */         save();
/*      */       
/*      */       }
/*  931 */       catch (IOException iOException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  938 */     return power;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, Item item, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider) {
/*  944 */     return skillCheck(check, item, bonus, test, times, useNewSystem, skillDivider, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double skillCheck(double check, Item item, double bonus, boolean test, float times) {
/*  950 */     return skillCheck(check, item, bonus, test, 10.0F, true, 2.0D, null, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getDecayTime() {
/*  955 */     return SkillSystem.getDecayTimeFor(this.number);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void touch() {
/*  961 */     if (SkillSystem.getTickTimeFor(getNumber()) <= 0L) {
/*  962 */       this.lastUsed = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   long getLastUsed() {
/*  971 */     return this.lastUsed;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean mayUpdateTimedSkill() {
/*  976 */     return (System.currentTimeMillis() - this.lastUsed < SkillSystem.getTickTimeFor(getNumber()));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkDecay() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void decay(boolean saved) {
/* 1058 */     float decrease = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1086 */     if (getType() == 1) {
/*      */       
/* 1088 */       alterSkill(-(100.0D - this.knowledge) / getDifficulty(false) * this.knowledge, true, 1.0F);
/*      */     }
/* 1090 */     else if (getType() == 0) {
/*      */       
/* 1092 */       decrease = -0.1F;
/* 1093 */       if (this.affinity > 0)
/* 1094 */         decrease = -0.1F + 0.05F * this.affinity; 
/* 1095 */       if (saved) {
/* 1096 */         alterSkill((decrease / 2.0F), true, 1.0F);
/*      */       } else {
/* 1098 */         alterSkill(decrease, true, 1.0F);
/*      */       } 
/*      */     } else {
/*      */       
/* 1102 */       decrease = -0.25F;
/* 1103 */       if (this.affinity > 0)
/* 1104 */         decrease = -0.25F + 0.025F * this.affinity; 
/* 1105 */       if (saved) {
/* 1106 */         alterSkill((decrease / 2.0F), true, 1.0F);
/*      */       } else {
/* 1108 */         alterSkill(decrease, true, 1.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getParentBonus() {
/* 1119 */     double bonus = 0.0D;
/* 1120 */     int[] dep = getDependencies();
/* 1121 */     for (int x = 0; x < dep.length; x++) {
/*      */       
/* 1123 */       short sType = SkillSystem.getTypeFor(dep[x]);
/* 1124 */       if (sType == 2) {
/*      */         
/*      */         try {
/*      */           
/* 1128 */           Skill enhancer = this.parent.getSkill(dep[x]);
/* 1129 */           double ebonus = enhancer.getKnowledge(0.0D);
/* 1130 */           bonus += ebonus;
/*      */         }
/* 1132 */         catch (NoSuchSkillException ex) {
/*      */           
/* 1134 */           logger.log(Level.WARNING, "Skill.checkAdvance(): Skillsystem bad. Skill '" + getName() + "' has no enhance parent with number " + dep[x] + ". Learning!", (Throwable)ex);
/*      */           
/* 1136 */           this.parent.learn(dep[x], 1.0F);
/*      */         } 
/*      */       }
/*      */     } 
/* 1140 */     return bonus;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getChance(double check, @Nullable Item item, double bonus) {
/* 1146 */     bonus += getParentBonus();
/* 1147 */     double skill = this.knowledge;
/* 1148 */     if (bonus != 0.0D || item != null)
/*      */     {
/* 1150 */       if (item == null) {
/* 1151 */         skill = getKnowledge(bonus);
/*      */       } else {
/* 1153 */         skill = getKnowledge(item, bonus);
/*      */       }  } 
/* 1155 */     if (skill < 1.0D)
/* 1156 */       skill = 1.0D; 
/* 1157 */     if (check < 1.0D) {
/* 1158 */       check = 1.0D;
/*      */     }
/* 1160 */     if (item != null && item.getSpellEffects() != null) {
/*      */       
/* 1162 */       float skillBonus = (float)((100.0D - skill) * (item.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SKILLCHECKBONUS) - 1.0F));
/* 1163 */       skill += skillBonus;
/*      */     } 
/*      */     
/* 1166 */     return getGaussianChance(skill, check);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final double getGaussianChance(double skill, double difficulty) {
/* 1171 */     if (skill > 99.0D || difficulty > 99.0D) {
/* 1172 */       return Math.max(0.0D, Math.min(100.0D, ((skill * skill * skill - difficulty * difficulty * difficulty) / 50000.0D + skill - difficulty) / 2.0D + 50.0D + 0.5D * (skill - difficulty)));
/*      */     }
/*      */     
/* 1175 */     return chances[(int)skill][(int)difficulty];
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
/*      */   public static final float rollGaussian(float skill, float difficulty, long parentId, String name) {
/* 1198 */     float slide = (skill * skill * skill - difficulty * difficulty * difficulty) / 50000.0F + skill - difficulty;
/*      */ 
/*      */     
/* 1201 */     float w = 30.0F - Math.abs(skill - difficulty) / 4.0F;
/* 1202 */     int attempts = 0;
/*      */     
/* 1204 */     float result = 0.0F;
/*      */ 
/*      */     
/*      */     do {
/* 1208 */       result = (float)random.nextGaussian() * (w + Math.abs(slide) / 6.0F) + slide;
/*      */ 
/*      */ 
/*      */       
/* 1212 */       float rejectCutoff = (float)random.nextGaussian() * (w - Math.abs(slide) / 6.0F) + slide;
/* 1213 */       if (slide > 0.0F) {
/*      */         
/* 1215 */         if (result > rejectCutoff + Math.max(100.0F - slide, 0.0F)) {
/* 1216 */           result = -1000.0F;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1221 */       else if (result < rejectCutoff - Math.max(100.0F + slide, 0.0F)) {
/* 1222 */         result = -1000.0F;
/*      */       } 
/*      */ 
/*      */       
/* 1226 */       attempts++;
/* 1227 */       if (attempts != 100) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1234 */       if (result > 100.0F)
/* 1235 */         return 90.0F + Server.rand.nextFloat() * 5.0F; 
/* 1236 */       if (result < -100.0F) {
/* 1237 */         return -90.0F - Server.rand.nextFloat() * 5.0F;
/*      */       }
/*      */     }
/* 1240 */     while (result < -100.0F || result > 100.0F);
/*      */     
/* 1242 */     return result;
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
/*      */   private double checkAdvance(double check, @Nullable Item item, double bonus, boolean dryRun, float times, boolean useNewSystem, double skillDivider) {
/* 1267 */     if (!dryRun)
/* 1268 */       dryRun = mayUpdateTimedSkill(); 
/* 1269 */     check = Math.max(1.0D, check);
/* 1270 */     short skillType = SkillSystem.getTypeFor(this.number);
/*      */     
/* 1272 */     int[] dep = getUniqueDependencies();
/*      */     
/* 1274 */     for (int x = 0; x < dep.length; x++) {
/*      */       
/* 1276 */       short sType = SkillSystem.getTypeFor(dep[x]);
/* 1277 */       if (sType == 2) {
/*      */ 
/*      */         
/*      */         try {
/* 1281 */           Skill enhancer = this.parent.getSkill(dep[x]);
/*      */           
/* 1283 */           double ebonus = Math.max(0.0D, enhancer
/* 1284 */               .skillCheck(check, 0.0D, dryRun, times, useNewSystem, skillDivider) / 10.0D);
/* 1285 */           bonus += ebonus;
/*      */         }
/* 1287 */         catch (NoSuchSkillException ex) {
/*      */           
/* 1289 */           Creature cret = null;
/*      */           
/*      */           try {
/* 1292 */             cret = Server.getInstance().getCreature(this.parent.getId());
/*      */           }
/* 1294 */           catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */           
/* 1297 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */           
/* 1300 */           String name = "Unknown creature";
/* 1301 */           if (cret != null)
/* 1302 */             name = cret.getName(); 
/* 1303 */           logger.log(Level.WARNING, name + " - Skill.checkAdvance(): Skillsystem bad. Skill '" + getName() + "' has no enhance parent with number " + dep[x], (Throwable)ex);
/*      */           
/* 1305 */           this.parent.learn(dep[x], 1.0F);
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1312 */           Skill par = this.parent.getSkill(dep[x]);
/* 1313 */           if (par.getNumber() != 1023) {
/* 1314 */             par.skillCheck(check, 0.0D, dryRun, times, useNewSystem, skillDivider);
/*      */           }
/* 1316 */         } catch (NoSuchSkillException ex) {
/*      */           
/* 1318 */           Creature cret = null;
/*      */           
/*      */           try {
/* 1321 */             cret = Server.getInstance().getCreature(this.parent.getId());
/*      */           }
/* 1323 */           catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */           
/* 1326 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */           
/* 1329 */           String name = "Unknown creature";
/* 1330 */           if (cret != null)
/* 1331 */             name = cret.getName(); 
/* 1332 */           logger.log(Level.WARNING, name + ": Skill.checkAdvance(): Skillsystem bad. Skill '" + getName() + "' has no limiting parent with number " + dep[x], (Throwable)ex);
/*      */           
/* 1334 */           this.parent.learn(dep[x], 1.0F);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1339 */     bonus = Math.min(70.0D, bonus);
/* 1340 */     double skill = this.knowledge;
/* 1341 */     double learnMod = 1.0D;
/* 1342 */     if (item == null) {
/* 1343 */       skill = getKnowledge(bonus);
/*      */     } else {
/*      */       
/* 1346 */       skill = getKnowledge(item, bonus);
/* 1347 */       if (item.getSpellSkillBonus() > 0.0F) {
/* 1348 */         learnMod += (item.getSpellSkillBonus() / 100.0F);
/*      */       }
/*      */     } 
/* 1351 */     if (item != null && item.getSpellEffects() != null) {
/*      */       
/* 1353 */       float skillBonus = (float)((100.0D - skill) * (item.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SKILLCHECKBONUS) - 1.0F));
/* 1354 */       skill += skillBonus;
/*      */     } 
/* 1356 */     double power = rollGaussian((float)skill, (float)check, this.parent.getId(), getName());
/*      */ 
/*      */ 
/*      */     
/* 1360 */     if (!dryRun)
/*      */     {
/* 1362 */       if (useNewSystem) {
/*      */         
/* 1364 */         double divs = skillDivider;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1380 */         doSkillGainNew(check, power, learnMod, times, divs);
/*      */       }
/*      */       else {
/*      */         
/* 1384 */         doSkillGainOld(power, learnMod, times);
/*      */       } 
/*      */     }
/* 1387 */     if (power > 0.0D) {
/*      */       
/* 1389 */       Player p = Players.getInstance().getPlayerOrNull(this.parent.getId());
/* 1390 */       if (p != null) {
/*      */         
/* 1392 */         totalAffinityChecks++;
/* 1393 */         if (p.shouldGiveAffinity(this.affinity, (skillType == 1 || skillType == 0))) {
/*      */           
/* 1395 */           if (this.affinity == 0) {
/* 1396 */             p.getCommunicator().sendNormalServerMessage("You realize that you have developed an affinity for " + 
/* 1397 */                 SkillSystem.getNameFor(this.number).toLowerCase() + ".", (byte)2);
/*      */           } else {
/* 1399 */             p.getCommunicator().sendNormalServerMessage("You realize that your affinity for " + 
/* 1400 */                 SkillSystem.getNameFor(this.number).toLowerCase() + " has grown stronger.", (byte)2);
/*      */           } 
/* 1402 */           Affinities.setAffinity(p.getWurmId(), this.number, this.affinity + 1, false);
/* 1403 */           totalAffinitiesGiven++;
/* 1404 */           affinityDebug.log(Level.INFO, p.getName() + " gained affinity for skill " + SkillSystem.getNameFor(this.number) + " from skill usage. New affinity: " + this.affinity + ". Total checks this restart: " + totalAffinityChecks + " Total affinities given this restart: " + totalAffinitiesGiven);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1411 */     return power;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void doSkillGainNew(double check, double power, double learnMod, float times, double skillDivider) {
/* 1417 */     double bonus = 1.0D;
/* 1418 */     double diff = Math.abs(check - this.knowledge);
/* 1419 */     short sType = SkillSystem.getTypeFor(this.number);
/* 1420 */     boolean awardBonus = true;
/* 1421 */     if (sType == 1 || sType == 0) {
/* 1422 */       awardBonus = false;
/*      */     }
/* 1424 */     if (diff <= 15.0D && awardBonus)
/*      */     {
/* 1426 */       bonus = 1.0D + 0.10000000149011612D * diff / 15.0D;
/*      */     }
/* 1428 */     if (power < 0.0D) {
/*      */       
/* 1430 */       if (this.knowledge < 20.0D)
/*      */       {
/* 1432 */         alterSkill((100.0D - this.knowledge) / getDifficulty(this.parent.priest) * this.knowledge * this.knowledge * learnMod * bonus, false, times, true, skillDivider);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1439 */       alterSkill((100.0D - this.knowledge) / getDifficulty(this.parent.priest) * this.knowledge * this.knowledge * learnMod * bonus, false, times, true, skillDivider);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void doSkillGainOld(double power, double learnMod, float times) {
/* 1446 */     if (power >= 0.0D)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1452 */       if (this.knowledge < 20.0D) {
/*      */         
/* 1454 */         alterSkill((100.0D - this.knowledge) / getDifficulty(this.parent.priest) * this.knowledge * this.knowledge * learnMod, false, times);
/*      */       
/*      */       }
/* 1457 */       else if (power > 0.0D && power < 40.0D) {
/*      */ 
/*      */         
/* 1460 */         alterSkill((100.0D - this.knowledge) / getDifficulty(this.parent.priest) * this.knowledge * this.knowledge * learnMod, false, times);
/*      */       
/*      */       }
/* 1463 */       else if (this.number == 10055 || this.number == 10053 || this.number == 10054) {
/*      */ 
/*      */         
/* 1466 */         Creature cret = null;
/*      */         
/*      */         try {
/* 1469 */           cret = Server.getInstance().getCreature(this.parent.getId());
/* 1470 */           if (cret.loggerCreature1 > 0L) {
/* 1471 */             logger.log(Level.INFO, cret
/*      */                 
/* 1473 */                 .getName() + " POWER=" + power);
/*      */           }
/* 1475 */         } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */         
/* 1478 */         } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void alterSkill(double advanceMultiplicator, boolean decay, float times) {
/* 1488 */     alterSkill(advanceMultiplicator, decay, times, false, 1.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void alterSkill(double advanceMultiplicator, boolean decay, float times, boolean useNewSystem, double skillDivider) {
/* 1495 */     if (this.parent.hasSkillGain) {
/*      */       
/* 1497 */       times = Math.min((SkillSystem.getTickTimeFor(getNumber()) > 0L || 
/* 1498 */           getNumber() == 10033) ? 100.0F : 30.0F, times);
/* 1499 */       advanceMultiplicator *= (times * Servers.localServer.getSkillGainRate());
/* 1500 */       this.lastUsed = System.currentTimeMillis();
/* 1501 */       boolean isplayer = false;
/* 1502 */       long pid = this.parent.getId();
/* 1503 */       if (WurmId.getType(pid) == 0)
/*      */       {
/* 1505 */         isplayer = true;
/*      */       }
/* 1507 */       double oldknowledge = this.knowledge;
/* 1508 */       if (decay) {
/*      */         
/* 1510 */         if (isplayer)
/*      */         {
/* 1512 */           if (this.knowledge <= 70.0D)
/*      */             return; 
/* 1514 */           double villageMod = 1.0D;
/*      */           
/*      */           try {
/* 1517 */             Player player = Players.getInstance().getPlayer(pid);
/*      */             
/* 1519 */             villageMod = player.getVillageSkillModifier();
/*      */           }
/* 1521 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1523 */             logger.log(Level.WARNING, "Player with id " + this.id + " is decaying skills while not online?", (Throwable)nsp);
/*      */           } 
/* 1525 */           this.knowledge = Math.max(1.0D, this.knowledge + advanceMultiplicator * villageMod);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */           
/* 1531 */           this.knowledge = Math.max(1.0D, this.knowledge + advanceMultiplicator);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1538 */         advanceMultiplicator *= skillMod;
/*      */         
/* 1540 */         if (this.number == 10086 && Servers.localServer.isChallengeOrEpicServer() && 
/* 1541 */           !Server.getInstance().isPS())
/* 1542 */           advanceMultiplicator *= 2.0D; 
/* 1543 */         if (isplayer) {
/*      */           
/*      */           try {
/*      */             
/* 1547 */             Player player = Players.getInstance().getPlayer(pid);
/* 1548 */             advanceMultiplicator *= (1.0F + ItemBonus.getSkillGainBonus((Creature)player, getNumber()));
/* 1549 */             int currstam = player.getStatus().getStamina();
/* 1550 */             float staminaMod = 1.0F;
/* 1551 */             if (currstam <= 400)
/*      */             {
/* 1553 */               staminaMod = 0.1F;
/*      */             }
/*      */             
/* 1556 */             if (player.getCultist() != null && player.getCultist().levelElevenSkillgain()) {
/* 1557 */               staminaMod *= 1.25F;
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 1562 */             if (player.getDeity() != null) {
/*      */               
/* 1564 */               if (player.mustChangeTerritory() && !player.isFighting()) {
/*      */                 
/* 1566 */                 staminaMod = 0.1F;
/* 1567 */                 if (Server.rand.nextInt(100) == 0)
/*      */                 {
/* 1569 */                   player.getCommunicator().sendAlertServerMessage("You sense a lack of energy. Rumours have it that " + 
/* 1570 */                       (player.getDeity()).name + " wants " + player
/* 1571 */                       .getDeity().getHisHerItsString() + " champions to move between kingdoms and seek out the enemy.");
/*      */                 }
/*      */               } 
/*      */               
/* 1575 */               if (player.getDeity().isLearner()) {
/*      */                 
/* 1577 */                 if (player.getFaith() > 20.0F && player.getFavor() >= 10.0F)
/*      */                 {
/* 1579 */                   staminaMod += 0.1F;
/*      */                 }
/*      */               }
/* 1582 */               else if (player.getDeity().isWarrior()) {
/*      */                 
/* 1584 */                 if (player.getFaith() > 20.0F && player.getFavor() >= 20.0F)
/*      */                 {
/* 1586 */                   if (isFightingSkill()) {
/* 1587 */                     staminaMod += 0.25F;
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1599 */             staminaMod += Math.max(player.getStatus().getNutritionlevel() / 10.0F - 0.05F, 0.0F);
/* 1600 */             if (player.isFighting() && currstam <= 400) {
/* 1601 */               staminaMod = 0.0F;
/*      */             }
/* 1603 */             advanceMultiplicator *= staminaMod;
/* 1604 */             if (player.getEnemyPresense() > Player.minEnemyPresence && 
/* 1605 */               !ignoresEnemy())
/* 1606 */               advanceMultiplicator *= 0.800000011920929D; 
/* 1607 */             if (this.knowledge < this.minimum || (this.basicPersonal && this.knowledge < 20.0D))
/*      */             {
/* 1609 */               advanceMultiplicator *= 3.0D;
/*      */             }
/* 1611 */             if (player.hasSleepBonus()) {
/* 1612 */               advanceMultiplicator *= 2.0D;
/*      */             }
/* 1614 */             int taffinity = this.affinity + (AffinitiesTimed.isTimedAffinity(pid, getNumber()) ? 1 : 0);
/* 1615 */             advanceMultiplicator *= (1.0F + taffinity * 0.1F);
/*      */             
/* 1617 */             if ((player.getMovementScheme()).samePosCounts > 20)
/*      */             {
/* 1619 */               advanceMultiplicator = 0.0D;
/*      */             }
/* 1621 */             if (!player.isPaying() && this.knowledge >= 20.0D) {
/*      */               
/* 1623 */               advanceMultiplicator = 0.0D;
/* 1624 */               if (!player.isPlayerAssistant() && Server.rand.nextInt(500) == 0) {
/* 1625 */                 player.getCommunicator().sendNormalServerMessage("You may only gain skill beyond level 20 if you have a premium account.", (byte)2);
/*      */               }
/*      */             } 
/*      */             
/* 1629 */             if (this.number == 10055 || this.number == 10053 || this.number == 10054)
/*      */             {
/*      */ 
/*      */               
/* 1633 */               if (player.loggerCreature1 > 0L) {
/* 1634 */                 logger.log(Level.INFO, player
/*      */                     
/* 1636 */                     .getName() + " advancing " + 
/* 1637 */                     Math.min(1.0D, advanceMultiplicator * this.knowledge / skillDivider) + "!");
/*      */               
/*      */               }
/*      */             }
/*      */           }
/* 1642 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1644 */             advanceMultiplicator = 0.0D;
/* 1645 */             logger.log(Level.WARNING, "Player with id " + this.id + " is learning skills while not online?", (Throwable)nsp);
/*      */           } 
/*      */         }
/* 1648 */         if (useNewSystem) {
/*      */           
/* 1650 */           double maxSkillRate = 40.0D;
/* 1651 */           double rateMod = 1.0D;
/* 1652 */           short sType = SkillSystem.getTypeFor(this.number);
/* 1653 */           if (sType == 1 || sType == 0) {
/*      */             
/* 1655 */             maxSkillRate = 60.0D;
/* 1656 */             rateMod = 0.8D;
/*      */           } 
/*      */           
/* 1659 */           double skillRate = Math.min(maxSkillRate, skillDivider * (1.0D + this.knowledge / (100.0D - 90.0D * this.knowledge / 110.0D)) * rateMod);
/* 1660 */           this
/* 1661 */             .knowledge = Math.max(1.0D, this.knowledge + Math.min(1.0D, advanceMultiplicator * this.knowledge / skillRate));
/*      */         }
/*      */         else {
/*      */           
/* 1665 */           this.knowledge = Math.max(1.0D, this.knowledge + Math.min(1.0D, advanceMultiplicator * this.knowledge));
/*      */         } 
/* 1667 */         if (this.minimum < this.knowledge)
/*      */         {
/* 1669 */           this.minimum = this.knowledge;
/*      */         }
/* 1671 */         checkTitleChange(oldknowledge, this.knowledge);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1678 */         if ((oldknowledge != this.knowledge && (this.saveCounter == 0 || this.knowledge > 50.0D)) || decay) {
/* 1679 */           saveValue(isplayer);
/*      */         }
/* 1681 */         this.saveCounter = (byte)(this.saveCounter + 1);
/* 1682 */         if (this.saveCounter == 10) {
/* 1683 */           this.saveCounter = 0;
/*      */         }
/* 1685 */       } catch (IOException ex) {
/*      */         
/* 1687 */         logger.log(Level.WARNING, "Failed to save skill " + 
/* 1688 */             getName() + "(" + getNumber() + ") for creature " + this.parent.getId(), ex);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1695 */       if (pid != -10L)
/*      */       {
/* 1697 */         if (isplayer) {
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 1702 */             Player holder = Players.getInstance().getPlayer(pid);
/* 1703 */             float weakMod = 1.0F;
/*      */             
/* 1705 */             double bonusKnowledge = this.knowledge;
/* 1706 */             float ws = holder.getBonusForSpellEffect((byte)41);
/* 1707 */             if (ws > 0.0F)
/*      */             {
/* 1709 */               weakMod = 0.8F;
/*      */             }
/* 1711 */             if (this.number == 102 && this.knowledge < 40.0D) {
/*      */               
/* 1713 */               float x = holder.getBonusForSpellEffect((byte)25);
/* 1714 */               if (x > 0.0F) {
/*      */                 
/* 1716 */                 double diff = 40.0D - this.knowledge;
/* 1717 */                 bonusKnowledge = this.knowledge + diff * x / 100.0D;
/*      */               }
/*      */               else {
/*      */                 
/* 1721 */                 float hs = holder.getBonusForSpellEffect((byte)40);
/* 1722 */                 if (hs > 0.0F) {
/*      */                   
/* 1724 */                   double diff = 40.0D - this.knowledge;
/* 1725 */                   bonusKnowledge = this.knowledge + diff * hs / 100.0D;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/* 1730 */             bonusKnowledge *= weakMod;
/* 1731 */             if (isplayer) {
/*      */               
/* 1733 */               int diff = (int)this.knowledge - (int)oldknowledge;
/* 1734 */               if (diff > 0)
/*      */               {
/* 1736 */                 holder.achievement(371, diff);
/*      */               }
/*      */             } 
/* 1739 */             if (!this.parent.paying && !this.basicPersonal) {
/* 1740 */               bonusKnowledge = Math.min(20.0D, bonusKnowledge);
/* 1741 */             } else if (!this.parent.paying && bonusKnowledge > 20.0D) {
/* 1742 */               bonusKnowledge = Math.min(getKnowledge(0.0D), bonusKnowledge);
/* 1743 */             }  holder.getCommunicator().sendUpdateSkill(this.number, (float)bonusKnowledge, isTemporary() ? 0 : this.affinity);
/*      */ 
/*      */             
/* 1746 */             if (this.number != 2147483644 && this.number != 2147483642) {
/* 1747 */               holder.resetInactivity(true);
/*      */             }
/* 1749 */           } catch (NoSuchPlayerException nsp) {
/*      */             
/* 1751 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTemporary() {
/* 1760 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFightingSkill() {
/* 1765 */     return SkillSystem.isFightingSkill(this.number);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkInitialTitle() {
/* 1773 */     if (getNumber() == 10067) {
/*      */       
/* 1775 */       long pid = this.parent.getId();
/* 1776 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1778 */         if (this.knowledge >= 20.0D) {
/*      */           
/* 1780 */           Player p = Players.getInstance().getPlayerOrNull(pid);
/* 1781 */           if (p != null)
/* 1782 */             p.maybeTriggerAchievement(605, true); 
/*      */         } 
/* 1784 */         if (this.knowledge >= 50.0D) {
/*      */           
/* 1786 */           Player p = Players.getInstance().getPlayerOrNull(pid);
/* 1787 */           if (p != null)
/* 1788 */             p.maybeTriggerAchievement(617, true); 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1792 */     if (this.knowledge >= 50.0D) {
/*      */       
/* 1794 */       long pid = this.parent.getId();
/* 1795 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1797 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.NORMAL);
/* 1798 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1802 */             Players.getInstance().getPlayer(pid).addTitle(title);
/*      */           }
/* 1804 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1806 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 1811 */       Player p = Players.getInstance().getPlayerOrNull(pid);
/* 1812 */       if (p != null)
/* 1813 */         p.maybeTriggerAchievement(555, true); 
/*      */     } 
/* 1815 */     if (this.knowledge >= 70.0D) {
/*      */       
/* 1817 */       long pid = this.parent.getId();
/* 1818 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1820 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.MINOR);
/* 1821 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1825 */             Players.getInstance().getPlayer(pid).addTitle(title);
/*      */           }
/* 1827 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1829 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 1834 */       Player p = Players.getInstance().getPlayerOrNull(pid);
/* 1835 */       if (p != null)
/* 1836 */         p.maybeTriggerAchievement(564, true); 
/* 1837 */       if (p != null && getNumber() == 10066)
/* 1838 */         p.maybeTriggerAchievement(633, true); 
/*      */     } 
/* 1840 */     if (this.knowledge >= 90.0D) {
/*      */       
/* 1842 */       long pid = this.parent.getId();
/* 1843 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1845 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.MASTER);
/* 1846 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1850 */             Players.getInstance().getPlayer(pid).addTitle(title);
/*      */           }
/* 1852 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1854 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 1859 */       Player p = Players.getInstance().getPlayerOrNull(pid);
/* 1860 */       if (p != null)
/* 1861 */         p.maybeTriggerAchievement(590, true); 
/*      */     } 
/* 1863 */     if (this.knowledge >= 99.99999615D) {
/*      */       
/* 1865 */       long pid = this.parent.getId();
/* 1866 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1868 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.LEGENDARY);
/*      */ 
/*      */         
/* 1871 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1875 */             Players.getInstance().getPlayer(pid).addTitle(title);
/*      */           }
/* 1877 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1879 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void checkTitleChange(double oldknowledge, double newknowledge) {
/* 1888 */     if (getNumber() == 10067 && oldknowledge < 20.0D && newknowledge >= 20.0D) {
/*      */       
/* 1890 */       long pid = this.parent.getId();
/* 1891 */       if (WurmId.getType(pid) == 0) {
/*      */         
/*      */         try {
/*      */           
/* 1895 */           Player p = Players.getInstance().getPlayer(pid);
/* 1896 */           p.maybeTriggerAchievement(605, true);
/*      */         }
/* 1898 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 1900 */           logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */         } 
/*      */       }
/*      */     } 
/* 1904 */     if (oldknowledge < 50.0D && newknowledge >= 50.0D) {
/*      */       
/* 1906 */       long pid = this.parent.getId();
/* 1907 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1909 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.NORMAL);
/* 1910 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1914 */             Player p = Players.getInstance().getPlayer(pid);
/* 1915 */             p.addTitle(title);
/* 1916 */             p.achievement(555);
/*      */             
/* 1918 */             if (getNumber() == 10067) {
/* 1919 */               p.maybeTriggerAchievement(617, true);
/*      */             }
/* 1921 */           } catch (NoSuchPlayerException nsp) {
/*      */             
/* 1923 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */         
/* 1927 */         int count = 0;
/* 1928 */         for (Skill s : this.parent.getSkills()) {
/* 1929 */           if (s.getKnowledge() >= 50.0D)
/* 1930 */             count++; 
/*      */         } 
/* 1932 */         if (count >= 10) {
/*      */           
/*      */           try {
/*      */             
/* 1936 */             Player p = Players.getInstance().getPlayer(pid);
/* 1937 */             p.maybeTriggerAchievement(598, true);
/*      */           }
/* 1939 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1946 */     if (oldknowledge < 70.0D && newknowledge >= 70.0D) {
/*      */       
/* 1948 */       long pid = this.parent.getId();
/* 1949 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1951 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.MINOR);
/* 1952 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1956 */             Player p = Players.getInstance().getPlayer(pid);
/* 1957 */             p.addTitle(title);
/* 1958 */             p.achievement(564);
/*      */             
/* 1960 */             if (getNumber() == 10066) {
/* 1961 */               p.maybeTriggerAchievement(633, true);
/*      */             }
/* 1963 */           } catch (NoSuchPlayerException nsp) {
/*      */             
/* 1965 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1970 */     if (oldknowledge < 90.0D && newknowledge >= 90.0D) {
/*      */       
/* 1972 */       long pid = this.parent.getId();
/* 1973 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1975 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.MASTER);
/* 1976 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 1980 */             Player p = Players.getInstance().getPlayer(pid);
/* 1981 */             p.addTitle(title);
/* 1982 */             p.achievement(590);
/*      */           }
/* 1984 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 1986 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1991 */     if (oldknowledge < 99.99999615D && newknowledge >= 99.99999615D) {
/*      */       
/* 1993 */       long pid = this.parent.getId();
/* 1994 */       if (WurmId.getType(pid) == 0) {
/*      */         
/* 1996 */         Titles.Title title = Titles.Title.getTitle(this.number, Titles.TitleType.LEGENDARY);
/* 1997 */         if (title != null) {
/*      */           
/*      */           try {
/*      */             
/* 2001 */             Players.getInstance().getPlayer(pid).addTitle(title);
/*      */           }
/* 2003 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 2005 */             logger.log(Level.WARNING, pid + ":" + nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAffinity(int aff) {
/* 2014 */     this.affinity = aff;
/* 2015 */     long pid = this.parent.getId();
/* 2016 */     if (WurmId.getType(pid) == 0)
/*      */     {
/* 2018 */       if (!isTemporary()) {
/*      */         
/*      */         try {
/* 2021 */           Player holder = Players.getInstance().getPlayer(pid);
/* 2022 */           float weakMod = 1.0F;
/*      */           
/* 2024 */           double bonusKnowledge = this.knowledge;
/* 2025 */           float ws = holder.getBonusForSpellEffect((byte)41);
/* 2026 */           if (ws > 0.0F)
/*      */           {
/* 2028 */             weakMod = 0.8F;
/*      */           }
/* 2030 */           if (this.number == 102 && this.knowledge < 40.0D) {
/*      */             
/* 2032 */             float x = holder.getBonusForSpellEffect((byte)25);
/* 2033 */             if (x > 0.0F) {
/*      */               
/* 2035 */               double diff = 40.0D - this.knowledge;
/* 2036 */               bonusKnowledge = this.knowledge + diff * x / 100.0D;
/*      */             }
/*      */             else {
/*      */               
/* 2040 */               float hs = holder.getBonusForSpellEffect((byte)40);
/* 2041 */               if (hs > 0.0F) {
/*      */                 
/* 2043 */                 double diff = 40.0D - this.knowledge;
/* 2044 */                 bonusKnowledge = this.knowledge + diff * hs / 100.0D;
/*      */               } 
/*      */             } 
/*      */           } 
/* 2048 */           bonusKnowledge *= weakMod;
/*      */           
/* 2050 */           if (!this.parent.paying && !this.basicPersonal) {
/* 2051 */             bonusKnowledge = Math.min(20.0D, bonusKnowledge);
/* 2052 */           } else if (!this.parent.paying && bonusKnowledge > 20.0D) {
/* 2053 */             bonusKnowledge = Math.min(getKnowledge(0.0D), bonusKnowledge);
/* 2054 */           }  holder.getCommunicator().sendUpdateSkill(this.number, (float)bonusKnowledge, this.affinity);
/*      */         }
/* 2056 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 2058 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
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
/*      */   public boolean hasLowCreationGain() {
/* 2083 */     switch (getNumber()) {
/*      */       
/*      */       case 1010:
/*      */       case 10034:
/*      */       case 10036:
/*      */       case 10037:
/*      */       case 10041:
/*      */       case 10042:
/*      */       case 10083:
/*      */       case 10091:
/* 2093 */         return false;
/*      */     } 
/*      */     
/* 2096 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void maybeSetMinimum() {
/* 2101 */     if (this.minimum < this.knowledge) {
/*      */       
/* 2103 */       this.minimum = this.knowledge;
/*      */ 
/*      */       
/*      */       try {
/* 2107 */         save();
/*      */       }
/* 2109 */       catch (IOException iox) {
/*      */         
/* 2111 */         logger.log(Level.INFO, "Failed to save skill " + this.id, iox);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getTotalAffinityChecks() {
/* 2118 */     return totalAffinityChecks;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getTotalAffinitiesGiven() {
/* 2123 */     return totalAffinitiesGiven;
/*      */   }
/*      */   
/*      */   abstract void save() throws IOException;
/*      */   
/*      */   abstract void load() throws IOException;
/*      */   
/*      */   abstract void saveValue(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setJoat(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setNumber(int paramInt) throws IOException;
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\Skill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */