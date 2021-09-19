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
/*    */ public class StorageVolume
/*    */   extends Container
/*    */ {
/* 27 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.storageVolume");
/*    */   
/*    */   public StorageVolume() {
/* 30 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public StorageVolume(Container other) {
/* 34 */     super(other);
/*    */   }
/*    */ 
/*    */   
/*    */   public StorageVolume(String id, Container parent, String title, String creator, Integer childCount, Long storageTotal, Long storageUsed, Long storageFree, StorageMedium storageMedium) {
/* 39 */     this(id, parent.getId(), title, creator, childCount, storageTotal, storageUsed, storageFree, storageMedium);
/*    */   }
/*    */ 
/*    */   
/*    */   public StorageVolume(String id, String parentID, String title, String creator, Integer childCount, Long storageTotal, Long storageUsed, Long storageFree, StorageMedium storageMedium) {
/* 44 */     super(id, parentID, title, creator, CLASS, childCount);
/* 45 */     if (storageTotal != null)
/* 46 */       setStorageTotal(storageTotal); 
/* 47 */     if (storageUsed != null)
/* 48 */       setStorageUsed(storageUsed); 
/* 49 */     if (storageFree != null)
/* 50 */       setStorageFree(storageFree); 
/* 51 */     if (storageMedium != null)
/* 52 */       setStorageMedium(storageMedium); 
/*    */   }
/*    */   
/*    */   public Long getStorageTotal() {
/* 56 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_TOTAL.class);
/*    */   }
/*    */   
/*    */   public StorageVolume setStorageTotal(Long l) {
/* 60 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_TOTAL(l));
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public Long getStorageUsed() {
/* 65 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_USED.class);
/*    */   }
/*    */   
/*    */   public StorageVolume setStorageUsed(Long l) {
/* 69 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_USED(l));
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public Long getStorageFree() {
/* 74 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_FREE.class);
/*    */   }
/*    */   
/*    */   public StorageVolume setStorageFree(Long l) {
/* 78 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_FREE(l));
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public StorageMedium getStorageMedium() {
/* 83 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*    */   }
/*    */   
/*    */   public StorageVolume setStorageMedium(StorageMedium storageMedium) {
/* 87 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/* 88 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\StorageVolume.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */