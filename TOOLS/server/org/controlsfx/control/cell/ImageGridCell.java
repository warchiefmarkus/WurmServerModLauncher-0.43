/*    */ package org.controlsfx.control.cell;
/*    */ 
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.image.Image;
/*    */ import javafx.scene.image.ImageView;
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
/*    */ 
/*    */ public class ImageGridCell
/*    */   extends GridCell<Image>
/*    */ {
/*    */   private final ImageView imageView;
/*    */   private final boolean preserveImageProperties;
/*    */   
/*    */   public ImageGridCell() {
/* 52 */     this(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImageGridCell(boolean preserveImageProperties) {
/* 60 */     getStyleClass().add("image-grid-cell");
/*    */     
/* 62 */     this.preserveImageProperties = preserveImageProperties;
/* 63 */     this.imageView = new ImageView();
/* 64 */     this.imageView.fitHeightProperty().bind((ObservableValue)heightProperty());
/* 65 */     this.imageView.fitWidthProperty().bind((ObservableValue)widthProperty());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updateItem(Image item, boolean empty) {
/* 72 */     super.updateItem(item, empty);
/*    */     
/* 74 */     if (empty) {
/* 75 */       setGraphic(null);
/*    */     } else {
/* 77 */       if (this.preserveImageProperties) {
/* 78 */         this.imageView.setPreserveRatio(item.isPreserveRatio());
/* 79 */         this.imageView.setSmooth(item.isSmooth());
/*    */       } 
/* 81 */       this.imageView.setImage(item);
/* 82 */       setGraphic((Node)this.imageView);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\cell\ImageGridCell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */