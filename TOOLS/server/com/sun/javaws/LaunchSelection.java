/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.config.JREInfo;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.jnl.ExtensionDesc;
/*     */ import com.sun.javaws.jnl.JARDesc;
/*     */ import com.sun.javaws.jnl.JREDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.PackageDesc;
/*     */ import com.sun.javaws.jnl.PropertyDesc;
/*     */ import com.sun.javaws.jnl.ResourceVisitor;
/*     */ import com.sun.javaws.jnl.ResourcesDesc;
/*     */ import com.sun.javaws.util.VersionID;
/*     */ import com.sun.javaws.util.VersionString;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class LaunchSelection
/*     */ {
/*     */   static JREInfo selectJRE(LaunchDesc paramLaunchDesc) {
/*  43 */     JREDesc[] arrayOfJREDesc = new JREDesc[1];
/*  44 */     JREInfo[] arrayOfJREInfo = new JREInfo[1];
/*     */ 
/*     */     
/*  47 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  48 */     resourcesDesc.visit(new ResourceVisitor(arrayOfJREInfo, arrayOfJREDesc) { private final JREInfo[] val$selectedJRE; private final JREDesc[] val$selectedJREDesc;
/*     */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*     */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*     */           public void visitPackageDesc(PackageDesc param1PackageDesc) {}
/*     */           
/*     */           public void visitJREDesc(JREDesc param1JREDesc) {
/*  54 */             if (this.val$selectedJRE[0] == null)
/*  55 */               LaunchSelection.handleJREDesc(param1JREDesc, this.val$selectedJRE, this.val$selectedJREDesc); 
/*     */           }
/*     */           
/*     */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {} }
/*     */       );
/*  60 */     arrayOfJREDesc[0].markAsSelected();
/*     */     
/*  62 */     resourcesDesc.addNested(arrayOfJREDesc[0].getNestedResources());
/*  63 */     return arrayOfJREInfo[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static JREInfo selectJRE(URL paramURL, String paramString) {
/*  68 */     JREInfo[] arrayOfJREInfo = JREInfo.get();
/*  69 */     if (arrayOfJREInfo == null) return null; 
/*  70 */     VersionString versionString = new VersionString(paramString);
/*  71 */     for (byte b = 0; b < arrayOfJREInfo.length; b++) {
/*     */ 
/*     */ 
/*     */       
/*  75 */       if (arrayOfJREInfo[b].isOsInfoMatch(Config.getOSName(), Config.getOSArch()))
/*     */       {
/*  77 */         if (arrayOfJREInfo[b].isEnabled())
/*     */         {
/*  79 */           if ((paramURL == null) ? isPlatformMatch(arrayOfJREInfo[b], versionString) : isProductMatch(arrayOfJREInfo[b], paramURL, versionString))
/*     */           {
/*     */             
/*  82 */             return arrayOfJREInfo[b];
/*     */           }
/*     */         }
/*     */       }
/*     */     } 
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleJREDesc(JREDesc paramJREDesc, JREInfo[] paramArrayOfJREInfo, JREDesc[] paramArrayOfJREDesc) {
/*  94 */     URL uRL = paramJREDesc.getHref();
/*  95 */     String str = paramJREDesc.getVersion();
/*     */ 
/*     */     
/*  98 */     StringTokenizer stringTokenizer = new StringTokenizer(str, " ", false);
/*  99 */     int i = stringTokenizer.countTokens();
/*     */     
/* 101 */     if (i > 0) {
/* 102 */       String[] arrayOfString = new String[i];
/* 103 */       for (byte b = 0; b < i; b++) {
/* 104 */         arrayOfString[b] = stringTokenizer.nextToken();
/*     */       }
/* 106 */       matchJRE(paramJREDesc, arrayOfString, paramArrayOfJREInfo, paramArrayOfJREDesc);
/* 107 */       if (paramArrayOfJREInfo[0] != null) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void matchJRE(JREDesc paramJREDesc, String[] paramArrayOfString, JREInfo[] paramArrayOfJREInfo, JREDesc[] paramArrayOfJREDesc) {
/* 117 */     URL uRL = paramJREDesc.getHref();
/*     */ 
/*     */ 
/*     */     
/* 121 */     JREInfo[] arrayOfJREInfo = JREInfo.get();
/* 122 */     if (arrayOfJREInfo == null)
/*     */       return; 
/* 124 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 125 */       VersionString versionString = new VersionString(paramArrayOfString[b]);
/* 126 */       for (byte b1 = 0; b1 < arrayOfJREInfo.length; b1++) {
/*     */ 
/*     */ 
/*     */         
/* 130 */         if (arrayOfJREInfo[b1].isOsInfoMatch(Config.getOSName(), Config.getOSArch()))
/*     */         {
/* 132 */           if (arrayOfJREInfo[b1].isEnabled()) {
/*     */             
/* 134 */             boolean bool1 = (uRL == null) ? isPlatformMatch(arrayOfJREInfo[b1], versionString) : isProductMatch(arrayOfJREInfo[b1], uRL, versionString);
/*     */ 
/*     */             
/* 137 */             boolean bool2 = JnlpxArgs.getJVMCommand().equals(new File(arrayOfJREInfo[b1].getPath()));
/*     */ 
/*     */             
/* 140 */             boolean bool3 = JnlpxArgs.isCurrentRunningJREHeap(paramJREDesc.getMinHeap(), paramJREDesc.getMaxHeap());
/*     */ 
/*     */ 
/*     */             
/* 144 */             if (bool1 && bool2 && bool3) {
/*     */               
/* 146 */               Trace.println("LaunchSelection: findJRE: Match on current JRE", TraceLevel.BASIC);
/*     */ 
/*     */               
/* 149 */               paramArrayOfJREInfo[0] = arrayOfJREInfo[b1];
/* 150 */               paramArrayOfJREDesc[0] = paramJREDesc; return;
/*     */             } 
/* 152 */             if (bool1) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 157 */               Trace.print("LaunchSelection: findJRE: No match on current JRE because ", TraceLevel.BASIC);
/* 158 */               if (!bool1) Trace.print("versions dont match, ", TraceLevel.BASIC); 
/* 159 */               if (!bool2) Trace.print("paths dont match, ", TraceLevel.BASIC); 
/* 160 */               if (!bool3) Trace.print("heap sizes dont match", TraceLevel.BASIC); 
/* 161 */               Trace.println("", TraceLevel.BASIC);
/*     */ 
/*     */ 
/*     */               
/* 165 */               VersionID versionID1 = new VersionID(arrayOfJREInfo[b1].getProduct());
/* 166 */               VersionID versionID2 = null;
/*     */               
/* 168 */               if (paramArrayOfJREInfo[0] != null) {
/* 169 */                 versionID2 = new VersionID(paramArrayOfJREInfo[0].getProduct());
/*     */               }
/*     */ 
/*     */               
/* 173 */               if (versionID2 == null || versionID1.isGreaterThan(versionID2)) {
/* 174 */                 paramArrayOfJREInfo[0] = arrayOfJREInfo[b1];
/* 175 */                 paramArrayOfJREDesc[0] = paramJREDesc;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     if (paramArrayOfJREDesc[0] == null) paramArrayOfJREDesc[0] = paramJREDesc;
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPlatformMatch(JREInfo paramJREInfo, VersionString paramVersionString) {
/*     */     boolean bool;
/* 192 */     String str = paramJREInfo.getProduct();
/* 193 */     if (str != null && str.indexOf('-') != -1 && !str.startsWith("1.2") && !isInstallJRE(paramJREInfo)) {
/*     */       
/* 195 */       bool = false;
/*     */     } else {
/* 197 */       bool = true;
/*     */     } 
/*     */     
/* 200 */     if ((new File(paramJREInfo.getPath())).exists()) {
/* 201 */       return (paramVersionString.contains(paramJREInfo.getPlatform()) && bool);
/*     */     }
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isProductMatch(JREInfo paramJREInfo, URL paramURL, VersionString paramVersionString) {
/* 208 */     if ((new File(paramJREInfo.getPath())).exists()) {
/* 209 */       return (paramJREInfo.getLocation().equals(paramURL.toString()) && paramVersionString.contains(paramJREInfo.getProduct()));
/*     */     }
/*     */     
/* 212 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isInstallJRE(JREInfo paramJREInfo) {
/* 216 */     File file1 = new File(Config.getJavaHome());
/* 217 */     File file2 = new File(paramJREInfo.getPath());
/* 218 */     File file3 = file2.getParentFile();
/* 219 */     return file1.equals(file3.getParentFile());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\LaunchSelection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */