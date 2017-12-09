package app.utility;

import app.domain.Image;
import app.domain.Story;
import app.repository.ImageRepository;
import app.repository.StoryRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class StoryUtility {

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private ImageRepository imageRepository;

    private List<String> errors;
    
    @Transactional
    public void setImage(MultipartFile file, Long id) throws IOException {
        Image image = new Image(LocalDateTime.now());
        image.setStory(storyRepository.getOne(id));
        
        try {
        image.setContent(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        image.setContentLength(file.getSize());
        image.setContentType(file.getContentType());
        image.setName(file.getName());
        
        Story story = storyRepository.getOne(id);
        
        imageRepository.save(image).setStory(story);
        storyRepository.getOne(id).setImage(image);
    }

    public List<String> validateInputs(String heading, String lead, String story, MultipartFile file) throws IOException{
        errors = new ArrayList();
        validateHeading(heading);        
        validateLead(lead);
        validateStory(story);
        validateImage(file);
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

    private void validateImage(MultipartFile file) throws IOException{
        if (!file.getContentType().contains("png")) {
            errors.add("Image must be of type png");
        } else if (file.getBytes().length > 1048576) {
            errors.add("Image is too large");
        } else if (file.isEmpty()) {
            errors.add("Add an image");
        }
    }
}
