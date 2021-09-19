/*    */ package com.sun.istack;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*    */ public interface Pool<T>
/*    */ {
/*    */   @NotNull
/*    */   T take();
/*    */   
/*    */   void recycle(@NotNull T paramT);
/*    */   
/*    */   public static abstract class Impl<T>
/*    */     extends ConcurrentLinkedQueue<T>
/*    */     implements Pool<T>
/*    */   {
/*    */     @NotNull
/*    */     public final T take() {
/* 45 */       T t = (T)poll();
/* 46 */       if (t == null)
/* 47 */         return create(); 
/* 48 */       return t;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public final void recycle(T t) {
/* 55 */       offer((E)t);
/*    */     }
/*    */     
/*    */     @NotNull
/*    */     protected abstract T create();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\Pool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */