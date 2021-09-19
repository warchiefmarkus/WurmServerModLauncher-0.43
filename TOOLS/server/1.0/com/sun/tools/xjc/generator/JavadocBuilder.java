/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.grammar.FieldItem;
/*    */ import com.sun.tools.xjc.grammar.FieldUse;
/*    */ import com.sun.tools.xjc.grammar.TypeItem;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavadocBuilder
/*    */ {
/*    */   public static String listPossibleTypes(FieldUse fu) {
/* 27 */     StringBuffer buf = new StringBuffer();
/*    */     
/* 29 */     for (Iterator itr = fu.items.iterator(); itr.hasNext(); ) {
/* 30 */       FieldItem fi = itr.next();
/* 31 */       TypeItem[] types = fi.listTypes();
/* 32 */       for (int i = 0; i < types.length; i++) {
/* 33 */         JType t = types[i].getType();
/* 34 */         if (t.isPrimitive() || t.isArray()) {
/* 35 */           buf.append(t.fullName());
/*    */         } else {
/* 37 */           buf.append("{@link " + t.fullName() + "}\n");
/*    */         } 
/*    */       } 
/*    */     } 
/* 41 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\JavadocBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */