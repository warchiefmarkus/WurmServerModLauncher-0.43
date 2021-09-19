/*     */ package com.sun.tools.jxc.apt;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessor;
/*     */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*     */ import com.sun.mirror.apt.AnnotationProcessorFactory;
/*     */ import com.sun.mirror.apt.Filer;
/*     */ import com.sun.mirror.declaration.AnnotationTypeDeclaration;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.xjc.api.Reference;
/*     */ import com.sun.tools.xjc.api.XJC;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.stream.StreamResult;
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
/*     */ public class SchemaGenerator
/*     */   implements AnnotationProcessorFactory
/*     */ {
/*  79 */   private final Map<String, File> schemaLocations = new HashMap<String, File>();
/*     */ 
/*     */   
/*     */   private File episodeFile;
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaGenerator(Map<String, File> m) {
/*  87 */     this.schemaLocations.putAll(m);
/*     */   }
/*     */   
/*     */   public void setEpisodeFile(File episodeFile) {
/*  91 */     this.episodeFile = episodeFile;
/*     */   }
/*     */   
/*     */   public Collection<String> supportedOptions() {
/*  95 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public Collection<String> supportedAnnotationTypes() {
/*  99 */     return Arrays.asList(new String[] { "*" });
/*     */   }
/*     */   
/*     */   public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, final AnnotationProcessorEnvironment env) {
/* 103 */     return new AnnotationProcessor() {
/* 104 */         final ErrorReceiverImpl errorListener = new ErrorReceiverImpl(env);
/*     */         
/*     */         public void process() {
/* 107 */           List<Reference> decls = new ArrayList<Reference>();
/* 108 */           for (TypeDeclaration d : env.getTypeDeclarations()) {
/*     */ 
/*     */             
/* 111 */             if (d instanceof com.sun.mirror.declaration.ClassDeclaration) {
/* 112 */               decls.add(new Reference(d, env));
/*     */             }
/*     */           } 
/* 115 */           J2SJAXBModel model = XJC.createJavaCompiler().bind(decls, Collections.emptyMap(), null, env);
/* 116 */           if (model == null) {
/*     */             return;
/*     */           }
/*     */           try {
/* 120 */             model.generateSchema(new SchemaOutputResolver()
/*     */                 {
/*     */                   public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
/*     */                     File file;
/*     */                     OutputStream out;
/* 125 */                     if (SchemaGenerator.this.schemaLocations.containsKey(namespaceUri)) {
/* 126 */                       file = (File)SchemaGenerator.this.schemaLocations.get(namespaceUri);
/* 127 */                       if (file == null) return null; 
/* 128 */                       out = new FileOutputStream(file);
/*     */                     } else {
/*     */                       
/* 131 */                       file = new File(suggestedFileName);
/* 132 */                       out = env.getFiler().createBinaryFile(Filer.Location.CLASS_TREE, "", file);
/* 133 */                       file = file.getAbsoluteFile();
/*     */                     } 
/*     */                     
/* 136 */                     StreamResult ss = new StreamResult(out);
/* 137 */                     env.getMessager().printNotice("Writing " + file);
/* 138 */                     ss.setSystemId(file.toURL().toExternalForm());
/* 139 */                     return ss;
/*     */                   }
/*     */                 }(ErrorListener)this.errorListener);
/*     */             
/* 143 */             if (SchemaGenerator.this.episodeFile != null) {
/* 144 */               env.getMessager().printNotice("Writing " + SchemaGenerator.this.episodeFile);
/* 145 */               model.generateEpisodeFile(new StreamResult(SchemaGenerator.this.episodeFile));
/*     */             } 
/* 147 */           } catch (IOException e) {
/* 148 */             this.errorListener.error(e.getMessage(), e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public SchemaGenerator() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\SchemaGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */