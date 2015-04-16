package com.sap.i076326;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RecordRowMapper implements RowMapper<Record>{
    public Record mapRow(ResultSet rs, int rowNum) throws SQLException{
        Record record = new Record();

        record.setA(rs.getInt(1));
        record.setB(rs.getInt(2));

        return record;
    }
}