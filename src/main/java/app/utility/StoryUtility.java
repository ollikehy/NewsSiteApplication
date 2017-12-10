package app.utility;

import app.domain.Author;
import app.domain.Image;
import app.domain.Story;
import app.repository.AuthorRepository;
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
    @Autowired
    private AuthorRepository authorRepository;

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

    @Transactional
    public void setAuthors(String authors, Long newStoryId) {
        Story story = storyRepository.getOne(newStoryId);
        String[] authorArray = authors.split(", ");

        for (String author : authorArray) {
            Author exists = authorRepository.findByName(author);
            if (exists != null) {
                List<Story> storyList = exists.getStoryList();
                storyList.add(story);
                story.getAuthorList().add(exists);
                storyRepository.save(story);
                authorRepository.save(exists);
            } else {
                Author newAuthor = new Author(author);
                List<Story> storyList = newAuthor.getStoryList();
                storyList.add(story);
                newAuthor.setStoryList(storyList);
                authorRepository.save(newAuthor);

                story.getAuthorList().add(newAuthor);
                storyRepository.save(story);
            }
        }
    }
}
