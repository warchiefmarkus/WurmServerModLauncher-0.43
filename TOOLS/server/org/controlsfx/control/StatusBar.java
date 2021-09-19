/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import impl.org.controlsfx.skin.StatusBarSkin;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
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
/*     */ public class StatusBar
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private final StringProperty text;
/*     */   private final ObjectProperty<Node> graphic;
/*     */   private final StringProperty styleTextProperty;
/*     */   private final ObservableList<Node> leftItems;
/*     */   private final ObservableList<Node> rightItems;
/*     */   private final DoubleProperty progress;
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new StatusBarSkin(this);
/*     */   }
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(StatusBar.class, "statusbar.css");
/*     */   }
/*     */   
/*     */   public final StringProperty textProperty() {
/*     */     return this.text;
/*     */   }
/*     */   
/*     */   public final void setText(String text) {
/*     */     textProperty().set(text);
/*     */   }
/*     */   
/*     */   public final String getText() {
/*     */     return (String)textProperty().get();
/*     */   }
/*     */   
/*     */   public StatusBar() {
/*  96 */     this
/*  97 */       .text = (StringProperty)new SimpleStringProperty(this, "text", Localization.localize(Localization.asKey("statusbar.ok")));
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
/* 126 */     this.graphic = (ObjectProperty<Node>)new SimpleObjectProperty(this, "graphic");
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
/* 157 */     this.styleTextProperty = (StringProperty)new SimpleStringProperty();
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
/* 187 */     this
/* 188 */       .leftItems = FXCollections.observableArrayList();
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
/* 199 */     this
/* 200 */       .rightItems = FXCollections.observableArrayList();
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
/* 211 */     this.progress = (DoubleProperty)new SimpleDoubleProperty(this, "progress");
/*     */     getStyleClass().add("status-bar");
/*     */   } public final ObjectProperty<Node> graphicProperty() {
/*     */     return this.graphic;
/*     */   } public final Node getGraphic() {
/*     */     return (Node)graphicProperty().get();
/*     */   } public final void setGraphic(Node node) {
/*     */     graphicProperty().set(node);
/*     */   }
/*     */   public final DoubleProperty progressProperty() {
/* 221 */     return this.progress;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStyleText(String style) {
/*     */     this.styleTextProperty.set(style);
/*     */   }
/*     */   
/*     */   public final void setProgress(double progress) {
/* 230 */     progressProperty().set(progress);
/*     */   }
/*     */   public String getStyleText() {
/*     */     return (String)this.styleTextProperty.get();
/*     */   }
/*     */   public final StringProperty styleTextProperty() {
/*     */     return this.styleTextProperty;
/*     */   }
/*     */   public final double getProgress() {
/* 239 */     return progressProperty().get();
/*     */   }
/*     */   
/*     */   public final ObservableList<Node> getLeftItems() {
/*     */     return this.leftItems;
/*     */   }
/*     */   
/*     */   public final ObservableList<Node> getRightItems() {
/*     */     return this.rightItems;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\StatusBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */