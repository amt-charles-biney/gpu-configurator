package com.amalitech.gpuconfigurator.service.search;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import com.amalitech.gpuconfigurator.dto.product.PageResponseDto;
import com.amalitech.gpuconfigurator.model.ProductDocument;
import com.amalitech.gpuconfigurator.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public PageResponseDto findProducts(
            String query,
            Integer pageNo,
            Integer pageSize,
            String sortField,
            String[] brands,
            String[] priceRanges
    ) {
        var searchQueryBuilder = NativeQuery.builder();

        if (!query.isBlank()) {
            searchQueryBuilder.withQuery(QueryBuilders.multiMatch().query(query)
                    .fields("productName", "productDescription", "productBrand")
                    .build()._toQuery());
        }

        searchQueryBuilder.withPageable(PageRequest.of(pageNo, pageSize));

        searchQueryBuilder.withSort(Sort.by(Sort.Direction.DESC, sortField));

        if (brands != null) {
            for (String brand : brands) {
                searchQueryBuilder.withFilter(
                        new MatchQuery.Builder().field("productBrand").query(brand).build()._toQuery());
            }
        }

        if (priceRanges != null) {
            for (String priceRange : priceRanges) {
                if (priceRange.contains("-")) {
                    String[] values = priceRange.split("-");
                    if (values.length == 2) {
                        searchQueryBuilder.withFilter(new RangeQuery.Builder()
                                .field("productPrice")
                                .gte(JsonData.of(Double.parseDouble(values[0].strip())))
                                .lte(JsonData.of(Double.parseDouble(values[1].strip())))
                                .build()._toQuery());
                    }
                } else if (priceRange.startsWith(">")) {
                    searchQueryBuilder.withFilter(new RangeQuery.Builder()
                            .field("productPrice")
                            .gte(JsonData.of(Double.parseDouble(priceRange.substring(1).strip())))
                            .build()._toQuery());
                }
            }
        }

        NativeQuery searchQuery = searchQueryBuilder.build();
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(searchQuery, ProductDocument.class);
        var searchPage = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        var products = searchPage.get().map(SearchHit::getContent).collect(Collectors.toList());
        PageResponseDto response = new PageResponseDto();
        response.setProducts(new ResponseMapper().getProductResponsesFromProductDocument(products));
        response.setTotal(products.size());
        return response;
    }
}
