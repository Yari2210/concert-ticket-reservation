package concert.ticket.reservation.repository;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.config.BaseRepositoryCustom;
import concert.ticket.reservation.config.DateUtils;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class ReservationRepositoryCustomImpl extends BaseRepositoryCustom<Reservation> implements ReservationRepositoryCustom {

    @Override
    public List<String> executeCustomSelectQueryCheckAvailable() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = DateUtils.toDateString("yyyy-MM-dd HH:mm:ss", DateUtils.now());
        sb.append(
                "SELECT c.id from concert c WHERE '"+ date1 +"' BETWEEN c.start_sell_date AND c.end_sell_date AND NOT (c.status = 'SOLDOUT')");
        List<String> query = entityManager.createNativeQuery(sb.toString()).getResultList();
        return query;
    }

    @Override
    public List<String> executeCustomSelectQueryCheckNotAvailable() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = DateUtils.toDateString("yyyy-MM-dd HH:mm:ss", DateUtils.now());
        sb.append(
                "SELECT c.id from concert c WHERE '"+ date1 +"' < c.start_sell_date AND NOT ('"+ date1 +"' BETWEEN c.start_sell_date AND c.end_sell_date)");
        List<String> query = entityManager.createNativeQuery(sb.toString()).getResultList();
        return query;
    }

    @Override
    public List<String> executeCustomSelectQueryCheckExpired() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = DateUtils.toDateString("yyyy-MM-dd HH:mm:ss", DateUtils.now());
        sb.append(
                "SELECT c.id from concert c WHERE '"+ date1 +"' > c.end_sell_date AND NOT ('"+ date1 +"' BETWEEN c.start_sell_date AND c.end_sell_date AND c.status = 'SOLDOUT')");
        List<String> query = entityManager.createNativeQuery(sb.toString()).getResultList();
        return query;
    }

    @Override
    public List<String> executeCustomSelectQueryCheckPay() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = DateUtils.toDateString("yyyy-MM-dd HH:mm:ss", DateUtils.now());
        sb.append(
                "SELECT r.id from reservation r WHERE '"+ date1 +"' > r.payment_deadline AND r.payment_status = 'WAITING_FOR_PAYMENT'");
        List<String> query = entityManager.createNativeQuery(sb.toString()).getResultList();
        return query;
    }

//    private String createSelectQueryCustom() {
//        StringBuilder sb = new StringBuilder();
//        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date1 = DateUtils.toDateString("yyyy-MM-dd HH:mm:ss", DateUtils.now());
//        sb.append(
//                "SELECT c.id from concert c WHERE '"+ date1 +"' BETWEEN c.start_sell_date AND c.end_sell_date");

//        return sb.toString();
//    }

}