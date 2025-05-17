package ru.mirea.ivlevdaniil.mirea_project.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.mirea.ivlevdaniil.mirea_project.R;

public class ProfileFragment extends Fragment {
    private EditText nameField, groupField, favField;
    private Button saveBtn, btnChoosePhoto;
    private ImageView profileImage;
    private SharedPreferences prefs;
    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameField = view.findViewById(R.id.editName);
        groupField = view.findViewById(R.id.editGroup);
        favField = view.findViewById(R.id.editFavorite);
        saveBtn = view.findViewById(R.id.btnSave);
        btnChoosePhoto = view.findViewById(R.id.btnChoosePhoto);
        profileImage = view.findViewById(R.id.profileImage);

        prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        loadProfile();

        saveBtn.setOnClickListener(v -> {
            prefs.edit()
                    .putString("name", nameField.getText().toString())
                    .putString("group", groupField.getText().toString())
                    .putString("favorite", favField.getText().toString())
                    .apply();
            Toast.makeText(getContext(), "Сохранено!", Toast.LENGTH_SHORT).show();
        });

        btnChoosePhoto.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putBoolean("select_mode", true);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_gallery_select, args);
        });

        getParentFragmentManager().setFragmentResultListener("profile_image_result", this, (key, bundle) -> {
            imageUri = bundle.getParcelable("image_uri");
            if (imageUri != null) {
                profileImage.setImageURI(imageUri);
                prefs.edit().putString("profile_image_uri", imageUri.toString()).apply();
            }
        });

        return view;
    }

    private void loadProfile() {
        nameField.setText(prefs.getString("name", ""));
        groupField.setText(prefs.getString("group", ""));
        favField.setText(prefs.getString("favorite", ""));
        String savedUri = prefs.getString("profile_image_uri", null);
        if (savedUri != null) {
            imageUri = Uri.parse(savedUri);
            profileImage.setImageURI(imageUri);
        }
    }
}
