/*     */ package com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.internalizer.AbstractReferenceFinderImpl;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.util.DOMUtils;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLSchemaInternalizationLogic
/*     */   implements InternalizationLogic
/*     */ {
/*     */   private static final class ReferenceFinder
/*     */     extends AbstractReferenceFinderImpl
/*     */   {
/*     */     ReferenceFinder(DOMForest parent) {
/*  63 */       super(parent);
/*     */     }
/*     */     
/*     */     protected String findExternalResource(String nsURI, String localName, Attributes atts) {
/*  67 */       if ("http://www.w3.org/2001/XMLSchema".equals(nsURI) && ("import".equals(localName) || "include".equals(localName)))
/*     */       {
/*  69 */         return atts.getValue("schemaLocation");
/*     */       }
/*  71 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public XMLFilterImpl createExternalReferenceFinder(DOMForest parent) {
/*  76 */     return (XMLFilterImpl)new ReferenceFinder(parent);
/*     */   }
/*     */   
/*     */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) {
/*  80 */     return "http://www.w3.org/2001/XMLSchema".equals(target.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   
/*     */   public Element refineTarget(Element target) {
/*  85 */     Element annotation = DOMUtils.getFirstChildElement(target, "http://www.w3.org/2001/XMLSchema", "annotation");
/*  86 */     if (annotation == null)
/*     */     {
/*  88 */       annotation = insertXMLSchemaElement(target, "annotation");
/*     */     }
/*     */     
/*  91 */     Element appinfo = DOMUtils.getFirstChildElement(annotation, "http://www.w3.org/2001/XMLSchema", "appinfo");
/*  92 */     if (appinfo == null)
/*     */     {
/*  94 */       appinfo = insertXMLSchemaElement(annotation, "appinfo");
/*     */     }
/*  96 */     return appinfo;
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
/*     */   private Element insertXMLSchemaElement(Element parent, String localName) {
/* 109 */     String qname = parent.getTagName();
/* 110 */     int idx = qname.indexOf(':');
/* 111 */     if (idx == -1) { qname = localName; }
/* 112 */     else { qname = qname.substring(0, idx + 1) + localName; }
/*     */     
/* 114 */     Element child = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", qname);
/*     */     
/* 116 */     NodeList children = parent.getChildNodes();
/*     */     
/* 118 */     if (children.getLength() == 0) {
/* 119 */       parent.appendChild(child);
/*     */     } else {
/* 121 */       parent.insertBefore(child, children.item(0));
/*     */     } 
/* 123 */     return child;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\parser\XMLSchemaInternalizationLogic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */