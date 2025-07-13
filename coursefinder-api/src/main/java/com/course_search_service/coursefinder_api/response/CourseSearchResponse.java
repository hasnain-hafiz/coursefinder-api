package com.course_search_service.coursefinder_api.response;

import lombok.Data;

/**
 * Represents the response model for course search results.
 * This class encapsulates the data of a course returned
 * as part of the search operation. The fields provide
 * high-level details about a course that match specific
 * search criteria.
 *
 * The attributes included in the response:
 * - id: Unique identifier of the course.
 * - title: Title of the course.
 * - category: Category under which the course is classified.
 * - type: Type of course (e.g., ONE_TIME, COURSE, CLUB).
 * - price: Cost of the course.
 * - nextSessionDate: The date of the next available session for the course.
 *
 * The data in this model is typically utilized by frontend or client
 * applications to display concise and relevant course details
 * to end-users based on their search actions.
 */
@Data
public class CourseSearchResponse {
    private String id;
    private String title;
    private String category;
    private String type;
    private double price;
    private String nextSessionDate;
}
