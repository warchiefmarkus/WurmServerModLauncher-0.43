/*     */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.msv.reader.AbortException;
/*     */ import com.sun.msv.verifier.jarv.RELAXNGFactoryImpl;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIEnumeration;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIInterface;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.SAXContentHandlerEx;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Element;
/*     */ import org.dom4j.Namespace;
/*     */ import org.dom4j.QName;
/*     */ import org.iso_relax.verifier.VerifierConfigurationException;
/*     */ import org.iso_relax.verifier.VerifierFilter;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindInfo
/*     */ {
/*     */   protected final ErrorReceiver errorReceiver;
/*     */   private final Options options;
/*     */   private final String defaultPackage;
/*     */   final JCodeModel codeModel;
/*     */   final CodeModelClassFactory classFactory;
/*     */   final NameConverter nameConverter;
/*     */   private final Element dom;
/*     */   private final Map conversions;
/*     */   private final Map elements;
/*     */   private final Map interfaces;
/*     */   
/*     */   public BindInfo(InputSource source, ErrorReceiver _errorReceiver, JCodeModel _codeModel, Options opts) throws AbortException {
/*  58 */     this(parse(source, _errorReceiver), _errorReceiver, _codeModel, opts);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindInfo(Document _dom, ErrorReceiver _errorReceiver, JCodeModel _codeModel, Options opts) {
/* 131 */     this.conversions = new HashMap();
/*     */ 
/*     */     
/* 134 */     this.elements = new HashMap();
/*     */ 
/*     */     
/* 137 */     this.interfaces = new HashMap(); this.dom = _dom.getRootElement(); this.codeModel = _codeModel; this.options = opts; this.errorReceiver = _errorReceiver; this.classFactory = new CodeModelClassFactory(_errorReceiver); this.nameConverter = NameConverter.standard; this.defaultPackage = opts.defaultPackage; Iterator itr = this.dom.elementIterator("element"); while (itr.hasNext()) { BIElement e = new BIElement(this, itr.next()); this.elements.put(e.name(), e); }  BIUserConversion.addBuiltinConversions(this, this.conversions); itr = this.dom.elementIterator("conversion"); while (itr.hasNext()) { BIUserConversion bIUserConversion = new BIUserConversion(this, itr.next()); this.conversions.put(bIUserConversion.name(), bIUserConversion); }
/*     */      itr = this.dom.elementIterator("enumeration"); while (itr.hasNext()) { BIEnumeration bIEnumeration = BIEnumeration.create(itr.next(), this); this.conversions.put(bIEnumeration.name(), bIEnumeration); }
/*     */      itr = this.dom.elementIterator("interface"); while (itr.hasNext()) { BIInterface c = new BIInterface(itr.next()); this.interfaces.put(c.name(), c); }
/*     */      this.options.generateMarshallingCode = (this.dom.element(new QName("noMarshaller", XJC_NS)) == null); this.options.generateUnmarshallingCode = (this.dom.element(new QName("noUnmarshaller", XJC_NS)) == null); this.options.generateValidationCode = (this.dom.element(new QName("noValidator", XJC_NS)) == null); this.options.generateValidatingUnmarshallingCode = (this.dom.element(new QName("noValidatingUnmarshaller", XJC_NS)) == null); if (!this.options.generateUnmarshallingCode)
/* 141 */       this.options.generateValidatingUnmarshallingCode = false;  } private static final Namespace XJC_NS = Namespace.get("http://java.sun.com/xml/ns/jaxb/xjc");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getSerialVersionUID() {
/* 150 */     Element serial = this.dom.element(new QName("serializable", XJC_NS));
/* 151 */     if (serial == null) return null;
/*     */     
/* 153 */     return new Long(serial.attributeValue("uid", "1"));
/*     */   }
/*     */   
/*     */   public JClass getSuperClass() {
/*     */     JDefinedClass c;
/* 158 */     Element sc = this.dom.element(new QName("superClass", XJC_NS));
/* 159 */     if (sc == null) return null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 164 */       c = this.codeModel._class(sc.attributeValue("name", "java.lang.Object"));
/* 165 */       c.hide();
/* 166 */     } catch (JClassAlreadyExistsException e) {
/* 167 */       c = e.getExistingClass();
/*     */     } 
/*     */     
/* 170 */     return (JClass)c;
/*     */   }
/*     */ 
/*     */   
/*     */   public JPackage getTargetPackage() {
/*     */     String p;
/* 176 */     if (this.defaultPackage != null) {
/* 177 */       p = this.defaultPackage;
/*     */     } else {
/* 179 */       p = getOption("package", "");
/* 180 */     }  return this.codeModel._package(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIConversion conversion(String name) {
/* 190 */     BIConversion r = (BIConversion)this.conversions.get(name);
/* 191 */     if (r == null)
/* 192 */       throw new JAXBAssertionError("undefined conversion name: this should be checked by the validator before we read it"); 
/* 193 */     return r;
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
/* 204 */     return (BIElement)this.elements.get(name);
/*     */   }
/*     */   
/*     */   public Iterator elements() {
/* 208 */     return this.elements.values().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator interfaces() {
/* 213 */     return this.interfaces.values().iterator();
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
/*     */   private String getOption(String attName, String defaultValue) {
/* 227 */     Element opt = this.dom.element("options");
/* 228 */     if (opt != null) {
/* 229 */       String s = opt.attributeValue(attName);
/* 230 */       if (s != null)
/* 231 */         return s; 
/*     */     } 
/* 233 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Document parse(InputSource is, ErrorReceiver receiver) throws AbortException {
/*     */     try {
/* 244 */       RELAXNGFactoryImpl rELAXNGFactoryImpl = new RELAXNGFactoryImpl();
/* 245 */       VerifierFilter verifier = rELAXNGFactoryImpl.newVerifier(com.sun.tools.xjc.reader.dtd.bindinfo.BindInfo.class.getResourceAsStream("bindingfile.rng")).getVerifierFilter();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 250 */       SAXParserFactory pf = SAXParserFactory.newInstance();
/* 251 */       pf.setNamespaceAware(true);
/* 252 */       SAXContentHandlerEx builder = SAXContentHandlerEx.create();
/*     */       
/* 254 */       ErrorReceiverFilter controller = new ErrorReceiverFilter(receiver);
/* 255 */       verifier.setContentHandler((ContentHandler)builder);
/* 256 */       verifier.setErrorHandler((ErrorHandler)controller);
/* 257 */       verifier.setParent(pf.newSAXParser().getXMLReader());
/* 258 */       verifier.parse(is);
/*     */       
/* 260 */       if (controller.hadError()) throw AbortException.theInstance; 
/* 261 */       return builder.getDocument();
/* 262 */     } catch (IOException e) {
/* 263 */       receiver.error(new SAXParseException(e.getMessage(), null, e));
/* 264 */     } catch (SAXException e) {
/* 265 */       receiver.error(new SAXParseException(e.getMessage(), null, e));
/* 266 */     } catch (VerifierConfigurationException ve) {
/* 267 */       ve.printStackTrace();
/* 268 */     } catch (ParserConfigurationException e) {
/* 269 */       receiver.error(new SAXParseException(e.getMessage(), null, e));
/*     */     } 
/*     */     
/* 272 */     throw AbortException.theInstance;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BindInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */