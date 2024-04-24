package com.backend.demo.repository.suggestions;
import com.backend.demo.entity.suggestion.ComplexNameSuggestions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplexNameSuggestionsRepository extends JpaRepository<ComplexNameSuggestions, String> {
    List<ComplexNameSuggestions> findByComplexNameIgnoreCaseContaining(String complexName, Pageable pageable);

    ComplexNameSuggestions findByComplexName(String complexName);
}
