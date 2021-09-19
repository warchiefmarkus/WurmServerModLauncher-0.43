/*     */ package com.sun.xml.bind.v2.util;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class QNameMap<V>
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*  75 */   transient Entry<V>[] table = (Entry<V>[])new Entry[16];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   transient int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int threshold;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private Set<Entry<V>> entrySet = null;
/*     */   
/*     */   public QNameMap() {
/* 102 */     this.threshold = 12;
/* 103 */     this.table = (Entry<V>[])new Entry[16];
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
/*     */ 
/*     */   
/*     */   public void put(String namespaceUri, String localname, V value) {
/* 119 */     assert localname != null;
/* 120 */     assert namespaceUri != null;
/*     */     
/* 122 */     assert localname == localname.intern();
/* 123 */     assert namespaceUri == namespaceUri.intern();
/*     */     
/* 125 */     int hash = hash(localname);
/* 126 */     int i = indexFor(hash, this.table.length);
/*     */     
/* 128 */     for (Entry<V> e = this.table[i]; e != null; e = e.next) {
/* 129 */       if (e.hash == hash && localname == e.localName && namespaceUri == e.nsUri) {
/* 130 */         e.value = value;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 135 */     addEntry(hash, namespaceUri, localname, value, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(QName name, V value) {
/* 140 */     put(name.getNamespaceURI(), name.getLocalPart(), value);
/*     */   }
/*     */   
/*     */   public void put(Name name, V value) {
/* 144 */     put(name.nsUri, name.localName, value);
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
/*     */   public V get(String nsUri, String localPart) {
/* 158 */     Entry<V> e = getEntry(nsUri, localPart);
/* 159 */     if (e == null) return null; 
/* 160 */     return e.value;
/*     */   }
/*     */   
/*     */   public V get(QName name) {
/* 164 */     return get(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 173 */     return this.size;
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
/*     */   public QNameMap<V> putAll(QNameMap<? extends V> map) {
/* 185 */     int numKeysToBeAdded = map.size();
/* 186 */     if (numKeysToBeAdded == 0) {
/* 187 */       return this;
/*     */     }
/*     */     
/* 190 */     if (numKeysToBeAdded > this.threshold) {
/* 191 */       int targetCapacity = numKeysToBeAdded;
/* 192 */       if (targetCapacity > 1073741824)
/* 193 */         targetCapacity = 1073741824; 
/* 194 */       int newCapacity = this.table.length;
/* 195 */       while (newCapacity < targetCapacity)
/* 196 */         newCapacity <<= 1; 
/* 197 */       if (newCapacity > this.table.length) {
/* 198 */         resize(newCapacity);
/*     */       }
/*     */     } 
/* 201 */     for (Entry<? extends V> e : map.entrySet())
/* 202 */       put(e.nsUri, e.localName, e.getValue()); 
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int hash(String x) {
/* 212 */     int h = x.hashCode();
/*     */     
/* 214 */     h += h << 9 ^ 0xFFFFFFFF;
/* 215 */     h ^= h >>> 14;
/* 216 */     h += h << 4;
/* 217 */     h ^= h >>> 10;
/* 218 */     return h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexFor(int h, int length) {
/* 225 */     return h & length - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEntry(int hash, String nsUri, String localName, V value, int bucketIndex) {
/* 235 */     Entry<V> e = this.table[bucketIndex];
/* 236 */     this.table[bucketIndex] = new Entry<V>(hash, nsUri, localName, value, e);
/* 237 */     if (this.size++ >= this.threshold) {
/* 238 */       resize(2 * this.table.length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resize(int newCapacity) {
/* 248 */     Entry<V>[] arrayOfEntry = this.table;
/* 249 */     int oldCapacity = arrayOfEntry.length;
/* 250 */     if (oldCapacity == 1073741824) {
/* 251 */       this.threshold = Integer.MAX_VALUE;
/*     */       
/*     */       return;
/*     */     } 
/* 255 */     Entry[] newTable = new Entry[newCapacity];
/* 256 */     transfer((Entry<V>[])newTable);
/* 257 */     this.table = (Entry<V>[])newTable;
/* 258 */     this.threshold = newCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void transfer(Entry<V>[] newTable) {
/* 265 */     Entry<V>[] src = this.table;
/* 266 */     int newCapacity = newTable.length;
/* 267 */     for (int j = 0; j < src.length; j++) {
/* 268 */       Entry<V> e = src[j];
/* 269 */       if (e != null) {
/* 270 */         src[j] = null;
/*     */         do {
/* 272 */           Entry<V> next = e.next;
/* 273 */           int i = indexFor(e.hash, newCapacity);
/* 274 */           e.next = newTable[i];
/* 275 */           newTable[i] = e;
/* 276 */           e = next;
/* 277 */         } while (e != null);
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
/*     */   public Entry<V> getOne() {
/* 290 */     for (Entry<V> e : this.table) {
/* 291 */       if (e != null)
/* 292 */         return e; 
/*     */     } 
/* 294 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<QName> keySet() {
/* 298 */     Set<QName> r = new HashSet<QName>();
/* 299 */     for (Entry<V> e : entrySet()) {
/* 300 */       r.add(e.createQName());
/*     */     }
/* 302 */     return r;
/*     */   }
/*     */   
/*     */   private abstract class HashIterator<E> implements Iterator<E> {
/*     */     QNameMap.Entry<V> next;
/*     */     int index;
/*     */     
/*     */     HashIterator() {
/* 310 */       QNameMap.Entry[] arrayOfEntry = QNameMap.this.table;
/* 311 */       int i = arrayOfEntry.length;
/* 312 */       QNameMap.Entry<V> n = null;
/* 313 */       if (QNameMap.this.size != 0) {
/* 314 */         while (i > 0 && (n = arrayOfEntry[--i]) == null);
/*     */       }
/*     */       
/* 317 */       this.next = n;
/* 318 */       this.index = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 322 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     QNameMap.Entry<V> nextEntry() {
/* 326 */       QNameMap.Entry<V> e = this.next;
/* 327 */       if (e == null) {
/* 328 */         throw new NoSuchElementException();
/*     */       }
/* 330 */       QNameMap.Entry<V> n = e.next;
/* 331 */       QNameMap.Entry[] arrayOfEntry = QNameMap.this.table;
/* 332 */       int i = this.index;
/* 333 */       while (n == null && i > 0)
/* 334 */         n = arrayOfEntry[--i]; 
/* 335 */       this.index = i;
/* 336 */       this.next = n;
/* 337 */       return e;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 341 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean containsKey(String nsUri, String localName) {
/* 346 */     return (getEntry(nsUri, localName) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 354 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Entry<V>
/*     */   {
/*     */     public final String nsUri;
/*     */     
/*     */     public final String localName;
/*     */     
/*     */     V value;
/*     */     
/*     */     final int hash;
/*     */     
/*     */     Entry<V> next;
/*     */ 
/*     */     
/*     */     Entry(int h, String nsUri, String localName, V v, Entry<V> n) {
/* 373 */       this.value = v;
/* 374 */       this.next = n;
/* 375 */       this.nsUri = nsUri;
/* 376 */       this.localName = localName;
/* 377 */       this.hash = h;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public QName createQName() {
/* 384 */       return new QName(this.nsUri, this.localName);
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 388 */       return this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V newValue) {
/* 392 */       V oldValue = this.value;
/* 393 */       this.value = newValue;
/* 394 */       return oldValue;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 398 */       if (!(o instanceof Entry))
/* 399 */         return false; 
/* 400 */       Entry e = (Entry)o;
/* 401 */       String k1 = this.nsUri;
/* 402 */       String k2 = e.nsUri;
/* 403 */       String k3 = this.localName;
/* 404 */       String k4 = e.localName;
/* 405 */       if (k1 == k2 || (k1 != null && k1.equals(k2) && (k3 == k4 || (k3 != null && k3.equals(k4))))) {
/*     */         
/* 407 */         Object v1 = getValue();
/* 408 */         Object v2 = e.getValue();
/* 409 */         if (v1 == v2 || (v1 != null && v1.equals(v2)))
/* 410 */           return true; 
/*     */       } 
/* 412 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 416 */       return this.localName.hashCode() ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 421 */       return '"' + this.nsUri + "\",\"" + this.localName + "\"=" + getValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<Entry<V>> entrySet() {
/* 426 */     Set<Entry<V>> es = this.entrySet;
/* 427 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*     */   }
/*     */   
/*     */   private Iterator<Entry<V>> newEntryIterator() {
/* 431 */     return new EntryIterator();
/*     */   }
/*     */   private class EntryIterator extends HashIterator<Entry<V>> { private EntryIterator() {}
/*     */     
/*     */     public QNameMap.Entry<V> next() {
/* 436 */       return nextEntry();
/*     */     } }
/*     */   
/*     */   private class EntrySet extends AbstractSet<Entry<V>> {
/*     */     public Iterator<QNameMap.Entry<V>> iterator() {
/* 441 */       return QNameMap.this.newEntryIterator();
/*     */     } private EntrySet() {}
/*     */     public boolean contains(Object o) {
/* 444 */       if (!(o instanceof QNameMap.Entry))
/* 445 */         return false; 
/* 446 */       QNameMap.Entry<V> e = (QNameMap.Entry<V>)o;
/* 447 */       QNameMap.Entry<V> candidate = QNameMap.this.getEntry(e.nsUri, e.localName);
/* 448 */       return (candidate != null && candidate.equals(e));
/*     */     }
/*     */     public boolean remove(Object o) {
/* 451 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public int size() {
/* 454 */       return QNameMap.this.size;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Entry<V> getEntry(String nsUri, String localName) {
/* 460 */     assert nsUri == nsUri.intern();
/* 461 */     assert localName == localName.intern();
/*     */     
/* 463 */     int hash = hash(localName);
/* 464 */     int i = indexFor(hash, this.table.length);
/* 465 */     Entry<V> e = this.table[i];
/* 466 */     while (e != null && (localName != e.localName || nsUri != e.nsUri))
/* 467 */       e = e.next; 
/* 468 */     return e;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 472 */     StringBuilder buf = new StringBuilder();
/* 473 */     buf.append('{');
/*     */     
/* 475 */     for (Entry<V> e : entrySet()) {
/* 476 */       if (buf.length() > 1)
/* 477 */         buf.append(','); 
/* 478 */       buf.append('[');
/* 479 */       buf.append(e);
/* 480 */       buf.append(']');
/*     */     } 
/*     */     
/* 483 */     buf.append('}');
/* 484 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\QNameMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */