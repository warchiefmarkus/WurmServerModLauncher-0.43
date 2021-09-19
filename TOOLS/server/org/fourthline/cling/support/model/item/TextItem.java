/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.PersonWithRole;
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
/*     */ public class TextItem
/*     */   extends Item
/*     */ {
/*  36 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.textItem");
/*     */   
/*     */   public TextItem() {
/*  39 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public TextItem(Item other) {
/*  43 */     super(other);
/*     */   }
/*     */   
/*     */   public TextItem(String id, Container parent, String title, String creator, Res... resource) {
/*  47 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public TextItem(String id, String parentID, String title, String creator, Res... resource) {
/*  51 */     super(id, parentID, title, creator, CLASS);
/*  52 */     if (resource != null) {
/*  53 */       getResources().addAll(Arrays.asList(resource));
/*     */     }
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstAuthor() {
/*  58 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.AUTHOR.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getAuthors() {
/*  62 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.AUTHOR.class);
/*  63 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public TextItem setAuthors(PersonWithRole[] persons) {
/*  67 */     removeProperties(DIDLObject.Property.UPNP.AUTHOR.class);
/*  68 */     for (PersonWithRole p : persons) {
/*  69 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.AUTHOR(p));
/*     */     }
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  75 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public TextItem setDescription(String description) {
/*  79 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  84 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public TextItem setLongDescription(String description) {
/*  88 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/*  93 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.LANGUAGE.class);
/*     */   }
/*     */   
/*     */   public TextItem setLanguage(String language) {
/*  97 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(language));
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/* 102 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public TextItem setStorageMedium(StorageMedium storageMedium) {
/* 106 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 111 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public TextItem setDate(String date) {
/* 115 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public URI getFirstRelation() {
/* 120 */     return (URI)getFirstPropertyValue(DIDLObject.Property.DC.RELATION.class);
/*     */   }
/*     */   
/*     */   public URI[] getRelations() {
/* 124 */     List<URI> list = getPropertyValues(DIDLObject.Property.DC.RELATION.class);
/* 125 */     return list.<URI>toArray(new URI[list.size()]);
/*     */   }
/*     */   
/*     */   public TextItem setRelations(URI[] relations) {
/* 129 */     removeProperties(DIDLObject.Property.DC.RELATION.class);
/* 130 */     for (URI relation : relations) {
/* 131 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RELATION(relation));
/*     */     }
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstRights() {
/* 137 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.RIGHTS.class);
/*     */   }
/*     */   
/*     */   public String[] getRights() {
/* 141 */     List<String> list = getPropertyValues(DIDLObject.Property.DC.RIGHTS.class);
/* 142 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public TextItem setRights(String[] rights) {
/* 146 */     removeProperties(DIDLObject.Property.DC.RIGHTS.class);
/* 147 */     for (String right : rights) {
/* 148 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RIGHTS(right));
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public String getRating() {
/* 154 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.RATING.class);
/*     */   }
/*     */   
/*     */   public TextItem setRating(String rating) {
/* 158 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RATING(rating));
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstContributor() {
/* 163 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getContributors() {
/* 167 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 168 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public TextItem setContributors(Person[] contributors) {
/* 172 */     removeProperties(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 173 */     for (Person p : contributors) {
/* 174 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(p));
/*     */     }
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstPublisher() {
/* 180 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.PUBLISHER.class);
/*     */   }
/*     */   
/*     */   public Person[] getPublishers() {
/* 184 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.PUBLISHER.class);
/* 185 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public TextItem setPublishers(Person[] publishers) {
/* 189 */     removeProperties(DIDLObject.Property.DC.PUBLISHER.class);
/* 190 */     for (Person publisher : publishers) {
/* 191 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.PUBLISHER(publisher));
/*     */     }
/* 193 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\TextItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */