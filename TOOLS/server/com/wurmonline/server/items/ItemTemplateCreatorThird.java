/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.shared.constants.ModelConstants;
/*      */ import java.io.IOException;
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
/*      */ public class ItemTemplateCreatorThird
/*      */   extends ItemTemplateCreator
/*      */   implements ModelConstants, ItemTypes
/*      */ {
/*   23 */   private static final Logger logger = Logger.getLogger(ItemTemplateCreatorThird.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void initializeTemplates() throws IOException {
/*   32 */     createItemTemplate(1049, "small shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 24, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.1.", 25.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   38 */     createItemTemplate(1050, "double shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 24, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.2.", 25.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   44 */     createItemTemplate(1051, "curved shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 24, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.3.", 25.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   50 */     createItemTemplate(1052, "triple shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 23, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.4.", 25.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   56 */     createItemTemplate(1053, "right elaborate shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 23, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 47 }, "model.armour.shoul.5.right.", 25.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   62 */     createItemTemplate(1054, "exquisite shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 23, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.6.", 25.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   68 */     createItemTemplate(1055, "right basic shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 47 }, "model.armour.shoul.7.right.", 25.0F, 500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   74 */     createItemTemplate(1056, "right shielding shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 47 }, "model.armour.shoul.8.right.", 25.0F, 500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   80 */     createItemTemplate(1057, "right stylish shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 47 }, "model.armour.shoul.9.right.", 25.0F, 500, (byte)7, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   86 */     createItemTemplate(1058, "right layered shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 47 }, "model.armour.shoul.10.right.", 25.0F, 300, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   92 */     createItemTemplate(1059, "chain shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.11.", 25.0F, 300, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   98 */     createItemTemplate(1060, "crafted shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.12.", 25.0F, 300, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  104 */     createItemTemplate(1061, "boar shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.13.", 25.0F, 500, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  110 */     createItemTemplate(1062, "ribboned shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 21, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.14.", 25.0F, 300, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     createItemTemplate(1063, "skull shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.15.", 25.0F, 400, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  122 */     createItemTemplate(1064, "human skull shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.16.", 25.0F, 500, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  128 */     createItemTemplate(1065, "dragon shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.17.", 25.0F, 500, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  135 */     createItemTemplate(1066, "left elaborate shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 23, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46 }, "model.armour.shoul.5.left.", 25.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  142 */     createItemTemplate(1092, "left basic shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46 }, "model.armour.shoul.7.left.", 25.0F, 500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  149 */     createItemTemplate(1093, "left shielding shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46 }, "model.armour.shoul.8.left.", 25.0F, 500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  156 */     createItemTemplate(1094, "left stylish shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46 }, "model.armour.shoul.9.left.", 25.0F, 500, (byte)7, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  163 */     createItemTemplate(1095, "left layered shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "A decorative shoulder pad. These armour pieces come in a variety of materials and designs.", new short[] { 108, 22, 147, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46 }, "model.armour.shoul.10.left.", 25.0F, 300, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  171 */     createItemTemplate(1067, 3, "green cloth tunic", "green cloth tunics", "excellent", "good", "ok", "poor", "A fine tunic made from green cloth with yellow trim and decorative belt.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)960, (short)1, 0, 3024000L, 1, 35, 40, -10, new byte[] { 2 }, "model.clothing.torso.shirt1.", 35.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  179 */     createItemTemplate(1068, 3, "black belted vest", "black belted vests", "excellent", "good", "ok", "poor", "A long, black vest fastened at the waist with a decorative belt.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)960, (short)1, 0, 3024000L, 1, 35, 40, -10, new byte[] { 2 }, "model.clothing.torso.shirt2.", 55.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  187 */     createItemTemplate(1069, 3, "red cloth tunic", "red cloth tunics", "excellent", "good", "ok", "poor", "A fine tunic made from red cloth with leather trim and a decorative belt.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)960, (short)1, 0, 3024000L, 1, 35, 40, -10, new byte[] { 2 }, "model.clothing.torso.shirt3.", 45.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  195 */     createItemTemplate(1106, 3, "plain white cloth sleeve", "white cloth sleeves", "excellent", "good", "ok", "poor", "Sleeves made from plain white fabric.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)962, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.clothing.sleeve.shirt.", 10.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  203 */     createItemTemplate(1074, 3, "green cloth sleeve", "green cloth sleeves", "excellent", "good", "ok", "poor", "Sleeves made from green fabric and leather with a yellow trim.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)962, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.clothing.sleeve.shirt1.", 10.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  211 */     createItemTemplate(1105, 3, "black cloth sleeve", "black cloth sleeves", "excellent", "good", "ok", "poor", "Sleeves made from black fabric with leather trim at the wrists.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)962, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.clothing.sleeve.shirt2.", 10.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  219 */     createItemTemplate(1075, 3, "red cloth sleeve", "red cloth sleeves", "excellent", "good", "ok", "poor", "Sleeves made from red fabric with leather trim at the wrists.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)962, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.clothing.sleeve.shirt3.", 10.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  227 */     createItemTemplate(1107, 3, "plain white cloth pants", "plain white pants", "excellent", "good", "ok", "poor", "Cotton pants made from plain white fabric.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)961, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.cloth.leg.pants.", 10.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  235 */     createItemTemplate(1070, 3, "brown striped breeches", "brown striped breeches", "excellent", "good", "ok", "poor", "Short striped trousers.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)961, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.cloth.leg.pants1.", 10.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  243 */     createItemTemplate(1071, 3, "patchwork pants", "patchwork pants", "excellent", "good", "ok", "poor", "Pants made from swatches of wool cloth stitched together.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)961, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.cloth.leg.pants2.", 10.0F, 300, (byte)69, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  251 */     createItemTemplate(1072, 3, "black cloth pants", "black cloth pants", "excellent", "good", "ok", "poor", "Leg protection sown from thick cloth.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)961, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.cloth.leg.pants3.", 10.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  259 */     createItemTemplate(1073, 3, "green cloth pants", "green cloth pants", "excellent", "good", "ok", "poor", "Leg protection sown from thick cloth.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)961, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.cloth.leg.pants3.green.", 10.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  267 */     createItemTemplate(1076, "socketed ring", "rings", "new", "fancy", "ok", "old", "This ring has a socket for a gem.", new short[] { 22, 87, 48 }, (short)249, (short)1, 0, Long.MAX_VALUE, 1, 2, 2, -10, new byte[] { 40, 39 }, "model.decoration.ring.rift.1.", 99.0F, 50, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  273 */     createItemTemplate(1077, "artisan ring", "rings", "new", "fancy", "ok", "old", "A ring engraved with various artisan symbols.", new short[] { 22, 87, 48 }, (short)249, (short)1, 0, Long.MAX_VALUE, 1, 2, 2, -10, new byte[] { 40, 39 }, "model.decoration.ring.rift.2.", 99.0F, 50, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  279 */     createItemTemplate(1078, "seal ring", "rings", "new", "fancy", "ok", "old", "This ring brandishes a seal symbol on the rim.", new short[] { 22, 87 }, (short)249, (short)1, 0, Long.MAX_VALUE, 1, 2, 2, -10, new byte[] { 40, 39 }, "model.decoration.ring.rift.3.", 99.0F, 50, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  285 */     createItemTemplate(1079, "dark ring", "rings", "new", "fancy", "ok", "old", "A dark ring that blends unnaturally well into any shadows.", new short[] { 22, 87 }, (short)249, (short)1, 0, Long.MAX_VALUE, 1, 2, 2, -10, new byte[] { 40, 39 }, "model.decoration.ring.rift.4.", 99.0F, 50, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  291 */     createItemTemplate(1080, "ring of the Eye", "rings", "new", "fancy", "ok", "old", "The gem on this ring looks like an eye, signalling some sort of magical detection.", new short[] { 22, 87 }, (short)249, (short)1, 0, Long.MAX_VALUE, 1, 2, 2, -10, new byte[] { 40, 39 }, "model.decoration.ring.rift.5.", 99.0F, 50, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  298 */     createItemTemplate(1081, "fist bracelet", "bracelets", "new", "fancy", "ok", "old", "This bracelet has a fist symbol on the top side.", new short[] { 22, 87, 258 }, (short)250, (short)1, 0, Long.MAX_VALUE, 1, 5, 5, -10, new byte[] { 3, 4 }, "model.decoration.armring.rift.1.", 10.0F, 300, (byte)67, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  303 */     createItemTemplate(1082, "huge sword bracelet", "bracelets", "new", "fancy", "ok", "old", "This bracelet has the symbol of a two handed sword engraved.", new short[] { 22, 87, 258 }, (short)250, (short)1, 0, Long.MAX_VALUE, 1, 5, 5, -10, new byte[] { 3, 4 }, "model.decoration.armring.rift.2.", 10.0F, 300, (byte)67, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  308 */     createItemTemplate(1083, "short sword bracelet", "bracelets", "new", "fancy", "ok", "old", "This bracelet has the symbol of a short sword engraved.", new short[] { 22, 87, 258 }, (short)250, (short)1, 0, Long.MAX_VALUE, 1, 5, 5, -10, new byte[] { 3, 4 }, "model.decoration.armring.rift.3.", 10.0F, 300, (byte)67, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  313 */     createItemTemplate(1084, "spear bracelet", "bracelets", "new", "fancy", "ok", "old", "This bracelet has the symbol of a spear engraved.", new short[] { 22, 87, 258 }, (short)250, (short)1, 0, Long.MAX_VALUE, 1, 5, 5, -10, new byte[] { 3, 4 }, "model.decoration.armring.rift.4.", 10.0F, 300, (byte)67, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  318 */     createItemTemplate(1085, "bracelet of inspiration", "bracelets", "new", "fancy", "ok", "old", "The aura around this bracelet emits enhancing magic.", new short[] { 22, 87, 258 }, (short)250, (short)1, 0, Long.MAX_VALUE, 1, 5, 5, -10, new byte[] { 3, 4 }, "model.decoration.armring.rift.5.", 10.0F, 300, (byte)67, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  324 */     createItemTemplate(1086, "soul stealer necklace", "necklaces", "new", "fancy", "ok", "old", "A heavy necklace carrying a white pendant representing Seris.", new short[] { 147, 22, 87 }, (short)268, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, new byte[] { 36 }, "model.decoration.n.rift.1.", 40.0F, 500, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  329 */     createItemTemplate(1087, "artisan necklace", "necklaces", "new", "fancy", "ok", "old", "A delicate necklace with artisan symbols. It helps improving items to a higher level.", new short[] { 147, 22, 87 }, (short)268, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, new byte[] { 36 }, "model.decoration.n.rift.2.", 40.0F, 100, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  334 */     createItemTemplate(1088, "necklace of protection", "necklaces", "new", "fancy", "ok", "old", "This necklace is adorned with symbols of protection.", new short[] { 147, 22, 87 }, (short)268, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, new byte[] { 36 }, "model.decoration.n.rift.3.", 40.0F, 100, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  339 */     createItemTemplate(1089, "necklace of focus", "necklaces", "new", "fancy", "ok", "old", "A large symbol of inner strength hangs from this necklace.", new short[] { 147, 22, 87 }, (short)268, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, new byte[] { 36 }, "model.decoration.n.rift.4.", 40.0F, 100, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  344 */     createItemTemplate(1090, "necklace of replenishment", "necklaces", "new", "fancy", "ok", "old", "Various fruit and vegetable symbols hang from this necklace.", new short[] { 147, 22, 87 }, (short)268, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, new byte[] { 36 }, "model.decoration.n.rift.5.", 40.0F, 100, (byte)67, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  349 */     createItemTemplate(1091, "metallic liquid", "liquid", "excellent", "good", "ok", "poor", "A metallic liquid mixed with tiny black flakes. Smear this on an item to protect it once from shattering during enchants.", new short[] { 26, 54, 183 }, (short)588, (short)1, 0, Long.MAX_VALUE, 4, 4, 9, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.shatterprot.", 40.0F, 144, (byte)21, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  354 */     createItemTemplate(1096, "rift heart", "hearts", "awesome", "good looking", "normal", "ugly", "This heart of a creature from the rift gleams from tiny seryll fragments.", new short[] { 28, 5, 62, 55, 48, 129 }, (short)516, (short)1, 0, 172800L, 4, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.heart.", 500.0F, 300, (byte)1);
/*      */ 
/*      */ 
/*      */     
/*  358 */     createItemTemplate(1100, "knapsack", "knapsacks", "strong", "well-made", "ok", "fragile", "A knapsack for keeping 10 years of Wurm, wogic and memories. Make sure not to burn it up!", new short[] { 44, 1, 97, 157 }, (short)241, (short)1, 0, 3024000L, 30, 50, 50, -10, new byte[] { 42 }, "model.container.knapsack.", 999.0F, 1000, (byte)16, 200, false);
/*      */ 
/*      */     
/*  361 */     createItemTemplate(1101, "champagne", "champagne", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "This champagne is at least 10 years old.", new short[0], (short)260, (short)1, 0, Long.MAX_VALUE, 5, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.champagne.", 99.0F, 1000, (byte)52, 200, false);
/*      */ 
/*      */     
/*  364 */     createItemTemplate(1289, "rune of Magranon", "runes of Magranon", "superb", "normal", "ok", "poor", "A small rune created from resources on Jackal and infused with the spirit of Magranon.", new short[] { 157, 48, 235, 233 }, (short)381, (short)1, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.rune.magranon.", 80.0F, 100, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  369 */     createItemTemplate(1290, "rune of Fo", "runes of Fo", "superb", "normal", "ok", "poor", "A small rune created from resources on Jackal and infused with the spirit of Fo.", new short[] { 157, 48, 235, 233 }, (short)382, (short)1, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.rune.fo.", 80.0F, 100, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  374 */     createItemTemplate(1291, "rune of Vynora", "runes of Vynora", "superb", "normal", "ok", "poor", "A small rune created from resources on Jackal and infused with the spirit of Vynora.", new short[] { 157, 48, 235, 233 }, (short)383, (short)1, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.rune.vynora.", 80.0F, 100, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  379 */     createItemTemplate(1292, "rune of Libila", "runes of Libila", "superb", "normal", "ok", "poor", "A small rune created from resources on Jackal and infused with the spirit of Libila.", new short[] { 157, 48, 235, 233 }, (short)384, (short)1, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.rune.libila.", 80.0F, 100, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  384 */     createItemTemplate(1293, "rune of Jackal", "runes of Jackal", "superb", "normal", "ok", "poor", "A small rune created from resources on Jackal and infused with the spirit of Jackal.", new short[] { 157, 48, 235, 233 }, (short)385, (short)1, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.rune.jackal.", 80.0F, 100, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  391 */     createItemTemplate(1108, "wine barrel rack", "wine barrel racks", "superb", "good", "ok", "poor", "A rack to store wine barrels.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178 }, (short)401, (short)1, 0, 9072000L, 30, 150, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.winebarrel.", 50.0F, 6500, (byte)14, 10000, true);
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
/*  402 */     createItemTemplate(1109, "small barrel rack", "small barrel racks", "superb", "good", "ok", "poor", "A rack to store small barrels.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178 }, (short)402, (short)1, 0, 9072000L, 30, 150, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.barrel.", 30.0F, 6500, (byte)14, 10000, true);
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
/*  414 */     createItemTemplate(1110, "planter rack", "planter racks", "superb", "good", "ok", "poor", "A rack to store herb and spice planters.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178 }, (short)421, (short)1, 0, 9072000L, 20, 30, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.planter.", 25.0F, 6500, (byte)14, 10000, true);
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
/*  426 */     createItemTemplate(1111, "amphora rack", "amphora racks", "superb", "good", "ok", "poor", "A rack to store amphoras.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178 }, (short)422, (short)1, 0, 9072000L, 50, 200, 336, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.amphora.", 45.0F, 6500, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  436 */     createItemTemplate(1412, "empty shelf", "empty shelves", "superb", "good", "ok", "poor", "An empty shelf good for placing items upon.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178, 256 }, (short)422, (short)1, 0, 9072000L, 50, 200, 336, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.amphora.empty.", 45.0F, 6500, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  446 */       .setContainerSize(50, 200, 50);
/*      */ 
/*      */ 
/*      */     
/*  450 */     createItemTemplate(1112, "waystone", "waystones", "superb", "good", "ok", "poor", "A marker to designate a junction in a highway.", new short[] { 25, 199, 178, 54, 52, 44, 241, 157, 86, 48, 246 }, (short)361, (short)56, 0, 12096000L, 20, 20, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.road.waystone.", 45.0F, 6600, (byte)15, 10000, true);
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
/*  462 */     createItemTemplate(1113, "blind catseye", "blind catseyes", "superb", "good", "ok", "poor", "A marker used in the middle of a highway, or it could be, if it had eyes.", new short[] { 25, 44, 157, 54, 146, 86, 246 }, (short)362, (short)56, 0, 12096000L, 5, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.road.catseye.blind.", 15.0F, 500, (byte)15, 10000, true);
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
/*  473 */     createItemTemplate(1114, "catseye", "catseyes", "superb", "good", "ok", "poor", "A marker used in the middle of a highway, protecting the surrounding tiles.", new short[] { 25, 178, 54, 244, 44, 157, 241, 146, 86, 246 }, (short)362, (short)56, 0, 12096000L, 5, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.road.catseye.", 15.0F, 500, (byte)15, 10000, true);
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
/*  485 */     createItemTemplate(1115, "crowbar", "crowbars", "superb", "good", "ok", "poor", "A tool consisting of a steel bar with a single curved end and flattened points.", new short[] { 108, 44, 38, 22, 14, 157 }, (short)738, (short)1, 25, 3024000L, 3, 5, 20, 1025, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.crowbar.", 17.0F, 2000, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  495 */     createItemTemplate(1116, "shards", "sandstone shards", "superb", "good", "ok", "poor", "Lots of different sized sandstone shards.", new short[] { 25, 146, 46, 112, 113, 129, 48, 175 }, (short)1449, (short)46, 0, 604800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.", 1.0F, 20000, (byte)89, 5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  505 */     createItemTemplate(1121, "sandstone brick", "sandstone bricks", "excellent", "good", "ok", "poor", "A stone chiselled from sandstone into a cubic shape.", new short[] { 25, 146, 113, 129, 158, 242, 243 }, (short)1450, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 1.0F, 15000, (byte)89, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  515 */     createItemTemplate(1122, "rounded stone", "rounded stones", "excellent", "good", "ok", "poor", "A stone chiselled into a cubic shape without the sharp corners so it looks more like a stone.", new short[] { 25, 146, 113, 129, 158, 242, 243 }, (short)1451, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.rounded.", 1.0F, 15000, (byte)15, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  525 */     createItemTemplate(1123, "slate brick", "slate bricks", "excellent", "good", "ok", "poor", "A stone chiselled into a cubic shape from slate.", new short[] { 25, 146, 113, 129, 158, 242, 243 }, (short)1452, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 1.0F, 15000, (byte)61, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  535 */     createItemTemplate(1124, "sandstone slab", "sandstone slabs", "excellent", "good", "ok", "poor", "A flat and square sandstone slab. It is about your length and width.", new short[] { 25, 51, 135, 86, 44, 146, 158, 242, 243 }, (short)1453, (short)1, 0, 12096000L, 10, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.slab.", 3.0F, 80000, (byte)89);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  545 */     createItemTemplate(1127, "almanac", "almanacs", "excellent", "good", "ok", "poor", "An annual calendar containing information about fruit, nuts, flowers and other harvestables.", new short[] { 23, 44, 1, 229 }, (short)1454, (short)57, 0, 3024000L, 20, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.almanac.", 5.0F, 250, (byte)16);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  554 */     createItemTemplate(1128, "almanac folder", "almanac folders", "excellent", "good", "ok", "poor", "A folder to help organize almanac pages.", new short[] { 21, 1, 207, 31, 229, 112, 178, 232, 40, 157 }, (short)1455, (short)57, 0, 3024000L, 5, 20, 26, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.almanac.folder.", 5.0F, 0, (byte)33);
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
/*  565 */     createItemTemplate(1404, "archaeology journal", "archaeology journals", "excellent", "good", "ok", "poor", "A journal to safely keep reports of archaeological discoveries you have made. Put a piece of paper or papyrus in this journal to turn it into a blank report.", new short[] { 23, 44, 1, 229 }, (short)1456, (short)57, 0, 3024000L, 15, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.journal.", 20.0F, 250, (byte)16);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  573 */     createItemTemplate(1403, "archaeology report", "archaeology reports", "excellent", "good", "ok", "poor", "A flat piece of papyrus made from pressed reed fibre.", new short[] { 21, 159, 48, 246, 157 }, (short)640, (short)57, 0, 3024000L, 1, 20, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.papyrus.", 5.0F, 10, (byte)33, 5000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  581 */     createItemTemplate(1409, "book", "books", "excellent", "good", "ok", "poor", "A small book with a dyeable cover. Can be used to hold papyrus or paper sheets.", new short[] { 44, 1, 229, 92, 245 }, (short)1457, (short)57, 0, 3024000L, 10, 21, 26, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.book.", 10.0F, 200, (byte)33, 5000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  589 */     createItemTemplate(1302, "slate keystone", "slate keystones", "excellent", "good", "ok", "poor", "Slate chiselled into a keystone.", new short[] { 25, 51, 135, 86, 44, 158, 147 }, (short)466, (short)1, 0, 12096000L, 10, 400, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.keystone.", 20.0F, 80000, (byte)61);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  597 */     createItemTemplate(1303, "clay keystone", "clay keystones", "excellent", "good", "ok", "poor", "clay formed in a keystone shape", new short[] { 108, 196, 44, 147 }, (short)466, (short)1, 0, 12096000L, 10, 145, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.keystone.", 20.0F, 80000, (byte)18);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  604 */     createItemTemplate(1304, "pottery keystone", "pottery keystones", "excellent", "good", "ok", "poor", "A pottery verion of a keystone.", new short[] { 25, 51, 135, 86, 44, 158, 147 }, (short)466, (short)1, 0, 12096000L, 10, 400, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.keystone.", 20.0F, 80000, (byte)19);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  612 */     createItemTemplate(1305, "sandstone keystone", "sandstone keystones", "excellent", "good", "ok", "poor", "Sandstone chiselled into a keystone.", new short[] { 25, 51, 135, 86, 44, 158, 147 }, (short)466, (short)1, 0, 12096000L, 10, 400, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.keystone.", 20.0F, 80000, (byte)89);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  621 */     createItemTemplate(1129, "wagoner contract", "wagoner contracts", "new", "fancy", "ok", "old", "A contract prepared for a wagoner to settle down on a deed and perform delivery runs.", new short[] { 40, 42, 48, 53, 114, 83, 54, 157, 246 }, (short)324, (short)59, 0, Long.MAX_VALUE, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.wagoner.", 100.0F, 0, (byte)33, 150000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  631 */     createItemTemplate(1301, "wagoner campfire", "wagoner campfires", "excellent", "good", "ok", "poor", "A campfire used by a wagoner when they are not out delivering.", new short[] { 52, 21, 31, 157, 245, 49, 207, 123, 48, 246 }, (short)291, (short)1, 0, Long.MAX_VALUE, 41, 41, 201, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.campfire.", 1.0F, 1500, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  641 */     createItemTemplate(1308, "wagoner tent", "wagoner tents", "excellent", "good", "ok", "poor", "A small but useful tent used by a wagoner when they are resting between deliveries. ", new short[] { 24, 181, 109, 61, 52, 48, 245, 207, 246 }, (short)640, (short)41, 0, Long.MAX_VALUE, 5, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.tent.small.wagoner.", 10.0F, 500, (byte)17, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  648 */       .setContainerSize(100, 200, 201);
/*      */     
/*  650 */     createItemTemplate(1309, "wagoner container", "wagoner containers", "superb", "good", "ok", "poor", "A container that can hold up to 20 large crates and is used as a collection point for bulk deliveries.", new short[] { 108, 157, 135, 21, 51, 52, 44, 47, 1, 54, 178, 48, 246, 257 }, (short)311, (short)61, 0, 9072000L, 240, 240, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.crate.huge.", 30.0F, 6500, (byte)14, 10000, true);
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
/*  663 */     createItemTemplate(1312, "crate rack", "crate racks", "superb", "good", "ok", "poor", "A rack to store crates.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178 }, (short)421, (short)1, 0, 9072000L, 240, 360, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.crate.", 30.0F, 6500, (byte)14, 10000, true);
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
/*  674 */     createItemTemplate(1313, "straw bed", "straw beds", "superb", "good", "ok", "poor", "A bed made from some grasses.", new short[] { 109, 51, 52, 117, 246, 86, 31, 67, 48, 110, 178, 157 }, (short)313, (short)1, 0, 9072000L, 3, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.bed.straw.", 10.0F, 10000, (byte)70, 10000, true);
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
/*  686 */     createItemTemplate(1315, "rack for empty bsb", "racks for empty bsbs", "superb", "good", "ok", "poor", "A rack to store empty bsbs in.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 199, 67, 178 }, (short)421, (short)1, 0, 9072000L, 400, 600, 2000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rack.bsb.", 30.0F, 6500, (byte)14, 10000, true);
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
/*  697 */     createItemTemplate(1316, "bulk container unit", "bulk containers units", "superb", "good", "ok", "poor", "A unit to hold several bulk storage bins.", new short[] { 108, 147, 135, 21, 51, 52, 44, 47, 1, 92, 199, 178, 112 }, (short)421, (short)1, 0, 9072000L, 400, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.bulk.unit.", 50.0F, 6500, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  707 */       .setContainerSize(0, 0, 0)
/*  708 */       .setInitialContainers(new InitialContainer[] { new InitialContainer(1317, "bulk storage shelf"), new InitialContainer(1317, "bulk storage shelf"), new InitialContainer(1317, "bulk storage shelf"), new InitialContainer(1317, "bulk storage shelf") });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  714 */     createItemTemplate(1317, "bulk storage shelf", "bulk storage shelves", "superb", "good", "ok", "poor", "A storage bin made from planks and strengthened with iron ribbons.", new short[] { 21, 1, 31, 229, 112, 232, 240, 157, 145, 48, 207, 245 }, (short)469, (short)1, 0, Long.MAX_VALUE, 200, 200, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.storagebin.", 10.0F, 20000, (byte)14, 100, false);
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
/*  725 */     createItemTemplate(1310, "stored creature", "stored creatures", "excellent", "good", "ok", "poor", "A stored creature.", new short[] { 31, 48, 246, 59 }, (short)60, (short)1, 0, Long.MAX_VALUE, 240, 240, 240, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "NotNeeded", 100.0F, 50000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  734 */     createItemTemplate(1311, "creature cage", "creature cages", "excellent", "good", "ok", "poor", "A cage to hold a creature.", new short[] { 167, 47, 21, 63, 52, 31, 1, 176, 145, 86, 246, 44, 59, 48, 257, 51 }, (short)363, (short)1, 0, 9072000L, 120, 120, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.cage.", 60.0F, 150000, (byte)14);
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
/*  749 */     createItemTemplate(1318, "tapestry of Evening", "tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made by Evening.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.comp1.", 30.0F, 12000, (byte)69, 10000, true);
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
/*  760 */     createItemTemplate(1319, "tapestry of Mclavin", "tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made by Mclavin.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.comp2.", 30.0F, 12000, (byte)69, 10000, true);
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
/*  771 */     createItemTemplate(1320, "tapestry of Ehizellbob", "tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made by Ehizellbob.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.comp3.", 30.0F, 12000, (byte)69, 10000, true);
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
/*  782 */     createItemTemplate(1323, "statue of eagle", "eagle statues", "excellent", "good", "ok", "poor", "A statue of a glorious eagle.", new short[] { 108, 31, 135, 25, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.eagle.", 30.0F, 70000, (byte)15)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  790 */       .setContainerSize(30, 30, 100);
/*  791 */     createItemTemplate(1324, "statue of worg", "worg statues", "excellent", "good", "ok", "poor", "A statue of a mighty worg.", new short[] { 108, 31, 135, 25, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.worg.", 30.0F, 70000, (byte)15)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  799 */       .setContainerSize(30, 30, 100);
/*  800 */     createItemTemplate(1325, "statue of hell horse", "hell horse statues", "excellent", "good", "ok", "poor", "A statue of a terrifying hell horse.", new short[] { 108, 31, 135, 25, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.hellhorse.", 30.0F, 70000, (byte)15)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  808 */       .setContainerSize(30, 30, 100);
/*  809 */     createItemTemplate(1330, "statue of drake", "drake statues", "excellent", "good", "ok", "poor", "A statue of an imposing drake.", new short[] { 108, 31, 135, 22, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.drake.", 30.0F, 100000, (byte)11)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  817 */       .setContainerSize(30, 30, 100);
/*  818 */     createItemTemplate(1328, "statue of Fo", "statues", "excellent", "good", "ok", "poor", "A statue of the forest god Fo.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.fo.", 30.0F, 50000, (byte)62);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  826 */     createItemTemplate(1327, "statue of Magranon", "magranon statues", "excellent", "good", "ok", "poor", "A statue of the warrior god Magranon.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.magranon.", 30.0F, 50000, (byte)62);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  834 */     createItemTemplate(1326, "statue of Vynora", "vynora statues", "excellent", "good", "ok", "poor", "A statue of the benevolent goddess Vynora.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.vynora.", 30.0F, 50000, (byte)62);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  842 */     createItemTemplate(1329, "statue of Libila", "libila statues", "excellent", "good", "ok", "poor", "A statue of the malevolent goddess Libila.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.libila.", 30.0F, 50000, (byte)61);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  850 */     createItemTemplate(1405, "statue of guard", "guard statues", "excellent", "good", "ok", "poor", "A statue of a strong guard.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 35, 35, 190, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.guard.", 30.0F, 70000, (byte)62);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  858 */     createItemTemplate(1406, "statue of kyklops", "kyklops statues", "excellent", "good", "ok", "poor", "A statue of a large, drooling one-eyed giant.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.kyklops.", 30.0F, 35000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  866 */     createItemTemplate(1407, "statue of rift beast", "rift beast statues", "excellent", "good", "ok", "poor", "A statue of a beast seen appearing from rifts of jackal.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.riftbeast.", 30.0F, 35000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  874 */     createItemTemplate(1408, "statue of mountain lion", "lion statues", "excellent", "good", "ok", "poor", "A statue of a mountain lion.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.mountainlion.", 30.0F, 35000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  882 */     createItemTemplate(1321, 3, "troll mask", "masks", "excellent", "good", "ok", "poor", "Quite an ugly mask that looks like it has been made by ripping the face off of a troll. Some bits of dried blood and flesh still hang from the back.", new short[] { 108, 44, 23, 4, 187, 157 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.troll.", 25.0F, 200, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  888 */     createItemTemplate(1322, "pumpkin shoulder pad", "shoulder pads", "excellent", "good", "ok", "poor", "Spooky, scary shoulder pads. They send shivers down your spine.", new short[] { 108, 44, 23, 187, 157, 92 }, (short)1140, (short)1, 0, 3024000L, 5, 5, 10, -10, new byte[] { 46, 47 }, "model.armour.shoul.18.", 25.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  895 */     createItemTemplate(1411, "lump", "electrum lumps", "excellent", "good", "ok", "poor", "An alloy of gold and silver.", new short[] { 22, 146, 46, 113, 157 }, (short)631, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 30.0F, 100, (byte)96, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  900 */     createItemTemplate(1414, "Puzzle King's crystal ball", "puzzle crystal balls", "excellent", "good", "ok", "poor", "A crystal ball once owned by the Puzzle King.", new short[] { 199, 147, 108, 246, 229, 187, 44, 52 }, (short)813, (short)1, 0, Long.MAX_VALUE, 30, 30, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.ffcrystalball.", 40.0F, 400, (byte)52);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  906 */     createItemTemplate(1392, "item slot", "item slots", "excellent", "good", "ok", "poor", "An empty slot where an item should go.", new short[] { 45, 233 }, (short)60, (short)0, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.unknown", 99.0F, 1, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  912 */     createItemTemplate(1415, "statue of unicorn", "unicorn statues", "excellent", "good", "ok", "poor", "A statue of a peaceful unicorn family.", new short[] { 108, 31, 135, 25, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 40, 40, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.unicorn.", 30.0F, 60000, (byte)62)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  920 */       .setContainerSize(30, 30, 100);
/*  921 */     createItemTemplate(1416, "statue of goblin", "goblin statues", "excellent", "good", "ok", "poor", "A statue of a menacing little goblin.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 30, 30, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.goblin.", 30.0F, 30000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  929 */     createItemTemplate(1417, "statue of lava fiend", "lava fiend statues", "excellent", "good", "ok", "poor", "A lava fiend frozen in time and mounted on a pedestal.", new short[] { 108, 31, 135, 22, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 40, 40, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.fiend.", 30.0F, 70000, (byte)11)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  937 */       .setContainerSize(30, 30, 100);
/*  938 */     createItemTemplate(1418, "statuette of miner", "miner statuettes", "excellent", "good", "ok", "poor", "A statuette of a miner in the middle of his daily grind.", new short[] { 108, 135, 22, 51, 52, 44, 86, 199, 178 }, (short)60, (short)1, 0, 12096000L, 20, 20, 45, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.knight.mine.", 30.0F, 10000, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  946 */     createItemTemplate(1419, "statuette of swordsman", "swordsman statuettes", "excellent", "good", "ok", "poor", "A statuette of a swordsman ready to dive into battle.", new short[] { 108, 135, 22, 51, 52, 44, 86, 199, 178 }, (short)60, (short)1, 0, 12096000L, 20, 20, 45, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.knight.sword.", 30.0F, 10000, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  954 */     createItemTemplate(1420, "statuette of axeman", "axeman statuettes", "excellent", "good", "ok", "poor", "A statuette of an axeman preparing his swing.", new short[] { 108, 135, 22, 51, 52, 44, 86, 199, 178 }, (short)60, (short)1, 0, 12096000L, 20, 20, 45, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.soldier.axe.", 30.0F, 10000, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  962 */     createItemTemplate(1421, "statuette of digger", "digger statuettes", "excellent", "good", "ok", "poor", "A statuette of a soldier in the middle of digging.", new short[] { 108, 135, 22, 51, 52, 44, 86, 199, 178 }, (short)60, (short)1, 0, 12096000L, 20, 20, 45, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.soldier.dig.", 30.0F, 10000, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  970 */     createItemTemplate(1422, "village cache", "caches", "almost full", "somewhat occupied", "half-full", "emptyish", "A small chest that rattles with the sound of some item fragments inside.", new short[] { 1, 31, 6, 157, 52, 40, 123, 63 }, (short)244, (short)1, 0, 9072000L, 30, 50, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.chest.large.treasure.arch.", 210.0F, 200000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  977 */     createItemTemplate(1423, "village token", "mini village tokens", "excellent", "good", "ok", "poor", "A small village token from investigating the history of a deed.", new short[] { 108, 48, 135, 22, 51, 52, 44, 199, 178, 157 }, (short)60, (short)1, 0, 12096000L, 15, 15, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.board.village.cache.", 30.0F, 3000, (byte)10);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  986 */     createItemTemplate(1424, "leather drinking boot", "leather boots", "excellent", "good", "ok", "poor", "A sturdy boot made from leather. Perfect for ruining a drink.", new short[] { 108, 44, 23, 92, 157, 33, 1 }, (short)985, (short)1, 0, 3024000L, 5, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.armour.foot.", 15.0F, 400, (byte)16);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  994 */     createItemTemplate(1432, "chicken coop", "chicken coops", "excellent", "good", "ok", "poor", "A small house where female chickens are kept safe and secure.", new short[] { 199, 135, 257, 47, 51, 44, 52, 31, 176, 48, 21, 1, 178, 59, 229, 112, 157 }, (short)403, (short)1, 0, 9072000L, 120, 120, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.coop.", 50.0F, 60000, (byte)14, 10000, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1004 */       .setContainerSize(0, 0, 0)
/* 1005 */       .setInitialContainers(new InitialContainer[] { new InitialContainer(1436, "Nesting Box"), new InitialContainer(1433, "Egg Box"), new InitialContainer(1434, "Feeder"), new InitialContainer(1435, "Drinker") });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     createItemTemplate(1433, "egg box", "egg boxes", "superb", "good", "ok", "poor", "A box to hold eggs.", new short[] { 48, 63, 21, 1, 31, 54, 232, 240, 178, 59, 229, 112, 157, 245 }, (short)312, (short)1, 0, Long.MAX_VALUE, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.sack.questionmark.", 50.0F, 6500, (byte)14, 100, false);
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
/* 1022 */     createItemTemplate(1434, "feeder", "feeders", "superb", "good", "ok", "poor", "A fed chicken is a happy chicken.", new short[] { 48, 21, 1, 31, 54, 232, 240, 178, 59, 229, 112, 157, 245 }, (short)312, (short)1, 0, Long.MAX_VALUE, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.sack.questionmark.", 50.0F, 6500, (byte)14, 100, false);
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
/* 1033 */     createItemTemplate(1435, "drinker", "drinkers", "superb", "good", "ok", "poor", "A small barrel, carefully placed, to be used as a rudimentary watering device.", new short[] { 48, 21, 1, 31, 54, 232, 240, 178, 59, 229, 112, 157, 245, 33 }, (short)245, (short)1, 0, Long.MAX_VALUE, 30, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.sack.questionmark.", 50.0F, 6500, (byte)14, 100, false);
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
/* 1044 */     createItemTemplate(1436, "nesting box", "nesting boxes", "superb", "good", "ok", "poor", "An essential part to any coop, the nesting box.", new short[] { 63, 48, 21, 1, 31, 54, 232, 240, 178, 59, 229, 112, 157, 245 }, (short)363, (short)1, 0, Long.MAX_VALUE, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.sack.questionmark.", 50.0F, 6500, (byte)14, 100, false);
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
/* 1057 */     createItemTemplate(1428, 3, "skull mask", "masks", "excellent", "good", "ok", "poor", "A gruesome and scary mask with blood staining the front of it. It appears to have been fashioned from a face of a humanoid skull.", new short[] { 108, 44, 23, 4, 187, 157 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.skull.", 25.0F, 200, (byte)35, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1064 */     createItemTemplate(1429, 3, "witch's hat", "hats", "excellent", "good", "ok", "poor", "A black pointed hat accented with a leather belt and golden buckle. Perfect attire for turning villagers into newts.", new short[] { 108, 44, 23, 4, 187, 157 }, (short)963, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 1 }, "model.armour.head.hat.witch.", 25.0F, 200, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1071 */     createItemTemplate(1430, "statue of Tich", "Tich statues", "excellent", "good", "ok", "poor", "In loving memory of our friend and developer, Tich.", new short[] { 108, 31, 135, 25, 51, 1, 52, 44, 86, 199, 256, 92, 176, 178, 22 }, (short)60, (short)1, 0, 12096000L, 30, 30, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.pifa.", 30.0F, 60000, (byte)62)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1079 */       .setContainerSize(25, 25, 100);
/*      */     
/* 1081 */     createItemTemplate(1437, "snowman statue", "snowman statues", "excellent", "good", "ok", "poor", "A statue of a snowman with his hands held out as if offering something.", new short[] { 25, 108, 51, 52, 40, 199, 1, 256, 92, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 50, 50, 140, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.snowman.xmas.", 80.0F, 10000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1089 */     createItemTemplate(1438, "affinity token", "affinity tokens", "excellent", "good", "ok", "poor", "A small token that can be claimed for your choice of permanent skill affinity.", new short[] { 42, 61, 31, 40, 157, 112, 229, 246, 178 }, (short)573, (short)1, 0, Long.MAX_VALUE, 2, 2, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 99.0F, 50, (byte)11);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateCreatorThird.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */