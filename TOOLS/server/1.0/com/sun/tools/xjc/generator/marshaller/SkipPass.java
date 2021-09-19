/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.marshaller.AbstractPassImpl;
/*    */ import com.sun.tools.xjc.generator.marshaller.Context;
/*    */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*    */ import com.sun.tools.xjc.grammar.ExternalItem;
/*    */ import com.sun.tools.xjc.grammar.PrimitiveItem;
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
/*    */ public class SkipPass
/*    */   extends AbstractPassImpl
/*    */ {
/*    */   SkipPass(Context _context) {
/* 26 */     super(_context, "Skip");
/*    */   }
/*    */   
/*    */   public void onElement(ElementExp exp) {
/* 30 */     this.context.skipPass.build(exp.contentModel);
/*    */   }
/*    */   
/*    */   public void onExternal(ExternalItem item) {
/* 34 */     increment();
/*    */   }
/*    */   
/*    */   public void onAttribute(AttributeExp exp) {
/* 38 */     this.context.skipPass.build(exp.exp);
/*    */   }
/*    */   
/*    */   public void onPrimitive(PrimitiveItem exp) {
/* 42 */     increment();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onValue(ValueExp exp) {}
/*    */   
/*    */   private void increment() {
/* 49 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/* 50 */     fmg.increment(this.context.getCurrentBlock());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\SkipPass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */