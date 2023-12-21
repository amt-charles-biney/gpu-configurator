package com.amalitech.gpuconfigurator.model.category;

        import com.amalitech.gpuconfigurator.model.Attribute;
        import com.amalitech.gpuconfigurator.model.AttributeOption;
        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoryConfigOption")
public class CategoryConfigOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_config_id")
    private CategoryConfig categoryConfig;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_option_id")
    private AttributeOption attributeOption;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "category_config")
    private List<CompatibleOption> compatibleOptions = new ArrayList<CompatibleOption>();

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
