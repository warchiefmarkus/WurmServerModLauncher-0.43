/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public final class NameBuilder
/*     */ {
/*  60 */   private Map<String, Integer> uriIndexMap = new HashMap<String, Integer>();
/*  61 */   private Set<String> nonDefaultableNsUris = new HashSet<String>();
/*  62 */   private Map<String, Integer> localNameIndexMap = new HashMap<String, Integer>();
/*  63 */   private QNameMap<Integer> elementQNameIndexMap = new QNameMap();
/*  64 */   private QNameMap<Integer> attributeQNameIndexMap = new QNameMap();
/*     */   
/*     */   public Name createElementName(QName name) {
/*  67 */     return createElementName(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */   
/*     */   public Name createElementName(String nsUri, String localName) {
/*  71 */     return createName(nsUri, localName, false, this.elementQNameIndexMap);
/*     */   }
/*     */   
/*     */   public Name createAttributeName(QName name) {
/*  75 */     return createAttributeName(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */   
/*     */   public Name createAttributeName(String nsUri, String localName) {
/*  79 */     assert nsUri.intern() == nsUri;
/*  80 */     assert localName.intern() == localName;
/*     */     
/*  82 */     if (nsUri.length() == 0) {
/*  83 */       return new Name(allocIndex(this.attributeQNameIndexMap, "", localName), -1, nsUri, allocIndex(this.localNameIndexMap, localName), localName, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     this.nonDefaultableNsUris.add(nsUri);
/*  92 */     return createName(nsUri, localName, true, this.attributeQNameIndexMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private Name createName(String nsUri, String localName, boolean isAttribute, QNameMap<Integer> map) {
/*  97 */     assert nsUri.intern() == nsUri;
/*  98 */     assert localName.intern() == localName;
/*     */     
/* 100 */     return new Name(allocIndex(map, nsUri, localName), allocIndex(this.uriIndexMap, nsUri), nsUri, allocIndex(this.localNameIndexMap, localName), localName, isAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int allocIndex(Map<String, Integer> map, String str) {
/* 110 */     Integer i = map.get(str);
/* 111 */     if (i == null) {
/* 112 */       i = Integer.valueOf(map.size());
/* 113 */       map.put(str, i);
/*     */     } 
/* 115 */     return i.intValue();
/*     */   }
/*     */   
/*     */   private int allocIndex(QNameMap<Integer> map, String nsUri, String localName) {
/* 119 */     Integer i = (Integer)map.get(nsUri, localName);
/* 120 */     if (i == null) {
/* 121 */       i = Integer.valueOf(map.size());
/* 122 */       map.put(nsUri, localName, i);
/*     */     } 
/* 124 */     return i.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameList conclude() {
/* 131 */     boolean[] nsUriCannotBeDefaulted = new boolean[this.uriIndexMap.size()];
/* 132 */     for (Map.Entry<String, Integer> e : this.uriIndexMap.entrySet()) {
/* 133 */       nsUriCannotBeDefaulted[((Integer)e.getValue()).intValue()] = this.nonDefaultableNsUris.contains(e.getKey());
/*     */     }
/*     */     
/* 136 */     NameList r = new NameList(list(this.uriIndexMap), nsUriCannotBeDefaulted, list(this.localNameIndexMap), this.elementQNameIndexMap.size(), this.attributeQNameIndexMap.size());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     this.uriIndexMap = null;
/* 144 */     this.localNameIndexMap = null;
/* 145 */     return r;
/*     */   }
/*     */   
/*     */   private String[] list(Map<String, Integer> map) {
/* 149 */     String[] r = new String[map.size()];
/* 150 */     for (Map.Entry<String, Integer> e : map.entrySet())
/* 151 */       r[((Integer)e.getValue()).intValue()] = e.getKey(); 
/* 152 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\NameBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */