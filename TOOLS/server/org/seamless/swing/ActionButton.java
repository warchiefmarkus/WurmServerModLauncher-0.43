/*    */ package org.seamless.swing;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.Action;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JButton;
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
/*    */ public class ActionButton
/*    */   extends JButton
/*    */ {
/*    */   public ActionButton(String actionCommand) {
/* 30 */     setActionCommand(actionCommand);
/*    */   }
/*    */   
/*    */   public ActionButton(Icon icon, String actionCommand) {
/* 34 */     super(icon);
/* 35 */     setActionCommand(actionCommand);
/*    */   }
/*    */   
/*    */   public ActionButton(String s, String actionCommand) {
/* 39 */     super(s);
/* 40 */     setActionCommand(actionCommand);
/*    */   }
/*    */   
/*    */   public ActionButton(Action action, String actionCommand) {
/* 44 */     super(action);
/* 45 */     setActionCommand(actionCommand);
/*    */   }
/*    */   
/*    */   public ActionButton(String s, Icon icon, String actionCommand) {
/* 49 */     super(s, icon);
/* 50 */     setActionCommand(actionCommand);
/*    */   }
/*    */   
/*    */   public ActionButton enableDefaultEvents(final Controller controller) {
/* 54 */     controller.registerAction(this, new DefaultAction()
/*    */         {
/*    */           public void actionPerformed(ActionEvent actionEvent) {
/*    */             Event e;
/* 58 */             if ((e = ActionButton.this.createDefaultEvent()) != null)
/* 59 */               controller.fireEvent(e); 
/* 60 */             if ((e = ActionButton.this.createDefaultGlobalEvent()) != null) {
/* 61 */               controller.fireEventGlobal(e);
/*    */             }
/*    */           }
/*    */         });
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public Event createDefaultEvent() {
/* 69 */     return null;
/*    */   }
/*    */   
/*    */   public Event createDefaultGlobalEvent() {
/* 73 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\ActionButton.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */