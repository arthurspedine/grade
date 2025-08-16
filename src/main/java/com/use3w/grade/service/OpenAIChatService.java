package com.use3w.grade.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class OpenAIChatService {

    @Value("${spring.ai.openai.api-key}")
    private String key;

    private final OpenAiChatModel model;

    public OpenAIChatService(OpenAiChatModel model) {
        this.model = model;
    }

    public String getFeedback(String answeredQuestions, String rawFeedback) {
        if (Objects.equals(key.toLowerCase(), "none")) {
            return "Chave de API do OpenAI não configurada.";
        }
        final String template = buildFeedbackTemplate(answeredQuestions, rawFeedback);
        final Prompt prompt = new Prompt(template);
        return model.call(prompt).getResult().getOutput().getText();
    }

    private static final String DEFAULT_PROMPT_PATH = "src/main/resources/feedback_prompt.txt";

    private String buildFeedbackTemplate(String answeredQuestions, String rawFeedback) {
        String template;
        try {
            Path path = Paths.get(DEFAULT_PROMPT_PATH);
            template = Files.readString(path);
        } catch (IOException e) {
            // fallback to hardcoded template if file is missing or unreadable
            template = "Você é um professor universitário encarregado de fornecer feedback acadêmico. \\\nO feedback deve ser claro, direto e com no máximo 800 caracteres. \\\nParabenize o aluno pelos pontos fortes e faça observações construtivas sobre as áreas que precisam de melhorias, \\\nsem focar excessivamente nos pontos fracos. \\\nIndique de forma objetiva o que pode ser aprimorado, mas mantendo um tom positivo e equilibrado. \\\nUtilize quebras de linha para facilitar a leitura e não use markdown. \\\nNo final do feedback, traga alguma mensagem, para o avaliado continuar com um bom trabalho.\n\nAvaliacao:\n%s\n\nFeedback do usuario:\n%s";
        }
        return String.format(template, answeredQuestions, rawFeedback);
    }

}
