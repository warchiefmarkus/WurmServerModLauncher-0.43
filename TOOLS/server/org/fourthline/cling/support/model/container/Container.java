/*     */ package org.fourthline.cling.support.model.container;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.DescMeta;
/*     */ import org.fourthline.cling.support.model.Res;
/*     */ import org.fourthline.cling.support.model.WriteStatus;
/*     */ import org.fourthline.cling.support.model.item.Item;
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
/*     */ public class Container
/*     */   extends DIDLObject
/*     */ {
/*  52 */   protected Integer childCount = null;
/*     */   protected boolean searchable;
/*  54 */   protected List<DIDLObject.Class> createClasses = new ArrayList<>();
/*  55 */   protected List<DIDLObject.Class> searchClasses = new ArrayList<>();
/*  56 */   protected List<Container> containers = new ArrayList<>();
/*  57 */   protected List<Item> items = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public Container() {}
/*     */   
/*     */   public Container(Container other) {
/*  63 */     super(other);
/*  64 */     setChildCount(other.getChildCount());
/*  65 */     setSearchable(other.isSearchable());
/*  66 */     setCreateClasses(other.getCreateClasses());
/*  67 */     setSearchClasses(other.getSearchClasses());
/*  68 */     setItems(other.getItems());
/*     */   }
/*     */   
/*     */   public Container(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, DIDLObject.Class clazz, List<Res> resources, List<DIDLObject.Property> properties, List<DescMeta> descMetadata) {
/*  72 */     super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
/*     */   }
/*     */   
/*     */   public Container(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, DIDLObject.Class clazz, List<Res> resources, List<DIDLObject.Property> properties, List<DescMeta> descMetadata, Integer childCount, boolean searchable, List<DIDLObject.Class> createClasses, List<DIDLObject.Class> searchClasses, List<Item> items) {
/*  76 */     super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
/*  77 */     this.childCount = childCount;
/*  78 */     this.searchable = searchable;
/*  79 */     this.createClasses = createClasses;
/*  80 */     this.searchClasses = searchClasses;
/*  81 */     this.items = items;
/*     */   }
/*     */   
/*     */   public Container(String id, Container parent, String title, String creator, DIDLObject.Class clazz, Integer childCount) {
/*  85 */     this(id, parent.getId(), title, creator, true, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), childCount, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*     */   }
/*     */   
/*     */   public Container(String id, String parentID, String title, String creator, DIDLObject.Class clazz, Integer childCount) {
/*  89 */     this(id, parentID, title, creator, true, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), childCount, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*     */   }
/*     */   
/*     */   public Container(String id, Container parent, String title, String creator, DIDLObject.Class clazz, Integer childCount, boolean searchable, List<DIDLObject.Class> createClasses, List<DIDLObject.Class> searchClasses, List<Item> items) {
/*  93 */     this(id, parent.getId(), title, creator, true, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), childCount, searchable, createClasses, searchClasses, items);
/*     */   }
/*     */   
/*     */   public Container(String id, String parentID, String title, String creator, DIDLObject.Class clazz, Integer childCount, boolean searchable, List<DIDLObject.Class> createClasses, List<DIDLObject.Class> searchClasses, List<Item> items) {
/*  97 */     this(id, parentID, title, creator, true, null, clazz, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), childCount, searchable, createClasses, searchClasses, items);
/*     */   }
/*     */   
/*     */   public Integer getChildCount() {
/* 101 */     return this.childCount;
/*     */   }
/*     */   
/*     */   public void setChildCount(Integer childCount) {
/* 105 */     this.childCount = childCount;
/*     */   }
/*     */   
/*     */   public boolean isSearchable() {
/* 109 */     return this.searchable;
/*     */   }
/*     */   
/*     */   public void setSearchable(boolean searchable) {
/* 113 */     this.searchable = searchable;
/*     */   }
/*     */   
/*     */   public List<DIDLObject.Class> getCreateClasses() {
/* 117 */     return this.createClasses;
/*     */   }
/*     */   
/*     */   public void setCreateClasses(List<DIDLObject.Class> createClasses) {
/* 121 */     this.createClasses = createClasses;
/*     */   }
/*     */   
/*     */   public List<DIDLObject.Class> getSearchClasses() {
/* 125 */     return this.searchClasses;
/*     */   }
/*     */   
/*     */   public void setSearchClasses(List<DIDLObject.Class> searchClasses) {
/* 129 */     this.searchClasses = searchClasses;
/*     */   }
/*     */   
/*     */   public Container getFirstContainer() {
/* 133 */     return getContainers().get(0);
/*     */   }
/*     */   
/*     */   public Container addContainer(Container container) {
/* 137 */     getContainers().add(container);
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public List<Container> getContainers() {
/* 142 */     return this.containers;
/*     */   }
/*     */   
/*     */   public void setContainers(List<Container> containers) {
/* 146 */     this.containers = containers;
/*     */   }
/*     */   
/*     */   public List<Item> getItems() {
/* 150 */     return this.items;
/*     */   }
/*     */   
/*     */   public void setItems(List<Item> items) {
/* 154 */     this.items = items;
/*     */   }
/*     */   
/*     */   public Container addItem(Item item) {
/* 158 */     getItems().add(item);
/* 159 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\Container.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */