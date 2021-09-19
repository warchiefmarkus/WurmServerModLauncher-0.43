/*     */ package org.kohsuke.rngom.parse.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.ast.builder.IncludedGrammar;
/*     */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Scope;
/*     */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*     */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*     */ import org.kohsuke.rngom.parse.Parseable;
/*     */ import org.kohsuke.rngom.xml.sax.JAXPXMLReaderCreator;
/*     */ import org.kohsuke.rngom.xml.sax.XMLReaderCreator;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SAXParseable
/*     */   implements Parseable
/*     */ {
/*     */   private final InputSource in;
/*     */   final XMLReaderCreator xrc;
/*     */   final ErrorHandler eh;
/*     */   
/*     */   public SAXParseable(InputSource in, ErrorHandler eh, XMLReaderCreator xrc) {
/*  31 */     this.xrc = xrc;
/*  32 */     this.eh = eh;
/*  33 */     this.in = in;
/*     */   }
/*     */   
/*     */   public SAXParseable(InputSource in, ErrorHandler eh) {
/*  37 */     this(in, eh, (XMLReaderCreator)new JAXPXMLReaderCreator());
/*     */   }
/*     */   
/*     */   public ParsedPattern parse(SchemaBuilder schemaBuilder) throws BuildException, IllegalSchemaException {
/*     */     try {
/*  42 */       XMLReader xr = this.xrc.createXMLReader();
/*  43 */       SchemaParser sp = new SchemaParser(this, xr, this.eh, schemaBuilder, null, null, "");
/*  44 */       xr.parse(this.in);
/*  45 */       ParsedPattern p = sp.getParsedPattern();
/*  46 */       return schemaBuilder.expandPattern(p);
/*     */     }
/*  48 */     catch (SAXException e) {
/*  49 */       throw toBuildException(e);
/*     */     }
/*  51 */     catch (IOException e) {
/*  52 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern parseInclude(String uri, SchemaBuilder schemaBuilder, IncludedGrammar g, String inheritedNs) throws BuildException, IllegalSchemaException {
/*     */     try {
/*  59 */       XMLReader xr = this.xrc.createXMLReader();
/*  60 */       SchemaParser sp = new SchemaParser(this, xr, this.eh, schemaBuilder, g, (Scope)g, inheritedNs);
/*  61 */       xr.parse(makeInputSource(xr, uri));
/*  62 */       return sp.getParsedPattern();
/*     */     }
/*  64 */     catch (SAXException e) {
/*  65 */       throw toBuildException(e);
/*     */     }
/*  67 */     catch (IOException e) {
/*  68 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern parseExternal(String uri, SchemaBuilder schemaBuilder, Scope s, String inheritedNs) throws BuildException, IllegalSchemaException {
/*     */     try {
/*  75 */       XMLReader xr = this.xrc.createXMLReader();
/*  76 */       SchemaParser sp = new SchemaParser(this, xr, this.eh, schemaBuilder, null, s, inheritedNs);
/*  77 */       xr.parse(makeInputSource(xr, uri));
/*  78 */       return sp.getParsedPattern();
/*     */     }
/*  80 */     catch (SAXException e) {
/*  81 */       throw toBuildException(e);
/*     */     }
/*  83 */     catch (IOException e) {
/*  84 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static InputSource makeInputSource(XMLReader xr, String systemId) throws IOException, SAXException {
/*  89 */     EntityResolver er = xr.getEntityResolver();
/*  90 */     if (er != null) {
/*  91 */       InputSource inputSource = er.resolveEntity(null, systemId);
/*  92 */       if (inputSource != null)
/*  93 */         return inputSource; 
/*     */     } 
/*  95 */     return new InputSource(systemId);
/*     */   }
/*     */   
/*     */   static BuildException toBuildException(SAXException e) {
/*  99 */     Exception inner = e.getException();
/* 100 */     if (inner instanceof BuildException)
/* 101 */       throw (BuildException)inner; 
/* 102 */     throw new BuildException(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\xml\SAXParseable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */