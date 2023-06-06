package services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for loading classes using the class loader.
 */
public class LoaderService {

    /**
     * Finds all classes in a package using the class loader.
     *
     * @param packageName The name of the package.
     * @return A set of classes found in the package.
     */
    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a class object from its name and package name.
     *
     * @param className    The name of the class.
     * @param packageName  The name of the package.
     * @return The class object if found, or null if not found.
     */
    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // Class not found, return null
        }
        return null;
    }
}