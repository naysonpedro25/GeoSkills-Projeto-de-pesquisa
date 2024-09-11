package br.com.geoskills.view.games;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentQuizGameBinding;
import br.com.geoskills.model.Question;
import br.com.geoskills.repository.GameQuizRepository;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.view.FinalGamesDialog;
import br.com.geoskills.viewmodel.AuthViewModel;
import br.com.geoskills.viewmodel.GameViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class QuizGameFragment extends Fragment {
    private GameViewModel viewModel;
    private AuthViewModel authViewModel;
    private FragmentQuizGameBinding binding;
    private Runnable runnable;
    private Sounds sounds;
    private List<Question> questions = new ArrayList<>();
    private NavController navController;
    private int seconds = 30;
    private final int min = 0;
    private boolean closehandler = false;
    private Handler handler;
    private String currentOptionSelect = "";
    private int currentOptionSelectIndex = 0;
    private TextView[] options;
    private boolean closeBack = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        navController = Navigation.findNavController(view);


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!closeBack) {
                    closehandler = true;
                    sounds.clickSound();
                    navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
                }
            }
        });

        sounds = Sounds.getInstance(requireContext());
        options = new TextView[]{
                binding.bntOption1,
                binding.bntOption2,
                binding.bntOption3,
                binding.bntOption4
        };
//        getAllQuestion();
        questions = new GameQuizRepository().getListQuestions();
        initGame();
//        startTutorial3();
    }

    @SuppressLint("SetTextI18n")
    public void initGame() {
        binding.textCont.setText((currentOptionSelectIndex + 1) + " / " + questions.size());
        initClicks();
        initTimer();

        if (currentOptionSelectIndex == 0) {
            Question currentQuestion = questions.get(currentOptionSelectIndex);
            String[] choices = currentQuestion.getChoices();
            binding.textQuestion.setText(currentQuestion.getStatement());
            binding.bntOption1.setText(choices[0]);
            binding.bntOption2.setText(choices[1]);
            binding.bntOption3.setText(choices[2]);
            binding.bntOption4.setText(choices[3]);
        }
    }

    public void initClicks() {

        for (TextView option : options) {
            option.setOnClickListener(v -> {
                sounds.clickSound();
                String correctAnswer = questions.get(currentOptionSelectIndex).getAnswer();
                if (currentOptionSelect.isEmpty()) {

                    currentOptionSelect = option.getText().toString();
                    if (option.getText().toString().equals(correctAnswer)) {
                        blinkView(v, R.drawable.option_correct_bg, makeExplanationAlert(true, questions.get(currentOptionSelectIndex)));
                    } else {
                        blinkView(v, R.drawable.option_wrong_bg, makeExplanationAlert(false, questions.get(currentOptionSelectIndex)));

                    }
                    v.setBackgroundResource(R.drawable.option_wrong_bg);

                    revealCorrectOption();
                    closehandler = true;
                    questions.get(currentOptionSelectIndex).setUserAnswer(currentOptionSelect);
                    closeBack = true;
                }
            });
        }

        ClickShrinkEffectKt.applyClickShrink(binding.bntOption1, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption2, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption3, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption4, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnBack, .95f, 200L);


        binding.btnBack.setOnClickListener(v -> {
            if (!closeBack) {
                closehandler = true;
                sounds.clickSound();
                navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void nextQuestion() {
        currentOptionSelectIndex++;

        if (currentOptionSelectIndex < questions.size()) {

            currentOptionSelect = "";

            binding.bntOption1.setBackgroundResource(R.drawable.option_bg);
            binding.bntOption2.setBackgroundResource(R.drawable.option_bg);
            binding.bntOption3.setBackgroundResource(R.drawable.option_bg);
            binding.bntOption4.setBackgroundResource(R.drawable.option_bg);

            Question currentQuestion = questions.get(currentOptionSelectIndex);
            String[] choices = currentQuestion.getChoices();
            binding.textQuestion.setText(currentQuestion.getStatement());
            binding.bntOption1.setText(choices[0]);
            binding.bntOption2.setText(choices[1]);
            binding.bntOption3.setText(choices[2]);
            binding.bntOption4.setText(choices[3]);

            binding.textCont.setText((currentOptionSelectIndex + 1) + " / " + questions.size());
        } else {
            viewModel.updatePointsUser(getResultPoins());
            AlertDialog finhisAlert = makeAlertDialogFinalGame(getResultPoins());
            AlertDialog loadingAlert = viewModel.makeLoadingAlert(requireContext());
            loadingAlert.show();
            viewModel.getIsEndedQuiz().observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    loadingAlert.dismiss();
                    finhisAlert.show();
                }
            });
            closehandler = true;
        }
    }

    private AlertDialog makeAlertDialogFinalGame(int points) {
        FinalGamesDialog mAlert = new FinalGamesDialog(requireContext(), finalGamesDialog -> {
            sounds.clickSound();
            finalGamesDialog.dismiss();
            navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
        });

        mAlert.setTextPoints(String.valueOf(points));
        mAlert.setStarsCount(points);


        return mAlert;
    }


    private void revealCorrectOption() {
        String correctAnswer = questions.get(currentOptionSelectIndex).getAnswer();

        if (binding.bntOption1.getText().toString().equals(correctAnswer)) {
            binding.bntOption1.setBackgroundResource(R.drawable.option_correct_bg);
        } else if (binding.bntOption2.getText().toString().equals(correctAnswer)) {
            binding.bntOption2.setBackgroundResource(R.drawable.option_correct_bg);
        } else if (binding.bntOption3.getText().toString().equals(correctAnswer)) {
            binding.bntOption3.setBackgroundResource(R.drawable.option_correct_bg);
        } else if (binding.bntOption4.getText().toString().equals(correctAnswer)) {
            binding.bntOption4.setBackgroundResource(R.drawable.option_correct_bg);
        }


    }


    @SuppressLint("DefaultLocale")
    private void initTimer() {
        handler = new Handler();
        runnable = () -> {
            binding.textTimer.setText(String.format("%02d:%02d", min, seconds));
            if (seconds != 0)
                seconds--;
            else {
                CookieBar.build(requireActivity()).setTitle("Acabou o tempo").setMessage("Tente novamente!")
                        .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.yellow).setDuration(4000).setIcon(R.drawable.ic_error).show();
                handler.removeCallbacks(runnable);
                closehandler = true;
                navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
            }
            if (!closehandler)
                handler.postDelayed(runnable, 1000);
        };
        runnable.run();
    }

    private int getResultPoins() {
        int correctsAnswer = 0;
        for (Question question : questions) {
            if (question.getAnswer().equals(question.getUserAnswer())) correctsAnswer++;
        }
        return correctsAnswer;
    }

    private AlertDialog makeExplanationAlert(boolean isCorrect, Question question) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.explanation_quiz_alert, null);
        AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animdialog;

        TextView textExplanation = view.findViewById(R.id.text_explanation);
        TextView title = view.findViewById(R.id.title_explanation);
        Button btnNext = view.findViewById(R.id.btn_next_question);

        if (isCorrect) {
            title.setText("Resposta Correta");
            title.setBackgroundResource(R.drawable.bg_title_explanation_positive);
            btnNext.setBackgroundResource(R.drawable.btn_login_bg);
            textExplanation.setText(question.getExplanation());
        } else {
            title.setText("Resposta Errada");
            textExplanation.setText(question.getExplanation());
            btnNext.setBackgroundResource(R.drawable.btn_recover_bg);
            title.setBackgroundResource(R.drawable.bg_title_explanation_negative);
        }

        btnNext.setOnClickListener(v -> {
            sounds.clickSound();
            alertDialog.dismiss();
            seconds = 30;
            closehandler = false;
            handler.post(runnable);
            nextQuestion();
            closeBack = false;

        });

        ClickShrinkEffectKt.applyClickShrink(btnNext, .95f, 200L);

        return alertDialog;


    }

    private void blinkView(View v, int res, AlertDialog alertDialog) {
        final Boolean[] toggle = new Boolean[]{true, false};
        Handler handler1 = new Handler(Looper.getMainLooper());
        final int[] i = {0};
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                i[0]++;
                if (toggle[0]) {
                    v.setBackgroundResource(res);
                } else {
                    v.setBackgroundResource(R.drawable.option_bg);
                }
                toggle[0] = !toggle[0];
                if (i[0] == 7) {
                    handler1.removeCallbacks(this);
                    alertDialog.show();

                } else
                    handler1.postDelayed(this, 500);
            }
        };
        handler1.post(runnable1);

    }

    private void startTutorial3() {


        new TapTargetSequence(requireActivity()).targets(
                TapTarget.forView(binding.textTimer, "Clique aqui para abrir a pagina home")
                        .tintTarget(false)
                        .outerCircleColor(R.color.blue_deep_dark)
                        .outerCircleAlpha(.9f)
                        .cancelable(false)
                        .targetRadius(80)
                        .textColor(R.color.white)
                        .transparentTarget(true),

                TapTarget.forView(binding.textQuestion, "Clique aqui para abrir a pagina rank")
                        .tintTarget(false)
                        .outerCircleColor(R.color.blue_deep_dark)
                        .outerCircleAlpha(.9f)
                        .cancelable(false)
                        .targetRadius(240)

                        .transparentTarget(true)
                        .textColor(R.color.white),
                TapTarget.forView(binding.textCont, "Clique aqui para abrir a pagina rank")
                        .tintTarget(false)
                        .outerCircleColor(R.color.blue_deep_dark)
                        .outerCircleAlpha(.9f)
                        .cancelable(false)
                        .targetRadius(80)
                        .transparentTarget(true)
                        .textColor(R.color.white)


        ).listener(
                new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }
        ).start();
    }


    private void getAllQuestion() {

        viewModel.getAllQuestionsOnDb(requireView());
        AlertDialog alertDialog = authViewModel.makeLoadingAlert(requireContext());
        alertDialog.show();
        viewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                if (questions != null) {
                   questions.addAll(questions);
                    alertDialog.dismiss();
//                    initGame();
                }
            }
        });
    }


}