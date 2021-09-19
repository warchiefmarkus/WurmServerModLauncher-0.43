/*     */ package org.fourthline.cling.support.shared;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.util.List;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.UIManager;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.seamless.swing.AbstractController;
/*     */ import org.seamless.swing.Application;
/*     */ import org.seamless.swing.Controller;
/*     */ import org.seamless.swing.Event;
/*     */ import org.seamless.swing.logging.LogCategory;
/*     */ import org.seamless.swing.logging.LogController;
/*     */ import org.seamless.swing.logging.LogMessage;
/*     */ import org.seamless.swing.logging.LoggingHandler;
/*     */ import org.seamless.util.logging.LoggingUtil;
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
/*     */ public abstract class MainController
/*     */   extends AbstractController<JFrame>
/*     */ {
/*     */   private final LogController logController;
/*     */   private final JPanel logPanel;
/*     */   
/*     */   public MainController(JFrame view, List<LogCategory> logCategories) {
/*  52 */     super(view);
/*     */ 
/*     */     
/*     */     try {
/*  56 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*  57 */     } catch (Exception ex) {
/*  58 */       System.out.println("Unable to load native look and feel: " + ex.toString());
/*     */     } 
/*     */ 
/*     */     
/*  62 */     System.setProperty("sun.awt.exception.handler", AWTExceptionHandler.class.getName());
/*     */ 
/*     */     
/*  65 */     Runtime.getRuntime().addShutdownHook(new Thread()
/*     */         {
/*     */           public void run() {
/*  68 */             if (MainController.this.getUpnpService() != null) {
/*  69 */               MainController.this.getUpnpService().shutdown();
/*     */             }
/*     */           }
/*     */         });
/*     */     
/*  74 */     this.logController = new LogController((Controller)this, logCategories)
/*     */       {
/*     */         protected void expand(LogMessage logMessage) {
/*  77 */           fireEventGlobal((Event)new TextExpandEvent(logMessage
/*  78 */                 .getMessage()));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         protected Frame getParentWindow() {
/*  84 */           return (Frame)MainController.this.getView();
/*     */         }
/*     */       };
/*  87 */     this.logPanel = (JPanel)this.logController.getView();
/*  88 */     this.logPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  92 */     LoggingHandler loggingHandler = new LoggingHandler() {
/*     */         protected void log(LogMessage msg) {
/*  94 */           MainController.this.logController.pushMessage(msg);
/*     */         }
/*     */       };
/*  97 */     if (System.getProperty("java.util.logging.config.file") == null) {
/*  98 */       LoggingUtil.resetRootHandler(new Handler[] { (Handler)loggingHandler });
/*     */     } else {
/* 100 */       LogManager.getLogManager().getLogger("").addHandler((Handler)loggingHandler);
/*     */     } 
/*     */   }
/*     */   
/*     */   public LogController getLogController() {
/* 105 */     return this.logController;
/*     */   }
/*     */   
/*     */   public JPanel getLogPanel() {
/* 109 */     return this.logPanel;
/*     */   }
/*     */   
/*     */   public void log(Level level, String msg) {
/* 113 */     log(new LogMessage(level, msg));
/*     */   }
/*     */   
/*     */   public void log(LogMessage message) {
/* 117 */     getLogController().pushMessage(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 122 */     super.dispose();
/* 123 */     ShutdownWindow.INSTANCE.setVisible(true);
/* 124 */     (new Thread()
/*     */       {
/*     */         public void run() {
/* 127 */           System.exit(0);
/*     */         }
/* 129 */       }).start();
/*     */   }
/*     */   public abstract UpnpService getUpnpService();
/*     */   
/* 133 */   public static class ShutdownWindow extends JWindow { public static final JWindow INSTANCE = new ShutdownWindow();
/*     */     
/*     */     protected ShutdownWindow() {
/* 136 */       JLabel shutdownLabel = new JLabel("Shutting down, please wait...");
/* 137 */       shutdownLabel.setHorizontalAlignment(0);
/* 138 */       getContentPane().add(shutdownLabel);
/* 139 */       setPreferredSize(new Dimension(300, 30));
/* 140 */       pack();
/* 141 */       Application.center(this);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\MainController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */