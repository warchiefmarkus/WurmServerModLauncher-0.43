/*     */ package 1.0.com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class Util
/*     */ {
/*     */   public static InputSource getInputSource(String fileOrURL) {
/*     */     try {
/*  28 */       return new InputSource(escapeSpace((new URL(fileOrURL)).toExternalForm()));
/*  29 */     } catch (MalformedURLException e) {
/*  30 */       String url = (new File(fileOrURL)).getCanonicalFile().toURL().toExternalForm();
/*  31 */       return new InputSource(escapeSpace(url));
/*     */     }
/*  33 */     catch (Exception e) {
/*     */       
/*  35 */       return new InputSource(fileOrURL);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String escapeSpace(String url) {
/*  41 */     StringBuffer buf = new StringBuffer();
/*  42 */     for (int i = 0; i < url.length(); i++) {
/*     */       
/*  44 */       if (url.charAt(i) == ' ') {
/*  45 */         buf.append("%20");
/*     */       } else {
/*  47 */         buf.append(url.charAt(i));
/*     */       } 
/*  49 */     }  return buf.toString();
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
/*     */   public static String getPackageNameFromNamespaceURI(String nsUri, NameConverter nameConv) {
/*  63 */     int idx = nsUri.indexOf(':');
/*  64 */     if (idx >= 0) {
/*  65 */       String scheme = nsUri.substring(0, idx);
/*  66 */       if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("urn")) {
/*  67 */         nsUri = nsUri.substring(idx + 1);
/*     */       }
/*     */     } 
/*     */     
/*  71 */     ArrayList tokens = tokenize(nsUri, "/: ");
/*  72 */     if (tokens.size() == 0) {
/*  73 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  77 */     if (tokens.size() > 1) {
/*     */ 
/*     */ 
/*     */       
/*  81 */       String lastToken = tokens.get(tokens.size() - 1);
/*  82 */       idx = lastToken.lastIndexOf('.');
/*  83 */       if (idx > 0) {
/*  84 */         lastToken = lastToken.substring(0, idx);
/*  85 */         tokens.set(tokens.size() - 1, lastToken);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  90 */     String domain = tokens.get(0);
/*  91 */     idx = domain.indexOf(':');
/*  92 */     if (idx >= 0) domain = domain.substring(0, idx); 
/*  93 */     ArrayList r = reverse(tokenize(domain, "."));
/*  94 */     if (((String)r.get(r.size() - 1)).equalsIgnoreCase("www"))
/*     */     {
/*  96 */       r.remove(r.size() - 1);
/*     */     }
/*     */ 
/*     */     
/* 100 */     tokens.addAll(1, r);
/* 101 */     tokens.remove(0);
/*     */ 
/*     */     
/* 104 */     for (int i = 0; i < tokens.size(); i++) {
/*     */ 
/*     */       
/* 107 */       String token = tokens.get(i);
/* 108 */       token = removeIllegalIdentifierChars(token);
/*     */ 
/*     */       
/* 111 */       if (!JJavaName.isJavaIdentifier(token)) {
/* 112 */         token = new String("_" + token);
/*     */       }
/*     */       
/* 115 */       tokens.set(i, token.toLowerCase());
/*     */     } 
/*     */ 
/*     */     
/* 119 */     return combine(tokens, '.');
/*     */   }
/*     */   
/*     */   private static String removeIllegalIdentifierChars(String token) {
/* 123 */     StringBuffer newToken = new StringBuffer();
/* 124 */     for (int i = 0; i < token.length(); i++) {
/* 125 */       char c = token.charAt(i);
/*     */       
/* 127 */       if (i == 0 && !Character.isJavaIdentifierStart(c)) {
/*     */         
/* 129 */         newToken.append("_" + c);
/* 130 */       } else if (!Character.isJavaIdentifierPart(c)) {
/*     */         
/* 132 */         newToken.append('_');
/*     */       } else {
/*     */         
/* 135 */         newToken.append(c);
/*     */       } 
/*     */     } 
/* 138 */     return newToken.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static ArrayList tokenize(String str, String sep) {
/* 143 */     StringTokenizer tokens = new StringTokenizer(str, sep);
/* 144 */     ArrayList r = new ArrayList();
/*     */     
/* 146 */     while (tokens.hasMoreTokens()) {
/* 147 */       r.add(tokens.nextToken());
/*     */     }
/* 149 */     return r;
/*     */   }
/*     */   
/*     */   private static ArrayList reverse(List a) {
/* 153 */     ArrayList r = new ArrayList();
/*     */     
/* 155 */     for (int i = a.size() - 1; i >= 0; i--) {
/* 156 */       r.add(a.get(i));
/*     */     }
/* 158 */     return r;
/*     */   }
/*     */   
/*     */   private static String combine(List r, char sep) {
/* 162 */     StringBuffer buf = new StringBuffer(r.get(0));
/*     */     
/* 164 */     for (int i = 1; i < r.size(); i++) {
/* 165 */       buf.append(sep);
/* 166 */       buf.append(r.get(i));
/*     */     } 
/*     */     
/* 169 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\Util.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */