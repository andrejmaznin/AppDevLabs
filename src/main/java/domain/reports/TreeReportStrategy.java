package domain.reports;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TreeReportStrategy implements ReportStrategy {

    @Override
    public TreeReport generate(Object anyDataModel) {
        if (anyDataModel == null) {
            return new TreeReport(new ReportTreeNode("Нет данных"));
        }

        String rootName = anyDataModel.getClass().getSimpleName();
        ReportTreeNode root = buildTreeNode(anyDataModel, rootName, new IdentityHashMap<>(), 0);

        return new TreeReport(root);
    }

    private ReportTreeNode buildTreeNode(Object obj, String name, Map<Object, Boolean> visited, int depth) {
        if (depth > 8) return new ReportTreeNode(name + ": (max depth)");
        if (obj == null) return new ReportTreeNode(name + ": null");

        Class<?> cls = obj.getClass();
        if (isSimple(cls)) {
            return new ReportTreeNode(name + ": " + obj);
        }

        if (visited.containsKey(obj)) {
            return new ReportTreeNode(name + ": (circular)");
        }
        visited.put(obj, Boolean.TRUE);

        ReportTreeNode node = new ReportTreeNode(name + " (" + cls.getSimpleName() + ")");

        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            if (col.isEmpty()) return new ReportTreeNode(name + ": []");
            int i = 0;
            for (Object item : col) {
                node.addChild(buildTreeNode(item, "[" + i + "]", visited, depth + 1));
                i++;
            }
        } else if (obj.getClass().isArray()) {
            int len = java.lang.reflect.Array.getLength(obj);
            if (len == 0) return new ReportTreeNode(name + ": []");
            for (int i = 0; i < len; i++) {
                Object item = java.lang.reflect.Array.get(obj, i);
                node.addChild(buildTreeNode(item, "[" + i + "]", visited, depth + 1));
            }
        } else {
            Method[] methods = cls.getMethods();
            List<Method> getters = new ArrayList<>();
            for (Method m : methods) {
                if (m.getParameterCount() == 0 && !Modifier.isStatic(m.getModifiers())) {
                    String n = m.getName();
                    if (((n.startsWith("get") && n.length() > 3) || (n.startsWith("is") && n.length() > 2)) 
                        && !n.equals("getClass") && !n.equals("getDeclaringClass")) {
                        getters.add(m);
                    }
                }
            }
            getters.sort(Comparator.comparing(Method::getName));
            
            boolean hasChildren = false;
            for (Method g : getters) {
                try {
                    Object val = g.invoke(obj);
                    if (val == null) continue;
                    
                    String propName = nameFromGetter(g.getName());
                    node.addChild(buildTreeNode(val, propName, visited, depth + 1));
                    hasChildren = true;
                } catch (Exception e) {
                }
            }
            
            if (!hasChildren) {
                return new ReportTreeNode(name + ": {}");
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
