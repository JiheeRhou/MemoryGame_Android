package com.georgian.assignment3.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.georgian.assignment3.databinding.FragmentHomeBinding;
import com.georgian.assignment3.ViewModel.MemoryGameViewModel;

public class HomeFragment extends Fragment {

    // View binding object
    private FragmentHomeBinding binding;

    // ViewModel object
    MemoryGameViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Get the root view
        View root = binding.getRoot();

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MemoryGameViewModel.class);

        // Observe changes in the name LiveData and update the UI
        viewModel.getName().observe(getViewLifecycleOwner(), name -> {
            binding.textName.setText(name);
        });

        // OnClickListener for the Save button
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the name entered by the user
                String name = binding.editTextName.getText().toString().trim();

                // Set the name in the ViewModel
                viewModel.setName(name);
            }
        });
        return root;
    }
}