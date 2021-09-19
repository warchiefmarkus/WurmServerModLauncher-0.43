/*    */ package 1.0.com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
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
/*    */ public class SingleStreamCodeWriter
/*    */   implements CodeWriter
/*    */ {
/*    */   private final PrintStream out;
/*    */   
/*    */   public SingleStreamCodeWriter(OutputStream os) {
/* 34 */     this.out = new PrintStream(os);
/*    */   }
/*    */   
/*    */   public OutputStream open(JPackage pkg, String fileName) throws IOException {
/* 38 */     String pkgName = pkg.name();
/* 39 */     if (pkgName.length() != 0) pkgName = pkgName + '.';
/*    */     
/* 41 */     this.out.println("-----------------------------------" + pkgName + fileName + "-----------------------------------");
/*    */ 
/*    */ 
/*    */     
/* 45 */     return (OutputStream)new Object(this, this.out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 53 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\writer\SingleStreamCodeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */