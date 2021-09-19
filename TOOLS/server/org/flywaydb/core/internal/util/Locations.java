/*    */ package org.flywaydb.core.internal.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Locations
/*    */ {
/* 29 */   private static final Log LOG = LogFactory.getLog(Locations.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   private final List<Location> locations = new ArrayList<Location>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Locations(String... rawLocations) {
/* 42 */     List<Location> normalizedLocations = new ArrayList<Location>();
/* 43 */     for (String rawLocation : rawLocations) {
/* 44 */       normalizedLocations.add(new Location(rawLocation));
/*    */     }
/* 46 */     Collections.sort(normalizedLocations);
/*    */     
/* 48 */     for (Location normalizedLocation : normalizedLocations) {
/* 49 */       if (this.locations.contains(normalizedLocation)) {
/* 50 */         LOG.warn("Discarding duplicate location '" + normalizedLocation + "'");
/*    */         
/*    */         continue;
/*    */       } 
/* 54 */       Location parentLocation = getParentLocationIfExists(normalizedLocation, this.locations);
/* 55 */       if (parentLocation != null) {
/* 56 */         LOG.warn("Discarding location '" + normalizedLocation + "' as it is a sublocation of '" + parentLocation + "'");
/*    */         
/*    */         continue;
/*    */       } 
/* 60 */       this.locations.add(normalizedLocation);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Location> getLocations() {
/* 68 */     return this.locations;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Location getParentLocationIfExists(Location location, List<Location> finalLocations) {
/* 79 */     for (Location finalLocation : finalLocations) {
/* 80 */       if (finalLocation.isParentOf(location)) {
/* 81 */         return finalLocation;
/*    */       }
/*    */     } 
/* 84 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\Locations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */