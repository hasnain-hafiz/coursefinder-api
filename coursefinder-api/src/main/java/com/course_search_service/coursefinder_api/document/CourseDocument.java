package com.course_search_service.coursefinder_api.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
/**
 * Represents an Elasticsearch document for a course. This class serves as a data model
 * for indexing and searching courses in an Elasticsearch datastore. Each field in this
 * class is mapped to a specific field type in Elasticsearch.
 *
 * Fields in the document include:
 * - Id: Unique identifier for the course document.
 * - Title: Title of the course, stored as text for full-text search.
 * - Description: Detailed description of the course, stored as text for full-text search.
 * - Category: Category of the course, stored as a keyword for filtering.
 * - Type: Type of the course (values: ONE_TIME, COURSE, or CLUB), stored as a keyword.
 * - GradeRange: Grade range the course targets, stored as a keyword.
 * - MinAge: Minimum age required for the course, stored as an integer for range queries.
 * - MaxAge: Maximum age allowed for the course, stored as an integer for range queries.
 * - Price: Price of the course, stored as a double for range and sorting operations.
 * - NextSessionDate: The date and time of the next session of the course, stored as a date for filtering and sorting.
 *
 * Annotations:
 * - {@link Document}: Maps this class to an Elasticsearch index with the name "courses".
 * - {@link Id}: Marks the `id` field as the unique identifier in the index.
 * - {@link Field}: Defines Elasticsearch-specific field types and properties for each attribute.
 *
 * Constructors:
 * - No-args constructor: Creates an instance without initializing any of the fields.
 * - All-args constructor: Creates an instance and initializes all fields.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "courses")
public class CourseDocument {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String type;
    @Field(type = FieldType.Keyword)
    private String gradeRange;
    @Field(type = FieldType.Integer)
    private int minAge;
    @Field(type = FieldType.Integer)
    private int maxAge;
    @Field(type = FieldType.Double)
    private double price;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant nextSessionDate;

}
