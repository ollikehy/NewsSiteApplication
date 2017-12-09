
package app.utility;

import app.domain.Image;
import app.domain.Story;
import app.repository.ImageRepository;
import app.repository.StoryRepository;
import java.io.IOException;
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
    public void setImage(MultipartFile file, Long id) throws IOException{
        Story story = storyRepository.getOne(id);
        if (file.getContentType().equals("png") || file.getContentType().equals("jpg")) {
            Image image = new Image();
            image.setContent(file.getBytes());
            image.setName(file.getName());
            story.setImage(image);
            image.setStory(story);
            imageRepository.save(image);
        }
    }
    
}
