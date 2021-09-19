/*    */ package org.controlsfx.dialog;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WizardPane
/*    */   extends DialogPane
/*    */ {
/*    */   public WizardPane() {
/* 43 */     getStylesheets().add(Wizard.class.getResource("wizard.css").toExternalForm());
/* 44 */     getStyleClass().add("wizard-pane");
/*    */   }
/*    */   
/*    */   public void onEnteringPage(Wizard wizard) {}
/*    */   
/*    */   public void onExitingPage(Wizard wizard) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\WizardPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */