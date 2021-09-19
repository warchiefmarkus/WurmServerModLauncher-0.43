/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.ChoiceNameClass;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.util.NameClassCollisionChecker;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*     */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.WildcardNameClassBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.AbstractCTBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.Messages;
/*     */ import com.sun.xml.xsom.XSAttContainer;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedComplexTypeBuilder
/*     */   extends AbstractCTBuilder
/*     */ {
/*  48 */   private final Map characteristicNameClasses = new HashMap();
/*     */   
/*     */   public ExtendedComplexTypeBuilder(ComplexTypeFieldBuilder _builder) {
/*  51 */     super(_builder);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     this.collisionChecker = new NameClassCollisionChecker();
/*     */ 
/*     */ 
/*     */     
/* 251 */     this.contentModelNameClassBuilder = (XSTermFunction)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final NameClassCollisionChecker collisionChecker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final XSTermFunction contentModelNameClassBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isApplicable(XSComplexType ct) {
/*     */     XSType baseType = ct.getBaseType();
/*     */     return (baseType != this.bgmBuilder.schemas.getAnyType() && baseType.isComplexType() && ct.getDerivationMethod() == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSComplexType getLastRestrictedType(XSComplexType t) {
/* 282 */     if (t.getBaseType() == this.bgmBuilder.schemas.getAnyType())
/* 283 */       return null; 
/* 284 */     if (t.getDerivationMethod() == 2) {
/* 285 */       return t;
/*     */     }
/* 287 */     XSComplexType baseType = t.getBaseType().asComplexType();
/* 288 */     if (baseType != null) {
/* 289 */       return getLastRestrictedType(baseType);
/*     */     }
/* 291 */     return null;
/*     */   }
/*     */   
/*     */   public Expression build(XSComplexType ct) {
/*     */     Expression expression;
/*     */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*     */     ClassItem baseClass = this.bgmBuilder.selector.bindToType(baseType);
/*     */     _assert((baseClass != null));
/*     */     SuperClassItem superClassItem = new SuperClassItem((Expression)baseClass, ct.getLocator());
/*     */     ComplexTypeBindingMode baseTypeFlag = this.builder.getBindingMode(baseType);
/*     */     XSContentType explicitContent = ct.getExplicitContent();
/*     */     if (!checkIfExtensionSafe(baseType, ct)) {
/*     */       this.bgmBuilder.errorReceiver.error(ct.getLocator(), Messages.format("FieldBuilder.NoFurtherExtension", baseType.getName(), ct.getName()));
/*     */       return Expression.epsilon;
/*     */     } 
/*     */     if (explicitContent != null && explicitContent.asParticle() != null) {
/*     */       if (baseTypeFlag == ComplexTypeBindingMode.NORMAL) {
/*     */         this.builder.recordBindingMode(ct, this.bgmBuilder.particleBinder.checkFallback(explicitContent.asParticle(), baseClass) ? ComplexTypeBindingMode.FALLBACK_REST : ComplexTypeBindingMode.NORMAL);
/*     */         expression = this.pool.createSequence((Expression)superClassItem, this.bgmBuilder.particleBinder.build(explicitContent.asParticle(), baseClass));
/*     */       } else {
/*     */         Expression body = this.bgmBuilder.typeBuilder.build((XSComponent)explicitContent);
/*     */         if (ct.isMixed())
/*     */           body = this.pool.createInterleave(this.pool.createZeroOrMore((Expression)this.bgmBuilder.grammar.createPrimitiveItem((Transducer)new IdentityTransducer(this.bgmBuilder.grammar.codeModel), (DatabindableDatatype)StringType.theInstance, this.pool.createData((XSDatatype)StringType.theInstance), ct.getLocator())), body); 
/*     */         FieldItem fi = new FieldItem((baseTypeFlag == ComplexTypeBindingMode.FALLBACK_CONTENT) ? "Content" : "Rest", body, ct.getLocator());
/*     */         fi.multiplicity = Multiplicity.star;
/*     */         fi.collisionExpected = true;
/*     */         expression = this.pool.createSequence(expression, (Expression)fi);
/*     */         this.builder.recordBindingMode(ct, baseTypeFlag);
/*     */       } 
/*     */     } else {
/*     */       this.builder.recordBindingMode(ct, baseTypeFlag);
/*     */     } 
/*     */     return this.pool.createSequence(this.bgmBuilder.fieldBuilder.attributeContainer((XSAttContainer)ct), expression);
/*     */   }
/*     */   
/*     */   private boolean checkIfExtensionSafe(XSComplexType baseType, XSComplexType thisType) {
/*     */     ChoiceNameClass choiceNameClass;
/*     */     XSComplexType lastType = getLastRestrictedType(baseType);
/*     */     if (lastType == null)
/*     */       return true; 
/*     */     NameClass anc = NameClass.NONE;
/*     */     Iterator itr = thisType.iterateDeclaredAttributeUses();
/*     */     while (itr.hasNext())
/*     */       choiceNameClass = new ChoiceNameClass(anc, getNameClass((XSDeclaration)((XSAttributeUse)itr.next()).getDecl())); 
/*     */     NameClass nameClass1 = choiceNameClass.simplify();
/*     */     NameClass enc = getNameClass(thisType.getExplicitContent()).simplify();
/*     */     while (lastType != lastType.getBaseType()) {
/*     */       if (checkCollision(nameClass1, enc, lastType))
/*     */         return false; 
/*     */       if (lastType.getBaseType().isSimpleType())
/*     */         return true; 
/*     */       lastType = lastType.getBaseType().asComplexType();
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   private boolean checkCollision(NameClass anc, NameClass enc, XSComplexType type) {
/*     */     ChoiceNameClass choiceNameClass;
/*     */     NameClass[] chnc = (NameClass[])this.characteristicNameClasses.get(type);
/*     */     if (chnc == null) {
/*     */       ChoiceNameClass choiceNameClass1;
/*     */       chnc = new NameClass[2];
/*     */       chnc[0] = getNameClass(type.getContentType());
/*     */       NameClass nc = NameClass.NONE;
/*     */       Iterator itr = type.iterateAttributeUses();
/*     */       while (itr.hasNext())
/*     */         choiceNameClass = new ChoiceNameClass(anc, getNameClass((XSDeclaration)((XSAttributeUse)itr.next()).getDecl())); 
/*     */       XSWildcard wc = type.getAttributeWildcard();
/*     */       if (wc != null)
/*     */         choiceNameClass1 = new ChoiceNameClass(nc, WildcardNameClassBuilder.build(wc)); 
/*     */       chnc[1] = (NameClass)choiceNameClass1;
/*     */       this.characteristicNameClasses.put(type, chnc);
/*     */     } 
/*     */     return (this.collisionChecker.check(chnc[0], enc) || this.collisionChecker.check(chnc[1], (NameClass)choiceNameClass));
/*     */   }
/*     */   
/*     */   private NameClass getNameClass(XSContentType t) {
/*     */     if (t == null)
/*     */       return NameClass.NONE; 
/*     */     XSParticle p = t.asParticle();
/*     */     if (p == null)
/*     */       return NameClass.NONE; 
/*     */     return (NameClass)p.getTerm().apply(this.contentModelNameClassBuilder);
/*     */   }
/*     */   
/*     */   private NameClass getNameClass(XSDeclaration decl) {
/*     */     return (NameClass)new SimpleNameClass(decl.getTargetNamespace(), decl.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\ExtendedComplexTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */