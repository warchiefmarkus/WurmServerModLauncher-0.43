/*     */ package com.sun.xml.xsom;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.scd.Iterators;
/*     */ import com.sun.xml.xsom.impl.scd.ParseException;
/*     */ import com.sun.xml.xsom.impl.scd.SCDImpl;
/*     */ import com.sun.xml.xsom.impl.scd.SCDParser;
/*     */ import com.sun.xml.xsom.impl.scd.Step;
/*     */ import com.sun.xml.xsom.impl.scd.TokenMgrError;
/*     */ import com.sun.xml.xsom.util.DeferedCollection;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SCD
/*     */ {
/*     */   public static SCD create(String path, NamespaceContext nsContext) throws ParseException {
/*     */     try {
/*  50 */       SCDParser p = new SCDParser(path, nsContext);
/*  51 */       List<?> list = p.RelativeSchemaComponentPath();
/*  52 */       return (SCD)new SCDImpl(path, list.<Step>toArray(new Step[list.size()]));
/*  53 */     } catch (TokenMgrError e) {
/*  54 */       throw setCause(new ParseException(e.getMessage(), -1), e);
/*  55 */     } catch (ParseException e) {
/*  56 */       throw setCause(new ParseException(e.getMessage(), e.currentToken.beginColumn), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ParseException setCause(ParseException e, Throwable x) {
/*  61 */     e.initCause(x);
/*  62 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Collection<XSComponent> select(XSComponent contextNode) {
/*  73 */     return (Collection<XSComponent>)new DeferedCollection(select(Iterators.singleton(contextNode)));
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
/*     */   public final Collection<XSComponent> select(XSSchemaSet contextNode) {
/*  88 */     return select((Collection)contextNode.getSchemas());
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
/*     */   public final XSComponent selectSingle(XSComponent contextNode) {
/* 100 */     Iterator<XSComponent> r = select(Iterators.singleton(contextNode));
/* 101 */     if (r.hasNext()) return r.next(); 
/* 102 */     return null;
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
/*     */   public final XSComponent selectSingle(XSSchemaSet contextNode) {
/* 114 */     Iterator<XSComponent> r = select((Iterator)contextNode.iterateSchema());
/* 115 */     if (r.hasNext()) return r.next(); 
/* 116 */     return null;
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
/*     */   public abstract Iterator<XSComponent> select(Iterator<? extends XSComponent> paramIterator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Collection<XSComponent> select(Collection<? extends XSComponent> contextNodes) {
/* 144 */     return (Collection<XSComponent>)new DeferedCollection(select(contextNodes.iterator()));
/*     */   }
/*     */   
/*     */   public abstract String toString();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\SCD.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */