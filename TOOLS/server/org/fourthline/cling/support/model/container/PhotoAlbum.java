/*    */ package org.fourthline.cling.support.model.container;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.item.Item;
/*    */ import org.fourthline.cling.support.model.item.Photo;
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
/*    */ public class PhotoAlbum
/*    */   extends Album
/*    */ {
/* 29 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.album.photoAlbum");
/*    */   
/*    */   public PhotoAlbum() {
/* 32 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public PhotoAlbum(Container other) {
/* 36 */     super(other);
/*    */   }
/*    */   
/*    */   public PhotoAlbum(String id, Container parent, String title, String creator, Integer childCount) {
/* 40 */     this(id, parent.getId(), title, creator, childCount, new ArrayList<>());
/*    */   }
/*    */   
/*    */   public PhotoAlbum(String id, Container parent, String title, String creator, Integer childCount, List<Photo> photos) {
/* 44 */     this(id, parent.getId(), title, creator, childCount, photos);
/*    */   }
/*    */   
/*    */   public PhotoAlbum(String id, String parentID, String title, String creator, Integer childCount) {
/* 48 */     this(id, parentID, title, creator, childCount, new ArrayList<>());
/*    */   }
/*    */   
/*    */   public PhotoAlbum(String id, String parentID, String title, String creator, Integer childCount, List<Photo> photos) {
/* 52 */     super(id, parentID, title, creator, childCount);
/* 53 */     setClazz(CLASS);
/* 54 */     addPhotos(photos);
/*    */   }
/*    */   
/*    */   public Photo[] getPhotos() {
/* 58 */     List<Photo> list = new ArrayList<>();
/* 59 */     for (Item item : getItems()) {
/* 60 */       if (item instanceof Photo) list.add((Photo)item); 
/*    */     } 
/* 62 */     return list.<Photo>toArray(new Photo[list.size()]);
/*    */   }
/*    */   
/*    */   public void addPhotos(List<Photo> photos) {
/* 66 */     addPhotos(photos.<Photo>toArray(new Photo[photos.size()]));
/*    */   }
/*    */   
/*    */   public void addPhotos(Photo[] photos) {
/* 70 */     if (photos != null)
/* 71 */       for (Photo photo : photos) {
/* 72 */         photo.setAlbum(getTitle());
/* 73 */         addItem((Item)photo);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\PhotoAlbum.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */