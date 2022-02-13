package com.lepse.integration.dao;

import com.lepse.integration.models.Model;
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
import java.util.Collections;
import java.util.List;

@Repository
@Component
public class ModelsDAO implements ModelDAO<Model> {

    private final JdbcTemplate jdbcTemplate;

    /**
     * creates a new instance of the material/other product/standard product data access object and embeds dependencies on the JdbcTemplate
     * @param jdbcTemplate JdbcTemplate instance responsible for interacting with the material/other product/standard product database
     */
    @Autowired
    public ModelsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * retrieves data from the material/other product/standard product database by material/other product/standard product number
     * @param id number of the material/other product/standard product
     * @return returns an material/other product/standard product by number or if there is no material/other product/standard product with this number returns null
     * @throws DataAccessException
     */
    @Override
    public Model get(String id) throws DataAccessException {
        String selectQuery = "SELECT * FROM mnm WHERE Nn= ?";
        StatementEncoder statementEncoder = new StatementEncoder(selectQuery, Charset.forName("CP1251"));
        PreparedStatementCreator psc = statementEncoder.createEncodedPreparedStatementCreator(Collections.singletonList(id));
        List<Model> model = jdbcTemplate.query(psc, new ModelsRowMapper());
        return !model.isEmpty() ? model.get(0) : new Model();
    }

    @Override
    public String save(Model model) throws DataAccessException, SQLException {
        String saveQuery = "SET NULL OFF; " +
                "INSERT INTO mnm (So, Bn, Ma, Da, Go, Ei, Nn, K_okpd, Na, Ra) " +
                "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        StatementEncoder statementEncoder = new StatementEncoder(saveQuery, Charset.forName("CP1251"), model);
        String logMessage = statementEncoder.getStatementBody();
        PreparedStatementCreator psc = statementEncoder.createEncodedPreparedStatementCreator();
        jdbcTemplate.update(psc);
        return logMessage;
    }

    @Override
    public String update(Model model, String id) throws DataAccessException, SQLException {
        String updateQuery = "SET NULL OFF;" +
                " UPDATE mnm SET So= ?, Bn= ?, Ma= ?, Da= ?, Go= ?, Ei= ?, Nn= ?, K_okpd= ?, Na= ?, Ra= ?" +
                " WHERE Nn= ?";
        List<Object> modelParams = model.defineModelParams(model);
        modelParams.add(model.getNomenclatureNumber());
        StatementEncoder statementEncoder = new StatementEncoder(updateQuery, Charset.forName("CP1251"));
        String logMessage = statementEncoder.getStatementBody(modelParams);
        PreparedStatementCreator psc = statementEncoder.createEncodedPreparedStatementCreator(modelParams);
        jdbcTemplate.update(psc);
        return logMessage;
    }
}
