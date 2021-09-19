/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class JDocComment
/*     */   extends JCommentPart
/*     */   implements JGenerable
/*     */ {
/*  39 */   private final Map<String, JCommentPart> atParams = new HashMap<String, JCommentPart>();
/*     */ 
/*     */   
/*  42 */   private final Map<String, Map<String, String>> atXdoclets = new HashMap<String, Map<String, String>>();
/*     */ 
/*     */   
/*  45 */   private final Map<JClass, JCommentPart> atThrows = new HashMap<JClass, JCommentPart>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private JCommentPart atReturn = null;
/*     */ 
/*     */   
/*  53 */   private JCommentPart atDeprecated = null;
/*     */   
/*     */   private final JCodeModel owner;
/*     */   private static final String INDENT = " *     ";
/*     */   
/*     */   public JDocComment(JCodeModel owner) {
/*  59 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public JDocComment append(Object o) {
/*  63 */     add(o);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addParam(String param) {
/*  71 */     JCommentPart p = this.atParams.get(param);
/*  72 */     if (p == null)
/*  73 */       this.atParams.put(param, p = new JCommentPart()); 
/*  74 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addParam(JVar param) {
/*  81 */     return addParam(param.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addThrows(Class exception) {
/*  89 */     return addThrows(this.owner.ref(exception));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addThrows(JClass exception) {
/*  96 */     JCommentPart p = this.atThrows.get(exception);
/*  97 */     if (p == null)
/*  98 */       this.atThrows.put(exception, p = new JCommentPart()); 
/*  99 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addReturn() {
/* 106 */     if (this.atReturn == null)
/* 107 */       this.atReturn = new JCommentPart(); 
/* 108 */     return this.atReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addDeprecated() {
/* 115 */     if (this.atDeprecated == null)
/* 116 */       this.atDeprecated = new JCommentPart(); 
/* 117 */     return this.atDeprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> addXdoclet(String name) {
/* 124 */     Map<String, String> p = this.atXdoclets.get(name);
/* 125 */     if (p == null)
/* 126 */       this.atXdoclets.put(name, p = new HashMap<String, String>()); 
/* 127 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> addXdoclet(String name, Map<String, String> attributes) {
/* 134 */     Map<String, String> p = this.atXdoclets.get(name);
/* 135 */     if (p == null)
/* 136 */       this.atXdoclets.put(name, p = new HashMap<String, String>()); 
/* 137 */     p.putAll(attributes);
/* 138 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> addXdoclet(String name, String attribute, String value) {
/* 145 */     Map<String, String> p = this.atXdoclets.get(name);
/* 146 */     if (p == null)
/* 147 */       this.atXdoclets.put(name, p = new HashMap<String, String>()); 
/* 148 */     p.put(attribute, value);
/* 149 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(JFormatter f) {
/* 156 */     f.p("/**").nl();
/*     */     
/* 158 */     format(f, " * ");
/*     */     
/* 160 */     f.p(" * ").nl();
/* 161 */     for (Map.Entry<String, JCommentPart> e : this.atParams.entrySet()) {
/* 162 */       f.p(" * @param ").p(e.getKey()).nl();
/* 163 */       ((JCommentPart)e.getValue()).format(f, " *     ");
/*     */     } 
/* 165 */     if (this.atReturn != null) {
/* 166 */       f.p(" * @return").nl();
/* 167 */       this.atReturn.format(f, " *     ");
/*     */     } 
/* 169 */     for (Map.Entry<JClass, JCommentPart> e : this.atThrows.entrySet()) {
/* 170 */       f.p(" * @throws ").t(e.getKey()).nl();
/* 171 */       ((JCommentPart)e.getValue()).format(f, " *     ");
/*     */     } 
/* 173 */     if (this.atDeprecated != null) {
/* 174 */       f.p(" * @deprecated").nl();
/* 175 */       this.atDeprecated.format(f, " *     ");
/*     */     } 
/* 177 */     for (Map.Entry<String, Map<String, String>> e : this.atXdoclets.entrySet()) {
/* 178 */       f.p(" * @").p(e.getKey());
/* 179 */       if (e.getValue() != null) {
/* 180 */         for (Map.Entry<String, String> a : (Iterable<Map.Entry<String, String>>)((Map)e.getValue()).entrySet()) {
/* 181 */           f.p(" ").p(a.getKey()).p("= \"").p(a.getValue()).p("\"");
/*     */         }
/*     */       }
/* 184 */       f.nl();
/*     */     } 
/* 186 */     f.p(" */").nl();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JDocComment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */