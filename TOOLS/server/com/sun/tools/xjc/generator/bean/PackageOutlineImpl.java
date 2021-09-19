/*     */ package com.sun.tools.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlSchemaWriter;
/*     */ import com.sun.tools.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElement;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyVisitor;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.PackageOutline;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
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
/*     */ final class PackageOutlineImpl
/*     */   implements PackageOutline
/*     */ {
/*     */   private final Model _model;
/*     */   private final JPackage _package;
/*     */   private final ObjectFactoryGenerator objectFactoryGenerator;
/*  76 */   final Set<ClassOutlineImpl> classes = new HashSet<ClassOutlineImpl>();
/*  77 */   private final Set<ClassOutlineImpl> classesView = Collections.unmodifiableSet(this.classes);
/*     */ 
/*     */   
/*     */   private String mostUsedNamespaceURI;
/*     */ 
/*     */   
/*     */   private XmlNsForm elementFormDefault;
/*     */ 
/*     */   
/*     */   private HashMap<String, Integer> uriCountMap;
/*     */   
/*     */   private HashMap<String, Integer> propUriCountMap;
/*     */ 
/*     */   
/*     */   public String getMostUsedNamespaceURI() {
/*  92 */     return this.mostUsedNamespaceURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlNsForm getElementFormDefault() {
/* 101 */     assert this.elementFormDefault != null;
/* 102 */     return this.elementFormDefault;
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/* 106 */     return this._package;
/*     */   }
/*     */   
/*     */   public ObjectFactoryGenerator objectFactoryGenerator() {
/* 110 */     return this.objectFactoryGenerator;
/*     */   }
/*     */   
/*     */   public Set<ClassOutlineImpl> getClasses() {
/* 114 */     return this.classesView;
/*     */   }
/*     */   
/*     */   public JDefinedClass objectFactory() {
/* 118 */     return this.objectFactoryGenerator.getObjectFactory();
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
/*     */   public void calcDefaultValues() {
/* 146 */     if (!this._model.isPackageLevelAnnotations()) {
/* 147 */       this.mostUsedNamespaceURI = "";
/* 148 */       this.elementFormDefault = XmlNsForm.UNQUALIFIED;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 153 */     CPropertyVisitor<Void> propVisitor = new CPropertyVisitor<Void>() {
/*     */         public Void onElement(CElementPropertyInfo p) {
/* 155 */           for (CTypeRef tr : p.getTypes()) {
/* 156 */             PackageOutlineImpl.this.countURI(PackageOutlineImpl.this.propUriCountMap, tr.getTagName());
/*     */           }
/* 158 */           return null;
/*     */         }
/*     */         
/*     */         public Void onReference(CReferencePropertyInfo p) {
/* 162 */           for (CElement e : p.getElements()) {
/* 163 */             PackageOutlineImpl.this.countURI(PackageOutlineImpl.this.propUriCountMap, e.getElementName());
/*     */           }
/* 165 */           return null;
/*     */         }
/*     */         
/*     */         public Void onAttribute(CAttributePropertyInfo p) {
/* 169 */           return null;
/*     */         }
/*     */         
/*     */         public Void onValue(CValuePropertyInfo p) {
/* 173 */           return null;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 178 */     for (ClassOutlineImpl co : this.classes) {
/* 179 */       CClassInfo ci = co.target;
/* 180 */       countURI(this.uriCountMap, ci.getTypeName());
/* 181 */       countURI(this.uriCountMap, ci.getElementName());
/*     */       
/* 183 */       for (CPropertyInfo p : ci.getProperties())
/* 184 */         p.accept(propVisitor); 
/*     */     } 
/* 186 */     this.mostUsedNamespaceURI = getMostUsedURI(this.uriCountMap);
/* 187 */     this.elementFormDefault = getFormDefault();
/*     */ 
/*     */ 
/*     */     
/* 191 */     if (!this.mostUsedNamespaceURI.equals("") || this.elementFormDefault == XmlNsForm.QUALIFIED) {
/* 192 */       XmlSchemaWriter w = (XmlSchemaWriter)this._model.strategy.getPackage(this._package, Aspect.IMPLEMENTATION).annotate2(XmlSchemaWriter.class);
/* 193 */       if (!this.mostUsedNamespaceURI.equals(""))
/* 194 */         w.namespace(this.mostUsedNamespaceURI); 
/* 195 */       if (this.elementFormDefault == XmlNsForm.QUALIFIED) {
/* 196 */         w.elementFormDefault(this.elementFormDefault);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected PackageOutlineImpl(BeanGenerator outline, Model model, JPackage _pkg) {
/* 202 */     this.uriCountMap = new HashMap<String, Integer>();
/*     */ 
/*     */ 
/*     */     
/* 206 */     this.propUriCountMap = new HashMap<String, Integer>(); this._model = model; this._package = _pkg;
/*     */     switch (model.strategy) {
/*     */       case BEAN_ONLY:
/*     */         this.objectFactoryGenerator = new PublicObjectFactoryGenerator(outline, model, _pkg);
/*     */         return;
/*     */       case INTF_AND_IMPL:
/*     */         this.objectFactoryGenerator = new DualObjectFactoryGenerator(outline, model, _pkg);
/*     */         return;
/*     */     } 
/* 215 */     throw new IllegalStateException(); } private void countURI(HashMap<String, Integer> map, QName qname) { if (qname == null)
/*     */       return; 
/* 217 */     String uri = qname.getNamespaceURI();
/*     */     
/* 219 */     if (map.containsKey(uri)) {
/* 220 */       map.put(uri, Integer.valueOf(((Integer)map.get(uri)).intValue() + 1));
/*     */     } else {
/* 222 */       map.put(uri, Integer.valueOf(1));
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getMostUsedURI(HashMap<String, Integer> map) {
/* 237 */     String mostPopular = null;
/* 238 */     int count = 0;
/*     */     
/* 240 */     for (Map.Entry<String, Integer> e : map.entrySet()) {
/* 241 */       String uri = e.getKey();
/* 242 */       int uriCount = ((Integer)e.getValue()).intValue();
/* 243 */       if (mostPopular == null) {
/* 244 */         mostPopular = uri;
/* 245 */         count = uriCount; continue;
/*     */       } 
/* 247 */       if (uriCount > count || (uriCount == count && mostPopular.equals(""))) {
/* 248 */         mostPopular = uri;
/* 249 */         count = uriCount;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 254 */     if (mostPopular == null) return ""; 
/* 255 */     return mostPopular;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlNsForm getFormDefault() {
/* 265 */     if (getMostUsedURI(this.propUriCountMap).equals("")) return XmlNsForm.UNQUALIFIED; 
/* 266 */     return XmlNsForm.QUALIFIED;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\PackageOutlineImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */