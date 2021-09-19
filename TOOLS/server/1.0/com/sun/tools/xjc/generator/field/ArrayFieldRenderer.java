/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JForLoop;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.JavadocBuilder;
/*     */ import com.sun.tools.xjc.generator.field.AbstractListFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
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
/*     */ public class ArrayFieldRenderer
/*     */   extends AbstractListFieldRenderer
/*     */ {
/*  47 */   public static final FieldRendererFactory theFactory = (FieldRendererFactory)new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   static Class class$java$util$ArrayList;
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayFieldRenderer(ClassContext cc, FieldUse fu, JClass coreList) {
/*  56 */     super(cc, fu, coreList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/*  62 */     JClass jClass = this.fu.type.array();
/*     */ 
/*     */     
/*  65 */     JType exposedType = this.fu.type;
/*     */ 
/*     */     
/*  68 */     JType internalType = (this.primitiveType != null) ? (JType)this.primitiveType.getWrapperClass() : this.fu.type;
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
/*  83 */     JMethod $get = this.writer.declareMethod((JType)exposedType.array(), "get" + this.fu.name);
/*  84 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*  85 */     JBlock body = $get.body();
/*     */     
/*  87 */     if (this.$defValues != null) {
/*  88 */       JBlock then = body._if(hasSetValue().not())._then();
/*  89 */       JVar $r = then.decl((JType)exposedType.array(), "r", (JExpression)JExpr.newArray(exposedType, (JExpression)this.$defValues.ref("length")));
/*     */ 
/*     */ 
/*     */       
/*  93 */       then.staticInvoke(this.codeModel.ref(System.class), "arraycopy").arg((JExpression)this.$defValues).arg(JExpr.lit(0)).arg((JExpression)$r).arg(JExpr.lit(0)).arg((JExpression)this.$defValues.ref("length"));
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
/* 107 */       then._return((JExpression)$r);
/*     */     } else {
/* 109 */       body._if(ref(true).eq(JExpr._null()))._then()._return((JExpression)JExpr.newArray(exposedType, 0));
/*     */     } 
/*     */ 
/*     */     
/* 113 */     if (this.primitiveType == null) {
/* 114 */       body._return((JExpression)JExpr.cast((JType)jClass, (JExpression)ref(true).invoke("toArray").arg((JExpression)JExpr.newArray(this.fu.type, (JExpression)ref(true).invoke("size")))));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 122 */       JVar $r = body.decl((JType)exposedType.array(), "r", (JExpression)JExpr.newArray(exposedType, (JExpression)ref(true).invoke("size")));
/* 123 */       JForLoop loop = body._for();
/* 124 */       JVar jVar1 = loop.init((JType)this.codeModel.INT, "__i", JExpr.lit(0));
/* 125 */       loop.test(jVar1.lt((JExpression)$r.ref("length")));
/* 126 */       loop.update(jVar1.incr());
/* 127 */       loop.body().assign((JAssignmentTarget)$r.component((JExpression)jVar1), this.primitiveType.unwrap((JExpression)JExpr.cast(internalType, (JExpression)ref(true).invoke("get").arg((JExpression)jVar1))));
/*     */       
/* 129 */       body._return((JExpression)$r);
/*     */     } 
/*     */     
/* 132 */     this.writer.javadoc().addReturn("array of\n" + JavadocBuilder.listPossibleTypes(this.fu));
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
/* 145 */     $get = this.writer.declareMethod(exposedType, "get" + this.fu.name);
/* 146 */     JVar $idx = this.writer.addParameter((JType)this.codeModel.INT, "idx");
/*     */     
/* 148 */     if (this.$defValues != null) {
/* 149 */       JBlock then = $get.body()._if(hasSetValue().not())._then();
/* 150 */       then._return((JExpression)this.$defValues.component((JExpression)$idx));
/*     */     } else {
/* 152 */       $get.body()._if(ref(true).eq(JExpr._null()))._then()._throw((JExpression)JExpr._new(this.codeModel.ref(IndexOutOfBoundsException.class)));
/*     */     } 
/*     */ 
/*     */     
/* 156 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/* 157 */     $get.body()._return(unbox((JExpression)JExpr.cast(internalType, (JExpression)ref(true).invoke("get").arg((JExpression)$idx))));
/*     */     
/* 159 */     this.writer.javadoc().addReturn("one of\n" + JavadocBuilder.listPossibleTypes(this.fu));
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
/* 172 */     JMethod $getLength = this.writer.declareMethod((JType)this.codeModel.INT, "get" + this.fu.name + "Length");
/* 173 */     if (this.$defValues != null) {
/* 174 */       $getLength.body()._if(hasSetValue().not())._then()._return((JExpression)this.$defValues.ref("length"));
/*     */     } else {
/*     */       
/* 177 */       $getLength.body()._if(ref(true).eq(JExpr._null()))._then()._return(JExpr.lit(0));
/*     */     } 
/*     */     
/* 180 */     $getLength.body()._return((JExpression)ref(true).invoke("size"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     JMethod $set = this.writer.declareMethod((JType)this.codeModel.VOID, "set" + this.fu.name);
/*     */ 
/*     */ 
/*     */     
/* 193 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */     
/* 195 */     JVar $value = this.writer.addParameter((JType)exposedType.array(), "values");
/* 196 */     $set.body().invoke(ref(false), "clear");
/* 197 */     JVar $len = $set.body().decl((JType)this.codeModel.INT, "len", (JExpression)$value.ref("length"));
/* 198 */     JForLoop _for = $set.body()._for();
/* 199 */     JVar $i = _for.init((JType)this.codeModel.INT, "i", JExpr.lit(0));
/* 200 */     _for.test(JOp.lt((JExpression)$i, (JExpression)$len));
/* 201 */     _for.update($i.incr());
/* 202 */     _for.body().invoke(ref(true), "add").arg(box((JExpression)$value.component((JExpression)$i)));
/*     */     
/* 204 */     this.writer.javadoc().addParam($value, "allowed objects are\n" + JavadocBuilder.listPossibleTypes(this.fu));
/*     */ 
/*     */ 
/*     */     
/* 208 */     $set = this.writer.declareMethod(exposedType, "set" + this.fu.name);
/*     */ 
/*     */     
/* 211 */     $idx = this.writer.addParameter((JType)this.codeModel.INT, "idx");
/* 212 */     $value = this.writer.addParameter(exposedType, "value");
/*     */     
/* 214 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */     
/* 216 */     body = $set.body();
/* 217 */     body._return(unbox((JExpression)JExpr.cast(internalType, (JExpression)ref(true).invoke("set").arg((JExpression)$idx).arg(box((JExpression)$value)))));
/*     */ 
/*     */     
/* 220 */     this.writer.javadoc().addParam($value, "allowed object is\n" + JavadocBuilder.listPossibleTypes(this.fu));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\ArrayFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */