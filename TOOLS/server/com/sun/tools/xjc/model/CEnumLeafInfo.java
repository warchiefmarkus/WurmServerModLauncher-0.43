/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import java.util.Collection;
/*     */ import javax.activation.MimeType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CEnumLeafInfo
/*     */   implements EnumLeafInfo<NType, NClass>, NClass, CNonElement
/*     */ {
/*     */   public final Model model;
/*     */   public final CClassInfoParent parent;
/*     */   public final String shortName;
/*     */   private final QName typeName;
/*     */   private final XSComponent source;
/*     */   public final CNonElement base;
/*     */   public final Collection<CEnumConstant> members;
/*     */   private final CCustomizations customizations;
/*     */   private final Locator sourceLocator;
/*     */   public String javadoc;
/*     */   
/*     */   public CEnumLeafInfo(Model model, QName typeName, CClassInfoParent container, String shortName, CNonElement base, Collection<CEnumConstant> _members, XSComponent source, CCustomizations customizations, Locator _sourceLocator) {
/* 129 */     this.model = model;
/* 130 */     this.parent = container;
/* 131 */     this.shortName = model.allocator.assignClassName(this.parent, shortName);
/* 132 */     this.base = base;
/* 133 */     this.members = _members;
/* 134 */     this.source = source;
/* 135 */     if (customizations == null)
/* 136 */       customizations = CCustomizations.EMPTY; 
/* 137 */     this.customizations = customizations;
/* 138 */     this.sourceLocator = _sourceLocator;
/* 139 */     this.typeName = typeName;
/*     */     
/* 141 */     for (CEnumConstant mem : this.members) {
/* 142 */       mem.setParent(this);
/*     */     }
/* 144 */     model.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locator getLocator() {
/* 155 */     return this.sourceLocator;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/* 159 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public NType getType() {
/* 163 */     return (NType)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeReferencedByIDREF() {
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isElement() {
/* 175 */     return false;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   public Element<NType, NClass> asElement() {
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   public NClass getClazz() {
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public XSComponent getSchemaComponent() {
/* 191 */     return this.source;
/*     */   }
/*     */   
/*     */   public JClass toType(Outline o, Aspect aspect) {
/* 195 */     return (JClass)(o.getEnum(this)).clazz;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 199 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isBoxedType() {
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public String fullName() {
/* 207 */     return this.parent.fullName() + '.' + this.shortName;
/*     */   }
/*     */   
/*     */   public boolean isPrimitive() {
/* 211 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 215 */     return true;
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
/*     */   public boolean needsValueField() {
/* 227 */     for (CEnumConstant cec : this.members) {
/* 228 */       if (!cec.getName().equals(cec.getLexicalValue()))
/* 229 */         return true; 
/*     */     } 
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression createConstant(Outline outline, XmlString literal) {
/* 237 */     JClass type = toType(outline, Aspect.EXPOSED);
/* 238 */     for (CEnumConstant mem : this.members) {
/* 239 */       if (mem.getLexicalValue().equals(literal.value))
/* 240 */         return (JExpression)type.staticRef(mem.getName()); 
/*     */     } 
/* 242 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isCollection() {
/* 247 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public CAdapter getAdapterUse() {
/* 252 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/* 257 */     return this;
/*     */   }
/*     */   
/*     */   public ID idUse() {
/* 261 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 265 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<CEnumConstant> getConstants() {
/* 269 */     return this.members;
/*     */   }
/*     */   
/*     */   public NonElement<NType, NClass> getBaseType() {
/* 273 */     return this.base;
/*     */   }
/*     */   
/*     */   public CCustomizations getCustomizations() {
/* 277 */     return this.customizations;
/*     */   }
/*     */   
/*     */   public Locatable getUpstream() {
/* 281 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 285 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CEnumLeafInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */