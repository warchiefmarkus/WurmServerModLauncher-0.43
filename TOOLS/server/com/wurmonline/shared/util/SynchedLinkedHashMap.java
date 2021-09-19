/*     */ package com.wurmonline.shared.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public final class SynchedLinkedHashMap<K, V>
/*     */   extends LinkedHashMap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 9173507152186508958L;
/*  36 */   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
/*  37 */   private final Lock readLock = this.rwl.readLock();
/*  38 */   private final Lock writeLock = this.rwl.writeLock();
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
/*     */   public SynchedLinkedHashMap(Map<? extends K, ? extends V> m) {
/*  51 */     super(m);
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
/*     */   public SynchedLinkedHashMap() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SynchedLinkedHashMap(int initialCapacity) {
/*  72 */     super(initialCapacity);
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
/*     */   public SynchedLinkedHashMap(int initialCapacity, float loadFactor) {
/*  86 */     super(initialCapacity, loadFactor);
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
/*  97 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 100 */       super.clear();
/*     */     }
/*     */     finally {
/*     */       
/* 104 */       this.writeLock.unlock();
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
/* 116 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 119 */       return super.containsKey(key);
/*     */     }
/*     */     finally {
/*     */       
/* 123 */       this.readLock.unlock();
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
/* 135 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 138 */       return super.containsValue(value);
/*     */     }
/*     */     finally {
/*     */       
/* 142 */       this.readLock.unlock();
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
/* 154 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 157 */       return super.entrySet();
/*     */     }
/*     */     finally {
/*     */       
/* 161 */       this.readLock.unlock();
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
/* 173 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 176 */       return super.get(key);
/*     */     }
/*     */     finally {
/*     */       
/* 180 */       this.readLock.unlock();
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
/* 192 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 195 */       return super.keySet();
/*     */     }
/*     */     finally {
/*     */       
/* 199 */       this.readLock.unlock();
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
/* 211 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 214 */       return super.put(key, value);
/*     */     }
/*     */     finally {
/*     */       
/* 218 */       this.writeLock.unlock();
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
/* 230 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 233 */       super.putAll(map);
/*     */     }
/*     */     finally {
/*     */       
/* 237 */       this.writeLock.unlock();
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
/* 249 */     this.writeLock.lock();
/*     */     
/*     */     try {
/* 252 */       return super.remove(key);
/*     */     }
/*     */     finally {
/*     */       
/* 256 */       this.writeLock.unlock();
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
/* 268 */     this.readLock.lock();
/*     */     
/*     */     try {
/* 271 */       return super.values();
/*     */     }
/*     */     finally {
/*     */       
/* 275 */       this.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\SynchedLinkedHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */