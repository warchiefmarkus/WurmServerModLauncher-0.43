/*     */ package com.wurmonline.math;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TilePos
/*     */   implements Cloneable
/*     */ {
/*     */   public int x;
/*     */   public int y;
/*     */   
/*     */   public TilePos() {
/*  43 */     this.x = 0;
/*  44 */     this.y = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TilePos(int x, int y) {
/*  55 */     this.x = x;
/*  56 */     this.y = y;
/*     */   }
/*     */ 
/*     */   
/*     */   public TilePos(TilePos rhs) {
/*  61 */     this.x = rhs.x;
/*  62 */     this.y = rhs.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TilePos fromXY(int tileX, int tileY) {
/*  73 */     return new TilePos(tileX, tileY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object clone() throws CloneNotSupportedException {
/*  80 */     TilePos newPos = (TilePos)super.clone();
/*  81 */     newPos.set(this.x, this.y);
/*  82 */     return newPos;
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
/*     */   public void set(int x, int y) {
/*  96 */     this.x = x;
/*  97 */     this.y = y;
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
/*     */   public TilePos add(int x, int y, TilePos storage) {
/* 110 */     if (storage == null)
/*     */     {
/* 112 */       storage = new TilePos();
/*     */     }
/* 114 */     storage.set(this.x + x, this.y + y);
/* 115 */     return storage;
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
/*     */   public TilePos North() {
/* 128 */     return fromXY(this.x, this.y - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TilePos South() {
/* 138 */     return new TilePos(this.x, this.y + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TilePos West() {
/* 148 */     return new TilePos(this.x - 1, this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TilePos East() {
/* 158 */     return fromXY(this.x + 1, this.y);
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
/*     */   public TilePos NorthWest() {
/* 170 */     return fromXY(this.x - 1, this.y - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public TilePos SouthEast() {
/* 175 */     return new TilePos(this.x + 1, this.y + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public TilePos NorthEast() {
/* 180 */     return fromXY(this.x + 1, this.y - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public TilePos SouthWest() {
/* 185 */     return new TilePos(this.x - 1, this.y + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 195 */     if (super.equals(obj))
/* 196 */       return true; 
/* 197 */     if (!(obj instanceof TilePos))
/*     */     {
/* 199 */       return false;
/*     */     }
/* 201 */     TilePos pos = (TilePos)obj;
/* 202 */     return ((this.x == pos.x)) & ((this.y == pos.y));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(TilePos pos) {
/* 208 */     return (this.x == pos.x && this.y == pos.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 214 */     return "tilePos: " + this.x + ", " + this.y;
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
/*     */   public static Iterable<TilePos> areaIterator(TilePos minPos, TilePos maxPos) {
/* 240 */     return new Area(minPos, maxPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<TilePos> areaIterator(int x1, int y1, int x2, int y2) {
/* 251 */     return new Area(x1, y1, x2, y2);
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
/*     */   public static Iterable<TilePos> bordersIterator(TilePos minPos, TilePos maxPos) {
/* 264 */     return new Borders(minPos, maxPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<TilePos> bordersIterator(int x1, int y1, int x2, int y2) {
/* 275 */     return new Borders(x1, y1, x2, y2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class IteratorPositions
/*     */     implements Iterator<TilePos>
/*     */   {
/*     */     final int x1;
/*     */ 
/*     */     
/*     */     final int y1;
/*     */ 
/*     */     
/*     */     final int x2;
/*     */ 
/*     */     
/*     */     final int y2;
/*     */     
/* 294 */     final TilePos curPos = new TilePos();
/* 295 */     final TilePos userPos = new TilePos();
/*     */ 
/*     */     
/*     */     IteratorPositions(int x1, int y1, int x2, int y2) {
/* 299 */       assert x1 <= x2;
/* 300 */       assert y1 <= y2;
/* 301 */       assert x1 < x2 || y1 < y2;
/*     */       
/* 303 */       this.x1 = x1;
/* 304 */       this.y1 = y1;
/* 305 */       this.x2 = x2;
/* 306 */       this.y2 = y2;
/* 307 */       this.curPos.set(x1, y1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 313 */       return (this.curPos.x < this.x2 || this.curPos.y < this.y2);
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
/*     */   private static class Area
/*     */     implements Iterable<TilePos>
/*     */   {
/*     */     private final AreaIterator it;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Area(TilePos posMin, TilePos posMax) {
/* 354 */       this.it = new AreaIterator(posMin, posMax);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Area(int x1, int y1, int x2, int y2) {
/* 367 */       this.it = new AreaIterator(x1, y1, x2, y2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<TilePos> iterator() {
/* 373 */       return this.it;
/*     */     }
/*     */ 
/*     */     
/*     */     private static class AreaIterator
/*     */       extends TilePos.IteratorPositions
/*     */     {
/*     */       AreaIterator(TilePos posMin, TilePos posMax) {
/* 381 */         this(posMin.x, posMin.y, posMax.x, posMax.y);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       AreaIterator(int x1, int y1, int x2, int y2) {
/* 387 */         super(x1, y1, x2, y2);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public TilePos next() {
/* 398 */         assert this.curPos.x <= this.x2 || this.curPos.y <= this.y2;
/* 399 */         if (this.curPos.x >= this.x2 && this.curPos.y >= this.y2)
/*     */         {
/* 401 */           throw new NoSuchElementException("This condition should not be possible!");
/*     */         }
/* 403 */         this.curPos.x++;
/* 404 */         if (this.curPos.x > this.x2) {
/*     */           
/* 406 */           this.curPos.x = this.x1;
/* 407 */           this.curPos.y++;
/*     */         } 
/* 409 */         this.userPos.set(this.curPos.x, this.curPos.y);
/* 410 */         return this.userPos;
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
/*     */   
/*     */   private static class Borders
/*     */     implements Iterable<TilePos>
/*     */   {
/*     */     private final BorderIterator it;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Borders(TilePos posMin, TilePos posMax) {
/* 463 */       this.it = new BorderIterator(posMin, posMax);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Borders(int x1, int y1, int x2, int y2) {
/* 477 */       this.it = new BorderIterator(x1, y1, x2, y2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<TilePos> iterator() {
/* 483 */       return this.it;
/*     */     }
/*     */ 
/*     */     
/*     */     private static class BorderIterator
/*     */       extends TilePos.IteratorPositions
/*     */     {
/*     */       BorderIterator(TilePos posMin, TilePos posMax) {
/* 491 */         this(posMin.x, posMin.y, posMax.x, posMax.y);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       BorderIterator(int x1, int y1, int x2, int y2) {
/* 497 */         super(x1, y1, x2, y2);
/*     */         
/* 499 */         assert x1 < x2 + 2 && y1 < y2 + 2;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public TilePos next() {
/* 510 */         assert this.curPos.x <= this.x2 || this.curPos.y <= this.y2;
/* 511 */         if (this.curPos.x >= this.x2 && this.curPos.y >= this.y2)
/*     */         {
/* 513 */           throw new NoSuchElementException("This condition should not be possible!");
/*     */         }
/* 515 */         if (this.curPos.y == this.y1 || this.curPos.y == this.y2) {
/*     */           
/* 517 */           this.curPos.x++;
/* 518 */           if (this.curPos.x > this.x2)
/*     */           {
/* 520 */             this.curPos.x = this.x1;
/* 521 */             this.curPos.y++;
/* 522 */             assert this.curPos.y <= this.y2 : "This condition should not be possible!";
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 528 */           assert this.curPos.x == this.x1 || this.curPos.x == this.x2 : "This condition should not be possible!";
/* 529 */           this.curPos.x = (this.curPos.x == this.x1) ? this.x2 : this.x1;
/* 530 */           if (this.curPos.x == this.x1)
/*     */           {
/* 532 */             this.curPos.y++;
/*     */           }
/*     */         } 
/* 535 */         this.userPos.set(this.curPos.x, this.curPos.y);
/* 536 */         return this.userPos;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\TilePos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */