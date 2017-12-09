package app.controller;

import app.domain.Image;
import app.repository.StoryRepository;
import app.domain.Story;
import app.repository.ImageRepository;
import app.utility.StoryUtility;
import java.io.IOException;
import java.time.LocalDateTime;
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

@Controller
public class NewsController {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StoryUtility storyUtility;

    @GetMapping("/news")
    public String getNews(Model model) {
        model.addAttribute("news", storyRepository.findAll());
        return "news";
    }

    @Transactional
    @PostMapping("/news")
    public String create(@RequestParam String heading, @RequestParam String lead, @RequestParam String story,
            @RequestParam("file") MultipartFile file) throws IOException {

        LocalDateTime date = LocalDateTime.now();

        Long newStoryId = storyRepository.save(new Story(heading, lead, story, date)).getId();
        
        storyUtility.setImage(file, newStoryId);

        return "redirect:/news";
    }

    @GetMapping("/news/{id}")
    public String getStory(Model model, @PathVariable Long id) {
        Story story = storyRepository.getOne(id);
        
        model.addAttribute("newsStory", story);
        model.addAttribute("imageId", story.getImage().getId());
        
        return "story";
    }
    
    @GetMapping(path = "/news/{id}/content", produces="image/png")
    @ResponseBody
    @Transactional
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image i = imageRepository.getOne(id);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(i.getContentType()));
        headers.setContentLength(i.getContentLength());
        
        return new ResponseEntity<>(i.getContent(), headers, HttpStatus.CREATED);
    }

}
