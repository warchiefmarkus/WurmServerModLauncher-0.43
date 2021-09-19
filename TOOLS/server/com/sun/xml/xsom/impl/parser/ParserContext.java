/*     */ package com.sun.xml.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.impl.ElementDecl;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.SchemaSetImpl;
/*     */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import com.sun.xml.xsom.parser.XSOMParser;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class ParserContext
/*     */ {
/*  35 */   public final SchemaSetImpl schemaSet = new SchemaSetImpl();
/*     */ 
/*     */   
/*     */   private final XSOMParser owner;
/*     */   
/*     */   final XMLParser parser;
/*     */   
/*  42 */   private final Vector<Patch> patchers = new Vector<Patch>();
/*  43 */   private final Vector<Patch> errorCheckers = new Vector<Patch>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public final Map<SchemaDocumentImpl, SchemaDocumentImpl> parsedDocuments = new HashMap<SchemaDocumentImpl, SchemaDocumentImpl>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hadError;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final PatcherManager patcherManager;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final ErrorHandler errorHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   final ErrorHandler noopHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/*  77 */     return this.owner.getEntityResolver();
/*     */   }
/*     */   
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/*  81 */     return this.owner.getAnnotationParserFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException {
/*  88 */     newNGCCRuntime().parseEntity(source, false, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XSSchemaSet getResult() throws SAXException {
/*  94 */     for (Patch patcher : this.patchers)
/*  95 */       patcher.run(); 
/*  96 */     this.patchers.clear();
/*     */ 
/*     */     
/*  99 */     Iterator<ElementDecl> itr = this.schemaSet.iterateElementDecls();
/* 100 */     while (itr.hasNext()) {
/* 101 */       ((ElementDecl)itr.next()).updateSubstitutabilityMap();
/*     */     }
/*     */     
/* 104 */     for (Patch patcher : this.errorCheckers)
/* 105 */       patcher.run(); 
/* 106 */     this.errorCheckers.clear();
/*     */ 
/*     */     
/* 109 */     if (this.hadError) return null; 
/* 110 */     return (XSSchemaSet)this.schemaSet;
/*     */   }
/*     */   
/*     */   public NGCCRuntimeEx newNGCCRuntime() {
/* 114 */     return new NGCCRuntimeEx(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParserContext(XSOMParser owner, XMLParser parser) {
/* 120 */     this.hadError = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     this.patcherManager = new PatcherManager() {
/*     */         public void addPatcher(Patch patch) {
/* 131 */           ParserContext.this.patchers.add(patch);
/*     */         }
/*     */         public void addErrorChecker(Patch patch) {
/* 134 */           ParserContext.this.errorCheckers.add(patch);
/*     */         }
/*     */         
/*     */         public void reportError(String msg, Locator src) throws SAXException {
/* 138 */           ParserContext.this.setErrorFlag();
/*     */           
/* 140 */           SAXParseException e = new SAXParseException(msg, src);
/* 141 */           if (ParserContext.this.errorHandler == null) {
/* 142 */             throw e;
/*     */           }
/* 144 */           ParserContext.this.errorHandler.error(e);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     this.errorHandler = new ErrorHandler() {
/*     */         private ErrorHandler getErrorHandler() {
/* 154 */           if (ParserContext.this.owner.getErrorHandler() == null) {
/* 155 */             return ParserContext.this.noopHandler;
/*     */           }
/* 157 */           return ParserContext.this.owner.getErrorHandler();
/*     */         }
/*     */         
/*     */         public void warning(SAXParseException e) throws SAXException {
/* 161 */           getErrorHandler().warning(e);
/*     */         }
/*     */         
/*     */         public void error(SAXParseException e) throws SAXException {
/* 165 */           ParserContext.this.setErrorFlag();
/* 166 */           getErrorHandler().error(e);
/*     */         }
/*     */         
/*     */         public void fatalError(SAXParseException e) throws SAXException {
/* 170 */           ParserContext.this.setErrorFlag();
/* 171 */           getErrorHandler().fatalError(e);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     this.noopHandler = new ErrorHandler() {
/*     */         public void warning(SAXParseException e) {}
/*     */         
/*     */         public void error(SAXParseException e) {}
/*     */         
/*     */         public void fatalError(SAXParseException e) {
/* 184 */           ParserContext.this.setErrorFlag();
/*     */         }
/*     */       };
/*     */     this.owner = owner;
/*     */     this.parser = parser;
/*     */     try {
/*     */       parse(new InputSource(ParserContext.class.getResource("datatypes.xsd").toExternalForm()));
/*     */       SchemaImpl xs = (SchemaImpl)this.schemaSet.getSchema("http://www.w3.org/2001/XMLSchema");
/*     */       xs.addSimpleType((XSSimpleType)this.schemaSet.anySimpleType, true);
/*     */       xs.addComplexType((XSComplexType)this.schemaSet.anyType, true);
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


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\ParserContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */