/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Tracks
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*     */   private static final long decayTimeRock = 1800000L;
/*     */   private static final long decayTimeDirt = 10800000L;
/*     */   private static final long decayTimeGrass = 3600000L;
/*     */   private static final long decayTimeClay = 86400000L;
/*     */   private static final long decayTimeBush = 7200000L;
/*     */   private static final long decayTimeTree = 3600000L;
/*     */   private static final long decayTimeField = 10800000L;
/*     */   private static final long decayTimeSand = 10800000L;
/*     */   private static final int MAX_TRACKS = 1000;
/*  56 */   private final ConcurrentLinkedDeque<Track> tracks = new ConcurrentLinkedDeque<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void addTrack(Track track) {
/*  69 */     if (this.tracks.size() > 1000)
/*  70 */       this.tracks.removeFirst(); 
/*  71 */     this.tracks.addLast(track);
/*     */   }
/*     */ 
/*     */   
/*     */   final void decay() {
/*  76 */     for (Iterator<Track> it = this.tracks.iterator(); it.hasNext(); ) {
/*     */       
/*  78 */       Track track = it.next();
/*  79 */       long decayTime = 1800000L;
/*  80 */       byte type = Tiles.decodeType(track.getTile());
/*  81 */       Tiles.Tile theTile = Tiles.getTile(type);
/*  82 */       if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_STEPPE.id) {
/*  83 */         decayTime = 3600000L;
/*  84 */       } else if (type == Tiles.Tile.TILE_KELP.id) {
/*  85 */         decayTime = 3600000L;
/*  86 */       } else if (type == Tiles.Tile.TILE_REED.id) {
/*  87 */         decayTime = 3600000L;
/*  88 */       } else if (type == Tiles.Tile.TILE_LAWN.id) {
/*  89 */         decayTime = 3600000L;
/*  90 */       } else if (theTile.isTree()) {
/*  91 */         decayTime = 3600000L;
/*  92 */       } else if (theTile.isBush()) {
/*  93 */         decayTime = 7200000L;
/*  94 */       } else if (type == Tiles.Tile.TILE_DIRT.id) {
/*  95 */         decayTime = 10800000L;
/*  96 */       } else if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) {
/*  97 */         decayTime = 10800000L;
/*  98 */       } else if (type == Tiles.Tile.TILE_ROCK.id) {
/*  99 */         decayTime = 1800000L;
/* 100 */       } else if (type == Tiles.Tile.TILE_SAND.id) {
/* 101 */         decayTime = 10800000L;
/* 102 */       } else if (type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_PEAT.id) {
/* 103 */         decayTime = 86400000L;
/* 104 */       }  if (System.currentTimeMillis() - track.getTime() > decayTime) {
/* 105 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final Track[] getTracksFor(int tilex, int tiley) {
/* 112 */     Set<Track> matches = new HashSet<>();
/*     */     
/* 114 */     for (Track track : this.tracks) {
/*     */       
/* 116 */       if (track.getTileX() == tilex && track.getTileY() == tiley)
/* 117 */         matches.add(track); 
/*     */     } 
/* 119 */     Track[] toReturn = new Track[matches.size()];
/* 120 */     return matches.<Track>toArray(toReturn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final Track[] getTracksFor(int tilex, int tiley, int dist) {
/* 126 */     Map<String, Track> matches = new HashMap<>();
/* 127 */     boolean add = false;
/* 128 */     int tx = tilex;
/* 129 */     int ty = tiley;
/* 130 */     int dir = 0;
/* 131 */     int maxy = tiley + dist;
/* 132 */     int maxx = tilex + dist;
/* 133 */     int minx = tilex - dist;
/* 134 */     int miny = tiley - dist;
/* 135 */     for (Track track : this.tracks) {
/*     */       
/* 137 */       tx = track.getTileX();
/* 138 */       ty = track.getTileY();
/* 139 */       dir = track.getDirection();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       if (tx == minx && ty == miny) {
/*     */         
/* 146 */         if (dir >= 5 || dir <= 1) {
/* 147 */           add = true;
/*     */         }
/* 149 */       } else if (tx == maxx && ty == miny) {
/*     */         
/* 151 */         if (dir >= 7 || dir <= 3) {
/* 152 */           add = true;
/*     */         }
/* 154 */       } else if (tx == maxx && ty == maxy) {
/*     */         
/* 156 */         if (dir >= 1 && dir <= 5) {
/* 157 */           add = true;
/*     */         }
/* 159 */       } else if (tx == minx && ty == maxy) {
/*     */         
/* 161 */         if (dir >= 3 && dir <= 7) {
/* 162 */           add = true;
/*     */         }
/* 164 */       } else if (ty == miny && tx > minx && tx < maxx) {
/*     */         
/* 166 */         if (dir >= 7 || dir <= 1) {
/* 167 */           add = true;
/*     */         }
/* 169 */       } else if (tx == maxx && ty > miny && ty < maxy) {
/*     */         
/* 171 */         if (dir >= 1 && dir <= 3) {
/* 172 */           add = true;
/*     */         }
/* 174 */       } else if (ty == miny && tx > minx && tx < maxx) {
/*     */         
/* 176 */         if (dir >= 3 && dir <= 5) {
/* 177 */           add = true;
/*     */         }
/* 179 */       } else if (tx == minx && ty > miny && ty < maxy) {
/*     */         
/* 181 */         if (dir >= 5 && dir <= 7) {
/* 182 */           add = true;
/*     */         }
/*     */       } 
/* 185 */       if (add) {
/*     */ 
/*     */         
/* 188 */         Track t = matches.get(track.getCreatureName());
/* 189 */         if (t != null) {
/*     */           
/* 191 */           if (t.getTime() > track.getTime()) {
/* 192 */             matches.put(track.getCreatureName(), track);
/*     */           }
/*     */         } else {
/* 195 */           matches.put(track.getCreatureName(), track);
/*     */         } 
/* 197 */       }  add = false;
/*     */     } 
/* 199 */     Track[] toReturn = new Track[matches.size()];
/* 200 */     return (Track[])matches.values().toArray((Object[])toReturn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getNumberOfTracks() {
/* 210 */     if (this.tracks != null)
/*     */     {
/* 212 */       return this.tracks.size();
/*     */     }
/*     */ 
/*     */     
/* 216 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Tracks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */