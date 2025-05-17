package ru.mirea.ivlevdaniil.mirea_project.ui.firebase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import ru.mirea.ivlevdaniil.mirea_project.R;

public class NetworkFragment extends Fragment {
    private TextView resultView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        resultView = view.findViewById(R.id.textResult);
        new GetIpTask().execute("https://ipinfo.io/json");
        return view;
    }

    private class GetIpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
                reader.close();

                JSONObject json = new JSONObject(result.toString());
                return "IP: " + json.getString("ip") + "\nГород: " + json.getString("city") + "\nСтрана: " + json.getString("country");
            } catch (Exception e) {
                return "Ошибка: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            resultView.setText(result);
        }
    }
}
