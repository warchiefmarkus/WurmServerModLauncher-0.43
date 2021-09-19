/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.Messages;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
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
/*     */ public final class NGCCRuntimeEx
/*     */   extends NGCCRuntime
/*     */ {
/*     */   public final JCodeModel codeModel;
/*     */   public final ErrorHandler errorHandler;
/*     */   public final Options options;
/*     */   public BindInfo currentBindInfo;
/*     */   static final String ERR_UNIMPLEMENTED = "NGCCRuntimeEx.Unimplemented";
/*     */   static final String ERR_UNSUPPORTED = "NGCCRuntimeEx.Unsupported";
/*     */   static final String ERR_UNDEFINED_PREFIX = "NGCCRuntimeEx.UndefinedPrefix";
/*     */   
/*     */   public NGCCRuntimeEx(JCodeModel _codeModel, Options opts, ErrorHandler _errorHandler) {
/*  42 */     this.codeModel = _codeModel;
/*  43 */     this.options = opts;
/*  44 */     this.errorHandler = _errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JType getType(String typeName) throws SAXException {
/*  51 */     return TypeUtil.getType(this.codeModel, typeName, this.errorHandler, getLocator());
/*     */   }
/*     */   
/*     */   public final Locator copyLocator() {
/*  55 */     return new LocatorImpl(getLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String truncateDocComment(String s) {
/*  63 */     StringBuffer buf = new StringBuffer(s.length());
/*  64 */     StringTokenizer tokens = new StringTokenizer(s, "\n");
/*  65 */     while (tokens.hasMoreTokens()) {
/*  66 */       buf.append(tokens.nextToken().trim());
/*  67 */       if (tokens.hasMoreTokens())
/*  68 */         buf.append('\n'); 
/*     */     } 
/*  70 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String escapeMarkup(String s) {
/*  77 */     StringBuffer buf = new StringBuffer(s.length());
/*  78 */     for (int i = 0; i < s.length(); i++) {
/*  79 */       char ch = s.charAt(i);
/*  80 */       switch (ch) {
/*     */         case '<':
/*  82 */           buf.append("&lt;");
/*     */           break;
/*     */         case '&':
/*  85 */           buf.append("&amp;");
/*     */           break;
/*     */         default:
/*  88 */           buf.append(ch);
/*     */           break;
/*     */       } 
/*     */     } 
/*  92 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public final boolean parseBoolean(String str) {
/*  96 */     str = str.trim();
/*  97 */     if (str.equals("true") || str.equals("1")) return true; 
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   public final QName parseQName(String str) throws SAXException {
/* 102 */     int idx = str.indexOf(':');
/* 103 */     if (idx < 0) {
/* 104 */       String str1 = resolveNamespacePrefix("");
/*     */       
/* 106 */       return new QName(str1, str);
/*     */     } 
/* 108 */     String prefix = str.substring(0, idx);
/* 109 */     String uri = resolveNamespacePrefix(prefix);
/* 110 */     if (uri == null) {
/*     */       
/* 112 */       this.errorHandler.error(new SAXParseException(Messages.format("NGCCRuntimeEx.UndefinedPrefix", prefix), getLocator()));
/*     */       
/* 114 */       uri = "undefined";
/*     */     } 
/* 116 */     return new QName(uri, str.substring(idx + 1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reportUnimplementedFeature(String name) throws SAXException {
/* 122 */     this.errorHandler.warning(new SAXParseException(Messages.format("NGCCRuntimeEx.Unimplemented", name), getLocator()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reportUnsupportedFeature(String name) throws SAXException {
/* 128 */     this.errorHandler.warning(new SAXParseException(Messages.format("NGCCRuntimeEx.Unsupported", name), getLocator()));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\NGCCRuntimeEx.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */