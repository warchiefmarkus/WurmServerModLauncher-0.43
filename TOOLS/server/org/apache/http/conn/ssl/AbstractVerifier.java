/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
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
/*     */ @Immutable
/*     */ public abstract class AbstractVerifier
/*     */   implements X509HostnameVerifier
/*     */ {
/*  70 */   private static final String[] BAD_COUNTRY_2LDS = new String[] { "ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  76 */     Arrays.sort((Object[])BAD_COUNTRY_2LDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void verify(String host, SSLSocket ssl) throws IOException {
/*  85 */     if (host == null) {
/*  86 */       throw new NullPointerException("host to verify is null");
/*     */     }
/*     */     
/*  89 */     SSLSession session = ssl.getSession();
/*  90 */     if (session == null) {
/*     */ 
/*     */ 
/*     */       
/*  94 */       InputStream in = ssl.getInputStream();
/*  95 */       in.available();
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
/* 114 */       session = ssl.getSession();
/* 115 */       if (session == null) {
/*     */ 
/*     */         
/* 118 */         ssl.startHandshake();
/*     */ 
/*     */ 
/*     */         
/* 122 */         session = ssl.getSession();
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     Certificate[] certs = session.getPeerCertificates();
/* 127 */     X509Certificate x509 = (X509Certificate)certs[0];
/* 128 */     verify(host, x509);
/*     */   }
/*     */   
/*     */   public final boolean verify(String host, SSLSession session) {
/*     */     try {
/* 133 */       Certificate[] certs = session.getPeerCertificates();
/* 134 */       X509Certificate x509 = (X509Certificate)certs[0];
/* 135 */       verify(host, x509);
/* 136 */       return true;
/*     */     }
/* 138 */     catch (SSLException e) {
/* 139 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void verify(String host, X509Certificate cert) throws SSLException {
/* 145 */     String[] cns = getCNs(cert);
/* 146 */     String[] subjectAlts = getSubjectAlts(cert, host);
/* 147 */     verify(host, cns, subjectAlts);
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
/*     */   public final void verify(String host, String[] cns, String[] subjectAlts, boolean strictWithSubDomains) throws SSLException {
/* 159 */     LinkedList<String> names = new LinkedList<String>();
/* 160 */     if (cns != null && cns.length > 0 && cns[0] != null) {
/* 161 */       names.add(cns[0]);
/*     */     }
/* 163 */     if (subjectAlts != null) {
/* 164 */       for (String subjectAlt : subjectAlts) {
/* 165 */         if (subjectAlt != null) {
/* 166 */           names.add(subjectAlt);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 171 */     if (names.isEmpty()) {
/* 172 */       String msg = "Certificate for <" + host + "> doesn't contain CN or DNS subjectAlt";
/* 173 */       throw new SSLException(msg);
/*     */     } 
/*     */ 
/*     */     
/* 177 */     StringBuilder buf = new StringBuilder();
/*     */ 
/*     */ 
/*     */     
/* 181 */     String hostName = host.trim().toLowerCase(Locale.US);
/* 182 */     boolean match = false;
/* 183 */     for (Iterator<String> it = names.iterator(); it.hasNext(); ) {
/*     */       
/* 185 */       String cn = it.next();
/* 186 */       cn = cn.toLowerCase(Locale.US);
/*     */       
/* 188 */       buf.append(" <");
/* 189 */       buf.append(cn);
/* 190 */       buf.append('>');
/* 191 */       if (it.hasNext()) {
/* 192 */         buf.append(" OR");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       String[] parts = cn.split("\\.");
/* 199 */       boolean doWildcard = (parts.length >= 3 && parts[0].endsWith("*") && acceptableCountryWildcard(cn) && !isIPAddress(host));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 204 */       if (doWildcard) {
/* 205 */         String firstpart = parts[0];
/* 206 */         if (firstpart.length() > 1) {
/* 207 */           String prefix = firstpart.substring(0, firstpart.length() - 1);
/* 208 */           String suffix = cn.substring(firstpart.length());
/* 209 */           String hostSuffix = hostName.substring(prefix.length());
/* 210 */           match = (hostName.startsWith(prefix) && hostSuffix.endsWith(suffix));
/*     */         } else {
/* 212 */           match = hostName.endsWith(cn.substring(1));
/*     */         } 
/* 214 */         if (match && strictWithSubDomains)
/*     */         {
/*     */           
/* 217 */           match = (countDots(hostName) == countDots(cn));
/*     */         }
/*     */       } else {
/* 220 */         match = hostName.equals(cn);
/*     */       } 
/* 222 */       if (match) {
/*     */         break;
/*     */       }
/*     */     } 
/* 226 */     if (!match) {
/* 227 */       throw new SSLException("hostname in certificate didn't match: <" + host + "> !=" + buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean acceptableCountryWildcard(String cn) {
/* 232 */     String[] parts = cn.split("\\.");
/* 233 */     if (parts.length != 3 || parts[2].length() != 2) {
/* 234 */       return true;
/*     */     }
/* 236 */     return (Arrays.binarySearch((Object[])BAD_COUNTRY_2LDS, parts[1]) < 0);
/*     */   }
/*     */   
/*     */   public static String[] getCNs(X509Certificate cert) {
/* 240 */     LinkedList<String> cnList = new LinkedList<String>();
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
/* 264 */     String subjectPrincipal = cert.getSubjectX500Principal().toString();
/* 265 */     StringTokenizer st = new StringTokenizer(subjectPrincipal, ",+");
/* 266 */     while (st.hasMoreTokens()) {
/* 267 */       String tok = st.nextToken().trim();
/* 268 */       if (tok.length() > 3 && 
/* 269 */         tok.substring(0, 3).equalsIgnoreCase("CN=")) {
/* 270 */         cnList.add(tok.substring(3));
/*     */       }
/*     */     } 
/*     */     
/* 274 */     if (!cnList.isEmpty()) {
/* 275 */       String[] cns = new String[cnList.size()];
/* 276 */       cnList.toArray(cns);
/* 277 */       return cns;
/*     */     } 
/* 279 */     return null;
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
/*     */   private static String[] getSubjectAlts(X509Certificate cert, String hostname) {
/*     */     int subjectType;
/* 294 */     if (isIPAddress(hostname)) {
/* 295 */       subjectType = 7;
/*     */     } else {
/* 297 */       subjectType = 2;
/*     */     } 
/*     */     
/* 300 */     LinkedList<String> subjectAltList = new LinkedList<String>();
/* 301 */     Collection<List<?>> c = null;
/*     */     try {
/* 303 */       c = cert.getSubjectAlternativeNames();
/*     */     }
/* 305 */     catch (CertificateParsingException cpe) {}
/*     */     
/* 307 */     if (c != null) {
/* 308 */       for (List<?> aC : c) {
/* 309 */         List<?> list = aC;
/* 310 */         int type = ((Integer)list.get(0)).intValue();
/* 311 */         if (type == subjectType) {
/* 312 */           String s = (String)list.get(1);
/* 313 */           subjectAltList.add(s);
/*     */         } 
/*     */       } 
/*     */     }
/* 317 */     if (!subjectAltList.isEmpty()) {
/* 318 */       String[] subjectAlts = new String[subjectAltList.size()];
/* 319 */       subjectAltList.toArray(subjectAlts);
/* 320 */       return subjectAlts;
/*     */     } 
/* 322 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getDNSSubjectAlts(X509Certificate cert) {
/* 341 */     return getSubjectAlts(cert, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countDots(String s) {
/* 350 */     int count = 0;
/* 351 */     for (int i = 0; i < s.length(); i++) {
/* 352 */       if (s.charAt(i) == '.') {
/* 353 */         count++;
/*     */       }
/*     */     } 
/* 356 */     return count;
/*     */   }
/*     */   
/*     */   private static boolean isIPAddress(String hostname) {
/* 360 */     return (hostname != null && (InetAddressUtils.isIPv4Address(hostname) || InetAddressUtils.isIPv6Address(hostname)));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\ssl\AbstractVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */