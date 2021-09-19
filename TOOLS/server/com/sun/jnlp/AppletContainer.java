/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.javaws.Main;
/*     */ import java.applet.Applet;
/*     */ import java.applet.AppletContext;
/*     */ import java.applet.AppletStub;
/*     */ import java.applet.AudioClip;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.EtchedBorder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AppletContainer
/*     */   extends JPanel
/*     */ {
/*     */   final AppletContainerCallback callback;
/*     */   final Applet applet;
/*     */   final String appletName;
/*     */   final URL documentBase;
/*     */   final URL codeBase;
/*     */   final Properties parameters;
/* 215 */   final boolean[] isActive = new boolean[] { false };
/*     */ 
/*     */   
/*     */   int appletWidth;
/*     */   
/*     */   int appletHeight;
/*     */   
/* 222 */   final JLabel statusLabel = new JLabel("");
/*     */   static class LoadImageAction implements PrivilegedAction {
/*     */     URL _url;
/*     */     
/* 226 */     public LoadImageAction(URL param1URL) { this._url = param1URL; } public Object run() {
/* 227 */       return ImageCache.getImage(this._url);
/*     */     }
/*     */   }
/* 230 */   static PrivilegedAction loadImageActionDummy = new LoadImageAction(null);
/*     */   class AppletContainerContext implements AppletContext { private final AppletContainer this$0;
/*     */     AppletContainerContext(AppletContainer this$0) {
/* 233 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public Applet getApplet(String param1String) {
/* 238 */       return param1String.equals(this.this$0.appletName) ? this.this$0.applet : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration getApplets() {
/* 243 */       Vector vector = new Vector();
/* 244 */       vector.add(this.this$0.applet);
/* 245 */       return vector.elements();
/*     */     }
/*     */     
/*     */     public AudioClip getAudioClip(URL param1URL) {
/* 249 */       return AppletAudioClip.get(param1URL);
/*     */     }
/*     */ 
/*     */     
/*     */     public Image getImage(URL param1URL) {
/* 254 */       AppletContainer.LoadImageAction loadImageAction = new AppletContainer.LoadImageAction(param1URL);
/* 255 */       return AccessController.<Image>doPrivileged(loadImageAction);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void showDocument(URL param1URL) {
/* 261 */       AccessController.doPrivileged(new PrivilegedAction(this, param1URL) { private final URL val$url; private final AppletContainer.AppletContainerContext this$1;
/*     */             public Object run() {
/* 263 */               Thread thread = new Thread(this) { private final AppletContainer.AppletContainerContext.null this$2;
/*     */                   public void run() {
/* 265 */                     this.this$2.this$1.this$0.callback.showDocument(this.this$2.val$url);
/*     */                   } }
/*     */                 ;
/* 268 */               thread.start();
/* 269 */               return null;
/*     */             } }
/*     */         );
/*     */     }
/*     */     
/*     */     public void showDocument(URL param1URL, String param1String) {
/* 275 */       showDocument(param1URL);
/*     */     }
/*     */     
/*     */     public void showStatus(String param1String) {
/* 279 */       this.this$0.statusLabel.setText(param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setStream(String param1String, InputStream param1InputStream) {}
/*     */ 
/*     */     
/*     */     public InputStream getStream(String param1String) {
/* 288 */       return null;
/*     */     }
/*     */     
/*     */     public Iterator getStreamKeys() {
/* 292 */       return null;
/*     */     } }
/*     */ 
/*     */   
/*     */   final class AppletContainerStub
/*     */     implements AppletStub
/*     */   {
/*     */     AppletContext context;
/*     */     private final AppletContainer this$0;
/*     */     
/*     */     AppletContainerStub(AppletContainer this$0, AppletContext param1AppletContext) {
/* 303 */       this.this$0 = this$0;
/* 304 */       this.context = param1AppletContext;
/*     */     }
/*     */     
/*     */     public void appletResize(int param1Int1, int param1Int2) {
/* 308 */       this.this$0.resizeApplet(param1Int1, param1Int2);
/*     */     }
/*     */     
/*     */     public AppletContext getAppletContext() {
/* 312 */       return this.context;
/*     */     }
/*     */     
/*     */     public URL getCodeBase() {
/* 316 */       return this.this$0.codeBase;
/*     */     }
/*     */     
/*     */     public URL getDocumentBase() {
/* 320 */       return this.this$0.documentBase;
/*     */     }
/*     */     
/*     */     public String getParameter(String param1String) {
/* 324 */       return this.this$0.parameters.getProperty(param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isActive() {
/* 330 */       return this.this$0.isActive[0];
/*     */     }
/*     */   }
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
/*     */   public AppletContainer(AppletContainerCallback paramAppletContainerCallback, Applet paramApplet, String paramString, URL paramURL1, URL paramURL2, int paramInt1, int paramInt2, Properties paramProperties) {
/* 349 */     this.callback = paramAppletContainerCallback;
/* 350 */     this.applet = paramApplet;
/* 351 */     this.appletName = paramString;
/* 352 */     this.documentBase = paramURL1;
/* 353 */     this.codeBase = paramURL2;
/* 354 */     this.parameters = paramProperties;
/* 355 */     this.isActive[0] = false;
/* 356 */     this.appletWidth = paramInt1;
/* 357 */     this.appletHeight = paramInt2;
/*     */ 
/*     */     
/* 360 */     AppletContainerContext appletContainerContext = new AppletContainerContext(this);
/* 361 */     AppletContainerStub appletContainerStub = new AppletContainerStub(this, appletContainerContext);
/* 362 */     paramApplet.setStub(appletContainerStub);
/*     */ 
/*     */     
/* 365 */     this.statusLabel.setBorder(new EtchedBorder());
/* 366 */     this.statusLabel.setText("Loading...");
/* 367 */     setLayout(new BorderLayout());
/* 368 */     add("Center", paramApplet);
/* 369 */     add("South", this.statusLabel);
/*     */     
/* 371 */     Dimension dimension = new Dimension(this.appletWidth, this.appletHeight + (int)this.statusLabel.getPreferredSize().getHeight());
/*     */     
/* 373 */     setPreferredSize(dimension);
/*     */   }
/*     */   
/*     */   public Applet getApplet() {
/* 377 */     return this.applet;
/*     */   }
/*     */   public void setStatus(String paramString) {
/* 380 */     this.statusLabel.setText(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resizeApplet(int paramInt1, int paramInt2) {
/* 386 */     if (paramInt1 < 0 || paramInt2 < 0) {
/*     */       return;
/*     */     }
/* 389 */     int i = paramInt1 - this.appletWidth;
/* 390 */     int j = paramInt2 - this.appletHeight;
/*     */ 
/*     */     
/* 393 */     Dimension dimension1 = getSize();
/* 394 */     Dimension dimension2 = new Dimension((int)dimension1.getWidth() + i, (int)dimension1.getHeight() + j);
/*     */     
/* 396 */     setSize(dimension2);
/*     */ 
/*     */     
/* 399 */     this.callback.relativeResize(new Dimension(i, j));
/*     */ 
/*     */     
/* 402 */     this.appletWidth = paramInt1;
/* 403 */     this.appletHeight = paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredFrameSize(Frame paramFrame) {
/* 410 */     Insets insets = paramFrame.getInsets();
/* 411 */     int i = this.appletWidth + insets.left + insets.right;
/* 412 */     int j = this.appletHeight + this.statusLabel.getHeight() + insets.top + insets.bottom;
/* 413 */     return new Dimension(i, j);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startApplet() {
/* 419 */     ImageCache.initialize();
/* 420 */     new AppletAudioClip();
/*     */ 
/*     */ 
/*     */     
/* 424 */     (new Thread(this) { private final AppletContainer this$0;
/*     */         public void run() {
/*     */           try {
/* 427 */             this.this$0.setStatus("Initializing Applet");
/* 428 */             this.this$0.applet.init();
/*     */ 
/*     */             
/*     */             try {
/* 432 */               this.this$0.isActive[0] = true;
/* 433 */               this.this$0.applet.start();
/* 434 */               this.this$0.setStatus("Applet running...");
/* 435 */             } catch (Throwable throwable) {
/* 436 */               this.this$0.setStatus("Failed to start Applet: " + throwable.toString());
/*     */               
/* 438 */               throwable.printStackTrace(System.out);
/* 439 */               this.this$0.isActive[0] = false;
/*     */             } 
/* 441 */           } catch (Throwable throwable) {
/* 442 */             this.this$0.setStatus("Failed to initialize: " + throwable.toString());
/*     */             
/* 444 */             throwable.printStackTrace(System.out);
/*     */           } 
/*     */         } }
/*     */       ).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopApplet() {
/* 457 */     this.applet.stop();
/* 458 */     this.applet.destroy();
/* 459 */     Main.systemExit(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void showApplet(AppletContainerCallback paramAppletContainerCallback, Applet paramApplet, String paramString, URL paramURL1, URL paramURL2, int paramInt1, int paramInt2, Properties paramProperties) {
/* 471 */     JFrame jFrame = new JFrame("Applet Window");
/*     */     
/* 473 */     AppletContainer appletContainer = new AppletContainer(paramAppletContainerCallback, paramApplet, paramString, paramURL1, paramURL2, paramInt1, paramInt2, paramProperties);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 483 */     jFrame.getContentPane().setLayout(new BorderLayout());
/* 484 */     jFrame.getContentPane().add("Center", appletContainer);
/* 485 */     jFrame.pack();
/* 486 */     jFrame.show();
/*     */     
/* 488 */     SwingUtilities.invokeLater(new Runnable(appletContainer, paramApplet) { private final AppletContainer val$container; private final Applet val$applet;
/*     */           public void run() {
/*     */             try {
/* 491 */               this.val$container.setStatus("Initializing Applet");
/* 492 */               this.val$applet.init();
/* 493 */               this.val$applet.start();
/* 494 */               this.val$container.setStatus("Applet Running");
/* 495 */             } catch (Throwable throwable) {
/* 496 */               this.val$container.setStatus("Failed to start Applet");
/*     */             } 
/*     */           } }
/*     */       );
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\AppletContainer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */