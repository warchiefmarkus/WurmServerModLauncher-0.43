/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DContainerPattern
/*    */   extends DPattern
/*    */   implements Iterable<DPattern>
/*    */ {
/*    */   private DPattern head;
/*    */   private DPattern tail;
/*    */   
/*    */   public DPattern firstChild() {
/* 16 */     return this.head;
/*    */   }
/*    */   
/*    */   public DPattern lastChild() {
/* 20 */     return this.tail;
/*    */   }
/*    */   
/*    */   public int countChildren() {
/* 24 */     int i = 0;
/* 25 */     for (DPattern p = firstChild(); p != null; p = p.next)
/* 26 */       i++; 
/* 27 */     return i;
/*    */   }
/*    */   
/*    */   public Iterator<DPattern> iterator() {
/* 31 */     return new Iterator<DPattern>() {
/* 32 */         DPattern next = DContainerPattern.this.head;
/*    */         public boolean hasNext() {
/* 34 */           return (this.next != null);
/*    */         }
/*    */         
/*    */         public DPattern next() {
/* 38 */           DPattern r = this.next;
/* 39 */           this.next = this.next.next;
/* 40 */           return r;
/*    */         }
/*    */         
/*    */         public void remove() {
/* 44 */           throw new UnsupportedOperationException();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   void add(DPattern child) {
/* 50 */     if (this.tail == null) {
/* 51 */       child.prev = child.next = null;
/* 52 */       this.head = this.tail = child;
/*    */     } else {
/* 54 */       child.prev = this.tail;
/* 55 */       this.tail.next = child;
/* 56 */       child.next = null;
/* 57 */       this.tail = child;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DContainerPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */