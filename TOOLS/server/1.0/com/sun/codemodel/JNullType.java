/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.util.Iterator;
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
/*    */ public final class JNullType
/*    */   extends JClass
/*    */ {
/*    */   JNullType(JCodeModel _owner) {
/* 22 */     super(_owner);
/*    */   }
/*    */   public String name() {
/* 25 */     return "null";
/*    */   } public JPackage _package() {
/* 27 */     return owner()._package("");
/*    */   } public JClass _extends() {
/* 29 */     return null;
/*    */   } public Iterator _implements() {
/* 31 */     return (Iterator)new Object(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isInterface() {
/* 38 */     return false;
/*    */   }
/*    */   public void generate(JFormatter f) {
/* 41 */     throw new IllegalStateException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JNullType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */