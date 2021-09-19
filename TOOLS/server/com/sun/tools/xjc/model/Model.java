/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.Plugin;
/*     */ import com.sun.tools.xjc.api.ClassNameAllocator;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.xjc.generator.bean.ImplStructureStrategy;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.util.FlattenIterator;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Model
/*     */   implements TypeInfoSet<NType, NClass, Void, Void>, CCustomizable
/*     */ {
/*  95 */   private final Map<NClass, CClassInfo> beans = new LinkedHashMap<NClass, CClassInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private final Map<NClass, CEnumLeafInfo> enums = new LinkedHashMap<NClass, CEnumLeafInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private final Map<NClass, Map<QName, CElementInfo>> elementMappings = new HashMap<NClass, Map<QName, CElementInfo>>();
/*     */ 
/*     */   
/* 108 */   private final Iterable<? extends CElementInfo> allElements = new Iterable<CElementInfo>()
/*     */     {
/*     */       public Iterator<CElementInfo> iterator() {
/* 111 */         return (Iterator<CElementInfo>)new FlattenIterator(Model.this.elementMappings.values());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private final Map<QName, TypeUse> typeUses = new LinkedHashMap<QName, TypeUse>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameConverter nameConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CCustomizations customizations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean packageLevelAnnotations = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XSSchemaSet schemaComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   private CCustomizations gloablCustomizations = new CCustomizations();
/*     */   
/*     */   @XmlTransient
/*     */   public final JCodeModel codeModel;
/*     */   
/*     */   public final Options options;
/*     */   
/*     */   @XmlAttribute
/*     */   public boolean serializable;
/*     */   
/*     */   @XmlAttribute
/*     */   public Long serialVersionUID;
/*     */   
/*     */   @XmlTransient
/*     */   public JClass rootClass;
/*     */   @XmlTransient
/*     */   public JClass rootInterface;
/*     */   public ImplStructureStrategy strategy;
/*     */   final ClassNameAllocatorWrapper allocator;
/*     */   @XmlTransient
/*     */   public final SymbolSpace defaultSymbolSpace;
/*     */   private final Map<String, SymbolSpace> symbolSpaces;
/*     */   private final Map<JPackage, CClassInfoParent.Package> cache;
/*     */   static final Locator EMPTY_LOCATOR;
/*     */   
/*     */   public void setNameConverter(NameConverter nameConverter) {
/* 176 */     assert this.nameConverter == null;
/* 177 */     assert nameConverter != null;
/* 178 */     this.nameConverter = nameConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final NameConverter getNameConverter() {
/* 185 */     return this.nameConverter;
/*     */   }
/*     */   
/*     */   public boolean isPackageLevelAnnotations() {
/* 189 */     return this.packageLevelAnnotations;
/*     */   }
/*     */   
/*     */   public void setPackageLevelAnnotations(boolean packageLevelAnnotations) {
/* 193 */     this.packageLevelAnnotations = packageLevelAnnotations;
/*     */   }
/*     */   public SymbolSpace getSymbolSpace(String name) { SymbolSpace ss = this.symbolSpaces.get(name); if (ss == null)
/*     */       this.symbolSpaces.put(name, ss = new SymbolSpace(this.codeModel)); 
/*     */     return ss; }
/*     */   public Outline generateCode(Options opt, ErrorReceiver receiver) { ErrorReceiverFilter ehf = new ErrorReceiverFilter((ErrorListener)receiver);
/*     */     for (Plugin ma : opt.activePlugins)
/*     */       ma.postProcessModel(this, (ErrorHandler)ehf); 
/*     */     Outline o = BeanGenerator.generate(this, (ErrorReceiver)ehf);
/*     */     try {
/*     */       for (Plugin ma : opt.activePlugins)
/*     */         ma.run(o, opt, (ErrorHandler)ehf); 
/*     */     } catch (SAXException e) {
/*     */       return null;
/*     */     } 
/*     */     Set<CCustomizations> check = new HashSet<CCustomizations>();
/*     */     for (CCustomizations c = this.customizations; c != null; c = c.next) {
/*     */       if (!check.add(c))
/*     */         throw new AssertionError(); 
/*     */       for (CPluginCustomization p : c) {
/*     */         if (!p.isAcknowledged()) {
/*     */           ehf.error(p.locator, Messages.format("UnusedCustomizationChecker.UnacknolwedgedCustomization", new Object[] { p.element.getNodeName() }));
/*     */           ehf.error(c.getOwner().getLocator(), Messages.format("UnusedCustomizationChecker.UnacknolwedgedCustomization.Relevant", new Object[0]));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     if (ehf.hadError())
/*     */       o = null; 
/*     */     return o; }
/*     */   public final Map<QName, CClassInfo> createTopLevelBindings() { Map<QName, CClassInfo> r = new HashMap<QName, CClassInfo>();
/*     */     for (CClassInfo b : beans().values()) {
/*     */       if (b.isElement())
/*     */         r.put(b.getElementName(), b); 
/*     */     } 
/*     */     return r; }
/*     */   public Navigator<NType, NClass, Void, Void> getNavigator() { return (Navigator<NType, NClass, Void, Void>)NavigatorImpl.theInstance; }
/*     */   public CNonElement getTypeInfo(NType type) { CBuiltinLeafInfo leaf = CBuiltinLeafInfo.LEAVES.get(type);
/*     */     if (leaf != null)
/*     */       return leaf; 
/*     */     return getClassInfo((NClass)getNavigator().asDecl(type)); }
/*     */   public CBuiltinLeafInfo getAnyTypeInfo() { return CBuiltinLeafInfo.ANYTYPE; }
/*     */   public CNonElement getTypeInfo(Ref<NType, NClass> ref) { assert !ref.valueList;
/*     */     return getTypeInfo((NType)ref.type); }
/*     */   public Map<NClass, CClassInfo> beans() { return this.beans; }
/* 237 */   public Map<NClass, CEnumLeafInfo> enums() { return this.enums; } public Map<QName, TypeUse> typeUses() { return this.typeUses; } public Model(Options opts, JCodeModel cm, NameConverter nc, ClassNameAllocator allocator, XSSchemaSet schemaComponent) { this.strategy = ImplStructureStrategy.BEAN_ONLY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     this.symbolSpaces = new HashMap<String, SymbolSpace>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 485 */     this.cache = new HashMap<JPackage, CClassInfoParent.Package>(); this.options = opts; this.codeModel = cm; this.nameConverter = nc; this.defaultSymbolSpace = new SymbolSpace(this.codeModel); this.defaultSymbolSpace.setType((JType)this.codeModel.ref(Object.class)); this.elementMappings.put(null, new HashMap<QName, CElementInfo>()); if (opts.automaticNameConflictResolution) allocator = new AutoClassNameAllocator(allocator);  this.allocator = new ClassNameAllocatorWrapper(allocator); this.schemaComponent = schemaComponent; this.gloablCustomizations.setParent(this, this); }
/*     */   public Map<NType, ? extends CArrayInfo> arrays() { return Collections.emptyMap(); }
/*     */   public Map<NType, ? extends CBuiltinLeafInfo> builtins() { return CBuiltinLeafInfo.LEAVES; }
/* 488 */   public CClassInfo getClassInfo(NClass t) { return this.beans.get(t); } public CClassInfoParent.Package getPackage(JPackage pkg) { CClassInfoParent.Package r = this.cache.get(pkg);
/* 489 */     if (r == null)
/* 490 */       this.cache.put(pkg, r = new CClassInfoParent.Package(pkg)); 
/* 491 */     return r; } public CElementInfo getElementInfo(NClass scope, QName name) { Map<QName, CElementInfo> m = this.elementMappings.get(scope); if (m != null) { CElementInfo r = m.get(name); if (r != null) return r;  }  return (CElementInfo)((Map)this.elementMappings.get(null)).get(name); } public Map<QName, CElementInfo> getElementMappings(NClass scope) { return this.elementMappings.get(scope); }
/*     */   public Iterable<? extends CElementInfo> getAllElements() { return this.allElements; }
/*     */   public XSComponent getSchemaComponent() { return null; }
/*     */   public Locator getLocator() { LocatorImpl r = new LocatorImpl(); r.setLineNumber(-1); r.setColumnNumber(-1); return r; }
/*     */   public CCustomizations getCustomizations() { return this.gloablCustomizations; }
/*     */   public Map<String, String> getXmlNs(String namespaceUri) { return Collections.emptyMap(); }
/* 497 */   static { LocatorImpl l = new LocatorImpl();
/* 498 */     l.setColumnNumber(-1);
/* 499 */     l.setLineNumber(-1);
/* 500 */     EMPTY_LOCATOR = l; }
/*     */ 
/*     */   
/*     */   public Map<String, String> getSchemaLocations() {
/*     */     return Collections.emptyMap();
/*     */   }
/*     */   
/*     */   public XmlNsForm getElementFormDefault(String nsUri) {
/*     */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public XmlNsForm getAttributeFormDefault(String nsUri) {
/*     */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void dump(Result out) {
/*     */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   void add(CEnumLeafInfo e) {
/*     */     this.enums.put(e.getClazz(), e);
/*     */   }
/*     */   
/*     */   void add(CClassInfo ci) {
/*     */     this.beans.put(ci.getClazz(), ci);
/*     */   }
/*     */   
/*     */   void add(CElementInfo ei) {
/*     */     NClass clazz = null;
/*     */     if (ei.getScope() != null)
/*     */       clazz = ei.getScope().getClazz(); 
/*     */     Map<QName, CElementInfo> m = this.elementMappings.get(clazz);
/*     */     if (m == null)
/*     */       this.elementMappings.put(clazz, m = new HashMap<QName, CElementInfo>()); 
/*     */     m.put(ei.getElementName(), ei);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\Model.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */