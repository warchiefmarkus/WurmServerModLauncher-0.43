/*    */ package 1.0.com.sun.codemodel.fmt;
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
/*    */ public class JTextFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private String contents;
/*    */   
/*    */   public JTextFile(String name) {
/* 24 */     super(name);
/*    */ 
/*    */     
/* 27 */     this.contents = null;
/*    */   }
/*    */   public void setContents(String _contents) {
/* 30 */     this.contents = _contents;
/*    */   }
/*    */   
/*    */   public void build(OutputStream out) throws IOException {
/* 34 */     Writer w = new OutputStreamWriter(out);
/* 35 */     w.write(this.contents);
/* 36 */     w.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\fmt\JTextFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */