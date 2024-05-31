package concert.ticket.reservation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectUtils extends BaseObject {

    private static Logger log = LoggerFactory.getLogger(ObjectUtils.class);
    private static final long serialVersionUID = 2085589798501372168L;

    public static boolean isNotNull(Object object, String key) throws Exception {
        if (object != null) {
            if (object instanceof String && object.toString().trim().equals("")) {
                throw new Exception(key + " is empty");
            }
            return true;
        }
        throw new Exception(key + " is null");
    }

    @SuppressWarnings("unchecked")
    public static <F, T> List<T> convert(List<F> from, T t)
            throws Exception {
        List<T> to = new ArrayList<T>();
        try {
            for (F objectFrom : from) {
                T objectTo = (T) t.getClass().newInstance();
                objectTo = convert(objectFrom, objectTo);
                to.add(objectTo);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e);
        }
        return to;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <F, T> T convert(F from, T to) {
        List<Field> fields = new ArrayList<>();
        try {
            fields = getAllFields(from.getClass());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        for (Field fieldFrom : fields) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(fieldFrom.getName().charAt(0)).toUpperCase());
            sb.append(fieldFrom.getName().substring(1));

            Field fieldTo = null;
            try {
                try {
                    fieldTo = getField(to.getClass(), fieldFrom.getName());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                if (fieldTo == null)
                    continue;

                Object returnFrom = null;
                try {
                    Method method = null;
                    if (fieldFrom.getType().equals(Boolean.TYPE))
                        method = from.getClass().getMethod("is" + sb.toString(), new Class[]{});
                    else
                        method = from.getClass().getMethod("get" + sb.toString(), new Class[]{});
                    returnFrom = method.invoke(from, new Object[]{});
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                if (returnFrom instanceof BaseInterface || returnFrom instanceof BaseModel) {
                    // Create new instance of returnTo Object
                    Object returnTo = null;
                    try {
                        if (fieldTo.getType().isEnum()) {
                            // Invoke setter method on Object "to", in enum
                            // there is a special case where the original object
                            // can be set
                            // directly without conversion
                            to.getClass().getMethod("set" + sb.toString(), new Class[]{fieldTo.getType()})
                                    .invoke(to, returnFrom);
                            continue;
                        } else if (!fieldTo.getType().isInterface()) {
                            returnTo = fieldTo.getType().newInstance();
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                    // Converting
                    convert(returnFrom, returnTo);

                    // Invoke setter method on Object "to"
                    to.getClass().getMethod("set" + sb.toString(), new Class[]{fieldTo.getType()})
                            .invoke(to, returnTo);


                } else if (returnFrom instanceof Collection
                        || returnFrom instanceof List
                        || returnFrom instanceof Set) {

                    try {
                        Collection collection = (Collection) returnFrom;
                        String typeTo = fieldTo.getGenericType().toString();
                        String classTo = typeTo.substring(
                                typeTo.indexOf('<') + 1, typeTo.indexOf('>'));

                        Collection vos = new ArrayList();
                        for (Object objectFrom : collection) {

                            Class claszTo = Class.forName(classTo);
                            Object objectTo = null;
                            if (claszTo.isInterface()) {
                                String implementedClassName = new StringBuffer(
                                        claszTo.getPackage().getName())
                                        .append(".")
                                        .append(claszTo.getSimpleName())
                                        .toString();
                                objectTo = Class.forName(implementedClassName)
                                        .newInstance();
                            } else {
                                objectTo = Class.forName(classTo).newInstance();
                            }

                            convert(objectFrom, objectTo);

                            vos.add(objectTo);
                        }

                        // Invoke setter method on Object "to"
                        to.getClass()
                                .getMethod("set" + sb.toString(),
                                        new Class[]{fieldTo.getType()})
                                .invoke(to, vos);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                } else {

                    // Invoke setter method on Object "to"
                    try {
                        to.getClass()
                                .getMethod("set" + sb.toString(),
                                        new Class[]{fieldTo.getType()})
                                .invoke(to, returnFrom);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return to;
    }

    @SuppressWarnings("rawtypes")
    public static List<Field> getAllFields(Class clasz) {
        List<Field> fields = new ArrayList<Field>();
        while (!clasz.getName().equals("java.lang.Object")) {
            Field[] fieldsA = clasz.getDeclaredFields();
            fields.addAll(Arrays.asList(fieldsA));
            clasz = clasz.getSuperclass();
        }
        return fields;
    }

    @SuppressWarnings("rawtypes")
    public static Field getField(Class clasz, String fieldName) {
        Field fieldTo = null;
        try {
            return clasz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // exception is ignore since it want to go deeper and deeper
        }
        try {
            while (!clasz.getName().equals("java.lang.Object")
                    && fieldTo == null) {
                clasz = clasz.getSuperclass();
                fieldTo = getField(clasz, fieldName);
            }
        } catch (SecurityException e) {
            log.error(e.getMessage(), e);
        }
        return fieldTo;
    }

    public static Map<String, String> cleanEmptyValue(Map<String, String> source) {
        Map<String, String> target = new HashMap<String, String>();
        if (source != null && source.size() > 0) {
            for (String key : source.keySet()) {
                if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(source.get(key))) {
                    target.put(key, source.get(key));
                }
            }
        }
        return target;
    }

    public static List<String> cleanEmptyValue(List<String> source) {
        List<String> target = new ArrayList<String>();
        if (source != null && source.size() > 0) {
            for (String key : source) {
                if (!StringUtils.isEmpty(key)) {
                    target.add(key);
                }
            }
        }
        return target;
    }

    public static Set<String> cleanEmptyValue(Set<String> source) {
        Set<String> target = new HashSet<>();
        if (source != null && source.size() > 0) {
            for (String key : source) {
                if (!StringUtils.isEmpty(key)) {
                    target.add(key);
                }
            }
        }
        return target;
    }

//    public static LevenshteinResults getLevenshtein(String reference, String target) {
//        return LevenshteinDetailedDistance.getDefaultInstance().apply(reference, target);
//    }
//
//    public static int getLevenshteinDistance(String reference, String target) {
//        return getLevenshtein(reference, target).getDistance();
//    }
//
//    public static int getLevenshteinDistance(String reference, String target, int prefix) {
//        return comparePrefix(reference, target, prefix) ? getLevenshtein(reference, target).getDistance() : -1;
//    }

    public static boolean comparePrefix(String reference, String target, int index) {
        if (index > reference.length() || index > target.length())
            return false;
        return reference.substring(0, index).equalsIgnoreCase(target.substring(0, index));
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static Double randomNumber() {
        return Math.random();
    }

    public static <T> Map<String, T> convertListToMap(List<T> tList, String fieldName) {
        Map<String, T> configMap = new LinkedHashMap<>();
        try {
            for (T t : tList) {
                Class<?> tClass = t.getClass();
                Field f = tClass.getDeclaredField(fieldName);
                f.setAccessible(true);
                configMap.put((String) f.get(t), t);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return configMap;
    }

}
