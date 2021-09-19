/*     */ package com.sun.xml.bind.v2.schemagen;
/*     */ 
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.ContentModelContainer;
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.Occurs;
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.Particle;
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.TypeDefParticle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Tree
/*     */ {
/*     */   Tree makeOptional(boolean really) {
/*  67 */     return really ? new Optional(this) : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Tree makeRepeated(boolean really) {
/*  78 */     return really ? new Repeated(this) : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Tree makeGroup(GroupKind kind, List<Tree> children) {
/*  86 */     if (children.size() == 1) {
/*  87 */       return children.get(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     List<Tree> normalizedChildren = new ArrayList<Tree>(children.size());
/*  94 */     for (Tree t : children) {
/*  95 */       if (t instanceof Group) {
/*  96 */         Group g = (Group)t;
/*  97 */         if (g.kind == kind) {
/*  98 */           normalizedChildren.addAll(Arrays.asList(g.children));
/*     */           continue;
/*     */         } 
/*     */       } 
/* 102 */       normalizedChildren.add(t);
/*     */     } 
/*     */     
/* 105 */     return new Group(kind, normalizedChildren.<Tree>toArray(new Tree[normalizedChildren.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean isNullable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean canBeTopLevel() {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void write(ContentModelContainer paramContentModelContainer, boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(TypeDefParticle ct) {
/* 133 */     if (canBeTopLevel()) {
/* 134 */       write((ContentModelContainer)ct._cast(ContentModelContainer.class), false, false);
/*     */     } else {
/*     */       
/* 137 */       (new Group(GroupKind.SEQUENCE, new Tree[] { this })).write(ct);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void writeOccurs(Occurs o, boolean isOptional, boolean repeated) {
/* 144 */     if (isOptional)
/* 145 */       o.minOccurs(0); 
/* 146 */     if (repeated) {
/* 147 */       o.maxOccurs("unbounded");
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class Term
/*     */     extends Tree
/*     */   {
/*     */     boolean isNullable() {
/* 155 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Optional
/*     */     extends Tree
/*     */   {
/*     */     private final Tree body;
/*     */     
/*     */     private Optional(Tree body) {
/* 166 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isNullable() {
/* 171 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     Tree makeOptional(boolean really) {
/* 176 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 181 */       this.body.write(parent, true, repeated);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Repeated
/*     */     extends Tree
/*     */   {
/*     */     private final Tree body;
/*     */     
/*     */     private Repeated(Tree body) {
/* 192 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isNullable() {
/* 197 */       return this.body.isNullable();
/*     */     }
/*     */ 
/*     */     
/*     */     Tree makeRepeated(boolean really) {
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 207 */       this.body.write(parent, isOptional, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Group
/*     */     extends Tree
/*     */   {
/*     */     private final GroupKind kind;
/*     */     private final Tree[] children;
/*     */     
/*     */     private Group(GroupKind kind, Tree... children) {
/* 219 */       this.kind = kind;
/* 220 */       this.children = children;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean canBeTopLevel() {
/* 225 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isNullable() {
/* 230 */       if (this.kind == GroupKind.CHOICE) {
/* 231 */         for (Tree t : this.children) {
/* 232 */           if (t.isNullable())
/* 233 */             return true; 
/*     */         } 
/* 235 */         return false;
/*     */       } 
/* 237 */       for (Tree t : this.children) {
/* 238 */         if (!t.isNullable())
/* 239 */           return false; 
/*     */       } 
/* 241 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 247 */       Particle c = this.kind.write(parent);
/* 248 */       writeOccurs((Occurs)c, isOptional, repeated);
/*     */       
/* 250 */       for (Tree child : this.children)
/* 251 */         child.write((ContentModelContainer)c, false, false); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\Tree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */