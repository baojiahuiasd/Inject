package com.tiger.annatation_processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.tiger.annatation.Inject;
import com.tiger.annatation.InjectSingle;
import com.tiger.annatation.InjectSingleSubclass;
import com.tiger.annatation.InjectSubclass;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class VariableProxy {
    private List<VariableElement> variableElements = new ArrayList<>();

    private String privates = " try {\n";
    private boolean addPrivates;
    private MethodSpec.Builder methodBuilder;

    public void addVariableElement(VariableElement variableElement) {
        for (VariableElement v : variableElements) {
            if (v.getSimpleName().toString().equals(variableElement.getSimpleName().toString())) {
                return;
            }
        }
        variableElements.add(variableElement);
    }

    public TypeSpec generateJavaCodeByPoet(TypeElement typeElement) throws ClassNotFoundException {
        methodBuilder = MethodSpec.methodBuilder("bind").addModifiers(Modifier.PUBLIC).returns(void.class).
                addParameter(ClassName.bestGuess(typeElement.getQualifiedName().toString()), "host");
        for (VariableElement variableElement : variableElements) {
            String variableName = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
            if (!variableElement.getModifiers().contains(Modifier.PUBLIC)) {
                addPrivates = true;
                getPrivates(variableElement, variableName, type);
            } else {
                getPublic(variableElement, variableName, type);
            }
        }
        addPrivates();
        TypeSpec build = TypeSpec.classBuilder(typeElement.getSimpleName().toString() + "_IntjectBinding").addModifiers(Modifier.PUBLIC).addMethod(methodBuilder.build()).build();
        return build;
    }

    private void getPublic(VariableElement variableElement, String variableName, String type) throws ClassNotFoundException {
        if (variableElement.getAnnotation(Inject.class) != null) {
            methodBuilder.addCode("host." + variableName + "=" + "new " + type + "();");
        } else if (variableElement.getAnnotation(InjectSingle.class) != null) {
            methodBuilder.addCode("host." + variableName + "=" + type + ".getInstance();");
        } else if (variableElement.getAnnotation(InjectSubclass.class) != null) {
            String annotation = variableElement.getAnnotation(InjectSubclass.class).value();
            if (Processor.mSubClassMap.containsKey(annotation)) {
                methodBuilder.addCode("host." + variableName + "=(" + type + ")new " + Processor.mSubClassMap.get(annotation) + "();");
            } else {
                throw new ClassNotFoundException("no found" + annotation + ", can you ues InjectSetClass to statement");
            }
        } else if (variableElement.getAnnotation(InjectSingleSubclass.class) != null) {
            String annotation = variableElement.getAnnotation(InjectSingleSubclass.class).value();
            if (Processor.mSubClassMap.containsKey(annotation)) {
                methodBuilder.addCode("host." + variableName + "=(" + type + ")" + Processor.mSubClassMap.get(annotation) + ".getInstance();");
            } else {
                throw new ClassNotFoundException("no found" + annotation + ", can you ues InjectSetClass to statement");
            }
        }
    }

    private void addPrivates() {
        privates = privates + "} catch (IllegalAccessException | NoSuchFieldException ex) {\n" +
                "            ex.printStackTrace();\n" +
                "        }";
        if (addPrivates) methodBuilder.addCode(privates);
    }

    void getPrivates(VariableElement variableElement, String variableName, String type) throws ClassNotFoundException {
        privates += "\n" + "java.lang.reflect.Field " + variableName + "=host.getClass().getDeclaredField(" + "\"" + variableName + "\"" + ");";
        privates += "\n" + variableName + ".setAccessible(true);";
        if (variableElement.getAnnotation(Inject.class) != null) {
            privates += "\n" + variableName + ".set(host, " + "new " + type + "()" + ");";
        } else if (variableElement.getAnnotation(InjectSingle.class) != null) {
            privates += "\n" + variableName + ".set(host, " + type + ".getInstance());";
        } else if (variableElement.getAnnotation(InjectSingleSubclass.class) != null) {
            String annotation = variableElement.getAnnotation(InjectSingleSubclass.class).value();
            if (Processor.mSubClassMap.containsKey(annotation)) {
                privates += "\n" + variableName + ".set(host, " + Processor.mSubClassMap.get(annotation) + ".getInstance());";
            } else {
                throw new ClassNotFoundException("no found" + annotation + ", can you ues InjectSetClass to statement");
            }
        } else if (variableElement.getAnnotation(InjectSubclass.class) != null) {
            String annotation = variableElement.getAnnotation(InjectSubclass.class).value();
            if (Processor.mSubClassMap.containsKey(annotation)) {
                privates += "\n" + variableName + ".set(host, " + "new " + Processor.mSubClassMap.get(annotation) + "());";
            } else {
                throw new ClassNotFoundException("no found " + annotation + ", can you ues InjectSetClass to statement");
            }
        }
    }
}
