/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.MasterDetailPaneSkin;
/*     */ import java.util.Objects;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MasterDetailPane
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private final DoubleProperty dividerSizeHint;
/*     */   private final ObjectProperty<Side> detailSide;
/*     */   private final BooleanProperty showDetailNode;
/*     */   private final ObjectProperty<Node> masterNode;
/*     */   private final ObjectProperty<Node> detailNode;
/*     */   private final BooleanProperty animated;
/*     */   private DoubleProperty dividerPosition;
/*     */   
/*     */   public MasterDetailPane(Side pos, boolean showDetail) {
/*     */     this(pos, (Node)new Placeholder(true), (Node)new Placeholder(false), showDetail);
/*     */   }
/*     */   
/*     */   public MasterDetailPane(Side pos) {
/*     */     this(pos, (Node)new Placeholder(true), (Node)new Placeholder(false), true);
/*     */   }
/*     */   
/*     */   public MasterDetailPane() {
/*     */     this(Side.RIGHT, (Node)new Placeholder(true), (Node)new Placeholder(false), true);
/*     */   }
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new MasterDetailPaneSkin(this);
/*     */   }
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(MasterDetailPane.class, "masterdetailpane.css");
/*     */   }
/*     */   
/*     */   public final void resetDividerPosition() {
/*     */     double ps;
/*     */     Node node = getDetailNode();
/*     */     if (node == null)
/*     */       return; 
/*     */     boolean wasShowing = isShowDetailNode();
/*     */     boolean wasAnimated = isAnimated();
/*     */     if (!wasShowing) {
/*     */       setAnimated(false);
/*     */       setShowDetailNode(true);
/*     */       node.applyCss();
/*     */     } 
/*     */     double dividerSize = getDividerSizeHint();
/*     */     switch (getDetailSide()) {
/*     */       case RIGHT:
/*     */       case LEFT:
/*     */         ps = node.prefWidth(-1.0D) + dividerSize;
/*     */         break;
/*     */       default:
/*     */         ps = node.prefHeight(-1.0D) + dividerSize;
/*     */         break;
/*     */     } 
/*     */     double position = 0.0D;
/*     */     switch (getDetailSide()) {
/*     */       case LEFT:
/*     */         position = ps / getWidth();
/*     */         break;
/*     */       case RIGHT:
/*     */         position = 1.0D - ps / getWidth();
/*     */         break;
/*     */       case TOP:
/*     */         position = ps / getHeight();
/*     */         break;
/*     */       case BOTTOM:
/*     */         position = 1.0D - ps / getHeight();
/*     */         break;
/*     */     } 
/*     */     setDividerPosition(Math.min(1.0D, Math.max(0.0D, position)));
/*     */     if (!wasShowing) {
/*     */       setShowDetailNode(wasShowing);
/*     */       setAnimated(wasAnimated);
/*     */     } 
/*     */   }
/*     */   
/*     */   public final DoubleProperty dividerSizeHintProperty() {
/*     */     return this.dividerSizeHint;
/*     */   }
/*     */   
/*     */   public final void setDividerSizeHint(double size) {
/*     */     this.dividerSizeHint.set(size);
/*     */   }
/*     */   
/*     */   public final double getDividerSizeHint() {
/*     */     return this.dividerSizeHint.get();
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Side> detailSideProperty() {
/*     */     return this.detailSide;
/*     */   }
/*     */   
/*     */   public final Side getDetailSide() {
/*     */     return (Side)detailSideProperty().get();
/*     */   }
/*     */   
/*     */   public MasterDetailPane(Side side, Node masterNode, Node detailNode, boolean showDetail) {
/* 232 */     this.dividerSizeHint = (DoubleProperty)new SimpleDoubleProperty(this, "dividerSizeHint", 10.0D)
/*     */       {
/*     */         public void set(double newValue) {
/* 235 */           super.set(Math.max(0.0D, newValue));
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
/* 270 */     this.detailSide = (ObjectProperty<Side>)new SimpleObjectProperty(this, "detailSide", Side.RIGHT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 305 */     this.showDetailNode = (BooleanProperty)new SimpleBooleanProperty(this, "showDetailNode", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     this.masterNode = (ObjectProperty<Node>)new SimpleObjectProperty(this, "masterNode");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 370 */     this.detailNode = (ObjectProperty<Node>)new SimpleObjectProperty(this, "detailNode");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 402 */     this.animated = (BooleanProperty)new SimpleBooleanProperty(this, "animated", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 435 */     this.dividerPosition = (DoubleProperty)new SimpleDoubleProperty(this, "dividerPosition", 0.33D); Objects.requireNonNull(side); Objects.requireNonNull(masterNode); Objects.requireNonNull(detailNode); getStyleClass().add("master-detail-pane"); setDetailSide(side); setMasterNode(masterNode); setDetailNode(detailNode); setShowDetailNode(showDetail); switch (side) {
/*     */       case BOTTOM:
/*     */       case RIGHT:
/*     */         setDividerPosition(0.8D); break;
/*     */       case TOP:
/*     */       case LEFT:
/*     */         setDividerPosition(0.2D); break;
/*     */     } 
/*     */   } public final void setDetailSide(Side side) { Objects.requireNonNull(side);
/* 444 */     detailSideProperty().set(side); } public final DoubleProperty dividerPositionProperty() { return this.dividerPosition; }
/*     */   
/*     */   public final BooleanProperty showDetailNodeProperty() {
/*     */     return this.showDetailNode;
/*     */   } public final boolean isShowDetailNode() {
/*     */     return showDetailNodeProperty().get();
/*     */   } public final void setShowDetailNode(boolean show) {
/*     */     showDetailNodeProperty().set(show);
/*     */   }
/* 453 */   public final double getDividerPosition() { return this.dividerPosition.get(); }
/*     */   public final ObjectProperty<Node> masterNodeProperty() { return this.masterNode; }
/*     */   public final Node getMasterNode() { return (Node)masterNodeProperty().get(); }
/*     */   public final void setMasterNode(Node node) { Objects.requireNonNull(node);
/*     */     masterNodeProperty().set(node); }
/*     */   public final ObjectProperty<Node> detailNodeProperty() { return this.detailNode; }
/*     */   public final Node getDetailNode() { return (Node)detailNodeProperty().get(); }
/*     */   public final void setDetailNode(Node node) { detailNodeProperty().set(node); } public final BooleanProperty animatedProperty() {
/*     */     return this.animated;
/*     */   } public final boolean isAnimated() {
/*     */     return animatedProperty().get();
/*     */   } public final void setAnimated(boolean animated) {
/*     */     animatedProperty().set(animated);
/*     */   } public final void setDividerPosition(double position) {
/* 467 */     if (getDividerPosition() == position) {
/* 468 */       this.dividerPosition.set(-1.0D);
/*     */     }
/* 470 */     this.dividerPosition.set(position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Placeholder
/*     */     extends Label
/*     */   {
/*     */     public Placeholder(boolean master) {
/* 480 */       super(master ? "Master" : "Detail");
/*     */       
/* 482 */       setAlignment(Pos.CENTER);
/*     */       
/* 484 */       if (master) {
/* 485 */         setStyle("-fx-background-color: -fx-background;");
/*     */       } else {
/* 487 */         setStyle("-fx-background-color: derive(-fx-background, -10%);");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\MasterDetailPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */