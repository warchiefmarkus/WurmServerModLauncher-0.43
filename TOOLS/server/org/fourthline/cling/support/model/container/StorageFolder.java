/*    */ package org.fourthline.cling.support.model.container;
/*    */ 
/*    */ import org.fourthline.cling.support.model.DIDLObject;
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
/*    */ public class StorageFolder
/*    */   extends Container
/*    */ {
/* 25 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.storageFolder");
/*    */   
/*    */   public StorageFolder() {
/* 28 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public StorageFolder(Container other) {
/* 32 */     super(other);
/*    */   }
/*    */ 
/*    */   
/*    */   public StorageFolder(String id, Container parent, String title, String creator, Integer childCount, Long storageUsed) {
/* 37 */     this(id, parent.getId(), title, creator, childCount, storageUsed);
/*    */   }
/*    */ 
/*    */   
/*    */   public StorageFolder(String id, String parentID, String title, String creator, Integer childCount, Long storageUsed) {
/* 42 */     super(id, parentID, title, creator, CLASS, childCount);
/* 43 */     if (storageUsed != null)
/* 44 */       setStorageUsed(storageUsed); 
/*    */   }
/*    */   
/*    */   public Long getStorageUsed() {
/* 48 */     return (Long)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_USED.class);
/*    */   }
/*    */   
/*    */   public StorageFolder setStorageUsed(Long l) {
/* 52 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_USED(l));
/* 53 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\StorageFolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */