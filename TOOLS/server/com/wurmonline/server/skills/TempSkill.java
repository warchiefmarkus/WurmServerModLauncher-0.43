/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ public final class TempSkill
/*     */   extends Skill
/*     */ {
/*  20 */   private static Logger logger = Logger.getLogger(TempSkill.class.getName());
/*     */ 
/*     */   
/*     */   public TempSkill(int aNumber, double aStartValue, Skills aParent) {
/*  24 */     super(aNumber, aStartValue, aParent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TempSkill(long aId, Skills aParent, int aNumber, double aKnowledge, double aMinimum, long aLastused) {
/*  30 */     super(aId, aParent, aNumber, aKnowledge, aMinimum, aLastused);
/*     */   }
/*     */ 
/*     */   
/*     */   public TempSkill(long aId, Skills aParent) throws IOException {
/*  35 */     super(aId, aParent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void save() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void load() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void saveValue(boolean aPlayer) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJoat(boolean aJoat) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumber(int newNumber) throws IOException {
/*  69 */     long pid = this.parent.getId();
/*  70 */     if (WurmId.getType(pid) == 0) {
/*     */ 
/*     */       
/*     */       try {
/*  74 */         Player player = Players.getInstance().getPlayer(pid);
/*  75 */         Skill realSkill = player.getSkills().learn(this.number, (float)this.knowledge, false);
/*  76 */         realSkill.setNumber(newNumber);
/*     */       }
/*  78 */       catch (NoSuchPlayerException nsp) {
/*     */         
/*  80 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  87 */         Creature creature = Creatures.getInstance().getCreature(pid);
/*  88 */         Skill realSkill = creature.getSkills().learn(this.number, (float)this.knowledge, false);
/*  89 */         realSkill.setNumber(newNumber);
/*     */       }
/*  91 */       catch (NoSuchCreatureException nsp) {
/*     */         
/*  93 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void alterSkill(double advanceMultiplicator, boolean decay, float times) {
/* 101 */     alterSkill(advanceMultiplicator, decay, times, false, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void alterSkill(double advanceMultiplicator, boolean decay, float times, boolean useNewSystem, double skillDivider) {
/* 108 */     long pid = this.parent.getId();
/* 109 */     if (WurmId.getType(pid) == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 113 */         Player player = Players.getInstance().getPlayer(pid);
/* 114 */         Skill realSkill = player.getSkills().learn(this.number, (float)this.knowledge, false);
/* 115 */         realSkill.alterSkill(advanceMultiplicator, decay, times, useNewSystem, skillDivider);
/*     */       }
/* 117 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 119 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 126 */         Creature creature = Creatures.getInstance().getCreature(pid);
/* 127 */         Skill realSkill = creature.getSkills().learn(this.number, (float)this.knowledge, false);
/* 128 */         realSkill.alterSkill(advanceMultiplicator, decay, times, useNewSystem, skillDivider);
/*     */       }
/* 130 */       catch (NoSuchCreatureException nsc) {
/*     */         
/* 132 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsc);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKnowledge(double aKnowledge, boolean load) {
/* 140 */     long pid = this.parent.getId();
/* 141 */     if (WurmId.getType(pid) == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 145 */         Player player = Players.getInstance().getPlayer(pid);
/* 146 */         Skill realSkill = player.getSkills().learn(this.number, (float)this.knowledge, false);
/* 147 */         realSkill.setKnowledge(aKnowledge, load);
/*     */       }
/* 149 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 151 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 158 */         Creature creature = Creatures.getInstance().getCreature(pid);
/* 159 */         Skill realSkill = creature.getSkills().learn(this.number, (float)this.knowledge, false);
/* 160 */         realSkill.setKnowledge(aKnowledge, load);
/*     */       }
/* 162 */       catch (NoSuchCreatureException nsp) {
/*     */         
/* 164 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKnowledge(double aKnowledge, boolean load, boolean setMinimum) {
/* 172 */     long pid = this.parent.getId();
/* 173 */     if (WurmId.getType(pid) == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 177 */         Player player = Players.getInstance().getPlayer(pid);
/* 178 */         Skill realSkill = player.getSkills().learn(this.number, (float)this.knowledge, false);
/* 179 */         realSkill.setKnowledge(aKnowledge, load, setMinimum);
/*     */       }
/* 181 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 183 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 190 */         Creature creature = Creatures.getInstance().getCreature(pid);
/* 191 */         Skill realSkill = creature.getSkills().learn(this.number, (float)this.knowledge, false);
/* 192 */         realSkill.setKnowledge(aKnowledge, load, setMinimum);
/*     */       }
/* 194 */       catch (NoSuchCreatureException nsp) {
/*     */         
/* 196 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, double bonus, boolean test, float times) {
/* 204 */     return skillCheck(check, bonus, test, 10.0F, true, 1.100000023841858D, (Creature)null, (Creature)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider) {
/* 211 */     return skillCheck(check, bonus, test, 10.0F, true, 1.100000023841858D, (Creature)null, (Creature)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, double bonus, boolean test, float times, @Nullable Creature skillowner, @Nullable Creature opponent) {
/* 220 */     return skillCheck(check, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider, @Nullable Creature skillowner, @Nullable Creature opponent) {
/* 230 */     if (skillowner != null) {
/*     */       
/* 232 */       Skill realSkill = skillowner.getSkills().learn(this.number, (float)this.knowledge, false);
/* 233 */       return realSkill.skillCheck(check, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */     } 
/*     */ 
/*     */     
/* 237 */     long pid = this.parent.getId();
/* 238 */     if (WurmId.getType(pid) == 0) {
/*     */       
/*     */       try {
/*     */         
/* 242 */         Player player = Players.getInstance().getPlayer(pid);
/* 243 */         Skill realSkill = player.getSkills().learn(this.number, (float)this.knowledge, false);
/* 244 */         return realSkill.skillCheck(check, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */       
/*     */       }
/* 247 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 249 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/* 250 */         return 0.0D;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 257 */       Creature creature = Creatures.getInstance().getCreature(pid);
/* 258 */       Skill realSkill = creature.getSkills().learn(this.number, (float)this.knowledge, false);
/* 259 */       return realSkill.skillCheck(check, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */     
/*     */     }
/* 262 */     catch (NoSuchCreatureException nsp) {
/*     */       
/* 264 */       logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/* 265 */       return 0.0D;
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
/*     */   public double skillCheck(double check, Item item, double bonus, boolean test, float times, @Nullable Creature skillowner, @Nullable Creature opponent) {
/* 277 */     return skillCheck(check, item, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, Item item, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider, @Nullable Creature skillowner, @Nullable Creature opponent) {
/* 285 */     if (skillowner != null) {
/*     */       
/* 287 */       Skill realSkill = skillowner.getSkills().learn(this.number, (float)this.knowledge, false);
/* 288 */       return realSkill.skillCheck(check, item, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 293 */     long pid = this.parent.getId();
/* 294 */     if (WurmId.getType(pid) == 0) {
/*     */       
/*     */       try {
/*     */         
/* 298 */         Player player = Players.getInstance().getPlayer(pid);
/* 299 */         Skill realSkill = player.getSkills().learn(this.number, (float)this.knowledge, false);
/* 300 */         return realSkill.skillCheck(check, item, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */       
/*     */       }
/* 303 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 305 */         logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/* 306 */         return 0.0D;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 313 */       Creature creature = Creatures.getInstance().getCreature(pid);
/* 314 */       Skill realSkill = creature.getSkills().learn(this.number, (float)this.knowledge, false);
/* 315 */       return realSkill.skillCheck(check, item, bonus, test, 10.0F, true, 1.100000023841858D, skillowner, opponent);
/*     */     
/*     */     }
/* 318 */     catch (NoSuchCreatureException nsp) {
/*     */       
/* 320 */       logger.log(Level.WARNING, "Unable to find owner for skill, parentid: " + pid, (Throwable)nsp);
/* 321 */       return 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, Item item, double bonus, boolean test, float times) {
/* 330 */     return skillCheck(check, item, bonus, test, 10.0F, true, 1.100000023841858D, (Creature)null, (Creature)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double skillCheck(double check, Item item, double bonus, boolean test, float times, boolean useNewSystem, double skillDivider) {
/* 337 */     return skillCheck(check, item, bonus, test, 10.0F, true, 1.100000023841858D, (Creature)null, (Creature)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isTemporary() {
/* 343 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\TempSkill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */