/*    */ package 1.0.com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintWriter;
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
/*    */ public class PrologCodeWriter
/*    */   implements CodeWriter
/*    */ {
/*    */   private final CodeWriter core;
/*    */   private final String prolog;
/*    */   
/*    */   public PrologCodeWriter(CodeWriter core, String prolog) throws IOException {
/* 41 */     this.core = core;
/* 42 */     this.prolog = prolog;
/*    */   }
/*    */ 
/*    */   
/*    */   public OutputStream open(JPackage pkg, String fileName) throws IOException {
/* 47 */     OutputStream fos = this.core.open(pkg, fileName);
/*    */     
/* 49 */     PrintWriter out = new PrintWriter(fos);
/*    */ 
/*    */     
/* 52 */     if (this.prolog != null && fileName.endsWith(".java")) {
/* 53 */       out.println("//");
/*    */       
/* 55 */       String s = this.prolog;
/*    */       int idx;
/* 57 */       while ((idx = s.indexOf('\n')) != -1) {
/* 58 */         out.println("// " + s.substring(0, idx));
/* 59 */         s = s.substring(idx + 1);
/*    */       } 
/* 61 */       out.println("//");
/* 62 */       out.println();
/*    */     } 
/* 64 */     out.flush();
/*    */     
/* 66 */     return fos;
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 70 */     this.core.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\writer\PrologCodeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */