/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.Res;
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
/*     */ public class AudioItem
/*     */   extends Item
/*     */ {
/*  34 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.audioItem");
/*     */   
/*     */   public AudioItem() {
/*  37 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public AudioItem(Item other) {
/*  41 */     super(other);
/*     */   }
/*     */   
/*     */   public AudioItem(String id, Container parent, String title, String creator, Res... resource) {
/*  45 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public AudioItem(String id, String parentID, String title, String creator, Res... resource) {
/*  49 */     super(id, parentID, title, creator, CLASS);
/*  50 */     if (resource != null) {
/*  51 */       getResources().addAll(Arrays.asList(resource));
/*     */     }
/*     */   }
/*     */   
/*     */   public String getFirstGenre() {
/*  56 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
/*     */   }
/*     */   
/*     */   public String[] getGenres() {
/*  60 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.GENRE.class);
/*  61 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public AudioItem setGenres(String[] genres) {
/*  65 */     removeProperties(DIDLObject.Property.UPNP.GENRE.class);
/*  66 */     for (String genre : genres) {
/*  67 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(genre));
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  73 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public AudioItem setDescription(String description) {
/*  77 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  82 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public AudioItem setLongDescription(String description) {
/*  86 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstPublisher() {
/*  91 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.PUBLISHER.class);
/*     */   }
/*     */   
/*     */   public Person[] getPublishers() {
/*  95 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.PUBLISHER.class);
/*  96 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public AudioItem setPublishers(Person[] publishers) {
/* 100 */     removeProperties(DIDLObject.Property.DC.PUBLISHER.class);
/* 101 */     for (Person publisher : publishers) {
/* 102 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.PUBLISHER(publisher));
/*     */     }
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public URI getFirstRelation() {
/* 108 */     return (URI)getFirstPropertyValue(DIDLObject.Property.DC.RELATION.class);
/*     */   }
/*     */   
/*     */   public URI[] getRelations() {
/* 112 */     List<URI> list = getPropertyValues(DIDLObject.Property.DC.RELATION.class);
/* 113 */     return list.<URI>toArray(new URI[list.size()]);
/*     */   }
/*     */   
/*     */   public AudioItem setRelations(URI[] relations) {
/* 117 */     removeProperties(DIDLObject.Property.DC.RELATION.class);
/* 118 */     for (URI relation : relations) {
/* 119 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RELATION(relation));
/*     */     }
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/* 125 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.LANGUAGE.class);
/*     */   }
/*     */   
/*     */   public AudioItem setLanguage(String language) {
/* 129 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(language));
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstRights() {
/* 134 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.RIGHTS.class);
/*     */   }
/*     */   
/*     */   public String[] getRights() {
/* 138 */     List<String> list = getPropertyValues(DIDLObject.Property.DC.RIGHTS.class);
/* 139 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public AudioItem setRights(String[] rights) {
/* 143 */     removeProperties(DIDLObject.Property.DC.RIGHTS.class);
/* 144 */     for (String right : rights) {
/* 145 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RIGHTS(right));
/*     */     }
/* 147 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\AudioItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */