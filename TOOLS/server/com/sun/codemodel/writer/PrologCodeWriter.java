/*    */ package com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.Writer;
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
/*    */   extends FilterCodeWriter
/*    */ {
/*    */   private final String prolog;
/*    */   
/*    */   public PrologCodeWriter(CodeWriter core, String prolog) {
/* 54 */     super(core);
/* 55 */     this.prolog = prolog;
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 60 */     Writer w = super.openSource(pkg, fileName);
/*    */     
/* 62 */     PrintWriter out = new PrintWriter(w);
/*    */ 
/*    */     
/* 65 */     if (this.prolog != null) {
/* 66 */       out.println("//");
/*    */       
/* 68 */       String s = this.prolog;
/*    */       int idx;
/* 70 */       while ((idx = s.indexOf('\n')) != -1) {
/* 71 */         out.println("// " + s.substring(0, idx));
/* 72 */         s = s.substring(idx + 1);
/*    */       } 
/* 74 */       out.println("//");
/* 75 */       out.println();
/*    */     } 
/* 77 */     out.flush();
/*    */     
/* 79 */     return w;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\writer\PrologCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */