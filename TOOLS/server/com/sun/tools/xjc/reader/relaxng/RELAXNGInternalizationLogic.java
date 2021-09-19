/*    */ package com.sun.tools.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.internalizer.AbstractReferenceFinderImpl;
/*    */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*    */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*    */ public class RELAXNGInternalizationLogic
/*    */   implements InternalizationLogic
/*    */ {
/*    */   private static final class ReferenceFinder
/*    */     extends AbstractReferenceFinderImpl
/*    */   {
/*    */     ReferenceFinder(DOMForest parent) {
/* 61 */       super(parent);
/*    */     }
/*    */     
/*    */     protected String findExternalResource(String nsURI, String localName, Attributes atts) {
/* 65 */       if ("http://relaxng.org/ns/structure/1.0".equals(nsURI) && ("include".equals(localName) || "externalRef".equals(localName)))
/*    */       {
/* 67 */         return atts.getValue("href");
/*    */       }
/* 69 */       return null;
/*    */     }
/*    */   }
/*    */   
/*    */   public XMLFilterImpl createExternalReferenceFinder(DOMForest parent) {
/* 74 */     return (XMLFilterImpl)new ReferenceFinder(parent);
/*    */   }
/*    */   
/*    */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) {
/* 78 */     return "http://relaxng.org/ns/structure/1.0".equals(target.getNamespaceURI());
/*    */   }
/*    */ 
/*    */   
/*    */   public Element refineTarget(Element target) {
/* 83 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\RELAXNGInternalizationLogic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */