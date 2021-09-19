/*     */ package com.sun.tools.jxc;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessorFactory;
/*     */ import com.sun.tools.jxc.apt.SchemaGenerator;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Commandline;
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
/*     */ public class SchemaGenTask
/*     */   extends AptBasedTask
/*     */ {
/*  58 */   private final List schemas = new ArrayList();
/*     */   
/*     */   private File episode;
/*     */   
/*     */   protected void setupCommandlineSwitches(Commandline cmd) {
/*  63 */     cmd.createArgument().setValue("-nocompile");
/*     */   }
/*     */   
/*     */   protected String getCompilationMessage() {
/*  67 */     return "Generating schema from ";
/*     */   }
/*     */   
/*     */   protected String getFailedMessage() {
/*  71 */     return "schema generation failed";
/*     */   }
/*     */   
/*     */   public Schema createSchema() {
/*  75 */     Schema s = new Schema(this);
/*  76 */     this.schemas.add(s);
/*  77 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEpisode(File f) {
/*  85 */     this.episode = f;
/*     */   }
/*     */   
/*     */   protected AnnotationProcessorFactory createFactory() {
/*  89 */     Map m = new HashMap();
/*  90 */     for (int i = 0; i < this.schemas.size(); i++) {
/*  91 */       Schema schema = this.schemas.get(i);
/*     */       
/*  93 */       if (m.containsKey(schema.namespace))
/*  94 */         throw new BuildException("the same namespace is specified twice"); 
/*  95 */       m.put(schema.namespace, schema.file);
/*     */     } 
/*     */ 
/*     */     
/*  99 */     SchemaGenerator r = new SchemaGenerator(m);
/* 100 */     if (this.episode != null)
/* 101 */       r.setEpisodeFile(this.episode); 
/* 102 */     return (AnnotationProcessorFactory)r;
/*     */   }
/*     */   
/*     */   public class Schema {
/*     */     private String namespace;
/*     */     
/*     */     public Schema(SchemaGenTask this$0) {
/* 109 */       this.this$0 = this$0;
/*     */     }
/*     */     private File file; private final SchemaGenTask this$0;
/*     */     
/*     */     public void setNamespace(String namespace) {
/* 114 */       this.namespace = namespace;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFile(String fileName) {
/* 119 */       File dest = this.this$0.getDestdir();
/* 120 */       if (dest == null)
/* 121 */         dest = this.this$0.getProject().getBaseDir(); 
/* 122 */       this.file = new File(dest, fileName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\SchemaGenTask.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */