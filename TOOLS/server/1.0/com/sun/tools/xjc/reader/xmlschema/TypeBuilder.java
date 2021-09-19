/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.BooleanType;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.grammar.ext.WildcardItem;
/*     */ import com.sun.tools.xjc.grammar.xducer.NilTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.AbstractXSFunctionImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.PrefixedJClassFactoryImpl;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.bind.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeBuilder
/*     */   extends AbstractXSFunctionImpl
/*     */   implements BGMBuilder.ParticleHandler
/*     */ {
/*     */   private final BGMBuilder builder;
/*     */   private final ExpressionPool pool;
/*     */   
/*     */   TypeBuilder(BGMBuilder _builder) {
/*  52 */     this.builder = _builder;
/*  53 */     this.pool = this.builder.pool;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Expression build(XSComponent sc) {
/*  58 */     return (Expression)sc.apply((XSFunction)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl agd) {
/*  68 */     Expression exp = this.builder.selector.bindToType((XSComponent)agd);
/*  69 */     if (exp != null) return exp;
/*     */ 
/*     */     
/*  72 */     _assert(false);
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  77 */     return _attributeDecl(decl);
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
/*     */   public AttributeExp _attributeDecl(XSAttributeDecl decl) {
/*  91 */     this.builder.simpleTypeBuilder.refererStack.push(decl);
/*     */     
/*  93 */     AttributeExp exp = (AttributeExp)this.pool.createAttribute((NameClass)new SimpleNameClass(decl.getTargetNamespace(), decl.getName()), this.builder.simpleTypeBuilder.build(decl.getType()));
/*     */ 
/*     */ 
/*     */     
/*  97 */     this.builder.simpleTypeBuilder.refererStack.pop();
/*     */     
/*  99 */     return exp;
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/* 103 */     Expression exp = this.builder.selector.bindToType((XSComponent)use);
/* 104 */     if (exp != null) return exp;
/*     */ 
/*     */     
/* 107 */     _assert(false);
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/* 112 */     return this.builder.selector.bindToType(type);
/*     */   }
/*     */   
/*     */   public Object simpleType(XSSimpleType type) {
/* 116 */     Expression exp = this.builder.selector.bindToType((XSComponent)type);
/* 117 */     if (exp != null) return exp;
/*     */     
/* 119 */     return this.builder.simpleTypeBuilder.build(type);
/*     */   }
/*     */   
/*     */   public Object particle(XSParticle p) {
/* 123 */     Expression exp = this.builder.selector.bindToType((XSComponent)p);
/* 124 */     if (exp != null) return exp;
/*     */     
/* 126 */     return this.builder.processMinMax(build((XSComponent)p.getTerm()), p);
/*     */   }
/*     */   
/*     */   public Object empty(XSContentType empty) {
/* 130 */     Expression exp = this.builder.selector.bindToType((XSComponent)empty);
/* 131 */     if (exp != null) return exp;
/*     */     
/* 133 */     return Expression.epsilon;
/*     */   }
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 137 */     Expression exp = this.builder.selector.bindToType((XSComponent)wc);
/* 138 */     if (exp != null) return exp;
/*     */ 
/*     */     
/* 141 */     return new WildcardItem(this.builder.grammar.codeModel, wc);
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 145 */     Expression exp = this.builder.selector.bindToType((XSComponent)decl);
/* 146 */     if (exp != null) return exp;
/*     */ 
/*     */ 
/*     */     
/* 150 */     this.builder.selector.pushClassFactory((JClassFactory)new PrefixedJClassFactoryImpl(this.builder, decl));
/*     */ 
/*     */     
/* 153 */     exp = build((XSComponent)decl.getModelGroup());
/*     */     
/* 155 */     this.builder.selector.popClassFactory();
/*     */     
/* 157 */     return exp;
/*     */   }
/*     */   
/*     */   public Object modelGroup(XSModelGroup mg) {
/* 161 */     Expression exp = this.builder.selector.bindToType((XSComponent)mg);
/* 162 */     if (exp != null) return exp;
/*     */     
/* 164 */     return this.builder.applyRecursively(mg, this);
/*     */   }
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 168 */     Expression exp = Expression.nullSet;
/*     */     
/* 170 */     for (Iterator itr = decl.getSubstitutables().iterator(); itr.hasNext(); ) {
/* 171 */       XSElementDecl e = itr.next();
/* 172 */       if (e.isAbstract())
/* 173 */         continue;  exp = this.pool.createChoice(exp, (Expression)elementDeclWithoutSubstGroup(e));
/*     */     } 
/*     */ 
/*     */     
/* 177 */     return exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeItem elementDeclWithoutSubstGroup(XSElementDecl decl) {
/* 185 */     TypeItem ti = this.builder.selector.bindToType(decl);
/* 186 */     if (ti != null) return ti;
/*     */     
/* 188 */     JDefinedClass cls = this.builder.selector.getClassFactory().create(this.builder.getNameConverter().toClassName(decl.getName()), decl.getLocator());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     cls._implements(Element.class);
/*     */     
/* 196 */     ClassItem ci = this.builder.grammar.createClassItem(cls, Expression.epsilon, decl.getLocator());
/*     */ 
/*     */     
/* 199 */     this.builder.selector.queueBuild((XSComponent)decl, ci);
/* 200 */     return (TypeItem)ci;
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
/*     */   protected ElementPattern elementDeclFlat(XSElementDecl decl) {
/*     */     Expression body;
/* 215 */     this.builder.selector.bindToType(decl);
/*     */     
/* 217 */     Expression type = this.builder.selector.bindToType((XSComponent)decl.getType());
/*     */     
/* 219 */     if (type != null) {
/*     */ 
/*     */       
/* 222 */       body = type;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 233 */       this.builder.simpleTypeBuilder.refererStack.push(decl);
/*     */       
/* 235 */       body = this.builder.typeBuilder.build((XSComponent)decl.getType());
/*     */       
/* 237 */       this.builder.simpleTypeBuilder.refererStack.pop();
/*     */     } 
/*     */     
/* 240 */     if (decl.isNillable())
/*     */     {
/* 242 */       body = this.pool.createChoice(buildXsiNilExpForProperty(), body);
/*     */     }
/*     */     
/* 245 */     if (decl.getType().isComplexType() && this.builder.getGlobalBinding().isTypeSubstitutionSupportEnabled()) {
/*     */       
/* 247 */       body = this.pool.createChoice(body, this.builder.getTypeSubstitutionList(decl.getType().asComplexType(), false));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 253 */       body = this.pool.createSequence(body, this.builder.createXsiTypeExp(decl));
/*     */     } 
/*     */     
/* 256 */     SimpleNameClass name = new SimpleNameClass(decl.getTargetNamespace(), decl.getName());
/*     */ 
/*     */     
/* 259 */     return new ElementPattern((NameClass)name, body);
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
/*     */   private Expression buildXsiNilExpForProperty() {
/* 273 */     return (Expression)new AttributeExp((NameClass)new SimpleNameClass("http://www.w3.org/2001/XMLSchema-instance", "nil"), (Expression)this.builder.grammar.createPrimitiveItem((Transducer)new NilTransducer(this.builder.grammar.codeModel), (DatabindableDatatype)StringType.theInstance, this.pool.createValue((XSDatatype)BooleanType.theInstance, Boolean.TRUE), null));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\TypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */