/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
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
/*     */ public class AudioBook
/*     */   extends AudioItem
/*     */ {
/*  33 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.audioItem.audioBook");
/*     */   
/*     */   public AudioBook() {
/*  36 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public AudioBook(Item other) {
/*  40 */     super(other);
/*     */   }
/*     */   
/*     */   public AudioBook(String id, Container parent, String title, String creator, Res... resource) {
/*  44 */     this(id, parent.getId(), title, creator, (Person)null, (Person)null, (String)null, resource);
/*     */   }
/*     */   
/*     */   public AudioBook(String id, Container parent, String title, String creator, String producer, String contributor, String date, Res... resource) {
/*  48 */     this(id, parent.getId(), title, creator, new Person(producer), new Person(contributor), date, resource);
/*     */   }
/*     */   
/*     */   public AudioBook(String id, String parentID, String title, String creator, Person producer, Person contributor, String date, Res... resource) {
/*  52 */     super(id, parentID, title, creator, resource);
/*  53 */     setClazz(CLASS);
/*  54 */     if (producer != null)
/*  55 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PRODUCER(producer)); 
/*  56 */     if (contributor != null)
/*  57 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(contributor)); 
/*  58 */     if (date != null)
/*  59 */       setDate(date); 
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/*  63 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public AudioBook setStorageMedium(StorageMedium storageMedium) {
/*  67 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstProducer() {
/*  72 */     return (Person)getFirstPropertyValue(DIDLObject.Property.UPNP.PRODUCER.class);
/*     */   }
/*     */   
/*     */   public Person[] getProducers() {
/*  76 */     List<Person> list = getPropertyValues(DIDLObject.Property.UPNP.PRODUCER.class);
/*  77 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public AudioBook setProducers(Person[] persons) {
/*  81 */     removeProperties(DIDLObject.Property.UPNP.PRODUCER.class);
/*  82 */     for (Person p : persons) {
/*  83 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PRODUCER(p));
/*     */     }
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstContributor() {
/*  89 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getContributors() {
/*  93 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*  94 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public AudioBook setContributors(Person[] contributors) {
/*  98 */     removeProperties(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*  99 */     for (Person p : contributors) {
/* 100 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(p));
/*     */     }
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 106 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public AudioBook setDate(String date) {
/* 110 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 111 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\AudioBook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */