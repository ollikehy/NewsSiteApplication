package app.controller;

import app.domain.Image;
import app.repository.StoryRepository;
import app.domain.Story;
import app.repository.CategoryRepository;
import app.repository.ImageRepository;
import app.utility.DeleteUtility;
import app.utility.StoryUtility;
import app.utility.ValidationUtility;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
public class NewsController {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StoryUtility storyUtility;

    @Autowired
    private ValidationUtility validationUtility;

    @Autowired
    private DeleteUtility deleteUtility;

    //Frontpage, shows latest 5 news
    @GetMapping("/news")
    public String getLatestNews(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "localTime");
        model.addAttribute("news", storyRepository.findAll(pageable));

        List<Long> imageIds = new ArrayList();
        for (Story story : storyRepository.findAll(pageable)) {
            imageIds.add(story.getImage().getId());
        }
        model.addAttribute("imageIds", imageIds);
        return "news";
    }
    
    //Creates a new news story
    @Transactional
    @PostMapping("/news")
    public String create(@RequestParam String heading, @RequestParam String lead, @RequestParam String story,
            @RequestParam("file") MultipartFile file, @RequestParam String authors,
            @RequestParam String categories, Model model) throws IOException {

        List<String> errors = validationUtility.validateInputs(heading, lead, story, file, authors, categories);
        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
            return "errors";
        }
        LocalDateTime date = LocalDateTime.now();
        Long newStoryId = storyRepository.save(new Story(heading, lead, story, date)).getId();

        storyUtility.setImage(file, newStoryId);
        storyUtility.setAuthors(authors, newStoryId);
        storyUtility.setCategories(categories, newStoryId);

        return "redirect:/news";
    }

    //Returns a specific news story
    @GetMapping("/news/{id}")
    public String getStory(Model model, @PathVariable Long id) {
        Story story = storyRepository.getOne(id);
        story.incrementVisists();
        storyRepository.save(story);
        model.addAttribute("newsStory", story);
        model.addAttribute("imageId", story.getImage().getId());
        model.addAttribute("authors", story.getAuthorList());

        return "story";
    }

    //Returns a page where you can edit chosen news story
    @GetMapping("/news/{id}/edit")
    public String editStory(Model model, @PathVariable Long id) {
        Story story = storyRepository.getOne(id);

        model.addAttribute("newsStory", new Story(story.getHeading(), story.getLead(), story.getText(), story.getLocalTime()));
        model.addAttribute("storyId", id);
        return "edit";
    }

    //Edits a single piece of news, only params that that are not null are edited
    @Transactional
    @PostMapping("/news/{id}/edit")
    public String editNewsStory(@PathVariable Long id, @RequestParam String heading,
            @RequestParam String lead, @RequestParam String story,
            @RequestParam("file") MultipartFile file, @RequestParam String authors,
            @RequestParam String categories, Model model) throws IOException {

        List<String> errors = validationUtility.validateEdit(heading, lead, story, file, authors, categories);
        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
            return "errors";
        }

        storyUtility.editStory(id, heading, lead, story);
        if (!file.isEmpty()) {
            storyUtility.setImage(file, id);
        }
        if (!authors.isEmpty()) {
            Story editted = storyRepository.getOne(id);
            editted.getAuthorList().clear();
            storyRepository.save(editted);
            storyUtility.setAuthors(authors, id);
        }
        if (!categories.isEmpty()) {
            Story editted = storyRepository.getOne(id);
            editted.getCategoryList().clear();
            storyRepository.save(editted);
            storyUtility.setCategories(categories, id);
        }
        return "redirect:/news";
    }

    //Produces the image for the news stories
    @GetMapping(path = "/news/{id}/content", produces = "image/png")
    @ResponseBody
    @Transactional
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image i = imageRepository.getOne(id);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(i.getContentType()));
        headers.setContentLength(i.getContentLength());

        return new ResponseEntity<>(i.getContent(), headers, HttpStatus.CREATED);
    }

    //Returns all news sorted by release date
    @GetMapping("/news/all")
    public String getAllNews(Model model) {
        if (storyRepository.findAll().size() < 1) {
            return "redirect:/news";
        }
        Pageable pageable = PageRequest.of(0, storyRepository.findAll().size(), Sort.Direction.DESC, "localTime");
        model.addAttribute("news", storyRepository.findAll(pageable));
        return "allNews";
    }

    //Returns top five news sorted by amount of views
    @GetMapping("/news/trending")
    public String getTrending(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "visits");
        model.addAttribute("news", storyRepository.findAll(pageable));
        return "trending";
    }

    //Returns a page with a list of all categories
    @GetMapping("/news/categories")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    //Returns a single category
    @GetMapping("/news/categories/{id}")
    public String getCategory(Model model, @PathVariable Long id) {
        String categoryName = categoryRepository.getOne(id).getName();
        model.addAttribute("category", categoryName);
        model.addAttribute("news", storyUtility.getNewsByCategory(categoryName));
        return "category";
    }
}
