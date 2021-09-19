/*     */ package org.controlsfx.control.action;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import impl.org.controlsfx.i18n.SimpleLocalizedStringProperty;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.beans.NamedArg;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.ObservableMap;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.input.KeyCombination;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Action
/*     */   implements EventHandler<ActionEvent>
/*     */ {
/*     */   private boolean locked = false;
/*     */   private Consumer<ActionEvent> eventHandler;
/*     */   private StringProperty style;
/*     */   private final ObservableList<String> styleClass;
/*     */   private final BooleanProperty selectedProperty;
/*     */   private final StringProperty textProperty;
/*     */   private final BooleanProperty disabledProperty;
/*     */   private final StringProperty longTextProperty;
/*     */   private final ObjectProperty<Node> graphicProperty;
/*     */   private final ObjectProperty<KeyCombination> acceleratorProperty;
/*     */   private ObservableMap<Object, Object> props;
/*     */   
/*     */   public Action(@NamedArg("text") String text) {
/*  84 */     this(text, null);
/*     */   }
/*     */   
/*     */   public Action(Consumer<ActionEvent> eventHandler) {
/*  88 */     this("", eventHandler);
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
/*     */   
/*     */   protected void lock() {
/* 107 */     this.locked = true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setStyle(String value)
/*     */   {
/* 130 */     styleProperty().set(value); } public final String getStyle() {
/* 131 */     return (this.style == null) ? "" : (String)this.style.get();
/*     */   } public final StringProperty styleProperty() {
/* 133 */     if (this.style == null) {
/* 134 */       this.style = (StringProperty)new SimpleStringProperty(this, "style")
/*     */         {
/*     */           public void set(String style) {
/* 137 */             if (Action.this.locked) throw new UnsupportedOperationException("The action is immutable, property change support is disabled."); 
/* 138 */             super.set(style);
/*     */           }
/*     */         };
/*     */     }
/* 142 */     return this.style;
/*     */   }
/*     */   
/*     */   public Action(@NamedArg("text") String text, Consumer<ActionEvent> eventHandler)
/*     */   {
/* 147 */     this.styleClass = FXCollections.observableArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     this.selectedProperty = (BooleanProperty)new SimpleBooleanProperty(this, "selected") {
/*     */         public void set(boolean selected) {
/* 164 */           if (Action.this.locked) throw new UnsupportedOperationException("The action is immutable, property change support is disabled."); 
/* 165 */           super.set(selected);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     this.textProperty = (StringProperty)new SimpleLocalizedStringProperty(this, "text") {
/*     */         public void set(String value) {
/* 200 */           if (Action.this.locked) throw new RuntimeException("The action is immutable, property change support is disabled."); 
/* 201 */           super.set(value);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     this.disabledProperty = (BooleanProperty)new SimpleBooleanProperty(this, "disabled") {
/*     */         public void set(boolean value) {
/* 235 */           if (Action.this.locked) throw new RuntimeException("The action is immutable, property change support is disabled."); 
/* 236 */           super.set(value);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     this.longTextProperty = (StringProperty)new SimpleLocalizedStringProperty(this, "longText") {
/*     */         public void set(String value) {
/* 274 */           if (Action.this.locked) throw new RuntimeException("The action is immutable, property change support is disabled."); 
/* 275 */           super.set(value);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     this.graphicProperty = (ObjectProperty<Node>)new SimpleObjectProperty<Node>(this, "graphic") {
/*     */         public void set(Node value) {
/* 313 */           if (Action.this.locked) throw new RuntimeException("The action is immutable, property change support is disabled."); 
/* 314 */           super.set(value);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     this.acceleratorProperty = (ObjectProperty<KeyCombination>)new SimpleObjectProperty<KeyCombination>(this, "accelerator")
/*     */       {
/* 349 */         public void set(KeyCombination value) { if (Action.this.locked) throw new RuntimeException("The action is immutable, property change support is disabled."); 
/* 350 */           super.set(value); }
/*     */       };
/*     */     setText(text);
/*     */     setEventHandler(eventHandler);
/*     */     getStyleClass().add("action"); }
/*     */   public ObservableList<String> getStyleClass() {
/*     */     return this.styleClass;
/*     */   } public final BooleanProperty selectedProperty() {
/*     */     return this.selectedProperty;
/*     */   } public final boolean isSelected() {
/*     */     return this.selectedProperty.get();
/*     */   } public final void setSelected(boolean selected) {
/*     */     this.selectedProperty.set(selected);
/* 363 */   } public final ObjectProperty<KeyCombination> acceleratorProperty() { return this.acceleratorProperty; }
/*     */   public final StringProperty textProperty() { return this.textProperty; }
/*     */   public final String getText() { return (String)this.textProperty.get(); }
/*     */   public final void setText(String value) { this.textProperty.set(value); }
/*     */   public final BooleanProperty disabledProperty() { return this.disabledProperty; }
/*     */   public final boolean isDisabled() { return this.disabledProperty.get(); }
/*     */   public final void setDisabled(boolean value) { this.disabledProperty.set(value); }
/*     */   public final StringProperty longTextProperty() { return this.longTextProperty; }
/*     */   public final String getLongText() { return Localization.localize((String)this.longTextProperty.get()); }
/* 372 */   public final void setLongText(String value) { this.longTextProperty.set(value); } public final ObjectProperty<Node> graphicProperty() { return this.graphicProperty; } public final Node getGraphic() { return (Node)this.graphicProperty.get(); } public final void setGraphic(Node value) { this.graphicProperty.set(value); } public final KeyCombination getAccelerator() { return (KeyCombination)this.acceleratorProperty.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setAccelerator(KeyCombination value) {
/* 381 */     this.acceleratorProperty.set(value);
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
/*     */   public final synchronized ObservableMap<Object, Object> getProperties() {
/* 396 */     if (this.props == null) this.props = FXCollections.observableHashMap(); 
/* 397 */     return this.props;
/*     */   }
/*     */   
/*     */   protected Consumer<ActionEvent> getEventHandler() {
/* 401 */     return this.eventHandler;
/*     */   }
/*     */   
/*     */   protected void setEventHandler(Consumer<ActionEvent> eventHandler) {
/* 405 */     this.eventHandler = eventHandler;
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
/*     */   public final void handle(ActionEvent event) {
/* 418 */     if (this.eventHandler != null && !isDisabled())
/* 419 */       this.eventHandler.accept(event); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\Action.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */