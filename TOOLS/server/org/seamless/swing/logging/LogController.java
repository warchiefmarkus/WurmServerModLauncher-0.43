/*     */ package org.seamless.swing.logging;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import org.seamless.swing.AbstractController;
/*     */ import org.seamless.swing.Application;
/*     */ import org.seamless.swing.Controller;
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
/*     */ 
/*     */ public abstract class LogController
/*     */   extends AbstractController<JPanel>
/*     */ {
/*     */   private final LogCategorySelector logCategorySelector;
/*     */   private final JTable logTable;
/*     */   private final LogTableModel logTableModel;
/*  53 */   private final JToolBar toolBar = new JToolBar();
/*  54 */   private final JButton configureButton = createConfigureButton();
/*  55 */   private final JButton clearButton = createClearButton();
/*  56 */   private final JButton copyButton = createCopyButton();
/*  57 */   private final JButton expandButton = createExpandButton();
/*  58 */   private final JButton pauseButton = createPauseButton();
/*  59 */   private final JLabel pauseLabel = new JLabel(" (Active)");
/*  60 */   private final JComboBox expirationComboBox = new JComboBox<Expiration>(Expiration.values());
/*     */   
/*     */   public enum Expiration {
/*  63 */     TEN_SECONDS(10, "10 Seconds"),
/*  64 */     SIXTY_SECONDS(60, "60 Seconds"),
/*  65 */     FIVE_MINUTES(300, "5 Minutes"),
/*  66 */     NEVER(2147483647, "Never");
/*     */     
/*     */     private int seconds;
/*     */     private String label;
/*     */     
/*     */     Expiration(int seconds, String label) {
/*  72 */       this.seconds = seconds;
/*  73 */       this.label = label;
/*     */     }
/*     */     
/*     */     public int getSeconds() {
/*  77 */       return this.seconds;
/*     */     }
/*     */     
/*     */     public String getLabel() {
/*  81 */       return this.label;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  86 */       return getLabel();
/*     */     }
/*     */   }
/*     */   
/*     */   public LogController(Controller parentController, List<LogCategory> logCategories) {
/*  91 */     this(parentController, Expiration.SIXTY_SECONDS, logCategories);
/*     */   }
/*     */   
/*     */   public LogController(Controller parentController, Expiration expiration, List<LogCategory> logCategories) {
/*  95 */     super(new JPanel(new BorderLayout()), parentController);
/*     */     
/*  97 */     this.logCategorySelector = new LogCategorySelector(logCategories);
/*     */     
/*  99 */     this.logTableModel = new LogTableModel(expiration.getSeconds());
/* 100 */     this.logTable = new JTable(this.logTableModel);
/*     */     
/* 102 */     this.logTable.setDefaultRenderer(LogMessage.class, new LogTableCellRenderer()
/*     */         {
/*     */           protected ImageIcon getWarnErrorIcon()
/*     */           {
/* 106 */             return LogController.this.getWarnErrorIcon();
/*     */           }
/*     */           
/*     */           protected ImageIcon getDebugIcon() {
/* 110 */             return LogController.this.getDebugIcon();
/*     */           }
/*     */           
/*     */           protected ImageIcon getTraceIcon() {
/* 114 */             return LogController.this.getTraceIcon();
/*     */           }
/*     */           
/*     */           protected ImageIcon getInfoIcon() {
/* 118 */             return LogController.this.getInfoIcon();
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
/* 130 */             if (e.getSource() == LogController.this.logTable.getSelectionModel()) {
/* 131 */               int[] rows = LogController.this.logTable.getSelectedRows();
/*     */               
/* 133 */               if (rows == null || rows.length == 0) {
/* 134 */                 LogController.this.copyButton.setEnabled(false);
/* 135 */                 LogController.this.expandButton.setEnabled(false);
/* 136 */               } else if (rows.length == 1) {
/* 137 */                 LogController.this.copyButton.setEnabled(true);
/* 138 */                 LogMessage msg = (LogMessage)LogController.this.logTableModel.getValueAt(rows[0], 0);
/* 139 */                 if (msg.getMessage().length() > LogController.this.getExpandMessageCharacterLimit()) {
/* 140 */                   LogController.this.expandButton.setEnabled(true);
/*     */                 } else {
/* 142 */                   LogController.this.expandButton.setEnabled(false);
/*     */                 } 
/*     */               } else {
/* 145 */                 LogController.this.copyButton.setEnabled(true);
/* 146 */                 LogController.this.expandButton.setEnabled(false);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 153 */     adjustTableUI();
/* 154 */     initializeToolBar(expiration);
/*     */     
/* 156 */     ((JPanel)getView()).setPreferredSize(new Dimension(250, 100));
/* 157 */     ((JPanel)getView()).setMinimumSize(new Dimension(250, 50));
/* 158 */     ((JPanel)getView()).add(new JScrollPane(this.logTable), "Center");
/* 159 */     ((JPanel)getView()).add(this.toolBar, "South");
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushMessage(final LogMessage message) {
/* 164 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run() {
/* 167 */             LogController.this.logTableModel.pushMessage(message);
/*     */ 
/*     */             
/* 170 */             if (!LogController.this.logTableModel.isPaused()) {
/* 171 */               LogController.this.logTable.scrollRectToVisible(LogController.this.logTable.getCellRect(LogController.this.logTableModel.getRowCount() - 1, 0, true));
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void adjustTableUI() {
/* 180 */     this.logTable.setFocusable(false);
/* 181 */     this.logTable.setRowHeight(18);
/* 182 */     this.logTable.getTableHeader().setReorderingAllowed(false);
/* 183 */     this.logTable.setBorder(BorderFactory.createEmptyBorder());
/*     */     
/* 185 */     this.logTable.getColumnModel().getColumn(0).setMinWidth(30);
/* 186 */     this.logTable.getColumnModel().getColumn(0).setMaxWidth(30);
/* 187 */     this.logTable.getColumnModel().getColumn(0).setResizable(false);
/*     */ 
/*     */     
/* 190 */     this.logTable.getColumnModel().getColumn(1).setMinWidth(90);
/* 191 */     this.logTable.getColumnModel().getColumn(1).setMaxWidth(90);
/* 192 */     this.logTable.getColumnModel().getColumn(1).setResizable(false);
/*     */     
/* 194 */     this.logTable.getColumnModel().getColumn(2).setMinWidth(100);
/* 195 */     this.logTable.getColumnModel().getColumn(2).setMaxWidth(250);
/*     */     
/* 197 */     this.logTable.getColumnModel().getColumn(3).setPreferredWidth(150);
/* 198 */     this.logTable.getColumnModel().getColumn(3).setMaxWidth(400);
/*     */     
/* 200 */     this.logTable.getColumnModel().getColumn(4).setPreferredWidth(600);
/*     */   }
/*     */   
/*     */   protected void initializeToolBar(Expiration expiration) {
/* 204 */     this.configureButton.setFocusable(false);
/* 205 */     this.configureButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 207 */             Application.center(LogController.this.logCategorySelector, LogController.this.getParentWindow());
/* 208 */             LogController.this.logCategorySelector.setVisible(!LogController.this.logCategorySelector.isVisible());
/*     */           }
/*     */         });
/*     */     
/* 212 */     this.clearButton.setFocusable(false);
/* 213 */     this.clearButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 215 */             LogController.this.logTableModel.clearMessages();
/*     */           }
/*     */         });
/*     */     
/* 219 */     this.copyButton.setFocusable(false);
/* 220 */     this.copyButton.setEnabled(false);
/* 221 */     this.copyButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 223 */             StringBuilder sb = new StringBuilder();
/* 224 */             List<LogMessage> messages = LogController.this.getSelectedMessages();
/* 225 */             for (LogMessage message : messages) {
/* 226 */               sb.append(message.toString()).append("\n");
/*     */             }
/* 228 */             Application.copyToClipboard(sb.toString());
/*     */           }
/*     */         });
/*     */     
/* 232 */     this.expandButton.setFocusable(false);
/* 233 */     this.expandButton.setEnabled(false);
/* 234 */     this.expandButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 236 */             List<LogMessage> messages = LogController.this.getSelectedMessages();
/* 237 */             if (messages.size() != 1)
/* 238 */               return;  LogController.this.expand(messages.get(0));
/*     */           }
/*     */         });
/*     */     
/* 242 */     this.pauseButton.setFocusable(false);
/* 243 */     this.pauseButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 245 */             LogController.this.logTableModel.setPaused(!LogController.this.logTableModel.isPaused());
/* 246 */             if (LogController.this.logTableModel.isPaused()) {
/* 247 */               LogController.this.pauseLabel.setText(" (Paused)");
/*     */             } else {
/* 249 */               LogController.this.pauseLabel.setText(" (Active)");
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 254 */     this.expirationComboBox.setSelectedItem(expiration);
/* 255 */     this.expirationComboBox.setMaximumSize(new Dimension(100, 32));
/* 256 */     this.expirationComboBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 258 */             JComboBox cb = (JComboBox)e.getSource();
/* 259 */             LogController.Expiration expiration = (LogController.Expiration)cb.getSelectedItem();
/* 260 */             LogController.this.logTableModel.setMaxAgeSeconds(expiration.getSeconds());
/*     */           }
/*     */         });
/*     */     
/* 264 */     this.toolBar.setFloatable(false);
/* 265 */     this.toolBar.add(this.copyButton);
/* 266 */     this.toolBar.add(this.expandButton);
/* 267 */     this.toolBar.add(Box.createHorizontalGlue());
/* 268 */     this.toolBar.add(this.configureButton);
/* 269 */     this.toolBar.add(this.clearButton);
/* 270 */     this.toolBar.add(this.pauseButton);
/* 271 */     this.toolBar.add(this.pauseLabel);
/* 272 */     this.toolBar.add(Box.createHorizontalGlue());
/* 273 */     this.toolBar.add(new JLabel("Clear after:"));
/* 274 */     this.toolBar.add(this.expirationComboBox);
/*     */   }
/*     */   
/*     */   protected List<LogMessage> getSelectedMessages() {
/* 278 */     List<LogMessage> messages = new ArrayList<LogMessage>();
/* 279 */     for (int row : this.logTable.getSelectedRows()) {
/* 280 */       messages.add((LogMessage)this.logTableModel.getValueAt(row, 0));
/*     */     }
/* 282 */     return messages;
/*     */   }
/*     */   
/*     */   protected int getExpandMessageCharacterLimit() {
/* 286 */     return 100;
/*     */   }
/*     */   
/*     */   public LogTableModel getLogTableModel() {
/* 290 */     return this.logTableModel;
/*     */   }
/*     */   
/*     */   protected JButton createConfigureButton() {
/* 294 */     return new JButton("Options...", Application.createImageIcon(LogController.class, "img/configure.png"));
/*     */   }
/*     */   
/*     */   protected JButton createClearButton() {
/* 298 */     return new JButton("Clear Log", Application.createImageIcon(LogController.class, "img/removetext.png"));
/*     */   }
/*     */   
/*     */   protected JButton createCopyButton() {
/* 302 */     return new JButton("Copy", Application.createImageIcon(LogController.class, "img/copyclipboard.png"));
/*     */   }
/*     */   
/*     */   protected JButton createExpandButton() {
/* 306 */     return new JButton("Expand", Application.createImageIcon(LogController.class, "img/viewtext.png"));
/*     */   }
/*     */   
/*     */   protected JButton createPauseButton() {
/* 310 */     return new JButton("Pause/Continue Log", Application.createImageIcon(LogController.class, "img/pause.png"));
/*     */   }
/*     */   
/*     */   protected ImageIcon getWarnErrorIcon() {
/* 314 */     return Application.createImageIcon(LogController.class, "img/warn.png");
/*     */   }
/*     */   
/*     */   protected ImageIcon getDebugIcon() {
/* 318 */     return Application.createImageIcon(LogController.class, "img/debug.png");
/*     */   }
/*     */   
/*     */   protected ImageIcon getTraceIcon() {
/* 322 */     return Application.createImageIcon(LogController.class, "img/trace.png");
/*     */   }
/*     */   
/*     */   protected ImageIcon getInfoIcon() {
/* 326 */     return Application.createImageIcon(LogController.class, "img/info.png");
/*     */   }
/*     */   
/*     */   protected abstract void expand(LogMessage paramLogMessage);
/*     */   
/*     */   protected abstract Frame getParentWindow();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LogController.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */