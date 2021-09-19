/*     */ package com.sun.javaws.security;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.security.BadCertificateDialog;
/*     */ import com.sun.deploy.security.CeilingPolicy;
/*     */ import com.sun.deploy.security.TrustDecider;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.Main;
/*     */ import com.sun.javaws.jnl.JARDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.jnlp.JNLPClassLoader;
/*     */ import java.awt.AWTPermission;
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.net.SocketPermission;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AllPermission;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.PropertyPermission;
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
/*     */ public class AppPolicy
/*     */ {
/*  51 */   private String _host = null;
/*     */ 
/*     */   
/*  54 */   private File _extensionDir = null;
/*     */   
/*  56 */   private static AppPolicy _instance = null;
/*     */ 
/*     */   
/*     */   public static AppPolicy getInstance() {
/*  60 */     return _instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public static AppPolicy createInstance(String paramString) {
/*  65 */     if (_instance == null) {
/*  66 */       _instance = new AppPolicy(paramString);
/*     */     }
/*  68 */     return _instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AppPolicy(String paramString) {
/*  75 */     this._host = paramString;
/*     */ 
/*     */     
/*  78 */     this._extensionDir = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "ext");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPermissions(PermissionCollection paramPermissionCollection, CodeSource paramCodeSource) {
/*  85 */     Trace.println("Permission requested for: " + paramCodeSource.getLocation(), TraceLevel.SECURITY);
/*     */ 
/*     */ 
/*     */     
/*  89 */     JARDesc jARDesc = JNLPClassLoader.getInstance().getJarDescFromFileURL(paramCodeSource.getLocation());
/*     */     
/*  91 */     if (jARDesc == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     LaunchDesc launchDesc = jARDesc.getParent().getParent();
/*  99 */     int i = launchDesc.getSecurityModel();
/*     */     
/* 101 */     if (i != 0) {
/* 102 */       grantUnrestrictedAccess(launchDesc, paramCodeSource);
/* 103 */       if (i == 1) {
/* 104 */         CeilingPolicy.addTrustedPermissions(paramPermissionCollection);
/*     */       } else {
/* 106 */         addJ2EEApplicationClientPermissionsObject(paramPermissionCollection);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 111 */     if (!paramPermissionCollection.implies(new AllPermission())) {
/* 112 */       addSandboxPermissionsObject(paramPermissionCollection, (launchDesc.getLaunchType() == 2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 117 */     if (!launchDesc.arePropsSet()) {
/*     */       
/* 119 */       Properties properties = launchDesc.getResources().getResourceProperties();
/* 120 */       Enumeration enumeration = properties.keys();
/* 121 */       while (enumeration.hasMoreElements()) {
/* 122 */         String str1 = (String)enumeration.nextElement();
/* 123 */         String str2 = properties.getProperty(str1);
/* 124 */         PropertyPermission propertyPermission = new PropertyPermission(str1, "write");
/*     */         
/* 126 */         if (paramPermissionCollection.implies(propertyPermission)) {
/* 127 */           System.setProperty(str1, str2); continue;
/*     */         } 
/* 129 */         Trace.ignoredException(new AccessControlException("access denied " + propertyPermission, propertyPermission));
/*     */       } 
/*     */ 
/*     */       
/* 133 */       launchDesc.setPropsSet(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUnrestrictedProps(LaunchDesc paramLaunchDesc) {
/* 139 */     if (!paramLaunchDesc.arePropsSet()) {
/*     */       
/* 141 */       Properties properties = paramLaunchDesc.getResources().getResourceProperties();
/* 142 */       Enumeration enumeration = properties.keys();
/* 143 */       while (enumeration.hasMoreElements()) {
/* 144 */         String str = (String)enumeration.nextElement();
/* 145 */         System.setProperty(str, properties.getProperty(str));
/*     */       } 
/* 147 */       paramLaunchDesc.setPropsSet(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void grantUnrestrictedAccess(LaunchDesc paramLaunchDesc, CodeSource paramCodeSource) {
/*     */     String str;
/* 154 */     boolean bool = false;
/* 155 */     switch (paramLaunchDesc.getLaunchType()) {
/*     */       
/*     */       default:
/* 158 */         str = "trustdecider.code.type.application";
/*     */         break;
/*     */       
/*     */       case 2:
/* 162 */         str = "trustdecider.code.type.applet";
/*     */         break;
/*     */       
/*     */       case 3:
/* 166 */         str = "trustdecider.code.type.extension";
/*     */         break;
/*     */       
/*     */       case 4:
/* 170 */         str = "trustdecider.code.type.installer";
/*     */         break;
/*     */     } 
/*     */     try {
/* 174 */       if (Globals.isSecureMode() || TrustDecider.isAllPermissionGranted(paramCodeSource, str)) {
/*     */         
/* 176 */         setUnrestrictedProps(paramLaunchDesc);
/*     */         return;
/*     */       } 
/* 179 */       Trace.println("We were not granted permission, exiting", TraceLevel.SECURITY);
/*     */     }
/* 181 */     catch (Exception exception) {
/* 182 */       BadCertificateDialog.show(paramCodeSource, str, exception);
/*     */     } 
/*     */     
/* 185 */     Main.systemExit(-1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addJ2EEApplicationClientPermissionsObject(PermissionCollection paramPermissionCollection) {
/* 190 */     Trace.println("Creating J2EE-application-client-permisisons object", TraceLevel.SECURITY);
/*     */ 
/*     */ 
/*     */     
/* 194 */     paramPermissionCollection.add(new AWTPermission("accessClipboard"));
/* 195 */     paramPermissionCollection.add(new AWTPermission("accessEventQueue"));
/* 196 */     paramPermissionCollection.add(new AWTPermission("showWindowWithoutWarningBanner"));
/*     */ 
/*     */     
/* 199 */     paramPermissionCollection.add(new RuntimePermission("exitVM"));
/* 200 */     paramPermissionCollection.add(new RuntimePermission("loadLibrary"));
/* 201 */     paramPermissionCollection.add(new RuntimePermission("queuePrintJob"));
/*     */ 
/*     */     
/* 204 */     paramPermissionCollection.add(new SocketPermission("*", "connect"));
/* 205 */     paramPermissionCollection.add(new SocketPermission("localhost:1024-", "accept,listen"));
/*     */ 
/*     */     
/* 208 */     paramPermissionCollection.add(new FilePermission("*", "read,write"));
/*     */ 
/*     */     
/* 211 */     paramPermissionCollection.add(new PropertyPermission("*", "read"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSandboxPermissionsObject(PermissionCollection paramPermissionCollection, boolean paramBoolean) {
/* 221 */     Trace.println("Add sandbox permissions", TraceLevel.SECURITY);
/*     */ 
/*     */ 
/*     */     
/* 225 */     paramPermissionCollection.add(new PropertyPermission("java.version", "read"));
/* 226 */     paramPermissionCollection.add(new PropertyPermission("java.vendor", "read"));
/* 227 */     paramPermissionCollection.add(new PropertyPermission("java.vendor.url", "read"));
/* 228 */     paramPermissionCollection.add(new PropertyPermission("java.class.version", "read"));
/* 229 */     paramPermissionCollection.add(new PropertyPermission("os.name", "read"));
/* 230 */     paramPermissionCollection.add(new PropertyPermission("os.arch", "read"));
/* 231 */     paramPermissionCollection.add(new PropertyPermission("os.version", "read"));
/* 232 */     paramPermissionCollection.add(new PropertyPermission("file.separator", "read"));
/* 233 */     paramPermissionCollection.add(new PropertyPermission("path.separator", "read"));
/* 234 */     paramPermissionCollection.add(new PropertyPermission("line.separator", "read"));
/*     */     
/* 236 */     paramPermissionCollection.add(new PropertyPermission("java.specification.version", "read"));
/* 237 */     paramPermissionCollection.add(new PropertyPermission("java.specification.vendor", "read"));
/* 238 */     paramPermissionCollection.add(new PropertyPermission("java.specification.name", "read"));
/*     */     
/* 240 */     paramPermissionCollection.add(new PropertyPermission("java.vm.specification.version", "read"));
/* 241 */     paramPermissionCollection.add(new PropertyPermission("java.vm.specification.vendor", "read"));
/* 242 */     paramPermissionCollection.add(new PropertyPermission("java.vm.specification.name", "read"));
/* 243 */     paramPermissionCollection.add(new PropertyPermission("java.vm.version", "read"));
/* 244 */     paramPermissionCollection.add(new PropertyPermission("java.vm.vendor", "read"));
/* 245 */     paramPermissionCollection.add(new PropertyPermission("java.vm.name", "read"));
/*     */ 
/*     */     
/* 248 */     paramPermissionCollection.add(new PropertyPermission("javawebstart.version", "read"));
/*     */ 
/*     */     
/* 251 */     paramPermissionCollection.add(new RuntimePermission("exitVM"));
/* 252 */     paramPermissionCollection.add(new RuntimePermission("stopThread"));
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
/* 272 */     String str = "Java " + (paramBoolean ? "Applet" : "Application") + " Window";
/*     */ 
/*     */     
/* 275 */     if (Config.getBooleanProperty("deployment.security.sandbox.awtwarningwindow")) {
/* 276 */       System.setProperty("awt.appletWarning", str);
/*     */     } else {
/* 278 */       paramPermissionCollection.add(new AWTPermission("showWindowWithoutWarningBanner"));
/*     */     } 
/*     */ 
/*     */     
/* 282 */     paramPermissionCollection.add(new SocketPermission("localhost:1024-", "listen"));
/*     */     
/* 284 */     paramPermissionCollection.add(new SocketPermission(this._host, "connect, accept"));
/*     */ 
/*     */     
/* 287 */     paramPermissionCollection.add(new PropertyPermission("jnlp.*", "read,write"));
/* 288 */     paramPermissionCollection.add(new PropertyPermission("javaws.*", "read,write"));
/*     */ 
/*     */     
/* 291 */     String[] arrayOfString = Config.getSecureProperties();
/* 292 */     for (byte b = 0; b < arrayOfString.length; b++)
/* 293 */       paramPermissionCollection.add(new PropertyPermission(arrayOfString[b], "read,write")); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\security\AppPolicy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */