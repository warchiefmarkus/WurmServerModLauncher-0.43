/*     */ package com.sun.tools.jxc;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.tools.jxc.gen.config.Config;
/*     */ import com.sun.tools.jxc.gen.config.NGCCHandler;
/*     */ import com.sun.tools.jxc.gen.config.Schema;
/*     */ import com.sun.tools.xjc.SchemaCache;
/*     */ import com.sun.tools.xjc.api.Reference;
/*     */ import com.sun.tools.xjc.util.ForkContentHandler;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ public final class ConfigReader
/*     */ {
/*  85 */   private final Set<Reference> classesToBeIncluded = new HashSet<Reference>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SchemaOutputResolver schemaOutputResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotationProcessorEnvironment env;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigReader(AnnotationProcessorEnvironment env, Collection<? extends TypeDeclaration> classes, File xmlFile, ErrorHandler errorHandler) throws SAXException, IOException {
/* 107 */     this.env = env;
/* 108 */     Config config = parseAndGetConfig(xmlFile, errorHandler);
/* 109 */     checkAllClasses(config, classes);
/* 110 */     String path = xmlFile.getAbsolutePath();
/* 111 */     String xmlPath = path.substring(0, path.lastIndexOf(File.separatorChar));
/* 112 */     this.schemaOutputResolver = createSchemaOutputResolver(config, xmlPath);
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
/*     */   
/*     */   public Collection<Reference> getClassesToBeIncluded() {
/* 126 */     return this.classesToBeIncluded;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkAllClasses(Config config, Collection<? extends TypeDeclaration> rootClasses) {
/* 131 */     List<Pattern> includeRegexList = config.getClasses().getIncludes();
/* 132 */     List<Pattern> excludeRegexList = config.getClasses().getExcludes();
/*     */ 
/*     */     
/* 135 */     label21: for (TypeDeclaration typeDecl : rootClasses) {
/*     */       
/* 137 */       String qualifiedName = typeDecl.getQualifiedName();
/*     */       
/* 139 */       for (Pattern pattern : excludeRegexList) {
/* 140 */         boolean match = checkPatternMatch(qualifiedName, pattern);
/* 141 */         if (match) {
/*     */           continue label21;
/*     */         }
/*     */       } 
/* 145 */       for (Pattern pattern : includeRegexList) {
/* 146 */         boolean match = checkPatternMatch(qualifiedName, pattern);
/* 147 */         if (match) {
/* 148 */           this.classesToBeIncluded.add(new Reference(typeDecl, this.env));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaOutputResolver getSchemaOutputResolver() {
/* 159 */     return this.schemaOutputResolver;
/*     */   }
/*     */   
/*     */   private SchemaOutputResolver createSchemaOutputResolver(Config config, String xmlpath) {
/* 163 */     File baseDir = new File(xmlpath, config.getBaseDir().getPath());
/* 164 */     SchemaOutputResolverImpl schemaOutputResolver = new SchemaOutputResolverImpl(baseDir);
/*     */     
/* 166 */     for (Schema schema : config.getSchema()) {
/* 167 */       String namespace = schema.getNamespace();
/* 168 */       File location = schema.getLocation();
/* 169 */       schemaOutputResolver.addSchemaInfo(namespace, location);
/*     */     } 
/* 171 */     return schemaOutputResolver;
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
/*     */   private boolean checkPatternMatch(String qualifiedName, Pattern pattern) {
/* 184 */     Matcher matcher = pattern.matcher(qualifiedName);
/* 185 */     return matcher.matches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   private static SchemaCache configSchema = new SchemaCache(Config.class.getResource("config.xsd"));
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
/*     */   private Config parseAndGetConfig(File xmlFile, ErrorHandler errorHandler) throws SAXException, IOException {
/*     */     XMLReader reader;
/*     */     try {
/* 207 */       SAXParserFactory factory = SAXParserFactory.newInstance();
/* 208 */       factory.setNamespaceAware(true);
/* 209 */       reader = factory.newSAXParser().getXMLReader();
/* 210 */     } catch (ParserConfigurationException e) {
/*     */       
/* 212 */       throw new Error(e);
/*     */     } 
/* 214 */     NGCCRuntimeEx runtime = new NGCCRuntimeEx(errorHandler);
/*     */ 
/*     */     
/* 217 */     ValidatorHandler validator = configSchema.newValidator();
/* 218 */     validator.setErrorHandler(errorHandler);
/*     */ 
/*     */     
/* 221 */     reader.setContentHandler((ContentHandler)new ForkContentHandler(validator, (ContentHandler)runtime));
/*     */     
/* 223 */     reader.setErrorHandler(errorHandler);
/* 224 */     Config config = new Config(runtime);
/* 225 */     runtime.setRootHandler((NGCCHandler)config);
/* 226 */     reader.parse(new InputSource(xmlFile.toURL().toExternalForm()));
/* 227 */     runtime.reset();
/*     */     
/* 229 */     return config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SchemaOutputResolverImpl
/*     */     extends SchemaOutputResolver
/*     */   {
/*     */     private final File baseDir;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     private final Map<String, File> schemas = new HashMap<String, File>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Result createOutput(String namespaceUri, String suggestedFileName) {
/* 260 */       if (this.schemas.containsKey(namespaceUri)) {
/* 261 */         File loc = this.schemas.get(namespaceUri);
/* 262 */         if (loc == null) return null;
/*     */ 
/*     */ 
/*     */         
/* 266 */         loc.getParentFile().mkdirs();
/*     */         
/* 268 */         return new StreamResult(loc);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 274 */       File schemaFile = new File(this.baseDir, suggestedFileName);
/*     */       
/* 276 */       return new StreamResult(schemaFile);
/*     */     }
/*     */ 
/*     */     
/*     */     public SchemaOutputResolverImpl(File baseDir) {
/* 281 */       assert baseDir != null;
/* 282 */       this.baseDir = baseDir;
/*     */     }
/*     */     
/*     */     public void addSchemaInfo(String namespaceUri, File location) {
/* 286 */       if (namespaceUri == null)
/*     */       {
/* 288 */         namespaceUri = ""; } 
/* 289 */       this.schemas.put(namespaceUri, location);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\ConfigReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */