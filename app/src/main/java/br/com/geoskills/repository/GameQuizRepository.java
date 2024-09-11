package br.com.geoskills.repository;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.geoskills.enums.QuestionEnum;
import br.com.geoskills.model.Question;

public class GameQuizRepository {
   private static GameQuizRepository instance = null;
   private FirestoreRepository firestoreRepository;
   private final CollectionReference collectionQuestions;
   private List<Question> geographyQuestions = new ArrayList<>();

   public static GameQuizRepository getInstance() {
      if (instance == null) {
         instance = new GameQuizRepository();
      }
      return instance;
   }

   public GameQuizRepository() {
      firestoreRepository = FirestoreRepository.getInstance();
      collectionQuestions = firestoreRepository.getFirestoreDb().collection(QuestionEnum.COLLECTION_QUESTION.getValue());
   }

   public void addQuestionOnDb(OnCompleteListener<Void> onCompleteListener) {
      getListQuestions();
      for (Question q : geographyQuestions) {
         collectionQuestions.document().set(q).addOnCompleteListener(onCompleteListener);
      }
   }

   public void getAllQuestionsOnDb(OnCompleteListener<QuerySnapshot> onCompleteListener) {
      collectionQuestions.get().addOnCompleteListener(onCompleteListener);
//        collectionUser.orderBy(AuthEnum.POINTS.getValue(), Query.Direction.DESCENDING).addSnapshotListener(eventListener);
   }


   public List<Question> getListQuestions() {
//        ArrayList<Question> geographyQuestions = new ArrayList<>();





      String statement1 = "O que é uma projeção cartográfica?";
      String answer1 = "Método para representar a superfície curva da Terra em uma superfície plana.";
      String[] choices1 = {"Um tipo de mapa", "Uma ferramenta de navegação", "Método para representar a superfície curva da Terra em uma superfície plana.", "Uma escala de medição"};
      String userAnswer1 = "";
      String explanation1 = "Uma projeção cartográfica é um método para representar a superfície esférica ou curva da Terra em um plano, necessário para criar mapas.";
      Question question1 = new Question(statement1, answer1, choices1, userAnswer1, explanation1);
      geographyQuestions.add(question1);

      String statement2 = "Qual é a principal característica da projeção de Mercator?";
      String answer2 = "Mantém a forma, mas distorce as áreas.";
      String[] choices2 = {"Mantém a área, mas distorce as formas.", "Mantém a forma, mas distorce as áreas.", "Mantém a distância entre os pontos.", "Mantém a direção em longas distâncias."};
      String userAnswer2 = "";
      String explanation2 = "A projeção de Mercator é conhecida por preservar a forma dos continentes, mas distorcer suas áreas, especialmente perto dos polos.";
      Question question2 = new Question(statement2, answer2, choices2, userAnswer2, explanation2);
      geographyQuestions.add(question2);

      String statement3 = "Qual é a linha imaginária que divide a Terra em hemisférios norte e sul?";
      String answer3 = "Linha do Equador";
      String[] choices3 = {"Meridiano de Greenwich", "Trópico de Capricórnio", "Linha do Equador", "Círculo Polar Ártico"};
      String userAnswer3 = "";
      String explanation3 = "A Linha do Equador é a linha imaginária que divide a Terra em hemisférios norte e sul, e está situada a igual distância dos polos.";
      Question question3 = new Question(statement3, answer3, choices3, userAnswer3, explanation3);
      geographyQuestions.add(question3);

      String statement4 = "O que representa a escala em um mapa?";
      String answer4 = "A relação entre a distância no mapa e a distância real.";
      String[] choices4 = {"A altura dos elementos representados.", "A direção norte-sul.", "A relação entre a distância no mapa e a distância real.", "A área de cobertura do mapa."};
      String userAnswer4 = "";
      String explanation4 = "A escala de um mapa indica quantas vezes a área representada foi reduzida em relação ao seu tamanho real.";
      Question question4 = new Question(statement4, answer4, choices4, userAnswer4, explanation4);
      geographyQuestions.add(question4);

      String statement5 = "Qual é o objetivo de uma legenda em um mapa?";
      String answer5 = "Explicar o significado dos símbolos usados no mapa.";
      String[] choices5 = {"Mostrar a escala do mapa.", "Indicar a direção norte.", "Explicar o significado dos símbolos usados no mapa.", "Determinar a altitude dos pontos no mapa."};
      String userAnswer5 = "";
      String explanation5 = "A legenda de um mapa é essencial para que o usuário entenda o que cada símbolo, linha ou cor representa.";
      Question question5 = new Question(statement5, answer5, choices5, userAnswer5, explanation5);
      geographyQuestions.add(question5);

      String statement6 = "O que é um meridiano?";
      String answer6 = "Uma linha imaginária que vai do Polo Norte ao Polo Sul.";
      String[] choices6 = {"Uma linha que divide o Equador.", "Uma linha que circunda a Terra no sentido leste-oeste.", "Uma linha imaginária que vai do Polo Norte ao Polo Sul.", "Uma linha de igual altitude."};
      String userAnswer6 = "";
      String explanation6 = "Meridianos são linhas imaginárias que correm do Polo Norte ao Polo Sul, usadas para definir a longitude.";
      Question question6 = new Question(statement6, answer6, choices6, userAnswer6, explanation6);
      geographyQuestions.add(question6);

      String statement7 = "Qual é o meridiano que serve de referência para a longitude 0°?";
      String answer7 = "Meridiano de Greenwich";
      String[] choices7 = {"Meridiano de Brasília", "Meridiano de Paris", "Meridiano de Greenwich", "Meridiano do Equador"};
      String userAnswer7 = "";
      String explanation7 = "O Meridiano de Greenwich, localizado em Londres, serve como linha de referência para a longitude 0°.";
      Question question7 = new Question(statement7, answer7, choices7, userAnswer7, explanation7);
      geographyQuestions.add(question7);

      String statement8 = "Como são chamados os mapas que representam a distribuição de um fenômeno específico, como população ou vegetação?";
      String answer8 = "Mapas temáticos";
      String[] choices8 = {"Mapas topográficos", "Mapas políticos", "Mapas físicos", "Mapas temáticos"};
      String userAnswer8 = "";
      String explanation8 = "Mapas temáticos representam a distribuição de um ou mais fenômenos específicos em uma determinada área.";
      Question question8 = new Question(statement8, answer8, choices8, userAnswer8, explanation8);
      geographyQuestions.add(question8);

      String statement9 = "Qual é o nome dado à técnica de mapear o relevo terrestre em três dimensões?";
      String answer9 = "Modelagem de Terreno";
      String[] choices9 = {"Projeção Cilíndrica", "Cartografia Digital", "Modelagem de Terreno", "Curvas de Nível"};
      String userAnswer9 = "";
      String explanation9 = "Modelagem de Terreno é uma técnica usada para representar o relevo terrestre em três dimensões, comumente usada em sistemas de informação geográfica (SIG).";
      Question question9 = new Question(statement9, answer9, choices9, userAnswer9, explanation9);
      geographyQuestions.add(question9);

      String statement10 = "O que são curvas de nível em um mapa topográfico?";
      String answer10 = "Linhas que conectam pontos de igual altitude.";
      String[] choices10 = {"Linhas que indicam o curso dos rios.", "Linhas que conectam pontos de igual latitude.", "Linhas que conectam pontos de igual altitude.", "Linhas que indicam fronteiras políticas."};
      String userAnswer10 = "";
      String explanation10 = "Curvas de nível são usadas em mapas topográficos para representar a elevação do terreno, conectando pontos de igual altitude.";
      Question question10 = new Question(statement10, answer10, choices10, userAnswer10, explanation10);
      geographyQuestions.add(question10);

      String statement11 = "Qual a principal função dos paralelos em um mapa?";
      String answer11 = "Determinar a latitude.";
      String[] choices11 = {"Determinar a longitude.", "Determinar a altitude.", "Determinar a latitude.", "Determinar a distância entre pontos."};
      String userAnswer11 = "";
      String explanation11 = "Paralelos são linhas imaginárias que circundam a Terra e são usadas para determinar a latitude de um ponto.";
      Question question11 = new Question(statement11, answer11, choices11, userAnswer11, explanation11);
      geographyQuestions.add(question11);

      String statement12 = "Qual é a diferença entre um mapa físico e um mapa político?";
      String answer12 = "O mapa físico mostra características naturais, enquanto o político mostra fronteiras e divisões administrativas.";
      String[] choices12 = {"O mapa físico mostra fronteiras, e o político mostra recursos naturais.", "O mapa político mostra características naturais, enquanto o físico mostra infraestrutura.", "O mapa físico mostra características naturais, enquanto o político mostra fronteiras e divisões administrativas.", "O mapa físico é tridimensional e o político é bidimensional."};
      String userAnswer12 = "";
      String explanation12 = "Mapas físicos representam características naturais da Terra, enquanto mapas políticos mostram fronteiras, cidades e divisões administrativas.";
      Question question12 = new Question(statement12, answer12, choices12, userAnswer12, explanation12);
      geographyQuestions.add(question12);

      String statement13 = "O que é uma carta náutica?";
      String answer13 = "Um mapa que representa áreas navegáveis, incluindo profundidades e características costeiras.";
      String[] choices13 = {"Um mapa que mostra as fronteiras de países costeiros.", "Um mapa que representa áreas navegáveis, incluindo profundidades e características costeiras.", "Um tipo de mapa temático.", "Uma ferramenta usada para medir distâncias em mapas."};
      String userAnswer13 = "";
      String explanation13 = "Cartas náuticas são usadas para navegação marítima, representando áreas navegáveis, profundidades e características da costa.";
      Question question13 = new Question(statement13, answer13, choices13, userAnswer13, explanation13);
      geographyQuestions.add(question13);

      String statement14 = "Como são chamadas as linhas que conectam pontos de igual pressão atmosférica em um mapa?";
      String answer14 = "Isóbaras";
      String[] choices14 = {"Isoietas", "Isotermas", "Isóbaras", "Isócronas"};
      String userAnswer14 = "";
      String explanation14 = "Isóbaras são linhas em mapas meteorológicos que conectam pontos de igual pressão atmosférica.";
      Question question14 = new Question(statement14, answer14, choices14, userAnswer14, explanation14);
      geographyQuestions.add(question14);

      String statement15 = "Qual é a escala ideal para um mapa de uma cidade pequena?";
      String answer15 = "1:10.000";
      String[] choices15 = {"1:50.000", "1:10.000", "1:100.000", "1:500.000"};
      String userAnswer15 = "";
      String explanation15 = "A escala 1:10.000 é adequada para mapear cidades pequenas, pois oferece um bom nível de detalhe para a área representada.";
      Question question15 = new Question(statement15, answer15, choices15, userAnswer15, explanation15);
      geographyQuestions.add(question15);

      String statement16 = "O que é cartografia digital?";
      String answer16 = "O uso de tecnologias digitais para criar, analisar e visualizar mapas.";
      String[] choices16 = {"Um método tradicional de desenhar mapas.", "O estudo das estrelas para criar mapas.", "O uso de tecnologias digitais para criar, analisar e visualizar mapas.", "Uma técnica para medir distâncias em mapas."};
      String userAnswer16 = "";
      String explanation16 = "Cartografia digital envolve o uso de computadores e softwares para criar, analisar e visualizar mapas, facilitando o processamento e a interpretação de dados geoespaciais.";
      Question question16 = new Question(statement16, answer16, choices16, userAnswer16, explanation16);
      geographyQuestions.add(question16);

      String statement17 = "O que é um SIG (Sistema de Informação Geográfica)?";
      String answer17 = "Um sistema que captura, armazena, analisa e gerencia dados geográficos.";
      String[] choices17 = {"Um tipo de mapa interativo.", "Uma ferramenta de navegação GPS.", "Um sistema que captura, armazena, analisa e gerencia dados geográficos.", "Um mapa usado para prever o clima."};
      String userAnswer17 = "";
      String explanation17 = "SIGs são ferramentas poderosas usadas para capturar, armazenar, analisar e gerenciar dados relacionados a posições geográficas na superfície da Terra.";
      Question question17 = new Question(statement17, answer17, choices17, userAnswer17, explanation17);
      geographyQuestions.add(question17);

      String statement18 = "Qual é a principal característica da projeção cônica?";
      String answer18 = "É usada para mapear áreas de latitude média, como os Estados Unidos e a Europa.";
      String[] choices18 = {"Mantém a forma em áreas polares.", "É usada para mapear áreas de latitude média, como os Estados Unidos e a Europa.", "Mantém a área em regiões tropicais.", "É ideal para mapear os oceanos."};
      String userAnswer18 = "";
      String explanation18 = "A projeção cônica é particularmente útil para mapear regiões em latitudes médias, mantendo a forma e a escala dessas áreas.";
      Question question18 = new Question(statement18, answer18, choices18, userAnswer18, explanation18);
      geographyQuestions.add(question18);

      String statement19 = "Qual é a diferença entre latitude e longitude?";
      String answer19 = "Latitude mede a distância ao norte ou ao sul do Equador, enquanto longitude mede a distância a leste ou oeste do Meridiano de Greenwich.";
      String[] choices19 = {"Latitude mede a distância ao norte ou ao sul do Equador, enquanto longitude mede a distância a leste ou oeste do Meridiano de Greenwich.", "Longitude mede a altura dos continentes, e latitude mede a profundidade dos oceanos.", "Latitude mede a direção dos ventos, e longitude mede a temperatura do solo.", "Longitude e latitude são a mesma coisa."};
      String userAnswer19 = "";
      String explanation19 = "Latitude e longitude são sistemas de coordenadas usados para localizar qualquer ponto na Terra. Latitude refere-se à posição norte-sul, e longitude refere-se à posição leste-oeste.";
      Question question19 = new Question(statement19, answer19, choices19, userAnswer19, explanation19);
      geographyQuestions.add(question19);

      String statement20 = "O que é uma carta topográfica?";
      String answer20 = "Um tipo de mapa que mostra o relevo de uma área, incluindo curvas de nível.";
      String[] choices20 = {"Um tipo de mapa climático.", "Um mapa que mostra a distribuição populacional.", "Um tipo de mapa que mostra o relevo de uma área, incluindo curvas de nível.", "Um mapa usado para navegação aérea."};
      String userAnswer20 = "";
      String explanation20 = "Cartas topográficas são mapas detalhados que representam o relevo de uma área, usando curvas de nível para mostrar a elevação do terreno.";
      Question question20 = new Question(statement20, answer20, choices20, userAnswer20, explanation20);
      geographyQuestions.add(question20);

      // Pergunta 1
      String statement21 = "Qual é a capital do Brasil?";
      String answer21 = "Brasília";
      String[] choices21 = {"Rio de Janeiro", "São Paulo", "Brasília", "Salvador"};
      String userAnswer21 = "";
      String explanation21 = "Brasília é a capital do Brasil desde 1960, quando substituiu o Rio de Janeiro.";
      Question question21 = new Question(statement1, answer1, choices1, userAnswer1, explanation1);
      geographyQuestions.add(question21);

// Pergunta 2
      String statement22 = "Qual é o maior oceano do mundo?";
      String answer22 = "Oceano Pacífico";
      String[] choices22 = {"Oceano Atlântico", "Oceano Índico", "Oceano Pacífico", "Oceano Antártico"};
      String userAnswer22 = "";
      String explanation22 = "O Oceano Pacífico é o maior e mais profundo oceano do mundo, cobrindo mais de 63 milhões de milhas quadradas.";
      Question question22 = new Question(statement2, answer2, choices2, userAnswer2, explanation2);
      geographyQuestions.add(question22);

// Pergunta 3
      String statement23 = "Qual é o ponto mais alto do mundo?";
      String answer23 = "Monte Everest";
      String[] choices23 = {"Monte Kilimanjaro", "Monte Aconcágua", "Monte Everest", "Monte McKinley"};
      String userAnswer23 = "";
      String explanation23 = "O Monte Everest, localizado na cordilheira do Himalaia, tem 8.848 metros de altura, tornando-se o ponto mais alto do mundo.";
      Question question23 = new Question(statement3, answer3, choices3, userAnswer3, explanation3);
      geographyQuestions.add(question23);

// Pergunta 4
      String statement24 = "Qual é o maior rio do mundo em volume de água?";
      String answer24 = "Rio Amazonas";
      String[] choices24 = {"Rio Nilo", "Rio Amazonas", "Rio Yangtzé", "Rio Mississipi"};
      String userAnswer24 = "";
      String explanation24 = "O Rio Amazonas possui o maior volume de água do mundo, com uma descarga média de aproximadamente 209.000 metros cúbicos por segundo.";
      Question question24 = new Question(statement4, answer4, choices4, userAnswer4, explanation4);
      geographyQuestions.add(question24);

// Pergunta 5
      String statement25 = "Qual é o maior deserto do mundo?";
      String answer25 = "Deserto do Saara";
      String[] choices25 = {"Deserto de Atacama", "Deserto do Saara", "Deserto da Arábia", "Deserto da Patagônia"};
      String userAnswer25 = "";
      String explanation25 = "O Deserto do Saara é o maior deserto quente do mundo, cobrindo uma área de cerca de 9,2 milhões de quilômetros quadrados.";
      Question question25 = new Question(statement5, answer5, choices5, userAnswer5, explanation5);
      geographyQuestions.add(question25);

      Collections.shuffle(geographyQuestions, new Random());

      return geographyQuestions.subList(0, 6);
   }
}
