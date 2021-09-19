/*     */ package org.fourthline.cling.support.model.container;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.StorageMedium;
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
/*     */ public class Album
/*     */   extends Container
/*     */ {
/*  32 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.album");
/*     */   
/*     */   public Album() {
/*  35 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public Album(Container other) {
/*  39 */     super(other);
/*     */   }
/*     */   
/*     */   public Album(String id, Container parent, String title, String creator, Integer childCount) {
/*  43 */     this(id, parent.getId(), title, creator, childCount);
/*     */   }
/*     */   
/*     */   public Album(String id, String parentID, String title, String creator, Integer childCount) {
/*  47 */     super(id, parentID, title, creator, CLASS, childCount);
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  51 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public Album setDescription(String description) {
/*  55 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  60 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public Album setLongDescription(String description) {
/*  64 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/*  69 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public Album setStorageMedium(StorageMedium storageMedium) {
/*  73 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/*  78 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public Album setDate(String date) {
/*  82 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public URI getFirstRelation() {
/*  87 */     return (URI)getFirstPropertyValue(DIDLObject.Property.DC.RELATION.class);
/*     */   }
/*     */   
/*     */   public URI[] getRelations() {
/*  91 */     List<URI> list = getPropertyValues(DIDLObject.Property.DC.RELATION.class);
/*  92 */     return list.<URI>toArray(new URI[list.size()]);
/*     */   }
/*     */   
/*     */   public Album setRelations(URI[] relations) {
/*  96 */     removeProperties(DIDLObject.Property.DC.RELATION.class);
/*  97 */     for (URI relation : relations) {
/*  98 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RELATION(relation));
/*     */     }
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstRights() {
/* 104 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.RIGHTS.class);
/*     */   }
/*     */   
/*     */   public String[] getRights() {
/* 108 */     List<String> list = getPropertyValues(DIDLObject.Property.DC.RIGHTS.class);
/* 109 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public Album setRights(String[] rights) {
/* 113 */     removeProperties(DIDLObject.Property.DC.RIGHTS.class);
/* 114 */     for (String right : rights) {
/* 115 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RIGHTS(right));
/*     */     }
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstContributor() {
/* 121 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getContributors() {
/* 125 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 126 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public Album setContributors(Person[] contributors) {
/* 130 */     removeProperties(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 131 */     for (Person p : contributors) {
/* 132 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(p));
/*     */     }
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstPublisher() {
/* 138 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.PUBLISHER.class);
/*     */   }
/*     */   
/*     */   public Person[] getPublishers() {
/* 142 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.PUBLISHER.class);
/* 143 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public Album setPublishers(Person[] publishers) {
/* 147 */     removeProperties(DIDLObject.Property.DC.PUBLISHER.class);
/* 148 */     for (Person publisher : publishers) {
/* 149 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.PUBLISHER(publisher));
/*     */     }
/* 151 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\Album.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */