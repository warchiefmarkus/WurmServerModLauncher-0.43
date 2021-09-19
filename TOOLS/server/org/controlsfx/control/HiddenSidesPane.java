/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.HiddenSidesPaneSkin;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.util.Duration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HiddenSidesPane
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private DoubleProperty triggerDistance;
/*     */   private ObjectProperty<Node> content;
/*     */   private ObjectProperty<Node> top;
/*     */   private ObjectProperty<Node> right;
/*     */   private ObjectProperty<Node> bottom;
/*     */   private ObjectProperty<Node> left;
/*     */   private ObjectProperty<Side> pinnedSide;
/*     */   private final ObjectProperty<Duration> animationDelay;
/*     */   private final ObjectProperty<Duration> animationDuration;
/*     */   
/*     */   public HiddenSidesPane() {
/*     */     this(null, null, null, null, null);
/*     */   }
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new HiddenSidesPaneSkin(this);
/*     */   }
/*     */   
/*     */   public final DoubleProperty triggerDistanceProperty() {
/*     */     return this.triggerDistance;
/*     */   }
/*     */   
/*     */   public final double getTriggerDistance() {
/*     */     return this.triggerDistance.get();
/*     */   }
/*     */   
/*     */   public final void setTriggerDistance(double distance) {
/*     */     this.triggerDistance.set(distance);
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Node> contentProperty() {
/*     */     return this.content;
/*     */   }
/*     */   
/*     */   public final Node getContent() {
/*     */     return (Node)contentProperty().get();
/*     */   }
/*     */   
/*     */   public final void setContent(Node content) {
/*     */     contentProperty().set(content);
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Node> topProperty() {
/*     */     return this.top;
/*     */   }
/*     */   
/*     */   public final Node getTop() {
/*     */     return (Node)topProperty().get();
/*     */   }
/*     */   
/*     */   public HiddenSidesPane(Node content, Node top, Node right, Node bottom, Node left) {
/* 101 */     this.triggerDistance = (DoubleProperty)new SimpleDoubleProperty(this, "triggerDistance", 16.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     this.content = (ObjectProperty<Node>)new SimpleObjectProperty(this, "content");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     this.top = (ObjectProperty<Node>)new SimpleObjectProperty(this, "top");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     this.right = (ObjectProperty<Node>)new SimpleObjectProperty(this, "right");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     this.bottom = (ObjectProperty<Node>)new SimpleObjectProperty(this, "bottom");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 293 */     this.left = (ObjectProperty<Node>)new SimpleObjectProperty(this, "left");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 326 */     this.pinnedSide = (ObjectProperty<Side>)new SimpleObjectProperty(this, "pinnedSide");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     this
/* 361 */       .animationDelay = (ObjectProperty<Duration>)new SimpleObjectProperty(this, "animationDelay", Duration.millis(300.0D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 395 */     this
/* 396 */       .animationDuration = (ObjectProperty<Duration>)new SimpleObjectProperty(this, "animationDuration", Duration.millis(200.0D));
/*     */     setContent(content);
/*     */     setTop(top);
/*     */     setRight(right);
/*     */     setBottom(bottom);
/*     */     setLeft(left);
/*     */   } public final void setTop(Node top) { topProperty().set(top); } public final ObjectProperty<Node> rightProperty() { return this.right; }
/*     */   public final Node getRight() { return (Node)rightProperty().get(); }
/*     */   public final ObjectProperty<Duration> animationDurationProperty() {
/* 405 */     return this.animationDuration;
/*     */   }
/*     */   public final void setRight(Node right) {
/*     */     rightProperty().set(right);
/*     */   } public final ObjectProperty<Node> bottomProperty() {
/*     */     return this.bottom;
/*     */   } public final Node getBottom() {
/*     */     return (Node)bottomProperty().get();
/*     */   }
/* 414 */   public final Duration getAnimationDuration() { return (Duration)this.animationDuration.get(); }
/*     */   public final void setBottom(Node bottom) { bottomProperty().set(bottom); }
/*     */   public final ObjectProperty<Node> leftProperty() { return this.left; }
/*     */   public final Node getLeft() { return (Node)leftProperty().get(); } public final void setLeft(Node left) {
/*     */     leftProperty().set(left);
/*     */   } public final ObjectProperty<Side> pinnedSideProperty() {
/*     */     return this.pinnedSide;
/*     */   } public final Side getPinnedSide() {
/*     */     return (Side)pinnedSideProperty().get();
/*     */   } public final void setAnimationDuration(Duration duration) {
/* 424 */     this.animationDuration.set(duration);
/*     */   }
/*     */   
/*     */   public final void setPinnedSide(Side side) {
/*     */     pinnedSideProperty().set(side);
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Duration> animationDelayProperty() {
/*     */     return this.animationDelay;
/*     */   }
/*     */   
/*     */   public final Duration getAnimationDelay() {
/*     */     return (Duration)this.animationDelay.get();
/*     */   }
/*     */   
/*     */   public final void setAnimationDelay(Duration duration) {
/*     */     this.animationDelay.set(duration);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\HiddenSidesPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */