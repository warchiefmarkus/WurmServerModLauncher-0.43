/*     */ package org.fourthline.cling.support.shared.log.impl;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.enterprise.event.Event;
/*     */ import javax.inject.Inject;
/*     */ import javax.inject.Singleton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableModel;
/*     */ import org.fourthline.cling.support.shared.CenterWindow;
/*     */ import org.fourthline.cling.support.shared.log.LogView;
/*     */ import org.seamless.swing.Application;
/*     */ import org.seamless.swing.logging.LogCategorySelector;
/*     */ import org.seamless.swing.logging.LogController;
/*     */ import org.seamless.swing.logging.LogMessage;
/*     */ import org.seamless.swing.logging.LogTableCellRenderer;
/*     */ import org.seamless.swing.logging.LogTableModel;
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
/*     */ @Singleton
/*     */ public class LogViewImpl
/*     */   extends JPanel
/*     */   implements LogView
/*     */ {
/*     */   @Inject
/*     */   protected LogView.LogCategories logCategories;
/*     */   @Inject
/*     */   protected Event<CenterWindow> centerWindowEvent;
/*     */   protected LogCategorySelector logCategorySelector;
/*     */   protected JTable logTable;
/*     */   protected LogTableModel logTableModel;
/*  67 */   protected final JToolBar toolBar = new JToolBar();
/*     */   
/*  69 */   protected final JButton configureButton = new JButton("Options...", 
/*  70 */       Application.createImageIcon(LogController.class, "img/configure.png"));
/*     */   
/*  72 */   protected final JButton clearButton = new JButton("Clear Log", 
/*  73 */       Application.createImageIcon(LogController.class, "img/removetext.png"));
/*     */   
/*  75 */   protected final JButton copyButton = new JButton("Copy", 
/*  76 */       Application.createImageIcon(LogController.class, "img/copyclipboard.png"));
/*     */   
/*  78 */   protected final JButton expandButton = new JButton("Expand", 
/*  79 */       Application.createImageIcon(LogController.class, "img/viewtext.png"));
/*     */   
/*  81 */   protected final JButton pauseButton = new JButton("Pause/Continue Log", 
/*  82 */       Application.createImageIcon(LogController.class, "img/pause.png"));
/*     */   
/*  84 */   protected final JLabel pauseLabel = new JLabel(" (Active)");
/*     */   
/*  86 */   protected final JComboBox expirationComboBox = new JComboBox<>(LogController.Expiration.values());
/*     */   
/*     */   protected LogView.Presenter presenter;
/*     */   
/*     */   @PostConstruct
/*     */   public void init() {
/*  92 */     setLayout(new BorderLayout());
/*     */     
/*  94 */     LogController.Expiration defaultExpiration = getDefaultExpiration();
/*     */     
/*  96 */     this.logCategorySelector = new LogCategorySelector((List)this.logCategories);
/*     */     
/*  98 */     this.logTableModel = new LogTableModel(defaultExpiration.getSeconds());
/*  99 */     this.logTable = new JTable((TableModel)this.logTableModel);
/*     */     
/* 101 */     this.logTable.setDefaultRenderer(LogMessage.class, (TableCellRenderer)new LogTableCellRenderer()
/*     */         {
/*     */           
/*     */           protected ImageIcon getWarnErrorIcon()
/*     */           {
/* 106 */             return LogViewImpl.this.getWarnErrorIcon();
/*     */           }
/*     */           
/*     */           protected ImageIcon getDebugIcon() {
/* 110 */             return LogViewImpl.this.getDebugIcon();
/*     */           }
/*     */           
/*     */           protected ImageIcon getTraceIcon() {
/* 114 */             return LogViewImpl.this.getTraceIcon();
/*     */           }
/*     */           
/*     */           protected ImageIcon getInfoIcon() {
/* 118 */             return LogViewImpl.this.getInfoIcon();
/*     */           }
/*     */         });
/*     */     
/* 122 */     this.logTable.setCellSelectionEnabled(false);
/* 123 */     this.logTable.setRowSelectionAllowed(true);
/* 124 */     this.logTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
/*     */         {
/*     */           public void valueChanged(ListSelectionEvent e)
/*     */           {
/* 128 */             if (e.getValueIsAdjusting())
/*     */               return; 
/* 130 */             if (e.getSource() == LogViewImpl.this.logTable.getSelectionModel()) {
/* 131 */               int[] rows = LogViewImpl.this.logTable.getSelectedRows();
/*     */               
/* 133 */               if (rows == null || rows.length == 0) {
/* 134 */                 LogViewImpl.this.copyButton.setEnabled(false);
/* 135 */                 LogViewImpl.this.expandButton.setEnabled(false);
/* 136 */               } else if (rows.length == 1) {
/* 137 */                 LogViewImpl.this.copyButton.setEnabled(true);
/* 138 */                 LogMessage msg = (LogMessage)LogViewImpl.this.logTableModel.getValueAt(rows[0], 0);
/*     */                 
/* 140 */                 if (msg.getMessage().length() > LogViewImpl.this.getExpandMessageCharacterLimit()) {
/* 141 */                   LogViewImpl.this.expandButton.setEnabled(true);
/*     */                 } else {
/* 143 */                   LogViewImpl.this.expandButton.setEnabled(false);
/*     */                 } 
/*     */               } else {
/* 146 */                 LogViewImpl.this.copyButton.setEnabled(true);
/* 147 */                 LogViewImpl.this.expandButton.setEnabled(false);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 154 */     adjustTableUI();
/* 155 */     initializeToolBar(defaultExpiration);
/*     */     
/* 157 */     setPreferredSize(new Dimension(250, 100));
/* 158 */     setMinimumSize(new Dimension(250, 50));
/* 159 */     add(new JScrollPane(this.logTable), "Center");
/* 160 */     add(this.toolBar, "South");
/*     */   }
/*     */ 
/*     */   
/*     */   public Component asUIComponent() {
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPresenter(LogView.Presenter presenter) {
/* 170 */     this.presenter = presenter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushMessage(LogMessage logMessage) {
/* 175 */     this.logTableModel.pushMessage(logMessage);
/*     */ 
/*     */     
/* 178 */     if (!this.logTableModel.isPaused()) {
/* 179 */       this.logTable.scrollRectToVisible(this.logTable
/* 180 */           .getCellRect(this.logTableModel.getRowCount() - 1, 0, true));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 187 */     this.logCategorySelector.dispose();
/*     */   }
/*     */   
/*     */   protected void adjustTableUI() {
/* 191 */     this.logTable.setFocusable(false);
/* 192 */     this.logTable.setRowHeight(18);
/* 193 */     this.logTable.getTableHeader().setReorderingAllowed(false);
/* 194 */     this.logTable.setBorder(BorderFactory.createEmptyBorder());
/*     */     
/* 196 */     this.logTable.getColumnModel().getColumn(0).setMinWidth(30);
/* 197 */     this.logTable.getColumnModel().getColumn(0).setMaxWidth(30);
/* 198 */     this.logTable.getColumnModel().getColumn(0).setResizable(false);
/*     */ 
/*     */     
/* 201 */     this.logTable.getColumnModel().getColumn(1).setMinWidth(90);
/* 202 */     this.logTable.getColumnModel().getColumn(1).setMaxWidth(90);
/* 203 */     this.logTable.getColumnModel().getColumn(1).setResizable(false);
/*     */     
/* 205 */     this.logTable.getColumnModel().getColumn(2).setMinWidth(110);
/* 206 */     this.logTable.getColumnModel().getColumn(2).setMaxWidth(250);
/*     */     
/* 208 */     this.logTable.getColumnModel().getColumn(3).setPreferredWidth(150);
/* 209 */     this.logTable.getColumnModel().getColumn(3).setMaxWidth(400);
/*     */     
/* 211 */     this.logTable.getColumnModel().getColumn(4).setPreferredWidth(600);
/*     */   }
/*     */   
/*     */   protected void initializeToolBar(LogController.Expiration expiration) {
/* 215 */     this.configureButton.setFocusable(false);
/* 216 */     this.configureButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 218 */             LogViewImpl.this.centerWindowEvent.fire(new CenterWindow((Window)LogViewImpl.this.logCategorySelector));
/* 219 */             LogViewImpl.this.logCategorySelector.setVisible(!LogViewImpl.this.logCategorySelector.isVisible());
/*     */           }
/*     */         });
/*     */     
/* 223 */     this.clearButton.setFocusable(false);
/* 224 */     this.clearButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 226 */             LogViewImpl.this.logTableModel.clearMessages();
/*     */           }
/*     */         });
/*     */     
/* 230 */     this.copyButton.setFocusable(false);
/* 231 */     this.copyButton.setEnabled(false);
/* 232 */     this.copyButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 234 */             StringBuilder sb = new StringBuilder();
/* 235 */             List<LogMessage> messages = LogViewImpl.this.getSelectedMessages();
/* 236 */             for (LogMessage message : messages) {
/* 237 */               sb.append(message.toString()).append("\n");
/*     */             }
/* 239 */             Application.copyToClipboard(sb.toString());
/*     */           }
/*     */         });
/*     */     
/* 243 */     this.expandButton.setFocusable(false);
/* 244 */     this.expandButton.setEnabled(false);
/* 245 */     this.expandButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 247 */             List<LogMessage> messages = LogViewImpl.this.getSelectedMessages();
/* 248 */             if (messages.size() != 1)
/* 249 */               return;  LogViewImpl.this.presenter.onExpand(messages.get(0));
/*     */           }
/*     */         });
/*     */     
/* 253 */     this.pauseButton.setFocusable(false);
/* 254 */     this.pauseButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 256 */             LogViewImpl.this.logTableModel.setPaused(!LogViewImpl.this.logTableModel.isPaused());
/* 257 */             if (LogViewImpl.this.logTableModel.isPaused()) {
/* 258 */               LogViewImpl.this.pauseLabel.setText(" (Paused)");
/*     */             } else {
/* 260 */               LogViewImpl.this.pauseLabel.setText(" (Active)");
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 265 */     this.expirationComboBox.setSelectedItem(expiration);
/* 266 */     this.expirationComboBox.setMaximumSize(new Dimension(100, 32));
/* 267 */     this.expirationComboBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 269 */             JComboBox cb = (JComboBox)e.getSource();
/* 270 */             LogController.Expiration expiration = (LogController.Expiration)cb.getSelectedItem();
/* 271 */             LogViewImpl.this.logTableModel.setMaxAgeSeconds(expiration.getSeconds());
/*     */           }
/*     */         });
/*     */     
/* 275 */     this.toolBar.setFloatable(false);
/* 276 */     this.toolBar.add(this.copyButton);
/* 277 */     this.toolBar.add(this.expandButton);
/* 278 */     this.toolBar.add(Box.createHorizontalGlue());
/* 279 */     this.toolBar.add(this.configureButton);
/* 280 */     this.toolBar.add(this.clearButton);
/* 281 */     this.toolBar.add(this.pauseButton);
/* 282 */     this.toolBar.add(this.pauseLabel);
/* 283 */     this.toolBar.add(Box.createHorizontalGlue());
/* 284 */     this.toolBar.add(new JLabel("Clear after:"));
/* 285 */     this.toolBar.add(this.expirationComboBox);
/*     */   }
/*     */   
/*     */   protected LogController.Expiration getDefaultExpiration() {
/* 289 */     return LogController.Expiration.SIXTY_SECONDS;
/*     */   }
/*     */   
/*     */   protected ImageIcon getWarnErrorIcon() {
/* 293 */     return Application.createImageIcon(LogController.class, "img/warn.png");
/*     */   }
/*     */   
/*     */   protected ImageIcon getDebugIcon() {
/* 297 */     return Application.createImageIcon(LogController.class, "img/debug.png");
/*     */   }
/*     */   
/*     */   protected ImageIcon getTraceIcon() {
/* 301 */     return Application.createImageIcon(LogController.class, "img/trace.png");
/*     */   }
/*     */   
/*     */   protected ImageIcon getInfoIcon() {
/* 305 */     return Application.createImageIcon(LogController.class, "img/info.png");
/*     */   }
/*     */   
/*     */   protected int getExpandMessageCharacterLimit() {
/* 309 */     return 100;
/*     */   }
/*     */   
/*     */   protected List<LogMessage> getSelectedMessages() {
/* 313 */     List<LogMessage> messages = new ArrayList<>();
/* 314 */     for (int row : this.logTable.getSelectedRows()) {
/* 315 */       messages.add((LogMessage)this.logTableModel.getValueAt(row, 0));
/*     */     }
/* 317 */     return messages;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\log\impl\LogViewImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */