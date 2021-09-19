/*    */ package 1.0.com.sun.tools.xjc.generator.field;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFieldVar;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.field.SingleFieldRenderer;
/*    */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*    */ import com.sun.tools.xjc.grammar.FieldUse;
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
/*    */ public class XsiTypeFieldRenderer
/*    */   extends SingleFieldRenderer
/*    */ {
/*    */   private final JClass defaultObject;
/*    */   
/*    */   public XsiTypeFieldRenderer(ClassContext context, FieldUse fu, JClass _defaultObject) {
/* 45 */     super(context, fu);
/* 46 */     this.defaultObject = _defaultObject;
/*    */   }
/*    */   
/*    */   protected JFieldVar generateField() {
/* 50 */     return this.context.implClass.field(2, this.fu.type, "_" + this.fu.name, (JExpression)JExpr._new(this.defaultObject));
/*    */   }
/*    */ 
/*    */   
/*    */   public JExpression ifCountEqual(int i) {
/* 55 */     if (i == 1) return JExpr.TRUE; 
/* 56 */     return JExpr.FALSE;
/*    */   }
/*    */   public JExpression ifCountGte(int i) {
/* 59 */     if (i <= 1) return JExpr.TRUE; 
/* 60 */     return JExpr.FALSE;
/*    */   }
/*    */   
/*    */   public JExpression ifCountLte(int i) {
/* 64 */     if (i == 0) return JExpr.FALSE; 
/* 65 */     return JExpr.TRUE;
/*    */   }
/*    */   
/*    */   public JExpression count() {
/* 69 */     return JExpr.lit(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 74 */     return (FieldMarshallerGenerator)new Object(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\XsiTypeFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */