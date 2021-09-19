/*     */ package org.seamless.swing.logging;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JToolBar;
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
/*     */ public class LogCategorySelector
/*     */   extends JDialog
/*     */ {
/*  40 */   protected final JPanel mainPanel = new JPanel();
/*     */   
/*     */   public LogCategorySelector(List<LogCategory> logCategories) {
/*  43 */     setTitle("Select logging categories...");
/*     */     
/*  45 */     this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 1));
/*  46 */     this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
/*     */     
/*  48 */     addLogCategories(logCategories);
/*     */     
/*  50 */     JScrollPane scrollPane = new JScrollPane(this.mainPanel);
/*  51 */     scrollPane.setBorder(BorderFactory.createEmptyBorder());
/*  52 */     add(scrollPane);
/*     */     
/*  54 */     setMaximumSize(new Dimension(750, 550));
/*  55 */     setResizable(false);
/*  56 */     pack();
/*     */   }
/*     */   
/*     */   protected void addLogCategories(List<LogCategory> logCategories) {
/*  60 */     for (LogCategory logCategory : logCategories) {
/*  61 */       addLogCategory(logCategory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addLogCategory(LogCategory logCategory) {
/*  67 */     JPanel categoryPanel = new JPanel(new BorderLayout());
/*  68 */     categoryPanel.setBorder(BorderFactory.createTitledBorder(logCategory.getName()));
/*     */     
/*  70 */     addLoggerGroups(logCategory, categoryPanel);
/*     */     
/*  72 */     this.mainPanel.add(categoryPanel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addLoggerGroups(final LogCategory logCategory, final JPanel categoryPanel) {
/*  77 */     JPanel checkboxPanel = new JPanel();
/*  78 */     checkboxPanel.setLayout(new BoxLayout(checkboxPanel, 1));
/*  79 */     for (LogCategory.Group group : logCategory.getGroups()) {
/*     */       
/*  81 */       JCheckBox checkBox = new JCheckBox(group.getName());
/*  82 */       checkBox.setSelected(group.isEnabled());
/*  83 */       checkBox.setFocusable(false);
/*  84 */       checkBox.addItemListener(new ItemListener() {
/*     */             public void itemStateChanged(ItemEvent e) {
/*  86 */               if (e.getStateChange() == 2) {
/*  87 */                 LogCategorySelector.this.disableLoggerGroup(group);
/*  88 */               } else if (e.getStateChange() == 1) {
/*  89 */                 LogCategorySelector.this.enableLoggerGroup(group);
/*     */               } 
/*     */             }
/*     */           });
/*  93 */       checkboxPanel.add(checkBox);
/*     */     } 
/*     */     
/*  96 */     JToolBar buttonBar = new JToolBar();
/*  97 */     buttonBar.setFloatable(false);
/*     */     
/*  99 */     JButton enableAllButton = new JButton("All");
/* 100 */     enableAllButton.setFocusable(false);
/* 101 */     enableAllButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 103 */             for (LogCategory.Group group : logCategory.getGroups()) {
/* 104 */               LogCategorySelector.this.enableLoggerGroup(group);
/*     */             }
/* 106 */             categoryPanel.removeAll();
/* 107 */             LogCategorySelector.this.addLoggerGroups(logCategory, categoryPanel);
/* 108 */             categoryPanel.revalidate();
/*     */           }
/*     */         });
/* 111 */     buttonBar.add(enableAllButton);
/*     */     
/* 113 */     JButton disableAllButton = new JButton("None");
/* 114 */     disableAllButton.setFocusable(false);
/* 115 */     disableAllButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 117 */             for (LogCategory.Group group : logCategory.getGroups()) {
/* 118 */               LogCategorySelector.this.disableLoggerGroup(group);
/*     */             }
/* 120 */             categoryPanel.removeAll();
/* 121 */             LogCategorySelector.this.addLoggerGroups(logCategory, categoryPanel);
/* 122 */             categoryPanel.revalidate();
/*     */           }
/*     */         });
/* 125 */     buttonBar.add(disableAllButton);
/*     */     
/* 127 */     categoryPanel.add(checkboxPanel, "Center");
/* 128 */     categoryPanel.add(buttonBar, "North");
/*     */   }
/*     */   
/*     */   protected void enableLoggerGroup(LogCategory.Group group) {
/* 132 */     group.setEnabled(true);
/*     */     
/* 134 */     group.getPreviousLevels().clear();
/* 135 */     for (LogCategory.LoggerLevel loggerLevel : group.getLoggerLevels()) {
/* 136 */       Logger logger = Logger.getLogger(loggerLevel.getLogger());
/*     */       
/* 138 */       group.getPreviousLevels().add(new LogCategory.LoggerLevel(logger.getName(), getLevel(logger)));
/*     */ 
/*     */ 
/*     */       
/* 142 */       logger.setLevel(loggerLevel.getLevel());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void disableLoggerGroup(LogCategory.Group group) {
/* 147 */     group.setEnabled(false);
/*     */     
/* 149 */     for (LogCategory.LoggerLevel loggerLevel : group.getPreviousLevels()) {
/* 150 */       Logger logger = Logger.getLogger(loggerLevel.getLogger());
/* 151 */       logger.setLevel(loggerLevel.getLevel());
/*     */     } 
/*     */     
/* 154 */     if (group.getPreviousLevels().size() == 0)
/*     */     {
/* 156 */       for (LogCategory.LoggerLevel loggerLevel : group.getLoggerLevels()) {
/* 157 */         Logger logger = Logger.getLogger(loggerLevel.getLogger());
/* 158 */         logger.setLevel(Level.INFO);
/*     */       } 
/*     */     }
/*     */     
/* 162 */     group.getPreviousLevels().clear();
/*     */   }
/*     */   
/*     */   public Level getLevel(Logger logger) {
/* 166 */     Level level = logger.getLevel();
/* 167 */     if (level == null && logger.getParent() != null) {
/* 168 */       level = logger.getParent().getLevel();
/*     */     }
/* 170 */     return level;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LogCategorySelector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */