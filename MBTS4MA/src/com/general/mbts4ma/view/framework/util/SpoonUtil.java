package com.general.mbts4ma.view.framework.util;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

public class SpoonUtil {

	public static CtMethod<?> getCtMethodFromMethodSignatureAndClassName(String methodSignature, String className, Launcher launcher) {
		
		List<CtType<?>> classesList = launcher.getFactory().Class().getAll();
		
		for (CtType<?> clazz : classesList) {
						
			if (clazz.getSimpleName().equals(className) || clazz.getQualifiedName().equals(className)) {
			
				Set<CtMethod<?>> methods = clazz.getMethods();
				
				for (CtMethod<?> ctMethod : methods) {
					if (ctMethod.getSignature().equals(methodSignature))
						return ctMethod;
				}
				
			}		
			
		}
		
		
		
		
		return null;
		
	}
	
	public static CtConstructor<?> getConstructorFromMethodSignatureAndClassName(String methodSignature, String className, Launcher launcher) {
		
		List<CtType<?>> classesList = launcher.getFactory().Class().getAll();
		
		for (CtType<?> clazz : classesList) {
			
			CtClass<?> ctClass = (CtClass<?>)clazz;						
			
			if (clazz.getSimpleName().equals(className) || clazz.getQualifiedName().equals(className)) {
			
				Set<?> constructors = ctClass.getConstructors(); 
				
				for (Object ctType : constructors) {
					CtConstructor<?> constructor = (CtConstructor<?>)ctType;
					
					if (constructor.getSignature().equals(methodSignature) || constructor.getSimpleName().equals(methodSignature))
						return constructor;
				}
			}
		}
		
		return null;
	}
	
}
