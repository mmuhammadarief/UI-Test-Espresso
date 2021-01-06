package org.d3if0130.mobpro2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_main.view.*
import org.d3if0130.mobpro2.data.Mahasiswa

class MainAdapter(private val handler:ClickHandler) :
    ListAdapter<Mahasiswa, MainAdapter.ViewHolder>(DIFF_CALLBACK) {
    interface ClickHandler {
        fun onClick(position: Int, mahasiswa: Mahasiswa)
        fun onLongClick(position: Int) : Boolean

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mahasiswa>() {
            override fun areItemsTheSame(
                oldData: Mahasiswa,
                newData: Mahasiswa
            ): Boolean {
                return oldData.id == newData.id
            }

            override fun areContentsTheSame(
                oldData: Mahasiswa,
                newData: Mahasiswa
            ): Boolean {
                return oldData == newData
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswa: Mahasiswa) {
            itemView.nimTextView.text = mahasiswa.nim
            itemView.namaTextView.text = mahasiswa.nama
            itemView.setOnLongClickListener { handler.onLongClick(adapterPosition) }
            itemView.isSelected = selectionIds.contains(mahasiswa.id)
            itemView.setOnClickListener {
                handler.onClick(adapterPosition, mahasiswa)
            }
        }
    }
    private val selectionIds = ArrayList<Int>()
    fun toggleSelection(pos: Int) {
        val id = getItem(pos).id
        if (selectionIds.contains(id))
            selectionIds.remove(id)
        else
            selectionIds.add(id)
        notifyDataSetChanged()
    }
    fun getSelection(): List<Int> {
        return selectionIds
    }
    fun resetSelection() {
        selectionIds.clear()
        notifyDataSetChanged()
    }
}