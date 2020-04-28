package com.ldongxu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 从ClassPath根下扫描basePackage目录下class
 * <p>
 * Created by 刘东旭 on 2017/7/21.
 */
public class ClasspathPackageScanner {
    private Logger logger = LoggerFactory.getLogger(ClasspathPackageScanner.class);
    private String basePackage;
    private ClassLoader cl;

    public ClasspathPackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = this.getClass().getClassLoader();
    }

    public ClasspathPackageScanner(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }

    public Set<String> getFullyQualifiedClassNameList() {
        this.logger.info("开始扫描包{}下的所有类", this.basePackage);
        Set<String> list = new HashSet<String>();
        return this.doScan(this.basePackage, list);
    }

    private Set<String> doScan(String basePackage, Set<String> classNameList) {
        String slashPath = dotToSlash(basePackage);
        URL url = cl.getResource(slashPath);
        if (url == null) {
            return classNameList;
        } else {
            String filePath = url.getPath();
            List<String> filenames = readFromDirectory(filePath);
            for (String filename : filenames) {
                if (isClassFile(filename)) {
                    classNameList.add(toFullyQualifiedName(filename, basePackage));
                } else {
                    doScan(basePackage + "." + filename, classNameList);
                }
            }
            return classNameList;
        }
    }

    /**
     * Convert short class name to fully qualified name.
     * e.g., String -> java.lang.String
     */
    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(trimExtension(shortName));
        return sb.toString();
    }

    /**
     * "Apple.class" -> "Apple"
     */
    public static String trimExtension(String name) {
        int pos = name.indexOf('.');
        if (-1 != pos) {
            return name.substring(0, pos);
        }

        return name;
    }

    /**
     * "com.guagua.service" -> "com/guagua/service"
     */
    private String dotToSlash(String basePackage) {
        return basePackage.replaceAll("\\.", "/");
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();
        if (null == names) {
            return null;
        }
        return Arrays.asList(names);
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    public static void main(String[] args) {
        ClasspathPackageScanner cps = new ClasspathPackageScanner("com.guagua.service");
        Set<String> l = cps.getFullyQualifiedClassNameList();
        for (String ll : l) {
            System.out.println(ll);
        }
    }
}
