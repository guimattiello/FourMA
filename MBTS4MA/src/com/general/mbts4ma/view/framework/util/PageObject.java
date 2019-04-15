package com.general.mbts4ma.view.framework.util;

import java.io.File;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;

public class PageObject {

	private String className;
	private String path;
	private String content;
	private CtClass parsedClass;
	
	
	
	public PageObject(String className, CtClass parsedClass) {
		this.className = className;
		this.parsedClass = parsedClass;
		//this.content = this.getContentByPath(path);
	}
	
	public String getClassName(){
		return this.className;
	}
	
	public String getPath(){
		return this.path;
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
	
	public void setParsedClass(CtClass ctClass) {
		this.parsedClass = ctClass;
	}
	
	public CtClass getParsedClass() {
		return this.parsedClass;
	}
	
}
