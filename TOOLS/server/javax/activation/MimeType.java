/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeType
/*     */   implements Externalizable
/*     */ {
/*     */   private String primaryType;
/*     */   private String subType;
/*     */   private MimeTypeParameterList parameters;
/*     */   private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
/*     */   
/*     */   public MimeType() {
/*  65 */     this.primaryType = "application";
/*  66 */     this.subType = "*";
/*  67 */     this.parameters = new MimeTypeParameterList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String rawdata) throws MimeTypeParseException {
/*  76 */     parse(rawdata);
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
/*     */   public MimeType(String primary, String sub) throws MimeTypeParseException {
/*  90 */     if (isValidToken(primary)) {
/*  91 */       this.primaryType = primary.toLowerCase(Locale.ENGLISH);
/*     */     } else {
/*  93 */       throw new MimeTypeParseException("Primary type is invalid.");
/*     */     } 
/*     */ 
/*     */     
/*  97 */     if (isValidToken(sub)) {
/*  98 */       this.subType = sub.toLowerCase(Locale.ENGLISH);
/*     */     } else {
/* 100 */       throw new MimeTypeParseException("Sub type is invalid.");
/*     */     } 
/*     */     
/* 103 */     this.parameters = new MimeTypeParameterList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(String rawdata) throws MimeTypeParseException {
/* 110 */     int slashIndex = rawdata.indexOf('/');
/* 111 */     int semIndex = rawdata.indexOf(';');
/* 112 */     if (slashIndex < 0 && semIndex < 0)
/*     */     {
/*     */       
/* 115 */       throw new MimeTypeParseException("Unable to find a sub type."); } 
/* 116 */     if (slashIndex < 0 && semIndex >= 0)
/*     */     {
/*     */       
/* 119 */       throw new MimeTypeParseException("Unable to find a sub type."); } 
/* 120 */     if (slashIndex >= 0 && semIndex < 0) {
/*     */       
/* 122 */       this.primaryType = rawdata.substring(0, slashIndex).trim().toLowerCase(Locale.ENGLISH);
/*     */       
/* 124 */       this.subType = rawdata.substring(slashIndex + 1).trim().toLowerCase(Locale.ENGLISH);
/*     */       
/* 126 */       this.parameters = new MimeTypeParameterList();
/* 127 */     } else if (slashIndex < semIndex) {
/*     */       
/* 129 */       this.primaryType = rawdata.substring(0, slashIndex).trim().toLowerCase(Locale.ENGLISH);
/*     */       
/* 131 */       this.subType = rawdata.substring(slashIndex + 1, semIndex).trim().toLowerCase(Locale.ENGLISH);
/*     */       
/* 133 */       this.parameters = new MimeTypeParameterList(rawdata.substring(semIndex));
/*     */     }
/*     */     else {
/*     */       
/* 137 */       throw new MimeTypeParseException("Unable to find a sub type.");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     if (!isValidToken(this.primaryType)) {
/* 144 */       throw new MimeTypeParseException("Primary type is invalid.");
/*     */     }
/*     */     
/* 147 */     if (!isValidToken(this.subType)) {
/* 148 */       throw new MimeTypeParseException("Sub type is invalid.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrimaryType() {
/* 157 */     return this.primaryType;
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
/*     */   public void setPrimaryType(String primary) throws MimeTypeParseException {
/* 169 */     if (!isValidToken(this.primaryType))
/* 170 */       throw new MimeTypeParseException("Primary type is invalid."); 
/* 171 */     this.primaryType = primary.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubType() {
/* 180 */     return this.subType;
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
/*     */   public void setSubType(String sub) throws MimeTypeParseException {
/* 192 */     if (!isValidToken(this.subType))
/* 193 */       throw new MimeTypeParseException("Sub type is invalid."); 
/* 194 */     this.subType = sub.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeTypeParameterList getParameters() {
/* 203 */     return this.parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 214 */     return this.parameters.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(String name, String value) {
/* 225 */     this.parameters.set(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeParameter(String name) {
/* 234 */     this.parameters.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 241 */     return getBaseType() + this.parameters.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBaseType() {
/* 251 */     return this.primaryType + "/" + this.subType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(MimeType type) {
/* 262 */     return (this.primaryType.equals(type.getPrimaryType()) && (this.subType.equals("*") || type.getSubType().equals("*") || this.subType.equals(type.getSubType())));
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
/*     */   public boolean match(String rawdata) throws MimeTypeParseException {
/* 276 */     return match(new MimeType(rawdata));
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
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 289 */     out.writeUTF(toString());
/* 290 */     out.flush();
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
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/*     */     try {
/* 307 */       parse(in.readUTF());
/* 308 */     } catch (MimeTypeParseException e) {
/* 309 */       throw new IOException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isTokenChar(char c) {
/* 319 */     return (c > ' ' && c < '' && "()<>@,;:/[]?=\\\"".indexOf(c) < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidToken(String s) {
/* 326 */     int len = s.length();
/* 327 */     if (len > 0) {
/* 328 */       for (int i = 0; i < len; i++) {
/* 329 */         char c = s.charAt(i);
/* 330 */         if (!isTokenChar(c)) {
/* 331 */           return false;
/*     */         }
/*     */       } 
/* 334 */       return true;
/*     */     } 
/* 336 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\MimeType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */