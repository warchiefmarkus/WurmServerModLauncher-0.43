/*     */ package org.seamless.swing.logging;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.table.AbstractTableModel;
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
/*     */ public class LogTableModel
/*     */   extends AbstractTableModel
/*     */ {
/*     */   protected int maxAgeSeconds;
/*     */   protected boolean paused = false;
/*     */   protected List<LogMessage> messages;
/*     */   
/*     */   public int getMaxAgeSeconds() {
/*  36 */     return this.maxAgeSeconds;
/*     */   }
/*     */   
/*     */   public void setMaxAgeSeconds(int maxAgeSeconds) {
/*  40 */     this.maxAgeSeconds = maxAgeSeconds;
/*     */   }
/*     */   
/*     */   public boolean isPaused() {
/*  44 */     return this.paused;
/*     */   }
/*     */   
/*     */   public void setPaused(boolean paused) {
/*  48 */     this.paused = paused;
/*     */   }
/*     */   public LogTableModel(int maxAgeSeconds) {
/*  51 */     this.messages = new ArrayList<LogMessage>();
/*     */     this.maxAgeSeconds = maxAgeSeconds;
/*     */   } public synchronized void pushMessage(LogMessage message) {
/*  54 */     if (this.paused)
/*     */       return; 
/*  56 */     if (this.maxAgeSeconds != Integer.MAX_VALUE) {
/*     */       
/*  58 */       Iterator<LogMessage> it = this.messages.iterator();
/*  59 */       long currentTime = (new Date()).getTime();
/*  60 */       while (it.hasNext()) {
/*  61 */         LogMessage logMessage = it.next();
/*  62 */         long delta = (this.maxAgeSeconds * 1000);
/*  63 */         if (logMessage.getCreatedOn().longValue() + delta < currentTime) {
/*  64 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     this.messages.add(message);
/*  70 */     fireTableDataChanged();
/*     */   }
/*     */   
/*     */   public Object getValueAt(int row, int column) {
/*  74 */     return this.messages.get(row);
/*     */   }
/*     */   
/*     */   public void clearMessages() {
/*  78 */     this.messages.clear();
/*  79 */     fireTableDataChanged();
/*     */   }
/*     */   
/*     */   public int getRowCount() {
/*  83 */     return this.messages.size();
/*     */   }
/*     */   
/*     */   public int getColumnCount() {
/*  87 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getColumnClass(int i) {
/*  92 */     return LogMessage.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnName(int column) {
/*  97 */     switch (column) {
/*     */       case 0:
/*  99 */         return "";
/*     */       case 1:
/* 101 */         return "Time";
/*     */       case 2:
/* 103 */         return "Thread";
/*     */       case 3:
/* 105 */         return "Source";
/*     */     } 
/* 107 */     return "Message";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LogTableModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */