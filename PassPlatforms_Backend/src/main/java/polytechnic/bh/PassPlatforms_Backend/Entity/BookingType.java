package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingTypeDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_bookingtype")
public class BookingType
{
    @Id
    private char typeid;
    private String typename;

    public BookingType(BookingTypeDao bookingTypeDao)
    {
        this.typeid = bookingTypeDao.getTypeid();
        this.typename = bookingTypeDao.getTypename();
    }
}
