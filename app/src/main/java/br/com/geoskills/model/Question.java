package br.com.geoskills.model;

public class Question {

   private String statement;
   private String answer;
   private String userAnswer;
   private String[] choices;
   private String explanation;



   public Question(){

   }

   public Question( String statement, String answer, String[] choices, String userAnswer, String explanation) {
      this.statement = statement;
      this.answer = answer;
      this.choices = choices;
      this.userAnswer = userAnswer;
      this.explanation = explanation;
   }


   public String getExplanation(){
      return explanation;
   }

   public String getStatement() {
      return statement;
   }

   public String getAnswer() {
      return answer;
   }

   public String[] getChoices() {
      return choices;
   }

   public String getUserAnswer() {
      return userAnswer;
   }

   public void setUserAnswer(String userAnswer) {
      this.userAnswer = userAnswer;
   }

}
