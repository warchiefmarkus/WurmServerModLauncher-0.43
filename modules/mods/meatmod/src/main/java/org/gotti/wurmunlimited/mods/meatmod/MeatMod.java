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

	private void Debug(String msg) {
		System.out.println(msg);
		System.out.flush();
		this.logger.log(Level.INFO, msg);
	}

	public void configure(Properties properties) {
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
		Debug("CONFIGURE DEBUG OF MEAT MOD CREATED");
	}

	public void preInit() {}

	public void init() {
		Debug("INIT");

		// if ("com.wurmonline.server.items.Item".equals(m.getClassName()) && m.getMethodName().equals("insertItem"))
		//						m.replace("corpse.insertItem(toCreate, true); $_ = $proceed($$);");
		try {
			String descriptCreateResultMethods = Descriptor.ofMethod(CtClass.voidType, new CtClass[] { HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.skills.Skill"),
					HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"), CtClass.doubleType, CtClass.doubleType,

					HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureTemplate"), CtClass.intType });
			CtMethod ctCreateResult = HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.CorpseBehaviour").getMethod("createResult", descriptCreateResultMethods);
			ctCreateResult.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException {
					if ("com.wurmonline.server.items.Item".equals(m.getClassName()) && m.getMethodName().equals("getFat()"))
						m.replace("100; $_ = $proceed($$);");
				}
			});
		} catch (CannotCompileException|NotFoundException ex) {
			this.logger.log(Level.SEVERE, ex.getMessage(), (Object[])ex.getStackTrace());
		}
	}
}
