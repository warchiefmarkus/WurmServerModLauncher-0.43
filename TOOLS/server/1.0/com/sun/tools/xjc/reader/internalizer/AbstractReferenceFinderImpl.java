/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*    */ import com.sun.tools.xjc.reader.internalizer.Messages;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public abstract class AbstractReferenceFinderImpl
/*    */   extends XMLFilterImpl
/*    */ {
/*    */   protected final DOMForest parent;
/*    */   private Locator locator;
/*    */   
/*    */   protected AbstractReferenceFinderImpl(DOMForest _parent) {
/* 31 */     this.parent = _parent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract String findExternalResource(String paramString1, String paramString2, Attributes paramAttributes);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 49 */     super.startElement(namespaceURI, localName, qName, atts);
/*    */     
/* 51 */     String relativeRef = findExternalResource(namespaceURI, localName, atts);
/* 52 */     if (relativeRef == null) {
/*    */       return;
/*    */     }
/*    */     
/*    */     try {
/* 57 */       String ref = (new URL(new URL(this.locator.getSystemId()), relativeRef)).toExternalForm();
/*    */ 
/*    */       
/* 60 */       this.parent.parse(ref);
/* 61 */     } catch (IOException e) {
/* 62 */       SAXParseException spe = new SAXParseException(Messages.format("AbstractReferenceFinderImpl.UnableToParse", relativeRef), this.locator, e);
/*    */ 
/*    */ 
/*    */       
/* 66 */       fatalError(spe);
/* 67 */       throw spe;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDocumentLocator(Locator locator) {
/* 74 */     super.setDocumentLocator(locator);
/* 75 */     this.locator = locator;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\AbstractReferenceFinderImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */