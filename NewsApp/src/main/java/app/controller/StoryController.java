package app.controller;

import app.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StoryController {

    @Autowired
    private StoryRepository storyRepository;
    
    @GetMapping("/news/{id}")
    public String getStory(Model model, @PathVariable Long id) {
        model.addAttribute(storyRepository.getOne(id));
        return "redirect:/story";
    }
}
