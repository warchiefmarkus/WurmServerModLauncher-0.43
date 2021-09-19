/*    */ package com.wurmonline.mesh;
/*    */ 
/*    */ import java.nio.IntBuffer;
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
/*    */ public class Chunk
/*    */ {
/*    */   private static final int CHUNK_WIDTH = 64;
/*    */   private static final int BYTES_PER_TILE = 4;
/*    */   static final int BYTES_PER_CHUNK = 16384;
/* 43 */   private final int[][] tiles = new int[64][64];
/*    */   
/*    */   private boolean dirty = false;
/*    */ 
/*    */   
/*    */   public boolean isDirty() {
/* 49 */     return this.dirty;
/*    */   }
/*    */ 
/*    */   
/*    */   public void makeDirty() {
/* 54 */     this.dirty = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Chunk decode(IntBuffer bb) {
/* 59 */     Chunk chunk = new Chunk();
/* 60 */     for (int i = 0; i < 64; i++) {
/* 61 */       bb.get(chunk.tiles[i]);
/*    */     }
/* 63 */     return chunk;
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(IntBuffer bb) {
/* 68 */     for (int i = 0; i < 64; i++)
/* 69 */       bb.put(this.tiles[i]); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\Chunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */