/*    */ package org.fourthline.cling.support.model.container;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MusicArtist
/*    */   extends PersonContainer
/*    */ {
/* 28 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.person.musicArtist");
/*    */   
/*    */   public MusicArtist() {
/* 31 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public MusicArtist(Container other) {
/* 35 */     super(other);
/*    */   }
/*    */   
/*    */   public MusicArtist(String id, Container parent, String title, String creator, Integer childCount) {
/* 39 */     this(id, parent.getId(), title, creator, childCount);
/*    */   }
/*    */   
/*    */   public MusicArtist(String id, String parentID, String title, String creator, Integer childCount) {
/* 43 */     super(id, parentID, title, creator, childCount);
/* 44 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public String getFirstGenre() {
/* 48 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
/*    */   }
/*    */   
/*    */   public String[] getGenres() {
/* 52 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.GENRE.class);
/* 53 */     return list.<String>toArray(new String[list.size()]);
/*    */   }
/*    */   
/*    */   public MusicArtist setGenres(String[] genres) {
/* 57 */     removeProperties(DIDLObject.Property.UPNP.GENRE.class);
/* 58 */     for (String genre : genres) {
/* 59 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(genre));
/*    */     }
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public URI getArtistDiscographyURI() {
/* 65 */     return (URI)getFirstPropertyValue(DIDLObject.Property.UPNP.ARTIST_DISCO_URI.class);
/*    */   }
/*    */   
/*    */   public MusicArtist setArtistDiscographyURI(URI uri) {
/* 69 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST_DISCO_URI(uri));
/* 70 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\MusicArtist.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */