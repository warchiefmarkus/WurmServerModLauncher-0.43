/*    */ package org.fourthline.cling.support.model.container;
/*    */ 
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
/*    */ public class MusicGenre
/*    */   extends GenreContainer
/*    */ {
/* 23 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.genre.musicGenre");
/*    */   
/*    */   public MusicGenre() {
/* 26 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public MusicGenre(Container other) {
/* 30 */     super(other);
/*    */   }
/*    */   
/*    */   public MusicGenre(String id, Container parent, String title, String creator, Integer childCount) {
/* 34 */     this(id, parent.getId(), title, creator, childCount);
/*    */   }
/*    */   
/*    */   public MusicGenre(String id, String parentID, String title, String creator, Integer childCount) {
/* 38 */     super(id, parentID, title, creator, childCount);
/* 39 */     setClazz(CLASS);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\MusicGenre.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */