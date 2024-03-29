/*     */ package com.sun.tools.xjc.reader.dtd;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import com.sun.tools.xjc.AbortException;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CDefaultValue;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.xjc.reader.ModelChecker;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIAttribute;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIInterface;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.dtdparser.DTDEventListener;
/*     */ import com.sun.xml.dtdparser.DTDHandlerBase;
/*     */ import com.sun.xml.dtdparser.DTDParser;
/*     */ import com.sun.xml.dtdparser.InputEntity;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public class TDTDReader
/*     */   extends DTDHandlerBase
/*     */ {
/*     */   private final EntityResolver entityResolver;
/*     */   final BindInfo bindInfo;
/*     */   final Model model;
/*     */   private final CodeModelClassFactory classFactory;
/*     */   private final ErrorReceiverFilter errorReceiver;
/*     */   private final Map<String, Element> elements;
/*     */   private final Stack<ModelGroup> modelGroups;
/*     */   private Locator locator;
/*     */   private static final Map<String, TypeUse> builtinConversions;
/*     */   
/*     */   public static Model parse(InputSource dtd, InputSource bindingInfo, ErrorReceiver errorReceiver, Options opts) {
/*     */     try {
/* 109 */       Ring old = Ring.begin();
/*     */       try {
/* 111 */         ErrorReceiverFilter ef = new ErrorReceiverFilter((ErrorListener)errorReceiver);
/*     */         
/* 113 */         JCodeModel cm = new JCodeModel();
/* 114 */         Model model = new Model(opts, cm, NameConverter.standard, opts.classNameAllocator, null);
/*     */         
/* 116 */         Ring.add(cm);
/* 117 */         Ring.add(model);
/* 118 */         Ring.add(ErrorReceiver.class, ef);
/*     */         
/* 120 */         TDTDReader reader = new TDTDReader((ErrorReceiver)ef, opts, bindingInfo);
/*     */         
/* 122 */         DTDParser parser = new DTDParser();
/* 123 */         parser.setDtdHandler((DTDEventListener)reader);
/* 124 */         if (opts.entityResolver != null) {
/* 125 */           parser.setEntityResolver(opts.entityResolver);
/*     */         }
/*     */         try {
/* 128 */           parser.parse(dtd);
/* 129 */         } catch (SAXParseException e) {
/* 130 */           return null;
/*     */         } 
/*     */         
/* 133 */         ((ModelChecker)Ring.get(ModelChecker.class)).check();
/*     */         
/* 135 */         if (ef.hadError()) return null; 
/* 136 */         return model;
/*     */       } finally {
/* 138 */         Ring.end(old);
/*     */       } 
/* 140 */     } catch (IOException e) {
/* 141 */       errorReceiver.error((SAXParseException)new SAXParseException2(e.getMessage(), null, e));
/* 142 */       return null;
/* 143 */     } catch (SAXException e) {
/* 144 */       errorReceiver.error((SAXParseException)new SAXParseException2(e.getMessage(), null, e));
/* 145 */       return null;
/* 146 */     } catch (AbortException e) {
/*     */       
/* 148 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TDTDReader(ErrorReceiver errorReceiver, Options opts, InputSource _bindInfo) throws AbortException {
/* 170 */     this.model = (Model)Ring.get(Model.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     this.elements = new HashMap<String, Element>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     this.modelGroups = new Stack<ModelGroup>(); this.entityResolver = opts.entityResolver; this.errorReceiver = new ErrorReceiverFilter((ErrorListener)errorReceiver); this.bindInfo = new BindInfo(this.model, _bindInfo, (ErrorReceiver)this.errorReceiver); this.classFactory = new CodeModelClassFactory(errorReceiver);
/*     */   } public void startDTD(InputEntity entity) throws SAXException {} public void endDTD() throws SAXException { for (Element e : this.elements.values()) e.bind();  if (this.errorReceiver.hadError()) return;  processInterfaceDeclarations(); this.model.serialVersionUID = this.bindInfo.getSerialVersionUID(); if (this.model.serialVersionUID != null) this.model.serializable = true;  this.model.rootClass = this.bindInfo.getSuperClass(); this.model.rootInterface = this.bindInfo.getSuperInterface(); processConstructorDeclarations(); } private void processInterfaceDeclarations() { Map<String, InterfaceAcceptor> fromName = new HashMap<String, InterfaceAcceptor>(); Map<BIInterface, JClass> decls = new HashMap<BIInterface, JClass>(); for (BIInterface decl : this.bindInfo.interfaces()) { final JDefinedClass intf = this.classFactory.createInterface((JClassContainer)this.bindInfo.getTargetPackage(), decl.name(), copyLocator()); decls.put(decl, intf); fromName.put(decl.name(), new InterfaceAcceptor() { public void implement(JClass c) { intf._implements(c); } }
/*     */         ); }  for (CClassInfo ci : this.model.beans().values()) { fromName.put(ci.getName(), new InterfaceAcceptor() { public void implement(JClass c) { ci._implements(c); } }); }  for (Map.Entry<BIInterface, JClass> e : decls.entrySet()) { BIInterface decl = e.getKey(); JClass c = e.getValue(); for (String member : decl.members()) { InterfaceAcceptor acc = fromName.get(member); if (acc == null) { error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentInterfaceMember", new Object[] { member }); } else { acc.implement(c); }  }  }  } private static interface InterfaceAcceptor {
/* 374 */     void implement(JClass param1JClass); } JPackage getTargetPackage() { return this.bindInfo.getTargetPackage(); } public void startModelGroup() throws SAXException { this.modelGroups.push(new ModelGroup()); }
/*     */   private void processConstructorDeclarations() { for (BIElement decl : this.bindInfo.elements()) { Element e = this.elements.get(decl.name()); if (e == null) { error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentElementDeclaration", new Object[] { decl.name() }); continue; }  if (!decl.isClass()) continue;  decl.declareConstructors(e.getClassInfo()); }  }
/*     */   public void attributeDecl(String elementName, String attributeName, String attributeType, String[] enumeration, short attributeUse, String defaultValue) throws SAXException { (getOrCreateElement(elementName)).attributes.add(createAttribute(elementName, attributeName, attributeType, enumeration, attributeUse, defaultValue)); }
/*     */   protected CPropertyInfo createAttribute(String elementName, String attributeName, String attributeType, String[] enums, short attributeUse, String defaultValue) throws SAXException { String propName; TypeUse use; boolean required = (attributeUse == 3); BIElement edecl = this.bindInfo.element(elementName); BIAttribute decl = null; if (edecl != null) decl = edecl.attribute(attributeName);  if (decl == null) { propName = this.model.getNameConverter().toPropertyName(attributeName); } else { propName = decl.getPropertyName(); }  QName qname = new QName("", attributeName); if (decl != null && decl.getConversion() != null) { use = decl.getConversion().getTransducer(); } else { use = builtinConversions.get(attributeType); }  CAttributePropertyInfo cAttributePropertyInfo = new CAttributePropertyInfo(propName, null, null, copyLocator(), qname, use, null, required); if (defaultValue != null) ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(use, new XmlString(defaultValue));  return (CPropertyInfo)cAttributePropertyInfo; }
/* 378 */   Element getOrCreateElement(String elementName) { Element r = this.elements.get(elementName); if (r == null) { r = new Element(this, elementName); this.elements.put(elementName, r); }  return r; } public void startContentModel(String elementName, short contentModelType) throws SAXException { assert this.modelGroups.isEmpty(); this.modelGroups.push(new ModelGroup()); } public void endContentModel(String elementName, short contentModelType) throws SAXException { assert this.modelGroups.size() == 1; Term term = ((ModelGroup)this.modelGroups.pop()).wrapUp(); Element e = getOrCreateElement(elementName); e.define(contentModelType, term, copyLocator()); } public void endModelGroup(short occurence) throws SAXException { Term t = Occurence.wrap(((ModelGroup)this.modelGroups.pop()).wrapUp(), occurence);
/* 379 */     ((ModelGroup)this.modelGroups.peek()).addTerm(t); }
/*     */ 
/*     */   
/*     */   public void connector(short connectorType) throws SAXException {
/* 383 */     ((ModelGroup)this.modelGroups.peek()).setKind(connectorType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void childElement(String elementName, short occurence) throws SAXException {
/* 390 */     Element child = getOrCreateElement(elementName);
/* 391 */     ((ModelGroup)this.modelGroups.peek()).addTerm(Occurence.wrap(child, occurence));
/* 392 */     child.isReferenced = true;
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
/*     */   public void setDocumentLocator(Locator loc) {
/* 408 */     this.locator = loc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator copyLocator() {
/* 415 */     return new LocatorImpl(this.locator);
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
/*     */   static {
/* 435 */     Map<String, TypeUse> m = new HashMap<String, TypeUse>();
/*     */     
/* 437 */     m.put("CDATA", CBuiltinLeafInfo.NORMALIZED_STRING);
/* 438 */     m.put("ENTITY", CBuiltinLeafInfo.TOKEN);
/* 439 */     m.put("ENTITIES", CBuiltinLeafInfo.STRING.makeCollection());
/* 440 */     m.put("NMTOKEN", CBuiltinLeafInfo.TOKEN);
/* 441 */     m.put("NMTOKENS", CBuiltinLeafInfo.STRING.makeCollection());
/* 442 */     m.put("ID", CBuiltinLeafInfo.ID);
/* 443 */     m.put("IDREF", CBuiltinLeafInfo.IDREF);
/* 444 */     m.put("IDREFS", TypeUseFactory.makeCollection(CBuiltinLeafInfo.IDREF));
/* 445 */     m.put("ENUMERATION", CBuiltinLeafInfo.TOKEN);
/*     */     
/* 447 */     builtinConversions = Collections.unmodifiableMap(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 457 */     this.errorReceiver.error(e);
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 461 */     this.errorReceiver.fatalError(e);
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 465 */     this.errorReceiver.warning(e);
/*     */   }
/*     */   
/*     */   protected final void error(Locator loc, String prop, Object... args) {
/* 469 */     this.errorReceiver.error(loc, Messages.format(prop, args));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\TDTDReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */