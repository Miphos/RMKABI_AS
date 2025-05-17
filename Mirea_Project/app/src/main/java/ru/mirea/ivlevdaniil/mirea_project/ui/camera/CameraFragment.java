package ru.mirea.ivlevdaniil.mirea_project.ui.camera;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.ivlevdaniil.mirea_project.R;

public class CameraFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isPermissionGranted = false;

    private ImageView imageView;
    private Button btnOpenGallery;
    private Uri imageUri;
    private File photoFile;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == requireActivity().RESULT_OK && imageUri != null) {
                            imageView.setImageURI(imageUri);
                            saveToGallery(photoFile);  // ← только здесь выводим Toast
                        }
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        imageView = view.findViewById(R.id.imageView);
        btnOpenGallery = view.findViewById(R.id.btnOpenGallery);

        int camPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        isPermissionGranted = camPermission == PackageManager.PERMISSION_GRANTED;

        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION);
        }

        imageView.setOnClickListener(v -> {
            if (isPermissionGranted) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Нет разрешения на камеру", Toast.LENGTH_SHORT).show();
            }
        });

        // Навигация в GalleryFragment по нажатию кнопки
        btnOpenGallery.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_gallery);
        });

        return view;
    }

    private void openCamera() {
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            return;
        }

        String authority = requireContext().getPackageName() + ".fileprovider";
        imageUri = FileProvider.getUriForFile(requireContext(), authority, photoFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraLauncher.launch(intent);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String filename = "IMG_" + timestamp;
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(filename, ".jpg", storageDir);
    }

    private void saveToGallery(File imageFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MireaProject");

            ContentResolver resolver = requireContext().getContentResolver();
            Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try (OutputStream out = resolver.openOutputStream(uri);
                 InputStream in = new FileInputStream(imageFile)) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                Toast.makeText(getContext(), "Фото сохранено в Галерею MireaProject", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(getContext(), "Ошибка записи в галерею", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isPermissionGranted = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (!isPermissionGranted) {
                Toast.makeText(getContext(), "Камера запрещена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
