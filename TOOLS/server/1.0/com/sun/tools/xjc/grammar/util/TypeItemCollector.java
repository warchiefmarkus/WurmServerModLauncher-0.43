/*    */ package 1.0.com.sun.tools.xjc.grammar.util;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*    */ import com.sun.tools.xjc.grammar.BGMWalker;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.ExternalItem;
/*    */ import com.sun.tools.xjc.grammar.FieldItem;
/*    */ import com.sun.tools.xjc.grammar.IgnoreItem;
/*    */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*    */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*    */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*    */ import com.sun.tools.xjc.grammar.TypeItem;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import java.util.Vector;
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
/*    */ public final class TypeItemCollector
/*    */   extends BGMWalker
/*    */ {
/* 33 */   private final Vector vec = new Vector();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TypeItem[] collect(Expression e) {
/* 42 */     com.sun.tools.xjc.grammar.util.TypeItemCollector tic = new com.sun.tools.xjc.grammar.util.TypeItemCollector();
/* 43 */     e.visit((ExpressionVisitorVoid)tic);
/* 44 */     return (TypeItem[])tic.vec.toArray((Object[])new TypeItem[tic.vec.size()]);
/*    */   }
/*    */   
/*    */   public Object onClass(ClassItem item) {
/* 48 */     this.vec.add(item);
/* 49 */     return null;
/*    */   }
/*    */   public Object onInterface(InterfaceItem item) {
/* 52 */     this.vec.add(item);
/* 53 */     return null;
/*    */   }
/*    */   public Object onPrimitive(PrimitiveItem item) {
/* 56 */     this.vec.add(item);
/* 57 */     return null;
/*    */   }
/*    */   public Object onExternal(ExternalItem item) {
/* 60 */     this.vec.add(item);
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object onSuper(SuperClassItem item) {
/* 66 */     throw new JAXBAssertionError();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object onField(FieldItem item) {
/* 71 */     throw new JAXBAssertionError();
/*    */   } public Object onIgnore(IgnoreItem item) {
/* 73 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\TypeItemCollector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */