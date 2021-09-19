/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.DeployUIManager;
/*     */ import com.sun.deploy.util.DialogFactory;
/*     */ import com.sun.javaws.Main;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.LookAndFeel;
/*     */ 
/*     */ final class SmartSecurityDialog
/*     */   extends Thread
/*     */ {
/*     */   private boolean _remembered = false;
/*  27 */   private int _lastResult = -1;
/*     */   private boolean _cbChecked = false;
/*     */   private int _answer;
/*  30 */   private Object _signalObject = null;
/*  31 */   private String _message = null;
/*     */   private DummyDialog _dummyDialog;
/*  33 */   private EventQueue _sysEventQueue = null;
/*  34 */   private static final ThreadGroup _secureGroup = Main.getSecurityThreadGroup();
/*     */   
/*  36 */   private Object[] _objs = null;
/*     */ 
/*     */ 
/*     */   
/*     */   SmartSecurityDialog() {
/*  41 */     this(null);
/*     */   }
/*     */   
/*     */   SmartSecurityDialog(String paramString) {
/*  45 */     this._signalObject = new Object();
/*  46 */     this._message = paramString;
/*     */   }
/*     */   
/*     */   SmartSecurityDialog(String paramString, boolean paramBoolean) {
/*  50 */     this(paramString);
/*  51 */     this._cbChecked = paramBoolean;
/*     */   }
/*     */   
/*     */   boolean showDialog(Object[] paramArrayOfObject) {
/*  55 */     this._objs = paramArrayOfObject;
/*  56 */     return showDialog();
/*     */   }
/*     */   
/*     */   boolean showDialog(String paramString) {
/*  60 */     this._message = paramString;
/*  61 */     this._objs = null;
/*  62 */     return showDialog();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean showDialog() {
/*  67 */     Integer integer = AccessController.<Integer>doPrivileged(new PrivilegedAction(this)
/*     */         {
/*     */           public Object run() {
/*  70 */             return new Integer(this.this$0.getUserDecision(null, this.this$0._message));
/*     */           } private final SmartSecurityDialog this$0;
/*     */         });
/*  73 */     return (integer.intValue() == 0);
/*     */   }
/*     */   
/*     */   private int getUserDecision(Frame paramFrame, String paramString) {
/*  77 */     if (this._remembered) {
/*  78 */       this._answer = this._lastResult;
/*  79 */       return this._answer;
/*     */     } 
/*  81 */     if (!Config.getBooleanProperty("deployment.security.sandbox.jnlp.enhanced")) {
/*  82 */       return 1;
/*     */     }
/*     */     
/*  85 */     synchronized (this._signalObject) {
/*     */       
/*  87 */       this._sysEventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
/*     */       
/*  89 */       Thread thread = new Thread(_secureGroup, this, "userDialog");
/*     */       
/*  91 */       this._message = paramString;
/*     */       
/*  93 */       this._dummyDialog = new DummyDialog(this, (Frame)null, true);
/*  94 */       this._dummyDialog.addWindowListener(new WindowAdapter(this, thread)
/*     */           {
/*     */             private final Thread val$handler;
/*     */             private final SmartSecurityDialog this$0;
/*     */             
/*     */             public void windowOpened(WindowEvent param1WindowEvent) {
/* 100 */               this.val$handler.start();
/*     */             }
/*     */             public void windowClosing(WindowEvent param1WindowEvent) {
/* 103 */               this.this$0._dummyDialog.hide();
/*     */             }
/*     */           });
/* 106 */       Rectangle rectangle = new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize());
/*     */ 
/*     */       
/* 109 */       this._dummyDialog.setLocation(rectangle.x + rectangle.width / 2 - 50, rectangle.y + rectangle.height / 2);
/*     */ 
/*     */       
/* 112 */       if (Config.getOSName().equals("Windows")) {
/* 113 */         this._dummyDialog.setLocation(-200, -200);
/*     */       }
/*     */       
/* 116 */       this._dummyDialog.setResizable(false);
/* 117 */       this._dummyDialog.toBack();
/* 118 */       this._dummyDialog.show();
/*     */       
/*     */       try {
/* 121 */         this._signalObject.wait();
/* 122 */       } catch (Exception exception) {
/* 123 */         exception.printStackTrace();
/* 124 */         this._dummyDialog.hide();
/*     */       } 
/* 126 */       return this._answer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 133 */     LookAndFeel lookAndFeel = DeployUIManager.setLookAndFeel();
/*     */     try {
/*     */       Object[] arrayOfObject1;
/* 136 */       JPanel jPanel = new JPanel();
/* 137 */       JCheckBox jCheckBox = new JCheckBox(ResourceManager.getString("APIImpl.securityDialog.remember"), this._cbChecked);
/* 138 */       Font font1 = jCheckBox.getFont();
/* 139 */       Font font2 = null;
/* 140 */       if (font1 != null) {
/* 141 */         font2 = font1.deriveFont(0);
/* 142 */         if (font2 != null) {
/* 143 */           jCheckBox.setFont(font2);
/*     */         }
/*     */       } 
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
/* 160 */       jCheckBox.addItemListener(new csiCheckBoxListener(this, jCheckBox));
/* 161 */       jPanel.add(jCheckBox, "Center");
/* 162 */       jCheckBox.setOpaque(false);
/* 163 */       jPanel.setOpaque(false);
/*     */ 
/*     */ 
/*     */       
/* 167 */       if (this._objs == null) {
/* 168 */         arrayOfObject1 = new Object[] { this._message, jPanel };
/*     */       } else {
/* 170 */         arrayOfObject1 = this._objs;
/*     */       } 
/*     */       
/* 173 */       Object[] arrayOfObject2 = { ResourceManager.getString("APIImpl.securityDialog.yes"), ResourceManager.getString("APIImpl.securityDialog.no") };
/*     */ 
/*     */       
/* 176 */       boolean bool = true;
/*     */       try {
/* 178 */         int i = DialogFactory.showOptionDialog(4, arrayOfObject1, ResourceManager.getString("APIImpl.securityDialog.title"), arrayOfObject2, arrayOfObject2[0]);
/*     */ 
/*     */ 
/*     */         
/* 182 */         bool = (i == 0) ? false : true;
/*     */         
/* 184 */         if (this._cbChecked) {
/* 185 */           this._remembered = true;
/* 186 */           this._lastResult = bool;
/*     */         } 
/*     */       } finally {
/* 189 */         this._dummyDialog.secureHide();
/*     */         
/* 191 */         synchronized (this._signalObject) {
/* 192 */           this._answer = bool;
/* 193 */           this._signalObject.notify();
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 198 */       DeployUIManager.restoreLookAndFeel(lookAndFeel);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setCBChecked(boolean paramBoolean) {
/* 203 */     this._cbChecked = paramBoolean;
/*     */   }
/*     */   
/*     */   private class DummyDialog extends JDialog { private ThreadGroup _unsecureGroup;
/*     */     private final SmartSecurityDialog this$0;
/*     */     
/*     */     DummyDialog(SmartSecurityDialog this$0, Frame param1Frame, boolean param1Boolean) {
/* 210 */       super(param1Frame, param1Boolean); this.this$0 = this$0;
/* 211 */       this._unsecureGroup = Thread.currentThread().getThreadGroup();
/*     */     }
/*     */     
/*     */     public void secureHide() {
/* 215 */       (new Thread(this._unsecureGroup, new Runnable(this) { public void run() {
/* 216 */               this.this$1.hide();
/*     */             }
/*     */             
/*     */             private final SmartSecurityDialog.DummyDialog this$1; }
/*     */         )).start();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\SmartSecurityDialog.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */