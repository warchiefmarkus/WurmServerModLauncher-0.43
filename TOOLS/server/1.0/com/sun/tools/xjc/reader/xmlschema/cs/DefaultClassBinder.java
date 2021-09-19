/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*     */ 
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.NameGenerator;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.AbstractBinderImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassSelector;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.JClassFactoryImpl;
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
/*     */ import javax.xml.bind.Element;
/*     */ 
/*     */ 
/*     */ class DefaultClassBinder
/*     */   extends AbstractBinderImpl
/*     */ {
/*     */   DefaultClassBinder(ClassSelector classSelector) {
/*  42 */     super(classSelector);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  47 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  51 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public Object modelGroup(XSModelGroup mgroup) {
/*     */     String defaultName;
/*     */     try {
/*  57 */       defaultName = NameGenerator.getName(this.owner.builder, mgroup);
/*  58 */     } catch (ParseException e) {
/*  59 */       defaultName = null;
/*     */     } 
/*  61 */     return allow((XSComponent)mgroup, defaultName);
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/*  65 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/*  69 */     ClassItem ci = allow((XSComponent)type, type.getName());
/*  70 */     if (ci != null) return ci;
/*     */ 
/*     */     
/*  73 */     if (type.isGlobal()) {
/*     */ 
/*     */       
/*  76 */       JPackage pkg = this.owner.getPackage(type.getTargetNamespace());
/*     */       
/*  78 */       JDefinedClass clazz = this.owner.codeModelClassFactory.createInterface((JClassContainer)pkg, deriveName((XSDeclaration)type), type.getLocator());
/*     */ 
/*     */       
/*  81 */       return wrapByClassItem((XSComponent)type, clazz);
/*     */     } 
/*     */     
/*  84 */     String className = this.builder.getNameConverter().toClassName(type.getScope().getName());
/*     */     
/*  86 */     BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)type.getOwnerSchema()).get(BISchemaBinding.NAME);
/*     */ 
/*     */     
/*  89 */     if (sb != null) { className = sb.mangleAnonymousTypeClassName(className); }
/*  90 */     else { className = className + "Type"; }
/*     */     
/*  92 */     return wrapByClassItem((XSComponent)type, getClassFactory((XSComponent)type).create(className, type.getLocator()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/*  98 */     ClassItem r = allow((XSComponent)decl, decl.getName());
/*     */     
/* 100 */     if (r == null && 
/* 101 */       decl.isGlobal()) {
/* 102 */       r = wrapByClassItem((XSComponent)decl, this.owner.codeModelClassFactory.createInterface((JClassContainer)this.owner.getPackage(decl.getTargetNamespace()), deriveName((XSDeclaration)decl), decl.getLocator()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (r != null)
/*     */     {
/*     */       
/* 112 */       r.getTypeAsDefined()._implements(Element.class);
/*     */     }
/*     */     
/* 115 */     return r;
/*     */   }
/*     */   public Object empty(XSContentType ct) {
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/* 122 */     return never((XSComponent)use);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object simpleType(XSSimpleType type) {
/* 128 */     this.builder.simpleTypeBuilder.refererStack.push(type);
/*     */     
/* 130 */     this.builder.simpleTypeBuilder.build(type);
/*     */     
/* 132 */     this.builder.simpleTypeBuilder.refererStack.pop();
/*     */     
/* 134 */     return never((XSComponent)type);
/*     */   }
/*     */   
/*     */   public Object particle(XSParticle particle) {
/* 138 */     return never((XSComponent)particle);
/*     */   }
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 142 */     return never((XSComponent)wc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object annotation(XSAnnotation annon) {
/* 148 */     _assert(false);
/* 149 */     return null;
/*     */   }
/*     */   
/*     */   public Object notation(XSNotation not) {
/* 153 */     _assert(false);
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   public Object facet(XSFacet decl) {
/* 158 */     _assert(false);
/* 159 */     return null;
/*     */   }
/*     */   public Object schema(XSSchema schema) {
/* 162 */     _assert(false);
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JClassFactory getClassFactory(XSComponent component) {
/* 173 */     JClassFactory cf = this.owner.getClassFactory();
/*     */     
/* 175 */     if (component instanceof XSComplexType) {
/* 176 */       XSComplexType xsct = (XSComplexType)component;
/* 177 */       if (xsct.isLocal()) {
/* 178 */         TypeItem parent = this.owner.bindToType(xsct.getScope());
/* 179 */         if (parent instanceof ClassItem)
/*     */         {
/*     */           
/* 182 */           return (JClassFactory)new JClassFactoryImpl(this.owner, ((ClassItem)parent).getTypeAsDefined().parentContainer());
/*     */         }
/*     */       } 
/*     */     } 
/* 186 */     return cf;
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
/*     */   private ClassItem never(XSComponent component) {
/* 217 */     return null;
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
/*     */   private ClassItem allow(XSComponent component, String defaultBaseName) {
/* 233 */     BIClass decl = (BIClass)this.builder.getBindInfo(component).get(BIClass.NAME);
/* 234 */     if (decl == null) return null;
/*     */     
/* 236 */     decl.markAsAcknowledged();
/*     */ 
/*     */ 
/*     */     
/* 240 */     String clsName = decl.getClassName();
/* 241 */     if (clsName == null) {
/*     */ 
/*     */       
/* 244 */       if (defaultBaseName == null) {
/* 245 */         this.builder.errorReceiver.error(decl.getLocation(), Messages.format("ClassSelector.ClassNameIsRequired"));
/*     */ 
/*     */ 
/*     */         
/* 249 */         defaultBaseName = "undefined" + component.hashCode();
/*     */       } 
/* 251 */       clsName = deriveName(defaultBaseName, component);
/*     */     }
/* 253 */     else if (!JJavaName.isJavaIdentifier(clsName)) {
/*     */       
/* 255 */       this.builder.errorReceiver.error(decl.getLocation(), Messages.format("ClassSelector.IncorrectClassName", clsName));
/*     */ 
/*     */       
/* 258 */       clsName = "Undefined" + component.hashCode();
/*     */     } 
/*     */ 
/*     */     
/* 262 */     JDefinedClass r = getClassFactory(component).create(clsName, decl.getLocation());
/*     */ 
/*     */     
/* 265 */     if (decl.getJavadoc() != null) {
/* 266 */       r.javadoc().appendComment(decl.getJavadoc() + "\n\n");
/*     */     }
/*     */ 
/*     */     
/* 270 */     ClassItem ci = wrapByClassItem(component, r);
/*     */ 
/*     */     
/* 273 */     String implClass = decl.getUserSpecifiedImplClass();
/* 274 */     if (implClass != null) {
/* 275 */       ci.setUserSpecifiedImplClass(implClass);
/*     */     }
/* 277 */     return ci;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\DefaultClassBinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */