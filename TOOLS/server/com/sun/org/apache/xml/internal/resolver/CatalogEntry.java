/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CatalogEntry
/*     */ {
/*  53 */   protected static int nextEntry = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected static Hashtable entryTypes = new Hashtable<Object, Object>();
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected static Vector entryArgs = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int addEntryType(String name, int numArgs) {
/*  78 */     entryTypes.put(name, new Integer(nextEntry));
/*  79 */     entryArgs.add(nextEntry, new Integer(numArgs));
/*  80 */     nextEntry++;
/*     */     
/*  82 */     return nextEntry - 1;
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
/*     */   public static int getEntryType(String name) throws CatalogException {
/*  95 */     if (!entryTypes.containsKey(name)) {
/*  96 */       throw new CatalogException(3);
/*     */     }
/*     */     
/*  99 */     Integer iType = (Integer)entryTypes.get(name);
/*     */     
/* 101 */     if (iType == null) {
/* 102 */       throw new CatalogException(3);
/*     */     }
/*     */     
/* 105 */     return iType.intValue();
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
/*     */   public static int getEntryArgCount(String name) throws CatalogException {
/* 118 */     return getEntryArgCount(getEntryType(name));
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
/*     */   public static int getEntryArgCount(int type) throws CatalogException {
/*     */     try {
/* 131 */       Integer iArgs = entryArgs.get(type);
/* 132 */       return iArgs.intValue();
/* 133 */     } catch (ArrayIndexOutOfBoundsException e) {
/* 134 */       throw new CatalogException(3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 139 */   protected int entryType = 0;
/*     */ 
/*     */   
/* 142 */   protected Vector args = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatalogEntry() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatalogEntry(String name, Vector args) throws CatalogException {
/* 161 */     Integer iType = (Integer)entryTypes.get(name);
/*     */     
/* 163 */     if (iType == null) {
/* 164 */       throw new CatalogException(3);
/*     */     }
/*     */     
/* 167 */     int type = iType.intValue();
/*     */     
/*     */     try {
/* 170 */       Integer iArgs = entryArgs.get(type);
/* 171 */       if (iArgs.intValue() != args.size()) {
/* 172 */         throw new CatalogException(2);
/*     */       }
/* 174 */     } catch (ArrayIndexOutOfBoundsException e) {
/* 175 */       throw new CatalogException(3);
/*     */     } 
/*     */     
/* 178 */     this.entryType = type;
/* 179 */     this.args = args;
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
/*     */   public CatalogEntry(int type, Vector args) throws CatalogException {
/*     */     try {
/* 195 */       Integer iArgs = entryArgs.get(type);
/* 196 */       if (iArgs.intValue() != args.size()) {
/* 197 */         throw new CatalogException(2);
/*     */       }
/* 199 */     } catch (ArrayIndexOutOfBoundsException e) {
/* 200 */       throw new CatalogException(3);
/*     */     } 
/*     */     
/* 203 */     this.entryType = type;
/* 204 */     this.args = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntryType() {
/* 213 */     return this.entryType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEntryArg(int argNum) {
/*     */     try {
/* 225 */       String arg = this.args.get(argNum);
/* 226 */       return arg;
/* 227 */     } catch (ArrayIndexOutOfBoundsException e) {
/* 228 */       return null;
/*     */     } 
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
/*     */   public void setEntryArg(int argNum, String newspec) throws ArrayIndexOutOfBoundsException {
/* 247 */     this.args.set(argNum, newspec);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\CatalogEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */