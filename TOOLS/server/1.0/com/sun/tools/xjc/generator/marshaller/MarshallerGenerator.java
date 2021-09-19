/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.marshaller.Context;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.runtime.XMLSerializable;
/*     */ import com.sun.tools.xjc.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.serializer.Util;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class MarshallerGenerator
/*     */ {
/*     */   private final AnnotatedGrammar grammar;
/*     */   private final GeneratorContext context;
/*     */   static Class class$java$lang$String;
/*     */   static Class class$com$sun$xml$bind$marshaller$IdentifiableObject;
/*     */   
/*     */   public static void generate(AnnotatedGrammar grammar, GeneratorContext context, Options opt) {
/*  56 */     new com.sun.tools.xjc.generator.marshaller.MarshallerGenerator(grammar, context, opt);
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
/*     */   private MarshallerGenerator(AnnotatedGrammar _grammar, GeneratorContext _context, Options _opt) {
/*  69 */     this.grammar = _grammar;
/*  70 */     this.context = _context;
/*     */ 
/*     */ 
/*     */     
/*  74 */     ClassItem[] cs = this.grammar.getClasses();
/*  75 */     for (int i = 0; i < cs.length; i++) {
/*  76 */       generate(this.context.getClassContext(cs[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generate(ClassContext cc) {
/*  85 */     cc.implClass._implements(this.context.getRuntime(XMLSerializable.class));
/*     */ 
/*     */ 
/*     */     
/*  89 */     (generateMethodSkeleton(cc, "serializeBody")).bodyPass.build(cc.target.exp);
/*  90 */     (generateMethodSkeleton(cc, "serializeAttributes")).attPass.build(cc.target.exp);
/*  91 */     (generateMethodSkeleton(cc, "serializeURIs")).uriPass.build(cc.target.exp);
/*     */     
/*  93 */     processID(cc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processID(ClassContext cc) {
/* 101 */     cc.target.exp.visit((ExpressionVisitorVoid)new Object(this, cc));
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
/*     */   private Context generateMethodSkeleton(ClassContext cc, String methodName) {
/* 164 */     JMethod p = cc.implClass.method(1, (JType)this.grammar.codeModel.VOID, methodName);
/* 165 */     JVar $serializer = p.param((JType)this.context.getRuntime(XMLSerializer.class), "context");
/* 166 */     p._throws(SAXException.class);
/* 167 */     JBlock body = p.body();
/*     */ 
/*     */     
/* 170 */     FieldUse[] uses = cc.target.getDeclaredFieldUses();
/* 171 */     Map fieldMarshallers = new HashMap();
/* 172 */     for (int i = 0; i < uses.length; i++) {
/* 173 */       fieldMarshallers.put(uses[i], this.context.getField(uses[i]).createMarshaller(body, Integer.toString(i + 1)));
/*     */ 
/*     */ 
/*     */       
/* 177 */       if ((uses[i]).multiplicity.isUnique() && (uses[i]).type.isPrimitive()) {
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
/* 190 */         JExpression hasSetValue = this.context.getField(uses[i]).hasSetValue();
/* 191 */         if (hasSetValue != null) {
/* 192 */           JConditional cond = body._if(hasSetValue.not());
/* 193 */           cond._then().invoke((JExpression)$serializer, "reportError").arg((JExpression)this.grammar.codeModel.ref(Util.class).staticInvoke("createMissingObjectError").arg(JExpr._this()).arg(JExpr.lit((uses[i]).name)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 200 */     return new Context(this.context, this.grammar.getPool(), cc.target, body, $serializer, fieldMarshallers);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\MarshallerGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */