/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.bean.field.IsSetFieldRenderer;
/*     */ import com.sun.tools.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CCustomizations;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
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
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.util.XSFinder;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElementRef;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.namespace.QName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRootElement(name = "property")
/*     */ public final class BIProperty
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlAttribute
/* 117 */   private String name = null;
/*     */ 
/*     */   
/*     */   @XmlElement
/* 121 */   private String javadoc = null;
/*     */ 
/*     */   
/*     */   @XmlElement
/* 125 */   private BaseTypeBean baseType = null; @XmlAttribute
/*     */   private boolean generateFailFastSetterMethod = false; @XmlAttribute
/*     */   private CollectionTypeAttribute collectionType;
/*     */   @XmlAttribute
/*     */   private OptionalPropertyMode optionalProperty;
/*     */   @XmlAttribute
/*     */   private Boolean generateElementProperty;
/*     */   @XmlAttribute(name = "fixedAttributeAsConstantProperty")
/*     */   private Boolean isConstantProperty;
/*     */   private final XSFinder hasFixedValue;
/*     */   
/* 136 */   public BIProperty(Locator loc, String _propName, String _javadoc, BaseTypeBean _baseType, CollectionTypeAttribute collectionType, Boolean isConst, OptionalPropertyMode optionalProperty, Boolean genElemProp) { super(loc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     this.collectionType = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     this.optionalProperty = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     this.generateElementProperty = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 573 */     this.hasFixedValue = new XSFinder() {
/*     */         public Boolean attributeDecl(XSAttributeDecl decl) {
/* 575 */           return Boolean.valueOf((decl.getFixedValue() != null));
/*     */         }
/*     */         
/*     */         public Boolean attributeUse(XSAttributeUse use) {
/* 579 */           return Boolean.valueOf((use.getFixedValue() != null));
/*     */         }
/*     */ 
/*     */         
/*     */         public Boolean schema(XSSchema s)
/*     */         {
/* 585 */           return Boolean.valueOf(true); } }; this.name = _propName; this.javadoc = _javadoc; this.baseType = _baseType; this.collectionType = collectionType; this.isConstantProperty = isConst; this.optionalProperty = optionalProperty; this.generateElementProperty = genElemProp; } public Collection<BIDeclaration> getChildren() { BIConversion conv = getConv(); if (conv == null) return super.getChildren();  return Collections.singleton(conv); } public void setParent(BindInfo parent) { super.setParent(parent); if (this.baseType != null && this.baseType.conv != null) this.baseType.conv.setParent(parent);  } public String getPropertyName(boolean forConstant) { if (this.name != null) { BIGlobalBinding gb = getBuilder().getGlobalBinding(); NameConverter nc = (getBuilder()).model.getNameConverter(); if (gb.isJavaNamingConventionEnabled() && !forConstant) return nc.toPropertyName(this.name);  return this.name; }  BIProperty next = getDefault(); if (next != null) return next.getPropertyName(forConstant);  return null; } public String getJavadoc() { return this.javadoc; } public JType getBaseType() { if (this.baseType != null && this.baseType.name != null) return TypeUtil.getType(getCodeModel(), this.baseType.name, (ErrorReceiver)Ring.get(ErrorReceiver.class), getLocation());  BIProperty next = getDefault(); if (next != null) return next.getBaseType();  return null; } protected BIProperty() { this.collectionType = null; this.optionalProperty = null; this.generateElementProperty = null; this.hasFixedValue = new XSFinder() { public Boolean attributeDecl(XSAttributeDecl decl) { return Boolean.valueOf((decl.getFixedValue() != null)); } public Boolean attributeUse(XSAttributeUse use) { return Boolean.valueOf((use.getFixedValue() != null)); } public Boolean schema(XSSchema s) { return Boolean.valueOf(true); } }; }
/*     */   CollectionTypeAttribute getCollectionType() { if (this.collectionType != null) return this.collectionType;  return getDefault().getCollectionType(); }
/*     */   @XmlAttribute void setGenerateIsSetMethod(boolean b) { this.optionalProperty = b ? OptionalPropertyMode.ISSET : OptionalPropertyMode.WRAPPER; }
/*     */   public OptionalPropertyMode getOptionalPropertyMode() { if (this.optionalProperty != null) return this.optionalProperty;  return getDefault().getOptionalPropertyMode(); }
/*     */   private Boolean generateElementProperty() { if (this.generateElementProperty != null) return this.generateElementProperty;  BIProperty next = getDefault(); if (next != null) return next.generateElementProperty();  return null; }
/*     */   public boolean isConstantProperty() { if (this.isConstantProperty != null) return this.isConstantProperty.booleanValue();  BIProperty next = getDefault(); if (next != null)
/*     */       return next.isConstantProperty();  throw new AssertionError(); }
/*     */   public CValuePropertyInfo createValueProperty(String defaultName, boolean forConstant, XSComponent source, TypeUse tu, QName typeName) { markAsAcknowledged(); constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null) { name = defaultName; if (tu.isCollection() && getBuilder().getGlobalBinding().isSimpleMode())
/*     */         name = JJavaName.getPluralForm(name);  }  CValuePropertyInfo prop = wrapUp(new CValuePropertyInfo(name, source, getCustomizations(source), source.getLocator(), tu, typeName), source); BIInlineBinaryData.handle(source, (CPropertyInfo)prop); return prop; }
/*     */   public CAttributePropertyInfo createAttributeProperty(XSAttributeUse use, TypeUse tu) { boolean forConstant = (getCustomization((XSComponent)use).isConstantProperty() && use.getFixedValue() != null); String name = getPropertyName(forConstant); if (name == null) { NameConverter conv = getBuilder().getNameConverter(); if (forConstant) { name = conv.toConstantName(use.getDecl().getName()); } else { name = conv.toPropertyName(use.getDecl().getName()); }  if (tu.isCollection() && getBuilder().getGlobalBinding().isSimpleMode())
/*     */         name = JJavaName.getPluralForm(name);  }  markAsAcknowledged(); constantPropertyErrorCheck(); return wrapUp(new CAttributePropertyInfo(name, (XSComponent)use, getCustomizations(use), use.getLocator(), BGMBuilder.getName((XSDeclaration)use.getDecl()), tu, BGMBuilder.getName((XSDeclaration)use.getDecl().getType()), use.isRequired()), (XSComponent)use); }
/*     */   public CElementPropertyInfo createElementProperty(String defaultName, boolean forConstant, XSParticle source, RawTypeSet types) { if (!types.refs.isEmpty())
/*     */       markAsAcknowledged();  constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null)
/* 598 */       name = defaultName;  CElementPropertyInfo prop = wrapUp(new CElementPropertyInfo(name, types.getCollectionMode(), types.id(), types.getExpectedMimeType(), (XSComponent)source, getCustomizations(source), source.getLocator(), types.isRequired()), (XSComponent)source); types.addTo(prop); BIInlineBinaryData.handle((XSComponent)source.getTerm(), (CPropertyInfo)prop); return prop; } protected BIProperty getDefault() { if (getOwner() == null) return null; 
/* 599 */     BIProperty next = getDefault(getBuilder(), getOwner());
/* 600 */     if (next == this) return null; 
/* 601 */     return next; }
/*     */   public CReferencePropertyInfo createReferenceProperty(String defaultName, boolean forConstant, XSComponent source, RawTypeSet types, boolean isMixed) { if (!types.refs.isEmpty()) markAsAcknowledged();  constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null) name = defaultName;  CReferencePropertyInfo prop = wrapUp(new CReferencePropertyInfo(name, (types.getCollectionMode().isRepeated() || isMixed), isMixed, source, getCustomizations(source), source.getLocator()), source); types.addTo(prop); BIInlineBinaryData.handle(source, (CPropertyInfo)prop); return prop; }
/*     */   public CPropertyInfo createElementOrReferenceProperty(String defaultName, boolean forConstant, XSParticle source, RawTypeSet types) { boolean generateRef; Boolean b; switch (types.canBeTypeRefs) { case PRIMITIVE: case WRAPPER: b = generateElementProperty(); if (b == null) { boolean bool = (types.canBeTypeRefs == RawTypeSet.Mode.CAN_BE_TYPEREF); break; }  generateRef = b.booleanValue(); break;case ISSET: generateRef = true; break;default: throw new AssertionError(); }  if (generateRef) return (CPropertyInfo)createReferenceProperty(defaultName, forConstant, (XSComponent)source, types, false);  return (CPropertyInfo)createElementProperty(defaultName, forConstant, source, types); }
/*     */   private <T extends CPropertyInfo> T wrapUp(T prop, XSComponent source) { FieldRenderer fieldRenderer; IsSetFieldRenderer isSetFieldRenderer; ((CPropertyInfo)prop).javadoc = concat(this.javadoc, getBuilder().getBindInfo(source).getDocumentation()); if (((CPropertyInfo)prop).javadoc == null) ((CPropertyInfo)prop).javadoc = "";  OptionalPropertyMode opm = getOptionalPropertyMode(); if (prop.isCollection()) { CollectionTypeAttribute ct = getCollectionType(); fieldRenderer = ct.get((getBuilder()).model); } else { FieldRendererFactory frf = (getBuilder()).fieldRendererFactory; if (prop.isOptionalPrimitive()) { switch (opm) { case PRIMITIVE: fieldRenderer = frf.getRequiredUnboxed(); break;case WRAPPER: fieldRenderer = frf.getSingle(); break;case ISSET: fieldRenderer = frf.getSinglePrimitiveAccess(); break;default: throw new Error(); }  } else { fieldRenderer = frf.getDefault(); }  }  if (opm == OptionalPropertyMode.ISSET) isSetFieldRenderer = new IsSetFieldRenderer(fieldRenderer, (prop.isOptionalPrimitive() || prop.isCollection()), true);  ((CPropertyInfo)prop).realization = (FieldRenderer)isSetFieldRenderer; JType bt = getBaseType(); if (bt != null) ((CPropertyInfo)prop).baseType = bt;  return prop; }
/* 605 */   private CCustomizations getCustomizations(XSComponent src) { return getBuilder().getBindInfo(src).toCustomizationList(); } private CCustomizations getCustomizations(XSComponent... src) { CCustomizations c = null; for (XSComponent s : src) { CCustomizations r = getCustomizations(s); if (c == null) { c = r; } else { c = CCustomizations.merge(c, r); }  }  return c; } private CCustomizations getCustomizations(XSAttributeUse src) { if (src.getDecl().isLocal()) return getCustomizations(new XSComponent[] { (XSComponent)src, (XSComponent)src.getDecl() });  return getCustomizations((XSComponent)src); } private CCustomizations getCustomizations(XSParticle src) { if (src.getTerm().isElementDecl()) { XSElementDecl xed = src.getTerm().asElementDecl(); if (xed.isGlobal()) return getCustomizations((XSComponent)src);  }  return getCustomizations(new XSComponent[] { (XSComponent)src, (XSComponent)src.getTerm() }); } public void markAsAcknowledged() { if (isAcknowledged()) return;  super.markAsAcknowledged(); BIProperty def = getDefault(); if (def != null) def.markAsAcknowledged();  } private void constantPropertyErrorCheck() { if (this.isConstantProperty != null && getOwner() != null) if (!this.hasFixedValue.find(getOwner())) { ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(getLocation(), Messages.ERR_ILLEGAL_FIXEDATTR.format(new Object[0])); this.isConstantProperty = null; }   } private static BIProperty getDefault(BGMBuilder builder, XSComponent c) { while (c != null) {
/* 606 */       c = (XSComponent)c.apply(defaultCustomizationFinder);
/* 607 */       if (c != null) {
/* 608 */         BIProperty prop = builder.getBindInfo(c).<BIProperty>get(BIProperty.class);
/* 609 */         if (prop != null) return prop;
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 614 */     return builder.getGlobalBinding().getDefaultProperty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BIProperty getCustomization(XSComponent c) {
/* 648 */     BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */     
/* 651 */     if (c != null) {
/* 652 */       BIProperty prop = builder.getBindInfo(c).<BIProperty>get(BIProperty.class);
/* 653 */       if (prop != null) return prop;
/*     */     
/*     */     } 
/*     */     
/* 657 */     return getDefault(builder, c);
/*     */   }
/*     */   
/* 660 */   private static final XSFunction<XSComponent> defaultCustomizationFinder = new XSFunction<XSComponent>()
/*     */     {
/*     */       public XSComponent attributeUse(XSAttributeUse use) {
/* 663 */         return (XSComponent)use.getDecl();
/*     */       }
/*     */       
/*     */       public XSComponent particle(XSParticle particle) {
/* 667 */         return (XSComponent)particle.getTerm();
/*     */       }
/*     */ 
/*     */       
/*     */       public XSComponent schema(XSSchema schema) {
/* 672 */         return null;
/*     */       }
/*     */       
/*     */       public XSComponent attributeDecl(XSAttributeDecl decl) {
/* 676 */         return (XSComponent)decl.getOwnerSchema();
/* 677 */       } public XSComponent wildcard(XSWildcard wc) { return (XSComponent)wc.getOwnerSchema(); }
/* 678 */       public XSComponent modelGroupDecl(XSModelGroupDecl decl) { return (XSComponent)decl.getOwnerSchema(); }
/* 679 */       public XSComponent modelGroup(XSModelGroup group) { return (XSComponent)group.getOwnerSchema(); }
/* 680 */       public XSComponent elementDecl(XSElementDecl decl) { return (XSComponent)decl.getOwnerSchema(); }
/* 681 */       public XSComponent complexType(XSComplexType type) { return (XSComponent)type.getOwnerSchema(); } public XSComponent simpleType(XSSimpleType st) {
/* 682 */         return (XSComponent)st.getOwnerSchema();
/*     */       }
/*     */       
/* 685 */       public XSComponent attGroupDecl(XSAttGroupDecl decl) { throw new IllegalStateException(); }
/* 686 */       public XSComponent empty(XSContentType empty) { throw new IllegalStateException(); }
/* 687 */       public XSComponent annotation(XSAnnotation xsAnnotation) { throw new IllegalStateException(); }
/* 688 */       public XSComponent facet(XSFacet xsFacet) { throw new IllegalStateException(); }
/* 689 */       public XSComponent notation(XSNotation xsNotation) { throw new IllegalStateException(); }
/* 690 */       public XSComponent identityConstraint(XSIdentityConstraint x) { throw new IllegalStateException(); } public XSComponent xpath(XSXPath xsxPath) {
/* 691 */         throw new IllegalStateException();
/*     */       }
/*     */     };
/*     */   
/*     */   private static String concat(String s1, String s2) {
/* 696 */     if (s1 == null) return s2; 
/* 697 */     if (s2 == null) return s1; 
/* 698 */     return s1 + "\n\n" + s2;
/*     */   }
/*     */   public QName getName() {
/* 701 */     return NAME;
/*     */   }
/*     */   
/* 704 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "property");
/*     */ 
/*     */   
/*     */   public BIConversion getConv() {
/* 708 */     if (this.baseType != null) {
/* 709 */       return this.baseType.conv;
/*     */     }
/* 711 */     return null;
/*     */   }
/*     */   
/*     */   private static final class BaseTypeBean {
/*     */     @XmlElementRef
/*     */     BIConversion conv;
/*     */     @XmlAttribute
/*     */     String name;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */