/*      */ package com.sun.javaws;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OperaPreferences
/*      */ {
/*      */   private static final String OPERA_ENCODING = "UTF-8";
/*      */   private static final char OPEN_BRACKET = '[';
/*      */   private static final char CLOSE_BRACKET = ']';
/*      */   private static final char SEPARATOR = '=';
/*      */   private static final int DEFAULT_SIZE = 16384;
/*      */   private static final int DEFAULT_SECTION_COUNT = 20;
/*      */   private ArrayList sections;
/*      */   
/*      */   public void load(InputStream paramInputStream) throws IOException {
/*   47 */     InputStreamReader inputStreamReader = new InputStreamReader(paramInputStream, "UTF-8");
/*   48 */     BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 16384);
/*   49 */     String str1 = "";
/*      */     
/*   51 */     for (String str2 = bufferedReader.readLine(); str2 != null; ) {
/*      */       
/*   53 */       if (str2.length() > 0)
/*      */       {
/*   55 */         if (str2.charAt(0) == '[') {
/*      */ 
/*      */           
/*   58 */           str1 = str2.substring(1, str2.indexOf(']'));
/*      */         }
/*      */         else {
/*      */           
/*   62 */           String str3 = null;
/*   63 */           String str4 = null;
/*   64 */           int i = str2.indexOf('=');
/*      */           
/*   66 */           if (i >= 0) {
/*      */             
/*   68 */             str3 = str2.substring(0, i);
/*   69 */             str4 = str2.substring(i + 1);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*   74 */             str3 = str2;
/*      */           } 
/*      */           
/*   77 */           put(str1, str3, str4);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*   82 */       str2 = bufferedReader.readLine();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void store(OutputStream paramOutputStream) throws IOException {
/*   93 */     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(paramOutputStream, "UTF-8");
/*   94 */     PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
/*      */     
/*   96 */     printWriter.println(toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsSection(String paramString) {
/*  109 */     return (indexOf(paramString) >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(String paramString1, String paramString2) {
/*  124 */     int i = indexOf(paramString1);
/*      */     
/*  126 */     return (i < 0) ? false : ((PreferenceSection)this.sections.get(i)).contains(paramString2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String get(String paramString1, String paramString2) {
/*  141 */     int i = indexOf(paramString1);
/*      */     
/*  143 */     PreferenceSection.PreferenceEntry preferenceEntry = (i < 0) ? null : ((PreferenceSection)this.sections.get(i)).get(paramString2);
/*      */ 
/*      */ 
/*      */     
/*  147 */     return (preferenceEntry == null) ? null : preferenceEntry.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String put(String paramString1, String paramString2, String paramString3) {
/*  162 */     int i = indexOf(paramString1);
/*  163 */     PreferenceSection preferenceSection = null;
/*      */     
/*  165 */     if (i < 0) {
/*      */ 
/*      */       
/*  168 */       preferenceSection = new PreferenceSection(this, paramString1);
/*  169 */       this.sections.add(preferenceSection);
/*      */     }
/*      */     else {
/*      */       
/*  173 */       preferenceSection = this.sections.get(i);
/*      */     } 
/*      */     
/*  176 */     return preferenceSection.put(paramString2, paramString3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreferenceSection remove(String paramString) {
/*  189 */     int i = indexOf(paramString);
/*      */     
/*  191 */     return (i < 0) ? null : this.sections.remove(i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String remove(String paramString1, String paramString2) {
/*  206 */     int i = indexOf(paramString1);
/*      */     
/*  208 */     return (i < 0) ? null : ((PreferenceSection)this.sections.get(i)).remove(paramString2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator iterator(String paramString) {
/*  224 */     int i = indexOf(paramString);
/*      */     
/*  226 */     return (i < 0) ? (new PreferenceSection(this, paramString)).iterator() : ((PreferenceSection)this.sections.get(i)).iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator iterator() {
/*  240 */     return new OperaPreferencesIterator(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object paramObject) {
/*  256 */     boolean bool = false;
/*      */     
/*  258 */     if (paramObject instanceof OperaPreferences) {
/*      */       
/*  260 */       OperaPreferences operaPreferences = (OperaPreferences)paramObject;
/*  261 */       ListIterator listIterator1 = this.sections.listIterator();
/*  262 */       ListIterator listIterator2 = operaPreferences.sections.listIterator();
/*      */ 
/*      */ 
/*      */       
/*  266 */       while (listIterator1.hasNext() && listIterator2.hasNext()) {
/*      */         
/*  268 */         PreferenceSection preferenceSection1 = listIterator1.next();
/*  269 */         PreferenceSection preferenceSection2 = listIterator2.next();
/*      */         
/*  271 */         if (!preferenceSection1.equals(preferenceSection2)) {
/*      */           // Byte code: goto -> 117
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  279 */       if (!listIterator1.hasNext() && !listIterator2.hasNext())
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  284 */         bool = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  297 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  303 */     return this.sections.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  309 */     StringBuffer stringBuffer = new StringBuffer();
/*      */     
/*  311 */     for (ListIterator listIterator = this.sections.listIterator(); listIterator.hasNext(); ) {
/*      */       
/*  313 */       PreferenceSection preferenceSection = listIterator.next();
/*      */       
/*  315 */       stringBuffer.append(preferenceSection);
/*      */     } 
/*      */     
/*  318 */     return stringBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OperaPreferences() {
/* 1125 */     this.sections = null;
/*      */     this.sections = new ArrayList(20);
/*      */   }
/*      */   
/*      */   private int indexOf(String paramString) {
/*      */     byte b = 0;
/*      */     byte b1 = -1;
/*      */     for (ListIterator listIterator = this.sections.listIterator(); listIterator.hasNext(); b++) {
/*      */       PreferenceSection preferenceSection = listIterator.next();
/*      */       if (preferenceSection != null && preferenceSection.getName().equalsIgnoreCase(paramString)) {
/*      */         b1 = b;
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     return b1;
/*      */   }
/*      */   
/*      */   private class PreferenceSection {
/*      */     private String name;
/*      */     private HashMap entries;
/*      */     private volatile int modified;
/*      */     private PreferenceEntry start;
/*      */     private PreferenceEntry end;
/*      */     private final OperaPreferences this$0;
/*      */     
/*      */     public String getName() {
/*      */       return this.name;
/*      */     }
/*      */     
/*      */     public boolean contains(String param1String) {
/*      */       return this.entries.containsKey(param1String);
/*      */     }
/*      */     
/*      */     public String put(String param1String1, String param1String2) {
/*      */       PreferenceEntry preferenceEntry = (PreferenceEntry)this.entries.get(param1String1);
/*      */       String str = null;
/*      */       if (preferenceEntry == null) {
/*      */         preferenceEntry = new PreferenceEntry(this, param1String1, param1String2);
/*      */         if (this.end == null) {
/*      */           this.start = preferenceEntry;
/*      */           this.end = preferenceEntry;
/*      */         } else {
/*      */           this.end.add(preferenceEntry);
/*      */           this.end = preferenceEntry;
/*      */         } 
/*      */         this.entries.put(preferenceEntry.getKey(), preferenceEntry);
/*      */         this.modified++;
/*      */       } else {
/*      */         str = preferenceEntry.getValue();
/*      */         preferenceEntry.setValue(param1String2);
/*      */       } 
/*      */       return str;
/*      */     }
/*      */     
/*      */     public PreferenceEntry get(String param1String) {
/*      */       return (PreferenceEntry)this.entries.get(param1String);
/*      */     }
/*      */     
/*      */     public String remove(String param1String) {
/*      */       PreferenceEntry preferenceEntry = (PreferenceEntry)this.entries.get(param1String);
/*      */       String str = null;
/*      */       if (preferenceEntry != null) {
/*      */         str = preferenceEntry.getValue();
/*      */         removeEntry(preferenceEntry);
/*      */       } 
/*      */       return str;
/*      */     }
/*      */     
/*      */     public Iterator iterator() {
/*      */       return new PreferenceEntryIterator(this, this.start);
/*      */     }
/*      */     
/*      */     public boolean equals(Object param1Object) {
/*      */       boolean bool = false;
/*      */       if (param1Object instanceof PreferenceSection) {
/*      */         PreferenceSection preferenceSection = (PreferenceSection)param1Object;
/*      */         if (this.name == preferenceSection.name || (this.name != null && this.name.equals(preferenceSection.name))) {
/*      */           Iterator iterator1 = iterator();
/*      */           Iterator iterator2 = preferenceSection.iterator();
/*      */           while (iterator1.hasNext() && iterator2.hasNext()) {
/*      */             PreferenceEntry preferenceEntry1 = iterator1.next();
/*      */             PreferenceEntry preferenceEntry2 = iterator2.next();
/*      */             if (!preferenceEntry1.equals(preferenceEntry2))
/*      */               // Byte code: goto -> 143 
/*      */           } 
/*      */           if (!iterator1.hasNext() && !iterator2.hasNext())
/*      */             bool = true; 
/*      */         } 
/*      */       } 
/*      */       return bool;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*      */       return this.entries.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*      */       StringBuffer stringBuffer = new StringBuffer(this.entries.size() * 80);
/*      */       if (this.name != null && this.name.length() > 0)
/*      */         stringBuffer.append('[').append(this.name).append(']').append(System.getProperty("line.separator")); 
/*      */       for (PreferenceEntry preferenceEntry : this)
/*      */         stringBuffer.append(preferenceEntry).append(System.getProperty("line.separator")); 
/*      */       stringBuffer.append(System.getProperty("line.separator"));
/*      */       return stringBuffer.toString();
/*      */     }
/*      */     
/*      */     public PreferenceSection(OperaPreferences this$0, String param1String) {
/*      */       this.this$0 = this$0;
/*      */       this.name = param1String;
/*      */       this.entries = new HashMap();
/*      */       this.modified = 0;
/*      */       this.start = null;
/*      */       this.end = null;
/*      */     }
/*      */     
/*      */     private void removeEntry(PreferenceEntry param1PreferenceEntry) {
/*      */       if (param1PreferenceEntry == this.start)
/*      */         this.start = param1PreferenceEntry.getNext(); 
/*      */       if (param1PreferenceEntry == this.end)
/*      */         this.end = param1PreferenceEntry.getPrevious(); 
/*      */       param1PreferenceEntry.remove();
/*      */       this.entries.remove(param1PreferenceEntry.getKey());
/*      */       this.modified++;
/*      */     }
/*      */     
/*      */     private class PreferenceEntry {
/*      */       private final String key;
/*      */       private String value;
/*      */       private PreferenceEntry previous;
/*      */       private PreferenceEntry next;
/*      */       private final OperaPreferences.PreferenceSection this$1;
/*      */       
/*      */       public String getKey() {
/*      */         return this.key;
/*      */       }
/*      */       
/*      */       public String getValue() {
/*      */         return this.value;
/*      */       }
/*      */       
/*      */       public void setValue(String param2String) {
/*      */         this.value = param2String;
/*      */       }
/*      */       
/*      */       public void add(PreferenceEntry param2PreferenceEntry) {
/*      */         if (this.next != null) {
/*      */           this.next.add(param2PreferenceEntry);
/*      */         } else {
/*      */           this.next = param2PreferenceEntry;
/*      */           param2PreferenceEntry.previous = this;
/*      */         } 
/*      */       }
/*      */       
/*      */       public void remove() {
/*      */         if (this.previous != null)
/*      */           this.previous.next = this.next; 
/*      */         if (this.next != null)
/*      */           this.next.previous = this.previous; 
/*      */         this.previous = null;
/*      */         this.next = null;
/*      */       }
/*      */       
/*      */       public PreferenceEntry getPrevious() {
/*      */         return this.previous;
/*      */       }
/*      */       
/*      */       public PreferenceEntry getNext() {
/*      */         return this.next;
/*      */       }
/*      */       
/*      */       public boolean equals(Object param2Object) {
/*      */         boolean bool = false;
/*      */         if (param2Object instanceof PreferenceEntry) {
/*      */           PreferenceEntry preferenceEntry = (PreferenceEntry)param2Object;
/*      */           String str1 = getKey();
/*      */           String str2 = preferenceEntry.getKey();
/*      */           if (str1 == str2 || (str1 != null && str1.equals(str2))) {
/*      */             String str3 = getValue();
/*      */             String str4 = preferenceEntry.getValue();
/*      */             if (str3 == str4 || (str3 != null && str3.equals(str4)))
/*      */               bool = true; 
/*      */           } 
/*      */         } 
/*      */         return bool;
/*      */       }
/*      */       
/*      */       public int hashCode() {
/*      */         return (this.key == null) ? 0 : this.key.hashCode();
/*      */       }
/*      */       
/*      */       public String toString() {
/*      */         StringBuffer stringBuffer = new StringBuffer(((this.key == null) ? 0 : this.key.length()) + ((this.value == null) ? 0 : this.value.length()) + 1);
/*      */         if (this.key != null && this.value != null) {
/*      */           stringBuffer.append(this.key).append('=').append(this.value);
/*      */         } else if (this.key != null) {
/*      */           stringBuffer.append(this.key);
/*      */         } else if (this.value != null) {
/*      */           stringBuffer.append(this.value);
/*      */         } 
/*      */         return stringBuffer.toString();
/*      */       }
/*      */       
/*      */       public PreferenceEntry(OperaPreferences.PreferenceSection this$0, String param2String1, String param2String2) {
/*      */         this.this$1 = this$0;
/*      */         this.key = param2String1;
/*      */         this.value = param2String2;
/*      */         this.previous = null;
/*      */         this.next = null;
/*      */       }
/*      */     }
/*      */     
/*      */     private class PreferenceEntryIterator implements Iterator {
/*      */       private OperaPreferences.PreferenceSection.PreferenceEntry next;
/*      */       private OperaPreferences.PreferenceSection.PreferenceEntry current;
/*      */       private int expectedModified;
/*      */       private final OperaPreferences.PreferenceSection this$1;
/*      */       
/*      */       public boolean hasNext() {
/*      */         return (this.next != null);
/*      */       }
/*      */       
/*      */       public Object next() {
/*      */         if (this.this$1.modified != this.expectedModified)
/*      */           throw new ConcurrentModificationException(); 
/*      */         if (this.next == null)
/*      */           throw new NoSuchElementException(); 
/*      */         this.current = this.next;
/*      */         this.next = this.next.getNext();
/*      */         return this.current;
/*      */       }
/*      */       
/*      */       public void remove() {
/*      */         if (this.current == null)
/*      */           throw new IllegalStateException(); 
/*      */         if (this.this$1.modified != this.expectedModified)
/*      */           throw new ConcurrentModificationException(); 
/*      */         this.this$1.removeEntry(this.current);
/*      */         this.current = null;
/*      */         this.expectedModified = this.this$1.modified;
/*      */       }
/*      */       
/*      */       public PreferenceEntryIterator(OperaPreferences.PreferenceSection this$0, OperaPreferences.PreferenceSection.PreferenceEntry param2PreferenceEntry) {
/*      */         this.this$1 = this$0;
/*      */         this.next = param2PreferenceEntry;
/*      */         this.current = null;
/*      */         this.expectedModified = this$0.modified;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class PreferenceEntry {
/*      */     private final String key;
/*      */     private String value;
/*      */     private PreferenceEntry previous;
/*      */     private PreferenceEntry next;
/*      */     private final OperaPreferences.PreferenceSection this$1;
/*      */     
/*      */     public String getKey() {
/*      */       return this.key;
/*      */     }
/*      */     
/*      */     public String getValue() {
/*      */       return this.value;
/*      */     }
/*      */     
/*      */     public void setValue(String param1String) {
/*      */       this.value = param1String;
/*      */     }
/*      */     
/*      */     public void add(PreferenceEntry param1PreferenceEntry) {
/*      */       if (this.next != null) {
/*      */         this.next.add(param1PreferenceEntry);
/*      */       } else {
/*      */         this.next = param1PreferenceEntry;
/*      */         param1PreferenceEntry.previous = this;
/*      */       } 
/*      */     }
/*      */     
/*      */     public void remove() {
/*      */       if (this.previous != null)
/*      */         this.previous.next = this.next; 
/*      */       if (this.next != null)
/*      */         this.next.previous = this.previous; 
/*      */       this.previous = null;
/*      */       this.next = null;
/*      */     }
/*      */     
/*      */     public PreferenceEntry getPrevious() {
/*      */       return this.previous;
/*      */     }
/*      */     
/*      */     public PreferenceEntry getNext() {
/*      */       return this.next;
/*      */     }
/*      */     
/*      */     public boolean equals(Object param1Object) {
/*      */       boolean bool = false;
/*      */       if (param1Object instanceof PreferenceEntry) {
/*      */         PreferenceEntry preferenceEntry = (PreferenceEntry)param1Object;
/*      */         String str1 = getKey();
/*      */         String str2 = preferenceEntry.getKey();
/*      */         if (str1 == str2 || (str1 != null && str1.equals(str2))) {
/*      */           String str3 = getValue();
/*      */           String str4 = preferenceEntry.getValue();
/*      */           if (str3 == str4 || (str3 != null && str3.equals(str4)))
/*      */             bool = true; 
/*      */         } 
/*      */       } 
/*      */       return bool;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*      */       return (this.key == null) ? 0 : this.key.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*      */       StringBuffer stringBuffer = new StringBuffer(((this.key == null) ? 0 : this.key.length()) + ((this.value == null) ? 0 : this.value.length()) + 1);
/*      */       if (this.key != null && this.value != null) {
/*      */         stringBuffer.append(this.key).append('=').append(this.value);
/*      */       } else if (this.key != null) {
/*      */         stringBuffer.append(this.key);
/*      */       } else if (this.value != null) {
/*      */         stringBuffer.append(this.value);
/*      */       } 
/*      */       return stringBuffer.toString();
/*      */     }
/*      */     
/*      */     public PreferenceEntry(OperaPreferences this$0, String param1String1, String param1String2) {
/*      */       this.this$1 = (OperaPreferences.PreferenceSection)this$0;
/*      */       this.key = param1String1;
/*      */       this.value = param1String2;
/*      */       this.previous = null;
/*      */       this.next = null;
/*      */     }
/*      */   }
/*      */   
/*      */   private class OperaPreferencesIterator implements Iterator {
/*      */     private Iterator i;
/*      */     private final OperaPreferences this$0;
/*      */     
/*      */     public boolean hasNext() {
/*      */       return this.i.hasNext();
/*      */     }
/*      */     
/*      */     public Object next() {
/*      */       return ((OperaPreferences.PreferenceSection)this.i.next()).getName();
/*      */     }
/*      */     
/*      */     public void remove() {
/*      */       this.i.remove();
/*      */     }
/*      */     
/*      */     public OperaPreferencesIterator(OperaPreferences this$0) {
/*      */       this.this$0 = this$0;
/*      */       this.i = this$0.sections.listIterator();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\OperaPreferences.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */