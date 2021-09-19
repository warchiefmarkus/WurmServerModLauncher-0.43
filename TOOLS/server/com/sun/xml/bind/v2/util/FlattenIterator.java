/*    */ package com.sun.xml.bind.v2.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlattenIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   private final Iterator<? extends Map<?, ? extends T>> parent;
/* 52 */   private Iterator<? extends T> child = null;
/*    */   private T next;
/*    */   
/*    */   public FlattenIterator(Iterable<? extends Map<?, ? extends T>> core) {
/* 56 */     this.parent = core.iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 61 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 65 */     getNext();
/* 66 */     return (this.next != null);
/*    */   }
/*    */   
/*    */   public T next() {
/* 70 */     T r = this.next;
/* 71 */     this.next = null;
/* 72 */     if (r == null)
/* 73 */       throw new NoSuchElementException(); 
/* 74 */     return r;
/*    */   }
/*    */   
/*    */   private void getNext() {
/* 78 */     if (this.next != null)
/*    */       return; 
/* 80 */     if (this.child != null && this.child.hasNext()) {
/* 81 */       this.next = this.child.next();
/*    */       
/*    */       return;
/*    */     } 
/* 85 */     if (this.parent.hasNext()) {
/* 86 */       this.child = ((Map)this.parent.next()).values().iterator();
/* 87 */       getNext();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\FlattenIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */