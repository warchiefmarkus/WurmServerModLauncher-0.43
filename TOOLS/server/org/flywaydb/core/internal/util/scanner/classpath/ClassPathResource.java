/*     */ package org.flywaydb.core.internal.util.scanner.classpath;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.Charset;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.util.FileCopyUtils;
/*     */ import org.flywaydb.core.internal.util.scanner.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathResource
/*     */   implements Comparable<ClassPathResource>, Resource
/*     */ {
/*     */   private String location;
/*     */   private ClassLoader classLoader;
/*     */   
/*     */   public ClassPathResource(String location, ClassLoader classLoader) {
/*  53 */     this.location = location;
/*  54 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  58 */     return this.location;
/*     */   }
/*     */   
/*     */   public String getLocationOnDisk() {
/*  62 */     URL url = getUrl();
/*  63 */     if (url == null) {
/*  64 */       throw new FlywayException("Unable to location resource on disk: " + this.location);
/*     */     }
/*     */     try {
/*  67 */       return (new File(URLDecoder.decode(url.getPath(), "UTF-8"))).getAbsolutePath();
/*  68 */     } catch (UnsupportedEncodingException e) {
/*  69 */       throw new FlywayException("Unknown encoding: UTF-8", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URL getUrl() {
/*  77 */     return this.classLoader.getResource(this.location);
/*     */   }
/*     */   
/*     */   public String loadAsString(String encoding) {
/*     */     try {
/*  82 */       InputStream inputStream = this.classLoader.getResourceAsStream(this.location);
/*  83 */       if (inputStream == null) {
/*  84 */         throw new FlywayException("Unable to obtain inputstream for resource: " + this.location);
/*     */       }
/*  86 */       Reader reader = new InputStreamReader(inputStream, Charset.forName(encoding));
/*     */       
/*  88 */       return FileCopyUtils.copyToString(reader);
/*  89 */     } catch (IOException e) {
/*  90 */       throw new FlywayException("Unable to load resource: " + this.location + " (encoding: " + encoding + ")", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] loadAsBytes() {
/*     */     try {
/*  96 */       InputStream inputStream = this.classLoader.getResourceAsStream(this.location);
/*  97 */       if (inputStream == null) {
/*  98 */         throw new FlywayException("Unable to obtain inputstream for resource: " + this.location);
/*     */       }
/* 100 */       return FileCopyUtils.copyToByteArray(inputStream);
/* 101 */     } catch (IOException e) {
/* 102 */       throw new FlywayException("Unable to load resource: " + this.location, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getFilename() {
/* 107 */     return this.location.substring(this.location.lastIndexOf("/") + 1);
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 111 */     return (getUrl() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 117 */     if (this == o) return true; 
/* 118 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 120 */     ClassPathResource that = (ClassPathResource)o;
/*     */     
/* 122 */     if (!this.location.equals(that.location)) return false;
/*     */     
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 129 */     return this.location.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(ClassPathResource o) {
/* 134 */     return this.location.compareTo(o.location);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\ClassPathResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */