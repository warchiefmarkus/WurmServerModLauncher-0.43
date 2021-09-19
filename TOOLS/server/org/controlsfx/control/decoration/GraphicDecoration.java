/*     */ package org.controlsfx.control.decoration;
/*     */ 
/*     */ import impl.org.controlsfx.ImplUtils;
/*     */ import java.util.List;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.geometry.Bounds;
/*     */ import javafx.geometry.HPos;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.VPos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GraphicDecoration
/*     */   extends Decoration
/*     */ {
/*     */   private final Node decorationNode;
/*     */   private final Pos pos;
/*     */   private final double xOffset;
/*     */   private final double yOffset;
/*     */   
/*     */   public GraphicDecoration(Node decorationNode) {
/*  67 */     this(decorationNode, Pos.TOP_LEFT);
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
/*     */   public GraphicDecoration(Node decorationNode, Pos position) {
/*  81 */     this(decorationNode, position, 0.0D, 0.0D);
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
/*     */   public GraphicDecoration(Node decorationNode, Pos position, double xOffset, double yOffset) {
/* 100 */     this.decorationNode = decorationNode;
/* 101 */     this.decorationNode.setManaged(false);
/* 102 */     this.pos = position;
/* 103 */     this.xOffset = xOffset;
/* 104 */     this.yOffset = yOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node applyDecoration(Node targetNode) {
/* 109 */     List<Node> targetNodeChildren = ImplUtils.getChildren((Parent)targetNode, true);
/* 110 */     updateGraphicPosition(targetNode);
/* 111 */     if (!targetNodeChildren.contains(this.decorationNode)) {
/* 112 */       targetNodeChildren.add(this.decorationNode);
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeDecoration(Node targetNode) {
/* 119 */     List<Node> targetNodeChildren = ImplUtils.getChildren((Parent)targetNode, true);
/*     */     
/* 121 */     if (targetNodeChildren.contains(this.decorationNode)) {
/* 122 */       targetNodeChildren.remove(this.decorationNode);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateGraphicPosition(final Node targetNode) {
/* 127 */     double decorationNodeWidth = this.decorationNode.prefWidth(-1.0D);
/* 128 */     double decorationNodeHeight = this.decorationNode.prefHeight(-1.0D);
/*     */     
/* 130 */     Bounds targetBounds = targetNode.getLayoutBounds();
/* 131 */     double x = targetBounds.getMinX();
/* 132 */     double y = targetBounds.getMinY();
/*     */     
/* 134 */     double targetWidth = targetBounds.getWidth();
/* 135 */     if (targetWidth <= 0.0D) {
/* 136 */       targetWidth = targetNode.prefWidth(-1.0D);
/*     */     }
/*     */     
/* 139 */     double targetHeight = targetBounds.getHeight();
/* 140 */     if (targetHeight <= 0.0D) {
/* 141 */       targetHeight = targetNode.prefHeight(-1.0D);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (targetWidth <= 0.0D && targetHeight <= 0.0D) {
/* 151 */       targetNode.layoutBoundsProperty().addListener(new ChangeListener<Bounds>()
/*     */           {
/*     */             public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue)
/*     */             {
/* 155 */               targetNode.layoutBoundsProperty().removeListener(this);
/* 156 */               GraphicDecoration.this.updateGraphicPosition(targetNode);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/* 162 */     switch (this.pos.getHpos()) {
/*     */       case CENTER:
/* 164 */         x += targetWidth / 2.0D - decorationNodeWidth / 2.0D;
/*     */         break;
/*     */       case TOP:
/* 167 */         x -= decorationNodeWidth / 2.0D;
/*     */         break;
/*     */       case BOTTOM:
/* 170 */         x += targetWidth - decorationNodeWidth / 2.0D;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 175 */     switch (this.pos.getVpos()) {
/*     */       case CENTER:
/* 177 */         y += targetHeight / 2.0D - decorationNodeHeight / 2.0D;
/*     */         break;
/*     */       case TOP:
/* 180 */         y -= decorationNodeHeight / 2.0D;
/*     */         break;
/*     */       case BOTTOM:
/* 183 */         y += targetHeight - decorationNodeWidth / 2.0D;
/*     */         break;
/*     */       case BASELINE:
/* 186 */         y += targetNode.getBaselineOffset() - this.decorationNode.getBaselineOffset() - decorationNodeHeight / 2.0D;
/*     */         break;
/*     */     } 
/*     */     
/* 190 */     this.decorationNode.setLayoutX(x + this.xOffset);
/* 191 */     this.decorationNode.setLayoutY(y + this.yOffset);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\decoration\GraphicDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */