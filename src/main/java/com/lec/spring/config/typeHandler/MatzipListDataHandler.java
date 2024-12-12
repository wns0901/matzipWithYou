package com.lec.spring.config.typeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.matzip.domain.DTO.MatzipListDataDTO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MatzipListDataHandler extends BaseTypeHandler<List<MatzipListDataDTO>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<MatzipListDataDTO> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MatzipListDataDTO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        System.out.println("getNullableResult called with columnName: " + columnName);
        try {
            return objectMapper.readValue(rs.getString(columnName), objectMapper.getTypeFactory().constructCollectionType(List.class, MatzipListDataDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MatzipListDataDTO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return objectMapper.readValue(rs.getString(columnIndex), objectMapper.getTypeFactory().constructCollectionType(List.class, MatzipListDataDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MatzipListDataDTO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return objectMapper.readValue(cs.getString(columnIndex), objectMapper.getTypeFactory().constructCollectionType(List.class, MatzipListDataDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}



