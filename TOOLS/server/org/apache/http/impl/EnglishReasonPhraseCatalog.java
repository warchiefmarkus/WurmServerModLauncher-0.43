/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.ReasonPhraseCatalog;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ public class EnglishReasonPhraseCatalog
/*     */   implements ReasonPhraseCatalog
/*     */ {
/*  53 */   public static final EnglishReasonPhraseCatalog INSTANCE = new EnglishReasonPhraseCatalog();
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
/*     */   public String getReason(int status, Locale loc) {
/*  75 */     if (status < 100 || status >= 600) {
/*  76 */       throw new IllegalArgumentException("Unknown category for status code " + status + ".");
/*     */     }
/*     */ 
/*     */     
/*  80 */     int category = status / 100;
/*  81 */     int subcode = status - 100 * category;
/*     */     
/*  83 */     String reason = null;
/*  84 */     if ((REASON_PHRASES[category]).length > subcode) {
/*  85 */       reason = REASON_PHRASES[category][subcode];
/*     */     }
/*  87 */     return reason;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final String[][] REASON_PHRASES = new String[][] { null, new String[3], new String[8], new String[8], new String[25], new String[8] };
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
/*     */   private static void setReason(int status, String reason) {
/* 111 */     int category = status / 100;
/* 112 */     int subcode = status - 100 * category;
/* 113 */     REASON_PHRASES[category][subcode] = reason;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 122 */     setReason(200, "OK");
/*     */     
/* 124 */     setReason(201, "Created");
/*     */     
/* 126 */     setReason(202, "Accepted");
/*     */     
/* 128 */     setReason(204, "No Content");
/*     */     
/* 130 */     setReason(301, "Moved Permanently");
/*     */     
/* 132 */     setReason(302, "Moved Temporarily");
/*     */     
/* 134 */     setReason(304, "Not Modified");
/*     */     
/* 136 */     setReason(400, "Bad Request");
/*     */     
/* 138 */     setReason(401, "Unauthorized");
/*     */     
/* 140 */     setReason(403, "Forbidden");
/*     */     
/* 142 */     setReason(404, "Not Found");
/*     */     
/* 144 */     setReason(500, "Internal Server Error");
/*     */     
/* 146 */     setReason(501, "Not Implemented");
/*     */     
/* 148 */     setReason(502, "Bad Gateway");
/*     */     
/* 150 */     setReason(503, "Service Unavailable");
/*     */ 
/*     */ 
/*     */     
/* 154 */     setReason(100, "Continue");
/*     */     
/* 156 */     setReason(307, "Temporary Redirect");
/*     */     
/* 158 */     setReason(405, "Method Not Allowed");
/*     */     
/* 160 */     setReason(409, "Conflict");
/*     */     
/* 162 */     setReason(412, "Precondition Failed");
/*     */     
/* 164 */     setReason(413, "Request Too Long");
/*     */     
/* 166 */     setReason(414, "Request-URI Too Long");
/*     */     
/* 168 */     setReason(415, "Unsupported Media Type");
/*     */     
/* 170 */     setReason(300, "Multiple Choices");
/*     */     
/* 172 */     setReason(303, "See Other");
/*     */     
/* 174 */     setReason(305, "Use Proxy");
/*     */     
/* 176 */     setReason(402, "Payment Required");
/*     */     
/* 178 */     setReason(406, "Not Acceptable");
/*     */     
/* 180 */     setReason(407, "Proxy Authentication Required");
/*     */     
/* 182 */     setReason(408, "Request Timeout");
/*     */ 
/*     */     
/* 185 */     setReason(101, "Switching Protocols");
/*     */     
/* 187 */     setReason(203, "Non Authoritative Information");
/*     */     
/* 189 */     setReason(205, "Reset Content");
/*     */     
/* 191 */     setReason(206, "Partial Content");
/*     */     
/* 193 */     setReason(504, "Gateway Timeout");
/*     */     
/* 195 */     setReason(505, "Http Version Not Supported");
/*     */     
/* 197 */     setReason(410, "Gone");
/*     */     
/* 199 */     setReason(411, "Length Required");
/*     */     
/* 201 */     setReason(416, "Requested Range Not Satisfiable");
/*     */     
/* 203 */     setReason(417, "Expectation Failed");
/*     */ 
/*     */ 
/*     */     
/* 207 */     setReason(102, "Processing");
/*     */     
/* 209 */     setReason(207, "Multi-Status");
/*     */     
/* 211 */     setReason(422, "Unprocessable Entity");
/*     */     
/* 213 */     setReason(419, "Insufficient Space On Resource");
/*     */     
/* 215 */     setReason(420, "Method Failure");
/*     */     
/* 217 */     setReason(423, "Locked");
/*     */     
/* 219 */     setReason(507, "Insufficient Storage");
/*     */     
/* 221 */     setReason(424, "Failed Dependency");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\EnglishReasonPhraseCatalog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */