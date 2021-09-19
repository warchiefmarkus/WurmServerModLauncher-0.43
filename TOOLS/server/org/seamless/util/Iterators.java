/*    */ package org.seamless.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Iterators
/*    */ {
/*    */   public static class Empty<E>
/*    */     implements Iterator<E>
/*    */   {
/*    */     public boolean hasNext() {
/* 19 */       return false;
/*    */     }
/*    */     
/*    */     public E next() {
/* 23 */       throw new NoSuchElementException();
/*    */     }
/*    */     
/*    */     public void remove() {
/* 27 */       throw new UnsupportedOperationException();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Singular<E>
/*    */     implements Iterator<E>
/*    */   {
/*    */     protected final E element;
/*    */     
/*    */     protected int current;
/*    */     
/*    */     public Singular(E element) {
/* 40 */       this.element = element;
/*    */     }
/*    */     
/*    */     public boolean hasNext() {
/* 44 */       return (this.current == 0);
/*    */     }
/*    */     
/*    */     public E next() {
/* 48 */       this.current++;
/* 49 */       return this.element;
/*    */     }
/*    */     
/*    */     public void remove() {
/* 53 */       throw new UnsupportedOperationException();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static abstract class Synchronized<E>
/*    */     implements Iterator<E>
/*    */   {
/*    */     final Iterator<E> wrapped;
/*    */ 
/*    */ 
/*    */     
/* 68 */     int nextIndex = 0;
/*    */     boolean removedCurrent = false;
/*    */     
/*    */     public Synchronized(Collection<E> collection) {
/* 72 */       this.wrapped = (new CopyOnWriteArrayList<E>(collection)).iterator();
/*    */     }
/*    */     
/*    */     public boolean hasNext() {
/* 76 */       return this.wrapped.hasNext();
/*    */     }
/*    */     
/*    */     public E next() {
/* 80 */       this.removedCurrent = false;
/* 81 */       this.nextIndex++;
/* 82 */       return this.wrapped.next();
/*    */     }
/*    */     
/*    */     public void remove() {
/* 86 */       if (this.nextIndex == 0)
/* 87 */         throw new IllegalStateException("Call next() first"); 
/* 88 */       if (this.removedCurrent)
/* 89 */         throw new IllegalStateException("Already removed current, call next()"); 
/* 90 */       synchronizedRemove(this.nextIndex - 1);
/* 91 */       this.removedCurrent = true;
/*    */     }
/*    */     
/*    */     protected abstract void synchronizedRemove(int param1Int);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Iterators.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */