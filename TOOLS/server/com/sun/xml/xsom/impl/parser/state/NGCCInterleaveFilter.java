/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NGCCInterleaveFilter
/*     */   implements NGCCEventSource, NGCCEventReceiver
/*     */ {
/*     */   protected NGCCEventReceiver[] _receivers;
/*     */   private final NGCCHandler _parent;
/*     */   private final int _cookie;
/*     */   private int lockedReceiver;
/*     */   private int lockCount;
/*     */   private boolean isJoining;
/*     */   
/*     */   protected void setHandlers(NGCCEventReceiver[] receivers) {
/*     */     this._receivers = receivers;
/*     */   }
/*     */   
/*     */   public int replace(NGCCEventReceiver oldHandler, NGCCEventReceiver newHandler) {
/*     */     for (int i = 0; i < this._receivers.length; i++) {
/*     */       if (this._receivers[i] == oldHandler) {
/*     */         this._receivers[i] = newHandler;
/*     */         return i;
/*     */       } 
/*     */     } 
/*     */     throw new InternalError();
/*     */   }
/*     */   
/*     */   public void enterElement(String uri, String localName, String qname, Attributes atts) throws SAXException {
/*     */     if (this.isJoining)
/*     */       return; 
/*     */     if (this.lockCount++ == 0) {
/*     */       this.lockedReceiver = findReceiverOfElement(uri, localName);
/*     */       if (this.lockedReceiver == -1) {
/*     */         joinByEnterElement(null, uri, localName, qname, atts);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     this._receivers[this.lockedReceiver].enterElement(uri, localName, qname, atts);
/*     */   }
/*     */   
/*     */   protected NGCCInterleaveFilter(NGCCHandler parent, int cookie) {
/*  64 */     this.lockCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.isJoining = false;
/*     */     this._parent = parent;
/*     */     this._cookie = cookie; } public void leaveElement(String uri, String localName, String qname) throws SAXException {
/*     */     if (this.isJoining)
/*     */       return; 
/*     */     if (this.lockCount-- == 0) {
/*     */       joinByLeaveElement(null, uri, localName, qname);
/*     */     } else {
/*     */       this._receivers[this.lockedReceiver].leaveElement(uri, localName, qname);
/*     */     } 
/*     */   } public void enterAttribute(String uri, String localName, String qname) throws SAXException {
/*     */     if (this.isJoining)
/*     */       return; 
/*     */     if (this.lockCount++ == 0) {
/*     */       this.lockedReceiver = findReceiverOfAttribute(uri, localName);
/*     */       if (this.lockedReceiver == -1) {
/*     */         joinByEnterAttribute(null, uri, localName, qname);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     this._receivers[this.lockedReceiver].enterAttribute(uri, localName, qname);
/*     */   }
/* 182 */   public void joinByEnterElement(NGCCEventReceiver source, String uri, String local, String qname, Attributes atts) throws SAXException { if (this.isJoining)
/* 183 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     for (int i = 0; i < this._receivers.length; i++) {
/* 191 */       if (this._receivers[i] != source) {
/* 192 */         this._receivers[i].enterElement(uri, local, qname, atts);
/*     */       }
/*     */     } 
/* 195 */     this._parent._source.replace(this, this._parent);
/* 196 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 198 */     this._parent.enterElement(uri, local, qname, atts); }
/*     */   public void leaveAttribute(String uri, String localName, String qname) throws SAXException { if (this.isJoining)
/*     */       return;  if (this.lockCount-- == 0) {
/*     */       joinByLeaveAttribute(null, uri, localName, qname);
/*     */     } else {
/*     */       this._receivers[this.lockedReceiver].leaveAttribute(uri, localName, qname);
/* 204 */     }  } public void joinByLeaveElement(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException { if (this.isJoining)
/* 205 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     for (int i = 0; i < this._receivers.length; i++) {
/* 213 */       if (this._receivers[i] != source) {
/* 214 */         this._receivers[i].leaveElement(uri, local, qname);
/*     */       }
/*     */     } 
/* 217 */     this._parent._source.replace(this, this._parent);
/* 218 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 220 */     this._parent.leaveElement(uri, local, qname); }
/*     */   public void text(String value) throws SAXException { if (this.isJoining)
/*     */       return;  if (this.lockCount != 0) { this._receivers[this.lockedReceiver].text(value); }
/*     */     else { int receiver = findReceiverOfText(); if (receiver != -1) { this._receivers[receiver].text(value); }
/*     */       else { joinByText(null, value); }
/*     */        }
/* 226 */      } protected abstract int findReceiverOfElement(String paramString1, String paramString2); protected abstract int findReceiverOfAttribute(String paramString1, String paramString2); protected abstract int findReceiverOfText(); public void joinByEnterAttribute(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException { if (this.isJoining)
/* 227 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     for (int i = 0; i < this._receivers.length; i++) {
/* 235 */       if (this._receivers[i] != source) {
/* 236 */         this._receivers[i].enterAttribute(uri, local, qname);
/*     */       }
/*     */     } 
/* 239 */     this._parent._source.replace(this, this._parent);
/* 240 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 242 */     this._parent.enterAttribute(uri, local, qname); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinByLeaveAttribute(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException {
/* 248 */     if (this.isJoining)
/* 249 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     for (int i = 0; i < this._receivers.length; i++) {
/* 257 */       if (this._receivers[i] != source) {
/* 258 */         this._receivers[i].leaveAttribute(uri, local, qname);
/*     */       }
/*     */     } 
/* 261 */     this._parent._source.replace(this, this._parent);
/* 262 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 264 */     this._parent.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinByText(NGCCEventReceiver source, String value) throws SAXException {
/* 270 */     if (this.isJoining)
/* 271 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     for (int i = 0; i < this._receivers.length; i++) {
/* 279 */       if (this._receivers[i] != source) {
/* 280 */         this._receivers[i].text(value);
/*     */       }
/*     */     } 
/* 283 */     this._parent._source.replace(this, this._parent);
/* 284 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 286 */     this._parent.text(value);
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
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname) throws SAXException {
/* 300 */     this._receivers[threadId].enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts) throws SAXException {
/* 306 */     this._receivers[threadId].enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname) throws SAXException {
/* 312 */     this._receivers[threadId].leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname) throws SAXException {
/* 318 */     this._receivers[threadId].leaveElement(uri, local, qname);
/*     */   }
/*     */   
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 322 */     this._receivers[threadId].text(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\NGCCInterleaveFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */