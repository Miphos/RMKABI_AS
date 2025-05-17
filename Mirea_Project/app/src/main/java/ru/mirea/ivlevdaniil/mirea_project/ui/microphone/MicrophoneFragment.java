package ru.mirea.ivlevdaniil.mirea_project.ui.microphone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

import ru.mirea.ivlevdaniil.mirea_project.R;

public class MicrophoneFragment extends Fragment {

    private static final int REQUEST_MIC_PERMISSION = 102;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private String filePath;
    private Button btnRecord, btnPlay;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);
        btnRecord = view.findViewById(R.id.btnRecord);
        btnPlay = view.findViewById(R.id.btnPlay);

        filePath = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "record.3gp").getAbsolutePath();

        btnRecord.setOnClickListener(v -> {
            if (!checkPermissions()) {
                requestPermissions();
                return;
            }

            if (!isRecording) {
                startRecording();
                btnRecord.setText("Стоп запись");
                btnPlay.setEnabled(false);
            } else {
                stopRecording();
                btnRecord.setText("Запись");
                btnPlay.setEnabled(true);
            }
            isRecording = !isRecording;
        });

        btnPlay.setOnClickListener(v -> {
            if (!isPlaying) {
                startPlaying();
                btnPlay.setText("Стоп");
                btnRecord.setEnabled(false);
            } else {
                stopPlaying();
                btnPlay.setText("Воспроизвести");
                btnRecord.setEnabled(true);
            }
            isPlaying = !isPlaying;
        });

        return view;
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_MIC_PERMISSION);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
}
