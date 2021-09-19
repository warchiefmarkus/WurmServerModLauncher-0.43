/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.math.TilePos;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import java.util.Date;
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
/*     */ public class Rift
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(Rift.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public Rift(TilePos center, float size) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public TilePos getCenterPos() {
/*  45 */     return new TilePos();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSize() {
/*  50 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(float size) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWave() {
/*  60 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWave(int wave) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPercentWaveCompletion() {
/*  70 */     return 100.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPercentWaveCompletion(float percent, boolean save) {}
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void createTable() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void save(boolean create) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void saveActivated() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void saveEnded() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void loadRifts() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumber() {
/* 100 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumber(int number) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getState() {
/* 110 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(int state) {}
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActive(boolean active) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getInitiated() {
/* 129 */     return new Date(System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitiated(Date initiated, boolean initiate) {}
/*     */ 
/*     */   
/*     */   public Date getActivated() {
/* 138 */     return getInitiated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActivated(Date activated, boolean saveNow) {}
/*     */ 
/*     */   
/*     */   public Date getEnded() {
/* 147 */     return getInitiated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnded(Date ended, boolean saveNow) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void poll() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void activateWave() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void spawnRiftItems() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void spawnTraps() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int getRandomTrapType() {
/* 179 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void spawnCreatures() {}
/*     */ 
/*     */   
/*     */   public static final boolean waterFound(int tilex, int tiley) {
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 193 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescription(String description) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static Rift getActiveRift() {
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setActiveRift(Rift activeRift) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 213 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(byte type) {}
/*     */ 
/*     */   
/*     */   public static Rift getLastRift() {
/* 222 */     return null;
/*     */   }
/*     */   
/*     */   public static void setLastRift(Rift lastRift) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Rift.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */