/*    */ package 1.0.com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
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
/*    */ public class ZipCodeWriter
/*    */   implements CodeWriter
/*    */ {
/*    */   private final ZipOutputStream zip;
/*    */   private final OutputStream filter;
/*    */   
/*    */   public ZipCodeWriter(OutputStream target) {
/* 28 */     this.zip = new ZipOutputStream(target);
/*    */     
/* 30 */     this.filter = (OutputStream)new Object(this, this.zip);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream open(JPackage pkg, String fileName) throws IOException {
/* 40 */     String name = fileName;
/* 41 */     if (!pkg.isUnnamed()) name = toDirName(pkg) + name;
/*    */     
/* 43 */     this.zip.putNextEntry(new ZipEntry(name));
/* 44 */     return this.filter;
/*    */   }
/*    */ 
/*    */   
/*    */   private String toDirName(JPackage pkg) {
/* 49 */     return pkg.name().replace('.', '/') + '/';
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 53 */     this.zip.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\writer\ZipCodeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */