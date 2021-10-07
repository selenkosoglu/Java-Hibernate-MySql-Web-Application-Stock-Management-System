package entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class PayInJoin {

    @Id
    private int payIn_id ;
    private String cu_name;
    private String cu_surname;
    private long o_receipt_number;
    private int payment_amount;
    private LocalDateTime date;

}
