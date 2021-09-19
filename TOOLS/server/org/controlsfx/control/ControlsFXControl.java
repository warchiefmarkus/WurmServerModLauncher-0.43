/*    */ package org.controlsfx.control;
/*    */ 
/*    */ import impl.org.controlsfx.version.VersionChecker;
/*    */ import javafx.scene.control.Control;
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
/*    */ abstract class ControlsFXControl
/*    */   extends Control
/*    */ {
/*    */   private String stylesheet;
/*    */   
/*    */   public ControlsFXControl() {
/* 35 */     VersionChecker.doVersionCheck();
/*    */   }
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
/*    */   protected final String getUserAgentStylesheet(Class<?> clazz, String fileName) {
/* 57 */     if (this.stylesheet == null) {
/* 58 */       this.stylesheet = clazz.getResource(fileName).toExternalForm();
/*    */     }
/*    */     
/* 61 */     return this.stylesheet;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\ControlsFXControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */