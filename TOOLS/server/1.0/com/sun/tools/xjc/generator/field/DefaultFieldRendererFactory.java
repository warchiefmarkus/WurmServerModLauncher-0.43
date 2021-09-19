/*    */ package 1.0.com.sun.tools.xjc.generator.field;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*    */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*    */ import com.sun.tools.xjc.generator.field.OptionalUnboxedFieldRenderer;
/*    */ import com.sun.tools.xjc.generator.field.SingleFieldRenderer;
/*    */ import com.sun.tools.xjc.generator.field.UntypedListFieldRenderer;
/*    */ import com.sun.tools.xjc.grammar.FieldUse;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultFieldRendererFactory
/*    */   implements FieldRendererFactory
/*    */ {
/*    */   private FieldRendererFactory defaultCollectionFieldRenderer;
/*    */   
/*    */   public DefaultFieldRendererFactory(JCodeModel codeModel) {
/* 23 */     this((FieldRendererFactory)new UntypedListFieldRenderer.Factory(codeModel.ref(ArrayList.class)));
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultFieldRendererFactory(FieldRendererFactory defaultCollectionFieldRenderer) {
/* 28 */     this.defaultCollectionFieldRenderer = defaultCollectionFieldRenderer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FieldRenderer create(ClassContext context, FieldUse fu) {
/* 34 */     if (fu.multiplicity.isAtMostOnce()) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 44 */       if (fu.isUnboxable()) {
/* 45 */         return (FieldRenderer)new OptionalUnboxedFieldRenderer(context, fu);
/*    */       }
/*    */       
/* 48 */       return (FieldRenderer)new SingleFieldRenderer(context, fu);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 53 */     return this.defaultCollectionFieldRenderer.create(context, fu);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\DefaultFieldRendererFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */