package com.course_search_service.coursefinder_api.repository;

import com.course_search_service.coursefinder_api.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD and search operations on
 * {@link CourseDocument} entities within an Elasticsearch datastore.
 *
 * This interface extends {@link ElasticsearchRepository}, inheriting functionality
 * such as saving, deleting, and retrieving documents by their ID, as well as
 * additional query execution methods provided by Spring Data Elasticsearch.
 *
 * Primary responsibilities include:
 * - Managing persistence of {@link CourseDocument} entities within the "courses" index.
 * - Facilitating full-text search and keyword-based filtering of course data.
 * - Supporting bulk operations like saving or deleting multiple documents at once.
 *
 * The type parameters define the following:
 * - {@link CourseDocument}: The entity type managed by the repository.
 * - {@link String}: The type of the unique identifier for the documents.
 *
 * The repository is annotated with {@link Repository}, indicating that it serves
 * as a mechanism for interacting with the Elasticsearch data source.
 */
@Repository
public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {
}

