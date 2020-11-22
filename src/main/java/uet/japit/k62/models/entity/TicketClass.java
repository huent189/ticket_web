package uet.japit.k62.models.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class TicketClass extends BaseEntity{
    private String name;
    private BigDecimal price;
    private Integer numberAvailable;
    private Integer total;
    private String description;
    private Integer minPerPerson;
    private Integer maxPerPerson;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "ticketClass")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<BookingDetail> bookingDetailList = new ArrayList<BookingDetail>();
}