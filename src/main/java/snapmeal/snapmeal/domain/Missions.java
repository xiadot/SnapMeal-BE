package snapmeal.snapmeal.domain;


import jakarta.persistence.*;
import lombok.*;
import snapmeal.snapmeal.domain.common.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "missions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missions extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    private String title;

    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}

