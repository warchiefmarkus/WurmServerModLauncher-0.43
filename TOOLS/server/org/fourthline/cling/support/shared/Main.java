/*     */ package org.fourthline.cling.support.shared;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.LogManager;
/*     */ import javax.inject.Inject;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import org.fourthline.cling.support.shared.log.LogView;
/*     */ import org.seamless.swing.Application;
/*     */ import org.seamless.swing.logging.LogMessage;
/*     */ import org.seamless.swing.logging.LoggingHandler;
/*     */ import org.seamless.util.OS;
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
/*     */ 
/*     */ 
/*     */ public abstract class Main
/*     */   implements ShutdownHandler, Thread.UncaughtExceptionHandler
/*     */ {
/*     */   @Inject
/*     */   LogView.Presenter logPresenter;
/*  53 */   protected final JFrame errorWindow = new JFrame();
/*     */ 
/*     */   
/*  56 */   protected final LoggingHandler loggingHandler = new LoggingHandler()
/*     */     {
/*     */       protected void log(LogMessage msg)
/*     */       {
/*  60 */         Main.this.logPresenter.pushMessage(msg);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   protected boolean isRegularShutdown;
/*     */ 
/*     */   
/*     */   public void init() {
/*     */     try {
/*  70 */       if (OS.checkForMac()) {
/*  71 */         NewPlatformApple.setup(this, getAppName());
/*     */       }
/*     */       
/*  74 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*     */     }
/*  76 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.errorWindow.setPreferredSize(new Dimension(900, 400));
/*  82 */     this.errorWindow.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent windowEvent) {
/*  85 */             Main.this.errorWindow.dispose();
/*     */           }
/*     */         });
/*  88 */     Thread.setDefaultUncaughtExceptionHandler(this);
/*     */ 
/*     */     
/*  91 */     Runtime.getRuntime().addShutdownHook(new Thread()
/*     */         {
/*     */           public void run() {
/*  94 */             if (!Main.this.isRegularShutdown) {
/*  95 */               Main.this.shutdown();
/*     */             }
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 102 */     if (System.getProperty("java.util.logging.config.file") == null) {
/* 103 */       LoggingUtil.resetRootHandler(new Handler[] { (Handler)this.loggingHandler });
/*     */     } else {
/* 105 */       LogManager.getLogManager().getLogger("").addHandler((Handler)this.loggingHandler);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 111 */     this.isRegularShutdown = true;
/* 112 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 114 */             Main.this.errorWindow.dispose();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void uncaughtException(Thread thread, final Throwable throwable) {
/* 122 */     System.err.println("In thread '" + thread + "' uncaught exception: " + throwable);
/* 123 */     throwable.printStackTrace(System.err);
/*     */     
/* 125 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run() {
/* 128 */             Main.this.errorWindow.getContentPane().removeAll();
/*     */             
/* 130 */             JTextArea textArea = new JTextArea();
/* 131 */             textArea.setEditable(false);
/* 132 */             StringBuilder text = new StringBuilder();
/*     */             
/* 134 */             text.append("An exceptional error occurred!\nYou can try to continue or exit the application.\n\n");
/* 135 */             text.append("Please tell us about this here:\nhttp://www.4thline.org/projects/mailinglists-cling.html\n\n");
/* 136 */             text.append("-------------------------------------------------------------------------------------------------------------\n\n");
/* 137 */             Writer stackTrace = new StringWriter();
/* 138 */             throwable.printStackTrace(new PrintWriter(stackTrace));
/* 139 */             text.append(stackTrace.toString());
/*     */             
/* 141 */             textArea.setText(text.toString());
/* 142 */             JScrollPane pane = new JScrollPane(textArea);
/* 143 */             Main.this.errorWindow.getContentPane().add(pane, "Center");
/*     */             
/* 145 */             JButton exitButton = new JButton("Exit Application");
/* 146 */             exitButton.addActionListener(new ActionListener()
/*     */                 {
/*     */                   public void actionPerformed(ActionEvent e) {
/* 149 */                     System.exit(1);
/*     */                   }
/*     */                 });
/*     */             
/* 153 */             Main.this.errorWindow.getContentPane().add(exitButton, "South");
/*     */             
/* 155 */             Main.this.errorWindow.pack();
/* 156 */             Application.center(Main.this.errorWindow);
/* 157 */             textArea.setCaretPosition(0);
/*     */             
/* 159 */             Main.this.errorWindow.setVisible(true);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void removeLoggingHandler() {
/* 165 */     LogManager.getLogManager().getLogger("").removeHandler((Handler)this.loggingHandler);
/*     */   }
/*     */   
/*     */   protected abstract String getAppName();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */