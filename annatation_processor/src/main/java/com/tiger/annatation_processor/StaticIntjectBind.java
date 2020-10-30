package com.tiger.annatation_processor;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;


import javax.lang.model.element.Modifier;



public class StaticIntjectBind {
    private MethodSpec.Builder methodBuilder;

    public  TypeSpec  generateJavaCodeByPoet() {
        addBind();
        TypeSpec build = TypeSpec.classBuilder("IntjectStaticBind").addModifiers(Modifier.PUBLIC).addMethod(methodBuilder.build()).build();
        return build;
    }
//try
//
//    {
//        Class<?> aClass = Class.forName(className);
//        Field field = aClass.getField(filesName);
//        field.setAccessible(true);
//        field.set(null, Class.forName(variableElement.asType().toString()).newInstance());
//    } catch(
//    ClassNotFoundException e)
//
//    {
//        e.printStackTrace();
//    } catch(
//    NoSuchFieldException e)
//
//    {
//        e.printStackTrace();
//    } catch(
//    IllegalAccessException e)
//
//    {
//        e.printStackTrace();
//    } catch(
//    InstantiationException e)
//
//    {
//        e.printStackTrace();
//    }

    private void addBind() {

        methodBuilder = MethodSpec.methodBuilder("bindStatic").addModifiers(Modifier.STATIC, Modifier.PUBLIC).returns(void.class).
                addParameter(ClassName.bestGuess("String"), "className").addParameter(ClassName.bestGuess("String"), "fileName").addParameter(ClassName.bestGuess("String"), "fileType");
        methodBuilder.addCode("Class bindViewClazz = null;");
        methodBuilder.addCode("     try {");
        methodBuilder.addCode("   Class<?> aClass = Class.forName(className);");
        methodBuilder.addStatement(" $T field = aClass.getField(fileName);", java.lang.reflect.Field.class);
        methodBuilder.addCode("     field.setAccessible(true);");
        methodBuilder.addCode("   field.set(null, Class.forName(fileType).newInstance());");
        methodBuilder.addCode("   } catch (ClassNotFoundException e) {\n" +
                "        e.printStackTrace();\n" +
                "    } catch (NoSuchFieldException e) {\n" +
                "        e.printStackTrace();\n" +
                "    } catch (IllegalAccessException e) {\n" +
                "        e.printStackTrace();\n" +
                "    } catch (InstantiationException e) {\n" +
                "        e.printStackTrace();\n" +
                "    }");


    }
}

