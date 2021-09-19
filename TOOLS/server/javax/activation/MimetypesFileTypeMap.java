/*     */ package javax.activation;
/*     */ 
/*     */ import com.sun.activation.registries.LogSupport;
/*     */ import com.sun.activation.registries.MimeTypeFile;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimetypesFileTypeMap
/*     */   extends FileTypeMap
/*     */ {
/*  89 */   private static MimeTypeFile defDB = null;
/*     */   
/*     */   private MimeTypeFile[] DB;
/*     */   private static final int PROG = 0;
/*  93 */   private static String defaultType = "application/octet-stream";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimetypesFileTypeMap() {
/*  99 */     Vector dbv = new Vector(5);
/* 100 */     MimeTypeFile mf = null;
/* 101 */     dbv.addElement(null);
/*     */     
/* 103 */     LogSupport.log("MimetypesFileTypeMap: load HOME");
/*     */     try {
/* 105 */       String user_home = System.getProperty("user.home");
/*     */       
/* 107 */       if (user_home != null) {
/* 108 */         String path = user_home + File.separator + ".mime.types";
/* 109 */         mf = loadFile(path);
/* 110 */         if (mf != null)
/* 111 */           dbv.addElement(mf); 
/*     */       } 
/* 113 */     } catch (SecurityException ex) {}
/*     */     
/* 115 */     LogSupport.log("MimetypesFileTypeMap: load SYS");
/*     */     
/*     */     try {
/* 118 */       String system_mimetypes = System.getProperty("java.home") + File.separator + "lib" + File.separator + "mime.types";
/*     */       
/* 120 */       mf = loadFile(system_mimetypes);
/* 121 */       if (mf != null)
/* 122 */         dbv.addElement(mf); 
/* 123 */     } catch (SecurityException ex) {}
/*     */     
/* 125 */     LogSupport.log("MimetypesFileTypeMap: load JAR");
/*     */     
/* 127 */     loadAllResources(dbv, "META-INF/mime.types");
/*     */     
/* 129 */     LogSupport.log("MimetypesFileTypeMap: load DEF");
/* 130 */     synchronized (MimetypesFileTypeMap.class) {
/*     */       
/* 132 */       if (defDB == null) {
/* 133 */         defDB = loadResource("/META-INF/mimetypes.default");
/*     */       }
/*     */     } 
/* 136 */     if (defDB != null) {
/* 137 */       dbv.addElement(defDB);
/*     */     }
/* 139 */     this.DB = new MimeTypeFile[dbv.size()];
/* 140 */     dbv.copyInto((Object[])this.DB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MimeTypeFile loadResource(String name) {
/* 147 */     InputStream clis = null;
/*     */     try {
/* 149 */       clis = SecuritySupport.getResourceAsStream(getClass(), name);
/* 150 */       if (clis != null) {
/* 151 */         MimeTypeFile mf = new MimeTypeFile(clis);
/* 152 */         if (LogSupport.isLoggable()) {
/* 153 */           LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types file: " + name);
/*     */         }
/* 155 */         return mf;
/*     */       } 
/* 157 */       if (LogSupport.isLoggable()) {
/* 158 */         LogSupport.log("MimetypesFileTypeMap: not loading mime types file: " + name);
/*     */       }
/*     */     }
/* 161 */     catch (IOException e) {
/* 162 */       if (LogSupport.isLoggable())
/* 163 */         LogSupport.log("MimetypesFileTypeMap: can't load " + name, e); 
/* 164 */     } catch (SecurityException sex) {
/* 165 */       if (LogSupport.isLoggable())
/* 166 */         LogSupport.log("MimetypesFileTypeMap: can't load " + name, sex); 
/*     */     } finally {
/*     */       try {
/* 169 */         if (clis != null)
/* 170 */           clis.close(); 
/* 171 */       } catch (IOException ex) {}
/*     */     } 
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadAllResources(Vector v, String name) {
/* 180 */     boolean anyLoaded = false;
/*     */     try {
/*     */       URL[] urls;
/* 183 */       ClassLoader cld = null;
/*     */       
/* 185 */       cld = SecuritySupport.getContextClassLoader();
/* 186 */       if (cld == null)
/* 187 */         cld = getClass().getClassLoader(); 
/* 188 */       if (cld != null) {
/* 189 */         urls = SecuritySupport.getResources(cld, name);
/*     */       } else {
/* 191 */         urls = SecuritySupport.getSystemResources(name);
/* 192 */       }  if (urls != null) {
/* 193 */         if (LogSupport.isLoggable())
/* 194 */           LogSupport.log("MimetypesFileTypeMap: getResources"); 
/* 195 */         for (int i = 0; i < urls.length; i++) {
/* 196 */           URL url = urls[i];
/* 197 */           InputStream clis = null;
/* 198 */           if (LogSupport.isLoggable());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
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
/*     */     }
/* 231 */     catch (Exception ex) {
/* 232 */       if (LogSupport.isLoggable()) {
/* 233 */         LogSupport.log("MimetypesFileTypeMap: can't load " + name, ex);
/*     */       }
/*     */     } 
/*     */     
/* 237 */     if (!anyLoaded) {
/* 238 */       LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
/* 239 */       MimeTypeFile mf = loadResource("/" + name);
/* 240 */       if (mf != null) {
/* 241 */         v.addElement(mf);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MimeTypeFile loadFile(String name) {
/* 249 */     MimeTypeFile mtf = null;
/*     */     
/*     */     try {
/* 252 */       mtf = new MimeTypeFile(name);
/* 253 */     } catch (IOException e) {}
/*     */ 
/*     */     
/* 256 */     return mtf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimetypesFileTypeMap(String mimeTypeFileName) throws IOException {
/* 266 */     this();
/* 267 */     this.DB[0] = new MimeTypeFile(mimeTypeFileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimetypesFileTypeMap(InputStream is) {
/* 277 */     this();
/*     */     try {
/* 279 */       this.DB[0] = new MimeTypeFile(is);
/* 280 */     } catch (IOException ex) {}
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
/*     */   public synchronized void addMimeTypes(String mime_types) {
/* 292 */     if (this.DB[0] == null) {
/* 293 */       this.DB[0] = new MimeTypeFile();
/*     */     }
/* 295 */     this.DB[0].appendToRegistry(mime_types);
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
/*     */   public String getContentType(File f) {
/* 307 */     return getContentType(f.getName());
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
/*     */   public synchronized String getContentType(String filename) {
/* 320 */     int dot_pos = filename.lastIndexOf(".");
/*     */     
/* 322 */     if (dot_pos < 0) {
/* 323 */       return defaultType;
/*     */     }
/* 325 */     String file_ext = filename.substring(dot_pos + 1);
/* 326 */     if (file_ext.length() == 0) {
/* 327 */       return defaultType;
/*     */     }
/* 329 */     for (int i = 0; i < this.DB.length; i++) {
/* 330 */       if (this.DB[i] != null) {
/*     */         
/* 332 */         String result = this.DB[i].getMIMETypeString(file_ext);
/* 333 */         if (result != null)
/* 334 */           return result; 
/*     */       } 
/* 336 */     }  return defaultType;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\MimetypesFileTypeMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */