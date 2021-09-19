/*    */ package org.fourthline.cling.registry;
/*    */ 
/*    */ import org.fourthline.cling.model.ExpirationDetails;
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
/*    */ class RegistryItem<K, I>
/*    */ {
/*    */   private K key;
/*    */   private I item;
/* 29 */   private ExpirationDetails expirationDetails = new ExpirationDetails();
/*    */   
/*    */   RegistryItem(K key) {
/* 32 */     this.key = key;
/*    */   }
/*    */   
/*    */   RegistryItem(K key, I item, int maxAgeSeconds) {
/* 36 */     this.key = key;
/* 37 */     this.item = item;
/* 38 */     this.expirationDetails = new ExpirationDetails(maxAgeSeconds);
/*    */   }
/*    */   
/*    */   public K getKey() {
/* 42 */     return this.key;
/*    */   }
/*    */   
/*    */   public I getItem() {
/* 46 */     return this.item;
/*    */   }
/*    */   
/*    */   public ExpirationDetails getExpirationDetails() {
/* 50 */     return this.expirationDetails;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 54 */     if (this == o) return true; 
/* 55 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 57 */     RegistryItem that = (RegistryItem)o;
/*    */     
/* 59 */     return this.key.equals(that.key);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 63 */     return this.key.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return "(" + getClass().getSimpleName() + ") " + getExpirationDetails() + " KEY: " + getKey() + " ITEM: " + getItem();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RegistryItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */