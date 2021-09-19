/*     */ package org.flywaydb.core.internal.util.scanner.filesystem;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.util.FileCopyUtils;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
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
/*     */ public class FileSystemResource
/*     */   implements Resource, Comparable<FileSystemResource>
/*     */ {
/*     */   private File location;
/*     */   
/*     */   public FileSystemResource(String location) {
/*  46 */     this.location = new File(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/*  53 */     return StringUtils.replaceAll(this.location.getPath(), "\\", "/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocationOnDisk() {
/*  62 */     return this.location.getAbsolutePath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String loadAsString(String encoding) {
/*     */     try {
/*  73 */       InputStream inputStream = new FileInputStream(this.location);
/*  74 */       Reader reader = new InputStreamReader(inputStream, Charset.forName(encoding));
/*     */       
/*  76 */       return FileCopyUtils.copyToString(reader);
/*  77 */     } catch (IOException e) {
/*  78 */       throw new FlywayException("Unable to load filesystem resource: " + this.location.getPath() + " (encoding: " + encoding + ")", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] loadAsBytes() {
/*     */     try {
/*  89 */       InputStream inputStream = new FileInputStream(this.location);
/*  90 */       return FileCopyUtils.copyToByteArray(inputStream);
/*  91 */     } catch (IOException e) {
/*  92 */       throw new FlywayException("Unable to load filesystem resource: " + this.location.getPath(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 100 */     return this.location.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(FileSystemResource o) {
/* 105 */     return this.location.compareTo(o.location);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\filesystem\FileSystemResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */