package br.com.geoskills.view.homeviewpager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import br.com.geoskills.adapter.RankRecyclerViewAdapter;
import br.com.geoskills.databinding.FragmentRankBinding;
import br.com.geoskills.model.User;
import br.com.geoskills.repository.AuthRepository;
import br.com.geoskills.viewmodel.RankViewModel;

import java.util.List;
import java.util.Objects;


public class RankFragment extends Fragment {
    private FragmentRankBinding binding;
    private RankViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRankBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RankViewModel.class);
        addUsersOnRecycler();
    }


    private void addUsersOnRecycler(){
        viewModel.getAllUsersOnDb(requireView());
        viewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), users -> {
            if(users != null && !users.isEmpty()){
                initRecycler(requireContext(), users);
            }
        });
    }

    public void initRecycler(Context context, List<User> listUsers) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rankRecycler.setLayoutManager(linearLayoutManager);
        binding.rankRecycler.setHasFixedSize(true);
        binding.rankRecycler.setAdapter(new RankRecyclerViewAdapter(listUsers, Objects.requireNonNull(AuthRepository.getInstance().getCurrentUser().getValue()).getUid()));
    }

}