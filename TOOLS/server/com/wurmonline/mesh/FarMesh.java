/*    */ package com.wurmonline.mesh;
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
/*    */ public class FarMesh
/*    */   extends Mesh
/*    */ {
/*    */   public FarMesh(int width, int height, int meshWidth) {
/* 24 */     super(width, height, meshWidth);
/* 25 */     generateEmpty(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFarTiles(int xStart, int yStart, int w, int h, short[][] tiles) {
/* 30 */     for (int x = 0; x < w; x++) {
/*    */       
/* 32 */       for (int y = 0; y < h; y++) {
/*    */         
/* 34 */         Node node = getNode(x + xStart, y + yStart);
/* 35 */         node.setHeight(Tiles.shortHeightToFloat(tiles[x][y]));
/*    */       } 
/*    */     } 
/*    */     
/* 39 */     calculateNormals(xStart - 1, yStart - 1, xStart + w + 2, yStart + h + 2);
/* 40 */     processData(xStart - 1, yStart - 1, xStart + w + 2, yStart + h + 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\FarMesh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */