package com.general.mbts4ma.view.framework.util;

import java.io.File;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

public class PageObject {

	private String className;
	private String path;
	private String content;

	/*public PageObject(String className, CtClass parsedClass) {
		this.className = className;
		this.parsedClass = parsedClass;		
	}*/
	
	public PageObject(String className, String content) {
		this.className = className;
		this.content = content;
		//this.content = this.getContentByPath(path);
	}
	
	public String getClassName(){
		return this.className;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getContent(){
		return this.content;
	}
	
	/*public String getContentByPath(String path) {
		String fileContent = FileUtil.readFile(new File(path));
		this.parsedClass = Launcher.parseClass(fileContent);
		
		return fileContent;
	}*/
	
	public void refreshContentByPath() {
		String fileContent = FileUtil.readFile(new File(this.path));
		
		this.content = fileContent;
	}
		
	public CtClass getParsedClass() {
		
		return SpoonUtil.getCtClassFromClassContent(this.content);
	}
	
	public void createNewAbstractMethod(CtMethod method) {
		
		CtClass clazz = this.getParsedClass();
		
		clazz.addMethod(method);
		
		this.content = clazz.toString();
	}
	
}
