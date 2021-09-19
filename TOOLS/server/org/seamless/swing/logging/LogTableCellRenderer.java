/*    */ package org.seamless.swing.logging;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.logging.Level;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableCellRenderer;
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
/*    */ public abstract class LogTableCellRenderer
/*    */   extends DefaultTableCellRenderer
/*    */ {
/* 32 */   protected SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*    */     Date date;
/* 39 */     LogMessage message = (LogMessage)value;
/*    */     
/* 41 */     switch (column) {
/*    */       case 0:
/* 43 */         if (message.getLevel().equals(Level.SEVERE) || message.getLevel().equals(Level.WARNING))
/*    */         {
/*    */           
/* 46 */           return new JLabel(getWarnErrorIcon());
/*    */         }
/* 48 */         if (message.getLevel().equals(Level.FINE))
/*    */         {
/* 50 */           return new JLabel(getDebugIcon());
/*    */         }
/* 52 */         if (message.getLevel().equals(Level.FINER) || message.getLevel().equals(Level.FINEST))
/*    */         {
/*    */           
/* 55 */           return new JLabel(getTraceIcon());
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 60 */         return new JLabel(getInfoIcon());
/*    */ 
/*    */ 
/*    */       
/*    */       case 1:
/* 65 */         date = new Date(message.getCreatedOn().longValue());
/* 66 */         return super.getTableCellRendererComponent(table, this.dateFormat.format(date), isSelected, hasFocus, row, column);
/*    */ 
/*    */       
/*    */       case 2:
/* 70 */         return super.getTableCellRendererComponent(table, message.getThread(), isSelected, hasFocus, row, column);
/*    */ 
/*    */       
/*    */       case 3:
/* 74 */         return super.getTableCellRendererComponent(table, message.getSource(), isSelected, hasFocus, row, column);
/*    */     } 
/*    */ 
/*    */     
/* 78 */     return super.getTableCellRendererComponent(table, message.getMessage().replaceAll("\n", "<NL>").replaceAll("\r", "<CR>"), isSelected, hasFocus, row, column);
/*    */   }
/*    */   
/*    */   protected abstract ImageIcon getWarnErrorIcon();
/*    */   
/*    */   protected abstract ImageIcon getInfoIcon();
/*    */   
/*    */   protected abstract ImageIcon getDebugIcon();
/*    */   
/*    */   protected abstract ImageIcon getTraceIcon();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LogTableCellRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */