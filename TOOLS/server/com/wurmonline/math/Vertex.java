/*    */ package com.wurmonline.math;
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
/*    */ public final class Vertex
/*    */ {
/* 24 */   public float[] point = new float[3];
/*    */   public byte flags;
/* 26 */   public float[] vertex = new float[3];
/*    */   
/*    */   public byte boneId;
/*    */   public byte refCount;
/* 30 */   public long lastRotateTime = 0L;
/* 31 */   public float[] rotatedVertex = new float[3];
/* 32 */   public float[] rotatedNormal = new float[3];
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Vertex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */