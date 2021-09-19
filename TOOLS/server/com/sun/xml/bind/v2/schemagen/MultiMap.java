/*    */ package com.sun.xml.bind.v2.schemagen;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class MultiMap<K extends Comparable<K>, V>
/*    */   extends TreeMap<K, V>
/*    */ {
/*    */   private final V many;
/*    */   
/*    */   public MultiMap(V many) {
/* 56 */     this.many = many;
/*    */   }
/*    */ 
/*    */   
/*    */   public V put(K key, V value) {
/* 61 */     V old = super.put(key, value);
/* 62 */     if (old != null && !old.equals(value))
/*    */     {
/* 64 */       super.put(key, this.many);
/*    */     }
/* 66 */     return old;
/*    */   }
/*    */ 
/*    */   
/*    */   public void putAll(Map<? extends K, ? extends V> map) {
/* 71 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\MultiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */