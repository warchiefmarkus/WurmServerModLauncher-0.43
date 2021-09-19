/*     */ package com.sun.javaws;
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.ConsoleWindow;
/*     */ import com.sun.deploy.util.DeployUIManager;
/*     */ import com.sun.deploy.util.DialogFactory;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.util.JavawsConsoleController;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.Document;
/*     */ 
/*     */ public class LaunchErrorDialog extends JDialog {
/*     */   private LaunchErrorDialog(Frame paramFrame, Throwable paramThrowable) {
/*  33 */     super(paramFrame, true);
/*     */     
/*  35 */     JNLPException jNLPException = null;
/*  36 */     if (paramThrowable instanceof JNLPException) {
/*  37 */       jNLPException = (JNLPException)paramThrowable;
/*     */     }
/*     */     
/*  40 */     JTabbedPane jTabbedPane = new JTabbedPane();
/*  41 */     getContentPane().setLayout(new BorderLayout());
/*  42 */     getContentPane().add("Center", jTabbedPane);
/*     */     
/*  44 */     jTabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
/*     */     
/*  46 */     String str1 = getErrorCategory(paramThrowable);
/*  47 */     setTitle(ResourceManager.getString("launcherrordialog.title", str1));
/*     */     
/*  49 */     String str2 = getLaunchDescTitle();
/*  50 */     String str3 = getLaunchDescVendor();
/*     */     
/*  52 */     if (Globals.isImportMode()) {
/*  53 */       str4 = ResourceManager.getString("launcherrordialog.import.errorintro");
/*     */     } else {
/*  55 */       str4 = ResourceManager.getString("launcherrordialog.errorintro");
/*     */     } 
/*  57 */     if (str2 != null) {
/*  58 */       str4 = str4 + ResourceManager.getString("launcherrordialog.errortitle", str2);
/*     */     }
/*  60 */     if (str3 != null) {
/*  61 */       str4 = str4 + ResourceManager.getString("launcherrordialog.errorvendor", str3);
/*     */     }
/*  63 */     String str4 = str4 + ResourceManager.getString("launcherrordialog.errorcategory", str1);
/*  64 */     str4 = str4 + getErrorDescription(paramThrowable);
/*     */ 
/*     */     
/*  67 */     JTextArea jTextArea = new JTextArea();
/*  68 */     jTextArea.setFont(ResourceManager.getUIFont());
/*  69 */     jTextArea.setEditable(false);
/*  70 */     jTextArea.setLineWrap(true);
/*  71 */     jTextArea.setText(str4);
/*  72 */     jTabbedPane.add(ResourceManager.getString("launcherrordialog.generalTab"), new JScrollPane(jTextArea));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     String str5 = null;
/*  78 */     String str6 = null;
/*     */ 
/*     */     
/*  81 */     if (jNLPException != null) {
/*  82 */       str5 = jNLPException.getLaunchDescSource();
/*  83 */       if (str5 == null) {
/*  84 */         LaunchDesc launchDesc = JNLPException.getDefaultLaunchDesc();
/*  85 */         if (launchDesc != null) {
/*  86 */           str5 = launchDesc.getSource();
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*  91 */     else if (JNLPException.getDefaultLaunchDesc() != null) {
/*  92 */       str5 = JNLPException.getDefaultLaunchDesc().getSource();
/*     */     } 
/*     */ 
/*     */     
/*  96 */     if (JNLPException.getDefaultLaunchDesc() != null) {
/*  97 */       str6 = JNLPException.getDefaultLaunchDesc().getSource();
/*     */     }
/*     */ 
/*     */     
/* 101 */     if (str6 != null && str6.equals(str5)) {
/* 102 */       str6 = null;
/*     */     }
/*     */ 
/*     */     
/* 106 */     if (str5 != null) {
/* 107 */       JTextArea jTextArea1 = new JTextArea();
/* 108 */       jTextArea1.setFont(ResourceManager.getUIFont());
/* 109 */       jTextArea1.setEditable(false);
/* 110 */       jTextArea1.setLineWrap(true);
/* 111 */       jTextArea1.setText(str5);
/* 112 */       jTabbedPane.add(ResourceManager.getString("launcherrordialog.jnlpTab"), new JScrollPane(jTextArea1));
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if (str6 != null) {
/* 117 */       JTextArea jTextArea1 = new JTextArea();
/* 118 */       jTextArea1.setFont(ResourceManager.getUIFont());
/* 119 */       jTextArea1.setEditable(false);
/* 120 */       jTextArea1.setLineWrap(true);
/* 121 */       jTextArea1.setText(str6);
/* 122 */       jTabbedPane.add(ResourceManager.getString("launcherrordialog.jnlpMainTab"), new JScrollPane(jTextArea1));
/*     */     } 
/*     */ 
/*     */     
/* 126 */     if (paramThrowable != null) {
/* 127 */       JTextArea jTextArea1 = new JTextArea();
/* 128 */       jTextArea1.setFont(ResourceManager.getUIFont());
/* 129 */       jTextArea1.setEditable(false);
/* 130 */       jTextArea1.setLineWrap(true);
/* 131 */       jTextArea1.setWrapStyleWord(false);
/* 132 */       StringWriter stringWriter = new StringWriter();
/* 133 */       PrintWriter printWriter = new PrintWriter(stringWriter);
/* 134 */       paramThrowable.printStackTrace(printWriter);
/* 135 */       jTextArea1.setText(stringWriter.toString());
/* 136 */       jTabbedPane.add(ResourceManager.getString("launcherrordialog.exceptionTab"), new JScrollPane(jTextArea1));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     if (jNLPException != null && jNLPException.getWrappedException() != null) {
/* 142 */       JTextArea jTextArea1 = new JTextArea();
/* 143 */       jTextArea1.setFont(ResourceManager.getUIFont());
/* 144 */       jTextArea1.setEditable(false);
/* 145 */       jTextArea1.setLineWrap(true);
/* 146 */       jTextArea1.setWrapStyleWord(false);
/* 147 */       StringWriter stringWriter = new StringWriter();
/* 148 */       PrintWriter printWriter = new PrintWriter(stringWriter);
/* 149 */       jNLPException.getWrappedException().printStackTrace(printWriter);
/* 150 */       jTextArea1.setText(stringWriter.toString());
/* 151 */       jTabbedPane.add(ResourceManager.getString("launcherrordialog.wrappedExceptionTab"), new JScrollPane(jTextArea1));
/*     */     } 
/*     */ 
/*     */     
/* 155 */     Document document = null;
/* 156 */     ConsoleWindow consoleWindow = JavawsConsoleController.getInstance().getConsole();
/* 157 */     if (consoleWindow != null) {
/* 158 */       document = consoleWindow.getTextArea().getDocument();
/*     */     }
/* 160 */     if (document != null) {
/* 161 */       JTextArea jTextArea1 = new JTextArea(document);
/* 162 */       jTextArea1.setFont(ResourceManager.getUIFont());
/*     */       
/* 164 */       jTabbedPane.add(ResourceManager.getString("launcherrordialog.consoleTab"), new JScrollPane(jTextArea1));
/*     */     } 
/*     */ 
/*     */     
/* 168 */     JButton jButton = new JButton(ResourceManager.getString("launcherrordialog.abort"));
/*     */     
/* 170 */     jButton.setMnemonic(ResourceManager.getVKCode("launcherrordialog.abortMnemonic"));
/*     */     
/* 172 */     Box box = new Box(0);
/* 173 */     box.add(Box.createHorizontalGlue());
/* 174 */     box.add(jButton);
/* 175 */     box.add(Box.createHorizontalGlue());
/* 176 */     getContentPane().add("South", box);
/* 177 */     getRootPane().setDefaultButton(jButton);
/*     */     
/* 179 */     jButton.addActionListener(new ActionListener(this) { private final LaunchErrorDialog this$0;
/*     */           public void actionPerformed(ActionEvent param1ActionEvent) {
/* 181 */             this.this$0.setVisible(false);
/*     */           } }
/*     */       );
/*     */     
/* 185 */     addWindowListener(new WindowAdapter(this) { private final LaunchErrorDialog this$0;
/*     */           public void windowClosing(WindowEvent param1WindowEvent) {
/* 187 */             this.this$0.setVisible(false);
/*     */           } }
/*     */       );
/* 190 */     pack();
/* 191 */     setSize(450, 300);
/*     */ 
/*     */     
/* 194 */     Rectangle rectangle = getBounds();
/* 195 */     Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
/* 196 */     rectangle.width = Math.min(dimension.width, rectangle.width);
/* 197 */     rectangle.height = Math.min(dimension.height, rectangle.height);
/* 198 */     setBounds((dimension.width - rectangle.width) / 2, (dimension.height - rectangle.height) / 2, rectangle.width, rectangle.height);
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
/*     */   public static void show(Frame paramFrame, Throwable paramThrowable, boolean paramBoolean) {
/*     */     try {
/* 211 */       SwingUtilities.invokeAndWait(new Runnable(paramFrame, paramThrowable) { private final Frame val$owner;
/*     */             public void run() {
/* 213 */               LaunchErrorDialog.showWarning(this.val$owner, this.val$e);
/*     */             } private final Throwable val$e; }
/*     */         );
/* 216 */     } catch (Exception exception) {}
/*     */     
/* 218 */     if (paramBoolean) {
/* 219 */       Main.systemExit(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void showWarning(Frame paramFrame, Throwable paramThrowable) {
/* 225 */     LookAndFeel lookAndFeel = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 230 */       lookAndFeel = DeployUIManager.setLookAndFeel();
/*     */       
/* 232 */       SplashScreen.hide();
/*     */ 
/*     */       
/* 235 */       System.err.println("#### Java Web Start Error:");
/* 236 */       System.err.println("#### " + paramThrowable.getMessage());
/* 237 */       boolean bool = (!Globals.TCKHarnessRun && (!Globals.isSilentMode() || Main.isViewer())) ? true : false;
/*     */ 
/*     */       
/* 240 */       if (bool && wantsDetails(paramFrame, paramThrowable)) {
/* 241 */         LaunchErrorDialog launchErrorDialog = new LaunchErrorDialog(paramFrame, paramThrowable);
/* 242 */         launchErrorDialog.setVisible(true);
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 248 */       DeployUIManager.restoreLookAndFeel(lookAndFeel);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getErrorCategory(Throwable paramThrowable) {
/* 254 */     String str = ResourceManager.getString("launch.error.category.unexpected");
/*     */     
/* 256 */     if (paramThrowable instanceof JNLPException) {
/*     */       
/* 258 */       JNLPException jNLPException = (JNLPException)paramThrowable;
/* 259 */       str = jNLPException.getCategory();
/* 260 */     } else if (paramThrowable instanceof SecurityException || paramThrowable instanceof java.security.GeneralSecurityException) {
/* 261 */       str = ResourceManager.getString("launch.error.category.security");
/* 262 */     } else if (paramThrowable instanceof OutOfMemoryError) {
/* 263 */       str = ResourceManager.getString("launch.error.category.memory");
/*     */     } 
/* 265 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getErrorDescription(Throwable paramThrowable) {
/* 272 */     String str = paramThrowable.getMessage();
/* 273 */     if (str == null)
/*     */     {
/* 275 */       str = ResourceManager.getString("launcherrordialog.genericerror", paramThrowable.getClass().getName());
/*     */     }
/* 277 */     return str;
/*     */   }
/*     */   
/*     */   private static String getLaunchDescTitle() {
/* 281 */     LaunchDesc launchDesc = JNLPException.getDefaultLaunchDesc();
/* 282 */     return (launchDesc == null) ? null : launchDesc.getInformation().getTitle();
/*     */   }
/*     */   
/*     */   private static String getLaunchDescVendor() {
/* 286 */     LaunchDesc launchDesc = JNLPException.getDefaultLaunchDesc();
/* 287 */     return (launchDesc == null) ? null : launchDesc.getInformation().getVendor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean wantsDetails(Frame paramFrame, Throwable paramThrowable) {
/* 296 */     String str1 = null;
/* 297 */     String str2 = getErrorCategory(paramThrowable);
/*     */     
/* 299 */     if (paramThrowable instanceof JNLPException) {
/* 300 */       str1 = ((JNLPException)paramThrowable).getBriefMessage();
/*     */     }
/* 302 */     if (str1 == null) {
/* 303 */       if (getLaunchDescTitle() == null) {
/* 304 */         if (Globals.isImportMode()) {
/* 305 */           str1 = ResourceManager.getString("launcherrordialog.import.brief.message");
/*     */         } else {
/*     */           
/* 308 */           str1 = ResourceManager.getString("launcherrordialog.brief.message");
/*     */         }
/*     */       
/*     */       }
/* 312 */       else if (Globals.isImportMode()) {
/* 313 */         str1 = ResourceManager.getString("launcherrordialog.import.brief.messageKnown", getLaunchDescTitle());
/*     */       } else {
/*     */         
/* 316 */         str1 = ResourceManager.getString("launcherrordialog.brief.messageKnown", getLaunchDescTitle());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 321 */     String[] arrayOfString = { ResourceManager.getString("launcherrordialog.brief.ok"), ResourceManager.getString("launcherrordialog.brief.details") };
/*     */ 
/*     */ 
/*     */     
/* 325 */     int i = DialogFactory.showOptionDialog(paramFrame, 1, str1, ResourceManager.getString("launcherrordialog.brief.title", str2), (Object[])arrayOfString, arrayOfString[0]);
/*     */ 
/*     */ 
/*     */     
/* 329 */     if (i == 1) {
/* 330 */       return true;
/*     */     }
/* 332 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\LaunchErrorDialog.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */