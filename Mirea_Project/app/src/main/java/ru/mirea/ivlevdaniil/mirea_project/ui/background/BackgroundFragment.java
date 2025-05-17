package ru.mirea.ivlevdaniil.mirea_project.ui.background;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import ru.mirea.ivlevdaniil.mirea_project.databinding.FragmentBackgroundBinding;

public class BackgroundFragment extends Fragment {

    private FragmentBackgroundBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBackgroundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonStart.setOnClickListener(v -> {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED) // Только Wi-Fi
                    .build();

            ConnectivityManager cm = (ConnectivityManager)
                    requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork == null || !activeNetwork.isConnected()
                    || activeNetwork.getType() != ConnectivityManager.TYPE_WIFI) {
                Snackbar.make(binding.getRoot(),
                        "Задача не может быть запущена — нет подключения по Wi-Fi",
                        Snackbar.LENGTH_LONG).show();
                return;
            }

            OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest);

            Snackbar.make(binding.getRoot(),
                    "Фоновая задача запущена",
                    Snackbar.LENGTH_SHORT).show();

            // Наблюдение за завершением задачи
            WorkManager.getInstance(requireContext())
                    .getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                Snackbar.make(binding.getRoot(),
                                        "Задача завершена",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
