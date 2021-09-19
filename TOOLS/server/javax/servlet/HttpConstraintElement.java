/*     */ package javax.servlet;
/*     */ 
/*     */ import javax.servlet.annotation.ServletSecurity;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpConstraintElement
/*     */ {
/*     */   private ServletSecurity.EmptyRoleSemantic emptyRoleSemantic;
/*     */   private ServletSecurity.TransportGuarantee transportGuarantee;
/*     */   private String[] rolesAllowed;
/*     */   
/*     */   public HttpConstraintElement() {
/*  63 */     this(ServletSecurity.EmptyRoleSemantic.PERMIT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic semantic) {
/*  72 */     this(semantic, ServletSecurity.TransportGuarantee.NONE, new String[0]);
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
/*     */   public HttpConstraintElement(ServletSecurity.TransportGuarantee guarantee, String... roleNames) {
/*  86 */     this(ServletSecurity.EmptyRoleSemantic.PERMIT, guarantee, roleNames);
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
/*     */   public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic semantic, ServletSecurity.TransportGuarantee guarantee, String... roleNames) {
/* 102 */     if (semantic == ServletSecurity.EmptyRoleSemantic.DENY && roleNames.length > 0) {
/* 103 */       throw new IllegalArgumentException("Deny semantic with rolesAllowed");
/*     */     }
/*     */     
/* 106 */     this.emptyRoleSemantic = semantic;
/* 107 */     this.transportGuarantee = guarantee;
/* 108 */     this.rolesAllowed = roleNames;
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
/*     */   public ServletSecurity.EmptyRoleSemantic getEmptyRoleSemantic() {
/* 123 */     return this.emptyRoleSemantic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletSecurity.TransportGuarantee getTransportGuarantee() {
/* 134 */     return this.transportGuarantee;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getRolesAllowed() {
/* 157 */     return this.rolesAllowed;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\HttpConstraintElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */