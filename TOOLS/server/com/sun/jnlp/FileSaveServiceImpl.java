/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.DialogFactory;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.jnlp.FileContents;
/*     */ import javax.jnlp.FileSaveService;
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
/*     */ public final class FileSaveServiceImpl
/*     */   implements FileSaveService
/*     */ {
/*  30 */   static FileSaveServiceImpl _sharedInstance = null;
/*     */   
/*  32 */   private SmartSecurityDialog _securityDialog = null;
/*     */   
/*     */   private String _lastPath;
/*     */   
/*     */   private FileSaveServiceImpl() {
/*  37 */     this._securityDialog = new SmartSecurityDialog(ResourceManager.getString("APIImpl.file.save.message"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized FileSaveService getInstance() {
/*  42 */     if (_sharedInstance == null) {
/*  43 */       _sharedInstance = new FileSaveServiceImpl();
/*     */     }
/*  45 */     return _sharedInstance;
/*     */   }
/*     */   
/*     */   String getLastPath() {
/*  49 */     return this._lastPath; } void setLastPath(String paramString) {
/*  50 */     this._lastPath = paramString;
/*     */   }
/*     */   
/*     */   public FileContents saveAsFileDialog(String paramString, String[] paramArrayOfString, FileContents paramFileContents) throws IOException {
/*  54 */     return saveFileDialog(paramString, paramArrayOfString, paramFileContents.getInputStream(), paramFileContents.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileContents saveFileDialog(String paramString1, String[] paramArrayOfString, InputStream paramInputStream, String paramString2) throws IOException {
/*     */     try {
/*  63 */       if (!askUser()) return null;
/*     */       
/*  65 */       FileContents fileContents = (FileContents)AccessController.doPrivileged(new PrivilegedAction(this, paramString1, paramInputStream) { private final String val$pathHint;
/*     */             public Object run() {
/*  67 */               JFileChooser jFileChooser = null;
/*  68 */               FileSystemView fileSystemView = FileOpenServiceImpl.getFileSystemView();
/*     */               
/*  70 */               if (this.val$pathHint != null) {
/*     */                 
/*  72 */                 jFileChooser = new JFileChooser(this.val$pathHint, fileSystemView);
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/*  77 */                 jFileChooser = new JFileChooser(this.this$0.getLastPath(), fileSystemView);
/*     */               } 
/*     */ 
/*     */               
/*  81 */               jFileChooser.setFileSelectionMode(0);
/*  82 */               jFileChooser.setDialogType(1);
/*  83 */               jFileChooser.setMultiSelectionEnabled(false);
/*  84 */               int i = jFileChooser.showSaveDialog(null);
/*  85 */               if (i == 1) {
/*  86 */                 return null;
/*     */               }
/*  88 */               File file = jFileChooser.getSelectedFile();
/*  89 */               if (file != null) {
/*     */                 
/*  91 */                 if (!FileSaveServiceImpl.fileChk(file)) {
/*  92 */                   return null;
/*     */                 }
/*     */                 try {
/*  95 */                   byte[] arrayOfByte = new byte[8192];
/*  96 */                   BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
/*  97 */                   BufferedInputStream bufferedInputStream = new BufferedInputStream(this.val$stream);
/*  98 */                   int j = bufferedInputStream.read(arrayOfByte);
/*  99 */                   while (j != -1) {
/* 100 */                     bufferedOutputStream.write(arrayOfByte, 0, j);
/* 101 */                     j = bufferedInputStream.read(arrayOfByte);
/*     */                   } 
/* 103 */                   bufferedOutputStream.close();
/* 104 */                   this.this$0.setLastPath(file.getPath());
/* 105 */                   return new FileContentsImpl(file, FileSaveServiceImpl.computeMaxLength(file.length()));
/* 106 */                 } catch (IOException iOException) {
/*     */                   
/* 108 */                   return iOException;
/*     */                 } 
/*     */               } 
/* 111 */               return null;
/*     */             } private final InputStream val$stream; private final FileSaveServiceImpl this$0; }
/*     */         );
/* 114 */       if (fileContents instanceof IOException) {
/* 115 */         throw (IOException)fileContents;
/*     */       }
/* 117 */       return fileContents;
/*     */     } finally {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized boolean askUser() {
/* 124 */     if (CheckServicePermission.hasFileAccessPermissions()) return true;
/*     */     
/* 126 */     return this._securityDialog.showDialog();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static long computeMaxLength(long paramLong) {
/* 132 */     return paramLong * 3L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean fileChk(File paramFile) {
/* 138 */     if (paramFile.exists()) {
/* 139 */       String str1 = ResourceManager.getString("APIImpl.file.save.fileExist", paramFile.getPath());
/*     */ 
/*     */       
/* 142 */       String str2 = ResourceManager.getMessage("APIImpl.file.save.fileExistTitle");
/*     */ 
/*     */       
/* 145 */       int i = DialogFactory.showConfirmDialog(str1, str2);
/* 146 */       return (i == 0);
/*     */     } 
/*     */     
/* 149 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\FileSaveServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */