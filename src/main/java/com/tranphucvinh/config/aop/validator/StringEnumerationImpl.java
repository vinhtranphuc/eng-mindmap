package com.tranphucvinh.config.aop.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class StringEnumerationImpl implements ConstraintValidator<StringEnumeration, String> {

    private Set<String> AVAILABLE_ENUM_NAMES;
    private boolean ALLOW_NULL_OR_EMPTY;

    public static Set<String> getNamesSet(Class<? extends Enum<?>> e) {
        Enum<?>[] enums = e.getEnumConstants();
        String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].name();
        }
        Set<String> mySet = new HashSet<>(Arrays.asList(names));
        return mySet;
    }

    private Set<String> getValuesSet(Class<? extends Enum<?>> enumClass, String getValueMethod) throws Exception {
        Set<String> values = new HashSet<>();
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            try {
                String value = (String) enumClass.getMethod(getValueMethod).invoke(enumValue);
                if (value != null) {
                    values.add(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    @Override
    public void initialize(StringEnumeration stringEnumeration) {
        Class<? extends Enum<?>> enumSelected = stringEnumeration.enumClass();
        try {
            AVAILABLE_ENUM_NAMES = stringEnumeration.checkValue() ? getValuesSet(enumSelected, stringEnumeration.getValueMethod()) : getNamesSet(enumSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALLOW_NULL_OR_EMPTY = stringEnumeration.allowNullOrEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ALLOW_NULL_OR_EMPTY && StringUtils.isEmpty(value)) {
            return true;
        }
        boolean isValid = AVAILABLE_ENUM_NAMES.contains(value);
        return isValid;
    }
}
