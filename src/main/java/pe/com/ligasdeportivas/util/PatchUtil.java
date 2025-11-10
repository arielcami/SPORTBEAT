package pe.com.ligasdeportivas.util;

import java.lang.reflect.Field;
import java.util.Map;

public class PatchUtil {

    public static <T> void applyPatch(T entity, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = entity.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object convertedValue = convertValue(field.getType(), value);
                field.set(entity, convertedValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Campo no válido: " + key, e);
            }
        });
    }

    private static Object convertValue(Class<?> targetType, Object value) {
        if (value == null) {
            return null;
        }

        // Si los tipos son compatibles, retornar directamente
        if (targetType.isInstance(value)) {
            return value;
        }

        String stringValue = value.toString();

        // Conversiones específicas por tipo
        try {
            if (targetType.isEnum()) {
                return convertToEnum(targetType, stringValue);
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.valueOf(stringValue);
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.valueOf(stringValue);
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.valueOf(stringValue);
            } else if (targetType == Float.class || targetType == float.class) {
                return Float.valueOf(stringValue);
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.valueOf(stringValue);
            } else if (targetType == String.class) {
                return stringValue;
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error convirtiendo valor '" + value + "' al tipo " + targetType.getSimpleName(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error convirtiendo valor '" + value + "' al tipo " + targetType.getSimpleName(), e);
        }

        // Si no podemos convertir, lanzar excepción
        throw new RuntimeException("No se puede convertir valor '" + value + "' al tipo " + targetType.getSimpleName());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object convertToEnum(Class<?> enumType, String value) {
        return Enum.valueOf((Class<Enum>) enumType, value);
    }
}
