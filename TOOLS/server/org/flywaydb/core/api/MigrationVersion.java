/*     */ package org.flywaydb.core.api;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MigrationVersion
/*     */   implements Comparable<MigrationVersion>
/*     */ {
/*  32 */   public static final MigrationVersion EMPTY = new MigrationVersion(null, "<< Empty Schema >>");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public static final MigrationVersion LATEST = new MigrationVersion(BigInteger.valueOf(-1L), "<< Latest Version >>");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final MigrationVersion CURRENT = new MigrationVersion(BigInteger.valueOf(-2L), "<< Current Version >>");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static Pattern splitPattern = Pattern.compile("\\.(?=\\d)");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<BigInteger> versionParts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String displayText;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MigrationVersion fromVersion(String version) {
/*  68 */     if ("current".equalsIgnoreCase(version)) return CURRENT; 
/*  69 */     if (LATEST.getVersion().equals(version)) return LATEST; 
/*  70 */     if (version == null) return EMPTY; 
/*  71 */     return new MigrationVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MigrationVersion(String version) {
/*  81 */     String normalizedVersion = version.replace('_', '.');
/*  82 */     this.versionParts = tokenize(normalizedVersion);
/*  83 */     this.displayText = normalizedVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MigrationVersion(BigInteger version, String displayText) {
/*  94 */     this.versionParts = new ArrayList<BigInteger>();
/*  95 */     this.versionParts.add(version);
/*  96 */     this.displayText = displayText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return this.displayText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 111 */     if (equals(EMPTY)) return null; 
/* 112 */     if (equals(LATEST)) return Long.toString(Long.MAX_VALUE); 
/* 113 */     return this.displayText;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 118 */     if (this == o) return true; 
/* 119 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 121 */     MigrationVersion version1 = (MigrationVersion)o;
/*     */     
/* 123 */     return (compareTo(version1) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return (this.versionParts == null) ? 0 : this.versionParts.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(MigrationVersion o) {
/* 133 */     if (o == null) {
/* 134 */       return 1;
/*     */     }
/*     */     
/* 137 */     if (this == EMPTY) {
/* 138 */       return (o == EMPTY) ? 0 : Integer.MIN_VALUE;
/*     */     }
/*     */     
/* 141 */     if (this == CURRENT) {
/* 142 */       return (o == CURRENT) ? 0 : Integer.MIN_VALUE;
/*     */     }
/*     */     
/* 145 */     if (this == LATEST) {
/* 146 */       return (o == LATEST) ? 0 : Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 149 */     if (o == EMPTY) {
/* 150 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 153 */     if (o == CURRENT) {
/* 154 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 157 */     if (o == LATEST) {
/* 158 */       return Integer.MIN_VALUE;
/*     */     }
/* 160 */     List<BigInteger> elements1 = this.versionParts;
/* 161 */     List<BigInteger> elements2 = o.versionParts;
/* 162 */     int largestNumberOfElements = Math.max(elements1.size(), elements2.size());
/* 163 */     for (int i = 0; i < largestNumberOfElements; i++) {
/* 164 */       int compared = getOrZero(elements1, i).compareTo(getOrZero(elements2, i));
/* 165 */       if (compared != 0) {
/* 166 */         return compared;
/*     */       }
/*     */     } 
/* 169 */     return 0;
/*     */   }
/*     */   
/*     */   private BigInteger getOrZero(List<BigInteger> elements, int i) {
/* 173 */     return (i < elements.size()) ? elements.get(i) : BigInteger.ZERO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<BigInteger> tokenize(String str) {
/* 183 */     List<BigInteger> numbers = new ArrayList<BigInteger>();
/* 184 */     for (String number : splitPattern.split(str)) {
/*     */       try {
/* 186 */         numbers.add(new BigInteger(number));
/* 187 */       } catch (NumberFormatException e) {
/* 188 */         throw new FlywayException("Invalid version containing non-numeric characters. Only 0..9 and . are allowed. Invalid version: " + str);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 193 */     for (int i = numbers.size() - 1; i > 0 && (
/* 194 */       (BigInteger)numbers.get(i)).equals(BigInteger.ZERO); i--) {
/* 195 */       numbers.remove(i);
/*     */     }
/* 197 */     return numbers;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\MigrationVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */