/*    */ package org.kohsuke.rngom.parse.compact;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PushbackInputStream;
/*    */ import java.io.Reader;
/*    */ import java.net.URL;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.IncludedGrammar;
/*    */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*    */ import org.kohsuke.rngom.ast.builder.Scope;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*    */ import org.kohsuke.rngom.parse.Parseable;
/*    */ import org.kohsuke.rngom.xml.util.EncodingMap;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompactParseable
/*    */   implements Parseable
/*    */ {
/*    */   private final InputSource in;
/*    */   private final ErrorHandler eh;
/*    */   
/*    */   public CompactParseable(InputSource in, ErrorHandler eh) {
/* 29 */     this.in = in;
/* 30 */     this.eh = eh;
/*    */   }
/*    */   
/*    */   public ParsedPattern parse(SchemaBuilder sb) throws BuildException, IllegalSchemaException {
/* 34 */     ParsedPattern p = (new CompactSyntax(this, makeReader(this.in), this.in.getSystemId(), sb, this.eh, "")).parse(null);
/* 35 */     return sb.expandPattern(p);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParsedPattern parseInclude(String uri, SchemaBuilder sb, IncludedGrammar g, String inheritedNs) throws BuildException, IllegalSchemaException {
/* 40 */     InputSource tem = new InputSource(uri);
/* 41 */     tem.setEncoding(this.in.getEncoding());
/* 42 */     return (new CompactSyntax(this, makeReader(tem), uri, sb, this.eh, inheritedNs)).parseInclude(g);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParsedPattern parseExternal(String uri, SchemaBuilder sb, Scope scope, String inheritedNs) throws BuildException, IllegalSchemaException {
/* 47 */     InputSource tem = new InputSource(uri);
/* 48 */     tem.setEncoding(this.in.getEncoding());
/* 49 */     return (new CompactSyntax(this, makeReader(tem), uri, sb, this.eh, inheritedNs)).parse(scope);
/*    */   }
/*    */   
/* 52 */   private static final String UTF8 = EncodingMap.getJavaName("UTF-8");
/* 53 */   private static final String UTF16 = EncodingMap.getJavaName("UTF-16");
/*    */   
/*    */   private static Reader makeReader(InputSource is) throws BuildException {
/*    */     try {
/* 57 */       Reader r = is.getCharacterStream();
/* 58 */       if (r == null) {
/* 59 */         InputStream in = is.getByteStream();
/* 60 */         if (in == null) {
/* 61 */           String systemId = is.getSystemId();
/* 62 */           in = (new URL(systemId)).openStream();
/*    */         } 
/* 64 */         String encoding = is.getEncoding();
/* 65 */         if (encoding == null) {
/* 66 */           PushbackInputStream pb = new PushbackInputStream(in, 2);
/* 67 */           encoding = detectEncoding(pb);
/* 68 */           in = pb;
/*    */         } 
/* 70 */         r = new InputStreamReader(in, encoding);
/*    */       } 
/* 72 */       return r;
/*    */     }
/* 74 */     catch (IOException e) {
/* 75 */       throw new BuildException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static String detectEncoding(PushbackInputStream in) throws IOException {
/* 80 */     String encoding = UTF8;
/* 81 */     int b1 = in.read();
/* 82 */     if (b1 != -1) {
/* 83 */       int b2 = in.read();
/* 84 */       if (b2 != -1) {
/* 85 */         in.unread(b2);
/* 86 */         if ((b1 == 255 && b2 == 254) || (b1 == 254 && b2 == 255))
/* 87 */           encoding = UTF16; 
/*    */       } 
/* 89 */       in.unread(b1);
/*    */     } 
/* 91 */     return encoding;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\compact\CompactParseable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */