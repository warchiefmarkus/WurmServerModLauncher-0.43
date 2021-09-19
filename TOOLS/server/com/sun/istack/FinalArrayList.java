/*    */ package com.sun.istack;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FinalArrayList<T>
/*    */   extends ArrayList<T>
/*    */ {
/*    */   public FinalArrayList(int initialCapacity) {
/* 16 */     super(initialCapacity);
/*    */   }
/*    */ 
/*    */   
/*    */   public FinalArrayList() {}
/*    */   
/*    */   public FinalArrayList(Collection<? extends T> ts) {
/* 23 */     super(ts);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\FinalArrayList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */