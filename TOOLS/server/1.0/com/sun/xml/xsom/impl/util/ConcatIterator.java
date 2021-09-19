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
/*    */ public class ConcatIterator
/*    */   implements Iterator
/*    */ {
/*    */   private Iterator lhs;
/*    */   private Iterator rhs;
/*    */   
/*    */   public ConcatIterator(Iterator _lhs, Iterator _rhs) {
/* 25 */     this.lhs = _lhs;
/* 26 */     this.rhs = _rhs;
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 30 */     if (this.lhs != null) {
/* 31 */       if (this.lhs.hasNext()) return true; 
/* 32 */       this.lhs = null;
/*    */     } 
/* 34 */     return this.rhs.hasNext();
/*    */   }
/*    */   
/*    */   public Object next() {
/* 38 */     if (this.lhs != null) return this.lhs.next(); 
/* 39 */     return this.rhs.next();
/*    */   }
/*    */   
/*    */   public void remove() {
/* 43 */     if (this.lhs != null) { this.lhs.remove(); }
/* 44 */     else { this.rhs.remove(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\imp\\util\ConcatIterator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */