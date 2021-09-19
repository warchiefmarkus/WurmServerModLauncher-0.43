/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import com.sun.javafx.event.EventHandlerManager;
/*     */ import impl.org.controlsfx.skin.HyperlinkLabelSkin;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HyperlinkLabel
/*     */   extends ControlsFXControl
/*     */   implements EventTarget
/*     */ {
/*  99 */   private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final StringProperty text;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectProperty<EventHandler<ActionEvent>> onAction;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HyperlinkLabel() {
/* 115 */     this(null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/* 140 */     return (Skin<?>)new HyperlinkLabelSkin(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public HyperlinkLabel(String text) {
/* 145 */     this.text = (StringProperty)new SimpleStringProperty(this, "text");
/*     */     setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringProperty textProperty() {
/* 152 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getText() {
/* 160 */     return (String)this.text.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setText(String value) {
/* 169 */     this.text.set(value);
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
/*     */   public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
/* 183 */     if (this.onAction == null) {
/* 184 */       this.onAction = (ObjectProperty<EventHandler<ActionEvent>>)new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "onAction") {
/*     */           protected void invalidated() {
/* 186 */             HyperlinkLabel.this.eventHandlerManager.setEventHandler(ActionEvent.ACTION, (EventHandler)get());
/*     */           }
/*     */         };
/*     */     }
/* 190 */     return this.onAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setOnAction(EventHandler<ActionEvent> value) {
/* 199 */     onActionProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final EventHandler<ActionEvent> getOnAction() {
/* 207 */     return (this.onAction == null) ? null : (EventHandler<ActionEvent>)this.onAction.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\HyperlinkLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */