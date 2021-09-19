/*     */ package com.sun.tools.xjc.generator.bean.field;
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
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ final class ArrayField
/*     */   extends AbstractListField
/*     */ {
/*     */   private JMethod $setAll;
/*     */   private JMethod $getAll;
/*     */   
/*     */   class Accessor
/*     */     extends AbstractListField.Accessor
/*     */   {
/*     */     protected Accessor(JExpression $target) {
/*  79 */       super($target);
/*     */     }
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/*  83 */       block.assign((JAssignmentTarget)$var, (JExpression)ArrayField.this.codeModel.ref(Arrays.class).staticInvoke("asList").arg((JExpression)this.$target.invoke(ArrayField.this.$getAll)));
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/*  87 */       block.invoke(this.$target, ArrayField.this.$setAll).arg((JExpression)$var.invoke("toArray").arg((JExpression)JExpr.newArray(ArrayField.this.exposedType, (JExpression)$var.invoke("size"))));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ArrayField(ClassOutlineImpl context, CPropertyInfo prop) {
/*  96 */     super(context, prop, false);
/*  97 */     generate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/* 102 */     MethodWriter writer = this.outline.createMethodWriter();
/* 103 */     Accessor acc = create(JExpr._this());
/*     */ 
/*     */     
/* 106 */     JClass jClass = this.exposedType.array();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     this.$getAll = writer.declareMethod((JType)this.exposedType.array(), "get" + this.prop.getName(true));
/* 113 */     writer.javadoc().append(this.prop.javadoc);
/* 114 */     JBlock body = this.$getAll.body();
/*     */     
/* 116 */     body._if(acc.ref(true).eq(JExpr._null()))._then()._return((JExpression)JExpr.newArray(this.exposedType, 0));
/*     */ 
/*     */     
/* 119 */     if (this.primitiveType == null) {
/* 120 */       body._return((JExpression)JExpr.cast((JType)jClass, (JExpression)acc.ref(true).invoke("toArray").arg((JExpression)JExpr.newArray(this.implType, (JExpression)acc.ref(true).invoke("size")))));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 128 */       JVar $r = body.decl((JType)this.exposedType.array(), "r", (JExpression)JExpr.newArray(this.exposedType, (JExpression)acc.ref(true).invoke("size")));
/* 129 */       JForLoop loop = body._for();
/* 130 */       JVar jVar1 = loop.init((JType)this.codeModel.INT, "__i", JExpr.lit(0));
/* 131 */       loop.test(jVar1.lt((JExpression)$r.ref("length")));
/* 132 */       loop.update(jVar1.incr());
/* 133 */       loop.body().assign((JAssignmentTarget)$r.component((JExpression)jVar1), this.primitiveType.unwrap((JExpression)acc.ref(true).invoke("get").arg((JExpression)jVar1)));
/*     */       
/* 135 */       body._return((JExpression)$r);
/*     */     } 
/*     */     
/* 138 */     List<Object> returnTypes = listPossibleTypes(this.prop);
/* 139 */     writer.javadoc().addReturn().append("array of\n").append(returnTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     JMethod $get = writer.declareMethod(this.exposedType, "get" + this.prop.getName(true));
/* 147 */     JVar $idx = writer.addParameter((JType)this.codeModel.INT, "idx");
/*     */     
/* 149 */     $get.body()._if(acc.ref(true).eq(JExpr._null()))._then()._throw((JExpression)JExpr._new(this.codeModel.ref(IndexOutOfBoundsException.class)));
/*     */ 
/*     */     
/* 152 */     writer.javadoc().append(this.prop.javadoc);
/* 153 */     $get.body()._return(acc.unbox((JExpression)acc.ref(true).invoke("get").arg((JExpression)$idx)));
/*     */     
/* 155 */     writer.javadoc().addReturn().append("one of\n").append(returnTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     JMethod $getLength = writer.declareMethod((JType)this.codeModel.INT, "get" + this.prop.getName(true) + "Length");
/* 163 */     $getLength.body()._if(acc.ref(true).eq(JExpr._null()))._then()._return(JExpr.lit(0));
/*     */     
/* 165 */     $getLength.body()._return((JExpression)acc.ref(true).invoke("size"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.$setAll = writer.declareMethod((JType)this.codeModel.VOID, "set" + this.prop.getName(true));
/*     */ 
/*     */ 
/*     */     
/* 178 */     writer.javadoc().append(this.prop.javadoc);
/*     */     
/* 180 */     JVar $value = writer.addParameter((JType)this.exposedType.array(), "values");
/* 181 */     this.$setAll.body().invoke(acc.ref(false), "clear");
/* 182 */     JVar $len = this.$setAll.body().decl((JType)this.codeModel.INT, "len", (JExpression)$value.ref("length"));
/* 183 */     JForLoop _for = this.$setAll.body()._for();
/* 184 */     JVar $i = _for.init((JType)this.codeModel.INT, "i", JExpr.lit(0));
/* 185 */     _for.test(JOp.lt((JExpression)$i, (JExpression)$len));
/* 186 */     _for.update($i.incr());
/* 187 */     _for.body().invoke(acc.ref(true), "add").arg(castToImplType(acc.box((JExpression)$value.component((JExpression)$i))));
/*     */     
/* 189 */     writer.javadoc().addParam($value).append("allowed objects are\n").append(returnTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     JMethod $set = writer.declareMethod(this.exposedType, "set" + this.prop.getName(true));
/*     */ 
/*     */     
/* 197 */     $idx = writer.addParameter((JType)this.codeModel.INT, "idx");
/* 198 */     $value = writer.addParameter(this.exposedType, "value");
/*     */     
/* 200 */     writer.javadoc().append(this.prop.javadoc);
/*     */     
/* 202 */     body = $set.body();
/* 203 */     body._return(acc.unbox((JExpression)acc.ref(true).invoke("set").arg((JExpression)$idx).arg(castToImplType(acc.box((JExpression)$value)))));
/*     */ 
/*     */     
/* 206 */     writer.javadoc().addParam($value).append("allowed object is\n").append(returnTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JClass getCoreListType() {
/* 212 */     return this.codeModel.ref(ArrayList.class).narrow(this.exposedType.boxify());
/*     */   }
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 216 */     return new Accessor(targetObject);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\ArrayField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */