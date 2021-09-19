/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.PersonWithRole;
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
/*     */ public class VideoItem
/*     */   extends Item
/*     */ {
/*  35 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.videoItem");
/*     */   
/*     */   public VideoItem() {
/*  38 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public VideoItem(Item other) {
/*  42 */     super(other);
/*     */   }
/*     */   
/*     */   public VideoItem(String id, Container parent, String title, String creator, Res... resource) {
/*  46 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public VideoItem(String id, String parentID, String title, String creator, Res... resource) {
/*  50 */     super(id, parentID, title, creator, CLASS);
/*  51 */     if (resource != null) {
/*  52 */       getResources().addAll(Arrays.asList(resource));
/*     */     }
/*     */   }
/*     */   
/*     */   public String getFirstGenre() {
/*  57 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
/*     */   }
/*     */   
/*     */   public String[] getGenres() {
/*  61 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.GENRE.class);
/*  62 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public VideoItem setGenres(String[] genres) {
/*  66 */     removeProperties(DIDLObject.Property.UPNP.GENRE.class);
/*  67 */     for (String genre : genres) {
/*  68 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(genre));
/*     */     }
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  74 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public VideoItem setDescription(String description) {
/*  78 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  83 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public VideoItem setLongDescription(String description) {
/*  87 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstProducer() {
/*  92 */     return (Person)getFirstPropertyValue(DIDLObject.Property.UPNP.PRODUCER.class);
/*     */   }
/*     */   
/*     */   public Person[] getProducers() {
/*  96 */     List<Person> list = getPropertyValues(DIDLObject.Property.UPNP.PRODUCER.class);
/*  97 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public VideoItem setProducers(Person[] persons) {
/* 101 */     removeProperties(DIDLObject.Property.UPNP.PRODUCER.class);
/* 102 */     for (Person p : persons) {
/* 103 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PRODUCER(p));
/*     */     }
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public String getRating() {
/* 109 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.RATING.class);
/*     */   }
/*     */   
/*     */   public VideoItem setRating(String rating) {
/* 113 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RATING(rating));
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstActor() {
/* 118 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.ACTOR.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getActors() {
/* 122 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.ACTOR.class);
/* 123 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public VideoItem setActors(PersonWithRole[] persons) {
/* 127 */     removeProperties(DIDLObject.Property.UPNP.ACTOR.class);
/* 128 */     for (PersonWithRole p : persons) {
/* 129 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ACTOR(p));
/*     */     }
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstDirector() {
/* 135 */     return (Person)getFirstPropertyValue(DIDLObject.Property.UPNP.DIRECTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getDirectors() {
/* 139 */     List<Person> list = getPropertyValues(DIDLObject.Property.UPNP.DIRECTOR.class);
/* 140 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public VideoItem setDirectors(Person[] persons) {
/* 144 */     removeProperties(DIDLObject.Property.UPNP.DIRECTOR.class);
/* 145 */     for (Person p : persons) {
/* 146 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.DIRECTOR(p));
/*     */     }
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstPublisher() {
/* 152 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.PUBLISHER.class);
/*     */   }
/*     */   
/*     */   public Person[] getPublishers() {
/* 156 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.PUBLISHER.class);
/* 157 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public VideoItem setPublishers(Person[] publishers) {
/* 161 */     removeProperties(DIDLObject.Property.DC.PUBLISHER.class);
/* 162 */     for (Person publisher : publishers) {
/* 163 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.PUBLISHER(publisher));
/*     */     }
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/* 169 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.LANGUAGE.class);
/*     */   }
/*     */   
/*     */   public VideoItem setLanguage(String language) {
/* 173 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(language));
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   public URI getFirstRelation() {
/* 178 */     return (URI)getFirstPropertyValue(DIDLObject.Property.DC.RELATION.class);
/*     */   }
/*     */   
/*     */   public URI[] getRelations() {
/* 182 */     List<URI> list = getPropertyValues(DIDLObject.Property.DC.RELATION.class);
/* 183 */     return list.<URI>toArray(new URI[list.size()]);
/*     */   }
/*     */   
/*     */   public VideoItem setRelations(URI[] relations) {
/* 187 */     removeProperties(DIDLObject.Property.DC.RELATION.class);
/* 188 */     for (URI relation : relations) {
/* 189 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RELATION(relation));
/*     */     }
/* 191 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\VideoItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */