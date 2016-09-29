package ch.awae.forcedInit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

public final class Initializer {

	private Initializer() {
		throw new IllegalAccessError();
	}

	private static Class<?> initializeClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}

	public static Collection<Class<?>> initializePackage(String pkg) throws ClassNotFoundException {
		Set<Class<?>> classes = new Reflections(pkg).getTypesAnnotatedWith(ForceInit.class);

		for (Class<?> c : classes)
			initializeClass(c.getName());

		return classes;
	}

	public static Collection<Class<?>> initializePackage(String pkg, String[] categories)
			throws ClassNotFoundException {
		List<Class<?>> list = new ArrayList<>();
		for (String cat : categories)
			list.addAll(initializePackage(pkg, cat));

		return list;
	}

	public static Collection<Class<?>> initializePackage(String pkg, String category) throws ClassNotFoundException {
		Set<Class<?>> classes = new Reflections(pkg).getTypesAnnotatedWith(ForceInit.class);

		List<Class<?>> list = new ArrayList<>();

		for (Class<?> c : classes) {
			String[] categories = c.getAnnotation(ForceInit.class).value();
			catLoop: for (String cat : categories)
				if (cat.equals(category)) {
					initializeClass(c.getName());
					list.add(c);
					break catLoop;
				}
		}

		return list;

	}

	public static Collection<Class<?>> initializeAll() throws ClassNotFoundException {
		List<Class<?>> list = new ArrayList<>();

		for (Package p : Package.getPackages())
			list.addAll(initializePackage(p.getName()));

		return list;
	}

	public static Collection<Class<?>> initializeAll(String[] categories) throws ClassNotFoundException {
		List<Class<?>> list = new ArrayList<>();
		for (String cat : categories)
			list.addAll(initializeAll(cat));
		return list;
	}

	public static Collection<Class<?>> initializeAll(String category) throws ClassNotFoundException {
		List<Class<?>> list = new ArrayList<>();
		for (Package p : Package.getPackages())
			list.addAll(initializePackage(p.getName(), category));
		return list;
	}

}
