package com.use3w.grade.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {

    private final OpenAiChatModel model;

    public OpenAIChatService(OpenAiChatModel model) {
        this.model = model;
    }

    public String getFeedback(String answeredQuestions, String rawFeedback) {
        final String template = buildFeedbackTemplate(answeredQuestions, rawFeedback);
        final Prompt prompt = new Prompt(template);
        return model.call(prompt).getResult().getOutput().getText();
    }

    private String buildFeedbackTemplate(String answeredQuestions, String rawFeedback) {
        return String.format(
                """
                        Você é um professor universitário encarregado de fornecer feedback acadêmico. \
                        O feedback deve ser claro, direto e com no máximo 800 caracteres. \
                        Parabenize o aluno pelos pontos fortes e faça observações construtivas sobre as áreas que precisam de melhorias, \
                        sem focar excessivamente nos pontos fracos. \
                        Indique de forma objetiva o que pode ser aprimorado, mas mantendo um tom positivo e equilibrado. \
                        Utilize quebras de linha para facilitar a leitura e não use markdown. \
                        No final do feedback, traga alguma mensagem, para o avaliado continuar com um bom trabalho.
                        
                        Avaliacao:
                        %s
                        
                        Feedback do usuario:
                        %s""",
                answeredQuestions, rawFeedback
        );
    }

}
