package com.jasonfunderburker.couchpotato.entities.converters;

import javax.persistence.AttributeConverter;

import static java.util.Objects.requireNonNull;

/**
 * Created on 19.04.2017
 *
 * @author JasonFunderburker
 */
public class EnumWithIdConverter<E extends Enum<E> & HasIdValue> implements AttributeConverter<E, Long>{

    private Class<E> enumType;

    @Override
    public Long convertToDatabaseColumn(E attribute) {
        requireNonNull(attribute, "Can't get id for Null-value status");
        return attribute.getId();
    }

    @Override
    public E convertToEntityAttribute(Long dbData) {
        requireNonNull(dbData, "Can't get status for Null-value id");
        for (E enm : enumType.getEnumConstants()) {
            if (dbData.equals(enm.getId())) {
                return enm;
            }
        }
        throw new IllegalArgumentException("Value of type "+enumType.getSimpleName()+" for id = "+dbData+" doesn't exist");
    }

    public void setEnumType(Class<E> enumType) {
        this.enumType = enumType;
    }
}
