/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.BooleanType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.generator.field.ConstFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.field.XsiNilFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.XsiTypeFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.util.WhitespaceNormalizer;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.DefaultValue;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*     */ import com.sun.tools.xjc.grammar.xducer.BuiltinDatatypeTransducerFactory;
/*     */ import com.sun.tools.xjc.grammar.xducer.WhitespaceTransducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.AbstractXSFunctionImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.FixedExpBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.tools.xjc.reader.xmlschema.NameGenerator;
/*     */ import com.sun.tools.xjc.reader.xmlschema.PrefixedJClassFactoryImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.xsom.XSAttContainer;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.xsom.impl.ParticleImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldBuilder
/*     */   extends AbstractXSFunctionImpl
/*     */ {
/*     */   private final BGMBuilder builder;
/*     */   private final ExpressionPool pool;
/*     */   
/*     */   FieldBuilder(BGMBuilder _builder) {
/*  70 */     this.builder = _builder;
/*  71 */     this.pool = this.builder.pool;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Expression build(XSComponent sc) {
/*  76 */     return (Expression)sc.apply((XSFunction)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  84 */     return attributeContainer((XSAttContainer)decl);
/*     */   }
/*     */   
/*     */   public Expression attributeContainer(XSAttContainer decl) {
/*  88 */     Expression exp = Expression.epsilon;
/*     */     
/*  90 */     Iterator itr = decl.iterateAttGroups();
/*  91 */     while (itr.hasNext()) {
/*  92 */       exp = this.pool.createSequence(exp, build((XSComponent)itr.next()));
/*     */     }
/*  94 */     itr = decl.iterateDeclaredAttributeUses();
/*  95 */     while (itr.hasNext()) {
/*  96 */       exp = this.pool.createSequence(exp, build((XSComponent)itr.next()));
/*     */     }
/*  98 */     return exp;
/*     */   }
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl arg0) {
/* 102 */     _assert(false);
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/* 107 */     BIProperty cust = getPropCustomization(use);
/*     */ 
/*     */     
/* 110 */     AttributeExp body = this.builder.typeBuilder._attributeDecl(use.getDecl());
/* 111 */     Expression originalBody = body.exp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     boolean hasFixedValue = (use.getFixedValue() != null);
/*     */     
/* 120 */     if (hasFixedValue) {
/*     */       
/* 122 */       String token = use.getFixedValue();
/*     */       
/* 124 */       Expression contents = FixedExpBuilder.build(body.exp, token, this.builder.grammar, use.getContext());
/*     */       
/* 126 */       if (contents == Expression.nullSet) {
/*     */         Locator loc;
/* 128 */         if (use.getDecl().getFixedValue() != null) { loc = use.getDecl().getLocator(); }
/* 129 */         else { loc = use.getLocator(); }
/* 130 */          this.builder.errorReporter.error(loc, "FieldBuilder.IncorrectFixedValue", token);
/*     */       } else {
/* 132 */         body = new AttributeExp(body.nameClass, contents);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     boolean toConstant = (BIProperty.getCustomization(this.builder, (XSComponent)use).isConstantProperty() && use.getFixedValue() != null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     String xmlName = use.getDecl().getName();
/* 148 */     String defaultName = toConstant ? makeJavaConstName(xmlName) : makeJavaName(xmlName);
/*     */ 
/*     */     
/* 151 */     FieldItem exp = createFieldItem(defaultName, toConstant, (Expression)body, (XSComponent)use);
/*     */ 
/*     */     
/* 154 */     if (use.getDefaultValue() != null) {
/*     */       
/* 156 */       String token = use.getDefaultValue();
/*     */       
/* 158 */       Expression contents = FixedExpBuilder.build(body.exp, token, this.builder.grammar, use.getContext());
/*     */       
/* 160 */       if (contents == Expression.nullSet) {
/*     */         Locator loc;
/* 162 */         if (use.getDecl().getDefaultValue() != null) { loc = use.getDecl().getLocator(); }
/* 163 */         else { loc = use.getLocator(); }
/* 164 */          this.builder.errorReporter.error(loc, "FieldBuilder.IncorrectDefaultValue", token);
/*     */       } 
/*     */       
/* 167 */       ArrayList values = new ArrayList();
/* 168 */       contents.visit((ExpressionVisitorVoid)new Object(this, values));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 174 */       exp.defaultValues = (DefaultValue[])values.toArray((Object[])new DefaultValue[values.size()]);
/*     */     } 
/*     */     
/* 177 */     if (toConstant)
/*     */     {
/*     */       
/* 180 */       exp.realization = ConstFieldRenderer.theFactory;
/*     */     }
/* 182 */     if (hasFixedValue)
/*     */     {
/*     */       
/* 185 */       originalBody.visit((ExpressionVisitorVoid)new Object(this, exp, use, cust));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     if (!use.isRequired()) return this.pool.createOptional((Expression)exp); 
/* 208 */     return exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BIProperty getPropCustomization(XSAttributeUse use) {
/* 216 */     BIProperty cust = (BIProperty)this.builder.getBindInfo((XSComponent)use).get(BIProperty.NAME);
/* 217 */     if (cust != null) return cust;
/*     */ 
/*     */     
/* 220 */     return (BIProperty)this.builder.getBindInfo((XSComponent)use.getDecl()).get(BIProperty.NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/* 228 */     return this.builder.complexTypeBuilder.build(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object simpleType(XSSimpleType type) {
/* 234 */     return simpleType(type, (XSComponent)type.getOwnerSchema());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression simpleType(XSSimpleType type, XSComponent property) {
/* 245 */     BIProperty prop = BIProperty.getCustomization(this.builder, property);
/*     */ 
/*     */     
/* 248 */     return (Expression)prop.createFieldItem("Value", false, this.builder.simpleTypeBuilder.build(type), (XSComponent)type);
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
/*     */   public Object particle(XSParticle p) {
/* 274 */     _assert(false);
/* 275 */     return null;
/*     */   }
/*     */   
/*     */   private Expression particle(XSParticle p, ClassItem superClass) {
/* 279 */     return this.builder.particleBinder.build(p, superClass);
/*     */   }
/*     */   
/*     */   public Object empty(XSContentType ct) {
/* 283 */     return Expression.epsilon;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private XSParticle makeParticle(XSTerm t) {
/* 289 */     return (XSParticle)new ParticleImpl(null, null, (Ref.Term)t, t.getLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 295 */     this.builder.selector.pushClassFactory((JClassFactory)new PrefixedJClassFactoryImpl(this.builder, decl));
/*     */ 
/*     */     
/* 298 */     Object r = build((XSComponent)decl.getModelGroup());
/*     */     
/* 300 */     this.builder.selector.popClassFactory();
/*     */     
/* 302 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 307 */     return particle(makeParticle((XSTerm)wc), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object modelGroup(XSModelGroup mg) {
/* 312 */     if (this.builder.getGlobalBinding().isModelGroupBinding())
/*     */     {
/* 314 */       return this.builder.applyRecursively(mg, (BGMBuilder.ParticleHandler)new Object(this));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     ModelGroupImpl modelGroupImpl = new ModelGroupImpl((SchemaImpl)mg.getOwnerSchema(), null, mg.getLocator(), mg.getCompositor(), mg.getChildren());
/*     */ 
/*     */ 
/*     */     
/* 327 */     return particle(makeParticle((XSTerm)modelGroupImpl), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldItem createFieldItem(Expression typeExp, XSDeclaration source, boolean forConstant) {
/* 336 */     String defaultName = this.builder.getNameConverter().toPropertyName(source.getName());
/*     */     
/* 338 */     return createFieldItem(defaultName, forConstant, typeExp, (XSComponent)source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression createFieldItem(Expression typeExp, XSModelGroup modelGroup) {
/*     */     try {
/* 347 */       String defaultName = NameGenerator.getName(this.builder, modelGroup);
/* 348 */       return (Expression)createFieldItem(defaultName, false, typeExp, (XSComponent)modelGroup);
/* 349 */     } catch (ParseException e) {
/*     */       
/* 351 */       this.builder.errorReporter.error(modelGroup.getLocator(), "ClassSelector.ClassNameIsRequired");
/*     */ 
/*     */ 
/*     */       
/* 355 */       return Expression.epsilon;
/*     */     } 
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
/*     */   public FieldItem createFieldItem(String defaultName, boolean forConstant, Expression typeExp, XSComponent source) {
/* 374 */     BIProperty cust = BIProperty.getCustomization(this.builder, source);
/* 375 */     return cust.createFieldItem(defaultName, forConstant, typeExp, source);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/*     */     Expression body;
/* 381 */     boolean isMappedToType = (this.builder.selector.bindToType(decl) != null);
/*     */     
/* 383 */     if (!isMappedToType) {
/* 384 */       return createFieldItem((Expression)this.builder.typeBuilder.elementDeclFlat(decl), (XSDeclaration)decl, false);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 389 */     Expression type = this.builder.selector.bindToType((XSComponent)decl.getType());
/*     */     
/* 391 */     if (type != null) {
/*     */       
/* 393 */       _assert(type instanceof ClassItem);
/* 394 */       ClassItem defaultType = (ClassItem)type;
/*     */       
/* 396 */       if (decl.getType() instanceof XSComplexType && this.builder.getGlobalBinding().isTypeSubstitutionSupportEnabled()) {
/*     */ 
/*     */ 
/*     */         
/* 400 */         type = this.pool.createChoice(type, this.builder.getTypeSubstitutionList((XSComplexType)decl.getType(), false));
/*     */       } else {
/*     */         
/* 403 */         type = this.pool.createSequence(type, this.builder.createXsiTypeExp(decl));
/*     */       } 
/*     */       
/* 406 */       if (this.builder.getGlobalBinding().isTypeSubstitutionSupportEnabled()) {
/*     */ 
/*     */         
/* 409 */         FieldItem fi = new FieldItem("ValueObject", type, decl.getLocator());
/* 410 */         fi.realization = (FieldRendererFactory)new XsiTypeFieldRenderer.Factory(defaultType);
/* 411 */         fi.setDelegation(true);
/* 412 */         fi.javadoc = Messages.format("FieldBuilder.Javadoc.ValueObject", defaultType.getType().fullName(), fi.name);
/*     */         
/* 414 */         FieldItem fieldItem1 = fi;
/*     */       } else {
/*     */         
/* 417 */         SuperClassItem superClassItem = new SuperClassItem(type, decl.getLocator());
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 425 */       this.builder.simpleTypeBuilder.refererStack.push(decl);
/*     */       
/* 427 */       body = build((XSComponent)decl.getType());
/*     */       
/* 429 */       this.builder.simpleTypeBuilder.refererStack.pop();
/*     */       
/* 431 */       body = this.pool.createSequence(body, this.builder.createXsiTypeExp(decl));
/*     */     } 
/*     */     
/* 434 */     SimpleNameClass name = new SimpleNameClass(decl.getTargetNamespace(), decl.getName());
/*     */ 
/*     */     
/* 437 */     if (decl.isNillable()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 452 */       FieldItem fi = new FieldItem("Nil", buildXsiNilExpForClass(), decl.getLocator());
/*     */       
/* 454 */       fi.realization = XsiNilFieldRenderer.theFactory;
/* 455 */       fi.javadoc = Messages.format("FieldBuilder.Javadoc.NilProperty");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 461 */       body.visit((ExpressionVisitorVoid)new Object(this));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 469 */       body = this.pool.createChoice((Expression)fi, body);
/*     */     } 
/*     */     
/* 472 */     return new ElementPattern((NameClass)name, body);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression buildXsiNilExpForClass() {
/* 481 */     return (Expression)new AttributeExp((NameClass)new SimpleNameClass("http://www.w3.org/2001/XMLSchema-instance", "nil"), (Expression)this.builder.grammar.createPrimitiveItem(WhitespaceTransducer.create(BuiltinDatatypeTransducerFactory.get(this.builder.grammar, (XSDatatype)BooleanType.theInstance), this.builder.grammar.codeModel, WhitespaceNormalizer.COLLAPSE), (DatabindableDatatype)BooleanType.theInstance, this.pool.createData((XSDatatype)BooleanType.theInstance), null));
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
/*     */   private String makeJavaName(String xmlName) {
/* 497 */     return this.builder.getNameConverter().toPropertyName(xmlName);
/*     */   }
/*     */ 
/*     */   
/*     */   private String makeJavaConstName(String xmlName) {
/* 502 */     return this.builder.getNameConverter().toConstantName(xmlName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\FieldBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */