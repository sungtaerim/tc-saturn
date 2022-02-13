package com.lepse.integration.controllers;

import com.lepse.integration.dao.ModelsDAO;
import com.lepse.integration.models.Model;
import com.lepse.integration.models.ModelRequest;
import com.lepse.integration.models.ModelResponse;
import com.lepse.integrations.dao.OperationStatus;
import com.lepse.integrations.log.LogRecord;
import com.lepse.integrations.log.LogsDAO;
import com.lepse.integrations.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.Date;

/**
 * the controller class that describes the REST api
 */
@RestController
@RequestMapping({"/materials"})
public class MaterialsController {

    private ModelsDAO materialsDAO;
    private LogsDAO logsDAO;

    private static final Logger logger = LoggerFactory.getLogger(MaterialsController.class);

    /**
     * creates a new instance of the controller and embeds dependencies on the DAO
     * @param materialsDAO material data access object
     * @param logsDAO logs data access object
     */
    @Autowired
    public MaterialsController(ModelsDAO materialsDAO, LogsDAO logsDAO) {
        this.materialsDAO = materialsDAO;
        this.logsDAO = logsDAO;
    }

    /**
     * GET request controller for receiving material by nomenclature number
     * @param id number of the material
     * @return returns an material by number or if there is no material with this number returns null
     */
    @GetMapping({ "/{id}"})
    @ResponseBody
    public Model get(@PathVariable String id) {
        try {
            return materialsDAO.get(id);
        }
        catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return new Model();
    }

    /**
     * POST request controller for saving an material to a database
     * @param request request body that includes information about the integration, material and its sender
     * @return returns the response body
     */
    @PostMapping({"/new"})
    @ResponseBody
    public ModelResponse create(@RequestBody ModelRequest<Model> request) {
        BaseResponse baseResponse = null;
        String logMessage = "";
        LogRecord logRecord = getLogRecord(request);
        try {
            for (Model material : request.getModel()) {
                logMessage = materialsDAO.save(material);
                logRecord.setMessage(logMessage);
                baseResponse = logsDAO.save(logRecord, OperationStatus.SUCCESS);
            }
        }
        catch (DataAccessException | SQLException exception) {
            baseResponse = errorStatus(exception, logRecord);
        }
        finally {
            if (baseResponse == null) {
                baseResponse = new BaseResponse(BaseResponse.Code.DATA_RW_ERROR_CODE, BaseResponse.Status.DATA_RW_ERROR_STATUS);
            }
            if (baseResponse.getCode().equals(BaseResponse.Code.LOG_RW_WARN_CODE))
                logger.warn(logMessage);
        }
        return new ModelResponse(baseResponse.getCode(), baseResponse.getStatus());
    }

    /**
     * PUT request controller for update the material by nomenclature number
     * @param id nomenclature number of the material to update
     * @param request request body that includes new information about the integration, material and its sender
     * @return returns the response body
     */
    @PutMapping({"edit/{id}"})
    @ResponseBody
    public ModelResponse update(@PathVariable String id, @RequestBody ModelRequest<Model> request) {
        BaseResponse baseResponse = null;
        String logMessage = "";
        LogRecord logRecord = getLogRecord(request);
        try {
            logMessage = materialsDAO.update(request.getModel().get(0), id);
            logRecord.setMessage(logMessage);
            baseResponse = logsDAO.save(logRecord, OperationStatus.SUCCESS);
        }
        catch (DataAccessException | SQLException exception) {
            baseResponse = errorStatus(exception, logRecord);
        }
        finally {
            if (baseResponse == null) {
                baseResponse = new BaseResponse(BaseResponse.Code.DATA_RW_ERROR_CODE, BaseResponse.Status.DATA_RW_ERROR_STATUS);
            }
            if (baseResponse.getCode() == BaseResponse.Code.LOG_RW_WARN_CODE)
                logger.warn(logMessage);
        }
        return new ModelResponse(baseResponse.getCode(), baseResponse.getStatus());
    }

    private LogRecord getLogRecord(ModelRequest request) {
        return new LogRecord.LogRecordBuilder()
                .setDate(new Date())
                .setIntegrationName(request.getIntegrationName())
                .setRequestType(LogRecord.RequestType.PUT)
                .setSender(request.getSender())
                .build();
    }

    private BaseResponse errorStatus(Exception exception, LogRecord logRecord) {
        String logMessage = exception.getMessage();
        logger.error(logMessage);
        logRecord.setMessage(logMessage);
        return logsDAO.save(logRecord, OperationStatus.ERROR);
    }
}
