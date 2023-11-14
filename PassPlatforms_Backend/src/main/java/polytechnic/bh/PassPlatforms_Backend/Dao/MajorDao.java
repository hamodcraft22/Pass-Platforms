package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Major;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MajorDao {
    private String majorid;
    private String majorname;
    private String majordesc;
    private SchoolDao school;

    public MajorDao(Major major) {
        this.majorid = major.getMajorid();
        this.majorname = major.getMajorname();
        this.majordesc = major.getMajordesc();
        this.school = new SchoolDao(major.getSchool());
    }
}
