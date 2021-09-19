/*     */ package org.controlsfx.dialog;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.concurrent.Worker;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ButtonType;
/*     */ import javafx.scene.control.Dialog;
/*     */ import javafx.scene.control.DialogPane;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ProgressBar;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.scene.layout.VBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProgressDialog
/*     */   extends Dialog<Void>
/*     */ {
/*     */   public ProgressDialog(Worker<?> worker) {
/*  47 */     if (worker != null && (worker
/*  48 */       .getState() == Worker.State.CANCELLED || worker
/*  49 */       .getState() == Worker.State.FAILED || worker
/*  50 */       .getState() == Worker.State.SUCCEEDED)) {
/*     */       return;
/*     */     }
/*  53 */     setResultConverter(dialogButton -> null);
/*     */     
/*  55 */     DialogPane dialogPane = getDialogPane();
/*     */     
/*  57 */     setTitle(Localization.getString("progress.dlg.title"));
/*  58 */     dialogPane.setHeaderText(Localization.getString("progress.dlg.header"));
/*  59 */     dialogPane.getStyleClass().add("progress-dialog");
/*  60 */     dialogPane.getStylesheets().add(ProgressDialog.class.getResource("dialogs.css").toExternalForm());
/*     */     
/*  62 */     Label progressMessage = new Label();
/*  63 */     progressMessage.textProperty().bind((ObservableValue)worker.messageProperty());
/*     */     
/*  65 */     WorkerProgressPane content = new WorkerProgressPane(this);
/*  66 */     content.setMaxWidth(Double.MAX_VALUE);
/*  67 */     content.setWorker(worker);
/*     */     
/*  69 */     VBox vbox = new VBox(10.0D, new Node[] { (Node)progressMessage, (Node)content });
/*  70 */     vbox.setMaxWidth(Double.MAX_VALUE);
/*  71 */     vbox.setPrefSize(300.0D, 100.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     Label contentText = new Label();
/*  78 */     contentText.setWrapText(true);
/*  79 */     vbox.getChildren().add(0, contentText);
/*  80 */     contentText.textProperty().bind((ObservableValue)dialogPane.contentTextProperty());
/*  81 */     dialogPane.setContent((Node)vbox);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WorkerProgressPane
/*     */     extends Region
/*     */   {
/*     */     private Worker<?> worker;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean dialogVisible = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean cancelDialogShow = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     private ChangeListener<Worker.State> stateListener = new ChangeListener<Worker.State>() {
/*     */         public void changed(ObservableValue<? extends Worker.State> observable, Worker.State old, Worker.State value) {
/* 109 */           switch (value) {
/*     */             case CANCELLED:
/*     */             case FAILED:
/*     */             case SUCCEEDED:
/* 113 */               if (!ProgressDialog.WorkerProgressPane.this.dialogVisible) {
/* 114 */                 ProgressDialog.WorkerProgressPane.this.cancelDialogShow = true;
/* 115 */                 ProgressDialog.WorkerProgressPane.this.end(); break;
/* 116 */               }  if (old == Worker.State.SCHEDULED || old == Worker.State.RUNNING) {
/* 117 */                 ProgressDialog.WorkerProgressPane.this.end();
/*     */               }
/*     */               break;
/*     */             case SCHEDULED:
/* 121 */               ProgressDialog.WorkerProgressPane.this.begin();
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/*     */     private final ProgressDialog dialog; private final ProgressBar progressBar;
/*     */     
/*     */     public final void setWorker(Worker<?> newWorker) {
/* 129 */       if (newWorker != this.worker) {
/* 130 */         if (this.worker != null) {
/* 131 */           this.worker.stateProperty().removeListener(this.stateListener);
/* 132 */           end();
/*     */         } 
/*     */         
/* 135 */         this.worker = newWorker;
/*     */         
/* 137 */         if (newWorker != null) {
/* 138 */           newWorker.stateProperty().addListener(this.stateListener);
/* 139 */           if (newWorker.getState() == Worker.State.RUNNING || newWorker.getState() == Worker.State.SCHEDULED)
/*     */           {
/* 141 */             begin();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WorkerProgressPane(ProgressDialog dialog) {
/* 154 */       this.dialog = dialog;
/*     */       
/* 156 */       this.progressBar = new ProgressBar();
/* 157 */       this.progressBar.setMaxWidth(Double.MAX_VALUE);
/* 158 */       getChildren().add(this.progressBar);
/*     */       
/* 160 */       if (this.worker != null) {
/* 161 */         this.progressBar.progressProperty().bind((ObservableValue)this.worker.progressProperty());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void begin() {
/* 184 */       this.cancelDialogShow = false;
/*     */       
/* 186 */       Platform.runLater(() -> {
/*     */             if (!this.cancelDialogShow) {
/*     */               this.progressBar.progressProperty().bind((ObservableValue)this.worker.progressProperty());
/*     */               this.dialogVisible = true;
/*     */               this.dialog.show();
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/*     */     private void end() {
/* 196 */       this.progressBar.progressProperty().unbind();
/* 197 */       this.dialogVisible = false;
/* 198 */       DialogUtils.forcefullyHideDialog(this.dialog);
/*     */     }
/*     */     
/*     */     protected void layoutChildren() {
/* 202 */       if (this.progressBar != null) {
/* 203 */         Insets insets = getInsets();
/* 204 */         double w = getWidth() - insets.getLeft() - insets.getRight();
/* 205 */         double h = getHeight() - insets.getTop() - insets.getBottom();
/*     */         
/* 207 */         double prefH = this.progressBar.prefHeight(-1.0D);
/* 208 */         double x = insets.getLeft() + (w - w) / 2.0D;
/* 209 */         double y = insets.getTop() + (h - prefH) / 2.0D;
/*     */         
/* 211 */         this.progressBar.resizeRelocate(x, y, w, prefH);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\ProgressDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */