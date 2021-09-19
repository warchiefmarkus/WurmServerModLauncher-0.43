/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.codemodel.JType;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JArrayClass
/*    */   extends JClass
/*    */ {
/*    */   private final JType componentType;
/*    */   
/*    */   JArrayClass(JCodeModel owner, JType component) {
/* 22 */     super(owner);
/* 23 */     this.componentType = component;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 28 */     return this.componentType.name() + "[]";
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 32 */     return this.componentType.fullName() + "[]";
/*    */   }
/*    */   
/*    */   public JPackage _package() {
/* 36 */     return owner().rootPackage();
/*    */   }
/*    */   
/*    */   public JClass _extends() {
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   public Iterator _implements() {
/* 44 */     return emptyIterator;
/*    */   }
/*    */   
/*    */   public boolean isInterface() {
/* 48 */     return false;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 52 */     this.componentType.generate(f);
/* 53 */     f.p("[]");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   private static Iterator emptyIterator = (Iterator)new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JType elementType() {
/* 65 */     return this.componentType;
/*    */   }
/*    */   
/*    */   public boolean isArray() {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 78 */     if (!(obj instanceof com.sun.codemodel.JArrayClass)) return false;
/*    */     
/* 80 */     if (this.componentType.equals(((com.sun.codemodel.JArrayClass)obj).componentType)) {
/* 81 */       return true;
/*    */     }
/* 83 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 87 */     return this.componentType.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JArrayClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */