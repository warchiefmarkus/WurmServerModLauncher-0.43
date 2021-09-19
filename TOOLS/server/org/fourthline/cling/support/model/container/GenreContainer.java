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
/*    */ public class GenreContainer
/*    */   extends Container
/*    */ {
/* 23 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.genre");
/*    */   
/*    */   public GenreContainer() {
/* 26 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public GenreContainer(Container other) {
/* 30 */     super(other);
/*    */   }
/*    */   
/*    */   public GenreContainer(String id, Container parent, String title, String creator, Integer childCount) {
/* 34 */     this(id, parent.getId(), title, creator, childCount);
/*    */   }
/*    */   
/*    */   public GenreContainer(String id, String parentID, String title, String creator, Integer childCount) {
/* 38 */     super(id, parentID, title, creator, CLASS, childCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\GenreContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */