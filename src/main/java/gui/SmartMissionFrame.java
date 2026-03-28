package gui;

import models.Mission;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.lang.reflect.Method;
import java.util.*;

public class SmartMissionFrame {
    private final JPanel mainPanel;
    private final JTree tree;


    public SmartMissionFrame(Mission mission) {
        mainPanel = new JPanel(new BorderLayout());
        DefaultMutableTreeNode root = buildTreeNode(mission, "mission", new IdentityHashMap<>(), 0);
        tree = new JTree(new DefaultTreeModel(root));
        tree.setCellRenderer(new SmartTreeRenderer());
        JScrollPane sp = new JScrollPane(tree);
        mainPanel.add(sp, BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
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
            java.util.List<Method> getters = new ArrayList<>();
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
        if (cls.isPrimitive()) return true;
        if (cls == String.class) return true;
        if (Number.class.isAssignableFrom(cls)) return true;
        if (cls == Boolean.class || cls == Character.class) return true;
        if (cls.isEnum()) return true;
        return false;
    }

    private String nameFromGetter(String getterName) {
        if (getterName.startsWith("get") && getterName.length() > 3) {
            return decap(getterName.substring(3));
        }
        if (getterName.startsWith("is") && getterName.length() > 2) {
            return decap(getterName.substring(2));
        }
        return getterName;
    }

    private String decap(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static void showInFrame(Mission mission) {
        JFrame f = new JFrame("Smart Mission Viewer");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SmartMissionFrame panel = new SmartMissionFrame(mission);
        f.setContentPane(panel.getMainPanel());
        f.setSize(700, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
