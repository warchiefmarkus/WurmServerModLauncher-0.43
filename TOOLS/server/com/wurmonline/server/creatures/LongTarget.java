/*    */ package com.wurmonline.server.creatures;
/*    */ 
/*    */ import com.wurmonline.server.creatures.ai.PathTile;
/*    */ import com.wurmonline.server.items.Item;
/*    */ 
/*    */ public class LongTarget
/*    */   extends PathTile {
/*    */   private Creature ctarget;
/*    */   private Item itemTarget;
/* 10 */   private long target = -10L;
/* 11 */   private int epicMission = -1;
/* 12 */   private int missionTrigger = -1;
/*    */   private final int startx;
/*    */   private final int starty;
/*    */   private final long startTime;
/*    */   
/*    */   public LongTarget(int tx, int ty, int t, boolean surf, int aFloorLevel, Creature starter) {
/* 18 */     super(tx, ty, t, surf, aFloorLevel);
/* 19 */     this.startx = starter.getTileX();
/* 20 */     this.starty = starter.getTileY();
/* 21 */     this.startTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public Creature getCreatureTarget() {
/* 25 */     return this.ctarget;
/*    */   }
/*    */   
/*    */   public void setCreaturetarget(Creature ctarget) {
/* 29 */     this.ctarget = ctarget;
/*    */   }
/*    */   
/*    */   public Item getItemTarget() {
/* 33 */     return this.itemTarget;
/*    */   }
/*    */   
/*    */   public void setItemTarget(Item itemTarget) {
/* 37 */     this.itemTarget = itemTarget;
/*    */   }
/*    */   
/*    */   public void setTileX(int tileX) {
/* 41 */     this.tilex = tileX;
/*    */   }
/*    */   
/*    */   public void setTileY(int tileY) {
/* 45 */     this.tiley = tileY;
/*    */   }
/*    */   
/*    */   public long getMissionTarget() {
/* 49 */     return this.target;
/*    */   }
/*    */   
/*    */   public void setMissionTarget(long target) {
/* 53 */     this.target = target;
/*    */   }
/*    */   
/*    */   public int getEpicMission() {
/* 57 */     return this.epicMission;
/*    */   }
/*    */   
/*    */   public void setEpicMission(int epicMission) {
/* 61 */     this.epicMission = epicMission;
/*    */   }
/*    */   
/*    */   public int getMissionTrigger() {
/* 65 */     return this.missionTrigger;
/*    */   }
/*    */   
/*    */   public void setMissionTrigger(int missionTrigger) {
/* 69 */     this.missionTrigger = missionTrigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStartx() {
/* 74 */     return this.startx;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStarty() {
/* 79 */     return this.starty;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getStartTime() {
/* 84 */     return this.startTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\LongTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */