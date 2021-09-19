/*     */ package 1.0.com.sun.xml.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.impl.ElementDecl;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.SchemaSetImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.Patch;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import com.sun.xml.xsom.parser.XSOMParser;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class ParserContext
/*     */ {
/*  36 */   public final SchemaSetImpl schemaSet = new SchemaSetImpl();
/*     */ 
/*     */   
/*     */   private final XSOMParser owner;
/*     */   
/*     */   final XMLParser parser;
/*     */   
/*  43 */   private final Vector patchers = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   protected final Set parsedDocuments = new HashSet();
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
/*     */   private boolean hadError;
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
/*     */   final PatcherManager patcherManager;
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
/*     */   final ErrorHandler errorHandler;
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
/*     */   final ErrorHandler noopHandler;
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
/*     */   public EntityResolver getEntityResolver() {
/* 107 */     return this.owner.getEntityResolver();
/*     */   }
/*     */   
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/* 111 */     return this.owner.getAnnotationParserFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException {
/* 118 */     newNGCCRuntime().parseEntity(source, false, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XSSchemaSet getResult() throws SAXException {
/* 124 */     Iterator itr = this.patchers.iterator();
/* 125 */     while (itr.hasNext())
/* 126 */       ((Patch)itr.next()).run(); 
/* 127 */     this.patchers.clear();
/*     */ 
/*     */     
/* 130 */     itr = this.schemaSet.iterateElementDecls();
/* 131 */     while (itr.hasNext()) {
/* 132 */       ((ElementDecl)itr.next()).updateSubstitutabilityMap();
/*     */     }
/* 134 */     if (this.hadError) return null; 
/* 135 */     return (XSSchemaSet)this.schemaSet;
/*     */   }
/*     */   
/*     */   public NGCCRuntimeEx newNGCCRuntime() {
/* 139 */     return new NGCCRuntimeEx(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParserContext(XSOMParser owner, XMLParser parser) {
/* 145 */     this.hadError = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     this.patcherManager = (PatcherManager)new Object(this);
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
/* 174 */     this.errorHandler = (ErrorHandler)new Object(this);
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
/* 200 */     this.noopHandler = (ErrorHandler)new Object(this);
/*     */     this.owner = owner;
/*     */     this.parser = parser;
/*     */     try {
/*     */       parse(new InputSource(com.sun.xml.xsom.impl.parser.ParserContext.class.getResource("datatypes.xsd").toExternalForm()));
/*     */       SchemaImpl xs = (SchemaImpl)this.schemaSet.getSchema("http://www.w3.org/2001/XMLSchema");
/*     */       xs.addSimpleType((XSSimpleType)this.schemaSet.anySimpleType);
/*     */       xs.addComplexType((XSComplexType)this.schemaSet.anyType);
/*     */     } catch (SAXException e) {
/*     */       if (e.getException() != null) {
/*     */         e.getException().printStackTrace();
/*     */       } else {
/*     */         e.printStackTrace();
/*     */       } 
/*     */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */   
/*     */   void setErrorFlag() {
/*     */     this.hadError = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\ParserContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */