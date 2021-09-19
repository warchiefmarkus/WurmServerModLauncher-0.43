/*     */ package com.sun.xml.dtdparser;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class XmlReader
/*     */   extends Reader
/*     */ {
/*     */   private static final int MAXPUSHBACK = 512;
/*     */   private Reader in;
/*     */   private String assignedEncoding;
/*     */   private boolean closed;
/*     */   
/*     */   public static Reader createReader(InputStream in) throws IOException {
/*  76 */     return new XmlReader(in);
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
/*     */   public static Reader createReader(InputStream in, String encoding) throws IOException {
/*  91 */     if (encoding == null)
/*  92 */       return new XmlReader(in); 
/*  93 */     if ("UTF-8".equalsIgnoreCase(encoding) || "UTF8".equalsIgnoreCase(encoding))
/*     */     {
/*  95 */       return new Utf8Reader(in); } 
/*  96 */     if ("US-ASCII".equalsIgnoreCase(encoding) || "ASCII".equalsIgnoreCase(encoding))
/*     */     {
/*  98 */       return new AsciiReader(in); } 
/*  99 */     if ("ISO-8859-1".equalsIgnoreCase(encoding))
/*     */     {
/*     */       
/* 102 */       return new Iso8859_1Reader(in);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     return new InputStreamReader(in, std2java(encoding));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   private static final Hashtable charsets = new Hashtable(31);
/*     */   
/*     */   static {
/* 122 */     charsets.put("UTF-16", "Unicode");
/* 123 */     charsets.put("ISO-10646-UCS-2", "Unicode");
/*     */ 
/*     */ 
/*     */     
/* 127 */     charsets.put("EBCDIC-CP-US", "cp037");
/* 128 */     charsets.put("EBCDIC-CP-CA", "cp037");
/* 129 */     charsets.put("EBCDIC-CP-NL", "cp037");
/* 130 */     charsets.put("EBCDIC-CP-WT", "cp037");
/*     */     
/* 132 */     charsets.put("EBCDIC-CP-DK", "cp277");
/* 133 */     charsets.put("EBCDIC-CP-NO", "cp277");
/* 134 */     charsets.put("EBCDIC-CP-FI", "cp278");
/* 135 */     charsets.put("EBCDIC-CP-SE", "cp278");
/*     */     
/* 137 */     charsets.put("EBCDIC-CP-IT", "cp280");
/* 138 */     charsets.put("EBCDIC-CP-ES", "cp284");
/* 139 */     charsets.put("EBCDIC-CP-GB", "cp285");
/* 140 */     charsets.put("EBCDIC-CP-FR", "cp297");
/*     */     
/* 142 */     charsets.put("EBCDIC-CP-AR1", "cp420");
/* 143 */     charsets.put("EBCDIC-CP-HE", "cp424");
/* 144 */     charsets.put("EBCDIC-CP-BE", "cp500");
/* 145 */     charsets.put("EBCDIC-CP-CH", "cp500");
/*     */     
/* 147 */     charsets.put("EBCDIC-CP-ROECE", "cp870");
/* 148 */     charsets.put("EBCDIC-CP-YU", "cp870");
/* 149 */     charsets.put("EBCDIC-CP-IS", "cp871");
/* 150 */     charsets.put("EBCDIC-CP-AR2", "cp918");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String std2java(String encoding) {
/* 160 */     String temp = encoding.toUpperCase();
/* 161 */     temp = (String)charsets.get(temp);
/* 162 */     return (temp != null) ? temp : encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 169 */     return this.assignedEncoding;
/*     */   }
/*     */   
/*     */   private XmlReader(InputStream stream) throws IOException {
/* 173 */     super(stream);
/*     */ 
/*     */     
/*     */     PushbackInputStream pb;
/*     */ 
/*     */     
/* 179 */     if (stream instanceof PushbackInputStream) {
/* 180 */       pb = (PushbackInputStream)stream;
/*     */     } else {
/* 182 */       pb = new PushbackInputStream(stream, 512);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     byte[] buf = new byte[4];
/* 189 */     int len = pb.read(buf);
/* 190 */     if (len > 0) {
/* 191 */       pb.unread(buf, 0, len);
/*     */     }
/* 193 */     if (len == 4) {
/* 194 */       switch (buf[0] & 0xFF) {
/*     */         
/*     */         case 0:
/* 197 */           if (buf[1] == 60 && buf[2] == 0 && buf[3] == 63) {
/* 198 */             setEncoding(pb, "UnicodeBig");
/*     */             return;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 60:
/* 205 */           switch (buf[1] & 0xFF) {
/*     */             default:
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 0:
/* 214 */               if (buf[2] == 63 && buf[3] == 0) {
/* 215 */                 setEncoding(pb, "UnicodeLittle");
/*     */                 return;
/*     */               } 
/*     */               break;
/*     */             
/*     */             case 63:
/*     */               break;
/*     */           } 
/* 223 */           if (buf[2] != 120 || buf[3] != 109) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 229 */           useEncodingDecl(pb, "UTF8");
/*     */           return;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 76:
/* 236 */           if (buf[1] == 111 && (0xFF & buf[2]) == 167 && (0xFF & buf[3]) == 148) {
/*     */ 
/*     */             
/* 239 */             useEncodingDecl(pb, "CP037");
/*     */             return;
/*     */           } 
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 254:
/* 247 */           if ((buf[1] & 0xFF) != 255)
/*     */             break; 
/* 249 */           setEncoding(pb, "UTF-16");
/*     */           return;
/*     */ 
/*     */         
/*     */         case 255:
/* 254 */           if ((buf[1] & 0xFF) != 254)
/*     */             break; 
/* 256 */           setEncoding(pb, "UTF-16");
/*     */           return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 268 */     setEncoding(pb, "UTF-8");
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
/*     */   private void useEncodingDecl(PushbackInputStream pb, String encoding) throws IOException {
/* 283 */     byte[] buffer = new byte[512];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     int len = pb.read(buffer, 0, buffer.length);
/* 295 */     pb.unread(buffer, 0, len);
/* 296 */     Reader r = new InputStreamReader(new ByteArrayInputStream(buffer, 4, len), encoding);
/*     */ 
/*     */ 
/*     */     
/*     */     int c;
/*     */ 
/*     */     
/* 303 */     if ((c = r.read()) != 108) {
/* 304 */       setEncoding(pb, "UTF-8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     StringBuffer buf = new StringBuffer();
/* 318 */     StringBuffer keyBuf = null;
/* 319 */     String key = null;
/* 320 */     boolean sawEq = false;
/* 321 */     char quoteChar = Character.MIN_VALUE;
/* 322 */     boolean sawQuestion = false;
/*     */     
/*     */     int i;
/* 325 */     label81: for (i = 0; i < 507 && (
/* 326 */       c = r.read()) != -1; i++) {
/*     */ 
/*     */ 
/*     */       
/* 330 */       if (c == 32 || c == 9 || c == 10 || c == 13) {
/*     */         continue;
/*     */       }
/*     */       
/* 334 */       if (i == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 338 */       if (c == 63) {
/* 339 */         sawQuestion = true;
/* 340 */       } else if (sawQuestion) {
/* 341 */         if (c == 62)
/*     */           break; 
/* 343 */         sawQuestion = false;
/*     */       } 
/*     */ 
/*     */       
/* 347 */       if (key == null || !sawEq) {
/* 348 */         if (keyBuf == null) {
/* 349 */           if (!Character.isWhitespace((char)c))
/*     */           
/* 351 */           { keyBuf = buf;
/* 352 */             buf.setLength(0);
/* 353 */             buf.append((char)c);
/* 354 */             sawEq = false; } 
/* 355 */         } else if (Character.isWhitespace((char)c)) {
/* 356 */           key = keyBuf.toString();
/* 357 */         } else if (c == 61) {
/* 358 */           if (key == null)
/* 359 */             key = keyBuf.toString(); 
/* 360 */           sawEq = true;
/* 361 */           keyBuf = null;
/* 362 */           quoteChar = Character.MIN_VALUE;
/*     */         } else {
/* 364 */           keyBuf.append((char)c);
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 369 */       if (Character.isWhitespace((char)c))
/*     */         continue; 
/* 371 */       if (c == 34 || c == 39) {
/* 372 */         if (quoteChar == '\000') {
/* 373 */           quoteChar = (char)c;
/* 374 */           buf.setLength(0); continue;
/*     */         } 
/* 376 */         if (c == quoteChar) {
/* 377 */           if ("encoding".equals(key)) {
/* 378 */             this.assignedEncoding = buf.toString();
/*     */ 
/*     */             
/* 381 */             for (i = 0; i < this.assignedEncoding.length(); i++) {
/* 382 */               c = this.assignedEncoding.charAt(i);
/* 383 */               if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
/*     */                 continue;
/*     */               }
/* 386 */               if (i == 0)
/*     */                 break label81; 
/* 388 */               if (i > 0) { if (c == 45 || (c >= 48 && c <= 57) || c == 46 || c == 95) {
/*     */                   continue;
/*     */                 }
/*     */                 
/*     */                 break label81; }
/*     */               
/*     */               break label81;
/*     */             } 
/* 396 */             setEncoding(pb, this.assignedEncoding);
/*     */             
/*     */             return;
/*     */           } 
/* 400 */           key = null;
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/* 405 */       buf.append((char)c);
/*     */       continue;
/*     */     } 
/* 408 */     setEncoding(pb, "UTF-8");
/*     */   }
/*     */ 
/*     */   
/*     */   private void setEncoding(InputStream stream, String encoding) throws IOException {
/* 413 */     this.assignedEncoding = encoding;
/* 414 */     this.in = createReader(stream, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] buf, int off, int len) throws IOException {
/* 423 */     if (this.closed)
/* 424 */       return -1; 
/* 425 */     int val = this.in.read(buf, off, len);
/* 426 */     if (val == -1)
/* 427 */       close(); 
/* 428 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 437 */     if (this.closed)
/* 438 */       throw new IOException("closed"); 
/* 439 */     int val = this.in.read();
/* 440 */     if (val == -1)
/* 441 */       close(); 
/* 442 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 449 */     return (this.in == null) ? false : this.in.markSupported();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int value) throws IOException {
/* 459 */     if (this.in != null) this.in.mark(value);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 466 */     if (this.in != null) this.in.reset();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long value) throws IOException {
/* 473 */     return (this.in == null) ? 0L : this.in.skip(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ready() throws IOException {
/* 480 */     return (this.in == null) ? false : this.in.ready();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 487 */     if (this.closed)
/*     */       return; 
/* 489 */     this.in.close();
/* 490 */     this.in = null;
/* 491 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseReader
/*     */     extends Reader
/*     */   {
/*     */     protected InputStream instream;
/*     */     
/*     */     protected byte[] buffer;
/*     */     
/*     */     protected int start;
/*     */     
/*     */     protected int finish;
/*     */ 
/*     */     
/*     */     BaseReader(InputStream stream) {
/* 508 */       super(stream);
/*     */       
/* 510 */       this.instream = stream;
/* 511 */       this.buffer = new byte[8192];
/*     */     }
/*     */     
/*     */     public boolean ready() throws IOException {
/* 515 */       return (this.instream == null || this.finish - this.start > 0 || this.instream.available() != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 522 */       if (this.instream != null) {
/* 523 */         this.instream.close();
/* 524 */         this.start = this.finish = 0;
/* 525 */         this.buffer = null;
/* 526 */         this.instream = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Utf8Reader
/*     */     extends BaseReader
/*     */   {
/*     */     private char nextChar;
/*     */ 
/*     */ 
/*     */     
/*     */     Utf8Reader(InputStream stream) {
/* 542 */       super(stream);
/*     */     }
/*     */     
/*     */     public int read(char[] buf, int offset, int len) throws IOException {
/* 546 */       int i = 0, c = 0;
/*     */       
/* 548 */       if (len <= 0) {
/* 549 */         return 0;
/*     */       }
/*     */       
/* 552 */       if (this.nextChar != '\000') {
/* 553 */         buf[offset + i++] = this.nextChar;
/* 554 */         this.nextChar = Character.MIN_VALUE;
/*     */       } 
/*     */       
/* 557 */       while (i < len) {
/*     */         
/* 559 */         if (this.finish <= this.start) {
/* 560 */           if (this.instream == null) {
/* 561 */             c = -1;
/*     */             break;
/*     */           } 
/* 564 */           this.start = 0;
/* 565 */           this.finish = this.instream.read(this.buffer, 0, this.buffer.length);
/* 566 */           if (this.finish <= 0) {
/* 567 */             close();
/* 568 */             c = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 585 */         c = this.buffer[this.start] & 0xFF;
/* 586 */         if ((c & 0x80) == 0) {
/*     */           
/* 588 */           this.start++;
/* 589 */           buf[offset + i++] = (char)c;
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 597 */         int off = this.start;
/*     */ 
/*     */         
/*     */         try {
/* 601 */           if ((this.buffer[off] & 0xE0) == 192) {
/* 602 */             c = (this.buffer[off++] & 0x1F) << 6;
/* 603 */             c += this.buffer[off++] & 0x3F;
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 608 */           else if ((this.buffer[off] & 0xF0) == 224) {
/* 609 */             c = (this.buffer[off++] & 0xF) << 12;
/* 610 */             c += (this.buffer[off++] & 0x3F) << 6;
/* 611 */             c += this.buffer[off++] & 0x3F;
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 616 */           else if ((this.buffer[off] & 0xF8) == 240) {
/* 617 */             c = (this.buffer[off++] & 0x7) << 18;
/* 618 */             c += (this.buffer[off++] & 0x3F) << 12;
/* 619 */             c += (this.buffer[off++] & 0x3F) << 6;
/* 620 */             c += this.buffer[off++] & 0x3F;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 625 */             if (c > 1114111) {
/* 626 */               throw new CharConversionException("UTF-8 encoding of character 0x00" + Integer.toHexString(c) + " can't be converted to Unicode.");
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 631 */             c -= 65536;
/* 632 */             this.nextChar = (char)(56320 + (c & 0x3FF));
/* 633 */             c = 55296 + (c >> 10);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 638 */             throw new CharConversionException("Unconvertible UTF-8 character beginning with 0x" + Integer.toHexString(this.buffer[this.start] & 0xFF));
/*     */           }
/*     */         
/*     */         }
/* 642 */         catch (ArrayIndexOutOfBoundsException e) {
/*     */           
/* 644 */           c = 0;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 653 */         if (off > this.finish) {
/* 654 */           System.arraycopy(this.buffer, this.start, this.buffer, 0, this.finish - this.start);
/*     */           
/* 656 */           this.finish -= this.start;
/* 657 */           this.start = 0;
/* 658 */           off = this.instream.read(this.buffer, this.finish, this.buffer.length - this.finish);
/*     */           
/* 660 */           if (off < 0) {
/* 661 */             close();
/* 662 */             throw new CharConversionException("Partial UTF-8 char");
/*     */           } 
/* 664 */           this.finish += off;
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 671 */         this.start++; for (; this.start < off; this.start++) {
/* 672 */           if ((this.buffer[this.start] & 0xC0) != 128) {
/* 673 */             close();
/* 674 */             throw new CharConversionException("Malformed UTF-8 char -- is an XML encoding declaration missing?");
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 682 */         buf[offset + i++] = (char)c;
/* 683 */         if (this.nextChar != '\000' && i < len) {
/* 684 */           buf[offset + i++] = this.nextChar;
/* 685 */           this.nextChar = Character.MIN_VALUE;
/*     */         } 
/*     */       } 
/* 688 */       if (i > 0)
/* 689 */         return i; 
/* 690 */       return (c == -1) ? -1 : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AsciiReader
/*     */     extends BaseReader
/*     */   {
/*     */     AsciiReader(InputStream in) {
/* 704 */       super(in);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(char[] buf, int offset, int len) throws IOException {
/* 710 */       if (this.instream == null)
/* 711 */         return -1; 
/*     */       int i;
/* 713 */       for (i = 0; i < len; i++) {
/* 714 */         if (this.start >= this.finish) {
/* 715 */           this.start = 0;
/* 716 */           this.finish = this.instream.read(this.buffer, 0, this.buffer.length);
/* 717 */           if (this.finish <= 0) {
/* 718 */             if (this.finish <= 0)
/* 719 */               close(); 
/*     */             break;
/*     */           } 
/*     */         } 
/* 723 */         int c = this.buffer[this.start++];
/* 724 */         if ((c & 0x80) != 0) {
/* 725 */           throw new CharConversionException("Illegal ASCII character, 0x" + Integer.toHexString(c & 0xFF));
/*     */         }
/* 727 */         buf[offset + i] = (char)c;
/*     */       } 
/* 729 */       if (i == 0 && this.finish <= 0)
/* 730 */         return -1; 
/* 731 */       return i;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Iso8859_1Reader extends BaseReader {
/*     */     Iso8859_1Reader(InputStream in) {
/* 737 */       super(in);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(char[] buf, int offset, int len) throws IOException {
/* 743 */       if (this.instream == null)
/* 744 */         return -1; 
/*     */       int i;
/* 746 */       for (i = 0; i < len; i++) {
/* 747 */         if (this.start >= this.finish) {
/* 748 */           this.start = 0;
/* 749 */           this.finish = this.instream.read(this.buffer, 0, this.buffer.length);
/* 750 */           if (this.finish <= 0) {
/* 751 */             if (this.finish <= 0)
/* 752 */               close(); 
/*     */             break;
/*     */           } 
/*     */         } 
/* 756 */         buf[offset + i] = (char)(0xFF & this.buffer[this.start++]);
/*     */       } 
/* 758 */       if (i == 0 && this.finish <= 0)
/* 759 */         return -1; 
/* 760 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\dtdparser\XmlReader.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */