package com.lepse.integration.controllers;

import com.lepse.integration.dao.ProfileProductDAO;
import com.lepse.integration.models.ModelRequest;
import com.lepse.integration.models.ModelResponse;
import com.lepse.integration.models.ProfileProduct;
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

@RestController
@RequestMapping({"/profile_product"})
public class ProfileProductController {

    private LogsDAO logsDAO;
    private ProfileProductDAO profileDAO;
    private static final Logger logger = LoggerFactory.getLogger(ProfileProductController.class);

    /**
     * creates a new instance of the controller and embeds dependencies on the DAO
     * @param profileDAO profile data access object
     * @param logsDAO logs data access object
     */
    @Autowired
    public ProfileProductController(LogsDAO logsDAO, ProfileProductDAO profileDAO) {
        this.logsDAO = logsDAO;
        this.profileDAO = profileDAO;
    }

    /**
     * GET request controller for receiving profile by nizd
     * @param id nizd of the profile
     * @return returns an profile by nizd or if there is no profile with this nizd returns null
     */
    @GetMapping({"/{id}"})
    @ResponseBody
    public ProfileProduct get(@PathVariable String id) {
        try {
            return profileDAO.get(id);
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return new ProfileProduct();
    }

    /**
     * POST request controller for saving the profile to a database
     * @param request request body that includes information about the integration, profile and its sender
     * @return returns the response body
     */
    @PostMapping({"/new"})
    @ResponseBody
    public ModelResponse create(@RequestBody ModelRequest<ProfileProduct> request) {
        BaseResponse baseResponse = null;
        String logMessage = "";
        LogRecord logRecord = getLogRecord(request);
        try {
            for (ProfileProduct profile : request.getModel()) {
                logMessage = profileDAO.save(profile);
                logRecord.setMessage(logMessage);
                baseResponse = logsDAO.save(logRecord, OperationStatus.SUCCESS);
            }
        } catch (DataAccessException | SQLException exception) {
            baseResponse = errorStatus(exception, logRecord);
        } finally {
            if (baseResponse == null) {
                baseResponse = new BaseResponse(BaseResponse.Code.DATA_RW_ERROR_CODE, BaseResponse.Status.DATA_RW_ERROR_STATUS);
            }
            if (baseResponse.getCode().equals(BaseResponse.Code.LOG_RW_WARN_CODE))
                logger.warn(logMessage);
        }
        return new ModelResponse(baseResponse.getCode(), baseResponse.getStatus());
    }

    /**
     * PUT request controller for update the profile by nizd
     * @param id nizd of the profile to update
     * @param request request body that includes new information about the integration, profile and its sender
     * @return returns the response body
     */
    @PutMapping({"edit/{id}"})
    public ModelResponse update(@PathVariable String id, @RequestBody ModelRequest<ProfileProduct> request) {
        BaseResponse baseResponse = null;
        String logMessage = "";
        LogRecord logRecord = getLogRecord(request);
        try {
            logMessage = profileDAO.update(request.getModel().get(0), id);
            logRecord.setMessage(logMessage);
            baseResponse = logsDAO.save(logRecord, OperationStatus.SUCCESS);
        } catch (DataAccessException | SQLException exception) {
            baseResponse = errorStatus(exception, logRecord);
        } finally {
            if (baseResponse == null) {
                baseResponse = new BaseResponse(BaseResponse.Code.DATA_RW_ERROR_CODE, BaseResponse.Status.DATA_RW_ERROR_STATUS);
            }
            if (baseResponse.getCode() == BaseResponse.Code.LOG_RW_WARN_CODE) {
                logger.warn(logMessage);
            }
        }
        return new ModelResponse(baseResponse.getCode(), baseResponse.getStatus());
    }

    private LogRecord getLogRecord(ModelRequest<ProfileProduct> request) {
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
