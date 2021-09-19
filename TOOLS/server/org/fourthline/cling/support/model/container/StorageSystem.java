/*    */ package org.fourthline.cling.support.model.container;
/*    */ 
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.StorageMedium;
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
/*    */ public class StorageSystem
/*    */   extends Container
/*    */ {
/* 27 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.storageSystem");
/*    */   
/*    */   public StorageSystem() {
/* 30 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public StorageSystem(Container other) {
/* 34 */     super(other);
/*    */   }
/*    */ 
/*    */   
/*    */   public StorageSystem(String id, Container parent, String title, String creator, Integer childCount, Long storageTotal, Long storageUsed, Long storageFree, Long storageMaxPartition, StorageMedium storageMedium) {
/* 39 */     this(id, parent.getId(), title, creator, childCount, storageTotal, storageUsed, storageFree, storageMaxPartition, storageMedium);
/*    */   }
/*    */ 
/*    */   
/*    */   public StorageSystem(String id, String parentID, String title, String creator, Integer childCount, Long storageTotal, Long storageUsed, Long storageFree, Long storageMaxPartition, StorageMedium storageMedium) {
/* 44 */     super(id, parentID, title, creator, CLASS, childCount);
/* 45 */     if (storageTotal != null)
/* 46 */       setStorageTotal(storageTotal); 
/* 47 */     if (storageUsed != null)
/* 48 */       setStorageUsed(storageUsed); 
/* 49 */     if (storageFree != null)
/* 50 */       setStorageFree(storageFree); 
/* 51 */     if (storageMaxPartition != null)
/* 52 */       setStorageMaxPartition(storageMaxPartition); 
/* 53 */     if (storageMedium != null)
/* 54 */       setStorageMedium(storageMedium); 
/*    */   }
/*    */   
/*    */   public Long getStorageTotal() {
/* 58 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_TOTAL.class);
/*    */   }
/*    */   
/*    */   public StorageSystem setStorageTotal(Long l) {
/* 62 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_TOTAL(l));
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public Long getStorageUsed() {
/* 67 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_USED.class);
/*    */   }
/*    */   
/*    */   public StorageSystem setStorageUsed(Long l) {
/* 71 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_USED(l));
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public Long getStorageFree() {
/* 76 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_FREE.class);
/*    */   }
/*    */   
/*    */   public StorageSystem setStorageFree(Long l) {
/* 80 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_FREE(l));
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public Long getStorageMaxPartition() {
/* 85 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MAX_PARTITION.class);
/*    */   }
/*    */   
/*    */   public StorageSystem setStorageMaxPartition(Long l) {
/* 89 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MAX_PARTITION(l));
/* 90 */     return this;
/*    */   }
/*    */   
/*    */   public StorageMedium getStorageMedium() {
/* 94 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*    */   }
/*    */   
/*    */   public StorageSystem setStorageMedium(StorageMedium storageMedium) {
/* 98 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/* 99 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\StorageSystem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */