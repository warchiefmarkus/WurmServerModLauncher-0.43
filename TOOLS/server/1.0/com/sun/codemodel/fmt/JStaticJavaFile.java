/*     */ package 1.0.com.sun.codemodel.fmt;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
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
/*     */ public final class JStaticJavaFile
/*     */   extends JResourceFile
/*     */ {
/*     */   private final JPackage pkg;
/*     */   private final String className;
/*     */   private final URL source;
/*     */   private final JStaticClass clazz;
/*     */   private final LineFilter filter;
/*     */   
/*     */   public JStaticJavaFile(JPackage _pkg, String className, String _resourceName) {
/*  55 */     this(_pkg, className, com.sun.codemodel.fmt.JStaticJavaFile.class.getClassLoader().getResource(_resourceName), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JStaticJavaFile(JPackage _pkg, String _className, URL _source, LineFilter _filter) {
/*  60 */     super(_className + ".java");
/*  61 */     if (_source == null) throw new NullPointerException(); 
/*  62 */     this.pkg = _pkg;
/*  63 */     this.clazz = new JStaticClass(this);
/*  64 */     this.className = _className;
/*  65 */     this.source = _source;
/*  66 */     this.filter = _filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass getJClass() {
/*  73 */     return (JClass)this.clazz;
/*     */   }
/*     */   
/*     */   protected void build(OutputStream os) throws IOException {
/*  77 */     InputStream is = this.source.openStream();
/*     */     
/*  79 */     BufferedReader r = new BufferedReader(new InputStreamReader(is));
/*  80 */     PrintWriter w = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
/*  81 */     LineFilter filter = createLineFilter();
/*  82 */     int lineNumber = 1;
/*     */     
/*     */     try {
/*     */       String line;
/*  86 */       while ((line = r.readLine()) != null) {
/*  87 */         line = filter.process(line);
/*  88 */         if (line != null)
/*  89 */           w.println(line); 
/*  90 */         lineNumber++;
/*     */       } 
/*  92 */     } catch (ParseException e) {
/*  93 */       throw new IOException("unable to process " + this.source + " line:" + lineNumber + "\n" + e.getMessage());
/*     */     } 
/*     */     
/*  96 */     w.close();
/*  97 */     r.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LineFilter createLineFilter() {
/* 108 */     Object object = new Object(this);
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
/* 119 */     if (this.filter != null) {
/* 120 */       return (LineFilter)new ChainFilter(this, this.filter, (LineFilter)object);
/*     */     }
/* 122 */     return (LineFilter)object;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\fmt\JStaticJavaFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */