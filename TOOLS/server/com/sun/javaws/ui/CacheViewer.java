/*      */ package com.sun.javaws.ui;
/*      */ 
/*      */ import com.sun.deploy.config.Config;
/*      */ import com.sun.deploy.resources.ResourceManager;
/*      */ import com.sun.deploy.si.DeploySIListener;
/*      */ import com.sun.deploy.si.SingleInstanceImpl;
/*      */ import com.sun.deploy.si.SingleInstanceManager;
/*      */ import com.sun.deploy.util.AboutDialog;
/*      */ import com.sun.deploy.util.DeployUIManager;
/*      */ import com.sun.deploy.util.DialogFactory;
/*      */ import com.sun.deploy.util.Trace;
/*      */ import com.sun.javaws.BrowserSupport;
/*      */ import com.sun.javaws.Globals;
/*      */ import com.sun.javaws.LocalApplicationProperties;
/*      */ import com.sun.javaws.LocalInstallHandler;
/*      */ import com.sun.javaws.Main;
/*      */ import com.sun.javaws.SplashScreen;
/*      */ import com.sun.javaws.cache.Cache;
/*      */ import com.sun.javaws.jnl.InformationDesc;
/*      */ import com.sun.javaws.jnl.LaunchDesc;
/*      */ import com.sun.javaws.util.JavawsConsoleController;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.net.URL;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBoxMenuItem;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.CompoundBorder;
/*      */ import javax.swing.border.TitledBorder;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.table.AbstractTableModel;
/*      */ 
/*      */ public class CacheViewer
/*      */   extends JFrame
/*      */   implements ActionListener, ChangeListener, ListSelectionListener, DeploySIListener
/*      */ {
/*      */   private final JButton _removeBtn;
/*      */   private final JButton _launchOnlineBtn;
/*      */   private final JButton _launchOfflineBtn;
/*      */   private final JTabbedPane _tabbedPane;
/*      */   private final CacheTable _userTable;
/*      */   private final CacheTable _sysTable;
/*      */   private final JScrollPane _userTab;
/*      */   private final JScrollPane _systemTab;
/*      */   private static final String BOUNDS_PROPERTY_KEY = "deployment.javaws.viewer.bounds";
/*      */   private JMenuItem _launchOnlineMI;
/*      */   private JMenuItem _launchOfflineMI;
/*      */   private JMenuItem _removeMI;
/*      */   private JMenuItem _showMI;
/*      */   private JMenuItem _installMI;
/*   82 */   private static int _status = 0; private JMenuItem _browseMI; private JMenu _fileMenu; private JMenu _editMenu; private JMenu _appMenu; private JMenu _viewMenu; private JMenu _helpMenu; private TitledBorder _titledBorder; public static final int STATUS_OK = 0; public static final int STATUS_REMOVING = 1; public static final int STATUS_LAUNCHING = 2; public static final int STATUS_BROWSING = 3; public static final int STATUS_SORTING = 4; public static final int STATUS_SEARCHING = 5; public static final int STATUS_INSTALLING = 6;
/*      */   private static JLabel _statusLabel;
/*   84 */   private static final JLabel _totalSize = new JLabel();
/*      */   
/*   86 */   private static final LocalInstallHandler _lih = LocalInstallHandler.getInstance();
/*      */   
/*   88 */   private static final boolean _isLocalInstallSupported = _lih.isLocalInstallSupported(); private static long t0; private static long t1;
/*      */   private static long t2;
/*      */   private static long t3;
/*      */   private static long t4;
/*      */   private SingleInstanceImpl _sil;
/*   93 */   private static final String JAVAWS_CV_ID = "JNLP Cache Viewer" + Config.getInstance().getSessionSpecificString();
/*      */   
/*      */   private static final int SLEEP_DELAY = 2000;
/*      */   
/*      */   static Class class$java$lang$String;
/*      */ 
/*      */   
/*      */   public CacheViewer() {
/*  101 */     this._sil = new SingleInstanceImpl();
/*  102 */     this._sil.addSingleInstanceListener(this, JAVAWS_CV_ID);
/*      */     
/*  104 */     this._removeBtn = makeButton("jnlp.viewer.remove.btn");
/*  105 */     this._launchOnlineBtn = makeButton("jnlp.viewer.launch.online.btn");
/*  106 */     this._launchOfflineBtn = makeButton("jnlp.viewer.launch.offline.btn");
/*      */     
/*  108 */     _statusLabel = new JLabel(" ");
/*      */     
/*  110 */     this._tabbedPane = new JTabbedPane();
/*  111 */     this._userTable = new CacheTable(this, false);
/*  112 */     this._sysTable = new CacheTable(this, true);
/*  113 */     this._userTab = new JScrollPane(this._userTable);
/*  114 */     this._systemTab = new JScrollPane(this._sysTable);
/*      */     
/*  116 */     initComponents();
/*      */   }
/*      */ 
/*      */   
/*      */   private void initComponents() {
/*  121 */     setTitle(ResourceManager.getMessage("jnlp.viewer.title"));
/*  122 */     addWindowListener(new WindowAdapter(this) { private final CacheViewer this$0;
/*      */           public void windowClosing(WindowEvent param1WindowEvent) {
/*  124 */             this.this$0.exitViewer();
/*      */           } }
/*      */       );
/*      */     
/*  128 */     JPanel jPanel1 = new JPanel();
/*  129 */     jPanel1.setLayout(new BorderLayout());
/*  130 */     this._titledBorder = new TitledBorder(ResourceManager.getMessage("jnlp.viewer.all"));
/*      */     
/*  132 */     Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
/*  133 */     CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(border, this._titledBorder);
/*      */     
/*  135 */     jPanel1.setBorder(BorderFactory.createCompoundBorder(compoundBorder, border));
/*      */     
/*  137 */     if (Globals.isSystemCache()) {
/*  138 */       this._tabbedPane.addTab(ResourceManager.getMessage("cert.dialog.system.level"), this._userTab);
/*      */     } else {
/*      */       
/*  141 */       this._tabbedPane.addTab(ResourceManager.getMessage("cert.dialog.user.level"), this._userTab);
/*      */       
/*  143 */       this._tabbedPane.addTab(ResourceManager.getMessage("cert.dialog.system.level"), this._systemTab);
/*      */     } 
/*      */ 
/*      */     
/*  147 */     this._tabbedPane.setSelectedIndex(0);
/*  148 */     this._tabbedPane.addChangeListener(this);
/*  149 */     jPanel1.add(this._tabbedPane, "Center");
/*      */     
/*  151 */     Box box = Box.createHorizontalBox();
/*  152 */     box.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
/*      */     
/*  154 */     box.add(this._removeBtn);
/*  155 */     box.add(Box.createHorizontalGlue());
/*  156 */     box.add(this._launchOnlineBtn);
/*  157 */     box.add(Box.createHorizontalStrut(5));
/*  158 */     box.add(this._launchOfflineBtn);
/*      */     
/*  160 */     jPanel1.add(box, "South");
/*      */     
/*  162 */     JPanel jPanel2 = new JPanel(new BorderLayout());
/*  163 */     _totalSize.setText(getAppMessage("jnlp.viewer.totalSize", ""));
/*  164 */     _totalSize.setHorizontalAlignment(0);
/*  165 */     _totalSize.setFont(ResourceManager.getUIFont());
/*      */     
/*  167 */     JPanel jPanel3 = new JPanel(new BorderLayout());
/*  168 */     _statusLabel = new JLabel(" ");
/*  169 */     _statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
/*  170 */     _statusLabel.setFont(ResourceManager.getUIFont());
/*      */     
/*  172 */     jPanel3.add(_statusLabel, "West");
/*  173 */     jPanel3.add(_totalSize, "Center");
/*  174 */     jPanel3.setBorder(BorderFactory.createEtchedBorder(1));
/*      */ 
/*      */     
/*  177 */     jPanel2.add(jPanel3, "South");
/*  178 */     jPanel2.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/*      */ 
/*      */     
/*  181 */     getContentPane().add(Box.createVerticalStrut(8), "North");
/*  182 */     getContentPane().add(jPanel1, "Center");
/*  183 */     getContentPane().add(jPanel2, "South");
/*      */     
/*  185 */     createMenuBar();
/*  186 */     pack();
/*      */     
/*  188 */     this._userTable.getSelectionModel().addListSelectionListener(this);
/*  189 */     this._sysTable.getSelectionModel().addListSelectionListener(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createMenuBar() {
/*  197 */     this._fileMenu = new JMenu(ResourceManager.getMessage("jnlp.viewer.file.menu"));
/*      */     
/*  199 */     this._fileMenu.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.file.menu.mnemonic"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  218 */     JMenuItem jMenuItem = this._fileMenu.add(ResourceManager.getMessage("jnlp.viewer.exit.mi"));
/*      */     
/*  220 */     jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.exit.mi.mnemonic"));
/*      */     
/*  222 */     jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  224 */             this.this$0.exitViewer();
/*      */           } }
/*      */       );
/*      */     
/*  228 */     this._editMenu = new JMenu(ResourceManager.getMessage("jnlp.viewer.edit.menu"));
/*      */     
/*  230 */     this._editMenu.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.edit.menu.mnemonic"));
/*      */ 
/*      */     
/*  233 */     jMenuItem = this._editMenu.add(ResourceManager.getMessage("jnlp.viewer.reinstall.mi"));
/*      */     
/*  235 */     jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.reinstall.mi.mnemonic"));
/*      */     
/*  237 */     jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  239 */             this.this$0.showReInstallDialog();
/*      */           } }
/*      */       );
/*      */     
/*  243 */     this._editMenu.addSeparator();
/*      */     
/*  245 */     jMenuItem = this._editMenu.add(ResourceManager.getMessage("jnlp.viewer.preferences.mi"));
/*      */     
/*  247 */     jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.preferences.mi.mnemonic"));
/*      */     
/*  249 */     jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  251 */             Main.launchJavaControlPanel("general");
/*      */           } }
/*      */       );
/*      */     
/*  255 */     this._appMenu = new JMenu(ResourceManager.getMessage("jnlp.viewer.app.menu"));
/*      */     
/*  257 */     this._appMenu.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.app.menu.mnemonic"));
/*      */     
/*  259 */     this._appMenu.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  261 */             this.this$0.refresh();
/*      */           } }
/*      */       );
/*      */     
/*  265 */     this._launchOfflineMI = this._appMenu.add("");
/*  266 */     this._launchOfflineMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.launch.offline.mi.mnemonic"));
/*      */ 
/*      */     
/*  269 */     this._launchOnlineMI = this._appMenu.add("");
/*  270 */     this._launchOnlineMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.launch.online.mi.mnemonic"));
/*      */ 
/*      */     
/*  273 */     this._appMenu.addSeparator();
/*      */ 
/*      */ 
/*      */     
/*  277 */     LocalInstallHandler localInstallHandler = LocalInstallHandler.getInstance();
/*  278 */     if (_isLocalInstallSupported) {
/*  279 */       this._installMI = this._appMenu.add("");
/*  280 */       this._installMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.install.mi.mnemonic"));
/*      */       
/*  282 */       this._installMI.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */             public void actionPerformed(ActionEvent param1ActionEvent) {
/*  284 */               this.this$0.integrateApplication();
/*      */             } }
/*      */         );
/*      */     } 
/*  288 */     this._showMI = this._appMenu.add("");
/*  289 */     this._showMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.show.mi.mnemonic"));
/*      */ 
/*      */     
/*  292 */     this._browseMI = this._appMenu.add("");
/*  293 */     this._browseMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.browse.mi.mnemonic"));
/*      */ 
/*      */     
/*  296 */     this._appMenu.addSeparator();
/*      */     
/*  298 */     this._removeMI = this._appMenu.add("");
/*  299 */     this._removeMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.remove.mi.mnemonic"));
/*      */ 
/*      */ 
/*      */     
/*  303 */     this._launchOfflineMI.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  305 */             this.this$0.launchApplication(false);
/*      */           } }
/*      */       );
/*      */     
/*  309 */     this._launchOnlineMI.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  311 */             this.this$0.launchApplication(true);
/*      */           } }
/*      */       );
/*      */     
/*  315 */     this._showMI.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  317 */             this.this$0.showApplication();
/*      */           } }
/*      */       );
/*      */     
/*  321 */     this._browseMI.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  323 */             this.this$0.browseApplication();
/*      */           } }
/*      */       );
/*      */     
/*  327 */     this._removeMI.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  329 */             this.this$0.removeApplications();
/*      */           } }
/*      */       );
/*      */     
/*  333 */     this._viewMenu = new JMenu(ResourceManager.getMessage("jnlp.viewer.view.menu"));
/*      */     
/*  335 */     this._viewMenu.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.view.menu.mnemonic"));
/*      */ 
/*      */     
/*  338 */     for (byte b = 0; b < 5; b++) {
/*  339 */       jMenuItem = this._viewMenu.add(new JCheckBoxMenuItem(ResourceManager.getMessage("jnlp.viewer.view." + b + ".mi"), (b == 0)));
/*      */ 
/*      */       
/*  342 */       jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.view." + b + ".mi.mnemonic"));
/*      */       
/*  344 */       jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */             public void actionPerformed(ActionEvent param1ActionEvent) {
/*  346 */               Object object = param1ActionEvent.getSource();
/*  347 */               SwingUtilities.invokeLater(new Runnable(this, object) { private final Object val$source; private final CacheViewer.null this$1;
/*      */                     public void run() {
/*  349 */                       for (byte b = 0; b < 5; b++) {
/*  350 */                         JMenuItem jMenuItem = this.this$1.this$0._viewMenu.getItem(b);
/*  351 */                         if (jMenuItem instanceof JCheckBoxMenuItem) {
/*  352 */                           JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem)jMenuItem;
/*      */                           
/*  354 */                           if (this.val$source.equals(jCheckBoxMenuItem)) {
/*  355 */                             jCheckBoxMenuItem.setState(true);
/*  356 */                             this.this$1.this$0.setFilter(b);
/*      */                           } else {
/*  358 */                             jCheckBoxMenuItem.setState(false);
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     } }
/*      */                 );
/*      */             } }
/*      */         );
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  370 */     this._helpMenu = new JMenu(ResourceManager.getMessage("jnlp.viewer.help.menu"));
/*      */     
/*  372 */     this._helpMenu.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.help.menu.mnemonic"));
/*      */ 
/*      */ 
/*      */     
/*  376 */     jMenuItem = this._helpMenu.add(ResourceManager.getMessage("jnlp.viewer.help.java.mi"));
/*      */     
/*  378 */     jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.help.java.mi.mnemonic"));
/*      */     
/*  380 */     jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  382 */             String str = Config.getProperty("deployment.home.j2se.url");
/*      */             try {
/*  384 */               URL uRL = new URL(str);
/*  385 */               this.this$0.showDocument(uRL);
/*  386 */             } catch (Exception exception) {
/*  387 */               Trace.ignoredException(exception);
/*      */             } 
/*      */           } }
/*      */       );
/*      */ 
/*      */     
/*  393 */     jMenuItem = this._helpMenu.add(ResourceManager.getMessage("jnlp.viewer.help.jnlp.mi"));
/*      */     
/*  395 */     jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.help.jnlp.mi.mnemonic"));
/*      */     
/*  397 */     jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  399 */             String str = Config.getProperty("deployment.javaws.home.jnlp.url");
/*      */             try {
/*  401 */               URL uRL = new URL(str);
/*  402 */               this.this$0.showDocument(uRL);
/*  403 */             } catch (Exception exception) {
/*  404 */               Trace.ignoredException(exception);
/*      */             } 
/*      */           } }
/*      */       );
/*      */     
/*  409 */     this._appMenu.addSeparator();
/*      */     
/*  411 */     jMenuItem = this._helpMenu.add(ResourceManager.getMessage("jnlp.viewer.about.mi"));
/*      */     
/*  413 */     jMenuItem.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.about.mi.mnemonic"));
/*      */     
/*  415 */     jMenuItem.addActionListener(new ActionListener(this) { private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  417 */             this.this$0.showAbout();
/*      */           } }
/*      */       );
/*      */     
/*  421 */     JMenuBar jMenuBar = new JMenuBar();
/*  422 */     jMenuBar.add(this._fileMenu);
/*  423 */     jMenuBar.add(this._editMenu);
/*  424 */     jMenuBar.add(this._appMenu);
/*  425 */     jMenuBar.add(this._viewMenu);
/*      */     
/*  427 */     jMenuBar.add(this._helpMenu);
/*  428 */     setJMenuBar(jMenuBar);
/*  429 */     resetSizes();
/*  430 */     refresh();
/*      */   }
/*      */ 
/*      */   
/*      */   private void setFilter(int paramInt) {
/*      */     String str;
/*  436 */     if (paramInt == 0) {
/*  437 */       str = ResourceManager.getMessage("jnlp.viewer.all");
/*      */     } else {
/*  439 */       MessageFormat messageFormat = new MessageFormat(ResourceManager.getMessage("jnlp.viewer.type"));
/*      */       
/*  441 */       Object[] arrayOfObject = new Object[1];
/*  442 */       arrayOfObject[0] = ResourceManager.getMessage("jnlp.viewer.view." + paramInt);
/*  443 */       str = messageFormat.format(arrayOfObject);
/*      */     } 
/*  445 */     this._titledBorder.setTitle(str);
/*  446 */     getSelectedTable().setFilter(paramInt);
/*  447 */     getContentPane().repaint();
/*      */   }
/*      */   
/*      */   public JButton makeButton(String paramString) {
/*  451 */     JButton jButton = new JButton(ResourceManager.getMessage(paramString));
/*  452 */     jButton.setMnemonic(ResourceManager.getVKCode(paramString + ".mnemonic"));
/*  453 */     jButton.addActionListener(this);
/*  454 */     return jButton;
/*      */   }
/*      */   
/*      */   public void valueChanged(ListSelectionEvent paramListSelectionEvent) {
/*  458 */     refresh();
/*      */   }
/*      */   
/*      */   public void stateChanged(ChangeEvent paramChangeEvent) {
/*  462 */     refresh();
/*  463 */     resetSizes();
/*      */   }
/*      */   
/*      */   private void resetSizes() {
/*  467 */     Component component = this._tabbedPane.getSelectedComponent();
/*  468 */     boolean bool = !component.equals(this._userTab) ? true : false;
/*  469 */     (new Thread(new Runnable(this, bool) { private final boolean val$system; private final CacheViewer this$0;
/*      */           public void run() {
/*  471 */             if (CacheViewer.getStatus() == 0) {
/*  472 */               CacheViewer.setStatus(5);
/*      */             }
/*      */             try {
/*  475 */               long l = Cache.getCacheSize(this.val$system);
/*  476 */               if (l > 0L) {
/*  477 */                 CacheViewer._totalSize.setText(this.this$0.getAppMessage("jnlp.viewer.totalSize", CacheViewer.tformat(l)));
/*      */               }
/*  479 */               else if (l < 0L) {
/*  480 */                 String str = this.this$0._tabbedPane.getTitleAt(this.this$0._tabbedPane.getSelectedIndex());
/*      */                 
/*  482 */                 CacheViewer._totalSize.setText(this.this$0.getMessage("jnlp.viewer.noCache"));
/*      */               } else {
/*  484 */                 String str = this.this$0._tabbedPane.getTitleAt(this.this$0._tabbedPane.getSelectedIndex());
/*      */                 
/*  486 */                 CacheViewer._totalSize.setText(this.this$0.getAppMessage("jnlp.viewer.emptyCache", str));
/*      */               } 
/*      */             } finally {
/*      */               
/*  490 */               if (CacheViewer.getStatus() == 5) {
/*  491 */                 CacheViewer.setStatus(0);
/*      */               }
/*      */             } 
/*      */           } }
/*      */       )).start();
/*      */   }
/*      */   
/*      */   private static String tformat(long paramLong) {
/*  499 */     if (paramLong > 10240L) {
/*  500 */       return "" + (paramLong / 1024L) + " KB";
/*      */     }
/*  502 */     return "" + (paramLong / 1024L) + "." + (paramLong % 1024L / 102L) + " KB";
/*      */   }
/*      */ 
/*      */   
/*      */   public void refresh() {
/*      */     CacheTable cacheTable;
/*  508 */     Component component = this._tabbedPane.getSelectedComponent();
/*  509 */     boolean bool1 = !component.equals(this._userTab) ? true : false;
/*  510 */     if (bool1) {
/*  511 */       cacheTable = this._sysTable;
/*      */     } else {
/*  513 */       cacheTable = this._userTable;
/*      */     } 
/*  515 */     int[] arrayOfInt = cacheTable.getSelectedRows();
/*      */     
/*  517 */     boolean bool2 = false;
/*  518 */     boolean bool3 = false;
/*  519 */     boolean bool4 = false;
/*  520 */     boolean bool5 = false;
/*  521 */     boolean bool6 = false;
/*  522 */     boolean bool = false;
/*  523 */     boolean bool7 = (!bool1 && arrayOfInt.length > 0) ? true : false;
/*      */     
/*  525 */     String str = "";
/*      */     
/*  527 */     if (arrayOfInt.length == 1) {
/*  528 */       bool5 = true;
/*  529 */       CacheObject cacheObject = cacheTable.getCacheObject(arrayOfInt[0]);
/*  530 */       if (cacheObject != null) {
/*  531 */         LaunchDesc launchDesc = cacheObject.getLaunchDesc();
/*  532 */         str = cacheObject.getTypeString();
/*  533 */         InformationDesc informationDesc = launchDesc.getInformation();
/*  534 */         if (launchDesc.isApplication() || launchDesc.isApplet()) {
/*  535 */           if (_isLocalInstallSupported) {
/*  536 */             LocalApplicationProperties localApplicationProperties = cacheObject.getLocalApplicationProperties();
/*      */             
/*  538 */             bool = localApplicationProperties.isLocallyInstalled();
/*  539 */             bool4 = (!bool1 && !localApplicationProperties.isLocallyInstalledSystem()) ? true : false;
/*      */           } 
/*  541 */           if (informationDesc.supportsOfflineOperation()) {
/*  542 */             bool3 = true;
/*      */           }
/*  544 */           if (launchDesc.getLocation() != null) {
/*  545 */             bool2 = true;
/*      */           }
/*      */         } 
/*  548 */         if (informationDesc.getHome() != null) {
/*  549 */           bool6 = true;
/*      */         }
/*  551 */         this._removeBtn.setText(getAppMessage("jnlp.viewer.remove.1.btn", str));
/*      */       }
/*      */     
/*  554 */     } else if (arrayOfInt.length == 0) {
/*  555 */       this._removeBtn.setText(ResourceManager.getMessage("jnlp.viewer.remove.btn"));
/*      */     } else {
/*      */       
/*  558 */       this._removeBtn.setText(ResourceManager.getMessage("jnlp.viewer.remove.2.btn"));
/*      */     } 
/*      */ 
/*      */     
/*  562 */     this._launchOnlineBtn.setEnabled(bool2);
/*  563 */     this._launchOnlineMI.setEnabled(bool2);
/*  564 */     this._launchOnlineMI.setText(getMessage("jnlp.viewer.launch.online.mi"));
/*      */ 
/*      */     
/*  567 */     this._launchOfflineBtn.setEnabled(bool3);
/*  568 */     this._launchOfflineMI.setEnabled(bool3);
/*  569 */     this._launchOfflineMI.setText(getMessage("jnlp.viewer.launch.offline.mi"));
/*      */ 
/*      */     
/*  572 */     if (_isLocalInstallSupported) {
/*  573 */       this._installMI.setEnabled(bool4);
/*  574 */       if (bool) {
/*  575 */         this._installMI.setText(getMessage("jnlp.viewer.uninstall.mi"));
/*      */         
/*  577 */         this._installMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.uninstall.mi.mnemonic"));
/*      */       } else {
/*      */         
/*  580 */         this._installMI.setText(getMessage("jnlp.viewer.install.mi"));
/*      */         
/*  582 */         this._installMI.setMnemonic(ResourceManager.getVKCode("jnlp.viewer.install.mi.mnemonic"));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  587 */     this._showMI.setEnabled(bool5);
/*  588 */     this._showMI.setText(getMessage("jnlp.viewer.show.mi"));
/*      */     
/*  590 */     this._browseMI.setEnabled(bool6);
/*  591 */     this._browseMI.setText(getMessage("jnlp.viewer.browse.mi"));
/*      */     
/*  593 */     this._removeBtn.setEnabled(bool7);
/*  594 */     this._removeMI.setEnabled(bool7);
/*  595 */     if (arrayOfInt.length == 1) {
/*  596 */       this._removeMI.setText(getAppMessage("jnlp.viewer.remove.mi", str));
/*      */     } else {
/*  598 */       this._removeMI.setText(getMessage("jnlp.viewer.remove.0.mi"));
/*      */     } 
/*      */   }
/*      */   
/*      */   private String getMessage(String paramString) {
/*  603 */     return ResourceManager.getMessage(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getAppMessage(String paramString1, String paramString2) {
/*  610 */     MessageFormat messageFormat = new MessageFormat(ResourceManager.getMessage(paramString1));
/*  611 */     Object[] arrayOfObject = new Object[1];
/*  612 */     arrayOfObject[0] = paramString2;
/*  613 */     return messageFormat.format(arrayOfObject);
/*      */   }
/*      */ 
/*      */   
/*      */   private CacheObject getSelectedCacheObject() {
/*      */     CacheTable cacheTable;
/*  619 */     Component component = this._tabbedPane.getSelectedComponent();
/*  620 */     if (component.equals(this._userTab)) {
/*  621 */       cacheTable = this._userTable;
/*      */     } else {
/*  623 */       cacheTable = this._sysTable;
/*      */     } 
/*  625 */     int[] arrayOfInt = cacheTable.getSelectedRows();
/*  626 */     if (arrayOfInt.length == 1) {
/*  627 */       return cacheTable.getCacheObject(arrayOfInt[0]);
/*      */     }
/*  629 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeDialog(WindowEvent paramWindowEvent) {
/*  636 */     exitViewer();
/*      */   }
/*      */   
/*      */   private void exitViewer() {
/*  640 */     this._sil.removeSingleInstanceListener(this);
/*  641 */     setVisible(false);
/*  642 */     dispose();
/*  643 */     Rectangle rectangle = getBounds();
/*  644 */     Config.setProperty("deployment.javaws.viewer.bounds", "" + rectangle.x + "," + rectangle.y + "," + rectangle.width + "," + rectangle.height);
/*      */     
/*  646 */     Config.storeIfDirty();
/*  647 */     Main.systemExit(0);
/*      */   }
/*      */   
/*      */   public void actionPerformed(ActionEvent paramActionEvent) {
/*  651 */     JButton jButton = (JButton)paramActionEvent.getSource();
/*      */     
/*  653 */     if (jButton == this._removeBtn) {
/*  654 */       removeApplications();
/*  655 */     } else if (jButton == this._launchOnlineBtn) {
/*  656 */       launchApplication(true);
/*  657 */     } else if (jButton == this._launchOfflineBtn) {
/*  658 */       launchApplication(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private CacheTable getSelectedTable() {
/*  663 */     return (this._tabbedPane.getSelectedComponent() == this._userTab) ? this._userTable : this._sysTable;
/*      */   }
/*      */ 
/*      */   
/*      */   private void launchApplication(boolean paramBoolean) {
/*  668 */     if (getStatus() != 2) {
/*  669 */       if (getStatus() == 0) {
/*  670 */         setStatus(2);
/*      */       }
/*      */       try {
/*  673 */         CacheObject cacheObject = getSelectedCacheObject();
/*  674 */         if (cacheObject != null)
/*  675 */           try { File file = cacheObject.getJnlpFile();
/*  676 */             String[] arrayOfString = new String[3];
/*  677 */             arrayOfString[0] = Config.getJavawsCommand();
/*  678 */             arrayOfString[1] = paramBoolean ? "-online" : "-offline";
/*  679 */             arrayOfString[2] = file.getPath();
/*  680 */             Runtime.getRuntime().exec(arrayOfString); }
/*  681 */           catch (IOException iOException)
/*      */           
/*  683 */           { Trace.ignoredException(iOException); }
/*      */            
/*  685 */         SwingUtilities.invokeLater(new Runnable(this) { private final CacheViewer this$0;
/*      */               public void run() {
/*  687 */                 this.this$0.reset(this.this$0._userTable);
/*      */               } }
/*      */           );
/*      */       } finally {
/*      */         
/*  692 */         if (getStatus() == 2) {
/*  693 */           setStatus(0);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void launchApplication() {
/*  700 */     if (this._launchOnlineBtn.isEnabled()) {
/*  701 */       launchApplication(true);
/*  702 */     } else if (this._launchOfflineBtn.isEnabled()) {
/*  703 */       launchApplication(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void browseApplication() {
/*  709 */     CacheObject cacheObject = getSelectedCacheObject();
/*  710 */     if (cacheObject != null) {
/*  711 */       LaunchDesc launchDesc = cacheObject.getLaunchDesc();
/*  712 */       if (launchDesc != null) {
/*  713 */         URL uRL = launchDesc.getInformation().getHome();
/*  714 */         showDocument(uRL);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void showDocument(URL paramURL) {
/*  720 */     if (getStatus() != 3) {
/*  721 */       (new Thread(new Runnable(this, paramURL) { private final URL val$page; private final CacheViewer this$0;
/*      */             public void run() {
/*  723 */               if (CacheViewer.getStatus() == 0) {
/*  724 */                 CacheViewer.setStatus(3);
/*      */               }
/*      */               try {
/*  727 */                 BrowserSupport.showDocument(this.val$page);
/*      */               } finally {
/*  729 */                 if (CacheViewer.getStatus() == 3) {
/*  730 */                   CacheViewer.setStatus(0);
/*      */                 }
/*      */               } 
/*      */             } }
/*      */         )).start();
/*      */     }
/*      */   }
/*      */   
/*      */   private void showApplication() {
/*  739 */     CacheObject cacheObject = getSelectedCacheObject();
/*  740 */     if (cacheObject != null) {
/*  741 */       LaunchDesc launchDesc = cacheObject.getLaunchDesc();
/*  742 */       String str = launchDesc.toString();
/*      */       
/*  744 */       JTextArea jTextArea = new JTextArea(str, 24, 81);
/*  745 */       jTextArea.setEditable(false);
/*      */       
/*  747 */       JScrollPane jScrollPane = new JScrollPane(jTextArea, 20, 30);
/*      */ 
/*      */ 
/*      */       
/*  751 */       DialogFactory.showMessageDialog(this, 2, jScrollPane, getAppMessage("jnlp.viewer.show.title", launchDesc.getInformation().getTitle()), false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void showAbout() {
/*  761 */     (new AboutDialog(this, true)).setVisible(true);
/*      */   }
/*      */   
/*      */   private void cleanCache() {
/*  765 */     if (getStatus() == 0) {
/*  766 */       (new Thread(new Runnable(this) { private final CacheViewer this$0;
/*      */             public void run() {
/*  768 */               CacheViewer.setStatus(1);
/*  769 */               SwingUtilities.invokeLater(new Runnable(this) { private final CacheViewer.null this$1;
/*      */                     public void run() {
/*      */                       try {
/*  772 */                         Cache.clean();
/*  773 */                         this.this$1.this$0.reset(this.this$1.this$0._userTable);
/*      */                       } finally {
/*  775 */                         if (CacheViewer.getStatus() == 1) {
/*  776 */                           CacheViewer.setStatus(0);
/*      */                         }
/*      */                       } 
/*      */                     } }
/*      */                 );
/*      */             } }
/*      */         )).start();
/*      */     }
/*      */   }
/*      */   
/*      */   private void removeApplications() {
/*  787 */     if (getStatus() == 0) {
/*  788 */       (new Thread(new Runnable(this) { private final CacheViewer this$0;
/*      */             public void run() {
/*  790 */               CacheViewer.setStatus(1);
/*  791 */               Component component = this.this$0._tabbedPane.getSelectedComponent();
/*  792 */               boolean bool = !component.equals(this.this$0._userTab) ? true : false;
/*  793 */               CacheTable cacheTable = bool ? this.this$0._sysTable : this.this$0._userTable;
/*      */               
/*  795 */               int[] arrayOfInt = cacheTable.getSelectedRows();
/*  796 */               for (byte b = 0; b < arrayOfInt.length; b++) {
/*  797 */                 CacheObject cacheObject = cacheTable.getCacheObject(arrayOfInt[b]);
/*  798 */                 Cache.remove(cacheObject.getDCE(), cacheObject.getLocalApplicationProperties(), cacheObject.getLaunchDesc());
/*      */               } 
/*      */ 
/*      */               
/*  802 */               Cache.clean();
/*  803 */               SwingUtilities.invokeLater(new Runnable(this) { private final CacheViewer.null this$1;
/*      */                     public void run() {
/*      */                       try {
/*  806 */                         this.this$1.this$0.reset(this.this$1.this$0._userTable);
/*      */                       } finally {
/*  808 */                         if (CacheViewer.getStatus() == 1) {
/*  809 */                           CacheViewer.setStatus(0);
/*      */                         }
/*      */                       } 
/*      */                     } }
/*      */                 );
/*      */             } }
/*      */         )).start();
/*      */     }
/*      */   }
/*      */   
/*      */   public void popupApplicationMenu(Component paramComponent, int paramInt1, int paramInt2) {
/*  820 */     CacheObject cacheObject = getSelectedCacheObject();
/*  821 */     if (cacheObject != null) {
/*  822 */       JPopupMenu jPopupMenu = new JPopupMenu();
/*  823 */       Component[] arrayOfComponent = this._appMenu.getMenuComponents();
/*  824 */       for (byte b = 0; b < arrayOfComponent.length; b++) {
/*  825 */         if (arrayOfComponent[b] instanceof JMenuItem) {
/*  826 */           JMenuItem jMenuItem1 = (JMenuItem)arrayOfComponent[b];
/*  827 */           JMenuItem jMenuItem2 = jPopupMenu.add(new JMenuItem(jMenuItem1.getText(), jMenuItem1.getMnemonic()));
/*      */           
/*  829 */           jMenuItem2.setEnabled(jMenuItem1.isEnabled());
/*  830 */           ActionListener[] arrayOfActionListener = jMenuItem1.getActionListeners();
/*  831 */           for (byte b1 = 0; b1 < arrayOfActionListener.length; jMenuItem2.addActionListener(arrayOfActionListener[b1++]));
/*      */         } else {
/*  833 */           jPopupMenu.addSeparator();
/*      */         } 
/*      */       } 
/*  836 */       jPopupMenu.show(paramComponent, paramInt1, paramInt2);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void integrateApplication() {
/*  841 */     CacheObject cacheObject = getSelectedCacheObject();
/*  842 */     if (cacheObject != null && _isLocalInstallSupported) {
/*  843 */       LocalApplicationProperties localApplicationProperties = cacheObject.getLocalApplicationProperties();
/*  844 */       Component component = this._tabbedPane.getSelectedComponent();
/*  845 */       boolean bool = !component.equals(this._userTab) ? true : false;
/*  846 */       CacheTable cacheTable = bool ? this._sysTable : this._userTable;
/*      */       
/*  848 */       (new Thread(new Installer(this, cacheObject.getLaunchDesc(), localApplicationProperties, cacheTable))).start();
/*      */     } 
/*      */   }
/*      */   
/*      */   class Installer
/*      */     implements Runnable {
/*      */     private final LaunchDesc _ld;
/*      */     private final LocalApplicationProperties _lap;
/*      */     private final CacheTable _table;
/*      */     private final CacheViewer this$0;
/*      */     
/*      */     public Installer(CacheViewer this$0, LaunchDesc param1LaunchDesc, LocalApplicationProperties param1LocalApplicationProperties, CacheTable param1CacheTable) {
/*  860 */       this.this$0 = this$0;
/*  861 */       this._ld = param1LaunchDesc;
/*  862 */       this._lap = param1LocalApplicationProperties;
/*  863 */       this._table = param1CacheTable;
/*      */     }
/*      */     
/*      */     public void run() {
/*  867 */       this._lap.refreshIfNecessary();
/*  868 */       if (this._lap.isLocallyInstalled()) {
/*  869 */         CacheViewer._lih.uninstall(this._ld, this._lap, true);
/*      */       } else {
/*  871 */         CacheViewer._lih.doInstall(this._ld, this._lap);
/*      */       } 
/*  873 */       this._lap.setAskedForInstall(true);
/*      */       try {
/*  875 */         this._lap.store();
/*  876 */       } catch (Exception exception) {
/*  877 */         Trace.ignoredException(exception);
/*      */       } 
/*  879 */       this.this$0.refresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void reset(CacheTable paramCacheTable) {
/*  885 */     resetSizes();
/*  886 */     paramCacheTable.reset();
/*  887 */     refresh();
/*      */   }
/*      */   
/*      */   public static int getStatus() {
/*  891 */     return _status;
/*      */   }
/*      */   public static void setStatus(int paramInt) {
/*      */     String str;
/*  895 */     _status = paramInt;
/*      */     
/*  897 */     switch (paramInt) {
/*      */       case 1:
/*  899 */         str = ResourceManager.getMessage("jnlp.viewer.removing");
/*      */         break;
/*      */       case 2:
/*  902 */         str = ResourceManager.getMessage("jnlp.viewer.launching");
/*      */         break;
/*      */       case 3:
/*  905 */         str = ResourceManager.getMessage("jnlp.viewer.browsing");
/*      */         break;
/*      */       case 4:
/*  908 */         str = ResourceManager.getMessage("jnlp.viewer.sorting");
/*      */         break;
/*      */       case 5:
/*  911 */         str = ResourceManager.getMessage("jnlp.viewer.searching");
/*      */         break;
/*      */       case 6:
/*  914 */         str = ResourceManager.getMessage("jnlp.viewer.installing");
/*      */         break;
/*      */       
/*      */       default:
/*  918 */         str = "";
/*      */         break;
/*      */     } 
/*  921 */     if (paramInt == 0) {
/*  922 */       _statusLabel.setText(str);
/*  923 */       _totalSize.setVisible(true);
/*      */     } else {
/*  925 */       _totalSize.setVisible(false);
/*  926 */       _statusLabel.setText(str);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void showReInstallDialog() {
/*  933 */     Properties properties = Cache.getRemovedApps();
/*      */     
/*  935 */     String[] arrayOfString = this._userTable.getAllHrefs();
/*  936 */     boolean bool = false;
/*  937 */     for (byte b = 0; b < arrayOfString.length; b++) {
/*  938 */       if (properties.getProperty(arrayOfString[b]) != null) {
/*  939 */         properties.remove(arrayOfString[b]);
/*  940 */         bool = true;
/*      */       } 
/*      */     } 
/*  943 */     if (bool) {
/*  944 */       Cache.setRemovedApps(properties);
/*      */     }
/*      */     
/*  947 */     ArrayList arrayList1 = new ArrayList();
/*  948 */     ArrayList arrayList2 = new ArrayList();
/*      */     
/*  950 */     Enumeration enumeration = properties.propertyNames();
/*  951 */     while (enumeration.hasMoreElements()) {
/*  952 */       String str = (String)enumeration.nextElement();
/*  953 */       arrayList1.add(str);
/*  954 */       arrayList2.add(properties.getProperty(str));
/*      */     } 
/*  956 */     String str1 = ResourceManager.getMessage("jnlp.viewer.reinstall.column.title");
/*      */     
/*  958 */     String str2 = ResourceManager.getMessage("jnlp.viewer.reinstall.column.location");
/*      */ 
/*      */     
/*  961 */     AbstractTableModel abstractTableModel = new AbstractTableModel(this, str1, str2, arrayList2, arrayList1) { private final String val$titleName; private final String val$hrefName; private final ArrayList val$titles; private final ArrayList val$hrefs; private final CacheViewer this$0;
/*      */         public String getColumnName(int param1Int) {
/*  963 */           return (param1Int == 0) ? this.val$titleName : this.val$hrefName;
/*      */         }
/*      */         public Object getValueAt(int param1Int1, int param1Int2) {
/*  966 */           return (param1Int2 == 0) ? this.val$titles.get(param1Int1) : this.val$hrefs.get(param1Int1);
/*      */         }
/*      */         public int getColumnCount() {
/*  969 */           return 2;
/*      */         }
/*      */         public int getRowCount() {
/*  972 */           return this.val$hrefs.size();
/*      */         }
/*      */         public Class getColumnClass(int param1Int) {
/*  975 */           return (CacheViewer.class$java$lang$String == null) ? (CacheViewer.class$java$lang$String = CacheViewer.class$("java.lang.String")) : CacheViewer.class$java$lang$String;
/*      */         } }
/*      */       ;
/*      */     
/*  979 */     String str3 = "jnlp.viewer.reinstallBtn";
/*  980 */     JButton jButton1 = new JButton(ResourceManager.getMessage(str3));
/*  981 */     jButton1.setMnemonic(ResourceManager.getVKCode(str3 + ".mnemonic"));
/*  982 */     jButton1.setEnabled(false);
/*      */     
/*  984 */     str3 = "jnlp.viewer.closeBtn";
/*  985 */     JButton jButton2 = new JButton(ResourceManager.getMessage(str3));
/*  986 */     jButton2.setMnemonic(ResourceManager.getVKCode(str3 + ".mnemonic"));
/*      */     
/*  988 */     Object[] arrayOfObject = { jButton1, jButton2 };
/*      */     
/*  990 */     JTable jTable = new JTable(abstractTableModel);
/*  991 */     jButton1.addActionListener(new ActionListener(this, jTable, arrayList1) { private final JTable val$table; private final ArrayList val$hrefs; private final CacheViewer this$0;
/*      */           public void actionPerformed(ActionEvent param1ActionEvent) {
/*  993 */             int[] arrayOfInt = this.val$table.getSelectedRows();
/*  994 */             String[] arrayOfString = new String[arrayOfInt.length];
/*  995 */             for (byte b = 0; b < arrayOfString.length; b++) {
/*  996 */               arrayOfString[b] = this.val$hrefs.get(arrayOfInt[b]);
/*      */             }
/*  998 */             this.this$0.do_reinstall(arrayOfString);
/*      */           } }
/*      */       );
/*      */     
/* 1002 */     jTable.getColumnModel().getColumn(0).setPreferredWidth(200);
/* 1003 */     jTable.getColumnModel().getColumn(1).setPreferredWidth(440);
/* 1004 */     jTable.setPreferredScrollableViewportSize(new Dimension(640, 180));
/* 1005 */     JScrollPane jScrollPane = new JScrollPane(jTable);
/* 1006 */     jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(this, jButton1, jTable) { private final JButton val$reinstall; private final JTable val$table; private final CacheViewer this$0;
/*      */           
/*      */           public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 1009 */             this.val$reinstall.setEnabled((this.val$table.getSelectedRowCount() > 0));
/*      */           } }
/*      */       );
/*      */     
/* 1013 */     int i = DialogFactory.showOptionDialog(this, 5, jScrollPane, ResourceManager.getMessage("jnlp.viewer.reinstall.title"), arrayOfObject, jButton2);
/*      */   } static Class class$(String paramString) {
/*      */     try {
/*      */       return Class.forName(paramString);
/*      */     } catch (ClassNotFoundException classNotFoundException) {
/*      */       throw new NoClassDefFoundError(classNotFoundException.getMessage());
/*      */     } 
/*      */   }
/*      */   public void do_reinstall(String[] paramArrayOfString) {
/* 1022 */     (new Thread(new Runnable(this, paramArrayOfString) { private final String[] val$hrefs; private final CacheViewer this$0;
/*      */           public void run() {
/* 1024 */             if (CacheViewer.getStatus() == 0)
/* 1025 */               CacheViewer.setStatus(6); 
/*      */             try {
/*      */               byte b;
/* 1028 */               for (b = 0; b < this.val$hrefs.length; b++) {
/* 1029 */                 Main.importApp(this.val$hrefs[b]);
/*      */ 
/*      */                 
/* 1032 */                 byte b1 = 0;
/* 1033 */                 while (Main.getLaunchThreadGroup().activeCount() > 8) {
/*      */                   
/* 1035 */                   try { Thread.sleep(2000L); } catch (Exception exception) {}
/* 1036 */                   if (++b1 > 5)
/*      */                     break; 
/* 1038 */                 }  if (Main.getLaunchThreadGroup().activeCount() > 8) {
/* 1039 */                   Trace.println("Warning: after waiting, still " + Main.getLaunchThreadGroup().activeCount() + " launching threads");
/*      */                 }
/*      */               }
/*      */             
/*      */             }
/* 1044 */             catch (Exception exception) {
/* 1045 */               Trace.ignoredException(exception);
/*      */             } finally {
/* 1047 */               for (byte b = 10; b > 0; b--) {
/* 1048 */                 int i = Main.getLaunchThreadGroup().activeCount();
/* 1049 */                 if (i <= 0)
/* 1050 */                   break;  try { Thread.sleep(2000L); } catch (Exception exception) {}
/*      */               } 
/* 1052 */               if (Main.getLaunchThreadGroup().activeCount() > 0) {
/* 1053 */                 Trace.println("Warning: after waiting 20 sec., still " + Main.getLaunchThreadGroup().activeCount() + " launching threads");
/*      */               }
/*      */ 
/*      */               
/* 1057 */               if (CacheViewer.getStatus() == 6) {
/* 1058 */                 CacheViewer.setStatus(0);
/*      */               }
/*      */             } 
/*      */           } }
/*      */       )).start();
/*      */   }
/*      */   
/*      */   public void newActivation(String[] paramArrayOfString) {
/* 1066 */     this._userTable.setFilter(0);
/* 1067 */     this._sysTable.setFilter(0);
/* 1068 */     setExtendedState(getExtendedState() & 0xFFFFFFFE);
/* 1069 */     toFront();
/*      */   }
/*      */   
/*      */   public Object getSingleInstanceListener() {
/* 1073 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void main(String[] paramArrayOfString) {
/* 1078 */     SplashScreen.hide();
/*      */     
/* 1080 */     if (SingleInstanceManager.isServerRunning(JAVAWS_CV_ID) && 
/* 1081 */       SingleInstanceManager.connectToServer("dummy")) {
/* 1082 */       System.exit(0);
/*      */     }
/*      */ 
/*      */     
/* 1086 */     LookAndFeel lookAndFeel = null;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1091 */       lookAndFeel = DeployUIManager.setLookAndFeel();
/*      */       
/* 1093 */       if (Config.getBooleanProperty("deployment.debug.console")) {
/* 1094 */         JavawsConsoleController.showConsoleIfEnable();
/*      */       }
/*      */       
/* 1097 */       CacheViewer cacheViewer = new CacheViewer();
/*      */       
/* 1099 */       String str = Config.getProperty("deployment.javaws.viewer.bounds");
/* 1100 */       if (str != null) {
/* 1101 */         StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
/* 1102 */         int[] arrayOfInt = new int[4];
/*      */         byte b;
/* 1104 */         for (b = 0; b < 4; b++) {
/* 1105 */           if (stringTokenizer.hasMoreTokens()) {
/* 1106 */             String str1 = stringTokenizer.nextToken();
/*      */             try {
/* 1108 */               arrayOfInt[b] = Integer.parseInt(str1);
/*      */             }
/* 1110 */             catch (NumberFormatException numberFormatException) {}
/*      */           } 
/*      */         } 
/* 1113 */         if (b == 4) {
/* 1114 */           cacheViewer.setBounds(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
/*      */         }
/*      */       } 
/* 1117 */       cacheViewer.setVisible(true);
/*      */       
/* 1119 */       long l1 = Cache.getLastAccessed(false);
/* 1120 */       long l2 = Cache.getLastAccessed(true);
/*      */       
/*      */       while (true) {
/*      */         try {
/* 1124 */           Thread.sleep(2000L);
/* 1125 */         } catch (InterruptedException interruptedException) {
/*      */           break;
/*      */         } 
/* 1128 */         long l3 = Cache.getLastAccessed(false);
/* 1129 */         long l4 = Cache.getLastAccessed(true);
/*      */         
/* 1131 */         if (l3 != l1) {
/* 1132 */           if (getStatus() == 0) {
/* 1133 */             l1 = l3;
/*      */             
/* 1135 */             SwingUtilities.invokeLater(new Runnable(cacheViewer) { private final CacheViewer val$cv;
/*      */                   public void run() {
/* 1137 */                     this.val$cv.reset(this.val$cv._userTable);
/*      */                   } }
/*      */               );
/*      */           } 
/*      */         }
/* 1142 */         if (l4 != l2) {
/* 1143 */           if (getStatus() == 0) {
/* 1144 */             l2 = l4;
/*      */             
/* 1146 */             SwingUtilities.invokeLater(new Runnable(cacheViewer) { private final CacheViewer val$cv;
/*      */                   public void run() {
/* 1148 */                     this.val$cv.reset(this.val$cv._sysTable);
/*      */                   } }
/*      */               );
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } finally {
/*      */       
/* 1156 */       DeployUIManager.restoreLookAndFeel(lookAndFeel);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\ui\CacheViewer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */