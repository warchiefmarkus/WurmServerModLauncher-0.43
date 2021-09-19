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
/*     */ public class MusicTrack
/*     */   extends AudioItem
/*     */ {
/*  34 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.audioItem.musicTrack");
/*     */   
/*     */   public MusicTrack() {
/*  37 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public MusicTrack(Item other) {
/*  41 */     super(other);
/*     */   }
/*     */   
/*     */   public MusicTrack(String id, Container parent, String title, String creator, String album, String artist, Res... resource) {
/*  45 */     this(id, parent.getId(), title, creator, album, artist, resource);
/*     */   }
/*     */   
/*     */   public MusicTrack(String id, Container parent, String title, String creator, String album, PersonWithRole artist, Res... resource) {
/*  49 */     this(id, parent.getId(), title, creator, album, artist, resource);
/*     */   }
/*     */   
/*     */   public MusicTrack(String id, String parentID, String title, String creator, String album, String artist, Res... resource) {
/*  53 */     this(id, parentID, title, creator, album, (artist == null) ? null : new PersonWithRole(artist), resource);
/*     */   }
/*     */   
/*     */   public MusicTrack(String id, String parentID, String title, String creator, String album, PersonWithRole artist, Res... resource) {
/*  57 */     super(id, parentID, title, creator, resource);
/*  58 */     setClazz(CLASS);
/*  59 */     if (album != null)
/*  60 */       setAlbum(album); 
/*  61 */     if (artist != null)
/*  62 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(artist)); 
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstArtist() {
/*  66 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.ARTIST.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getArtists() {
/*  70 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.ARTIST.class);
/*  71 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicTrack setArtists(PersonWithRole[] artists) {
/*  75 */     removeProperties(DIDLObject.Property.UPNP.ARTIST.class);
/*  76 */     for (PersonWithRole artist : artists) {
/*  77 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(artist));
/*     */     }
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public String getAlbum() {
/*  83 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.ALBUM.class);
/*     */   }
/*     */   
/*     */   public MusicTrack setAlbum(String album) {
/*  87 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ALBUM(album));
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Integer getOriginalTrackNumber() {
/*  92 */     return (Integer)getFirstPropertyValue(DIDLObject.Property.UPNP.ORIGINAL_TRACK_NUMBER.class);
/*     */   }
/*     */   
/*     */   public MusicTrack setOriginalTrackNumber(Integer number) {
/*  96 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ORIGINAL_TRACK_NUMBER(number));
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstPlaylist() {
/* 101 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.PLAYLIST.class);
/*     */   }
/*     */   
/*     */   public String[] getPlaylists() {
/* 105 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.PLAYLIST.class);
/* 106 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicTrack setPlaylists(String[] playlists) {
/* 110 */     removeProperties(DIDLObject.Property.UPNP.PLAYLIST.class);
/* 111 */     for (String s : playlists) {
/* 112 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PLAYLIST(s));
/*     */     }
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/* 118 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public MusicTrack setStorageMedium(StorageMedium storageMedium) {
/* 122 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstContributor() {
/* 127 */     return (Person)getFirstPropertyValue(DIDLObject.Property.DC.CONTRIBUTOR.class);
/*     */   }
/*     */   
/*     */   public Person[] getContributors() {
/* 131 */     List<Person> list = getPropertyValues(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 132 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicTrack setContributors(Person[] contributors) {
/* 136 */     removeProperties(DIDLObject.Property.DC.CONTRIBUTOR.class);
/* 137 */     for (Person p : contributors) {
/* 138 */       addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(p));
/*     */     }
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 144 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.DATE.class);
/*     */   }
/*     */   
/*     */   public MusicTrack setDate(String date) {
/* 148 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(date));
/* 149 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\MusicTrack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */