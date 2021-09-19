/*    */ package org.controlsfx.control.cell;
/*    */ 
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.paint.Color;
/*    */ import javafx.scene.paint.Paint;
/*    */ import javafx.scene.shape.Rectangle;
/*    */ import org.controlsfx.control.GridCell;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ColorGridCell
/*    */   extends GridCell<Color>
/*    */ {
/*    */   private Rectangle colorRect;
/*    */   private static final boolean debug = false;
/*    */   
/*    */   public ColorGridCell() {
/* 52 */     getStyleClass().add("color-grid-cell");
/*    */     
/* 54 */     this.colorRect = new Rectangle();
/* 55 */     this.colorRect.setStroke((Paint)Color.BLACK);
/* 56 */     this.colorRect.heightProperty().bind((ObservableValue)heightProperty());
/* 57 */     this.colorRect.widthProperty().bind((ObservableValue)widthProperty());
/* 58 */     setGraphic((Node)this.colorRect);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updateItem(Color item, boolean empty) {
/* 69 */     super.updateItem(item, empty);
/*    */     
/* 71 */     if (empty) {
/* 72 */       setGraphic(null);
/*    */     } else {
/* 74 */       this.colorRect.setFill((Paint)item);
/* 75 */       setGraphic((Node)this.colorRect);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\cell\ColorGridCell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */