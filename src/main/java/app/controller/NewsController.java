package app.controller;

import app.domain.Image;
import app.repository.StoryRepository;
import app.domain.Story;
import app.repository.ImageRepository;
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
    private StoryUtility storyUtility;

    @Autowired
    private ValidationUtility validationUtility;

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

    @Transactional
    @PostMapping("/news")
    public String create(@RequestParam String heading, @RequestParam String lead, @RequestParam String story,
            @RequestParam("file") MultipartFile file, @RequestParam String authors, Model model) throws IOException {

        List<String> errors = validationUtility.validateInputs(heading, lead, story, file, authors);
        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
            return "errors";
        }
        LocalDateTime date = LocalDateTime.now();
        Long newStoryId = storyRepository.save(new Story(heading, lead, story, date)).getId();

        storyUtility.setImage(file, newStoryId);
        storyUtility.setAuthors(authors, newStoryId);

        return "redirect:/news";
    }

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

    @GetMapping("/news/all")
    public String getAllNews(Model model) {
        
        List<Long> imageIds = new ArrayList();
        for (Story story : storyRepository.findAll()) {
            imageIds.add(story.getImage().getId());
        }

        model.addAttribute("imageIds", imageIds);
        model.addAttribute("news", storyRepository.findAll());
        return "allNews";
    }
}
