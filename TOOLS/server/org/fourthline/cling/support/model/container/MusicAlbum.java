/*     */ package org.fourthline.cling.support.model.container;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Person;
/*     */ import org.fourthline.cling.support.model.PersonWithRole;
/*     */ import org.fourthline.cling.support.model.item.Item;
/*     */ import org.fourthline.cling.support.model.item.MusicTrack;
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
/*     */ public class MusicAlbum
/*     */   extends Album
/*     */ {
/*  34 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.album.musicAlbum");
/*     */   
/*     */   public MusicAlbum() {
/*  37 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public MusicAlbum(Container other) {
/*  41 */     super(other);
/*     */   }
/*     */   
/*     */   public MusicAlbum(String id, Container parent, String title, String creator, Integer childCount) {
/*  45 */     this(id, parent.getId(), title, creator, childCount, new ArrayList<>());
/*     */   }
/*     */   
/*     */   public MusicAlbum(String id, Container parent, String title, String creator, Integer childCount, List<MusicTrack> musicTracks) {
/*  49 */     this(id, parent.getId(), title, creator, childCount, musicTracks);
/*     */   }
/*     */   
/*     */   public MusicAlbum(String id, String parentID, String title, String creator, Integer childCount) {
/*  53 */     this(id, parentID, title, creator, childCount, new ArrayList<>());
/*     */   }
/*     */   
/*     */   public MusicAlbum(String id, String parentID, String title, String creator, Integer childCount, List<MusicTrack> musicTracks) {
/*  57 */     super(id, parentID, title, creator, childCount);
/*  58 */     setClazz(CLASS);
/*  59 */     addMusicTracks(musicTracks);
/*     */   }
/*     */   
/*     */   public PersonWithRole getFirstArtist() {
/*  63 */     return (PersonWithRole)getFirstPropertyValue(DIDLObject.Property.UPNP.ARTIST.class);
/*     */   }
/*     */   
/*     */   public PersonWithRole[] getArtists() {
/*  67 */     List<PersonWithRole> list = getPropertyValues(DIDLObject.Property.UPNP.ARTIST.class);
/*  68 */     return list.<PersonWithRole>toArray(new PersonWithRole[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicAlbum setArtists(PersonWithRole[] artists) {
/*  72 */     removeProperties(DIDLObject.Property.UPNP.ARTIST.class);
/*  73 */     for (PersonWithRole artist : artists) {
/*  74 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(artist));
/*     */     }
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstGenre() {
/*  80 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
/*     */   }
/*     */   
/*     */   public String[] getGenres() {
/*  84 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.GENRE.class);
/*  85 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicAlbum setGenres(String[] genres) {
/*  89 */     removeProperties(DIDLObject.Property.UPNP.GENRE.class);
/*  90 */     for (String genre : genres) {
/*  91 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(genre));
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Person getFirstProducer() {
/*  97 */     return (Person)getFirstPropertyValue(DIDLObject.Property.UPNP.PRODUCER.class);
/*     */   }
/*     */   
/*     */   public Person[] getProducers() {
/* 101 */     List<Person> list = getPropertyValues(DIDLObject.Property.UPNP.PRODUCER.class);
/* 102 */     return list.<Person>toArray(new Person[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicAlbum setProducers(Person[] persons) {
/* 106 */     removeProperties(DIDLObject.Property.UPNP.PRODUCER.class);
/* 107 */     for (Person p : persons) {
/* 108 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PRODUCER(p));
/*     */     }
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public URI getFirstAlbumArtURI() {
/* 114 */     return (URI)getFirstPropertyValue(DIDLObject.Property.UPNP.ALBUM_ART_URI.class);
/*     */   }
/*     */   
/*     */   public URI[] getAlbumArtURIs() {
/* 118 */     List<URI> list = getPropertyValues(DIDLObject.Property.UPNP.ALBUM_ART_URI.class);
/* 119 */     return list.<URI>toArray(new URI[list.size()]);
/*     */   }
/*     */   
/*     */   public MusicAlbum setAlbumArtURIs(URI[] uris) {
/* 123 */     removeProperties(DIDLObject.Property.UPNP.ALBUM_ART_URI.class);
/* 124 */     for (URI uri : uris) {
/* 125 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ALBUM_ART_URI(uri));
/*     */     }
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public String getToc() {
/* 131 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.TOC.class);
/*     */   }
/*     */   
/*     */   public MusicAlbum setToc(String toc) {
/* 135 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.TOC(toc));
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public MusicTrack[] getMusicTracks() {
/* 140 */     List<MusicTrack> list = new ArrayList<>();
/* 141 */     for (Item item : getItems()) {
/* 142 */       if (item instanceof MusicTrack) list.add((MusicTrack)item); 
/*     */     } 
/* 144 */     return list.<MusicTrack>toArray(new MusicTrack[list.size()]);
/*     */   }
/*     */   
/*     */   public void addMusicTracks(List<MusicTrack> musicTracks) {
/* 148 */     addMusicTracks(musicTracks.<MusicTrack>toArray(new MusicTrack[musicTracks.size()]));
/*     */   }
/*     */   
/*     */   public void addMusicTracks(MusicTrack[] musicTracks) {
/* 152 */     if (musicTracks != null)
/* 153 */       for (MusicTrack musicTrack : musicTracks) {
/* 154 */         musicTrack.setAlbum(getTitle());
/* 155 */         addItem((Item)musicTrack);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\MusicAlbum.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */