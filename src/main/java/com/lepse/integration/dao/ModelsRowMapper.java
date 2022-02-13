package com.lepse.integration.dao;

import com.lepse.integration.models.Model;
import org.springframework.jdbc.core.RowMapper;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelsRowMapper implements RowMapper<Model> {

    private ResultSet resultSet;

    @Override
    public Model mapRow(ResultSet rs, int rowNum) throws SQLException {
        resultSet = rs;
        Model model = new Model();
        model.setBNM(decodeResultSetMember("Bn"));
        model.setNomenclatureNumber(decodeResultSetMember("Nn"));
        model.setShortName(decodeResultSetMember("Na"));
        model.setBrand(decodeResultSetMember("Ma"));
        model.setGostName(decodeResultSetMember("Go"));
        model.setAssortmentDocument(decodeResultSetMember("So"));
        model.setSize(decodeResultSetMember("Ra"));
        model.setMeasureUnit(decodeResultSetMember("Ei"));
        model.setOkpdCode(decodeResultSetMember("K_okpd"));
        Date curDate = resultSet.getDate("Da");
        model.setDate(curDate == null ? new Date(0) : curDate);
        return model;
    }

    private String decodeResultSetMember(String memberName) throws SQLException {
        final Charset decodingCharset = Charset.forName("CP1251");
        return new String(resultSet.getBytes(memberName), decodingCharset);
    }
}
