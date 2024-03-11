// SurveyController.java
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/create")
    public ResponseEntity<?> createSurvey(@RequestBody Survey survey) {
        surveyService.createSurvey(survey);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<Survey> getSurvey(@PathVariable("surveyId") Long surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok(survey);
    }

    @PostMapping("/{surveyId}/submit")
    public ResponseEntity<?> submitSurvey(@PathVariable("surveyId") Long surveyId, @RequestBody SurveyResponse response) {
        surveyService.submitSurveyResponse(surveyId, response);
        return ResponseEntity.ok().build();
    }
}

// Survey.java
@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questions;

    // Getters and setters
}

// Question.java
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    // Getters and setters
}

// SurveyResponse.java
@Entity
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<Answer> answers;

    // Getters and setters
}

// Answer.java
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private SurveyResponse response;

    // Getters and setters
}

// SurveyService.java
@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    public void createSurvey(Survey survey) {
        surveyRepository.save(survey);
    }

    public Survey getSurveyById(Long surveyId) {
        return surveyRepository.findById(surveyId).orElse(null);
    }

    public void submitSurveyResponse(Long surveyId, SurveyResponse response) {
        Survey survey = getSurveyById(surveyId);
        response.setSurvey(survey);
        surveyRepository.save(response);
    }
}
