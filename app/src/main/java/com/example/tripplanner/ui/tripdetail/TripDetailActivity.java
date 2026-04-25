package com.example.tripplanner.ui.tripdetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.R;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.ItemEntity;
import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.ItemRepository;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.tripform.TripFormActivity;
import com.example.tripplanner.viewmodel.TripDetailViewModel;
import com.example.tripplanner.viewmodel.TripDetailViewModelFactory;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Pantalla que muestra el detalle de un viaje concreto.
 * Recarga datos en onResume para reflejar cambios al volver del formulario de edición.
 */
public class TripDetailActivity extends BaseActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private TripDetailViewModel viewModel;
    private TripEntity currentTrip;
    private long tripId = -1L;
    private boolean checklistEditMode = false;

    private TextView tvTitle;
    private TextView tvDestination;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvChecklistEmpty;
    private LinearLayout itemsContainer;
    private LinearLayout addItemContainer;
    private EditText etNewItem;
    private MaterialButton btnAddItem;
    private MaterialButton btnEditChecklist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        tripId = getIntent().getLongExtra(EXTRA_TRIP_ID, -1L);
        if (tripId < 0) {
            Toast.makeText(this, "Viaje no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        TripRepository tripRepository = new TripRepository(db);
        ItemRepository itemRepository = new ItemRepository(db);
        viewModel = new ViewModelProvider(this,
                new TripDetailViewModelFactory(tripRepository, itemRepository))
                .get(TripDetailViewModel.class);

        tvTitle = findViewById(R.id.tvTitle);
        tvDestination = findViewById(R.id.tvDestination);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvChecklistEmpty = findViewById(R.id.tvChecklistEmpty);
        itemsContainer = findViewById(R.id.itemsContainer);
        addItemContainer = findViewById(R.id.addItemContainer);
        etNewItem = findViewById(R.id.etNewItem);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnEditChecklist = findViewById(R.id.btnEditChecklist);
        MaterialButton btnEdit = findViewById(R.id.btnEditTrip);
        MaterialButton btnDelete = findViewById(R.id.btnDeleteTrip);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailActivity.this, TripFormActivity.class);
            intent.putExtra(TripFormActivity.EXTRA_TRIP_ID, tripId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
        btnBack.setOnClickListener(v -> finish());

        btnEditChecklist.setOnClickListener(v -> toggleChecklistEditMode());
        btnAddItem.setOnClickListener(v -> addNewItem());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tripId >= 0) {
            refreshTripFromDatabase();
            viewModel.createDefaultChecklistIfEmpty(tripId);
            renderChecklist();
        }
    }

    private void refreshTripFromDatabase() {
        currentTrip = viewModel.getTripById(tripId);
        if (currentTrip == null) {
            Toast.makeText(this, "Viaje no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvTitle.setText(currentTrip.getTitle());
        tvDestination.setText(currentTrip.getDestination() != null ? currentTrip.getDestination() : "");
        tvStartDate.setText("Inicio: " + viewModel.formatDate(currentTrip.getStartDate()));
        tvEndDate.setText("Fin: " + viewModel.formatDate(currentTrip.getEndDate()));
    }

    private void showDeleteConfirmation() {
        if (currentTrip == null) return;
        new AlertDialog.Builder(this)
                .setTitle("Eliminar viaje")
                .setMessage("¿Estás seguro de eliminar este viaje?")
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.deleteTrip(currentTrip);
                    Toast.makeText(TripDetailActivity.this, "Viaje eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }

    private void toggleChecklistEditMode() {
        checklistEditMode = !checklistEditMode;
        btnEditChecklist.setText(checklistEditMode ? "Listo" : "Editar");
        addItemContainer.setVisibility(checklistEditMode ? View.VISIBLE : View.GONE);
        renderChecklist();
    }

    private void addNewItem() {
        String name = etNewItem.getText() != null ? etNewItem.getText().toString().trim() : "";
        if (name.isEmpty()) {
            Toast.makeText(this, "Escribe el nombre de la tarea", Toast.LENGTH_SHORT).show();
            return;
        }
        long inserted = viewModel.addItem(tripId, name);
        if (inserted > 0) {
            etNewItem.setText("");
            renderChecklist();
        } else {
            Toast.makeText(this, "No se pudo agregar la tarea", Toast.LENGTH_SHORT).show();
        }
    }

    private void renderChecklist() {
        itemsContainer.removeAllViews();
        List<ItemEntity> items = viewModel.getItemsByTripId(tripId);
        if (items.isEmpty()) {
            tvChecklistEmpty.setVisibility(View.VISIBLE);
            return;
        }

        tvChecklistEmpty.setVisibility(View.GONE);
        for (ItemEntity item : items) {
            if (checklistEditMode) {
                itemsContainer.addView(buildEditableRow(item));
            } else {
                itemsContainer.addView(buildReadOnlyRow(item));
            }
        }
    }

    private View buildReadOnlyRow(ItemEntity item) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(item.getName());
        checkBox.setChecked(item.isCompleted());
        checkBox.setTextSize(15f);
        checkBox.setPadding(0, 8, 0, 8);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setCompleted(isChecked);
            viewModel.updateItem(item);
        });
        return checkBox;
    }

    private View buildEditableRow(ItemEntity item) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(0, 8, 0, 8);

        EditText editText = new EditText(this);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        editText.setLayoutParams(etParams);
        editText.setBackgroundResource(R.drawable.bg_edittext_rounded);
        editText.setPadding(24, 16, 24, 16);
        editText.setText(item.getName());
        editText.setTextSize(15f);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String newName = s.toString().trim();
                if (!newName.isEmpty() && !newName.equals(item.getName())) {
                    item.setName(newName);
                    viewModel.updateItem(item);
                }
            }
        });

        ImageButton btnDelete = new ImageButton(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMarginStart(12);
        btnDelete.setLayoutParams(btnParams);
        btnDelete.setImageResource(android.R.drawable.ic_menu_delete);
        btnDelete.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        btnDelete.setContentDescription("Eliminar tarea");
        btnDelete.setOnClickListener(v -> {
            viewModel.deleteItem(item);
            renderChecklist();
        });

        row.addView(editText);
        row.addView(btnDelete);
        return row;
    }
}
