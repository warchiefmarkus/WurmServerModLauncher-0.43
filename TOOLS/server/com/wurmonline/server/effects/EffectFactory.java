/*     */ package com.wurmonline.server.effects;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.EffectConstants;
/*     */ import java.io.IOException;
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
/*     */ public final class EffectFactory
/*     */   implements EffectConstants
/*     */ {
/*     */   private static final String GETEFFECTS = "SELECT * FROM EFFECTS";
/*  47 */   private static final Logger logger = Logger.getLogger(EffectFactory.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static EffectFactory instance;
/*     */ 
/*     */ 
/*     */   
/*  55 */   private final Map<Integer, Effect> effects = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EffectFactory getInstance() {
/*  64 */     if (instance == null)
/*  65 */       instance = new EffectFactory(); 
/*  66 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addEffect(Effect effect) {
/*  71 */     addEffect(effect, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addEffect(Effect effect, boolean temp) {
/*  76 */     if (!temp)
/*  77 */       this.effects.put(Integer.valueOf(effect.getId()), effect); 
/*  78 */     int tileX = (int)effect.getPosX() >> 2;
/*  79 */     int tileY = (int)effect.getPosY() >> 2;
/*  80 */     if (effect.isGlobal()) {
/*  81 */       Players.getInstance().sendGlobalNonPersistantEffect(effect.getOwner(), effect.getType(), tileX, tileY, effect
/*  82 */           .getPosZ());
/*     */     } else {
/*     */       
/*     */       try {
/*  86 */         Zone zone = Zones.getZone(tileX, tileY, effect.isOnSurface());
/*  87 */         zone.addEffect(effect, temp);
/*     */       }
/*  89 */       catch (NoSuchZoneException nsz) {
/*     */         
/*  91 */         logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Effect createFire(long id, float posX, float posY, float posZ, boolean surfaced) {
/*  97 */     Effect toReturn = new DbEffect(id, (short)0, posX, posY, posZ, surfaced);
/*  98 */     addEffect(toReturn);
/*  99 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Effect createProjectileLanding(float posX, float posY, float posZ, boolean surfaced) {
/* 104 */     Effect toReturn = new TempEffect(-1L, (short)26, posX, posY, posZ, surfaced);
/* 105 */     addEffect(toReturn, true);
/* 106 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Effect createGenericEffect(long id, String effectName, float posX, float posY, float posZ, boolean surfaced, float timeout, float rotationOffset) {
/* 112 */     Effect toReturn = new TempEffect(id, (short)27, posX, posY, posZ, surfaced);
/* 113 */     toReturn.setEffectString(effectName);
/* 114 */     toReturn.setTimeout(timeout);
/* 115 */     toReturn.setRotationOffset(rotationOffset);
/* 116 */     addEffect(toReturn, (id == -1L));
/* 117 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Effect createGenericTempEffect(String effectName, float posX, float posY, float posZ, boolean surfaced, float timeout, float rotationOffset) {
/* 123 */     return createGenericEffect(-1L, effectName, posX, posY, posZ, surfaced, timeout, rotationOffset);
/*     */   }
/*     */ 
/*     */   
/*     */   public Effect createSpawnEff(long id, float posX, float posY, float posZ, boolean surfaced) {
/* 128 */     Effect toReturn = new DbEffect(id, (short)19, posX, posY, posZ, surfaced);
/* 129 */     addEffect(toReturn);
/* 130 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Effect createChristmasEff(long id, float posX, float posY, float posZ, boolean surfaced) {
/* 135 */     Effect toReturn = new DbEffect(id, (short)4, posX, posY, posZ, surfaced);
/* 136 */     addEffect(toReturn);
/* 137 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void deleteEffByOwner(long id) {
/* 142 */     for (Effect eff : getAllEffects()) {
/*     */       
/* 144 */       if (eff.getOwner() == id) {
/*     */         
/* 146 */         deleteEffect(eff.getId());
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final Effect[] getAllEffects() {
/* 154 */     return (Effect[])this.effects.values().toArray((Object[])new Effect[this.effects.size()]);
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
/*     */   public Effect deleteEffect(int id) {
/* 174 */     Effect toRemove = this.effects.get(Integer.valueOf(id));
/* 175 */     this.effects.remove(Integer.valueOf(id));
/*     */     
/* 177 */     if (toRemove != null) {
/*     */       
/* 179 */       if (toRemove.isGlobal()) {
/* 180 */         Players.getInstance().removeGlobalEffect(toRemove.getOwner());
/*     */       } else {
/*     */         
/* 183 */         int tileX = (int)toRemove.getPosX() >> 2;
/* 184 */         int tileY = (int)toRemove.getPosY() >> 2;
/*     */         
/*     */         try {
/* 187 */           Zone zone = Zones.getZone(tileX, tileY, toRemove.isOnSurface());
/* 188 */           zone.removeEffect(toRemove);
/*     */         }
/* 190 */         catch (NoSuchZoneException nsz) {
/*     */           
/* 192 */           logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*     */         } 
/*     */       } 
/* 195 */       toRemove.delete();
/*     */     } 
/* 197 */     return toRemove;
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
/*     */   public void getEffectsFor(Item item) {
/* 217 */     for (Effect effect : this.effects.values()) {
/*     */       
/* 219 */       if (effect.getOwner() == item.getWurmId()) {
/*     */         
/* 221 */         effect.setPosX(item.getPosX());
/* 222 */         effect.setPosY(item.getPosY());
/* 223 */         effect.setSurfaced(item.isOnSurface());
/* 224 */         item.addEffect(effect);
/* 225 */         int tileX = (int)effect.getPosX() >> 2;
/* 226 */         int tileY = (int)effect.getPosY() >> 2;
/*     */         
/*     */         try {
/* 229 */           Zone zone = Zones.getZone(tileX, tileY, effect.isOnSurface());
/* 230 */           zone.addEffect(effect, false);
/*     */         }
/* 232 */         catch (NoSuchZoneException nsz) {
/*     */           
/* 234 */           logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Effect getEffectForOwner(long id) {
/* 242 */     for (Effect eff : getAllEffects()) {
/* 243 */       if (eff.getOwner() == id)
/* 244 */         return eff; 
/*     */     } 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadEffects() throws IOException {
/* 251 */     long start = System.nanoTime();
/* 252 */     Connection dbcon = null;
/* 253 */     PreparedStatement ps = null;
/* 254 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 257 */       dbcon = DbConnector.getItemDbCon();
/* 258 */       ps = dbcon.prepareStatement("SELECT * FROM EFFECTS");
/* 259 */       rs = ps.executeQuery();
/* 260 */       while (rs.next())
/*     */       {
/* 262 */         float posX = rs.getFloat("POSX");
/* 263 */         float posY = rs.getFloat("POSY");
/* 264 */         float posZ = rs.getFloat("POSZ");
/* 265 */         short type = rs.getShort("TYPE");
/* 266 */         long owner = rs.getLong("OWNER");
/* 267 */         long startTime = rs.getLong("STARTTIME");
/* 268 */         int id = rs.getInt("ID");
/* 269 */         DbEffect effect = new DbEffect(id, owner, type, posX, posY, posZ, startTime);
/* 270 */         this.effects.put(Integer.valueOf(id), effect);
/*     */       }
/*     */     
/* 273 */     } catch (SQLException sqx) {
/*     */       
/* 275 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 279 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 280 */       DbConnector.returnConnection(dbcon);
/* 281 */       long end = System.nanoTime();
/* 282 */       logger.info("Loaded " + this.effects.size() + " effects from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\effects\EffectFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */