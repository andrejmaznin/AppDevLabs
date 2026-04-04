package specifications;

import models.Mission;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class FullTextSpecification implements MissionSpecification {
    private final String[] tokens;

    public FullTextSpecification(String query) {
        if (query == null || query.trim().isEmpty()) {
            this.tokens = new String[0];
        } else {
            String normalizedQuery = query.toLowerCase().replaceAll("[\\p{Punct}]", " ");
            this.tokens = normalizedQuery.split("\\s+");
        }
    }

    @Override
    public boolean isSatisfiedBy(Mission mission) {
        if (tokens.length == 0) return true;

        StringBuilder sb = new StringBuilder();

        extractTextViaReflection(mission, sb, new HashSet<>());

        String allText = sb.toString().toLowerCase().replaceAll("[\\p{Punct}]", " ");

        for (String token : tokens) {
            if (!allText.contains(token)) {
                return false;
            }
        }
        return true;
    }

    private void extractTextViaReflection(Object obj, StringBuilder sb, Set<Object> visited) {
        if (obj == null || visited.contains(obj)) {
            return;
        }

        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj.getClass().isPrimitive()) {
            sb.append(obj).append(" ");
            return;
        }

        visited.add(obj);

        if (obj instanceof Iterable<?>) {
            for (Object item : (Iterable<?>) obj) {
                extractTextViaReflection(item, sb, visited);
            }
            return;
        }

        if (!obj.getClass().getName().startsWith("models")) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                extractTextViaReflection(value, sb, visited);
            } catch (IllegalAccessException e) {
                // Если доступ получить не удалось, просто пропускаем поле
            }
        }
    }
}