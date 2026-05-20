package com.mindmatrix.jalsanchaytracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mindmatrix.jalsanchaytracker.data.RainfallEntry
import com.mindmatrix.jalsanchaytracker.model.SurfaceType
import com.mindmatrix.jalsanchaytracker.ui.DashboardState
import com.mindmatrix.jalsanchaytracker.ui.JalSanchayViewModel
import com.mindmatrix.jalsanchaytracker.ui.RainfallEntryAdapter
import com.mindmatrix.jalsanchaytracker.ui.WaterTankView
import com.mindmatrix.jalsanchaytracker.ui.WaterTipEngine
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val viewModel: JalSanchayViewModel by viewModels()
    private val decimal = DecimalFormat("#,##0.#")
    private val whole = DecimalFormat("#,##0")
    private lateinit var adapter: RainfallEntryAdapter
    private var currentState = DashboardState()
    private var selectedSurfaceType = SurfaceType.CONCRETE
    private lateinit var screens: Map<Int, View>
    private var populatedProfileKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupScreens()
        setupSurfaceSpinner()
        setupHistoryList()
        setupActions()

        viewModel.state.observe(this) { state ->
            currentState = state
            render(state)
        }
    }

    private fun setupScreens() {
        screens = mapOf(
            R.id.dashboardScreen to findViewById(R.id.dashboardScreen),
            R.id.setupScreen to findViewById(R.id.setupScreen),
            R.id.rainfallScreen to findViewById(R.id.rainfallScreen),
            R.id.profileScreen to findViewById(R.id.profileScreen),
            R.id.reportScreen to findViewById(R.id.reportScreen),
            R.id.historyScreen to findViewById(R.id.historyScreen)
        )

        findViewById<Button>(R.id.navDashboardButton).setOnClickListener { showScreen(R.id.dashboardScreen) }
        findViewById<Button>(R.id.navSetupButton).setOnClickListener { showScreen(R.id.setupScreen) }
        findViewById<Button>(R.id.navRainfallButton).setOnClickListener { showScreen(R.id.rainfallScreen) }
        findViewById<Button>(R.id.navProfileButton).setOnClickListener { showScreen(R.id.profileScreen) }
        findViewById<Button>(R.id.navReportButton).setOnClickListener { showScreen(R.id.reportScreen) }
        findViewById<Button>(R.id.navHistoryButton).setOnClickListener { showScreen(R.id.historyScreen) }
        findViewById<Button>(R.id.dashboardAddRainButton).setOnClickListener {
            showScreen(R.id.rainfallScreen)
        }
        showScreen(R.id.dashboardScreen)
    }

    private fun setupSurfaceSpinner() {
        val spinner = findViewById<Spinner>(R.id.surfaceSpinner)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            SurfaceType.entries.map { "${it.label} (${it.runoffCoefficient})" }
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSurfaceType = SurfaceType.entries[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun setupHistoryList() {
        adapter = RainfallEntryAdapter(
            onEdit = ::showEditDialog,
            onDelete = { viewModel.deleteEntry(it) }
        )
        findViewById<RecyclerView>(R.id.historyList).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupActions() {
        findViewById<Button>(R.id.saveSetupButton).setOnClickListener { saveProfile() }
        findViewById<Button>(R.id.addRainfallButton).setOnClickListener { addRainfall() }
        findViewById<Button>(R.id.shareReportButton).setOnClickListener { shareReport() }
    }

    private fun showScreen(screenId: Int) {
        screens.forEach { (id, view) ->
            view.visibility = if (id == screenId) View.VISIBLE else View.GONE
        }

        val (title, subtitle) = when (screenId) {
            R.id.setupScreen -> "Setup" to "Enter roof area, tank capacity, and surface type"
            R.id.rainfallScreen -> "Rainfall Entry" to "Type rainfall and save litres collected"
            R.id.profileScreen -> "Saved Profile" to "Check the values stored on this device"
            R.id.reportScreen -> "Report and Tips" to "Review monthly impact and recommendations"
            R.id.historyScreen -> "Rainfall History" to "Edit or delete previous rainfall records"
            else -> "Dashboard" to "Your water wealth at a glance"
        }

        findViewById<TextView>(R.id.screenTitleText).text = title
        findViewById<TextView>(R.id.screenSubtitleText).text = subtitle
    }

    private fun saveProfile() {
        val roofArea = findViewById<EditText>(R.id.roofAreaInput).numberOrNull()
        val tankCapacity = findViewById<EditText>(R.id.tankCapacityInput).numberOrNull()

        when {
            roofArea == null || roofArea <= 0.0 -> toast("Enter a valid roof area.")
            tankCapacity == null || tankCapacity <= 0.0 -> toast("Enter a valid tank capacity.")
            else -> {
                viewModel.saveProfile(roofArea, tankCapacity, selectedSurfaceType)
                toast("Setup saved.")
                showScreen(R.id.profileScreen)
            }
        }
    }

    private fun addRainfall() {
        val rainfall = findViewById<EditText>(R.id.rainfallInput).numberOrNull()
        if (rainfall == null || rainfall <= 0.0) {
            toast("Enter rainfall greater than 0 mm.")
            return
        }
        if (currentState.profile == null) {
            toast("Save your setup before adding rainfall.")
            showScreen(R.id.setupScreen)
            return
        }

        viewModel.addRainfall(rainfall) {
            toast("Save your setup before adding rainfall.")
            showScreen(R.id.setupScreen)
        }
        findViewById<EditText>(R.id.rainfallInput).text.clear()
        toast("Rainfall entry saved.")
        showScreen(R.id.dashboardScreen)
    }

    private fun showEditDialog(entry: RainfallEntry) {
        val input = EditText(this).apply {
            setText(decimal.format(entry.rainfallMm))
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = "Rainfall in mm"
            setPadding(32, 16, 32, 16)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit rainfall")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val rainfall = input.numberOrNull()
                if (rainfall == null || rainfall <= 0.0) {
                    toast("Enter rainfall greater than 0 mm.")
                } else {
                    viewModel.updateRainfall(entry, rainfall)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun render(state: DashboardState) {
        findViewById<TextView>(R.id.todayLitresText).text = "Today saved\n${whole.format(state.todayLitres)} L"
        findViewById<TextView>(R.id.totalLitresText).text = "Total saved\n${whole.format(state.totalLitres)} L"
        findViewById<TextView>(R.id.waterDaysText).text = "Household water days\n${decimal.format(state.waterDays)}"
        findViewById<WaterTankView>(R.id.waterTankView).setFill(state.latestTankFill)
        findViewById<TextView>(R.id.rainfallHelpText).text = if (state.profile == null) {
            "Complete setup first, then enter rainfall."
        } else {
            "Setup is ready. Enter today's rainfall in mm."
        }

        val profile = state.profile
        if (profile != null) {
            val surface = SurfaceType.fromName(profile.surfaceType)
            val profileKey = "${profile.roofAreaSqFt}-${profile.tankCapacityLitres}-${profile.surfaceType}"
            if (profileKey != populatedProfileKey && !findViewById<EditText>(R.id.roofAreaInput).hasFocus() && !findViewById<EditText>(R.id.tankCapacityInput).hasFocus()) {
                findViewById<EditText>(R.id.roofAreaInput).setText(decimal.format(profile.roofAreaSqFt))
                findViewById<EditText>(R.id.tankCapacityInput).setText(decimal.format(profile.tankCapacityLitres))
                findViewById<Spinner>(R.id.surfaceSpinner).setSelection(SurfaceType.entries.indexOf(surface))
                populatedProfileKey = profileKey
            }
            findViewById<TextView>(R.id.setupStatusText).text = "Setup ready: ${surface.label}, runoff ${surface.runoffCoefficient}"
            findViewById<TextView>(R.id.profileSummaryText).text =
                "Roof area: ${decimal.format(profile.roofAreaSqFt)} sq.ft\n\n" +
                    "Tank capacity: ${decimal.format(profile.tankCapacityLitres)} L\n\n" +
                    "Surface type: ${surface.label}\n\n" +
                    "Runoff coefficient: ${surface.runoffCoefficient}\n\n" +
                    "Total saved so far: ${whole.format(state.totalLitres)} L"
        } else {
            findViewById<TextView>(R.id.setupStatusText).text = "Complete setup to start tracking water wealth."
            findViewById<TextView>(R.id.profileSummaryText).text = "No setup saved yet. Open the Setup screen and enter your roof and tank details."
        }

        findViewById<TextView>(R.id.latestEntryText).text = state.entries.firstOrNull()?.let {
            "Latest entry: ${decimal.format(it.rainfallMm)} mm -> ${whole.format(it.litresSaved)} L saved"
        } ?: "No rainfall entries saved yet."
        adapter.submit(state.entries)
        findViewById<TextView>(R.id.emptyHistoryText).visibility = if (state.entries.isEmpty()) View.VISIBLE else View.GONE
        findViewById<TextView>(R.id.monthlyReportText).text = buildReport(state)
        findViewById<TextView>(R.id.tipsText).text = WaterTipEngine.tipsFor(state).joinToString("\n\n")
    }

    private fun buildReport(state: DashboardState): String {
        val now = LocalDate.now()
        val monthEntries = state.entries.filter {
            val date = LocalDate.ofEpochDay(it.dateEpochDay)
            date.month == now.month && date.year == now.year
        }
        val total = monthEntries.sumOf { it.litresSaved }
        val average = if (monthEntries.isEmpty()) 0.0 else total / monthEntries.size

        return "Entries this month: ${monthEntries.size}\n" +
            "Saved this month: ${whole.format(total)} L\n" +
            "Average per entry: ${whole.format(average)} L"
    }

    private fun shareReport() {
        val text = "Jal-Sanchay monthly report\n\n${buildReport(currentState)}\n\n" +
            "Total saved: ${whole.format(currentState.totalLitres)} L\n" +
            "Household water days: ${decimal.format(currentState.waterDays)}"
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }, "Share conservation report"))
    }

    private fun EditText.numberOrNull(): Double? = text.toString().trim().replace(",", "").toDoubleOrNull()

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    }
}
