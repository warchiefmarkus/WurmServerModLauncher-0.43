/*     */ package com.wurmonline.server.utils;
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
/*     */ public enum HttpResponseStatus
/*     */ {
/*  41 */   OK(200, "OK"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   CREATED(201, "Created"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   ACCEPTED(202, "Accepted"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   NO_CONTENT(204, "No Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   MOVED_PERMANENTLY(301, "Moved Permanently"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   SEE_OTHER(303, "See Other"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   NOT_MODIFIED(304, "Not Modified"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   TEMPORARY_REDIRECT(307, "Temporary Redirect"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   BAD_REQUEST(400, "Bad Request"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   UNAUTHORIZED(401, "Unauthorized"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   FORBIDDEN(403, "Forbidden"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   NOT_FOUND(404, "Not Found"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   NOT_ACCEPTABLE(406, "Not Acceptable"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   CONFLICT(409, "Conflict"),
/*     */ 
/*     */ 
/*     */   
/* 112 */   GONE(410, "Gone"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   PRECONDITION_FAILED(412, "Precondition Failed"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   SERVICE_UNAVAILABLE(503, "Service Unavailable");
/*     */   
/*     */   private final int code;
/*     */   
/*     */   private final String reason;
/*     */   
/*     */   private Family family;
/*     */ 
/*     */   
/*     */   public enum Family
/*     */   {
/* 143 */     INFORMATIONAL, SUCCESSFUL, REDIRECTION, CLIENT_ERROR, SERVER_ERROR, OTHER;
/*     */   }
/*     */ 
/*     */   
/*     */   HttpResponseStatus(int statusCode, String reasonPhrase) {
/* 148 */     this.code = statusCode;
/* 149 */     this.reason = reasonPhrase;
/* 150 */     switch (this.code / 100) {
/*     */       
/*     */       case 1:
/* 153 */         this.family = Family.INFORMATIONAL;
/*     */         return;
/*     */       case 2:
/* 156 */         this.family = Family.SUCCESSFUL;
/*     */         return;
/*     */       case 3:
/* 159 */         this.family = Family.REDIRECTION;
/*     */         return;
/*     */       case 4:
/* 162 */         this.family = Family.CLIENT_ERROR;
/*     */         return;
/*     */       case 5:
/* 165 */         this.family = Family.SERVER_ERROR;
/*     */         return;
/*     */     } 
/* 168 */     this.family = Family.OTHER;
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
/*     */   public Family getFamily() {
/* 180 */     return this.family;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/* 190 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     return this.reason;
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
/*     */   public static HttpResponseStatus fromStatusCode(int statusCode) {
/* 213 */     for (HttpResponseStatus s : values()) {
/*     */       
/* 215 */       if (s.code == statusCode)
/*     */       {
/* 217 */         return s;
/*     */       }
/*     */     } 
/* 220 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\HttpResponseStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */