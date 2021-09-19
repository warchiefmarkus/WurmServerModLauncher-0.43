/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.JavadocBuilder;
/*     */ import com.sun.tools.xjc.generator.field.AbstractListFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.xml.bind.util.EmptyIterator;
/*     */ import java.util.Iterator;
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
/*     */ public class TypedListFieldRenderer
/*     */   extends AbstractListFieldRenderer
/*     */ {
/*  41 */   public static final FieldRendererFactory theFactory = (FieldRendererFactory)new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   static Class class$java$util$ArrayList;
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypedListFieldRenderer(ClassContext context, FieldUse fu, JClass coreList) {
/*  50 */     super(context, fu, coreList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/*  58 */     JMethod $add = this.writer.declareMethod((JType)this.codeModel.VOID, "add" + this.fu.name);
/*  59 */     JVar $idx = this.writer.addParameter((JType)this.codeModel.INT, "idx");
/*  60 */     JVar $value = this.writer.addParameter(this.fu.type, "value");
/*     */     
/*  62 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */     
/*  64 */     JBlock body = $add.body();
/*  65 */     body.invoke(ref(false), "add").arg((JExpression)$idx).arg((JExpression)$value);
/*     */ 
/*     */     
/*  68 */     this.writer.javadoc().addParam($value, "allowed object is\n" + JavadocBuilder.listPossibleTypes(this.fu));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     JMethod $get = this.writer.declareMethod(this.fu.type, "get" + this.fu.name);
/*  77 */     $idx = this.writer.addParameter((JType)this.codeModel.INT, "idx");
/*     */     
/*  79 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */ 
/*     */ 
/*     */     
/*  83 */     $get.body()._return((JExpression)JExpr.cast(this.fu.type, (JExpression)ref(true).invoke("get").arg((JExpression)$idx)));
/*     */ 
/*     */     
/*  86 */     this.writer.javadoc().addReturn(JavadocBuilder.listPossibleTypes(this.fu));
/*     */ 
/*     */ 
/*     */     
/*  90 */     JMethod $iterate = this.writer.declareMethod((JType)this.codeModel.ref(Iterator.class), "iterate" + this.fu.name);
/*     */ 
/*     */     
/*  93 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*  94 */     $iterate.body()._return(JOp.cond(ref(true).eq(JExpr._null()), (JExpression)this.codeModel.ref(EmptyIterator.class).staticRef("theInstance"), (JExpression)ref(true).invoke("iterator")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     JMethod $size = this.writer.declareMethod((JType)this.codeModel.INT, "sizeOf" + this.fu.name);
/*     */ 
/*     */     
/* 103 */     $size.body()._return(count());
/*     */ 
/*     */     
/* 106 */     JMethod $set = this.writer.declareMethod(this.fu.type, "set" + this.fu.name);
/*     */ 
/*     */     
/* 109 */     $idx = this.writer.addParameter((JType)this.codeModel.INT, "idx");
/* 110 */     $value = this.writer.addParameter(this.fu.type, "value");
/*     */     
/* 112 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */     
/* 114 */     body = $set.body();
/* 115 */     body._return((JExpression)JExpr.cast(this.fu.type, (JExpression)ref(false).invoke("set").arg((JExpression)$idx).arg((JExpression)$value)));
/*     */ 
/*     */     
/* 118 */     this.writer.javadoc().addParam($value, "allowed object is\n" + JavadocBuilder.listPossibleTypes(this.fu));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\TypedListFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */