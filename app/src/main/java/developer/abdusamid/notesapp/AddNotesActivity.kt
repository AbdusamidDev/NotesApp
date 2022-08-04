package developer.abdusamid.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import developer.abdusamid.notesapp.databinding.ActivityAddNotesBinding
import developer.abdusamid.notesapp.databinding.ActivityMainBinding
import developer.abdusamid.notesapp.db.dbmanager.DBManager
import java.lang.Exception

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    var dbTable = "Notes"
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val bundle: Bundle = intent.extras!!
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                binding.etTitle.setText(bundle.getString("Title"))
                binding.etDes.setText(bundle.getString("Description"))
            }
        } catch (e: Exception) { }
    }

    fun addFun(view: View) {
        val dbManager = DBManager(this)
        val contentValues = ContentValues()
        contentValues.put("Title", binding.etTitle.text.toString())
        contentValues.put("Description", binding.etDes.text.toString())
        if (id == 0) {
            val id = dbManager.insert(contentValues)
            if (id > 0) {
                Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Couldn't Add Note", Toast.LENGTH_SHORT).show()
            }
        } else {
            val selectionArs = arrayOf(id.toString())
            val id = dbManager.update(contentValues, "ID=?", selectionArs)
            if (id > 0) {
                Toast.makeText(this, "Note Add", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Couldn't Add Note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}