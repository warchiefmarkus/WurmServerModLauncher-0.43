/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIFactoryMethod;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIInlineBinaryData;
/*     */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlElement;
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
/*     */ public final class CElementInfo
/*     */   extends AbstractCElement
/*     */   implements ElementInfo<NType, NClass>, NType, CClassInfoParent
/*     */ {
/*     */   private final QName tagName;
/*     */   private NType type;
/*     */   private String className;
/*     */   public final CClassInfoParent parent;
/*     */   private CElementInfo substitutionHead;
/*     */   private Set<CElementInfo> substitutionMembers;
/*     */   private final Model model;
/*     */   private CElementPropertyInfo property;
/*     */   @Nullable
/*     */   private String squeezedName;
/*     */   
/*     */   public CElementInfo(Model model, QName tagName, CClassInfoParent parent, TypeUse contentType, XmlString defaultValue, XSElementDecl source, CCustomizations customizations, Locator location) {
/* 131 */     super(model, (XSComponent)source, location, customizations);
/* 132 */     this.tagName = tagName;
/* 133 */     this.model = model;
/* 134 */     this.parent = parent;
/* 135 */     if (contentType != null) {
/* 136 */       initContentType(contentType, source, defaultValue);
/*     */     }
/* 138 */     model.add(this);
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
/*     */   public CElementInfo(Model model, QName tagName, CClassInfoParent parent, String className, CCustomizations customizations, Locator location) {
/* 154 */     this(model, tagName, parent, null, null, null, customizations, location);
/* 155 */     this.className = className;
/*     */   }
/*     */   
/*     */   public void initContentType(TypeUse contentType, @Nullable XSElementDecl source, XmlString defaultValue) {
/* 159 */     assert this.property == null;
/*     */     
/* 161 */     this.property = new CElementPropertyInfo("Value", contentType.isCollection() ? CElementPropertyInfo.CollectionMode.REPEATED_VALUE : CElementPropertyInfo.CollectionMode.NOT_REPEATED, contentType.idUse(), contentType.getExpectedMimeType(), (XSComponent)source, null, getLocator(), true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     this.property.setAdapter(contentType.getAdapterUse());
/* 167 */     BIInlineBinaryData.handle((XSComponent)source, this.property);
/* 168 */     this.property.getTypes().add(new CTypeRef(contentType.getInfo(), this.tagName, CTypeRef.getSimpleTypeName(source), true, defaultValue));
/* 169 */     this.type = NavigatorImpl.createParameterizedType(NavigatorImpl.theInstance.ref(JAXBElement.class), new NType[] { getContentInMemoryType() });
/*     */ 
/*     */ 
/*     */     
/* 173 */     BIFactoryMethod factoryMethod = (BIFactoryMethod)((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo((XSComponent)source).get(BIFactoryMethod.class);
/* 174 */     if (factoryMethod != null) {
/* 175 */       factoryMethod.markAsAcknowledged();
/* 176 */       this.squeezedName = factoryMethod.name;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDefaultValue() {
/* 182 */     return ((CTypeRef)getProperty().getTypes().get(0)).getDefaultValue();
/*     */   }
/*     */   
/*     */   public final JPackage _package() {
/* 186 */     return this.parent.getOwnerPackage();
/*     */   }
/*     */   
/*     */   public CNonElement getContentType() {
/* 190 */     return getProperty().ref().get(0);
/*     */   }
/*     */   
/*     */   public NType getContentInMemoryType() {
/* 194 */     if (getProperty().getAdapter() == null) {
/* 195 */       NType itemType = (NType)getContentType().getType();
/* 196 */       if (!this.property.isCollection()) {
/* 197 */         return itemType;
/*     */       }
/* 199 */       return NavigatorImpl.createParameterizedType(List.class, new NType[] { itemType });
/*     */     } 
/* 201 */     return (NType)(getProperty().getAdapter()).customType;
/*     */   }
/*     */ 
/*     */   
/*     */   public CElementPropertyInfo getProperty() {
/* 206 */     return this.property;
/*     */   }
/*     */   
/*     */   public CClassInfo getScope() {
/* 210 */     if (this.parent instanceof CClassInfo)
/* 211 */       return (CClassInfo)this.parent; 
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NType getType() {
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/* 223 */     return this.tagName;
/*     */   }
/*     */   
/*     */   public JType toType(Outline o, Aspect aspect) {
/* 227 */     if (this.className == null) {
/* 228 */       return this.type.toType(o, aspect);
/*     */     }
/* 230 */     return (JType)(o.getElement(this)).implClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement
/*     */   public String getSqueezedName() {
/* 240 */     if (this.squeezedName != null) return this.squeezedName;
/*     */     
/* 242 */     StringBuilder b = new StringBuilder();
/* 243 */     CClassInfo s = getScope();
/* 244 */     if (s != null)
/* 245 */       b.append(s.getSqueezedName()); 
/* 246 */     if (this.className != null) {
/* 247 */       b.append(this.className);
/*     */     } else {
/* 249 */       b.append(this.model.getNameConverter().toClassName(this.tagName.getLocalPart()));
/* 250 */     }  return b.toString();
/*     */   }
/*     */   
/*     */   public CElementInfo getSubstitutionHead() {
/* 254 */     return this.substitutionHead;
/*     */   }
/*     */   
/*     */   public Collection<CElementInfo> getSubstitutionMembers() {
/* 258 */     if (this.substitutionMembers == null) {
/* 259 */       return Collections.emptyList();
/*     */     }
/* 261 */     return this.substitutionMembers;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSubstitutionHead(CElementInfo substitutionHead) {
/* 266 */     assert this.substitutionHead == null;
/* 267 */     assert substitutionHead != null;
/* 268 */     this.substitutionHead = substitutionHead;
/*     */     
/* 270 */     if (substitutionHead.substitutionMembers == null)
/* 271 */       substitutionHead.substitutionMembers = new HashSet<CElementInfo>(); 
/* 272 */     substitutionHead.substitutionMembers.add(this);
/*     */   }
/*     */   
/*     */   public boolean isBoxedType() {
/* 276 */     return false;
/*     */   }
/*     */   
/*     */   public String fullName() {
/* 280 */     if (this.className == null) {
/* 281 */       return this.type.fullName();
/*     */     }
/* 283 */     String r = this.parent.fullName();
/* 284 */     if (r.length() == 0) return this.className; 
/* 285 */     return r + '.' + this.className;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T accept(CClassInfoParent.Visitor<T> visitor) {
/* 290 */     return visitor.onElement(this);
/*     */   }
/*     */   
/*     */   public JPackage getOwnerPackage() {
/* 294 */     return this.parent.getOwnerPackage();
/*     */   }
/*     */   
/*     */   public String shortName() {
/* 298 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasClass() {
/* 306 */     return (this.className != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CElementInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */