/*     */ package com.winterwell.jgeoplanet;
/*     */ 
/*     */ import winterwell.json.JSONException;
/*     */ import winterwell.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoundingBox
/*     */ {
/*     */   final Location northEast;
/*     */   final Location southWest;
/*     */   
/*     */   public BoundingBox(Location northEast, Location southWest) {
/*  21 */     if (northEast.latitude < southWest.latitude) {
/*  22 */       throw new IllegalArgumentException("North east corner is south of south west corner");
/*     */     }
/*  24 */     this.northEast = northEast;
/*  25 */     this.southWest = southWest;
/*     */   }
/*     */   
/*     */   public Location getCenter() {
/*  29 */     Location ne = this.northEast;
/*  30 */     Location sw = this.southWest;
/*     */ 
/*     */ 
/*     */     
/*  34 */     double tempLat = (ne.latitude + sw.latitude) / 2.0D;
/*  35 */     if (Math.abs(ne.latitude - sw.latitude) > 90.0D)
/*  36 */       if (tempLat <= 0.0D) { tempLat += 90.0D; }
/*  37 */       else { tempLat -= 90.0D; }
/*     */        
/*  39 */     double tempLong = (ne.longitude + sw.longitude) / 2.0D;
/*     */ 
/*     */     
/*  42 */     if (Math.abs(ne.longitude - sw.longitude) > 180.0D)
/*  43 */       if (tempLong <= 0.0D) { tempLong += 180.0D; }
/*  44 */       else { tempLong -= 180.0D; }
/*     */        
/*  46 */     Location tempCentroid = new Location(tempLat, tempLong);
/*  47 */     return tempCentroid;
/*     */   }
/*     */ 
/*     */   
/*     */   BoundingBox(JSONObject bbox) throws JSONException {
/*  52 */     this(getLocation(bbox.getJSONObject("northEast")), getLocation(bbox.getJSONObject("southWest")));
/*     */   }
/*     */   
/*     */   public BoundingBox(Location centre, Dx radius) {
/*  56 */     double r = radius.getMetres();
/*  57 */     this.northEast = centre.move(r, r);
/*  58 */     this.southWest = centre.move(-r, -r);
/*     */   }
/*     */   
/*     */   public Location getNorthEast() {
/*  62 */     return this.northEast;
/*     */   }
/*     */   
/*     */   public Location getSouthWest() {
/*  66 */     return this.southWest;
/*     */   }
/*     */   
/*     */   public Location getNorthWest() {
/*  70 */     return new Location(this.northEast.latitude, this.southWest.longitude);
/*     */   }
/*     */   
/*     */   public Location getSouthEast() {
/*  74 */     return new Location(this.southWest.latitude, this.northEast.longitude);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Location location) {
/*  84 */     if (location.latitude > this.northEast.latitude) return false; 
/*  85 */     if (location.latitude < this.southWest.latitude) return false; 
/*  86 */     if (this.northEast.longitude < 0.0D && this.southWest.longitude >= 0.0D && this.southWest.longitude > this.northEast.longitude) {
/*  87 */       if (location.longitude < 0.0D && location.longitude > this.northEast.longitude) return false; 
/*  88 */       if (location.longitude >= 0.0D && location.longitude < this.southWest.longitude) return false;
/*     */     
/*     */     } else {
/*  91 */       if (location.longitude > this.northEast.longitude) return false; 
/*  92 */       if (location.longitude < this.southWest.longitude) return false; 
/*     */     } 
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(BoundingBox other) {
/* 104 */     return (contains(other.southWest) && contains(other.northEast));
/*     */   }
/*     */   
/*     */   public boolean intersects(BoundingBox other) {
/* 108 */     return !(!contains(other.northEast) && 
/* 109 */       !contains(other.southWest) && 
/* 110 */       !contains(other.getNorthWest()) && 
/* 111 */       !contains(other.getSouthEast()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     int prime = 31;
/* 117 */     int result = 1;
/* 118 */     result = 31 * result + (
/* 119 */       (this.northEast == null) ? 0 : this.northEast.hashCode());
/* 120 */     result = 31 * result + (
/* 121 */       (this.southWest == null) ? 0 : this.southWest.hashCode());
/* 122 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 127 */     if (this == obj)
/* 128 */       return true; 
/* 129 */     if (obj == null)
/* 130 */       return false; 
/* 131 */     if (getClass() != obj.getClass())
/* 132 */       return false; 
/* 133 */     BoundingBox other = (BoundingBox)obj;
/* 134 */     if (this.northEast == null) {
/* 135 */       if (other.northEast != null)
/* 136 */         return false; 
/* 137 */     } else if (!this.northEast.equals(other.northEast)) {
/* 138 */       return false;
/* 139 */     }  if (this.southWest == null) {
/* 140 */       if (other.southWest != null)
/* 141 */         return false; 
/* 142 */     } else if (!this.southWest.equals(other.southWest)) {
/* 143 */       return false;
/* 144 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     return "BoundingBox [northEast=" + this.northEast + ", southWest=" + 
/* 150 */       this.southWest + "]";
/*     */   }
/*     */   
/*     */   static Location getLocation(JSONObject jo) throws JSONException {
/* 154 */     return new Location(
/* 155 */         jo.getDouble("latitude"), jo.getDouble("longitude"));
/*     */   }
/*     */   
/*     */   public boolean isPoint() {
/* 159 */     return this.northEast.equals(this.southWest);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\winterwell\jgeoplanet\BoundingBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */