/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.SegmentedButtonSkin;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.ToggleButton;
/*     */ import javafx.scene.control.ToggleGroup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SegmentedButton
/*     */   extends ControlsFXControl
/*     */ {
/*     */   public static final String STYLE_CLASS_DARK = "dark";
/*     */   private final ObservableList<ToggleButton> buttons;
/* 141 */   private final ObjectProperty<ToggleGroup> toggleGroup = (ObjectProperty<ToggleGroup>)new SimpleObjectProperty(new ToggleGroup());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SegmentedButton() {
/* 153 */     this((ObservableList<ToggleButton>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SegmentedButton(ToggleButton... buttons) {
/* 164 */     this((buttons == null) ? 
/* 165 */         FXCollections.observableArrayList() : 
/* 166 */         FXCollections.observableArrayList((Object[])buttons));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SegmentedButton(ObservableList<ToggleButton> buttons) {
/* 176 */     getStyleClass().add("segmented-button");
/* 177 */     this.buttons = (buttons == null) ? FXCollections.observableArrayList() : buttons;
/*     */ 
/*     */ 
/*     */     
/* 181 */     setFocusTraversable(false);
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
/* 195 */     return (Skin<?>)new SegmentedButtonSkin(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObservableList<ToggleButton> getButtons() {
/* 205 */     return this.buttons;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectProperty<ToggleGroup> toggleGroupProperty() {
/* 212 */     return this.toggleGroup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToggleGroup getToggleGroup() {
/* 219 */     return (ToggleGroup)toggleGroupProperty().getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToggleGroup(ToggleGroup toggleGroup) {
/* 226 */     toggleGroupProperty().setValue(toggleGroup);
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
/*     */   public String getUserAgentStylesheet() {
/* 238 */     return getUserAgentStylesheet(SegmentedButton.class, "segmentedbutton.css");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\SegmentedButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */