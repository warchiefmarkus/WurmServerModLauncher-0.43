/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class SpellEffects
/*     */ {
/*     */   private Creature creature;
/*     */   private final Map<Byte, SpellEffect> spellEffects;
/*  39 */   private static final Logger logger = Logger.getLogger(SpellEffects.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final SpellEffect[] EMPTY_SPELLS = new SpellEffect[0];
/*     */ 
/*     */ 
/*     */   
/*     */   SpellEffects(long _creatureId) {
/*     */     try {
/*  51 */       this.creature = Server.getInstance().getCreature(_creatureId);
/*     */     
/*     */     }
/*  54 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */     
/*  57 */     } catch (NoSuchPlayerException nsp) {
/*     */       
/*  59 */       logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */     } 
/*  61 */     this.spellEffects = new HashMap<>();
/*  62 */     if (WurmId.getType(_creatureId) == 0) {
/*     */       
/*  64 */       SpellEffect[] speffs = SpellEffect.loadEffectsForPlayer(_creatureId);
/*  65 */       for (int x = 0; x < speffs.length; x++)
/*     */       {
/*  67 */         addSpellEffect(speffs[x]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Creature getCreature() {
/*  78 */     return this.creature;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSpellEffect(SpellEffect effect) {
/*  83 */     SpellEffect old = getSpellEffect(effect.type);
/*     */ 
/*     */     
/*  86 */     if (old != null && old.power > effect.power) {
/*     */       
/*  88 */       effect.delete();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  93 */     if (old != null) {
/*     */       
/*  95 */       old.delete();
/*  96 */       if (this.creature != null)
/*     */       {
/*  98 */         this.creature.sendUpdateSpellEffect(effect);
/*     */       }
/*     */     }
/* 101 */     else if (this.creature != null) {
/*     */       
/* 103 */       this.creature.sendAddSpellEffect(effect);
/*     */     } 
/* 105 */     this.spellEffects.put(Byte.valueOf(effect.type), effect);
/*     */     
/* 107 */     if (effect.type == 22 && this.creature.getCurrentTile() != null)
/*     */     {
/* 109 */       this.creature.getCurrentTile().setNewRarityShader(this.creature);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendAllSpellEffects() {
/* 116 */     if (this.creature != null) {
/* 117 */       for (SpellEffect sp : getEffects())
/*     */       {
/* 119 */         this.creature.sendAddSpellEffect(sp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public SpellEffect getSpellEffect(byte type) {
/* 125 */     Byte key = Byte.valueOf(type);
/* 126 */     if (this.spellEffects.containsKey(key))
/* 127 */       return this.spellEffects.get(key); 
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpellEffect[] getEffects() {
/* 133 */     if (this.spellEffects.size() > 0) {
/* 134 */       return (SpellEffect[])this.spellEffects.values().toArray((Object[])new SpellEffect[this.spellEffects.size()]);
/*     */     }
/* 136 */     return EMPTY_SPELLS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void poll() {
/* 141 */     SpellEffect[] effects = getEffects();
/* 142 */     for (int x = 0; x < effects.length; x++) {
/*     */       
/* 144 */       if ((effects[x]).type == 94)
/*     */       {
/* 146 */         if (Server.rand.nextInt(10) == 0) {
/*     */           
/* 148 */           Creature c = getCreature();
/*     */           
/*     */           try {
/* 151 */             c.addWoundOfType(null, (byte)4, c.getBody().getRandomWoundPos(), false, 0.0F, true, (
/* 152 */                 Math.max(20.0F, effects[x].getPower()) * 50.0F), 0.0F, 0.0F, false, true);
/* 153 */             c.getCommunicator().sendAlertServerMessage("The pain from the heat is excruciating!");
/*     */           }
/* 155 */           catch (Exception e) {
/*     */             
/* 157 */             logger.log(Level.WARNING, c.getName() + ": " + e.getMessage());
/*     */           } 
/*     */         } 
/*     */       }
/* 161 */       if (effects[x].poll(this))
/*     */       {
/* 163 */         if ((effects[x]).type == 22) {
/*     */ 
/*     */           
/* 166 */           Creature c = getCreature();
/* 167 */           if (c.getCurrentTile() != null) {
/* 168 */             c.getCurrentTile().setNewRarityShader(c);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SpellEffect removeSpellEffect(SpellEffect old) {
/* 177 */     if (old != null) {
/*     */       
/* 179 */       if (this.creature != null)
/*     */       {
/* 181 */         this.creature.removeSpellEffect(old);
/*     */       }
/* 183 */       old.delete();
/* 184 */       this.spellEffects.remove(Byte.valueOf(old.type));
/*     */     } 
/* 186 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void destroy(boolean keepHunted) {
/* 197 */     SpellEffect[] effects = getEffects();
/* 198 */     SpellEffect hunted = null;
/* 199 */     for (int x = 0; x < effects.length; x++) {
/*     */       
/* 201 */       if ((effects[x]).type != 64 || !keepHunted) {
/*     */         
/* 203 */         if (this.creature != null)
/*     */         {
/* 205 */           if (this.creature.getCommunicator() != null) {
/*     */             
/* 207 */             SpellEffectsEnum spellEffect = SpellEffectsEnum.getEnumByName(effects[x].getName());
/* 208 */             if (spellEffect != SpellEffectsEnum.NONE) {
/* 209 */               this.creature.getCommunicator().sendRemoveSpellEffect((effects[x]).id, spellEffect);
/*     */             }
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 218 */         effects[x].delete();
/*     */       }
/* 220 */       else if ((effects[x]).type == 64) {
/*     */         
/* 222 */         hunted = effects[x];
/*     */       } 
/*     */     } 
/* 225 */     this.spellEffects.clear();
/* 226 */     if (hunted == null) {
/*     */ 
/*     */       
/* 229 */       if (!keepHunted) {
/* 230 */         this.creature = null;
/*     */       }
/*     */     } else {
/* 233 */       this.spellEffects.put(Byte.valueOf((byte)64), hunted);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sleep() {
/* 244 */     this.spellEffects.clear();
/* 245 */     this.creature = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\SpellEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */