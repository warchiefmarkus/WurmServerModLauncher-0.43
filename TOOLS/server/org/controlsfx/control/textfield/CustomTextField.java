/*     */ package org.controlsfx.control.textfield;
/*     */ 
/*     */ import impl.org.controlsfx.skin.CustomTextFieldSkin;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomTextField
/*     */   extends TextField
/*     */ {
/*     */   private ObjectProperty<Node> left;
/*     */   
/*     */   public CustomTextField() {
/*  89 */     this.left = (ObjectProperty<Node>)new SimpleObjectProperty(this, "left");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     this.right = (ObjectProperty<Node>)new SimpleObjectProperty(this, "right");
/*     */     getStyleClass().add("custom-text-field");
/*     */   }
/*     */   private ObjectProperty<Node> right;
/*     */   public final ObjectProperty<Node> leftProperty() {
/*     */     return this.left;
/*     */   }
/*     */   public final ObjectProperty<Node> rightProperty() {
/* 128 */     return this.right;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Node getLeft() {
/*     */     return (Node)this.left.get();
/*     */   }
/*     */   
/*     */   public final Node getRight() {
/* 137 */     return (Node)this.right.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setLeft(Node value) {
/*     */     this.left.set(value);
/*     */   }
/*     */   
/*     */   public final void setRight(Node value) {
/* 146 */     this.right.set(value);
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
/*     */   protected Skin<?> createDefaultSkin() {
/* 161 */     return (Skin<?>)new CustomTextFieldSkin(this) {
/*     */         public ObjectProperty<Node> leftProperty() {
/* 163 */           return CustomTextField.this.leftProperty();
/*     */         }
/*     */         
/*     */         public ObjectProperty<Node> rightProperty() {
/* 167 */           return CustomTextField.this.rightProperty();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/* 176 */     return CustomTextField.class.getResource("customtextfield.css").toExternalForm();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\textfield\CustomTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */