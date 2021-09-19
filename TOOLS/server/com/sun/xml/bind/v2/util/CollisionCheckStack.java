/*     */ package com.sun.xml.bind.v2.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CollisionCheckStack<E>
/*     */   extends AbstractList<E>
/*     */ {
/*     */   private Object[] data;
/*     */   private int[] next;
/*  65 */   private int size = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useIdentity = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] initialHash;
/*     */ 
/*     */ 
/*     */   
/*     */   public CollisionCheckStack() {
/*  78 */     this.initialHash = new int[17];
/*  79 */     this.data = new Object[16];
/*  80 */     this.next = new int[16];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseIdentity(boolean useIdentity) {
/*  88 */     this.useIdentity = useIdentity;
/*     */   }
/*     */   
/*     */   public boolean getUseIdentity() {
/*  92 */     return this.useIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean push(E o) {
/* 102 */     if (this.data.length == this.size) {
/* 103 */       expandCapacity();
/*     */     }
/* 105 */     this.data[this.size] = o;
/* 106 */     int hash = hash(o);
/* 107 */     boolean r = findDuplicate(o, hash);
/* 108 */     this.next[this.size] = this.initialHash[hash];
/* 109 */     this.initialHash[hash] = this.size + 1;
/* 110 */     this.size++;
/* 111 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushNocheck(E o) {
/* 119 */     if (this.data.length == this.size)
/* 120 */       expandCapacity(); 
/* 121 */     this.data[this.size] = o;
/* 122 */     this.next[this.size] = -1;
/* 123 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 128 */     return (E)this.data[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 133 */     return this.size;
/*     */   }
/*     */   
/*     */   private int hash(Object o) {
/* 137 */     return ((this.useIdentity ? System.identityHashCode(o) : o.hashCode()) & Integer.MAX_VALUE) % this.initialHash.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E pop() {
/* 144 */     this.size--;
/* 145 */     Object o = this.data[this.size];
/* 146 */     this.data[this.size] = null;
/* 147 */     int n = this.next[this.size];
/* 148 */     if (n >= 0) {
/*     */ 
/*     */       
/* 151 */       int hash = hash(o);
/* 152 */       assert this.initialHash[hash] == this.size + 1;
/* 153 */       this.initialHash[hash] = n;
/*     */     } 
/* 155 */     return (E)o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peek() {
/* 162 */     return (E)this.data[this.size - 1];
/*     */   }
/*     */   
/*     */   private boolean findDuplicate(E o, int hash) {
/* 166 */     int p = this.initialHash[hash];
/* 167 */     while (p != 0) {
/* 168 */       p--;
/* 169 */       Object existing = this.data[p];
/* 170 */       if (this.useIdentity)
/* 171 */       { if (existing == o) return true;
/*     */          }
/* 173 */       else if (o.equals(existing)) { return true; }
/*     */       
/* 175 */       p = this.next[p];
/*     */     } 
/* 177 */     return false;
/*     */   }
/*     */   
/*     */   private void expandCapacity() {
/* 181 */     int oldSize = this.data.length;
/* 182 */     int newSize = oldSize * 2;
/* 183 */     Object[] d = new Object[newSize];
/* 184 */     int[] n = new int[newSize];
/*     */     
/* 186 */     System.arraycopy(this.data, 0, d, 0, oldSize);
/* 187 */     System.arraycopy(this.next, 0, n, 0, oldSize);
/*     */     
/* 189 */     this.data = d;
/* 190 */     this.next = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 197 */     if (this.size > 0) {
/* 198 */       this.size = 0;
/* 199 */       Arrays.fill(this.initialHash, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCycleString() {
/*     */     Object x;
/* 207 */     StringBuilder sb = new StringBuilder();
/* 208 */     int i = size() - 1;
/* 209 */     E obj = get(i);
/* 210 */     sb.append(obj);
/*     */     
/*     */     do {
/* 213 */       sb.append(" -> ");
/* 214 */       x = get(--i);
/* 215 */       sb.append(x);
/* 216 */     } while (obj != x);
/*     */     
/* 218 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\CollisionCheckStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */