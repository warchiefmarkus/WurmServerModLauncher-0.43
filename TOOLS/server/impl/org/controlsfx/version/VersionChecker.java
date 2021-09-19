/*     */ package impl.org.controlsfx.version;
/*     */ 
/*     */ import com.sun.javafx.runtime.VersionInfo;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
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
/*     */ public class VersionChecker
/*     */ {
/*     */   private static final String javaFXVersion;
/*     */   private static final String controlsFXSpecTitle;
/*     */   private static final String controlsFXSpecVersion;
/*     */   private static final String controlsFXImpVersion;
/*  48 */   private static final Package controlsFX = VersionChecker.class.getPackage();
/*     */   static {
/*  50 */     javaFXVersion = VersionInfo.getVersion();
/*  51 */     controlsFXSpecTitle = getControlsFXSpecificationTitle();
/*  52 */     controlsFXSpecVersion = getControlsFXSpecificationVersion();
/*  53 */     controlsFXImpVersion = getControlsFXImplementationVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Properties props;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void doVersionCheck() {
/*  72 */     if (controlsFXSpecVersion == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     Comparable[] splitSpecVersion = (Comparable[])toComparable(controlsFXSpecVersion.split("\\."));
/*     */ 
/*     */     
/*  81 */     Comparable[] splitJavaVersion = (Comparable[])toComparable(javaFXVersion.replace('-', '.').split("\\."));
/*     */     
/*  83 */     boolean notSupportedVersion = false;
/*     */ 
/*     */     
/*  86 */     if (splitSpecVersion[0].compareTo(splitJavaVersion[0]) > 0) {
/*  87 */       notSupportedVersion = true;
/*  88 */     } else if (splitSpecVersion[0].compareTo(splitJavaVersion[0]) == 0) {
/*     */       
/*  90 */       if (splitSpecVersion[1].compareTo(splitJavaVersion[2]) > 0) {
/*  91 */         notSupportedVersion = true;
/*     */       }
/*     */     } 
/*     */     
/*  95 */     if (notSupportedVersion) {
/*  96 */       throw new RuntimeException("ControlsFX Error: ControlsFX " + controlsFXImpVersion + " requires at least " + controlsFXSpecTitle);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Comparable<Comparable>[] toComparable(String[] tokens) {
/* 102 */     Comparable[] ret = new Comparable[tokens.length];
/* 103 */     for (int i = 0; i < tokens.length; i++) {
/* 104 */       String token = tokens[i];
/*     */       try {
/* 106 */         ret[i] = new Integer(token);
/*     */       }
/* 108 */       catch (NumberFormatException e) {
/* 109 */         ret[i] = token;
/*     */       } 
/*     */     } 
/* 112 */     return (Comparable<Comparable>[])ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getControlsFXSpecificationTitle() {
/*     */     try {
/* 118 */       return controlsFX.getSpecificationTitle();
/* 119 */     } catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       return getPropertyValue("controlsfx_specification_title");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getControlsFXSpecificationVersion() {
/*     */     try {
/* 149 */       return controlsFX.getSpecificationVersion();
/* 150 */     } catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 156 */       return getPropertyValue("controlsfx_specification_title");
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getControlsFXImplementationVersion() {
/*     */     try {
/* 179 */       return controlsFX.getImplementationVersion();
/* 180 */     } catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 187 */       return getPropertyValue("controlsfx_specification_title") + 
/* 188 */         getPropertyValue("artifact_suffix");
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized String getPropertyValue(String key) {
/* 210 */     if (props == null) {
/*     */       try {
/* 212 */         File file = new File("../controlsfx-build.properties");
/* 213 */         if (file.exists()) {
/* 214 */           props.load(new FileReader(file));
/*     */         }
/* 216 */       } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 220 */     return props.getProperty(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\version\VersionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */