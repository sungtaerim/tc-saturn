package com.lepse.integration.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.lepse.integrations.models.ReflectiveModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * profile product model
 * */
@JsonRootName("item")
public class ProfileProduct implements ReflectiveModel {

    private String nizd;
    private String namizd;
    private String si;
    private String va;
    private String is;
    private String ni;
    private String pni;
    private String ti;
    private String pv;
    private String d;
    private String nomer;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String tni;
    @JsonProperty("item_max")
    private String itemMax;
    @JsonProperty("name_max")
    private String nameMax;
    @JsonProperty("max_group")
    private String maxGroup;
    @JsonProperty("pr_pt")
    private String prPt;
    private String uom;

    @Override
    public List<Object> defineModelParams(ReflectiveModel modelClass) {
        List<Object> modelParams = new ArrayList<>();
        Class<?> thisModelClass = this.getClass();
        List<Method> modelGetters = Arrays.stream(thisModelClass.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get"))
                .sorted(Comparator.comparing(Method::getName))
                .collect(Collectors.toList());
        try {
            for (Method modelGetter : modelGetters) {
                modelParams.add(modelGetter.invoke(this));
            }
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.getMessage();
        }
        return modelParams;
    }

    // используюся для insert - data, is, nizd, si, tni, va
    public String getNizd() {
        return nizd;
    }

    public void setNizd(String nizd) {
        this.nizd = nizd;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getVa() {
        return va;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    public Date getDate() {
        if (date == null) {
            return new java.sql.Date(new java.util.Date().getTime());
        } else {
            return date;
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTni() {
        return tni;
    }

    public void setTni(String tni) {
        this.tni = tni;
    }

    // не используются в интеграции - остаются пустыми
    public void setNamizd(String namizd) {
        this.namizd = namizd;
    }

    public void setNi(String ni) {
        this.ni = ni;
    }

    public void setPni(String pni) {
        this.pni = pni;
    }

    public void setTi(String ti) {
        this.ti = ti;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public void setD(String d) {
        this.d = d;
    }

    public void setNomer(String nomer) {
        this.nomer = nomer;
    }

    public void setItemMax(String itemMax) {
        this.itemMax = itemMax;
    }

    public void setNameMax(String nameMax) {
        this.nameMax = nameMax;
    }

    public void setMaxGroup(String maxGroup) {
        this.maxGroup = maxGroup;
    }

    public void setPrPt(String prPt) {
        this.prPt = prPt;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getNamizd() {
        return namizd;
    }

    public String getNi() {
        return ni;
    }

    public String getPni() {
        return pni;
    }

    public String getTi() {
        return ti;
    }

    public String getPv() {
        return pv;
    }

    public String getD() {
        return d;
    }

    public String getNomer() {
        return nomer;
    }

    public String getItemMax() {
        return itemMax;
    }

    public String getNameMax() {
        return nameMax;
    }

    public String getMaxGroup() {
        return maxGroup;
    }

    public String getPrPt() {
        return prPt;
    }

    public String getUom() {
        return uom;
    }
}
