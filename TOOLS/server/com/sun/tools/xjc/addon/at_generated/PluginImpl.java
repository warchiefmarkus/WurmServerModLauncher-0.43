/*     */ package com.sun.tools.xjc.addon.at_generated;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.tools.xjc.Driver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.Plugin;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ public class PluginImpl
/*     */   extends Plugin
/*     */ {
/*     */   private JClass annotation;
/*     */   
/*     */   public String getOptionName() {
/*  63 */     return "mark-generated";
/*     */   }
/*     */   
/*     */   public String getUsage() {
/*  67 */     return "  -mark-generated    :  mark the generated code as @javax.annotation.Generated";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {
/*  74 */     this.annotation = model.getCodeModel().ref("javax.annotation.Generated");
/*     */     
/*  76 */     for (ClassOutline co : model.getClasses())
/*  77 */       augument(co); 
/*  78 */     for (EnumOutline eo : model.getEnums()) {
/*  79 */       augument(eo);
/*     */     }
/*     */ 
/*     */     
/*  83 */     return true;
/*     */   }
/*     */   
/*     */   private void augument(EnumOutline eo) {
/*  87 */     annotate((JAnnotatable)eo.clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void augument(ClassOutline co) {
/*  94 */     annotate((JAnnotatable)co.implClass);
/*  95 */     for (JMethod m : co.implClass.methods())
/*  96 */       annotate((JAnnotatable)m); 
/*  97 */     for (JFieldVar f : co.implClass.fields().values())
/*  98 */       annotate((JAnnotatable)f); 
/*     */   }
/*     */   
/*     */   private void annotate(JAnnotatable m) {
/* 102 */     m.annotate(this.annotation).param("value", Driver.class.getName()).param("date", getISO8601Date()).param("comments", "JAXB RI v" + Options.getBuildID());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private String date = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getISO8601Date() {
/* 116 */     if (this.date == null) {
/* 117 */       StringBuffer tstamp = new StringBuffer();
/* 118 */       tstamp.append((new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")).format(new Date()));
/*     */ 
/*     */       
/* 121 */       tstamp.insert(tstamp.length() - 2, ':');
/* 122 */       this.date = tstamp.toString();
/*     */     } 
/* 124 */     return this.date;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\addon\at_generated\PluginImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */