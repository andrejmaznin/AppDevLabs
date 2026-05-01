package domain.reports;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TreeReportStrategy implements ReportStrategy {

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
            Collection<?> col = (Collection<?>) obj;
            if (col.isEmpty()) return new DefaultMutableTreeNode(name + ": []");
            int i = 0;
            for (Object item : col) {
                node.add(buildTreeNode(item, "[" + i + "]", visited, depth + 1));
                i++;
            }
        } else if (obj.getClass().isArray()) {
            int len = java.lang.reflect.Array.getLength(obj);
            if (len == 0) return new DefaultMutableTreeNode(name + ": []");
            for (int i = 0; i < len; i++) {
                Object item = java.lang.reflect.Array.get(obj, i);
                node.add(buildTreeNode(item, "[" + i + "]", visited, depth + 1));
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
                    if (val == null) continue; // Пропускаем null для чистоты отчета
                    
                    String propName = nameFromGetter(g.getName());
                    node.add(buildTreeNode(val, propName, visited, depth + 1));
                    hasChildren = true;
                } catch (Exception e) {
                    // Игнорируем ошибки вызова
                }
            }
            
            if (!hasChildren) {
                return new DefaultMutableTreeNode(name + ": {}");
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
