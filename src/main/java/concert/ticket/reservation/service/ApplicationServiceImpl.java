package concert.ticket.reservation.service;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.Model.PaymentStatusEnum;
import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.Model.StatusEnum;
import concert.ticket.reservation.config.*;
import concert.ticket.reservation.repository.ApplicationRepository;
import concert.ticket.reservation.repository.ReservationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl extends BaseService<Concert, Long> implements ApplicationService {

    Logger logger = LogManager.getLogger(ApplicationServiceImpl.class);

    private ApplicationRepository applicationRepository;

    private ReservationRepository reservationRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ReservationRepository reservationRepository) {
        this.applicationRepository = applicationRepository;
        this.reservationRepository = reservationRepository;
        this.setRepository(applicationRepository);
        this.setCustomRepository(applicationRepository);
    }



    public Reservation reservation(BaseRequest<BaseParameter<Reservation>> request) {
        Reservation reservation = request.getParameter().getData();
        Concert concert = applicationRepository.findById(reservation.getConcertid()).orElse(null);
        if (concert == null) {
            throw new ApplicationException(Status.DATA_NOT_FOUND("Concert Not Found"));
        }
        if(concert.getStatus().equals(StatusEnum.NOT_AVAILABLE)){
            throw new ApplicationException(Status.INVALID("Concert is not available"));
        }
        if(concert.getStatus().equals(StatusEnum.EXPIRED)){
            throw new ApplicationException(Status.INVALID("Concert is expired"));
        }
        if (concert.getAvailableTickets() < reservation.getNumberoftickets()) {
            throw new ApplicationException(Status.INVALID("Concert is sold out"));
        }
        if(reservation.getNumberoftickets() > 5 ){
            throw new ApplicationException(Status.INVALID("Maximum number of tickets is 5"));
        }
        List<Reservation> reservationuser = reservationRepository.findByUser(reservation.getUser());
        if(reservationuser != null){
            int not = 0;
            for(Reservation item: reservationuser){
                if(item.getConcertid().equals(reservation.getConcertid())){
                    if(item.getPaymentstatus().equals(PaymentStatusEnum.WAITING_FOR_PAYMENT) || item.getPaymentstatus().equals(PaymentStatusEnum.ALREADY_PAID)){
                        not = not + item.getNumberoftickets();
                    }
                }
            }
            if(not+reservation.getNumberoftickets() > 5){
                throw new ApplicationException(Status.INVALID("One user can only buy a maximum of 5 tickets"));
            }
        }

        int count = concert.getAvailableTickets() - reservation.getNumberoftickets();
        concert.setAvailableTickets(count);
        if (count == 0) {
            concert.setStatus(StatusEnum.SOLDOUT);
        }
        applicationRepository.save(concert);

        Reservation booking = new Reservation();
        booking.setConcertid(reservation.getConcertid());
        booking.setUser(reservation.getUser());
        booking.setNumberoftickets(reservation.getNumberoftickets());
        booking.setBookingTime(DateUtils.now());
        booking.setPaymentmethod(reservation.getPaymentmethod());
        booking.setPaymentnumber(GeneratorUtils.GenerateId("", DateUtils.now(), 7));
        booking.setPaymentstatus(PaymentStatusEnum.WAITING_FOR_PAYMENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5);
        booking.setPaymentdeadline(calendar.getTime());
        booking.setPaymentlink("http://localhost:9191/concert/ticket/reservation/v1.0/payment?paymentnumber="+ booking.getPaymentnumber());

        reservationRepository.save(booking);
        return booking;
    }

    public Reservation payment(String paymentnumber) {
        Reservation booking = reservationRepository.findByPaymentnumber(paymentnumber);
        if (booking == null) {
            throw new ApplicationException(Status.DATA_NOT_FOUND("Concert Not Found"));
        }

        booking.setPaymenttime(DateUtils.now());
        booking.setPaymentstatus(PaymentStatusEnum.ALREADY_PAID);

        reservationRepository.save(booking);
        return booking;
    }

    public void scheduler() throws IOException, ParseException {
        List<String> available = reservationRepository.executeCustomSelectQueryCheckAvailable();

        for (String item : available) {
            Concert concert = applicationRepository.findById(Long.valueOf(item)).orElse(null);
            concert.setStatus(StatusEnum.AVAILABLE);
            applicationRepository.save(concert);
        }

        List<String> expired = reservationRepository.executeCustomSelectQueryCheckExpired();

        for (String item : expired) {
            Concert concert = applicationRepository.findById(Long.valueOf(item)).orElse(null);
            concert.setStatus(StatusEnum.EXPIRED);
            applicationRepository.save(concert);
        }

        List<String> notavailable = reservationRepository.executeCustomSelectQueryCheckNotAvailable();

        for (String item : notavailable) {
            Concert concert = applicationRepository.findById(Long.valueOf(item)).orElse(null);
            concert.setStatus(StatusEnum.NOT_AVAILABLE);
            applicationRepository.save(concert);
        }

        List<String> failedtopay = reservationRepository.executeCustomSelectQueryCheckPay();

        for (String item : failedtopay) {
            Reservation reservation = reservationRepository.findById(Long.valueOf(item)).orElse(null);
            reservation.setPaymentstatus(PaymentStatusEnum.FAILED_TO_PAY);
            reservationRepository.save(reservation);

            Concert concert = applicationRepository.findById(Long.valueOf(reservation.getConcertid())).orElse(null);
            int count = concert.getAvailableTickets() + reservation.getNumberoftickets();
            concert.setAvailableTickets(count);
            if (count != 0) {
                concert.setStatus(StatusEnum.AVAILABLE);
            }
            applicationRepository.save(concert);
        }
    }


}
