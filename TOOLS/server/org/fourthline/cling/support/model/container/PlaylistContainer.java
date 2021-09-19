/*     */ package org.fourthline.cling.support.model.container;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.PersonWithRole;
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
/*     */ public class PlaylistContainer
/*     */   extends Container
/*     */ {
/*  32 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.playlistContainer");
/*     */   
/*     */   public PlaylistContainer() {
/*  35 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public PlaylistContainer(Container other) {
/*  39 */     super(other);
/*     */   }
/*     */   
/*     */   public PlaylistContainer(String id, Container parent, String title, String creator, Integer childCount) {
/*  43 */     this(id, parent.getId(), title, creator, childCount);
/*     */   }
/*     */   
/*     */   public PlaylistContainer(String id, String parentID, String title, String creator, Integer childCount) {
/*  47 */     super(id, parentID, title, creator, CLASS, childCount);
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstArtist() {
/*  51 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.ARTIST.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getArtists() {
/*  55 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.ARTIST.class);
/*  56 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setArtists(PersonWithRole[] artists) {
/*  60 */     removeProperties(DIDLObject.Property.UPNP.ARTIST.class);
/*  61 */     for (PersonWithRole artist : artists) {
/*  62 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(artist));
/*     */     }
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstGenre() {
/*  68 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
/*     */   }
/*     */   
/*     */   public String[] getGenres() {
/*  72 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.GENRE.class);
/*  73 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setGenres(String[] genres) {
/*  77 */     removeProperties(DIDLObject.Property.UPNP.GENRE.class);
/*  78 */     for (String genre : genres) {
/*  79 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(genre));
/*     */     }
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  85 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setDescription(String description) {
/*  89 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  94 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setLongDescription(String description) {
/*  98 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstProducer() {
/* 103 */     return (Person)getFirstPropertyValue(DIDLObject.Property.UPNP.PRODUCER.class);
/*     */   }
/*     */   
/*     */   public Person[] getProducers() {
/* 107 */     List<Person> list = getPropertyValues(DIDLObject.Property.UPNP.PRODUCER.class);
/* 108 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setProducers(Person[] persons) {
/* 112 */     removeProperties(DIDLObject.Property.UPNP.PRODUCER.class);
/* 113 */     for (Person p : persons) {
/* 114 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PRODUCER(p));
/*     */     }
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/* 120 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setStorageMedium(StorageMedium storageMedium) {
/* 124 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 129 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setDate(String date) {
/* 133 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstRights() {
/* 138 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.RIGHTS.class);
/*     */   }
/*     */   
/*     */   public String[] getRights() {
/* 142 */     List<String> list = getPropertyValues(DIDLObject.Property.DC.RIGHTS.class);
/* 143 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setRights(String[] rights) {
/* 147 */     removeProperties(DIDLObject.Property.DC.RIGHTS.class);
/* 148 */     for (String right : rights) {
/* 149 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RIGHTS(right));
/*     */     }
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstContributor() {
/* 155 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getContributors() {
/* 159 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 160 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setContributors(Person[] contributors) {
/* 164 */     removeProperties(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 165 */     for (Person p : contributors) {
/* 166 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(p));
/*     */     }
/* 168 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/* 172 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.LANGUAGE.class);
/*     */   }
/*     */   
/*     */   public PlaylistContainer setLanguage(String language) {
/* 176 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(language));
/* 177 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\PlaylistContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */