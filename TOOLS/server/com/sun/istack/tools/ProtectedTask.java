/*     */ package com.sun.istack.tools;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DynamicConfigurator;
/*     */ import org.apache.tools.ant.IntrospectionHelper;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public abstract class ProtectedTask
/*     */   extends Task
/*     */   implements DynamicConfigurator
/*     */ {
/*  28 */   private final AntElement root = new AntElement("root");
/*     */   
/*     */   public void setDynamicAttribute(String name, String value) throws BuildException {
/*  31 */     this.root.setDynamicAttribute(name, value);
/*     */   }
/*     */   
/*     */   public Object createDynamicElement(String name) throws BuildException {
/*  35 */     return this.root.createDynamicElement(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  44 */     ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/*     */     try {
/*  46 */       ClassLoader cl = createClassLoader();
/*  47 */       Class driver = cl.loadClass(getCoreClassName());
/*     */       
/*  49 */       Task t = (Task)driver.newInstance();
/*  50 */       t.setProject(getProject());
/*  51 */       t.setTaskName(getTaskName());
/*  52 */       this.root.configure(t);
/*     */       
/*  54 */       Thread.currentThread().setContextClassLoader(cl);
/*  55 */       t.execute();
/*  56 */     } catch (UnsupportedClassVersionError e) {
/*  57 */       throw new BuildException("Requires JDK 5.0 or later. Please download it from http://java.sun.com/j2se/1.5/");
/*  58 */     } catch (ClassNotFoundException e) {
/*  59 */       throw new BuildException(e);
/*  60 */     } catch (InstantiationException e) {
/*  61 */       throw new BuildException(e);
/*  62 */     } catch (IllegalAccessException e) {
/*  63 */       throw new BuildException(e);
/*  64 */     } catch (IOException e) {
/*  65 */       throw new BuildException(e);
/*     */     } finally {
/*  67 */       Thread.currentThread().setContextClassLoader(ccl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getCoreClassName();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ClassLoader createClassLoader() throws ClassNotFoundException, IOException;
/*     */ 
/*     */ 
/*     */   
/*     */   private class AntElement
/*     */     implements DynamicConfigurator
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */     
/*  88 */     private final Map attributes = new HashMap();
/*     */     
/*  90 */     private final List elements = new ArrayList();
/*     */     
/*     */     public AntElement(String name) {
/*  93 */       this.name = name;
/*     */     }
/*     */     private final ProtectedTask this$0;
/*     */     public void setDynamicAttribute(String name, String value) throws BuildException {
/*  97 */       this.attributes.put(name, value);
/*     */     }
/*     */     
/*     */     public Object createDynamicElement(String name) throws BuildException {
/* 101 */       AntElement e = new AntElement(name);
/* 102 */       this.elements.add(e);
/* 103 */       return e;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void configure(Object antObject) {
/* 110 */       IntrospectionHelper ih = IntrospectionHelper.getHelper(antObject.getClass());
/*     */ 
/*     */       
/* 113 */       for (Iterator iterator = this.attributes.entrySet().iterator(); iterator.hasNext(); ) {
/* 114 */         Map.Entry att = iterator.next();
/* 115 */         ih.setAttribute(ProtectedTask.this.getProject(), antObject, (String)att.getKey(), (String)att.getValue());
/*     */       } 
/*     */ 
/*     */       
/* 119 */       for (Iterator itr = this.elements.iterator(); itr.hasNext(); ) {
/* 120 */         AntElement e = itr.next();
/* 121 */         Object child = ih.createElement(ProtectedTask.this.getProject(), antObject, e.name);
/* 122 */         e.configure(child);
/* 123 */         ih.storeElement(ProtectedTask.this.getProject(), antObject, child, e.name);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\tools\ProtectedTask.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */