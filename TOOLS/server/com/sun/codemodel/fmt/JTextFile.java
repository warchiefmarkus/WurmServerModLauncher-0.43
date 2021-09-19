/*    */ package com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
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
/*    */ public class JTextFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private String contents;
/*    */   
/*    */   public JTextFile(String name) {
/* 39 */     super(name);
/*    */ 
/*    */     
/* 42 */     this.contents = null;
/*    */   }
/*    */   public void setContents(String _contents) {
/* 45 */     this.contents = _contents;
/*    */   }
/*    */   
/*    */   public void build(OutputStream out) throws IOException {
/* 49 */     Writer w = new OutputStreamWriter(out);
/* 50 */     w.write(this.contents);
/* 51 */     w.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\fmt\JTextFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */