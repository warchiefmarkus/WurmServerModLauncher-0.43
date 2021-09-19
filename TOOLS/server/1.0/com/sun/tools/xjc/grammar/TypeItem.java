/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JPrimitiveType;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.grammar.JavaItem;
/*    */ import org.xml.sax.Locator;
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
/*    */ public abstract class TypeItem
/*    */   extends JavaItem
/*    */ {
/*    */   public TypeItem(String displayName, Locator loc) {
/* 25 */     super(displayName, loc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JType getType();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void sort(com.sun.tools.xjc.grammar.TypeItem[] t) {
/* 40 */     for (int i = 0; i < t.length - 1; i++) {
/* 41 */       int k = i;
/* 42 */       JClass tk = toJClass(t[k]);
/*    */       
/* 44 */       for (int j = i + 1; j < t.length; j++) {
/* 45 */         JClass tj = toJClass(t[j]);
/* 46 */         if (tk.isAssignableFrom(tj)) {
/* 47 */           k = j;
/* 48 */           tk = tj;
/*    */         } 
/*    */       } 
/*    */ 
/*    */       
/* 53 */       com.sun.tools.xjc.grammar.TypeItem tmp = t[i];
/* 54 */       t[i] = t[k];
/* 55 */       t[k] = tmp;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static JClass toJClass(com.sun.tools.xjc.grammar.TypeItem t) {
/* 60 */     JType jt = t.getType();
/* 61 */     if (jt.isPrimitive()) return ((JPrimitiveType)jt).getWrapperClass(); 
/* 62 */     return (JClass)jt;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 66 */     return getClass().getName() + '[' + getType().fullName() + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\TypeItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */