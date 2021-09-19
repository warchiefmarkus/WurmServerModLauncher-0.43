/*     */ package com.sun.xml.xsom.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferedCollection<T>
/*     */   implements Collection<T>
/*     */ {
/*     */   private final Iterator<T> result;
/*  24 */   private final List<T> archive = new ArrayList<T>();
/*     */   
/*     */   public DeferedCollection(Iterator<T> result) {
/*  27 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  31 */     if (this.archive.isEmpty())
/*  32 */       fetch(); 
/*  33 */     return this.archive.isEmpty();
/*     */   }
/*     */   
/*     */   public int size() {
/*  37 */     fetchAll();
/*  38 */     return this.archive.size();
/*     */   }
/*     */   
/*     */   public boolean contains(Object o) {
/*  42 */     if (this.archive.contains(o))
/*  43 */       return true; 
/*  44 */     while (this.result.hasNext()) {
/*  45 */       T value = this.result.next();
/*  46 */       this.archive.add(value);
/*  47 */       if (value.equals(o))
/*  48 */         return true; 
/*     */     } 
/*  50 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/*  54 */     for (Object o : c) {
/*  55 */       if (!contains(o))
/*  56 */         return false; 
/*     */     } 
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/*  62 */     return new Iterator<T>() {
/*  63 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  65 */           if (this.idx < DeferedCollection.this.archive.size())
/*  66 */             return true; 
/*  67 */           return DeferedCollection.this.result.hasNext();
/*     */         }
/*     */         
/*     */         public T next() {
/*  71 */           if (this.idx == DeferedCollection.this.archive.size())
/*  72 */             DeferedCollection.this.fetch(); 
/*  73 */           if (this.idx == DeferedCollection.this.archive.size())
/*  74 */             throw new NoSuchElementException(); 
/*  75 */           return (T)DeferedCollection.this.archive.get(this.idx++);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {}
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/*  85 */     fetchAll();
/*  86 */     return this.archive.toArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/*  90 */     fetchAll();
/*  91 */     return this.archive.toArray(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fetchAll() {
/*  97 */     while (this.result.hasNext()) {
/*  98 */       this.archive.add(this.result.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fetch() {
/* 105 */     if (this.result.hasNext()) {
/* 106 */       this.archive.add(this.result.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean add(T o) {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean remove(Object o) {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends T> c) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 127 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 131 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\DeferedCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */