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
				
				methods = clazz.getAllMethods();
				
				for (CtMethod<?> ctMethod : methods) {
					if (ctMethod.getSignature().equals(methodSignature))
						return ctMethod;
				}
				
			}		
			
		}
		
		
		
		
		return null;
		
	}
	
	public static CtConstructor<?> getCtConstructorFromMethodSignatureAndClassName(String methodSignature, String className, Launcher launcher) {
		
		List<CtType<?>> classesList = launcher.getFactory().Class().getAll();
		
		for (CtType<?> clazz : classesList) {
			
			CtClass<?> ctClass = (CtClass<?>)clazz;
			
			if (clazz.getSimpleName().toLowerCase().equals(className.toLowerCase()) || clazz.getQualifiedName().toLowerCase().equals(className.toLowerCase())) {
			
				Set<?> constructors = ctClass.getConstructors(); 
				
				for (Object ctType : constructors) {
					CtConstructor<?> constructor = (CtConstructor<?>)ctType;
					
					if (constructor.getSignature().equals(methodSignature) || constructor.getSimpleName().equals(methodSignature))
						return constructor;
				}
				
				for (Object ctType : constructors) {
					CtConstructor<?> constructor = (CtConstructor<?>)ctType;
					
					return constructor;
				}
			}
		}
		
		return null;
	}
	
	public static CtMethod<?> getMethodBySignature(String signature, List<CtType<?>> classesList, String className) {
		
		for (CtType<?> type : classesList) {
			Set<CtMethod<?>> ctMethods = type.getMethods();
			for (CtMethod<?> method : ctMethods) { 
				if (method.getSignature().equals(signature) && (className.equals(type) || (className == null))) {
					return method;
				}
			}
		}
		
		return null;
		
	}
	
	public static String getSignatureFromMethodTemplate(String methodTemplate) {
		if (methodTemplate.indexOf("::") != -1) {
			methodTemplate = methodTemplate.replace("\n", "");
			String[] aux = methodTemplate.split("::");
			return aux[1];
		}
		
		return methodTemplate;
	}
	
}
