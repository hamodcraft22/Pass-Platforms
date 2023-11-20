package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.StudentMajor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentMajorDao
{
    private int stumajorid;
    private boolean isminor;
    private UserDao user;
    private MajorDao major;

    public StudentMajorDao(StudentMajor studentMajor)
    {
        this.stumajorid = studentMajor.getStumajorid();
        this.isminor = studentMajor.isIsminor();
        this.user = new UserDao(studentMajor.getUser());
        this.major = new MajorDao(studentMajor.getMajor());
    }
}
