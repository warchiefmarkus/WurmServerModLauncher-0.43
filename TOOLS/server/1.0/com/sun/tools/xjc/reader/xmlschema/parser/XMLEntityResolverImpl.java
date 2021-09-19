/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*    */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
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
/*    */ class XMLEntityResolverImpl
/*    */   implements XMLEntityResolver
/*    */ {
/*    */   private final EntityResolver entityResolver;
/*    */   
/*    */   XMLEntityResolverImpl(EntityResolver er) {
/* 34 */     if (er == null) throw new NullPointerException(); 
/* 35 */     this.entityResolver = er;
/*    */   }
/*    */   
/*    */   public XMLInputSource resolveEntity(XMLResourceIdentifier r) throws XNIException, IOException {
/* 39 */     String publicId = r.getPublicId();
/* 40 */     String systemId = r.getExpandedSystemId();
/*    */     
/* 42 */     if (publicId == null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 50 */       publicId = r.getNamespace();
/*    */     }
/*    */     try {
/* 53 */       InputSource is = this.entityResolver.resolveEntity(publicId, systemId);
/* 54 */       if (is == null) return null;
/*    */       
/* 56 */       XMLInputSource xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), r.getBaseSystemId(), is.getByteStream(), is.getEncoding());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 63 */       xis.setCharacterStream(is.getCharacterStream());
/* 64 */       return xis;
/* 65 */     } catch (SAXException e) {
/* 66 */       throw new XNIException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\XMLEntityResolverImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */