/*     */ package com.wurmonline.shared.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ public final class SynchedHashMap<K, V>
/*     */   extends HashMap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = -7481512012392422544L;
/*  37 */   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
/*  38 */   private final Lock readLock = this.rwl.readLock();
/*  39 */   private final Lock writeLock = this.rwl.writeLock();
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
/*     */   public SynchedHashMap(Map<? extends K, ? extends V> m) {
/*  52 */     super(m);
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
/*     */   public SynchedHashMap() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SynchedHashMap(int initialCapacity) {
/*  73 */     super(initialCapacity);
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
/*     */   
/*     */   public SynchedHashMap(int initialCapacity, float loadFactor) {
/*  87 */     super(initialCapacity, loadFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  98 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 101 */       super.clear();
/*     */     }
/*     */     finally {
/*     */       
/* 105 */       this.writeLock.unlock();
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
/*     */   public boolean containsKey(Object key) {
/* 117 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 120 */       return super.containsKey(key);
/*     */     }
/*     */     finally {
/*     */       
/* 124 */       this.readLock.unlock();
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
/*     */   public boolean containsValue(Object value) {
/* 136 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 139 */       return super.containsValue(value);
/*     */     }
/*     */     finally {
/*     */       
/* 143 */       this.readLock.unlock();
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
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 155 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 158 */       return super.entrySet();
/*     */     }
/*     */     finally {
/*     */       
/* 162 */       this.readLock.unlock();
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
/*     */   public V get(Object key) {
/* 174 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 177 */       return super.get(key);
/*     */     }
/*     */     finally {
/*     */       
/* 181 */       this.readLock.unlock();
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
/*     */   public Set<K> keySet() {
/* 193 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 196 */       return super.keySet();
/*     */     }
/*     */     finally {
/*     */       
/* 200 */       this.readLock.unlock();
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
/*     */   public V put(K key, V value) {
/* 212 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 215 */       return super.put(key, value);
/*     */     }
/*     */     finally {
/*     */       
/* 219 */       this.writeLock.unlock();
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
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 231 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 234 */       super.putAll(map);
/*     */     }
/*     */     finally {
/*     */       
/* 238 */       this.writeLock.unlock();
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
/*     */   public V remove(Object key) {
/* 250 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 253 */       return super.remove(key);
/*     */     }
/*     */     finally {
/*     */       
/* 257 */       this.writeLock.unlock();
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
/*     */   public Collection<V> values() {
/* 269 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 272 */       return super.values();
/*     */     }
/*     */     finally {
/*     */       
/* 276 */       this.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\SynchedHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */