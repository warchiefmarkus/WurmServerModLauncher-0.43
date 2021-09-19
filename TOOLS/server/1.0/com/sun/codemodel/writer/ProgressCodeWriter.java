/*    */ package 1.0.com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProgressCodeWriter
/*    */   implements CodeWriter
/*    */ {
/*    */   private final CodeWriter output;
/*    */   private final PrintStream progress;
/*    */   
/*    */   public ProgressCodeWriter(CodeWriter output, PrintStream progress) {
/* 24 */     this.output = output;
/* 25 */     this.progress = progress;
/* 26 */     if (progress == null) {
/* 27 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream open(JPackage pkg, String fileName) throws IOException {
/* 34 */     if (pkg.isUnnamed()) { this.progress.println(fileName); }
/*    */     else
/* 36 */     { this.progress.println(pkg.name().replace('.', File.separatorChar) + File.separatorChar + fileName); }
/*    */ 
/*    */ 
/*    */     
/* 40 */     return this.output.open(pkg, fileName);
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 44 */     this.output.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\writer\ProgressCodeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */