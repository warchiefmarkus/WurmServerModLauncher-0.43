package org.gotti.wurmunlimited.mods.meatmod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Descriptor;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

public class MeatMod implements WurmServerMod, Configurable, Initable, PreInitable {
	private Logger logger = Logger.getLogger(getClass().getName());

    private int multiplier = 1;

	private void Debug(String msg) {
		System.out.println(msg);
		System.out.flush();
		this.logger.log(Level.INFO, msg);
	}

    // The method configure is called when the mod is being loaded
    @Override
    public void configure(Properties properties) {
        multiplier = Integer.valueOf(properties.getProperty("multiplier", Integer.toString(multiplier)));
        logger.log(Level.INFO, "multiplier: " + multiplier);

        // DEBUGER
        try {
            String logsPath = Paths.get("mods", new String[0]) + "/logs/";
            File newDirectory = new File(logsPath);
            if (!newDirectory.exists())
                newDirectory.mkdirs();
            FileHandler fh = new FileHandler(String.valueOf(String.valueOf(String.valueOf(logsPath))) + getClass().getSimpleName() + ".log", 10240000, 200, true);
            fh.setFormatter(new SimpleFormatter());
            this.logger.addHandler(fh);
        } catch (IOException ie) {
            System.err.println(String.valueOf(String.valueOf(getClass().getName())) + ": Unable to add file handler to logger");
        }
    }

	public void preInit() {}

	public void init() {
		Debug("INIT");

		try {
			String descriptCreateResultMethods = Descriptor.ofMethod(CtClass.voidType, new CtClass[]{
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.skills.Skill"),
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"), CtClass.doubleType, CtClass.doubleType,
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureTemplate"), CtClass.intType});

			CtMethod ctCreateResult = HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.CorpseBehaviour").getMethod("createResult", descriptCreateResultMethods);

			ctCreateResult.setBody(
					"    try {"+
							"      int[] itemnums = $7.getItemsButchered();"+
							"      String creatureName = \"\";"+
							"      boolean dryRun = true;"+
							"      creatureName = $7.getName().toLowerCase();"+
							"      dryRun = $7.isHuman();"+
							"      com.wurmonline.server.items.ItemTemplate meattemplate = null;"+
							"      int meatType = ($7.getTemplateId() == 95) ? 900 : 92;"+
							"      try {"+
							"        meattemplate = com.wurmonline.server.items.ItemTemplateFactory.getInstance().getTemplate(meatType);"+
							"      } catch (com.wurmonline.server.items.NoSuchTemplateException nst) {"+
							"        logger.log(java.util.logging.Level.WARNING, \"No template for meat!\");"+
							"      } "+
							"      boolean createMeat = false;"+
							"      if ((!dryRun && $7.isNeedFood()) || $7.isAnimal()) {"+
							"        createMeat = true;"+
							"      } else if ($1.getKingdomTemplateId() == 3) {"+
							"        if (!$7.isNoSkillgain())"+
							"          createMeat = true; "+
							"      } "+
							"      if (meattemplate != null && $2.getWeightGrams() < meattemplate.getWeightGrams())"+
							"        createMeat = false; "+
							"      if ($7.isKingdomGuard())"+
							"        createMeat = false; "+
							"      int diffAdded = 0;"+
							"      if ($4.getTemplateId() != 93)"+
							"        diffAdded = 1; "+
							"      if (createMeat && meattemplate != null) {"+
							"        int max = $4.getRarity() + $8 / 10;"+
							"        for (int i = 0; i < max; i++) {"+
							"          $5 = $3.skillCheck((double)com.wurmonline.server.Server.rand.nextInt((i + 1 + diffAdded) * 3), $4, $6, dryRun, 1.0F);"+
							"          if ($4.getSpellEffects() != null) {"+
							"            float imbueEnhancement = 1.0F + $4.getSkillSpellImprovement(10059) / 100.0F;"+
							"            $5 *= ($4.getSpellEffects().getRuneEffect(com.wurmonline.server.items.RuneUtilities.ModifierEffect.ENCH_RESGATHERED) * imbueEnhancement);"+
							"          } "+
							"          if (($5 > 0.0D && $4.getTemplateId() == 93) || i == 0)"+
							"            try {"+
							"              com.wurmonline.server.items.Item toCreate = com.wurmonline.server.items.ItemFactory.createItem(meatType, "+
							"                  Math.min(" +
							"							(float)Math.max((1.0D + com.wurmonline.server.Server.rand.nextFloat() * (10 + $4.getRarity() * 5)), " +
							"							                 Math.min(100.0D, (double)$5)), " +
							"							(float)$2.getCurrentQualityLevel()), " +
							"null);"+
							"              toCreate.setData2($2.getData1());"+
							"              toCreate.setMaterial($7.getMeatMaterial());"+
							"              toCreate.setWeight(((int)Math.min($2.getWeightGrams() * 0.5F, (float)(meattemplate.getWeightGrams() * $7.getSize()) ))*"+multiplier+", true);"+
							"              if (toCreate.getWeightGrams() != 0) {"+
							"                $2.insertItem(toCreate, true);"+
							"                $1.getCommunicator().sendNormalServerMessage(\"You produce YES \" + toCreate.getNameWithGenus() + \".\");"+
							"              } "+
							"            } catch (com.wurmonline.server.items.NoSuchTemplateException nst) {"+
							"              logger.log(java.util.logging.Level.WARNING, \"No template for meat!\");"+
							"            }  "+
							"        } "+
							"      } "+
							"      for (int x = 0; x < itemnums.length; x++) {"+
							"        if (!createMeat || (itemnums[x] != 92 && itemnums[x] != 900))"+
							"          try {"+
							"            meattemplate = com.wurmonline.server.items.ItemTemplateFactory.getInstance().getTemplate(itemnums[x]);"+
							"            $5 = $3.skillCheck((double)com.wurmonline.server.Server.rand.nextInt((x + 1 + diffAdded) * 10), $4, 0.0D, dryRun, 1.0F);"+
							"            if ($4.getSpellEffects() != null) {"+
							"              float imbueEnhancement = 1.0F + $4.getSkillSpellImprovement(10059) / 100.0F;"+
							"              $5 *= ($4.getSpellEffects().getRuneEffect(com.wurmonline.server.items.RuneUtilities.ModifierEffect.ENCH_RESGATHERED) * imbueEnhancement);"+
							"            } "+
							"            if ($5 > 0.0D) {"+
							"              com.wurmonline.server.items.Item toCreate = com.wurmonline.server.items.ItemFactory.createItem(itemnums[x], " +
							"                 Math.min((float)$5 + com.wurmonline.server.Server.rand.nextFloat() * $4.getRarity() * 5.0F, (float)$2.getCurrentQualityLevel()), null);"+
							"              toCreate.setData2($2.getData1());"+
							"              if (toCreate.getTemplateId() != 683) {"+
							"                if (!toCreate.getName().contains(creatureName)){"+
							"                  toCreate.setName(creatureName.toLowerCase() + \" \" + meattemplate.getName());}"+
							"                float modWeight = meattemplate.getWeightGrams() * $7.getSize();"+
							"                toCreate.setWeight((int)Math.min($2.getWeightGrams() * 0.5F, (float)modWeight), true);"+
							"                if (toCreate.getTemplateId() == 867)"+
							"                  if (com.wurmonline.server.Server.rand.nextInt(250) == 0) {"+
							"                    toCreate.setRarity((byte)3);"+
							"                  } else if (com.wurmonline.server.Server.rand.nextInt(50) == 0) {"+
							"                    toCreate.setRarity((byte)2);"+
							"                  } else {"+
							"                    toCreate.setRarity((byte)1);"+
							"                  }  "+
							"              } "+
							"              if (toCreate.getWeightGrams() != 0) {"+
							"                toCreate.setLastOwnerId($1.getWurmId());"+
							"                $2.insertItem(toCreate, true);"+
							"                $1.getCommunicator().sendNormalServerMessage(\"You produce YES\" + toCreate.getNameWithGenus() + \".\");"+
							"              } "+
							"            } else {"+
							"              $1.getCommunicator().sendNormalServerMessage(\"You fail to produce \" + meattemplate.getNameWithGenus() + \".\");"+
							"            } "+
							"          } catch (com.wurmonline.server.items.NoSuchTemplateException nst) {"+
							"            logger.log(java.util.logging.Level.WARNING, \"No template for item id \" + itemnums[x]);"+
							"          }  "+
							"      } "+
							"      if ($7.isFromValrei)"+
							"        try {"+
							"          if ($5 > 0.0D) {"+
							"            int chanceModifier = 1;"+
							"            if (com.wurmonline.server.Server.rand.nextInt(30 * chanceModifier) == 0) {"+
							"              com.wurmonline.server.items.Item seryll = com.wurmonline.server.items.ItemFactory.createItem(837, "+
							"                  Math.min((float)($5 + com.wurmonline.server.Server.rand.nextFloat() * $4.getRarity() * 5.0F), (float)(70.0F + com.wurmonline.server.Server.rand.nextFloat() * 5.0F)), null);"+
							"              seryll.setLastOwnerId($1.getWurmId());"+
							"              $2.insertItem(seryll, true);"+
							"              $1.getCommunicator().sendNormalServerMessage(\"You manage to extract some seryll from the cranium.\");"+
							"            } "+
							"            if (com.wurmonline.server.Server.rand.nextInt(60 * chanceModifier) == 0) {"+
							"              int num = 871 + com.wurmonline.server.Server.rand.nextInt(14);"+
							"              com.wurmonline.server.items.Item potion = com.wurmonline.server.items.ItemFactory.createItem(num, "+
							"                  Math.min((float)($5 + com.wurmonline.server.Server.rand.nextFloat() * $4.getRarity() * 5.0F), (float)(70.0F + com.wurmonline.server.Server.rand.nextFloat() * 5.0F)), null);"+
							"              potion.setLastOwnerId($1.getWurmId());"+
							"              $2.insertItem(potion, true);"+
							"              $1.getCommunicator().sendNormalServerMessage(\"You manage to extract some weird concoction from the liver.\");"+
							"            } "+
							"          } "+
							"        } catch (com.wurmonline.server.items.NoSuchTemplateException nst) {"+
							"          logger.log(java.util.logging.Level.WARNING, nst.getMessage(), (Throwable)nst);"+
							"        }  "+
							"    } catch (com.wurmonline.server.FailedException fe) {"+
							"      logger.log(java.util.logging.Level.WARNING, $1.getName() + \" had a problem with $2: \" + $2 + \", $3 skill: \" + $3 + \", $4: \" + $4 + \", template: \" + $7 + \", fatigue: \" + $8 + \" due to \" + fe.getMessage(), (Throwable)fe);"+
							"    } "
			);

		} catch (CannotCompileException|NotFoundException ex) {
			this.logger.log(Level.SEVERE, ex.getMessage(), (Object[])ex.getStackTrace());
		}
	}
}
