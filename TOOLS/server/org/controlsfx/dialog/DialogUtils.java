/*    */ package org.controlsfx.dialog;
/*    */ 
/*    */ import javafx.scene.control.ButtonBar;
/*    */ import javafx.scene.control.ButtonType;
/*    */ import javafx.scene.control.Dialog;
/*    */ import javafx.scene.control.DialogPane;
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
/*    */ class DialogUtils
/*    */ {
/*    */   static void forcefullyHideDialog(Dialog<?> dialog) {
/* 41 */     DialogPane dialogPane = dialog.getDialogPane();
/* 42 */     if (containsCancelButton(dialog)) {
/* 43 */       dialog.hide();
/*    */       
/*    */       return;
/*    */     } 
/* 47 */     dialogPane.getButtonTypes().add(ButtonType.CANCEL);
/* 48 */     dialog.hide();
/* 49 */     dialogPane.getButtonTypes().remove(ButtonType.CANCEL);
/*    */   }
/*    */   
/*    */   static boolean containsCancelButton(Dialog<?> dialog) {
/* 53 */     DialogPane dialogPane = dialog.getDialogPane();
/* 54 */     for (ButtonType type : dialogPane.getButtonTypes()) {
/* 55 */       if (type.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\DialogUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */