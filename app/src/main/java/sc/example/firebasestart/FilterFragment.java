package sc.example.firebasestart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterFragment extends BottomSheetDialogFragment {

    public interface FilterListener {
        void onFilterApplied(String position, String team, int minAge, int maxAge);
    }

    private FilterListener listener;

    public void setFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        Spinner spinnerPosition = view.findViewById(R.id.spinnerPosition);
        EditText etTeam = view.findViewById(R.id.etTeam);
        EditText etMinAge = view.findViewById(R.id.etMinAge);
        EditText etMaxAge = view.findViewById(R.id.etMaxAge);
        Button btnApply = view.findViewById(R.id.btnApplyFilter);
        Button btnReset = view.findViewById(R.id.btnReset);

        // עמדות
        String[] positions = {"cm", "st", "wing", "cb", "goal keeper", "lb/rb"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, positions);
        spinnerPosition.setAdapter(adapter);

        btnApply.setOnClickListener(v -> {
            String position = spinnerPosition.getSelectedItem().toString();
            String team = etTeam.getText().toString().trim();

            int minAge = 0, maxAge = 100;
            try {
                if (!etMinAge.getText().toString().isEmpty())
                    minAge = Integer.parseInt(etMinAge.getText().toString());
                if (!etMaxAge.getText().toString().isEmpty())
                    maxAge = Integer.parseInt(etMaxAge.getText().toString());
            } catch (NumberFormatException e) { }

            if (listener != null)
                listener.onFilterApplied(position, team, minAge, maxAge);

            dismiss();
        });

        btnReset.setOnClickListener(v -> {
            if (listener != null)
                listener.onFilterApplied("הכל", "", 0, 100);
            dismiss();
        });

        return view;
    }
}
