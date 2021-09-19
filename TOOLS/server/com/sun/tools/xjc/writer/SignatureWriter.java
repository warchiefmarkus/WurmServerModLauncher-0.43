/*     */ package com.sun.tools.xjc.writer;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ public class SignatureWriter
/*     */ {
/*     */   private final Collection<? extends ClassOutline> classes;
/*     */   private final Map<JDefinedClass, ClassOutline> classSet;
/*     */   private final Writer out;
/*     */   private int indent;
/*     */   
/*     */   public static void write(Outline model, Writer out) throws IOException {
/*  69 */     (new SignatureWriter(model, out)).dump();
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
/*     */   private SignatureWriter(Outline model, Writer out) {
/*  83 */     this.classSet = new HashMap<JDefinedClass, ClassOutline>();
/*     */ 
/*     */     
/*  86 */     this.indent = 0; this.out = out; this.classes = model.getClasses();
/*     */     for (ClassOutline ci : this.classes)
/*  88 */       this.classSet.put(ci.ref, ci);  } private void printIndent() throws IOException { for (int i = 0; i < this.indent; i++)
/*  89 */       this.out.write("  ");  }
/*     */   
/*     */   private void println(String s) throws IOException {
/*  92 */     printIndent();
/*  93 */     this.out.write(s);
/*  94 */     this.out.write(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dump() throws IOException {
/* 100 */     Set<JPackage> packages = new TreeSet<JPackage>(new Comparator<JPackage>() {
/*     */           public int compare(JPackage lhs, JPackage rhs) {
/* 102 */             return lhs.name().compareTo(rhs.name());
/*     */           }
/*     */         });
/* 105 */     for (ClassOutline ci : this.classes) {
/* 106 */       packages.add(ci._package()._package());
/*     */     }
/* 108 */     for (JPackage pkg : packages) {
/* 109 */       dump(pkg);
/*     */     }
/* 111 */     this.out.flush();
/*     */   }
/*     */   
/*     */   private void dump(JPackage pkg) throws IOException {
/* 115 */     println("package " + pkg.name() + " {");
/* 116 */     this.indent++;
/* 117 */     dumpChildren((JClassContainer)pkg);
/* 118 */     this.indent--;
/* 119 */     println("}");
/*     */   }
/*     */   
/*     */   private void dumpChildren(JClassContainer cont) throws IOException {
/* 123 */     Iterator<JDefinedClass> itr = cont.classes();
/* 124 */     while (itr.hasNext()) {
/* 125 */       JDefinedClass cls = itr.next();
/* 126 */       ClassOutline ci = this.classSet.get(cls);
/* 127 */       if (ci != null)
/* 128 */         dump(ci); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dump(ClassOutline ci) throws IOException {
/* 133 */     JDefinedClass cls = ci.implClass;
/*     */     
/* 135 */     StringBuilder buf = new StringBuilder();
/* 136 */     buf.append("interface ");
/* 137 */     buf.append(cls.name());
/*     */     
/* 139 */     boolean first = true;
/* 140 */     Iterator<JClass> itr = cls._implements();
/* 141 */     while (itr.hasNext()) {
/* 142 */       if (first) {
/* 143 */         buf.append(" extends ");
/* 144 */         first = false;
/*     */       } else {
/* 146 */         buf.append(", ");
/*     */       } 
/* 148 */       buf.append(printName((JType)itr.next()));
/*     */     } 
/* 150 */     buf.append(" {");
/* 151 */     println(buf.toString());
/* 152 */     this.indent++;
/*     */ 
/*     */     
/* 155 */     for (FieldOutline fo : ci.getDeclaredFields()) {
/* 156 */       String type = printName(fo.getRawType());
/* 157 */       println(type + ' ' + fo.getPropertyInfo().getName(true) + ';');
/*     */     } 
/*     */     
/* 160 */     dumpChildren((JClassContainer)cls);
/*     */     
/* 162 */     this.indent--;
/* 163 */     println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   private String printName(JType t) {
/* 168 */     String name = t.fullName();
/* 169 */     if (name.startsWith("java.lang."))
/* 170 */       name = name.substring(10); 
/* 171 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\writer\SignatureWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */