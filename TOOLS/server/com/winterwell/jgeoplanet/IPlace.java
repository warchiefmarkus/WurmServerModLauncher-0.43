package com.winterwell.jgeoplanet;

public interface IPlace {
  public static final String TYPE_CITY = "city";
  
  public static final String TYPE_COUNTRY = "country";
  
  String getName();
  
  String getCountryName();
  
  IPlace getParent();
  
  Location getCentroid();
  
  BoundingBox getBoundingBox();
  
  String getType();
  
  String getUID();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\winterwell\jgeoplanet\IPlace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */