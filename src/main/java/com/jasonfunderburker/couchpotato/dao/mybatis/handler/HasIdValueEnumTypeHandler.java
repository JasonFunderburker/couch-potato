package com.jasonfunderburker.couchpotato.dao.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Ekaterina.Bashkankova on 16.09.2016
 */
@MappedJdbcTypes(JdbcType.INTEGER)
public class HasIdValueEnumTypeHandler<E extends Enum<E> & HasIdValue> extends BaseTypeHandler<E> {

    private final E[] enums;
    private Class<E> type;

    public HasIdValueEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + "does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        for (E enm : enums) {
            if (value == enm.getId()) {
                return enm;
            }
        }
        throw new IllegalArgumentException("Cannot convert "
                + value + " to " + type.getSimpleName());
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        for (E enm : enums) {
            if (value == enm.getId()) {
                return enm;
            }
        }
        throw new IllegalArgumentException("Cannot convert "
                + value + " to " + type.getSimpleName());
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        for (E enm : enums) {
            if (value == enm.getId()) {
                return enm;
            }
        }
        throw new IllegalArgumentException("Cannot convert "
                + value + " to " + type.getSimpleName());
    }
}

