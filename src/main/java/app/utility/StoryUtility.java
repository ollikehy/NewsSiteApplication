package app.utility;

import app.domain.Author;
import app.domain.Category;
import app.domain.Image;
import app.domain.Story;
import app.repository.AuthorRepository;
import app.repository.CategoryRepository;
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
    @Autowired
    private CategoryRepository categoryRepository;

    //Sets the image on a news story
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

    //Sets the authors for a news story
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

    //Sets the categories for a news story
    @Transactional
    public void setCategories(String categories, Long newStoryId) {
        Story story = storyRepository.getOne(newStoryId);
        String[] categoryArray = categories.split(", ");

        for (String category : categoryArray) {
            Category exists = categoryRepository.findByName(category);

            if (exists != null) {
                List<Story> storyList = exists.getStoryList();
                storyList.add(story);
                story.getCategoryList().add(exists);
                storyRepository.save(story);
                categoryRepository.save(exists);
            } else {
                Category newCategory = new Category(category);
                List<Story> storyList = newCategory.getStoryList();
                storyList.add(story);
                newCategory.setStoryList(storyList);
                categoryRepository.save(newCategory);

                story.getCategoryList().add(newCategory);
                storyRepository.save(story);
            }
        }
    }

    //Edits the story based on what params are not null
    @Transactional
    public void editStory(Long id, String heading, String lead, String text
    ) throws IOException {
        Story edittedStory = storyRepository.getOne(id);

        if (!heading.isEmpty()) {
            edittedStory.setHeading(heading);
        }
        if (!lead.isEmpty()) {
            edittedStory.setLead(lead);
        }
        if (!text.isEmpty()) {
            edittedStory.setText(text);
        }

        storyRepository.save(edittedStory);
    }

    //Returns a list of news stories with chosen category
    public List<Story> getNewsByCategory(String categoryName) {
        List<Story> stories = new ArrayList();
        for (Story story : storyRepository.findAll()) {
            for (Category category : story.getCategoryList()) {
                if (category.getName().equals(categoryName)) {
                    stories.add(story);
                }
            }
        }

        return stories;
    }
}
