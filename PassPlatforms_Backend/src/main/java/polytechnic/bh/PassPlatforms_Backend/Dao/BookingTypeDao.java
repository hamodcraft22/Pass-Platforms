package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingType;
import polytechnic.bh.PassPlatforms_Backend.Entity.SlotType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingTypeDao
{
    private char typeid;
    private String typename;

    public BookingTypeDao(BookingType bookingType) {
        this.typeid = bookingType.getTypeid();
        this.typename = bookingType.getTypename();
    }
}
