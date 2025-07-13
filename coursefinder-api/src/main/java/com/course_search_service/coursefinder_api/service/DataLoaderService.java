package com.course_search_service.coursefinder_api.service;

import com.course_search_service.coursefinder_api.document.CourseDocument;
import com.course_search_service.coursefinder_api.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Service class responsible for loading and indexing course data into an Elasticsearch datastore.
 *
 * This service is annotated with {@link Service} to indicate that it is a Spring-managed component.
 * It facilitates the process of loading sample course data from a JSON file and saving it into
 * Elasticsearch for subsequent search and retrieval operations.
 *
 * Dependencies:
 * - {@link CourseRepository}: Provides repository methods for interacting with Elasticsearch
 *   for saving and querying course data.
 * - {@link ObjectMapper}: Used for parsing JSON content into Java objects.
 *
 * Key Responsibilities:
 * - Automates the initial loading of sample course data when the application starts.
 * - Parses JSON data structured as a list of {@link CourseDocument} objects.
 * - Handles the bulk indexing of parsed course data into Elasticsearch through the repository.
 *
 * Error Handling:
 * - If an {@link IOException} occurs during file reading or parsing, it will be propagated to
 *   alert the calling environment.
 */
@Service
public class DataLoaderService {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    public DataLoaderService(CourseRepository courseRepository, ObjectMapper objectMapper) {
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Loads sample course data from a JSON file and indexes it into an Elasticsearch datastore.
     *
     * This method performs the following operations:
     * - Reads a JSON file named "sample-courses.json" from the resources directory.
     * - Parses the JSON content into a list of {@link CourseDocument} objects using Jackson.
     * - Indexes the parsed course documents into Elasticsearch by invoking the saveAll() method
     *   of the {@link CourseRepository}.
     * - Prints a message to the console confirming that the data has been indexed.
     *
     * This method is annotated with {@link PostConstruct}, indicating that it is executed
     * automatically after the Spring bean has been initialized.
     *
     * @throws IOException if the JSON file cannot be found or read.
     */
    @PostConstruct
    public void loadData() throws IOException {
        // Read JSON file from resources
        InputStream is = getClass().getResourceAsStream("/sample-courses.json");
        List<CourseDocument> courses = objectMapper.readValue(
                is,
                new TypeReference<List<CourseDocument>>() {}
        );
        // Bulk save to Elasticsearch
        courseRepository.saveAll(courses);
        System.out.println("Sample courses indexed into Elasticsearch.");
    }
}
