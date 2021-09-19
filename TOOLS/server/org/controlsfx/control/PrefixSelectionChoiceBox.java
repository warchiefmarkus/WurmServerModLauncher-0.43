/*    */ package org.controlsfx.control;
/*    */ 
/*    */ import impl.org.controlsfx.tools.PrefixSelectionCustomizer;
/*    */ import javafx.scene.control.ChoiceBox;
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
/*    */ public class PrefixSelectionChoiceBox<T>
/*    */   extends ChoiceBox<T>
/*    */ {
/*    */   public PrefixSelectionChoiceBox() {
/* 74 */     PrefixSelectionCustomizer.customize(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\PrefixSelectionChoiceBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */