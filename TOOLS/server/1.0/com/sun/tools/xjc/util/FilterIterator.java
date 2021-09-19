/*    */ package 1.0.com.sun.tools.xjc.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FilterIterator
/*    */   implements Iterator
/*    */ {
/*    */   private final Iterator core;
/*    */   private boolean ready;
/*    */   private boolean noMore;
/*    */   private Object obj;
/*    */   
/*    */   protected abstract boolean test(Object paramObject);
/*    */   
/*    */   public FilterIterator(Iterator _core) {
/* 22 */     this.ready = false;
/* 23 */     this.noMore = false;
/* 24 */     this.obj = null;
/*    */     this.core = _core;
/*    */   } public final Object next() {
/* 27 */     if (!hasNext()) throw new IllegalStateException("no more object"); 
/* 28 */     this.ready = false;
/* 29 */     return this.obj;
/*    */   }
/*    */   
/*    */   public final boolean hasNext() {
/* 33 */     if (this.noMore) return false; 
/* 34 */     if (this.ready) return true;
/*    */     
/* 36 */     while (this.core.hasNext()) {
/* 37 */       Object o = this.core.next();
/* 38 */       if (test(o)) {
/* 39 */         this.obj = o;
/* 40 */         this.ready = true;
/* 41 */         return true;
/*    */       } 
/*    */     } 
/* 44 */     this.noMore = true;
/* 45 */     return false;
/*    */   }
/*    */   
/*    */   public final void remove() {
/* 49 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\FilterIterator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */