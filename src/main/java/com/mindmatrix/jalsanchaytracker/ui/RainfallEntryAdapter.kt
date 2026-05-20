package com.mindmatrix.jalsanchaytracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mindmatrix.jalsanchaytracker.MainActivity
import com.mindmatrix.jalsanchaytracker.R
import com.mindmatrix.jalsanchaytracker.data.RainfallEntry
import java.text.DecimalFormat
import java.time.LocalDate

class RainfallEntryAdapter(
    private val onEdit: (RainfallEntry) -> Unit,
    private val onDelete: (RainfallEntry) -> Unit
) : RecyclerView.Adapter<RainfallEntryAdapter.EntryViewHolder>() {
    private val items = mutableListOf<RainfallEntry>()
    private val decimal = DecimalFormat("#,##0.#")
    private val whole = DecimalFormat("#,##0")

    fun submit(entries: List<RainfallEntry>) {
        items.clear()
        items.addAll(entries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        return EntryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rainfall_entry, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entry: RainfallEntry) {
            itemView.findViewById<TextView>(R.id.entryDateText).text =
                LocalDate.ofEpochDay(entry.dateEpochDay).format(MainActivity.dateFormatter)
            itemView.findViewById<TextView>(R.id.entryDetailsText).text =
                "${decimal.format(entry.rainfallMm)} mm rainfall -> ${whole.format(entry.litresSaved)} L saved"
            itemView.findViewById<Button>(R.id.editEntryButton).setOnClickListener { onEdit(entry) }
            itemView.findViewById<Button>(R.id.deleteEntryButton).setOnClickListener { onDelete(entry) }
        }
    }
}
