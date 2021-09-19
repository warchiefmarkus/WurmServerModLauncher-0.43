/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.ToggleSwitchSkin;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.BooleanPropertyBase;
/*     */ import javafx.css.PseudoClass;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.scene.control.Labeled;
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
/*     */ public class ToggleSwitch
/*     */   extends Labeled
/*     */ {
/*     */   private BooleanProperty selected;
/*     */   private static final String DEFAULT_STYLE_CLASS = "toggle-switch";
/*     */   
/*     */   public ToggleSwitch() {
/*  62 */     initialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToggleSwitch(String text) {
/*  71 */     super(text);
/*  72 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  76 */     getStyleClass().setAll((Object[])new String[] { "toggle-switch" });
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSelected(boolean value) {
/*  94 */     selectedProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSelected() {
/* 101 */     return (this.selected == null) ? false : this.selected.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BooleanProperty selectedProperty() {
/* 108 */     if (this.selected == null) {
/* 109 */       this.selected = (BooleanProperty)new BooleanPropertyBase() {
/*     */           protected void invalidated() {
/* 111 */             Boolean v = Boolean.valueOf(get());
/* 112 */             ToggleSwitch.this.pseudoClassStateChanged(ToggleSwitch.PSEUDO_CLASS_SELECTED, v.booleanValue());
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public Object getBean() {
/* 118 */             return ToggleSwitch.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public String getName() {
/* 123 */             return "selected";
/*     */           }
/*     */         };
/*     */     }
/* 127 */     return this.selected;
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
/*     */   public void fire() {
/* 142 */     if (!isDisabled()) {
/* 143 */       setSelected(!isSelected());
/* 144 */       fireEvent((Event)new ActionEvent());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/* 150 */     return (Skin<?>)new ToggleSwitchSkin(this);
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
/* 163 */   private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/* 168 */     return ToggleSwitch.class.getResource("toggleswitch.css").toExternalForm();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\ToggleSwitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */