/*    */ package org.fourthline.cling.support.model.item;
/*    */ 
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.Res;
/*    */ import org.fourthline.cling.support.model.container.Container;
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
/*    */ public class Photo
/*    */   extends ImageItem
/*    */ {
/* 28 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.imageItem.photo");
/*    */   
/*    */   public Photo() {
/* 31 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public Photo(Item other) {
/* 35 */     super(other);
/*    */   }
/*    */   
/*    */   public Photo(String id, Container parent, String title, String creator, String album, Res... resource) {
/* 39 */     this(id, parent.getId(), title, creator, album, resource);
/*    */   }
/*    */   
/*    */   public Photo(String id, String parentID, String title, String creator, String album, Res... resource) {
/* 43 */     super(id, parentID, title, creator, resource);
/* 44 */     setClazz(CLASS);
/* 45 */     if (album != null)
/* 46 */       setAlbum(album); 
/*    */   }
/*    */   
/*    */   public String getAlbum() {
/* 50 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.ALBUM.class);
/*    */   }
/*    */   
/*    */   public Photo setAlbum(String album) {
/* 54 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ALBUM(album));
/* 55 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\Photo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */