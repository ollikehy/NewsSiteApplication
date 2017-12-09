package app.utility;

import app.domain.Image;
import app.domain.Story;
import app.repository.ImageRepository;
import app.repository.StoryRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StoryUtility {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ImageRepository imageRepository;

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
}
