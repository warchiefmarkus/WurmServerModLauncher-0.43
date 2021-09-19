/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.ClassType;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import com.sun.tools.xjc.AbortException;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.SchemaCache;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.model.CCustomizations;
/*     */ import com.sun.tools.xjc.model.CPluginCustomization;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.tools.xjc.util.ForkContentHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindInfo
/*     */ {
/*     */   protected final ErrorReceiver errorReceiver;
/*     */   final Model model;
/*     */   private final String defaultPackage;
/*     */   final JCodeModel codeModel;
/*     */   final CodeModelClassFactory classFactory;
/*     */   private final Element dom;
/*     */   private final Map<String, BIConversion> conversions;
/*     */   private final Map<String, BIElement> elements;
/*     */   private final Map<String, BIInterface> interfaces;
/*     */   private static final String XJC_NS = "http://java.sun.com/xml/ns/jaxb/xjc";
/*     */   
/*     */   public BindInfo(Model model, InputSource source, ErrorReceiver _errorReceiver) throws AbortException {
/*  89 */     this(model, parse(model, source, _errorReceiver), _errorReceiver);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindInfo(Model model, Document _dom, ErrorReceiver _errorReceiver) {
/* 144 */     this.conversions = new HashMap<String, BIConversion>();
/*     */ 
/*     */     
/* 147 */     this.elements = new HashMap<String, BIElement>();
/*     */ 
/*     */     
/* 150 */     this.interfaces = new HashMap<String, BIInterface>(); this.model = model; this.dom = _dom.getDocumentElement(); this.codeModel = model.codeModel; this.errorReceiver = _errorReceiver; this.classFactory = new CodeModelClassFactory(_errorReceiver); this.defaultPackage = model.options.defaultPackage; model.getCustomizations().addAll((Collection)getGlobalCustomizations()); for (Element ele : DOMUtil.getChildElements(this.dom, "element")) {
/*     */       BIElement e = new BIElement(this, ele); this.elements.put(e.name(), e);
/*     */     }  BIUserConversion.addBuiltinConversions(this, this.conversions); for (Element cnv : DOMUtil.getChildElements(this.dom, "conversion")) {
/*     */       BIConversion c = new BIUserConversion(this, cnv);
/*     */       this.conversions.put(c.name(), c);
/*     */     } 
/*     */     for (Element en : DOMUtil.getChildElements(this.dom, "enumeration")) {
/*     */       BIConversion c = BIEnumeration.create(en, this);
/*     */       this.conversions.put(c.name(), c);
/*     */     } 
/*     */     for (Element itf : DOMUtil.getChildElements(this.dom, "interface")) {
/*     */       BIInterface c = new BIInterface(itf);
/*     */       this.interfaces.put(c.name(), c);
/* 163 */     }  } public Long getSerialVersionUID() { Element serial = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "serializable");
/* 164 */     if (serial == null) return null;
/*     */     
/* 166 */     String v = DOMUtil.getAttribute(serial, "uid");
/* 167 */     if (v == null) v = "1"; 
/* 168 */     return new Long(v); }
/*     */ 
/*     */   
/*     */   public JClass getSuperClass() {
/*     */     JDefinedClass jDefinedClass;
/* 173 */     Element sc = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "superClass");
/* 174 */     if (sc == null) return null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 179 */       String v = DOMUtil.getAttribute(sc, "name");
/* 180 */       if (v == null) return null; 
/* 181 */       jDefinedClass = this.codeModel._class(v);
/* 182 */       jDefinedClass.hide();
/* 183 */     } catch (JClassAlreadyExistsException e) {
/* 184 */       jDefinedClass = e.getExistingClass();
/*     */     } 
/*     */     
/* 187 */     return (JClass)jDefinedClass;
/*     */   }
/*     */   
/*     */   public JClass getSuperInterface() {
/*     */     JDefinedClass jDefinedClass;
/* 192 */     Element sc = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "superInterface");
/* 193 */     if (sc == null) return null;
/*     */     
/* 195 */     String name = DOMUtil.getAttribute(sc, "name");
/* 196 */     if (name == null) return null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 201 */       jDefinedClass = this.codeModel._class(name, ClassType.INTERFACE);
/* 202 */       jDefinedClass.hide();
/* 203 */     } catch (JClassAlreadyExistsException e) {
/* 204 */       jDefinedClass = e.getExistingClass();
/*     */     } 
/*     */     
/* 207 */     return (JClass)jDefinedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage getTargetPackage() {
/*     */     String p;
/* 214 */     if (this.model.options.defaultPackage != null)
/*     */     {
/* 216 */       return this.codeModel._package(this.model.options.defaultPackage);
/*     */     }
/*     */     
/* 219 */     if (this.defaultPackage != null) {
/* 220 */       p = this.defaultPackage;
/*     */     } else {
/* 222 */       p = getOption("package", "");
/* 223 */     }  return this.codeModel._package(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIConversion conversion(String name) {
/* 233 */     BIConversion r = this.conversions.get(name);
/* 234 */     if (r == null)
/* 235 */       throw new AssertionError("undefined conversion name: this should be checked by the validator before we read it"); 
/* 236 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIElement element(String name) {
/* 247 */     return this.elements.get(name);
/*     */   }
/*     */   
/*     */   public Collection<BIElement> elements() {
/* 251 */     return this.elements.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<BIInterface> interfaces() {
/* 256 */     return this.interfaces.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CCustomizations getGlobalCustomizations() {
/* 263 */     CCustomizations r = null;
/* 264 */     for (Element e : DOMUtil.getChildElements(this.dom)) {
/* 265 */       if (!this.model.options.pluginURIs.contains(e.getNamespaceURI()))
/*     */         continue; 
/* 267 */       if (r == null)
/* 268 */         r = new CCustomizations(); 
/* 269 */       r.add(new CPluginCustomization(e, DOMLocator.getLocationInfo(e)));
/*     */     } 
/*     */     
/* 272 */     if (r == null) r = CCustomizations.EMPTY; 
/* 273 */     return new CCustomizations((Collection)r);
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
/*     */   private String getOption(String attName, String defaultValue) {
/* 288 */     Element opt = DOMUtil.getElement(this.dom, "options");
/* 289 */     if (opt != null) {
/* 290 */       String s = DOMUtil.getAttribute(opt, attName);
/* 291 */       if (s != null)
/* 292 */         return s; 
/*     */     } 
/* 294 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   private static SchemaCache bindingFileSchema = new SchemaCache(BindInfo.class.getResource("bindingfile.xsd"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Document parse(Model model, InputSource is, ErrorReceiver receiver) throws AbortException {
/*     */     try {
/* 308 */       ValidatorHandler validator = bindingFileSchema.newValidator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       SAXParserFactory pf = SAXParserFactory.newInstance();
/* 315 */       pf.setNamespaceAware(true);
/* 316 */       DOMBuilder builder = new DOMBuilder();
/*     */       
/* 318 */       ErrorReceiverFilter controller = new ErrorReceiverFilter((ErrorListener)receiver);
/* 319 */       validator.setErrorHandler((ErrorHandler)controller);
/* 320 */       XMLReader reader = pf.newSAXParser().getXMLReader();
/* 321 */       reader.setErrorHandler((ErrorHandler)controller);
/*     */       
/* 323 */       DTDExtensionBindingChecker checker = new DTDExtensionBindingChecker("", model.options, (ErrorHandler)controller);
/* 324 */       checker.setContentHandler(validator);
/*     */       
/* 326 */       reader.setContentHandler((ContentHandler)new ForkContentHandler((ContentHandler)checker, (ContentHandler)builder));
/*     */       
/* 328 */       reader.parse(is);
/*     */       
/* 330 */       if (controller.hadError()) throw new AbortException(); 
/* 331 */       return (Document)builder.getDOM();
/* 332 */     } catch (IOException e) {
/* 333 */       receiver.error((SAXParseException)new SAXParseException2(e.getMessage(), null, e));
/* 334 */     } catch (SAXException e) {
/* 335 */       receiver.error((SAXParseException)new SAXParseException2(e.getMessage(), null, e));
/* 336 */     } catch (ParserConfigurationException e) {
/* 337 */       receiver.error((SAXParseException)new SAXParseException2(e.getMessage(), null, e));
/*     */     } 
/*     */     
/* 340 */     throw new AbortException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BindInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */