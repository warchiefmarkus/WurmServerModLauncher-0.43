/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
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
/*     */ public class MusicVideoClip
/*     */   extends VideoItem
/*     */ {
/*  34 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.videoItem.musicVideoClip");
/*     */   
/*     */   public MusicVideoClip() {
/*  37 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public MusicVideoClip(Item other) {
/*  41 */     super(other);
/*     */   }
/*     */   
/*     */   public MusicVideoClip(String id, Container parent, String title, String creator, Res... resource) {
/*  45 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public MusicVideoClip(String id, String parentID, String title, String creator, Res... resource) {
/*  49 */     super(id, parentID, title, creator, resource);
/*  50 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstArtist() {
/*  54 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.ARTIST.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getArtists() {
/*  58 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.ARTIST.class);
/*  59 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setArtists(PersonWithRole[] artists) {
/*  63 */     removeProperties(DIDLObject.Property.UPNP.ARTIST.class);
/*  64 */     for (PersonWithRole artist : artists) {
/*  65 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(artist));
/*     */     }
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/*  71 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setStorageMedium(StorageMedium storageMedium) {
/*  75 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public String getAlbum() {
/*  80 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.ALBUM.class);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setAlbum(String album) {
/*  84 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ALBUM(album));
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstScheduledStartTime() {
/*  89 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.SCHEDULED_START_TIME.class);
/*     */   }
/*     */   
/*     */   public String[] getScheduledStartTimes() {
/*  93 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.SCHEDULED_START_TIME.class);
/*  94 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setScheduledStartTimes(String[] strings) {
/*  98 */     removeProperties(DIDLObject.Property.UPNP.SCHEDULED_START_TIME.class);
/*  99 */     for (String s : strings) {
/* 100 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.SCHEDULED_START_TIME(s));
/*     */     }
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstScheduledEndTime() {
/* 106 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.SCHEDULED_END_TIME.class);
/*     */   }
/*     */   
/*     */   public String[] getScheduledEndTimes() {
/* 110 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.SCHEDULED_END_TIME.class);
/* 111 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setScheduledEndTimes(String[] strings) {
/* 115 */     removeProperties(DIDLObject.Property.UPNP.SCHEDULED_END_TIME.class);
/* 116 */     for (String s : strings) {
/* 117 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.SCHEDULED_END_TIME(s));
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstContributor() {
/* 123 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getContributors() {
/* 127 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 128 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setContributors(Person[] contributors) {
/* 132 */     removeProperties(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 133 */     for (Person p : contributors) {
/* 134 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(p));
/*     */     }
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 140 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public MusicVideoClip setDate(String date) {
/* 144 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 145 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\MusicVideoClip.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */