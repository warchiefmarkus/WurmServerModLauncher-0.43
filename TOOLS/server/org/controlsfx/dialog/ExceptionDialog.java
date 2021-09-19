/*    */ package org.controlsfx.dialog;
/*    */ 
/*    */ import impl.org.controlsfx.i18n.Localization;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.control.ButtonType;
/*    */ import javafx.scene.control.Dialog;
/*    */ import javafx.scene.control.DialogPane;
/*    */ import javafx.scene.control.Label;
/*    */ import javafx.scene.control.TextArea;
/*    */ import javafx.scene.layout.GridPane;
/*    */ import javafx.scene.layout.Priority;
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
/*    */ public class ExceptionDialog
/*    */   extends Dialog<ButtonType>
/*    */ {
/*    */   public ExceptionDialog(Throwable exception) {
/* 46 */     DialogPane dialogPane = getDialogPane();
/*    */     
/* 48 */     setTitle(Localization.getString("exception.dlg.title"));
/* 49 */     dialogPane.setHeaderText(Localization.getString("exception.dlg.header"));
/* 50 */     dialogPane.getStyleClass().add("exception-dialog");
/* 51 */     dialogPane.getStylesheets().add(ProgressDialog.class.getResource("dialogs.css").toExternalForm());
/* 52 */     dialogPane.getButtonTypes().addAll((Object[])new ButtonType[] { ButtonType.OK });
/*    */ 
/*    */     
/* 55 */     String contentText = getContentText();
/* 56 */     dialogPane.setContent((Node)new Label((contentText != null && !contentText.isEmpty()) ? contentText : exception
/* 57 */           .getMessage()));
/*    */ 
/*    */     
/* 60 */     StringWriter sw = new StringWriter();
/* 61 */     PrintWriter pw = new PrintWriter(sw);
/* 62 */     exception.printStackTrace(pw);
/* 63 */     String exceptionText = sw.toString();
/*    */     
/* 65 */     Label label = new Label(Localization.localize(Localization.getString("exception.dlg.label")));
/*    */     
/* 67 */     TextArea textArea = new TextArea(exceptionText);
/* 68 */     textArea.setEditable(false);
/* 69 */     textArea.setWrapText(true);
/*    */     
/* 71 */     textArea.setMaxWidth(Double.MAX_VALUE);
/* 72 */     textArea.setMaxHeight(Double.MAX_VALUE);
/* 73 */     GridPane.setVgrow((Node)textArea, Priority.ALWAYS);
/* 74 */     GridPane.setHgrow((Node)textArea, Priority.ALWAYS);
/*    */     
/* 76 */     GridPane root = new GridPane();
/* 77 */     root.setMaxWidth(Double.MAX_VALUE);
/* 78 */     root.add((Node)label, 0, 0);
/* 79 */     root.add((Node)textArea, 0, 1);
/*    */ 
/*    */     
/* 82 */     dialogPane.setExpandableContent((Node)root);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\ExceptionDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */