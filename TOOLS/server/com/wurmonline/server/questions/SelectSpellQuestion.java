/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public class SelectSpellQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*     */   private static final boolean useSets = false;
/*     */   
/*     */   public SelectSpellQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  46 */     super(aResponder, aTitle, aQuestion, 80, aTarget);
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
/*     */   private String getDeityPassives(Deity deity, int level) {
/*  58 */     StringBuilder buf = new StringBuilder();
/*     */ 
/*     */     
/*  61 */     if (!deity.isHateGod()) {
/*  62 */       buf.append("text{text=\"You are aligned with the benevolent.\"};");
/*     */     } else {
/*  64 */       buf.append("text{text=\"You are aligned with the malevolent.\"};");
/*     */     } 
/*     */     
/*  67 */     if (deity.isClayAffinity()) {
/*  68 */       buf.append("text{text=\"" + deity.getName() + " is interested in pottery.\"};");
/*     */     }
/*     */     
/*  71 */     if (deity.isClothAffinity()) {
/*  72 */       buf.append("text{text=\"" + deity.getName() + " is interested in cloth.\"};");
/*     */     }
/*     */     
/*  75 */     if (deity.isMetalAffinity()) {
/*  76 */       buf.append("text{text=\"" + deity.getName() + " is interested in metal.\"};");
/*     */     }
/*     */     
/*  79 */     if (deity.isWoodAffinity()) {
/*  80 */       buf.append("text{text=\"" + deity.getName() + " is interested in wood.\"};");
/*     */     }
/*     */     
/*  83 */     if (deity.isMeatAffinity()) {
/*  84 */       buf.append("text{text=\"" + deity.getName() + " is interested in the products of death.\"};");
/*     */     }
/*     */     
/*  87 */     if (deity.isFoodAffinity()) {
/*  88 */       buf.append("text{text=\"" + deity.getName() + " is interested in food.\"};");
/*     */     }
/*  90 */     buf.append("text{text=\"\"};");
/*     */ 
/*     */     
/*  93 */     if (deity.isLearner() && level >= 20) {
/*  94 */       buf.append("text{text=\"You feel capable of learning swiftly.\"};");
/*     */     }
/*     */     
/*  97 */     if (deity.isWarrior() && level >= 20) {
/*  98 */       buf.append("text{text=\"You feel a higher aptitude for combat.\"};");
/*     */     }
/*     */     
/* 101 */     if (deity.isStaminaBonus() && level >= 20) {
/* 102 */       buf.append("text{text=\"You feel able to catch your breath quickly.\"};");
/*     */     }
/*     */     
/* 105 */     if (deity.isFoodBonus() && level >= 20) {
/* 106 */       buf.append("text{text=\"You feel less hungry than normal.\"};");
/*     */     }
/*     */     
/* 109 */     if (deity.isHealer() && level >= 20) {
/* 110 */       buf.append("text{text=\"Your wounds heal more quickly.\"};");
/*     */     }
/*     */     
/* 113 */     if (deity.isForestGod() && level >= 35) {
/* 114 */       buf.append("text{text=\"You feel thorns no longer pierce you.\"};");
/*     */     }
/*     */     
/* 117 */     if (deity.isMountainGod() && level >= 35) {
/* 118 */       buf.append("text{text=\"You feel lava no longer burns you.\"};");
/*     */     }
/*     */     
/* 121 */     if (deity.isFavorRegenerator() && level >= 35) {
/* 122 */       buf.append("text{text=\"You feel your favor comes back to you faster than normal.\"};");
/*     */     }
/*     */     
/* 125 */     if (deity.isWarrior() && level >= 40) {
/* 126 */       buf.append("text{text=\"You feel capable of striking harder in combat.\"};");
/*     */     }
/*     */     
/* 129 */     if (deity.isBefriendCreature() && level >= 60) {
/* 130 */       buf.append("text{text=\"You feel attuned with animals.\"};");
/*     */     }
/*     */     
/* 133 */     if (deity.isBefriendMonster() && level >= 60) {
/* 134 */       buf.append("text{text=\"You feel attuned with monsters.\"};");
/*     */     }
/*     */     
/* 137 */     if (deity.isRoadProtector() && level >= 60) {
/* 138 */       buf.append("text{text=\"You feel light footed on pavement.\"};");
/*     */     }
/*     */     
/* 141 */     if (deity.isHateGod() && level >= 60) {
/* 142 */       buf.append("text{text=\"You feel light footed on mycelium.\"};");
/*     */     }
/*     */     
/* 145 */     if (deity.isMountainGod() && level >= 60) {
/* 146 */       buf.append("text{text=\"You feel light footed on rock and cliffs.\"};");
/*     */     }
/*     */     
/* 149 */     if (deity.isDeathProtector() && level >= 60) {
/* 150 */       buf.append("text{text=\"You feel that " + deity.getName() + " may protect your skills in death.\"};");
/*     */     }
/*     */     
/* 153 */     if (deity.isItemProtector() && level >= 70) {
/* 154 */       buf.append("text{text=\"You feel your possessions resist the elements.\"};");
/*     */     }
/*     */     
/* 157 */     if (deity.isForestGod() && level >= 70) {
/* 158 */       buf.append("text{text=\"You feel light footed on natural land.\"};");
/*     */     }
/*     */     
/* 161 */     if (deity.isDeathItemProtector() && level >= 70) {
/* 162 */       buf.append("text{text=\"You feel that " + deity.getName() + " may protect your items in death.\"};");
/*     */     }
/*     */     
/* 165 */     if (deity.isRepairer() && level >= 80) {
/* 166 */       buf.append("text{text=\"You feel more skilled at improvement.\"};");
/*     */     }
/*     */     
/* 169 */     if (deity.isFo() && level >= 70) {
/* 170 */       buf.append("text{text=\"" + deity.getName() + " makes you more capable in combat when fighting in the wild.\"};");
/*     */     }
/*     */     
/* 173 */     if ((deity.isMagranon() || deity.isLibila()) && level >= 70) {
/* 174 */       buf.append("text{text=\"" + deity.getName() + " makes you more capable in combat when on the offensive.\"};");
/*     */     }
/*     */     
/* 177 */     if (deity.isVynora() && level >= 70) {
/* 178 */       buf.append("text{text=\"" + deity.getName() + " makes you more capable in combat when on the defensive while on pavement or at sea.\"};");
/*     */     }
/* 180 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDeityConnection(int level) {
/* 191 */     if (level < 40)
/* 192 */       return "faint"; 
/* 193 */     if (level < 50)
/* 194 */       return "mild"; 
/* 195 */     if (level < 60)
/* 196 */       return "moderate"; 
/* 197 */     if (level < 70)
/* 198 */       return "good"; 
/* 199 */     if (level < 80)
/* 200 */       return "strong"; 
/* 201 */     if (level < 90)
/* 202 */       return "powerful"; 
/* 203 */     if (level < 100) {
/* 204 */       return "deep";
/*     */     }
/* 206 */     return "perfect";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {}
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
/*     */   private String addSpell(int id, Spell spell) {
/* 252 */     StringBuilder buf = new StringBuilder();
/* 253 */     buf.append("row{id=\"e" + id + "\";hover=\"" + 
/* 254 */         StringUtilities.raiseFirstLetterOnly(spell.getDescription()) + "\";name=\"" + spell
/* 255 */         .getName() + "\";rarity=\"0\";children=\"0\";col{text=\"" + spell.level + "\"};col{text=\"" + spell
/*     */ 
/*     */         
/* 258 */         .getCost() + "\"};col{text=\"" + spell
/* 259 */         .getDifficulty(false) + "\"};col{text=\"" + 
/* 260 */         getTargets(spell) + "\"};col{text=\"" + 
/* 261 */         StringUtilities.raiseFirstLetterOnly(spell.getDescription()) + "\"}}");
/*     */     
/* 263 */     return buf.toString();
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
/*     */   public void sendQuestion() {
/* 275 */     String lHtml = "border{scroll{vertical='true';horizontal='false';varray{rescale='true';passthrough{id='id';text='" + getId() + "'}";
/* 276 */     StringBuilder buf = new StringBuilder(lHtml);
/*     */     
/* 278 */     if (getResponder().getDeity() != null) {
/*     */       String deityConnection;
/* 280 */       Deity deity = getResponder().getDeity();
/* 281 */       int level = (int)getResponder().getFaith();
/* 282 */       Set<Spell> spellset = getResponder().getDeity().getSpells();
/* 283 */       Spell[] spells = spellset.<Spell>toArray(new Spell[spellset.size()]);
/*     */       
/* 285 */       Arrays.sort(spells, new Comparator<Spell>()
/*     */           {
/*     */             
/*     */             public int compare(Spell o1, Spell o2)
/*     */             {
/* 290 */               if (o1.level - o2.level == 0)
/* 291 */                 return o1.name.compareTo(o2.name); 
/* 292 */               return o1.level - o2.level;
/*     */             }
/*     */           });
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
/* 376 */       int available = 0;
/* 377 */       for (Spell s : spells) {
/*     */ 
/*     */         
/* 380 */         if (s.level <= level && (
/* 381 */           !s.isRitual || deity.getFavor() > 100000 || Servers.isThisATestServer()))
/*     */         {
/* 383 */           available++;
/*     */         }
/*     */       } 
/*     */       
/* 387 */       if (!deity.isCustomDeity()) {
/*     */         
/* 389 */         deityConnection = "You have a " + getDeityConnection(level) + " connection to " + deity.getName() + ".";
/*     */       }
/*     */       else {
/*     */         
/* 393 */         Deity templateGod = Deities.getDeity(deity.getTemplateDeity());
/* 394 */         deityConnection = "You have a " + getDeityConnection(level) + " connection to " + deity.getName() + ", demigod of " + templateGod.getName() + ".";
/*     */       } 
/* 396 */       buf.append("text{text=\"" + deityConnection + "\";type=\"bold\"}");
/* 397 */       buf.append("text{text=\"\"}");
/* 398 */       buf.append(getDeityPassives(deity, level));
/* 399 */       buf.append("text{text=\"\"}");
/* 400 */       buf.append("text{text=\"The valid targets in the following table are (T)ile, (Wo)und, (C)reature.\";type=\"bold\"}");
/* 401 */       buf.append("text{text=\"Item-specific targets: (I)tem [any], (W)eapon, (A)rmour, (J)ewelry, (P)endulum.\";type=\"bold\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 406 */       int rowNumb = 0;
/* 407 */       int height = 16 + 16 * available;
/* 408 */       buf.append("tree{id=\"t1\";cols=\"5\";showheader=\"true\";height=\"" + height + "\"col{text=\"Level\";width=\"50\"};col{text=\"Favor\";width=\"50\"};col{text=\"Difficulty\";width=\"50\"};col{text=\"Targets\";width=\"50\"};col{text=\"Description\";width=\"300\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 415 */       for (Spell s : spells) {
/*     */         
/* 417 */         if (s.level <= level)
/*     */         {
/* 419 */           if (!s.isRitual || deity.getFavor() > 100000 || Servers.isThisATestServer()) {
/*     */             
/* 421 */             rowNumb++;
/* 422 */             buf.append(addSpell(rowNumb, s));
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 427 */       buf.append("}");
/*     */     }
/*     */     else {
/*     */       
/* 431 */       buf.append("text{text=\"Fool, you don't even have a deity?\"}");
/* 432 */     }  buf.append(createAnswerButton3());
/* 433 */     getResponder().getCommunicator().sendBml(700, 800, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getTargets(Spell s) {
/* 438 */     boolean addComma = false;
/* 439 */     StringBuilder tbuf = new StringBuilder();
/* 440 */     if (s.isTargetTile()) {
/*     */       
/* 442 */       tbuf.append("T");
/* 443 */       addComma = true;
/*     */     } 
/* 445 */     if (s.isTargetCreature()) {
/*     */       
/* 447 */       if (addComma) {
/* 448 */         tbuf.append(",C");
/*     */       } else {
/* 450 */         tbuf.append("C");
/* 451 */       }  addComma = true;
/*     */     } 
/* 453 */     if (s.isTargetItem()) {
/*     */       
/* 455 */       if (addComma) {
/* 456 */         tbuf.append(",I");
/*     */       } else {
/* 458 */         tbuf.append("I");
/* 459 */       }  addComma = true;
/*     */     } 
/* 461 */     if (s.isTargetWeapon()) {
/*     */       
/* 463 */       if (addComma) {
/* 464 */         tbuf.append(",W");
/*     */       } else {
/* 466 */         tbuf.append("W");
/* 467 */       }  addComma = true;
/*     */     } 
/* 469 */     if (s.isTargetArmour()) {
/*     */       
/* 471 */       if (addComma) {
/* 472 */         tbuf.append(",A");
/*     */       } else {
/* 474 */         tbuf.append("A");
/* 475 */       }  addComma = true;
/*     */     } 
/* 477 */     if (s.isTargetJewelry()) {
/*     */       
/* 479 */       if (addComma) {
/* 480 */         tbuf.append(",J");
/*     */       } else {
/* 482 */         tbuf.append("J");
/* 483 */       }  addComma = true;
/*     */     } 
/* 485 */     if (s.isTargetPendulum()) {
/*     */       
/* 487 */       if (addComma) {
/* 488 */         tbuf.append(",P");
/*     */       } else {
/* 490 */         tbuf.append("P");
/* 491 */       }  addComma = true;
/*     */     } 
/* 493 */     if (s.isTargetWound())
/*     */     {
/* 495 */       if (addComma) {
/* 496 */         tbuf.append(",Wo");
/*     */       } else {
/* 498 */         tbuf.append("Wo");
/*     */       }  } 
/* 500 */     return tbuf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SelectSpellQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */