package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingStatusDto {
    private char statusid;
    private String statusname;
    private String statusdesc;

    public BookingStatusDto(BookingStatus bookingStatus) {
        this.statusid = bookingStatus.getStatusid();
        this.statusname = bookingStatus.getStatusname();
        this.statusdesc = bookingStatus.getStatusdesc();
    }
}
