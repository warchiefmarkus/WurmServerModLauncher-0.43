/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import javax.xml.bind.annotation.DomHandler;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.sax.TransformerHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DomLoader<ResultT extends Result>
/*     */   extends Loader
/*     */ {
/*     */   private final DomHandler<?, ResultT> dom;
/*     */   
/*     */   private final class State
/*     */   {
/*  63 */     private final TransformerHandler handler = JAXBContextImpl.createTransformerHandler();
/*     */ 
/*     */     
/*     */     private final ResultT result;
/*     */ 
/*     */     
/*  69 */     int depth = 1;
/*     */     
/*     */     public State(UnmarshallingContext context) throws SAXException {
/*  72 */       this.result = (ResultT)DomLoader.this.dom.createUnmarshaller(context);
/*     */       
/*  74 */       this.handler.setResult((Result)this.result);
/*     */ 
/*     */       
/*     */       try {
/*  78 */         this.handler.setDocumentLocator(context.getLocator());
/*  79 */         this.handler.startDocument();
/*  80 */         declarePrefixes(context, context.getAllDeclaredPrefixes());
/*  81 */       } catch (SAXException e) {
/*  82 */         context.handleError(e);
/*  83 */         throw e;
/*     */       } 
/*     */     }
/*     */     
/*     */     public Object getElement() {
/*  88 */       return DomLoader.this.dom.getElement((Result)this.result);
/*     */     }
/*     */     
/*     */     private void declarePrefixes(UnmarshallingContext context, String[] prefixes) throws SAXException {
/*  92 */       for (int i = prefixes.length - 1; i >= 0; i--) {
/*  93 */         String nsUri = context.getNamespaceURI(prefixes[i]);
/*  94 */         if (nsUri == null) throw new IllegalStateException("prefix '" + prefixes[i] + "' isn't bound"); 
/*  95 */         this.handler.startPrefixMapping(prefixes[i], nsUri);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void undeclarePrefixes(String[] prefixes) throws SAXException {
/* 100 */       for (int i = prefixes.length - 1; i >= 0; i--)
/* 101 */         this.handler.endPrefixMapping(prefixes[i]); 
/*     */     }
/*     */   }
/*     */   
/*     */   public DomLoader(DomHandler<?, ResultT> dom) {
/* 106 */     super(true);
/* 107 */     this.dom = dom;
/*     */   }
/*     */   
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 111 */     UnmarshallingContext context = state.getContext();
/* 112 */     if (state.target == null) {
/* 113 */       state.target = new State(context);
/*     */     }
/* 115 */     State s = (State)state.target;
/*     */     try {
/* 117 */       s.declarePrefixes(context, context.getNewlyDeclaredPrefixes());
/* 118 */       s.handler.startElement(ea.uri, ea.local, ea.getQname(), ea.atts);
/* 119 */     } catch (SAXException e) {
/* 120 */       context.handleError(e);
/* 121 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 127 */     state.loader = this;
/* 128 */     State s = (State)state.prev.target;
/* 129 */     s.depth++;
/* 130 */     state.target = s;
/*     */   }
/*     */   
/*     */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/* 134 */     if (text.length() == 0)
/*     */       return; 
/*     */     try {
/* 137 */       State s = (State)state.target;
/* 138 */       s.handler.characters(text.toString().toCharArray(), 0, text.length());
/* 139 */     } catch (SAXException e) {
/* 140 */       state.getContext().handleError(e);
/* 141 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 146 */     State s = (State)state.target;
/* 147 */     UnmarshallingContext context = state.getContext();
/*     */     
/*     */     try {
/* 150 */       s.handler.endElement(ea.uri, ea.local, ea.getQname());
/* 151 */       s.undeclarePrefixes(context.getNewlyDeclaredPrefixes());
/* 152 */     } catch (SAXException e) {
/* 153 */       context.handleError(e);
/* 154 */       throw e;
/*     */     } 
/*     */     
/* 157 */     if (--s.depth == 0) {
/*     */       
/*     */       try {
/* 160 */         s.undeclarePrefixes(context.getAllDeclaredPrefixes());
/* 161 */         s.handler.endDocument();
/* 162 */       } catch (SAXException e) {
/* 163 */         context.handleError(e);
/* 164 */         throw e;
/*     */       } 
/*     */ 
/*     */       
/* 168 */       state.target = s.getElement();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\DomLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */