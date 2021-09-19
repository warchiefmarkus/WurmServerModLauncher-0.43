/*    */ package org.fourthline.cling.support.model.item;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.DescMeta;
/*    */ import org.fourthline.cling.support.model.Res;
/*    */ import org.fourthline.cling.support.model.WriteStatus;
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
/*    */ 
/*    */ public class Item
/*    */   extends DIDLObject
/*    */ {
/*    */   protected String refID;
/*    */   
/*    */   public Item() {}
/*    */   
/*    */   public Item(Item other) {
/* 38 */     super(other);
/* 39 */     setRefID(other.getRefID());
/*    */   }
/*    */   
/*    */   public Item(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, DIDLObject.Class clazz, List<Res> resources, List<DIDLObject.Property> properties, List<DescMeta> descMetadata) {
/* 43 */     super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
/*    */   }
/*    */   
/*    */   public Item(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, DIDLObject.Class clazz, List<Res> resources, List<DIDLObject.Property> properties, List<DescMeta> descMetadata, String refID) {
/* 47 */     super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
/* 48 */     this.refID = refID;
/*    */   }
/*    */   
/*    */   public Item(String id, Container parent, String title, String creator, DIDLObject.Class clazz) {
/* 52 */     this(id, parent.getId(), title, creator, false, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*    */   }
/*    */   
/*    */   public Item(String id, Container parent, String title, String creator, DIDLObject.Class clazz, String refID) {
/* 56 */     this(id, parent.getId(), title, creator, false, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), refID);
/*    */   }
/*    */   
/*    */   public Item(String id, String parentID, String title, String creator, DIDLObject.Class clazz) {
/* 60 */     this(id, parentID, title, creator, false, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*    */   }
/*    */   
/*    */   public Item(String id, String parentID, String title, String creator, DIDLObject.Class clazz, String refID) {
/* 64 */     this(id, parentID, title, creator, false, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), refID);
/*    */   }
/*    */   
/*    */   public String getRefID() {
/* 68 */     return this.refID;
/*    */   }
/*    */   
/*    */   public void setRefID(String refID) {
/* 72 */     this.refID = refID;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\Item.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */