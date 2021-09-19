/*     */ package org.kohsuke.rngom.digested;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class DAnnotation
/*     */ {
/*  23 */   static final DAnnotation EMPTY = new DAnnotation();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   final Map<QName, Attribute> attributes = new HashMap<QName, Attribute>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   final List<Element> contents = new ArrayList<Element>();
/*     */ 
/*     */   
/*     */   public static class Attribute
/*     */   {
/*     */     private final String ns;
/*     */     
/*     */     private final String localName;
/*     */     
/*     */     private final String prefix;
/*     */     private String value;
/*     */     private Locator loc;
/*     */     
/*     */     public Attribute(String ns, String localName, String prefix) {
/*  47 */       this.ns = ns;
/*  48 */       this.localName = localName;
/*  49 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public Attribute(String ns, String localName, String prefix, String value, Locator loc) {
/*  53 */       this.ns = ns;
/*  54 */       this.localName = localName;
/*  55 */       this.prefix = prefix;
/*  56 */       this.value = value;
/*  57 */       this.loc = loc;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getNs() {
/*  67 */       return this.ns;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLocalName() {
/*  77 */       return this.localName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPrefix() {
/*  87 */       return this.prefix;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/*  97 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Locator getLoc() {
/* 107 */       return this.loc;
/*     */     }
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
/*     */   public Attribute getAttribute(String nsUri, String localName) {
/* 120 */     return getAttribute(new QName(nsUri, localName));
/*     */   }
/*     */   
/*     */   public Attribute getAttribute(QName n) {
/* 124 */     return this.attributes.get(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<QName, Attribute> getAttributes() {
/* 135 */     return Collections.unmodifiableMap(this.attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Element> getChildren() {
/* 146 */     return Collections.unmodifiableList(this.contents);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DAnnotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */