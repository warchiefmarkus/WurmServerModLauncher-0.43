/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlAnyElementWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlAttributeWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlElementRefWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlElementRefsWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlElementWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlElementsWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlSchemaTypeWriter;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CElement;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlValue;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractField
/*     */   implements FieldOutline
/*     */ {
/*     */   protected final ClassOutlineImpl outline;
/*     */   protected final CPropertyInfo prop;
/*     */   protected final JCodeModel codeModel;
/*     */   protected final JType implType;
/*     */   protected final JType exposedType;
/*     */   private XmlElementsWriter xesw;
/*     */   
/*     */   protected AbstractField(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 325 */     this.xesw = null; this.outline = outline; this.prop = prop; this.codeModel = outline.parent().getCodeModel(); this.implType = getType(Aspect.IMPLEMENTATION); this.exposedType = getType(Aspect.EXPOSED);
/*     */   } public final ClassOutline parent() { return (ClassOutline)this.outline; } public final CPropertyInfo getPropertyInfo() { return this.prop; }
/*     */   protected void annotate(JAnnotatable field) { assert field != null; if (this.prop instanceof CAttributePropertyInfo) { annotateAttribute(field); } else if (this.prop instanceof CElementPropertyInfo) { annotateElement(field); } else if (this.prop instanceof com.sun.tools.xjc.model.CValuePropertyInfo) { field.annotate(XmlValue.class); } else if (this.prop instanceof CReferencePropertyInfo) { annotateReference(field); }  this.outline.parent().generateAdapterIfNecessary(this.prop, field); QName st = this.prop.getSchemaType(); if (st != null) ((XmlSchemaTypeWriter)field.annotate2(XmlSchemaTypeWriter.class)).name(st.getLocalPart()).namespace(st.getNamespaceURI());  if (this.prop.inlineBinaryData())
/*     */       field.annotate(XmlInlineBinaryData.class);  }
/* 329 */   private XmlElementWriter getXew(boolean checkWrapper, JAnnotatable field) { XmlElementWriter xew; if (checkWrapper) {
/* 330 */       if (this.xesw == null) {
/* 331 */         this.xesw = (XmlElementsWriter)field.annotate2(XmlElementsWriter.class);
/*     */       }
/* 333 */       xew = this.xesw.value();
/*     */     } else {
/* 335 */       xew = (XmlElementWriter)field.annotate2(XmlElementWriter.class);
/*     */     } 
/* 337 */     return xew; } private void annotateReference(JAnnotatable field) { CReferencePropertyInfo rp = (CReferencePropertyInfo)this.prop; TODO.prototype(); Collection<CElement> elements = rp.getElements(); if (elements.size() == 1) { XmlElementRefWriter refw = (XmlElementRefWriter)field.annotate2(XmlElementRefWriter.class); CElement e = elements.iterator().next(); refw.name(e.getElementName().getLocalPart()).namespace(e.getElementName().getNamespaceURI()).type(((NType)e.getType()).toType((Outline)this.outline.parent(), Aspect.IMPLEMENTATION)); }
/*     */     else if (elements.size() > 1) { XmlElementRefsWriter refsw = (XmlElementRefsWriter)field.annotate2(XmlElementRefsWriter.class); for (CElement e : elements) { XmlElementRefWriter refw = refsw.value(); refw.name(e.getElementName().getLocalPart()).namespace(e.getElementName().getNamespaceURI()).type(((NType)e.getType()).toType((Outline)this.outline.parent(), Aspect.IMPLEMENTATION)); }
/*     */        }
/*     */      if (rp.isMixed())
/*     */       field.annotate(XmlMixed.class);  NClass dh = rp.getDOMHandler(); if (dh != null) { XmlAnyElementWriter xaew = (XmlAnyElementWriter)field.annotate2(XmlAnyElementWriter.class); xaew.lax((rp.getWildcard()).allowTypedObject); JClass value = dh.toType((Outline)this.outline.parent(), Aspect.IMPLEMENTATION); if (!value.equals(this.codeModel.ref(W3CDomHandler.class)))
/*     */         xaew.value((JType)value);  }
/*     */      }
/* 344 */   private void annotateAttribute(JAnnotatable field) { CAttributePropertyInfo ap = (CAttributePropertyInfo)this.prop;
/* 345 */     QName attName = ap.getXmlName();
/*     */ 
/*     */ 
/*     */     
/* 349 */     XmlAttributeWriter xaw = (XmlAttributeWriter)field.annotate2(XmlAttributeWriter.class);
/*     */     
/* 351 */     String generatedName = attName.getLocalPart();
/* 352 */     String generatedNS = attName.getNamespaceURI();
/*     */ 
/*     */     
/* 355 */     if (!generatedName.equals(ap.getName(false))) {
/* 356 */       xaw.name(generatedName);
/*     */     }
/*     */ 
/*     */     
/* 360 */     if (!generatedNS.equals("")) {
/* 361 */       xaw.namespace(generatedNS);
/*     */     }
/*     */ 
/*     */     
/* 365 */     if (ap.isRequired())
/* 366 */       xaw.required(true);  } private void annotateElement(JAnnotatable field) { CElementPropertyInfo ep = (CElementPropertyInfo)this.prop; List<CTypeRef> types = ep.getTypes(); if (ep.isValueList())
/*     */       field.annotate(XmlList.class);  assert ep.getXmlName() == null; if (types.size() == 1) { CTypeRef t = types.get(0); writeXmlElementAnnotation(field, t, resolve(t, Aspect.IMPLEMENTATION), false); } else { for (CTypeRef t : types)
/*     */         writeXmlElementAnnotation(field, t, resolve(t, Aspect.IMPLEMENTATION), true);  this.xesw = null; }  } private void writeXmlElementAnnotation(JAnnotatable field, CTypeRef ctype, JType jtype, boolean checkWrapper) { String enclosingTypeNS; XmlElementWriter xew = null; XmlNsForm formDefault = parent()._package().getElementFormDefault(); String propName = this.prop.getName(false); if ((parent()).target.getTypeName() == null) { enclosingTypeNS = parent()._package().getMostUsedNamespaceURI(); } else { enclosingTypeNS = (parent()).target.getTypeName().getNamespaceURI(); }  String generatedName = ctype.getTagName().getLocalPart(); if (!generatedName.equals(propName)) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.name(generatedName); }  String generatedNS = ctype.getTagName().getNamespaceURI(); if ((formDefault == XmlNsForm.QUALIFIED && !generatedNS.equals(enclosingTypeNS)) || (formDefault == XmlNsForm.UNQUALIFIED && !generatedNS.equals(""))) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.namespace(generatedNS); }  CElementPropertyInfo ep = (CElementPropertyInfo)this.prop; if (ep.isRequired() && this.exposedType.isReference()) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.required(true); }
/*     */      if (ep.isRequired() && !this.prop.isCollection())
/*     */       jtype = jtype.unboxify();  if (!jtype.equals(this.exposedType) || ((parent().parent().getModel()).options.runtime14 && this.prop.isCollection())) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.type(jtype); }
/*     */      String defaultValue = ctype.getDefaultValue(); if (defaultValue != null) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.defaultValue(defaultValue); }
/*     */      if (ctype.isNillable()) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.nillable(true); }
/*     */      } protected abstract class Accessor implements FieldAccessor
/*     */   {
/* 381 */     protected Accessor(JExpression $target) { this.$target = $target; }
/*     */     
/*     */     protected final JExpression $target;
/*     */     public final FieldOutline owner() {
/* 385 */       return AbstractField.this;
/*     */     }
/*     */     
/*     */     public final CPropertyInfo getPropertyInfo() {
/* 389 */       return AbstractField.this.prop;
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
/*     */   protected final JFieldVar generateField(JType type) {
/* 404 */     return this.outline.implClass.field(2, type, this.prop.getName(false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JExpression castToImplType(JExpression exp) {
/* 411 */     if (this.implType == this.exposedType) {
/* 412 */       return exp;
/*     */     }
/* 414 */     return (JExpression)JExpr.cast(this.implType, exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JType getType(final Aspect aspect) {
/*     */     JType t;
/* 422 */     if (this.prop.getAdapter() != null)
/* 423 */       return ((NType)(this.prop.getAdapter()).customType).toType((Outline)this.outline.parent(), aspect); 
/*     */     final class TypeList
/*     */       extends ArrayList<JType> {
/*     */       void add(CTypeInfo t) {
/* 427 */         add(((NType)t.getType()).toType((Outline)AbstractField.this.outline.parent(), aspect));
/* 428 */         if (t instanceof CElementInfo)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 433 */           add(((CElementInfo)t).getSubstitutionMembers());
/*     */         }
/*     */       }
/*     */       
/*     */       void add(Collection<? extends CTypeInfo> col) {
/* 438 */         for (CTypeInfo typeInfo : col)
/* 439 */           add(typeInfo); 
/*     */       }
/*     */     };
/* 442 */     TypeList r = new TypeList();
/* 443 */     r.add(this.prop.ref());
/*     */ 
/*     */     
/* 446 */     if (this.prop.baseType != null) {
/* 447 */       t = this.prop.baseType;
/*     */     } else {
/* 449 */       t = TypeUtil.getCommonBaseType(this.codeModel, r);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     if (this.prop.isUnboxable())
/* 457 */       t = t.unboxify(); 
/* 458 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<Object> listPossibleTypes(CPropertyInfo prop) {
/* 465 */     List<Object> r = new ArrayList();
/* 466 */     for (CTypeInfo tt : prop.ref()) {
/* 467 */       JType t = ((NType)tt.getType()).toType((Outline)this.outline.parent(), Aspect.EXPOSED);
/* 468 */       if (t.isPrimitive() || t.isArray()) {
/* 469 */         r.add(t.fullName()); continue;
/*     */       } 
/* 471 */       r.add(t);
/* 472 */       r.add("\n");
/*     */     } 
/*     */ 
/*     */     
/* 476 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JType resolve(CTypeRef typeRef, Aspect a) {
/* 483 */     return this.outline.parent().resolve(typeRef, a);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\AbstractField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */