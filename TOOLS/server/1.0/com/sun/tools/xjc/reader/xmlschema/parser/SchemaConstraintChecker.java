/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.Version;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.parsers.XMLGrammarPreparser;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.tools.xjc.ConsoleErrorReporter;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.Messages;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.VersionNumber;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLEntityResolverImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLFallthroughEntityResolver;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.util.Which;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
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
/*     */   private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
/*     */   
/*     */   public static boolean check(InputSource[] schemas, ErrorReceiver errorHandler, EntityResolver entityResolver) throws IOException {
/*  51 */     checkXercesVersion(errorHandler);
/*     */     
/*  53 */     XMLGrammarPreparser preparser = new XMLGrammarPreparser();
/*  54 */     preparser.registerPreparser("http://www.w3.org/2001/XMLSchema", null);
/*     */ 
/*     */     
/*  57 */     preparser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
/*     */ 
/*     */     
/*  60 */     ErrorReceiverFilter filter = new ErrorReceiverFilter(errorHandler);
/*  61 */     preparser.setErrorHandler(new ErrorHandlerWrapper((ErrorHandler)filter));
/*  62 */     if (entityResolver != null)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  67 */       preparser.setEntityResolver((XMLEntityResolver)new XMLFallthroughEntityResolver((XMLEntityResolver)new XMLEntityResolverImpl(entityResolver), new XMLEntityManager()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     preparser.setGrammarPool(new XMLGrammarPoolImpl());
/*     */     
/*     */     try {
/*  76 */       for (int i = 0; i < schemas.length; i++) {
/*     */         
/*  78 */         preparser.preparseGrammar("http://www.w3.org/2001/XMLSchema", createXMLInputSource(schemas[i]));
/*  79 */         rewind(schemas[i]);
/*     */       } 
/*  81 */     } catch (XNIException e) {}
/*     */ 
/*     */ 
/*     */     
/*  85 */     return !filter.hadError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkXercesVersion(ErrorReceiver errorHandler) {
/*  92 */     String version = null;
/*     */     
/*     */     try {
/*  95 */       version = (String)Version.class.getField("fVersion").get(null);
/*  96 */     } catch (Throwable t) {
/*     */       
/*     */       try {
/*  99 */         version = Version.getVersion();
/* 100 */       } catch (Throwable tt) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (version != null) {
/*     */       
/* 107 */       StringTokenizer tokens = new StringTokenizer(version);
/* 108 */       while (tokens.hasMoreTokens()) {
/*     */         VersionNumber v;
/*     */         try {
/* 111 */           v = new VersionNumber(tokens.nextToken());
/* 112 */         } catch (IllegalArgumentException e) {
/*     */           continue;
/*     */         } 
/* 115 */         if (v.isOlderThan(new VersionNumber("2.2")))
/*     */         {
/* 117 */           errorHandler.warning(null, Messages.format("SchemaConstraintChecker.XercesTooOld", Which.which(Version.class), version));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 127 */     errorHandler.warning(null, Messages.format("SchemaConstraintChecker.UnableToCheckXercesVersion", Which.which(Version.class), version));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static XMLInputSource createXMLInputSource(InputSource is) throws IOException {
/* 135 */     XMLInputSource xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), null);
/*     */     
/* 137 */     xis.setByteStream(is.getByteStream());
/* 138 */     xis.setCharacterStream(is.getCharacterStream());
/* 139 */     xis.setEncoding(is.getEncoding());
/* 140 */     return xis;
/*     */   }
/*     */   
/*     */   private static void rewind(InputSource is) throws IOException {
/* 144 */     if (is.getByteStream() != null)
/* 145 */       is.getByteStream().reset(); 
/* 146 */     if (is.getCharacterStream() != null) {
/* 147 */       is.getCharacterStream().reset();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 153 */     InputSource[] sources = new InputSource[args.length];
/* 154 */     for (int i = 0; i < args.length; i++) {
/* 155 */       sources[i] = new InputSource((new File(args[i])).toURL().toExternalForm());
/*     */     }
/* 157 */     check(sources, (ErrorReceiver)new ConsoleErrorReporter(), null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\SchemaConstraintChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */