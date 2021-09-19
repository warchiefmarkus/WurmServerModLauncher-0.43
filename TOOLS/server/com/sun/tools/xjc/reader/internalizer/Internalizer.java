/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.util.DOMUtils;
/*     */ import com.sun.xml.bind.v2.util.EditDistance;
/*     */ import com.sun.xml.xsom.SCD;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  81 */   private static final XPathFactory xpf = XPathFactory.newInstance();
/*     */   
/*  83 */   private final XPath xpath = xpf.newXPath();
/*     */ 
/*     */   
/*     */   private final DOMForest forest;
/*     */   
/*     */   private ErrorReceiver errorHandler;
/*     */   
/*     */   private boolean enableSCD;
/*     */   
/*     */   private static final String EXTENSION_PREFIXES = "extensionBindingPrefixes";
/*     */ 
/*     */   
/*     */   static SCDBasedBindingSet transform(DOMForest forest, boolean enableSCD) {
/*  96 */     return (new Internalizer(forest, enableSCD)).transform();
/*     */   }
/*     */ 
/*     */   
/*     */   private Internalizer(DOMForest forest, boolean enableSCD) {
/* 101 */     this.errorHandler = forest.getErrorHandler();
/* 102 */     this.forest = forest;
/* 103 */     this.enableSCD = enableSCD;
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
/*     */   private SCDBasedBindingSet transform() {
/* 125 */     Map<Element, Node> targetNodes = new HashMap<Element, Node>();
/*     */     
/* 127 */     SCDBasedBindingSet scd = new SCDBasedBindingSet(this.forest);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     for (Element jaxbBindings : this.forest.outerMostBindings)
/*     */     {
/* 134 */       buildTargetNodeMap(jaxbBindings, jaxbBindings, null, targetNodes, scd);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     for (Element jaxbBindings : this.forest.outerMostBindings) {
/* 141 */       move(jaxbBindings, targetNodes);
/*     */     }
/*     */     
/* 144 */     return scd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate(Element bindings) {
/* 151 */     NamedNodeMap atts = bindings.getAttributes();
/* 152 */     for (int i = 0; i < atts.getLength(); i++) {
/* 153 */       Attr a = (Attr)atts.item(i);
/* 154 */       if (a.getNamespaceURI() == null)
/*     */       {
/* 156 */         if (!a.getLocalName().equals("node"))
/*     */         {
/* 158 */           if (!a.getLocalName().equals("schemaLocation"))
/*     */           {
/* 160 */             if (a.getLocalName().equals("scd"));
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildTargetNodeMap(Element bindings, @NotNull Node inheritedTarget, @Nullable SCDBasedBindingSet.Target inheritedSCD, Map<Element, Node> result, SCDBasedBindingSet scdResult) {
/* 184 */     Node target = inheritedTarget;
/*     */     
/* 186 */     validate(bindings);
/*     */ 
/*     */     
/* 189 */     if (bindings.getAttributeNode("schemaLocation") != null) {
/* 190 */       String schemaLocation = bindings.getAttribute("schemaLocation");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 196 */         schemaLocation = (new URL(new URL(this.forest.getSystemId(bindings.getOwnerDocument())), schemaLocation)).toExternalForm();
/*     */       
/*     */       }
/* 199 */       catch (MalformedURLException e) {}
/*     */ 
/*     */ 
/*     */       
/* 203 */       target = this.forest.get(schemaLocation);
/* 204 */       if (target == null) {
/* 205 */         reportError(bindings, Messages.format("Internalizer.IncorrectSchemaReference", new Object[] { schemaLocation, EditDistance.findNearest(schemaLocation, this.forest.listSystemIDs()) }));
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 213 */       target = ((Document)target).getDocumentElement();
/*     */     } 
/*     */ 
/*     */     
/* 217 */     if (bindings.getAttributeNode("node") != null) {
/* 218 */       NodeList nlst; String nodeXPath = bindings.getAttribute("node");
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 223 */         this.xpath.setNamespaceContext(new NamespaceContextImpl(bindings));
/* 224 */         nlst = (NodeList)this.xpath.evaluate(nodeXPath, target, XPathConstants.NODESET);
/* 225 */       } catch (XPathExpressionException e) {
/* 226 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluationError", new Object[] { e.getMessage() }), e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 231 */       if (nlst.getLength() == 0) {
/* 232 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluatesToNoTarget", new Object[] { nodeXPath }));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 237 */       if (nlst.getLength() != 1) {
/* 238 */         reportError(bindings, Messages.format("Internalizer.XPathEvaulatesToTooManyTargets", new Object[] { nodeXPath, Integer.valueOf(nlst.getLength()) }));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 243 */       Node rnode = nlst.item(0);
/* 244 */       if (!(rnode instanceof Element)) {
/* 245 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluatesToNonElement", new Object[] { nodeXPath }));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 250 */       if (!this.forest.logic.checkIfValidTargetNode(this.forest, bindings, (Element)rnode)) {
/* 251 */         reportError(bindings, Messages.format("Internalizer.XPathEvaluatesToNonSchemaElement", new Object[] { nodeXPath, rnode.getNodeName() }));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 257 */       target = rnode;
/*     */     } 
/*     */ 
/*     */     
/* 261 */     if (bindings.getAttributeNode("scd") != null) {
/* 262 */       String scdPath = bindings.getAttribute("scd");
/* 263 */       if (!this.enableSCD) {
/*     */ 
/*     */         
/* 266 */         reportError(bindings, Messages.format("SCD_NOT_ENABLED", new Object[0]));
/*     */         
/* 268 */         this.enableSCD = true;
/*     */       } 
/*     */       
/*     */       try {
/* 272 */         inheritedSCD = scdResult.createNewTarget(inheritedSCD, bindings, SCD.create(scdPath, new NamespaceContextImpl(bindings)));
/*     */       }
/* 274 */       catch (ParseException e) {
/* 275 */         reportError(bindings, Messages.format("ERR_SCD_EVAL", new Object[] { e.getMessage() }), e);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 281 */     if (inheritedSCD != null) {
/* 282 */       inheritedSCD.addBinidng(bindings);
/*     */     } else {
/* 284 */       result.put(bindings, target);
/*     */     } 
/*     */     
/* 287 */     Element[] children = DOMUtils.getChildElements(bindings, "http://java.sun.com/xml/ns/jaxb", "bindings");
/* 288 */     for (Element value : children) {
/* 289 */       buildTargetNodeMap(value, target, inheritedSCD, result, scdResult);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void move(Element bindings, Map<Element, Node> targetNodes) {
/* 296 */     Node target = targetNodes.get(bindings);
/* 297 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 302 */     for (Element item : DOMUtils.getChildElements(bindings)) {
/* 303 */       String localName = item.getLocalName();
/*     */       
/* 305 */       if ("bindings".equals(localName)) {
/*     */         
/* 307 */         move(item, targetNodes);
/*     */       }
/* 309 */       else if ("globalBindings".equals(localName)) {
/*     */         
/* 311 */         moveUnder(item, this.forest.getOneDocument().getDocumentElement());
/*     */       } else {
/* 313 */         if (!(target instanceof Element)) {
/* 314 */           reportError(item, Messages.format("Internalizer.ContextNodeIsNotElement", new Object[0]));
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 319 */         if (!this.forest.logic.checkIfValidTargetNode(this.forest, item, (Element)target)) {
/* 320 */           reportError(item, Messages.format("Internalizer.OrphanedCustomization", new Object[] { item.getNodeName() }));
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 326 */         moveUnder(item, (Element)target);
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
/* 342 */     Element realTarget = this.forest.logic.refineTarget(target);
/*     */     
/* 344 */     declExtensionNamespace(decl, target);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     Element p = decl;
/* 350 */     Set<String> inscopes = new HashSet<String>();
/*     */     while (true) {
/* 352 */       NamedNodeMap atts = p.getAttributes();
/* 353 */       for (int i = 0; i < atts.getLength(); i++) {
/* 354 */         Attr a = (Attr)atts.item(i);
/* 355 */         if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI())) {
/*     */           String prefix;
/* 357 */           if (a.getName().indexOf(':') == -1) { prefix = ""; }
/* 358 */           else { prefix = a.getLocalName(); }
/*     */           
/* 360 */           if (inscopes.add(prefix) && p != decl)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 366 */             decl.setAttributeNodeNS((Attr)a.cloneNode(true));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 371 */       if (p.getParentNode() instanceof Document) {
/*     */         break;
/*     */       }
/* 374 */       p = (Element)p.getParentNode();
/*     */     } 
/*     */     
/* 377 */     if (!inscopes.contains(""))
/*     */     {
/*     */ 
/*     */       
/* 381 */       decl.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 386 */     if (realTarget.getOwnerDocument() != decl.getOwnerDocument()) {
/*     */       
/* 388 */       Element original = decl;
/* 389 */       decl = (Element)realTarget.getOwnerDocument().importNode(decl, true);
/*     */ 
/*     */       
/* 392 */       copyLocators(original, decl);
/*     */     } 
/*     */ 
/*     */     
/* 396 */     realTarget.appendChild(decl);
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
/* 407 */     if (!"http://java.sun.com/xml/ns/jaxb".equals(decl.getNamespaceURI())) {
/* 408 */       declareExtensionNamespace(target, decl.getNamespaceURI());
/*     */     }
/* 410 */     NodeList lst = decl.getChildNodes();
/* 411 */     for (int i = 0; i < lst.getLength(); i++) {
/* 412 */       Node n = lst.item(i);
/* 413 */       if (n instanceof Element) {
/* 414 */         declExtensionNamespace((Element)n, target);
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
/* 428 */     Element root = target.getOwnerDocument().getDocumentElement();
/* 429 */     Attr att = root.getAttributeNodeNS("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes");
/* 430 */     if (att == null) {
/* 431 */       String jaxbPrefix = allocatePrefix(root, "http://java.sun.com/xml/ns/jaxb");
/*     */       
/* 433 */       att = target.getOwnerDocument().createAttributeNS("http://java.sun.com/xml/ns/jaxb", jaxbPrefix + ':' + "extensionBindingPrefixes");
/*     */       
/* 435 */       root.setAttributeNodeNS(att);
/*     */     } 
/*     */     
/* 438 */     String prefix = allocatePrefix(root, nsUri);
/* 439 */     if (att.getValue().indexOf(prefix) == -1)
/*     */     {
/* 441 */       att.setValue(att.getValue() + ' ' + prefix);
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
/* 453 */     NamedNodeMap atts = e.getAttributes();
/* 454 */     for (int i = 0; i < atts.getLength(); i++) {
/* 455 */       Attr a = (Attr)atts.item(i);
/* 456 */       if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI()) && 
/* 457 */         a.getName().indexOf(':') != -1)
/*     */       {
/* 459 */         if (a.getValue().equals(nsUri)) {
/* 460 */           return a.getLocalName();
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/*     */     while (true) {
/* 466 */       prefix = "p" + (int)(Math.random() * 1000000.0D) + '_';
/* 467 */       if (e.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", prefix) != null)
/*     */         continue;  break;
/*     */     } 
/* 470 */     e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, nsUri);
/* 471 */     return prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyLocators(Element src, Element dst) {
/* 480 */     this.forest.locatorTable.storeStartLocation(dst, this.forest.locatorTable.getStartLocation(src));
/*     */     
/* 482 */     this.forest.locatorTable.storeEndLocation(dst, this.forest.locatorTable.getEndLocation(src));
/*     */ 
/*     */ 
/*     */     
/* 486 */     Element[] srcChilds = DOMUtils.getChildElements(src);
/* 487 */     Element[] dstChilds = DOMUtils.getChildElements(dst);
/*     */     
/* 489 */     for (int i = 0; i < srcChilds.length; i++) {
/* 490 */       copyLocators(srcChilds[i], dstChilds[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg) {
/* 495 */     reportError(errorSource, formattedMsg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg, Exception nestedException) {
/* 501 */     SAXParseException2 sAXParseException2 = new SAXParseException2(formattedMsg, this.forest.locatorTable.getStartLocation(errorSource), nestedException);
/*     */ 
/*     */     
/* 504 */     this.errorHandler.error((SAXParseException)sAXParseException2);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\Internalizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */