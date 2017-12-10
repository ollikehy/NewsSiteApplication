
package app.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class ValidationUtility {
    
    private List<String> errors;
    
    public ValidationUtility() {
        this.errors = new ArrayList();
    }
    
    public List<String> validateInputs(String heading, String lead, String story, 
            MultipartFile file, String authors) throws IOException {
        errors = new ArrayList();
        validateHeading(heading);
        validateLead(lead);
        validateStory(story);
        validateImage(file);
        validateAuthors(authors);
        return errors;
    }

    private void validateHeading(String heading) {
        if (heading.isEmpty()) {
            errors.add("No heading");
        } else if (heading.length() > 1000) {
            errors.add("Heading is too long, maximum length 1000 symbols");
        }
    }

    private void validateLead(String lead) {
        if (lead.isEmpty()) {
            errors.add("No lead");
        } else if (lead.length() > 5000) {
            errors.add("Lead is too long, maximum length 1000 symbols");
        }
    }

    private void validateStory(String story) {
        if (story.isEmpty()) {
            errors.add("No story");
        } else if (story.length() > 10000) {
            errors.add("Story is too long, maximum length 10000 symbols");
        }
    }

    private void validateImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            errors.add("Add an image");
        } else if (!file.getContentType().contains("png")) {
            errors.add("Image must be of type png");
        } else if (file.getBytes().length > 1048576) {
            errors.add("Image is too large");
        }
    }

    private void validateAuthors(String authors) {
        if (authors.isEmpty()) {
            errors.add("Add an author");
        } 
    }
}
