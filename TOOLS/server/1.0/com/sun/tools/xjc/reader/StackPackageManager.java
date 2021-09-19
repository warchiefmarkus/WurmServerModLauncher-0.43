/*    */ package 1.0.com.sun.tools.xjc.reader;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.tools.xjc.reader.PackageManager;
/*    */ import java.util.Stack;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class StackPackageManager
/*    */   implements PackageManager
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   private final Stack stack;
/*    */   
/*    */   public final JPackage getCurrentPackage() {
/*    */     return this.stack.peek();
/*    */   }
/*    */   
/*    */   public StackPackageManager(JPackage pkg) {
/* 47 */     this.stack = new Stack();
/*    */     this.codeModel = pkg.owner();
/*    */     this.stack.push(pkg); } public final void startElement(Attributes atts) {
/* 50 */     if (atts.getIndex("http://java.sun.com/xml/ns/jaxb", "package") != -1) {
/*    */       
/* 52 */       String name = atts.getValue("http://java.sun.com/xml/ns/jaxb", "package");
/* 53 */       this.stack.push(this.codeModel._package(name));
/*    */     } else {
/*    */       
/* 56 */       this.stack.push(this.stack.peek());
/*    */     } 
/*    */   } public final void endElement() {
/* 59 */     this.stack.pop();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\StackPackageManager.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */