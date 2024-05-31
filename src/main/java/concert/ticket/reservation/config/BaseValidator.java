package concert.ticket.reservation.config;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class BaseValidator<T> {

    private String FIELD_FORMAT_NUMERIC = "field.format.numeric";
    private String FIELD_FORMAT_ALPHABET = "field.format.alphabet";
    private String FIELD_FORMAT_ALPHABET_UPPER = "field.format.alphabet.upper";
    private String FIELD_FORMAT_ALPHABET_SPACE = "field.format.alphabet.space";
    private String FIELD_FORMAT_ALPHANUMERIC = "field.format.alphanumeric";
    private String FIELD_FORMAT_ALPHANUMERIC_UPPER = "field.format.alphanumeric.upper";
    private String FIELD_FORMAT_ALPHANUMERIC_SPACE = "field.format.alphanumeric.space";
    private String FIELD_REQUIRED = "field.required";
    private String DATA_NOT_FOUND = "data.not-found";

    public Status fieldIsRequired(String param) {
        return Status.INVALID(Message.get(FIELD_REQUIRED, param));
    }

    public Status dataNotFound(String modulename, String parameter) {
        return dataNotFound(modulename, parameter, DATA_NOT_FOUND);
    }

    public Status dataNotFound(String modulename, String parameter, String key) {
        return dataNotFound(Message.get(key, modulename, parameter));
    }

    public Status dataNotFound(String message) {
        return Status.DATA_NOT_FOUND(message);
    }

    public void validate(Parameter<T> parameter, Class<T> clasz) {
        if (parameter == null) {
            return;
        }

        List<Field> fields = ObjectUtils.getAllFields(clasz);

        // Validate Parameter Column
        List<String> columns = parameter.getColumn();
        if (columns != null && columns.size() > 0) {
            for (String column : columns) {
                if (StringUtils.isEmpty(column))
                    throw new ApplicationException(Status.INVALID("Parameter column should not empty"));

                try {
                    if (!isFieldExist(fields, column))
                        throw new ApplicationException(Status.INVALID("Invalid parameter column"));
                } catch (Exception e) {
                    throw new ApplicationException(Status.INVALID("Invalid parameter column"));
                }
            }
        }

        // Validate Parameter Sort
        Map<String, String> sort = parameter.getSort();
        if (sort != null && sort.size() > 0) {
            for (String key : sort.keySet()) {
                // Parameter Key is Empty
                if (StringUtils.isEmpty(key))
                    throw new ApplicationException(Status.INVALID("Parameter sort key should not empty"));

                try {
                    if (!isFieldExist(fields, key))
                        throw new ApplicationException(Status.INVALID("Invalid parameter key on sort"));
                } catch (Exception e) {
                    throw new ApplicationException(Status.INVALID("Invalid parameter key on sort"));
                }

                if (StringUtils.isEmpty(sort.get(key)))
                    throw new ApplicationException(Status.INVALID("Parameter sort value should not empty"));

                // Parameter Value is Not ASC or DESC
                if (!(sort.get(key).toUpperCase().equals(Constants.ASC)
                        || sort.get(key).toUpperCase().equals(Constants.DESC)))
                    throw new ApplicationException(Status.INVALID("Parameter sort value should be ASC or DESC"));

            }
        }

        // Validate Parameter Criteria
        Map<String, String> criteria = parameter.getCriteria();
        if (criteria != null && criteria.size() > 0) {
            for (String key : criteria.keySet()) {
                // Parameter Key is Empty
                if (StringUtils.isEmpty(key))
                    throw new ApplicationException(Status.INVALID("Parameter key criteria should not empty"));

                try {
                    if (!isFieldExist(fields, key))
                        throw new ApplicationException(Status.INVALID("Invalid parameter key on criteria"));
                } catch (Exception e) {
                    throw new ApplicationException(Status.INVALID("Invalid parameter key on criteria"));
                }
            }
        }
    }

    protected void notBlank(String value, String field) {
        if (StringUtils.isEmpty(value)) {
            throw new ApplicationException(fieldIsRequired(field));
        }
    }

    protected void notNull(Object value, String field) {
        if (value == null) {
            throw new ApplicationException(fieldIsRequired(field));
        }
    }

    protected void notZero(Object value, String field) {
        notNull(value, field);
        if ((value instanceof Integer && value.equals(0)) ||
                (value instanceof Long && value.equals(0L)) ||
                (value instanceof Double && value.equals(0.0)) ||
                (value instanceof Float && value.equals(0.0f))) {
            throw new ApplicationException(fieldIsRequired(field));
        }
    }

    protected void isAlphanumericUpper(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_ALPHANUMERIC_UPPER)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_ALPHANUMERIC_UPPER, field)));
        }
    }

    protected void isAlphanumeric(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_ALPHANUMERIC)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_ALPHANUMERIC, field)));
        }
    }

    protected void isAlphanumericSpace(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_ALPHANUMERIC_SPACE)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_ALPHANUMERIC_SPACE, field)));
        }
    }

    protected void isAlphabet(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_ALPHABETH)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_ALPHABET, field)));
        }
    }

    protected void isAlphabetUpper(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_ALPHABET_UPPER)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_ALPHABET_UPPER, field)));
        }
    }

    protected void isAlphabetSpace(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_ALPHABETH_SPACE)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_ALPHABET_SPACE, field)));
        }
    }

    protected void isNumeric(String value, String field) {
        if (!StringUtils.isEmpty(value) && !value.matches(Constants._REGEX_NUMBERS)) {
            throw new ApplicationException(Status.INVALID(Message.get(FIELD_FORMAT_NUMERIC, field)));
        }
    }

    protected void isMax(String value, int length, String field) {
        if (!StringUtils.isEmpty(value) && value.length() > length) {
            throw new ApplicationException(Status.INVALID(field + " max length is " + length));
        }
    }

    protected void isMin(String value, int length, String field) {
        if (!StringUtils.isEmpty(value) && value.length() < length) {
            throw new ApplicationException(Status.INVALID(field + " min length is " + length));
        }
    }

    /**
     * @param fields    list of properties declared in an entity/model/POJO
     * @param fieldName name of field to be checked against fields param
     * @return whether or not the field exist in the list of fields
     */
    private boolean isFieldExist(List<Field> fields, String fieldName) {
        for (Field field : fields) {
            if (field.getName().toLowerCase().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected void setFIELD_FORMAT_NUMERIC(String FIELD_FORMAT_NUMERIC) {
        this.FIELD_FORMAT_NUMERIC = FIELD_FORMAT_NUMERIC;
    }

    protected void setFIELD_FORMAT_ALPHABET(String FIELD_FORMAT_ALPHABET) {
        this.FIELD_FORMAT_ALPHABET = FIELD_FORMAT_ALPHABET;
    }

    protected void setFIELD_FORMAT_ALPHABET_UPPER(String FIELD_FORMAT_ALPHABET_UPPER) {
        this.FIELD_FORMAT_ALPHABET_UPPER = FIELD_FORMAT_ALPHABET_UPPER;
    }

    protected void setFIELD_FORMAT_ALPHABET_SPACE(String FIELD_FORMAT_ALPHABET_SPACE) {
        this.FIELD_FORMAT_ALPHABET_SPACE = FIELD_FORMAT_ALPHABET_SPACE;
    }

    protected void setFIELD_FORMAT_ALPHANUMERIC(String FIELD_FORMAT_ALPHANUMERIC) {
        this.FIELD_FORMAT_ALPHANUMERIC = FIELD_FORMAT_ALPHANUMERIC;
    }

    protected void setFIELD_FORMAT_ALPHANUMERIC_UPPER(String FIELD_FORMAT_ALPHANUMERIC_UPPER) {
        this.FIELD_FORMAT_ALPHANUMERIC_UPPER = FIELD_FORMAT_ALPHANUMERIC_UPPER;
    }

    protected void setFIELD_FORMAT_ALPHANUMERIC_SPACE(String FIELD_FORMAT_ALPHANUMERIC_SPACE) {
        this.FIELD_FORMAT_ALPHANUMERIC_SPACE = FIELD_FORMAT_ALPHANUMERIC_SPACE;
    }

    protected void setFIELD_REQUIRED(String FIELD_REQUIRED) {
        this.FIELD_REQUIRED = FIELD_REQUIRED;
    }

    protected void setDATA_NOT_FOUND(String DATA_NOT_FOUND) {
        this.DATA_NOT_FOUND = DATA_NOT_FOUND;
    }
}
