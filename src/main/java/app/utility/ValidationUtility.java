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
    }

    public List<String> validateInputs(String heading, String lead, String text,
            MultipartFile file, String authors, String categories) throws IOException {
        this.errors = new ArrayList();
        validateHeading(heading);
        validateLead(lead);
        validateText(text);
        validateImage(file);
        validateAuthors(authors);
        validateCategories(categories);
        return this.errors;
    }
    
    public List<String> validateEdit(String heading, String lead, String story,
            MultipartFile file, String authors, String categories) throws IOException {
        this.errors = new ArrayList();
        if (!heading.isEmpty()) {
            validateHeading(heading);
        }
        if (!lead.isEmpty()) {
            validateLead(lead);
        }
        if (!story.isEmpty()) {
            validateText(story);
        }
        if (!file.isEmpty()) {
            validateImage(file);
        }
        if (!authors.isEmpty()) {
            validateAuthors(authors);
        }
        if (!categories.isEmpty()) {
            validateCategories(categories);
        }
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

    private void validateText(String text) {
        if (text.isEmpty()) {
            errors.add("No story");
        } else if (text.length() > 10000) {
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

    private void validateCategories(String categories) {
        if (categories.isEmpty()) {
            errors.add("Add a category");
        }
    }
}
