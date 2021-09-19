/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*     */ 
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.NameGenerator;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.AbstractBinderImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassSelector;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.Messages;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import java.text.ParseException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ModelGroupBindingClassBinder
/*     */   extends AbstractBinderImpl
/*     */ {
/*     */   private final ClassBinder base;
/*  46 */   private final Set topLevelChoices = new HashSet();
/*     */   
/*     */   ModelGroupBindingClassBinder(ClassSelector classSelector, ClassBinder base) {
/*  49 */     super(classSelector);
/*  50 */     this.base = base;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object modelGroup(XSModelGroup mgroup) {
/*  55 */     ClassItem ci = (ClassItem)this.base.modelGroup(mgroup);
/*     */     
/*  57 */     if (mgroup.getCompositor() == XSModelGroup.CHOICE && !this.topLevelChoices.contains(mgroup)) {
/*  58 */       if (ci == null && !this.builder.getGlobalBinding().isChoiceContentPropertyModelGroupBinding()) {
/*     */         
/*     */         try {
/*  61 */           JDefinedClass clazz = this.owner.getClassFactory().create(NameGenerator.getName(this.owner.builder, mgroup), mgroup.getLocator());
/*     */ 
/*     */ 
/*     */           
/*  65 */           ci = wrapByClassItem((XSComponent)mgroup, clazz);
/*  66 */         } catch (ParseException e) {
/*     */           
/*  68 */           this.builder.errorReceiver.error(mgroup.getLocator(), Messages.format("DefaultParticleBinder.UnableToGenerateNameFromModelGroup"));
/*     */ 
/*     */ 
/*     */           
/*  72 */           ci = null;
/*     */         } 
/*     */       }
/*     */       
/*  76 */       if (ci != null)
/*     */       {
/*     */         
/*  79 */         ci.hasGetContentMethod = true;
/*     */       }
/*     */     } 
/*  82 */     return ci;
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/*  86 */     ClassItem ci = (ClassItem)this.base.complexType(type);
/*  87 */     if (ci == null) return null;
/*     */     
/*  89 */     if (needsToHaveChoiceContentProperty(type)) {
/*  90 */       this.topLevelChoices.add(type.getContentType().asParticle().getTerm());
/*  91 */       ci.hasGetContentMethod = true;
/*     */     } 
/*     */     
/*  94 */     return ci;
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/*  98 */     ClassItem ci = (ClassItem)this.base.modelGroupDecl(decl);
/*  99 */     if (ci != null) return ci;
/*     */ 
/*     */     
/* 102 */     JPackage pkg = this.owner.getPackage(decl.getTargetNamespace());
/*     */     
/* 104 */     JDefinedClass clazz = this.owner.codeModelClassFactory.createInterface((JClassContainer)pkg, deriveName((XSDeclaration)decl), decl.getLocator());
/*     */ 
/*     */     
/* 107 */     ci = wrapByClassItem((XSComponent)decl, clazz);
/*     */     
/* 109 */     if (needsToHaveChoiceContentProperty(decl)) {
/* 110 */       ci.hasGetContentMethod = true;
/*     */     }
/* 112 */     return ci;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needsToHaveChoiceContentProperty(XSComplexType type) {
/* 122 */     if (type.iterateDeclaredAttributeUses().hasNext()) {
/* 123 */       return false;
/*     */     }
/* 125 */     XSParticle p = type.getContentType().asParticle();
/* 126 */     if (p == null) return false;
/*     */     
/* 128 */     if (p.getMaxOccurs() != 1) return false;
/*     */     
/* 130 */     XSModelGroup mg = p.getTerm().asModelGroup();
/* 131 */     if (mg == null) return false;
/*     */     
/* 133 */     if (this.builder.getGlobalBinding().isChoiceContentPropertyModelGroupBinding()) {
/* 134 */       return false;
/*     */     }
/* 136 */     return (mg.getCompositor() == XSModelGroup.CHOICE);
/*     */   }
/*     */   
/*     */   private boolean needsToHaveChoiceContentProperty(XSModelGroupDecl decl) {
/* 140 */     return (decl.getModelGroup().getCompositor() == XSModelGroup.CHOICE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object annotation(XSAnnotation ann) {
/* 148 */     return this.base.annotation(ann);
/*     */   }
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/* 151 */     return this.base.attGroupDecl(decl);
/*     */   }
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/* 154 */     return this.base.attributeDecl(decl);
/*     */   }
/*     */   public Object attributeUse(XSAttributeUse use) {
/* 157 */     return this.base.attributeUse(use);
/*     */   }
/*     */   public Object facet(XSFacet facet) {
/* 160 */     return this.base.facet(facet);
/*     */   }
/*     */   public Object notation(XSNotation notation) {
/* 163 */     return this.base.notation(notation);
/*     */   }
/*     */   public Object schema(XSSchema schema) {
/* 166 */     return this.base.schema(schema);
/*     */   }
/*     */   public Object empty(XSContentType empty) {
/* 169 */     return this.base.empty(empty);
/*     */   }
/*     */   public Object particle(XSParticle particle) {
/* 172 */     return this.base.particle(particle);
/*     */   }
/*     */   public Object simpleType(XSSimpleType simpleType) {
/* 175 */     return this.base.simpleType(simpleType);
/*     */   }
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 178 */     return this.base.elementDecl(decl);
/*     */   }
/*     */   public Object wildcard(XSWildcard wc) {
/* 181 */     return this.base.wildcard(wc);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\ModelGroupBindingClassBinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */