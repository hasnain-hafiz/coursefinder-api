package com.course_search_service.coursefinder_api.service;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.course_search_service.coursefinder_api.document.CourseDocument;
import com.course_search_service.coursefinder_api.request.CourseSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.json.JsonData;

import java.util.Set;

/**
 * Service class for searching and filtering course documents within an Elasticsearch index.
 * Provides functionality to apply various search queries, filters, and sorting options
 * based on request parameters.
 * This service interacts with Elasticsearch using ElasticsearchOperations.
 */
@Service
@RequiredArgsConstructor
public class CourseSearchService {

    private final ElasticsearchOperations operations;
    private static final Set<String> VALID_SORT_FIELDS = Set.of("priceasc", "pricedesc");

    /**
     * Searches for courses based on the given criteria encapsulated in the request object.
     * This method constructs and executes an Elasticsearch query using various filters such as keywords,
     * age range, price range, category, type, and start date.
     * Additionally, it applies sorting and pagination based on the provided request parameters.
     *
     * @param request the CourseSearchRequest object containing search criteria including
     *                keywords, age range, price range, category, course type, start date,
     *                sorting preferences, page number, and page size for pagination.
     * @return a SearchHits object containing the search results for the course documents
     *         that match the specified criteria, along with additional metadata such as hit count.
     */
    public SearchHits<CourseDocument> searchCourses(CourseSearchRequest request) {
        var nativeQuery = new NativeQueryBuilder();
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();


        applyKeywordQuery(boolQuery, request.getKeyword());
        applyAgeRangeQuery(boolQuery, request.getMinAge(), request.getMaxAge());
        applyPriceRangeQuery(boolQuery, request.getMinPrice(), request.getMaxPrice());
        applyCategoryQuery(boolQuery, request.getCategory());
        applyTypeQuery(boolQuery, request.getType());
        applyStartDateQuery(boolQuery, request.getStartDate());
        applySortingQuery(nativeQuery, request.getSort());


        Query finalQuery = new Query.Builder()
                .bool(boolQuery.build())
                .build();

        nativeQuery.withQuery(finalQuery);

        nativeQuery.withPageable(PageRequest.of(request.getPage(), request.getSize()));

        return operations.search(nativeQuery.build(), CourseDocument.class);
    }

    /**
     * Applies a keyword-based query to the provided Boolean query builder.
     * This method checks if the given keyword is not null or empty, and if valid,
     * it adds a clause to the query that matches the keyword against multiple fields.
     *
     * @param boolQuery the BoolQuery.Builder object where the keyword query will be applied
     * @param keyword the keyword to be searched within the specified fields ("title" and "description")
     */
    private void applyKeywordQuery(BoolQuery.Builder boolQuery, String keyword){
        if(keyword != null && !keyword.isEmpty()){
            boolQuery.must(q -> q
                    .multiMatch(m -> m
                            .fields("title", "description")
                            .query(keyword)
                    )
            );
        }
    }

    /**
     * Applies an age range filter to the provided Boolean query builder.
     * This method filters courses based on the minimum and/or maximum age specified.
     * If both `minAge` and `maxAge` are provided, the filter ensures the course
     * adheres to the specified range. If only one is provided, a filter is applied
     * based on the available bound.
     *
     * @param boolQuery the BoolQuery.Builder object to which the age range filter will be applied
     * @param minAge the minimum age allowed for the course; if null, it is ignored
     * @param maxAge the maximum age allowed for the course; if null, it is ignored
     */
    private void applyAgeRangeQuery(BoolQuery.Builder boolQuery, Integer minAge, Integer maxAge){
        if (minAge != null && maxAge != null) {
            // Strict range between min and max
            boolQuery.filter(q -> q.range(r -> r
                    .field("minAge")
                    .gte(JsonData.of(minAge))
            ));
            boolQuery.filter(q -> q.range(r -> r
                    .field("maxAge")
                    .lte(JsonData.of(maxAge))
            ));
        } else if (minAge != null) {
            // Only minAge given: course must start at or after minAge
            boolQuery.filter(q -> q.range(r -> r
                    .field("minAge")
                    .gte(JsonData.of(minAge))
            ));
        } else if (maxAge != null) {
            // Only maxAge given: course must end at or before maxAge
            boolQuery.filter(q -> q.range(r -> r
                    .field("maxAge")
                    .lte(JsonData.of(maxAge))
            ));
        }
    }

    /**
     * Applies a price range filter to the provided Boolean query builder.
     * This method allows filtering courses based on the specified minimum
     * and/or maximum price. If both `minPrice` and `maxPrice` are provided,
     * the filter ensures the course's price falls within the specified range.
     * If only one bound is provided, the filter is applied based on the available bound.
     *
     * @param boolQuery the BoolQuery.Builder object to which the price range filter will be applied
     * @param minPrice the minimum price allowed for the course; if null, it is ignored
     * @param maxPrice the maximum price allowed for the course; if null, it is ignored
     */
    private void applyPriceRangeQuery(BoolQuery.Builder boolQuery, Double minPrice, Double maxPrice){
        if (minPrice != null && maxPrice != null) {
            boolQuery.filter(q -> q.range(r -> r
                    .field("price")
                    .gte(JsonData.of(minPrice))
            ));
            boolQuery.filter(q -> q.range(r -> r
                    .field("price")
                    .lte(JsonData.of(maxPrice))
            ));
        } else if (minPrice != null) {
            boolQuery.filter(q -> q.range(r -> r
                    .field("price")
                    .gte(JsonData.of(minPrice))
            ));
        } else if (maxPrice != null) {
            boolQuery.filter(q -> q.range(r -> r
                    .field("price")
                    .lte(JsonData.of(maxPrice))
            ));
        }
    }

    /**
     * Applies a category filter to the provided Boolean query builder.
     * This method filters courses based on the specified category.
     * If the category is not null or empty, it adds a filter condition
     * to match the courses with the given category.
     *
     * @param boolQuery the BoolQuery.Builder object to which the category filter will be applied
     * @param category the category to filter courses by; if null or empty, no filter is applied
     */
    private void applyCategoryQuery(BoolQuery.Builder boolQuery, String category){
        if(category != null && !category.isEmpty()){
            boolQuery.filter(q -> q
                    .term(t -> t
                            .field("category")
                            .value(category)
                    )
            );
        }
    }

    /**
     * Applies a type filter to the provided Boolean query builder.
     * This method filters courses based on the specified type.
     * If the type is not null or empty, it adds a filter condition
     * to match the courses with the given type.
     *
     * @param boolQuery the BoolQuery.Builder object to which the type filter will be applied
     * @param type the type to filter courses by; if null or empty, no filter is applied
     */
    private void applyTypeQuery(BoolQuery.Builder boolQuery, String type){
        if(type != null && !type.isEmpty()){
            boolQuery.filter(q -> q
                    .term(t -> t
                            .field("type")
                            .value(type)
                    )
            );
        }
    }

    /**
     * Applies a filter to the given BoolQuery.Builder to restrict results based on the specified startDate.
     * If the startDate is non-null and not empty, it adds a range query to filter results
     * where the "nextSessionDate" field is greater than or equal to the provided startDate.
     *
     * @param boolQuery the BoolQuery.Builder instance to which the filter will be applied
     * @param startDate the start date to filter results; should be in a valid date format and non-null/non-empty
     */
    private void applyStartDateQuery(BoolQuery.Builder boolQuery, String startDate){
        if(startDate != null && !startDate.isEmpty()){
            boolQuery.filter(q -> q
                    .range(r -> r
                            .field("nextSessionDate")
                            .gte(JsonData.of(startDate))
                    )
            );
        }
    }

    /**
     * Applies sorting to the given NativeQueryBuilder instance based on the provided sort parameter.
     *
     * @param nativeQuery the NativeQueryBuilder instance to which the sorting criteria will be applied
     * @param sort a string representing the sorting field and order. Valid values include "priceasc", "pricedesc",
     *             or null for default sorting by next session date in ascending order
     */
    private void applySortingQuery(NativeQueryBuilder nativeQuery, String sort) {

        String sortField = "nextSessionDate";
        SortOrder sortOrder = SortOrder.Asc;
        String normalizedSort = sort == null ? null : sort.toLowerCase();

        if (normalizedSort != null && VALID_SORT_FIELDS.contains(normalizedSort)) {
        switch (normalizedSort) {
            case "priceasc" -> {
                sortField = "price";
                sortOrder = SortOrder.Asc;
            }
            case "pricedesc" -> {
                sortField = "price";
                sortOrder = SortOrder.Desc;
            }
            default -> {
                sortField = "nextSessionDate";
                sortOrder = SortOrder.Asc;
            }
        }
    }

        SortOrder finalSortOrder = sortOrder;
        String finalSortField = sortField;
        nativeQuery.withSort(s -> s
                .field(f -> f
                        .field(finalSortField)
                        .order(finalSortOrder)
                )
        );

    }
}