/*     */ package com.sun.xml.dtdparser;
/*     */ 
/*     */ import java.util.Enumeration;
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
/*     */ final class SimpleHashtable
/*     */   implements Enumeration
/*     */ {
/*     */   private Entry[] table;
/*  47 */   private Entry current = null;
/*  48 */   private int currentBucket = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private int count;
/*     */ 
/*     */   
/*     */   private int threshold;
/*     */ 
/*     */   
/*     */   private static final float loadFactor = 0.75F;
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHashtable(int initialCapacity) {
/*  63 */     if (initialCapacity < 0) {
/*  64 */       throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
/*     */     }
/*  66 */     if (initialCapacity == 0)
/*  67 */       initialCapacity = 1; 
/*  68 */     this.table = new Entry[initialCapacity];
/*  69 */     this.threshold = (int)(initialCapacity * 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHashtable() {
/*  76 */     this(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  82 */     this.count = 0;
/*  83 */     this.currentBucket = 0;
/*  84 */     this.current = null;
/*  85 */     for (int i = 0; i < this.table.length; i++) {
/*  86 */       this.table[i] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  95 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration keys() {
/* 105 */     this.currentBucket = 0;
/* 106 */     this.current = null;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMoreElements() {
/* 115 */     if (this.current != null)
/* 116 */       return true; 
/* 117 */     while (this.currentBucket < this.table.length) {
/* 118 */       this.current = this.table[this.currentBucket++];
/* 119 */       if (this.current != null)
/* 120 */         return true; 
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object nextElement() {
/* 132 */     if (this.current == null)
/* 133 */       throw new IllegalStateException(); 
/* 134 */     Object retval = this.current.key;
/* 135 */     this.current = this.current.next;
/* 136 */     return retval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String key) {
/* 144 */     Entry[] tab = this.table;
/* 145 */     int hash = key.hashCode();
/* 146 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 147 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 148 */       if (e.hash == hash && e.key == key)
/* 149 */         return e.value; 
/*     */     } 
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNonInterned(String key) {
/* 159 */     Entry[] tab = this.table;
/* 160 */     int hash = key.hashCode();
/* 161 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 162 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 163 */       if (e.hash == hash && e.key.equals(key))
/* 164 */         return e.value; 
/*     */     } 
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rehash() {
/* 177 */     int oldCapacity = this.table.length;
/* 178 */     Entry[] oldMap = this.table;
/*     */     
/* 180 */     int newCapacity = oldCapacity * 2 + 1;
/* 181 */     Entry[] newMap = new Entry[newCapacity];
/*     */     
/* 183 */     this.threshold = (int)(newCapacity * 0.75F);
/* 184 */     this.table = newMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     for (int i = oldCapacity; i-- > 0;) {
/* 194 */       for (Entry old = oldMap[i]; old != null; ) {
/* 195 */         Entry e = old;
/* 196 */         old = old.next;
/*     */         
/* 198 */         int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
/* 199 */         e.next = newMap[index];
/* 200 */         newMap[index] = e;
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */   public Object put(Object key, Object value) {
/* 215 */     if (value == null) {
/* 216 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */     
/* 220 */     Entry[] tab = this.table;
/* 221 */     int hash = key.hashCode();
/* 222 */     int index = (hash & Integer.MAX_VALUE) % tab.length; Entry e;
/* 223 */     for (e = tab[index]; e != null; e = e.next) {
/*     */       
/* 225 */       if (e.hash == hash && e.key == key) {
/* 226 */         Object old = e.value;
/* 227 */         e.value = value;
/* 228 */         return old;
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     if (this.count >= this.threshold) {
/*     */       
/* 234 */       rehash();
/*     */       
/* 236 */       tab = this.table;
/* 237 */       index = (hash & Integer.MAX_VALUE) % tab.length;
/*     */     } 
/*     */ 
/*     */     
/* 241 */     e = new Entry(hash, key, value, tab[index]);
/* 242 */     tab[index] = e;
/* 243 */     this.count++;
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Entry
/*     */   {
/*     */     int hash;
/*     */     
/*     */     Object key;
/*     */     
/*     */     Object value;
/*     */     Entry next;
/*     */     
/*     */     protected Entry(int hash, Object key, Object value, Entry next) {
/* 258 */       this.hash = hash;
/* 259 */       this.key = key;
/* 260 */       this.value = value;
/* 261 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\dtdparser\SimpleHashtable.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */