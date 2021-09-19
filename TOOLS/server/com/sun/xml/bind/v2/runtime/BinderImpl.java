/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.unmarshaller.InfosetScanner;
/*     */ import com.sun.xml.bind.v2.runtime.output.DOMOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.XmlOutput;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.InterningXmlVisitor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.SAXConnector;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XmlVisitor;
/*     */ import javax.xml.bind.Binder;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BinderImpl<XmlNode>
/*     */   extends Binder<XmlNode>
/*     */ {
/*     */   private final JAXBContextImpl context;
/*     */   private UnmarshallerImpl unmarshaller;
/*     */   private MarshallerImpl marshaller;
/*     */   private final InfosetScanner<XmlNode> scanner;
/*  91 */   private final AssociationMap<XmlNode> assoc = new AssociationMap<XmlNode>();
/*     */   
/*     */   BinderImpl(JAXBContextImpl _context, InfosetScanner<XmlNode> scanner) {
/*  94 */     this.context = _context;
/*  95 */     this.scanner = scanner;
/*     */   }
/*     */   
/*     */   private UnmarshallerImpl getUnmarshaller() {
/*  99 */     if (this.unmarshaller == null)
/* 100 */       this.unmarshaller = new UnmarshallerImpl(this.context, this.assoc); 
/* 101 */     return this.unmarshaller;
/*     */   }
/*     */   
/*     */   private MarshallerImpl getMarshaller() {
/* 105 */     if (this.marshaller == null)
/* 106 */       this.marshaller = new MarshallerImpl(this.context, this.assoc); 
/* 107 */     return this.marshaller;
/*     */   }
/*     */   
/*     */   public void marshal(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
/* 111 */     if (xmlNode == null || jaxbObject == null)
/* 112 */       throw new IllegalArgumentException(); 
/* 113 */     getMarshaller().marshal(jaxbObject, (XmlOutput)createOutput(xmlNode));
/*     */   }
/*     */ 
/*     */   
/*     */   private DOMOutput createOutput(XmlNode xmlNode) {
/* 118 */     return new DOMOutput((Node)xmlNode, this.assoc);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object updateJAXB(XmlNode xmlNode) throws JAXBException {
/* 123 */     return associativeUnmarshal(xmlNode, true, null);
/*     */   }
/*     */   
/*     */   public Object unmarshal(XmlNode xmlNode) throws JAXBException {
/* 127 */     return associativeUnmarshal(xmlNode, false, null);
/*     */   }
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(XmlNode xmlNode, Class<T> expectedType) throws JAXBException {
/* 131 */     if (expectedType == null) throw new IllegalArgumentException(); 
/* 132 */     return (JAXBElement<T>)associativeUnmarshal(xmlNode, true, expectedType);
/*     */   }
/*     */   
/*     */   public void setSchema(Schema schema) {
/* 136 */     getMarshaller().setSchema(schema);
/* 137 */     getUnmarshaller().setSchema(schema);
/*     */   }
/*     */   
/*     */   public Schema getSchema() {
/* 141 */     return getUnmarshaller().getSchema();
/*     */   }
/*     */   
/*     */   private Object associativeUnmarshal(XmlNode xmlNode, boolean inplace, Class<?> expectedType) throws JAXBException {
/* 145 */     if (xmlNode == null) {
/* 146 */       throw new IllegalArgumentException();
/*     */     }
/* 148 */     JaxBeanInfo<?> bi = null;
/* 149 */     if (expectedType != null) {
/* 150 */       bi = this.context.getBeanInfo(expectedType, true);
/*     */     }
/* 152 */     InterningXmlVisitor handler = new InterningXmlVisitor(getUnmarshaller().createUnmarshallerHandler(this.scanner, inplace, bi));
/*     */     
/* 154 */     this.scanner.setContentHandler((ContentHandler)new SAXConnector((XmlVisitor)handler, this.scanner.getLocator()));
/*     */     try {
/* 156 */       this.scanner.scan(xmlNode);
/* 157 */     } catch (SAXException e) {
/* 158 */       throw this.unmarshaller.createUnmarshalException(e);
/*     */     } 
/*     */     
/* 161 */     return handler.getContext().getResult();
/*     */   }
/*     */   
/*     */   public XmlNode getXMLNode(Object jaxbObject) {
/* 165 */     if (jaxbObject == null)
/* 166 */       throw new IllegalArgumentException(); 
/* 167 */     AssociationMap.Entry<XmlNode> e = this.assoc.byPeer(jaxbObject);
/* 168 */     if (e == null) return null; 
/* 169 */     return e.element();
/*     */   }
/*     */   
/*     */   public Object getJAXBNode(XmlNode xmlNode) {
/* 173 */     if (xmlNode == null)
/* 174 */       throw new IllegalArgumentException(); 
/* 175 */     AssociationMap.Entry<XmlNode> e = this.assoc.byElement(xmlNode);
/* 176 */     if (e == null) return null; 
/* 177 */     if (e.outer() != null) return e.outer(); 
/* 178 */     return e.inner();
/*     */   }
/*     */   
/*     */   public XmlNode updateXML(Object jaxbObject) throws JAXBException {
/* 182 */     return updateXML(jaxbObject, getXMLNode(jaxbObject));
/*     */   }
/*     */   
/*     */   public XmlNode updateXML(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
/* 186 */     if (jaxbObject == null || xmlNode == null) throw new IllegalArgumentException();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     Element e = (Element)xmlNode;
/* 192 */     Node ns = e.getNextSibling();
/* 193 */     Node p = e.getParentNode();
/* 194 */     p.removeChild(e);
/*     */ 
/*     */ 
/*     */     
/* 198 */     JaxBeanInfo bi = this.context.getBeanInfo(jaxbObject, true);
/* 199 */     if (!bi.isElement()) {
/* 200 */       jaxbObject = new JAXBElement(new QName(e.getNamespaceURI(), e.getLocalName()), bi.jaxbType, jaxbObject);
/*     */     }
/*     */     
/* 203 */     getMarshaller().marshal(jaxbObject, p);
/* 204 */     Node newNode = p.getLastChild();
/* 205 */     p.removeChild(newNode);
/* 206 */     p.insertBefore(newNode, ns);
/*     */     
/* 208 */     return (XmlNode)newNode;
/*     */   }
/*     */   
/*     */   public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
/* 212 */     getUnmarshaller().setEventHandler(handler);
/* 213 */     getMarshaller().setEventHandler(handler);
/*     */   }
/*     */   
/*     */   public ValidationEventHandler getEventHandler() {
/* 217 */     return getUnmarshaller().getEventHandler();
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) throws PropertyException {
/* 221 */     if (name == null) {
/* 222 */       throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
/*     */     }
/*     */     
/* 225 */     if (excludeProperty(name)) {
/* 226 */       throw new PropertyException(name);
/*     */     }
/*     */     
/* 229 */     Object prop = null;
/* 230 */     PropertyException pe = null;
/*     */     
/*     */     try {
/* 233 */       prop = getMarshaller().getProperty(name);
/* 234 */       return prop;
/* 235 */     } catch (PropertyException p) {
/* 236 */       pe = p;
/*     */ 
/*     */       
/*     */       try {
/* 240 */         prop = getUnmarshaller().getProperty(name);
/* 241 */         return prop;
/* 242 */       } catch (PropertyException propertyException) {
/* 243 */         pe = propertyException;
/*     */ 
/*     */         
/* 246 */         pe.setStackTrace(Thread.currentThread().getStackTrace());
/* 247 */         throw pe;
/*     */       } 
/*     */     } 
/*     */   } public void setProperty(String name, Object value) throws PropertyException {
/* 251 */     if (name == null) {
/* 252 */       throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
/*     */     }
/*     */     
/* 255 */     if (excludeProperty(name)) {
/* 256 */       throw new PropertyException(name, value);
/*     */     }
/*     */     
/* 259 */     PropertyException pe = null;
/*     */     
/*     */     try {
/* 262 */       getMarshaller().setProperty(name, value);
/*     */       return;
/* 264 */     } catch (PropertyException p) {
/* 265 */       pe = p;
/*     */ 
/*     */       
/*     */       try {
/* 269 */         getUnmarshaller().setProperty(name, value);
/*     */         return;
/* 271 */       } catch (PropertyException propertyException) {
/* 272 */         pe = propertyException;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 277 */         pe.setStackTrace(Thread.currentThread().getStackTrace());
/* 278 */         throw pe;
/*     */       } 
/*     */     } 
/*     */   } private boolean excludeProperty(String name) {
/* 282 */     return (name.equals("com.sun.xml.bind.characterEscapeHandler") || name.equals("com.sun.xml.bind.xmlDeclaration") || name.equals("com.sun.xml.bind.xmlHeaders"));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\BinderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */