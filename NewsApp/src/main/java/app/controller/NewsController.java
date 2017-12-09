
package app.controller;

import app.repository.StoryRepository;
import app.story.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewsController {
    
    @Autowired
    private StoryRepository storyRepository;
    
    @GetMapping("/news")
    public String getNews(Model model) {
        model.addAttribute("news", storyRepository.findAll());
        return "news";
    }
    
    @PostMapping("/news")
    public String create(@RequestParam String heading, @RequestParam String lead, @RequestParam String story) {
        Story s = new Story(heading, lead, story);
        storyRepository.save(s);
        return "redirect:/news";
    }
}
