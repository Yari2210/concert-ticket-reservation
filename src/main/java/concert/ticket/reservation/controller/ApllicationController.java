package concert.ticket.reservation.controller;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.service.ApplicationService;
import concert.ticket.reservation.validator.ApplicationValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import concert.ticket.reservation.config.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ApllicationController {

    Logger logger = LogManager.getLogger();

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationValidator applicationValidator;

//    public ApllicationController(ApplicationService applicationService, ApplicationValidator applicationValidator) {
//        this.applicationService = applicationService;
//        this.applicationValidator=applicationValidator;
//
//    }

    @GetMapping("/concerts")
    public BaseResponse<List<Concert>> retrieve(@Valid @RequestBody BaseRequest<BaseParameter<Concert>> request) {
        BaseResponse<List<Concert>> response = new BaseResponse<>();
        try {

            Paging paging = (request.getPaging() == null) ? Paging.initialize() : Paging.validate(request.getPaging());
            List<Concert> codeshareList;
            if (paging == null || paging.getLimit() < 0) {
                    if (request.getParameter() != null) {
                        codeshareList = applicationService.executeCustomSelectQuery(request.getParameter(), Concert.class, true, true);
                    } else {
                        codeshareList = applicationService.findAll();
                    }
            } else {
                Page<Concert> codesharePage;
                    if (request.getParameter() != null) {
                        codesharePage = applicationService.executeCustomSelectQuery(request.getParameter(), paging, Concert.class, true, true);
                        codeshareList = codesharePage.getContent();
                    } else {
                        codesharePage = applicationService.findAll(paging);
                        codeshareList = codesharePage.getContent();
                    }
                paging.setTotalpage(codesharePage.getTotalPages());
                paging.setTotalrecord(codesharePage.getTotalElements());
                response.setPaging(paging);
            }
            // Set Response Result
            response.setResult(codeshareList);

            // Set Response Status
            response.setStatus(Status.SUCCESS());
        } catch (ApplicationException ve) {
            if (ve.getStatus() == null) {
                response.setStatus(Status.INVALID(""));
            } else {
                response.setStatus(ve.getStatus());
            }
        } catch (DataIntegrityViolationException he) {
            response.setStatus(new Status("9005", "Invalid Data", "data.constraint.violation"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(new Status(Status.ERROR_CODE, Status.ERROR_DESC, e.getLocalizedMessage()));
        }
        return response;
    }

    private synchronized Reservation getReservation(BaseRequest<BaseParameter<Reservation>> request) {
        return applicationService.reservation(request);
    }

    @PostMapping("/reservation")
    public BaseResponse<Reservation> reservation(@Valid @RequestBody BaseRequest<BaseParameter<Reservation>> request) {
        BaseResponse<Reservation> response = new BaseResponse<>();
        Reservation reservation = request.getParameter().getData();
        try {

            applicationValidator.validate(request.getParameter().getData());
            Reservation reservation2 = getReservation(request);
            response.setResult(reservation2);
            response.setStatus(new Status(Status.SUCCESS_CODE, Status.SUCCESS_DESC, "Ticket reservation successful"));
        } catch (ApplicationException ve) {
            if (ve.getStatus() == null) {
                response.setStatus(Status.INVALID(Translator.get(ve.getKey(), ve.getModuleName(), ve.getParameter())));
            } else {
                response.setStatus(ve.getStatus());
            }
        } catch (DataIntegrityViolationException he) {
            response.setStatus(new Status(Status.ERROR_INVALID_DATA_CODE, Status.ERROR_INVALID_DATA_DESC, Translator.get("data.constraint.violation")));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(Status.ERROR(e.getMessage()));
        }
        return response;
    }

    @GetMapping("/payment")
    public BaseResponse<Reservation> payment(@RequestParam String paymentnumber) {
        BaseResponse<Reservation> response = new BaseResponse<>();
//        Reservation accrualPromo = request.getParameter().getData();
        try {

//            accrualPromoValidator.validate(accrualPromo, ValidatorType.CREATE);
//            response.setIdentity(request.getIdentity());
            response.setResult(applicationService.payment(paymentnumber));
            response.setStatus(new Status(Status.SUCCESS_CODE, Status.SUCCESS_DESC, "Ticket has been successfully paid"));
        } catch (ApplicationException | SystemsException e) {
            e.printStackTrace();
            response.setStatus(e.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            response.setStatus(Status.ERROR(MDC.get("X-B3-TraceId") + " - " + MDC.get("X-B3-SpanId")));
        }
        return response;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void scheduledMethod() throws IOException, ParseException {
        applicationService.scheduler();
    }

}

