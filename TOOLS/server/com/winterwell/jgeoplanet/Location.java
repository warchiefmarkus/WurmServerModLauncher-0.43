/*     */ package com.winterwell.jgeoplanet;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Location
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final double DIAMETER_OF_EARTH = 1.27562E7D;
/*     */   public final double longitude;
/*     */   public final double latitude;
/*     */   
/*     */   public Location(double latitude, double longitude) throws IllegalArgumentException {
/*  34 */     if (latitude < -90.0D || latitude > 90.0D) {
/*  35 */       throw new IllegalArgumentException("Invalid latitude: " + latitude + ", " + longitude);
/*     */     }
/*  37 */     if (longitude < -180.0D || longitude > 180.0D) {
/*  38 */       longitude %= 360.0D;
/*  39 */       if (longitude > 180.0D) { longitude = 360.0D - longitude; }
/*  40 */       else if (longitude < -180.0D) { longitude += 360.0D; }
/*  41 */        assert longitude >= -180.0D || longitude <= 180.0D : longitude;
/*     */     } 
/*  43 */     this.latitude = latitude;
/*  44 */     this.longitude = longitude;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLatitude() {
/*  52 */     return this.latitude;
/*     */   }
/*     */   
/*     */   public double[] getLatLong() {
/*  56 */     return new double[] { this.latitude, this.longitude };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLongitude() {
/*  65 */     return this.longitude;
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
/*     */   public Dx distance(Location other) {
/*  78 */     double lat = this.latitude * Math.PI / 180.0D;
/*  79 */     double lon = this.longitude * Math.PI / 180.0D;
/*  80 */     double olat = other.latitude * Math.PI / 180.0D;
/*  81 */     double olon = other.longitude * Math.PI / 180.0D;
/*     */     
/*  83 */     double sin2lat = Math.sin((lat - olat) / 2.0D);
/*  84 */     sin2lat *= sin2lat;
/*  85 */     double sin2long = Math.sin((lon - olon) / 2.0D);
/*  86 */     sin2long *= sin2long;
/*  87 */     double m = 1.27562E7D * Math.asin(
/*  88 */         Math.sqrt(sin2lat + Math.cos(lat) * Math.cos(olat) * sin2long));
/*  89 */     return new Dx(m, LengthUnit.METRE);
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
/*     */   public Location move(double metresNorth, double metresEast) {
/* 103 */     double fracNorth = metresNorth / 2.003739210386106E10D;
/* 104 */     double lat = this.latitude + fracNorth * 180.0D;
/*     */ 
/*     */     
/* 107 */     if (lat > 90.0D) { lat = 90.0D; }
/* 108 */     else if (lat < -90.0D) { lat = -90.0D; }
/*     */ 
/*     */ 
/*     */     
/* 112 */     double lng = this.longitude + metresEast;
/*     */     
/* 114 */     for (; lng > 180.0D; lng -= 360.0D);
/* 115 */     for (; lng < -180.0D; lng += 360.0D);
/* 116 */     return new Location(lat, lng);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     int prime = 31;
/* 123 */     int result = 1;
/*     */     
/* 125 */     long temp = Double.doubleToLongBits(this.latitude);
/* 126 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 127 */     temp = Double.doubleToLongBits(this.longitude);
/* 128 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 135 */     if (this == obj) {
/* 136 */       return true;
/*     */     }
/* 138 */     if (obj == null) {
/* 139 */       return false;
/*     */     }
/* 141 */     if (getClass() != obj.getClass()) {
/* 142 */       return false;
/*     */     }
/* 144 */     Location other = (Location)obj;
/* 145 */     if (Double.doubleToLongBits(this.latitude) != 
/* 146 */       Double.doubleToLongBits(other.latitude)) {
/* 147 */       return false;
/*     */     }
/* 149 */     if (Double.doubleToLongBits(this.longitude) != 
/* 150 */       Double.doubleToLongBits(other.longitude)) {
/* 151 */       return false;
/*     */     }
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return "(" + this.latitude + " N, " + this.longitude + " E)";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSimpleCoords() {
/* 166 */     return String.valueOf(this.latitude) + "," + this.longitude;
/*     */   }
/*     */   
/* 169 */   public static final Pattern latLongLocn = Pattern.compile(
/* 170 */       "\\s*(-?[\\d\\.]+),\\s*(-?[\\d\\.]+)\\s*");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Location parse(String locnDesc) {
/* 179 */     Matcher m = latLongLocn.matcher(locnDesc);
/* 180 */     if (!m.matches()) return null; 
/* 181 */     String lat = m.group(1);
/* 182 */     String lng = m.group(2);
/* 183 */     double _lat = Double.valueOf(lat).doubleValue();
/* 184 */     if (Math.abs(_lat) > 90.0D)
/*     */     {
/*     */       
/* 187 */       return null;
/*     */     }
/* 189 */     return new Location(_lat, Double.valueOf(lng).doubleValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\winterwell\jgeoplanet\Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */