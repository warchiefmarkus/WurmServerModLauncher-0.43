/*    */ package 1.0.com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileCodeWriter
/*    */   implements CodeWriter
/*    */ {
/*    */   private final File target;
/*    */   private final boolean readOnly;
/* 33 */   private final Set readonlyFiles = new HashSet();
/*    */   
/*    */   public FileCodeWriter(File target) throws IOException {
/* 36 */     this(target, false);
/*    */   }
/*    */   
/*    */   public FileCodeWriter(File target, boolean readOnly) throws IOException {
/* 40 */     this.target = target;
/* 41 */     this.readOnly = readOnly;
/* 42 */     if (!target.exists() || !target.isDirectory()) {
/* 43 */       throw new IOException(target + ": non-existent directory");
/*    */     }
/*    */   }
/*    */   
/*    */   public OutputStream open(JPackage pkg, String fileName) throws IOException {
/* 48 */     return new FileOutputStream(getFile(pkg, fileName));
/*    */   }
/*    */   
/*    */   protected File getFile(JPackage pkg, String fileName) throws IOException {
/*    */     File dir;
/* 53 */     if (pkg.isUnnamed()) {
/* 54 */       dir = this.target;
/*    */     } else {
/* 56 */       dir = new File(this.target, toDirName(pkg));
/*    */     } 
/* 58 */     if (!dir.exists()) dir.mkdirs();
/*    */     
/* 60 */     File fn = new File(dir, fileName);
/*    */     
/* 62 */     if (fn.exists() && 
/* 63 */       !fn.delete()) {
/* 64 */       throw new IOException(fn + ": Can't delete previous version");
/*    */     }
/*    */ 
/*    */     
/* 68 */     if (this.readOnly) this.readonlyFiles.add(fn); 
/* 69 */     return fn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 74 */     for (Iterator itr = this.readonlyFiles.iterator(); itr.hasNext(); ) {
/* 75 */       File f = itr.next();
/* 76 */       f.setReadOnly();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private String toDirName(JPackage pkg) {
/* 82 */     return pkg.name().replace('.', File.separatorChar);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\writer\FileCodeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */