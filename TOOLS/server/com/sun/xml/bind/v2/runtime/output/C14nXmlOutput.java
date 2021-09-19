/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class C14nXmlOutput
/*     */   extends UTF8XmlOutput
/*     */ {
/*     */   private StaticAttribute[] staticAttributes;
/*     */   private int len;
/*     */   private int[] nsBuf;
/*     */   private final FinalArrayList<DynamicAttribute> otherAttributes;
/*     */   private final boolean namedAttributesAreOrdered;
/*     */   
/*     */   public C14nXmlOutput(OutputStream out, Encoded[] localNames, boolean namedAttributesAreOrdered) {
/*  56 */     super(out, localNames);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.staticAttributes = new StaticAttribute[8];
/*  69 */     this.len = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.nsBuf = new int[8];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     this.otherAttributes = new FinalArrayList();
/*     */     this.namedAttributesAreOrdered = namedAttributesAreOrdered;
/*     */     for (int i = 0; i < this.staticAttributes.length; i++) {
/*     */       this.staticAttributes[i] = new StaticAttribute();
/*     */     }
/*     */   }
/*     */   
/*     */   final class StaticAttribute
/*     */     implements Comparable<StaticAttribute>
/*     */   {
/*     */     Name name;
/*     */     String value;
/*     */     
/*     */     public void set(Name name, String value) {
/*  97 */       this.name = name;
/*  98 */       this.value = value;
/*     */     }
/*     */     
/*     */     void write() throws IOException {
/* 102 */       C14nXmlOutput.this.attribute(this.name, this.value);
/*     */     }
/*     */     
/*     */     C14nXmlOutput.DynamicAttribute toDynamicAttribute() {
/* 106 */       int prefix, nsUriIndex = this.name.nsUriIndex;
/*     */       
/* 108 */       if (nsUriIndex == -1) {
/* 109 */         prefix = -1;
/*     */       } else {
/* 111 */         prefix = C14nXmlOutput.this.nsUriIndex2prefixIndex[nsUriIndex];
/* 112 */       }  return new C14nXmlOutput.DynamicAttribute(prefix, this.name.localName, this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(StaticAttribute that) {
/* 117 */       return this.name.compareTo(that.name);
/*     */     }
/*     */   }
/*     */   
/*     */   final class DynamicAttribute
/*     */     implements Comparable<DynamicAttribute> {
/*     */     final int prefix;
/*     */     final String localName;
/*     */     final String value;
/*     */     
/*     */     public DynamicAttribute(int prefix, String localName, String value) {
/* 128 */       this.prefix = prefix;
/* 129 */       this.localName = localName;
/* 130 */       this.value = value;
/*     */     }
/*     */     
/*     */     private String getURI() {
/* 134 */       if (this.prefix == -1) return ""; 
/* 135 */       return C14nXmlOutput.this.nsContext.getNamespaceURI(this.prefix);
/*     */     }
/*     */     
/*     */     public int compareTo(DynamicAttribute that) {
/* 139 */       int r = getURI().compareTo(that.getURI());
/* 140 */       if (r != 0) return r; 
/* 141 */       return this.localName.compareTo(that.localName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void attribute(Name name, String value) throws IOException {
/* 147 */     if (this.staticAttributes.length == this.len) {
/*     */       
/* 149 */       int newLen = this.len * 2;
/* 150 */       StaticAttribute[] newbuf = new StaticAttribute[newLen];
/* 151 */       System.arraycopy(this.staticAttributes, 0, newbuf, 0, this.len);
/* 152 */       for (int i = this.len; i < newLen; i++)
/* 153 */         this.staticAttributes[i] = new StaticAttribute(); 
/* 154 */       this.staticAttributes = newbuf;
/*     */     } 
/*     */     
/* 157 */     this.staticAttributes[this.len++].set(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) throws IOException {
/* 162 */     this.otherAttributes.add(new DynamicAttribute(prefix, localName, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void endStartTag() throws IOException {
/* 167 */     if (this.otherAttributes.isEmpty()) {
/* 168 */       if (this.len != 0)
/*     */       {
/*     */         
/* 171 */         if (!this.namedAttributesAreOrdered) {
/* 172 */           Arrays.sort((Object[])this.staticAttributes, 0, this.len);
/*     */         }
/* 174 */         for (int i = 0; i < this.len; i++)
/* 175 */           this.staticAttributes[i].write(); 
/* 176 */         this.len = 0;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 182 */       for (int i = 0; i < this.len; i++)
/* 183 */         this.otherAttributes.add(this.staticAttributes[i].toDynamicAttribute()); 
/* 184 */       this.len = 0;
/* 185 */       Collections.sort((List<DynamicAttribute>)this.otherAttributes);
/*     */ 
/*     */       
/* 188 */       int size = this.otherAttributes.size();
/* 189 */       for (int j = 0; j < size; j++) {
/* 190 */         DynamicAttribute a = (DynamicAttribute)this.otherAttributes.get(j);
/* 191 */         super.attribute(a.prefix, a.localName, a.value);
/*     */       } 
/* 193 */       this.otherAttributes.clear();
/*     */     } 
/* 195 */     super.endStartTag();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeNsDecls(int base) throws IOException {
/* 203 */     int count = this.nsContext.getCurrent().count();
/*     */     
/* 205 */     if (count == 0) {
/*     */       return;
/*     */     }
/* 208 */     if (count > this.nsBuf.length)
/* 209 */       this.nsBuf = new int[count]; 
/*     */     int i;
/* 211 */     for (i = count - 1; i >= 0; i--) {
/* 212 */       this.nsBuf[i] = base + i;
/*     */     }
/*     */ 
/*     */     
/* 216 */     for (i = 0; i < count; i++) {
/* 217 */       for (int j = i + 1; j < count; j++) {
/* 218 */         String p = this.nsContext.getPrefix(this.nsBuf[i]);
/* 219 */         String q = this.nsContext.getPrefix(this.nsBuf[j]);
/* 220 */         if (p.compareTo(q) > 0) {
/*     */           
/* 222 */           int t = this.nsBuf[j];
/* 223 */           this.nsBuf[j] = this.nsBuf[i];
/* 224 */           this.nsBuf[i] = t;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 230 */     for (i = 0; i < count; i++)
/* 231 */       writeNsDecl(this.nsBuf[i]); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\C14nXmlOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */