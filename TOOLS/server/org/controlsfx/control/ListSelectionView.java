/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import impl.org.controlsfx.skin.ListSelectionViewSkin;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ListCell;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.util.Callback;
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
/*     */ 
/*     */ public class ListSelectionView<T>
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private static final String DEFAULT_STYLECLASS = "list-selection-view";
/*     */   private final ObjectProperty<Node> sourceHeader;
/*     */   private final ObjectProperty<Node> sourceFooter;
/*     */   private final ObjectProperty<Node> targetHeader;
/*     */   private final ObjectProperty<Node> targetFooter;
/*     */   private ObjectProperty<ObservableList<T>> sourceItems;
/*     */   private ObjectProperty<ObservableList<T>> targetItems;
/*     */   private final ObjectProperty<Orientation> orientation;
/*     */   private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory;
/*     */   
/*     */   protected Skin<ListSelectionView<T>> createDefaultSkin() {
/*     */     return (Skin<ListSelectionView<T>>)new ListSelectionViewSkin(this);
/*     */   }
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(ListSelectionView.class, "listselectionview.css");
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Node> sourceHeaderProperty() {
/*     */     return this.sourceHeader;
/*     */   }
/*     */   
/*     */   public final Node getSourceHeader() {
/*     */     return (Node)this.sourceHeader.get();
/*     */   }
/*     */   
/*     */   public final void setSourceHeader(Node node) {
/*     */     this.sourceHeader.set(node);
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Node> sourceFooterProperty() {
/*     */     return this.sourceFooter;
/*     */   }
/*     */   
/*     */   public final Node getSourceFooter() {
/*     */     return (Node)this.sourceFooter.get();
/*     */   }
/*     */   
/*     */   public final void setSourceFooter(Node node) {
/*     */     this.sourceFooter.set(node);
/*     */   }
/*     */   
/*     */   public ListSelectionView() {
/* 102 */     this.sourceHeader = (ObjectProperty<Node>)new SimpleObjectProperty(this, "sourceHeader");
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
/* 135 */     this.sourceFooter = (ObjectProperty<Node>)new SimpleObjectProperty(this, "sourceFooter");
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
/* 168 */     this.targetHeader = (ObjectProperty<Node>)new SimpleObjectProperty(this, "targetHeader");
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
/* 201 */     this.targetFooter = (ObjectProperty<Node>)new SimpleObjectProperty(this, "targetFooter");
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
/* 307 */     this.orientation = (ObjectProperty<Orientation>)new SimpleObjectProperty(this, "orientation", Orientation.HORIZONTAL); getStyleClass().add("list-selection-view"); Label sourceHeader = new Label(Localization.localize(Localization.asKey("listSelectionView.header.source"))); sourceHeader.getStyleClass().add("list-header-label");
/*     */     sourceHeader.setId("source-header-label");
/*     */     setSourceHeader((Node)sourceHeader);
/*     */     Label targetHeader = new Label(Localization.localize(Localization.asKey("listSelectionView.header.target")));
/*     */     targetHeader.getStyleClass().add("list-header-label");
/*     */     targetHeader.setId("target-header-label");
/*     */     setTargetHeader((Node)targetHeader);
/*     */   } public final ObjectProperty<Node> targetHeaderProperty() { return this.targetHeader; } public final Node getTargetHeader() { return (Node)this.targetHeader.get(); }
/* 315 */   public final ObjectProperty<Orientation> orientationProperty() { return this.orientation; }
/*     */   
/*     */   public final void setTargetHeader(Node node) {
/*     */     this.targetHeader.set(node);
/*     */   }
/*     */   public final ObjectProperty<Node> targetFooterProperty() {
/*     */     return this.targetFooter;
/*     */   }
/* 323 */   public final void setOrientation(Orientation value) { orientationProperty().set(value); }
/*     */   public final Node getTargetFooter() { return (Node)this.targetFooter.get(); }
/*     */   public final void setTargetFooter(Node node) { this.targetFooter.set(node); }
/*     */   public final void setSourceItems(ObservableList<T> value) { sourceItemsProperty().set(value); }
/*     */   public final ObservableList<T> getSourceItems() { return (ObservableList<T>)sourceItemsProperty().get(); }
/*     */   public final ObjectProperty<ObservableList<T>> sourceItemsProperty() { if (this.sourceItems == null)
/*     */       this.sourceItems = (ObjectProperty<ObservableList<T>>)new SimpleObjectProperty(this, "sourceItems", FXCollections.observableArrayList()); 
/*     */     return this.sourceItems; } public final Orientation getOrientation() {
/* 331 */     return (Orientation)this.orientation.get();
/*     */   } public final void setTargetItems(ObservableList<T> value) {
/*     */     targetItemsProperty().set(value);
/*     */   } public final ObservableList<T> getTargetItems() {
/*     */     return (ObservableList<T>)targetItemsProperty().get();
/*     */   }
/*     */   public final ObjectProperty<ObservableList<T>> targetItemsProperty() {
/*     */     if (this.targetItems == null)
/*     */       this.targetItems = (ObjectProperty<ObservableList<T>>)new SimpleObjectProperty(this, "targetItems", FXCollections.observableArrayList()); 
/*     */     return this.targetItems;
/*     */   }
/*     */   public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
/* 343 */     cellFactoryProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
/* 350 */     return (this.cellFactory == null) ? null : (Callback<ListView<T>, ListCell<T>>)this.cellFactory.get();
/*     */   }
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
/*     */   public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
/* 365 */     if (this.cellFactory == null) {
/* 366 */       this.cellFactory = (ObjectProperty<Callback<ListView<T>, ListCell<T>>>)new SimpleObjectProperty(this, "cellFactory");
/*     */     }
/* 368 */     return this.cellFactory;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\ListSelectionView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */