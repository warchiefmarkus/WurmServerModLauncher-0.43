/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.MethodWriter;
/*     */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IsSetFieldRenderer
/*     */   implements FieldRenderer
/*     */ {
/*     */   private final FieldRenderer core;
/*     */   private final ClassContext context;
/*     */   private final FieldUse use;
/*     */   private final boolean generateUnSetMethod;
/*     */   private final boolean generateIsSetMethod;
/*     */   
/*     */   public static FieldRendererFactory createFactory(FieldRendererFactory core, boolean generateUnSetMethod, boolean generateIsSetMethod) {
/*  40 */     return (FieldRendererFactory)new Object(core, generateUnSetMethod, generateIsSetMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FieldRendererFactory createFactory(FieldRendererFactory core) {
/*  50 */     return createFactory(core, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IsSetFieldRenderer(ClassContext _context, FieldUse _use, FieldRenderer _core, boolean generateUnSetMethod, boolean generateIsSetMethod) {
/*  65 */     this.core = _core;
/*  66 */     this.context = _context;
/*  67 */     this.use = _use;
/*  68 */     this.generateUnSetMethod = generateUnSetMethod;
/*  69 */     this.generateIsSetMethod = generateIsSetMethod;
/*     */   }
/*     */   
/*     */   public void generate() {
/*  73 */     this.core.generate();
/*     */ 
/*     */     
/*  76 */     MethodWriter writer = this.context.createMethodWriter();
/*     */     
/*  78 */     JCodeModel codeModel = this.context.parent.getCodeModel();
/*     */     
/*  80 */     if (this.generateIsSetMethod) {
/*     */       
/*  82 */       JExpression hasSetValue = this.core.hasSetValue();
/*  83 */       if (hasSetValue == null);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  88 */       writer.declareMethod((JType)codeModel.BOOLEAN, "isSet" + this.use.name).body()._return(hasSetValue);
/*     */     } 
/*     */ 
/*     */     
/*  92 */     if (this.generateUnSetMethod)
/*     */     {
/*  94 */       this.core.unsetValues(writer.declareMethod((JType)codeModel.VOID, "unset" + this.use.name).body());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JBlock getOnSetEventHandler() {
/* 100 */     return this.core.getOnSetEventHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unsetValues(JBlock body) {
/* 105 */     this.core.unsetValues(body);
/*     */   }
/*     */   public void toArray(JBlock block, JExpression $array) {
/* 108 */     this.core.toArray(block, $array);
/*     */   }
/*     */   public JExpression hasSetValue() {
/* 111 */     return this.core.hasSetValue();
/*     */   }
/*     */   public JExpression getValue() {
/* 114 */     return this.core.getValue();
/*     */   }
/*     */   public JClass getValueType() {
/* 117 */     return this.core.getValueType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUse getFieldUse() {
/* 123 */     return this.core.getFieldUse();
/*     */   }
/*     */   
/*     */   public void setter(JBlock body, JExpression newValue) {
/* 127 */     this.core.setter(body, newValue);
/*     */   }
/*     */   
/*     */   public JExpression ifCountEqual(int i) {
/* 131 */     return this.core.ifCountEqual(i);
/*     */   }
/*     */   
/*     */   public JExpression ifCountGte(int i) {
/* 135 */     return this.core.ifCountGte(i);
/*     */   }
/*     */   
/*     */   public JExpression ifCountLte(int i) {
/* 139 */     return this.core.ifCountLte(i);
/*     */   }
/*     */   public JExpression count() {
/* 142 */     return this.core.count();
/*     */   }
/*     */   
/*     */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 146 */     return this.core.createMarshaller(block, uniqueId);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\IsSetFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */