package com.tiger.annatation_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.tiger.annatation.Inject;
import com.tiger.annatation.InjectSetClass;
import com.tiger.annatation.InjectSingle;
import com.tiger.annatation.InjectSingleSubclass;
import com.tiger.annatation.InjectSubclass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {
    public static Map<String, String> mSubClassMap = new HashMap<>();
    public Map<TypeElement, VariableProxy> mTvap = new HashMap<>();
    private Filer filer;
    private Elements elementUtils;
    private TypeElement typeElement;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(Inject.class);
        Set<? extends Element> elementSet2 = roundEnvironment.getElementsAnnotatedWith(InjectSingle.class);
        Set<? extends Element> elementSet3 = roundEnvironment.getElementsAnnotatedWith(InjectSubclass.class);
        Set<? extends Element> elementSet4 = roundEnvironment.getElementsAnnotatedWith(InjectSingleSubclass.class);
        Set<? extends Element> elementSet5 = roundEnvironment.getElementsAnnotatedWith(InjectSetClass.class);
        for (Element element : elementSet) {
            getVariable(element);
        }
        for (Element element : elementSet4) {
            getVariable(element);
        }
        for (Element element : elementSet2) {
            getVariable(element);
        }
        for (Element element : elementSet3) {
            getVariable(element);
        }
        for (Element element : elementSet5) {
            TypeElement variableElement = (TypeElement) element;
            mSubClassMap.put(variableElement.getSimpleName().toString(), variableElement.getQualifiedName().toString());
        }
        createFile();
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add(Inject.class.getCanonicalName());
        hashSet.add(InjectSingle.class.getCanonicalName());
        hashSet.add(InjectSubclass.class.getCanonicalName());
        hashSet.add(InjectSingleSubclass.class.getCanonicalName());
        hashSet.add(InjectSetClass.class.getCanonicalName());
        return hashSet;
    }

    private void getVariable(Element element) {
        VariableElement variableElement = (VariableElement) element;
        typeElement = (TypeElement) variableElement.getEnclosingElement();
        VariableProxy variableProxy;
        if (mTvap.containsKey(typeElement)) {
            variableProxy = mTvap.get(typeElement);
        } else {
            variableProxy = new VariableProxy();
            mTvap.put(typeElement, variableProxy);
        }
        variableProxy.addVariableElement(variableElement);
    }

    private void createFile() {
        for (TypeElement typeElement : mTvap.keySet()) {
            try {
                JavaFile.builder(elementUtils.getPackageOf(typeElement).getQualifiedName().toString(), mTvap.get(typeElement).generateJavaCodeByPoet(typeElement)).build().writeTo(filer);
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
                System.out.println("create file " + e.getMessage().toString());
            }
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
