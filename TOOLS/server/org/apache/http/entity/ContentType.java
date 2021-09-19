/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHeaderValueParser;
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
/*     */ @Immutable
/*     */ public final class ContentType
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7768694718232371896L;
/*  59 */   public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
/*     */   
/*  61 */   public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
/*     */   
/*  63 */   public static final ContentType APPLICATION_JSON = create("application/json", Consts.UTF_8);
/*     */   
/*  65 */   public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset)null);
/*     */   
/*  67 */   public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
/*     */   
/*  69 */   public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
/*     */   
/*  71 */   public static final ContentType APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
/*     */   
/*  73 */   public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
/*     */   
/*  75 */   public static final ContentType TEXT_HTML = create("text/html", Consts.ISO_8859_1);
/*     */   
/*  77 */   public static final ContentType TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
/*     */   
/*  79 */   public static final ContentType TEXT_XML = create("text/xml", Consts.ISO_8859_1);
/*     */   
/*  81 */   public static final ContentType WILDCARD = create("*/*", (Charset)null);
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
/*  86 */   public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String mimeType;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Charset charset;
/*     */ 
/*     */ 
/*     */   
/*     */   ContentType(String mimeType, Charset charset) {
/*  99 */     this.mimeType = mimeType;
/* 100 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public String getMimeType() {
/* 104 */     return this.mimeType;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/* 108 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     StringBuilder buf = new StringBuilder();
/* 118 */     buf.append(this.mimeType);
/* 119 */     if (this.charset != null) {
/* 120 */       buf.append("; charset=");
/* 121 */       buf.append(this.charset.name());
/*     */     } 
/* 123 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static boolean valid(String s) {
/* 127 */     for (int i = 0; i < s.length(); i++) {
/* 128 */       char ch = s.charAt(i);
/* 129 */       if (ch == '"' || ch == ',' || ch == ';') {
/* 130 */         return false;
/*     */       }
/*     */     } 
/* 133 */     return true;
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
/*     */   public static ContentType create(String mimeType, Charset charset) {
/* 145 */     if (mimeType == null) {
/* 146 */       throw new IllegalArgumentException("MIME type may not be null");
/*     */     }
/* 148 */     String type = mimeType.trim().toLowerCase(Locale.US);
/* 149 */     if (type.length() == 0) {
/* 150 */       throw new IllegalArgumentException("MIME type may not be empty");
/*     */     }
/* 152 */     if (!valid(type)) {
/* 153 */       throw new IllegalArgumentException("MIME type may not contain reserved characters");
/*     */     }
/* 155 */     return new ContentType(type, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentType create(String mimeType) {
/* 166 */     return new ContentType(mimeType, (Charset)null);
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
/*     */   public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
/* 180 */     return create(mimeType, (charset != null && charset.length() > 0) ? Charset.forName(charset) : null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ContentType create(HeaderElement helem) {
/* 185 */     String mimeType = helem.getName();
/* 186 */     String charset = null;
/* 187 */     NameValuePair param = helem.getParameterByName("charset");
/* 188 */     if (param != null) {
/* 189 */       charset = param.getValue();
/*     */     }
/* 191 */     return create(mimeType, charset);
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
/*     */   public static ContentType parse(String s) throws ParseException, UnsupportedCharsetException {
/* 204 */     if (s == null) {
/* 205 */       throw new IllegalArgumentException("Content type may not be null");
/*     */     }
/* 207 */     HeaderElement[] elements = BasicHeaderValueParser.parseElements(s, null);
/* 208 */     if (elements.length > 0) {
/* 209 */       return create(elements[0]);
/*     */     }
/* 211 */     throw new ParseException("Invalid content type: " + s);
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
/*     */ 
/*     */   
/*     */   public static ContentType get(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
/* 227 */     if (entity == null) {
/* 228 */       return null;
/*     */     }
/* 230 */     Header header = entity.getContentType();
/* 231 */     if (header != null) {
/* 232 */       HeaderElement[] elements = header.getElements();
/* 233 */       if (elements.length > 0) {
/* 234 */         return create(elements[0]);
/*     */       }
/*     */     } 
/* 237 */     return null;
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
/*     */   public static ContentType getOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
/* 251 */     ContentType contentType = get(entity);
/* 252 */     return (contentType != null) ? contentType : DEFAULT_TEXT;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\ContentType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */