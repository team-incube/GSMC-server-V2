package team.incude.gsmc.v2.domain.member.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_homeroom_teacher_detail")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeroomTeacherDetailJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homeroom_teacher_detail_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @OneToOne
    private MemberJpaEntity member;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "class_number", nullable = false)
    private Integer classNumber;
}