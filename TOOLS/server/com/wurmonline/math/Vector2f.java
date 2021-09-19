/*     */ package com.wurmonline.math;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Vector2f
/*     */   implements Externalizable, Cloneable
/*     */ {
/*  18 */   private static final Logger logger = Logger.getLogger(Vector2f.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float x;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float y;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f(float x, float y) {
/*  40 */     this.x = x;
/*  41 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f() {
/*  49 */     this.x = this.y = 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f(Vector2f vector2f) {
/*  60 */     this.x = vector2f.x;
/*  61 */     this.y = vector2f.y;
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
/*     */   public Vector2f set(float x, float y) {
/*  75 */     this.x = x;
/*  76 */     this.y = y;
/*  77 */     return this;
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
/*     */   public Vector2f set(Vector2f vec) {
/*  89 */     this.x = vec.x;
/*  90 */     this.y = vec.y;
/*  91 */     return this;
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
/*     */   public Vector2f add(Vector2f vec) {
/* 104 */     if (null == vec) {
/*     */       
/* 106 */       logger.warning("Provided vector is null, null returned.");
/* 107 */       return null;
/*     */     } 
/* 109 */     return new Vector2f(this.x + vec.x, this.y + vec.y);
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
/*     */   public Vector2f add(float dx, float dy) {
/* 121 */     return new Vector2f(this.x + dx, this.y + dy);
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
/*     */   public Vector2f addLocal(Vector2f vec) {
/* 134 */     if (null == vec) {
/*     */       
/* 136 */       logger.warning("Provided vector is null, null returned.");
/* 137 */       return null;
/*     */     } 
/* 139 */     this.x += vec.x;
/* 140 */     this.y += vec.y;
/* 141 */     return this;
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
/*     */   public Vector2f addLocal(float addX, float addY) {
/* 157 */     this.x += addX;
/* 158 */     this.y += addY;
/* 159 */     return this;
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
/*     */   public Vector2f add(Vector2f vec, Vector2f result) {
/* 173 */     if (null == vec) {
/*     */       
/* 175 */       logger.warning("Provided vector is null, null returned.");
/* 176 */       return null;
/*     */     } 
/* 178 */     if (result == null)
/* 179 */       result = new Vector2f(); 
/* 180 */     this.x += vec.x;
/* 181 */     this.y += vec.y;
/* 182 */     return result;
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
/*     */   public float dot(Vector2f vec) {
/* 195 */     if (null == vec) {
/*     */       
/* 197 */       logger.warning("Provided vector is null, 0 returned.");
/* 198 */       return 0.0F;
/*     */     } 
/* 200 */     return this.x * vec.x + this.y * vec.y;
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
/*     */   public Vector3f cross(Vector2f v) {
/* 212 */     return new Vector3f(0.0F, 0.0F, determinant(v));
/*     */   }
/*     */ 
/*     */   
/*     */   public float determinant(Vector2f v) {
/* 217 */     return this.x * v.y - this.y * v.x;
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
/*     */   public void interpolate(Vector2f finalVec, float changeAmnt) {
/* 231 */     this.x = (1.0F - changeAmnt) * this.x + changeAmnt * finalVec.x;
/* 232 */     this.y = (1.0F - changeAmnt) * this.y + changeAmnt * finalVec.y;
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
/*     */   public void interpolate(Vector2f beginVec, Vector2f finalVec, float changeAmnt) {
/* 248 */     this.x = (1.0F - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
/* 249 */     this.y = (1.0F - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
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
/*     */   public static boolean isValidVector(Vector2f vector) {
/* 261 */     if (vector == null)
/* 262 */       return false; 
/* 263 */     if (Float.isNaN(vector.x) || Float.isNaN(vector.y))
/* 264 */       return false; 
/* 265 */     if (Float.isInfinite(vector.x) || Float.isInfinite(vector.y))
/* 266 */       return false; 
/* 267 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float length() {
/* 277 */     return FastMath.sqrt(lengthSquared());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float lengthSquared() {
/* 287 */     return this.x * this.x + this.y * this.y;
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
/*     */   public float distanceSquared(Vector2f v) {
/* 299 */     double dx = (this.x - v.x);
/* 300 */     double dy = (this.y - v.y);
/* 301 */     return (float)(dx * dx + dy * dy);
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
/*     */   public float distanceSquared(float otherX, float otherY) {
/* 315 */     double dx = (this.x - otherX);
/* 316 */     double dy = (this.y - otherY);
/* 317 */     return (float)(dx * dx + dy * dy);
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
/*     */   public float distance(Vector2f v) {
/* 329 */     return FastMath.sqrt(distanceSquared(v));
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
/*     */   public Vector2f mult(float scalar) {
/* 341 */     return new Vector2f(this.x * scalar, this.y * scalar);
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
/*     */   public Vector2f multLocal(float scalar) {
/* 354 */     this.x *= scalar;
/* 355 */     this.y *= scalar;
/* 356 */     return this;
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
/*     */   public Vector2f multLocal(Vector2f vec) {
/* 369 */     if (null == vec) {
/*     */       
/* 371 */       logger.warning("Provided vector is null, null returned.");
/* 372 */       return null;
/*     */     } 
/* 374 */     this.x *= vec.x;
/* 375 */     this.y *= vec.y;
/* 376 */     return this;
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
/*     */   public Vector2f mult(float scalar, Vector2f product) {
/* 391 */     if (null == product)
/*     */     {
/* 393 */       product = new Vector2f();
/*     */     }
/*     */     
/* 396 */     this.x *= scalar;
/* 397 */     this.y *= scalar;
/* 398 */     return product;
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
/*     */   public Vector2f divide(float scalar) {
/* 411 */     return new Vector2f(this.x / scalar, this.y / scalar);
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
/*     */   public Vector2f divideLocal(float scalar) {
/* 424 */     this.x /= scalar;
/* 425 */     this.y /= scalar;
/* 426 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f negate() {
/* 436 */     return new Vector2f(-this.x, -this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f negateLocal() {
/* 446 */     this.x = -this.x;
/* 447 */     this.y = -this.y;
/* 448 */     return this;
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
/*     */   public Vector2f subtract(Vector2f vec) {
/* 461 */     return subtract(vec, (Vector2f)null);
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
/*     */   public Vector2f subtract(Vector2f vec, Vector2f store) {
/* 476 */     if (store == null)
/* 477 */       store = new Vector2f(); 
/* 478 */     this.x -= vec.x;
/* 479 */     this.y -= vec.y;
/* 480 */     return store;
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
/*     */   public Vector2f subtract(float valX, float valY) {
/* 494 */     return new Vector2f(this.x - valX, this.y - valY);
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
/*     */   public Vector2f subtractLocal(Vector2f vec) {
/* 507 */     if (null == vec) {
/*     */       
/* 509 */       logger.warning("Provided vector is null, null returned.");
/* 510 */       return null;
/*     */     } 
/* 512 */     this.x -= vec.x;
/* 513 */     this.y -= vec.y;
/* 514 */     return this;
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
/*     */   public Vector2f subtractLocal(float valX, float valY) {
/* 529 */     this.x -= valX;
/* 530 */     this.y -= valY;
/* 531 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f normalize() {
/* 541 */     float length = length();
/* 542 */     if (length != 0.0F)
/*     */     {
/* 544 */       return divide(length);
/*     */     }
/*     */     
/* 547 */     return divide(1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f normalizeLocal() {
/* 557 */     float length = length();
/* 558 */     if (length != 0.0F)
/*     */     {
/* 560 */       return divideLocal(length);
/*     */     }
/*     */     
/* 563 */     return divideLocal(1.0F);
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
/*     */   public float smallestAngleBetween(Vector2f otherVector) {
/* 576 */     float dotProduct = dot(otherVector);
/* 577 */     float angle = FastMath.acos(dotProduct);
/* 578 */     return angle;
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
/*     */   public float angleBetween(Vector2f otherVector) {
/* 592 */     float angle = FastMath.atan2(otherVector.y, otherVector.x) - FastMath.atan2(this.y, this.x);
/* 593 */     return angle;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 598 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/* 603 */     this.x = x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 608 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(float y) {
/* 613 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngle() {
/* 624 */     return -FastMath.atan2(this.y, this.x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void zero() {
/* 632 */     this.x = this.y = 0.0F;
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
/*     */   public int hashCode() {
/* 644 */     int hash = 37;
/* 645 */     hash += 37 * hash + Float.floatToIntBits(this.x);
/* 646 */     hash += 37 * hash + Float.floatToIntBits(this.y);
/* 647 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f clone() {
/*     */     try {
/* 655 */       return (Vector2f)super.clone();
/*     */     }
/* 657 */     catch (CloneNotSupportedException e) {
/*     */       
/* 659 */       throw new AssertionError();
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
/*     */   public float[] toArray(float[] floats) {
/* 672 */     if (floats == null)
/*     */     {
/* 674 */       floats = new float[2];
/*     */     }
/* 676 */     floats[0] = this.x;
/* 677 */     floats[1] = this.y;
/* 678 */     return floats;
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
/*     */   public boolean equals(Object o) {
/* 691 */     if (!(o instanceof Vector2f))
/*     */     {
/* 693 */       return false;
/*     */     }
/*     */     
/* 696 */     if (this == o)
/*     */     {
/* 698 */       return true;
/*     */     }
/*     */     
/* 701 */     Vector2f comp = (Vector2f)o;
/* 702 */     if (Float.compare(this.x, comp.x) != 0)
/* 703 */       return false; 
/* 704 */     if (Float.compare(this.y, comp.y) != 0)
/* 705 */       return false; 
/* 706 */     return true;
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
/*     */   public String toString() {
/* 720 */     return "(" + this.x + ", " + this.y + ')';
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
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 735 */     this.x = in.readFloat();
/* 736 */     this.y = in.readFloat();
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
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 750 */     out.writeFloat(this.x);
/* 751 */     out.writeFloat(this.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends Vector2f> getClassTag() {
/* 756 */     return (Class)getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotateAroundOrigin(float angle, boolean cw) {
/* 761 */     if (cw)
/* 762 */       angle = -angle; 
/* 763 */     float newX = FastMath.cos(angle) * this.x - FastMath.sin(angle) * this.y;
/* 764 */     float newY = FastMath.sin(angle) * this.x + FastMath.cos(angle) * this.y;
/* 765 */     this.x = newX;
/* 766 */     this.y = newY;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Vector2f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */