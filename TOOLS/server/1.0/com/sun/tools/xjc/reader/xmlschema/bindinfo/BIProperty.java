/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.field.IsSetFieldRenderer;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.Messages;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.util.XSFinder;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
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
/*     */ public final class BIProperty
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   private final String propName;
/*     */   private final String javadoc;
/*     */   private final JType baseType;
/*     */   public final BIConversion conv;
/*     */   private final FieldRendererFactory realization;
/*     */   private final Boolean needIsSetMethod;
/*     */   private Boolean isConstantProperty;
/*     */   private final XSFinder hasFixedValue;
/*     */   
/*     */   public BIProperty(Locator loc, String _propName, String _javadoc, JType _baseType, BIConversion _conv, FieldRendererFactory real, Boolean isConst, Boolean isSet) {
/*  83 */     super(loc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     this.hasFixedValue = (XSFinder)new Object(this); this.propName = _propName; this.javadoc = _javadoc; this.baseType = _baseType; this.realization = real;
/*     */     this.isConstantProperty = isConst;
/*     */     this.needIsSetMethod = isSet;
/*     */     this.conv = _conv;
/*     */   }
/*     */   public void setParent(BindInfo parent) { super.setParent(parent);
/*     */     if (this.conv != null)
/*     */       this.conv.setParent(parent);  }
/*     */   public String getPropertyName(boolean forConstant) { if (this.propName != null) {
/*     */       BIGlobalBinding gb = getBuilder().getGlobalBinding();
/*     */       if (gb.isJavaNamingConventionEnabled() && !forConstant)
/*     */         return gb.getNameConverter().toPropertyName(this.propName); 
/*     */       return this.propName;
/*     */     } 
/*     */     com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty next = getDefault();
/*     */     if (next != null)
/*     */       return next.getPropertyName(forConstant); 
/*     */     return null; }
/*     */   public String getJavadoc() { return this.javadoc; }
/*     */   public JType getBaseType() { if (this.baseType != null)
/*     */       return this.baseType; 
/*     */     com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty next = getDefault();
/*     */     if (next != null)
/*     */       return next.getBaseType(); 
/*     */     return null; } public FieldRendererFactory getRealization() { if (this.realization != null)
/*     */       return this.realization; 
/*     */     com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty next = getDefault();
/*     */     if (next != null)
/*     */       return next.getRealization(); 
/* 310 */     throw new JAXBAssertionError(); } protected com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty getDefault() { if (getOwner() == null) return null; 
/* 311 */     com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty next = getDefault(getBuilder(), getOwner());
/* 312 */     if (next == this) return null; 
/* 313 */     return next; }
/*     */   public boolean needIsSetMethod() { if (this.needIsSetMethod != null) return this.needIsSetMethod.booleanValue();  com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty next = getDefault(); if (next != null) return next.needIsSetMethod();  throw new JAXBAssertionError(); }
/*     */   public boolean isConstantProperty() { if (this.isConstantProperty != null) return this.isConstantProperty.booleanValue();  com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty next = getDefault(); if (next != null) return next.isConstantProperty();  throw new JAXBAssertionError(); }
/*     */   public FieldItem createFieldItem(String defaultName, boolean forConstant, Expression body, XSComponent source) { markAsAcknowledged(); constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null) name = defaultName;  FieldItem fi = new FieldItem(name, body, getBaseType(), source.getLocator()); fi.javadoc = concat(this.javadoc, getBuilder().getBindInfo(source).getDocumentation()); fi.realization = getRealization(); _assert((fi.realization != null)); if (needIsSetMethod()) fi.realization = IsSetFieldRenderer.createFactory(fi.realization);  return fi; }
/* 317 */   public void markAsAcknowledged() { if (isAcknowledged()) return;  super.markAsAcknowledged(); com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty def = getDefault(); if (def != null) def.markAsAcknowledged();  } private void constantPropertyErrorCheck() { if (this.isConstantProperty != null && getOwner() != null) if (!this.hasFixedValue.find(getOwner())) { (getBuilder()).errorReceiver.error(getLocation(), Messages.format("BIProperty.IllegalFixedAttributeAsConstantProperty")); this.isConstantProperty = null; }   } private static com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty getDefault(BGMBuilder builder, XSComponent c) { while (c != null) {
/* 318 */       c = (XSComponent)c.apply(defaultCustomizationFinder);
/* 319 */       if (c != null) {
/* 320 */         com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty prop = (com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty)builder.getBindInfo(c).get(NAME);
/* 321 */         if (prop != null) return prop;
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 326 */     return builder.getGlobalBinding().getDefaultProperty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty getCustomization(BGMBuilder builder, XSComponent c) {
/* 362 */     if (c != null) {
/* 363 */       com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty prop = (com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty)builder.getBindInfo(c).get(NAME);
/* 364 */       if (prop != null) return prop;
/*     */     
/*     */     } 
/*     */     
/* 368 */     return getDefault(builder, c);
/*     */   }
/*     */   
/* 371 */   private static final XSFunction defaultCustomizationFinder = (XSFunction)new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String concat(String s1, String s2) {
/* 402 */     if (s1 == null) return s2; 
/* 403 */     if (s2 == null) return s1; 
/* 404 */     return s1 + "\n\n" + s2;
/*     */   }
/*     */   public QName getName() {
/* 407 */     return NAME;
/*     */   }
/*     */   
/* 410 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "property");
/*     */   private static final String ERR_ILLEGAL_FIXEDATTR = "BIProperty.IllegalFixedAttributeAsConstantProperty";
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIProperty.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */