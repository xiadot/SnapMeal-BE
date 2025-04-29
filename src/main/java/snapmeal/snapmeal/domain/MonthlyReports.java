package snapmeal.snapmeal.domain;


import jakarta.persistence.*;
import lombok.*;

import snapmeal.snapmeal.domain.common.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "monthly_reports")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReports extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "report_month")
    private LocalDate reportMonth;

    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}