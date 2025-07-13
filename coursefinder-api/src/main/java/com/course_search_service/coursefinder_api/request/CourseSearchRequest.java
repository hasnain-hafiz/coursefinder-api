package com.course_search_service.coursefinder_api.request;

import lombok.Data;

/**
 * Represents a request model for searching courses based on specific criteria.
 * This class is used to encapsulate filtering, sorting, and pagination parameters
 * for course search operations.
 *
 * The following filters and parameters can be specified:
 * - Keyword: A search term to match against course titles or descriptions.
 * - Age Range: Minimum and maximum age restrictions for the courses.
 * - Price Range: Minimum and maximum price restrictions for the courses.
 * - Category: The category under which the course falls.
 * - Type: Specifies the type of the course.
 * - Start Date: Filters courses starting on or after the given date.
 * - Sort: Defines the sorting order of the results (e.g., by price or start date).
 * - Pagination: Configures the page number and the number of results per page.
 *
 * This model is typically utilized in API endpoints to capture user preferences
 * and in service layers to dynamically build queries for fetching data.
 */
@Data
public class CourseSearchRequest {
    private String keyword;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private String category;
    private String type;
    private String startDate;
    private String sort;
    private Integer page=0;
    private Integer size=10;
}
