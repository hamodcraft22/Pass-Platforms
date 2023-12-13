package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.SlotTypeDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_slottype")
public class SlotType
{
    @Id
    private char typeid;
    private String typename;

    public SlotType(SlotTypeDao slotTypeDao) {
        this.typeid = slotTypeDao.getTypeid();
        this.typename = slotTypeDao.getTypename();
    }
}
