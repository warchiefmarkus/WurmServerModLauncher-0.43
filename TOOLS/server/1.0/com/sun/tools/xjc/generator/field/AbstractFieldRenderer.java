/*    */ package 1.0.com.sun.tools.xjc.generator.field;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JFieldVar;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.MethodWriter;
/*    */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*    */ import com.sun.tools.xjc.grammar.FieldUse;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
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
/*    */ abstract class AbstractFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   protected final JCodeModel codeModel;
/*    */   protected final ClassContext context;
/*    */   protected final FieldUse fu;
/*    */   protected final MethodWriter writer;
/*    */   
/*    */   protected AbstractFieldRenderer(ClassContext _context, FieldUse _fu) {
/* 38 */     this.context = _context;
/* 39 */     this.fu = _fu;
/* 40 */     this.codeModel = _context.parent.getCodeModel();
/* 41 */     this.writer = this.context.createMethodWriter();
/*    */   }
/*    */   public final FieldUse getFieldUse() {
/* 44 */     return this.fu;
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
/*    */   protected final JFieldVar generateField(JType type) {
/* 58 */     return this.context.implClass.field(2, type, "_" + this.fu.name);
/*    */   }
/*    */   protected final void _assert(boolean b) {
/* 61 */     if (!b)
/* 62 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\AbstractFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */