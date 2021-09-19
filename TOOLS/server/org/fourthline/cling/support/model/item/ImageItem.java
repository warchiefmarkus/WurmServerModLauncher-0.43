/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.Res;
/*     */ import org.fourthline.cling.support.model.StorageMedium;
/*     */ import org.fourthline.cling.support.model.container.Container;
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
/*     */ public class ImageItem
/*     */   extends Item
/*     */ {
/*  34 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.imageItem");
/*     */   
/*     */   public ImageItem() {
/*  37 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public ImageItem(Item other) {
/*  41 */     super(other);
/*     */   }
/*     */   
/*     */   public ImageItem(String id, Container parent, String title, String creator, Res... resource) {
/*  45 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public ImageItem(String id, String parentID, String title, String creator, Res... resource) {
/*  49 */     super(id, parentID, title, creator, CLASS);
/*  50 */     if (resource != null) {
/*  51 */       getResources().addAll(Arrays.asList(resource));
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  56 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public ImageItem setDescription(String description) {
/*  60 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  65 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public ImageItem setLongDescription(String description) {
/*  69 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstPublisher() {
/*  74 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.PUBLISHER.class);
/*     */   }
/*     */   
/*     */   public Person[] getPublishers() {
/*  78 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.PUBLISHER.class);
/*  79 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public ImageItem setPublishers(Person[] publishers) {
/*  83 */     removeProperties(DIDLObject.Property.DC.PUBLISHER.class);
/*  84 */     for (Person publisher : publishers) {
/*  85 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.PUBLISHER(publisher));
/*     */     }
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/*  91 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public ImageItem setStorageMedium(StorageMedium storageMedium) {
/*  95 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public String getRating() {
/* 100 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.RATING.class);
/*     */   }
/*     */   
/*     */   public ImageItem setRating(String rating) {
/* 104 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RATING(rating));
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 109 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public ImageItem setDate(String date) {
/* 113 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstRights() {
/* 118 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.RIGHTS.class);
/*     */   }
/*     */   
/*     */   public String[] getRights() {
/* 122 */     List<String> list = getPropertyValues(DIDLObject.Property.DC.RIGHTS.class);
/* 123 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public ImageItem setRights(String[] rights) {
/* 127 */     removeProperties(DIDLObject.Property.DC.RIGHTS.class);
/* 128 */     for (String right : rights) {
/* 129 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RIGHTS(right));
/*     */     }
/* 131 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\ImageItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */