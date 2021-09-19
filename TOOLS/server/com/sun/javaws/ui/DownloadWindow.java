/*     */ package com.sun.javaws.ui;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.DeployUIManager;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.LaunchDownload;
/*     */ import com.sun.javaws.Main;
/*     */ import com.sun.javaws.cache.CacheImageLoader;
/*     */ import com.sun.javaws.cache.CacheImageLoaderCallback;
/*     */ import com.sun.javaws.jnl.IconDesc;
/*     */ import com.sun.javaws.jnl.InformationDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultBoundedRangeModel;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ 
/*     */ public class DownloadWindow
/*     */   extends WindowAdapter implements ActionListener, LaunchDownload.DownloadProgress, CacheImageLoaderCallback {
/*  49 */   private JFrame _frame = null;
/*     */ 
/*     */   
/*     */   private String _title;
/*     */   
/*     */   private String _vendor;
/*     */   
/*  56 */   private long _estimatedDownloadSize = 0L;
/*  57 */   private long _totalDownloadedBytes = 0L;
/*  58 */   private URL _currentUrl = null;
/*     */   
/*     */   static final int TIMER_UPDATE_RATE = 1000;
/*     */   
/*     */   static final int TIMER_INITIAL_DELAY = 10;
/*     */   static final int TIMER_AVERAGE_SIZE = 10;
/*  64 */   Timer _timerObject = null;
/*  65 */   long[] _timerDownloadAverage = new long[10];
/*  66 */   int _timerCount = 0;
/*  67 */   long _timerLastBytesCount = 0L;
/*     */   
/*     */   boolean _timerOn = false;
/*     */   
/*     */   static final int HEART_BEAT_RATE = 250;
/*  72 */   static final boolean[] HEART_BEAT_RYTHM = new boolean[] { false, false, false, true, false, true };
/*  73 */   Timer _heartbeatTimer = null;
/*  74 */   Object _heartbeatLock = new Object();
/*  75 */   int _heartbeatCount = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean _heartbeatOn = false;
/*     */ 
/*     */   
/*     */   boolean _isCanceled = false;
/*     */ 
/*     */   
/*     */   boolean _exitOnCancel = true;
/*     */ 
/*     */   
/*     */   private Image _appImage;
/*     */ 
/*     */   
/*  91 */   private JButton _cancelButton = null;
/*  92 */   private JLabel _titleLabel = null;
/*  93 */   private JLabel _vendorLabel = null;
/*  94 */   private JLabel _infoStatus = null;
/*  95 */   private JLabel _infoProgressTxt = null;
/*  96 */   private JLabel _infoEstimatedTime = null;
/*  97 */   private JProgressBar _infoProgressBar = null;
/*  98 */   private JLabel _imageLabel = null;
/*     */ 
/*     */   
/*     */   private static final int _yRestriction = 20;
/*     */ 
/*     */   
/*     */   private static final int MAX_DISPLAY = 20;
/*     */ 
/*     */   
/*     */   private static final String LEAD = "...";
/*     */ 
/*     */   
/*     */   private DefaultBoundedRangeModel _loadingModel;
/*     */ 
/*     */   
/*     */   private ActionListener _cancelActionListener;
/*     */ 
/*     */   
/*     */   public DownloadWindow(LaunchDesc paramLaunchDesc, boolean paramBoolean) {
/* 117 */     setLaunchDesc(paramLaunchDesc, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLaunchDesc(LaunchDesc paramLaunchDesc, boolean paramBoolean) {
/* 125 */     InformationDesc informationDesc = paramLaunchDesc.getInformation();
/* 126 */     this._title = informationDesc.getTitle();
/* 127 */     this._vendor = informationDesc.getVendor();
/* 128 */     if (this._titleLabel != null) {
/* 129 */       this._titleLabel.setText(this._title);
/* 130 */       this._vendorLabel.setText(this._vendor);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     this._isCanceled = false;
/* 135 */     this._exitOnCancel = paramBoolean;
/*     */     
/* 137 */     if (informationDesc != null) {
/* 138 */       IconDesc iconDesc = informationDesc.getIconLocation(2, 0);
/*     */       
/* 140 */       if (iconDesc != null) {
/* 141 */         CacheImageLoader.getInstance().loadImage(iconDesc, this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void imageAvailable(IconDesc paramIconDesc, Image paramImage, File paramFile) {
/* 148 */     updateImage(paramImage, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void finalImageAvailable(IconDesc paramIconDesc, Image paramImage, File paramFile) {}
/*     */ 
/*     */   
/*     */   public JFrame getFrame() {
/* 156 */     return this._frame;
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildIntroScreen() {
/* 161 */     LookAndFeel lookAndFeel = null;
/*     */ 
/*     */     
/*     */     try {
/* 165 */       lookAndFeel = DeployUIManager.setLookAndFeel();
/*     */       
/* 167 */       this._frame = new JFrame(ResourceManager.getString("product.javaws.name", ""));
/*     */       
/* 169 */       this._frame.addWindowListener(this);
/*     */       
/* 171 */       JPanel jPanel1 = new JPanel(new BorderLayout());
/* 172 */       Container container = this._frame.getContentPane();
/* 173 */       container.setLayout(new BorderLayout());
/* 174 */       container.add(jPanel1, "Center");
/*     */       
/* 176 */       jPanel1.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8), new BevelBorder(1)));
/*     */ 
/*     */ 
/*     */       
/* 180 */       JPanel jPanel2 = new JPanel(new BorderLayout());
/* 181 */       jPanel1.add(jPanel2, "North");
/*     */       
/* 183 */       JPanel jPanel3 = new JPanel(new BorderLayout());
/* 184 */       jPanel1.add(jPanel3, "Center");
/*     */ 
/*     */       
/* 187 */       JPanel jPanel4 = new JPanel(new BorderLayout());
/* 188 */       this._imageLabel = new BLabel(this);
/* 189 */       jPanel4.add(this._imageLabel, "Center");
/* 190 */       jPanel4.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
/* 191 */       updateImage(ResourceManager.getIcon("java48.image").getImage(), false);
/*     */       
/* 193 */       jPanel2.add(jPanel4, "West");
/*     */ 
/*     */ 
/*     */       
/* 197 */       Font font1 = ResourceManager.getUIFont();
/* 198 */       Font font2 = font1.deriveFont(22.0F);
/* 199 */       Font font3 = font1.deriveFont(18.0F);
/*     */ 
/*     */       
/* 202 */       JPanel jPanel5 = new JPanel(new GridLayout(2, 3));
/* 203 */       jPanel2.add(jPanel5, "Center");
/*     */       
/* 205 */       this._titleLabel = new BLabel(this, this._title, 360, 0);
/* 206 */       this._titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
/* 207 */       this._titleLabel.setFont(font2);
/* 208 */       jPanel5.add(this._titleLabel);
/*     */       
/* 210 */       this._vendorLabel = new BLabel(this, this._vendor, 0, 0);
/* 211 */       this._vendorLabel.setFont(font3);
/* 212 */       this._vendorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
/* 213 */       jPanel5.add(this._vendorLabel);
/*     */       
/* 215 */       JPanel jPanel6 = new JPanel(new BorderLayout());
/* 216 */       container.add(jPanel6, "South");
/*     */ 
/*     */       
/* 219 */       this._cancelButton = new JButton(ResourceManager.getString("launch.cancel"));
/*     */       
/* 221 */       this._cancelButton.addActionListener(new ActionListener(this) { private final DownloadWindow this$0;
/*     */             public void actionPerformed(ActionEvent param1ActionEvent) {
/* 223 */               this.this$0.cancelAction();
/*     */             } }
/*     */         );
/*     */ 
/*     */       
/* 228 */       this._infoStatus = new BLabel(this, ResourceManager.getString("launch.initializing", this._title, this._vendor), 0, 0);
/*     */ 
/*     */       
/* 231 */       this._infoProgressTxt = new BLabel(this, " ", 0, 0);
/*     */       
/* 233 */       this._infoEstimatedTime = new BLabel(this, " ", 0, 0);
/*     */       
/* 235 */       this._loadingModel = new DefaultBoundedRangeModel(0, 1, 0, 100);
/* 236 */       this._infoProgressBar = new JProgressBar(this._loadingModel);
/* 237 */       this._infoProgressBar.setOpaque(true);
/* 238 */       this._infoProgressBar.setVisible(true);
/*     */       
/* 240 */       jPanel3.add(this._infoStatus, "North");
/* 241 */       jPanel3.add(this._infoProgressTxt, "Center");
/* 242 */       jPanel3.add(this._infoEstimatedTime, "South");
/* 243 */       jPanel3.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
/*     */       
/* 245 */       this._infoProgressBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
/*     */ 
/*     */       
/* 248 */       jPanel6.add(this._infoProgressBar, "Center");
/* 249 */       jPanel6.add(this._cancelButton, "East");
/* 250 */       jPanel6.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       this._frame.pack();
/* 256 */       setIndeterminedProgressBar(true);
/*     */       
/* 258 */       Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
/* 259 */       int i = (dimension.width - this._frame.getWidth()) / 2;
/* 260 */       int j = (dimension.height - this._frame.getHeight()) / 2;
/* 261 */       this._frame.setLocation(i, j);
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 267 */       DeployUIManager.restoreLookAndFeel(lookAndFeel);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void showLoadingProgressScreen() {
/* 272 */     setStatus(ResourceManager.getString("launch.progressScreen"));
/*     */ 
/*     */     
/* 275 */     this._timerObject = new Timer(1000, this);
/* 276 */     this._timerObject.start();
/*     */   }
/*     */   
/*     */   public void setStatus(String paramString) {
/* 280 */     Runnable runnable = new Runnable(this, paramString) { private final String val$text;
/*     */         private final DownloadWindow this$0;
/*     */         
/*     */         public void run() {
/* 284 */           if (this.this$0._infoStatus != null) {
/* 285 */             this.this$0._infoStatus.setText((this.val$text == null) ? " " : this.val$text);
/*     */           }
/*     */         } }
/*     */       ;
/* 289 */     if (this._infoStatus != null && this._infoStatus.isShowing()) {
/* 290 */       SwingUtilities.invokeLater(runnable);
/*     */     } else {
/* 292 */       runnable.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setProgressText(String paramString) {
/* 297 */     SwingUtilities.invokeLater(new Runnable(this, paramString) { private final String val$text;
/*     */           private final DownloadWindow this$0;
/*     */           
/*     */           public void run() {
/* 301 */             if (this.this$0._infoProgressTxt != null) {
/* 302 */               this.this$0._infoProgressTxt.setText((this.val$text == null) ? " " : this.val$text);
/*     */             }
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProgressBarVisible(boolean paramBoolean) {
/* 313 */     SwingUtilities.invokeLater(new Runnable(this, paramBoolean) { private final boolean val$isVisible; private final DownloadWindow this$0;
/*     */           
/*     */           public void run() {
/* 316 */             if (this.this$0._infoProgressBar != null) {
/* 317 */               this.this$0._infoProgressBar.setVisible(this.val$isVisible);
/*     */             }
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   public void setProgressBarValue(int paramInt) {
/* 324 */     if (this._heartbeatOn) {
/* 325 */       setIndeterminedProgressBar(false);
/*     */     }
/* 327 */     if (this._loadingModel != null) {
/* 328 */       this._loadingModel.setValue(paramInt);
/*     */     }
/* 330 */     setProgressBarVisible((paramInt != 0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndeterminedProgressBar(boolean paramBoolean) {
/* 336 */     if (this._heartbeatTimer == null)
/*     */     {
/*     */ 
/*     */       
/* 340 */       this._heartbeatTimer = new Timer(250, new ActionListener(this) { private final DownloadWindow this$0;
/*     */             public void actionPerformed(ActionEvent param1ActionEvent) {
/* 342 */               synchronized (this.this$0._heartbeatLock) {
/* 343 */                 if (this.this$0._heartbeatOn && this.this$0._heartbeatTimer != null) {
/* 344 */                   this.this$0._heartbeatCount = (this.this$0._heartbeatCount + 1) % DownloadWindow.HEART_BEAT_RYTHM.length;
/* 345 */                   boolean bool = DownloadWindow.HEART_BEAT_RYTHM[this.this$0._heartbeatCount];
/* 346 */                   if (bool) {
/* 347 */                     this.this$0._loadingModel.setValue(100);
/*     */                   } else {
/* 349 */                     this.this$0._loadingModel.setValue(0);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } }
/*     */         );
/*     */     }
/* 356 */     synchronized (this._heartbeatLock) {
/* 357 */       if (paramBoolean) {
/* 358 */         setProgressBarVisible(true);
/* 359 */         this._loadingModel.setValue(0);
/* 360 */         this._heartbeatTimer.start();
/* 361 */         this._heartbeatOn = true;
/*     */       } else {
/* 363 */         setProgressBarVisible(false);
/* 364 */         this._heartbeatTimer.stop();
/* 365 */         this._heartbeatOn = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void showLaunchingApplication(String paramString) {
/* 371 */     SwingUtilities.invokeLater(new Runnable(this, paramString) { private final String val$title;
/*     */           private final DownloadWindow this$0;
/*     */           
/*     */           public void run() {
/* 375 */             if (this.this$0._loadingModel != null) {
/* 376 */               this.this$0._infoStatus.setText(this.val$title);
/* 377 */               this.this$0._infoProgressTxt.setText(" ");
/* 378 */               this.this$0._infoEstimatedTime.setText(" ");
/* 379 */               this.this$0._loadingModel.setValue(0);
/*     */             } 
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   private void setEstimatedTime(String paramString) {
/* 386 */     SwingUtilities.invokeLater(new Runnable(this, paramString) { private final String val$title;
/*     */           private final DownloadWindow this$0;
/*     */           
/*     */           public void run() {
/* 390 */             if (this.this$0._infoEstimatedTime != null) {
/* 391 */               this.this$0._infoEstimatedTime.setText((this.val$title == null) ? " " : this.val$title);
/*     */             }
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearWindow() {
/* 402 */     if (SwingUtilities.isEventDispatchThread()) {
/* 403 */       clearWindowHelper();
/*     */     } else {
/*     */       try {
/* 406 */         SwingUtilities.invokeAndWait(new Runnable(this) { private final DownloadWindow this$0;
/*     */               
/* 408 */               public void run() { this.this$0.clearWindowHelper(); } }
/*     */           );
/* 410 */       } catch (Exception exception) {
/* 411 */         Trace.ignoredException(exception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clearWindowHelper() {
/* 417 */     if (this._timerObject != null) {
/* 418 */       this._timerObject.stop();
/* 419 */       this._timerObject = null;
/* 420 */       this._timerDownloadAverage = null;
/*     */     } 
/* 422 */     if (this._heartbeatTimer != null) {
/* 423 */       synchronized (this._heartbeatLock) {
/* 424 */         this._heartbeatTimer.stop();
/* 425 */         this._heartbeatTimer = null;
/*     */       } 
/*     */     }
/* 428 */     if (this._frame != null) {
/* 429 */       this._infoStatus = null;
/* 430 */       this._infoProgressTxt = null;
/* 431 */       this._infoProgressBar = null;
/* 432 */       this._loadingModel = null;
/* 433 */       this._infoEstimatedTime = null;
/* 434 */       this._cancelButton.removeActionListener(this._cancelActionListener);
/* 435 */       this._cancelButton = null;
/* 436 */       this._cancelActionListener = null;
/* 437 */       this._frame.getContentPane().removeAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disposeWindow() {
/* 444 */     if (this._frame != null) {
/* 445 */       clearWindow();
/* 446 */       this._frame.removeWindowListener(this);
/* 447 */       this._frame.setVisible(false);
/* 448 */       this._frame.dispose();
/* 449 */       this._frame = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 455 */     setStatus(null);
/* 456 */     setProgressText(null);
/* 457 */     setProgressBarVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent paramActionEvent) {
/* 463 */     if (!this._timerOn)
/* 464 */       return;  if (this._estimatedDownloadSize <= 0L) {
/*     */       return;
/*     */     }
/* 467 */     long l = this._totalDownloadedBytes - this._timerLastBytesCount;
/* 468 */     this._timerLastBytesCount = this._totalDownloadedBytes;
/* 469 */     this._timerDownloadAverage[this._timerCount % 10] = l;
/*     */ 
/*     */     
/* 472 */     if (this._totalDownloadedBytes > this._estimatedDownloadSize) this._estimatedDownloadSize = this._totalDownloadedBytes;
/*     */ 
/*     */     
/* 475 */     if (this._timerCount > 10) {
/*     */       
/* 477 */       float f = 0.0F; int i;
/* 478 */       for (i = 0; i < 10; ) { f += (float)this._timerDownloadAverage[i]; i++; }
/* 479 */        f /= 10.0F;
/* 480 */       f /= 1.0F;
/*     */       
/* 482 */       if (f == 0.0F) {
/*     */         
/* 484 */         setEstimatedTime(ResourceManager.getString("launch.stalledDownload"));
/* 485 */       } else if (this._estimatedDownloadSize > 0L) {
/* 486 */         i = (int)((float)(this._estimatedDownloadSize - this._totalDownloadedBytes) / f);
/* 487 */         int j = i / 3600;
/* 488 */         i -= j * 3600;
/* 489 */         int k = i / 60;
/* 490 */         i -= k * 60;
/* 491 */         int m = i;
/*     */         
/* 493 */         setEstimatedTime(ResourceManager.getString("launch.estimatedTimeLeft", j, k, m));
/*     */       } 
/*     */     } 
/* 496 */     this._timerCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetDownloadTimer() {
/* 501 */     this._timerCount = 0;
/* 502 */     this._timerLastBytesCount = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void progress(URL paramURL, String paramString, long paramLong1, long paramLong2, int paramInt) {
/* 509 */     this._timerOn = true;
/* 510 */     this._totalDownloadedBytes = Math.max(0L, paramLong1);
/* 511 */     this._estimatedDownloadSize = paramLong2;
/*     */ 
/*     */     
/* 514 */     if (paramURL != this._currentUrl && paramURL != null) {
/*     */       
/* 516 */       String str1 = paramURL.getHost();
/* 517 */       String str2 = paramURL.getFile();
/* 518 */       int i = str2.lastIndexOf('/');
/* 519 */       if (i != -1) str2 = str2.substring(i + 1);
/*     */       
/* 521 */       if (str2.length() + str1.length() > 40) {
/* 522 */         str2 = maxDisplay(str2);
/* 523 */         str1 = maxDisplay(str1);
/*     */       } 
/*     */       
/* 526 */       setStatus(ResourceManager.getString("launch.loadingNetStatus", str2, str1));
/* 527 */       this._currentUrl = paramURL;
/*     */     } 
/*     */     
/* 530 */     if (paramLong2 == -1L) {
/* 531 */       setProgressText(ResourceManager.getString("launch.loadingNetProgress", bytesToString(this._totalDownloadedBytes)));
/*     */     } else {
/*     */       
/* 534 */       setProgressText(ResourceManager.getString("launch.loadingNetProgressPercent", bytesToString(this._totalDownloadedBytes), bytesToString(paramLong2), (new Long(Math.max(0, paramInt))).toString()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 539 */       setProgressBarValue(paramInt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void patching(URL paramURL, String paramString, int paramInt1, int paramInt2) {
/* 549 */     this._timerOn = false;
/* 550 */     setEstimatedTime(null);
/*     */     
/* 552 */     if (this._currentUrl != paramURL || paramInt1 == 0) {
/* 553 */       String str1 = paramURL.getHost();
/* 554 */       String str2 = paramURL.getFile();
/* 555 */       int i = str2.lastIndexOf('/');
/* 556 */       if (i != -1) str2 = str2.substring(i + 1);
/*     */       
/* 558 */       if (str2.length() + str1.length() > 40) {
/* 559 */         str2 = maxDisplay(str2);
/* 560 */         str1 = maxDisplay(str1);
/*     */       } 
/*     */       
/* 563 */       setStatus(ResourceManager.getString("launch.patchingStatus", str2, str1));
/*     */       
/* 565 */       this._currentUrl = paramURL;
/*     */     } 
/* 567 */     setProgressText(null);
/* 568 */     setProgressBarValue(paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String maxDisplay(String paramString) {
/* 575 */     int i = paramString.length();
/*     */     
/* 577 */     if (i > 20)
/*     */     {
/* 579 */       paramString = "..." + paramString.substring(i - 20 - "...".length(), i);
/*     */     }
/*     */     
/* 582 */     return paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validating(URL paramURL, String paramString, long paramLong1, long paramLong2, int paramInt) {
/* 592 */     this._timerOn = false;
/* 593 */     setEstimatedTime(null);
/*     */ 
/*     */     
/* 596 */     long l = (paramLong2 == 0L) ? 0L : (paramLong1 * 100L / paramLong2);
/*     */     
/* 598 */     if (this._currentUrl != paramURL || paramLong1 == 0L) {
/* 599 */       String str1 = paramURL.getHost();
/* 600 */       String str2 = paramURL.getFile();
/* 601 */       int i = str2.lastIndexOf('/');
/* 602 */       if (i != -1) str2 = str2.substring(i + 1);
/*     */       
/* 604 */       if (str2.length() + str1.length() > 40) {
/* 605 */         str2 = maxDisplay(str2);
/* 606 */         str1 = maxDisplay(str1);
/*     */       } 
/*     */       
/* 609 */       setStatus(ResourceManager.getString("launch.validatingStatus", str2, str1));
/* 610 */       this._currentUrl = paramURL;
/*     */     } 
/* 612 */     if (paramLong1 != 0L) {
/* 613 */       setProgressText(ResourceManager.getString("launch.validatingProgress", (int)l));
/*     */     } else {
/* 615 */       setProgressText(null);
/*     */     } 
/* 617 */     setProgressBarValue(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void downloadFailed(URL paramURL, String paramString) {
/* 627 */     this._timerOn = false;
/* 628 */     setEstimatedTime(null);
/*     */     
/* 630 */     setStatus(ResourceManager.getString("launch.loadingResourceFailedSts", paramURL.toString()));
/* 631 */     setProgressText(ResourceManager.getString("launch.loadingResourceFailed"));
/* 632 */     setProgressBarVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void extensionDownload(String paramString, int paramInt) {
/* 640 */     this._timerOn = false;
/* 641 */     setEstimatedTime(null);
/*     */     
/* 643 */     if (paramString != null) {
/* 644 */       setStatus(ResourceManager.getString("launch.extensiondownload-name", paramString, paramInt));
/*     */     } else {
/* 646 */       setStatus(ResourceManager.getString("launch.extensiondownload", paramString, paramInt));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void jreDownload(String paramString, URL paramURL) {
/* 655 */     this._timerOn = false;
/* 656 */     setEstimatedTime(null);
/*     */ 
/*     */     
/* 659 */     String str = paramURL.getHost();
/*     */     
/* 661 */     str = maxDisplay(str);
/*     */     
/* 663 */     setStatus(ResourceManager.getString("launch.downloadingJRE", paramString, str));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadingFromNet(URL paramURL, int paramInt1, int paramInt2) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setAppImage(Image paramImage) {
/* 680 */     updateImage(paramImage, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateImage(Image paramImage, boolean paramBoolean) {
/* 686 */     if (paramImage != null) {
/* 687 */       int i = paramImage.getWidth(null);
/* 688 */       int j = paramImage.getHeight(null);
/* 689 */       if (i > 64 || j > 64) {
/*     */         
/* 691 */         int k = 64;
/*     */ 
/*     */         
/* 694 */         if (j > i && j < 2 * i) {
/* 695 */           k = 64 * i / j;
/*     */         }
/*     */         
/* 698 */         BufferedImage bufferedImage = new BufferedImage(64, 64, 1);
/*     */         
/* 700 */         if (!Globals.isHeadless()) {
/*     */           
/* 702 */           Graphics graphics = bufferedImage.getGraphics();
/*     */           
/*     */           try {
/* 705 */             if (this._imageLabel != null) {
/* 706 */               graphics.setColor(this._imageLabel.getBackground());
/* 707 */               graphics.fillRect(0, 0, 64, 64);
/*     */             } 
/* 709 */             graphics.drawImage(paramImage, (64 - k) / 2, 0, k, 64, null);
/*     */           } finally {
/*     */             
/* 712 */             graphics.dispose();
/*     */           } 
/*     */         } 
/* 715 */         paramImage = bufferedImage;
/* 716 */       } else if (i < 64 || j < 64) {
/* 717 */         BufferedImage bufferedImage = new BufferedImage(64, 64, 1);
/*     */         
/* 719 */         Graphics graphics = bufferedImage.getGraphics();
/*     */         
/*     */         try {
/* 722 */           if (this._imageLabel != null) {
/* 723 */             graphics.setColor(this._imageLabel.getBackground());
/* 724 */             graphics.fillRect(0, 0, 64, 64);
/*     */           } 
/* 726 */           graphics.drawImage(paramImage, (64 - i) / 2, (64 - j) / 2, i, j, null);
/*     */         } finally {
/*     */           
/* 729 */           graphics.dispose();
/*     */         } 
/* 731 */         paramImage = bufferedImage;
/*     */       } 
/*     */     } 
/* 734 */     synchronized (this) {
/* 735 */       if (this._appImage == null || paramBoolean) {
/* 736 */         this._appImage = paramImage;
/*     */       }
/*     */     } 
/* 739 */     if (this._imageLabel != null) {
/* 740 */       if (this._appImage != null) {
/* 741 */         this._imageLabel.setIcon(new ImageIcon(this._appImage));
/*     */       }
/* 743 */       this._imageLabel.repaint();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String bytesToString(long paramLong) {
/* 753 */     String str = "";
/* 754 */     double d = paramLong;
/* 755 */     boolean bool = false;
/* 756 */     if (paramLong > 1073741824L) {
/* 757 */       d /= 1.073741824E9D;
/* 758 */       str = "G";
/* 759 */       bool = true;
/* 760 */     } else if (paramLong > 1048576L) {
/* 761 */       d /= 1048576.0D;
/* 762 */       str = "M";
/* 763 */       bool = true;
/* 764 */     } else if (paramLong > 1024L) {
/* 765 */       d /= 1024.0D;
/* 766 */       str = "K";
/* 767 */       bool = false;
/*     */     } 
/* 769 */     return ResourceManager.formatDouble(d, bool) + str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowClosing(WindowEvent paramWindowEvent) {
/* 777 */     cancelAction();
/*     */   }
/*     */   
/*     */   private void cancelAction() {
/* 781 */     if (this._exitOnCancel) {
/*     */       
/* 783 */       AccessController.doPrivileged(new PrivilegedAction(this) { private final DownloadWindow this$0;
/*     */             public Object run() {
/* 785 */               Main.systemExit(-1);
/* 786 */               return null;
/*     */             } }
/*     */         );
/*     */     } else {
/* 790 */       this._isCanceled = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCanceled() {
/* 795 */     return this._isCanceled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetCancled() {
/* 800 */     this._isCanceled = false;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean paramBoolean) {
/* 804 */     JFrame jFrame = this._frame;
/* 805 */     if (jFrame != null) SwingUtilities.invokeLater(new Runnable(this, jFrame, paramBoolean) { private final Frame val$f;
/*     */             public void run() {
/* 807 */               this.val$f.setVisible(this.val$show);
/*     */             }
/*     */             private final boolean val$show; private final DownloadWindow this$0; }
/*     */         ); 
/*     */   }
/*     */   public DownloadWindow() {}
/*     */   class BLabel extends JLabel { int _w; int _h;
/*     */     private final DownloadWindow this$0;
/*     */     
/*     */     public BLabel(DownloadWindow this$0) {
/* 817 */       this.this$0 = this$0;
/* 818 */       this._w = 0;
/* 819 */       this._h = 0;
/* 820 */       setOpaque(true);
/* 821 */       setForeground(UIManager.getColor("textText"));
/*     */     }
/*     */     
/*     */     public BLabel(DownloadWindow this$0, String param1String, int param1Int1, int param1Int2) {
/* 825 */       super(param1String); this.this$0 = this$0;
/* 826 */       this._w = param1Int1;
/* 827 */       this._h = param1Int2;
/* 828 */       setOpaque(true);
/* 829 */       setForeground(UIManager.getColor("textText"));
/*     */     }
/*     */     
/*     */     public Dimension getPreferredSize() {
/* 833 */       Dimension dimension = super.getPreferredSize();
/* 834 */       if (this._w > dimension.width) dimension.width = this._w; 
/* 835 */       if (this._h > dimension.height) dimension.height = this._h; 
/* 836 */       return dimension;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\ui\DownloadWindow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */