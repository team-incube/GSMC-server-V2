package team.incube.gsmc.v2.domain.member.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_student_detail")
@Getter
@NoArgsConstructor
public class StudentDetailJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_detail_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @OneToOne
    private MemberJpaEntity member;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "class_number", nullable = false)
    private Integer classNumber;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Column(name = "student_code", unique = true, nullable = false)
    private String studentCode;

    @Builder
    public StudentDetailJpaEntity(Long id, MemberJpaEntity member, Integer grade, Integer classNumber, Integer number, Integer totalScore, String studentCode) {
        this.id = id;
        this.member = member;
        this.grade = grade;
        this.classNumber = classNumber;
        this.number = number;
        this.totalScore = totalScore;
        this.studentCode = studentCode;
    }
}