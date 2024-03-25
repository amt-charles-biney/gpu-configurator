package com.amalitech.gpuconfigurator.scheduler;

import com.amalitech.gpuconfigurator.mapper.CategoryMapper;
import com.amalitech.gpuconfigurator.mapper.ProductMapper;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductDocumentRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticsearchSynchronizer {
    private final String EVERY_TWO_MINUTES_CRON = "0 */2 * * * *";
    private static final long TWO_MINUTES = 120_000;

    private final ProductDocumentRepository productElasticsearchRepository;
    private final ProductRepository productJpaRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    private static Predicate getUpdatedAtPredicate(CriteriaBuilder criteriaBuilder, Root<?> root) {
        return criteriaBuilder.between(
                root.<Date>get("updatedAt"),
                criteriaBuilder.literal(new Timestamp(System.currentTimeMillis() - TWO_MINUTES)),
                criteriaBuilder.currentTimestamp()
        );
    }

    @Scheduled(cron = EVERY_TWO_MINUTES_CRON)
    @Transactional
    public void sync() {
        syncProducts();
    }

    private void syncProducts() {
        Specification<Product> productSpecification = (root, criteriaQuery, criteriaBuilder) ->
                getUpdatedAtPredicate(criteriaBuilder, root);

        List<Product> productList;
        if (productElasticsearchRepository.count() == 0) {
            productList = productJpaRepository.findAll();
        } else {
            productList = productJpaRepository.findAll(productSpecification);
        }

        for (Product product : productList) {
            var productDocument = productMapper.toProductDocument(product);
            productDocument.setCategory(categoryMapper.toCategoryDocument(product.getCategory()));
            productElasticsearchRepository.save(productDocument);
        }
    }
}