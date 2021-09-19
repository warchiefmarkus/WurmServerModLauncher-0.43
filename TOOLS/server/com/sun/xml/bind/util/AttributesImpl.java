/*     */ package com.sun.xml.bind.util;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  96 */     this.length = 0;
/*  97 */     this.data = null;
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
/* 111 */     setAttributes(atts);
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
/* 129 */     return this.length;
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
/* 143 */     if (index >= 0 && index < this.length) {
/* 144 */       return this.data[index * 5];
/*     */     }
/* 146 */     return null;
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
/* 161 */     if (index >= 0 && index < this.length) {
/* 162 */       return this.data[index * 5 + 1];
/*     */     }
/* 164 */     return null;
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
/* 179 */     if (index >= 0 && index < this.length) {
/* 180 */       return this.data[index * 5 + 2];
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
/*     */   public String getType(int index) {
/* 197 */     if (index >= 0 && index < this.length) {
/* 198 */       return this.data[index * 5 + 3];
/*     */     }
/* 200 */     return null;
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
/* 214 */     if (index >= 0 && index < this.length) {
/* 215 */       return this.data[index * 5 + 4];
/*     */     }
/* 217 */     return null;
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
/* 237 */     int max = this.length * 5;
/* 238 */     for (int i = 0; i < max; i += 5) {
/* 239 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 240 */         return i / 5;
/*     */       }
/*     */     } 
/* 243 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndexFast(String uri, String localName) {
/* 250 */     for (int i = (this.length - 1) * 5; i >= 0; i -= 5) {
/*     */       
/* 252 */       if (this.data[i + 1] == localName && this.data[i] == uri) {
/* 253 */         return i / 5;
/*     */       }
/*     */     } 
/* 256 */     return -1;
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
/* 269 */     int max = this.length * 5;
/* 270 */     for (int i = 0; i < max; i += 5) {
/* 271 */       if (this.data[i + 2].equals(qName)) {
/* 272 */         return i / 5;
/*     */       }
/*     */     } 
/* 275 */     return -1;
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
/* 291 */     int max = this.length * 5;
/* 292 */     for (int i = 0; i < max; i += 5) {
/* 293 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 294 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 297 */     return null;
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
/* 311 */     int max = this.length * 5;
/* 312 */     for (int i = 0; i < max; i += 5) {
/* 313 */       if (this.data[i + 2].equals(qName)) {
/* 314 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 317 */     return null;
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
/* 333 */     int max = this.length * 5;
/* 334 */     for (int i = 0; i < max; i += 5) {
/* 335 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 336 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 339 */     return null;
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
/* 353 */     int max = this.length * 5;
/* 354 */     for (int i = 0; i < max; i += 5) {
/* 355 */       if (this.data[i + 2].equals(qName)) {
/* 356 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 359 */     return null;
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
/* 378 */     if (this.data != null)
/* 379 */       for (int i = 0; i < this.length * 5; i++) {
/* 380 */         this.data[i] = null;
/*     */       } 
/* 382 */     this.length = 0;
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
/* 396 */     clear();
/* 397 */     this.length = atts.getLength();
/* 398 */     if (this.length > 0) {
/* 399 */       this.data = new String[this.length * 5];
/* 400 */       for (int i = 0; i < this.length; i++) {
/* 401 */         this.data[i * 5] = atts.getURI(i);
/* 402 */         this.data[i * 5 + 1] = atts.getLocalName(i);
/* 403 */         this.data[i * 5 + 2] = atts.getQName(i);
/* 404 */         this.data[i * 5 + 3] = atts.getType(i);
/* 405 */         this.data[i * 5 + 4] = atts.getValue(i);
/*     */       } 
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
/* 431 */     ensureCapacity(this.length + 1);
/* 432 */     this.data[this.length * 5] = uri;
/* 433 */     this.data[this.length * 5 + 1] = localName;
/* 434 */     this.data[this.length * 5 + 2] = qName;
/* 435 */     this.data[this.length * 5 + 3] = type;
/* 436 */     this.data[this.length * 5 + 4] = value;
/* 437 */     this.length++;
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
/* 465 */     if (index >= 0 && index < this.length) {
/* 466 */       this.data[index * 5] = uri;
/* 467 */       this.data[index * 5 + 1] = localName;
/* 468 */       this.data[index * 5 + 2] = qName;
/* 469 */       this.data[index * 5 + 3] = type;
/* 470 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 472 */       badIndex(index);
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
/* 487 */     if (index >= 0 && index < this.length) {
/* 488 */       if (index < this.length - 1) {
/* 489 */         System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, (this.length - index - 1) * 5);
/*     */       }
/*     */       
/* 492 */       index = (this.length - 1) * 5;
/* 493 */       this.data[index++] = null;
/* 494 */       this.data[index++] = null;
/* 495 */       this.data[index++] = null;
/* 496 */       this.data[index++] = null;
/* 497 */       this.data[index] = null;
/* 498 */       this.length--;
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
/*     */   
/*     */   public void setURI(int index, String uri) {
/* 517 */     if (index >= 0 && index < this.length) {
/* 518 */       this.data[index * 5] = uri;
/*     */     } else {
/* 520 */       badIndex(index);
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
/* 537 */     if (index >= 0 && index < this.length) {
/* 538 */       this.data[index * 5 + 1] = localName;
/*     */     } else {
/* 540 */       badIndex(index);
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
/* 557 */     if (index >= 0 && index < this.length) {
/* 558 */       this.data[index * 5 + 2] = qName;
/*     */     } else {
/* 560 */       badIndex(index);
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
/* 576 */     if (index >= 0 && index < this.length) {
/* 577 */       this.data[index * 5 + 3] = type;
/*     */     } else {
/* 579 */       badIndex(index);
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
/* 595 */     if (index >= 0 && index < this.length) {
/* 596 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 598 */       badIndex(index);
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
/*     */   private void ensureCapacity(int n) {
/*     */     int max;
/* 616 */     if (n <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 620 */     if (this.data == null || this.data.length == 0) {
/* 621 */       max = 25;
/*     */     } else {
/* 623 */       if (this.data.length >= n * 5) {
/*     */         return;
/*     */       }
/*     */       
/* 627 */       max = this.data.length;
/*     */     } 
/* 629 */     while (max < n * 5) {
/* 630 */       max *= 2;
/*     */     }
/*     */     
/* 633 */     String[] newData = new String[max];
/* 634 */     if (this.length > 0) {
/* 635 */       System.arraycopy(this.data, 0, newData, 0, this.length * 5);
/*     */     }
/* 637 */     this.data = newData;
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
/* 650 */     String msg = "Attempt to modify attribute at illegal index: " + index;
/*     */     
/* 652 */     throw new ArrayIndexOutOfBoundsException(msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bin\\util\AttributesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */