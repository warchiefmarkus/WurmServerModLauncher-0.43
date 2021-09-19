/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileDataSource
/*     */   implements DataSource
/*     */ {
/*  75 */   private File _file = null;
/*  76 */   private FileTypeMap typeMap = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileDataSource(File file) {
/*  86 */     this._file = file;
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
/*     */   public FileDataSource(String name) {
/*  98 */     this(new File(name));
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
/*     */   public InputStream getInputStream() throws IOException {
/* 110 */     return new FileInputStream(this._file);
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 122 */     return new FileOutputStream(this._file);
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
/*     */   public String getContentType() {
/* 138 */     if (this.typeMap == null) {
/* 139 */       return FileTypeMap.getDefaultFileTypeMap().getContentType(this._file);
/*     */     }
/* 141 */     return this.typeMap.getContentType(this._file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 152 */     return this._file.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 160 */     return this._file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileTypeMap(FileTypeMap map) {
/* 169 */     this.typeMap = map;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\FileDataSource.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */