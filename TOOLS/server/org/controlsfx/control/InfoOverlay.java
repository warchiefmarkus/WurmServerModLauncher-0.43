/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.InfoOverlaySkin;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.image.ImageView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InfoOverlay
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private ObjectProperty<Node> content;
/*     */   private StringProperty text;
/*     */   private BooleanProperty showOnHover;
/*     */   private static final String DEFAULT_STYLE_CLASS = "info-overlay";
/*     */   
/*     */   public InfoOverlay() {
/*  67 */     this((Node)null, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InfoOverlay(String imageUrl, String text) {
/*  78 */     this((Node)new ImageView(imageUrl), text);
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
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new InfoOverlaySkin(this);
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
/*     */   public final ObjectProperty<Node> contentProperty() {
/*     */     return this.content;
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
/*     */   public InfoOverlay(Node content, String text) {
/* 118 */     this.content = (ObjectProperty<Node>)new SimpleObjectProperty(this, "content");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     this.text = (StringProperty)new SimpleStringProperty(this, "text");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     this.showOnHover = (BooleanProperty)new SimpleBooleanProperty(this, "showOnHover", true);
/*     */     getStyleClass().setAll((Object[])new String[] { "info-overlay" });
/*     */     setContent(content);
/*     */     setText(text);
/*     */   } public final void setContent(Node content) {
/*     */     contentProperty().set(content);
/*     */   }
/*     */   public final BooleanProperty showOnHoverProperty() {
/* 185 */     return this.showOnHover;
/*     */   }
/*     */   
/*     */   public final Node getContent() {
/*     */     return (Node)contentProperty().get();
/*     */   }
/*     */   
/*     */   public final boolean isShowOnHover() {
/* 193 */     return showOnHoverProperty().get();
/*     */   } public final StringProperty textProperty() {
/*     */     return this.text;
/*     */   } public final String getText() {
/*     */     return (String)textProperty().get();
/*     */   }
/*     */   public final void setText(String text) {
/*     */     textProperty().set(text);
/*     */   }
/*     */   public final void setShowOnHover(boolean value) {
/* 203 */     showOnHoverProperty().set(value);
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
/*     */   public String getUserAgentStylesheet() {
/* 218 */     return getUserAgentStylesheet(InfoOverlay.class, "info-overlay.css");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\InfoOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */