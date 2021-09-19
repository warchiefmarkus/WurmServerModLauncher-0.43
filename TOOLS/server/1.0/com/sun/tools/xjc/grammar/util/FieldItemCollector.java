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
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import java.util.Hashtable;
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
/*    */ public final class FieldItemCollector
/*    */   extends BGMWalker
/*    */ {
/* 28 */   private final Hashtable m = new Hashtable();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static FieldItem[] collect(Expression exp) {
/* 36 */     com.sun.tools.xjc.grammar.util.FieldItemCollector fim = new com.sun.tools.xjc.grammar.util.FieldItemCollector();
/* 37 */     exp.visit((ExpressionVisitorVoid)fim);
/* 38 */     return (FieldItem[])fim.m.values().toArray((Object[])new FieldItem[fim.m.values().size()]);
/*    */   }
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
/*    */   public Object onSuper(SuperClassItem item) {
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object onField(FieldItem item) {
/* 64 */     this.m.put(item.name, item);
/* 65 */     return null;
/*    */   } public Object onIgnore(IgnoreItem item) {
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object onClass(ClassItem item) {
/* 72 */     throw new JAXBAssertionError(); }
/* 73 */   public Object onInterface(InterfaceItem item) { throw new JAXBAssertionError(); }
/* 74 */   public Object onPrimitive(PrimitiveItem item) { throw new JAXBAssertionError(); } public Object onExternal(ExternalItem item) {
/* 75 */     throw new JAXBAssertionError();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\FieldItemCollector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */