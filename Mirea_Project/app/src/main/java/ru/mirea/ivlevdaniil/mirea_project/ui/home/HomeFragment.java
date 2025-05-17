package ru.mirea.ivlevdaniil.mirea_project.ui.home;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.ivlevdaniil.mirea_project.R;

public class HomeFragment extends Fragment {

    private TextView greetingText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        greetingText = root.findViewById(R.id.greetingText);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            greetingText.setText("Добро пожаловать,\n" + email + "!");
        } else {
            greetingText.setText("Добро пожаловать в MireaProject!");
        }

        return root;
    }
}
