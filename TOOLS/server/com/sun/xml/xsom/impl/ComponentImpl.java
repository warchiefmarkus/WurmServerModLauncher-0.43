/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.SCD;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.parser.SchemaDocument;
/*     */ import com.sun.xml.xsom.util.ComponentNameFunction;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.xml.sax.Locator;
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
/*     */ public abstract class ComponentImpl
/*     */   implements XSComponent
/*     */ {
/*     */   protected final SchemaDocumentImpl ownerDocument;
/*     */   private AnnotationImpl annotation;
/*     */   private final Locator locator;
/*     */   private Object foreignAttributes;
/*     */   
/*     */   protected ComponentImpl(SchemaDocumentImpl _owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa) {
/*  42 */     this.ownerDocument = _owner;
/*  43 */     this.annotation = _annon;
/*  44 */     this.locator = _loc;
/*  45 */     this.foreignAttributes = fa;
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemaImpl getOwnerSchema() {
/*  50 */     if (this.ownerDocument == null) {
/*  51 */       return null;
/*     */     }
/*  53 */     return this.ownerDocument.getSchema();
/*     */   }
/*     */   
/*     */   public XSSchemaSet getRoot() {
/*  57 */     if (this.ownerDocument == null) {
/*  58 */       return null;
/*     */     }
/*  60 */     return getOwnerSchema().getRoot();
/*     */   }
/*     */   
/*     */   public SchemaDocument getSourceDocument() {
/*  64 */     return (SchemaDocument)this.ownerDocument;
/*     */   }
/*     */   
/*     */   public final XSAnnotation getAnnotation() {
/*  68 */     return this.annotation;
/*     */   }
/*     */   public XSAnnotation getAnnotation(boolean createIfNotExist) {
/*  71 */     if (createIfNotExist && this.annotation == null) {
/*  72 */       this.annotation = new AnnotationImpl();
/*     */     }
/*  74 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public final Locator getLocator() {
/*  78 */     return this.locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ForeignAttributesImpl> getForeignAttributes() {
/*  89 */     Object t = this.foreignAttributes;
/*     */     
/*  91 */     if (t == null) {
/*  92 */       return Collections.EMPTY_LIST;
/*     */     }
/*  94 */     if (t instanceof List) {
/*  95 */       return (List<ForeignAttributesImpl>)t;
/*     */     }
/*  97 */     t = this.foreignAttributes = convertToList((ForeignAttributesImpl)t);
/*  98 */     return (List<ForeignAttributesImpl>)t;
/*     */   }
/*     */   
/*     */   public String getForeignAttribute(String nsUri, String localName) {
/* 102 */     for (ForeignAttributesImpl fa : getForeignAttributes()) {
/* 103 */       String v = fa.getValue(nsUri, localName);
/* 104 */       if (v != null) return v; 
/*     */     } 
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   private List<ForeignAttributesImpl> convertToList(ForeignAttributesImpl fa) {
/* 110 */     List<ForeignAttributesImpl> lst = new ArrayList<ForeignAttributesImpl>();
/* 111 */     while (fa != null) {
/* 112 */       lst.add(fa);
/* 113 */       fa = fa.next;
/*     */     } 
/* 115 */     return Collections.unmodifiableList(lst);
/*     */   }
/*     */   
/*     */   public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 120 */       return SCD.create(scd, nsContext).select(this);
/* 121 */     } catch (ParseException e) {
/* 122 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 128 */       return SCD.create(scd, nsContext).selectSingle(this);
/* 129 */     } catch (ParseException e) {
/* 130 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 135 */     return (String)apply((XSFunction)new ComponentNameFunction());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ComponentImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */