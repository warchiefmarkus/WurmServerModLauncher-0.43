/*     */ package com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.ConsoleErrorReporter;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
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
/*     */ public class SchemaConstraintChecker
/*     */ {
/*     */   public static boolean check(InputSource[] schemas, ErrorReceiver errorHandler, final EntityResolver entityResolver) {
/*  75 */     ErrorReceiverFilter errorFilter = new ErrorReceiverFilter((ErrorListener)errorHandler);
/*  76 */     boolean hadErrors = false;
/*     */     
/*  78 */     SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/*  79 */     sf.setErrorHandler((ErrorHandler)errorFilter);
/*  80 */     if (entityResolver != null) {
/*  81 */       sf.setResourceResolver(new LSResourceResolver()
/*     */           {
/*     */             public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI)
/*     */             {
/*     */               try {
/*  86 */                 InputSource is = entityResolver.resolveEntity(namespaceURI, systemId);
/*  87 */                 if (is == null) return null; 
/*  88 */                 return new LSInputSAXWrapper(is);
/*  89 */               } catch (SAXException e) {
/*     */                 
/*  91 */                 return null;
/*  92 */               } catch (IOException e) {
/*     */                 
/*  94 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     try {
/* 101 */       sf.newSchema(getSchemaSource(schemas));
/* 102 */     } catch (SAXException e) {
/*     */       
/* 104 */       hadErrors = true;
/* 105 */     } catch (OutOfMemoryError e) {
/* 106 */       errorHandler.warning(null, Messages.format("SchemaConstraintChecker.UnableToCheckCorrectness", new Object[0]));
/*     */     } 
/*     */     
/* 109 */     return (!hadErrors && !errorFilter.hadError());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Source[] getSchemaSource(InputSource[] schemas) {
/* 120 */     SAXSource[] sources = new SAXSource[schemas.length];
/* 121 */     for (int i = 0; i < schemas.length; i++)
/* 122 */       sources[i] = new SAXSource(schemas[i]); 
/* 123 */     return (Source[])sources;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 128 */     InputSource[] sources = new InputSource[args.length];
/* 129 */     for (int i = 0; i < args.length; i++) {
/* 130 */       sources[i] = new InputSource((new File(args[i])).toURL().toExternalForm());
/*     */     }
/* 132 */     check(sources, (ErrorReceiver)new ConsoleErrorReporter(), null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\parser\SchemaConstraintChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */