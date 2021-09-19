/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Pageable;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.jnlp.PrintService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PrintServiceImpl
/*     */   implements PrintService
/*     */ {
/*  23 */   private static PrintServiceImpl _sharedInstance = null;
/*  24 */   private static SmartSecurityDialog _securityDialog = null;
/*  25 */   private PageFormat _pageFormat = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized PrintServiceImpl getInstance() {
/*  33 */     if (_sharedInstance == null) {
/*  34 */       _sharedInstance = new PrintServiceImpl();
/*     */     }
/*  36 */     return _sharedInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PageFormat getDefaultPage() {
/*  42 */     PrinterJob printerJob = PrinterJob.getPrinterJob();
/*  43 */     if (printerJob != null)
/*  44 */       return AccessController.<PageFormat>doPrivileged(new PrivilegedAction(this, printerJob) { private final PrinterJob val$sysPrinterJob;
/*     */             
/*     */             public Object run() {
/*  47 */               return this.val$sysPrinterJob.defaultPage();
/*     */             }
/*     */             private final PrintServiceImpl this$0; }
/*     */         ); 
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PageFormat showPageFormatDialog(PageFormat paramPageFormat) {
/*  57 */     PrinterJob printerJob = PrinterJob.getPrinterJob();
/*  58 */     if (printerJob != null) {
/*  59 */       return AccessController.<PageFormat>doPrivileged(new PrivilegedAction(this, printerJob, paramPageFormat) { private final PrinterJob val$sysPrinterJob; private final PageFormat val$page;
/*     */             private final PrintServiceImpl this$0;
/*     */             
/*     */             public Object run() {
/*  63 */               this.this$0._pageFormat = this.val$sysPrinterJob.pageDialog(this.val$page);
/*  64 */               return this.this$0._pageFormat;
/*     */             } }
/*     */         );
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean print(Pageable paramPageable) {
/*  73 */     return doPrinting(null, paramPageable);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean print(Printable paramPrintable) {
/*  78 */     return doPrinting(paramPrintable, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean doPrinting(Printable paramPrintable, Pageable paramPageable) {
/*  83 */     if (!askUser()) return false; 
/*  84 */     PrinterJob printerJob = PrinterJob.getPrinterJob();
/*  85 */     if (printerJob == null) return false; 
/*     */     try {
/*  87 */       Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this, paramPageable, printerJob, paramPrintable) { private final Pageable val$document;
/*     */             private final PrinterJob val$sysPrinterJob;
/*     */             
/*     */             public Object run() {
/*  91 */               if (this.val$document != null) {
/*  92 */                 this.val$sysPrinterJob.setPageable(this.val$document);
/*     */               }
/*  94 */               else if (this.this$0._pageFormat == null) {
/*  95 */                 this.val$sysPrinterJob.setPrintable(this.val$painter);
/*     */               } else {
/*  97 */                 this.val$sysPrinterJob.setPrintable(this.val$painter, this.this$0._pageFormat);
/*     */               } 
/*     */ 
/*     */               
/* 101 */               if (this.val$sysPrinterJob.printDialog()) {
/*     */                 
/* 103 */                 Thread thread = new Thread(new Runnable(this) { private final PrintServiceImpl.null this$1;
/*     */                       public void run() {
/*     */                         try {
/* 106 */                           this.this$1.val$sysPrinterJob.print();
/* 107 */                         } catch (PrinterException printerException) {
/* 108 */                           Trace.ignoredException(printerException);
/*     */                         } 
/*     */                       } }
/*     */                   );
/* 112 */                 thread.start();
/* 113 */                 return Boolean.TRUE;
/*     */               } 
/* 115 */               return Boolean.FALSE;
/*     */             } private final Printable val$painter;
/*     */             private final PrintServiceImpl this$0; }
/*     */         );
/* 119 */       return bool.booleanValue();
/*     */     } finally {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized boolean askUser() {
/* 126 */     if (CheckServicePermission.hasPrintAccessPermissions()) return true; 
/* 127 */     return requestPrintPermission();
/*     */   }
/*     */   
/*     */   public static boolean requestPrintPermission() {
/* 131 */     if (_securityDialog == null) {
/* 132 */       _securityDialog = new SmartSecurityDialog(ResourceManager.getString("APIImpl.print.message"), true);
/*     */     }
/*     */     
/* 135 */     return _securityDialog.showDialog();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\PrintServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */