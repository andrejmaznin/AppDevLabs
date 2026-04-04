package reports;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Method;
import java.util.*;

public class UniversalTreeGenerator implements ReportGenerator<SmartTreeReport, Object> {

    @Override
    public SmartTreeReport generate(Object anyDataModel) {
        if (anyDataModel == null) {
            return new SmartTreeReport(new DefaultMutableTreeNode("Нет данных"));
        }

        String rootName = anyDataModel.getClass().getSimpleName();
        DefaultMutableTreeNode root = buildTreeNode(anyDataModel, rootName, new IdentityHashMap<>(), 0);

        return new SmartTreeReport(root);
    }

    private DefaultMutableTreeNode buildTreeNode(Object obj, String name, Map<Object, Boolean> visited, int depth) {
        if (depth > 8) return new DefaultMutableTreeNode(name + ": (max depth)");
        if (obj == null) return new DefaultMutableTreeNode(name + ": null");

        Class<?> cls = obj.getClass();
        if (isSimple(cls)) {
            return new DefaultMutableTreeNode(name + ": " + obj);
        }

        if (visited.containsKey(obj)) {
            return new DefaultMutableTreeNode(name + ": (circular)");
        }
        visited.put(obj, Boolean.TRUE);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name + " (" + cls.getSimpleName() + ")");

        if (obj instanceof Collection) {
            int i = 0;
            for (Object item : (Collection<?>) obj) {
                node.add(buildTreeNode(item, "[" + i + "]", visited, depth + 1));
                i++;
            }
        } else if (obj.getClass().isArray()) {
            int len = java.lang.reflect.Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object item = java.lang.reflect.Array.get(obj, i);
                node.add(buildTreeNode(item, "[" + i + "]", visited, depth + 1));
            }
        } else {
            Method[] methods = cls.getMethods();
            List<Method> getters = new ArrayList<>();
            for (Method m : methods) {
                if (m.getParameterCount() == 0) {
                    String n = m.getName();
                    if ((n.startsWith("get") && !n.equals("getClass")) || n.startsWith("is")) {
                        getters.add(m);
                    }
                }
            }
            getters.sort(Comparator.comparing(Method::getName));
            for (Method g : getters) {
                try {
                    Object val = g.invoke(obj);
                    String propName = nameFromGetter(g.getName());
                    node.add(buildTreeNode(val, propName, visited, depth + 1));
                } catch (Exception e) {
                    node.add(new DefaultMutableTreeNode(g.getName() + ": (error)"));
                }
            }
        }

        visited.remove(obj);
        return node;
    }

    private boolean isSimple(Class<?> cls) {
        if (cls.isPrimitive() || cls == String.class || Number.class.isAssignableFrom(cls) ||
            cls == Boolean.class || cls == Character.class || cls.isEnum()) {
            return true;
        }
        return false;
    }

    private String nameFromGetter(String getterName) {
        if (getterName.startsWith("get") && getterName.length() > 3) {
            return Character.toLowerCase(getterName.charAt(3)) + getterName.substring(4);
        }
        if (getterName.startsWith("is") && getterName.length() > 2) {
            return Character.toLowerCase(getterName.charAt(2)) + getterName.substring(3);
        }
        return getterName;
    }
}