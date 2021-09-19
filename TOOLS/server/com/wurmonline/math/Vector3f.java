/*      */ package com.wurmonline.math;
/*      */ 
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Vector3f
/*      */   implements Externalizable, Cloneable
/*      */ {
/*   19 */   private static final Logger logger = Logger.getLogger(Vector3f.class.getName());
/*      */   
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*   23 */   public static final Vector3f ZERO = new Vector3f(0.0F, 0.0F, 0.0F);
/*      */   
/*   25 */   public static final Vector3f UNIT_X = new Vector3f(1.0F, 0.0F, 0.0F);
/*   26 */   public static final Vector3f UNIT_Y = new Vector3f(0.0F, 1.0F, 0.0F);
/*   27 */   public static final Vector3f UNIT_Z = new Vector3f(0.0F, 0.0F, 1.0F);
/*   28 */   public static final Vector3f UNIT_XYZ = new Vector3f(1.0F, 1.0F, 1.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float x;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float y;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float z;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f() {
/*   51 */     this.x = this.y = this.z = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f(float x, float y, float z) {
/*   66 */     this.x = x;
/*   67 */     this.y = y;
/*   68 */     this.z = z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f(Vector3f copy) {
/*   79 */     set(copy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f set(float x, float y, float z) {
/*   95 */     this.x = x;
/*   96 */     this.y = y;
/*   97 */     this.z = z;
/*   98 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f set(Vector3f vect) {
/*  110 */     this.x = vect.x;
/*  111 */     this.y = vect.y;
/*  112 */     this.z = vect.z;
/*  113 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f add(Vector3f vec) {
/*  128 */     if (null == vec) {
/*      */       
/*  130 */       logger.warning("Provided vector is null, null returned.");
/*  131 */       return null;
/*      */     } 
/*  133 */     return new Vector3f(this.x + vec.x, this.y + vec.y, this.z + vec.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f add(Vector3f vec, Vector3f result) {
/*  148 */     this.x += vec.x;
/*  149 */     this.y += vec.y;
/*  150 */     this.z += vec.z;
/*  151 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f addLocal(Vector3f vec) {
/*  164 */     if (null == vec) {
/*      */       
/*  166 */       logger.warning("Provided vector is null, null returned.");
/*  167 */       return null;
/*      */     } 
/*  169 */     this.x += vec.x;
/*  170 */     this.y += vec.y;
/*  171 */     this.z += vec.z;
/*  172 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f add(float addX, float addY, float addZ) {
/*  189 */     return new Vector3f(this.x + addX, this.y + addY, this.z + addZ);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f addLocal(float addX, float addY, float addZ) {
/*  206 */     this.x += addX;
/*  207 */     this.y += addY;
/*  208 */     this.z += addZ;
/*  209 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scaleAdd(float scalar, Vector3f add) {
/*  223 */     this.x = this.x * scalar + add.x;
/*  224 */     this.y = this.y * scalar + add.y;
/*  225 */     this.z = this.z * scalar + add.z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scaleAdd(float scalar, Vector3f mult, Vector3f add) {
/*  241 */     this.x = mult.x * scalar + add.x;
/*  242 */     this.y = mult.y * scalar + add.y;
/*  243 */     this.z = mult.z * scalar + add.z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float dot(Vector3f vec) {
/*  257 */     if (null == vec) {
/*      */       
/*  259 */       logger.warning("Provided vector is null, 0 returned.");
/*  260 */       return 0.0F;
/*      */     } 
/*  262 */     return this.x * vec.x + this.y * vec.y + this.z * vec.z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f cross(Vector3f v) {
/*  277 */     return cross(v, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f cross(Vector3f v, Vector3f result) {
/*  295 */     return cross(v.x, v.y, v.z, result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f cross(float otherX, float otherY, float otherZ, Vector3f result) {
/*  314 */     if (result == null)
/*  315 */       result = new Vector3f(); 
/*  316 */     float resX = this.y * otherZ - this.z * otherY;
/*  317 */     float resY = this.z * otherX - this.x * otherZ;
/*  318 */     float resZ = this.x * otherY - this.y * otherX;
/*  319 */     result.set(resX, resY, resZ);
/*  320 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f crossLocal(Vector3f v) {
/*  332 */     return crossLocal(v.x, v.y, v.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f crossLocal(float otherX, float otherY, float otherZ) {
/*  348 */     float tempx = this.y * otherZ - this.z * otherY;
/*  349 */     float tempy = this.z * otherX - this.x * otherZ;
/*  350 */     this.z = this.x * otherY - this.y * otherX;
/*  351 */     this.x = tempx;
/*  352 */     this.y = tempy;
/*  353 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float length() {
/*  363 */     return FastMath.sqrt(lengthSquared());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float lengthSquared() {
/*  373 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float distanceSquared(Vector3f v) {
/*  385 */     double dx = (this.x - v.x);
/*  386 */     double dy = (this.y - v.y);
/*  387 */     double dz = (this.z - v.z);
/*  388 */     return (float)(dx * dx + dy * dy + dz * dz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float distance(Vector3f v) {
/*  400 */     return FastMath.sqrt(distanceSquared(v));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f mult(float scalar) {
/*  412 */     return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f mult(float scalar, Vector3f product) {
/*  428 */     if (null == product)
/*      */     {
/*  430 */       product = new Vector3f();
/*      */     }
/*      */     
/*  433 */     this.x *= scalar;
/*  434 */     this.y *= scalar;
/*  435 */     this.z *= scalar;
/*  436 */     return product;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f multLocal(float scalar) {
/*  449 */     this.x *= scalar;
/*  450 */     this.y *= scalar;
/*  451 */     this.z *= scalar;
/*  452 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f multLocal(Vector3f vec) {
/*  465 */     if (null == vec) {
/*      */       
/*  467 */       logger.warning("Provided vector is null, null returned.");
/*  468 */       return null;
/*      */     } 
/*  470 */     this.x *= vec.x;
/*  471 */     this.y *= vec.y;
/*  472 */     this.z *= vec.z;
/*  473 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f mult(Vector3f vec) {
/*  488 */     if (null == vec) {
/*      */       
/*  490 */       logger.warning("Provided vector is null, null returned.");
/*  491 */       return null;
/*      */     } 
/*  493 */     return mult(vec, (Vector3f)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f mult(Vector3f vec, Vector3f store) {
/*  514 */     if (null == vec) {
/*      */       
/*  516 */       logger.warning("Provided vector is null, null returned.");
/*  517 */       return null;
/*      */     } 
/*  519 */     if (store == null)
/*  520 */       store = new Vector3f(); 
/*  521 */     return store.set(this.x * vec.x, this.y * vec.y, this.z * vec.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f divide(float scalar) {
/*  534 */     scalar = 1.0F / scalar;
/*  535 */     return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f divideLocal(float scalar) {
/*  548 */     scalar = 1.0F / scalar;
/*  549 */     this.x *= scalar;
/*  550 */     this.y *= scalar;
/*  551 */     this.z *= scalar;
/*  552 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f divide(Vector3f scalar) {
/*  565 */     return new Vector3f(this.x / scalar.x, this.y / scalar.y, this.z / scalar.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f divideLocal(Vector3f scalar) {
/*  578 */     this.x /= scalar.x;
/*  579 */     this.y /= scalar.y;
/*  580 */     this.z /= scalar.z;
/*  581 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f negate() {
/*  592 */     return new Vector3f(-this.x, -this.y, -this.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f negateLocal() {
/*  603 */     this.x = -this.x;
/*  604 */     this.y = -this.y;
/*  605 */     this.z = -this.z;
/*  606 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f subtract(Vector3f vec) {
/*  620 */     return new Vector3f(this.x - vec.x, this.y - vec.y, this.z - vec.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f subtractLocal(Vector3f vec) {
/*  633 */     if (null == vec) {
/*      */       
/*  635 */       logger.warning("Provided vector is null, null returned.");
/*  636 */       return null;
/*      */     } 
/*  638 */     this.x -= vec.x;
/*  639 */     this.y -= vec.y;
/*  640 */     this.z -= vec.z;
/*  641 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f subtract(Vector3f vec, Vector3f result) {
/*  656 */     if (result == null)
/*      */     {
/*  658 */       result = new Vector3f();
/*      */     }
/*  660 */     this.x -= vec.x;
/*  661 */     this.y -= vec.y;
/*  662 */     this.z -= vec.z;
/*  663 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f subtract(float subtractX, float subtractY, float subtractZ) {
/*  680 */     return new Vector3f(this.x - subtractX, this.y - subtractY, this.z - subtractZ);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f subtractLocal(float subtractX, float subtractY, float subtractZ) {
/*  697 */     this.x -= subtractX;
/*  698 */     this.y -= subtractY;
/*  699 */     this.z -= subtractZ;
/*  700 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f normalize() {
/*  710 */     float length = length();
/*  711 */     if (length != 0.0F)
/*      */     {
/*  713 */       return divide(length);
/*      */     }
/*      */     
/*  716 */     return divide(1.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f normalizeLocal() {
/*  726 */     float length = length();
/*  727 */     if (length != 0.0F)
/*      */     {
/*  729 */       return divideLocal(length);
/*      */     }
/*      */     
/*  732 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void zero() {
/*  740 */     this.x = this.y = this.z = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float angleBetween(Vector3f otherVector) {
/*  753 */     float dotProduct = dot(otherVector);
/*  754 */     float angle = FastMath.acos(dotProduct);
/*  755 */     return angle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void interpolate(Vector3f finalVec, float changeAmnt) {
/*  769 */     this.x = (1.0F - changeAmnt) * this.x + changeAmnt * finalVec.x;
/*  770 */     this.y = (1.0F - changeAmnt) * this.y + changeAmnt * finalVec.y;
/*  771 */     this.z = (1.0F - changeAmnt) * this.z + changeAmnt * finalVec.z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void interpolate(Vector3f beginVec, Vector3f finalVec, float changeAmnt) {
/*  787 */     this.x = (1.0F - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
/*  788 */     this.y = (1.0F - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
/*  789 */     this.z = (1.0F - changeAmnt) * beginVec.z + changeAmnt * finalVec.z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isValidVector(Vector3f vector) {
/*  801 */     if (vector == null)
/*  802 */       return false; 
/*  803 */     if (Float.isNaN(vector.x) || Float.isNaN(vector.y) || Float.isNaN(vector.z))
/*  804 */       return false; 
/*  805 */     if (Float.isInfinite(vector.x) || Float.isInfinite(vector.y) || Float.isInfinite(vector.z))
/*  806 */       return false; 
/*  807 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void generateOrthonormalBasis(Vector3f u, Vector3f v, Vector3f w) {
/*  812 */     w.normalizeLocal();
/*  813 */     generateComplementBasis(u, v, w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void generateComplementBasis(Vector3f u, Vector3f v, Vector3f w) {
/*  820 */     if (FastMath.abs(w.x) >= FastMath.abs(w.y)) {
/*      */ 
/*      */       
/*  823 */       float fInvLength = FastMath.invSqrt(w.x * w.x + w.z * w.z);
/*  824 */       u.x = -w.z * fInvLength;
/*  825 */       u.y = 0.0F;
/*  826 */       u.z = w.x * fInvLength;
/*  827 */       v.x = w.y * u.z;
/*  828 */       v.y = w.z * u.x - w.x * u.z;
/*  829 */       v.z = -w.y * u.x;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  834 */       float fInvLength = FastMath.invSqrt(w.y * w.y + w.z * w.z);
/*  835 */       u.x = 0.0F;
/*  836 */       u.y = w.z * fInvLength;
/*  837 */       u.z = -w.y * fInvLength;
/*  838 */       v.x = w.y * u.z - w.z * u.y;
/*  839 */       v.y = -w.x * u.z;
/*  840 */       v.z = w.x * u.y;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3f clone() {
/*      */     try {
/*  849 */       return (Vector3f)super.clone();
/*      */     }
/*  851 */     catch (CloneNotSupportedException e) {
/*      */       
/*  853 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] toArray(float[] floats) {
/*  866 */     if (floats == null)
/*      */     {
/*  868 */       floats = new float[3];
/*      */     }
/*  870 */     floats[0] = this.x;
/*  871 */     floats[1] = this.y;
/*  872 */     floats[2] = this.z;
/*  873 */     return floats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  886 */     if (!(o instanceof Vector3f))
/*      */     {
/*  888 */       return false;
/*      */     }
/*      */     
/*  891 */     if (this == o)
/*      */     {
/*  893 */       return true;
/*      */     }
/*      */     
/*  896 */     Vector3f comp = (Vector3f)o;
/*  897 */     if (Float.compare(this.x, comp.x) != 0)
/*  898 */       return false; 
/*  899 */     if (Float.compare(this.y, comp.y) != 0)
/*  900 */       return false; 
/*  901 */     if (Float.compare(this.z, comp.z) != 0)
/*  902 */       return false; 
/*  903 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  915 */     int hash = 37;
/*  916 */     hash += 37 * hash + Float.floatToIntBits(this.x);
/*  917 */     hash += 37 * hash + Float.floatToIntBits(this.y);
/*  918 */     hash += 37 * hash + Float.floatToIntBits(this.z);
/*  919 */     return hash;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  934 */     return "(" + this.x + ", " + this.y + ", " + this.z + ')';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/*  949 */     this.x = in.readFloat();
/*  950 */     this.y = in.readFloat();
/*  951 */     this.z = in.readFloat();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeExternal(ObjectOutput out) throws IOException {
/*  965 */     out.writeFloat(this.x);
/*  966 */     out.writeFloat(this.y);
/*  967 */     out.writeFloat(this.z);
/*      */   }
/*      */ 
/*      */   
/*      */   public Class<? extends Vector3f> getClassTag() {
/*  972 */     return (Class)getClass();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getX() {
/*  977 */     return this.x;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setX(float x) {
/*  982 */     this.x = x;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getY() {
/*  987 */     return this.y;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setY(float y) {
/*  992 */     this.y = y;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getZ() {
/*  997 */     return this.z;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setZ(float z) {
/* 1002 */     this.z = z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float get(int index) {
/* 1013 */     switch (index) {
/*      */       
/*      */       case 0:
/* 1016 */         return this.x;
/*      */       case 1:
/* 1018 */         return this.y;
/*      */       case 2:
/* 1020 */         return this.z;
/*      */     } 
/* 1022 */     throw new IllegalArgumentException("index must be either 0, 1 or 2");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void set(int index, float value) {
/* 1036 */     switch (index) {
/*      */       
/*      */       case 0:
/* 1039 */         this.x = value;
/*      */         return;
/*      */       case 1:
/* 1042 */         this.y = value;
/*      */         return;
/*      */       case 2:
/* 1045 */         this.z = value;
/*      */         return;
/*      */     } 
/* 1048 */     throw new IllegalArgumentException("index must be either 0, 1 or 2");
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Vector3f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */