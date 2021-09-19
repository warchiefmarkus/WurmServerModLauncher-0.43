/*    */ package 1.0.com.sun.tools.xjc.generator.field;
/*    */ 
/*    */ import com.sun.codemodel.JFieldVar;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.field.AbstractFieldRenderer;
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
/*    */ abstract class AbstractFieldRendererWithVar
/*    */   extends AbstractFieldRenderer
/*    */ {
/*    */   private JFieldVar field;
/*    */   
/*    */   protected AbstractFieldRendererWithVar(ClassContext _context, FieldUse _fu) {
/* 32 */     super(_context, _fu);
/*    */   }
/*    */   
/*    */   public final void generate() {
/* 36 */     this.field = generateField();
/* 37 */     generateAccessors();
/*    */   }
/*    */   public JFieldVar ref() {
/* 40 */     return this.field;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected JFieldVar generateField() {
/* 46 */     return generateField((JType)getValueType());
/*    */   }
/*    */   
/*    */   public abstract void generateAccessors();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\AbstractFieldRendererWithVar.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */