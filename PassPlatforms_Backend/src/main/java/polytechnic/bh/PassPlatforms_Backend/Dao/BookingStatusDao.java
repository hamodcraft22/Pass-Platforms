package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingStatusDao {
    private char statusid;
    private String statusname;
    private String statusdesc;

    public BookingStatusDao(BookingStatus bookingStatus) {
        this.statusid = bookingStatus.getStatusid();
        this.statusname = bookingStatus.getStatusname();
        this.statusdesc = bookingStatus.getStatusdesc();
    }
}
