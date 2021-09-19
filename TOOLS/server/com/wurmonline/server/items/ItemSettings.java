/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.MineDoorSettings;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsByPlayer;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ItemSettings
/*      */   implements MiscConstants
/*      */ {
/*      */   public enum GMItemPermissions
/*      */     implements Permissions.IPermission
/*      */   {
/*   51 */     MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*   52 */     COMMANDER(1, "Commander", "Can", "Command", "Allows commanding of this vehicle."),
/*   53 */     PASSENGER(2, "Passenger", "Can be", "Passenger", "Allows being a passenger of this vehicle."),
/*   54 */     ACCESS_HOLD(3, "Access Hold", "Access", "Hold", "Allows acces to the hold."),
/*   55 */     MAY_USE_BED(4, "Can Sleep", "Can", "Sleep", ""),
/*   56 */     FREE_SLEEP(5, "Free Use", "Free", "Use", ""),
/*   57 */     DRAG(6, "Drag", "May", "Drag", "Allows the Vehicle to be dragged."),
/*   58 */     MAY_POST_NOTICES(7, "Notices", "May Post", "Notices", "Allows notices to be posted."),
/*   59 */     MAY_ADD_PMS(8, "PMs", "May Add", "PMs", "Allows PMs to be added."),
/*   60 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     GMItemPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  147 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  152 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() {
/*      */       return this.header2;
/*      */     } public String getHover() {
/*      */       return this.hover;
/*      */     } static {
/*      */     
/*      */     } }
/*  159 */   public enum VehiclePermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  160 */     COMMANDER(1, "Commander", "Can", "Command", "Allows commanding of this vehicle."),
/*  161 */     PASSENGER(2, "Passenger", "Can be", "Passenger", "Allows being a passenger of this vehicle."),
/*  162 */     ACCESS_HOLD(3, "Access Hold", "Access", "Hold", "Allows acces to the hold."),
/*      */ 
/*      */     
/*  165 */     DRAG(6, "Drag", "May", "Drag", "Allows the Vehicle to be dragged."),
/*  166 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     VehiclePermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  253 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  258 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*      */       return this.hover;
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*  264 */   public enum SmallCartPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*      */ 
/*      */     
/*  267 */     ACCESS_HOLD(3, "Access Hold", "Access", "Hold", "Allows acces to the hold."),
/*      */ 
/*      */     
/*  270 */     DRAG(6, "Drag", "May", "Drag", "Allows the Vehicle to be dragged."),
/*  271 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SmallCartPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  358 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  363 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() {
/*      */       return this.header2;
/*      */     } public String getHover() {
/*      */       return this.hover;
/*      */     } static {
/*      */     
/*      */     } }
/*  370 */   public enum WagonPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  371 */     COMMANDER(1, "Commander", "Can", "Command", "Allows commanding of this vehicle."),
/*  372 */     PASSENGER(2, "Passenger", "Can be", "Passenger", "Allows being a passenger of this vehicle."),
/*  373 */     ACCESS_HOLD(3, "Access Hold", "Access", "Hold", "Allows acces to the hold."),
/*      */ 
/*      */ 
/*      */     
/*  377 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WagonPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  464 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  469 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*      */       return this.hover;
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*  475 */   public enum ShipTransporterPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  476 */     COMMANDER(1, "Commander", "Can", "Command", "Allows commanding of this vehicle."),
/*      */     
/*  478 */     ACCESS_HOLD(3, "Access Hold", "Access", "Hold", "Allows acces to the hold."),
/*      */ 
/*      */     
/*  481 */     DRAG(6, "Drag", "May", "Drag", "Allows the Vehicle to be dragged."),
/*  482 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ShipTransporterPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  569 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  574 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*      */       return this.hover;
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*  580 */   public enum CreatureTransporterPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  581 */     COMMANDER(1, "Commander", "Can", "Command", "Allows commanding of this vehicle."),
/*  582 */     PASSENGER(2, "Passenger", "Can be", "Passenger", "Allows being a passenger of this vehicle."),
/*  583 */     ACCESS_HOLD(3, "Access Hold", "Access", "Hold", "Allows acces to the hold."),
/*      */ 
/*      */     
/*  586 */     DRAG(6, "Drag", "May", "Drag", "Allows the Vehicle to be dragged."),
/*  587 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CreatureTransporterPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  674 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])ItemSettings.ShipTransporterPermissions.values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  679 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*      */       return this.hover;
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*  685 */   public enum ItemPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*      */ 
/*      */     
/*  688 */     ACCESS_HOLD(3, "Access Item", "May", "Open", "Allows acces to this container."),
/*      */ 
/*      */ 
/*      */     
/*  692 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ItemPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  779 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  784 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*      */       return this.hover;
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*  790 */   public enum BedPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*      */ 
/*      */ 
/*      */     
/*  794 */     MAY_USE_BED(4, "May Use Bed", "May Use", "Bed", "Allows acess to use this bed."),
/*  795 */     FREE_SLEEP(5, "Free Sleep", "Free", "Sleep", "Allows this bed to be used for free (requires 'May Use Bed' as well)."),
/*      */     
/*  797 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     BedPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  884 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  889 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*      */       return this.hover;
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*  895 */   public enum MessageBoardPermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  896 */     MANAGE_NOTICES(1, "Manage Notices", "Manage", "Notices", "Allows managing of any notices."),
/*      */     
/*  898 */     ACCESS_HOLD(3, "Access Item", "May View", "Messages", "Allows viewing of mesages on this board."),
/*      */ 
/*      */ 
/*      */     
/*  902 */     MAY_POST_NOTICES(7, "Notices", "May Post", "Notices", "Allows notices to be posted."),
/*  903 */     MAY_ADD_PMS(8, "PMs", "May Add", "PMs", "Allows PMs to be added."),
/*  904 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MessageBoardPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  991 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])ItemSettings.ItemPermissions.values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  996 */       return types; } public String getHeader1() { return this.header1; } public String getHeader2() {
/*      */       return this.header2;
/*      */     } public String getHover() {
/*      */       return this.hover;
/*      */     } static {
/*      */     
/*      */     } }
/* 1003 */   public enum CorpsePermissions implements Permissions.IPermission { COMMANDER(1, "Commander", "Can", "Access", "Allows looting of this corpse."),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String hover;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CorpsePermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = aHeader1;
/*      */       this.header2 = aHeader2;
/*      */       this.hover = aHover;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getBit() {
/*      */       return this.bit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1096 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])ItemSettings.ItemPermissions.values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/* 1101 */       return types; } public String getHeader1() { return this.header1; }
/*      */     public String getHeader2() { return this.header2; }
/*      */     public String getHover() { return this.hover; }
/*      */     static {
/*      */     
/* 1106 */     } } private static final Logger logger = Logger.getLogger(ItemSettings.class.getName());
/*      */   private static final String GET_ALL_SETTINGS = "SELECT * FROM ITEMSETTINGS";
/*      */   private static final String ADD_PLAYER = "INSERT INTO ITEMSETTINGS (SETTINGS,WURMID,PLAYERID) VALUES(?,?,?)";
/*      */   private static final String DELETE_SETTINGS = "DELETE FROM ITEMSETTINGS WHERE WURMID=?";
/*      */   private static final String REMOVE_PLAYER = "DELETE FROM ITEMSETTINGS WHERE WURMID=? AND PLAYERID=?";
/*      */   private static final String UPDATE_PLAYER = "UPDATE ITEMSETTINGS SET SETTINGS=? WHERE WURMID=? AND PLAYERID=?";
/* 1112 */   private static int MAX_PLAYERS_PER_OBJECT = 1000;
/* 1113 */   private static Map<Long, PermissionsPlayerList> objectSettings = new ConcurrentHashMap<>();
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
/*      */   public static void loadAll() throws IOException {
/* 1127 */     logger.log(Level.INFO, "Loading all item settings.");
/* 1128 */     long start = System.nanoTime();
/* 1129 */     long count = 0L;
/* 1130 */     Connection dbcon = null;
/* 1131 */     PreparedStatement ps = null;
/* 1132 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1135 */       dbcon = DbConnector.getItemDbCon();
/* 1136 */       ps = dbcon.prepareStatement("SELECT * FROM ITEMSETTINGS");
/* 1137 */       rs = ps.executeQuery();
/* 1138 */       while (rs.next())
/*      */       {
/* 1140 */         long wurmId = rs.getLong("WURMID");
/* 1141 */         long playerId = rs.getLong("PLAYERID");
/* 1142 */         int settings = rs.getInt("SETTINGS");
/* 1143 */         add(wurmId, playerId, settings);
/* 1144 */         count++;
/*      */       }
/*      */     
/* 1147 */     } catch (SQLException ex) {
/*      */       
/* 1149 */       logger.log(Level.WARNING, "Failed to load settings for items.", ex);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1154 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1155 */       DbConnector.returnConnection(dbcon);
/* 1156 */       long end = System.nanoTime();
/* 1157 */       logger.log(Level.INFO, "Loaded " + count + " item settings. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getMaxAllowed() {
/* 1168 */     return Servers.isThisATestServer() ? 10 : MAX_PLAYERS_PER_OBJECT;
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
/*      */   private static PermissionsByPlayer add(long wurmId, long playerId, int settings) {
/* 1180 */     Long id = Long.valueOf(wurmId);
/* 1181 */     if (objectSettings.containsKey(id)) {
/*      */ 
/*      */       
/* 1184 */       PermissionsPlayerList permissionsPlayerList = objectSettings.get(id);
/* 1185 */       return permissionsPlayerList.add(playerId, settings);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1190 */     PermissionsPlayerList ppl = new PermissionsPlayerList();
/* 1191 */     objectSettings.put(id, ppl);
/* 1192 */     return ppl.add(playerId, settings);
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
/*      */   public static void addPlayer(long wurmId, long playerId, int settings) {
/* 1205 */     PermissionsByPlayer pbp = add(wurmId, playerId, settings);
/* 1206 */     if (pbp == null) {
/* 1207 */       dbAddPlayer(wurmId, playerId, settings, true);
/* 1208 */     } else if (pbp.getSettings() != settings) {
/* 1209 */       dbAddPlayer(wurmId, playerId, settings, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removePlayer(long wurmId, long playerId) {
/* 1220 */     Long id = Long.valueOf(wurmId);
/* 1221 */     if (objectSettings.containsKey(id)) {
/*      */       
/* 1223 */       PermissionsPlayerList ppl = objectSettings.get(id);
/* 1224 */       ppl.remove(playerId);
/* 1225 */       dbRemovePlayer(wurmId, playerId);
/*      */       
/* 1227 */       if (ppl.isEmpty()) {
/* 1228 */         objectSettings.remove(id);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1233 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for item " + wurmId + ".");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void dbAddPlayer(long wurmId, long playerId, int settings, boolean add) {
/* 1239 */     Connection dbcon = null;
/* 1240 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1243 */       dbcon = DbConnector.getItemDbCon();
/* 1244 */       if (add) {
/* 1245 */         ps = dbcon.prepareStatement("INSERT INTO ITEMSETTINGS (SETTINGS,WURMID,PLAYERID) VALUES(?,?,?)");
/*      */       } else {
/* 1247 */         ps = dbcon.prepareStatement("UPDATE ITEMSETTINGS SET SETTINGS=? WHERE WURMID=? AND PLAYERID=?");
/*      */       } 
/* 1249 */       ps.setInt(1, settings);
/* 1250 */       ps.setLong(2, wurmId);
/* 1251 */       ps.setLong(3, playerId);
/* 1252 */       ps.executeUpdate();
/*      */     }
/* 1254 */     catch (SQLException ex) {
/*      */       
/* 1256 */       logger.log(Level.WARNING, "Failed to " + (add ? "add" : "update") + " player (" + playerId + ") for item with id " + wurmId, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1260 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1261 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void dbRemovePlayer(long wurmId, long playerId) {
/* 1267 */     Connection dbcon = null;
/* 1268 */     PreparedStatement ps = null;
/* 1269 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1272 */       dbcon = DbConnector.getItemDbCon();
/* 1273 */       ps = dbcon.prepareStatement("DELETE FROM ITEMSETTINGS WHERE WURMID=? AND PLAYERID=?");
/* 1274 */       ps.setLong(1, wurmId);
/* 1275 */       ps.setLong(2, playerId);
/* 1276 */       ps.executeUpdate();
/*      */     }
/* 1278 */     catch (SQLException ex) {
/*      */       
/* 1280 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for item " + wurmId + ".", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1284 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1285 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean exists(long wurmId) {
/* 1296 */     Long id = Long.valueOf(wurmId);
/* 1297 */     return objectSettings.containsKey(id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void remove(long wurmId) {
/* 1307 */     Long id = Long.valueOf(wurmId);
/* 1308 */     if (objectSettings.containsKey(id)) {
/*      */       
/* 1310 */       dbRemove(wurmId);
/* 1311 */       objectSettings.remove(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemove(long wurmId) {
/* 1321 */     Connection dbcon = null;
/* 1322 */     PreparedStatement ps = null;
/* 1323 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1326 */       dbcon = DbConnector.getItemDbCon();
/* 1327 */       ps = dbcon.prepareStatement("DELETE FROM ITEMSETTINGS WHERE WURMID=?");
/* 1328 */       ps.setLong(1, wurmId);
/* 1329 */       ps.executeUpdate();
/*      */     }
/* 1331 */     catch (SQLException ex) {
/*      */       
/* 1333 */       logger.log(Level.WARNING, "Failed to delete settings for item " + wurmId + ".", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1337 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1338 */       DbConnector.returnConnection(dbcon);
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
/*      */   public static PermissionsPlayerList getPermissionsPlayerList(long wurmId) {
/* 1350 */     Long id = Long.valueOf(wurmId);
/* 1351 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 1352 */     if (ppl == null)
/* 1353 */       return new PermissionsPlayerList(); 
/* 1354 */     return ppl;
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
/*      */   private static boolean hasPermission(PermissionsPlayerList.ISettings is, Creature creature, int bit) {
/* 1367 */     if (is.isOwner(creature)) {
/* 1368 */       return (bit != MineDoorSettings.MinedoorPermissions.EXCLUDE.getBit());
/*      */     }
/* 1370 */     Long id = Long.valueOf(is.getWurmId());
/* 1371 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 1372 */     if (ppl == null)
/*      */     {
/* 1374 */       return false;
/*      */     }
/* 1376 */     if (ppl.exists(creature.getWurmId())) {
/* 1377 */       return ppl.getPermissionsFor(creature.getWurmId()).hasPermission(bit);
/*      */     }
/* 1379 */     if (is.isCitizen(creature) && ppl
/* 1380 */       .exists(-30L)) {
/* 1381 */       return ppl.getPermissionsFor(-30L).hasPermission(bit);
/*      */     }
/* 1383 */     if (is.isAllied(creature) && ppl
/* 1384 */       .exists(-20L)) {
/* 1385 */       return ppl.getPermissionsFor(-20L).hasPermission(bit);
/*      */     }
/* 1387 */     if (is.isSameKingdom(creature) && ppl
/* 1388 */       .exists(-40L)) {
/* 1389 */       return ppl.getPermissionsFor(-40L).hasPermission(bit);
/*      */     }
/* 1391 */     return (ppl.exists(-50L) && ppl
/* 1392 */       .getPermissionsFor(-50L).hasPermission(bit));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isGuest(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1403 */     return isGuest(is, creature.getWurmId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isGuest(PermissionsPlayerList.ISettings is, long playerId) {
/* 1414 */     if (is.isOwner(playerId)) {
/* 1415 */       return true;
/*      */     }
/* 1417 */     Long id = Long.valueOf(is.getWurmId());
/* 1418 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 1419 */     if (ppl == null)
/*      */     {
/* 1421 */       return false;
/*      */     }
/*      */     
/* 1424 */     return ppl.exists(playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean canManage(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1435 */     return hasPermission(is, creature, VehiclePermissions.MANAGE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayCommand(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1445 */     if (creature.getPower() > 1)
/* 1446 */       return true; 
/* 1447 */     return hasPermission(is, creature, VehiclePermissions.COMMANDER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayPassenger(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1457 */     if (creature.getPower() > 1)
/* 1458 */       return true; 
/* 1459 */     return hasPermission(is, creature, VehiclePermissions.PASSENGER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayAccessHold(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1469 */     if (creature.getPower() > 1)
/* 1470 */       return true; 
/* 1471 */     return hasPermission(is, creature, VehiclePermissions.ACCESS_HOLD.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayUseBed(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1481 */     if (creature.getPower() > 1)
/* 1482 */       return true; 
/* 1483 */     return hasPermission(is, creature, BedPermissions.MAY_USE_BED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayFreeSleep(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1493 */     if (creature.getPower() > 1)
/* 1494 */       return true; 
/* 1495 */     return hasPermission(is, creature, BedPermissions.FREE_SLEEP.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayDrag(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1505 */     if (creature.getPower() > 1)
/* 1506 */       return true; 
/* 1507 */     return hasPermission(is, creature, VehiclePermissions.DRAG.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayPostNotices(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1517 */     if (creature.getPower() > 1)
/* 1518 */       return true; 
/* 1519 */     return hasPermission(is, creature, MessageBoardPermissions.MAY_POST_NOTICES.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayAddPMs(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1529 */     if (creature.getPower() > 1)
/* 1530 */       return true; 
/* 1531 */     return hasPermission(is, creature, MessageBoardPermissions.MAY_ADD_PMS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isExcluded(PermissionsPlayerList.ISettings is, Creature creature) {
/* 1541 */     if (creature.getPower() > 1)
/* 1542 */       return false; 
/* 1543 */     return hasPermission(is, creature, VehiclePermissions.EXCLUDE.getBit());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */