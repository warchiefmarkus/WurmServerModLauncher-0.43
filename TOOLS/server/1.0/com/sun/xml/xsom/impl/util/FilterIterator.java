/*    */ package 1.0.com.sun.xml.xsom.impl.util;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ public abstract class FilterIterator
/*    */   implements Iterator
/*    */ {
/*    */   private final Iterator core;
/*    */   private Object next;
/*    */   
/*    */   protected FilterIterator(Iterator core) {
/* 26 */     this.core = core;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract boolean allows(Object paramObject);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 38 */     while (this.next == null && this.core.hasNext()) {
/*    */       
/* 40 */       Object o = this.core.next();
/* 41 */       if (allows(o))
/* 42 */         this.next = o; 
/*    */     } 
/* 44 */     return (this.next != null);
/*    */   }
/*    */   
/*    */   public Object next() {
/* 48 */     Object r = this.next;
/* 49 */     this.next = null;
/* 50 */     return r;
/*    */   }
/*    */   
/*    */   public void remove() {
/* 54 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\imp\\util\FilterIterator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */