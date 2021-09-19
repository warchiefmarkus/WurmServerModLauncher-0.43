/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.MaskerPaneSkin;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ProgressIndicator;
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
/*     */ public class MaskerPane
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private final DoubleProperty progress;
/*     */   private final ObjectProperty<Node> progressNode;
/*     */   private final BooleanProperty progressVisible;
/*     */   private final StringProperty text;
/*     */   
/*     */   public final DoubleProperty progressProperty() {
/*     */     return this.progress;
/*     */   }
/*     */   
/*     */   public final double getProgress() {
/*     */     return this.progress.get();
/*     */   }
/*     */   
/*     */   public final void setProgress(double progress) {
/*     */     this.progress.set(progress);
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Node> progressNodeProperty() {
/*     */     return this.progressNode;
/*     */   }
/*     */   
/*     */   public final Node getProgressNode() {
/*     */     return (Node)this.progressNode.get();
/*     */   }
/*     */   
/*     */   public final void setProgressNode(Node progressNode) {
/*     */     this.progressNode.set(progressNode);
/*     */   }
/*     */   
/*     */   public final BooleanProperty progressVisibleProperty() {
/*     */     return this.progressVisible;
/*     */   }
/*     */   
/*     */   public final boolean getProgressVisible() {
/*     */     return this.progressVisible.get();
/*     */   }
/*     */   
/*     */   public final void setProgressVisible(boolean progressVisible) {
/*     */     this.progressVisible.set(progressVisible);
/*     */   }
/*     */   
/*     */   public MaskerPane() {
/*  72 */     this.progress = (DoubleProperty)new SimpleDoubleProperty(this, "progress", -1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     this.progressNode = (ObjectProperty<Node>)new SimpleObjectProperty<Node>()
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public String getName()
/*     */         {
/*  85 */           return "progressNode"; } public Object getBean() {
/*  86 */           return MaskerPane.this;
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     this.progressVisible = (BooleanProperty)new SimpleBooleanProperty(this, "progressVisible", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     this.text = (StringProperty)new SimpleStringProperty(this, "text", "Please Wait...");
/* 100 */     getStyleClass().add("masker-pane"); } public final StringProperty textProperty() { return this.text; }
/* 101 */   public final String getText() { return (String)this.text.get(); } public final void setText(String text) {
/* 102 */     this.text.set(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/* 113 */     return (Skin<?>)new MaskerPaneSkin(this);
/*     */   }
/*     */   public String getUserAgentStylesheet() {
/* 116 */     return getUserAgentStylesheet(MaskerPane.class, "maskerpane.css");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\MaskerPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */