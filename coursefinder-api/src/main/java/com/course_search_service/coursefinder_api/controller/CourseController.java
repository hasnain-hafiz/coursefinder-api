package com.course_search_service.coursefinder_api.controller;

import com.course_search_service.coursefinder_api.response.CourseSearchResponse;
import com.course_search_service.coursefinder_api.document.CourseDocument;
import com.course_search_service.coursefinder_api.request.CourseSearchRequest;
import com.course_search_service.coursefinder_api.service.CourseSearchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the course search endpoint by providing search functionalities for courses
 * based on various criteria such as keyword, age range, price range, category, type,
 * and start date. It also allows pagination and sorting of search results.
 *
 * This controller utilizes the {@link CourseSearchService} to perform the search logic
 * and maps results using {@link ModelMapper}.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class CourseController {

    private final CourseSearchService searchService;
    private final ModelMapper modelMapper;


    /**
     * Searches for courses based on the provided search criteria and returns a paginated
     * response with matching course details.
     *
     * @param request an instance of {@link CourseSearchRequest} containing search criteria such as
     *                keywords, age range, price range, category, type, start date, sort order, and pagination details.
     * @return a {@link ResponseEntity} containing a map with total number of matching courses
     *         and a list of {@link CourseSearchResponse} representing the matching courses.
     */
    @GetMapping("/")
    public ResponseEntity<?> search(@ModelAttribute CourseSearchRequest request) {
        SearchHits<CourseDocument> results = searchService.searchCourses(request);
        List<CourseDocument> courses = results.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        List<CourseSearchResponse> courseResponses = courses.stream()
                .map(doc -> modelMapper.map(doc, CourseSearchResponse.class))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("total", results.getTotalHits());
        response.put("courses", courseResponses);

        return ResponseEntity.ok(response);
    }
}
