/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
/*     */ public abstract class CPropertyInfo
/*     */   implements PropertyInfo<NType, NClass>, CCustomizable
/*     */ {
/*     */   @XmlTransient
/*     */   private CClassInfo parent;
/*     */   private String privateName;
/*     */   private String publicName;
/*     */   private final boolean isCollection;
/*     */   @XmlTransient
/*     */   public final Locator locator;
/*     */   private final XSComponent source;
/*     */   public JType baseType;
/*  94 */   public String javadoc = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inlineBinaryData;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
/*     */   public FieldRenderer realization;
/*     */ 
/*     */ 
/*     */   
/*     */   public CDefaultValue defaultValue;
/*     */ 
/*     */ 
/*     */   
/*     */   private final CCustomizations customizations;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CPropertyInfo(String name, boolean collection, XSComponent source, CCustomizations customizations, Locator locator) {
/* 119 */     this.publicName = name;
/* 120 */     String n = NameConverter.standard.toVariableName(name);
/* 121 */     if (!JJavaName.isJavaIdentifier(n))
/* 122 */       n = '_' + n; 
/* 123 */     this.privateName = n;
/*     */     
/* 125 */     this.isCollection = collection;
/* 126 */     this.locator = locator;
/* 127 */     if (customizations == null) {
/* 128 */       this.customizations = CCustomizations.EMPTY;
/*     */     } else {
/* 130 */       this.customizations = customizations;
/* 131 */     }  this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   final void setParent(CClassInfo parent) {
/* 136 */     assert this.parent == null;
/* 137 */     assert parent != null;
/* 138 */     this.parent = parent;
/* 139 */     this.customizations.setParent(parent.model, this);
/*     */   }
/*     */   
/*     */   public CTypeInfo parent() {
/* 143 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 147 */     return this.locator;
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
/*     */   public final XSComponent getSchemaComponent() {
/* 159 */     return this.source;
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
/*     */   public String getName() {
/* 189 */     return getName(false);
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
/*     */   public String getName(boolean isPublic) {
/* 208 */     return isPublic ? this.publicName : this.privateName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(boolean isPublic, String newName) {
/* 219 */     if (isPublic) {
/* 220 */       this.publicName = newName;
/*     */     } else {
/* 222 */       this.privateName = newName;
/*     */     } 
/*     */   }
/*     */   public String displayName() {
/* 226 */     return this.parent.toString() + '#' + getName(false);
/*     */   }
/*     */   
/*     */   public boolean isCollection() {
/* 230 */     return this.isCollection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnboxable() {
/* 265 */     Collection<? extends CTypeInfo> ts = ref();
/* 266 */     if (ts.size() != 1)
/*     */     {
/*     */       
/* 269 */       return false;
/*     */     }
/* 271 */     if (this.baseType != null && this.baseType instanceof com.sun.codemodel.JClass) {
/* 272 */       return false;
/*     */     }
/* 274 */     CTypeInfo t = ts.iterator().next();
/*     */     
/* 276 */     return ((NType)t.getType()).isBoxedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/* 284 */     return false;
/*     */   }
/*     */   
/*     */   public CCustomizations getCustomizations() {
/* 288 */     return this.customizations;
/*     */   }
/*     */   
/*     */   public boolean inlineBinaryData() {
/* 292 */     return this.inlineBinaryData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean needsExplicitTypeName(TypeUse type, QName typeName) {
/* 302 */     if (typeName == null)
/*     */     {
/* 304 */       return false;
/*     */     }
/* 306 */     if (!typeName.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema"))
/*     */     {
/*     */       
/* 309 */       return false;
/*     */     }
/* 311 */     if (type.isCollection())
/*     */     {
/*     */       
/* 314 */       return true;
/*     */     }
/* 316 */     QName itemType = type.getInfo().getTypeName();
/* 317 */     if (itemType == null)
/*     */     {
/*     */ 
/*     */       
/* 321 */       return true;
/*     */     }
/*     */     
/* 324 */     return !itemType.equals(typeName);
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
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table) {
/* 336 */     return null;
/*     */   }
/*     */   
/*     */   public final <A extends Annotation> A readAnnotation(Class<A> annotationType) {
/* 340 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 344 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public abstract CAdapter getAdapter();
/*     */   
/*     */   public abstract Collection<? extends CTypeInfo> ref();
/*     */   
/*     */   public abstract <V> V accept(CPropertyVisitor<V> paramCPropertyVisitor);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */