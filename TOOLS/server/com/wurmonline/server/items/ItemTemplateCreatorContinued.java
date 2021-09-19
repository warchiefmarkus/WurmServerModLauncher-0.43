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
/*      */ public class ItemTemplateCreatorContinued
/*      */   extends ItemTemplateCreator
/*      */   implements ModelConstants, ItemTypes
/*      */ {
/*   67 */   private static final Logger logger = Logger.getLogger(ItemTemplateCreatorContinued.class.getName());
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
/*      */   public static final void initializeTemplates() throws IOException {
/*   79 */     createItemTemplate(609, "spring", "springs", "excellent", "good", "ok", "poor", "A strong spring capable of generating a severe punch when unleashed.", new short[] { 44, 22 }, (short)60, (short)1, 0, 3024000L, 4, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.spring.", 30.0F, 1000, (byte)9);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   85 */     createItemTemplate(610, "stick trap", "stick traps", "almost full", "somewhat occupied", "half-full", "emptyish", "A devious trap made from sharp sticks that will be hard to detect in grass.", new short[] { 132, 108, 147, 21, 44 }, (short)60, (short)1, 0, 9072000L, 30, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.sticks.", 15.0F, 50000, (byte)14, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   92 */     createItemTemplate(611, "pole trap", "pole traps", "almost full", "somewhat occupied", "half-full", "emptyish", "A trap made from ropes and a sharp deadly pole.", new short[] { 132, 108, 147, 21, 44 }, (short)60, (short)1, 0, 9072000L, 10, 50, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.pole.", 25.0F, 50000, (byte)14, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   99 */     createItemTemplate(612, "corrosive trap", "corrosive traps", "almost full", "somewhat occupied", "half-full", "emptyish", "This trap contains fragile pottery flasks full of corrosive liquid.", new short[] { 132, 108, 147, 30, 44 }, (short)60, (short)1, 0, 9072000L, 30, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.corrosive.", 25.0F, 10000, (byte)19, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  106 */     createItemTemplate(613, "axe trap", "axe traps", "almost full", "somewhat occupied", "half-full", "emptyish", "The trap is made from a strong wooden frame, and a spring that triggers a heavy axe.", new short[] { 132, 108, 147, 21, 44 }, (short)60, (short)1, 0, 9072000L, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.axe.", 65.0F, 50000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  112 */     createItemTemplate(614, "knife trap", "knife traps", "almost full", "somewhat occupied", "half-full", "emptyish", "A lot of small knife blades are fitted on a wooden frame that springs to life as someone steps on its trigger plate.", new short[] { 132, 108, 147, 21, 44 }, (short)60, (short)1, 0, 9072000L, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.knife.", 55.0F, 40000, (byte)14, 10000, true);
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
/*  125 */     createItemTemplate(615, "net trap", "net traps", "almost full", "somewhat occupied", "half-full", "emptyish", "A net that should be laid out, entwining anyone who triggers it.", new short[] { 132, 108, 147, 24, 44 }, (short)60, (short)1, 0, 9072000L, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.net.", 45.0F, 10000, (byte)17, 3000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  131 */     createItemTemplate(616, "scythe trap", "scythe traps", "almost full", "somewhat occupied", "half-full", "emptyish", "A very deadly trap, this one is made from a scythe blade hidden in masonry disguised as rock.", new short[] { 132, 108, 147, 25, 44 }, (short)60, (short)1, 0, 9072000L, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.scythe.", 65.0F, 50000, (byte)15, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  137 */     createItemTemplate(617, "man trap", "man traps", "almost full", "somewhat occupied", "half-full", "emptyish", "This one will hurt. A large steel jaw triggered by a spring that could chop off a foot or two.", new short[] { 132, 108, 147, 22, 44 }, (short)60, (short)1, 0, 9072000L, 30, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.man.", 65.0F, 10000, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  143 */     createItemTemplate(618, "bow trap", "bow traps", "almost full", "somewhat occupied", "half-full", "emptyish", "A bow mounted on a wooden frame, poised to shoot anyone who triggers it.", new short[] { 132, 108, 147, 21, 44 }, (short)60, (short)1, 0, 9072000L, 30, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.bow.", 35.0F, 30000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  149 */     createItemTemplate(619, "rope trap", "rope traps", "almost full", "somewhat occupied", "half-full", "emptyish", "This rope tied in a strangling loop will slow people down if laid out on the ground with a trigger mechanism.", new short[] { 132, 108, 147 }, (short)60, (short)1, 0, 9072000L, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.trap.rope.", 25.0F, 3000, (byte)53, 1000, true);
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
/*  162 */     createItemTemplate(621, "saddle", "saddles", "strong", "well-made", "ok", "fragile", "A leather saddle complete with a girth and stirrups.", new short[] { 44, 147, 23, 136, 1, 180, 92, 139 }, (short)757, (short)1, 0, 3024000L, 30, 40, 50, -10, new byte[] { 2 }, "model.riding.saddle.", 30.0F, 5500, (byte)16, 2000, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  169 */       .setContainerSize(30, 40, 50)
/*  170 */       .addContainerRestriction(true, new int[] { 1333, 1334 });
/*  171 */     createItemTemplate(622, 4, "saddle", "saddles", "strong", "well-made", "ok", "fragile", "A leather saddle complete with a girth and stirrups.", new short[] { 44, 147, 23, 136, 92 }, (short)777, (short)1, 0, 3024000L, 40, 50, 60, -10, new byte[] { 2 }, "model.riding.saddle.", 60.0F, 6000, (byte)16, 10000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  179 */     createItemTemplate(702, 3, "leather barding", "bardings", "strong", "well-made", "ok", "fragile", "A leather barding fitted together with metal rings and rivets.", new short[] { 44, 147, 23, 136, 4, 92, 108 }, (short)1120, (short)1, 0, 3024000L, 40, 50, 60, -10, new byte[] { 2 }, "model.riding.barding.", 40.0F, 15000, (byte)16, 20000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  187 */     createItemTemplate(703, 3, "chain barding", "bardings", "strong", "well-made", "ok", "fragile", "A powerful chain barding to protect your noble steed.", new short[] { 44, 147, 22, 136, 4, 92, 108 }, (short)1120, (short)1, 0, 3024000L, 40, 50, 60, -10, new byte[] { 2 }, "model.riding.barding.", 60.0F, 25000, (byte)11, 50000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  195 */     createItemTemplate(704, 3, "cloth barding", "bardings", "strong", "well-made", "ok", "fragile", "A simple yet functional cloth barding that will provide moderate protection.", new short[] { 44, 147, 24, 136, 4, 108 }, (short)1120, (short)1, 0, 3024000L, 40, 50, 60, -10, new byte[] { 2 }, "model.riding.barding.", 40.0F, 12000, (byte)17, 10000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  203 */     createItemTemplate(629, "saddle seat", "saddle seats", "strong", "well-made", "ok", "fragile", "A leather saddle seat. Combined with a girth and stirrups it can be used on a horse.", new short[] { 44, 23, 108 }, (short)797, (short)1, 0, 3024000L, 30, 40, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.saddleseat.", 30.0F, 2000, (byte)16, 2000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  210 */     createItemTemplate(630, 4, "saddle seat", "saddle seats", "strong", "well-made", "ok", "fragile", "A large leather saddle seat. Together with a girth and stirrups, it will make a large saddle.", new short[] { 44, 23, 108 }, (short)817, (short)1, 0, 3024000L, 40, 50, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.saddleseat.", 60.0F, 2500, (byte)16, 10000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  217 */     createItemTemplate(624, "bridle", "bridles", "excellent", "good", "ok", "poor", "These thin leather reins, headstall and metal bit should be put on the head of a creature to direct it.", new short[] { 108, 147, 44, 23, 92, 136 }, (short)917, (short)1, 0, 3024000L, 1, 4, 10, -10, new byte[] { 1 }, "model.riding.bridle.", 15.0F, 800, (byte)16, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  225 */     createItemTemplate(625, "girth", "girth", "excellent", "good", "ok", "poor", "This is a thick saddlebelt designed for a creatures belly.", new short[] { 44, 147, 23, 92 }, (short)897, (short)1, 0, 3024000L, 2, 8, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.girth.", 18.0F, 2500, (byte)16, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  231 */     createItemTemplate(628, "reins", "reins", "excellent", "good", "ok", "poor", "Thin leather reins, used together with a bit and headstall to direct a creature.", new short[] { 44, 147, 23, 92 }, (short)957, (short)1, 0, 3024000L, 2, 4, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.reins.", 16.0F, 300, (byte)16, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  237 */     createItemTemplate(631, "headstall", "headstall", "excellent", "good", "ok", "poor", "These are the leather straps that go around a creatures head in the bridle used to direct it.", new short[] { 44, 147, 23, 92 }, (short)937, (short)1, 0, 3024000L, 2, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.headstall.", 16.0F, 300, (byte)16, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  244 */     createItemTemplate(626, "stirrups", "stirrups", "excellent", "good", "ok", "poor", "Supposed to hang from a saddle, these sturdy leather stirrups cover half the foot.", new short[] { 44, 147, 23, 92 }, (short)837, (short)1, 0, 3024000L, 5, 8, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.stirrups.", 22.0F, 1000, (byte)16, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  251 */     createItemTemplate(627, "mouth bit", "mouth bits", "excellent", "good", "ok", "poor", "A metal bit to put in the mouth of a creature that you want to direct. Use with a headstall and reins.", new short[] { 44, 147, 22 }, (short)60, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.riding.bit.", 5.0F, 200, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  258 */     createItemTemplate(623, "horse shoe", "horse shoes", "excellent", "good", "ok", "poor", "These are said to bring luck! Apart from improving horse speed and safety, of course.", new short[] { 44, 147, 22, 136, 108 }, (short)737, (short)1, 0, 3024000L, 1, 5, 10, -10, new byte[] { 13, 14, 15, 16 }, "model.riding.horseshoe.", 5.0F, 500, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  266 */     createItemTemplate(632, "yoke", "yokes", "fine", "nice", "mediocre", "poor", "This fits on the shoulders of a pair of creatures like oxen and will be used to drag a cart or a plow.", new short[] { 21, 147, 44 }, (short)60, (short)1, 0, 9072000L, 10, 40, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.yoke.", 20.0F, 20000, (byte)14, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  273 */     createItemTemplate(1332, "small bag", "small bags", "excellent", "good", "ok", "poor", "A small bag or pouch, used in the creation of other items.", new short[] { 44, 147, 23 }, (short)242, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.satchel.", 15.0F, 1000, (byte)16, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  280 */     createItemTemplate(1333, "saddle bags", "saddle bags", "strong", "well-made", "ok", "fragile", "A pair of bags that may be added to a saddle.", new short[] { 44, 147, 23, 1, 248, 92, 139 }, (short)242, (short)1, 0, 3024000L, 40, 50, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.mountbag.", 30.0F, 3500, (byte)16, 1500, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  286 */     createItemTemplate(1334, "saddle sacks", "saddle sacks", "strong", "well-made", "ok", "fragile", "A pair of sacks that look like they may fit on a saddle. A special gift from Santa.", new short[] { 44, 157, 23, 1, 187, 139 }, (short)242, (short)1, 0, Long.MAX_VALUE, 40, 55, 55, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.mountbag.xmas.", 30.0F, 3500, (byte)16, 1500, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  291 */     createItemTemplate(640, "Fo puppet", "puppets", "superb", "good", "ok", "poor", "A hand puppet made from cloth and yarn.", new short[] { 24, 44, 138, 157 }, (short)306, (short)1, 0, 9072000L, 10, 10, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.toy.puppet.fo.", 30.0F, 500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  297 */     createItemTemplate(642, "Vynora puppet", "puppets", "superb", "good", "ok", "poor", "A hand puppet made from cloth and yarn.", new short[] { 24, 44, 138, 157 }, (short)303, (short)1, 0, 9072000L, 10, 10, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.toy.puppet.vynora.", 30.0F, 500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  303 */     createItemTemplate(641, "Magranon puppet", "puppets", "superb", "good", "ok", "poor", "A hand puppet made from cloth and yarn.", new short[] { 24, 44, 138, 157 }, (short)305, (short)1, 0, 9072000L, 10, 10, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.toy.puppet.magranon.", 30.0F, 500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  310 */     createItemTemplate(643, "Libila puppet", "puppets", "superb", "good", "ok", "poor", "A hand puppet made from cloth and yarn.", new short[] { 24, 44, 138, 157 }, (short)304, (short)1, 0, 9072000L, 10, 10, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.toy.puppet.libila.", 30.0F, 500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  316 */     createItemTemplate(647, "grooming brush", "grooming brushes", "excellent", "good", "ok", "poor", "This wooden brush is used to groom animals. Its wooden handle is lined with lots of thin, strong wemp straws.", new short[] { 108, 21, 147, 44, 38 }, (short)902, (short)1, 0, 9072000L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.groomingbrush.", 15.0F, 350, (byte)14, 100, true);
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
/*  330 */     createItemTemplate(650, "farmer's salve", "salves", "excellent", "good", "ok", "poor", "This faintly healing salve will cure swelling and bruises. It has a pretty vibrant smell of garlic.", new short[] { 27, 46, 147 }, (short)262, (short)1, 0, 86400L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.healing.salve.", 5.0F, 100, (byte)32, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  337 */     createItemTemplate(653, "glass flask", "flask", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small, brittle and flat glass flask.", new short[] { 1, 33, 108 }, (short)261, (short)1, 0, 12096000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.", 10.0F, 70, (byte)20, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  342 */     createItemTemplate(654, "transmutation liquid", "liquid", "excellent", "good", "ok", "poor", "A milky and oily grey liquid. It has a strong pungent odor that burns your nostrils.", new short[] { 26, 54 }, (short)588, (short)1, 0, Long.MAX_VALUE, 4, 4, 9, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.transmutation.", 40.0F, 144, (byte)21, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  348 */     createItemTemplate(655, "snowman", "snowmen", "almost full", "somewhat occupied", "half-full", "emptyish", "A proper snowman is guarding this area.", new short[] { 108, 31, 51, 52, 67, 86, 157, 92, 237 }, (short)60, (short)1, 0, 604800L, 40, 40, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.snowman.", 5.0F, 150000, (byte)26);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  355 */     createItemTemplate(652, "christmas tree", "christmas trees", "fresh", "dry", "brittle", "rotten", "A beautiful christmas tree, with colorful decoration. You should check if santa left you something!", new short[] { 21, 113, 157, 52, 129, 237 }, (short)606, (short)1, 20, 604800L, 40, 40, 200, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.tree.christmas.", 1.0F, 15000, (byte)37);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  362 */     createItemTemplate(738, "garden gnome", "gnomes", "almost full", "somewhat occupied", "half-full", "emptyish", "A small serious gnome stands here ready for christmas.", new short[] { 108, 51, 52, 67, 92, 89, 1, 33, 40, 178 }, (short)60, (short)1, 0, 604800L, 10, 10, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.garden.", 300.0F, 20000, (byte)18);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  370 */     createItemTemplate(661, 3, "food storage bin", "food storage bins", "almost full", "somewhat occupied", "half-full", "emptyish", "A storage bin made from planks and strengthened with iron ribbons.", new short[] { 108, 147, 135, 144, 21, 51, 52, 44, 47, 1, 92, 145, 176, 48, 199 }, (short)468, (short)1, 0, 9072000L, 200, 200, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.storagebin.food.", 10.0F, 20000, (byte)14, 10000, false, 3000);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  380 */     createItemTemplate(662, 3, "bulk storage bin", "bulk storage bins", "almost full", "somewhat occupied", "half-full", "emptyish", "A storage bin made from planks and strengthened with iron ribbons.", new short[] { 108, 147, 135, 144, 21, 51, 52, 44, 47, 1, 92, 145, 176, 48, 199 }, (short)469, (short)1, 0, 9072000L, 200, 200, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.storagebin.", 10.0F, 20000, (byte)14, 10000, false, 3000);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  390 */     createItemTemplate(663, "settlement form", "settlement forms", "new", "fancy", "ok", "old", "A form to fill out for renting land and creating a settlement.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 100000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  396 */     createItemTemplate(664, 4, "magical chest", "magical chests", "almost full", "somewhat occupied", "half-full", "emptyish", "A large chest that prevents non-food decay while having normal decay itself. It can not be repaired or picked up.", new short[] { 108, 135, 1, 31, 21, 47, 51, 52, 53, 127, 176, 259 }, (short)244, (short)1, 0, 9072000L, 30, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.chest.large.magical.", 10.0F, 15000, (byte)14, 500000, false, 0);
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
/*  414 */     createItemTemplate(665, 2, "magical chest", "magical chests", "almost full", "somewhat occupied", "half-full", "emptyish", "A small chest that prevents non-food decay while having normal decay itself. It can not be repaired but picked up.", new short[] { 108, 135, 1, 21, 47, 51, 52, 53, 127, 259 }, (short)244, (short)1, 0, 9072000L, 30, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.chest.small.magical.", 10.0F, 3000, (byte)14, 250000, false, 0);
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
/*  430 */     createItemTemplate(666, "sleep powder", "sleep powder", "excellent", "good", "ok", "poor", "This greyish powder, when consumed, will give 1 hour sleep bonus.", new short[] { 42, 5, 53, 76, 127 }, (short)641, (short)1, 0, Long.MAX_VALUE, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.whitepowder", 300.0F, 25, (byte)6, 50000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  438 */     createItemTemplate(667, "tuning fork of metal detection", "tuning forks", "excellent", "good", "ok", "poor", "This silver tuning fork can be used one time to calculate the max qualitylevel and amount of ore left in an ore deposit.", new short[] { 42, 22, 53, 127 }, (short)739, (short)1, 0, Long.MAX_VALUE, 1, 1, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.tunefork.", 300.0F, 300, (byte)8, 10000, false);
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
/*  451 */     createItemTemplate(668, "Rod of Transmutation", "rods of transmutation", "excellent", "good", "ok", "poor", "A two foot long granite rod that turns a normal cave tile into the ore of your choice. The tile will have max ql limit of 99 and 10 000 mining operations. The rod is destroyed in the process.", new short[] { 42, 53, 127, 155 }, (short)1259, (short)1, 0, Long.MAX_VALUE, 5, 10, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.rodtrans.", 300.0F, 1000, (byte)15, 500000, false);
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
/*  466 */     createItemTemplate(669, "bulk item", "items", "superb", "good", "ok", "poor", "A generic bulk item", new short[] { 31, 54, 216, 212, 48 }, (short)60, (short)1, 0, 19353600L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 10.0F, 0, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  476 */     createItemTemplate(670, "trash heap", "trash heaps", "almost full", "somewhat occupied", "half-full", "emptyish", "A simple trash heap with a base made from planks. Things sure rot away quickly in there.", new short[] { 108, 147, 21, 135, 144, 109, 31, 51, 52, 44, 1, 92 }, (short)60, (short)1, 0, 9072000L, 200, 200, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.storagebin.trash.", 1.0F, 20000, (byte)14, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  485 */     createItemTemplate(672, "decayitem", "items", "superb", "good", "ok", "poor", "A quick decay item that looks like a wooden potato. It is used for testing decay.", new short[] { 20, 55, 129, 21, 59 }, (short)500, (short)1, 0, 1800L, 5, 5, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.potato.", 200.0F, 100, (byte)37, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  491 */     createItemTemplate(676, "mission ruler", "mission rulers", "excellent", "good", "ok", "poor", "A weird magical wooden ruler with obscure signs and letters. Its purpose seems to be to measure human progress in definite terms.", new short[] { 40, 21, 42, 53, 48, 127 }, (short)759, (short)1, 0, Long.MAX_VALUE, 3, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 99.0F, 10, (byte)14, 100000, false);
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
/*  505 */     createItemTemplate(751, "mission ruler recharge", "ruler recharges", "excellent", "good", "ok", "poor", "An inscribed piece of wood which can be used to recharge a mission ruler.", new short[] { 40, 21, 42, 53, 127 }, (short)759, (short)1, 0, Long.MAX_VALUE, 3, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 99.0F, 10, (byte)14, 100000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  512 */     createItemTemplate(682, "declaration of independence", "declarations", "new", "fancy", "ok", "old", "This can be used to form a new kingdom. You need to be mayor and have 25 premium players closeby.", new short[] { 40, 42, 53 }, (short)324, (short)1, 0, Long.MAX_VALUE, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.declaration.", 100.0F, 0, (byte)33, 1000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  520 */     createItemTemplate(683, "Valrei item", "items", "excellent", "good", "ok", "poor", "A weird item belonging on Valrei.", new short[] { 32, 59 }, (short)462, (short)1, 0, 86400L, 3, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.valrei.", 300.0F, 100, (byte)21, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  525 */     createItemTemplate(737, "Valrei mission item", "items", "excellent", "good", "ok", "poor", "A weird item belonging on Valrei.", new short[] { 32, 59, 147, 60 }, (short)462, (short)1, 0, 86400L, 3, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.valrei.", 300.0F, 100, (byte)21, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  531 */     createItemTemplate(685, "crude knife", "crude knives", "superb", "good", "ok", "poor", "This piece of rock can barely be called a knife but it will do the trick.", new short[] { 38, 25, 17, 13, 11, 97, 151, 113 }, (short)1201, (short)1, 1, 3024000L, 1, 3, 10, 10007, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.knife.crude.", 1.0F, 600, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  538 */     createItemTemplate(687, "crude pickaxe", "crude pickaxes", "superb", "good", "ok", "poor", "A hard, sharp piece of rock fastened to a shaft.", new short[] { 38, 25, 10, 2, 97, 151, 113, 157, 247 }, (short)743, (short)1, 3, 9072000L, 1, 30, 70, 10009, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.pickaxe.crude.", 1.0F, 3000, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  545 */     createItemTemplate(686, "crude pickaxe head", "crude pickaxe heads", "excellent", "good", "ok", "poor", "A piece of hard stone roughly shaped into a pickaxe blade.", new short[] { 25, 151 }, (short)723, (short)1, 0, 3024000L, 1, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.pickaxe.blade.crude.", 15.0F, 2000, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  551 */     createItemTemplate(1011, "crude axe", "crude axes", "superb", "good", "ok", "poor", "A hard, sharp piece of rock fastened to a short shaft.", new short[] { 38, 25, 37, 97, 15, 2, 151, 113, 157 }, (short)744, (short)1, 3, 9072000L, 1, 30, 70, 10009, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.small.crude.", 1.0F, 3000, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  558 */     createItemTemplate(1010, "crude axe head", "crude axe heads", "excellent", "good", "ok", "poor", "A piece of hard stone roughly shaped into an axe blade.", new short[] { 25, 151 }, (short)724, (short)1, 0, 3024000L, 1, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.small.blade.crude.", 15.0F, 2000, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  564 */     createItemTemplate(688, "branch", "branches", "fresh", "dry", "brittle", "rotten", "A one step long wooden branch that could be used to create a shaft.", new short[] { 133, 146, 21, 14, 144, 37, 84, 129, 151, 113, 175, 211 }, (short)646, (short)1, 3, 28800L, 3, 7, 100, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.shaft.crude.", 1.0F, 2000, (byte)14, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  573 */     createItemTemplate(689, "crude shovel blade", "stone shovel blades", "excellent", "good", "ok", "poor", "A sort of stone blade that maybe would make a shovel were it not for the lack of a shaft.", new short[] { 25, 151, 113 }, (short)726, (short)1, 0, 3024000L, 1, 15, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.shovel.blade.crude.", 1.0F, 1000, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  579 */     createItemTemplate(690, "crude shovel", "crude shovels", "superb", "good", "ok", "poor", "A tool for digging.", new short[] { 133, 38, 21, 19, 37, 2, 97, 151, 113, 157, 247 }, (short)746, (short)1, 2, 3024000L, 2, 20, 100, 10002, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.shovel.crude.", 20.0F, 2000, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  588 */     createItemTemplate(691, "crude shaft", "crude shafts", "fresh", "dry", "brittle", "rotten", "A one step long crooked wooden shaft that could be used to create a very simple tool or a weapon.", new short[] { 133, 146, 21, 14, 144, 37, 84, 129, 151, 113, 157 }, (short)646, (short)1, 1, 28800L, 3, 7, 100, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.shaft.crude", 3.0F, 1000, (byte)14, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  598 */     createItemTemplate(700, "fireworks", "fireworks", "superb", "good", "ok", "poor", "A package of fireworks.", new short[] { 108, 21, 52 }, (short)60, (short)1, 0, Long.MAX_VALUE, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireworks.", 70.0F, 4000, (byte)14, 30000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  603 */     createItemTemplate(725, "polearms rack", "polearms racks", "almost full", "somewhat occupied", "half-full", "emptyish", "A functional rack for the really long weapons.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 199, 176, 178 }, (short)60, (short)1, 0, 9072000L, 10, 110, 250, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.weapons.polearms.", 15.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  612 */     createItemTemplate(724, "weapons rack", "weapons racks", "almost full", "somewhat occupied", "half-full", "emptyish", "A purposeful weapons rack that works for most weapons.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 199, 176, 178 }, (short)60, (short)1, 0, 9072000L, 10, 110, 180, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.weapons.", 15.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  621 */     createItemTemplate(735, "large clapper", "large clappers", "excellent", "good", "ok", "poor", "This is a lump of metal designed to hang in a large bell and make ding dong.", new short[] { 22, 44 }, (short)60, (short)1, 0, 3024000L, 10, 10, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.clapper.large.", 20.0F, 2000, (byte)11, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  627 */     createItemTemplate(734, "tiny clapper", "tiny clappers", "excellent", "good", "ok", "poor", "This small metal lump should be attached to a small ceremonial bell and make tingeling.", new short[] { 22, 44 }, (short)60, (short)1, 0, 3024000L, 1, 1, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.clapper.small.", 10.0F, 100, (byte)11, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  633 */     createItemTemplate(718, "huge bell", "huge bells", "excellent", "good", "ok", "poor", "A huge bell that can be put in a cot to ring when celebrating or at a ceremony.", new short[] { 22, 44 }, (short)298, (short)1, 0, 3024000L, 40, 40, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.bell.huge.", 30.0F, 8300, (byte)31, 50000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  639 */     createItemTemplate(719, "small bell", "small bells", "excellent", "good", "ok", "poor", "A small hand held bell that will make a pleasant tingeling sound to summon spirits.", new short[] { 22, 44 }, (short)278, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.bell.small.", 20.0F, 500, (byte)30, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  645 */     createItemTemplate(721, "large bell resonator", "large resonators", "excellent", "good", "ok", "poor", "The resonator part of a huge bell. It still needs a clapper if it's going to make a sound.", new short[] { 22, 44, 52 }, (short)298, (short)1, 0, 3024000L, 40, 40, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.bell.huge.resonator.", 60.0F, 6300, (byte)31, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  651 */     createItemTemplate(720, "small resonator", "small resonators", "excellent", "good", "ok", "poor", "The resonator of small hand held bell. It still needs a clapper.", new short[] { 22, 44 }, (short)278, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.bell.small.resonator.", 60.0F, 400, (byte)30, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  657 */     createItemTemplate(723, "bell cot", "bell cots", "almost full", "somewhat occupied", "half-full", "emptyish", "This is the framework where a huge bell should hang.", new short[] { 21, 44, 52 }, (short)309, (short)1, 0, 9072000L, 110, 110, 180, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.bellcot.", 25.0F, 30000, (byte)14, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  663 */     createItemTemplate(722, "bell tower", "bell towers", "almost full", "somewhat occupied", "half-full", "emptyish", "A bell cot with a huge bell hanging inside. It will make a powerful sound when rung.", new short[] { 21, 44, 52, 31, 67, 109, 51, 178, 86, 176, 199 }, (short)310, (short)1, 0, 9072000L, 110, 110, 180, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.belltower.", 45.0F, 36500, (byte)14, 100000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  672 */     createItemTemplate(713, "pylon", "pylons", "excellent", "good", "ok", "poor", "This monumental structure is said to represent two hills between which the sun rises and sets. It is commonly associated with recreation and birth.", new short[] { 25, 49, 31, 52, 44, 67, 109, 195, 194, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 300, 500, 1500, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.pylon.", 55.0F, 2000000, (byte)15, 10000, false);
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
/*  688 */     createItemTemplate(714, "obelisk", "obelisk", "excellent", "good", "ok", "poor", "A large obelisk. These monuments represent the sun.", new short[] { 25, 49, 31, 52, 44, 67, 109, 178, 194, 195, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.obelisque.", 40.0F, 1500000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     createItemTemplate(736, "pillar", "pillar", "excellent", "good", "ok", "poor", "A round beautiful pillar. It can be used to support a roof but many consider it a piece of art in itself.", new short[] { 25, 31, 52, 44, 67, 109, 51, 86, 178 }, (short)60, (short)1, 0, 12096000L, 100, 100, 500, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.pillar.", 20.0F, 1500000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  705 */     createItemTemplate(717, "foundation pillar", "foundation pillars", "excellent", "good", "ok", "poor", "A pillar that is as powerful as it is beautiful. It seems to have some religious or symbolic meaning.", new short[] { 25, 49, 31, 52, 44, 67, 195, 194, 109, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 500, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.pillar.foundation.", 20.0F, 1500000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  715 */     createItemTemplate(716, "spirit gate", "spirit gate", "excellent", "good", "ok", "poor", "These spirit gates marks the transition between the normal world and the sacred. They are usually placed near the entrance to shrines.", new short[] { 25, 49, 31, 52, 44, 67, 109, 195, 194, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 400, 500, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.spiritgate.", 55.0F, 500000, (byte)15, 10000, false);
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
/*  731 */     createItemTemplate(712, "shrine", "shrine", "excellent", "good", "ok", "poor", "A small shrine for worshipping spirits, large enough to enter but not much more.", new short[] { 25, 49, 31, 52, 44, 67, 195, 194, 109, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 400, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.shrine.", 25.0F, 1500000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  739 */     createItemTemplate(715, "temple", "temple", "excellent", "good", "ok", "poor", "A small temple for worshipping deities, large enough to enter but not much more.", new short[] { 25, 49, 31, 52, 44, 67, 195, 194, 109, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 400, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.temple.", 35.0F, 1500000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  747 */     createItemTemplate(726, "ring center", "ring centers", "excellent", "good", "ok", "poor", "This is the center stone of the duelling ring.", new short[] { 25, 49, 31, 52, 40, 67, 98, 123, 48, 109, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.duelring.", 139.0F, 100000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  754 */     createItemTemplate(727, "duelling ring", "duelling rings", "excellent", "good", "ok", "poor", "Within this ring of stones may the fate of kings and heroes be decided.", new short[] { 25, 49, 31, 52, 40, 98, 48, 123, 109, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.duelring.side.", 139.0F, 100000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  763 */     createItemTemplate(728, "ring corner", "ring corners", "excellent", "good", "ok", "poor", "Within this ring of stones may the fate of kings and heroes be decided.", new short[] { 25, 49, 31, 52, 40, 98, 48, 109, 123, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.duelring.corner.", 139.0F, 100000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  771 */     createItemTemplate(731, "tree stump", "stumps", "fresh", "dry", "brittle", "rotten", "Apparently, this is where a tree used to grow.", new short[] { 21, 113, 52, 129, 31, 123, 86, 237 }, (short)606, (short)1, 20, 86401L, 40, 40, 200, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.stump.", 200.0F, 200000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  779 */     createItemTemplate(739, "Hota pillar", "hota pillars", "excellent", "good", "ok", "poor", "This pillar is part of a game invented by the gods. When you control 4 pillars your side wins.", new short[] { 25, 49, 31, 52, 40, 67, 109, 123, 92, 48, 114, 32, 156, 60, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 500, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.pillar.hota.", 39.0F, 1500000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  788 */     createItemTemplate(740, "medallion", "medallions", "new", "fancy", "ok", "old", "This medallion is the prize of a Hota tournament.", new short[] { 52, 22 }, (short)464, (short)1, 0, Long.MAX_VALUE, 3, 3, 4, -10, new byte[] { 29, 2, 36 }, "model.decoration.medallion.hota.", 10.0F, 70, (byte)7, 4000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  795 */     createItemTemplate(741, "shrine of the rush", "rush shrines", "excellent", "good", "ok", "poor", "A small shrine that inhabit speedy water spirits who may invigorate you.", new short[] { 49, 31, 52, 67, 98, 109, 123, 40, 114, 60, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 400, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.shrine.", 66.0F, 1500000, (byte)0, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  804 */     createItemTemplate(742, "hota statue", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "This statue is the prize of a Hota tournament.", new short[] { 108, 22, 51, 52, 40, 67, 92, 1, 32, 48, 176, 199, 178, 259 }, (short)467, (short)1, 0, 12096000L, 40, 40, 260, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "hota.", 5.0F, 150000, (byte)7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  813 */     createItemTemplate(757, 5, "oil barrel", "oil barrel", "almost full", "somewhat occupied", "half-full", "emptyish", "A large closed wooden barrel with a tap, designed to hold lots of oil.", new short[] { 108, 135, 144, 31, 1, 21, 86, 33, 51, 52, 44, 92, 109, 178, 176, 259 }, (short)245, (short)1, 0, 9072000L, 100, 150, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.barrel.huge.oil.", 35.0F, 60000, (byte)14, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  823 */     createItemTemplate(758, "bow rack", "bow racks", "almost full", "somewhat occupied", "half-full", "emptyish", "A purposeful bow rack that will hold most types of bows.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 135, 176, 178, 199 }, (short)60, (short)1, 0, 9072000L, 10, 20, 201, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.weapons.bows.", 25.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  832 */     createItemTemplate(759, "armour stand", "armour stands", "almost full", "somewhat occupied", "half-full", "emptyish", "An armour stand used to keep your armour.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 135, 176, 178, 199 }, (short)60, (short)1, 0, 9072000L, 10, 40, 45, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.weapons.armour.", 20.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  841 */     createItemTemplate(760, "outpost", "outposts", "excellent", "good", "ok", "poor", "Some warriors have set up camp here.", new short[] { 52, 21, 31, 67, 40, 49, 98, 48, 161, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.", 100.0F, 500000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  848 */     createItemTemplate(761, "battle camp", "battle camps", "excellent", "good", "ok", "poor", "A group of fighters see the strategic value of this place.", new short[] { 52, 21, 31, 67, 40, 49, 98, 48, 161, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.capturetower.", 100.0F, 500000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  856 */     createItemTemplate(762, "fortification", "fortification", "excellent", "good", "ok", "poor", "This place is apparently well worth defending.", new short[] { 52, 21, 31, 67, 40, 49, 98, 48, 161, 178, 123 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.", 100.0F, 500000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  864 */     createItemTemplate(766, "source fountain", "source fountains", "almost full", "somewhat occupied", "half-full", "emptyish", "An ancient fountain has been built on top of a Source spring. It has a pinkish tint from the mysterious substance.", new short[] { 31, 40, 25, 52, 67, 1, 33, 128, 123, 162, 178 }, (short)60, (short)1, 0, 12096000L, 10, 20, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.fountain.source.", 40.0F, 120000, (byte)15);
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
/*  879 */     createItemTemplate(767, "source spring", "source springs", "almost full", "somewhat occupied", "half-full", "emptyish", "A few stones glisten from the pinkish liquid known as the Source which emanates here.", new short[] { 31, 40, 25, 52, 67, 1, 33, 128, 123, 162, 178 }, (short)60, (short)1, 0, 12096000L, 10, 20, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.spring.source.", 40.0F, 120000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  887 */     createItemTemplate(764, "source salt", "source salt", "excellent", "good", "ok", "poor", "This pink salt comes from the Source, which is believed to be the essence of the future.", new short[] { 5, 55, 146, 163, 46 }, (short)643, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.salt.source.", 1.0F, 1, (byte)21, 300, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  893 */     createItemTemplate(765, "source crystal", "source crystals", "excellent", "good", "ok", "poor", "This is a translucent pink, glittering crystal containing the Source - the essense of the future.", new short[] { 93, 163 }, (short)542, (short)1, 0, Long.MAX_VALUE, 1, 2, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.source.", 99.0F, 30, (byte)21, 300, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  900 */     createItemTemplate(763, "source", "source", "excellent", "good", "ok", "poor", "This is the rare light pink liquid known as the Source. It is said to contain the essence of the future.", new short[] { 26, 88, 90, 163 }, (short)540, (short)1, 0, Long.MAX_VALUE, 1, 2, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.source.", 99.0F, 10, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  908 */     createItemTemplate(768, 2, "wine barrel", "wine barrels", "almost full", "somewhat occupied", "half-full", "emptyish", "A small, carefully crafted wooden barrel which can be used for fermenting liquids.", new short[] { 108, 135, 51, 1, 21, 33, 86, 52, 147, 44, 92, 77, 67, 178, 199, 236, 259 }, (short)245, (short)1, 0, 9072000L, 30, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.winebarrel.small.", 50.0F, 2000, (byte)14, 30000, true, 0);
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
/*  920 */     createItemTemplate(756, "thatch", "bunches of thatch", "excellent", "good", "ok", "poor", "A sheaf of grass stems, aligned together and of similar length.", new short[] { 186, 146, 46, 158 }, (short)599, (short)1, 0, 9072000L, 1, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.thatch.", 5.0F, 50, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  928 */     createItemTemplate(774, "leggat", "leggats", "excellent", "good", "ok", "poor", "A hammer looking item with a handle and a flat surface mounted with flattened nails. It is used to create thatch from reed.", new short[] { 108, 44, 38, 21, 14, 119, 147 }, (short)862, (short)1, 20, 3024000L, 3, 10, 30, 10026, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.leggat.", 1.0F, 1200, (byte)14, 1400, false);
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
/*  943 */     createItemTemplate(775, "staircase", "staircases", "almost full", "somewhat occupied", "half-full", "emptyish", "A staircase made from wooden planks and nails, intended for stepping on.", new short[] { 108, 135, 31, 21, 51, 86, 52, 44 }, (short)60, (short)1, 0, 9072000L, 100, 200, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.staircase.simple.", 12.0F, 35000, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     createItemTemplate(773, "sheet", "iron sheets", "excellent", "good", "ok", "poor", "A thin iron sheet.", new short[] { 22, 146, 158 }, (short)675, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  958 */     createItemTemplate(772, "sheet", "copper sheets", "excellent", "good", "ok", "poor", "A thin copper sheet.", new short[] { 22, 146, 158 }, (short)675, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)10, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  964 */     createItemTemplate(1298, "sheet", "tin sheets", "excellent", "good", "ok", "poor", "A thin tin sheet.", new short[] { 22, 146, 158 }, (short)1447, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)34, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  970 */     createItemTemplate(1299, "sheet", "lead sheets", "excellent", "good", "ok", "poor", "A thin lead sheet.", new short[] { 22, 146, 158 }, (short)1446, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)12, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  976 */     createItemTemplate(769, "clay brick", "clay brick", "excellent", "good", "ok", "poor", "A clay brick that could be hardened by fire.", new short[] { 108, 146, 113, 129, 196 }, (short)574, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 3.0F, 5000, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  983 */     createItemTemplate(776, "pottery brick", "pottery bricks", "excellent", "good", "ok", "poor", "A pottery brick hardened by fire.", new short[] { 108, 30, 92, 147, 146, 242, 243 }, (short)575, (short)1, 0, 12096000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.pottery", 3.0F, 5000, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  991 */     createItemTemplate(779, 3, "blue cloth hood", "blue cloth hoods", "excellent", "good", "ok", "poor", "A hood to shelter you from poor weather, glaring sun and glaring eyes.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.blue.", 10.0F, 300, (byte)17, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  998 */     createItemTemplate(1425, 3, "white cloth hood", "white cloth hoods", "excellent", "good", "ok", "poor", "A hood to shelter you from poor weather, glaring sun and glaring eyes.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.", 10.0F, 300, (byte)17, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1005 */     createItemTemplate(781, "hand mirror", "hand mirror", "excellent", "good", "ok", "poor", "Using this shiny metal mirror, you can change your appearance.", new short[] { 22, 40, 42, 53, 127 }, (short)920, (short)1, 10, 3024000L, 1, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.handmirror.", 100.0F, 100, (byte)8, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     createItemTemplate(1300, "golden mirror", "golden mirrors", "excellent", "good", "ok", "poor", "Using this shiny golden mirror, you can change your gender and appearance.", new short[] { 22, 40, 53, 127 }, (short)921, (short)1, 10, 3024000L, 1, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.handmirror.", 100.0F, 100, (byte)7, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1017 */     createItemTemplate(782, "concrete", "concrete", "excellent", "good", "ok", "poor", "Concrete, for instance used to raise cave floors.", new short[] { 25, 146, 46, 112, 113, 158, 247 }, (short)591, (short)1, 0, 18000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.concrete.", 25.0F, 3000, (byte)18, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1023 */     createItemTemplate(770, "shards", "slate shards", "superb", "good", "ok", "poor", "Lots of different sized slate shards.", new short[] { 25, 146, 46, 112, 113, 129, 48, 175 }, (short)610, (short)46, 0, 604800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.", 1.0F, 20000, (byte)61, 5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1029 */     createItemTemplate(785, "shards", "marble shards", "superb", "good", "ok", "poor", "Lots of different sized marble shards.", new short[] { 25, 146, 46, 112, 113, 129, 48, 175 }, (short)610, (short)46, 0, 604800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.", 1.0F, 20000, (byte)62, 5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1036 */     createItemTemplate(786, "marble brick", "marble bricks", "excellent", "good", "ok", "poor", "Marble chiselled into a cubic shape.", new short[] { 25, 146, 113, 129, 158, 242, 243 }, (short)670, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 5.0F, 15000, (byte)62, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     createItemTemplate(784, "slate shingle", "slate shingles", "excellent", "good", "ok", "poor", "Slate chiselled into a flat shingle.", new short[] { 25, 146, 113, 129, 158 }, (short)670, (short)1, 0, 3024000L, 30, 30, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 1.0F, 7500, (byte)61, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1049 */     createItemTemplate(787, "marble slab", "marble slabs", "almost full", "somewhat occupied", "half-full", "emptyish", "A flat and square marble slab. It is about your length and width.", new short[] { 25, 51, 135, 86, 44, 146, 158, 242, 243 }, (short)689, (short)1, 0, 12096000L, 10, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.slab.", 20.0F, 80000, (byte)62);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1057 */     createItemTemplate(771, "slate slab", "slate slabs", "almost full", "somewhat occupied", "half-full", "emptyish", "A flat and square slate slab. It is about your length and width.", new short[] { 25, 51, 135, 86, 44, 146, 158, 242, 243 }, (short)689, (short)1, 0, 12096000L, 10, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.slab.", 3.0F, 80000, (byte)61);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1065 */     createItemTemplate(788, "smelting pot", "smelting pots", "excellent", "good", "ok", "poor", "A ceramic smelting pot, used to smelt metal.", new short[] { 108, 30, 92, 147 }, (short)511, (short)1, 0, 12096000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.smeltingpot.", 5.0F, 500, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1071 */     createItemTemplate(789, "clay smelting pot", "clay smelting pot", "excellent", "good", "ok", "poor", "An unfinished smelting pot that could be hardened by fire.", new short[] { 108, 196, 44, 147 }, (short)491, (short)1, 0, 172800L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.smeltingpot.", 40.0F, 500, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1077 */     createItemTemplate(777, "clay shingle", "clay shingles", "excellent", "good", "ok", "poor", "Clay shaped into a flat shingle.", new short[] { 25, 146, 113, 129, 196, 158 }, (short)576, (short)1, 0, 3024000L, 30, 30, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 1.0F, 2000, (byte)18, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1084 */     createItemTemplate(778, "pottery shingle", "pottery shingles", "excellent", "good", "ok", "poor", "Pottery shaped into a flat shingle.", new short[] { 25, 146, 113, 129, 158 }, (short)577, (short)1, 0, 3024000L, 30, 30, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.shingle.", 1.0F, 2000, (byte)19, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1091 */     createItemTemplate(790, "wood shingle", "wood shingles", "excellent", "good", "ok", "poor", "Wood carved into a flat shingle.", new short[] { 25, 146, 113, 129, 158 }, (short)626, (short)1, 0, 3024000L, 30, 30, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.shingle.", 10.0F, 250, (byte)14, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1098 */     createItemTemplate(791, 3, "soft cap", "caps", "excellent", "good", "ok", "poor", "A fine soft cap that has a strange shimmer.", new short[] { 108, 44, 24, 4, 92 }, (short)963, (short)1, 0, 3024000L, 1, 10, 10, -10, new byte[] { 1, 28 }, "model.clothing.head.cap.soft.", 10.0F, 30, (byte)17, 1, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1106 */     createItemTemplate(792, "sacrificial knife", "sacrificial knives", "excellent", "good", "ok", "poor", "A curved, sharp knife with a long decorated blade inset with gems.", new short[] { 108, 44, 38, 22, 17, 13, 147 }, (short)1379, (short)1, 1, 3024000L, 1, 3, 21, 10029, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.knife.sacrificial.", 30.0F, 1230, (byte)8, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1113 */     createItemTemplate(793, "sacrificial knife blade", "sacrificial knife blades", "excellent", "good", "ok", "poor", "The bent blade of a sacrificial knife.", new short[] { 44, 22 }, (short)735, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.knife.sacrificial.", 20.0F, 1100, (byte)8, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1119 */     createItemTemplate(806, "libram of the night", "night librams", "new", "fancy", "ok", "old", "A black leather book with an etched silver owl, to be read by the wicked and twisted.", new short[] { 53, 168 }, (short)330, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.libramnight.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1125 */     createItemTemplate(794, "key of the heavens", "heaven keys", "superb", "good", "ok", "poor", "A small golden puzzle covered with unintelligible movable bricks.", new short[] { 53, 168, 48 }, (short)789, (short)1, 0, Long.MAX_VALUE, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.keyheavens.", 100.0F, 150, (byte)7, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1131 */     createItemTemplate(795, "blood of the angels", "angel bloods", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A crystal vial containing a shining liquid.", new short[] { 53, 32, 156, 168, 40 }, (short)260, (short)1, 0, Long.MAX_VALUE, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.bloodang.", 100.0F, 200, (byte)52, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1140 */     createItemTemplate(796, "smoke from sol", "sol smoke vials", "superb", "good", "ok", "poor", "A crystal vial containing the vicious fumes of Sol that twisted the demons and fuels their madness.", new short[] { 53, 168 }, (short)260, (short)1, 0, Long.MAX_VALUE, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.smokesol.", 100.0F, 200, (byte)52, 100000, true);
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
/* 1151 */     createItemTemplate(797, "uttacha slime", "uttacha slimes", "superb", "good", "ok", "poor", "This crystal vial contains the paralyzing slime of Uttacha, a giant slug living leagues deep in the Shaded Depths of Valrei.", new short[] { 53, 168 }, (short)260, (short)1, 0, Long.MAX_VALUE, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.slimeutt.", 10.0F, 200, (byte)52, 100000, true);
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
/* 1167 */     createItemTemplate(798, "red tome of magic", "tomes of magic", "new", "fancy", "ok", "old", "The red leather covers will reveal the secrets of high magic for the one who dares open them.", new short[] { 53, 168 }, (short)325, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.tomemagic.red.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1174 */     createItemTemplate(809, "blue tome of magic", "tomes of magic", "new", "fancy", "ok", "old", "The blue leather covers will reveal the secrets of high magic for the one who dares open them.", new short[] { 53, 168 }, (short)326, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.tomemagic.blue.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1181 */     createItemTemplate(808, "black tome of magic", "tomes of magic", "new", "fancy", "ok", "old", "The black leather covers will reveal the secrets of high magic for the one who dares open them.", new short[] { 53, 168 }, (short)328, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.tomemagic.black.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1189 */     createItemTemplate(807, "green tome of magic", "tomes of magic", "new", "fancy", "ok", "old", "The green leather covers will reveal the secrets of high magic for the one who dares open them.", new short[] { 53, 168 }, (short)327, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.tomemagic.green.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1197 */     createItemTemplate(810, "white tome of magic", "tomes of magic", "new", "fancy", "ok", "old", "The white leather covers will reveal the secrets of high magic for the one who dares open them.", new short[] { 53, 168 }, (short)329, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.tomemagic.white.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1205 */     createItemTemplate(799, "scroll of binding", "scrolls of binding", "new", "fancy", "ok", "old", "A parchment roll inscribed with symbols that glow faintly as you eye them. You have a hard time refraining from reading as you feel that it will change your inner being.", new short[] { 53, 168 }, (short)331, (short)1, 0, Long.MAX_VALUE, 3, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.scrollbind.", 1000.0F, 100, (byte)33, 100000, true);
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
/* 1219 */     createItemTemplate(800, "white cherry", "white cherries", "delicious", "nice", "old", "rotten", "This giant cherry was almost ripe when it was picked from the tree on Valrei, home of the gods. God food that may contain mystical powers!", new short[] { 53, 168 }, (short)60, (short)1, 0, Long.MAX_VALUE, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.cherry.white.", 100.0F, 200, (byte)22, 100000, true);
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
/* 1234 */     createItemTemplate(801, "red cherry", "red cherries", "delicious", "nice", "old", "rotten", "This giant cherry was fully ripe when it was picked from the tree on Valrei, home of the gods. It may take you there to join the deities!", new short[] { 53, 168 }, (short)713, (short)1, 0, Long.MAX_VALUE, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.cherry.red.", 100.0F, 200, (byte)22, 100000, true);
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
/* 1249 */     createItemTemplate(802, "green cherry", "green cherries", "delicious", "nice", "old", "rotten", "This giant cherry wasn't mature when it was picked from the tree on Valrei, home of the gods. God food that may contain strange powers!", new short[] { 53, 168 }, (short)60, (short)1, 0, Long.MAX_VALUE, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.cherry.green.", 100.0F, 200, (byte)22, 100000, true);
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
/* 1264 */     createItemTemplate(803, "giant walnut", "giant walnut", "delicious", "nice", "old", "rotten", "This giant walnut has been picked on Valrei, home of the gods. Strange powers may dwell within!", new short[] { 53, 168 }, (short)579, (short)1, 0, Long.MAX_VALUE, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.walnut.", 100.0F, 200, (byte)22, 100000, true);
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
/* 1277 */     createItemTemplate(1009, "rod of eruption", "rods of eruption", "superb", "good", "ok", "poor", "A thick red rod carved from an unusual stone material. A symbol of a volcano is inlaid on the end that doesn't go into the ground. Should probably be planted with extreme care.", new short[] { 53, 168, 48, 143, 59, 199, 52 }, (short)60, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.roderuption.", 10.0F, 5, (byte)15, 100000, true);
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
/* 1292 */     createItemTemplate(805, "wand of the seas", "wands of the seas", "superb", "good", "ok", "poor", "A blue colored wooden stick with a bunch of green leaves in a fan shape on one end. Symbols indicate that if you plant it at sea, an island will emerge!", new short[] { 53, 168, 48, 143, 59, 199, 52 }, (short)60, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.wandseas.", 10.0F, 5, (byte)38, 100000, true);
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
/* 1307 */     createItemTemplate(811, "statue of horse", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A statue of a proud stallion.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.horse.", 15.0F, 70000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1316 */     createItemTemplate(812, "clay flowerpot", "clay flowerpots", "excellent", "good", "ok", "poor", "A clay flowerpot that could be hardened by fire.", new short[] { 108, 196, 44, 147 }, (short)490, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpot.", 15.0F, 600, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1323 */     createItemTemplate(813, "pottery flowerpot", "pottery flowerpots", "excellent", "good", "ok", "poor", "A clay flowerpot hardened by fire.", new short[] { 108, 30 }, (short)510, (short)47, 0, 12096000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpot.", 15.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1331 */     createItemTemplate(815, "blue flowerpot", "blue flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some crooked but beautiful blue flowers.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotBlue.", 50.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1341 */     createItemTemplate(814, "yellow flowerpot", "yellow flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some yellow prickly flowers.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotYellow.", 15.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1351 */     createItemTemplate(819, "greenish-yellow flowerpot", "greenish-yellow flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some greenish-yellow furry flowers.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotGreenish.", 60.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1361 */     createItemTemplate(818, "orange-red flowerpot", "orange-red flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some long-stemmed orange-red flowers with thick, pointy leaves.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotOrange.", 25.0F, 600, (byte)19, 10000, true);
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
/* 1372 */     createItemTemplate(816, "purple flowerpot", "purple flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some purple fluffy flowers.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotPurple.", 30.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1382 */     createItemTemplate(817, "white flowerpot", "white flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some thick-stemmed white flowers with long leaves.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotWhite.", 35.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1392 */     createItemTemplate(820, "white-dotted flowerpot", "white-dotted flowerpots", "excellent", "good", "ok", "poor", "A flowerpot with some uncommon white-dotted flowers.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flowerpotDotted.", 70.0F, 600, (byte)19, 10000, true);
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
/* 1403 */     createItemTemplate(821, "gravestone", "gravestones", "excellent", "good", "ok", "poor", "A gravestone to mark the final resting place of someone important.", new short[] { 25, 113, 144, 86, 119, 44 }, (short)335, (short)48, 0, 12096000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gravestone.", 30.0F, 20000, (byte)15, 8, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1411 */     createItemTemplate(822, "gravestone", "gravestones", "excellent", "good", "ok", "poor", "A gravestone to mark the final resting place of someone important.", new short[] { 25, 31, 144, 86, 159, 51, 52, 44, 178 }, (short)335, (short)48, 0, 12096000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gravestone.buried.", 30.0F, 20000, (byte)15, 8, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1421 */     createItemTemplate(824, "group", "groups", "almost full", "somewhat occupied", "half-full", "emptyish", "A group to help organize your things.", new short[] { 1, 112, 42, 31, 61, 178 }, (short)20, (short)49, 0, Long.MAX_VALUE, 400, 400, 4000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1430 */     createItemTemplate(827, "diamond staff", "staves", "fresh", "dry", "brittle", "rotten", "A long, well polished staff with a fantastic diamond attached.", new short[] { 133, 21, 14, 154, 144, 37, 84, 129, 172 }, (short)646, (short)1, 35, 9072000L, 3, 4, 200, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.diamond.", 10.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1439 */     createItemTemplate(828, "opal staff", "staves", "fresh", "dry", "brittle", "rotten", "A long, well polished staff with a fantastic opal attached.", new short[] { 133, 21, 14, 154, 144, 37, 84, 129, 172 }, (short)646, (short)1, 35, 9072000L, 3, 4, 200, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.opal.", 10.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1448 */     createItemTemplate(826, "ruby staff", "staves", "fresh", "dry", "brittle", "rotten", "A long, well polished staff with a fantastic ruby attached.", new short[] { 133, 21, 14, 154, 144, 37, 84, 129, 172 }, (short)646, (short)1, 35, 9072000L, 3, 4, 200, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.ruby.", 10.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1457 */     createItemTemplate(829, "emerald staff", "staves", "fresh", "dry", "brittle", "rotten", "A long, well polished staff with a fantastic emerald attached.", new short[] { 133, 21, 14, 154, 144, 37, 84, 129, 172 }, (short)646, (short)1, 35, 9072000L, 3, 4, 200, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.emerald.", 10.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1466 */     createItemTemplate(825, "sapphire staff", "staves", "fresh", "dry", "brittle", "rotten", "A long, well polished staff with a fantastic sapphire attached.", new short[] { 133, 21, 14, 154, 144, 37, 84, 129, 172 }, (short)646, (short)1, 35, 9072000L, 3, 4, 200, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.sapphire.", 10.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1476 */     createItemTemplate(830, "fragile arrow", "fragile arrows", "excellent", "good", "ok", "poor", "An slender arrow with a piercing tip designed to inflict massive damage and then break.", new short[] { 21, 45, 59 }, (short)1215, (short)1, 0, 1L, 1, 1, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.war.", 15.0F, 140, (byte)14, 0, false);
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
/* 1490 */     createItemTemplate(823, "equipmentslot", "equipmentslots", "superb", "strong", "normal", "Weak", "A place to put equipment.", new short[] { 170, 8, 1 }, (short)60, (short)10, 0, Long.MAX_VALUE, 1, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1497 */     createItemTemplate(831, "kingdom tabard", "kingdom tabards", "excellent", "good", "ok", "poor", "A tabard that is worn to show off which kingdom you belong to.", new short[] { 24, 44 }, (short)307, (short)1, 0, 3024000L, 30, 30, 5, -10, new byte[] { 35 }, "model.clothing.tabard.", 25.0F, 300, (byte)17);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1504 */     createItemTemplate(832, "walnut", "walnuts", "delicious", "nice", "old", "rotten", "The corrugated seed of a walnut tree.", new short[] { 5, 21, 146, 80, 212, 217, 74 }, (short)579, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.walnut.", 1.0F, 200, (byte)22, 8, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1513 */       .setNutritionValues(6540, 137, 652, 152)
/* 1514 */       .setFoodGroup(1197);
/*      */     
/* 1516 */     createItemTemplate(833, "chestnut", "chestnuts", "delicious", "nice", "old", "rotten", "A fruit from the chestnut tree, it has a pointed end with a small tuft at its tip.", new short[] { 5, 21, 146, 80, 212, 74 }, (short)559, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.chestnut.", 1.0F, 200, (byte)22, 8, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1525 */       .setNutritionValues(2000, 440, 13, 16)
/* 1526 */       .setFoodGroup(1197);
/*      */     
/* 1528 */     createItemTemplate(834, "yellow potion", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A round flask containing liquid with a really funny strong smell. Maybe something equally fun will happen if you quaff it?", new short[] { 6 }, (short)260, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.yellow.", 200.0F, 1, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1538 */     createItemTemplate(835, 3, "village recruitment board", "village recruitment boards", "fresh", "dry", "brittle", "rotten", "A board where you can find villages that are looking for new residents.", new short[] { 21, 142, 51, 52, 44, 18, 199, 86, 178, 48 }, (short)252, (short)1, 20, 9072000L, 1, 5, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.recruitment.", 10.0F, 5000, (byte)14, 5000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1548 */     createItemTemplate(836, "brown potion", "potions", "faintly glowing", "barely glowing", "almost glowing", "not glowing", "A diamond shaped brown flask containing some sort of horrible concoct.", new short[] { 6 }, (short)260, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.brown.", 200.0F, 1, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1555 */     createItemTemplate(837, "lump", "seryll lumps", "excellent", "good", "ok", "poor", "Seryll - this reddish metal is believed to exist naturally only on Seris, and be delivered here by envoys from the gods.", new short[] { 22, 146, 46, 113, 157 }, (short)630, (short)1, 0, 3024000L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 80.0F, 1000, (byte)67, 100, true);
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
/* 1569 */     createItemTemplate(838, 3, "brazier stand", "copper brazier stands", "fresh", "dry", "brittle", "rotten", "A stand for braziers.", new short[] { 22, 44, 119 }, (short)557, (short)1, 0, 3024000L, 1, 10, 2, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.brazier.stand.", 60.0F, 400, (byte)10, 500, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1578 */     createItemTemplate(839, 3, "brazier bowl", "copper brazier bowls", "fresh", "dry", "brittle", "rotten", "A bowl for a brazier.", new short[] { 22, 44, 119 }, (short)302, (short)1, 0, 3024000L, 20, 20, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.brazier.bowl.", 60.0F, 3000, (byte)10, 500, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1587 */     createItemTemplate(841, 3, "small brazier", "small braziers", "fresh", "dry", "brittle", "rotten", "A brazier made from copper.", new short[] { 22, 52, 44, 119, 32, 101, 179, 199, 51 }, (short)301, (short)1, 0, 3024000L, 20, 20, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.brazier.", 60.0F, 4200, (byte)10, 5000, true, 0);
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
/* 1598 */     createItemTemplate(840, 4, "brazier bowl", "large gold brazier bowls", "fresh", "dry", "brittle", "rotten", "A bowl for a brazier.", new short[] { 22, 44, 119 }, (short)302, (short)1, 0, 3024000L, 20, 20, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.large.brazier.bowl.", 60.0F, 5000, (byte)7, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1607 */     createItemTemplate(842, 3, "marble brazier pillar", "marble brazier pillars", "fresh", "dry", "brittle", "rotten", "A pillar with a golden brazier on the top.", new short[] { 25, 44, 119, 52, 199, 176, 32, 178, 101, 179, 51, 86 }, (short)60, (short)1, 0, 12096000L, 100, 122, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.brazier.pillar.", 60.0F, 855000, (byte)62, 10000, true, 0);
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
/* 1618 */     createItemTemplate(843, "name change certificate", "name change certificate", "new", "fancy", "ok", "old", "A form to fill out for changing your name.", new short[] { 40, 42, 53, 54 }, (short)340, (short)1, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.namecert.", 100.0F, 0, (byte)33, 500000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1624 */     createItemTemplate(847, "brown bear rug", "rugs", "excellent", "good", "ok", "poor", "A thick brown bear rug.", new short[] { 23, 44, 119, 52, 173, 51, 184 }, (short)603, (short)1, 0, 9072000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.brown.bear.rug.", 50.0F, 3000, (byte)55, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1634 */     createItemTemplate(846, "black bear rug", "rugs", "excellent", "good", "ok", "poor", "A thick black bear rug.", new short[] { 23, 44, 119, 52, 173, 51, 184 }, (short)603, (short)1, 0, 9072000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.black.bear.rug.", 50.0F, 3000, (byte)55, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1644 */     createItemTemplate(848, "mountain lion rug", "rugs", "excellent", "good", "ok", "poor", "A thick mountain lion rug.", new short[] { 23, 44, 119, 52, 173, 51, 184 }, (short)603, (short)1, 0, 9072000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.mountain.lion.rug.", 50.0F, 400, (byte)55, 20000, true);
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
/* 1655 */     createItemTemplate(849, "black wolf rug", "rugs", "excellent", "good", "ok", "poor", "A thick black wolf rug.", new short[] { 23, 44, 119, 52, 173, 51, 184 }, (short)603, (short)1, 0, 9072000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.black.wolf.rug.", 50.0F, 3000, (byte)55, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1665 */     createItemTemplate(850, 3, "wagon", "wagons", "almost full", "somewhat occupied", "half-full", "emptyish", "A fairly large wagon designed to be dragged by four animals.", new short[] { 108, 1, 31, 21, 51, 52, 44, 117, 193, 134, 47, 48, 176, 180, 160, 54 }, (short)60, (short)41, 0, 9072000L, 550, 300, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.transports.medium.wagon.", 70.0F, 240000, (byte)14, 50000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1675 */       .setContainerSize(200, 260, 400);
/* 1676 */     createItemTemplate(852, "large crate", "crates", "almost full", "somewhat occupied", "half-full", "emptyish", "A large crate made from planks, primarily used to transport goods.", new short[] { 108, 135, 21, 48, 51, 52, 44, 1, 92, 145, 176 }, (short)311, (short)1, 0, 9072000L, 120, 120, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.crate.large.", 50.0F, 20000, (byte)14, 10000, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1684 */       .setDyeAmountGrams(2500);
/* 1685 */     createItemTemplate(851, "small crate", "crates", "almost full", "somewhat occupied", "half-full", "emptyish", "A small crate made from planks, primarily used to transport goods.", new short[] { 108, 135, 21, 48, 51, 52, 44, 1, 92, 145, 176 }, (short)312, (short)1, 0, 9072000L, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.crate.small.", 20.0F, 20000, (byte)14, 10000, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1693 */       .setDyeAmountGrams(1500);
/* 1694 */     createItemTemplate(844, "snow lantern", "snow lanterns", "superb", "good", "ok", "poor", "Expel the darkness with this +2 lantern, gives +4 to jollyness checks during winter.", new short[] { 108, 52, 32, 51 }, (short)60, (short)1, 0, Long.MAX_VALUE, 5, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.snowlantern.seasoncycle.", 20.0F, 2500, (byte)21, 70000, true);
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
/* 1710 */     createItemTemplate(845, "water marker", "markers", "superb", "good", "ok", "poor", "A watery cube.", new short[] { 45, 40, 52, 92 }, (short)540, (short)1, 0, Long.MAX_VALUE, 80, 80, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.marker.temporary.water", 3.0F, 0, (byte)26);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1715 */     createItemTemplate(853, 3, "ship transporter", "transporters", "almost full", "somewhat occupied", "half-full", "emptyish", "A cart designed to transport ships over land.", new short[] { 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 54, 134, 193, 47, 160 }, (short)60, (short)41, 0, 9072000L, 130, 120, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.transports.large.ship.transporter.", 30.0F, 240000, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1725 */       .setDyeAmountGrams(15000);
/* 1726 */     createItemTemplate(1410, 3, "creature transporter", "transporters", "almost full", "somewhat occupied", "half-full", "emptyish", "A cart designed to transport creature cages over land.", new short[] { 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 54, 134, 193, 47, 160 }, (short)60, (short)41, 0, 9072000L, 130, 120, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.transports.large.creature.transporter.", 30.0F, 240000, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1736 */       .setDyeAmountGrams(15000);
/* 1737 */     createItemTemplate(854, 3, "tutorial object", "tutorial objects", "superb", "good", "ok", "poor", "", new short[] { 108, 31, 51, 40, 52, 44, 48 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "tutorial.object.", 30.0F, 240000, (byte)0, 10000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1746 */     createItemTemplate(855, 3, "steel portal", "portals", "superb", "good", "ok", "poor", "", new short[] { 31, 40, 52, 131 }, (short)60, (short)1, 0, Long.MAX_VALUE, 100, 100, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.9.", 30.0F, 240000, (byte)57, 10000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1752 */     createItemTemplate(754, "cooked rice", "rice", "excellent", "good", "ok", "poor", "White sticky, boiled rice.", new short[] { 82, 74, 219, 222 }, (short)608, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.porridge.", 1.0F, 700, (byte)26, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1759 */     createItemTemplate(856, "rice porridge", "porridges", "excellent", "good", "ok", "poor", "Rice cooked in milk.", new short[] { 82, 74, 26, 219 }, (short)608, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.porridge.", 1.0F, 700, (byte)26, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1766 */     createItemTemplate(857, "risotto", "risottos", "excellent", "good", "ok", "poor", "A meal based on rice, vegetables and seafood", new short[] { 82, 76, 219, 222 }, (short)608, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.risotto.", 1.0F, 700, (byte)26, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1773 */     createItemTemplate(858, "rice wine", "rice wines", "excellent", "good", "ok", "poor", "An almost colorless alcoholic beverage popularly served in warm cups. Very hard to do good and does not improve with age.", new short[] { 108, 26, 88, 90, 213, 212 }, (short)582, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.wine.rice.", 70.0F, 1000, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1782 */       .setAlcoholStrength(15);
/*      */     
/* 1784 */     createItemTemplate(859, 4, "chain link", "chain links", "superb", "good", "ok", "poor", "A large chain link.", new short[] { 22, 146, 158 }, (short)470, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.largechain.iron.", 28.0F, 1000, (byte)11, 2000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1791 */     createItemTemplate(860, 3, "wooden beam", "wooden beams", "superb", "good", "ok", "poor", "A wooden beam used in construction.", new short[] { 21, 146, 158, 147 }, (short)553, (short)1, 0, 9072000L, 18, 18, 180, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.beam.", 35.0F, 15000, (byte)14, 8000, false, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1796 */     createItemTemplate(861, "tent", "starter tents", "excellent", "good", "ok", "poor", "A small but useful tent. ", new short[] { 24, 1, 47, 97, 181, 109, 201, 61, 52, 98, 180, 157 }, (short)640, (short)41, 0, 604800L, 5, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.tent.small.", 10.0F, 500, (byte)17, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1804 */       .setContainerSize(100, 200, 201);
/* 1805 */     createItemTemplate(862, "deed stake", "stakes", "fresh", "dry", "brittle", "rotten", "A one step long wooden stake that is used to stake out a settlement, which would provide increased security and reserve the land for your use. Use it on the ground where you wish to settle.", new short[] { 133, 21, 97, 165 }, (short)646, (short)24, 25, 28800L, 3, 7, 50, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.shaft.", 3.0F, 200, (byte)14, 100, false);
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
/* 1819 */     createItemTemplate(863, "explorer tent", "explorer tents", "excellent", "good", "ok", "poor", "A more robust version of the standard tent but without the item protection restrictions.", new short[] { 24, 1, 47, 181, 109, 117, 52, 86, 98, 180 }, (short)640, (short)41, 0, 3024000L, 5, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.tent.small.", 10.0F, 3500, (byte)17, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1829 */       .setContainerSize(100, 200, 201);
/* 1830 */     createItemTemplate(864, "military tent", "military tents", "excellent", "good", "ok", "poor", "This is the standard tent for military actions.", new short[] { 24, 1, 47, 181, 109, 117, 52, 86, 98, 180 }, (short)640, (short)41, 0, 3024000L, 5, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.tent.military.", 10.0F, 3500, (byte)17, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1838 */       .setContainerSize(100, 200, 201);
/* 1839 */     createItemTemplate(865, "pavilion", "pavilions", "excellent", "good", "ok", "poor", "A pleasant open air tent designed for various kinds of gatherings.", new short[] { 24, 47, 109, 52, 86, 51, 98, 180, 182 }, (short)640, (short)1, 0, 3024000L, 5, 5, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.tent.pavilion.", 10.0F, 2500, (byte)17, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1848 */       .setContainerSize(100, 200, 201);
/* 1849 */     createItemTemplate(866, "blood", "blood", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "Blood from special creatures is said to have magic enchanting powers.", new short[] { 6, 48 }, (short)582, (short)1, 0, 12096000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.blood.", 200.0F, 100, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1855 */     createItemTemplate(868, "skull", "skull", "excellent", "good", "ok", "poor", "The skull of a special animal can be a great trophy.", new short[] { 62 }, (short)556, (short)1, 0, Long.MAX_VALUE, 20, 20, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.skull.", 200.0F, 3000, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1860 */     createItemTemplate(867, "strange bone", "bones", "excellent", "good", "ok", "poor", "This looks like the collar bone from some animal. These are said to provide luck.", new short[] { 62 }, (short)536, (short)1, 0, Long.MAX_VALUE, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.collarbone.", 200.0F, 1500, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1866 */     createItemTemplate(869, "Colossus of Vynora", "Colossus", "excellent", "good", "ok", "poor", "A grand statue depicting the avatar of the benevolent diety Vynora.", new short[] { 108, 31, 25, 194, 135, 52, 44, 195, 67, 49, 123, 178, 157 }, (short)60, (short)1, 0, 12096000L, 500, 500, 2000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.colossus.vynora.", 50.0F, 7500000, (byte)15);
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
/* 1878 */     createItemTemplate(870, "Colossus of Magranon", "Colossus", "excellent", "good", "ok", "poor", "A grand statue depicting the avatar of the fierce warrior god Magranon.", new short[] { 108, 31, 25, 194, 135, 52, 44, 195, 67, 49, 123, 178, 157 }, (short)60, (short)1, 0, 12096000L, 500, 500, 2000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.colossus.magranon.", 50.0F, 7500000, (byte)15);
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
/* 1890 */     createItemTemplate(871, "oil of the weapon smith", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a grey oily substance that glows in the dark.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ws.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1897 */     createItemTemplate(872, "potion of the ropemaker", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing an white substance that glows in the dark.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.rm.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1904 */     createItemTemplate(873, "potion of water walking", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing an clear blue substance that glows in the dark.", new short[] { 6 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ww.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1910 */     createItemTemplate(874, "potion of mining", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a dark substance that glows in the dark.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.mi.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1917 */     createItemTemplate(875, "ointment of tailoring", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a syrupy golden substance.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ta.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1924 */     createItemTemplate(876, "oil of the armour smith", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a black thick liquid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.as.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1931 */     createItemTemplate(877, "fletching potion", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a green syrupy liquid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.fl.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1938 */     createItemTemplate(878, "oil of the blacksmith", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a yellow oily fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.bs.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1945 */     createItemTemplate(879, "potion of leatherworking", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a bright yellow smoky fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.lw.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1952 */     createItemTemplate(880, "potion of shipbuilding", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a bright blue smoky fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.sb.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1959 */     createItemTemplate(881, "ointment of stonecutting", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small jar containing an dull grey ointment.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.sc.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1966 */     createItemTemplate(882, "ointment of masonry", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small jar containing an dull black ointment.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ma.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1973 */     createItemTemplate(883, "potion of woodcutting", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing an bright green shining fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.wc.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1980 */     createItemTemplate(884, "potion of carpentry", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing an brown smoky fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ca.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1987 */     createItemTemplate(1413, "potion of butchery", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing an brown smoky fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ca.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1994 */     createItemTemplate(886, "potion of acid", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing a poisonous green smoky fluid.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.ac.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2001 */     createItemTemplate(887, "salve of fire", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small jar containing an orange mixture.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.fi.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2008 */     createItemTemplate(888, "salve of frost", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small jar containing an crystalline white mixture.", new short[] { 6, 183 }, (short)260, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.fr.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2016 */     createItemTemplate(885, 2, "bedside table", "bedside tables", "superb", "good", "ok", "poor", "A small wooden bedside table on four round legs featuring some intricate wooden carvings.", new short[] { 108, 21, 1, 135, 86, 199, 157, 51, 52, 44, 47, 92, 176, 180, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 87, 50, 81, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.bedside.table.", 30.0F, 10000, (byte)14, 10000, false, 0)
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
/* 2027 */       .setContainerSize(30, 30, 50)
/* 2028 */       .setContainerSize(40, 50, 50);
/*      */     
/* 2030 */     createItemTemplate(889, 3, "open fireplace", "fireplaces", "superb", "good", "ok", "poor", "A decorative open fireplace which provides light and warmth to those nearby.", new short[] { 108, 135, 31, 25, 51, 86, 52, 59, 44, 176, 1, 180, 256 }, (short)60, (short)18, 0, 12096000L, 150, 100, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.open.decorative.", 60.0F, 100000, (byte)15, 10000, false, 0)
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
/* 2041 */       .setContainerSize(40, 30, 200);
/*      */     
/* 2043 */     createItemTemplate(890, "canopy bed", "beds", "superb", "good", "ok", "poor", "An inviting canopy bed, fit for a king.", new short[] { 109, 108, 21, 51, 52, 44, 86, 31, 67, 135, 48, 110, 111, 176, 178, 1, 180, 256 }, (short)313, (short)1, 0, 9072000L, 87, 50, 81, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.bed.canopy.", 60.0F, 40000, (byte)14, 20000, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2053 */       .setContainerSize(60, 180, 200);
/*      */     
/* 2055 */     createItemTemplate(891, "bench", "benches", "almost full", "somewhat occupied", "half-full", "emptyish", "A comfortable wooden bench with wrought-iron decorations.", new short[] { 108, 31, 135, 21, 86, 51, 52, 44, 199, 67, 176, 178, 117, 197 }, (short)60, (short)41, 0, 9072000L, 50, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wood.bench.", 40.0F, 90000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2065 */     createItemTemplate(892, 3, "wardrobe", "wardrobes", "almost full", "somewhat occupied", "half-full", "emptyish", "A large wardrobe with wooden inlays.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 199, 176, 178, 180, 47 }, (short)60, (short)1, 0, 9072000L, 120, 80, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wardrobe.", 60.0F, 100000, (byte)14, 10000, false, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2075 */       .setContainerSize(90, 80, 200);
/*      */     
/* 2077 */     createItemTemplate(893, 3, "coffer", "coffers", "almost full", "somewhat occupied", "half-full", "emptyish", "A wooden coffer. Simple yet beautifully decorated.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 47, 176, 178, 199, 180, 259 }, (short)60, (short)1, 0, 9072000L, 80, 70, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.coffer.", 40.0F, 100000, (byte)14, 10000, false, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2087 */       .setContainerSize(30, 50, 200);
/*      */     
/* 2089 */     createItemTemplate(894, "royal throne", "thrones", "superb", "good", "ok", "poor", "A Royal Throne.", new short[] { 109, 108, 21, 51, 52, 44, 86, 67, 135, 178, 117, 197, 199 }, (short)274, (short)41, 0, 9072000L, 75, 75, 185, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.throne.", 60.0F, 30000, (byte)14, 50000, false);
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
/* 2100 */     createItemTemplate(895, 3, "washing bowl", "washing bowls", "almost full", "somewhat occupied", "half-full", "emptyish", "An earthen washing bowl on a tripod made from brass.", new short[] { 22, 44, 1, 86, 52, 67, 51, 33, 178, 199, 180 }, (short)60, (short)1, 0, 3024000L, 40, 40, 74, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.bowl.washing.", 40.0F, 7000, (byte)30, 10000, false, 0)
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
/* 2112 */       .setContainerSize(28, 28, 35);
/*      */     
/* 2114 */     createItemTemplate(896, 2, "tripod table", "tables", "superb", "good", "ok", "poor", "A small round table on three legs.", new short[] { 21, 44, 86, 52, 67, 51, 157, 199, 178, 180, 51, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 70, 70, 84, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tripod.table.", 35.0F, 12000, (byte)14, 10000, false, 0)
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
/* 2127 */       .setContainerSize(40, 50, 50);
/*      */     
/* 2129 */     createItemTemplate(897, "brass ribbon", "brass ribbons", "almost full", "somewhat occupied", "half-full", "emptyish", "A sturdy brass ribbon, used to strengthen constructions like chests and barrels.", new short[] { 22, 146 }, (short)709, (short)1, 0, 3024000L, 1, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ribbon.", 5.0F, 3000, (byte)30, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2136 */     createItemTemplate(900, "crab meat", "crab meat", "excellent", "good", "ok", "poor", "The very nutritious meat from a crab.", new short[] { 28, 5, 62, 129, 146, 76, 219, 212, 223 }, (short)503, (short)1, 0, 172800L, 2, 2, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.meat.", 1.0F, 100, (byte)2, 20, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2142 */       .setNutritionValues(970, 0, 15, 190)
/* 2143 */       .setFoodGroup(1261);
/*      */     
/* 2145 */     createItemTemplate(898, 2, "tortoise shell", "tortoise shells", "excellent", "good", "ok", "poor", "A fine looking turtle shell of good size.", new short[] { 52, 157 }, (short)60, (short)1, 0, 3024000L, 2, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.tortoise.", 10.0F, 600, (byte)35, 1000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2152 */     createItemTemplate(899, 2, "tortoise shield", "tortoise shields", "excellent", "good", "ok", "poor", "A strange shield made from a tortoise shell.", new short[] { 108, 44, 3, 157 }, (short)970, (short)1, 0, 3024000L, 2, 30, 50, 10019, new byte[] { 3, 44 }, "model.shield.tortoise.", 10.0F, 700, (byte)35, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2161 */     createItemTemplate(901, "range pole", "range poles", "excellent", "good", "ok", "poor", "A range pole used in surveying operations, it has bands of same thickness of alternating colours (white and red).", new short[] { 21, 44, 119, 147, 199, 52 }, (short)776, (short)1, 0, 9072000L, 2, 2, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.rangepole.", 20.0F, 100, (byte)14, 4000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2171 */     createItemTemplate(902, "protractor", "protractors", "excellent", "good", "ok", "poor", "A semi circular device to help working out angles.", new short[] { 22, 44, 119, 147 }, (short)422, (short)1, 0, 3024000L, 2, 2, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.protractor.", 50.0F, 50, (byte)30, 2500, true);
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
/* 2182 */     createItemTemplate(903, "dioptra", "dioptras", "excellent", "good", "ok", "poor", "An astronomical and surveying instrument, mounted on a tripod.", new short[] { 22, 44, 119, 147 }, (short)775, (short)1, 0, 3024000L, 30, 30, 180, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.dioptra.", 70.0F, 500, (byte)31, 10000, true);
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
/* 2193 */     createItemTemplate(904, "sight", "sights", "excellent", "good", "ok", "poor", "A rod with a sighting device at both ends.", new short[] { 22, 44, 119, 147 }, (short)421, (short)1, 0, 3024000L, 5, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.sight.", 40.0F, 50, (byte)31, 1000, true);
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
/* 2204 */     createItemTemplate(905, "stone keystone", "stone keystones", "excellent", "good", "ok", "poor", "stone chiselled into a keystone.", new short[] { 25, 51, 135, 86, 44, 158, 147 }, (short)466, (short)1, 0, 12096000L, 10, 400, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.keystone.", 20.0F, 80000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2211 */     createItemTemplate(906, "marble keystone", "marble keystones", "excellent", "good", "ok", "poor", "Marble chiselled into a keystone.", new short[] { 25, 51, 135, 86, 44, 158, 147 }, (short)466, (short)1, 0, 12096000L, 10, 400, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.keystone.", 20.0F, 80000, (byte)62);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2219 */     createItemTemplate(907, "Colossus of Fo", "Colossus", "excellent", "good", "ok", "poor", "A grand statue depicting the avatar of the forest god Fo.", new short[] { 108, 31, 25, 194, 135, 52, 44, 195, 67, 49, 123, 178, 157 }, (short)60, (short)1, 0, 12096000L, 500, 500, 2000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.colossus.fo.", 50.0F, 7500000, (byte)15);
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
/* 2233 */     createItemTemplate(908, 2, "colourful carpet", "colourful carpets", "excellent", "good", "ok", "poor", "This carpet adds a nice patch of colour to any house or castle.", new short[] { 24, 44, 52, 51, 184 }, (short)901, (short)1, 0, 3024000L, 200, 120, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.colorful.carpet.small.", 20.0F, 3000, (byte)17, 3000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2243 */     createItemTemplate(910, 4, "colourful carpet", "colourful carpets", "excellent", "good", "ok", "poor", "This carpet adds a nice patch of colour to any house or castle.", new short[] { 24, 44, 52, 51, 184 }, (short)901, (short)1, 0, 3024000L, 200, 170, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.colorful.carpet.large.", 40.0F, 5000, (byte)17, 5000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2253 */     createItemTemplate(909, 3, "colourful carpet", "colourful carpets", "excellent", "good", "ok", "poor", "This carpet adds a nice patch of colour to any house or castle.", new short[] { 24, 44, 52, 51, 184 }, (short)901, (short)1, 0, 3024000L, 200, 150, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.colorful.carpet.medium.", 30.0F, 4000, (byte)17, 4000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2262 */     createItemTemplate(911, 3, "high bookshelf", "high bookshelves", "excellent", "good", "ok", "poor", "Simply looking at this collection of wisdom makes you feel more clever.", new short[] { 21, 44, 52, 178, 67, 51, 199, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 150, 40, 240, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.high.bookshelf.", 35.0F, 30000, (byte)14, 20000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2272 */       .setContainerSize(40, 30, 60);
/* 2273 */     createItemTemplate(912, 3, "low bookshelf", "low bookshelves", "excellent", "good", "ok", "poor", "Simply looking at this collection of wisdom makes you feel more clever.", new short[] { 21, 44, 52, 178, 67, 51, 199, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 150, 40, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.low.bookshelf.", 25.0F, 19000, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2283 */       .setContainerSize(40, 30, 30);
/* 2284 */     createItemTemplate(1401, 3, "empty high bookshelf", "empty high shelves", "excellent", "good", "ok", "poor", "A sharp looking empty bookshelf to store your special writings.", new short[] { 21, 44, 52, 178, 67, 51, 199, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 150, 40, 240, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.high.bookshelf.empty.", 35.0F, 30000, (byte)14, 20000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2294 */       .setContainerSize(40, 50, 100);
/* 2295 */     createItemTemplate(1400, 3, "empty low bookshelf", "empty low shelves", "excellent", "good", "ok", "poor", "A sharp looking empty bookshelf to store your special writings.", new short[] { 21, 44, 52, 178, 67, 51, 199, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 150, 40, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.low.bookshelf.empty.", 25.0F, 19000, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2305 */       .setContainerSize(40, 50, 60);
/* 2306 */     createItemTemplate(1402, 4, "bar table", "bar tables", "superb", "good", "ok", "poor", "A large bar made for holding copious amounts of drinks.", new short[] { 108, 21, 135, 86, 31, 51, 52, 157, 199, 44, 92, 176, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 80, 60, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.thirstyaussiebar.", 35.0F, 18000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2316 */       .setContainerSize(40, 60, 200);
/*      */     
/* 2318 */     createItemTemplate(913, 3, "fine high chair", "fine high chairs", "excellent", "good", "ok", "poor", "A comfy looking chair which invites you to rest.", new short[] { 21, 44, 52, 48, 67, 51, 117, 197, 178, 199, 249 }, (short)274, (short)41, 0, 9072000L, 60, 65, 175, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.fine.high.chair.", 60.0F, 7500, (byte)14, 30000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2328 */       .setSecondryItem("Seat");
/*      */     
/* 2330 */     createItemTemplate(914, 3, "high chair", "high chairs", "excellent", "good", "ok", "poor", "A comfy looking chair which invites you to rest.", new short[] { 21, 44, 52, 48, 67, 51, 117, 197, 178, 199 }, (short)274, (short)41, 0, 9072000L, 60, 65, 170, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.regular.high.chair.", 50.0F, 7000, (byte)14, 20000, true, 0);
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
/* 2341 */     createItemTemplate(915, 3, "paupers high chair", "paupers high chairs", "excellent", "good", "ok", "poor", "A comfy looking chair which invites you to rest.", new short[] { 21, 44, 52, 48, 67, 51, 117, 197, 178, 199 }, (short)274, (short)41, 0, 9072000L, 60, 65, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.pauper.high.chair.", 25.0F, 7000, (byte)14, 10000, true, 0);
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
/* 2352 */     createItemTemplate(916, "Colossus of Libila", "Colossus", "excellent", "good", "ok", "poor", "A grand statue depicting the avatar of the malevolent goddess Libila.", new short[] { 108, 31, 25, 194, 135, 52, 44, 195, 67, 49, 123, 178, 157 }, (short)60, (short)1, 0, 12096000L, 500, 500, 2000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.colossus.libila.", 50.0F, 7500000, (byte)15);
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
/* 2366 */     createItemTemplate(917, "ivy seedling", "ivy seedlings", "superb", "good", "ok", "poor", "A tiny ivy seedling.", new short[] { 146 }, (short)484, (short)1, 0, 86400L, 1, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 200.0F, 50, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2374 */     createItemTemplate(918, "grape seedling", "grape seedlings", "superb", "good", "ok", "poor", "A tiny grape seedling.", new short[] { 146, 157 }, (short)484, (short)1, 0, 86400L, 1, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 200.0F, 50, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2382 */     createItemTemplate(919, "ivy trellis", "trellises", "excellent", "good", "ok", "poor", "Some ivy vines growing up a sturdy wooden trellis.", new short[] { 108, 21, 52, 44, 86, 178, 51, 199, 167, 230 }, (short)420, (short)58, 0, 9072000L, 10, 250, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.trellis.ivy.", 40.0F, 5000, (byte)68);
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
/* 2394 */     createItemTemplate(920, "grape trellis", "trellises", "excellent", "good", "ok", "poor", "Some grape vines growing up a sturdy wooden trellis.", new short[] { 108, 21, 52, 44, 86, 178, 51, 199, 167, 230 }, (short)420, (short)58, 0, 9072000L, 10, 250, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.trellis.grape.", 40.0F, 5000, (byte)49)
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
/* 2406 */       .setHarvestsTo(411);
/*      */     
/* 2408 */     createItemTemplate(921, "wool", "wools", "superb", "good", "ok", "poor", "A bale of wool.", new short[] { 24, 146, 46, 113 }, (short)558, (short)1, 0, 28800L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wool.", 1.0F, 100, (byte)69, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2414 */     createItemTemplate(936, "ballista", "ballistas", "superb", "good", "ok", "poor", "This is a large mounted crossbow firing enormous darts.", new short[] { 108, 21, 51, 52, 44, 92, 86, 109, 176, 177, 113, 117, 157 }, (short)60, (short)40, 0, 9072000L, 250, 250, 320, 10093, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.ballista.", 30.0F, 70000, (byte)14, 10000, true);
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
/* 2425 */     createItemTemplate(933, "machine mount", "mounts", "superb", "good", "ok", "poor", "This is a large mount suited for placing large weapons on.", new short[] { 108, 21, 51, 52, 44, 92, 86, 176, 113 }, (short)60, (short)1, 0, 9072000L, 250, 180, 200, 10093, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.mount.", 30.0F, 50000, (byte)14, 10000, true);
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
/* 2436 */     createItemTemplate(937, "trebuchet", "trebuchet", "superb", "good", "ok", "poor", "The trebuchet is a huge frightening machine designed to haul devastating rocks long distances. You need to put a lot of rocks or ore into it as counterweights.", new short[] { 108, 1, 31, 63, 21, 51, 67, 52, 44, 92, 86, 109, 177, 113, 117, 178 }, (short)60, (short)40, 0, 9072000L, 450, 900, 750, 10094, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.trebuchet.", 30.0F, 1200000, (byte)14, 10000, true);
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
/* 2452 */     createItemTemplate(1125, "battering ram", "battering rams", "superb", "good", "ok", "poor", "A large war machine designed to knock down walls and doors, used by ramming the large log into the wall you wish to no longer exist.", new short[] { 108, 31, 63, 21, 51, 67, 52, 44, 92, 86, 109, 177, 113, 56, 178 }, (short)60, (short)40, 0, 9072000L, 300, 300, 400, 1029, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.batteringram.", 30.0F, 200000, (byte)14, 10000, true);
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
/* 2468 */     createItemTemplate(1126, "battering ram head", "ram heads", "excellent", "good", "ok", "poor", "A large and heavy four-sided pyramidal head that will provide quite the punch.", new short[] { 22, 146, 113, 158 }, (short)1216, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.head.batteringram.", 40.0F, 10000, (byte)0, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2476 */     createItemTemplate(934, "strange device", "devices", "superb", "good", "ok", "poor", "A pole fastened in chains lies here.", new short[] { 108, 21, 51, 52, 44, 86, 109, 176, 177, 113, 48, 199 }, (short)60, (short)1, 0, 9072000L, 250, 250, 250, 1029, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.pewpewdie.uncharged.", 30.0F, 150000, (byte)14, 10000, true);
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
/* 2489 */     createItemTemplate(941, "fire turret", "turret", "superb", "good", "ok", "poor", "A pole in chains floats here. It seems inhabited by fire spirits.", new short[] { 108, 21, 51, 59, 52, 44, 86, 109, 176, 177, 113, 48, 199 }, (short)60, (short)1, 0, 9072000L, 250, 400, 250, 10044, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.pewpewdie.fire.", 70.0F, 150000, (byte)14, 10000, true);
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
/* 2507 */     createItemTemplate(942, "lightning turret", "turret", "superb", "good", "ok", "poor", "A pole in chains floats here. It seems inhabited by lightning spirits.", new short[] { 108, 21, 51, 59, 52, 44, 86, 109, 176, 177, 113, 48, 199 }, (short)60, (short)1, 0, 9072000L, 250, 400, 250, 10044, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.pewpewdie.lightning.", 70.0F, 150000, (byte)14, 10000, true);
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
/* 2526 */     createItemTemplate(968, "frost turret", "turret", "superb", "good", "ok", "poor", "A pole in chains floats here. It seems inhabited by frost spirits.", new short[] { 108, 21, 51, 59, 52, 44, 86, 109, 176, 177, 113, 48, 199 }, (short)60, (short)1, 0, 9072000L, 250, 400, 250, 10044, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.pewpewdie.frost.", 70.0F, 150000, (byte)14, 10000, true);
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
/* 2545 */     createItemTemplate(940, "acid turret", "turret", "superb", "good", "ok", "poor", "A pole in chains floats here. It seems inhabited by acid spirits.", new short[] { 108, 21, 51, 59, 52, 44, 86, 109, 176, 177, 113, 48, 199 }, (short)60, (short)1, 0, 9072000L, 250, 400, 250, 10044, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.pewpewdie.acid.", 70.0F, 150000, (byte)14, 10000, true);
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
/* 2564 */     createItemTemplate(938, "spike barrier", "barriers", "fresh", "dry", "brittle", "rotten", "This is a very quick easy barrier construction made from a log and lots of shafts.", new short[] { 21, 113, 52, 86, 109, 98, 51, 67, 31, 123 }, (short)606, (short)1, 10, 604800L, 300, 250, 400, 1005, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.barrier.spike.", 10.0F, 30000, (byte)0, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2574 */     createItemTemplate(931, "siege shield", "siege shields", "fresh", "dry", "brittle", "rotten", "A movable wall made from planks, this will block most arrows and also provide some protection against hot oil.", new short[] { 21, 113, 52, 86, 109, 51, 98, 117, 31, 190 }, (short)606, (short)1, 10, 604800L, 400, 250, 200, 1005, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.siegeshield.", 10.0F, 30000, (byte)0, 20, true);
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
/* 2590 */     createItemTemplate(939, "archery tower", "archery towers", "excellent", "good", "ok", "poor", "Inside this building live fanatic guards who spend all their time awake shooting arrows at every enemy in sight.", new short[] { 52, 21, 31, 67, 44, 109, 86, 49, 98, 123, 59 }, (short)60, (short)1, 0, 19353600L, 400, 1600, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.archerytower.", 60.0F, 1500000, (byte)14);
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
/* 2606 */     createItemTemplate(932, "ballista dart", "ballista darts", "excellent", "good", "ok", "poor", "A long dart with a four-sided pyramidal head which will pierce even thick shields.", new short[] { 108, 146, 44, 21, 147, 113 }, (short)1215, (short)1, 0, 9072000L, 1, 1, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.ballista.", 45.0F, 500, (byte)14, 30, false);
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
/* 2620 */     createItemTemplate(935, "ballista dart head", "dart heads", "excellent", "good", "ok", "poor", "A four-sided pyramidal dart head which will provide quite the punch.", new short[] { 22, 146, 113, 158 }, (short)1216, (short)1, 0, 3024000L, 1, 4, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.head.ballista.", 40.0F, 200, (byte)0, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2630 */     createItemTemplate(922, "spinning wheel", "spinning wheels", "excellent", "good", "ok", "poor", "An ingenious device which turns rough wool into yarn.", new short[] { 21, 199, 52, 44, 86, 67, 51, 176, 108 }, (short)826, (short)1, 0, 9072000L, 135, 37, 135, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.wheel.spinning.", 40.0F, 4500, (byte)14);
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
/* 2644 */     createItemTemplate(923, 3, "lounge chair", "lounge chairs", "excellent", "good", "ok", "poor", "A curved wooden chair with a footrest.", new short[] { 21, 44, 52, 86, 67, 51, 176, 178, 199 }, (short)274, (short)1, 0, 9072000L, 230, 90, 130, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.chair.lounge.", 30.0F, 8000, (byte)14, 10000, true, 0);
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
/* 2655 */     createItemTemplate(924, 3, "royal lounge chaise", "royal lounge chaises", "excellent", "good", "ok", "poor", "Feel like the captain of a dragon ship while leaning back comfortably.", new short[] { 21, 44, 52, 86, 67, 51, 176, 117, 197, 178, 199, 249 }, (short)274, (short)41, 0, 9072000L, 294, 110, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.chair.lounge.royal.", 50.0F, 10000, (byte)14, 20000, true, 0)
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
/* 2666 */       .setSecondryItem("Seat");
/*      */     
/* 2668 */     createItemTemplate(925, "yarn", "yarn", "excellent", "good", "ok", "poor", "A fluffy ball of yarn, incorrect use may be fatal. WARNING: Do not leave unattended near cats!", new short[] { 24, 146, 46, 158 }, (short)620, (short)1, 0, 28800L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.yarn.", 1.0F, 200, (byte)69, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2675 */     createItemTemplate(926, "square piece of wool cloth", "square pieces of wool cloth", "excellent", "good", "ok", "poor", "A piece of wool cloth, about an arm length square.", new short[] { 24, 146, 46 }, (short)640, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.yard.", 15.0F, 600, (byte)69, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2683 */     createItemTemplate(927, 3, "cupboard", "cupboards", "excellent", "good", "ok", "poor", "A wooden cupboard, used to store all kinds of small items.", new short[] { 21, 44, 52, 47, 86, 67, 178, 51, 1, 199, 180, 176, 259 }, (short)60, (short)1, 0, 9072000L, 135, 58, 90, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.cupboard.", 50.0F, 6500, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2692 */       .setContainerSize(130, 55, 80);
/*      */     
/* 2694 */     createItemTemplate(1118, "alchemy flask", "alchemy flasks", "superb", "good", "ok", "poor", "A container for storing liquids.", new short[] { 30, 178, 33, 232, 1, 31, 229, 112, 240 }, (short)425, (short)1, 0, Long.MAX_VALUE, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.pottery.thermos.", 50.0F, 250, (byte)19, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2704 */     createItemTemplate(1117, 3, "alchemist's cupboard", "alchemist's cupboards", "excellent", "good", "ok", "poor", "A wooden cupboard, used to store small amounts of precious liquids.", new short[] { 21, 178, 44, 52, 47, 86, 67, 51, 1, 199, 180, 176 }, (short)60, (short)1, 0, 9072000L, 135, 58, 90, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.cupboard.alchemist.", 50.0F, 6500, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2713 */       .setContainerSize(0, 0, 0)
/* 2714 */       .setInitialContainers(new InitialContainer[] {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19), new InitialContainer(1118, "vial", (byte)19)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2725 */         }).setMaxItemCount(11);
/*      */     
/* 2727 */     createItemTemplate(1120, "storage shelf", "storage shelves", "superb", "good", "ok", "poor", "A shelf for storing things.", new short[] { 21, 178, 232, 108, 1, 31, 229, 112, 240, 245 }, (short)626, (short)1, 0, Long.MAX_VALUE, 80, 80, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.storageshelf.", 40.0F, 10000, (byte)14, 100, false);
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
/* 2738 */     createItemTemplate(1119, 4, "storage unit", "storage units", "excellent", "good", "ok", "poor", "A wooden storage unit with shelves inside to store a large amount of items.", new short[] { 21, 178, 44, 52, 47, 86, 67, 51, 1, 199, 180, 176 }, (short)60, (short)1, 0, 9072000L, 140, 140, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.storageunit.", 40.0F, 200000, (byte)14, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2748 */       .setContainerSize(0, 0, 0)
/* 2749 */       .setInitialContainers(new InitialContainer[] {
/*      */           
/*      */           new InitialContainer(1120, "shelf"), new InitialContainer(1120, "shelf"), new InitialContainer(1120, "shelf")
/*      */         
/* 2753 */         }).setMaxItemCount(6);
/*      */     
/* 2755 */     createItemTemplate(928, 3, "round marble table", "round marble tables", "excellent", "good", "ok", "poor", "A sturdy round table made from marble.", new short[] { 108, 25, 44, 86, 52, 67, 51, 176, 199, 173, 178, 1, 180, 256 }, (short)60, (short)1, 0, 12096000L, 148, 100, 148, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.table.round.marble.", 45.0F, 80000, (byte)62, 30000, true, 0)
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
/* 2767 */       .setContainerSize(60, 150, 150);
/*      */     
/* 2769 */     createItemTemplate(929, 3, "rectangular marble table", "rectangular marble tables", "excellent", "good", "ok", "poor", "A sturdy rectangular table made from marble.", new short[] { 108, 25, 44, 86, 52, 67, 51, 176, 199, 173, 178, 1, 180, 256 }, (short)60, (short)1, 0, 12096000L, 210, 105, 133, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.table.square.marble.", 45.0F, 100000, (byte)62, 30000, true, 0)
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
/* 2781 */       .setContainerSize(60, 140, 200);
/*      */     
/* 2783 */     createItemTemplate(943, 3, "peasant wool cap", "peasant wool caps", "excellent", "good", "ok", "poor", "A shabby wool cap.", new short[] { 108, 44, 24, 4, 157, 92 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.cap0.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2793 */     createItemTemplate(944, 3, "yellow peasant wool cap", "yellow peasant wool caps", "excellent", "good", "ok", "poor", "A shabby yellow wool cap.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.cap0.yellow.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2802 */     createItemTemplate(945, 3, "green peasant wool cap", "green peasant wool caps", "excellent", "good", "ok", "poor", "A shabby green wool cap.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.cap0.green.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2811 */     createItemTemplate(946, 3, "red peasant wool cap", "red peasant wool caps", "excellent", "good", "ok", "poor", "A shabby red wool cap.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.cap0.red.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2820 */     createItemTemplate(947, 3, "blue peasant wool cap", "blue peasant wool caps", "excellent", "good", "ok", "poor", "A shabby blue wool cap.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.cap0.blue.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2829 */     createItemTemplate(948, 3, "common wool hat", "common wool hats", "excellent", "good", "ok", "poor", "A common wool hat for all weathers.", new short[] { 108, 44, 24, 4, 157, 92 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat1.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2839 */     createItemTemplate(949, 3, "dark common wool hat", "dark common wool hats", "excellent", "good", "ok", "poor", "A dark common wool hat for all weathers.", new short[] { 108, 44, 24, 4, 157 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat1.dark.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2848 */     createItemTemplate(950, 3, "brown common wool hat", "brown common wool hats", "excellent", "good", "ok", "poor", "A brown common wool hat for all weathers.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat1.brown.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2857 */     createItemTemplate(951, 3, "green common wool hat", "green common wool hats", "excellent", "good", "ok", "poor", "A green common wool hat for all weathers.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat1.green.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2866 */     createItemTemplate(952, 3, "red common wool hat", "red common wool hats", "excellent", "good", "ok", "poor", "A red common wool hat for all weathers.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat1.red.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2875 */     createItemTemplate(953, 3, "blue common wool hat", "blue common wool hats", "excellent", "good", "ok", "poor", "A blue common wool hat for all weathers.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat1.blue.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2884 */     createItemTemplate(954, 3, "foresters wool hat", "foresters wool hats", "excellent", "good", "ok", "poor", "A comfy but oddly shaped wool hat.", new short[] { 108, 44, 24, 4, 157, 92 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat2.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2894 */     createItemTemplate(957, 3, "blue foresters wool hat", "blue foresters wool hats", "excellent", "good", "ok", "poor", "A comfy but oddly shaped blue wool hat.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat2.blue.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2903 */     createItemTemplate(956, 3, "dark foresters wool hat", "dark foresters wool hats", "excellent", "good", "ok", "poor", "A comfy but oddly shaped dark wool hat.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat2.dark.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2912 */     createItemTemplate(955, 3, "green foresters wool hat", "green foresters wool hats", "excellent", "good", "ok", "poor", "A comfy but oddly shaped green wool hat.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat2.green.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2921 */     createItemTemplate(958, 3, "red foresters wool hat", "red foresters wool hats", "excellent", "good", "ok", "poor", "A comfy but oddly shaped red wool hat.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat2.red.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2930 */     createItemTemplate(959, 3, "brown bear helm", "brown bear helms", "excellent", "good", "ok", "poor", "A gorgeous head gear made from a mighty brown bear.", new short[] { 108, 44, 23, 4, 147 }, (short)1003, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.brownbear.", 15.0F, 700, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2939 */     createItemTemplate(960, 3, "leather adventurer hat", "adventurer hats", "excellent", "good", "ok", "poor", "The leather hat to wear for all of your adventures.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.hat1.leather.", 10.0F, 400, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2948 */     createItemTemplate(961, 3, "squire wool cap", "squire wool caps", "excellent", "good", "ok", "poor", "A more robust wool cap commonly used by squires.", new short[] { 108, 44, 24, 4, 157, 92 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat3.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2958 */     createItemTemplate(964, 3, "black squire wool cap", "black squire wool caps", "excellent", "good", "ok", "poor", "A more robust wool cap commonly used by squires.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat3.black.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2967 */     createItemTemplate(963, 3, "blue squire wool cap", "blue squire wool caps", "excellent", "good", "ok", "poor", "A more robust wool cap commonly used by squires.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat3.blue.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2976 */     createItemTemplate(962, 3, "green squire wool cap", "green squire wool caps", "excellent", "good", "ok", "poor", "A more robust wool cap commonly used by squires.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat3.green.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2985 */     createItemTemplate(965, 3, "red squire wool cap", "red squire wool caps", "excellent", "good", "ok", "poor", "A more robust wool cap commonly used by squires.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat3.red.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2994 */     createItemTemplate(966, 3, "yellow squire wool cap", "yellow squire wool caps", "excellent", "good", "ok", "poor", "A more robust wool cap commonly used by squires.", new short[] { 108, 44, 24, 4, 147 }, (short)963, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.wool.hat3.yellow.", 10.0F, 300, (byte)69, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3002 */     createItemTemplate(967, "garden gnome", "gnomes", "almost full", "somewhat occupied", "half-full", "emptyish", "A small serious green gnome stands here ready for christmas.", new short[] { 108, 51, 52, 67, 1, 33, 40, 178 }, (short)60, (short)1, 0, 2419200L, 10, 10, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.garden.green.", 300.0F, 20000, (byte)18);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3009 */     createItemTemplate(969, "supply depot", "depots", "excellent", "good", "ok", "poor", "This place is well frequented and littered with broken goods.", new short[] { 52, 21, 31, 67, 40, 49, 98, 48, 1, 178, 63 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.war.supplydepot.1.", 100.0F, 500000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3017 */     createItemTemplate(970, "supply depot", "depots", "excellent", "good", "ok", "poor", "This place is well frequented and littered with broken goods.", new short[] { 52, 21, 31, 67, 40, 49, 98, 48, 1, 178, 63 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.war.supplydepot.2.", 100.0F, 500000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3025 */     createItemTemplate(971, "supply depot", "depots", "excellent", "good", "ok", "poor", "This place is well frequented and littered with broken goods.", new short[] { 52, 21, 31, 67, 40, 49, 98, 48, 1, 178, 63 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.war.supplydepot.3.", 100.0F, 500000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3033 */     createItemTemplate(972, "yule goat", "goats", "excellent", "good", "ok", "poor", "A popular christmas decoration is a goat made from straw. To be placed by the christmas tree.", new short[] { 108, 51, 52, 40 }, (short)60, (short)1, 0, Long.MAX_VALUE, 10, 40, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.bucketthree.", 300.0F, 500, (byte)70);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3039 */     createItemTemplate(973, 3, "mask of the enlightened", "masks", "excellent", "good", "ok", "poor", "At first glance the mask looks brittle, it's however surprisingly sturdy and the materials used are of the highest quality.", new short[] { 108, 44, 23, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.enlightened.", 25.0F, 200, (byte)16, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3047 */       .setFragmentAmount(10);
/* 3048 */     createItemTemplate(974, 3, "mask of the ravager", "masks", "excellent", "good", "ok", "poor", "Strikingly intricate leatherwork makes this mask stand out as rare and of high quality.", new short[] { 108, 44, 23, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.ravager.", 25.0F, 200, (byte)16, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3055 */       .setFragmentAmount(10);
/* 3056 */     createItemTemplate(975, 3, "pale mask", "masks", "excellent", "good", "ok", "poor", "The mask is made of light materials with a luxurious finish..", new short[] { 108, 44, 30, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.pale.", 25.0F, 200, (byte)19, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3063 */       .setFragmentAmount(10);
/* 3064 */     createItemTemplate(976, 3, "mask of the shadow", "masks", "excellent", "good", "ok", "poor", "A dark mask concealing its wearers features.", new short[] { 108, 44, 23, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.shadow.", 25.0F, 200, (byte)16, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3071 */       .setFragmentAmount(10);
/* 3072 */     createItemTemplate(977, 3, "mask of the challenger", "masks", "excellent", "good", "ok", "poor", "The expression of the mask is that of a predator daring anyone to challenge the wearer in combat.", new short[] { 108, 44, 22, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.challenge.", 25.0F, 200, (byte)8, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3080 */       .setFragmentAmount(10);
/* 3081 */     createItemTemplate(978, 3, "mask of the isles", "masks", "excellent", "good", "ok", "poor", "A mask.", new short[] { 108, 44, 30, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.isles.", 25.0F, 200, (byte)19, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3088 */       .setFragmentAmount(10);
/* 3089 */     createItemTemplate(1306, 3, "mask of rebirth", "masks", "excellent", "good", "ok", "poor", "You can almost feel the mask is alive with little shoots among the leaves.", new short[] { 108, 44, 22, 4, 187 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.midsummer.", 25.0F, 200, (byte)19, 10000, true, 0)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3096 */       .setFragmentAmount(10);
/* 3097 */     createItemTemplate(979, 3, "horned helmet of gold", "horned helmets", "excellent", "good", "ok", "poor", "A horned helmet that inspires respect. It looks dangerous, heavy and robust.", new short[] { 108, 44, 22, 4 }, (short)968, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.greathelmHornedOfGold.", 25.0F, 1700, (byte)9, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3105 */     createItemTemplate(980, 3, "plumed helm of the hunt", "plumed helmets", "excellent", "good", "ok", "poor", "An open-faced round-top helm.", new short[] { 108, 44, 22, 4 }, (short)966, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.openhelmplumedhunt.", 15.0F, 1100, (byte)9, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3113 */     createItemTemplate(981, "challenge statue", "challenge statues", "almost full", "somewhat occupied", "half-full", "emptyish", "The golden statue of challenges.", new short[] { 108, 22, 51, 52, 40, 67, 176, 178, 187 }, (short)60, (short)1, 0, Long.MAX_VALUE, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.challenge.", 15.0F, 100000, (byte)7);
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
/* 3130 */     createItemTemplate(982, "challenge statue", "challenge statues", "almost full", "somewhat occupied", "half-full", "emptyish", "The silver statue of challenges.", new short[] { 108, 22, 51, 52, 40, 67, 176, 178, 187 }, (short)60, (short)1, 0, Long.MAX_VALUE, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.challenge.", 15.0F, 100000, (byte)8);
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
/* 3147 */     createItemTemplate(983, "challenge statue", "challenge statues", "almost full", "somewhat occupied", "half-full", "emptyish", "The bronze statue of challenges.", new short[] { 108, 22, 51, 52, 40, 67, 176, 178, 187 }, (short)60, (short)1, 0, Long.MAX_VALUE, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.challenge.", 15.0F, 100000, (byte)31);
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
/* 3164 */     createItemTemplate(984, "challenge statue", "challenge statues", "almost full", "somewhat occupied", "half-full", "emptyish", "The marble statue of challenges.", new short[] { 108, 25, 51, 52, 40, 67, 176, 178, 187 }, (short)60, (short)1, 0, Long.MAX_VALUE, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.challenge.", 15.0F, 100000, (byte)62);
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
/* 3181 */     createItemTemplate(985, "hota necklace", "hota necklaces", "new", "fancy", "ok", "old", "A HotA necklace.", new short[] { 108, 52, 147, 22, 44, 87, 153, 187, 157 }, (short)268, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, new byte[] { 29, 36 }, "model.decoration.necklace.hota.", 40.0F, 100, (byte)8, 7000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3191 */     createItemTemplate(986, "staff of land", "land staves", "fresh", "dry", "brittle", "rotten", "A rare and masterfully crafted staff. The staff looks intimidating and though the finer detailing looks brittle at first, it is surprisingly strong and sharp.", new short[] { 44, 133, 22, 14, 37, 84, 129, 187 }, (short)646, (short)1, 45, Long.MAX_VALUE, 3, 4, 150, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.staffOfLand.", 10.0F, 3000, (byte)9, 10000, true);
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
/* 3206 */     createItemTemplate(987, "tapestry stand", "tapestry stands", "excellent", "good", "ok", "poor", " A stand used for making a tapestry.", new short[] { 52, 21, 86, 119, 44, 67 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.stand.", 30.0F, 6000, (byte)14, 10000, true);
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
/* 3222 */     createItemTemplate(988, "green tapestry", "green tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made of wool with a green pattern.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.pattern1.", 30.0F, 12000, (byte)69, 10000, true);
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
/* 3238 */     createItemTemplate(989, "beige tapestry", "beige tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made of wool with a beige orange pattern.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.pattern2.", 30.0F, 12000, (byte)69, 10000, true);
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
/* 3254 */     createItemTemplate(990, "orange tapestry", "orange tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made of wool with a orange pattern.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.pattern3.", 30.0F, 12000, (byte)69, 10000, true);
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
/* 3270 */     createItemTemplate(991, "cavalry motif tapestry", "cavalry tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made of wool with a cavalry attack motif.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.motif1.", 30.0F, 12000, (byte)69, 10000, true);
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
/* 3286 */     createItemTemplate(992, "festivities motif tapestry", "festivity tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made of wool with a motif of festivities.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.motif2.", 30.0F, 12000, (byte)69, 10000, true);
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
/* 3302 */     createItemTemplate(993, "battle of Kyara tapestry", "kyara tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made of wool with a motif showing the great battle of Kyara.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.motif3.", 30.0F, 12000, (byte)69, 10000, true);
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
/* 3318 */     createItemTemplate(994, "tapestry of Faeldray", "faeldray tapestries", "excellent", "good", "ok", "poor", "A nice woven Tapestry made by Faeldray.", new short[] { 24, 52, 109, 86, 119, 44, 188, 157, 173, 51 }, (short)60, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tapestry.faeldray.", 30.0F, 12000, (byte)69, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3328 */     createItemTemplate(995, 4, "treasure chest", "treasure chests", "almost full", "somewhat occupied", "half-full", "emptyish", "A chest inscribed with various runes. Only the gods would know why it appeared here.", new short[] { 1, 31, 6, 147, 47, 52, 40 }, (short)244, (short)1, 0, 9072000L, 30, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.chest.large.treasure.", 210.0F, 200000, (byte)14, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3337 */     createItemTemplate(996, "neutral guard tower", "neutral guard towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 40, 49, 98, 123, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.neutraltower.", 20.0F, 500000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3346 */     createItemTemplate(997, "valentines", "valentines", "excellent", "good", "ok", "poor", "A vase with some sweet roses to a sweet friend.", new short[] { 108, 30, 169, 52, 51 }, (short)510, (short)47, 0, 1382400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.valentineroses.", 50.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3356 */     createItemTemplate(998, 3, "cavalier helmet", "cavalier helmets", "excellent", "good", "ok", "poor", "This special helmet is handed out to a limited number of cavaliers in order for them to inspire and lead their peers.", new short[] { 108, 44, 22, 4, 157 }, (short)967, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.greathelmWolf.", 95.0F, 1100, (byte)9, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3366 */     createItemTemplate(999, "tall kingdom banner", "tall banners", "excellent", "good", "ok", "poor", "An elegant symbol of allegiance and faith.", new short[] { 24, 92, 51, 52, 167, 48, 86, 119, 44, 173, 199 }, (short)640, (short)1, 0, 9072000L, 5, 5, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.tallbanner.", 50.0F, 5500, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3376 */     createItemTemplate(1000, "ownership papers", "ownership papers", "new", "fancy", "ok", "old", "A paper that shows the ownership of an Item or creature, used to transfer ownership.", new short[] { 83, 40, 42, 48, 53, 54, 114 }, (short)321, (short)52, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.", 100.0F, 0, (byte)33, 100000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3384 */     createItemTemplate(1001, "marble planter", "planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, pleasant to look upon.", new short[] { 108, 25, 52, 86, 119, 44, 176, 51, 199 }, (short)60, (short)47, 0, 12096000L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.", 15.0F, 90000, (byte)62, 10000, true);
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
/* 3395 */     createItemTemplate(1003, "blue planter", "blue planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some crooked but beautiful blue flowers.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.blue.", 50.0F, 90000, (byte)62, 10000, true);
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
/* 3406 */     createItemTemplate(1002, "yellow planter", "yellow planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some yellow prickly flowers.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.yellow.", 15.0F, 90000, (byte)62, 10000, true);
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
/* 3417 */     createItemTemplate(1007, "greenish-yellow planter", "greenish-yellow planter", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some greenish-yellow furry flowers.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.greenish.", 60.0F, 90000, (byte)62, 10000, true);
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
/* 3428 */     createItemTemplate(1006, "orange-red planter", "orange-red planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some long-stemmed orange-red flowers with thick, pointy leaves.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.orange.", 25.0F, 90000, (byte)62, 10000, true);
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
/* 3440 */     createItemTemplate(1004, "purple planter", "purple planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some purple fluffy flowers.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.purple.", 30.0F, 90000, (byte)62, 10000, true);
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
/* 3451 */     createItemTemplate(1005, "white planter", "white planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some thick-stemmed white flowers with long leaves.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.white.", 35.0F, 90000, (byte)62, 10000, true);
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
/* 3462 */     createItemTemplate(1008, "white-dotted planter", "white-dotted planters", "excellent", "good", "ok", "poor", "A planter made from the whitest of marble, with some uncommon white-dotted flowers.", new short[] { 108, 25, 169, 52, 86, 51, 199, 176 }, (short)60, (short)47, 0, 1382400L, 40, 120, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.planter.dotted.", 70.0F, 90000, (byte)62, 10000, true);
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
/* 3473 */     createItemTemplate(804, "tome of incineration", "tomes of incineration", "new", "fancy", "ok", "old", "Orange red golden flames endorse the covers of this tome.", new short[] { 53, 168 }, (short)325, (short)1, 0, Long.MAX_VALUE, 3, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.tomemagic.incineration.", 1000.0F, 1000, (byte)33, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3481 */     createItemTemplate(1012, "sheep milk", "sheep milk", "excellent", "good", "ok", "poor", "White, frothing, fat milk.", new short[] { 26, 88, 90, 113, 191, 212 }, (short)541, (short)1, 0, 3600L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 1000, (byte)28)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3487 */       .setNutritionValues(420, 50, 10, 34)
/* 3488 */       .setFoodGroup(1200);
/*      */     
/* 3490 */     createItemTemplate(1013, "bison milk", "bison milk", "excellent", "good", "ok", "poor", "White, frothing, fat milk.", new short[] { 26, 88, 90, 113, 191, 212 }, (short)541, (short)1, 0, 3600L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 1000, (byte)28)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3496 */       .setNutritionValues(420, 50, 10, 34)
/* 3497 */       .setFoodGroup(1200);
/*      */     
/* 3499 */     createItemTemplate(1014, 3, "goblin war bonnet", "war bonnets", "excellent", "good", "ok", "poor", "A sloppy looking headdress, the pinnacle of goblin craftsmanship.", new short[] { 108, 44, 23, 4 }, (short)1003, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.goblin.war.bonnet.", 15.0F, 700, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3507 */     createItemTemplate(1015, 3, "crown of the troll king", "troll crown", "excellent", "good", "ok", "poor", "A mouldy cow head, with some fleshy bits still attached to it.", new short[] { 108, 44, 23, 4, 157 }, (short)1003, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.troll.king.hat.", 15.0F, 700, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3515 */     createItemTemplate(1016, "Stone of Soulfall", "stones", "almost full", "somewhat occupied", "half-full", "emptyish", "The material of this stone is unworldly and can not be found naturally around here.", new short[] { 25, 49, 31, 52, 40 }, (short)60, (short)1, 0, 3600L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.10.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3521 */     createItemTemplate(1017, "rose seedling", "rose seedlings", "superb", "good", "ok", "poor", "A tiny rose seedling.", new short[] { 146, 157 }, (short)484, (short)1, 0, 86400L, 1, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 200.0F, 50, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3529 */     createItemTemplate(1018, "rose trellis", "trellises", "excellent", "good", "ok", "poor", "Some climbing roses growing up a sturdy wooden trellis.", new short[] { 108, 21, 52, 44, 86, 178, 51, 199, 167, 230 }, (short)420, (short)58, 0, 9072000L, 10, 250, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.trellis.rose.", 40.0F, 5000, (byte)47)
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
/* 3540 */       .setHarvestsTo(426);
/*      */     
/* 3542 */     createItemTemplate(1019, "small clay amphora", "small clay amphoras", "excellent", "good", "ok", "poor", "A small clay amphora that could be fired in a kiln.", new short[] { 108, 196, 44, 147, 1, 194, 63 }, (short)662, (short)1, 0, 172800L, 40, 70, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.amphora.small.", 50.0F, 6000, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3549 */     createItemTemplate(1020, "small pottery amphora", "small pottery amphoras", "excellent", "good", "ok", "poor", "A small clay amphora hardened by fire. It could be used to make resource changing potions", new short[] { 108, 30, 1, 33, 123, 195, 194, 52, 92, 215, 48 }, (short)682, (short)1, 0, 12096000L, 40, 70, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.amphora.small.", 5.0F, 6000, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3559 */     createItemTemplate(1021, "large clay amphora", "large clay amphoras", "excellent", "good", "ok", "poor", "A large clay amphora that could be fired in a kiln.", new short[] { 108, 196, 44, 147, 1, 194, 63 }, (short)662, (short)1, 0, 172800L, 50, 110, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.amphora.large.", 70.0F, 12000, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3566 */     createItemTemplate(1022, "large pottery amphora", "large pottery amphoras", "excellent", "good", "ok", "poor", "A large clay amphora hardened by fire.", new short[] { 108, 30, 1, 33, 123, 195, 194, 52, 92, 215, 48 }, (short)682, (short)1, 0, 12096000L, 50, 110, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.amphora.large.", 5.0F, 12000, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3576 */     createItemTemplate(1023, "kiln", "kilns", "excellent", "good", "ok", "poor", "A large kiln made from bricks with a clay inner lining and covered in dirt. The purpose is to fire clay items.", new short[] { 52, 31, 1, 25, 51, 48, 59, 108, 176, 199, 109, 44, 119, 86, 67, 178, 209 }, (short)60, (short)18, 0, 12096000L, 75, 150, 75, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.kiln.", 50.0F, 410000, (byte)15, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3586 */     createItemTemplate(1024, "conch", "conches", "excellent", "good", "ok", "poor", "This large conch shell makes weird sounds as you listen to it.", new short[] { 42, 40, 48 }, (short)588, (short)1, 0, Long.MAX_VALUE, 2, 2, 2, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.transmutation.", 40.0F, 14, (byte)52, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3593 */     createItemTemplate(1025, "birdcage", "birdcages", "excellent", "good", "ok", "poor", "A shiny gold coloured birdcage which looks like its made from brass.", new short[] { 108, 147, 22, 44, 52, 51, 199 }, (short)361, (short)1, 0, 3024000L, 35, 167, 35, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.birdcage.", 75.0F, 10000, (byte)30, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3601 */     createItemTemplate(1026, "unstable source rift", "rifts", "almost full", "somewhat occupied", "half-full", "emptyish", "A black hole with shimmering edges.", new short[] { 31, 135, 86, 6, 51, 52, 44, 67, 1, 33, 176, 178 }, (short)60, (short)1, 0, 12096000L, 50, 150, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.rift.unstable.", 40.0F, 120000, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3610 */     createItemTemplate(1027, "steel wand", "steel wands", "excellent", "good", "ok", "poor", "A stainless steel wand that lets the wand wielder learn more about other people.", new short[] { 108, 40, 42, 48, 53 }, (short)400, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.gm.", 10.0F, 5, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3617 */     createItemTemplate(1028, "smelter", "smelters", "almost full", "somewhat occupied", "half-full", "emptyish", "A smelter made from bricks and clay, fits more ore than a forge.", new short[] { 52, 31, 1, 25, 51, 48, 59, 108, 176, 199, 109, 44, 119, 86, 67, 178, 209, 259 }, (short)60, (short)18, 0, 12096000L, 85, 150, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.smelter.", 65.0F, 1000000, (byte)15);
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
/* 3628 */     createItemTemplate(1030, "sword display", "sword displays", "excellent", "good", "ok", "poor", "Two swords mounted on a shield.", new short[] { 108, 21, 147, 44, 52, 51, 199, 119, 86 }, (short)60, (short)1, 0, 9072000L, 90, 200, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.hanger.sword.", 50.0F, 16500, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3636 */     createItemTemplate(1031, "axe display", "axe displays", "excellent", "good", "ok", "poor", "Two axes mounted on a shield.", new short[] { 108, 21, 147, 44, 52, 51, 199, 119, 86 }, (short)60, (short)1, 0, 9072000L, 90, 200, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.hanger.axe.", 50.0F, 15500, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3644 */     createItemTemplate(1032, "yule reindeer", "reindeers", "excellent", "good", "ok", "poor", "This years fad is a reindeer made from straw. Best friend of the yule goat.", new short[] { 108, 51, 52, 40 }, (short)60, (short)1, 0, Long.MAX_VALUE, 10, 40, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.bucketfour.", 300.0F, 500, (byte)70);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3650 */     createItemTemplate(1033, "rift stone", "stones", "almost full", "somewhat occupied", "half-full", "emptyish", "A weird stone formation.", new short[] { 25, 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.stone.1.", 99.0F, 200000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3657 */     createItemTemplate(1034, "rift stone", "stones", "almost full", "somewhat occupied", "half-full", "emptyish", "A weird stone formation.", new short[] { 25, 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.stone.2.", 99.0F, 200000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3664 */     createItemTemplate(1035, "rift stone", "stones", "almost full", "somewhat occupied", "half-full", "emptyish", "A weird stone formation.", new short[] { 25, 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.stone.3.", 99.0F, 200000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3671 */     createItemTemplate(1036, "rift stone", "stones", "almost full", "somewhat occupied", "half-full", "emptyish", "A weird stone formation.", new short[] { 25, 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.stone.4.", 99.0F, 200000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3678 */     createItemTemplate(1037, "rift crystal", "crystals", "solid", "damaged", "brittle", "fragile", "A crystal of unnatural proportions.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.crystal.1.", 99.0F, 200000, (byte)52, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3685 */     createItemTemplate(1038, "rift crystal", "crystals", "solid", "damaged", "brittle", "fragile", "A crystal of unnatural proportions.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.crystal.2.", 99.0F, 200000, (byte)52, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3692 */     createItemTemplate(1039, "rift crystal", "crystals", "solid", "damaged", "brittle", "fragile", "A crystal of unnatural proportions.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.crystal.3.", 99.0F, 200000, (byte)52, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3699 */     createItemTemplate(1040, "rift crystal", "crystals", "solid", "damaged", "brittle", "fragile", "A crystal of unnatural proportions.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.crystal.4.", 99.0F, 200000, (byte)52, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3706 */     createItemTemplate(1041, "plant", "plants", "solid", "damaged", "brittle", "fragile", "This rare plant doesn't seem indigineous to the area.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.plant.1.", 99.0F, 200000, (byte)22, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3713 */     createItemTemplate(1042, "plant", "plants", "solid", "damaged", "brittle", "fragile", "This rare plant doesn't seem indigineous to the area.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.plant.2.", 99.0F, 200000, (byte)22, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3720 */     createItemTemplate(1043, "plant", "plants", "solid", "damaged", "brittle", "fragile", "This rare plant doesn't seem indigineous to the area.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.plant.3.", 99.0F, 200000, (byte)22, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3727 */     createItemTemplate(1044, "plant", "plants", "solid", "damaged", "brittle", "fragile", "This rare plant doesn't seem indigineous to the area.", new short[] { 49, 31, 52, 67, 86, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.plant.4.", 99.0F, 200000, (byte)22, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3734 */     createItemTemplate(1045, "rift altar", "altars", "solid", "damaged", "brittle", "fragile", "This thing seems to be stabilizing the Rift.", new short[] { 49, 31, 52, 67, 123, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.altar.1.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3741 */     createItemTemplate(1046, "rift device", "devices", "solid", "damaged", "brittle", "fragile", "This seems to be a device for something.", new short[] { 49, 31, 52, 67, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.altar.1.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3748 */     createItemTemplate(1047, "rift device", "devices", "solid", "damaged", "brittle", "fragile", "This seems to be a device for something.", new short[] { 49, 31, 52, 67, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.altar.1.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3755 */     createItemTemplate(1048, "rift device", "devices", "solid", "damaged", "brittle", "fragile", "This seems to be a device for something.", new short[] { 49, 31, 52, 67, 123, 178 }, (short)60, (short)1, 0, 604800L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.rift.altar.1.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3762 */     ItemTemplateCreatorThird.initializeTemplates();
/* 3763 */     createItemTemplate(1097, "gift pack", "giftpacks", "superb", "good", "ok", "poor", "A gift pack containing something interesting.", new short[] { 53, 48, 108, 52, 21, 83, 42, 147, 157 }, (short)243, (short)1, 0, 9072000L, 10, 10, 31, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.giftbox.", 10.0F, 23, (byte)14, 100, false);
/*      */     
/* 3765 */     createItemTemplate(1098, "returner tool chest", "chests", "superb", "good", "ok", "poor", "A gift pack containing something interesting.", new short[] { 53, 48, 108, 52, 21, 83, 42, 157 }, (short)243, (short)1, 0, 9072000L, 10, 10, 31, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.giftbox.", 10.0F, 23, (byte)14, 100, false);
/*      */     
/* 3767 */     createItemTemplate(1099, 3, "mask of the returner", "masks", "excellent", "good", "ok", "poor", "A mask.", new short[] { 108, 44, 23, 4, 187, 157 }, (short)465, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 29 }, "model.armour.head.mask.returner.", 25.0F, 200, (byte)16, 10000, true, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3773 */     createItemTemplate(1102, "rift stone shard", "rift stone shards", "superb", "good", "ok", "poor", "Some pieces of stone gathered from a defeated rift. Can be crafted together with metal to create a rune that will attach to stone, leather, cloth and pottery items.", new short[] { 25, 146, 46, 112, 129, 48, 157 }, (short)610, (short)46, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.stone.rift.", 40.0F, 200, (byte)15, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3781 */     createItemTemplate(1103, "rift crystal", "rift crystals", "superb", "good", "ok", "poor", "Some pieces of crystal gathered from a defeated rift. Can be crafted together with metal to create a rune that will attach to metal items.", new short[] { 22, 146, 46, 112, 129, 48, 157 }, (short)60, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.crystal.", 40.0F, 200, (byte)52, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3789 */     createItemTemplate(1104, "rift wood", "rift wood", "superb", "good", "ok", "poor", "Some pieces of hardened wood gathered from a defeated rift. Can be crafted together with metal to create a rune that will attach to wooden items.", new short[] { 21, 146, 46, 112, 211, 129, 48, 157 }, (short)646, (short)1, 10, 28800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.beam.hardened.", 40.0F, 200, (byte)14, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3798 */     createItemTemplate(1307, "fragment", "fragments", "superb", "good", "ok", "poor", "A fragment of another item. You think you can see how multiples of these fragments may fit together in a way that restores the original item.", new short[] { 48, 157, 187, 233, 246 }, (short)1460, (short)1, 0, 12096000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fragment.", 30.0F, 1000, (byte)0, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3805 */     ItemTemplateCreatorKingdom.initializeTemplates();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateCreatorContinued.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */