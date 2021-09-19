/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Vector;
/*     */ import javax.jnlp.FileContents;
/*     */ import javax.jnlp.FileOpenService;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.filechooser.FileSystemView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FileOpenServiceImpl
/*     */   implements FileOpenService
/*     */ {
/*  31 */   static FileOpenServiceImpl _sharedInstance = null;
/*     */ 
/*     */   
/*     */   static FileSaveServiceImpl _fileSaveServiceImpl;
/*     */ 
/*     */   
/*     */   private FileOpenServiceImpl(FileSaveServiceImpl paramFileSaveServiceImpl) {
/*  38 */     _fileSaveServiceImpl = paramFileSaveServiceImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized FileOpenService getInstance() {
/*  44 */     if (_sharedInstance == null) {
/*  45 */       _sharedInstance = new FileOpenServiceImpl((FileSaveServiceImpl)FileSaveServiceImpl.getInstance());
/*     */     }
/*  47 */     return _sharedInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public static FileSystemView getFileSystemView() {
/*  52 */     FileSystemView fileSystemView = FileSystemView.getFileSystemView();
/*     */     
/*  54 */     if (Config.getInstance().useAltFileSystemView()) {
/*     */       
/*  56 */       String str = System.getProperty("java.version");
/*     */ 
/*     */ 
/*     */       
/*  60 */       if (str.startsWith("1.2") || str.startsWith("1.3"))
/*     */       {
/*  62 */         fileSystemView = new WindowsAltFileSystemView();
/*     */       }
/*     */     } 
/*     */     
/*  66 */     return fileSystemView;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileContents openFileDialog(String paramString, String[] paramArrayOfString) throws IOException {
/*     */     try {
/*  73 */       if (!_fileSaveServiceImpl.askUser()) return null; 
/*  74 */       FileContents fileContents = AccessController.<FileContents>doPrivileged(new PrivilegedAction(this, paramString) { private final String val$pathHint;
/*     */             
/*     */             public Object run() {
/*  77 */               JFileChooser jFileChooser = null;
/*     */               
/*  79 */               FileSystemView fileSystemView = FileOpenServiceImpl.getFileSystemView();
/*     */ 
/*     */               
/*  82 */               if (this.val$pathHint != null) {
/*     */                 
/*  84 */                 jFileChooser = new JFileChooser(this.val$pathHint, fileSystemView);
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/*  89 */                 jFileChooser = new JFileChooser(FileOpenServiceImpl._fileSaveServiceImpl.getLastPath(), fileSystemView);
/*     */               } 
/*     */               
/*  92 */               jFileChooser.setFileSelectionMode(0);
/*  93 */               jFileChooser.setDialogType(0);
/*  94 */               jFileChooser.setMultiSelectionEnabled(false);
/*  95 */               int i = jFileChooser.showOpenDialog(null);
/*  96 */               if (i == 1)
/*     */               {
/*  98 */                 return null;
/*     */               }
/* 100 */               File file = jFileChooser.getSelectedFile();
/* 101 */               if (file != null) {
/*     */                 try {
/* 103 */                   FileOpenServiceImpl._fileSaveServiceImpl.setLastPath(file.getPath());
/* 104 */                   return new FileContentsImpl(file, FileSaveServiceImpl.computeMaxLength(file.length()));
/*     */                 
/*     */                 }
/* 107 */                 catch (FileNotFoundException fileNotFoundException) {
/*     */                 
/* 109 */                 } catch (IOException iOException) {}
/*     */               }
/*     */               
/* 112 */               return null;
/*     */             } private final FileOpenServiceImpl this$0; }
/*     */         );
/* 115 */       return fileContents;
/*     */     } finally {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileContents[] openMultiFileDialog(String paramString, String[] paramArrayOfString) throws IOException {
/*     */     try {
/* 125 */       if (!_fileSaveServiceImpl.askUser()) return null;
/*     */       
/* 127 */       FileContents[] arrayOfFileContents = AccessController.<FileContents[]>doPrivileged(new PrivilegedAction(this, paramString) { private final String val$pathHint; private final FileOpenServiceImpl this$0;
/*     */             
/*     */             public Object run() {
/* 130 */               JFileChooser jFileChooser = null;
/* 131 */               FileSystemView fileSystemView = FileOpenServiceImpl.getFileSystemView();
/*     */ 
/*     */               
/* 134 */               if (this.val$pathHint != null) {
/*     */                 
/* 136 */                 jFileChooser = new JFileChooser(this.val$pathHint, fileSystemView);
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/* 141 */                 jFileChooser = new JFileChooser(FileOpenServiceImpl._fileSaveServiceImpl.getLastPath(), fileSystemView);
/*     */               } 
/* 143 */               jFileChooser.setFileSelectionMode(0);
/* 144 */               jFileChooser.setDialogType(0);
/* 145 */               jFileChooser.setMultiSelectionEnabled(true);
/* 146 */               int i = jFileChooser.showOpenDialog(null);
/* 147 */               if (i == 1)
/*     */               {
/* 149 */                 return null;
/*     */               }
/* 151 */               File[] arrayOfFile = jFileChooser.getSelectedFiles();
/* 152 */               if (arrayOfFile != null && arrayOfFile.length > 0) {
/* 153 */                 FileContents[] arrayOfFileContents = new FileContents[arrayOfFile.length];
/* 154 */                 for (byte b = 0; b < arrayOfFile.length; b++) {
/*     */                   try {
/* 156 */                     arrayOfFileContents[b] = new FileContentsImpl(arrayOfFile[b], FileSaveServiceImpl.computeMaxLength(arrayOfFile[b].length()));
/* 157 */                     FileOpenServiceImpl._fileSaveServiceImpl.setLastPath(arrayOfFile[b].getPath());
/* 158 */                   } catch (FileNotFoundException fileNotFoundException) {
/*     */                   
/* 160 */                   } catch (IOException iOException) {}
/*     */                 } 
/*     */ 
/*     */                 
/* 164 */                 return arrayOfFileContents;
/*     */               } 
/* 166 */               return null;
/*     */             } }
/*     */         );
/* 169 */       return arrayOfFileContents;
/*     */     } finally {}
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
/*     */   static class WindowsAltFileSystemView
/*     */     extends FileSystemView
/*     */   {
/* 188 */     private static final Object[] noArgs = new Object[0];
/* 189 */     private static final Class[] noArgTypes = new Class[0];
/*     */     
/* 191 */     private static Method listRootsMethod = null;
/*     */ 
/*     */     
/*     */     private static boolean listRootsMethodChecked = false;
/*     */ 
/*     */     
/*     */     public boolean isRoot(File param1File) {
/* 198 */       if (!param1File.isAbsolute()) {
/* 199 */         return false;
/*     */       }
/*     */       
/* 202 */       String str = param1File.getParent();
/* 203 */       if (str == null) {
/* 204 */         return true;
/*     */       }
/* 206 */       File file = new File(str);
/* 207 */       return file.equals(param1File);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public File createNewFolder(File param1File) throws IOException {
/* 215 */       if (param1File == null) {
/* 216 */         throw new IOException("Containing directory is null:");
/*     */       }
/* 218 */       File file = null;
/*     */       
/* 220 */       file = createFileObject(param1File, "New Folder");
/* 221 */       byte b = 2;
/* 222 */       while (file.exists() && b < 100) {
/* 223 */         file = createFileObject(param1File, "New Folder (" + b + ")");
/* 224 */         b++;
/*     */       } 
/*     */       
/* 227 */       if (file.exists()) {
/* 228 */         throw new IOException("Directory already exists:" + file.getAbsolutePath());
/*     */       }
/* 230 */       file.mkdirs();
/*     */ 
/*     */       
/* 233 */       return file;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isHiddenFile(File param1File) {
/* 242 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public File[] getRoots() {
/* 250 */       Vector vector = new Vector();
/*     */ 
/*     */       
/* 253 */       FileSystemRoot fileSystemRoot = new FileSystemRoot(this, "A:\\");
/* 254 */       vector.addElement(fileSystemRoot);
/*     */ 
/*     */ 
/*     */       
/* 258 */       for (char c = 'C'; c <= 'Z'; c = (char)(c + 1)) {
/* 259 */         char[] arrayOfChar = { c, ':', '\\' };
/* 260 */         String str = new String(arrayOfChar);
/* 261 */         FileSystemRoot fileSystemRoot1 = new FileSystemRoot(this, str);
/* 262 */         if (fileSystemRoot1 != null && fileSystemRoot1.exists()) {
/* 263 */           vector.addElement(fileSystemRoot1);
/*     */         }
/*     */       } 
/* 266 */       File[] arrayOfFile = new File[vector.size()];
/* 267 */       vector.copyInto((Object[])arrayOfFile);
/* 268 */       return arrayOfFile;
/*     */     }
/*     */     class FileSystemRoot extends File { private final FileOpenServiceImpl.WindowsAltFileSystemView this$0;
/*     */       
/*     */       public FileSystemRoot(FileOpenServiceImpl.WindowsAltFileSystemView this$0, File param2File) {
/* 273 */         super(param2File, "");
/*     */         this.this$0 = this$0;
/*     */       }
/*     */       public FileSystemRoot(FileOpenServiceImpl.WindowsAltFileSystemView this$0, String param2String) {
/* 277 */         super(param2String);
/*     */         this.this$0 = this$0;
/*     */       }
/*     */       public boolean isDirectory() {
/* 281 */         return true;
/*     */       } }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\FileOpenServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */