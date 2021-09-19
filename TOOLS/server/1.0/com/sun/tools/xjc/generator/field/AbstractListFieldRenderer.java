/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JForLoop;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.field.AbstractFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.DefaultValue;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.xml.bind.util.ListImpl;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractListFieldRenderer
/*     */   extends AbstractFieldRenderer
/*     */ {
/*  49 */   protected JVar $defValues = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final JClass coreList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JPrimitiveType primitiveType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JBlock onSetHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JExpression newListObjectExp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JFieldVar field;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JMethod internalGetter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JExpression lazyInitializer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractListFieldRenderer(ClassContext context, FieldUse fu, JClass coreList) {
/*  90 */     super(context, fu);
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
/* 155 */     this.lazyInitializer = (JExpression)new Object(this);
/*     */     this.coreList = coreList;
/*     */     if (fu.type instanceof JPrimitiveType) {
/*     */       this.primitiveType = (JPrimitiveType)fu.type;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final JExpression unbox(JExpression exp) {
/*     */     if (this.primitiveType == null) {
/*     */       return exp;
/*     */     }
/*     */     return this.primitiveType.unwrap(exp);
/*     */   }
/*     */   
/*     */   protected final JExpression box(JExpression exp) {
/*     */     if (this.primitiveType == null) {
/*     */       return exp;
/*     */     }
/*     */     return this.primitiveType.wrap(exp);
/*     */   }
/*     */   
/*     */   protected final JExpression ref(boolean canBeNull) {
/* 178 */     if (canBeNull) {
/* 179 */       return (JExpression)this.field;
/*     */     }
/* 181 */     return (JExpression)JExpr.invoke(this.internalGetter);
/*     */   }
/*     */   public JBlock getOnSetEventHandler() { if (this.onSetHandler != null)
/*     */       return this.onSetHandler;  JDefinedClass anonymousClass = this.codeModel.newAnonymousClass(this.codeModel.ref(ListImpl.class)); this.newListObjectExp = (JExpression)JExpr._new((JClass)anonymousClass).arg((JExpression)JExpr._new(this.coreList)); JMethod method = anonymousClass.method(1, (JType)this.codeModel.VOID, "setModified"); JVar $f = method.param((JType)this.codeModel.BOOLEAN, "f"); method.body().invoke(JExpr._super(), "setModified").arg((JExpression)$f); this.onSetHandler = method.body()._if((JExpression)$f)._then(); return this.onSetHandler; }
/*     */   public JClass getValueType() { return this.codeModel.ref(List.class); } public final void generate() { this.field = generateField(); this.internalGetter = this.context.implClass.method(2, ListImpl.class, "_get" + this.fu.name);
/*     */     this.internalGetter.body()._if(this.field.eq(JExpr._null()))._then().assign((JAssignmentTarget)this.field, this.lazyInitializer);
/*     */     this.internalGetter.body()._return((JExpression)this.field);
/* 188 */     generateAccessors(); } protected final JFieldVar generateField() { DefaultValue[] defaultValues = this.fu.getDefaultValues();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     JClass list = this.codeModel.ref(ListImpl.class);
/* 196 */     JFieldVar ref = generateField((JType)list);
/* 197 */     this.newListObjectExp = (JExpression)JExpr._new(list).arg((JExpression)JExpr._new(this.coreList));
/*     */ 
/*     */     
/* 200 */     if (defaultValues != null) {
/*     */       
/* 202 */       JClass jClass = this.fu.type.array();
/*     */       
/*     */       JInvocation initializer;
/*     */       
/* 206 */       this.$defValues = (JVar)this.context.implClass.field(26, (JType)jClass, this.fu.name + "_defaultValues", (JExpression)(initializer = JExpr._new((JType)jClass)));
/*     */ 
/*     */ 
/*     */       
/* 210 */       for (int i = 0; i < defaultValues.length; i++) {
/* 211 */         initializer.arg(defaultValues[i].generateConstant());
/*     */       }
/*     */     } 
/* 214 */     return ref; }
/*     */ 
/*     */   
/*     */   public void setter(JBlock body, JExpression newValue) {
/* 218 */     if (this.primitiveType != null)
/* 219 */       newValue = this.primitiveType.wrap(newValue); 
/* 220 */     body.invoke(ref(false), "add").arg(newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toArray(JBlock block, JExpression $array) {
/* 225 */     block = block._if(this.field.ne(JExpr._null()))._then();
/*     */     
/* 227 */     if (this.primitiveType == null) {
/*     */ 
/*     */       
/* 230 */       block.invoke(ref(true), "toArray").arg($array);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 236 */       JForLoop $for = block._for();
/* 237 */       JVar $idx = $for.init((JType)this.codeModel.INT, "q" + hashCode(), count().minus(JExpr.lit(1)));
/* 238 */       $for.test($idx.gte(JExpr.lit(0)));
/* 239 */       $for.update($idx.decr());
/*     */       
/* 241 */       $for.body().assign((JAssignmentTarget)$array.component((JExpression)$idx), this.primitiveType.unwrap((JExpression)JExpr.cast((JType)this.primitiveType.getWrapperClass(), (JExpression)ref(true).invoke("get").arg((JExpression)$idx))));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression count() {
/* 248 */     return JOp.cond(this.field.eq(JExpr._null()), JExpr.lit(0), (JExpression)this.field.invoke("size"));
/*     */   }
/*     */   public JExpression ifCountEqual(int i) {
/* 251 */     return count().eq(JExpr.lit(i));
/*     */   }
/*     */   public JExpression ifCountGte(int i) {
/* 254 */     return count().gte(JExpr.lit(i));
/*     */   }
/*     */   public JExpression ifCountLte(int i) {
/* 257 */     return count().lte(JExpr.lit(i));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 307 */     JVar $idx = block.decl((JType)this.codeModel.INT, "idx" + uniqueId, JExpr.lit(0));
/*     */     
/* 309 */     JVar $len = block.decl(8, (JType)this.codeModel.INT, "len" + uniqueId, (this.$defValues != null) ? JOp.cond(this.field.ne(JExpr._null()).cand((JExpression)this.field.invoke("isModified")), (JExpression)this.field.invoke("size"), JExpr.lit(0)) : count());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     return (FieldMarshallerGenerator)new FMGImpl(this, $idx, $len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsetValues(JBlock body) {
/* 323 */     body = body._if(this.field.ne(JExpr._null()))._then();
/*     */     
/* 325 */     body.invoke((JExpression)this.field, "clear");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     body.invoke((JExpression)this.field, "setModified").arg(JExpr.FALSE);
/*     */   }
/*     */   public JExpression hasSetValue() {
/* 333 */     return JOp.cond(this.field.eq(JExpr._null()), JExpr.FALSE, (JExpression)this.field.invoke("isModified"));
/*     */   }
/*     */   
/*     */   public JExpression getValue() {
/* 337 */     return ref(false);
/*     */   }
/*     */   
/*     */   public abstract void generateAccessors();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\AbstractListFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */