/*     */ package com.sun.tools.jxc.gen.config;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ public abstract class NGCCHandler
/*     */   implements NGCCEventReceiver
/*     */ {
/*     */   protected final NGCCHandler _parent;
/*     */   protected final NGCCEventSource _source;
/*     */   protected final int _cookie;
/*     */   
/*     */   protected NGCCHandler(NGCCEventSource source, NGCCHandler parent, int parentCookie) {
/*  15 */     this._parent = parent;
/*  16 */     this._source = source;
/*  17 */     this._cookie = parentCookie;
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
/*     */   protected abstract NGCCRuntime getRuntime();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onChildCompleted(Object paramObject, int paramInt, boolean paramBoolean) throws SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnChildFromEnterElement(NGCCEventReceiver child, String uri, String localname, String qname, Attributes atts) throws SAXException {
/*  73 */     int id = this._source.replace(this, child);
/*  74 */     this._source.sendEnterElement(id, uri, localname, qname, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromEnterAttribute(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/*  79 */     int id = this._source.replace(this, child);
/*  80 */     this._source.sendEnterAttribute(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromLeaveElement(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/*  85 */     int id = this._source.replace(this, child);
/*  86 */     this._source.sendLeaveElement(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromLeaveAttribute(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/*  91 */     int id = this._source.replace(this, child);
/*  92 */     this._source.sendLeaveAttribute(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromText(NGCCEventReceiver child, String value) throws SAXException {
/*  97 */     int id = this._source.replace(this, child);
/*  98 */     this._source.sendText(id, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revertToParentFromEnterElement(Object result, int cookie, String uri, String local, String qname, Attributes atts) throws SAXException {
/* 109 */     int id = this._source.replace(this, this._parent);
/* 110 */     this._parent.onChildCompleted(result, cookie, true);
/* 111 */     this._source.sendEnterElement(id, uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromLeaveElement(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 116 */     if (uri == "\000" && uri == local && uri == qname && this._parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     int id = this._source.replace(this, this._parent);
/* 123 */     this._parent.onChildCompleted(result, cookie, true);
/* 124 */     this._source.sendLeaveElement(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromEnterAttribute(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 129 */     int id = this._source.replace(this, this._parent);
/* 130 */     this._parent.onChildCompleted(result, cookie, true);
/* 131 */     this._source.sendEnterAttribute(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromLeaveAttribute(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 136 */     int id = this._source.replace(this, this._parent);
/* 137 */     this._parent.onChildCompleted(result, cookie, true);
/* 138 */     this._source.sendLeaveAttribute(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromText(Object result, int cookie, String text) throws SAXException {
/* 143 */     int id = this._source.replace(this, this._parent);
/* 144 */     this._parent.onChildCompleted(result, cookie, true);
/* 145 */     this._source.sendText(id, text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unexpectedEnterElement(String qname) throws SAXException {
/* 155 */     getRuntime().unexpectedX('<' + qname + '>');
/*     */   }
/*     */   public void unexpectedLeaveElement(String qname) throws SAXException {
/* 158 */     getRuntime().unexpectedX("</" + qname + '>');
/*     */   }
/*     */   public void unexpectedEnterAttribute(String qname) throws SAXException {
/* 161 */     getRuntime().unexpectedX('@' + qname);
/*     */   }
/*     */   public void unexpectedLeaveAttribute(String qname) throws SAXException {
/* 164 */     getRuntime().unexpectedX("/@" + qname);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\gen\config\NGCCHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */