/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AttributesImpl
/*     */   implements Attributes
/*     */ {
/*     */   int length;
/*     */   String[] data;
/*     */   
/*     */   public AttributesImpl() {
/*  61 */     this.length = 0;
/*  62 */     this.data = null;
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
/*     */   public AttributesImpl(Attributes atts) {
/*  76 */     setAttributes(atts);
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
/*     */   public int getLength() {
/*  94 */     return this.length;
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
/*     */   public String getURI(int index) {
/* 108 */     if (index >= 0 && index < this.length) {
/* 109 */       return this.data[index * 5];
/*     */     }
/* 111 */     return null;
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
/*     */   public String getLocalName(int index) {
/* 126 */     if (index >= 0 && index < this.length) {
/* 127 */       return this.data[index * 5 + 1];
/*     */     }
/* 129 */     return null;
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
/*     */   public String getQName(int index) {
/* 144 */     if (index >= 0 && index < this.length) {
/* 145 */       return this.data[index * 5 + 2];
/*     */     }
/* 147 */     return null;
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
/*     */   public String getType(int index) {
/* 162 */     if (index >= 0 && index < this.length) {
/* 163 */       return this.data[index * 5 + 3];
/*     */     }
/* 165 */     return null;
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
/*     */   public String getValue(int index) {
/* 179 */     if (index >= 0 && index < this.length) {
/* 180 */       return this.data[index * 5 + 4];
/*     */     }
/* 182 */     return null;
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
/*     */   public int getIndex(String uri, String localName) {
/* 202 */     int max = this.length * 5;
/* 203 */     for (int i = 0; i < max; i += 5) {
/* 204 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 205 */         return i / 5;
/*     */       }
/*     */     } 
/* 208 */     return -1;
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
/*     */   public int getIndex(String qName) {
/* 221 */     int max = this.length * 5;
/* 222 */     for (int i = 0; i < max; i += 5) {
/* 223 */       if (this.data[i + 2].equals(qName)) {
/* 224 */         return i / 5;
/*     */       }
/*     */     } 
/* 227 */     return -1;
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
/*     */   public String getType(String uri, String localName) {
/* 243 */     int max = this.length * 5;
/* 244 */     for (int i = 0; i < max; i += 5) {
/* 245 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 246 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 249 */     return null;
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
/*     */   public String getType(String qName) {
/* 263 */     int max = this.length * 5;
/* 264 */     for (int i = 0; i < max; i += 5) {
/* 265 */       if (this.data[i + 2].equals(qName)) {
/* 266 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 269 */     return null;
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
/*     */   public String getValue(String uri, String localName) {
/* 285 */     int max = this.length * 5;
/* 286 */     for (int i = 0; i < max; i += 5) {
/* 287 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 288 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 291 */     return null;
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
/*     */   public String getValue(String qName) {
/* 305 */     int max = this.length * 5;
/* 306 */     for (int i = 0; i < max; i += 5) {
/* 307 */       if (this.data[i + 2].equals(qName)) {
/* 308 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 311 */     return null;
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
/*     */   public void clear() {
/* 330 */     this.length = 0;
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
/*     */   public void setAttributes(Attributes atts) {
/* 344 */     clear();
/* 345 */     this.length = atts.getLength();
/* 346 */     this.data = new String[this.length * 5];
/* 347 */     for (int i = 0; i < this.length; i++) {
/* 348 */       this.data[i * 5] = atts.getURI(i);
/* 349 */       this.data[i * 5 + 1] = atts.getLocalName(i);
/* 350 */       this.data[i * 5 + 2] = atts.getQName(i);
/* 351 */       this.data[i * 5 + 3] = atts.getType(i);
/* 352 */       this.data[i * 5 + 4] = atts.getValue(i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(String uri, String localName, String qName, String type, String value) {
/* 377 */     ensureCapacity(this.length + 1);
/* 378 */     this.data[this.length * 5] = uri;
/* 379 */     this.data[this.length * 5 + 1] = localName;
/* 380 */     this.data[this.length * 5 + 2] = qName;
/* 381 */     this.data[this.length * 5 + 3] = type;
/* 382 */     this.data[this.length * 5 + 4] = value;
/* 383 */     this.length++;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(int index, String uri, String localName, String qName, String type, String value) {
/* 411 */     if (index >= 0 && index < this.length) {
/* 412 */       this.data[index * 5] = uri;
/* 413 */       this.data[index * 5 + 1] = localName;
/* 414 */       this.data[index * 5 + 2] = qName;
/* 415 */       this.data[index * 5 + 3] = type;
/* 416 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 418 */       badIndex(index);
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
/*     */   public void removeAttribute(int index) {
/* 433 */     if (index >= 0 && index < this.length) {
/* 434 */       if (index < this.length - 1) {
/* 435 */         System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, (this.length - index - 1) * 5);
/*     */       }
/*     */       
/* 438 */       this.length--;
/*     */     } else {
/* 440 */       badIndex(index);
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
/*     */   public void setURI(int index, String uri) {
/* 457 */     if (index >= 0 && index < this.length) {
/* 458 */       this.data[index * 5] = uri;
/*     */     } else {
/* 460 */       badIndex(index);
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
/*     */   public void setLocalName(int index, String localName) {
/* 477 */     if (index >= 0 && index < this.length) {
/* 478 */       this.data[index * 5 + 1] = localName;
/*     */     } else {
/* 480 */       badIndex(index);
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
/*     */   public void setQName(int index, String qName) {
/* 497 */     if (index >= 0 && index < this.length) {
/* 498 */       this.data[index * 5 + 2] = qName;
/*     */     } else {
/* 500 */       badIndex(index);
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
/*     */   public void setType(int index, String type) {
/* 516 */     if (index >= 0 && index < this.length) {
/* 517 */       this.data[index * 5 + 3] = type;
/*     */     } else {
/* 519 */       badIndex(index);
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
/*     */   public void setValue(int index, String value) {
/* 535 */     if (index >= 0 && index < this.length) {
/* 536 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 538 */       badIndex(index);
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
/*     */   private void ensureCapacity(int n) {
/* 557 */     if (n > 0 && (this.data == null || this.data.length == 0)) {
/* 558 */       this.data = new String[25];
/*     */     }
/*     */     
/* 561 */     int max = this.data.length;
/* 562 */     if (max >= n * 5) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 567 */     while (max < n * 5) {
/* 568 */       max *= 2;
/*     */     }
/* 570 */     String[] newData = new String[max];
/* 571 */     System.arraycopy(this.data, 0, newData, 0, this.length * 5);
/* 572 */     this.data = newData;
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
/*     */   private void badIndex(int index) throws ArrayIndexOutOfBoundsException {
/* 585 */     String msg = "Attempt to modify attribute at illegal index: " + index;
/*     */     
/* 587 */     throw new ArrayIndexOutOfBoundsException(msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\AttributesImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */