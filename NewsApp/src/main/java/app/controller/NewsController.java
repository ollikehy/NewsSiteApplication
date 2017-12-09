package app.controller;

import app.repository.StoryRepository;
import app.domain.Story;
import app.repository.ImageRepository;
import app.utility.StoryUtility;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Story s = new Story(heading, lead, story, date);
        storyRepository.save(s);
        storyUtility.setImage(file, s.getId());

        return "redirect:/news";
    }

    @GetMapping("/news/{id}")
    public String getStory(Model model, @PathVariable Long id) {
        model.addAttribute("newsStory", storyRepository.getOne(id));
        return "story";
    }

}
