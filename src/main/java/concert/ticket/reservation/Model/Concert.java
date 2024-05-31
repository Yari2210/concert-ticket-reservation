package concert.ticket.reservation.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import concert.ticket.reservation.config.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "concert")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude
public class Concert extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String artist;
    private Date date;
    private int totalTickets;
    private int availableTickets;

    @Column(name = "status")
    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private String venue;

    @Column(name = "start_sell_date")
    private Date startselldate;

    @Column(name = "end_sell_date")
    private Date endselldate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getStartselldate() {
        return startselldate;
    }

    public void setStartselldate(Date startselldate) {
        this.startselldate = startselldate;
    }

    public Date getEndselldate() {
        return endselldate;
    }

    public void setEndselldate(Date endselldate) {
        this.endselldate = endselldate;
    }
}
