package com.lepse.integration.dao;

import com.lepse.integration.models.ProfileProduct;
import org.springframework.jdbc.core.RowMapper;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileProductRowMapper implements RowMapper<ProfileProduct> {

    enum Columns {
        NIZD, NAMIZD, SI, VA, IS, NI, PNI, TI, PV, D, NOMER, DATA, TNI, ITEM_MAX, NAME_MAX, MAX_GROUP, PR_PT;
    }


    private ResultSet resultSet;

    @Override
    public ProfileProduct mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        this.resultSet = resultSet;

        ProfileProduct profile = new ProfileProduct();
        profile.setNizd(decodeResultSetMember(Columns.NIZD.toString()));
        profile.setNamizd(decodeResultSetMember(Columns.NAMIZD.toString()));
        profile.setSi(decodeResultSetMember(Columns.SI.toString()));
        profile.setVa(decodeResultSetMember(Columns.VA.toString()));
        profile.setIs(decodeResultSetMember(Columns.IS.toString()));
        profile.setNi(decodeResultSetMember(Columns.NI.toString()));
        profile.setPni(decodeResultSetMember(Columns.PNI.toString()));
        profile.setTi(decodeResultSetMember(Columns.TI.toString()));
        profile.setPv(decodeResultSetMember(Columns.PV.toString()));
        profile.setD(decodeResultSetMember(Columns.D.toString()));
        profile.setNomer(decodeResultSetMember(Columns.NOMER.toString()));
        profile.setDate(resultSet.getDate(Columns.DATA.toString()));
        profile.setTni(decodeResultSetMember(Columns.TNI.toString()));
        profile.setItemMax(decodeResultSetMember(Columns.ITEM_MAX.toString()));
        profile.setNameMax(decodeResultSetMember(Columns.NAME_MAX.toString()));
        profile.setMaxGroup(decodeResultSetMember(Columns.MAX_GROUP.toString()));
        profile.setPrPt(decodeResultSetMember(Columns.PR_PT.toString()));

        return profile;
    }

    private String decodeResultSetMember(String member) throws SQLException {
        final Charset decodongCharset = Charset.forName("CP1251");
        return new String(resultSet.getBytes(member), decodongCharset);
    }
}
