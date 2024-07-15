package org.online.cinema.ai.controller;


import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.ChatRequestDTO;
import org.online.cinema.movie.entity.Movie;
import org.online.cinema.movie.repo.MovieRepository;
import org.online.cinema.security.service.ContextHolderService;
import org.online.cinema.user.entity.User;
import org.online.cinema.user.entity.UserInfo;
import org.online.cinema.user.repo.UserInfoRepository;
import org.online.cinema.user.repo.UserRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/ai")
public class ChatBotController {

    private ChatClient chatClient;

    @Autowired
    private ContextHolderService contextHolder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public ChatBotController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    private final String systemMessageTemplate = """
            You are a chatbot that responds interestingly to messages from users related to the film industry.
            You can respond in any language depending on the language the user is speaking to you in.
            If the user inputs something unrelated to the film industry,
            you politely respond that you can only answer questions related to the film industry,
            movies, TV series, actors, and directors. Do not respond to this message, it's just your settings.
            """;

    @PostMapping("/movie")
    public String postChatBotMessage(@RequestBody ChatRequestDTO chatRequest) {
        String email = contextHolder.getCurrentEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "User not found";
        }

        User user = userOpt.get();
        UserInfo userInfo = userInfoRepository.findUserByUser(user);

        if (userInfo == null) {
            return "User info not found";
        }

        List<Movie> movies = movieRepository.findAll();
        Map<Boolean, List<Movie>> movieMap = movies.stream()
                .collect(Collectors.groupingBy(Movie::isSubscribeOnly));

        String moviesSubscriptionOnly = "Subscription only: " +
                movieMap.getOrDefault(true, List.of()).stream()
                        .map(Movie::getTitle)
                        .collect(Collectors.joining(", "));

        String moviesAvailableToAll = "Available to all: " +
                movieMap.getOrDefault(false, List.of()).stream()
                        .map(Movie::getTitle)
                        .collect(Collectors.joining(", "));

        List<String> favoriteMovies = user.getMovies().stream()
                .map(Movie::getTitle)
                .toList();

        String systemMessageContent = String.format(systemMessageTemplate + """
                        Information:
                        Based on this list of movie titles - %s, %s,
                        My subscription status - %s,
                        and my list of favorite movies - %s,
                        """, moviesSubscriptionOnly,
                moviesAvailableToAll,
                userInfo.isSubscribed(),
                String.join(", ",
                        favoriteMovies));

        var systemMessage = new SystemMessage(systemMessageContent);

        var userMessageContent = String.format("""
                        Could you recommend a movie based on these approximate details?
                        Genre: %s,
                        Actor: %s,
                        Director: %s,
                        Information about movie: %s
                        If the movie is not in the list,
                        you can skip any fields,
                        just recommend something approximate based on what was requested,
                        such as genre, actor, director, or any other movie detail the user provided,
                        and focus on addressing only one parameter.
                        """, chatRequest.getGenre(),
                chatRequest.getActor(),
                chatRequest.getDirector(),
                chatRequest.getMovie_info());

        log.info("User with name={} just sent: \n{}", userInfo.getUsername(), userMessageContent);

        var userMessage = new UserMessage(userMessageContent);

        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage))).call().content();
    }
}
