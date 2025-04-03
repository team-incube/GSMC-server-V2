package team.incude.gsmc.v2.domain.member.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_student_detail")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_detail_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @OneToOne(mappedBy = "tb_member")
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
}