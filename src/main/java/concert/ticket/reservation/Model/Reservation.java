package concert.ticket.reservation.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import concert.ticket.reservation.config.BaseModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude
public class Reservation extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "concert_id", referencedColumnName = "id")
//    private Concert concert;

    @Column(name = "concert_id")
    private Long concertid;
    private String user;
    @Column(name = "number_of_tickets")
    private int numberoftickets;
    @Column(name = "booking_time")
    private Date bookingTime;

    @Column(name = "payment_time")
    private Date paymenttime;

    @Column(name = "payment_deadline")
    private Date paymentdeadline;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentEnum paymentmethod;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentstatus;

    @Column(name = "payment_number")
    private String paymentnumber;

    @Transient
    private String paymentlink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Concert getConcert() {
//        return concert;
//    }
//
//    public void setConcert(Concert concert) {
//        this.concert = concert;
//    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Long getConcertid() {
        return concertid;
    }

    public void setConcertid(Long concertid) {
        this.concertid = concertid;
    }

    public int getNumberoftickets() {
        return numberoftickets;
    }

    public void setNumberoftickets(int numberoftickets) {
        this.numberoftickets = numberoftickets;
    }

    public PaymentEnum getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(PaymentEnum paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getPaymentnumber() {
        return paymentnumber;
    }

    public void setPaymentnumber(String paymentnumber) {
        this.paymentnumber = paymentnumber;
    }

    public PaymentStatusEnum getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(PaymentStatusEnum paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public Date getPaymenttime() {
        return paymenttime;
    }

    public void setPaymenttime(Date paymenttime) {
        this.paymenttime = paymenttime;
    }

    public Date getPaymentdeadline() {
        return paymentdeadline;
    }

    public void setPaymentdeadline(Date paymentdeadline) {
        this.paymentdeadline = paymentdeadline;
    }

    public String getPaymentlink() {
        return paymentlink;
    }

    public void setPaymentlink(String paymentlink) {
        this.paymentlink = paymentlink;
    }
}
