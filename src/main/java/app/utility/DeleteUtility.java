package app.utility;

import app.domain.Author;
import app.domain.Category;
import app.domain.Story;
import app.repository.AuthorRepository;
import app.repository.CategoryRepository;
import app.repository.ImageRepository;
import app.repository.StoryRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class DeleteUtility {

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    //Deletes authors from a news story
    @Transactional
    public void deleteAuthors(String storyHeading) {
        List<Author> authors = authorRepository.findAll();
        for (Author author : authors) {
            for (Story story : author.getStoryList()) {
                if (story.getHeading().equals(storyHeading)) {
                    author.getStoryList().remove(story);
                    if (author.getStoryList().size() < 1) {
                        authorRepository.deleteById(author.getId());
                    }

                }
            }
        }
    }

    //Deletes categories from a news story
    @Transactional
    public void deleteCategories(String storyHeading) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            for (Story story : category.getStoryList()) {
                if (story.getHeading().equals(storyHeading)) {
                    category.getStoryList().remove(story);
                    if (category.getStoryList().size() < 1) {
                        categoryRepository.deleteById(category.getId());
                    }
                }
            }
        }
    }
}
