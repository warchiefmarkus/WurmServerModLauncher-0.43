/*    */ package org.seamless.swing;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
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
/*    */ public abstract class DefaultAction
/*    */   extends AbstractAction
/*    */ {
/*    */   public void executeInController(Controller controller, ActionEvent event) {
/* 28 */     actionPerformed(event);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\DefaultAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */