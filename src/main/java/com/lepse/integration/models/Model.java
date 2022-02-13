package com.lepse.integration.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.lepse.integration.config.ModelConfig;
import com.lepse.integrations.models.ReflectiveModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * material/other product/standard product model
 */
@JsonRootName("item")
public class Model implements ReflectiveModel {
    @JsonProperty("bnm")
    private String bnm;
    @JsonProperty("code")
    private String nomenclatureNumber;
    @JsonProperty("name")
    private String shortName;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("gost")
    private String gostName;
    @JsonProperty("sort")
    private String assortmentDocument;
    @JsonProperty("size")
    private String size;
    @JsonProperty("uom")
    private String measureUnit;
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @JsonProperty("okpdCode")
    private String okpdCode;

    public String getBNM() {
        return bnm;
    }

    public void setBNM(String bnm) {
        this.bnm = bnm;
    }

    public String getNomenclatureNumber() {
        return nomenclatureNumber;
    }

    public void setNomenclatureNumber(String nomenclatureNumber) {
        this.nomenclatureNumber = nomenclatureNumber;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGostName() {
        return gostName;
    }

    public void setGostName(String GostName) {
        this.gostName = GostName;
    }

    public String getAssortmentDocument() {
        return assortmentDocument;
    }

    public void setAssortmentDocument(String assortmentDocument) {
        this.assortmentDocument = assortmentDocument;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = uomToNumber(measureUnit);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date == null) {
            java.util.Date temp = new java.util.Date();
            this.date = new Date(temp.getTime());
        } else {
            this.date = date;
        }
    }

    public String getOkpdCode() {
        return okpdCode;
    }

    public void setOkpdCode(String okpdCode) {
        this.okpdCode = okpdCode;
    }

    @Override
    public List<Object> defineModelParams(ReflectiveModel modelClass) {
        List<Object> modelParams = new ArrayList<>();
        Class<?> thisModelClass = this.getClass();
        List<Method> modelGetters = Arrays.stream(thisModelClass.getDeclaredMethods()).filter(method -> method.getName().startsWith("get"))
                .sorted(Comparator.comparing(Method::getName))
                .collect(Collectors.toList());
        try {
            for (Method modelGetter : modelGetters)
                modelParams.add(modelGetter.invoke(this));
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.getMessage();
        }
        return modelParams;
    }

    private String uomToNumber(String uomString) {
        String url = ModelConfig.urlToUOM;
        String textQuery = "SELECT * FROM b100311 WHERE LOWER(obozn) = ?";
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(textQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setBytes(1, uomString.getBytes(Charset.forName("CP1251")));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("ediz");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "07";
    }
}
