/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.jnlp.ExtendedService;
/*     */ import javax.jnlp.FileContents;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtendedServiceImpl
/*     */   implements ExtendedService
/*     */ {
/*  18 */   private static ExtendedServiceImpl _sharedInstance = null;
/*     */ 
/*     */   
/*  21 */   private static int DEFAULT_FILESIZE = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized ExtendedServiceImpl getInstance() {
/*  27 */     if (_sharedInstance == null) {
/*  28 */       _sharedInstance = new ExtendedServiceImpl();
/*     */     }
/*  30 */     return _sharedInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileContents openFile(File paramFile) throws IOException {
/*  36 */     if (!askUser(paramFile.getPath())) return null;
/*     */ 
/*     */     
/*  39 */     FileContents fileContents = (FileContents)AccessController.doPrivileged(new PrivilegedAction(this, paramFile) { private final File val$file;
/*     */           public Object run() {
/*     */             try {
/*  42 */               return new FileContentsImpl(this.val$file, ExtendedServiceImpl.DEFAULT_FILESIZE);
/*  43 */             } catch (IOException iOException) {
/*  44 */               return iOException;
/*     */             } 
/*     */           }
/*     */           private final ExtendedServiceImpl this$0; }
/*     */       );
/*  49 */     if (fileContents instanceof IOException) {
/*  50 */       throw (IOException)fileContents;
/*     */     }
/*  52 */     return fileContents;
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized boolean askUser(String paramString) {
/*  57 */     SmartSecurityDialog smartSecurityDialog = new SmartSecurityDialog();
/*     */ 
/*     */     
/*  60 */     JTextArea jTextArea = new JTextArea(4, 30);
/*  61 */     jTextArea.setFont(ResourceManager.getUIFont());
/*  62 */     jTextArea.setEditable(false);
/*     */     
/*  64 */     jTextArea.append(paramString);
/*     */     
/*  66 */     JScrollPane jScrollPane = new JScrollPane(jTextArea);
/*     */     
/*  68 */     String str1 = ResourceManager.getString("APIImpl.extended.fileOpen.message1");
/*     */     
/*  70 */     String str2 = ResourceManager.getString("APIImpl.extended.fileOpen.message2");
/*     */     
/*  72 */     Object[] arrayOfObject = { str1, jScrollPane, str2 };
/*     */ 
/*     */     
/*  75 */     return smartSecurityDialog.showDialog(arrayOfObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileContents[] openFiles(File[] paramArrayOfFile) throws IOException {
/*  80 */     if (paramArrayOfFile == null || paramArrayOfFile.length <= 0) return null;
/*     */ 
/*     */ 
/*     */     
/*  84 */     String str = "";
/*  85 */     for (byte b = 0; b < paramArrayOfFile.length; b++) {
/*  86 */       str = str + paramArrayOfFile[b].getPath() + "\n";
/*     */     }
/*     */     
/*  89 */     if (!askUser(str)) return null;
/*     */     
/*  91 */     Object[] arrayOfObject = AccessController.<Object[]>doPrivileged(new PrivilegedAction(this, paramArrayOfFile) { private final File[] val$files; private final ExtendedServiceImpl this$0;
/*     */           public Object run() {
/*  93 */             FileContents[] arrayOfFileContents = new FileContents[this.val$files.length];
/*     */             try {
/*  95 */               for (byte b = 0; b < this.val$files.length; b++) {
/*  96 */                 arrayOfFileContents[b] = new FileContentsImpl(this.val$files[b], ExtendedServiceImpl.DEFAULT_FILESIZE);
/*     */               }
/*  98 */             } catch (IOException iOException) {
/*  99 */               arrayOfFileContents[0] = (FileContents)iOException;
/*     */             } 
/* 101 */             return arrayOfFileContents;
/*     */           } }
/*     */       );
/*     */     
/* 105 */     if (arrayOfObject[0] instanceof IOException) {
/* 106 */       throw (IOException)arrayOfObject[0];
/*     */     }
/* 108 */     return (FileContents[])arrayOfObject;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\ExtendedServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */