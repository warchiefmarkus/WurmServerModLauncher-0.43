/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ItemSpellEffects
/*     */ {
/*     */   private static final String GET_ALL_ITEMSPELLEFFECTS = "SELECT * FROM SPELLEFFECTS";
/*     */   private final Map<Byte, SpellEffect> spellEffects;
/*  46 */   private static final Logger logger = Logger.getLogger(ItemSpellEffects.class.getName());
/*     */   
/*  48 */   private static final Map<Long, ItemSpellEffects> itemSpellEffects = new HashMap<>();
/*     */ 
/*     */   
/*     */   public ItemSpellEffects(long _itemId) {
/*  52 */     this.spellEffects = new HashMap<>();
/*  53 */     itemSpellEffects.put(new Long(_itemId), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSpellEffect(SpellEffect effect) {
/*  58 */     SpellEffect old = getSpellEffect(effect.type);
/*  59 */     if (old != null && old.power > effect.power) {
/*     */       
/*  61 */       effect.delete();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  66 */     if (old != null)
/*  67 */       old.delete(); 
/*  68 */     this.spellEffects.put(Byte.valueOf(effect.type), effect);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRandomRuneEffect() {
/*  74 */     for (int i = -128; i <= -51; i++) {
/*     */       
/*  76 */       if (this.spellEffects.containsKey(Byte.valueOf((byte)i))) {
/*  77 */         return (byte)i;
/*     */       }
/*     */     } 
/*  80 */     return -10;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRuneEffect(RuneUtilities.ModifierEffect effect) {
/*  85 */     float toReturn = 1.0F;
/*  86 */     for (int i = -128; i <= -51; i++) {
/*     */       
/*  88 */       if (this.spellEffects.containsKey(Byte.valueOf((byte)i))) {
/*  89 */         toReturn += RuneUtilities.getModifier((byte)i, effect);
/*     */       }
/*     */     } 
/*  92 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumberOfRuneEffects() {
/*  97 */     int toReturn = 0;
/*  98 */     for (int i = -128; i <= -51; i++) {
/*     */       
/* 100 */       if (this.spellEffects.containsKey(Byte.valueOf((byte)i))) {
/* 101 */         toReturn++;
/*     */       }
/*     */     } 
/* 104 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpellEffect getSpellEffect(byte type) {
/* 109 */     if (this.spellEffects.containsKey(Byte.valueOf(type)))
/* 110 */       return this.spellEffects.get(Byte.valueOf(type)); 
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpellEffect[] getEffects() {
/* 116 */     return (SpellEffect[])this.spellEffects.values().toArray((Object[])new SpellEffect[this.spellEffects.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpellEffect removeSpellEffect(byte number) {
/* 121 */     SpellEffect old = getSpellEffect(number);
/* 122 */     if (old != null) {
/*     */       
/* 124 */       old.delete();
/* 125 */       this.spellEffects.remove(Byte.valueOf(number));
/*     */     } 
/* 127 */     return old;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 132 */     SpellEffect[] effects = getEffects();
/* 133 */     for (int x = 0; x < effects.length; x++)
/*     */     {
/* 135 */       effects[x].delete();
/*     */     }
/* 137 */     this.spellEffects.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 142 */     this.spellEffects.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemSpellEffects getSpellEffects(long itemid) {
/* 147 */     return itemSpellEffects.get(new Long(itemid));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadSpellEffectsForItems() {
/* 152 */     long start = System.nanoTime();
/* 153 */     Connection dbcon = null;
/* 154 */     PreparedStatement ps = null;
/* 155 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 158 */       dbcon = DbConnector.getItemDbCon();
/* 159 */       ps = dbcon.prepareStatement("SELECT * FROM SPELLEFFECTS");
/* 160 */       rs = ps.executeQuery();
/* 161 */       int numEffects = 0;
/* 162 */       while (rs.next()) {
/*     */ 
/*     */         
/* 165 */         SpellEffect sp = new SpellEffect(rs.getLong("WURMID"), rs.getLong("ITEMID"), rs.getByte("TYPE"), rs.getFloat("POWER"), rs.getInt("TIMELEFT"), (byte)9, (byte)0);
/*     */         
/* 167 */         Long id = new Long(sp.owner);
/* 168 */         ItemSpellEffects eff = itemSpellEffects.get(id);
/* 169 */         if (eff == null)
/*     */         {
/* 171 */           eff = new ItemSpellEffects(sp.owner);
/*     */         }
/* 173 */         eff.addSpellEffect(sp);
/* 174 */         numEffects++;
/*     */       } 
/* 176 */       logger.log(Level.INFO, "Loaded " + numEffects + " Spell Effects For Items, that took " + (
/* 177 */           (float)(System.nanoTime() - start) / 1000000.0F) + " ms");
/*     */     }
/* 179 */     catch (SQLException sqx) {
/*     */       
/* 181 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 185 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 186 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemSpellEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */