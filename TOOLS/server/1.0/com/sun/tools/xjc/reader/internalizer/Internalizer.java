/*     */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.XPathAPI;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.Messages;
/*     */ import com.sun.tools.xjc.util.DOMUtils;
/*     */ import com.sun.tools.xjc.util.EditDistance;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ class Internalizer
/*     */ {
/*     */   private final DOMForest forest;
/*     */   private ErrorHandler errorHandler;
/*     */   private static final String EXTENSION_PREFIXES = "extensionBindingPrefixes";
/*     */   
/*     */   static void transform(DOMForest forest) throws SAXException {
/*  52 */     (new com.sun.tools.xjc.reader.internalizer.Internalizer(forest)).transform();
/*     */   }
/*     */ 
/*     */   
/*     */   private Internalizer(DOMForest forest) {
/*  57 */     this.errorHandler = forest.getErrorHandler();
/*  58 */     this.forest = forest;
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
/*     */   private void transform() throws SAXException {
/*  75 */     Map targetNodes = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     for (Iterator iterator1 = this.forest.outerMostBindings.iterator(); iterator1.hasNext(); ) {
/*  81 */       Element jaxbBindings = iterator1.next();
/*     */ 
/*     */       
/*  84 */       buildTargetNodeMap(jaxbBindings, jaxbBindings, targetNodes);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     for (Iterator itr = this.forest.outerMostBindings.iterator(); itr.hasNext(); ) {
/*  91 */       Element jaxbBindings = itr.next();
/*     */       
/*  93 */       move(jaxbBindings, targetNodes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate(Element bindings) throws SAXException {
/* 102 */     NamedNodeMap atts = bindings.getAttributes();
/* 103 */     for (int i = 0; i < atts.getLength(); i++) {
/* 104 */       Attr a = (Attr)atts.item(i);
/* 105 */       if (a.getNamespaceURI() == null)
/*     */       {
/* 107 */         if (!a.getLocalName().equals("node"))
/*     */         {
/* 109 */           if (a.getLocalName().equals("schemaLocation"));
/*     */         }
/*     */       }
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
/*     */   private void buildTargetNodeMap(Element bindings, Node inheritedTarget, Map result) throws SAXException {
/* 124 */     Node target = inheritedTarget;
/*     */     
/* 126 */     validate(bindings);
/*     */ 
/*     */     
/* 129 */     if (bindings.getAttributeNode("schemaLocation") != null) {
/* 130 */       String schemaLocation = bindings.getAttribute("schemaLocation");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 136 */         schemaLocation = (new URL(new URL(this.forest.getSystemId(bindings.getOwnerDocument())), schemaLocation)).toExternalForm();
/*     */       
/*     */       }
/* 139 */       catch (MalformedURLException e) {}
/*     */ 
/*     */ 
/*     */       
/* 143 */       target = this.forest.get(schemaLocation);
/* 144 */       if (target == null) {
/* 145 */         reportError(bindings, Messages.format("Internalizer.IncorrectSchemaReference", schemaLocation, EditDistance.findNearest(schemaLocation, this.forest.listSystemIDs())));
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (bindings.getAttributeNode("node") != null) {
/* 156 */       NodeList nlst; String nodeXPath = bindings.getAttribute("node");
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 161 */         nlst = XPathAPI.selectNodeList(target, nodeXPath, bindings);
/* 162 */       } catch (TransformerException e) {
/* 163 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluationError", e.getMessage()), e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 169 */       if (nlst.getLength() == 0) {
/* 170 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluatesToNoTarget", nodeXPath));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 176 */       if (nlst.getLength() != 1) {
/* 177 */         reportError(bindings, Messages.format("Internalizer.XPathEvaulatesToTooManyTargets", nodeXPath, new Integer(nlst.getLength())));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 183 */       Node rnode = nlst.item(0);
/* 184 */       if (!(rnode instanceof Element)) {
/* 185 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluatesToNonElement", nodeXPath));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 191 */       if (!this.forest.logic.checkIfValidTargetNode(this.forest, bindings, (Element)rnode)) {
/* 192 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluatesToNonSchemaElement", nodeXPath, rnode.getNodeName()));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 199 */       target = rnode;
/*     */     } 
/*     */ 
/*     */     
/* 203 */     result.put(bindings, target);
/*     */ 
/*     */     
/* 206 */     Element[] children = DOMUtils.getChildElements(bindings, "http://java.sun.com/xml/ns/jaxb", "bindings");
/* 207 */     for (int i = 0; i < children.length; i++) {
/* 208 */       buildTargetNodeMap(children[i], target, result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void move(Element bindings, Map targetNodes) throws SAXException {
/* 215 */     Node target = (Node)targetNodes.get(bindings);
/* 216 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 221 */     Element[] children = DOMUtils.getChildElements(bindings);
/* 222 */     for (int i = 0; i < children.length; i++) {
/* 223 */       Element item = children[i];
/*     */       
/* 225 */       if ("bindings".equals(item.getLocalName())) {
/*     */         
/* 227 */         move(item, targetNodes);
/*     */       } else {
/* 229 */         if (!(target instanceof Element)) {
/* 230 */           reportError(item, Messages.format("Internalizer.ContextNodeIsNotElement"));
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 235 */         if (!this.forest.logic.checkIfValidTargetNode(this.forest, item, (Element)target)) {
/* 236 */           reportError(item, Messages.format("Internalizer.OrphanedCustomization", item.getNodeName()));
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 241 */         moveUnder(item, (Element)target);
/*     */       } 
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
/*     */   private void moveUnder(Element decl, Element target) {
/* 257 */     Element realTarget = this.forest.logic.refineTarget(target);
/*     */     
/* 259 */     declExtensionNamespace(decl, target);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     Element p = decl;
/* 265 */     Set inscopes = new HashSet();
/*     */     while (true) {
/* 267 */       NamedNodeMap atts = p.getAttributes();
/* 268 */       for (int i = 0; i < atts.getLength(); i++) {
/* 269 */         Attr a = (Attr)atts.item(i);
/* 270 */         if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI())) {
/*     */           String prefix;
/* 272 */           if (a.getName().indexOf(':') == -1) { prefix = ""; }
/* 273 */           else { prefix = a.getLocalName(); }
/*     */           
/* 275 */           if (inscopes.add(prefix) && p != decl)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 281 */             decl.setAttributeNodeNS((Attr)a.cloneNode(true));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 286 */       if (p.getParentNode() instanceof org.w3c.dom.Document) {
/*     */         break;
/*     */       }
/* 289 */       p = (Element)p.getParentNode();
/*     */     } 
/*     */     
/* 292 */     if (!inscopes.contains(""))
/*     */     {
/*     */ 
/*     */       
/* 296 */       decl.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 301 */     if (realTarget.getOwnerDocument() != decl.getOwnerDocument()) {
/*     */       
/* 303 */       Element original = decl;
/* 304 */       decl = (Element)realTarget.getOwnerDocument().importNode(decl, true);
/*     */ 
/*     */       
/* 307 */       copyLocators(original, decl);
/*     */     } 
/*     */ 
/*     */     
/* 311 */     realTarget.appendChild(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void declExtensionNamespace(Element decl, Element target) {
/* 322 */     if (!"http://java.sun.com/xml/ns/jaxb".equals(decl.getNamespaceURI())) {
/* 323 */       declareExtensionNamespace(target, decl.getNamespaceURI());
/*     */     }
/* 325 */     NodeList lst = decl.getChildNodes();
/* 326 */     for (int i = 0; i < lst.getLength(); i++) {
/* 327 */       Node n = lst.item(i);
/* 328 */       if (n instanceof Element) {
/* 329 */         declExtensionNamespace((Element)n, target);
/*     */       }
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
/*     */   private void declareExtensionNamespace(Element target, String nsUri) {
/* 343 */     Element root = target.getOwnerDocument().getDocumentElement();
/* 344 */     Attr att = root.getAttributeNodeNS("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes");
/* 345 */     if (att == null) {
/* 346 */       String jaxbPrefix = allocatePrefix(root, "http://java.sun.com/xml/ns/jaxb");
/*     */       
/* 348 */       att = target.getOwnerDocument().createAttributeNS("http://java.sun.com/xml/ns/jaxb", jaxbPrefix + ":" + "extensionBindingPrefixes");
/*     */       
/* 350 */       root.setAttributeNodeNS(att);
/*     */     } 
/*     */     
/* 353 */     String prefix = allocatePrefix(root, nsUri);
/* 354 */     if (att.getValue().indexOf(prefix) == -1)
/*     */     {
/* 356 */       att.setValue(att.getValue() + " " + prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String allocatePrefix(Element e, String nsUri) {
/*     */     String prefix;
/* 368 */     NamedNodeMap atts = e.getAttributes();
/* 369 */     for (int i = 0; i < atts.getLength(); i++) {
/* 370 */       Attr a = (Attr)atts.item(i);
/* 371 */       if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI()) && 
/* 372 */         a.getName().indexOf(':') != -1)
/*     */       {
/* 374 */         if (a.getValue().equals(nsUri)) {
/* 375 */           return a.getLocalName();
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/*     */     while (true) {
/* 381 */       prefix = "p" + (int)(Math.random() * 1000000.0D) + "_";
/* 382 */       if (e.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", prefix) != null)
/*     */         continue;  break;
/*     */     } 
/* 385 */     e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, nsUri);
/* 386 */     return prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyLocators(Element src, Element dst) {
/* 395 */     this.forest.locatorTable.storeStartLocation(dst, this.forest.locatorTable.getStartLocation(src));
/*     */     
/* 397 */     this.forest.locatorTable.storeEndLocation(dst, this.forest.locatorTable.getEndLocation(src));
/*     */ 
/*     */ 
/*     */     
/* 401 */     Element[] srcChilds = DOMUtils.getChildElements(src);
/* 402 */     Element[] dstChilds = DOMUtils.getChildElements(dst);
/*     */     
/* 404 */     for (int i = 0; i < srcChilds.length; i++) {
/* 405 */       copyLocators(srcChilds[i], dstChilds[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg) throws SAXException {
/* 410 */     reportError(errorSource, formattedMsg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg, Exception nestedException) throws SAXException {
/* 416 */     SAXParseException e = new SAXParseException(formattedMsg, this.forest.locatorTable.getStartLocation(errorSource), nestedException);
/*     */ 
/*     */     
/* 419 */     this.errorHandler.error(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\Internalizer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */