/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
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
/*     */ public class PlaylistItem
/*     */   extends Item
/*     */ {
/*  34 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.playlistItem");
/*     */   
/*     */   public PlaylistItem() {
/*  37 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public PlaylistItem(Item other) {
/*  41 */     super(other);
/*     */   }
/*     */   
/*     */   public PlaylistItem(String id, Container parent, String title, String creator, Res... resource) {
/*  45 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public PlaylistItem(String id, String parentID, String title, String creator, Res... resource) {
/*  49 */     super(id, parentID, title, creator, CLASS);
/*  50 */     if (resource != null) {
/*  51 */       getResources().addAll(Arrays.asList(resource));
/*     */     }
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstArtist() {
/*  56 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.ARTIST.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getArtists() {
/*  60 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.ARTIST.class);
/*  61 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistItem setArtists(PersonWithRole[] artists) {
/*  65 */     removeProperties(DIDLObject.Property.UPNP.ARTIST.class);
/*  66 */     for (PersonWithRole artist : artists) {
/*  67 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(artist));
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstGenre() {
/*  73 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
/*     */   }
/*     */   
/*     */   public String[] getGenres() {
/*  77 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.GENRE.class);
/*  78 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public PlaylistItem setGenres(String[] genres) {
/*  82 */     removeProperties(DIDLObject.Property.UPNP.GENRE.class);
/*  83 */     for (String genre : genres) {
/*  84 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(genre));
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  90 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public PlaylistItem setDescription(String description) {
/*  94 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(description));
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public String getLongDescription() {
/*  99 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.LONG_DESCRIPTION.class);
/*     */   }
/*     */   
/*     */   public PlaylistItem setLongDescription(String description) {
/* 103 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(description));
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/* 108 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.LANGUAGE.class);
/*     */   }
/*     */   
/*     */   public PlaylistItem setLanguage(String language) {
/* 112 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(language));
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/* 117 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public PlaylistItem setStorageMedium(StorageMedium storageMedium) {
/* 121 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 126 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public PlaylistItem setDate(String date) {
/* 130 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 131 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\PlaylistItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */