package kz.school.grants.about_us_menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import kz.school.grants.R;


public class AboutUsFragment extends Fragment {
    View view;
    GridView gridView;
    ArrayList<Moderator> moderators;
    ModeratorsAdapter adapter;
//    LinearLayout askQuestion;

    public AboutUsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moderator, container, false);
        gridView = view.findViewById(R.id.gridView);

        /*
        askQuestion = view.findViewById(R.id.askQuestion);
        askQuestion.setOnClickListener(view -> {
            try {
                PackageManager packageManager = getActivity().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=+77072844575";
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
//                    getActivity().finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        */

        initGrid();
        return view;
    }

    public void initGrid() {
        moderators = new ArrayList<>();

        moderators.add(new Moderator(R.drawable.edu_olgo, "Educon"," ", R.color.second));
        moderators.add(new Moderator(R.drawable.d_logo, "Достық"," ", R.color.first));

        adapter = new ModeratorsAdapter(getActivity(), moderators);
        gridView.setAdapter(adapter);
    }
}
