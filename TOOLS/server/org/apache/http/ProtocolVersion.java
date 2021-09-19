/*     */ package org.apache.http;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ProtocolVersion
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 8950662842175091068L;
/*     */   protected final String protocol;
/*     */   protected final int major;
/*     */   protected final int minor;
/*     */   
/*     */   public ProtocolVersion(String protocol, int major, int minor) {
/*  70 */     if (protocol == null) {
/*  71 */       throw new IllegalArgumentException("Protocol name must not be null.");
/*     */     }
/*     */     
/*  74 */     if (major < 0) {
/*  75 */       throw new IllegalArgumentException("Protocol major version number must not be negative.");
/*     */     }
/*     */     
/*  78 */     if (minor < 0) {
/*  79 */       throw new IllegalArgumentException("Protocol minor version number may not be negative");
/*     */     }
/*     */     
/*  82 */     this.protocol = protocol;
/*  83 */     this.major = major;
/*  84 */     this.minor = minor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getProtocol() {
/*  93 */     return this.protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMajor() {
/* 102 */     return this.major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMinor() {
/* 111 */     return this.minor;
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
/*     */   public ProtocolVersion forVersion(int major, int minor) {
/* 132 */     if (major == this.major && minor == this.minor) {
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 137 */     return new ProtocolVersion(this.protocol, major, minor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 148 */     return this.protocol.hashCode() ^ this.major * 100000 ^ this.minor;
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
/*     */   public final boolean equals(Object obj) {
/* 167 */     if (this == obj) {
/* 168 */       return true;
/*     */     }
/* 170 */     if (!(obj instanceof ProtocolVersion)) {
/* 171 */       return false;
/*     */     }
/* 173 */     ProtocolVersion that = (ProtocolVersion)obj;
/*     */     
/* 175 */     return (this.protocol.equals(that.protocol) && this.major == that.major && this.minor == that.minor);
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
/*     */   public boolean isComparable(ProtocolVersion that) {
/* 192 */     return (that != null && this.protocol.equals(that.protocol));
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
/*     */   public int compareToVersion(ProtocolVersion that) {
/* 213 */     if (that == null) {
/* 214 */       throw new IllegalArgumentException("Protocol version must not be null.");
/*     */     }
/*     */     
/* 217 */     if (!this.protocol.equals(that.protocol)) {
/* 218 */       throw new IllegalArgumentException("Versions for different protocols cannot be compared. " + this + " " + that);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 223 */     int delta = getMajor() - that.getMajor();
/* 224 */     if (delta == 0) {
/* 225 */       delta = getMinor() - that.getMinor();
/*     */     }
/* 227 */     return delta;
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
/*     */   public final boolean greaterEquals(ProtocolVersion version) {
/* 242 */     return (isComparable(version) && compareToVersion(version) >= 0);
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
/*     */   public final boolean lessEquals(ProtocolVersion version) {
/* 257 */     return (isComparable(version) && compareToVersion(version) <= 0);
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
/* 268 */     StringBuilder buffer = new StringBuilder();
/* 269 */     buffer.append(this.protocol);
/* 270 */     buffer.append('/');
/* 271 */     buffer.append(Integer.toString(this.major));
/* 272 */     buffer.append('.');
/* 273 */     buffer.append(Integer.toString(this.minor));
/* 274 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 279 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\ProtocolVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */