/*     */ package com.sun.xml.xsom.impl.scd;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Iterators
/*     */ {
/*     */   static abstract class ReadOnly<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     public final void remove() {
/*  17 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  22 */   private static final Iterator EMPTY = Collections.EMPTY_LIST.iterator();
/*     */   
/*     */   public static <T> Iterator<T> empty() {
/*  25 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static <T> Iterator<T> singleton(T value) {
/*  29 */     return new Singleton<T>(value);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Singleton<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private T next;
/*     */     
/*     */     Singleton(T next) {
/*  39 */       this.next = next;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  43 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public T next() {
/*  47 */       T r = this.next;
/*  48 */       this.next = null;
/*  49 */       return r;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Adapter<T, U>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends U> core;
/*     */     
/*     */     public Adapter(Iterator<? extends U> core) {
/*  60 */       this.core = core;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  64 */       return this.core.hasNext();
/*     */     }
/*     */     
/*     */     public T next() {
/*  68 */       return filter(this.core.next());
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract T filter(U param1U);
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Map<T, U>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends U> core;
/*     */     
/*     */     private Iterator<? extends T> current;
/*     */     
/*     */     protected Map(Iterator<? extends U> core) {
/*  84 */       this.core = core;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  88 */       while (this.current == null || !this.current.hasNext()) {
/*  89 */         if (!this.core.hasNext())
/*  90 */           return false; 
/*  91 */         this.current = apply(this.core.next());
/*     */       } 
/*  93 */       return true;
/*     */     }
/*     */     
/*     */     public T next() {
/*  97 */       return this.current.next();
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract Iterator<? extends T> apply(U param1U);
/*     */   }
/*     */   
/*     */   public static abstract class Filter<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends T> core;
/*     */     private T next;
/*     */     
/*     */     protected Filter(Iterator<? extends T> core) {
/* 111 */       this.core = core;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract boolean matches(T param1T);
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 120 */       while (this.core.hasNext() && this.next == null) {
/* 121 */         this.next = this.core.next();
/* 122 */         if (!matches(this.next)) {
/* 123 */           this.next = null;
/*     */         }
/*     */       } 
/* 126 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public T next() {
/* 130 */       if (this.next == null) throw new NoSuchElementException(); 
/* 131 */       T r = this.next;
/* 132 */       this.next = null;
/* 133 */       return r;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Unique<T>
/*     */     extends Filter<T>
/*     */   {
/* 141 */     private Set<T> values = new HashSet<T>();
/*     */     public Unique(Iterator<? extends T> core) {
/* 143 */       super(core);
/*     */     }
/*     */     
/*     */     protected boolean matches(T value) {
/* 147 */       return this.values.add(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Union<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends T> first;
/*     */     private final Iterator<? extends T> second;
/*     */     
/*     */     public Union(Iterator<? extends T> first, Iterator<? extends T> second) {
/* 158 */       this.first = first;
/* 159 */       this.second = second;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 163 */       return (this.first.hasNext() || this.second.hasNext());
/*     */     }
/*     */     
/*     */     public T next() {
/* 167 */       if (this.first.hasNext()) return this.first.next(); 
/* 168 */       return this.second.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Array<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final T[] items;
/* 177 */     private int index = 0;
/*     */     public Array(T[] items) {
/* 179 */       this.items = items;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 183 */       return (this.index < this.items.length);
/*     */     }
/*     */     
/*     */     public T next() {
/* 187 */       return this.items[this.index++];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\Iterators.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */