/*     */ package org.controlsfx.control.cell;
/*     */ 
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.media.Media;
/*     */ import javafx.scene.media.MediaPlayer;
/*     */ import javafx.scene.media.MediaView;
/*     */ import org.controlsfx.control.GridCell;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaImageCell
/*     */   extends GridCell<Media>
/*     */ {
/*     */   private MediaPlayer mediaPlayer;
/*     */   private final MediaView mediaView;
/*     */   
/*     */   public MediaImageCell() {
/*  51 */     getStyleClass().add("media-grid-cell");
/*     */     
/*  53 */     this.mediaView = new MediaView();
/*  54 */     this.mediaView.setMediaPlayer(this.mediaPlayer);
/*  55 */     this.mediaView.fitHeightProperty().bind((ObservableValue)heightProperty());
/*  56 */     this.mediaView.fitWidthProperty().bind((ObservableValue)widthProperty());
/*  57 */     this.mediaView.setMediaPlayer(this.mediaPlayer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pause() {
/*  64 */     if (this.mediaPlayer != null) {
/*  65 */       this.mediaPlayer.pause();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void play() {
/*  73 */     if (this.mediaPlayer != null) {
/*  74 */       this.mediaPlayer.play();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  82 */     if (this.mediaPlayer != null) {
/*  83 */       this.mediaPlayer.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateItem(Media item, boolean empty) {
/*  91 */     super.updateItem(item, empty);
/*     */     
/*  93 */     getChildren().clear();
/*  94 */     if (this.mediaPlayer != null) {
/*  95 */       this.mediaPlayer.stop();
/*     */     }
/*     */     
/*  98 */     if (empty) {
/*  99 */       setGraphic(null);
/*     */     } else {
/* 101 */       this.mediaPlayer = new MediaPlayer(item);
/* 102 */       this.mediaView.setMediaPlayer(this.mediaPlayer);
/* 103 */       setGraphic((Node)this.mediaView);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\cell\MediaImageCell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */