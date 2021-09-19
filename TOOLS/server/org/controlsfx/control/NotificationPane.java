/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.NotificationPaneSkin;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ReadOnlyBooleanProperty;
/*     */ import javafx.beans.property.ReadOnlyBooleanWrapper;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventType;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
/*     */ import org.controlsfx.control.action.Action;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NotificationPane
/*     */   extends ControlsFXControl
/*     */ {
/*     */   public static final String STYLE_CLASS_DARK = "dark";
/* 158 */   public static final EventType<Event> ON_SHOWING = new EventType(Event.ANY, "NOTIFICATION_PANE_ON_SHOWING");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static final EventType<Event> ON_SHOWN = new EventType(Event.ANY, "NOTIFICATION_PANE_ON_SHOWN");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public static final EventType<Event> ON_HIDING = new EventType(Event.ANY, "NOTIFICATION_PANE_ON_HIDING");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public static final EventType<Event> ON_HIDDEN = new EventType(Event.ANY, "NOTIFICATION_PANE_ON_HIDDEN");
/*     */   
/*     */   private ObjectProperty<Node> content;
/*     */   
/*     */   private StringProperty text;
/*     */   
/*     */   private ObjectProperty<Node> graphic;
/*     */   private ReadOnlyBooleanWrapper showing;
/*     */   private BooleanProperty showFromTop;
/*     */   private ObjectProperty<EventHandler<Event>> onShowing;
/*     */   private ObjectProperty<EventHandler<Event>> onShown;
/*     */   private ObjectProperty<EventHandler<Event>> onHiding;
/*     */   private ObjectProperty<EventHandler<Event>> onHidden;
/*     */   private BooleanProperty closeButtonVisible;
/*     */   private final ObservableList<Action> actions;
/*     */   private static final String DEFAULT_STYLE_CLASS = "notification-pane";
/*     */   
/*     */   public NotificationPane() {
/* 194 */     this((Node)null);
/*     */   }
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new NotificationPaneSkin(this);
/*     */   }
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(NotificationPane.class, "notificationpane.css");
/*     */   }
/*     */   public final ObjectProperty<Node> contentProperty() {
/*     */     return this.content;
/*     */   }
/*     */   public final void setContent(Node value) {
/*     */     this.content.set(value);
/*     */   }
/*     */   public final Node getContent() {
/*     */     return (Node)this.content.get();
/*     */   }
/*     */   
/*     */   public final StringProperty textProperty() {
/*     */     return this.text;
/*     */   }
/*     */   
/*     */   public final void setText(String value) {
/*     */     this.text.set(value);
/*     */   }
/*     */   
/*     */   public final String getText() {
/*     */     return (String)this.text.get();
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Node> graphicProperty() {
/*     */     return this.graphic;
/*     */   }
/*     */   
/*     */   public final void setGraphic(Node value) {
/*     */     this.graphic.set(value);
/*     */   }
/*     */   
/*     */   public final Node getGraphic() {
/*     */     return (Node)this.graphic.get();
/*     */   }
/*     */   
/*     */   public final ReadOnlyBooleanProperty showingProperty() {
/*     */     return this.showing.getReadOnlyProperty();
/*     */   }
/*     */   
/* 240 */   public NotificationPane(Node content) { this.content = (ObjectProperty<Node>)new SimpleObjectProperty(this, "content");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     this.text = (StringProperty)new SimpleStringProperty(this, "text");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     this.graphic = (ObjectProperty<Node>)new SimpleObjectProperty(this, "graphic");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     this.showing = new ReadOnlyBooleanWrapper(this, "showing");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     this.showFromTop = (BooleanProperty)new SimpleBooleanProperty(this, "showFromTop", true) {
/*     */         protected void invalidated() {
/* 373 */           NotificationPane.this.updateStyleClasses();
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     this.onShowing = (ObjectProperty<EventHandler<Event>>)new SimpleObjectProperty<EventHandler<Event>>(this, "onShowing") {
/*     */         protected void invalidated() {
/* 415 */           NotificationPane.this.setEventHandler(NotificationPane.ON_SHOWING, (EventHandler)get());
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
/* 427 */     this.onShown = (ObjectProperty<EventHandler<Event>>)new SimpleObjectProperty<EventHandler<Event>>(this, "onShown") {
/*     */         protected void invalidated() {
/* 429 */           NotificationPane.this.setEventHandler(NotificationPane.ON_SHOWN, (EventHandler)get());
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
/* 441 */     this.onHiding = (ObjectProperty<EventHandler<Event>>)new SimpleObjectProperty<EventHandler<Event>>(this, "onHiding") {
/*     */         protected void invalidated() {
/* 443 */           NotificationPane.this.setEventHandler(NotificationPane.ON_HIDING, (EventHandler)get());
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
/* 455 */     this.onHidden = (ObjectProperty<EventHandler<Event>>)new SimpleObjectProperty<EventHandler<Event>>(this, "onHidden") {
/*     */         protected void invalidated() {
/* 457 */           NotificationPane.this.setEventHandler(NotificationPane.ON_HIDDEN, (EventHandler)get());
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 462 */     this.closeButtonVisible = (BooleanProperty)new SimpleBooleanProperty(this, "closeButtonVisible", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 497 */     this.actions = FXCollections.observableArrayList(); getStyleClass().add("notification-pane"); setContent(content);
/*     */     updateStyleClasses(); }
/*     */   private final void setShowing(boolean value) { this.showing.set(value); }
/*     */   public final boolean isShowing() { return this.showing.get(); }
/*     */   public final BooleanProperty showFromTopProperty() { return this.showFromTop; }
/*     */   public final void setShowFromTop(boolean value) { this.showFromTop.set(value); }
/*     */   public final boolean isShowFromTop() { return this.showFromTop.get(); }
/*     */   public final ObjectProperty<EventHandler<Event>> onShowingProperty() { return this.onShowing; }
/*     */   public final void setOnShowing(EventHandler<Event> value) { onShowingProperty().set(value); }
/* 506 */   public final EventHandler<Event> getOnShowing() { return (EventHandler<Event>)onShowingProperty().get(); } public final ObservableList<Action> getActions() { return this.actions; } public final ObjectProperty<EventHandler<Event>> onShownProperty() {
/*     */     return this.onShown;
/*     */   } public final void setOnShown(EventHandler<Event> value) {
/*     */     onShownProperty().set(value);
/*     */   } public final EventHandler<Event> getOnShown() {
/*     */     return (EventHandler<Event>)onShownProperty().get();
/*     */   } public final ObjectProperty<EventHandler<Event>> onHidingProperty() {
/*     */     return this.onHiding;
/*     */   } public void show() {
/* 515 */     setShowing(true); } public final void setOnHiding(EventHandler<Event> value) { onHidingProperty().set(value); } public final EventHandler<Event> getOnHiding() { return (EventHandler<Event>)onHidingProperty().get(); } public final ObjectProperty<EventHandler<Event>> onHiddenProperty() { return this.onHidden; } public final void setOnHidden(EventHandler<Event> value) { onHiddenProperty().set(value); }
/*     */   public final EventHandler<Event> getOnHidden() { return (EventHandler<Event>)onHiddenProperty().get(); }
/*     */   public final BooleanProperty closeButtonVisibleProperty() {
/*     */     return this.closeButtonVisible;
/*     */   }
/*     */   public final void setCloseButtonVisible(boolean value) {
/*     */     this.closeButtonVisible.set(value);
/*     */   }
/*     */   public final boolean isCloseButtonVisible() {
/*     */     return this.closeButtonVisible.get();
/*     */   }
/*     */   public void show(final String text) {
/* 527 */     hideAndThen(new Runnable() {
/*     */           public void run() {
/* 529 */             NotificationPane.this.setText(text);
/* 530 */             NotificationPane.this.setShowing(true);
/*     */           }
/*     */         });
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
/*     */   public void show(final String text, final Node graphic) {
/* 545 */     hideAndThen(new Runnable() {
/*     */           public void run() {
/* 547 */             NotificationPane.this.setText(text);
/* 548 */             NotificationPane.this.setGraphic(graphic);
/* 549 */             NotificationPane.this.setShowing(true);
/*     */           }
/*     */         });
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
/*     */   public void show(final String text, final Node graphic, Action... actions) {
/* 565 */     hideAndThen(new Runnable() {
/*     */           public void run() {
/* 567 */             NotificationPane.this.setText(text);
/* 568 */             NotificationPane.this.setGraphic(graphic);
/*     */             
/* 570 */             if (actions == null) {
/* 571 */               NotificationPane.this.getActions().clear();
/*     */             } else {
/* 573 */               for (Action action : actions) {
/* 574 */                 if (action != null) {
/* 575 */                   NotificationPane.this.getActions().add(action);
/*     */                 }
/*     */               } 
/*     */             } 
/* 579 */             NotificationPane.this.setShowing(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void hide() {
/* 590 */     setShowing(false);
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
/*     */   private void updateStyleClasses() {
/* 602 */     getStyleClass().removeAll((Object[])new String[] { "top", "bottom" });
/* 603 */     getStyleClass().add(isShowFromTop() ? "top" : "bottom");
/*     */   }
/*     */   
/*     */   private void hideAndThen(final Runnable r) {
/* 607 */     if (isShowing()) {
/* 608 */       EventHandler<Event> eventHandler = new EventHandler<Event>() {
/*     */           public void handle(Event e) {
/* 610 */             r.run();
/* 611 */             NotificationPane.this.removeEventHandler(NotificationPane.ON_HIDDEN, this);
/*     */           }
/*     */         };
/* 614 */       addEventHandler(ON_HIDDEN, eventHandler);
/* 615 */       hide();
/*     */     } else {
/* 617 */       r.run();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\NotificationPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */