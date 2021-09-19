/*    */ package org.seamless.swing;
/*    */ 
/*    */ import java.awt.event.KeyEvent;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.DefaultButtonModel;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPopupMenu;
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
/*    */ public class JPopupMenuButton
/*    */   extends JButton
/*    */ {
/*    */   public JPopupMenu popup;
/*    */   
/*    */   public JPopupMenuButton(JPopupMenu m) {
/* 36 */     this.popup = m;
/* 37 */     enableEvents(8L);
/* 38 */     enableEvents(16L);
/*    */   }
/*    */   
/*    */   public JPopupMenuButton(String s, JPopupMenu m) {
/* 42 */     super(s);
/* 43 */     this.popup = m;
/* 44 */     enableEvents(8L);
/* 45 */     enableEvents(16L);
/*    */   }
/*    */   
/*    */   public JPopupMenuButton(String s, Icon icon, JPopupMenu popup) {
/* 49 */     super(s, icon);
/* 50 */     this.popup = popup;
/* 51 */     enableEvents(8L);
/* 52 */     enableEvents(16L);
/*    */     
/* 54 */     setModel(new DefaultButtonModel()
/*    */         {
/*    */           public void setPressed(boolean b) {}
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void processMouseEvent(MouseEvent e) {
/* 63 */     super.processMouseEvent(e);
/* 64 */     int id = e.getID();
/* 65 */     if (id == 501) {
/* 66 */       if (this.popup != null) {
/* 67 */         this.popup.show(this, 0, 0);
/*    */       }
/* 69 */     } else if (id == 502 && 
/* 70 */       this.popup != null) {
/* 71 */       this.popup.setVisible(false);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void processKeyEvent(KeyEvent e) {
/* 77 */     int id = e.getID();
/* 78 */     if (id == 401 || id == 400) {
/* 79 */       if (e.getKeyCode() == 10) {
/* 80 */         this.popup.show(this, 0, 10);
/*    */       }
/* 82 */       super.processKeyEvent(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\JPopupMenuButton.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */