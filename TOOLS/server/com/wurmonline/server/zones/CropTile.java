/*    */ package com.wurmonline.server.zones;
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
/*    */ public class CropTile
/*    */   implements Comparable<CropTile>
/*    */ {
/*    */   private int data;
/*    */   private int x;
/*    */   private int y;
/*    */   private int cropType;
/*    */   private boolean onSurface;
/*    */   
/*    */   public CropTile(int tileData, int tileX, int tileY, int typeOfCrop, boolean surface) {
/* 35 */     this.data = tileData;
/* 36 */     this.x = tileX;
/* 37 */     this.y = tileY;
/* 38 */     this.cropType = typeOfCrop;
/* 39 */     this.onSurface = surface;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getData() {
/* 44 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getX() {
/* 49 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getY() {
/* 54 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getCropType() {
/* 59 */     return this.cropType;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isOnSurface() {
/* 64 */     return this.onSurface;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 70 */     if (!(obj instanceof CropTile)) {
/* 71 */       return false;
/*    */     }
/* 73 */     CropTile c = (CropTile)obj;
/*    */     
/* 75 */     return (c.getCropType() == getCropType() && c.getData() == getData() && c
/* 76 */       .getX() == getX() && c.getY() == getY() && c.isOnSurface() == isOnSurface());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(CropTile o) {
/* 86 */     int EQUAL = 0;
/* 87 */     int AFTER = 1;
/* 88 */     int BEFORE = -1;
/* 89 */     if (o.equals(this))
/* 90 */       return 0; 
/* 91 */     if (o.getCropType() > getCropType())
/* 92 */       return 1; 
/* 93 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\CropTile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */