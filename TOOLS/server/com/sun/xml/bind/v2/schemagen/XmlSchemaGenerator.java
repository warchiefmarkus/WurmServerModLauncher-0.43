/*      */ package com.sun.xml.bind.v2.schemagen;
/*      */ 
/*      */ import com.sun.istack.NotNull;
/*      */ import com.sun.istack.Nullable;
/*      */ import com.sun.xml.bind.Util;
/*      */ import com.sun.xml.bind.api.CompositeStructure;
/*      */ import com.sun.xml.bind.api.ErrorListener;
/*      */ import com.sun.xml.bind.v2.TODO;
/*      */ import com.sun.xml.bind.v2.model.core.Adapter;
/*      */ import com.sun.xml.bind.v2.model.core.ArrayInfo;
/*      */ import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*      */ import com.sun.xml.bind.v2.model.core.Element;
/*      */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*      */ import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.EnumConstant;
/*      */ import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/*      */ import com.sun.xml.bind.v2.model.core.ID;
/*      */ import com.sun.xml.bind.v2.model.core.MapPropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.NonElement;
/*      */ import com.sun.xml.bind.v2.model.core.NonElementRef;
/*      */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*      */ import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*      */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*      */ import com.sun.xml.bind.v2.model.core.TypeRef;
/*      */ import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
/*      */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*      */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*      */ import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
/*      */ import com.sun.xml.bind.v2.schemagen.episode.Bindings;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.Any;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.AttrDecls;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.AttributeType;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.ComplexExtension;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.ComplexType;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.ComplexTypeHost;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.ContentModelContainer;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.ExplicitGroup;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.Import;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.List;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.LocalAttribute;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.LocalElement;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.Occurs;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.Schema;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleExtension;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleRestriction;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleType;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleTypeHost;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.TopLevelAttribute;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.TopLevelElement;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.TypeDefParticle;
/*      */ import com.sun.xml.bind.v2.schemagen.xmlschema.TypeHost;
/*      */ import com.sun.xml.bind.v2.util.CollisionCheckStack;
/*      */ import com.sun.xml.bind.v2.util.StackRecorder;
/*      */ import com.sun.xml.txw2.TXW;
/*      */ import com.sun.xml.txw2.TxwException;
/*      */ import com.sun.xml.txw2.TypedXmlWriter;
/*      */ import com.sun.xml.txw2.output.ResultFactory;
/*      */ import com.sun.xml.txw2.output.XmlSerializer;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.activation.MimeType;
/*      */ import javax.xml.bind.SchemaOutputResolver;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.transform.Result;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import org.xml.sax.SAXParseException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class XmlSchemaGenerator<T, C, F, M>
/*      */ {
/*  140 */   private static final Logger logger = Util.getClassLogger();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  151 */   private final Map<String, Namespace> namespaces = new TreeMap<String, Namespace>(NAMESPACE_COMPARATOR);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ErrorListener errorListener;
/*      */ 
/*      */ 
/*      */   
/*      */   private Navigator<T, C, F, M> navigator;
/*      */ 
/*      */ 
/*      */   
/*      */   private final TypeInfoSet<T, C, F, M> types;
/*      */ 
/*      */ 
/*      */   
/*      */   private final NonElement<T, C> stringType;
/*      */ 
/*      */ 
/*      */   
/*      */   private final NonElement<T, C> anyType;
/*      */ 
/*      */ 
/*      */   
/*  176 */   private final CollisionCheckStack<ClassInfo<T, C>> collisionChecker = new CollisionCheckStack();
/*      */   
/*      */   public XmlSchemaGenerator(Navigator<T, C, F, M> navigator, TypeInfoSet<T, C, F, M> types) {
/*  179 */     this.navigator = navigator;
/*  180 */     this.types = types;
/*      */     
/*  182 */     this.stringType = types.getTypeInfo(navigator.ref(String.class));
/*  183 */     this.anyType = types.getAnyTypeInfo();
/*      */ 
/*      */     
/*  186 */     for (ClassInfo<T, C> ci : (Iterable<ClassInfo<T, C>>)types.beans().values())
/*  187 */       add(ci); 
/*  188 */     for (ElementInfo<T, C> ei1 : (Iterable<ElementInfo<T, C>>)types.getElementMappings(null).values())
/*  189 */       add(ei1); 
/*  190 */     for (EnumLeafInfo<T, C> ei : (Iterable<EnumLeafInfo<T, C>>)types.enums().values())
/*  191 */       add(ei); 
/*  192 */     for (ArrayInfo<T, C> a : (Iterable<ArrayInfo<T, C>>)types.arrays().values())
/*  193 */       add(a); 
/*      */   }
/*      */   
/*      */   private Namespace getNamespace(String uri) {
/*  197 */     Namespace n = this.namespaces.get(uri);
/*  198 */     if (n == null)
/*  199 */       this.namespaces.put(uri, n = new Namespace(uri)); 
/*  200 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ClassInfo<T, C> clazz) {
/*  212 */     assert clazz != null;
/*      */     
/*  214 */     String nsUri = null;
/*      */     
/*  216 */     if (clazz.getClazz() == this.navigator.asDecl(CompositeStructure.class)) {
/*      */       return;
/*      */     }
/*  219 */     if (clazz.isElement()) {
/*      */       
/*  221 */       nsUri = clazz.getElementName().getNamespaceURI();
/*  222 */       Namespace ns = getNamespace(nsUri);
/*  223 */       ns.classes.add(clazz);
/*  224 */       ns.addDependencyTo(clazz.getTypeName());
/*      */ 
/*      */       
/*  227 */       add(clazz.getElementName(), false, (NonElement<T, C>)clazz);
/*      */     } 
/*      */     
/*  230 */     QName tn = clazz.getTypeName();
/*  231 */     if (tn != null) {
/*  232 */       nsUri = tn.getNamespaceURI();
/*      */     
/*      */     }
/*  235 */     else if (nsUri == null) {
/*      */       return;
/*      */     } 
/*      */     
/*  239 */     Namespace n = getNamespace(nsUri);
/*  240 */     n.classes.add(clazz);
/*      */ 
/*      */     
/*  243 */     for (PropertyInfo<T, C> p : (Iterable<PropertyInfo<T, C>>)clazz.getProperties()) {
/*  244 */       n.processForeignNamespaces(p);
/*  245 */       if (p instanceof AttributePropertyInfo) {
/*  246 */         AttributePropertyInfo<T, C> ap = (AttributePropertyInfo<T, C>)p;
/*  247 */         String aUri = ap.getXmlName().getNamespaceURI();
/*  248 */         if (aUri.length() > 0) {
/*      */           
/*  250 */           getNamespace(aUri).addGlobalAttribute(ap);
/*  251 */           n.addDependencyTo(ap.getXmlName());
/*      */         } 
/*      */       } 
/*  254 */       if (p instanceof ElementPropertyInfo) {
/*  255 */         ElementPropertyInfo<T, C> ep = (ElementPropertyInfo<T, C>)p;
/*  256 */         for (TypeRef<T, C> tref : (Iterable<TypeRef<T, C>>)ep.getTypes()) {
/*  257 */           String eUri = tref.getTagName().getNamespaceURI();
/*  258 */           if (eUri.length() > 0 && !eUri.equals(n.uri)) {
/*  259 */             getNamespace(eUri).addGlobalElement(tref);
/*  260 */             n.addDependencyTo(tref.getTagName());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  265 */       if (generateSwaRefAdapter(p)) {
/*  266 */         n.useSwaRef = true;
/*      */       }
/*      */     } 
/*      */     
/*  270 */     ClassInfo<T, C> bc = clazz.getBaseClass();
/*  271 */     if (bc != null) {
/*  272 */       add(bc);
/*  273 */       n.addDependencyTo(bc.getTypeName());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ElementInfo<T, C> elem) {
/*  281 */     assert elem != null;
/*      */     
/*  283 */     QName name = elem.getElementName();
/*  284 */     Namespace n = getNamespace(name.getNamespaceURI());
/*  285 */     n.getClass(); n.elementDecls.put(name.getLocalPart(), new Namespace.ElementWithType(true, elem.getContentType()));
/*      */ 
/*      */     
/*  288 */     n.processForeignNamespaces((PropertyInfo<T, C>)elem.getProperty());
/*      */   }
/*      */   
/*      */   public void add(EnumLeafInfo<T, C> envm) {
/*  292 */     assert envm != null;
/*      */     
/*  294 */     String nsUri = null;
/*      */     
/*  296 */     if (envm.isElement()) {
/*      */       
/*  298 */       nsUri = envm.getElementName().getNamespaceURI();
/*  299 */       Namespace ns = getNamespace(nsUri);
/*  300 */       ns.enums.add(envm);
/*  301 */       ns.addDependencyTo(envm.getTypeName());
/*      */ 
/*      */       
/*  304 */       add(envm.getElementName(), false, (NonElement<T, C>)envm);
/*      */     } 
/*      */     
/*  307 */     QName typeName = envm.getTypeName();
/*  308 */     if (typeName != null) {
/*  309 */       nsUri = typeName.getNamespaceURI();
/*      */     }
/*  311 */     else if (nsUri == null) {
/*      */       return;
/*      */     } 
/*      */     
/*  315 */     Namespace n = getNamespace(nsUri);
/*  316 */     n.enums.add(envm);
/*      */ 
/*      */     
/*  319 */     n.addDependencyTo(envm.getBaseType().getTypeName());
/*      */   }
/*      */   
/*      */   public void add(ArrayInfo<T, C> a) {
/*  323 */     assert a != null;
/*      */     
/*  325 */     String namespaceURI = a.getTypeName().getNamespaceURI();
/*  326 */     Namespace n = getNamespace(namespaceURI);
/*  327 */     n.arrays.add(a);
/*      */ 
/*      */     
/*  330 */     n.addDependencyTo(a.getItemType().getTypeName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(QName tagName, boolean isNillable, NonElement<T, C> type) {
/*  344 */     if (type != null && type.getType() == this.navigator.ref(CompositeStructure.class)) {
/*      */       return;
/*      */     }
/*      */     
/*  348 */     Namespace n = getNamespace(tagName.getNamespaceURI());
/*  349 */     n.getClass(); n.elementDecls.put(tagName.getLocalPart(), new Namespace.ElementWithType(isNillable, type));
/*      */ 
/*      */     
/*  352 */     if (type != null) {
/*  353 */       n.addDependencyTo(type.getTypeName());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeEpisodeFile(XmlSerializer out) {
/*  360 */     Bindings root = (Bindings)TXW.create(Bindings.class, out);
/*      */     
/*  362 */     if (this.namespaces.containsKey(""))
/*  363 */       root._namespace("http://java.sun.com/xml/ns/jaxb", "jaxb"); 
/*  364 */     root.version("2.1");
/*      */ 
/*      */ 
/*      */     
/*  368 */     for (Map.Entry<String, Namespace> e : this.namespaces.entrySet()) {
/*  369 */       String prefix; Bindings group = root.bindings();
/*      */ 
/*      */       
/*  372 */       String tns = e.getKey();
/*  373 */       if (!tns.equals("")) {
/*  374 */         group._namespace(tns, "tns");
/*  375 */         prefix = "tns:";
/*      */       } else {
/*  377 */         prefix = "";
/*      */       } 
/*      */       
/*  380 */       group.scd("x-schema::" + (tns.equals("") ? "" : "tns"));
/*  381 */       group.schemaBindings().map(false);
/*      */       
/*  383 */       for (ClassInfo<T, C> ci : (Iterable<ClassInfo<T, C>>)(e.getValue()).classes) {
/*  384 */         if (ci.getTypeName() == null)
/*      */           continue; 
/*  386 */         if (ci.getTypeName().getNamespaceURI().equals(tns)) {
/*  387 */           Bindings child = group.bindings();
/*  388 */           child.scd('~' + prefix + ci.getTypeName().getLocalPart());
/*  389 */           child.klass().ref(ci.getName());
/*      */         } 
/*      */         
/*  392 */         if (ci.isElement() && ci.getElementName().getNamespaceURI().equals(tns)) {
/*  393 */           Bindings child = group.bindings();
/*  394 */           child.scd(prefix + ci.getElementName().getLocalPart());
/*  395 */           child.klass().ref(ci.getName());
/*      */         } 
/*      */       } 
/*      */       
/*  399 */       for (EnumLeafInfo<T, C> en : (Iterable<EnumLeafInfo<T, C>>)(e.getValue()).enums) {
/*  400 */         if (en.getTypeName() == null)
/*      */           continue; 
/*  402 */         Bindings child = group.bindings();
/*  403 */         child.scd('~' + prefix + en.getTypeName().getLocalPart());
/*  404 */         child.klass().ref(this.navigator.getClassName(en.getClazz()));
/*      */       } 
/*      */       
/*  407 */       group.commit(true);
/*      */     } 
/*      */     
/*  410 */     root.commit();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(SchemaOutputResolver resolver, ErrorListener errorListener) throws IOException {
/*  417 */     if (resolver == null) {
/*  418 */       throw new IllegalArgumentException();
/*      */     }
/*  420 */     if (logger.isLoggable(Level.FINE))
/*      */     {
/*  422 */       logger.log(Level.FINE, "Wrigin XML Schema for " + toString(), (Throwable)new StackRecorder());
/*      */     }
/*      */ 
/*      */     
/*  426 */     resolver = new FoolProofResolver(resolver);
/*  427 */     this.errorListener = errorListener;
/*      */     
/*  429 */     Map<String, String> schemaLocations = this.types.getSchemaLocations();
/*      */     
/*  431 */     Map<Namespace, Result> out = new HashMap<Namespace, Result>();
/*  432 */     Map<Namespace, String> systemIds = new HashMap<Namespace, String>();
/*      */ 
/*      */ 
/*      */     
/*  436 */     this.namespaces.remove("http://www.w3.org/2001/XMLSchema");
/*      */ 
/*      */ 
/*      */     
/*  440 */     for (Namespace n : this.namespaces.values()) {
/*  441 */       String schemaLocation = schemaLocations.get(n.uri);
/*  442 */       if (schemaLocation != null) {
/*  443 */         systemIds.put(n, schemaLocation); continue;
/*      */       } 
/*  445 */       Result output = resolver.createOutput(n.uri, "schema" + (out.size() + 1) + ".xsd");
/*  446 */       if (output != null) {
/*  447 */         out.put(n, output);
/*  448 */         systemIds.put(n, output.getSystemId());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  454 */     for (Map.Entry<Namespace, Result> e : out.entrySet()) {
/*  455 */       Result result = e.getValue();
/*  456 */       ((Namespace)e.getKey()).writeTo(result, systemIds);
/*  457 */       if (result instanceof StreamResult) {
/*  458 */         OutputStream outputStream = ((StreamResult)result).getOutputStream();
/*  459 */         if (outputStream != null) {
/*  460 */           outputStream.close(); continue;
/*      */         } 
/*  462 */         Writer writer = ((StreamResult)result).getWriter();
/*  463 */         if (writer != null) writer.close();
/*      */       
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class Namespace
/*      */   {
/*      */     @NotNull
/*      */     final String uri;
/*      */ 
/*      */ 
/*      */     
/*  480 */     private final Set<Namespace> depends = new LinkedHashSet<Namespace>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean selfReference;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  490 */     private final Set<ClassInfo<T, C>> classes = new LinkedHashSet<ClassInfo<T, C>>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  495 */     private final Set<EnumLeafInfo<T, C>> enums = new LinkedHashSet<EnumLeafInfo<T, C>>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  500 */     private final Set<ArrayInfo<T, C>> arrays = new LinkedHashSet<ArrayInfo<T, C>>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  505 */     private final MultiMap<String, AttributePropertyInfo<T, C>> attributeDecls = new MultiMap<String, AttributePropertyInfo<T, C>>(null);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  510 */     private final MultiMap<String, ElementDeclaration> elementDecls = new MultiMap<String, ElementDeclaration>(new ElementWithType(true, XmlSchemaGenerator.this.anyType));
/*      */ 
/*      */     
/*      */     private Form attributeFormDefault;
/*      */ 
/*      */     
/*      */     private Form elementFormDefault;
/*      */ 
/*      */     
/*      */     private boolean useSwaRef;
/*      */ 
/*      */     
/*      */     public Namespace(String uri) {
/*  523 */       this.uri = uri;
/*  524 */       assert !XmlSchemaGenerator.access$900(XmlSchemaGenerator.this).containsKey(uri);
/*  525 */       XmlSchemaGenerator.access$900(XmlSchemaGenerator.this).put(uri, this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void processForeignNamespaces(PropertyInfo<T, C> p) {
/*  539 */       for (TypeInfo<T, C> t : (Iterable<TypeInfo<T, C>>)p.ref()) {
/*  540 */         if (t instanceof Element) {
/*  541 */           addDependencyTo(((Element)t).getElementName());
/*      */         }
/*  543 */         if (t instanceof NonElement) {
/*  544 */           addDependencyTo(((NonElement)t).getTypeName());
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void addDependencyTo(@Nullable QName qname) {
/*  553 */       if (qname == null)
/*      */         return; 
/*  555 */       String nsUri = qname.getNamespaceURI();
/*      */       
/*  557 */       if (nsUri.equals("http://www.w3.org/2001/XMLSchema")) {
/*      */         return;
/*      */       }
/*      */       
/*  561 */       if (nsUri.equals(this.uri)) {
/*  562 */         this.selfReference = true;
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  567 */       this.depends.add(XmlSchemaGenerator.this.getNamespace(nsUri));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeTo(Result result, Map<Namespace, String> systemIds) throws IOException {
/*      */       try {
/*  578 */         Schema schema = (Schema)TXW.create(Schema.class, ResultFactory.createSerializer(result));
/*      */ 
/*      */         
/*  581 */         Map<String, String> xmlNs = XmlSchemaGenerator.this.types.getXmlNs(this.uri);
/*      */         
/*  583 */         for (Map.Entry<String, String> e : xmlNs.entrySet()) {
/*  584 */           schema._namespace(e.getValue(), e.getKey());
/*      */         }
/*      */         
/*  587 */         if (this.useSwaRef) {
/*  588 */           schema._namespace("http://ws-i.org/profiles/basic/1.1/xsd", "swaRef");
/*      */         }
/*  590 */         this.attributeFormDefault = Form.get(XmlSchemaGenerator.this.types.getAttributeFormDefault(this.uri));
/*  591 */         this.attributeFormDefault.declare("attributeFormDefault", schema);
/*      */         
/*  593 */         this.elementFormDefault = Form.get(XmlSchemaGenerator.this.types.getElementFormDefault(this.uri));
/*      */         
/*  595 */         this.elementFormDefault.declare("elementFormDefault", schema);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  600 */         if (!xmlNs.containsValue("http://www.w3.org/2001/XMLSchema") && !xmlNs.containsKey("xs"))
/*      */         {
/*  602 */           schema._namespace("http://www.w3.org/2001/XMLSchema", "xs"); } 
/*  603 */         schema.version("1.0");
/*      */         
/*  605 */         if (this.uri.length() != 0) {
/*  606 */           schema.targetNamespace(this.uri);
/*      */         }
/*      */ 
/*      */         
/*  610 */         for (Namespace ns : this.depends) {
/*  611 */           schema._namespace(ns.uri);
/*      */         }
/*      */         
/*  614 */         if (this.selfReference && this.uri.length() != 0)
/*      */         {
/*      */           
/*  617 */           schema._namespace(this.uri, "tns");
/*      */         }
/*      */         
/*  620 */         schema._pcdata("\n");
/*      */ 
/*      */         
/*  623 */         for (Namespace n : this.depends) {
/*  624 */           Import imp = schema._import();
/*  625 */           if (n.uri.length() != 0)
/*  626 */             imp.namespace(n.uri); 
/*  627 */           String refSystemId = systemIds.get(n);
/*  628 */           if (refSystemId != null && !refSystemId.equals(""))
/*      */           {
/*  630 */             imp.schemaLocation(XmlSchemaGenerator.relativize(refSystemId, result.getSystemId()));
/*      */           }
/*  632 */           schema._pcdata("\n");
/*      */         } 
/*  634 */         if (this.useSwaRef) {
/*  635 */           schema._import().namespace("http://ws-i.org/profiles/basic/1.1/xsd").schemaLocation("http://ws-i.org/profiles/basic/1.1/swaref.xsd");
/*      */         }
/*      */ 
/*      */         
/*  639 */         for (Map.Entry<String, ElementDeclaration> e : this.elementDecls.entrySet()) {
/*  640 */           ((ElementDeclaration)e.getValue()).writeTo(e.getKey(), schema);
/*  641 */           schema._pcdata("\n");
/*      */         } 
/*  643 */         for (ClassInfo<T, C> c : this.classes) {
/*  644 */           if (c.getTypeName() == null) {
/*      */             continue;
/*      */           }
/*      */           
/*  648 */           if (this.uri.equals(c.getTypeName().getNamespaceURI()))
/*  649 */             writeClass(c, (TypeHost)schema); 
/*  650 */           schema._pcdata("\n");
/*      */         } 
/*  652 */         for (EnumLeafInfo<T, C> e : this.enums) {
/*  653 */           if (e.getTypeName() == null) {
/*      */             continue;
/*      */           }
/*      */           
/*  657 */           if (this.uri.equals(e.getTypeName().getNamespaceURI()))
/*  658 */             writeEnum(e, (SimpleTypeHost)schema); 
/*  659 */           schema._pcdata("\n");
/*      */         } 
/*  661 */         for (ArrayInfo<T, C> a : this.arrays) {
/*  662 */           writeArray(a, schema);
/*  663 */           schema._pcdata("\n");
/*      */         } 
/*  665 */         for (Map.Entry<String, AttributePropertyInfo<T, C>> e : this.attributeDecls.entrySet()) {
/*  666 */           TopLevelAttribute a = schema.attribute();
/*  667 */           a.name(e.getKey());
/*  668 */           if (e.getValue() == null) {
/*  669 */             writeTypeRef((TypeHost)a, XmlSchemaGenerator.this.stringType, "type");
/*      */           } else {
/*  671 */             writeAttributeTypeRef(e.getValue(), (AttributeType)a);
/*  672 */           }  schema._pcdata("\n");
/*      */         } 
/*      */ 
/*      */         
/*  676 */         schema.commit();
/*  677 */       } catch (TxwException e) {
/*  678 */         XmlSchemaGenerator.logger.log(Level.INFO, e.getMessage(), (Throwable)e);
/*  679 */         throw new IOException(e.getMessage());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeTypeRef(TypeHost th, NonElementRef<T, C> typeRef, String refAttName) {
/*  696 */       switch (typeRef.getSource().id()) {
/*      */         case LAX:
/*  698 */           th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "ID"));
/*      */           return;
/*      */         case SKIP:
/*  701 */           th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
/*      */           return;
/*      */         
/*      */         case STRICT:
/*      */           break;
/*      */         default:
/*  707 */           throw new IllegalStateException();
/*      */       } 
/*      */ 
/*      */       
/*  711 */       MimeType mimeType = typeRef.getSource().getExpectedMimeType();
/*  712 */       if (mimeType != null) {
/*  713 */         th._attribute(new QName("http://www.w3.org/2005/05/xmlmime", "expectedContentTypes", "xmime"), mimeType.toString());
/*      */       }
/*      */ 
/*      */       
/*  717 */       if (XmlSchemaGenerator.this.generateSwaRefAdapter(typeRef)) {
/*  718 */         th._attribute(refAttName, new QName("http://ws-i.org/profiles/basic/1.1/xsd", "swaRef", "ref"));
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  723 */       if (typeRef.getSource().getSchemaType() != null) {
/*  724 */         th._attribute(refAttName, typeRef.getSource().getSchemaType());
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  730 */       writeTypeRef(th, typeRef.getTarget(), refAttName);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeTypeRef(TypeHost th, NonElement<T, C> type, String refAttName) {
/*  746 */       if (type.getTypeName() == null) {
/*      */         
/*  748 */         th.block();
/*  749 */         if (type instanceof ClassInfo) {
/*  750 */           if (XmlSchemaGenerator.this.collisionChecker.push(type)) {
/*  751 */             XmlSchemaGenerator.this.errorListener.error(new SAXParseException(Messages.ANONYMOUS_TYPE_CYCLE.format(new Object[] { XmlSchemaGenerator.access$1500(this.this$0).getCycleString() }, ), null));
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  756 */             writeClass((ClassInfo<T, C>)type, th);
/*      */           } 
/*  758 */           XmlSchemaGenerator.this.collisionChecker.pop();
/*      */         } else {
/*  760 */           writeEnum((EnumLeafInfo<T, C>)type, (SimpleTypeHost)th);
/*      */         } 
/*      */       } else {
/*  763 */         th._attribute(refAttName, type.getTypeName());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeArray(ArrayInfo<T, C> a, Schema schema) {
/*  771 */       ComplexType ct = schema.complexType().name(a.getTypeName().getLocalPart());
/*  772 */       ct._final("#all");
/*  773 */       LocalElement le = ct.sequence().element().name("item");
/*  774 */       le.type(a.getItemType().getTypeName());
/*  775 */       le.minOccurs(0).maxOccurs("unbounded");
/*  776 */       le.nillable(true);
/*  777 */       ct.commit();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeEnum(EnumLeafInfo<T, C> e, SimpleTypeHost th) {
/*  784 */       SimpleType st = th.simpleType();
/*  785 */       writeName((NonElement<T, C>)e, (TypedXmlWriter)st);
/*      */       
/*  787 */       SimpleRestriction simpleRestriction = st.restriction();
/*  788 */       writeTypeRef((TypeHost)simpleRestriction, e.getBaseType(), "base");
/*      */       
/*  790 */       for (EnumConstant c : e.getConstants()) {
/*  791 */         simpleRestriction.enumeration().value(c.getLexicalValue());
/*      */       }
/*  793 */       st.commit();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeClass(ClassInfo<T, C> c, TypeHost parent) {
/*      */       ComplexExtension complexExtension1, complexExtension2;
/*  804 */       if (containsValueProp(c)) {
/*  805 */         if (c.getProperties().size() == 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  811 */           ValuePropertyInfo<T, C> vp = c.getProperties().get(0);
/*  812 */           SimpleType st = ((SimpleTypeHost)parent).simpleType();
/*  813 */           writeName((NonElement<T, C>)c, (TypedXmlWriter)st);
/*  814 */           if (vp.isCollection()) {
/*  815 */             writeTypeRef((TypeHost)st.list(), vp.getTarget(), "itemType");
/*      */           } else {
/*  817 */             writeTypeRef((TypeHost)st.restriction(), vp.getTarget(), "base");
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  833 */         ComplexType complexType = ((ComplexTypeHost)parent).complexType();
/*  834 */         writeName((NonElement<T, C>)c, (TypedXmlWriter)complexType);
/*  835 */         if (c.isFinal()) {
/*  836 */           complexType._final("extension restriction");
/*      */         }
/*  838 */         SimpleExtension se = complexType.simpleContent().extension();
/*  839 */         se.block();
/*  840 */         for (PropertyInfo<T, C> p : (Iterable<PropertyInfo<T, C>>)c.getProperties()) {
/*  841 */           ValuePropertyInfo vp; switch (p.kind()) {
/*      */             case LAX:
/*  843 */               handleAttributeProp((AttributePropertyInfo<T, C>)p, (AttrDecls)se);
/*      */               continue;
/*      */             case SKIP:
/*  846 */               TODO.checkSpec("what if vp.isCollection() == true?");
/*  847 */               vp = (ValuePropertyInfo)p;
/*  848 */               se.base(vp.getTarget().getTypeName());
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/*      */           assert false;
/*  854 */           throw new IllegalStateException();
/*      */         } 
/*      */         
/*  857 */         se.commit();
/*      */         
/*  859 */         TODO.schemaGenerator("figure out what to do if bc != null");
/*  860 */         TODO.checkSpec("handle sec 8.9.5.2, bullet #4");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  870 */       ComplexType ct = ((ComplexTypeHost)parent).complexType();
/*  871 */       writeName((NonElement<T, C>)c, (TypedXmlWriter)ct);
/*  872 */       if (c.isFinal())
/*  873 */         ct._final("extension restriction"); 
/*  874 */       if (c.isAbstract()) {
/*  875 */         ct._abstract(true);
/*      */       }
/*      */       
/*  878 */       ComplexType complexType1 = ct;
/*  879 */       ComplexType complexType2 = ct;
/*      */ 
/*      */       
/*  882 */       ClassInfo<T, C> bc = c.getBaseClass();
/*  883 */       if (bc != null) {
/*  884 */         if (bc.hasValueProperty()) {
/*      */           
/*  886 */           SimpleExtension se = ct.simpleContent().extension();
/*  887 */           SimpleExtension simpleExtension1 = se;
/*  888 */           complexType2 = null;
/*  889 */           se.base(bc.getTypeName());
/*      */         } else {
/*  891 */           ComplexExtension ce = ct.complexContent().extension();
/*  892 */           complexExtension1 = ce;
/*  893 */           complexExtension2 = ce;
/*      */           
/*  895 */           ce.base(bc.getTypeName());
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  900 */       if (complexExtension2 != null) {
/*      */         
/*  902 */         ArrayList<Tree> children = new ArrayList<Tree>();
/*  903 */         for (PropertyInfo<T, C> p : (Iterable<PropertyInfo<T, C>>)c.getProperties()) {
/*      */           
/*  905 */           if (p instanceof ReferencePropertyInfo && ((ReferencePropertyInfo)p).isMixed()) {
/*  906 */             ct.mixed(true);
/*      */           }
/*  908 */           Tree t = buildPropertyContentModel(p);
/*  909 */           if (t != null) {
/*  910 */             children.add(t);
/*      */           }
/*      */         } 
/*  913 */         Tree top = Tree.makeGroup(c.isOrdered() ? GroupKind.SEQUENCE : GroupKind.ALL, children);
/*      */ 
/*      */         
/*  916 */         top.write((TypeDefParticle)complexExtension2);
/*      */       } 
/*      */ 
/*      */       
/*  920 */       for (PropertyInfo<T, C> p : (Iterable<PropertyInfo<T, C>>)c.getProperties()) {
/*  921 */         if (p instanceof AttributePropertyInfo) {
/*  922 */           handleAttributeProp((AttributePropertyInfo<T, C>)p, (AttrDecls)complexExtension1);
/*      */         }
/*      */       } 
/*  925 */       if (c.hasAttributeWildcard()) {
/*  926 */         complexExtension1.anyAttribute().namespace("##other").processContents("skip");
/*      */       }
/*  928 */       ct.commit();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeName(NonElement<T, C> c, TypedXmlWriter xw) {
/*  935 */       QName tn = c.getTypeName();
/*  936 */       if (tn != null)
/*  937 */         xw._attribute("name", tn.getLocalPart()); 
/*      */     }
/*      */     
/*      */     private boolean containsValueProp(ClassInfo<T, C> c) {
/*  941 */       for (PropertyInfo p : c.getProperties()) {
/*  942 */         if (p instanceof ValuePropertyInfo) return true; 
/*      */       } 
/*  944 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Tree buildPropertyContentModel(PropertyInfo<T, C> p) {
/*  951 */       switch (p.kind()) {
/*      */         case STRICT:
/*  953 */           return handleElementProp((ElementPropertyInfo<T, C>)p);
/*      */         
/*      */         case LAX:
/*  956 */           return null;
/*      */         case null:
/*  958 */           return handleReferenceProp((ReferencePropertyInfo<T, C>)p);
/*      */         case null:
/*  960 */           return handleMapProp((MapPropertyInfo<T, C>)p);
/*      */         
/*      */         case SKIP:
/*      */           assert false;
/*  964 */           throw new IllegalStateException();
/*      */       } 
/*      */       assert false;
/*  967 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Tree handleElementProp(final ElementPropertyInfo<T, C> ep) {
/*  981 */       if (ep.isValueList()) {
/*  982 */         return new Tree.Term() {
/*      */             protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/*  984 */               TypeRef<T, C> t = ep.getTypes().get(0);
/*  985 */               LocalElement e = parent.element();
/*  986 */               e.block();
/*  987 */               QName tn = t.getTagName();
/*  988 */               e.name(tn.getLocalPart());
/*  989 */               List lst = e.simpleType().list();
/*  990 */               XmlSchemaGenerator.Namespace.this.writeTypeRef((TypeHost)lst, (NonElementRef<T, C>)t, "itemType");
/*  991 */               XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, tn);
/*  992 */               writeOccurs((Occurs)e, (isOptional || !ep.isRequired()), repeated);
/*      */             }
/*      */           };
/*      */       }
/*      */       
/*  997 */       ArrayList<Tree> children = new ArrayList<Tree>();
/*  998 */       for (TypeRef<T, C> t : (Iterable<TypeRef<T, C>>)ep.getTypes()) {
/*  999 */         children.add(new Tree.Term() {
/*      */               protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1001 */                 LocalElement e = parent.element();
/*      */                 
/* 1003 */                 QName tn = t.getTagName();
/*      */                 
/* 1005 */                 if (XmlSchemaGenerator.Namespace.this.canBeDirectElementRef(t, tn) || (!tn.getNamespaceURI().equals(XmlSchemaGenerator.Namespace.this.uri) && tn.getNamespaceURI().length() > 0)) {
/* 1006 */                   e.ref(tn);
/*      */                 } else {
/* 1008 */                   e.name(tn.getLocalPart());
/* 1009 */                   XmlSchemaGenerator.Namespace.this.writeTypeRef((TypeHost)e, (NonElementRef<T, C>)t, "type");
/* 1010 */                   XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, tn);
/*      */                 } 
/*      */                 
/* 1013 */                 if (t.isNillable()) {
/* 1014 */                   e.nillable(true);
/*      */                 }
/* 1016 */                 if (t.getDefaultValue() != null)
/* 1017 */                   e._default(t.getDefaultValue()); 
/* 1018 */                 writeOccurs((Occurs)e, isOptional, repeated);
/*      */               }
/*      */             });
/*      */       } 
/*      */       
/* 1023 */       final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeOptional(!ep.isRequired()).makeRepeated(ep.isCollection());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1028 */       final QName ename = ep.getXmlName();
/* 1029 */       if (ename != null) {
/* 1030 */         return new Tree.Term() {
/*      */             protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1032 */               LocalElement e = parent.element();
/* 1033 */               if (ename.getNamespaceURI().length() > 0 && 
/* 1034 */                 !ename.getNamespaceURI().equals(XmlSchemaGenerator.Namespace.this.uri)) {
/*      */ 
/*      */                 
/* 1037 */                 e.ref(new QName(ename.getNamespaceURI(), ename.getLocalPart()));
/*      */                 
/*      */                 return;
/*      */               } 
/* 1041 */               e.name(ename.getLocalPart());
/* 1042 */               XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, ename);
/*      */               
/* 1044 */               if (ep.isCollectionNillable()) {
/* 1045 */                 e.nillable(true);
/*      */               }
/* 1047 */               writeOccurs((Occurs)e, !ep.isCollectionRequired(), repeated);
/*      */               
/* 1049 */               ComplexType p = e.complexType();
/* 1050 */               choice.write((TypeDefParticle)p);
/*      */             }
/*      */           };
/*      */       }
/* 1054 */       return choice;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean canBeDirectElementRef(TypeRef<T, C> t, QName tn) {
/* 1065 */       if (t.isNillable() || t.getDefaultValue() != null)
/*      */       {
/* 1067 */         return false;
/*      */       }
/* 1069 */       if (t.getTarget() instanceof Element) {
/* 1070 */         Element te = (Element)t.getTarget();
/* 1071 */         QName targetTagName = te.getElementName();
/* 1072 */         return (targetTagName != null && targetTagName.equals(tn));
/*      */       } 
/*      */       
/* 1075 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void handleAttributeProp(AttributePropertyInfo<T, C> ap, AttrDecls attr) {
/* 1108 */       LocalAttribute localAttribute = attr.attribute();
/*      */       
/* 1110 */       String attrURI = ap.getXmlName().getNamespaceURI();
/* 1111 */       if (attrURI.equals("")) {
/* 1112 */         localAttribute.name(ap.getXmlName().getLocalPart());
/*      */         
/* 1114 */         writeAttributeTypeRef(ap, (AttributeType)localAttribute);
/*      */         
/* 1116 */         this.attributeFormDefault.writeForm(localAttribute, ap.getXmlName());
/*      */       } else {
/* 1118 */         localAttribute.ref(ap.getXmlName());
/*      */       } 
/*      */       
/* 1121 */       if (ap.isRequired())
/*      */       {
/* 1123 */         localAttribute.use("required");
/*      */       }
/*      */     }
/*      */     
/*      */     private void writeAttributeTypeRef(AttributePropertyInfo<T, C> ap, AttributeType a) {
/* 1128 */       if (ap.isCollection()) {
/* 1129 */         writeTypeRef((TypeHost)a.simpleType().list(), (NonElementRef<T, C>)ap, "itemType");
/*      */       } else {
/* 1131 */         writeTypeRef((TypeHost)a, (NonElementRef<T, C>)ap, "type");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Tree handleReferenceProp(final ReferencePropertyInfo<T, C> rp) {
/* 1143 */       ArrayList<Tree> children = new ArrayList<Tree>();
/*      */       
/* 1145 */       for (Element<T, C> e : (Iterable<Element<T, C>>)rp.getElements()) {
/* 1146 */         children.add(new Tree.Term() {
/*      */               protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1148 */                 LocalElement eref = parent.element();
/*      */                 
/* 1150 */                 boolean local = false;
/*      */                 
/* 1152 */                 QName en = e.getElementName();
/* 1153 */                 if (e.getScope() != null) {
/*      */                   
/* 1155 */                   boolean qualified = en.getNamespaceURI().equals(XmlSchemaGenerator.Namespace.this.uri);
/* 1156 */                   boolean unqualified = en.getNamespaceURI().equals("");
/* 1157 */                   if (qualified || unqualified) {
/*      */ 
/*      */ 
/*      */                     
/* 1161 */                     if (unqualified) {
/* 1162 */                       if (XmlSchemaGenerator.Namespace.this.elementFormDefault.isEffectivelyQualified) {
/* 1163 */                         eref.form("unqualified");
/*      */                       }
/* 1165 */                     } else if (!XmlSchemaGenerator.Namespace.this.elementFormDefault.isEffectivelyQualified) {
/* 1166 */                       eref.form("qualified");
/*      */                     } 
/*      */                     
/* 1169 */                     local = true;
/* 1170 */                     eref.name(en.getLocalPart());
/*      */ 
/*      */                     
/* 1173 */                     if (e instanceof ClassInfo) {
/* 1174 */                       XmlSchemaGenerator.Namespace.this.writeTypeRef((TypeHost)eref, (NonElement<T, C>)e, "type");
/*      */                     } else {
/* 1176 */                       XmlSchemaGenerator.Namespace.this.writeTypeRef((TypeHost)eref, ((ElementInfo)e).getContentType(), "type");
/*      */                     } 
/*      */                   } 
/*      */                 } 
/* 1180 */                 if (!local)
/* 1181 */                   eref.ref(en); 
/* 1182 */                 writeOccurs((Occurs)eref, isOptional, repeated);
/*      */               }
/*      */             });
/*      */       } 
/*      */       
/* 1187 */       final WildcardMode wc = rp.getWildcard();
/* 1188 */       if (wc != null) {
/* 1189 */         children.add(new Tree.Term() {
/*      */               protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1191 */                 Any any = parent.any();
/* 1192 */                 String pcmode = XmlSchemaGenerator.getProcessContentsModeName(wc);
/* 1193 */                 if (pcmode != null) any.processContents(pcmode); 
/* 1194 */                 any.namespace("##other");
/* 1195 */                 writeOccurs((Occurs)any, isOptional, repeated);
/*      */               }
/*      */             });
/*      */       }
/*      */ 
/*      */       
/* 1201 */       final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeRepeated(rp.isCollection()).makeOptional(rp.isCollection());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1206 */       final QName ename = rp.getXmlName();
/*      */       
/* 1208 */       if (ename != null) {
/* 1209 */         return new Tree.Term() {
/*      */             protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1211 */               LocalElement e = parent.element().name(ename.getLocalPart());
/* 1212 */               XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, ename);
/* 1213 */               if (rp.isCollectionNillable())
/* 1214 */                 e.nillable(true); 
/* 1215 */               writeOccurs((Occurs)e, true, repeated);
/*      */               
/* 1217 */               ComplexType p = e.complexType();
/* 1218 */               choice.write((TypeDefParticle)p);
/*      */             }
/*      */           };
/*      */       }
/* 1222 */       return choice;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Tree handleMapProp(final MapPropertyInfo<T, C> mp) {
/* 1233 */       return new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1235 */             QName ename = mp.getXmlName();
/*      */             
/* 1237 */             LocalElement e = parent.element();
/* 1238 */             XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, ename);
/* 1239 */             if (mp.isCollectionNillable()) {
/* 1240 */               e.nillable(true);
/*      */             }
/* 1242 */             e = e.name(ename.getLocalPart());
/* 1243 */             writeOccurs((Occurs)e, isOptional, repeated);
/* 1244 */             ComplexType p = e.complexType();
/*      */ 
/*      */ 
/*      */             
/* 1248 */             e = p.sequence().element();
/* 1249 */             e.name("entry").minOccurs(0).maxOccurs("unbounded");
/*      */             
/* 1251 */             ExplicitGroup seq = e.complexType().sequence();
/* 1252 */             XmlSchemaGenerator.Namespace.this.writeKeyOrValue(seq, "key", mp.getKeyType());
/* 1253 */             XmlSchemaGenerator.Namespace.this.writeKeyOrValue(seq, "value", mp.getValueType());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     private void writeKeyOrValue(ExplicitGroup seq, String tagName, NonElement<T, C> typeRef) {
/* 1259 */       LocalElement key = seq.element().name(tagName);
/* 1260 */       key.minOccurs(0);
/* 1261 */       writeTypeRef((TypeHost)key, typeRef, "type");
/*      */     }
/*      */     
/*      */     public void addGlobalAttribute(AttributePropertyInfo<T, C> ap) {
/* 1265 */       this.attributeDecls.put(ap.getXmlName().getLocalPart(), ap);
/* 1266 */       addDependencyTo(ap.getTarget().getTypeName());
/*      */     }
/*      */     
/*      */     public void addGlobalElement(TypeRef<T, C> tref) {
/* 1270 */       this.elementDecls.put(tref.getTagName().getLocalPart(), new ElementWithType(false, tref.getTarget()));
/* 1271 */       addDependencyTo(tref.getTarget().getTypeName());
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1275 */       StringBuilder buf = new StringBuilder();
/* 1276 */       buf.append("[classes=").append(this.classes);
/* 1277 */       buf.append(",elementDecls=").append(this.elementDecls);
/* 1278 */       buf.append(",enums=").append(this.enums);
/* 1279 */       buf.append("]");
/* 1280 */       return super.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract class ElementDeclaration
/*      */     {
/*      */       public abstract boolean equals(Object param2Object);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public abstract int hashCode();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public abstract void writeTo(String param2String, Schema param2Schema);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     class ElementWithType
/*      */       extends ElementDeclaration
/*      */     {
/*      */       private final boolean nillable;
/*      */ 
/*      */ 
/*      */       
/*      */       private final NonElement<T, C> type;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public ElementWithType(boolean nillable, NonElement<T, C> type) {
/* 1319 */         this.type = type;
/* 1320 */         this.nillable = nillable;
/*      */       }
/*      */       
/*      */       public void writeTo(String localName, Schema schema) {
/* 1324 */         TopLevelElement e = schema.element().name(localName);
/* 1325 */         if (this.nillable)
/* 1326 */           e.nillable(true); 
/* 1327 */         if (this.type != null) {
/* 1328 */           XmlSchemaGenerator.Namespace.this.writeTypeRef((TypeHost)e, this.type, "type");
/*      */         } else {
/* 1330 */           e.complexType();
/*      */         } 
/* 1332 */         e.commit();
/*      */       }
/*      */       
/*      */       public boolean equals(Object o) {
/* 1336 */         if (this == o) return true; 
/* 1337 */         if (o == null || getClass() != o.getClass()) return false;
/*      */         
/* 1339 */         ElementWithType that = (ElementWithType)o;
/* 1340 */         return this.type.equals(that.type);
/*      */       }
/*      */       
/*      */       public int hashCode() {
/* 1344 */         return this.type.hashCode(); } } } class ElementWithType extends Namespace.ElementDeclaration { private final boolean nillable; private final NonElement<T, C> type; public ElementWithType(boolean nillable, NonElement<T, C> type) { super((XmlSchemaGenerator.Namespace)XmlSchemaGenerator.this); this.type = type; this.nillable = nillable; } public void writeTo(String localName, Schema schema) { TopLevelElement e = schema.element().name(localName); if (this.nillable) e.nillable(true);  if (this.type != null) { this.this$1.writeTypeRef((TypeHost)e, this.type, "type"); } else { e.complexType(); }  e.commit(); } public int hashCode() { return this.type.hashCode(); }
/*      */      public boolean equals(Object o) {
/*      */       if (this == o)
/*      */         return true; 
/*      */       if (o == null || getClass() != o.getClass())
/*      */         return false; 
/*      */       ElementWithType that = (ElementWithType)o;
/*      */       return this.type.equals(that.type);
/*      */     } }
/*      */   private boolean generateSwaRefAdapter(NonElementRef<T, C> typeRef) {
/* 1354 */     return generateSwaRefAdapter(typeRef.getSource());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean generateSwaRefAdapter(PropertyInfo<T, C> prop) {
/* 1361 */     Adapter<T, C> adapter = prop.getAdapter();
/* 1362 */     if (adapter == null) return false; 
/* 1363 */     Object o = this.navigator.asDecl(SwaRefAdapter.class);
/* 1364 */     if (o == null) return false; 
/* 1365 */     return o.equals(adapter.adapterType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1372 */     StringBuilder buf = new StringBuilder();
/* 1373 */     for (Namespace ns : this.namespaces.values()) {
/* 1374 */       if (buf.length() > 0) buf.append(','); 
/* 1375 */       buf.append(ns.uri).append('=').append(ns);
/*      */     } 
/* 1377 */     return super.toString() + '[' + buf + ']';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getProcessContentsModeName(WildcardMode wc) {
/* 1386 */     switch (wc) {
/*      */       case LAX:
/*      */       case SKIP:
/* 1389 */         return wc.name().toLowerCase();
/*      */       case STRICT:
/* 1391 */         return null;
/*      */     } 
/* 1393 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String relativize(String uri, String baseUri) {
/*      */     try {
/* 1418 */       assert uri != null;
/*      */       
/* 1420 */       if (baseUri == null) return uri;
/*      */       
/* 1422 */       URI theUri = new URI(Util.escapeURI(uri));
/* 1423 */       URI theBaseUri = new URI(Util.escapeURI(baseUri));
/*      */       
/* 1425 */       if (theUri.isOpaque() || theBaseUri.isOpaque()) {
/* 1426 */         return uri;
/*      */       }
/* 1428 */       if (!Util.equalsIgnoreCase(theUri.getScheme(), theBaseUri.getScheme()) || !Util.equal(theUri.getAuthority(), theBaseUri.getAuthority()))
/*      */       {
/* 1430 */         return uri;
/*      */       }
/* 1432 */       String uriPath = theUri.getPath();
/* 1433 */       String basePath = theBaseUri.getPath();
/*      */ 
/*      */       
/* 1436 */       if (!basePath.endsWith("/")) {
/* 1437 */         basePath = Util.normalizeUriPath(basePath);
/*      */       }
/*      */       
/* 1440 */       if (uriPath.equals(basePath)) {
/* 1441 */         return ".";
/*      */       }
/* 1443 */       String relPath = calculateRelativePath(uriPath, basePath, fixNull(theUri.getScheme()).equals("file"));
/*      */       
/* 1445 */       if (relPath == null)
/* 1446 */         return uri; 
/* 1447 */       StringBuffer relUri = new StringBuffer();
/* 1448 */       relUri.append(relPath);
/* 1449 */       if (theUri.getQuery() != null)
/* 1450 */         relUri.append('?').append(theUri.getQuery()); 
/* 1451 */       if (theUri.getFragment() != null) {
/* 1452 */         relUri.append('#').append(theUri.getFragment());
/*      */       }
/* 1454 */       return relUri.toString();
/* 1455 */     } catch (URISyntaxException e) {
/* 1456 */       throw new InternalError("Error escaping one of these uris:\n\t" + uri + "\n\t" + baseUri);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String fixNull(String s) {
/* 1461 */     if (s == null) return ""; 
/* 1462 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String calculateRelativePath(String uri, String base, boolean fileUrl) {
/* 1468 */     boolean onWindows = (File.pathSeparatorChar == ';');
/*      */     
/* 1470 */     if (base == null) {
/* 1471 */       return null;
/*      */     }
/* 1473 */     if ((fileUrl && onWindows && startsWithIgnoreCase(uri, base)) || uri.startsWith(base)) {
/* 1474 */       return uri.substring(base.length());
/*      */     }
/* 1476 */     return "../" + calculateRelativePath(uri, Util.getParentUriPath(base), fileUrl);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean startsWithIgnoreCase(String s, String t) {
/* 1481 */     return s.toUpperCase().startsWith(t.toUpperCase());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1488 */   private static final Comparator<String> NAMESPACE_COMPARATOR = new Comparator<String>() {
/*      */       public int compare(String lhs, String rhs) {
/* 1490 */         return -lhs.compareTo(rhs);
/*      */       }
/*      */     };
/*      */   
/*      */   private static final String newline = "\n";
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\XmlSchemaGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */