/*     */ package org.fourthline.cling.support.shared;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/*     */   Set<K> keySet;
/*     */   Collection<V> valuesCollection;
/*     */   
/*     */   public static class SimpleImmutableEntry<K, V>
/*     */     implements Map.Entry<K, V>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 7138329143949025153L;
/*     */     private final K key;
/*     */     private final V value;
/*     */     
/*     */     public SimpleImmutableEntry(K theKey, V theValue) {
/*  55 */       this.key = theKey;
/*  56 */       this.value = theValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> copyFrom) {
/*  63 */       this.key = copyFrom.getKey();
/*  64 */       this.value = copyFrom.getValue();
/*     */     }
/*     */     
/*     */     public K getKey() {
/*  68 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/*  72 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V setValue(V object) {
/*  80 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/*  84 */       if (this == object) {
/*  85 */         return true;
/*     */       }
/*  87 */       if (object instanceof Map.Entry) {
/*  88 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/*  89 */         return (((this.key == null) ? (entry.getKey() == null) : this.key.equals(entry
/*  90 */             .getKey())) && ((this.value == null) ? (entry
/*  91 */           .getValue() == null) : this.value
/*  92 */           .equals(entry.getValue())));
/*     */       } 
/*  94 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  99 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 103 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SimpleEntry<K, V>
/*     */     implements Map.Entry<K, V>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -8499721149061103585L;
/*     */     
/*     */     private final K key;
/*     */     
/*     */     private V value;
/*     */ 
/*     */     
/*     */     public SimpleEntry(K theKey, V theValue) {
/* 120 */       this.key = theKey;
/* 121 */       this.value = theValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SimpleEntry(Map.Entry<? extends K, ? extends V> copyFrom) {
/* 128 */       this.key = copyFrom.getKey();
/* 129 */       this.value = copyFrom.getValue();
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 133 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 137 */       return this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V object) {
/* 141 */       V result = this.value;
/* 142 */       this.value = object;
/* 143 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object object) {
/* 147 */       if (this == object) {
/* 148 */         return true;
/*     */       }
/* 150 */       if (object instanceof Map.Entry) {
/* 151 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 152 */         return (((this.key == null) ? (entry.getKey() == null) : this.key.equals(entry
/* 153 */             .getKey())) && ((this.value == null) ? (entry
/* 154 */           .getValue() == null) : this.value
/* 155 */           .equals(entry.getValue())));
/*     */       } 
/* 157 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 162 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 166 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
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
/*     */   public void clear() {
/* 180 */     entrySet().clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 190 */     Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 191 */     if (key != null) {
/* 192 */       while (it.hasNext()) {
/* 193 */         if (key.equals(((Map.Entry)it.next()).getKey())) {
/* 194 */           return true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 198 */       while (it.hasNext()) {
/* 199 */         if (((Map.Entry)it.next()).getKey() == null) {
/* 200 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 214 */     Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 215 */     if (value != null) {
/* 216 */       while (it.hasNext()) {
/* 217 */         if (value.equals(((Map.Entry)it.next()).getValue())) {
/* 218 */           return true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 222 */       while (it.hasNext()) {
/* 223 */         if (((Map.Entry)it.next()).getValue() == null) {
/* 224 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Set<Map.Entry<K, V>> entrySet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 243 */     if (this == object) {
/* 244 */       return true;
/*     */     }
/* 246 */     if (object instanceof Map) {
/* 247 */       Map<?, ?> map = (Map<?, ?>)object;
/* 248 */       if (size() != map.size()) {
/* 249 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 253 */         for (Map.Entry<K, V> entry : entrySet()) {
/* 254 */           K key = entry.getKey();
/* 255 */           V mine = entry.getValue();
/* 256 */           Object theirs = map.get(key);
/* 257 */           if (mine == null) {
/* 258 */             if (theirs != null || !map.containsKey(key))
/* 259 */               return false;  continue;
/*     */           } 
/* 261 */           if (!mine.equals(theirs)) {
/* 262 */             return false;
/*     */           }
/*     */         } 
/* 265 */       } catch (NullPointerException ignored) {
/* 266 */         return false;
/* 267 */       } catch (ClassCastException ignored) {
/* 268 */         return false;
/*     */       } 
/* 270 */       return true;
/*     */     } 
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 282 */     Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 283 */     if (key != null) {
/* 284 */       while (it.hasNext()) {
/* 285 */         Map.Entry<K, V> entry = it.next();
/* 286 */         if (key.equals(entry.getKey())) {
/* 287 */           return entry.getValue();
/*     */         }
/*     */       } 
/*     */     } else {
/* 291 */       while (it.hasNext()) {
/* 292 */         Map.Entry<K, V> entry = it.next();
/* 293 */         if (entry.getKey() == null) {
/* 294 */           return entry.getValue();
/*     */         }
/*     */       } 
/*     */     } 
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 308 */     int result = 0;
/* 309 */     Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 310 */     while (it.hasNext()) {
/* 311 */       result += ((Map.Entry)it.next()).hashCode();
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 322 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 332 */     if (this.keySet == null) {
/* 333 */       this.keySet = new AbstractSet<K>() {
/*     */           public boolean contains(Object object) {
/* 335 */             return AbstractMap.this.containsKey(object);
/*     */           }
/*     */           
/*     */           public int size() {
/* 339 */             return AbstractMap.this.size();
/*     */           }
/*     */           
/*     */           public Iterator<K> iterator() {
/* 343 */             return new Iterator<K>() {
/* 344 */                 Iterator<Map.Entry<K, V>> setIterator = AbstractMap.this.entrySet().iterator();
/*     */                 
/*     */                 public boolean hasNext() {
/* 347 */                   return this.setIterator.hasNext();
/*     */                 }
/*     */                 
/*     */                 public K next() {
/* 351 */                   return (K)((Map.Entry)this.setIterator.next()).getKey();
/*     */                 }
/*     */                 
/*     */                 public void remove() {
/* 355 */                   this.setIterator.remove();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/* 361 */     return this.keySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 370 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 380 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 381 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 392 */     Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 393 */     if (key != null) {
/* 394 */       while (it.hasNext()) {
/* 395 */         Map.Entry<K, V> entry = it.next();
/* 396 */         if (key.equals(entry.getKey())) {
/* 397 */           it.remove();
/* 398 */           return entry.getValue();
/*     */         } 
/*     */       } 
/*     */     } else {
/* 402 */       while (it.hasNext()) {
/* 403 */         Map.Entry<K, V> entry = it.next();
/* 404 */         if (entry.getKey() == null) {
/* 405 */           it.remove();
/* 406 */           return entry.getValue();
/*     */         } 
/*     */       } 
/*     */     } 
/* 410 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 419 */     return entrySet().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 430 */     if (isEmpty()) {
/* 431 */       return "{}";
/*     */     }
/*     */     
/* 434 */     StringBuilder buffer = new StringBuilder(size() * 28);
/* 435 */     buffer.append('{');
/* 436 */     Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 437 */     while (it.hasNext()) {
/* 438 */       Map.Entry<K, V> entry = it.next();
/* 439 */       Object key = entry.getKey();
/* 440 */       if (key != this) {
/* 441 */         buffer.append(key);
/*     */       } else {
/* 443 */         buffer.append("(this Map)");
/*     */       } 
/* 445 */       buffer.append('=');
/* 446 */       Object value = entry.getValue();
/* 447 */       if (value != this) {
/* 448 */         buffer.append(value);
/*     */       } else {
/* 450 */         buffer.append("(this Map)");
/*     */       } 
/* 452 */       if (it.hasNext()) {
/* 453 */         buffer.append(", ");
/*     */       }
/*     */     } 
/* 456 */     buffer.append('}');
/* 457 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 467 */     if (this.valuesCollection == null) {
/* 468 */       this.valuesCollection = new AbstractCollection<V>() {
/*     */           public int size() {
/* 470 */             return AbstractMap.this.size();
/*     */           }
/*     */           
/*     */           public boolean contains(Object object) {
/* 474 */             return AbstractMap.this.containsValue(object);
/*     */           }
/*     */           
/*     */           public Iterator<V> iterator() {
/* 478 */             return new Iterator<V>() {
/* 479 */                 Iterator<Map.Entry<K, V>> setIterator = AbstractMap.this.entrySet().iterator();
/*     */                 
/*     */                 public boolean hasNext() {
/* 482 */                   return this.setIterator.hasNext();
/*     */                 }
/*     */                 
/*     */                 public V next() {
/* 486 */                   return (V)((Map.Entry)this.setIterator.next()).getValue();
/*     */                 }
/*     */                 
/*     */                 public void remove() {
/* 490 */                   this.setIterator.remove();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/* 496 */     return this.valuesCollection;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object clone() throws CloneNotSupportedException {
/* 501 */     AbstractMap<K, V> result = (AbstractMap<K, V>)super.clone();
/* 502 */     result.keySet = null;
/* 503 */     result.valuesCollection = null;
/* 504 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\AbstractMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */