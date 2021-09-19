/*     */ package org.seamless.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DOMParser<D extends DOM>
/*     */   implements ErrorHandler, EntityResolver
/*     */ {
/*  74 */   private static Logger log = Logger.getLogger(DOMParser.class.getName());
/*     */   
/*  76 */   public static final URL XML_SCHEMA_RESOURCE = Thread.currentThread().getContextClassLoader().getResource("org/seamless/schemas/xml.xsd");
/*     */   
/*     */   protected Source[] schemaSources;
/*     */   
/*     */   protected Schema schema;
/*     */   
/*     */   public DOMParser() {
/*  83 */     this(null);
/*     */   }
/*     */   
/*     */   public DOMParser(Source[] schemaSources) {
/*  87 */     this.schemaSources = schemaSources;
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema() {
/*  92 */     if (this.schema == null) {
/*     */       
/*     */       try {
/*     */         
/*  96 */         SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/*     */         
/*  98 */         schemaFactory.setResourceResolver(new CatalogResourceResolver(new HashMap<URI, URL>()
/*     */               {
/*     */               
/*     */               }));
/*     */ 
/*     */         
/* 104 */         if (this.schemaSources != null) {
/* 105 */           this.schema = schemaFactory.newSchema(this.schemaSources);
/*     */         } else {
/* 107 */           this.schema = schemaFactory.newSchema();
/*     */         }
/*     */       
/* 110 */       } catch (Exception ex) {
/* 111 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     }
/* 114 */     return this.schema;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract D createDOM(Document paramDocument);
/*     */ 
/*     */   
/*     */   public DocumentBuilderFactory createFactory(boolean validating) throws ParserException {
/* 122 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 127 */       factory.setNamespaceAware(true);
/*     */       
/* 129 */       if (validating)
/*     */       {
/*     */ 
/*     */         
/* 133 */         factory.setXIncludeAware(true);
/*     */ 
/*     */         
/* 136 */         factory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", false);
/* 137 */         factory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", false);
/*     */ 
/*     */ 
/*     */         
/* 141 */         factory.setSchema(getSchema());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         factory.setFeature("http://apache.org/xml/features/validation/dynamic", true);
/*     */       }
/*     */     
/* 155 */     } catch (ParserConfigurationException ex) {
/*     */       
/* 157 */       throw new ParserException(ex);
/*     */     } 
/* 159 */     return factory;
/*     */   }
/*     */   
/*     */   public Transformer createTransformer(String method, int indent, boolean standalone) throws ParserException {
/*     */     try {
/* 164 */       TransformerFactory transFactory = TransformerFactory.newInstance();
/*     */       
/* 166 */       if (indent > 0) {
/*     */         try {
/* 168 */           transFactory.setAttribute("indent-number", Integer.valueOf(indent));
/* 169 */         } catch (IllegalArgumentException ex) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 174 */       Transformer transformer = transFactory.newTransformer();
/* 175 */       transformer.setOutputProperty("omit-xml-declaration", standalone ? "no" : "yes");
/*     */ 
/*     */       
/* 178 */       if (standalone) {
/*     */         try {
/* 180 */           transformer.setOutputProperty("http://www.oracle.com/xml/is-standalone", "yes");
/* 181 */         } catch (IllegalArgumentException e) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 186 */       transformer.setOutputProperty("indent", (indent > 0) ? "yes" : "no");
/* 187 */       if (indent > 0)
/* 188 */         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent)); 
/* 189 */       transformer.setOutputProperty("method", method);
/*     */       
/* 191 */       return transformer;
/* 192 */     } catch (Exception ex) {
/* 193 */       throw new ParserException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public D createDocument() {
/*     */     try {
/* 199 */       return createDOM(createFactory(false).newDocumentBuilder().newDocument());
/* 200 */     } catch (Exception ex) {
/* 201 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public D parse(URL url) throws ParserException {
/* 208 */     return parse(url, true);
/*     */   }
/*     */   
/*     */   public D parse(String string) throws ParserException {
/* 212 */     return parse(string, true);
/*     */   }
/*     */   
/*     */   public D parse(File file) throws ParserException {
/* 216 */     return parse(file, true);
/*     */   }
/*     */   
/*     */   public D parse(InputStream stream) throws ParserException {
/* 220 */     return parse(stream, true);
/*     */   }
/*     */   
/*     */   public D parse(URL url, boolean validate) throws ParserException {
/* 224 */     if (url == null) throw new IllegalArgumentException("Can't parse null URL"); 
/*     */     try {
/* 226 */       return parse(url.openStream(), validate);
/* 227 */     } catch (Exception ex) {
/* 228 */       throw new ParserException("Parsing URL failed: " + url, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public D parse(String string, boolean validate) throws ParserException {
/* 233 */     if (string == null) throw new IllegalArgumentException("Can't parse null string"); 
/* 234 */     return parse(new InputSource(new StringReader(string)), validate);
/*     */   }
/*     */   
/*     */   public D parse(File file, boolean validate) throws ParserException {
/* 238 */     if (file == null) throw new IllegalArgumentException("Can't parse null file"); 
/*     */     try {
/* 240 */       return parse(file.toURI().toURL(), validate);
/* 241 */     } catch (Exception ex) {
/* 242 */       throw new ParserException("Parsing file failed: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public D parse(InputStream stream, boolean validate) throws ParserException {
/* 247 */     return parse(new InputSource(stream), validate);
/*     */   }
/*     */ 
/*     */   
/*     */   public D parse(InputSource source, boolean validate) throws ParserException {
/*     */     try {
/* 253 */       DocumentBuilder parser = createFactory(validate).newDocumentBuilder();
/*     */       
/* 255 */       parser.setEntityResolver(this);
/*     */       
/* 257 */       parser.setErrorHandler(this);
/*     */       
/* 259 */       Document dom = parser.parse(source);
/*     */       
/* 261 */       dom.normalizeDocument();
/*     */       
/* 263 */       return createDOM(dom);
/*     */     }
/* 265 */     catch (Exception ex) {
/* 266 */       throw unwrapException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(URL url) throws ParserException {
/* 273 */     if (url == null) throw new IllegalArgumentException("Can't validate null URL"); 
/* 274 */     log.fine("Validating XML of URL: " + url);
/* 275 */     validate(new StreamSource(url.toString()));
/*     */   }
/*     */   
/*     */   public void validate(String string) throws ParserException {
/* 279 */     if (string == null) throw new IllegalArgumentException("Can't validate null string"); 
/* 280 */     log.fine("Validating XML string characters: " + string.length());
/* 281 */     validate(new SAXSource(new InputSource(new StringReader(string))));
/*     */   }
/*     */   
/*     */   public void validate(Document document) throws ParserException {
/* 285 */     validate(new DOMSource(document));
/*     */   }
/*     */   
/*     */   public void validate(DOM dom) throws ParserException {
/* 289 */     validate(new DOMSource(dom.getW3CDocument()));
/*     */   }
/*     */   
/*     */   public void validate(Source source) throws ParserException {
/*     */     try {
/* 294 */       Validator validator = getSchema().newValidator();
/* 295 */       validator.setErrorHandler(this);
/* 296 */       validator.validate(source);
/* 297 */     } catch (Exception ex) {
/* 298 */       throw unwrapException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XPathFactory createXPathFactory() {
/* 305 */     return XPathFactory.newInstance();
/*     */   }
/*     */   
/*     */   public XPath createXPath(NamespaceContext nsContext) {
/* 309 */     XPath xpath = createXPathFactory().newXPath();
/* 310 */     xpath.setNamespaceContext(nsContext);
/* 311 */     return xpath;
/*     */   }
/*     */   
/*     */   public XPath createXPath(XPathFactory factory, NamespaceContext nsContext) {
/* 315 */     XPath xpath = factory.newXPath();
/* 316 */     xpath.setNamespaceContext(nsContext);
/* 317 */     return xpath;
/*     */   }
/*     */   
/*     */   public Object getXPathResult(DOM dom, XPath xpath, String expr, QName result) {
/* 321 */     return getXPathResult(dom.getW3CDocument(), xpath, expr, result);
/*     */   }
/*     */   
/*     */   public Object getXPathResult(DOMElement element, XPath xpath, String expr, QName result) {
/* 325 */     return getXPathResult(element.getW3CElement(), xpath, expr, result);
/*     */   }
/*     */   
/*     */   public Object getXPathResult(Node context, XPath xpath, String expr, QName result) {
/*     */     try {
/* 330 */       log.fine("Evaluating xpath query: " + expr);
/* 331 */       return xpath.evaluate(expr, context, result);
/* 332 */     } catch (Exception ex) {
/* 333 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String print(DOM dom) throws ParserException {
/* 340 */     return print(dom, 4, true);
/*     */   }
/*     */   
/*     */   public String print(DOM dom, int indent) throws ParserException {
/* 344 */     return print(dom, indent, true);
/*     */   }
/*     */   
/*     */   public String print(DOM dom, boolean standalone) throws ParserException {
/* 348 */     return print(dom, 4, standalone);
/*     */   }
/*     */   
/*     */   public String print(DOM dom, int indent, boolean standalone) throws ParserException {
/* 352 */     return print(dom.getW3CDocument(), indent, standalone);
/*     */   }
/*     */   
/*     */   public String print(Document document, int indent, boolean standalone) throws ParserException {
/* 356 */     removeIgnorableWSNodes(document.getDocumentElement());
/* 357 */     return print(new DOMSource(document.getDocumentElement()), indent, standalone);
/*     */   }
/*     */   
/*     */   public String print(String string, int indent, boolean standalone) throws ParserException {
/* 361 */     return print(new StreamSource(new StringReader(string)), indent, standalone);
/*     */   }
/*     */   
/*     */   public String print(Source source, int indent, boolean standalone) throws ParserException {
/*     */     try {
/* 366 */       Transformer transformer = createTransformer("xml", indent, standalone);
/* 367 */       transformer.setOutputProperty("encoding", "utf-8");
/*     */       
/* 369 */       StringWriter out = new StringWriter();
/* 370 */       transformer.transform(source, new StreamResult(out));
/* 371 */       out.flush();
/*     */       
/* 373 */       return out.toString();
/*     */     }
/* 375 */     catch (Exception e) {
/* 376 */       throw new ParserException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String printHTML(Document dom) throws ParserException {
/* 381 */     return printHTML(dom, 4, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String printHTML(Document dom, int indent, boolean standalone, boolean doctype) throws ParserException {
/* 389 */     dom = (Document)dom.cloneNode(true);
/*     */ 
/*     */ 
/*     */     
/* 393 */     accept(dom.getDocumentElement(), new NodeVisitor((short)4)
/*     */         {
/*     */           public void visit(Node node) {
/* 396 */             CDATASection cdata = (CDATASection)node;
/* 397 */             cdata.getParentNode().setTextContent(cdata.getData());
/*     */           }
/*     */         });
/*     */     
/* 401 */     removeIgnorableWSNodes(dom.getDocumentElement());
/*     */     
/*     */     try {
/* 404 */       Transformer transformer = createTransformer("html", indent, standalone);
/*     */       
/* 406 */       if (doctype) {
/* 407 */         transformer.setOutputProperty("doctype-public", "-//W3C//DTD HTML 4.01 Transitional//EN");
/* 408 */         transformer.setOutputProperty("doctype-system", "http://www.w3.org/TR/html4/loose.dtd");
/*     */       } 
/*     */       
/* 411 */       StringWriter out = new StringWriter();
/* 412 */       transformer.transform(new DOMSource(dom), new StreamResult(out));
/* 413 */       out.flush();
/* 414 */       String output = out.toString();
/*     */ 
/*     */       
/* 417 */       String meta = "\\s*<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
/* 418 */       output = output.replaceFirst(meta, "");
/*     */ 
/*     */       
/* 421 */       String xmlns = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
/* 422 */       output = output.replaceFirst(xmlns, "<html>");
/*     */       
/* 424 */       return output;
/*     */     }
/* 426 */     catch (Exception ex) {
/* 427 */       throw new ParserException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeIgnorableWSNodes(Element element) {
/* 432 */     Node nextNode = element.getFirstChild();
/*     */     
/* 434 */     while (nextNode != null) {
/* 435 */       Node child = nextNode;
/* 436 */       nextNode = child.getNextSibling();
/* 437 */       if (isIgnorableWSNode(child)) {
/* 438 */         element.removeChild(child); continue;
/* 439 */       }  if (child.getNodeType() == 1) {
/* 440 */         removeIgnorableWSNodes((Element)child);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnorableWSNode(Node node) {
/* 447 */     return (node.getNodeType() == 3 && node.getTextContent().matches("[\\t\\n\\x0B\\f\\r\\s]+"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 454 */     log.warning(e.toString());
/*     */   }
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 458 */     throw new SAXException(new ParserException(e));
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 462 */     throw new SAXException(new ParserException(e));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ParserException unwrapException(Exception ex) {
/* 467 */     if (ex.getCause() != null && ex.getCause() instanceof ParserException) {
/* 468 */       return (ParserException)ex.getCause();
/*     */     }
/* 470 */     return new ParserException(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/*     */     InputSource is;
/* 479 */     if (systemId.startsWith("file://")) {
/* 480 */       is = new InputSource(new FileInputStream(new File(URI.create(systemId))));
/*     */     } else {
/* 482 */       is = new InputSource(new ByteArrayInputStream(new byte[0]));
/*     */     } 
/* 484 */     is.setPublicId(publicId);
/* 485 */     is.setSystemId(systemId);
/* 486 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String string) {
/* 493 */     return escape(string, false, false);
/*     */   }
/*     */   
/*     */   public static String escape(String string, boolean convertNewlines, boolean convertSpaces) {
/* 497 */     if (string == null) return null; 
/* 498 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     
/* 501 */     for (int i = 0; i < string.length(); i++) {
/* 502 */       String entity = null;
/* 503 */       char c = string.charAt(i);
/* 504 */       switch (c) {
/*     */         case '<':
/* 506 */           entity = "&#60;";
/*     */           break;
/*     */         case '>':
/* 509 */           entity = "&#62;";
/*     */           break;
/*     */         case '&':
/* 512 */           entity = "&#38;";
/*     */           break;
/*     */         case '"':
/* 515 */           entity = "&#34;";
/*     */           break;
/*     */       } 
/* 518 */       if (entity != null) {
/* 519 */         sb.append(entity);
/*     */       } else {
/* 521 */         sb.append(c);
/*     */       } 
/*     */     } 
/* 524 */     String result = sb.toString();
/* 525 */     if (convertSpaces) {
/*     */       
/* 527 */       Matcher matcher = Pattern.compile("(\\n+)(\\s*)(.*)").matcher(result);
/* 528 */       StringBuffer temp = new StringBuffer();
/* 529 */       while (matcher.find()) {
/* 530 */         String group = matcher.group(2);
/* 531 */         StringBuilder spaces = new StringBuilder();
/* 532 */         for (int j = 0; j < group.length(); j++) {
/* 533 */           spaces.append("&#160;");
/*     */         }
/* 535 */         matcher.appendReplacement(temp, "$1" + spaces.toString() + "$3");
/*     */       } 
/* 537 */       matcher.appendTail(temp);
/* 538 */       result = temp.toString();
/*     */     } 
/* 540 */     if (convertNewlines) {
/* 541 */       result = result.replaceAll("\n", "<br/>");
/*     */     }
/* 543 */     return result;
/*     */   }
/*     */   
/*     */   public static String stripElements(String xml) {
/* 547 */     if (xml == null) return null; 
/* 548 */     return xml.replaceAll("<([a-zA-Z]|/).*?>", "");
/*     */   }
/*     */   
/*     */   public static void accept(Node node, NodeVisitor visitor) {
/* 552 */     if (node == null)
/* 553 */       return;  if (visitor.isHalted())
/* 554 */       return;  NodeList children = node.getChildNodes();
/* 555 */     for (int i = 0; i < children.getLength(); i++) {
/* 556 */       Node child = children.item(i);
/* 557 */       boolean cont = true;
/* 558 */       if (child.getNodeType() == visitor.nodeType) {
/* 559 */         visitor.visit(child);
/* 560 */         if (visitor.isHalted())
/*     */           break; 
/* 562 */       }  accept(child, visitor);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static abstract class NodeVisitor {
/*     */     private short nodeType;
/*     */     
/*     */     protected NodeVisitor(short nodeType) {
/* 570 */       assert nodeType < 12;
/* 571 */       this.nodeType = nodeType;
/*     */     }
/*     */     
/*     */     public boolean isHalted() {
/* 575 */       return false;
/*     */     }
/*     */     
/*     */     public abstract void visit(Node param1Node);
/*     */   }
/*     */   
/*     */   public static String wrap(String wrapperName, String fragment) {
/* 582 */     return wrap(wrapperName, null, fragment);
/*     */   }
/*     */   
/*     */   public static String wrap(String wrapperName, String xmlns, String fragment) {
/* 586 */     StringBuilder wrapper = new StringBuilder();
/* 587 */     wrapper.append("<").append(wrapperName);
/* 588 */     if (xmlns != null) {
/* 589 */       wrapper.append(" xmlns=\"").append(xmlns).append("\"");
/*     */     }
/* 591 */     wrapper.append(">");
/* 592 */     wrapper.append(fragment);
/* 593 */     wrapper.append("</").append(wrapperName).append(">");
/* 594 */     return wrapper.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\DOMParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */