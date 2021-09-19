/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class AreaSpellEffect
/*     */   implements MiscConstants
/*     */ {
/*     */   private final int tilex;
/*     */   private final int tiley;
/*     */   private final int layer;
/*     */   private final byte type;
/*     */   private final long expireTime;
/*     */   private final long creator;
/*     */   private final float power;
/*     */   private final long id;
/*     */   private final int floorLevel;
/*     */   private final int heightOffset;
/*  41 */   private static final Map<Long, AreaSpellEffect> LAYER_0 = new HashMap<>();
/*  42 */   private static final Map<Long, AreaSpellEffect> LAYER_MINI = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public AreaSpellEffect(long _creator, int _tilex, int _tiley, int _layer, byte _type, long _expireTime, float _power, int _floorLevel, int _heightOffset, boolean loop) {
/*  47 */     this.tilex = _tilex;
/*  48 */     this.tiley = _tiley;
/*  49 */     this.layer = _layer;
/*  50 */     this.type = _type;
/*  51 */     this.expireTime = _expireTime;
/*  52 */     this.power = _power;
/*  53 */     this.floorLevel = _floorLevel;
/*  54 */     this.id = calculateId(this.tilex, this.tiley);
/*  55 */     this.creator = _creator;
/*  56 */     this.heightOffset = _heightOffset;
/*  57 */     addToMap(this);
/*  58 */     addToWorld(this, loop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFloorLevel() {
/*  68 */     return this.floorLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getTilex() {
/*  78 */     return this.tilex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getTiley() {
/*  88 */     return this.tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   int getHeightOffset() {
/*  93 */     return this.heightOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getLayer() {
/* 103 */     return this.layer;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 108 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCreator() {
/* 113 */     return this.creator;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPower() {
/* 118 */     return this.power;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getExpireTime() {
/* 123 */     return this.expireTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 128 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addToMap(AreaSpellEffect sp) {
/* 133 */     switch (sp.layer) {
/*     */       
/*     */       case 0:
/* 136 */         LAYER_0.put(Long.valueOf(sp.id), sp);
/*     */         return;
/*     */       case -1:
/* 139 */         LAYER_MINI.put(Long.valueOf(sp.id), sp);
/*     */         return;
/*     */     } 
/* 142 */     LAYER_0.put(Long.valueOf(sp.id), sp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addToWorld(AreaSpellEffect sp, boolean loop) {
/* 149 */     VolaTile vt = Zones.getOrCreateTile(sp.tilex, sp.tiley, (sp.layer >= 0));
/* 150 */     vt.sendAddTileEffect(sp, loop);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<Long, AreaSpellEffect> getMap(int layer) {
/* 155 */     switch (layer) {
/*     */       
/*     */       case 0:
/* 158 */         return LAYER_0;
/*     */       case -1:
/* 160 */         return LAYER_MINI;
/*     */     } 
/* 162 */     return LAYER_0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long calculateId(int tileX, int tileY) {
/* 168 */     return ((tileX << 16) + tileY);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void pollEffects() {
/* 173 */     pollEffects(LAYER_0, 0);
/* 174 */     pollEffects(LAYER_MINI, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void pollEffects(Map<Long, AreaSpellEffect> map, int layer) {
/* 179 */     AreaSpellEffect[] eff = (AreaSpellEffect[])map.values().toArray((Object[])new AreaSpellEffect[map.size()]);
/* 180 */     long now = System.currentTimeMillis();
/* 181 */     for (int as = 0; as < eff.length; as++) {
/*     */       
/* 183 */       if ((eff[as]).expireTime < now) {
/*     */         
/* 185 */         map.remove(Long.valueOf(eff[as].getId()));
/* 186 */         VolaTile vt = Zones.getOrCreateTile((eff[as]).tilex, (eff[as]).tiley, (layer >= 0));
/* 187 */         vt.sendRemoveTileEffect(eff[as]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeAreaEffect(int tilex, int tiley, int layer) {
/* 194 */     Map<Long, AreaSpellEffect> map = getMap(layer);
/* 195 */     AreaSpellEffect sp = map.remove(Long.valueOf(calculateId(tilex, tiley)));
/* 196 */     if (sp != null) {
/*     */       
/* 198 */       VolaTile vt = Zones.getOrCreateTile(sp.tilex, sp.tiley, (layer >= 0));
/* 199 */       vt.sendRemoveTileEffect(sp);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static AreaSpellEffect getEffect(int tilex, int tiley, int layer) {
/* 205 */     Map<Long, AreaSpellEffect> map = getMap(layer);
/* 206 */     if (map != null)
/*     */     {
/* 208 */       return map.get(Long.valueOf(calculateId(tilex, tiley)));
/*     */     }
/* 210 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\AreaSpellEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */