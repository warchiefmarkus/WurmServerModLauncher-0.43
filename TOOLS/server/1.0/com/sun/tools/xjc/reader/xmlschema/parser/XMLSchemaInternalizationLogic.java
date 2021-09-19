/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*    */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*    */ import com.sun.tools.xjc.util.DOMUtils;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.NodeList;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XMLSchemaInternalizationLogic
/*    */   implements InternalizationLogic
/*    */ {
/*    */   public XMLFilter createExternalReferenceFinder(DOMForest parent) {
/* 46 */     return (XMLFilter)new ReferenceFinder(this, parent);
/*    */   }
/*    */   
/*    */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) throws SAXException {
/* 50 */     return "http://www.w3.org/2001/XMLSchema".equals(target.getNamespaceURI());
/*    */   }
/*    */ 
/*    */   
/*    */   public Element refineTarget(Element target) {
/* 55 */     Element annotation = DOMUtils.getFirstChildElement(target, "http://www.w3.org/2001/XMLSchema", "annotation");
/* 56 */     if (annotation == null)
/*    */     {
/* 58 */       annotation = insertXMLSchemaElement(target, "annotation");
/*    */     }
/*    */     
/* 61 */     Element appinfo = DOMUtils.getFirstChildElement(annotation, "http://www.w3.org/2001/XMLSchema", "appinfo");
/* 62 */     if (appinfo == null)
/*    */     {
/* 64 */       appinfo = insertXMLSchemaElement(annotation, "appinfo");
/*    */     }
/* 66 */     return appinfo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Element insertXMLSchemaElement(Element parent, String localName) {
/* 79 */     String qname = parent.getTagName();
/* 80 */     int idx = qname.indexOf(':');
/* 81 */     if (idx == -1) { qname = localName; }
/* 82 */     else { qname = qname.substring(0, idx + 1) + localName; }
/*    */     
/* 84 */     Element child = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", qname);
/*    */     
/* 86 */     NodeList children = parent.getChildNodes();
/*    */     
/* 88 */     if (children.getLength() == 0) {
/* 89 */       parent.appendChild(child);
/*    */     } else {
/* 91 */       parent.insertBefore(child, children.item(0));
/*    */     } 
/* 93 */     return child;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\XMLSchemaInternalizationLogic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */