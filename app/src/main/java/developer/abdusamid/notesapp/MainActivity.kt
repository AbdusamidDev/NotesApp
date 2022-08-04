package developer.abdusamid.notesapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import developer.abdusamid.notesapp.databinding.ActivityMainBinding
import developer.abdusamid.notesapp.databinding.ItemTiketBinding
import developer.abdusamid.notesapp.db.dbmanager.DBManager
import developer.abdusamid.notesapp.db.models.Note

class MainActivity : AppCompatActivity() {
    private var listNotes = ArrayList<Note>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadQuery("%")
    }

    @SuppressLint("Range")
    private fun loadQuery(title: String) {
        val dbManager = DBManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "Title Like?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }
        val myNotesAdapter = ListAdapter(this, listNotes)
        binding.lvNotes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        val searchView: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(sm.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNote -> {
                startActivity(Intent(this, AddNotesActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ListAdapter(context: Context, list: ArrayList<Note>) : BaseAdapter() {
        private val list = ArrayList<Note>()
        private lateinit var context: Context
        override fun getCount(): Int = list.size
        override fun getItem(position: Int): Any = list[position]
        override fun getItemId(position: Int): Long = position.toLong()

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, view: View, parent: ViewGroup): View {
            val myView =
                ItemTiketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val myNote = list[position]
            myView.tvTitle.text = myNote.noteName
            myView.tvDes.text = myNote.noteDescription
            myView.ivDelete.setOnClickListener {
                val dbManager = DBManager(this@MainActivity)
                val selectionArgs = arrayOf(myNote.noteId.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }
            myView.ivEdit.setOnClickListener {
                goUpdate(myNote)
            }
            return myView.root
        }
    }

    fun goUpdate(note: Note) {
        val intent = Intent(this, AddNotesActivity::class.java)
        intent.putExtra("ID", note.noteId)
        intent.putExtra("name", note.noteName)
        intent.putExtra("des", note.noteDescription)
        startActivity(intent)
    }
}

