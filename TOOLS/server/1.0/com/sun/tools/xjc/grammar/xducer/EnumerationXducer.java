/*     */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.msv.grammar.util.ExpressionPrinter;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.xducer.BuiltinDatatypeTransducerFactory;
/*     */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.Messages;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumerationXducer
/*     */   extends TransducerImpl
/*     */ {
/*     */   private final JDefinedClass type;
/*     */   private final NameConverter nameConverter;
/*     */   private final JCodeModel codeModel;
/*     */   private final Map members;
/*     */   private Locator sourceLocator;
/*     */   private boolean populated;
/*     */   private ValueExp[] values;
/*     */   private JFieldVar[] items;
/*     */   private JType valueType;
/*     */   private static final String ERR_CONTEXT_DEPENDENT_TYPE = "EnumerationXducer.ContextDependentType";
/*     */   private static final String ERR_UNSUPPORTED_TYPE_FOR_ENUM = "EnumerationXducer.UnsupportedTypeForEnum";
/*     */   private static final String ERR_UNUSABLE_NAME = "EnumerationXducer.UnusableName";
/*     */   private static final String ERR_MULTIPLE_TYPES_IN_ENUM = "EnumerationXducer.MultipleTypesInEnum";
/*     */   private static final String ERR_NAME_COLLISION = "EnumerationXducer.NameCollision";
/*     */   
/*     */   public JType getReturnType() {
/*  69 */     return (JType)this.type;
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
/*     */   public EnumerationXducer(NameConverter _nc, JDefinedClass clz, Expression enumExp, Map _members, Locator _sourceLocator) {
/* 114 */     this.populated = false;
/*     */     this.type = clz;
/*     */     this.codeModel = clz.owner();
/*     */     this.nameConverter = _nc;
/*     */     this.members = _members;
/*     */     this.sourceLocator = _sourceLocator;
/*     */     this.values = getValues(enumExp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void populate(AnnotatedGrammar grammar, GeneratorContext context) {
/* 131 */     if (this.populated)
/* 132 */       return;  this.populated = true;
/*     */ 
/*     */     
/* 135 */     JClass stringRef = this.codeModel.ref(String.class);
/* 136 */     JClass objectRef = this.codeModel.ref(Object.class);
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (!sanityCheck(context)) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     Transducer xducer = BuiltinDatatypeTransducerFactory.get(grammar, (XSDatatype)(this.values[0]).dt);
/*     */     
/* 146 */     this.valueType = xducer.getReturnType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     JFieldVar jFieldVar1 = this.type.field(28, Map.class, "valueMap", (JExpression)JExpr._new(this.codeModel.ref(HashMap.class)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     this.items = new JFieldVar[this.values.length];
/* 164 */     JVar[] valueObjs = new JVar[this.values.length];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     Set enumFieldNames = new HashSet();
/*     */     
/* 173 */     for (int i = 0; i < this.values.length; i++) {
/*     */       String lexical;
/*     */       
/* 176 */       if ((this.values[i]).dt instanceof XSDatatype) {
/*     */         
/* 178 */         lexical = ((XSDatatype)(this.values[i]).dt).convertToLexicalValue((this.values[i]).value, null);
/*     */       }
/*     */       else {
/*     */         
/* 182 */         lexical = (this.values[i]).value.toString();
/*     */       } 
/* 184 */       MemberInfo mem = (MemberInfo)this.members.get(this.values[i]);
/* 185 */       String constName = null;
/*     */       
/* 187 */       if (mem != null) {
/* 188 */         constName = mem.name;
/*     */       }
/* 190 */       if (constName == null) {
/* 191 */         constName = this.nameConverter.toConstantName(fixUnsafeCharacters(lexical));
/*     */       }
/* 193 */       if (!JJavaName.isJavaIdentifier(constName))
/*     */       {
/* 195 */         reportError(context, Messages.format("EnumerationXducer.UnusableName", lexical, constName));
/*     */       }
/*     */ 
/*     */       
/* 199 */       if (!enumFieldNames.add(constName)) {
/* 200 */         reportError(context, Messages.format("EnumerationXducer.NameCollision", constName));
/*     */ 
/*     */       
/*     */       }
/* 204 */       else if (!enumFieldNames.add('_' + constName)) {
/* 205 */         reportError(context, Messages.format("EnumerationXducer.NameCollision", '_' + constName));
/*     */       } 
/* 207 */       valueObjs[i] = (JVar)this.type.field(25, this.valueType, '_' + constName);
/*     */ 
/*     */       
/* 210 */       this.items[i] = this.type.field(25, (JType)this.type, constName);
/*     */ 
/*     */       
/* 213 */       this.items[i].init((JExpression)JExpr._new((JClass)this.type).arg((JExpression)valueObjs[i]));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 218 */       if (mem != null && mem.javadoc != null) {
/* 219 */         this.items[i].javadoc().appendComment(mem.javadoc);
/*     */       }
/*     */       
/* 222 */       valueObjs[i].init(xducer.generateDeserializer((JExpression)this.codeModel.ref(DatatypeConverterImpl.class).staticInvoke("installHook").arg(JExpr.lit(lexical)), null));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     JFieldVar jFieldVar2 = this.type.field(12, (JType)stringRef, "lexicalValue");
/*     */ 
/*     */     
/* 236 */     JFieldVar jFieldVar3 = this.type.field(12, this.valueType, "value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     JMethod m = this.type.constructor(2);
/* 248 */     JVar jVar1 = m.param(this.valueType, "v");
/* 249 */     m.body().assign((JAssignmentTarget)jFieldVar3, (JExpression)jVar1);
/* 250 */     m.body().assign((JAssignmentTarget)jFieldVar2, xducer.generateSerializer((JExpression)jVar1, null));
/*     */     
/* 252 */     m.body().invoke((JExpression)jFieldVar1, "put").arg(wrapToObject((JExpression)jVar1)).arg(JExpr._this());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     this.type.method(1, (JType)stringRef, "toString").body()._return((JExpression)jFieldVar2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     this.type.method(1, this.valueType, "getValue").body()._return((JExpression)jFieldVar3);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     this.type.method(9, (JType)this.codeModel.INT, "hashCode").body()._return((JExpression)JExpr._super().invoke("hashCode"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     m = this.type.method(9, (JType)this.codeModel.BOOLEAN, "equals");
/* 274 */     JVar o = m.param(Object.class, "o");
/* 275 */     m.body()._return((JExpression)JExpr._super().invoke("equals").arg((JExpression)o));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     JMethod fromValue = this.type.method(17, (JType)this.type, "fromValue");
/* 289 */     JVar $v = fromValue.param(this.valueType, "value");
/*     */     
/* 291 */     JVar $t = fromValue.body().decl((JType)this.type, "t", (JExpression)JExpr.cast((JType)this.type, (JExpression)jFieldVar1.invoke("get").arg(wrapToObject((JExpression)$v))));
/*     */ 
/*     */     
/* 294 */     JConditional cond = fromValue.body()._if($t.eq(JExpr._null()));
/* 295 */     cond._then()._throw((JExpression)JExpr._new(this.codeModel.ref(IllegalArgumentException.class)));
/* 296 */     cond._else()._return((JExpression)$t);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     JMethod fromString = this.type.method(17, (JType)this.type, "fromString");
/* 305 */     JVar $str = fromString.param((JType)stringRef, "str");
/*     */     
/* 307 */     JExpression rhs = xducer.generateDeserializer((JExpression)$str, null);
/* 308 */     fromString.body()._return((JExpression)JExpr.invoke("fromValue").arg(rhs));
/*     */ 
/*     */ 
/*     */     
/* 312 */     if (grammar.serialVersionUID != null) {
/*     */       
/* 314 */       this.type._implements(Serializable.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 320 */       this.type.method(4, (JType)objectRef, "readResolve").body()._return((JExpression)JExpr.invoke("fromValue").arg((JExpression)JExpr.invoke("getValue")));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JExpression wrapToObject(JExpression var) {
/* 329 */     if (this.valueType.isPrimitive()) {
/* 330 */       return ((JPrimitiveType)this.valueType).wrap(var);
/*     */     }
/* 332 */     return var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String fixUnsafeCharacters(String lexical) {
/* 342 */     StringBuffer buf = new StringBuffer();
/* 343 */     int len = lexical.length();
/* 344 */     for (int i = 0; i < len; i++) {
/* 345 */       char ch = lexical.charAt(i);
/* 346 */       if (!Character.isJavaIdentifierPart(ch)) {
/* 347 */         buf.append('-');
/*     */       } else {
/* 349 */         buf.append(ch);
/*     */       } 
/* 351 */     }  return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sanityCheck(GeneratorContext context) {
/* 359 */     for (int i = 0; i < this.values.length; i++) {
/* 360 */       if ((this.values[i]).dt.isContextDependent()) {
/* 361 */         reportError(context, Messages.format("EnumerationXducer.ContextDependentType"));
/* 362 */         return false;
/*     */       } 
/* 364 */       if (!((this.values[i]).dt instanceof XSDatatype)) {
/* 365 */         reportError(context, Messages.format("EnumerationXducer.UnsupportedTypeForEnum", this.values[i].getName()));
/*     */       }
/*     */ 
/*     */       
/* 369 */       if (!(this.values[0]).dt.equals((this.values[i]).dt)) {
/* 370 */         reportError(context, Messages.format("EnumerationXducer.MultipleTypesInEnum", (this.values[0]).name, (this.values[i]).name));
/*     */ 
/*     */ 
/*     */         
/* 374 */         return false;
/*     */       } 
/*     */     } 
/* 377 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 382 */     return (JExpression)value.invoke("toString");
/*     */   }
/*     */   
/*     */   public JExpression generateDeserializer(JExpression value, DeserializerContext context) {
/* 386 */     return (JExpression)this.type.staticInvoke("fromString").arg(value);
/*     */   }
/*     */   
/*     */   public JExpression generateConstant(ValueExp exp) {
/* 390 */     for (int i = 0; i < this.values.length; i++) {
/* 391 */       if (exp.dt.sameValue((this.values[i]).value, exp.value)) {
/* 392 */         return (JExpression)this.type.staticRef(this.items[i].name());
/*     */       }
/*     */     } 
/*     */     
/* 396 */     throw new JAXBAssertionError();
/*     */   }
/*     */ 
/*     */   
/*     */   private ValueExp[] getValues(Expression exp) {
/* 401 */     if (!(exp instanceof ChoiceExp)) {
/* 402 */       if (!(exp instanceof ValueExp)) {
/* 403 */         System.out.println(ExpressionPrinter.printContentModel(exp));
/*     */         
/* 405 */         throw new InternalError("assertion failed");
/*     */       } 
/* 407 */       return new ValueExp[] { (ValueExp)exp };
/*     */     } 
/* 409 */     Expression[] children = ((ChoiceExp)exp).getChildren();
/* 410 */     ValueExp[] values = new ValueExp[children.length];
/* 411 */     System.arraycopy(children, 0, values, 0, children.length);
/* 412 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(GeneratorContext context, String msg) {
/* 419 */     context.getErrorReceiver().error(this.sourceLocator, msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\EnumerationXducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */