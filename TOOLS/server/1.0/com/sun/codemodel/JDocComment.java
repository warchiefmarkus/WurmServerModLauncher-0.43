/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JVar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDocComment
/*     */   implements JGenerable
/*     */ {
/*  19 */   private String comment = "";
/*     */ 
/*     */   
/*  22 */   private final Map atParams = new HashMap();
/*     */ 
/*     */   
/*  25 */   private final Map atThrows = new HashMap();
/*     */ 
/*     */   
/*  28 */   private String atReturn = null;
/*     */ 
/*     */   
/*  31 */   private String atDeprecated = null;
/*     */ 
/*     */   
/*     */   public String getComment() {
/*  35 */     return this.comment;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment setComment(String comment) {
/*  40 */     this.comment = comment;
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment appendComment(String comment) {
/*  46 */     this.comment += comment;
/*  47 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment addParam(String param, String comment) {
/*  54 */     String s = (String)this.atParams.get(param);
/*  55 */     if (s != null) comment = s + comment;
/*     */     
/*  57 */     this.atParams.put(param, comment);
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment addParam(JVar param, String comment) {
/*  65 */     return addParam(param.name, comment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment addThrows(String exception, String comment) {
/*  72 */     String s = (String)this.atThrows.get(exception);
/*  73 */     if (s != null) comment = s + comment;
/*     */     
/*  75 */     this.atThrows.put(exception, comment);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment addThrows(Class exception, String comment) {
/*  83 */     return addThrows(exception.getName(), comment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment addThrows(JClass exception, String comment) {
/*  90 */     return addThrows(exception.fullName(), comment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDocComment addReturn(String comment) {
/*  97 */     if (this.atReturn == null) { this.atReturn = comment; }
/*  98 */     else { this.atReturn += comment; }
/*  99 */      return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeprecated(String comment) {
/* 107 */     this.atDeprecated = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(JFormatter f) {
/* 115 */     f.p("/**").nl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     format(f, this.comment);
/*     */     
/* 123 */     f.p(" * ").nl();
/* 124 */     for (Iterator iterator1 = this.atParams.entrySet().iterator(); iterator1.hasNext(); ) {
/* 125 */       Map.Entry e = iterator1.next();
/* 126 */       format(f, "@param " + e.getKey(), (String)e.getValue());
/*     */     } 
/* 128 */     if (this.atReturn != null)
/* 129 */       format(f, "@return", this.atReturn); 
/* 130 */     for (Iterator i = this.atThrows.entrySet().iterator(); i.hasNext(); ) {
/* 131 */       Map.Entry e = i.next();
/* 132 */       format(f, "@throws " + e.getKey(), (String)e.getValue());
/*     */     } 
/* 134 */     if (this.atDeprecated != null)
/* 135 */       format(f, "@deprecated", this.atDeprecated); 
/* 136 */     f.p(" */").nl();
/*     */   }
/*     */ 
/*     */   
/*     */   private void format(JFormatter f, String key, String s) {
/* 141 */     f.p(" * " + key).nl(); int idx;
/* 142 */     while ((idx = s.indexOf('\n')) != -1) {
/* 143 */       f.p(" *     " + s.substring(0, idx)).nl();
/* 144 */       s = s.substring(idx + 1);
/*     */     } 
/* 146 */     if (s.length() != 0)
/* 147 */       f.p(" *     " + s).nl(); 
/*     */   }
/*     */   
/*     */   private void format(JFormatter f, String s) {
/*     */     int idx;
/* 152 */     while ((idx = s.indexOf('\n')) != -1) {
/* 153 */       f.p(" * " + s.substring(0, idx)).nl();
/* 154 */       s = s.substring(idx + 1);
/*     */     } 
/* 156 */     if (s.length() != 0)
/* 157 */       f.p(" * " + s).nl(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JDocComment.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */