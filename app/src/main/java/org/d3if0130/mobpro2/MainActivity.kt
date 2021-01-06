package org.d3if0130.mobpro2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.d3if0130.mobpro2.data.Mahasiswa
import org.d3if0130.mobpro2.data.MahasiswaDb

class MainActivity : AppCompatActivity(),MainDialog.DialogListener  {

    private lateinit var adapter : MainAdapter
    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?):
                Boolean {
            if (item?.itemId == R.id.menu_delete) {
                deleteData()
                return true
            }
            return false
        }
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?):
                Boolean {
            mode?.menuInflater?.inflate(R.menu.main_mode, menu)
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?):
                Boolean {
            mode?.title = adapter.getSelection().size.toString()
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            adapter.resetSelection()
        }
    }

    private val handler = object : MainAdapter.ClickHandler {
        override fun onLongClick(position: Int): Boolean {
            if (actionMode != null) return false
            adapter.toggleSelection(position)
            actionMode = startSupportActionMode(actionModeCallback)
            return true
        }
        override fun onClick(position: Int, mahasiswa: Mahasiswa) {
            if (actionMode != null) {
                adapter.toggleSelection(position)
                if (adapter.getSelection().isEmpty())
                    actionMode?.finish()
                else
                    actionMode?.invalidate()
                return
            }
            val message = getString(R.string.mahasiswa_klik, mahasiswa.nama)
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }
    private fun deleteData() {
        val builder = AlertDialog.Builder(this)
            .setMessage(R.string.pesan_hapus)
            .setPositiveButton(R.string.hapus) { _, _ ->
                viewModel.deleteData(adapter.getSelection())
                actionMode?.finish()
            }
            .setNegativeButton(R.string.batal) { dialog, _ ->
                dialog.cancel()
                actionMode?.finish()
            }
        builder.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab.setOnClickListener {
            MainDialog().show(supportFragmentManager, "MainDialog")
        }

        adapter = MainAdapter(handler)
        val itemDecor = DividerItemDecoration(this, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)
        recyclerView.adapter = adapter
        viewModel.data.observe(this, Observer {
            adapter.submitList(it)
            emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })

    }
    override fun processDialog(mahasiswa: Mahasiswa) {
        viewModel.insertData(mahasiswa)
    }
    private val viewModel: MainViewModel by lazy {
        val dataSource = MahasiswaDb.getInstance(this).dao
        val factory = MainViewModelFactory(dataSource)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }
}
