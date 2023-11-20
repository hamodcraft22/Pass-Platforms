package polytechnic.bh.PassPlatforms_Backend.Entity.Child;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import polytechnic.bh.PassPlatforms_Backend.Entity.OfferedCourse;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("2")
@EqualsAndHashCode(callSuper = true)
public class Leader extends Student
{

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
    private List<Transcript> transcripts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
    private List<Slot> slots;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
    private List<OfferedCourse> offeredCourses;
}
