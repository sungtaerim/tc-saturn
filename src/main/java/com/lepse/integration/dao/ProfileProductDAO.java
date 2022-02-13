package com.lepse.integration.dao;

import com.lepse.integration.models.ProfileProduct;
import com.lepse.integrations.dao.ModelDAO;
import com.lepse.integrations.dao.StatementEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Component
public class ProfileProductDAO implements ModelDAO<ProfileProduct> {

    private final JdbcTemplate jdbcTemplate;
    private final Charset charset = Charset.forName("CP1251");

    /**
     * creates a new instance of the profile data access object and embeds dependencies on the JdbcTemplate
     * @param jdbcTemplate JdbcTemplate instance responsible for interacting with the mni database
     */
    @Autowired
    public ProfileProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * retrieves data from the mni database by profile nizd
     * @param id nizd of the profile
     * @return returns the profile by nizd or if there is no profile with this nizd returns new profile
     * @throws DataAccessException
     */
    @Override
    public ProfileProduct get(String id) throws DataAccessException {
        String select = "SELECT * FROM mni WHERE nizd = ?    ";
        StatementEncoder statementEncoder = new StatementEncoder(select, charset);
        PreparedStatementCreator psc = statementEncoder.createEncodedPreparedStatementCreator(Collections.singletonList(id));
        List<ProfileProduct> profiles = jdbcTemplate.query(psc, new ProfileProductRowMapper());
        return !profiles.isEmpty() ? profiles.get(0) : new ProfileProduct();
    }

    @Override
    public String save(ProfileProduct profile) throws DataAccessException, SQLException {
        String save = "INSERT INTO mni (D, DATA, IS, ITEM_MAX, MAX_GROUP, " +
                                       "NAME_MAX, NAMIZD, NI, NIZD, NOMER, " +
                                       "PNI, PR_PT, PV, SI, TI, " +
                                       "TNI, VA)" +
                "VALUES(  ?,  ?,  ?,  ?,  ?," +
                       "  ?,  ?,  ?,  ?,  ?," +
                       "  ?,  ?,  ?,  ?,  ?," +
                       "  ?,  ?)";
        StatementEncoder statementEncoder = new StatementEncoder(save, charset, profile);
        String logMessage = statementEncoder.getStatementBody();
        PreparedStatementCreator psc = statementEncoder.createEncodedPreparedStatementCreator(profile.defineModelParams(profile));
        jdbcTemplate.update(psc);
        return logMessage;
    }

    @Override
    public String update(ProfileProduct profile, String id) throws DataAccessException, SQLException {
        String update = "UPDATE mni SET TNI =  ?, DATA =  ? WHERE NIZD = ?";
        List<Object> params = new ArrayList<>();
        params.add(profile.getTni());
        params.add(profile.getDate());
        params.add(id);
        StatementEncoder statementEncoder = new StatementEncoder(update, charset);
        String logMessage = statementEncoder.getStatementBody(params);
        PreparedStatementCreator psc = statementEncoder.createEncodedPreparedStatementCreator(params);
        jdbcTemplate.update(psc);
        return logMessage;
    }
}
