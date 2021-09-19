/*     */ package org.controlsfx.control.textfield;
/*     */ 
/*     */ import impl.org.controlsfx.skin.CustomTextFieldSkin;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.PasswordField;
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
/*     */ public class CustomPasswordField
/*     */   extends PasswordField
/*     */ {
/*     */   private ObjectProperty<Node> left;
/*     */   
/*     */   public CustomPasswordField() {
/*  81 */     this.left = (ObjectProperty<Node>)new SimpleObjectProperty(this, "left");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     this.right = (ObjectProperty<Node>)new SimpleObjectProperty(this, "right");
/*     */     getStyleClass().addAll((Object[])new String[] { "custom-text-field", "custom-password-field" });
/*     */   }
/*     */   private ObjectProperty<Node> right;
/*     */   public final ObjectProperty<Node> leftProperty() {
/*     */     return this.left;
/*     */   }
/*     */   public final ObjectProperty<Node> rightProperty() {
/* 120 */     return this.right;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Node getLeft() {
/*     */     return (Node)this.left.get();
/*     */   }
/*     */   
/*     */   public final Node getRight() {
/* 129 */     return (Node)this.right.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setLeft(Node value) {
/*     */     this.left.set(value);
/*     */   }
/*     */   
/*     */   public final void setRight(Node value) {
/* 138 */     this.right.set(value);
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
/* 153 */     return (Skin<?>)new CustomTextFieldSkin((TextField)this) {
/*     */         public ObjectProperty<Node> leftProperty() {
/* 155 */           return CustomPasswordField.this.leftProperty();
/*     */         }
/*     */         
/*     */         public ObjectProperty<Node> rightProperty() {
/* 159 */           return CustomPasswordField.this.rightProperty();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/* 168 */     return CustomTextField.class.getResource("customtextfield.css").toExternalForm();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\textfield\CustomPasswordField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */