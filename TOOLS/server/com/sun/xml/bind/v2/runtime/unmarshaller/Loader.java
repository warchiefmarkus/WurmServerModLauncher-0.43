/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public abstract class Loader
/*     */ {
/*     */   protected boolean expectText;
/*     */   
/*     */   protected Loader(boolean expectText) {
/*  60 */     this.expectText = expectText;
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
/*     */   protected Loader() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 101 */     reportUnexpectedChildElement(ea, true);
/* 102 */     state.loader = Discarder.INSTANCE;
/* 103 */     state.receiver = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void reportUnexpectedChildElement(TagName ea, boolean canRecover) throws SAXException {
/* 108 */     if (canRecover && !(UnmarshallingContext.getInstance()).parent.hasEventHandler()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (ea.uri != ea.uri.intern() || ea.local != ea.local.intern()) {
/* 114 */       reportError(Messages.UNINTERNED_STRINGS.format(new Object[0]), canRecover);
/*     */     } else {
/* 116 */       reportError(Messages.UNEXPECTED_ELEMENT.format(new Object[] { ea.uri, ea.local, computeExpectedElements() }, ), canRecover);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<QName> getExpectedChildElements() {
/* 123 */     return Collections.emptyList();
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
/*     */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/* 135 */     text = text.toString().replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').trim();
/* 136 */     reportError(Messages.UNEXPECTED_TEXT.format(new Object[] { text }, ), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean expectText() {
/* 144 */     return this.expectText;
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
/*     */   public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String computeExpectedElements() {
/* 174 */     StringBuilder r = new StringBuilder();
/*     */     
/* 176 */     for (QName n : getExpectedChildElements()) {
/* 177 */       if (r.length() != 0) r.append(','); 
/* 178 */       r.append("<{").append(n.getNamespaceURI()).append('}').append(n.getLocalPart()).append('>');
/*     */     } 
/* 180 */     if (r.length() == 0) {
/* 181 */       return "(none)";
/*     */     }
/*     */     
/* 184 */     return r.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireBeforeUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state) throws SAXException {
/* 194 */     if (beanInfo.lookForLifecycleMethods()) {
/* 195 */       UnmarshallingContext context = state.getContext();
/* 196 */       Unmarshaller.Listener listener = context.parent.getListener();
/* 197 */       if (beanInfo.hasBeforeUnmarshalMethod()) {
/* 198 */         beanInfo.invokeBeforeUnmarshalMethod(context.parent, child, state.prev.target);
/*     */       }
/* 200 */       if (listener != null) {
/* 201 */         listener.beforeUnmarshal(child, state.prev.target);
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
/*     */   protected final void fireAfterUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state) throws SAXException {
/* 214 */     if (beanInfo.lookForLifecycleMethods()) {
/* 215 */       UnmarshallingContext context = state.getContext();
/* 216 */       Unmarshaller.Listener listener = context.parent.getListener();
/* 217 */       if (beanInfo.hasAfterUnmarshalMethod()) {
/* 218 */         beanInfo.invokeAfterUnmarshalMethod(context.parent, child, state.target);
/*     */       }
/* 220 */       if (listener != null) {
/* 221 */         listener.afterUnmarshal(child, state.target);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void handleGenericException(Exception e) throws SAXException {
/* 230 */     handleGenericException(e, false);
/*     */   }
/*     */   
/*     */   public static void handleGenericException(Exception e, boolean canRecover) throws SAXException {
/* 234 */     reportError(e.getMessage(), e, canRecover);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void reportError(String msg, boolean canRecover) throws SAXException {
/* 239 */     reportError(msg, null, canRecover);
/*     */   }
/*     */   
/*     */   public static void reportError(String msg, Exception nested, boolean canRecover) throws SAXException {
/* 243 */     UnmarshallingContext context = UnmarshallingContext.getInstance();
/* 244 */     context.handleEvent((ValidationEvent)new ValidationEventImpl(canRecover ? 1 : 2, msg, context.getLocator().getLocation(), nested), canRecover);
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
/*     */   protected static void handleParseConversionException(UnmarshallingContext.State state, Exception e) throws SAXException {
/* 257 */     state.getContext().handleError(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\Loader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */