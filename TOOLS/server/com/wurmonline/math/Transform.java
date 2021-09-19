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
/*    */ public class Transform
/*    */ {
/* 15 */   public final Quaternion rotation = new Quaternion();
/* 16 */   public final Vector3f translation = new Vector3f();
/*    */ 
/*    */ 
/*    */   
/*    */   public final void identity() {
/* 21 */     this.rotation.identity();
/* 22 */     this.translation.zero();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Transform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */