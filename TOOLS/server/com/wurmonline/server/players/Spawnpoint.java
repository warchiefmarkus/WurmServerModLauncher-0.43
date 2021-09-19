/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import com.wurmonline.server.MiscConstants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Spawnpoint
/*    */   implements MiscConstants
/*    */ {
/*    */   public final String description;
/*    */   public final short tilex;
/*    */   public final short tiley;
/*    */   public final boolean surfaced;
/*    */   public final byte number;
/*    */   public final byte kingdom;
/*    */   public final String name;
/*    */   
/*    */   public Spawnpoint(byte num, String desc, short x, short y, boolean surf) {
/* 51 */     this("sp1", num, desc, x, y, surf, (byte)0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Spawnpoint(byte num, String desc, short x, short y, boolean surf, byte kingdomId) {
/* 65 */     this("sp1", num, desc, x, y, surf, kingdomId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Spawnpoint(String initialName, byte num, String desc, short x, short y, boolean surf, byte kingdomId) {
/* 79 */     this.number = num;
/* 80 */     this.description = desc;
/* 81 */     this.tilex = x;
/* 82 */     this.tiley = y;
/* 83 */     this.surfaced = surf;
/* 84 */     this.name = initialName;
/* 85 */     this.kingdom = kingdomId;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Spawnpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */