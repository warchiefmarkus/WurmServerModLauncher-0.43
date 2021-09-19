/*     */ package 1.0.com.sun.tools.xjc.writer;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Comparator;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
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
/*     */ public class SignatureWriter
/*     */ {
/*     */   private final ClassItem[] classes;
/*     */   private final Hashtable classSet;
/*     */   private final Writer out;
/*     */   private int indent;
/*     */   
/*     */   public static void write(AnnotatedGrammar grammar, Writer out) throws IOException {
/*  36 */     (new com.sun.tools.xjc.writer.SignatureWriter(grammar, out)).dump();
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
/*     */   private SignatureWriter(AnnotatedGrammar grammar, Writer out) {
/*  50 */     this.classSet = new Hashtable();
/*     */ 
/*     */     
/*  53 */     this.indent = 0; this.out = out; this.classes = grammar.getClasses();
/*     */     for (int i = 0; i < this.classes.length; i++)
/*  55 */       this.classSet.put(this.classes[i].getTypeAsDefined(), this.classes[i]);  } private void printIndent() throws IOException { for (int i = 0; i < this.indent; i++)
/*  56 */       this.out.write("  ");  }
/*     */   
/*     */   private void println(String s) throws IOException {
/*  59 */     printIndent();
/*  60 */     this.out.write(s);
/*  61 */     this.out.write(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dump() throws IOException {
/*  67 */     Set packages = new TreeSet((Comparator)new Object(this));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     for (int i = 0; i < this.classes.length; i++) {
/*  74 */       JDefinedClass cls = this.classes[i].getTypeAsDefined();
/*  75 */       packages.add(cls._package());
/*     */     } 
/*     */     
/*  78 */     for (Iterator itr = packages.iterator(); itr.hasNext();) {
/*  79 */       dump(itr.next());
/*     */     }
/*  81 */     this.out.flush();
/*     */   }
/*     */   
/*     */   private void dump(JPackage pkg) throws IOException {
/*  85 */     println("package " + pkg.name() + " {");
/*  86 */     this.indent++;
/*  87 */     dumpChildren((JClassContainer)pkg);
/*  88 */     this.indent--;
/*  89 */     println("}");
/*     */   }
/*     */   
/*     */   private void dumpChildren(JClassContainer cont) throws IOException {
/*  93 */     Iterator itr = cont.classes();
/*  94 */     while (itr.hasNext()) {
/*  95 */       JDefinedClass cls = itr.next();
/*  96 */       ClassItem ci = (ClassItem)this.classSet.get(cls);
/*  97 */       if (ci != null)
/*  98 */         dump(ci); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dump(ClassItem ci) throws IOException {
/* 103 */     JDefinedClass cls = ci.getTypeAsDefined();
/*     */     
/* 105 */     StringBuffer buf = new StringBuffer();
/* 106 */     buf.append("interface ");
/* 107 */     buf.append(cls.name());
/*     */     
/* 109 */     boolean first = true;
/* 110 */     Iterator itr = cls._implements();
/* 111 */     while (itr.hasNext()) {
/* 112 */       if (first) {
/* 113 */         buf.append(" extends ");
/* 114 */         first = false;
/*     */       } else {
/* 116 */         buf.append(", ");
/*     */       } 
/* 118 */       buf.append(printName((JType)itr.next()));
/*     */     } 
/* 120 */     buf.append(" {");
/* 121 */     println(buf.toString());
/* 122 */     this.indent++;
/*     */ 
/*     */     
/* 125 */     FieldUse[] fu = ci.getDeclaredFieldUses();
/* 126 */     for (int i = 0; i < fu.length; i++) {
/*     */       String type;
/* 128 */       if (!(fu[i]).multiplicity.isAtMostOnce()) {
/* 129 */         type = "List<" + printName((fu[i]).type) + ">";
/*     */       } else {
/* 131 */         type = printName((fu[i]).type);
/* 132 */       }  println(type + " " + (fu[i]).name + ";");
/*     */     } 
/*     */     
/* 135 */     dumpChildren((JClassContainer)cls);
/*     */     
/* 137 */     this.indent--;
/* 138 */     println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   private String printName(JType t) {
/* 143 */     String name = t.fullName();
/* 144 */     if (name.startsWith("java.lang."))
/* 145 */       name = name.substring(10); 
/* 146 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\writer\SignatureWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */