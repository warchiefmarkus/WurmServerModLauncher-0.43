/*     */ package com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.Multiplicity;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RawTypeSet
/*     */ {
/*     */   public final Set<Ref> refs;
/*     */   public final Mode canBeTypeRefs;
/*     */   public final Multiplicity mul;
/*     */   private CElementPropertyInfo.CollectionMode collectionMode;
/*     */   
/*     */   public RawTypeSet(Set<Ref> refs, Multiplicity m) {
/*  81 */     this.refs = refs;
/*  82 */     this.mul = m;
/*  83 */     this.canBeTypeRefs = canBeTypeRefs();
/*     */   }
/*     */   
/*     */   public CElementPropertyInfo.CollectionMode getCollectionMode() {
/*  87 */     return this.collectionMode;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/*  91 */     return (this.mul.min > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Mode
/*     */   {
/* 103 */     SHOULD_BE_TYPEREF(0),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     CAN_BE_TYPEREF(1),
/*     */ 
/*     */ 
/*     */     
/* 112 */     MUST_BE_REFERENCE(2);
/*     */     
/*     */     private final int rank;
/*     */     
/*     */     Mode(int rank) {
/* 117 */       this.rank = rank;
/*     */     }
/*     */     
/*     */     Mode or(Mode that) {
/* 121 */       switch (Math.max(this.rank, that.rank)) { case 0:
/* 122 */           return SHOULD_BE_TYPEREF;
/* 123 */         case 1: return CAN_BE_TYPEREF;
/* 124 */         case 2: return MUST_BE_REFERENCE; }
/*     */       
/* 126 */       throw new AssertionError();
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
/*     */   private Mode canBeTypeRefs() {
/* 142 */     Set<NType> types = new HashSet<NType>();
/*     */     
/* 144 */     this.collectionMode = this.mul.isAtMostOnce() ? CElementPropertyInfo.CollectionMode.NOT_REPEATED : CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT;
/*     */ 
/*     */ 
/*     */     
/* 148 */     Mode mode = Mode.SHOULD_BE_TYPEREF;
/*     */     
/* 150 */     for (Ref r : this.refs) {
/* 151 */       mode = mode.or(r.canBeType(this));
/* 152 */       if (mode == Mode.MUST_BE_REFERENCE) {
/* 153 */         return mode;
/*     */       }
/* 155 */       if (!types.add(r.toTypeRef(null).getTarget().getType()))
/* 156 */         return Mode.MUST_BE_REFERENCE; 
/* 157 */       if (r.isListOfValues()) {
/* 158 */         if (this.refs.size() > 1 || !this.mul.isAtMostOnce())
/* 159 */           return Mode.MUST_BE_REFERENCE; 
/* 160 */         this.collectionMode = CElementPropertyInfo.CollectionMode.REPEATED_VALUE;
/*     */       } 
/*     */     } 
/* 163 */     return mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTo(CElementPropertyInfo prop) {
/* 170 */     assert this.canBeTypeRefs != Mode.MUST_BE_REFERENCE;
/* 171 */     if (this.mul.isZero()) {
/*     */       return;
/*     */     }
/* 174 */     List<CTypeRef> dst = prop.getTypes();
/* 175 */     for (Ref t : this.refs)
/* 176 */       dst.add(t.toTypeRef(prop)); 
/*     */   }
/*     */   
/*     */   public void addTo(CReferencePropertyInfo prop) {
/* 180 */     if (this.mul.isZero())
/*     */       return; 
/* 182 */     for (Ref t : this.refs)
/* 183 */       t.toElementRef(prop); 
/*     */   }
/*     */   
/*     */   public ID id() {
/* 187 */     for (Ref t : this.refs) {
/* 188 */       ID id = t.id();
/* 189 */       if (id != ID.NONE) return id; 
/*     */     } 
/* 191 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 195 */     for (Ref t : this.refs) {
/* 196 */       MimeType mt = t.getExpectedMimeType();
/* 197 */       if (mt != null) return mt; 
/*     */     } 
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Ref
/*     */   {
/*     */     protected abstract CTypeRef toTypeRef(CElementPropertyInfo param1CElementPropertyInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void toElementRef(CReferencePropertyInfo param1CReferencePropertyInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract RawTypeSet.Mode canBeType(RawTypeSet param1RawTypeSet);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract boolean isListOfValues();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract ID id();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected MimeType getExpectedMimeType() {
/* 235 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\RawTypeSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */